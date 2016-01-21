package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.example.netease.R;
import com.listview.MyAdapter;
import com.pinyin.SearchAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/** 短信管理Fragment */
public class MessageMonitorFragment extends BaseFragment {

	AutoCompleteTextView autotext;
	ArrayAdapter arrayAdapter;
	SearchAdapter adapter = null;
	ListView listview;
	/** 通过webservice获取之后保存下来,模糊查询的数据*/
	public String[] hanzi = new String[] { "长江证券100002", "长江证券100001",
			"农业银行200001", "工商银行300001", "招商银行100001", "建设银行100001",
			"中国银行100002", "华夏银行500002", "上海银行100010", "浦发银行200009" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());// 必须在View之前
		View view = inflater.inflate(R.layout.message_monitor, null);
		
		autotext = (AutoCompleteTextView) view.findViewById(R.id.autotext);
		adapter = new SearchAdapter<String>(getActivity().getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, hanzi,
				SearchAdapter.ALL);// 速度优先
		autotext.setAdapter(adapter);
		autotext.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
		listview = (ListView)view.findViewById(R.id.lv_message);
		List<Map<String, Object>> list = getData();
		listview.setAdapter(new MyAdapter(getActivity().getApplicationContext(), list));
		return view;
	}
	/** 设置ListView里边的内容 */
	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 在这边改传递的内容
			map.put("title", "泵站运行情况" + i);
			map.put("time", "2016:1:21:18:26" + i);
			map.put("content", "这是一个详细内容" + i);
			list.add(map);
		}
		return list;
	}

}
