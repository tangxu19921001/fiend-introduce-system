package cn.zjut.act;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Protectable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.color;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.zjut.login.R;
import cn.zjut.util.GetLocationHelp;
import cn.zjut.util.LocationPoint;
import cn.zjut.util.ResizeLayout;
import cn.zjut.util.StaticPara;
import cn.zjut.util.ResizeLayout.OnResizeListener;

public class LBSMainActivity extends Activity {
	private static final int step = 3 * 60* 1000; // 30min
	private LocationManager locationManager;
	private int gpsstatus = 0,position = 0,status = 0;
	private String change = "yes";
	private LinearLayout lay1, lay2;
	private TextView tv;
	private EditText et;
	private ListView lv;
	private Button b1, b2, b3,b4;
	private Long datetime;
	private String name = "", info = "", re = "",str = "",friname ="",friinfo ="";
	private HashMap<String, Object> ma = new HashMap<String, Object>();
	private ArrayList<LocationPoint> al = new ArrayList<LocationPoint>();
	private SimpleAdapter listItemAdapter;
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;
	private InputHandler mHandler = new InputHandler();
	private ArrayList<HashMap<String, Object>> listItem;
	

	class InputHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE: {
				if (msg.arg1 == BIGGER) {
					et.clearFocus();
					updateinfo();
				} else {
					//findViewById(R.id.bottom_layout).setVisibility(View.GONE);
				}
			}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lbsmain);
		Display display = this.getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		Intent it = getIntent();
		re = it.getStringExtra("re");
		name = it.getStringExtra("name");
		info = it.getStringExtra("info");
		gpsstatus = it.getIntExtra("gpsstatus", 0);
		datetime = it.getLongExtra("datetime", 0l);
		ma = (HashMap) it.getSerializableExtra("ma");
		al = (ArrayList<LocationPoint>) it.getExtras().getSerializable("al");
		lay1 = (LinearLayout) findViewById(R.id.lay1);
		lay2 = (LinearLayout) findViewById(R.id.lay2);
		tv = (TextView) findViewById(R.id.tv);
		et = (EditText) findViewById(R.id.et);
		lv = (ListView) findViewById(R.id.listv);
		b1 = (Button) findViewById(R.id.opengps);
		b2 = (Button) findViewById(R.id.recommand);
		b3 = (Button) findViewById(R.id.exit);
		b4 = (Button) findViewById(R.id.search);
		b2.setText("好友推荐");
		b3.setText("退出");
		b4.setText("查询");
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lay1
				.getLayoutParams();
		linearParams.height = (int) (height * 0.17);
		linearParams = (LinearLayout.LayoutParams) lv.getLayoutParams();
		linearParams.height = (int) (height * 0.62);
		linearParams = (LinearLayout.LayoutParams) lay2.getLayoutParams();
		linearParams.height = (int) (height * 0.15);
		if (gpsstatus == 1) {
			b1.setText("轨迹记录（开启）");
			b1.setTextColor(android.graphics.Color.GREEN);
		} else {
			b1.setText("轨迹记录（关闭）");
			b1.setTextColor(android.graphics.Color.RED);
		}
		b1.setOnClickListener(new OnClickListener() { //跳转到记录轨迹
			// @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				GetLocationHelp gh = new GetLocationHelp();
				if (status == 0) {
					status = 1;
					startLoactionService();
					b1.setText("轨迹记录（开启）");
					b1.setTextColor(android.graphics.Color.GREEN);
				} else {
					status = 0;
					stopLocationService();
					b1.setText("轨迹记录（关闭）");
					b1.setTextColor(android.graphics.Color.RED);
				}
				Toast.makeText(LBSMainActivity.this, "Getlocation", Toast.LENGTH_LONG).show();
			}
		});
		b2.setOnClickListener(new OnClickListener() { //跳转到推荐好友
			// @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(LBSMainActivity.this, RecommandActivity.class);
				it.putExtra("gpsstatus", gpsstatus);
				it.putExtra("name", name);
				it.putExtra("info", info);
				it.putExtra("re", re);
				it.putExtra("datetime", datetime);
				it.putExtra("change", change);
				if (ma != null && al != null) {
					it.putExtra("ma", ma);
					it.putParcelableArrayListExtra("al", al);
				}
				startActivity(it);
				LBSMainActivity.this.finish();
			}
		});
		b3.setOnClickListener(new OnClickListener() { //真退出

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startMain = new Intent(Intent.ACTION_MAIN);//指定跳到系统桌面
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				System.exit(0);//
			}
		});
		b4.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(LBSMainActivity.this, "跳转", Toast.LENGTH_SHORT).show();
				Intent it = new Intent();
				re=updateFriends(name);
				if(re == null ){
					re = "";
				}
				it.setClass(LBSMainActivity.this, SearchActivity.class);
				it.putExtra("gpsstatus", gpsstatus);
				it.putExtra("name", name);
				it.putExtra("info", info);
				it.putExtra("re", re);
				it.putExtra("datetime", datetime);
				it.putExtra("change", change);
				if (ma != null && al != null) {
					it.putExtra("ma", ma);
					it.putParcelableArrayListExtra("al", al);
				}
				startActivity(it);
				LBSMainActivity.this.finish();
			}
		});
		// imv.setImageBitmap(bm);
		tv.setText(name);
		et.setText(info);
		ResizeLayout layout = (ResizeLayout) findViewById(R.id.lbslayout);
		layout.setOnResizeListener(new OnResizeListener() {

			public void OnResize(int w, int h, int oldw, int oldh) {
				int change = BIGGER;
				if (h < oldh) {
					change = SMALLER;
				}
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = change;
				mHandler.sendMessage(msg);

			}
		});
		listItem = new ArrayList<HashMap<String, Object>>();
		if (!re.equals("")) {
			try {
				JSONArray ja = new JSONArray(re);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("image", R.drawable.ic_launcher);
					map.put("fname", jo.getString("name"));
					map.put("finfo", jo.getString("info"));
					listItem.add(map);
				}
			} catch (Exception e) {
				Toast.makeText(LBSMainActivity.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}else
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", R.drawable.ic_launcher);
			map.put("fname", "暂无好友");
			map.put("finfo", "");
			listItem.add(map);
		}
		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.listitem,
				new String[] { "image", "fname", "finfo" }, new int[] {
						R.id.ItemImage, R.id.fname, R.id.finfo });
		lv.setAdapter(listItemAdapter);
	    lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				str = arg0.getItemAtPosition(arg2).toString();
				position = arg2;
				//Toast.makeText(LBSMainActivity.this, str, Toast.LENGTH_LONG).show();
				return false;
			}
	    	
		});
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				String[] str2 =str.split(",");
				String str3 = str2[1];
				String str4 = str2[2];
				friinfo = str4.substring(7, str4.length()-1);
				friname = str3.substring(7, str3.length());
				menu.setHeaderTitle("你的好友"+friname);
				MenuItem m1 = menu.add(0, 1, 0, "好友资料");
				MenuItem m2 = menu.add(0, 2, 0, "删除好友");
				
				m1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
//						Toast.makeText(LBSMainActivity.this, "111111111111", Toast.LENGTH_LONG).show();
						showDialog(1);
						return false;
					}
				
				});
			   m2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
//					Toast.makeText(LBSMainActivity.this, "222222", Toast.LENGTH_LONG).show();
					String Ustr = StaticPara.SERVICE_URL + "del?";
					StringBuffer buffer = new StringBuffer(Ustr);
					try{
						buffer.append("name11=").append(URLEncoder.encode(name,"utf-8"))
						.append("&name22=").append(URLEncoder.encode(friname,"utf-8"));
					}
					catch(Exception e1){
						
					}
					Ustr = buffer.toString();
					try{
						URL url = new URL(Ustr);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setRequestMethod("GET");
						if(true){
							//Toast.makeText(LBSMainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
							InputStream in = conn.getInputStream();
							InputStreamReader ins=new InputStreamReader(in,"utf-8");
							BufferedReader br=new BufferedReader(ins);
							String msg =br.readLine();
							Toast.makeText(LBSMainActivity.this, msg, Toast.LENGTH_LONG).show();
							in.close();
							listItem.remove(position);
							listItemAdapter.notifyDataSetChanged();
							lv.invalidate();
							if(listItem.size() == 0){
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("image", R.drawable.ic_launcher);
								map.put("fname", "暂无好友");
								map.put("finfo", "");
								listItem.add(map);
								listItemAdapter.notifyDataSetChanged();
								lv.invalidate();
								}
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					return false;
				}
			});
			}
			

		});
		
	}
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case 1:
		return buildDialog(LBSMainActivity.this);
		}
		return null;}
	private Dialog buildDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(friname);
		builder.setMessage("好友昵称："+friname+"\r\n"+"好友状态："+friinfo+"\r\n"+"其他：");
		builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		return builder.create();
		}
	public boolean onTouchEvent(MotionEvent event) {//修改好签名点空白处，签名修改完成
		et.clearFocus();
		updateinfo();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭输入法
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		return true;
	}
	public void updateinfo(){//签名更新
		String newinfo=et.getText().toString();
		if(!newinfo.equals(info)){
			String result = "";
			String urlStr = StaticPara.SERVICE_URL + "updateinfo?";
			StringBuffer buffer = new StringBuffer(urlStr);
			try {
				buffer.append("name=").append(URLEncoder.encode(name, "utf-8"))
				.append("&info=").append(URLEncoder.encode(newinfo, "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			// String query = "name=" + username + "&password=" + password;
			urlStr = buffer.toString();
			try {
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				if (true) {
					InputStream in = conn.getInputStream();
					
//					InputStreamReader ins = new InputStreamReader(in, "utf-8");
//					BufferedReader br = new BufferedReader(ins);
//					result = br.readLine();
//					Log.i("logre", result);
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			info=newinfo;
		}
	}
	public String updateFriends(String name){
		String result = "";
		String urlStr = StaticPara.SERVICE_URL + "update?";
		StringBuffer buffer = new StringBuffer(urlStr);
		try {
			buffer.append("name=").append(URLEncoder.encode(name, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// String query = "name=" + username + "&password=" + password;
		urlStr = buffer.toString();
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
				result = br.readLine();
				result = br.readLine();
				//Log.i("logre", result);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		///Toast.makeText(RecommandActivity.this, result,
				//Toast.LENGTH_LONG).show();
		return result;
	}
	public void startLoactionService() {//轨迹记录
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
		if(location != null)
		{
			uploadLocation(location,name);
		}
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
		locationManager.requestLocationUpdates(provider, step, 0,
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
