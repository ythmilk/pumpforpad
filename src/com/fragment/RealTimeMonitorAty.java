package com.fragment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSONException;
import com.data.RealTimeData;
import com.data.SetValueData;
import com.example.netease.R;
import com.webservice.WebserviceUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**实时运行参数显示显示*/
public class RealTimeMonitorAty extends Activity {
	TimerTask task;
	/** 监测时间 */
	TextView tv_settime;
	/** 进水压力 */
	TextView tv_inlet_press;
	/** 出水压力 */
	TextView tv_outlet_press;
	/** 变频器频率 */
	TextView tv_frequency;
	/** 变频器功率 */
	TextView tv_power_v;
	/** 流量 */
	TextView tv_flow_v;
	/** 液位 */
	TextView tv_liquid_level;
	/** 恒压设定 */
	TextView tv_settingPressure;
	/** 进水压力低限 */
	TextView tv_inletPressureLowest;
	/** 出水压力高限 */
	TextView tv_outletPressureMaximum;
	/** 变频器频率高限 */
	TextView tv_inverterFrequencyMaximum;
	/** 变频器功率低限 */
	TextView tv_inverterPowerLowest;
	/** 定期轮换时间 */
	TextView tv_timingRotationTime;
	/** 出水压力量程 */
	TextView tv_pressureFullscale;
	/** webservice请求实时数据 */
	List<RealTimeData> listC = new ArrayList<RealTimeData>();
	/** webservice请求设定参数 */
	List<SetValueData> listS = new ArrayList<SetValueData>();
	RealTimeData realData = new RealTimeData();
	SetValueData setData = new SetValueData();
	/** 5个泵站信息 */
	List<String> pumStatus = new ArrayList<String>();
	ImageView img_back;
	String id = null;
	Handler handler;

	
	/** 获取数据的时间值 */
	String realTime = null;
	/** 时间差值 */
	long differValue = 0;
	long num = 120;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.real_time_moniror_aty);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		initiview();
		try {
			try {
				setdata();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NullPointerException e) {
		}
		timeRefreshData();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (listC.size() > 0 && listS.size() > 0) {
					realData = listC.get(0);
					setData = listS.get(0);
					try {
						setdata();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(RealTimeMonitorAty.this, "没有设备运行",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	private void initiview() {
		try {
			Bundle bd = getIntent().getBundleExtra("bd");

			realData = (RealTimeData) bd.get("realTimeData");
			setData = (SetValueData) bd.get("setValueData");
			id = bd.getString("id");
		} catch (NullPointerException e) {
			Toast.makeText(this, "泵站没有运行", Toast.LENGTH_SHORT).show();
		}

		tv_settime = (TextView) findViewById(R.id.tv_settime);
		tv_inlet_press = (TextView) findViewById(R.id.tv_inlet_press);
		tv_outlet_press = (TextView) findViewById(R.id.tv_outlet_press);
		tv_frequency = (TextView) findViewById(R.id.tv_frequency);
		tv_power_v = (TextView) findViewById(R.id.tv_power_v);
		tv_flow_v = (TextView) findViewById(R.id.tv_flow_v);
		tv_liquid_level = (TextView) findViewById(R.id.tv_liquid_level);

		tv_settingPressure = (TextView) findViewById(R.id.tv_settingPressure);
		tv_inletPressureLowest = (TextView) findViewById(R.id.tv_inletPressureLowest);
		tv_outletPressureMaximum = (TextView) findViewById(R.id.tv_outletPressureMaximum);
		tv_inverterFrequencyMaximum = (TextView) findViewById(R.id.tv_inverterFrequencyMaximum);
		tv_inverterPowerLowest = (TextView) findViewById(R.id.tv_InverterPowerLowest);
		tv_timingRotationTime = (TextView) findViewById(R.id.tv_timingRotationTime);
		tv_pressureFullscale = (TextView) findViewById(R.id.tv_pressureFullscale);
	}

	/**
	 * 设置TextView数据
	 * @throws ParseException 
	 * */
	private void setdata() throws ParseException {
		DecimalFormat df = new DecimalFormat("####0.00");
		
		
		String time1=realData.getAcquisitionTime().split("T")[0] + " " + realData.getAcquisitionTime().split("T")[1];
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = new Date();
		try {
			date1 = format.parse(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 当前系统时间
		Date date2 = calendar.getTime();
		// 时间差值
		long mi = (date2.getTime() - date1.getTime()) / 1000;
		differValue = Math.abs(mi);
		if (differValue>num) {
			tv_settime.setTextColor(Color.RED);
			tv_inlet_press.setTextColor(Color.RED);
			tv_outlet_press.setTextColor(Color.RED);
			tv_frequency.setTextColor(Color.RED);
			tv_power_v.setTextColor(Color.RED);
			tv_flow_v.setTextColor(Color.RED);
			tv_liquid_level.setTextColor(Color.RED);
		}else {
			tv_settime.setTextColor(Color.parseColor("#339966"));
			tv_inlet_press.setTextColor(Color.parseColor("#339966"));
			tv_outlet_press.setTextColor(Color.parseColor("#339966"));
			tv_frequency.setTextColor(Color.parseColor("#339966"));
			tv_power_v.setTextColor(Color.parseColor("#339966"));
			tv_flow_v.setTextColor(Color.parseColor("#339966"));
			tv_liquid_level.setTextColor(Color.parseColor("#339966"));
		}
		tv_settime.setText(timesplit(realData.getAcquisitionTime()));
		tv_inlet_press.setText(df.format(Double.valueOf(realData
				.getInletPressure())) + "MPa");
		tv_outlet_press.setText(df.format(Double.valueOf(realData
				.getOutletPressure())) + "MPa");
		tv_frequency.setText(df.format(Double.valueOf(realData.getFrequency()))
				+ "Hz");
		tv_power_v.setText(df.format(Double.valueOf(realData.getPowerV())*10)
				+ "(Kw)");
		tv_flow_v.setText(df.format(Double.valueOf(realData.getFlowV()))+"L/S");
		tv_liquid_level.setText(df.format(Double.valueOf(realData
				.getLiquidlevel()))+"m");

		/**中间参数*/
		Double dSettingPressure=Double.parseDouble(setData.getSettingPressure())*Double.parseDouble(setData.getPressureFullscale());
		tv_settingPressure.setText(df.format(dSettingPressure)+ "Mpa");
		tv_inletPressureLowest
				.setText(df.format(Double.valueOf(setData.getInletPressureLowest())) + "MPa");
		tv_outletPressureMaximum.setText(df.format(Double.valueOf(setData.getOutletPressureMaximum()))
				+ "MPa");
		tv_inverterFrequencyMaximum.setText(df.format(Double.valueOf(setData
				.getInverterFrequencyMaximum())) + "Hz");
		/**功率底限转换，取到的值*10*/
		Double d01=((Double.parseDouble(setData.getInverterPowerLowest()))*10);
		tv_inverterPowerLowest.setText(df.format(d01) + "(Kw)");
		/**定期轮换时间/4*/
		Double d02=Double.parseDouble(setData.getTimingRotationTime())/4;
		tv_timingRotationTime.setText(df.format(d02) + "Hour");
		tv_pressureFullscale.setText(df.format(Double.valueOf(setData.getPressureFullscale())) + "MPa");
	}
	/**
	 * 分割时间
	 * */
	public String timesplit(String str) {
		String[] array = str.split("T");
		String str2 = array[1];
		return str2;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 通过webservice获取实时监测信息
	 * */
	private void getMonitorInfo() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("ID", id);
				LinkedHashMap<String, Object> params1 = new LinkedHashMap<String, Object>();
				params1.put("AssemblingSetID", id);
				try {
					// name2 = WebserviceUtil.doSoapAssembleName(
					// "AK_A_GetAssemblingSetNamebyID", params1);
					listC = WebserviceUtil.doSoapList(
							"AK_A_GetASData_1_ByAssemblingID", params,
							RealTimeData.class);
					listS = WebserviceUtil.doSoapList(
							"AK_A_GetCurrentAssemblingSetPSByAssemblingSet",
							params, SetValueData.class);

					Message msg = new Message();
					handler.sendMessage(msg);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
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
				System.gc();
				getMonitorInfo();
			}
		};
		Timer time = new Timer();
		long delay = 0;
		// 30s请求一次
		long interpe = 30 * 1000;
		time.schedule(task, delay, interpe);
	}
	@Override
	public void onPause() {
		super.onPause();
		task.cancel();
	}
	@Override
	public void onResume() {
		// 设置横屏
		if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}
}
