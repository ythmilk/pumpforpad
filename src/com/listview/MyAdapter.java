package com.listview;

import java.util.List;
import java.util.Map;

import com.example.netease.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	/** ���캯�� */
	public MyAdapter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Control control = null;
		if (convertView == null) {
			control = new Control();
			convertView = layoutInflater.inflate(R.layout.list_content, null);
			control.tv_message_list_title = (TextView) convertView
					.findViewById(R.id.tv_message_list_title);
			control.tv_message_list_time = (TextView) convertView
					.findViewById(R.id.tv_message_list_time);
			control.tv_message_list_content = (TextView) convertView
					.findViewById(R.id.tv_message_list_content);
			convertView.setTag(control);
		} else {
			control = (Control) convertView.getTag();
		}
		control.tv_message_list_title.setText(data.get(position).get("title")
				.toString());
		control.tv_message_list_time.setText(data.get(position).get("time")
				.toString());
		control.tv_message_list_content.setText(data.get(position).get("content")
				.toString());
		return convertView;
	}

}
