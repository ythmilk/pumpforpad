package com.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.netease.R;
import com.materialmenu.MaterialMenuDrawable;
import com.materialmenu.MaterialMenuDrawable.Stroke;
import com.materialmenu.MaterialMenuIcon;

public class MainActivityTest extends FragmentActivity {
	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	/** 左边栏菜单 */
	private ListView mMenuListView;
	/** 菜单列表 */
	private String[] mMenuTitles;
	/** Material Design风格 */
	private MaterialMenuIcon mMaterialMenuIcon;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	private View showView;
	private LinearLayout mMenu_layout;
	FragmentManager fm = getSupportFragmentManager();
	/** 用户ID */
	String userId = null;
	/** 用户Name */
	String userName = null;
	/** 用户权限 */
	int USER_PERMISSION = 1;
	/** 用户管理Linlayout */
	LinearLayout linUserManage = null;
	/** Textview UserName */
	TextView tv_userName = null;
	/** 是否点击选择页面 */
	static int IsClick = 0;
	Fragment fragmentTwo;
	Fragment homeF;
	Fragment monitorF;
	/** 管理员权限获取状态统计Fragment */
	Fragment adminFragmentStatus;
	/** 管理员权限获取实时监测数据Fragment */
	Fragment adminFragmentReal;
	/** 主界面fragment */
	Fragment homeFragment;
	/**短信管理Fragment*/
	Fragment messageMonitorFragment;
	FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuListView = (ListView) findViewById(R.id.left_drawer);
		mMenu_layout = (LinearLayout) findViewById(R.id.menu_layout);
		fragmentManager = getSupportFragmentManager();
		linUserManage = (LinearLayout) findViewById(R.id.user_manage_lin);
		linUserManage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivityTest.this, LoginAty.class);
				startActivity(i);
			}
		});
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		Bundle bd = getIntent().getExtras();
		userId = bd.getString("UserId");
		userName = bd.getString("UserName");
		USER_PERMISSION = bd.getInt("USER_PERMISSION");
		tv_userName.setText(userName);

		this.showView = mMenu_layout;
		// 初始化菜单列表
		mMenuTitles = new String[] { "主页", "机组实时监测", "机组状态统计" ,"短信管理"};
		// mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mMenuListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mMenuTitles));
		mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// 设置ActionBar可见，并且切换菜单和内容视图
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mMaterialMenuIcon = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	/**
	 * ListView上的Item点击事件
	 * 
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends
			DrawerLayout.SimpleDrawerListener {
		/**
		 * 当导航菜单滑动的时候被执行
		 */
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			showView = drawerView;
			if (drawerView == mMenu_layout) {// 根据isDirection_left决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_left ? 2 - slideOffset : slideOffset);
			}
		}

		/**
		 * 当导航菜单打开时执行
		 */
		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			if (drawerView == mMenu_layout) {
				isDirection_left = true;
			}
		}

		/**
		 * 当导航菜单关闭时执行
		 */
		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			if (drawerView == mMenu_layout) {
				isDirection_left = false;
			}
		}
	}

	/**
	 * 切换主视图区域的Fragment
	 * 
	 * @param position
	 */
	private void selectItem(int position) {
		switch (position) {
		case 0:
			setHomeFragment();
			break;
		case 1:
			setAdminFragmentReal();
			break;
		case 2:
			setAdminFragmentStatus();
			break;
		case 3:
			setMessageMonitorFragment();
			break;
		default:
			break;
		}
		// 更新选择后的item和title
		mMenuListView.setItemChecked(position, true);
		setTitle(mMenuTitles[position]);
		// 关闭菜单
		mDrawerLayout.closeDrawer(mMenu_layout);
	}

	/**
	 * 点击ActionBar上菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			if (showView == mMenu_layout) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(mMenu_layout);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(mMenu_layout);
				}
			}

			break;
		case R.id.action_settings: {
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();
		}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 根据onPostCreate回调的状态，还原对应的icon state
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mMaterialMenuIcon.syncState(savedInstanceState);
	}

	/**
	 * 根据onSaveInstanceState回调的状态，保存当前icon state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mMaterialMenuIcon.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 加载菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onResume() {
		// 设置横屏
		if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	/** 隐藏所有fragmrnt */
	private void hideall(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (adminFragmentReal != null) {
			transaction.hide(adminFragmentReal);
		}
		if (adminFragmentStatus != null) {
			transaction.hide(adminFragmentStatus);
		}if (messageMonitorFragment!=null) {
			transaction.hide(messageMonitorFragment);
		}
	}

	/** 设置homeFragmet */
	private void setHomeFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏所有fragment
		hideall(transaction);
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
			transaction.add(R.id.content_frame, homeFragment);
		} else {
			transaction.show(homeFragment);
		}
		transaction.commit();
	}

	/** 设置SelectAssembleFragmentAdminReal */
	private void setAdminFragmentReal() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏所有fragment
		hideall(transaction);
		Bundle bd = new Bundle();
		bd.putString("TAG", "1");
		if (adminFragmentReal == null) {
			adminFragmentReal = new SelectAssembleFragmentAdminReal();

			adminFragmentReal.setArguments(bd);
			transaction.add(R.id.content_frame, adminFragmentReal);
		} else {
			// adminFragmentReal.setArguments(bd);
			transaction.show(adminFragmentReal);
		}
		transaction.commit();
	}

	/** 设置SelectAssembleFragmentAdminiStatus */
	private void setAdminFragmentStatus() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideall(transaction);
		Bundle bd = new Bundle();
		bd.putString("TAG", "3");
		if (adminFragmentStatus == null) {
			adminFragmentStatus = new SelectAssembleFragmentAdminiStatus();
			adminFragmentStatus.setArguments(bd);
			transaction.add(R.id.content_frame, adminFragmentStatus);
		} else {
			// adminFragmentStatus.setArguments(bd);
			transaction.show(adminFragmentStatus);
		}
		transaction.commit();
	}
	/** 设置SelectAssembleFragmentAdminiStatus */
	private void setMessageMonitorFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideall(transaction);
		if (messageMonitorFragment == null) {
			messageMonitorFragment = new MessageMonitorFragment();
			transaction.add(R.id.content_frame, messageMonitorFragment);
		} else {
			transaction.show(messageMonitorFragment);
		}
		transaction.commit();
	}
}