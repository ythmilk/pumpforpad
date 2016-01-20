package com.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.data.PumpLocationS;
import com.example.mymap.SelectAssembleDialog;
import com.example.netease.R;
import com.webservice.WebserviceUtil;

public class HomeFragment extends Fragment {

	MapView mMapView = null;

	BaiduMap mBaiduMap;

	LocationClient mLocClient;

	Marker marker;

	private LocationMode mCurrentMode;

	BitmapDescriptor mCurrentMarker;

	private InfoWindow mInfoWindow;
	Handler handler;
	LatLng ll = null;
	boolean isFirstLoc = true;
	String userid = null;
	List<PumpLocationS> PumpLocationList = new ArrayList<PumpLocationS>();
	List<Marker> markList = new ArrayList<Marker>();
	/** 泵站经纬度信息 */
	List<LatLng> locationList = new ArrayList<LatLng>();
	BitmapDescriptor bitmap;
	//public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());// 必须在View之前
		View view = inflater.inflate(R.layout.home, null);
		userid = getArguments().getString("userId");
		mMapView = (MapView) view.findViewById(R.id.id_mymap_home);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		//mLocClient = new LocationClient(getActivity());
		//mLocClient.registerLocationListener(myListener);
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
		// LocationClientOption设置定位相关的属性
		//LocationClientOption option = new LocationClientOption();
		//option.setOpenGps(true);// 打开gps
		//option.setCoorType("bd09ll"); // 设置坐标类型
		//option.setScanSpan(1000);// 设置的扫描间隔，单位是毫秒
		//mLocClient.setLocOption(option);
//		mLocClient.start();
//		LatLng normal = new LatLng(39.5427, 116.2317);
//		MapStatusUpdate s = MapStatusUpdateFactory.newLatLngZoom(normal, 9);
//		mBaiduMap.animateMapStatus(s);
		getPumInfo();
		addInfosOverlay();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			android.support.v4.app.FragmentManager fm = getFragmentManager();
			SelectAssembleDialog sp = new SelectAssembleDialog();

			@Override
			public boolean onMarkerClick(Marker arg0) {
				PumpLocationS ls = (PumpLocationS) arg0.getExtraInfo()
						.get("ls");
				String id = ls.getID();
				String name = ls.getName();
				Bundle args = new Bundle();
				args.putString("id", id);
				args.putString("name", name);
				sp.setArguments(args);
				sp.show(fm, "fragment_edit_name");
				return true;
			}
		});
		return view;
	}
	/** 初始化Marker */
	private void addInfosOverlay() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				LatLngBounds.Builder nn=new LatLngBounds.Builder();
				for (int i = 0; i < PumpLocationList.size(); i++) {
					LatLng l = new LatLng(
							(PumpLocationList.get(i).getLatitude()),
							PumpLocationList.get(i).getLongitude());
					locationList.add(l);
					nn.include(l);
					// PumpLocationS ls=PumpLocationList.get(i);
					// 构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option1 = new MarkerOptions().position(l)
							.icon(bitmap);
					//添加泵站下面的说明文字
					OverlayOptions optionText=new TextOptions().fontSize(24).text(PumpLocationList.get(i).getName()).position(l);
					mBaiduMap.addOverlay(optionText);
					try {
						Marker mm = (Marker) mBaiduMap.addOverlay(option1);
						Bundle bundle = new Bundle();
						bundle.putSerializable("ls", PumpLocationList.get(i));
						mm.setExtraInfo(bundle);
					} catch (NullPointerException e) {
					}
				}
				LatLng center=nn.build().getCenter();
				MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(center);
				mBaiduMap.animateMapStatus(u2);
			}
		
		};
//		LatLngBounds.Builder nn=new LatLngBounds.Builder();
//		for (int i = 0; i < locationList.size(); i++) {
//			nn.include(locationList.get(i));	
//		}
//		Log.i("locationListSize", locationList.size()+"");
//		LatLng center=nn.build().getCenter();
//		LatLng normal = new LatLng(30.67, 104.06);
//		MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(normal);
//		Log.i("jingdu", center.latitude+"");
//		mBaiduMap.animateMapStatus(u2);
		
		//
		
	}
	/** 调用webservice获取所有泵站的信息，包括位置，id,名字 */
	private void getPumInfo() {
		new Thread() {
			public void run() {
				LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("ID", "1");
				try {
					
					PumpLocationList=WebserviceUtil.getAsDataTwo(userid, PumpLocationS.class);
				}  catch (IOException e) {
				} catch (NullPointerException e) {
				}
				Message msg = new Message();
				handler.sendMessage(msg);
			};
		}.start();
	}
	public class MyOnMarkerClickListener implements OnMarkerClickListener {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			return false;
		}
	}

	/**
	 * 定位监听函数
	 * */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			// MyLocationData定位数据，经纬度，等等。。
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			// LatLng地理坐标基本数据结构
			ll = new LatLng(location.getLatitude(), location.getLongitude());
			mBaiduMap.showInfoWindow(mInfoWindow);
			// MapStatusUpdate描述地图状态将要发生的变化newLatLng(ll)设置地图的中心点
//			if (isFirstLoc) {
//				isFirstLoc = false;
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 9);
//				mBaiduMap.animateMapStatus(u);
//			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//mLocClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}
}
