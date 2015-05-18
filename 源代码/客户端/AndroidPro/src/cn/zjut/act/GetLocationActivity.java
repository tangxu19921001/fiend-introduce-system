package cn.zjut.act;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.zjut.login.R;
import cn.zjut.util.LocationPoint;
import cn.zjut.util.StaticPara;

public class GetLocationActivity extends Activity {
	private static final int step = 3 * 60* 1000; // 30min
	private LocationManager locationManager;
	private int status;
	private Long datetime;
	private Button btn, ba, zoom;
	private Location preLocation;
	private ImageView iv;
	private Bitmap newb;
	private TextView myLocationText;
	private Paint p;// 声明画笔
	private Canvas canvasTemp;// 画布
	private String name = "", info = "", re = "";
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private ArrayList<LocationPoint> al = new ArrayList<LocationPoint>();
	private Context context;
	// private HashMap<String, Object> lp = new HashMap<String, Object>();

	// private Resources r;
	/** Called when the activity is first created. */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getgps);
		Intent it = getIntent();
		status = it.getIntExtra("gpsstatus", 0);
		name = it.getStringExtra("name");
		info = it.getStringExtra("info");
		re = it.getStringExtra("re");
		context=this;
		al = (ArrayList<LocationPoint>) it.getExtras().getSerializable("al");
		map = (HashMap) it.getSerializableExtra("ma");
		datetime = it.getLongExtra("datetime", 0l);
		if (al != null) {
			Log.i("al", al.get(0).getX() + "|" + al.get(0).getY());
		} else {
			al = new ArrayList<LocationPoint>();
		}
		iv = (ImageView) findViewById(R.id.map);
		btn = (Button) findViewById(R.id.beginBtn);
		ba = (Button) findViewById(R.id.back);
		zoom = (Button) findViewById(R.id.zoom);
		myLocationText = (TextView) findViewById(R.id.result);
		// r = this.getResources();
		newb = Bitmap.createBitmap(360, 360, Config.ARGB_8888);
		canvasTemp = new Canvas(newb);
		canvasTemp.drawColor(Color.TRANSPARENT);
		p = new Paint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		p.setColor(Color.RED);
		p.setTypeface(font);
		p.setTextSize(22);
		canvasTemp.drawLine(0, 180, 360, 180, p);
		canvasTemp.drawLine(180, 0, 180, 360, p);
		p.setColor(Color.YELLOW);
		// canvasTemp.drawText("0", 180, 190, p);
		for (int i = 0; i <= 360; i += 60) {
			p.setTextSize(22);
			canvasTemp.drawPoint(i, 180, p);
			// canvasTemp.drawText((i-180)+"", i+5, 185, p);
			canvasTemp.drawPoint(180, i, p);
			p.setTextSize(11);
			if (i == 180) {
				canvasTemp.drawText("0", 185, 185, p);
			} else {
				canvasTemp.drawText((i - 180) + "", i, 185, p);
				canvasTemp.drawText((90 - i / 2) + "", 185, i, p);
			}

			// canvasTemp.drawText((90-i/2)+"", 185, i+5, p);
		}
		if (map != null) {
			preLocation = (Location) map.get("prel");
			// SimpleDateFormat dateFormatter = new SimpleDateFormat(
			// "yyyy-MM-dd HH:MM:SS",java.util.Locale.CHINA);
			Date da = new Date(datetime);
			String ss = "纬度:" + preLocation.getLatitude() + "\n经度:"
					+ preLocation.getLongitude() + "\n时间:" + da.toString();
			myLocationText.setText("当前位置:\n" + ss);
			LocationPoint locap, plocap = null;
			for (int i = 0; i < al.size(); i++) {
				
				locap = al.get(i);
				if (i == 0) {
					if (i == al.size() - 1) {
						p.setColor(Color.GREEN);
						p.setStrokeWidth((float) 2.5);
						canvasTemp.drawPoint(locap.getX(), locap.getY(), p);
					} else {
						p.setColor(Color.WHITE);
						p.setStrokeWidth((float) 2.5);
						canvasTemp.drawPoint(locap.getX(), locap.getY(), p);
					}
				} else {
					if (i == al.size() - 1) {
						p.setColor(Color.GREEN);
						p.setStrokeWidth((float) 2.5);
						canvasTemp.drawPoint(locap.getX(), locap.getY(), p);
						p.setStrokeWidth((float) 0.5);
						canvasTemp.drawLine(plocap.getX(), plocap.getY(),
								locap.getX(), locap.getY(), p);
					} else {
						p.setColor(Color.WHITE);
						p.setStrokeWidth((float) 2.5);
						canvasTemp.drawPoint(locap.getX(), locap.getY(), p);
						p.setColor(Color.GREEN);
						p.setStrokeWidth((float) 0.5);
						canvasTemp.drawLine(plocap.getX(), plocap.getY(),
								locap.getX(), locap.getY(), p);
					}
				}
				plocap = locap;
			}
		} else {
			map = new HashMap<String, Object>();
		}
		if (status == 1) {
			startLoactionService();
			btn.setText("轨迹记录（开启）");
			btn.setTextColor(android.graphics.Color.GREEN);
		} else {
			// stopLocationService();
			btn.setText("轨迹记录（关闭）");
			btn.setTextColor(android.graphics.Color.RED);
		}
		iv.setImageBitmap(newb);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (status == 0) {
					status = 1;
					startLoactionService();
					btn.setText("轨迹记录（开启）");
					btn.setTextColor(android.graphics.Color.GREEN);
				} else {
					status = 0;
					stopLocationService();
					btn.setText("轨迹记录（关闭）");
					btn.setTextColor(android.graphics.Color.RED);
				}
			}
		});
		ba.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(GetLocationActivity.this, LBSMainActivity.class);
				it.putExtra("name", name);
				it.putExtra("info", info);
				it.putExtra("re", re);
				it.putExtra("gpsstatus", status);
				it.putExtra("datetime", datetime);
				if (map.size() !=0 && al.size() != 0) {
				it.putExtra("ma", map);
				
				it.putParcelableArrayListExtra("al", al);}
				/*
				 * Bundle b=new Bundle(); b.put
				 */
				// it.putExtra("lp", lp);
				startActivity(it);
				GetLocationActivity.this.finish();
				// setVisible(false);
			}
		});
//		zoom.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				
//				if (al.size() >= 1) {
//					Bitmap pic ;
//					Paint paint;// 声明画笔
//					Canvas canvas;// 画布
//					pic = Bitmap.createBitmap(360, 360, Config.ARGB_8888);
//					canvas = new Canvas(pic);
//					canvas.drawColor(Color.TRANSPARENT);
//					paint = new Paint();
//					LayoutInflater inflater=LayoutInflater.from(context);
//					View layout = inflater.inflate(R.layout.lookinfo,null);
//					ImageView ima=(ImageView)layout.findViewById(R.id.themap);
//					TextView te1=(TextView)layout.findViewById(R.id.te1);
//					TextView te2=(TextView)layout.findViewById(R.id.te2);
//					ArrayList<LocationPoint> newal = new ArrayList<LocationPoint>();
//					for(int i=0;i<al.size();i++){
//						LocationPoint nlp=new LocationPoint();
//						nlp.setX(al.get(i).getX()-180);
//						nlp.setY(90-al.get(i).getY()/2);
//						newal.add(nlp);
//					}
//					int xrate = 1, yrate = 1;
//					//Toast.makeText(GetLocationActivity.this,"|"+newal.size(),Toast.LENGTH_LONG).show();
//					float xmin = newal.get(0).getX(), ymin = newal.get(0).getY();
//					float xmax = newal.get(0).getX(), ymax = newal.get(0).getY();
//					for (int i = 0; i < newal.size(); i++) {
//						if (newal.get(i).getX() < xmin) {
//							xmin = newal.get(i).getX();
//						}
//						if (newal.get(i).getX() > xmax) {
//							xmax = newal.get(i).getX();
//						}
//						if (newal.get(i).getY() < ymin) {
//							ymin = newal.get(i).getY();
//						}
//						if (newal.get(i).getY() > ymax) {
//							ymax = newal.get(i).getY();
//						}
//					}
//					float xf,yf;
//					xf=(float)Math.floor(xmin);
//					yf=(float)Math.floor(ymin);
//					xmin=(float) Math.floor((xmin-xf)/0.01);
//					ymin=(float) Math.floor((ymin-yf)/0.01);
//					xmin=xf+xmin/100;
//					ymin=yf+ymin/100;
//					xrate=(int)Math.ceil((xmax-xmin)/0.045);
//					yrate=(int)Math.ceil((ymax-ymin)/0.045);
//					Log.i("abc", "x"+xmin+"x"+xmax+"y"+ymin+"y"+ymax+"xr"+xrate+"yr"+yrate);
//					if(xrate>=4000||yrate>=2000){
//						
//						String familyName = "宋体";
//						Typeface font = Typeface.create(familyName, Typeface.BOLD);
//						paint.setColor(Color.RED);                                            
//						paint.setTypeface(font);
//						paint.setTextSize(22);
//						canvas.drawLine(0, 180, 360, 180, paint);
//						canvas.drawLine(180, 0, 180, 360, paint);
//						paint.setColor(Color.YELLOW);
//						// canvasTemp.drawText("0", 180, 190, p);
//						for (int i = 0; i <= 360; i += 60) {
//							paint.setTextSize(22);
//							canvas.drawPoint(i, 180, paint);
//							// canvasTemp.drawText((i-180)+"", i+5, 185, p);
//							canvas.drawPoint(180, i, paint);
//							paint.setTextSize(11);
//							if (i == 180) {
//								canvas.drawText("0", 185, 185, paint);
//							} else {
//								canvas.drawText((i - 180) + "", i, 185, paint);
//								canvas.drawText((90 - i / 2) + "", 185, i, paint);
//							}
//
//							// canvasTemp.drawText((90-i/2)+"", 185, i+5, p);
//						}
//						LocationPoint locap, plocap = null;
//						for(int i=0;i<al.size();i++){
//							
//							locap = al.get(i);
//							if (i == 0) {
//								if (i == al.size() - 1) {
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(locap.getX(), locap.getY(), paint);
//								} else {
//									paint.setColor(Color.WHITE);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(locap.getX(), locap.getY(), paint);
//								}
//							} else {
//								if (i == al.size() - 1) {
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(locap.getX(), locap.getY(), paint);
//									paint.setStrokeWidth((float) 0.5);
//									canvas.drawLine(plocap.getX(), plocap.getY(),
//											locap.getX(), locap.getY(), paint);
//								} else {
//									paint.setColor(Color.WHITE);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(locap.getX(), locap.getY(), paint);
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 0.5);
//									canvas.drawLine(plocap.getX(), plocap.getY(),
//											locap.getX(), locap.getY(), paint);
//								}
//							}
//							plocap = locap;
//							
//						}
//					}
//					else{
//						float xff=(float) (0.045*xrate);
//						float yff=(float) (0.045*yrate);
//						te1.setText("X轴"+xmin+"~"+String.format("%.3f", xmin+xff));
//						te2.setText("Y轴"+ymin+"~"+String.format("%.3f", ymin+yff));
//						String familyName = "宋体";
//						Typeface font = Typeface.create(familyName, Typeface.BOLD);
//						paint.setColor(Color.RED);
//						paint.setTypeface(font);
//						paint.setTextSize(22);
//						canvas.drawLine(0, 0, 0, 360, paint);
//						canvas.drawLine(0, 359, 360, 359, paint);
//						for(int i=0;i<=360;i+=120){
//							paint.setColor(Color.YELLOW);
//							paint.setStrokeWidth((float) 2.5);
//							canvas.drawPoint(i, 360, paint);
//							canvas.drawPoint(0, i, paint);
//						}
//						paint.setStrokeWidth((float) 0.5);
//						paint.setTextSize(11);
//						canvas.drawText(""+String.format("%.3f", xmin+xff), 320, 350, paint);
//						canvas.drawText(""+String.format("%.3f", xmin+xff*2/3), 215, 350, paint);
//						canvas.drawText(""+String.format("%.3f", xmin+xff/3), 85, 350, paint);
//						canvas.drawText(""+String.format("%.3f", ymin+yff), 10, 10, paint);
//						canvas.drawText(""+String.format("%.3f", ymin+yff*2/3), 10, 130, paint);
//						canvas.drawText(""+String.format("%.3f", ymin+yff/3), 10, 240, paint);
//						paint.setTextSize(22);
//						LocationPoint locap, plocap = null;
//						for(int i=0;i<newal.size();i++){
//							
//							locap = newal.get(i);
//							float tx=(float) ((locap.getX()-xmin)*360/(0.045*xrate));
//							float ty=(float) ((locap.getY()-ymin)*360/(0.045*yrate));
//							Log.i("www",(locap.getX()-xmin)+"|"+(locap.getY()-ymin)+"xy"+ tx+"|"+ty);
//							if (i == 0) {
//								if (i == newal.size() - 1) {
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(tx, 360-ty, paint);
//								} else {
//									paint.setColor(Color.WHITE);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(tx, 360-ty, paint);
//								}
//							}else {
//								//Toast.makeText(GetLocationActivity.this,"|"+plocap.getX()+"|"+plocap.getY(),Toast.LENGTH_LONG).show();
//								float ptx=(float) ((plocap.getX()-xmin)*360/(0.045*xrate));
//								float pty=(float) ((plocap.getY()-ymin)*360/(0.045*yrate));
//								//Toast.makeText(GetLocationActivity.this,ptx+"|"+pty+"|"+plocap.getX()+"|"+plocap.getY(),Toast.LENGTH_LONG).show();
//								if (i == newal.size() - 1) {
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(tx, 360-ty, paint);
//									paint.setStrokeWidth((float) 0.5);
//									canvas.drawLine(ptx,360-pty,
//											tx, 360-ty, paint);
//								} else {
//									
//									paint.setColor(Color.WHITE);
//									paint.setStrokeWidth((float) 2.5);
//									canvas.drawPoint(tx, 360-ty, paint);
//									paint.setColor(Color.GREEN);
//									paint.setStrokeWidth((float) 0.5);
//									canvas.drawLine(ptx, 360-pty,
//											tx, 360-ty, paint);
//								}
//							}
//							plocap = locap;
//							
//						}
//					}
//					ima.setImageBitmap(pic);
//					new AlertDialog.Builder(context).setTitle("放大图").setView(layout)
//				     .setPositiveButton("确定", null).show();
//				}
//				else{
//					Toast.makeText(GetLocationActivity.this,"请先记录数据",Toast.LENGTH_LONG).show();
//				}
//
//			}
//		}
//		);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Toast.makeText(GetLocationActivity.this,"xxxxxx",Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPause() {
		super.onPause();
		// Toast.makeText(GetLocationActivity.this,"YYYYYY",Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRestart() {
		super.onRestart();
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
		updateWithNewLocation(location);
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

	public void stopLocationService() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		map.clear();
		String latLongString = null;
		LocationPoint lpo = new LocationPoint();
		// map.clear();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:MM:SS", java.util.Locale.CHINA);
		Date date = new Date();
		Log.i("date", date.toString());
		datetime = date.getTime();
		// String sdate = dateFormatter.format(date);
		if (location != null) {
			if (preLocation != null) {
				double prelat = preLocation.getLatitude();
				double prelng = preLocation.getLongitude();
				float px = (float) (prelng + 180);
				float py = (float) (2 * (90 - prelat));
				p.setColor(Color.WHITE);
				canvasTemp.drawPoint(px, py, p);
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				float x = (float) (lng + 180);
				float y = (float) (2 * (90 - lat));
				p.setColor(Color.GREEN);
				p.setStrokeWidth((float) 2.5);
				canvasTemp.drawPoint(x, y, p);
				p.setStrokeWidth((float) 0.5);
				canvasTemp.drawLine(px, py, x, y, p);
				latLongString = "纬度:" + lat + "\n经度:" + lng + "\n时间:"
						+ date.toString();
				lpo.setX(x);
				lpo.setY(y);
				al.add(lpo);
				// l.add(object)

			} else {
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				float x = (float) (lng + 180);
				float y = (float) (2 * (90 - lat));
				p.setColor(Color.GREEN);
				p.setStrokeWidth((float) 2.5);
				canvasTemp.drawPoint(x, y, p);
				latLongString = "纬度:" + lat + "\n" + "经度:" + lng + "\n时间:"
						+ date.toString();
				lpo.setX(x);
				lpo.setY(y);
				al.add(lpo);
			}
			uploadLocation(location, name);
			preLocation = location;
			map.put("prel", preLocation);
		} else {
			latLongString = "无法获取地理信息";
		}
		myLocationText.setText("当前位置:\n" + latLongString);
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
