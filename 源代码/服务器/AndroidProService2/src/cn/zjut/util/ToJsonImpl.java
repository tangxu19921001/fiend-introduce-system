package cn.zjut.util;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.zjut.entity.Location;
import cn.zjut.entity.User;

public class ToJsonImpl implements ToJson {
	public String toJson(int i, List<User> l,List<Integer> score,List<String> pt) {
		// TODO Auto-generated method stub
		JSONArray ja = new JSONArray();
		// String result=null;
		for (int j=0;j<l.size();j++) {
			User u=l.get(j);
			//System.out.println(""+u.getName());
			JSONObject jo = new JSONObject();
			try {
				jo.put("id", u.getId());
				jo.put("name", u.getName());
				jo.put("password", u.getPassword());
				if (u.getInfo() != null) {
					jo.put("info", u.getInfo());
				} else {
					jo.put("info", "");
				}
				if(i==2){
//					DecimalFormat df=new DecimalFormat("#.0");
					jo.put("score",score.get(j));//df.format(100*percent.get(j))
				}
				if(pt != null){
				jo.put("pt", pt.get(j));}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ja.put(jo);
		}
		System.out.println(ja.toString()+"jajajajajajajaaj");
		return ja.toString();
	}
	public String toLocationJson(List<Location> lo1, List<Location> lo2) {
		JSONArray ja = new JSONArray();
		for(int i=0;i<lo1.size();i++){
			Location l1=lo1.get(i);
			Location l2=lo2.get(i);
			JSONObject jo = new JSONObject();
			try {
				jo.put("x1", l1.getLongitude());
				jo.put("y1", l1.getLatitude());
				jo.put("x2", l2.getLongitude());
				jo.put("y2", l2.getLatitude());
			} catch (Exception e) {
				e.printStackTrace();
			}
			ja.put(jo);
		}
		return ja.toString();
	}
}
