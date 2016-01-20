package com.fragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import pl.droidsonroids.gif.GifImageView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.data.RealTimeData;
import com.data.SetValueData;
import com.example.netease.R;
import com.webservice.WebserviceUtil;

/** 实时运行状况 */
public class RealTimeMonitorActivity extends Activity {
	List<String> pumpInfo = new ArrayList<String>();
	/** 泵站名称 */
	TextView tv_assemble_name;
	// GifImageView gif;
	Handler handler;
	String name2 = "实时参数显示";
	String nameTest = "";
	/** webservice请求实时数据 */
	List<RealTimeData> listC = new ArrayList<RealTimeData>();
	/** webservice请求设定参数 */
	List<SetValueData> listS = new ArrayList<SetValueData>();
	/** 5个泵站信息 */
	List<String> pumStatus = new ArrayList<String>();
	List<TextView> listTextView = new ArrayList<TextView>();
	/** 有无辅泵 */
	int haveAuxiliaryPump;
	/** 泵的数量 */
	int pumpNum;
	/** 接受传过来的泵站ID */
	String id;
	/** 接受传过来的泵站名称 */
	String name;
	TimerTask task;
	TextView tv_pumpstatus1;
	TextView tv_pumpstatus2;
	TextView tv_pumpstatus3;
	TextView tv_pumpstatus4;
	TextView tv_pumpstatus5;
	/** 实时数据 */
	Button btn_real_time;
	String IMAGE_URL = null;
	// Boolean isFirstRun = true;
	WebView runWebView = null;
	LinearLayout webviewContain;
	/** 获取数据的时间值 */
	String realTime = null;
	/** 时间差值 */
	long differValue = 0;
	long num = 10;
	/** 屏幕宽度 */
	int width = 0;
	/** 屏幕高度 */
	int height = 0;
	/**加载缓冲条*/
	ProgressDialog dialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.real_time_monitor);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		// 获取屏幕分辨率
		// DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		// width = mDisplayMetrics.widthPixels;
		// height = mDisplayMetrics.heightPixels;
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();

		tv_assemble_name = (TextView) findViewById(R.id.tv_assemble_name);
		tv_pumpstatus1 = (TextView) findViewById(R.id.tv_pumpstatus1);
		tv_pumpstatus2 = (TextView) findViewById(R.id.tv_pumpstatus2);
		tv_pumpstatus3 = (TextView) findViewById(R.id.tv_pumpstatus3);
		tv_pumpstatus4 = (TextView) findViewById(R.id.tv_pumpstatus4);
		tv_pumpstatus5 = (TextView) findViewById(R.id.tv_pumpstatus5);
		listTextView.add(tv_pumpstatus1);
		listTextView.add(tv_pumpstatus2);
		listTextView.add(tv_pumpstatus3);
		listTextView.add(tv_pumpstatus4);
		listTextView.add(tv_pumpstatus5);
		webviewContain = (LinearLayout) findViewById(R.id.webview_contain);
		dialog= new ProgressDialog(RealTimeMonitorActivity.this);
		dialog.setMessage("Loading....");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		btn_real_time = (Button) findViewById(R.id.btn_real_time);
		// Webview的相关设置
		runWebView = (WebView) findViewById(R.id.runWebView);
		runWebView.getSettings().setUseWideViewPort(true);
		runWebView.getSettings().setBuiltInZoomControls(false);
		runWebView.getSettings().setLoadWithOverviewMode(true);
		runWebView.getSettings().setJavaScriptEnabled(true);
		// 添加js操作接口
		// runWebView.loadUrl("file:///android_asset/initiaImage.html");
		runWebView.loadUrl("file:///android_asset/Image_SVG.html");
		runWebView.setVisibility(View.INVISIBLE);
		runWebView.addJavascriptInterface(new jsInterface(), "android");

		btn_real_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(RealTimeMonitorActivity.this,
						RealTimeMonitorAty.class);
				Bundle bd = new Bundle();
				if (listC.size() > 0 && listS.size() > 0) {
					RealTimeData rd = listC.get(0);
					SetValueData sd = listS.get(0);
					bd.putSerializable("realTimeData", rd);
					bd.putSerializable("setValueData", sd);
					bd.putString("id", id);
					i.putExtra("bd", bd);
				}
				startActivity(i);
			}
		});

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int h = (width * 4) / 5;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, h);
		webviewContain.setLayoutParams(lp);

		runWebView.getSettings().setUseWideViewPort(true);
		runWebView.getSettings().setLoadWithOverviewMode(true);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		id = bundle.getString("assembleId");
		name = bundle.getString("assembleName");
		dialog.show();
		getMonitorInfo();
		// hand();
		timeRefreshData();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (listC.size() > 0 && listS.size() > 0) {
					haveAuxiliaryPump = listS.get(0).getHaveAuxiliaryPump();
					pumpNum = listS.get(0).getPumpingNum();
					String ss = null;
					ss = getGifUrl();
					// 每次获取数据之后判断是否有变化,有变化更新数据
					if (!(ss.equals(IMAGE_URL))) {
						IMAGE_URL = ss;
						setdata();
					}
				}
				// else {
				// Toast.makeText(RealTimeMonitorActivity.this, "没有设备运行",
				// Toast.LENGTH_SHORT).show();
				// }
			}
		};
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
	 * 分割时间
	 * */
	public String timesplit(String str) {
		String[] array = str.split("T");
		String str2 = array[1];
		return str2;
	}

	/**
	 * 设置TextView数据
	 * */
	private void setdata() {

		tv_assemble_name.setText(name2);

		realTime = listC.get(0).getAcquisitionTime();
		pumStatus.add(0, listC.get(0).getPumpStatusName1());
		pumStatus.add(1, listC.get(0).getPumpStatusName2());
		pumStatus.add(2, listC.get(0).getPumpStatusName3());
		pumStatus.add(3, listC.get(0).getPumpStatusName4());
		pumStatus.add(4, listC.get(0).getPumpStatusName5());
		// String str = listC.get(0).getPumpStatusName1();
		// 判断是否隐藏显示泵站
		if (haveAuxiliaryPump == 1) {
			switch (pumpNum) {
			case 1:
				tv_pumpstatus1.setVisibility(View.INVISIBLE);
				tv_pumpstatus2.setVisibility(View.INVISIBLE);
				tv_pumpstatus3.setVisibility(View.INVISIBLE);
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				break;
			case 2:
				tv_pumpstatus2.setVisibility(View.INVISIBLE);
				tv_pumpstatus3.setVisibility(View.INVISIBLE);
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				break;

			case 3:
				tv_pumpstatus3.setVisibility(View.INVISIBLE);
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				break;

			case 4:
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				break;

			case 5:
				break;

			default:
				break;
			}
		} else {
			switch (pumpNum) {
			case 1:
				tv_pumpstatus2.setVisibility(View.INVISIBLE);
				tv_pumpstatus3.setVisibility(View.INVISIBLE);
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				tv_pumpstatus5.setVisibility(View.INVISIBLE);
				break;
			case 2:
				tv_pumpstatus3.setVisibility(View.INVISIBLE);
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				tv_pumpstatus5.setVisibility(View.INVISIBLE);
				break;

			case 3:
				tv_pumpstatus4.setVisibility(View.INVISIBLE);
				tv_pumpstatus5.setVisibility(View.INVISIBLE);
				break;

			case 4:
				tv_pumpstatus5.setVisibility(View.INVISIBLE);
				break;

			case 5:
				break;

			default:
				break;
			}

		}
		for (int i = 1; i < 5; i++) {
			switch (listC.get(0).getPumpStatusName(i)) {
			case "设备未投入运行":
				listTextView.get(i - 1).setTextColor(Color.GRAY);
				listTextView.get(i - 1).setText(i + "号水泵未投入运行");
				break;
			case "工频运行":
				listTextView.get(i - 1).setTextColor(
						Color.parseColor("#339966"));
				listTextView.get(i - 1).setText(i + "号水泵工频运行");

				break;
			case "变频运行":

				listTextView.get(i - 1).setTextColor(
						Color.parseColor("#339966"));

				listTextView.get(i - 1).setText(i + "号水泵变频运行");

				break;
			case "故障状态":
				listTextView.get(i - 1).setTextColor(Color.GRAY);

				listTextView.get(i - 1).setText(i + "号水泵故障状态");

				break;
			case "检修":
				listTextView.get(i - 1).setTextColor(
						Color.parseColor("#980000"));
				listTextView.get(i - 1).setText(i + "号水泵检修");
				break;
			case "停止状态":
				listTextView.get(i - 1).setTextColor(Color.GRAY);
				listTextView.get(i - 1).setText(i + "号水泵停止运行");
				break;
			default:
				break;
			}
		}
		switch (listC.get(0).getPumpStatusName(5)) {
		case "设备未投入运行":
			tv_pumpstatus5.setTextColor(Color.GRAY);
			tv_pumpstatus5.setText("辅泵未投入运行");
			break;
		case "工频运行":
			tv_pumpstatus5.setTextColor(Color.GREEN);

			tv_pumpstatus5.setText("辅泵工频运行");

			break;
		case "变频运行":
			tv_pumpstatus5.setTextColor(Color.GREEN);
			tv_pumpstatus5.setText("辅泵变频运行");

			break;
		case "故障状态":
			tv_pumpstatus5.setTextColor(Color.GRAY);
			tv_pumpstatus5.setText("辅泵故障状态");

			break;
		case "检修":
			tv_pumpstatus5.setTextColor(Color.parseColor("#980000"));
			tv_pumpstatus5.setText("辅泵检修");

			break;
		case "停止状态":
			tv_pumpstatus5.setTextColor(Color.GRAY);
			tv_pumpstatus5.setText("辅泵停止运行");

			break;
		default:
			break;
		}
		runWebView
				.loadUrl("javascript:FnConvertCurrentAssemblingSetParamsNow()");
	
		runWebView.setVisibility(View.VISIBLE);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		// String Data = "<!DOCTYPE html><html><body style='overflow:hidden'>"
		// + "<div style='border:0px;marign:0px;padding:0px'>"
		// +
		// "<img style='width:100%;height:100%;border:0px;marign:0px;padding:-100px;border-image:0px' src="
		// + "\'" + IMAGE_URL + "\'" + "/>" + "</div></body></html>";
		// runWebView.loadDataWithBaseURL(IMAGE_URL, Data, "text/html", "utf-8",
		// null);
	}

	/**
	 * 通过webservice获取实时监测信息
	 * */
	private void getMonitorInfo() {
		new Thread() {
			@Override
			public void run() {
				// super.run();
				LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
				Log.i("IDtest", id);
				params.put("ID", id);
				LinkedHashMap<String, Object> params1 = new LinkedHashMap<String, Object>();
				params1.put("AssemblingSetID", id);
				try {
					name2 = WebserviceUtil.doSoapAssembleName(
							"AK_A_GetAssemblingSetNamebyID", params1);
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
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
		}.start();
		/** Handler 返回处理数据 */
		// private void hand() {
		// handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// super.handleMessage(msg);
		// if (listC.size() > 0 && listS.size() > 0) {
		// haveAuxiliaryPump = listS.get(0).getHaveAuxiliaryPump();
		// pumpNum = listS.get(0).getPumpingNum();
		// String ss = null;
		// ss = getGifUrl();
		// //每次获取数据之后判断是否有变化,有变化更新数据
		// if (!(ss.equals(IMAGE_URL))) {
		// IMAGE_URL = ss;
		// setdata();
		// }
		// }
		// // else {
		// // Toast.makeText(RealTimeMonitorActivity.this, "没有设备运行",
		// // Toast.LENGTH_SHORT).show();
		// // }
		// }
		// };
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

	// 页面返回时，停止刷新书局
	@Override
	public void onPause() {
		super.onPause();
		task.cancel();
	}

	/** 通过gif名字获取ID */
	// public int getGifId(String imageName) {
	// Context context = RealTimeMonitorActivity.this.getBaseContext();
	// int resId = getResources().getIdentifier(imageName, "drawable",
	// context.getPackageName());
	// return resId;
	//
	// }

	String Url = null;

	/** 通过gif名字获取ID */
	public String getGifUrl() {

		pumpInfo.add(listC.get(0).getPumpStatusName1());
		pumpInfo.add(listC.get(0).getPumpStatusName2());
		pumpInfo.add(listC.get(0).getPumpStatusName3());
		pumpInfo.add(listC.get(0).getPumpStatusName4());
		pumpInfo.add(listC.get(0).getPumpStatusName5());

		Url = WebserviceUtil.setGifName(pumpNum, haveAuxiliaryPump, pumpInfo);
		return Url;
	}

	@Override
	public void onResume() {
		// 设置横屏
		if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	/** Android与JS交互类接口 */
	class jsInterface {
		@JavascriptInterface
		public String jstohtmltest() throws org.json.JSONException {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < pumStatus.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("pumoState", pumStatus.get(i));
				jsonArray.put(jo);
			}
			JSONObject jo = new JSONObject();
			jo.put("screenWidth", width);
			jo.put("screenHeight", height);
			jsonArray.put(jo);
			JSONObject joo = new JSONObject();
			joo.put("pumpNum", pumpNum);
			joo.put("isMinor", haveAuxiliaryPump);

			jsonArray.put(joo);
			Log.i("ss", jsonArray.toString());
			return jsonArray.toString();

		}
	}
}
