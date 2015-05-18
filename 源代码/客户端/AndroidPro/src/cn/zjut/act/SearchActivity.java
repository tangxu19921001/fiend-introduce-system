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

import cn.zjut.login.R;
import cn.zjut.login.R.layout;
import cn.zjut.login.R.menu;
import cn.zjut.util.LocationPoint;
import cn.zjut.util.StaticPara;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity {

	private Button searchButton,back;
	private EditText searchText;
	private int gpsstatus = 0;
	private Long datetime;
	private String name = "", info = "", re = "",s2="";
	private HashMap<String, Object> ma = new HashMap<String, Object>();
	private ArrayList<LocationPoint> al = new ArrayList<LocationPoint>();
	private ArrayList<String> addname = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Intent it = getIntent();
		re = it.getStringExtra("re");
		name = it.getStringExtra("name");
		info = it.getStringExtra("info"); 
		gpsstatus = it.getIntExtra("gpsstatus", 0);
		datetime = it.getLongExtra("datetime", 0l);
		ma = (HashMap) it.getSerializableExtra("ma");
		al = (ArrayList<LocationPoint>) it.getExtras().getSerializable("al");
		back = (Button) findViewById(R.id.back);
		searchButton = (Button) findViewById(R.id.searchbutton);
		searchButton.setText("查询");
		back.setText("返回");
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchText = (EditText) findViewById(R.id.searchtext);
				String str = searchText.getText().toString();
				if(str.equals("")){
					Toast.makeText(SearchActivity.this, "请输入", Toast.LENGTH_SHORT).show();
				}else if(str.equals(name)){
					Toast.makeText(SearchActivity.this, "请不要查询自己哦", Toast.LENGTH_SHORT).show();
				}
				else{
					try {
						JSONArray ja = new JSONArray(re);
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = ja.getJSONObject(i);
							String fn =jo.getString("name");
							if(str.equals(fn)){
								Toast.makeText(SearchActivity.this, "已添加该好友", Toast.LENGTH_SHORT).show();
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭输入法
								imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
								return;
							}
						}}
						catch (Exception e) {
						}
//					    Toast.makeText(SearchActivity.this, "查找", Toast.LENGTH_LONG).show();
//					    if(addname.size() != 0){
//					    for(int a = 0;a<addname.size();a++){
//					    	if(str.equals(addname.get(a))){
//					    		Toast.makeText(SearchActivity.this, "已添加该好友", Toast.LENGTH_SHORT).show();
//								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭输入法
//								imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
//								return;
//					    	}
//					    }}
					    s2 = str;
					    findbyname(str);
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭输入法
				imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				re=updateFriends(name);
				if(re == null ){
					re = "";
				}
				it.setClass(SearchActivity.this, LBSMainActivity.class);
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
				SearchActivity.this.finish();
			}
		});
	}
	public void findbyname(String s){
		String result = "";
		String urlStr = StaticPara.SERVICE_URL + "search?";
		StringBuffer buffer = new StringBuffer(urlStr);
		try {
			buffer.append("name=").append(URLEncoder.encode(s, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
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
		if(result.equals("查无此人")){
		Toast.makeText(SearchActivity.this, result,
				Toast.LENGTH_LONG).show();
		}else{
			
			String[] ss = result.split(",");
//			Toast.makeText(SearchActivity.this, a,
//					Toast.LENGTH_LONG).show();
		    AlertDialog.Builder a =	new AlertDialog.Builder(this);
		    a.setTitle("用户信息");
		    a.setItems(ss, null);
		    a.setPositiveButton("添加", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(SearchActivity.this, "OK", Toast.LENGTH_LONG).show();
					String urlStr = StaticPara.SERVICE_URL + "add?";
					StringBuffer buffer = new StringBuffer(urlStr);
					try {
						String name2= s2;
						buffer.append("name1=").append(URLEncoder.encode(name, "utf-8"))
						.append("&name2=").append(URLEncoder.encode(name2, "utf-8"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					urlStr = buffer.toString();
					try {
						URL url = new URL(urlStr);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setRequestMethod("GET");
						if (true) {
							String msg="";
							InputStream in = conn.getInputStream();
							InputStreamReader ins = new InputStreamReader(in, "utf-8");
							BufferedReader br = new BufferedReader(ins);
							msg = br.readLine();
							//Log.i("logre", result);
							addname.add(s2);
							re=updateFriends(name);
							if(re == null ){
								re = "";
							}
							in.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		    a.setNegativeButton("返回", null);
		    a.create();
		    a.show();
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

}
