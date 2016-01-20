package com.example.mymap;

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
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.data.AssembleS;
import com.example.netease.R;
import com.fragment.RealTimeMonitorActivity;
import com.fragment.StatusActivity;
import com.webservice.WebserviceUtil;

public class SelectAssembleDialog extends DialogFragment {
	/** 泵站ID */
	String id = null;
	/** 泵站Name */
	String name = null;
	/** 机组ID */
	String assembleId=null;
	/** 机组Name */
	String assembleName=null;
	/** 选择机组Spinner */
	Spinner spMap;
	/** 取消按钮 */
	Button canncel;
	/** 确定按钮 */
	Button sure;
	/** dialog标题 */
	TextView tv_title;
	/** 机组信息列表 */
	List<AssembleS> AssembleList = new ArrayList<AssembleS>();

	Handler handler;
	Context context;
	/**采集器ID*/
	String CollectorNoId=null;
	public SelectAssembleDialog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 去掉标题
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = inflater.inflate(R.layout.select_assemble_dialog, null);

		context = getActivity().getApplicationContext();
		tv_title = (TextView) view.findViewById(R.id.tv_map_dialog_title);

		canncel = (Button) view.findViewById(R.id.btn_dialog_canncel);
		canncel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CollectorNoId == null || "".equals(CollectorNoId)||"anyType{}".equals(CollectorNoId)) {
					Toast.makeText(getActivity(), "请稍后",
							Toast.LENGTH_SHORT).show();
				} else {
					Bundle bd = new Bundle();
					bd.putString("assembleId", assembleId);
					bd.putString("assembleName", assembleName);
					bd.putString("CollectorNoID", CollectorNoId);
					Intent i = new Intent(getActivity(), StatusActivity.class);
					i.putExtras(bd);
					startActivity(i);
				}
				dismiss();
			}
		});
		sure = (Button) view.findViewById(R.id.btn_dialog_sure);
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("assembleId", assembleId);
				bundle.putString("assembleName", assembleName);
				Intent i=new Intent(getActivity(), RealTimeMonitorActivity.class);
				i.putExtras(bundle);
				startActivity(i);
				dismiss();
			}
		});
		id = getArguments().getString("id");
		name = getArguments().getString("name");
		setdialogInfo();
		getassembleInfo(id);
		spMap = (Spinner) view.findViewById(R.id.sp_map);
		spMap.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					final int position, long id) {
				assembleId = AssembleList.get(position).getID();
				Log.i("assembleId", assembleId);
				new Thread(){
					@Override
					public void run() {
						super.run();
						LinkedHashMap<String, Object> params1 = new LinkedHashMap<String, Object>();
						params1.put("AssemblingSetID", assembleId);
						try {
							CollectorNoId = WebserviceUtil
									.doSoapAssembleName(
											"AK_C_CollectorNoByAssemblingSetID",
											params1);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}catch (NullPointerException e) {
						}
					}
				}.start();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		// 通过泵站id获得泵站下机组列表

		return view;
	}
	/** 请求Webservice获得机组下泵站信息 */
	private void getassembleInfo(final String id) {
		if (id.equals(null)) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), R.layout.my_spinner_style);
			spMap.setAdapter(adapter);
		} else {
			new Thread() {
				public void run() {
					LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
					params.put("ID", id);
					try {
						AssembleList = WebserviceUtil.doSoapList(
								"AK_A_GetAssemblingSetByPumpingS", params,
								AssembleS.class);
						
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}catch (NullPointerException e) {
					}
					Message msg = new Message();
					handler.sendMessage(msg);
				};
			}.start();
		}
	}

	
	/** 设置Dialog的Spinner选项和Title */
	private void setdialogInfo() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					WebserviceUtil.setItemContent(spMap, AssembleList, context);
					tv_title.setText(name + ":");
				} catch (NullPointerException e) {
				}
			}
		};
	}
}
