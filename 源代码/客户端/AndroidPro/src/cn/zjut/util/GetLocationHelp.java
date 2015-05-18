package cn.zjut.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GetLocationHelp extends Activity{
	private static final int step = 3 * 60* 1000; // 30min
	private LocationManager locationManager;
	private Long datetime;
	private String name = "";
	public void startLoactionService(String n) {//轨迹记录
		name = n;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceName);
		// String provider = LocationManager.GPS_PROVIDER;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置最大精度
		criteria.setAltitudeRequired(false);//不要求海拔信息
		criteria.setBearingRequired(false);//不要求方位信息
		criteria.setCostAllowed(true);//是否收费
		criteria.setPowerRequirement(Criteria.POWER_LOW);//电量要求
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		//if (preLocation == null) {
		uploadLocation(location,name);
	/*	} else {
			double abs_la = Math.abs(location.getLatitude()
					- preLocation.getLatitude());
			double abs_ln = Math.abs(location.getLongitude()
					- preLocation.getLongitude());
			
			 * if (preLocation.getLatitude() == location.getLatitude() &&
			 * preLocation.getLongitude() == location.getLongitude()) {
			 * Log.i("====", String.valueOf(preLocation)); } else {
			 * updateWithNewLocation(location); }
			 
			if (abs_la >= 0.001 || abs_ln >= 0.001) {
				updateWithNewLocation(location);
			}
		}*/
		locationManager.requestLocationUpdates(provider, step, 400,
				locationListener);//注册位置更新监听，最小时间间隔step，最小距离间隔400米
	}
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			uploadLocation(location,name);
		}

		public void onProviderDisabled(String provider) {
			uploadLocation(null, null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	public void stopLocationService() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	private void uploadLocation(Location l, String na) {
		String urlStr = StaticPara.SERVICE_URL + "upload?";
		StringBuffer buffer = new StringBuffer(urlStr);
		try {
			buffer.append("name=").append(URLEncoder.encode(na, "utf-8"))
					.append("&x=").append(l.getLongitude()).append("&y=")
					.append(l.getLatitude()).append("&time=").append(datetime);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// String query = "name=" + username + "&password=" + password;
		urlStr = buffer.toString();
		Log.i("info", urlStr);
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			if (true) {
				InputStream in = conn.getInputStream();
				InputStreamReader ins = new InputStreamReader(in, "utf-8");
				BufferedReader br = new BufferedReader(ins);
				String msg = br.readLine();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
