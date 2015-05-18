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

public class RegisterActivity extends Activity {
	private Button regb, bab;
	private EditText rena, repa, repa2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		regb = (Button) findViewById(R.id.regb);
		bab = (Button) findViewById(R.id.bab);
		rena = (EditText) findViewById(R.id.rena);
		repa = (EditText) findViewById(R.id.repa);
		repa2 = (EditText) findViewById(R.id.repa2);
		regb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String na=rena.getText().toString();
				String pa=repa.getText().toString();
				String pa2=repa2.getText().toString();
				int result = StaticPara.doregister(na,pa,pa2); //检查输入是否为空
				if(result==1){
					String urlStr =StaticPara.SERVICE_URL+"register?";
					StringBuffer buffer=new StringBuffer(urlStr);
					try {
						buffer.append("name=").append(URLEncoder.encode(na,"utf-8")).append("&password=").append(pa);
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
							in.close();
							Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
							if(msg.equals("注册成功")){
								Intent it=new Intent();
								it.setClass(RegisterActivity.this, AndroidProActivity.class);
								startActivity(it);
								RegisterActivity.this.finish();
							}
							else{
								rena.setText("");
								repa.setText("");
								repa2.setText("");
							}
						}
					}catch(Exception e){
						Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
				else if(result==-1){
					Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
				}
				else if(result==0){
					Toast.makeText(RegisterActivity.this, "请输入完整", Toast.LENGTH_LONG).show();
				}
			}
		});
		bab.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent();
				it.setClass(RegisterActivity.this, AndroidProActivity.class);
				startActivity(it);
				RegisterActivity.this.finish();
			}
		});

	}
}
