package com.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYSeries;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.data.SelectionS;
import com.data.StatisticValueS;
import com.data.TestS;
import com.example.mymap.SelectTimeDialog;
import com.example.mymap.SelectTimeDialog.onTimeDialogListener;
import com.example.mymap.StatisticDialog;
import com.example.netease.R;
import com.webservice.TestUtil;
import com.webservice.WebserviceUtil;

/** 统计信息查询 */
public class StatusActivity extends FragmentActivity implements
		onTimeDialogListener {
	private static final OnItemSelectedListener OnItemSelectedListener = null;
	WebView myWebview;
	Button btnx;
	/** 开始时间Edittext */
	EditText et_start;
	/** 结束时间 */
	EditText et_stop;
	/** 参考值 */
	EditText et_refvalue;
	/** 选择统计项 */
	Spinner sp_status_select;
	/** 选择项ID */
	int selection = 1;

	/** 小于参考值次数Textview */
	EditText tv_lessthan_refvalue;
	/** 大于参考值Textview */
	EditText tv_morethan_refvalue;
	/** 完整统计值Textview */
	TextView tv_more_info;

	String mytime = null;
	/** 统计列表方法名 */
	String methodName = null;
	String CollectorNoId = null;
	/** 参考值 */
	String RefValue = null;
	private XYSeries series;
	LinearLayout chartContain;
	private GraphicalView chart;
	private onTimeDialogListener mOnListener;
	/** 获取时间段内的数据 */
	List<TestS> listC = new ArrayList<TestS>();
	/** 选择统计选项 */
	List<SelectionS> listSelect = new ArrayList<SelectionS>();
	/** 完整统计值数据 */
	List<StatisticValueS> listStatistic = new ArrayList<StatisticValueS>();
	/** 时间下标 */
	List<String> listtime = new ArrayList<String>();
	/** 传往echart的数据 */
	List<String> listMain = new ArrayList<String>();
	/** 数据项 */
	List<String> listdata1 = new ArrayList<String>();
	List<String> listdata2 = new ArrayList<String>();
	List<String> listdata3 = new ArrayList<String>();
	List<String> listdata4 = new ArrayList<String>();
	List<String> listdata5 = new ArrayList<String>();
	List<String> listdata6 = new ArrayList<String>();

	/** 泵站ID */
	String id;
	Handler handler;
	/** ListC长度 */
	int length;
	ProgressDialog dialog = null;
	/** Series名称 */
	String seriesName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statu_fragment);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		inliView();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					dialog.dismiss();
					String str = "";
					switch (selection) {
					case 1:
						listMain.clear();
						listdata1.clear();
						listtime.clear();
						break;
					case 2:
						listMain.clear();
						listdata2.clear();
						listtime.clear();

						break;
					case 3:
						listMain.clear();
						listdata3.clear();
						listtime.clear();
						break;
					case 4:
						listMain.clear();
						listdata4.clear();
						listtime.clear();
						break;
					case 5:
						listMain.clear();
						listdata5.clear();
						listtime.clear();
						break;
					case 6:
						listMain.clear();
						listdata6.clear();
						listtime.clear();
						break;
					default:
						break;
					}
					length = listC.size();
					if (length > 0) {

						for (int i = 0; i < listC.size(); i++) {
							str = listC.get(i).getAcquisitionTime();

							String[] st = str.split("T");
							listtime.add(i, st[1]);
							switch (selection) {
							case 1:
								listdata1.add(i, listC.get(i)
										.getOutletPressure());
								break;
							case 2:
								listdata2.add(i, listC.get(i)
										.getInletPressure());
								break;
							case 3:
								listdata3.add(i, listC.get(i).getPowerV());

								break;
							case 4:
								listdata4.add(i, listC.get(i).getFrequency());

								break;
							case 5:
								listdata4.add(i, listC.get(i).getFlowV());

								break;
							case 6:
								listdata6.add(i, listC.get(i).getLiquidlevel());

								break;
							default:
								break;
							}
						}
						switch (selection) {
						case 1:
							listMain = listdata1;
							seriesName = "压力";

							break;
						case 2:
							listMain = listdata2;
							seriesName = "压力";

							break;
						case 3:
							listMain = listdata3;
							seriesName = "功率";

							break;
						case 4:
							listMain = listdata4;
							seriesName = "频率";

							break;
						case 5:
							listMain = listdata5;
							seriesName = "流量";
							break;
						case 6:
							listMain = listdata6;
							seriesName = "液位";

							break;

						default:
							break;
						}
					} else {
						Toast.makeText(StatusActivity.this, "当前时间段没有数据",
								Toast.LENGTH_SHORT).show();
					}
					myWebview.loadUrl("javascript:mchart()");
					if (listStatistic.size() != 0) {
						tv_lessthan_refvalue.setText(listStatistic.get(0)
								.getLowerNumber());
						tv_morethan_refvalue.setText(listStatistic.get(0)
								.getSuperNumber());
					}

				}

			}
		};
	}

	private void inliView() {
		Bundle bd = new Bundle();
		bd = getIntent().getExtras();
		id = bd.getString("assembleId");
		CollectorNoId = bd.getString("CollectorNoID");
		listSelect.add(0, new SelectionS("1", "出口压力"));
		listSelect.add(1, new SelectionS("2", "进口压力"));
		listSelect.add(2, new SelectionS("3", "功率"));
		listSelect.add(3, new SelectionS("4", "频率"));
		listSelect.add(4, new SelectionS("5", "流量"));
		listSelect.add(5, new SelectionS("6", "液位"));
		btnx = (Button) findViewById(R.id.btn_x);
		myWebview = (WebView) findViewById(R.id.mywebview);
		myWebview.getSettings().setJavaScriptEnabled(true);
		myWebview.loadUrl("file:///android_asset/echart.html");
		myWebview.addJavascriptInterface(new JsInterface(), "AndroidWebview");
		btnx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getMonitorInfo();
			}
		});
		// chartContain = (LinearLayout) findViewById(R.id.lin_chart_status);
		// 设置时间的格式和
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		String timeEnd = format.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.get(Calendar.HOUR_OF_DAY) - 3);
		String timeStart = format.format(calendar.getTime());
		et_start = (EditText) findViewById(R.id.et_start_time);
		et_start.setText(timeStart);
		et_start.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					SelectTimeDialog f = new SelectTimeDialog();
					f.setDismissListener(StatusActivity.this);
					FragmentManager fm = getSupportFragmentManager();
					Bundle bd = new Bundle();
					bd.putInt("TAG", 1);
					f.setArguments(bd);
					f.show(fm, "hh");
				}
				return true;
			}
		});
		et_stop = (EditText) findViewById(R.id.et_end_time);
		et_stop.setText(timeEnd);
		et_stop.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					SelectTimeDialog f = new SelectTimeDialog();
					f.setDismissListener(StatusActivity.this);
					FragmentManager fm = getSupportFragmentManager();
					Bundle bd = new Bundle();
					bd.putInt("TAG", 2);
					f.setArguments(bd);
					f.show(fm, "hh");
				}
				return true;
			}
		});
		et_refvalue = (EditText) findViewById(R.id.et_refvalue);
		et_refvalue.setText("1");
		sp_status_select = (Spinner) findViewById(R.id.sp_status_select);
		WebserviceUtil.setItemContent(sp_status_select, listSelect,
				StatusActivity.this);
		sp_status_select
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						selection = position + 1;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		tv_lessthan_refvalue = (EditText) findViewById(R.id.et_lessthan_refvalue);
		tv_morethan_refvalue = (EditText) findViewById(R.id.et_morethan_refvalue);
		tv_more_info = (TextView) findViewById(R.id.tv_more_info);
		// 完整统计值点击事件
		tv_more_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				StatisticDialog sd = new StatisticDialog();
				Bundle bd = new Bundle();

				if (listStatistic.isEmpty()) {
					Toast.makeText(StatusActivity.this, "没有数据",
							Toast.LENGTH_SHORT).show();
				} else {
					StatisticValueS sv = listStatistic.get(0);
					bd.putSerializable("StasticValue", sv);
					sd.setArguments(bd);
					sd.show(fm, "staticdialog");
				}

			}
		});
		dialog = new ProgressDialog(StatusActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Loading...");
	}

	/**
	 * 通过HttpGet获取实时监测信息和通过Webservice获得统计信息
	 * */
	private void getMonitorInfo() {
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				super.run();
				String startTime = et_start.getText().toString();
				String endTime = et_stop.getText().toString();
				String time1 = startTime.replace(" ", "%20");
				String time2 = endTime.replace(" ", "%20");
				methodName = getmethodName();
				RefValue = et_refvalue.getText().toString();
				if ((startTime == null || "".equals(startTime))
						|| (endTime == null || "".equals(endTime))
						|| (RefValue == null || "".equals(RefValue))
						|| (CollectorNoId == null || "".equals(CollectorNoId))) {
					Toast.makeText(StatusActivity.this, "参数选择有误",
							Toast.LENGTH_SHORT).show();
				} else {
					try {

						listC = WebserviceUtil.getAsData(id, time1, time2,
								TestS.class);
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();

						params.put("CollectorNo", CollectorNoId);
						params.put("StartTime", startTime);
						params.put("EndTime", endTime);
						params.put("RefValue", RefValue);
						// 获取统计值
						listStatistic = WebserviceUtil.doSoapList(methodName,
								params, StatisticValueS.class);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (JSONException e) {
					}
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}


	/** 根据ID获取要统计的Webservice方法名 */
	private String getmethodName() {
		String msName = null;
		switch (selection) {
		case 1:
			msName = "AK_S_GetASOutletPressureASByCollectorNo";

			break;
		case 2:
			msName = "AK_S_GetASInletPressureASByCollectorNo";

			break;
		case 3:
			msName = "AK_S_GetASPowerVASByCollectorNo";

			break;
		case 4:
			msName = "AK_S_GetASPowerVASByCollectorNo";

			break;
		case 5:
			msName = "AK_S_GetASPowerVASByCollectorNo";

			break;
		case 6:
			msName = "AK_S_GetASPowerVASByCollectorNo";

			break;

		default:
			break;
		}
		return msName;
	}

	// 开始时间Dialog回调函数
	@Override
	public void onTimeDialogListener(int uniqueIdentifier, String time) {
		if (uniqueIdentifier == 1) {
			et_start.setText(time);
		}
	}

	// 结束时间回调函数
	@Override
	public void onTimeDialogListeners(int uniqueIdentifier, String times) {
		if (uniqueIdentifier == 2) {
			et_stop.setText(times);
		}

	}

	@Override
	public void onResume() {
		// 设置横屏
		if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
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

	public class JsInterface {

		@android.webkit.JavascriptInterface
		public String jsontohtml() {
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < listtime.size(); i++) {
				JSONObject jo = new JSONObject();
				try {
					jo.put("time", listtime.get(i));
					jo.put("data", listMain.get(i));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (org.json.JSONException e) {
					e.printStackTrace();
				}
				jsonArray.put(jo);
			}
			JSONObject jo = new JSONObject();

			try {
				jo.put("seriesName", seriesName);
			} catch (org.json.JSONException e) {
				e.printStackTrace();
			}
			jsonArray.put(jo);
			return jsonArray.toString();
		}
	}
}