package com.fragment;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netease.R;
import com.webservice.WebserviceUtil;
public class LoginAty extends FragmentActivity {
	/** 取消登陆图片 */
	ImageView imgCancle;
	/** 登陆按钮 */
	Button btnLogin;
	/** 用户名 EditText */
	EditText et_userName;
	/** 密码 */
	EditText et_password;
	/** 忘记密码 EditText */
	TextView tv_forgetPassword;
	/** 用户名 */
	String userName = null;
	/** 密码 */
	String password = null;
	/** 用户ID */
	String userId = null;
	Handler handler;
	/** 是否登陆成功标志 */
	int TAG = 0;
	/** 用户权限标志 */
	int USER_PERMISSION;
	/** 是否记住密码CheckBox */
	CheckBox remember;
	/** 网络请求缓冲 ProgressDialog */
	ProgressDialog dialog = null;
	String localVersionName = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_aty);
		initiView();
		// 用户名输入完成监测
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userName = et_userName.getText().toString();
				password = et_password.getText().toString();
				if (isNetConnect()) {
					if ((userName == null || "".equals(userName))
							|| (password == null || "".equals(password))) {
						Toast.makeText(LoginAty.this, "用户名或密码输入错误",
								Toast.LENGTH_SHORT).show();
					} else {
						judgelogin();
					}
				} else {
					Toast.makeText(LoginAty.this, "网络错误", Toast.LENGTH_SHORT)
							.show();
				}
			}

			private void judgelogin() {
				dialog.show();
				new Thread() {
					@Override
					public void run() {
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("Name", userName);
						params.put("MyPassword", password);
						String methodName = "AK_IsValidUser";

						LinkedHashMap<String, Object> paramsId = new LinkedHashMap<String, Object>();
						paramsId.put("UserName", userName);
						String methodNameId = "AK_U_GetUserIDByUserName";
						try {
							String isVaildUser = WebserviceUtil
									.doSoapAssembleName(methodName, params);
							USER_PERMISSION = Integer.valueOf(isVaildUser);

							userId = WebserviceUtil.doSoapAssembleName(
									methodNameId, paramsId);
							if (isVaildUser.equals("0")) {
								TAG = 1;
								Message msg = new Message();
								handler.sendMessage(msg);
							} else {
								if (remember.isChecked()) {
									SaveUserDate();
								}
								// 将用户ID和用户权限标志传到MainActivity
								Bundle bd = new Bundle();
								bd.putString("UserId", userId);
								bd.putString("UserName", userName);
								bd.putInt("USER_PERMISSION", USER_PERMISSION);
								Log.i("USER_PERMISSION", USER_PERMISSION + "");
								Intent i = new Intent(LoginAty.this,
										MainActivity.class);
								i.putExtras(bd);
								startActivity(i);
								finish();
							}
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
		handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
				if (TAG == 1) {
					Toast.makeText(LoginAty.this, "用户名或密码输入错误",
							Toast.LENGTH_SHORT).show();
				}
			};
		};
	}
	private void initiView() {
		dialog = new ProgressDialog(LoginAty.this);
		dialog.setMessage("Loading...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		remember = (CheckBox) findViewById(R.id.remember);
		imgCancle = (ImageView) findViewById(R.id.image_login_cancle);
		imgCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		et_userName = (EditText) findViewById(R.id.login_et_username);
		et_password = (EditText) findViewById(R.id.login_et_login_password);

		tv_forgetPassword = (TextView) findViewById(R.id.tv_login_forgetpassword);
		tv_forgetPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(LoginAty.this, "请联系管理员", Toast.LENGTH_SHORT)
						.show();
			}
		});
		btnLogin = (Button) findViewById(R.id.btn_login_in1);
		LoadUserDate();
	}

	/**
	 * 保存用户信息
	 */
	private void SaveUserDate() {
		// 载入配置文件
		SharedPreferences sp = getSharedPreferences("loginInfo", 0);
		// 写入配置文件
		Editor spEd = sp.edit();
		if (remember.isChecked()) {
			spEd.putBoolean("isSave", true);
			spEd.putString("name", et_userName.getText().toString());
			spEd.putString("password", et_password.getText().toString());
		} else {
			spEd.putBoolean("isSave", false);
			spEd.putString("name", "");
			spEd.putString("password", "");
		}
		spEd.commit();
	}

	/**
	 * 载入已记住的用户信息
	 */
	private void LoadUserDate() {
		SharedPreferences sp = getSharedPreferences("loginInfo", 0);
		if (sp.getBoolean("isSave", false)) {
			String username = sp.getString("name", "");
			String userpassword = sp.getString("password", "");
			if (!("".equals(username) && "".equals(userpassword))) {
				et_userName.setText(username);
				et_password.setText(userpassword);
				remember.setChecked(true);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialog.dismiss();
	}

	@Override
	public void onResume() {
		// 设置横屏
		if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	/** 判断网络连接情况 */
	public boolean isNetConnect() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();

	}
}
