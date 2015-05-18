package cn.zjut.act;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.zjut.login.R;
import cn.zjut.util.StaticPara;

public class AndroidProActivity extends Activity {
	private Button login,register,exit;
	private EditText na,pa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		register=(Button)findViewById(R.id.rebutton);
		login = (Button)findViewById(R.id.lobutton);
		exit = (Button)findViewById(R.id.exbutton);
		register.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent();
				it.setClass(AndroidProActivity.this, RegisterActivity.class);
				startActivity(it);
			}
		});
			
		exit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startMain = new Intent(Intent.ACTION_MAIN);//指定跳到系统桌面
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				System.exit(0);
			}
		});
		login.setOnClickListener(onClickListenr);
	}
	private View.OnClickListener onClickListenr = new View.OnClickListener() {
		public void onClick(View v) {
			//login("abc", "abcd");
			na=(EditText)findViewById(R.id.nameip);
			pa=(EditText)findViewById(R.id.passwordip);
			if(!na.getText().toString().equals("")&&!pa.getText().toString().equals("")){
				login(na.getText().toString(),pa.getText().toString());
			}
			else{
				Toast.makeText(AndroidProActivity.this, "请输入用户们或密码", Toast.LENGTH_LONG).show();
			}
		}
	};

	private void login(String username, String password) {
		String urlStr =StaticPara.SERVICE_URL+"login?";
		StringBuffer buffer=new StringBuffer(urlStr);
		try {
			buffer.append("name=").append(URLEncoder.encode(username,"utf-8")).append("&password=").append(password);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//String query = "name=" + username + "&password=" + password;
		urlStr=buffer.toString();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			if (true) {
				InputStream in = conn.getInputStream();
				InputStreamReader ins=new InputStreamReader(in,"utf-8");
				BufferedReader br=new BufferedReader(ins);
				String msg =br.readLine();
				Toast.makeText(AndroidProActivity.this, msg, Toast.LENGTH_LONG).show();
				if(!msg.equals("用户名或密码错误")){
				    String info=br.readLine();
					String arr=br.readLine();
					String re="";
					/*if(arr!=null){
						Toast.makeText(AndroidProActivity.this, arr, Toast.LENGTH_LONG).show();
						JSONArray ja=new JSONArray(arr);
						for(int i=0;i<ja.length();i++){
							JSONObject jo=ja.getJSONObject(i);
							re+=jo.toString();
						}
					}*/
					if(arr!=null){
						re=arr;
					}
					Intent it=new Intent();
					it.setClass(AndroidProActivity.this, LBSMainActivity.class);
					it.putExtra("re", re);
					it.putExtra("name", username);
					it.putExtra("info", info);
					startActivity(it);
				}
				in.close();
			}
		} catch (Exception e) {
			Toast.makeText(AndroidProActivity.this, e.getMessage()+"123", Toast.LENGTH_LONG).show();
		//	Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT);
		}
	}
}