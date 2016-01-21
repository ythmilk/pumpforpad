package com.webservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.example.netease.R;

/**
 * Webservice封装类
 * */
public class WebserviceUtil {

	public static final HttpTransportSE ht = new HttpTransportSE(Info.wsdlUrl);
/**调用webervice*/
	public static <T> List<T> doSoapList(String methodName,
			LinkedHashMap<String, Object> params, Class<T> clazz)
			throws XmlPullParserException, IOException {
		
		SoapObject request = new SoapObject(Info.wsdlNs, methodName);
		// 设置Webservice的请求参数
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			request.addProperty(entry.getKey(), entry.getValue());
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		
		envelope.dotNet = true;
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		ht.call(Info.wsdlNs + methodName, envelope);
		// 得到返回数据
		SoapObject result = (SoapObject) envelope.bodyIn;
		// 得到json类型的字符串
		String str = (result).getProperty(0).toString();
		if (!str.equals("")) {
			return JSON.parseArray(str, clazz);
		}else return null;
		
	}
	/**解析一个XML内容，泵站名称的解析*/
	public static String doSoapAssembleName(String methodName,
			LinkedHashMap<String, Object> params)
			throws XmlPullParserException, IOException {
		SoapObject request = new SoapObject(Info.wsdlNs, methodName);
		// 设置Webservice的请求参数
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			request.addProperty(entry.getKey(), entry.getValue());
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		ht.call(Info.wsdlNs + methodName, envelope);
		// 得到返回数据
		SoapObject result = (SoapObject) envelope.bodyIn;
		// 得到json类型的字符串
		String str = result.getProperty(0).toString();	
		return str;
	}
	/**设置Spinner的内容
	*/
	public static <T> void setItemContent(Spinner sp,List<T>list,Context context){
		ArrayAdapter<T> adapter;
		if (list.size()==0) {
			adapter = new ArrayAdapter<T>(context,
					R.layout.my_spinner_style);
			sp.setAdapter(adapter);
		} else {
			ArrayList<T> listProvince = new ArrayList<T>();
			for (int i = 0; i < list.size(); i++) {
				listProvince.add(list.get(i));
			}
			adapter = new ArrayAdapter<T>(context,
					R.layout.my_spinner_style, listProvince);
			sp.setAdapter(adapter);
		}
	}
	/**根据信息产生gif图的请求链接（废弃，现在用来判断两次取过来的状态是否改变）
	 */
	public static String setGifName(int pumpNum,int haveAuxiliaryPump,List<String>list) {
		String mGifname=new String("http://221.131.89.111:8090/AKGSImage/");
		String state="";
		if(haveAuxiliaryPump>0)
		{
			mGifname+=(pumpNum-1)+"_/";
		    for (int i = 0; i < pumpNum - 1; i++) {
		    	
		    	String str=list.get(i);
		    	if (str.equals("设备未投入运行")) {state += "_检修";
				}else if (str.equals("工频运行")) {state += "_运行";
				}else if (str.equals("变频运行")) {state += "_运行";
				}else if (str.equals("故障状态")) {state += "_故障";
				}else if (str.equals("检修")) {state += "_检修";
				}else if (str.equals("停止状态")) {state += "_停止";
				}
		    }
		    String str2=list.get(list.size() - 1);
		    if (str2.equals("设备未投入运行")) {state += "_检修";
		    }else if (str2.equals("工频运行")) {state += "_运行";
			}else if (str2.equals("变频运行")) {state += "_运行";
			}else if (str2.equals("故障状态")) {state += "_故障";
			}else if (str2.equals("检修")) {state += "_检修";
			}else if (str2.equals("停止状态")) {state += "_停止";
			}
		}else{
			mGifname+=pumpNum+"/";
			for (int i = 0; i < pumpNum; i++) {
				String str=list.get(i);
				if (str.equals("设备未投入运行")) {state += "_检修";
				 }else if (str.equals("工频运行")) {state += "_运行";
					}else if (str.equals("变频运行")) {state += "_运行";
					}else if (str.equals("故障状态")) {state += "_故障";
					}else if (str.equals("检修")) {state += "_检修";
					}else if (str.equals("停止状态")) {state += "_停止";
					}
			}
		}
		try {
			mGifname+=URLEncoder.encode(state, "UTF-8");
			mGifname+=".gif";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	return mGifname;
}
	/**获取一段时间的泵站信息*/
	public static  <T> List<T> getAsData(String id,String startTime,String endTime,Class<T> clazz) throws ClientProtocolException, IOException{
		 final String SERVER_URL = "http://221.131.89.111:8090/akgswebservice.asmx/AK_A_GetASDataByAssemblingSet?ID="+id+"&StartTime="+startTime+"&EndTime="+endTime; 
		 String test=null;
         HttpGet httpget=new HttpGet(SERVER_URL);
         HttpResponse ht=new DefaultHttpClient().execute(httpget);
         if (ht.getStatusLine().getStatusCode()==200) {
			String result=EntityUtils.toString(ht.getEntity());
			String[] strArr = result.split("\\>");
        	String ee=strArr[2];
        	Log.i("ee", ee);
        	String[] strArr2 = ee.split("\\<");
        	 test=strArr2[0];
         }
         Log.i("historyData", test);
		return JSON.parseArray(test, clazz);
		
	}
	/**根据用户ID获取泵站信息*/
	public static  <T> List<T> getAsDataTwo(String id,Class<T> clazz) throws ClientProtocolException, IOException{
		 final String SERVER_URL = "http://221.131.89.111:8090/akgswebservice.asmx/AK_G_GetPunmingStationByUser?Id="+id; 
		 String test=null;
        HttpGet httpget=new HttpGet(SERVER_URL);
        HttpResponse ht=new DefaultHttpClient().execute(httpget);
        if (ht.getStatusLine().getStatusCode()==200) {
			String result=EntityUtils.toString(ht.getEntity());
			String[] strArr = result.split("\\>");
       	String ee=strArr[2];
       	String[] strArr2 = ee.split("\\<");
       	 test=strArr2[0];
        }
        Log.i("historyData", test);
		return JSON.parseArray(test, clazz);
		
	}
}
