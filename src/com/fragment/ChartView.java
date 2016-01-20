package com.fragment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.xmlpull.v1.XmlPullParserException;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.data.RealTimeData;
import com.example.netease.R;
import com.webservice.CreatRnederUtil;
import com.webservice.WebserviceUtil;

/** 实时曲线 */
public class ChartView extends Fragment {

	private GraphicalView chart;
	private Timer time = new Timer();
	private TimerTask task;
	private Float addPoint;
	private Float addPoint2;
	private Float addPoint3;
	private Float addPoint4;
	private String addX;
	private int TAG = 1;
	/** chartView容器 */
	LinearLayout chartContainer;
	/** 频率曲线Button */
	Button btn_chart_frequency;
	/** 功率曲线Button */
	Button btn_chart_power;
	/** 进口压力Button */
	Button btn_chart_interPress;
	/** 出口压力Button */
	Button btn_chart_outPress;
	/** 泵站名称 */
	TextView tv_chart_title;
	/** 实时曲线选项 */
	TextView tv_chart_title_option;
	/** webservice请求实时数据 */
	List<RealTimeData> listC = new ArrayList<RealTimeData>();
	List<String> xTime = new ArrayList<String>();
	List<String> fragmentS = new ArrayList<String>();
	List<String> powerS = new ArrayList<String>();
	List<String> inS = new ArrayList<String>();
	List<String> outS = new ArrayList<String>();
	/** 接受传过来的泵站ID */
	String id = null;
	int serilength = 0;
	/** 泵站名称 */
	String name = null;
	/** X轴数据缓冲区 */
	String[] xScale = new String[50];// x轴数据缓冲
	String[] xScaleF = new String[50];// x轴数据缓冲
	/** 数据点数据缓冲区 */
	Float[] pointValueS = new Float[50];
	Float[] pointValueF = new Float[50];
	Float[] pointValueP = new Float[50];
	Float[] pointValueI = new Float[50];
	Float[] pointValueO = new Float[50];
	/** 数据集，存放曲线的数据 */
	private XYSeries series;
	private XYSeries seriesP;
	private XYSeries seriesI;
	private XYSeries seriesO;

	/** 数据集容器，存放多条曲线的数据集，数据集都要添加进来 */
	private XYMultipleSeriesDataset datasetF;
	private XYMultipleSeriesDataset datasetP;
	private XYMultipleSeriesDataset datasetI;
	private XYMultipleSeriesDataset datasetO;

	/** 渲染器，存放曲线的参数 */
	private XYMultipleSeriesRenderer renderF;
	private XYMultipleSeriesRenderer renderP;
	private XYMultipleSeriesRenderer renderI;
	private XYMultipleSeriesRenderer renderO;
	/** 时间格式 */
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	LinearLayout linearLayout;
	Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 保持屏幕常亮
		getActivity().getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		View view = inflater.inflate(R.layout.chart_view, null);
		initView(view);
		// 曲线图容器

		linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
		linearLayout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		// 加判断
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (listC.size() > 0) {
					switch (TAG) {

					case 1:
						initichart();
						updatechart(listC.get(0).getFrequency(), renderF,
								datasetF);
						break;
					case 2:
						updatechart(listC.get(0).getPowerV(), renderP, datasetP);
						break;
					case 3:
						updatechart(listC.get(0).getInletPressure(), renderI,
								datasetI);
						break;
					case 4:
						updatechart(listC.get(0).getOutletPressure(), renderO,
								datasetO);
						break;
					default:
						break;
					}
				} else
					Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT)
							.show();

			}

		};

		timeRefreshData();
		return view;
	}

	/** 初始化View */
	private void initView(View view) {
		id = getArguments().getString("assembleId");

		name = getArguments().getString("assembleName");

		// 生成曲线图
		datasetF = getfrequencydataset();

		renderF = CreatRnederUtil.getdemorenderer();
		renderF.setYAxisMax(50);// 设置y轴的范围
		renderF.setYAxisMin(0);
		renderF.setYLabels(5);
		renderF.setXLabels(0);
		chart = ChartFactory.getCubeLineChartView(getActivity()
				.getApplicationContext(), datasetF, renderF, 0.33f);
		linearLayout = (LinearLayout) view.findViewById(R.id.lin_chart);
		linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
		linearLayout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		tv_chart_title = (TextView) view.findViewById(R.id.tv_chart_title);
		tv_chart_title.setText(name);

		tv_chart_title_option = (TextView) view
				.findViewById(R.id.tv_chart_title_option);
		btn_chart_frequency = (Button) view
				.findViewById(R.id.btn_chart_frequency);
		btn_chart_frequency.setOnClickListener(new MyOnclickListenrt());

		btn_chart_power = (Button) view.findViewById(R.id.btn_chart_power);
		btn_chart_power.setOnClickListener(new MyOnclickListenrt());

		btn_chart_interPress = (Button) view
				.findViewById(R.id.btn_chart_inserPress);
		btn_chart_interPress.setOnClickListener(new MyOnclickListenrt());

		btn_chart_outPress = (Button) view
				.findViewById(R.id.btn_chart_outPress);
		btn_chart_outPress.setOnClickListener(new MyOnclickListenrt());

	}

	private void initichart() {
	}

	private void updatechart(String pointValue,
			XYMultipleSeriesRenderer render, XYMultipleSeriesDataset dataset) {
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		// 判断当前点集中到底有多少点，当点超过50时，设为50
		int length = series.getItemCount();
		int a = length;
		if (length > 50) {
			length = 50;
		}

		addX = timeFormat.format(new java.util.Date());
		xScaleF[a] = addX;
		if (listC.isEmpty()) {
			if (a == 0) {
				addPoint = (float) 0;
			} else {
				addPoint = pointValueS[a - 1];
			}

		} else {
			addPoint = Float.parseFloat(pointValue);
		}
		if (a < 50) {
			pointValueF[a] = addPoint;
		} else
			pointValueF[a - 1] = addPoint;
		// 移除数据集中旧的点集
		dataset.removeSeries(series);
		if (a < 50)// 当数据集中不够50个点的时候直接添加
		{
			series.add(a + 1, Double.parseDouble(df.format(addPoint)));// 第一个参数代表第几个点，要与下面语句中的第一个参数对应
			render.addXTextLabel(a + 1, addX);
			xScale[a] = addX;
		} else {
			// 将旧的点集中x和y的数值取出来放入backup中，造成曲线向左平移的效果
			for (int i = 0; i < length - 1; i++) {
				pointValueS[i] = (float) series.getY(i + 1);
				xScale[i] = xScale[i + 1];
			}
			// 点集先清空，为了做成新的点集而准备
			series.clear();
			// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
			for (int k = 0; k < length - 1; k++) {
				series.add(k + 1, Double.parseDouble(df.format(pointValueS[k])));
				render.addXTextLabel(k + 1, xScale[k]);
			}
			xScale[49] = addX;
			series.add(50, Double.parseDouble(df.format(addPoint)));
			render.addXTextLabel(50, addX);
		}
		// 在数据集中添加新的点集
		dataset.addSeries(series);
		// 视图更新，没有这一步，曲线不会呈现动态
		chart.invalidate();
	}

	private XYMultipleSeriesRenderer getfrequencyrenderer() {

		XYMultipleSeriesRenderer render = CreatRnederUtil.getdemorenderer();
		render.setYAxisMax(50);// 设置y轴的范围
		render.setYAxisMin(0);
		render.setYLabels(5);
		render.setYTitle("Value (Hz)");
		return render;
	}

	private XYMultipleSeriesDataset getfrequencydataset() {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();// xy轴数据源
		series = new XYSeries("");
		series.setTitle("频率值");// 这个事是显示多条用的，显不显示在上面render设置
		dataset.addSeries(series);

		return dataset;
	}

	/**
	 * 通过webservice获取实时监测信息
	 * */
	private void getMonitorInfo() {
		Log.i("请求Webservice", "1");
		new Thread() {
			@Override
			public void run() {
				super.run();
				LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("ID", id);
				try {
					listC = WebserviceUtil.doSoapList(
							"AK_A_GetASDataByAssemblingSetNow", params,
							RealTimeData.class);
					Message msg = new Message();
					handler.sendMessage(msg);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
				}
			}
		}.start();
	}

	/**
	 * 定时刷新数据
	 * */
	private void timeRefreshData() {
		task = new TimerTask() {
			@Override
			public void run() {
				getMonitorInfo();
			}
		};
		Timer time = new Timer();
		long delay = 0;
		// 30s请求一次
		long interpe = 30 * 1000;
		time.schedule(task, delay, interpe);
	}

	public void onPause() {
		// 当结束程序时关掉Timer
		time.cancel();
		// 设置竖屏
		if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		getActivity().getWindow().clearFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onPause();
	}

	@Override
	public void onResume() {
		// 设置横屏
		if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

	/** 清空数组 */
	public void delectArray(String[] xScale) {
		for (int i = 0; i < xScale.length; i++) {
			xScale[i] = "";
		}
	}

	/** 清空数组 */
	public void delectArray(Float[] xScale) {
		for (int i = 0; i < xScale.length; i++) {
			xScale[i] = (float) 0;
		}
	}

	/** Button点击事件处理 */
	class MyOnclickListenrt implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_chart_frequency:
				TAG = 1;
				tv_chart_title_option.setText("实时频率曲线");
				series.clear();
				delectArray(xScale);
				delectArray(pointValueS);
				renderF = CreatRnederUtil.getdemorenderer();
				renderF.setYAxisMax(50);// 设置y轴的范围
				renderF.setYAxisMin(0);
				renderF.setYLabels(5);
				renderF.setXLabels(0);
				renderF.setYTitle("Value (Hz)");
				datasetF = new XYMultipleSeriesDataset();
				series.setTitle("频率");
				datasetF.addSeries(series);
				chart = ChartFactory.getCubeLineChartView(getActivity()
						.getApplicationContext(), datasetF, renderF, 0.33f);
				linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
				linearLayout.addView(chart, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				break;
			case R.id.btn_chart_power:
				TAG = 2;

				tv_chart_title_option.setText("实时功率曲线");
				series.clear();
				delectArray(xScale);
				delectArray(pointValueS);
				renderP = CreatRnederUtil.getdemorenderer();
				renderP.setYLabels(0);
				renderP.setYTitle("Value (kw)");
				renderP.setXLabels(0);

				datasetP = new XYMultipleSeriesDataset();
				series.setTitle("功率");
				datasetP.addSeries(series);

				chart = ChartFactory.getLineChartView(getActivity()
						.getApplicationContext(), datasetP, renderP);
				linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
				linearLayout.addView(chart, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				break;
			case R.id.btn_chart_inserPress:
				TAG = 3;

				tv_chart_title_option.setText("实时进口压力");
				serilength = series.getItemCount();
				series.clear();
				delectArray(xScale);
				delectArray(pointValueS);
				renderI = CreatRnederUtil.getdemorenderer();
				renderI.setYTitle("Value (Mpa)");
				renderI.setYAxisMax(0.6);// 设置y轴的范围
				renderI.setYAxisMin(0);
				renderI.setYLabels(6);
				renderI.setXLabels(0);
				datasetI = new XYMultipleSeriesDataset();
				series.setTitle("进口压力");
				datasetI.addSeries(series);
				chart = ChartFactory.getCubeLineChartView(getActivity()
						.getApplicationContext(), datasetI, renderI, 0.33f);
				linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
				linearLayout.addView(chart, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				break;
			case R.id.btn_chart_outPress:
				TAG = 4;

				tv_chart_title_option.setText("实时出口压力");
				series.clear();

				delectArray(xScale);
				delectArray(pointValueS);
				renderO = CreatRnederUtil.getdemorenderer();
				renderO.setYTitle("Value (Mpa)");
				renderO.setYAxisMax(2);// 设置y轴的范围
				renderO.setYAxisMin(0);
				renderO.setYLabels(4);
				renderO.setXLabels(0);
				series.setTitle("出口压力");
				datasetO = new XYMultipleSeriesDataset();
				datasetO.addSeries(series);
				chart = ChartFactory.getCubeLineChartView(getActivity()
						.getApplicationContext(), datasetO, renderO, 0.33f);
				linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
				linearLayout.addView(chart, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				break;

			default:
				break;
			}
		}

	}
}
