package cn.zjut.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.zjut.act.LBSMainActivity;
import cn.zjut.login.R;

public class ItemAdapter extends BaseAdapter
{

	LayoutInflater inflater;
	Context context;
	String name;
	private List<HashMap<String,Object>> listItems;
	private int change=0;
	//
	private Paint p;// 声明画笔
	private Canvas canvasTemp;// 画布
	private Bitmap newb;
	private ArrayList<HashMap<String, Float>> jsonpoint;
	private String str ="";
	//
	public ItemAdapter(Context context,List<HashMap<String,Object>> listItems,String name){
		this.context=context;
		inflater=LayoutInflater.from(context);
		this.listItems = listItems; 
		this.name=name;
	}
	public int getchange(){
		return change;
	}
	public int getCount()
	{
		return listItems.size();
	}

	public Object getItem(int position)
	{
		return null;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final Holder holder;
		if(convertView!=null)
		{
			holder=(Holder) convertView.getTag();
		}else {
			holder=new Holder();
			convertView=inflater.inflate(R.layout.relistitem, null);
			holder.add=(Button) convertView.findViewById(R.id.addf);
			holder.look=(Button) convertView.findViewById(R.id.look);
			holder.iv=(ImageView) convertView.findViewById(R.id.ItemImage2);
			holder.name=(TextView)convertView.findViewById(R.id.fname2);
			holder.info=(TextView)convertView.findViewById(R.id.finfo2);
			holder.per=(TextView)convertView.findViewById(R.id.per);
			holder.name.setText((String)listItems.get(position).get("fname"));
			holder.info.setText((String)listItems.get(position).get("finfo"));
			holder.per.setText((String)listItems.get(position).get("score"));
			convertView.setTag(holder);
		}
		OnClickListener listener=new OnClickListener(){
			//@Override
			public void onClick(View v)
			{
				if(v==holder.add){
					Toast.makeText(context, listItems.get(position)+"|"+position+"add", Toast.LENGTH_SHORT).show();
					String urlStr = StaticPara.SERVICE_URL + "add?";
					StringBuffer buffer = new StringBuffer(urlStr);
					try {
						String name2=(String)listItems.get(position).get("fname");
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
							in.close();
							listItems.remove(position);
							notifyDataSetChanged();
							change=1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(v==holder.look){
					//Toast.makeText(context, listItems.get(position)+"|"+position+"add", Toast.LENGTH_SHORT).show();
					str = (String) listItems.get(position).get("pt");
					showDialogs(str);
//					showDialog(name,name2);
				}
					
			}
		};
		holder.add.setOnClickListener(listener);
		holder.look.setOnClickListener(listener);
		return convertView;
	}
	public void showDialogs(String s){
		String[] str2 =str.split("#");
		String[] str3 =new String[str2.length-1];
		String time="";
		String place="";
		int aa = str2.length;
		String a = String.valueOf(aa);
		for(int i = 1; i<str2.length;i++){
			place =comparePlace(str2[i]);
			time = compareTime(str2[i]);
			String xiangxi = "您和他在"+time+"同时在"+place;
			str3[i-1] = xiangxi;
		}
		new AlertDialog.Builder(context).setTitle("详细").setItems(str3, null).setNegativeButton("确定", null).show();
        	}
	public String comparePlace(String s){
		String[] str = s.split("!");
		s = str[0];
		return s;
		
	}
	public String compareTime(String s){
		if(s.contains("2")){
			s = "8点到11点";
		}
		else if(s.contains("3")){
			s = "11点到13点";
		}
		else if(s.contains("4")){
			s = "13点到17点";
		}
		else if(s.contains("5")){
			s = "17点到19点";
		}
		else if(s.contains("6")){
			s = "19点到22点";
		}
		else if(s.contains("1")){
			s = "22点到8点";
		}
		return s;
		
	}

	public String getLocationPoint(String name1,String name2){
		String result = "";
		String urlStr = StaticPara.SERVICE_URL + "get?";
		StringBuffer buffer = new StringBuffer(urlStr);
		try {
			buffer.append("name1=").append(URLEncoder.encode(name, "utf-8"))
			.append("&name2=").append(URLEncoder.encode(name2, "utf-8"));;
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
	class Holder{
		public Button add,look;
		public ImageView iv;
		public TextView name,info,per;
	}
}