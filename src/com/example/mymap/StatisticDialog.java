package com.example.mymap;

import com.data.StatisticValueS;
import com.example.netease.R;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**统计值Dialog*/
public class StatisticDialog extends DialogFragment {
	
	TextView tv_s_argdur;
	TextView tv_s_avgdurbottom;
	TextView tv_s_avgdurtop;
	TextView tv_s_lownumber;
	TextView tv_s_maxdur;
	TextView tv_s_maxdurbottom;
	TextView tv_s_maxdurtop;
	TextView tv_s_mindur;
	TextView tv_s_mindurbottom;
	TextView tv_s_mindurtop;
	TextView tv_s_supnumber;
	TextView tv_settime;
	StatisticValueS sv=new StatisticValueS();
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	getDialog().setTitle("统计值");
	View view=inflater.inflate(R.layout.stat_dialog, null);
	sv=(StatisticValueS) getArguments().getSerializable("StasticValue");
//	Log.i("cc", sv.getAvgduration());
	initview(view);
	 tv_s_argdur.setText(sv.getAvgduration());
	 
	 
	 tv_s_avgdurbottom.setText(sv.getAvgdurationOfButtom());
	 tv_s_avgdurtop.setText(sv.getAvgdurationOfTop());
	 tv_s_lownumber.setText(sv.getLowerNumber());
	 tv_s_maxdur.setText(sv.getMaxduration());
	 tv_s_maxdurbottom.setText(sv.getMaxdurationOfButtom());
	 tv_s_maxdurtop.setText(sv.getMaxdurationOfTop());
	 tv_s_mindur.setText(sv.getMinduration());
	 tv_s_mindurbottom.setText(sv.getMindurationOfButtom());
	 tv_s_mindurtop.setText(sv.getMindurationOfTop());
	 tv_s_supnumber.setText(sv.getSuperNumber());
	return view;
}
private void initview(View view) {
	 tv_s_argdur=(TextView) view.findViewById(R.id.tv_s_argdur);
	 tv_s_avgdurbottom=(TextView) view.findViewById(R.id.tv_s_avgdurbottom);
	 tv_s_avgdurtop=(TextView) view.findViewById(R.id.tv_s_avgdurtop);
	 tv_s_lownumber=(TextView) view.findViewById(R.id.tv_s_lownumber);
	 tv_s_maxdur=(TextView) view.findViewById(R.id.tv_s_maxdur);
	 tv_s_maxdurbottom=(TextView) view.findViewById(R.id.tv_s_maxdurbottom);
	 tv_s_maxdurtop=(TextView) view.findViewById(R.id.tv_s_maxdurtop);
	 tv_s_mindur=(TextView) view.findViewById(R.id.tv_s_mindur);
	 tv_s_mindurbottom=(TextView) view.findViewById(R.id.tv_s_mindurbottom);
	 tv_s_mindurtop=(TextView) view.findViewById(R.id.tv_s_mindurtop);
	 tv_s_supnumber=(TextView) view.findViewById(R.id.tv_s_supnumber);
}
}
