package com.example.mymap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.data.RealTimeData;
import com.example.netease.R;


public class SelectTimeDialog extends DialogFragment {
	DatePicker datePicker;
	TimePicker timePicker;
	Button btnSure;
	String time;
	int TAG;

	private onTimeDialogListener monTimeDialogListener = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.select_time_dialog, null);
		TAG=getArguments().getInt("TAG");
		
		datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		timePicker = (TimePicker) view.findViewById(R.id.time_picker);
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), null);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		
		btnSure = (Button) view.findViewById(R.id.btn_sure_time_dialog);
		btnSure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		
				Fragment f = new SelectTimeDialog();
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d/%02d/%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				sb.append(" ");
				sb.append(timePicker.getCurrentHour()).append(":")
						.append(timePicker.getCurrentMinute());
				time = sb.toString();
				if (TAG==1) {
					monTimeDialogListener.onTimeDialogListener(1, time);
				}else if (TAG==2) {
					monTimeDialogListener.onTimeDialogListeners(2, time);
				}
				dismiss();
				System.out.println(time);
			}
		});
		
		return view;
	}
	public interface onTimeDialogListener {
		public abstract void onTimeDialogListener(int uniqueIdentifier,
				String time);
		public abstract void onTimeDialogListeners(int uniqueIdentifier,
				String times);
	}

	public void setDismissListener(onTimeDialogListener monTimeDialogListener) {
		this.monTimeDialogListener = monTimeDialogListener;
	}
}
