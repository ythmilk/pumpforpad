
var m_AssemblingSetID = "";
var m_Profile;     //密码
var m_UserName;  //用户名

//水泵状态对象，包含6种工作状态
var PumpingStatus=new Object();
PumpingStatus.Status1="设备未投入运行";
PumpingStatus.Status2="工频运行";
PumpingStatus.Status3="变频运行";
PumpingStatus.Status4="故障状态";
PumpingStatus.Status5="检修";
PumpingStatus.Status6="停止状态";
//当前水泵状态数组，将ajax获取的水泵状态存放到数组里面，然后与水泵状态对象相比较
var PumpingStatusNameArray=[];

//保存上一次状态的数组，如果当前状态与上一次状态一致，则不需要进行变化，如果当前状态与上一次状态不一致，则进行重绘svg
var LastPumpingStatusNameArray=[];
//当前水泵的个数，通过ajax获取 此处水泵的数量为 大泵数量+辅泵数量
var pumpingNum=2;
//动态的添加样式
var style="";
//是否有辅泵状态，有-1，无-0
var HaveAuxiliaryPump=0;
//定时器，用于定时获取数据
var m_Interval;
var isSetSize=false;
$(window).load(function (){	

	
	style=document.createElement('style');
	style.type='text/css';
	
	PumpingStatusNameArray[0]="变频运行";
	PumpingStatusNameArray[1]="工频运行";
	PumpingStatusNameArray[2]="设备未投入运行";
	PumpingStatusNameArray[3]="设备未投入运行";
	PumpingStatusNameArray[4]="变频运行";
});

$(window).resize(function (){
		
	/* 在此处获取当前存放svg的div的大小，并对svg的宽度和高度进行设置 */	
	ResizeSVGByClientArea(pumpingNum);
});

//根据屏幕宽度和高度，以及当前水泵的个数，确定当前ViewBox以及svg.width 和svg.height的大小
function ResizeSVGByClientArea(num)
{	
	var viewboxwidth;
	var viewboxheight;
	switch(num)
	{
		case 2:
			viewboxwidth=900;
			viewboxheight=800;
		break;
		case 3:
			viewboxwidth=1050;
			viewboxheight=900;
		break;
		case 4:
			viewboxwidth=1250;
			viewboxheight=1000;
		break;
		case 5:
			viewboxwidth=1450;
			viewboxheight=1000;
		break;
		case 6:
			viewboxwidth=1600;
			viewboxheight=1200;		
		break;
		default:viewboxwidth=850;
				viewboxheight=850;
	}
	
	document.getElementById('svgForStroke').setAttribute("viewBox", "0 0 "+viewboxwidth+" "+viewboxheight+" ");
	isSetSize=true;
	$("#svgForStroke g").not('#arc').remove();
	Draw(pumpingNum);
}

//开始绘制
function Draw(Num)
{

	//绘制 右下-1 水管 起点为(10,10)
	DrawWaterPipe(10,10,125,120,2);

	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		CreatLineStyleCSS('top_right','dash_top_right');
		DrawWaterPipeAnimation('top_right',11,17.5,140,17.5);
		CreatLineStyleCSS('top_down','dash_top_down');
		DrawWaterPipeAnimation('top_down',142.5,20,142.5,135);
	}
	else
	{
		$("#top_right").removeAttr('style');
		$("svg #top_right").remove();
		$("#top_down").removeAttr('style');
		$("svg #top_down").remove();
	}
	//第一层矩形
	DrawRect(55,45,175,60,1,'rect_input','0%','0%','100%','0%');
	//中间小矩形，第一层
	DrawRect(95,130,95,10,1,'rect_input','0%','0%','100%','0%');
	//中间小矩形，第二层
	DrawRect(115,140,55,20,1,'rect_input','0%','0%','100%','0%');
	//第三层
	DrawRect(105,160,75,15,1,'rect_input','0%','0%','100%','0%');	
	//绘制罐体矩形
	DrawRect(25,175,350,160,1,'rect_MainTank','0%','0%','0%','100%');
	
	//绘制罐体上方的矩形
	DrawRect(270,120,40,40,0,'rect_input','0%','0%','100%','0%');
	
	//绘制主罐体下方的出水口矩形
	DrawRect(115,335,55,35,1,'rect_input','0%','0%','100%','0%');
	
	//绘制 左下-1 水管 起点为(115,325) 水平长度为80 垂直高度为500
	DrawWaterPipe(115,345,80,450,1);
	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		//再上一层绘制动画效果 左侧横向向左的流动效果 起点为(115,352.5) 
		DrawWaterPipeAnimation('left_left_line',115,352.5,35,352.5);
		CreatLineStyleCSS('left_left_line','dash_left_left');
		//左侧向下的水管流动效果 起点为(27.5,360)
		DrawWaterPipeAnimation('left_down_line',27.5,360,27.5,780);
		CreatLineStyleCSS('left_down_line','dash_left_down');
	}
	else
	{
		$("#left_left_line").removeAttr('style');
		$("svg #left_left_line").remove();
		$("#left_down_line").removeAttr('style');
		$("svg #left_down_line").remove();
	}
	//绘制竖直-4 向下的水管 起点为(260,315)
	DrawWaterPipe(260,335,0,150,4);
	
	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		//绘制中间竖直向下的动画 起点为(267.5,335)
		DrawWaterPipeAnimation('mid_down_line',267.5,335,267.5,485);
		CreatLineStyleCSS('mid_down_line','dash_mid_down');
	}
	else
	{
		$("#mid_down_line").removeAttr('style');
		$("svg #mid_down_line").remove();	
	}
	//绘制水管中间的小矩形 起点为(230,515) 长度为75 高度为50
	DrawRect(230,465,75,50,0,'rect_input','0%','0%','100%','0%');
	//绘制右上水管 type=3 起点为(305,525) 
	DrawWaterPipe(305,500,60,100,3);
	/*绘制动画*/
	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		//绘制动画 中间横向向右的动画 mid-right 起点为(305,492.5)
		DrawWaterPipeAnimation('mid_right_line',305,492.5,365,492.5);
		CreatLineStyleCSS('mid_right_line','dash_mid_right');
		//绘制动画 中间竖直向上的动画 mid-up 起点为(365,492.5)
		DrawWaterPipeAnimation('mid_up_line',372.5,492.5,372.5,415);
		CreatLineStyleCSS('mid_up_line','dash_mid_up');
	}
	else
	{
		$("#mid_right_line").removeAttr('style');
		$("svg #mid_right_line").remove();
		$("#mid_up_line").removeAttr('style');
		$("svg #mid_up_line").remove();	
	}

	/* 根据水泵的个数，生成横向水管的长度，按照水泵台数，确定水泵的起始位置，既可以保证每次水泵在水管上面均匀分布 (n)*200*/	
	//绘制汇聚出水管 右上 type=3 起点坐标为(380,350)	
	DrawWaterPipe(380,415,Num*200,150,3);
	
	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		DrawWaterPipeAnimation('right_right_line',380,407.5,(380+Num*200),407.5);
		CreatLineStyleCSS('right_right_line','dash_right_right');
	}
	else
	{
		$("#right_right_line").removeAttr('style');
		$("svg #right_right_line").remove();
	}
	
	//画出水管上方的小圆形仪表盘 在最后出水管的200处
	var instrumentx=Num*200+300;
	var instrumenty=400;
	var g=makeSVG('g',{});
	var line=makeSVG('line',{x1:instrumentx,y1:instrumenty,x2:instrumentx,y2:instrumenty-15,'stroke-width':6,stroke:"black"});
	g.appendChild(line);
		
	var circle=makeSVG('circle',{cx:instrumentx,cy:(instrumenty-30),r:15,fill:"white",'stroke-width':2,stroke:'black'});
	g.appendChild(circle);

	var circleline=makeSVG('line',{x1:instrumentx,y1:(instrumenty-30),x2:instrumentx+10,y2:(instrumenty-20),'stroke-width':2,stroke:"black"});
	g.appendChild(circleline);
	document.getElementById('svgForStroke').appendChild(g);	

	if(HaveAuxiliaryPump>0)
	{
		//先绘制大泵，最后一个是一个小泵，仅仅缩小大小，其他的不变
		for(var i=0;i<Num-1;i++)
		{
			DrawPumping(300+i*200,795,60,60,70,80,415,i);	
		}
		DrawAuxiliaryPumping(300+(Num-1)*200,795,60,60,50,60,415);
	}
	else
	{
		//判断是否是存在辅泵，如果存在辅泵，则最后一个辅泵为一个小泵，如果不存在则全都是大泵
		for(var i=0;i<Num;i++)
		{
			DrawPumping(300+i*200,795,60,60,70,80,415,i);	
		}

	}
	/* 同理根据最后一个水泵的位置，确定进水管的长度，并进行绘制  265+(n-1)*200 */
	/* 根据水泵状态，设置相应的动画长度 */
	DrawWaterPipe(35,780,265+(Num-1)*200+1,150,5);	
	if(JudgeAllStatus(PumpingStatusNameArray,Num)==false)
	{
		var num=JudgeInputRightArrowStatus(PumpingStatusNameArray,Num);
		DrawWaterPipeAnimation('down_right_line',35,787.5,(35+265+(num-1)*200),787.5);	
		CreatLineStyleCSS('down_right_line','dash_down_right');
	}	
	else
	{
		$("#down_right_line").removeAttr('style');
		$("svg #down_right_line").remove();		
	}
}


//绘制矩形，并对该矩形进行颜色渲染(矩形的起点，矩形的终点，宽度，高度，defsID,defs_x1,defs_y1,defs_x2,defs_y2)
function DrawRect(rectx,recty,rect_width,rect_height,stroke_width,defsID,defs_x1,defs_y1,defs_x2,defs_y2)
{	
	var g=makeSVG('g',{});
	
	var circle= makeSVG('rect', {x: rectx, y: recty, width:rect_width,height:rect_height,stroke: 'black', 'stroke-width': stroke_width, style:"fill:url(#"+defsID+")"});
	g.appendChild(circle);
	
	var defs=makeSVG('defs',{});
	var linearGradient=makeSVG('linearGradient',{id:defsID,x1:defs_x1,y1:defs_y1,x2:defs_x2,y2:defs_y2});
	
	var stop1=makeSVG('stop',{offset:'0%',style:'stop-color:#505050'});
	linearGradient.appendChild(stop1);
	var stop2=makeSVG('stop',{offset:'40%',style:'stop-color:#FFFFFF'});
	linearGradient.appendChild(stop2);
	var stop3=makeSVG('stop',{offset:'60%',style:'stop-color:#FFFFFF'});
	linearGradient.appendChild(stop3);
	var stop4=makeSVG('stop',{offset:'100%',style:'stop-color:#505050'});
	linearGradient.appendChild(stop4);

	defs.appendChild(linearGradient);
	g.appendChild(defs);
	
	document.getElementById('svgForStroke').appendChild(g);	
}

//插入SVG元素，需要通过createElementNS来完成，不可以直接通过appendTo
function makeSVG(tag,attributes)
{
	var elem=document.createElementNS("http://www.w3.org/2000/svg",tag);
	for (var k in attributes)
       elem.setAttribute(k, attributes[k]);
    return elem;
}

//绘制水管
function DrawWaterPipe(pipex,pipey,pipeH_len,pipeV_len,type)
{
	var g=makeSVG('g',{});
	var pipe="";
	switch(type)
	{
		case 1: //左下
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" L"+(pipex-pipeH_len)+" "+pipey+" A15 15 0 0 0 "+(pipex-pipeH_len-15)+" "+(pipey+15)+" V"+(pipey+pipeV_len-15)+"A15 15 0 0 0 "+(pipex-pipeH_len)+" "+(pipey+pipeV_len)+" V"+(pipey+20)+" A5 5 0 0 1 "+(pipex-pipeH_len+5)+" "+(pipey+15)+"  H"+pipex+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 2:	//右下
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" L"+(pipex+pipeH_len)+" "+pipey+" A15 15 0 0 1 "+(pipex+pipeH_len+15)+" "+(pipey+15)+" V"+(pipey+pipeV_len)+" H"+(pipex+pipeH_len)+" V"+(pipey+20)+" A5 5 0 0 0 "+(pipex+pipeH_len-5)+" "+(pipey+15)+"  H"+pipex+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 3:	//右上
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" L"+(pipex+pipeH_len)+" "+pipey+" A15 15 0 0 0 "+(pipex+pipeH_len+15)+" "+(pipey-15)+" V"+(pipey-pipeV_len)+"A15 15 0 0 0 "+(pipex+pipeH_len)+" "+(pipey-pipeV_len+15)+" V"+(pipey-20)+" A5 5 0 0 1 "+(pipex+pipeH_len-5)+" "+(pipey-15)+"  H"+pipex+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 4: //竖直
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" V"+(pipey+pipeV_len)+" H"+(pipex+15)+" V"+(pipey)+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 5:	//水平
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" H"+(pipex+pipeH_len)+"  V"+(pipey+15)+" H"+(pipex)+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 6:	//上右
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" V"+(pipey-pipeV_len)+" A15 15 0 0 1 "+(pipex+15)+" "+(pipey-pipeV_len-15)+" H"+(pipex+pipeH_len)+" V"+(pipey-pipeV_len)+" H"+(pipex+20)+" A5 5 0 0 0 "+(pipex+15)+" "+(pipey-pipeV_len+5)+"  V"+pipey+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});
		break;
		case 7: //下左
			pipe=makeSVG("path",{d:"M"+pipex+" "+pipey+" V"+(pipey+pipeV_len)+" A15 15 0 0 1 "+(pipex-15)+" "+(pipey+pipeV_len+15)+" H"+(pipex-pipeH_len-15)+" V"+(pipey+pipeV_len)+" H"+(pipex-20)+" A5 5 0 0 0 "+(pipex-15)+" "+(pipey+pipeV_len-5)+"  V"+pipey+" Z",fill:'#0686C6',stroke:'#0686C6','stroke-width':'0'});	
		break;
	}
		
	g.appendChild(pipe);
	document.getElementById('svgForStroke').appendChild(g);	
/*   
    //向svg中动态的添加path
    var shape = document.createElementNS("http://www.w3.org/2000/svg", "path");
    shape.setAttributeNS(null, "d", "M500,500 C5,45 45,45 45,5");
    shape.setAttributeNS(null, "fill", "none");
    shape.setAttributeNS(null, "stroke", "black");		
	g.appendChild(shape);
*/	
	
}

//水泵和进水管出水管为一个整体
function DrawPumping(pumpingx,pumpingy,inputH_len,inputV_len,pumpingWidth,pumpingHeight,outPut_End_y,i)
{
	//绘制进水管 上右 type=6 起点为(pumpingx,pumpingy)
	DrawWaterPipe(pumpingx,pumpingy,inputH_len,inputV_len,6);
	
	/* 绘制进水管出的动画 */
	if((PumpingStatusNameArray[i]==PumpingStatus.Status2)||(PumpingStatusNameArray[i]==PumpingStatus.Status3))
	{
		CreatLineStyleCSS('pump_input_up'+i,'dash_pump_input_up'+i);
		DrawWaterPipeAnimation('pump_input_up'+i,(pumpingx+7.5),(pumpingy-15),(pumpingx+7.5),(pumpingy-inputV_len));
		
		DrawWaterPipeAnimation('pump_input_right'+i,(pumpingx+7.5),(pumpingy-inputV_len-7.5),(pumpingx+inputH_len),(pumpingy-inputV_len-7.5));
		CreatLineStyleCSS('pump_input_right'+i,'dash_pump_input_right'+i);
	}
	//绘制水泵主体
	var MainPumpingStart_x=pumpingx+inputH_len;
	var MainPumpingStart_y=pumpingy-inputV_len+15-pumpingHeight;
	
	DrawRect(MainPumpingStart_x,MainPumpingStart_y,pumpingWidth,pumpingHeight,1,"rect_input",'0%','0%','100%','0%');
	
	//画水泵中间的圆形
	var circlex=MainPumpingStart_x+pumpingWidth/2;
	var circley=MainPumpingStart_y+pumpingHeight/2;
	var circler=pumpingWidth/2-3;
	DrawCicle(circlex,circley,circler,'circle'+i);
	
	//根据当前状态，设置每个水泵的CSS
	PumpingColorCSS('circle'+i,PumpingStatusNameArray[i]);
		
	//画水泵的输水管
	var output_Start_x=MainPumpingStart_x+pumpingWidth+55;
	var output_Start_y=outPut_End_y;
	var outputV_len=MainPumpingStart_y-outPut_End_y+30;
	DrawWaterPipe(output_Start_x,outPut_End_y,40,outputV_len,7);
	
	/* 绘制出水管出的动画 */
	if((PumpingStatusNameArray[i]==PumpingStatus.Status2)||(PumpingStatusNameArray[i]==PumpingStatus.Status3))
	{
		CreatLineStyleCSS('pump_out_right'+i,'dash_pump_out_right'+i);
		DrawWaterPipeAnimation('pump_out_right'+i,(output_Start_x-55),(output_Start_y+outputV_len+7.5),(output_Start_x-15),(output_Start_y+outputV_len+7.5));
		
		DrawWaterPipeAnimation('pump_out_up'+i,(output_Start_x-7.5),(output_Start_y+outputV_len+5),(output_Start_x-7.5),(output_Start_y));
		CreatLineStyleCSS('pump_out_up'+i,'dash_pump_out_up'+i);
	}
	/* 绘制底座，此处一块 */
	//绘制底座 以黑色的直线代替
	var g1=makeSVG('g',{});
	var downlinex=MainPumpingStart_x-10;
	var downliney=MainPumpingStart_y+pumpingHeight+3;	
	var downline=makeSVG('line',{x1:downlinex,y1:downliney,x2:(downlinex+pumpingWidth+20),y2:downliney,'stroke-width':6,stroke:"black"});	
	g1.appendChild(downline);

	//绘制矩形底座
	var rect=makeSVG('rect',{x:(downlinex-5),y:(downliney+3),width:pumpingWidth+30,height:10,'stroke-width':0,stroke:"black",fill:'#A0A0A0'});	
	g1.appendChild(rect);

	//绘制上方黑色线条	
	var uplinex=MainPumpingStart_x-10;
	var upliney=MainPumpingStart_y-3;
	var upline=makeSVG('line',{x1:uplinex,y1:upliney,x2:(uplinex+pumpingWidth+20),y2:upliney,'stroke-width':6,stroke:"black"});		
	g1.appendChild(upline);
	
	//绘制上方两个黑色的短线
	var uplinex1=uplinex+15;
	var upliney1=upliney;
	var upline1=makeSVG('line',{x1:uplinex1,y1:upliney1,x2:uplinex1,y2:upliney1-10,'stroke-width':6,stroke:"black"});	
	g1.appendChild(upline1);	

	var uplinex2=uplinex+pumpingWidth+5;
	var upliney2=upliney;
	var upline2=makeSVG('line',{x1:uplinex2,y1:upliney2,x2:uplinex2,y2:upliney2-10,'stroke-width':6,stroke:"black"});	
	g1.appendChild(upline2);
	
	document.getElementById('svgForStroke').appendChild(g1);	
	
	/* 绘制水泵上方部分，此处为一块 */	
	var g2=makeSVG('g',{});
	var rectx=MainPumpingStart_x+pumpingWidth/3-5;
	var recty=MainPumpingStart_y-pumpingHeight/3-5;
	DrawRect(rectx,recty,pumpingWidth/3+10,pumpingHeight/3,1,"rect_input",'0%','0%','100%','0%');
	DrawRect(rectx-10,recty-pumpingHeight/2-30,pumpingWidth/2+20,pumpingHeight/2+30,1,"rect_input",'0%','0%','100%','0%');	
	var rect1=makeSVG('rect',{x:(rectx-10),y:(recty-pumpingHeight/2-20),width:pumpingWidth/2+20,height:pumpingHeight/2+10,'stroke-width':0,stroke:"black",fill:'black'});	
	g2.appendChild(rect1);
	
	var uplinewhitex=rectx-10;
	var uplinewhitey=recty-pumpingHeight/2-20;
	var uplinewhite=makeSVG('line',{x1:uplinewhitex,y1:uplinewhitey,x2:uplinewhitex+pumpingWidth/2+20,y2:uplinewhitey,'stroke-width':4,stroke:"white"});	
	g2.appendChild(uplinewhite);
	
	var uplinewhitex1=rectx-10;
	var uplinewhitey1=recty-10;
	var uplinewhite1=makeSVG('line',{x1:uplinewhitex1,y1:uplinewhitey1,x2:uplinewhitex1+pumpingWidth/2+20,y2:uplinewhitey1,'stroke-width':4,stroke:"white"});	
	g2.appendChild(uplinewhite1);
	document.getElementById('svgForStroke').appendChild(g2);
	
	//绘制竖直的被色直线
	
	var g3=makeSVG('g',{});
	var strightlinewhitex=rectx-10;
	var strightlinewhitey=uplinewhitey+2;
	var strightlinewhite=makeSVG('line',{x1:strightlinewhitex+1,y1:strightlinewhitey+2,x2:strightlinewhitex+1,y2:strightlinewhitey+pumpingHeight/2+5,'stroke-width':3,stroke:"white"});	
	g3.appendChild(strightlinewhite);
	
	var strightlinewhitex1=rectx-10+(pumpingWidth/2+20)/4;
	var strightlinewhitey1=uplinewhitey+2;
	var strightlinewhite1=makeSVG('line',{x1:strightlinewhitex1+1,y1:strightlinewhitey1+2,x2:strightlinewhitex1+1,y2:strightlinewhitey1+pumpingHeight/2+5,'stroke-width':5,stroke:"white"});	
	g3.appendChild(strightlinewhite1);
	
	var strightlinewhitex2=rectx-10+((pumpingWidth/2+20)/4)*2;
	var strightlinewhitey2=uplinewhitey+2;
	var strightlinewhite2=makeSVG('line',{x1:strightlinewhitex2+1,y1:strightlinewhitey2+2,x2:strightlinewhitex2+1,y2:strightlinewhitey2+pumpingHeight/2+5,'stroke-width':4,stroke:"white"});	
	g3.appendChild(strightlinewhite2);
	
	var strightlinewhitex3=rectx-10+((pumpingWidth/2+20)/4)*3;
	var strightlinewhitey3=uplinewhitey+2;
	var strightlinewhite3=makeSVG('line',{x1:strightlinewhitex3+1,y1:strightlinewhitey3+2,x2:strightlinewhitex3+1,y2:strightlinewhitey3+pumpingHeight/2+5,'stroke-width':4,stroke:"white"});	
	g3.appendChild(strightlinewhite3);
	
	var strightlinewhitex4=rectx-10+((pumpingWidth/2+20)/4)*4-2;
	var strightlinewhitey4=uplinewhitey+2;
	var strightlinewhite4=makeSVG('line',{x1:strightlinewhitex4+1,y1:strightlinewhitey4+2,x2:strightlinewhitex4+1,y2:strightlinewhitey4+pumpingHeight/2+5,'stroke-width':3,stroke:"white"});	
	g3.appendChild(strightlinewhite4);

	var rect2=makeSVG('rect',{x:(strightlinewhitex-10),y:strightlinewhitey+5,width:10,height:40,'stroke-width':0,stroke:"black",fill:'#737373'});	
	g3.appendChild(rect2);
	document.getElementById('svgForStroke').appendChild(g3);

}

//辅泵 水泵和进水管出水管为一个整体
function DrawAuxiliaryPumping(pumpingx,pumpingy,inputH_len,inputV_len,pumpingWidth,pumpingHeight,outPut_End_y)
{

	//绘制进水管 上右 type=6 起点为(pumpingx,pumpingy)
	DrawWaterPipe(pumpingx,pumpingy,inputH_len,inputV_len,6);
	
	/* 绘制进水管出的动画 */
	if((PumpingStatusNameArray[4]==PumpingStatus.Status2)||(PumpingStatusNameArray[4]==PumpingStatus.Status3))
	{
		CreatLineStyleCSS('AuxPump_input_up','dash_AuxPump_input_up');
		DrawWaterPipeAnimation('AuxPump_input_up',(pumpingx+7.5),(pumpingy-15),(pumpingx+7.5),(pumpingy-inputV_len));
		
		DrawWaterPipeAnimation('AuxPump_input_right',(pumpingx+7.5),(pumpingy-inputV_len-7.5),(pumpingx+inputH_len),(pumpingy-inputV_len-7.5));
		CreatLineStyleCSS('AuxPump_input_right','dash_AuxPump_input_right');
	}
	//绘制水泵主体
	var MainPumpingStart_x=pumpingx+inputH_len;
	var MainPumpingStart_y=pumpingy-inputV_len+15-pumpingHeight;
	
	DrawRect(MainPumpingStart_x,MainPumpingStart_y,pumpingWidth,pumpingHeight,1,"rect_input",'0%','0%','100%','0%');
	
	//画水泵中间的圆形
	var circlex=MainPumpingStart_x+pumpingWidth/2;
	var circley=MainPumpingStart_y+pumpingHeight/2;
	var circler=pumpingWidth/2-3;
	DrawCicle(circlex,circley,circler,'Auxcircle');
	

	//根据当前状态，设置每个水泵的CSS
	PumpingColorCSS('Auxcircle',PumpingStatusNameArray[4]);
	$("#Auxcircle").css('fill','Green');	
	//画水泵的输水管
	var output_Start_x=MainPumpingStart_x+pumpingWidth+55;
	var output_Start_y=outPut_End_y;
	var outputV_len=MainPumpingStart_y-outPut_End_y+30;
	DrawWaterPipe(output_Start_x,outPut_End_y,40,outputV_len,7);
	
	/* 绘制出水管出的动画 */
	if((PumpingStatusNameArray[4]==PumpingStatus.Status2)||(PumpingStatusNameArray[4]==PumpingStatus.Status3))
	{
		CreatLineStyleCSS('AuxPump_out_right','dash_AuxPump_out_right');
		DrawWaterPipeAnimation('AuxPump_out_right',(output_Start_x-55),(output_Start_y+outputV_len+7.5),(output_Start_x-15),(output_Start_y+outputV_len+7.5));
		
		DrawWaterPipeAnimation('AuxPump_out_up',(output_Start_x-7.5),(output_Start_y+outputV_len+5),(output_Start_x-7.5),(output_Start_y));
		CreatLineStyleCSS('AuxPump_out_up','dash_AuxPump_out_up');
	}
	//绘制底座 以黑色的直线代替
	var downlinex=MainPumpingStart_x-10;
	var downliney=MainPumpingStart_y+pumpingHeight+3;
	var downline=makeSVG('line',{x1:downlinex,y1:downliney,x2:(downlinex+pumpingWidth+20),y2:downliney,'stroke-width':6,stroke:"black"});	
	document.getElementById('svgForStroke').appendChild(downline);	
	
	//绘制矩形底座
	var rect=makeSVG('rect',{x:(downlinex-5),y:(downliney+3),width:pumpingWidth+30,height:10,'stroke-width':0,stroke:"black",fill:'#A0A0A0'});	
	document.getElementById('svgForStroke').appendChild(rect);
	
	//绘制上方黑色线条	
	var uplinex=MainPumpingStart_x-10;
	var upliney=MainPumpingStart_y-3;
	var upline=makeSVG('line',{x1:uplinex,y1:upliney,x2:(uplinex+pumpingWidth+20),y2:upliney,'stroke-width':6,stroke:"black"});	
	document.getElementById('svgForStroke').appendChild(upline);	
	
	//绘制上方两个黑色的短线
	var uplinex1=uplinex+15;
	var upliney1=upliney;
	var upline1=makeSVG('line',{x1:uplinex1,y1:upliney1,x2:uplinex1,y2:upliney1-10,'stroke-width':6,stroke:"black"});	
	document.getElementById('svgForStroke').appendChild(upline1);	

	var uplinex2=uplinex+pumpingWidth+5;
	var upliney2=upliney;
	var upline2=makeSVG('line',{x1:uplinex2,y1:upliney2,x2:uplinex2,y2:upliney2-10,'stroke-width':6,stroke:"black"});	
	document.getElementById('svgForStroke').appendChild(upline2);	
	
	//绘制水泵上方的部分
	var rectx=MainPumpingStart_x+pumpingWidth/3-5;
	var recty=MainPumpingStart_y-pumpingHeight/3-5;
	DrawRect(rectx,recty,pumpingWidth/3+10,pumpingHeight/3,1,"rect_input",'0%','0%','100%','0%');
	DrawRect(rectx-10,recty-pumpingHeight/2-30,pumpingWidth/2+20,pumpingHeight/2+30,1,"rect_input",'0%','0%','100%','0%');	
	var rect1=makeSVG('rect',{x:(rectx-10),y:(recty-pumpingHeight/2-20),width:pumpingWidth/2+20,height:pumpingHeight/2+10,'stroke-width':0,stroke:"black",fill:'black'});	
	document.getElementById('svgForStroke').appendChild(rect1);
	
	var uplinewhitex=rectx-10;
	var uplinewhitey=recty-pumpingHeight/2-20;
	var uplinewhite=makeSVG('line',{x1:uplinewhitex,y1:uplinewhitey,x2:uplinewhitex+pumpingWidth/2+20,y2:uplinewhitey,'stroke-width':4,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(uplinewhite);	
	
	var uplinewhitex1=rectx-10;
	var uplinewhitey1=recty-10;
	var uplinewhite1=makeSVG('line',{x1:uplinewhitex1,y1:uplinewhitey1,x2:uplinewhitex1+pumpingWidth/2+20,y2:uplinewhitey1,'stroke-width':4,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(uplinewhite1);
	
	//绘制竖直的被色直线
	var strightlinewhitex=rectx-10;
	var strightlinewhitey=uplinewhitey+2;
	var strightlinewhite=makeSVG('line',{x1:strightlinewhitex+1,y1:strightlinewhitey+2,x2:strightlinewhitex+1,y2:strightlinewhitey+pumpingHeight/2+5,'stroke-width':3,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(strightlinewhite);
	
	var strightlinewhitex1=rectx-10+(pumpingWidth/2+20)/4;
	var strightlinewhitey1=uplinewhitey+2;
	var strightlinewhite1=makeSVG('line',{x1:strightlinewhitex1+1,y1:strightlinewhitey1+2,x2:strightlinewhitex1+1,y2:strightlinewhitey1+pumpingHeight/2+5,'stroke-width':5,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(strightlinewhite1);
	
	var strightlinewhitex2=rectx-10+((pumpingWidth/2+20)/4)*2;
	var strightlinewhitey2=uplinewhitey+2;
	var strightlinewhite2=makeSVG('line',{x1:strightlinewhitex2+1,y1:strightlinewhitey2+2,x2:strightlinewhitex2+1,y2:strightlinewhitey2+pumpingHeight/2+5,'stroke-width':4,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(strightlinewhite2);
	
	var strightlinewhitex3=rectx-10+((pumpingWidth/2+20)/4)*3;
	var strightlinewhitey3=uplinewhitey+2;
	var strightlinewhite3=makeSVG('line',{x1:strightlinewhitex3+1,y1:strightlinewhitey3+2,x2:strightlinewhitex3+1,y2:strightlinewhitey3+pumpingHeight/2+5,'stroke-width':4,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(strightlinewhite3);
	
	var strightlinewhitex4=rectx-10+((pumpingWidth/2+20)/4)*4-2;
	var strightlinewhitey4=uplinewhitey+2;
	var strightlinewhite4=makeSVG('line',{x1:strightlinewhitex4+1,y1:strightlinewhitey4+2,x2:strightlinewhitex4+1,y2:strightlinewhitey4+pumpingHeight/2+5,'stroke-width':3,stroke:"white"});	
	document.getElementById('svgForStroke').appendChild(strightlinewhite4);

	var rect2=makeSVG('rect',{x:(strightlinewhitex-10),y:strightlinewhitey+5,width:10,height:40,'stroke-width':0,stroke:"black",fill:'#737373'});	
	document.getElementById('svgForStroke').appendChild(rect2);					
	
}

//绘制圆以及叶片
function DrawCicle(circlex,circley,circler,circleid)
{
	var g=makeSVG('g',{});
	var circle=makeSVG('circle',{id:circleid,cx:circlex,cy:circley,r:circler,'stroke-width':2,stroke:'white',fill:'#0686C6'});
	g.appendChild(circle);
	/* 中间圆点 */
	var midCirclex=circlex;
	var midCircley=circley;
	var midCircler=3;
	var midCircle=makeSVG('circle',{cx:midCirclex,cy:midCircley,r:midCircler,'stroke-width':1,stroke:'white',fill:'white'});
	g.appendChild(midCircle);
	
	/* 叶片构成一个整体，用于进行旋转 */
	var up=makeSVG('path',{id:circleid+"_0",d:"M "+midCirclex+" "+(midCircley-3)+" V"+(midCircley-circler)+" A"+(circler/2)+" "+(circler/2)+" 0 0 1 "+midCirclex+" "+(midCircley-3)
	+"M "+(midCirclex+3)+" "+(midCircley)+" H"+(midCirclex+circler)+" A"+(circler/2)+" "+(circler/2)+" 0 0 1 "+(midCirclex+3)+" "+(midCircley)
	+"M "+midCirclex+" "+(midCircley+3)+" V"+(midCircley+circler)+" A"+(circler/2)+" "+(circler/2)+" 0 0 1 "+midCirclex+" "+(midCircley+3)
	+"M "+(midCirclex-3)+" "+(midCircley)+" H"+(midCirclex-circler)+" A"+(circler/2)+" "+(circler/2)+" 0 0 1 "+(midCirclex-3)+" "+(midCircley)+" Z",'stroke-width':1,fill:'white'});
	g.appendChild(up);
	document.getElementById('svgForStroke').appendChild(g);
	document.getElementById(circleid+"_0").style.transformOrigin =circlex+"px "+circley+"px";
	document.getElementById(circleid+"_0").style.WebkitTransformOrigin =circlex+"px "+circley+"px";
	//设置叶片的动画属性
	CreateCircleStyleCSS(circleid+"_0","move_"+circleid);

}
//绘制水管流动动画
function DrawWaterPipeAnimation(ID,x1,y1,x2,y2)
{
	var g=makeSVG('g',{});
	var line_animation=makeSVG('line',{id:ID,fill:'none','stroke-width':4,stroke:'white','stroke-dasharray':"null",'stroke-linejoin':"null",'stroke-linecap':"round",x1:x1,y1:y1,x2:x2,y2:y2});
	g.appendChild(line_animation);
	document.getElementById('svgForStroke').appendChild(g);
}

//根据参数生成箭头的位置(所属动画名称,选择器)
function CreatLineStyleCSS(selector,movename)
{
	var rule="#"+selector+"{stroke-dasharray: 20; stroke-dashoffset: 80;"
	+" animation:"+movename+" 1s linear  infinite ;"
	+" -webkit-animation:"+movename+" 1s linear  infinite ; /*Safari and Chrome*/}"
	+" @keyframes "+movename
	+"{to{ stroke-dashoffset: 0;}}"
	+" @-webkit-keyframes "+movename
	+" {to{ stroke-dashoffset: 0;}}"
	
	loadStyleString(rule);
}
//水泵的动画
function CreateCircleStyleCSS(selector,movename){
	var rule="#"+selector+"{animation:"+movename+" 2s  infinite linear;-webkit-animation:"+movename+" 2s  infinite linear;}"
		+"@keyframes "+movename +
		"{"+
		"0%{  transform:rotate(0deg); }"+
		"2.8%{ transform:rotate(10deg);  }"+
		"5.6%{  transform:rotate(20deg);  }"+
		"8.4%{ transform:rotate(30deg); }"+
		"11.2%{ transform:rotate(40deg); }"+
		"14%{  transform:rotate(50deg);  }"+
		"16.8%{ transform:rotate(60deg); }"+
		"19.6%{  transform:rotate(70deg); }"+
		"22.4%{ transform:rotate(80deg); }"+
		"25.2%{ transform:rotate(90deg);  }"+
		"28%{  transform:rotate(100deg); }"+
		"30.8%{  transform:rotate(110deg);  }"+
		"33.6%{  transform:rotate(120deg); }"+
		"36.4%{ transform:rotate(130deg);  }"+
		"39.2%{  transform:rotate(140deg);  }"+
		"42%{  transform:rotate(150deg);  }"+
		"44.8%{  transform:rotate(160deg);  }"+
		"47.6%{  transform:rotate(170deg); }"+
		"50.4%{  transform:rotate(180deg);  }"+
		"53.2%{ transform:rotate(190deg); }"+
		"56%{transform:rotate(200deg);}"+
		"58.8%{  transform:rotate(210deg); }"+
		"61.6%{  transform:rotate(220deg); }"+
		"64.4%{  transform:rotate(230deg);  }"+
		"67.2%{  transform:rotate(240deg);  }"+
		"70%{ transform:rotate(250deg); }"+
		"72.8%{  transform:rotate(260deg); }"+
		"75.6%{ transform:rotate(270deg); }"+
		"81.2%{ transform:rotate(290deg);  }"+
		"84%{  transform:rotate(300deg); }"+
		"86.8%{  transform:rotate(310deg);  }"+
		"89.6%{  transform:rotate(320deg); }"+
		"92.4%{ transform:rotate(330deg);  }"+
		"95.2%{ transform:rotate(340deg); }"+
		"98%{ transform:rotate(350deg); }"+
		"100%{ transform:rotate(360deg); }"+
		"}"+
		"@-webkit-keyframes "+ movename+
		"{"+
		"0%{  -webkit-transform:rotate(0deg); }"+
		"2.8%{ -webkit-transform:rotate(10deg);  }"+
		"5.6%{  -webkit-transform:rotate(20deg);  }"+
		"8.4%{ -webkit-transform:rotate(30deg); }"+
		"11.2%{ -webkit-transform:rotate(40deg); }"+
		"14%{  -webkit-transform:rotate(50deg);  }"+
		"16.8%{ -webkit-transform:rotate(60deg); }"+
		"19.6%{ -webkit-transform:rotate(70deg); }"+
		"22.4%{ -webkit-transform:rotate(80deg); }"+
		"25.2%{ -webkit-transform:rotate(90deg);  }"+
		"28%{  -webkit-transform:rotate(100deg); }"+
		"30.8%{  -webkit-transform:rotate(110deg);  }"+
		"36.4%{ -webkit-transform:rotate(130deg);  }"+
		"39.2%{  -webkit-transform:rotate(140deg);  }"+
		"42%{  -webkit-transform:rotate(150deg);  }"+
		"44.8%{  -webkit-transform:rotate(160deg);  }"+
		"47.6%{  -webkit-transform:rotate(170deg); }"+
		"50.4%{  -webkit-transform:rotate(180deg);  }"+
		"53.2%{ -webkit-transform:rotate(190deg); }"+
		"56%{-webkit-transform:rotate(200deg);}"+
		"58.8%{  -webkit-transform:rotate(210deg); }"+
		"61.6%{  -webkit-transform:rotate(220deg); }"+
		"64.4%{  -webkit-transform:rotate(230deg);  }"+
		"67.2%{ -webkit-transform:rotate(240deg);  }"+
		"70%{ -webkit-transform:rotate(250deg); }"+
		"72.8%{ -webkit-transform:rotate(260deg); }"+
		"75.6%{ -webkit-transform:rotate(270deg); }"+
		"78.4%{ -webkit-transform:rotate(280deg);  }"+
		"81.2%{ -webkit-transform:rotate(290deg);  }"+
		"84%{  -webkit-transform:rotate(300deg); }"+
		"86.8%{  -webkit-transform:rotate(310deg);  }"+
		"89.6%{  -webkit-transform:rotate(320deg); }"+
		"92.4%{ -webkit-transform:rotate(330deg);  }"+
		"95.2%{ -webkit-transform:rotate(340deg); }"+
		"98%{ -webkit-transform:rotate(350deg); }"+
		"100%{ -webkit-transform:rotate(360deg); }"+
		"}";
	loadStyleString(rule);
}
//动态的插入新的样式
function loadStyleString(css)
{
	//创建给新的style来存放这些动态规则
	try
	{
		style.appendChild(document.createTextNode(css));
	}catch(ex)
	{
		style.styleSheet.cssText = css;
	}   
	var head = document.getElementsByTagName("head")[0];
	head.appendChild(style);
}

//获取水泵状态，根据当前水泵状态
//function GetPumpinStatus()
//{
//	$.ajax({
//		type:'GET',
//		url:'../../AKGSWebService.asmx/AK_A_GetASData_1_ByAssemblingID',
//		data:{ID:m_AssemblingSetID},
//		success:function (data)
//		{
//			FnConvertCurrentAssemblingSetParamsNow(data);
//		},
//		error:function(){}
//	});
//	FnGetAssemblingInfo(m_AssemblingSetID);
//	FnGetAssemblingSetParams(m_AssemblingSetID);
//	FnGetFeedBackAssemblingSetParams(m_AssemblingSetID);
//	FnGetAlarm(m_AssemblingSetID);
//	Fnrefresh();
//}
	
	
//function Fnrefresh()
//{
//	clearInterval(m_Interval);
//	m_Interval=setInterval(function (){GetPumpinStatus();},5000);
//}
function setSize(width,height){
	document.getElementById('svgForStroke').style.width=document.body.clientWidth ;
	document.getElementById('svgForStroke').style.height=document.body.clientWidth*3/4;
	ResizeSVGByClientArea(pumpingNum);
}
//根据当前水泵的状态，改变叶片的颜色以及水流动的状态
function FnConvertCurrentAssemblingSetParamsNow()
{
	isSetSize=false;
	//获取Android传递过来的参数
	var request=window.android.jstohtmltest();
	var obj=eval('('+request+')');
	var length1=obj.length;
	//var pumpInfo=new Array();
	for(var i=0;i<=obj.length-2;i++){
		//pumpInfo[i]=obj[i].pumoState;
		//设置泵站的运行情况
		PumpingStatusNameArray[i]=obj[i].pumoState;
	}
	document.getElementById("p1").innerHTML="";
	var width=obj[length1-2].screenWidth;
	var height=obj[length1-2].screenHeight;
	HaveAuxiliaryPump=obj[length1-1].isMinor;
	pumpingNum=obj[length1-1].pumpNum+HaveAuxiliaryPump;
	//d得到数据后先设置svg的大小
	var w=width;
	setSize(w,w);
	//while (document.getElementById('svgForStroke').style.width!=w) {
//	setSize(w,w);
//}
while(!isSetSize){setSize(w,w);}
	
	var i=0;
	while(i<5)
	{
		//判断当前获取状态与上一次状态是否相同，如果相同，则不进行重绘，否则将本次状态保存下来，并对当前状态进行重绘
		if(PumpingStatusNameArray[i]!=LastPumpingStatusNameArray[i])
		{
			//重绘状态
			for(var j=0;j<5;j++)
			{
				LastPumpingStatusNameArray[j]=PumpingStatusNameArray[j];
			}
			
			$("#svgForStroke g").not('#arc').remove();
			Draw(pumpingNum);	         
			break;			
		}
		else
		{
			i++;
		}		
	}	
}
//根据水泵状态，设置CSS属性
function PumpingColorCSS(id,status)
{

	switch(status)
	{
		case "设备未投入运行":
			$("#"+id).css('fill','yellow');
			$("#"+id+"_0").css('animation-duration','0s');
		break;
		case "工频运行":
			$("#"+id).css('fill','#0686C6');
			$("#"+id+"_0").css('animation-duration','2s');
		break;
		case "变频运行":
			$("#"+id).css('fill','#0686C6');
			$("#"+id+"_0").css('animation-duration','2s');
		break;
		case "故障状态":
			$("#"+id).css('fill','red');
			$("#"+id+"_0").css('animation-duration','0s');
		break;
		case "检修":
			$("#"+id).css('fill','yellow');
			$("#"+id+"_0").css('animation-duration','0s');
		break;
		case "停止状态":
			$("#"+id).css('fill','#0686C6');
			$("#"+id+"_0").css('animation-duration','0s');
		break;	
	}
}
//绘制最下方的出水管的动画时，需要判断当前水泵停止的位置，是否需要将动画距离缩短
function JudgeInputRightArrowStatus(PumpingStatusNameArray,PumpingNum)
{
	var Num=PumpingNum;
	if(HaveAuxiliaryPump==0)
	{
		if((PumpingStatusNameArray[PumpingNum-1]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-1]!=PumpingStatus.Status3))
		{
			Num=Num-1;
			if((PumpingNum-2>0)&&((PumpingStatusNameArray[PumpingNum-2]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-2]!=PumpingStatus.Status3)))
			{
				Num=Num-1;
				if((PumpingNum-3>0)&&((PumpingStatusNameArray[PumpingNum-3]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-3]!=PumpingStatus.Status3)))
				{
					Num=Num-1;
				  if((PumpingNum-4>0)&&((PumpingStatusNameArray[PumpingNum-4]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-4]!=PumpingStatus.Status3)))
				  {
					Num=Num-1;
				  }
				}
			}
		}	
		return Num;	
	}
	else
	{
		if((PumpingStatusNameArray[4]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[4]!=PumpingStatus.Status3))
		{
			Num=Num-1;
			if((PumpingStatusNameArray[PumpingNum-2]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-1]!=PumpingStatus.Status3))
			{
				Num=Num-1;
				if((PumpingNum-2>0)&&((PumpingStatusNameArray[PumpingNum-3]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-2]!=PumpingStatus.Status3)))
				{
					Num=Num-1;
					if((PumpingNum-3>0)&&((PumpingStatusNameArray[PumpingNum-4]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-3]!=PumpingStatus.Status3)))
					{
						Num=Num-1;
					  if((PumpingNum-4>0)&&((PumpingStatusNameArray[PumpingNum-5]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[PumpingNum-4]!=PumpingStatus.Status3)))
					  {
						Num=Num-1;
					  }
					}
				}
			}	
		}	
		return Num;	
	}
}

//判断所有水泵状态，如果所有水泵状态都为停止状态，则将进水管处的水流动画停止
function JudgeAllStatus(PumpingStatusNameArray,PumpingNum)
{
	//当前停止状态的水泵个数
	var currentStopNum=0;
	for(var i=0;i<PumpingNum;i++)
	{
		if((PumpingStatusNameArray[i]!=PumpingStatus.Status2)&&(PumpingStatusNameArray[i]!=PumpingStatus.Status3))
		{
			currentStopNum++;
		}	
	}
	//如果停止的个数为总数，那么返回真，否则返回假
	if(currentStopNum==PumpingNum)
	{
		return true;	
	}
	else
	{
		return false;
	}
}






