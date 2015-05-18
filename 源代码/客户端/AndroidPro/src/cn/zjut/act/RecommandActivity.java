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

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.zjut.login.R;
import cn.zjut.util.ItemAdapter;
import cn.zjut.util.LocationPoint;
import cn.zjut.util.StaticPara;

public class RecommandActivity extends Activity {
	private Button reback;
	private TextView reflist;
	private ListView lv;
	private Long datetime;
	private String name = "", info = "", re = "",change = "no";
	private int gpsstatus = 0;
	private HashMap<String, Object> ma = new HashMap<String, Object>();
	private ArrayList<LocationPoint> al = new ArrayList<LocationPoint>();
	@SuppressWarnings("unused")
	private SimpleAdapter listItemAdapter;
	private String findList;
	private ArrayList<HashMap<String, Object>> listItem;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommand);
		reback = (Button) findViewById(R.id.reback);
		reflist = (TextView) findViewById(R.id.reflist);
		lv = (ListView) findViewById(R.id.listv2);
		Intent it = getIntent();
		re = it.getStringExtra("re");
		name = it.getStringExtra("name");
		info = it.getStringExtra("info"); 
		change = it.getStringExtra("change");
		gpsstatus = it.getIntExtra("gpsstatus", 0);
		datetime = it.getLongExtra("datetime", 0l);
		ma = (HashMap) it.getSerializableExtra("ma");
		al = (ArrayList<LocationPoint>) it.getExtras().getSerializable("al");
		listItem = new ArrayList<HashMap<String, Object>>();
		findList = findFriends(name);//获取推荐好友
		if (!findList.equals("")) {	//创建好友推荐列表
			try {
				JSONArray ja = new JSONArray(findList);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("image", R.drawable.ic_launcher);
					map.put("fname", jo.getString("name"));
					map.put("finfo", jo.getString("info"));
					int score = Integer.parseInt(jo.getString("score"));
					String star = "";
					if(score>=10 && score <12){
						star = "★★";
					}
					else if(score>=12 && score <15){
						star = "★★★";
					}
					else if(score>=15 && score <18){
						star = "★★★★";
					}
					else if(score>=18 ){
						star = "★★★★★";
					}
					map.put("score",star);
					map.put("pt", jo.getString("pt"));
					listItem.add(map);
				}
				if(listItem.size()==0){
					reflist.setText("暂无推荐好友");
				}
				else{
					reflist.setText("可能认识的人");
				}
			} catch (Exception e) {
				Toast.makeText(RecommandActivity.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
		final ItemAdapter ba=new ItemAdapter(this,listItem,name);
		lv.setAdapter(ba);//好友推荐列表适配器
		reback.setOnClickListener(new OnClickListener() {//返回好友列表并更新
			// @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
//				if(ba.getchange()==1 ){
					re=updateFriends(name);
					if(re == null ){
						re = "";
					}
//				}
				it.setClass(RecommandActivity.this, LBSMainActivity.class);
				it.putExtra("gpsstatus", gpsstatus);
				it.putExtra("name", name);
				it.putExtra("info", info);
				it.putExtra("re", re);
				it.putExtra("datetime", datetime);
				if (ma != null && al != null) {
					it.putExtra("ma", ma);
					it.putParcelableArrayListExtra("al", al);
				}
				startActivity(it);
				RecommandActivity.this.finish();
			}
		});
	}
	public String findFriends(String name) {//获得推荐好友列表
		String result = "";
		String urlStr = StaticPara.SERVICE_URL + "find?";
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
}
