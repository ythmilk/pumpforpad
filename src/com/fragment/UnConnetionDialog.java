package com.fragment;

import com.example.netease.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
/**
 * 网络设置提示DialogFragment*/
public class UnConnetionDialog extends DialogFragment {
	Button btnSure;
	Button btnCanncel;

	public UnConnetionDialog() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("请设置你的网络：");
		View view = inflater.inflate(R.layout.dialog_unconnection, null);
		btnSure = (Button) view.findViewById(R.id.btn_uc_sure);
		btnCanncel = (Button) view.findViewById(R.id.btn_uc_canncel);
		btnSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);
				dismiss();
			}
		});
		btnCanncel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		getActivity().finish();
			}
		});
		return view;
	}
}
