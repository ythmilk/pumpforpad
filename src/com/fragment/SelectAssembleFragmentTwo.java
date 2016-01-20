package com.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.data.AssembleS;
import com.data.BaseDataS;
import com.example.netease.R;
import com.webservice.WebserviceUtil;

public class SelectAssembleFragmentTwo extends BaseFragment {
	/** 用户权限2泵站 */
	Spinner sp_pump;
	/** 用户权限2机组 */
	Spinner sp_assemble;
	/** 用户权限2确定按钮 */
	Button btn_sure;
	String userId = null;
	String TAG = null;
	Handler handler;
	List<BaseDataS> listPumus = new ArrayList<BaseDataS>();
	List<AssembleS> listAssembbles = new ArrayList<AssembleS>();
	String assembleName = null;
	String CollectorNoId = null;
	String assembleId = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.select_assemble_two, null);
		initiview(view);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

				switch (msg.what) {
				case 1:
					WebserviceUtil.setItemContent(sp_pump, listPumus,
							getActivity().getApplicationContext());
					break;
				case 2:
					WebserviceUtil.setItemContent(sp_assemble, listAssembbles,
							getActivity().getApplicationContext());
					break;
				default:
					break;
				}
			};
		};
		getpunms();

		sp_pump.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				new Thread() {
					@Override
					public void run() {
						super.run();
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("ID", listPumus.get(arg2).getID());
						try {
							listAssembbles = WebserviceUtil.doSoapList(
									"AK_A_GetAssemblingSetByPumpingS", params,
									AssembleS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
					}
				}.start();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		sp_assemble.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				new Thread() {
					@Override
					public void run() {
						super.run();
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("AssemblingSetID", listAssembbles.get(arg2)
								.getID());
						try {
							assembleId = listAssembbles.get(arg2).getID();
							assembleName = WebserviceUtil.doSoapAssembleName(
									"AK_A_GetAssemblingSetNamebyID", params);
							CollectorNoId = WebserviceUtil
									.doSoapAssembleName(
											"AK_C_CollectorNoByAssemblingSetID",
											params);

						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		btn_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Fragment chartF = new ChartView();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();

				Bundle bundle = new Bundle();
				bundle.putString("assembleId", assembleId);
				bundle.putString("assembleName", assembleName);
				// f1.setArguments(bundle);
				chartF.setArguments(bundle);
				Bundle bd = new Bundle();
				bd.putString("assembleId", assembleId);
				bd.putString("assembleName", assembleName);
				bd.putString("CollectorNoID", CollectorNoId);

				// 根据传过来的TAG来判断是实时曲线还是实时监测
				String tag = getArguments().getString("TAG");
				if (tag.equals("1")) {
					Intent i = new Intent(getActivity(),
							RealTimeMonitorActivity.class);
					i.putExtras(bundle);
					startActivity(i);
				} else if (tag.equals("2")) {
					tx.add(R.id.content_frame, chartF);
				} else if (tag.equals("3")) {
					if (CollectorNoId == null || "".equals(CollectorNoId)
							|| CollectorNoId.equals("anyType{}")) {
						Toast.makeText(getActivity(), "没有采集器ID，请选择其他泵站",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent i = new Intent(getActivity(),
								StatusActivity.class);
						i.putExtras(bd);
						startActivity(i);
					}
					;
				}
				tx.addToBackStack(null);
				tx.commit();

			}
		});

		return view;
	}

	/** 初始化控件 */
	private void initiview(View view) {
		userId = getArguments().getString("userId");
		TAG = getArguments().getString("TAG");
		Log.i("sss", userId);
		sp_pump = (Spinner) view.findViewById(R.id.sp_pump_two);
		sp_assemble = (Spinner) view.findViewById(R.id.sp_assemble_two);
		btn_sure = (Button) view.findViewById(R.id.btn_sure_two);
	}

	/** 根据用户ID获取泵站信息 */
	private void getpunms() {
		new Thread() {
			public void run() {

				try {
					listPumus = WebserviceUtil.getAsDataTwo(userId,
							BaseDataS.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			};
		}.start();
	}

}
