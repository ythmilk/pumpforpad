package com.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
/**基础Activity，用于排断网络状况*/
public class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 检查用户的网络情况
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 返回网络信息
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
		} else {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			UnConnetionDialog df=new UnConnetionDialog();
			df.show(fm, "dd");
		}
		super.onCreate(savedInstanceState);
	}
}