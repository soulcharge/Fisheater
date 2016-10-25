package com.baidu.location.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.baidulocationdemo.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.LocationService;
import com.baidu.location.service.Utils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends Activity implements View.OnClickListener {
	//权限请求
	private final int SDK_PERMISSION_REQUEST = 127;
	private String permissionInfo;
	//地图
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	//	private Button reset;
	private LocationService locService;
	private LinkedList<MainActivity.LocationEntity> locationList = new LinkedList<MainActivity.LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
	//短信监听器
	private IntentFilter receiveFilter;
	private MessageReceiver messageReceiver;
	//按钮
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private ToggleButton btn4;
	//定位经纬度
	private LocationService locationService;
	private String point;
	private Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 取得权限
		getPersimmions();
		//地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		locService = ((LocationApplication) getApplication()).locationService;
		LocationClientOption mOption = locService.getDefaultLocationClientOption();
		mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
		mOption.setCoorType("bd09ll");
		mOption.setOpenGps(true);

		LocationClient mClient = new LocationClient(this);
		mClient.setLocOption(mOption);
		mClient.registerLocationListener(listener);
		mClient.start();
		//短信监听器
		receiveFilter = new IntentFilter();
		receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, receiveFilter);

		//按钮
		btn1 = (Button) findViewById(R.id.btn_friends);
		btn2 = (Button) findViewById(R.id.btn_enemies);
		btn3 = (Button) findViewById(R.id.btn_locate);
		btn4 = (ToggleButton) findViewById(R.id.btn_refresh);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
//		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);

	}

	// 取得权限
	@TargetApi(23)
	private void getPersimmions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ArrayList<String> permissions = new ArrayList<String>();
			/***
			 * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
			 */
			// 定位精确位置
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
			}
			if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
			}
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
			// 读写权限
			if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
			}
			// 读取电话状态权限
			if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
			}

			if (permissions.size() > 0) {
				requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
			}
		}
	}

	//增加权限
	@TargetApi(23)
	private boolean addPermission(ArrayList<String> permissionsList, String permission) {
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请	
			if (shouldShowRequestPermissionRationale(permission)) {
				return true;
			} else {
				permissionsList.add(permission);
				return false;
			}

		} else {
			return true;
		}
	}

	//权限请求结果
	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}
	//地图定位
	/***
	 * 定位结果回调，在此方法中处理定位结果
	 */
	BDLocationListener listener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub

			if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
				Message locMsg = locHander.obtainMessage();
				Bundle locData;
				locData = Algorithm(location);
				if (locData != null) {
					locData.putParcelable("loc", location);
					locMsg.setData(locData);
					locHander.sendMessage(locMsg);
				}
			}
		}
	};

	/***
	 * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
	 * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
	 * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
	 *
	 * @param BDLocation
	 * @return Bundle
	 */
	private Bundle Algorithm(BDLocation location) {
		Bundle locData = new Bundle();
		double curSpeed = 0;
		if (locationList.isEmpty() || locationList.size() < 2) {
			LocationEntity temp = new LocationEntity();
			temp.location = location;
			temp.time = System.currentTimeMillis();
			locData.putInt("iscalculate", 0);
			locationList.add(temp);
		} else {
			if (locationList.size() > 5)
				locationList.removeFirst();
			double score = 0;
			for (int i = 0; i < locationList.size(); ++i) {
				LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
						locationList.get(i).location.getLongitude());
				LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
				double distance = DistanceUtil.getDistance(lastPoint, curPoint);
				curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
				score += curSpeed * Utils.EARTH_WEIGHT[i];
			}
			if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
				location.setLongitude(
						(locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
								/ 2);
				location.setLatitude(
						(locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
								/ 2);
				locData.putInt("iscalculate", 1);
			} else {
				locData.putInt("iscalculate", 0);
			}
			LocationEntity newLocation = new LocationEntity();
			newLocation.location = location;
			newLocation.time = System.currentTimeMillis();
			locationList.add(newLocation);

		}
		return locData;
	}

	/***
	 * 接收定位结果消息，并显示在地图上
	 */
	private Handler locHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				BDLocation location = msg.getData().getParcelable("loc");
				int iscal = msg.getData().getInt("iscalculate");
				if (location != null) {
					LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
					// 构建Marker图标
					BitmapDescriptor bitmap = null;
					if (iscal == 0) {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaji); // 非推算结果
					}
					// 构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
					// 在地图上添加Marker，并显示
					mBaiduMap.addOverlay(option);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	};

	/**
	 * 封装定位结果和时间的实体类
	 *
	 * @author baidu
	 */
	class LocationEntity {
		BDLocation location;
		long time;
	}

	//经纬度
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// -----------location config ------------
		locationService = ((LocationApplication) getApplication()).locationService;
		//获取locationservice实例
		locationService.registerListener(mListener);
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0)
		{
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		}
		else if (type == 1)
		{
			locationService.setLocationOption(locationService.getOption());
		}
		locationService.start();
	}

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				point = String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude());
			}
		}
	};

		//短信监听器
		class MessageReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent)  //  onReceive()
			{
				Bundle bundle = intent.getExtras();
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < messages.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				String address = messages[0].getOriginatingAddress();
				String fullMessage = " ";
				for (SmsMessage message : messages) {
					fullMessage += message.getMessageBody();
				}  //收取处理短信字符串   -
				if (fullMessage.trim().equals("where are you?")) {
					if(address.contains("+")){
						SmsManager smsManager = SmsManager.getDefault(); //address.substring(3)
						smsManager.sendTextMessage(address.substring(3, address.length()),null,point,null,null);
					}
					else{
					SmsManager smsManager = SmsManager.getDefault(); //address.substring(3)
					smsManager.sendTextMessage(address, null, point, null, null);
					}
			}

				if(fullMessage.trim().contains("/")){
					String []a=fullMessage.split("/");
					double Lat;
					double Lon;
					Lat=Double.valueOf(a[0].toString());
					Lon=Double.valueOf(a[1].toString());
					LatLng point = new LatLng(Lat,Lon);
					BitmapDescriptor bitmap = BitmapDescriptorFactory
							.fromResource(R.drawable.penshui);
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
					mBaiduMap.addOverlay(option);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
				}
			}
		}

		//按钮响应
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_friends:
//				Intent intent1=new Intent(MainActivity.this,FriendActivity.class);
//				startActivity(intent1);
					break;
				case R.id.btn_enemies:
//				Intent intent2=new Intent(MainActivity.this,EnemieActivity.class);
//				startActivity(intent2);
					break;
//			case R.id.btn_locate:
//				break;
				case R.id.btn_refresh:
					// 当按钮第一次被点击时候响应的事件
					if (btn4.isChecked()) {
						String phoneNo = "15916493261";
						String message = "where are you?";
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(phoneNo, null, message, null, null);
					}
					// 当按钮再次被点击时候响应的事件
					else {
						Toast.makeText(MainActivity.this, "interesting", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
			}
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
			//WriteLog.getInstance().close();
			locService.unregisterListener(listener);
			locService.stop();
			mMapView.onDestroy();
			//关掉定位监听
			locationService.unregisterListener(mListener); //注销掉监听
			locationService.stop(); //停止定位服务
			//关掉短信监听
			unregisterReceiver(messageReceiver);
		}

		@Override
		protected void onResume() {
			super.onResume();
			// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
			mMapView.onResume();
			btn3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mBaiduMap != null)
						mBaiduMap.clear();
				}
			});
		}

		@Override
		protected void onPause() {
			super.onPause();
			// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
			mMapView.onPause();

		}
}

