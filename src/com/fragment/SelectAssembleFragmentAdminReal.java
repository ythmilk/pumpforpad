package com.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.data.AssembleS;
import com.data.CommunityS;
import com.data.ProvinceS;
import com.data.PumpingS;
import com.data.SetValueData;
import com.example.netease.R;
import com.webservice.WebserviceUtil;

/** 管理员权限选择泵站实时运行状态Fragment */
public class SelectAssembleFragmentAdminReal extends BaseFragment implements
		OnClickListener {
	Spinner spPRovince;
	Spinner spDistrict;
	Spinner spCounty;
	Spinner spCommunity;
	Spinner spPump;
	Spinner spAssemble;
	Button btnSure;

	List<ProvinceS> ProvinceList = new ArrayList<ProvinceS>();
	List<ProvinceS> DistrictList = new ArrayList<ProvinceS>();
	List<ProvinceS> CountyList = new ArrayList<ProvinceS>();
	List<CommunityS> CommunityList = new ArrayList<CommunityS>();
	List<PumpingS> PumpList = new ArrayList<PumpingS>();
	List<AssembleS> AssembleList = new ArrayList<AssembleS>();
	List<SetValueData> currentList = new ArrayList<SetValueData>();
	Handler handler;
	String assembleId=null;
	String assembleName=null;
	String assembleName1=null;
	String assembleId1=null;
	Context context;
	String CollectorNoId=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.select_assemble, null);
		btnSure = (Button) view.findViewById(R.id.btn_sure);
		btnSure.setOnClickListener(this);

		spPRovince = (Spinner) view.findViewById(R.id.sp_province);
		spPRovince.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		spDistrict = (Spinner) view.findViewById(R.id.sp_district);
		spDistrict.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spCounty = (Spinner) view.findViewById(R.id.sp_county);
		spCounty.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spCommunity = (Spinner) view.findViewById(R.id.sp_community);
		spCommunity.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spPump = (Spinner) view.findViewById(R.id.sp_pump);
		spPump.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spAssemble = (Spinner) view.findViewById(R.id.sp_assemble);
		spAssemble.setOnItemSelectedListener(new MyOnItemSelectedListener());
		context = getActivity().getApplicationContext();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					if (msg.what == 1) {
						WebserviceUtil.setItemContent(spPRovince, ProvinceList,
								context);
					//spPRovince.setSelection(position)
					}
					if (msg.what == 2) {
						WebserviceUtil.setItemContent(spDistrict, DistrictList,
								context);
					}
					if (msg.what == 3) {
						WebserviceUtil.setItemContent(spCounty, CountyList,
								context);
					}
					if (msg.what == 4) {
						WebserviceUtil.setItemContent(spCommunity,
								CommunityList, context);
					}
					if (msg.what == 5) {
						WebserviceUtil
								.setItemContent(spPump, PumpList, context);
					}
					if (msg.what == 6) {
						WebserviceUtil.setItemContent(spAssemble, AssembleList,
								context);
					}

				} catch (NullPointerException e) {
				}

			}

		};
		new Thread() {
			public void run() {
				LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
				try {
					ProvinceList = WebserviceUtil.doSoapList("AK_GetProvince",
							params, ProvinceS.class);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			};
		}.start();

		return view;
	}

	/** Spinner 点击监听事件 */
	class MyOnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				final int position, long id) {
			switch (parent.getId()) {

			case R.id.sp_province: {
				new Thread() {

					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("ProvinceID", ProvinceList.get(position)
								.getID());
						try {
							DistrictList = WebserviceUtil.doSoapList(
									"AK_GetDistrictsByProvince", params,
									ProvinceS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
						}
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
					};
				}.start();
			}
				break;
			case R.id.sp_district: {
				new Thread() {

					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("DistrictID", DistrictList.get(position)
								.getID());
						try {
							CountyList = WebserviceUtil.doSoapList(
									"AK_GetCountyByDistrict", params,
									ProvinceS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
						}
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					};
				}.start();

			}
				break;
			case R.id.sp_county: {
				new Thread() {

					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("countyID", CountyList.get(position).getID());

						try {
							CommunityList = WebserviceUtil.doSoapList(
									"AK_GetCommunityByCounty", params,
									CommunityS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
						}
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					};
				}.start();

			}
				break;

			case R.id.sp_community: {
				new Thread() {
					public void run() {
						PumpList = null;
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("ID", CommunityList.get(position).getID());
						try {
							PumpList = WebserviceUtil.doSoapList(
									"AK_P_GetPumpingSByCommunity", params,
									PumpingS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
						}
						Message msg = new Message();
						msg.what = 5;
						handler.sendMessage(msg);
					};
				}.start();
			}
				break;
			case R.id.sp_pump: {
				new Thread() {
					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("ID", PumpList.get(position).getID());
						try {
							AssembleList = WebserviceUtil.doSoapList(
									"AK_A_GetAssemblingSetByPumpingS", params,
									AssembleS.class);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
						}
						Message msg = new Message();
						msg.what = 6;
						handler.sendMessage(msg);
					};
				}.start();
			}
				break;
			case R.id.sp_assemble: {
				assembleId = AssembleList.get(position).getID();
				assembleName1 = AssembleList.get(position).getName();
				new Thread() {
					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("AssemblingSetID", AssembleList
								.get(position).getID());
						try {
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
						} catch (NullPointerException e) {
						}
					};
				}.start();
			}
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

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
			Intent i = new Intent(getActivity(), RealTimeMonitorActivity.class);
			i.putExtras(bundle);
			startActivity(i);
		} else if (tag.equals("2")) {
			tx.add(R.id.content_frame, chartF);
		} else if (tag.equals("3")) {
			if (CollectorNoId == null || "".equals(CollectorNoId)||CollectorNoId.equals("anyType{}")) {
				Toast.makeText(getActivity(), "没有采集器ID，请选择其他泵站",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(getActivity(), StatusActivity.class);
				i.putExtras(bd);
				startActivity(i);
			}
			;
		}
		tx.addToBackStack(null);
		tx.commit();
	}
	@Override
	public void onResume() {
		super.onResume();
		if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
}
