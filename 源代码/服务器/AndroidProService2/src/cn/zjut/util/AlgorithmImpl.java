package cn.zjut.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import sun.print.resources.serviceui;

import cn.zjut.dao.LocationDAO;
import cn.zjut.dao.SemanticlocationDAO;
import cn.zjut.entity.Location;
import cn.zjut.entity.Semanticlocation;
import cn.zjut.entity.User;

public class AlgorithmImpl implements Algorithm {
	private LocationDAO ldao = new LocationDAO();
	private List<Location> user1;
	private List<Location> user2;
	private int startpoint1 = -1;
	private int startpoint2 = -1;
	private int us1 = 0;
	private int us2 = 0;
	private int max = 0;
    private ArrayList<String> pt = new ArrayList<String>();
	public int getStartpoint1() {
		return startpoint1;
	}

	public void setStartpoint1(int startpoint1) {
		this.startpoint1 = startpoint1;
	}

	public int getStartpoint2() {
		return startpoint2;
	}
	public ArrayList<String> getPlaceAndTime(){
//		org.json.JSONArray ja = new org.json.JSONArray();
//			System.out.println(pt.size()+"!!!!!!!!!!!!!!!!!!!!!1");
//	for(int ti = 0; ti<pt.size();ti++){
//			String placetime = pt.get(ti);
//			org.json.JSONObject jo = new org.json.JSONObject();
//			try{
//				jo.put("PT", placetime);
//			}catch(Exception e){
//				System.out.println("出错");
//			}
//			ja.put(jo);
//		}
//		pt.removeAll(pt);
//		System.out.println(pt.size()+"!!!!!!!!!!!2");
		return pt;
		
	}

	public void setStartpoint2(int startpoint2) {
		this.startpoint2 = startpoint2;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	public String getLocation(User u1, User u2){
		beforetocount(u1,u2);
		thePercentage(u1, u2);
		return ""+getStartpoint1()+getStartpoint2()+getMax();
	}
	
	@SuppressWarnings({ "null", "unchecked" })
	public int newalgorithm(User u1,User u2){  //函数获取权重
		int u1Number = user1.size();  
		int u2Number = user2.size();  //获取u1和u2的location长度
		int ii = 0 ;
		String PlaceTime ="";
		String place = "";
//		List<String> u1list = null;
		ArrayList<String> u1list = new ArrayList<String>();
//		List<String> u2list = null;
		ArrayList<String> u2list = new ArrayList<String>();
		SemanticlocationDAO slDAO = new SemanticlocationDAO();
		for(int i = 0; i< u1Number;i++){
			double abs_x = user1.get(i).getLongitude(); //获取经度
			double abs_y = user1.get(i).getLatitude();  //获取纬度
			@SuppressWarnings("deprecation")
			int time1 = user1.get(i).getTime().getHours(); //获取时间（小时）
			int T = timecompare(time1);  //将时间转化成1,2,3,4,5,6
			Semanticlocation sl =slDAO.getsl(abs_y, abs_x, T);  //查找数据库获取满足经度，纬度，时间的Semanticlocation对象
			System.out.println("测试");
			if(sl != null){ //如果SL不为空
				String time =String.valueOf(sl.getTime());  //拿到sl的时间并转化为string
				String semantictime = sl.getSemantic()+"!"+time;  //将sl的语义位置和时间组成字符串，以！号分隔
				if(SameCompare(u1list,semantictime)){  //查询u1list中是否有相同的字符串
					u1list.add(semantictime); //没有则将字符串添加到u1list中
				}
			}

		}
		System.out.println(u1list.size()+"@@@");
		for(int j = 0; j< u2Number;j++){          //u2和u1一样
			double abs_x = user2.get(j).getLongitude();
			double abs_y = user2.get(j).getLatitude();
			System.out.println(abs_y+"!!!!"+abs_x);
			@SuppressWarnings("deprecation")
			int time2 = user2.get(j).getTime().getHours();
			int T = timecompare(time2);
			Semanticlocation sl =slDAO.getsl(abs_y, abs_x, T);
			if(sl != null){
				String time =String.valueOf(sl.getTime());
				String semantictime = sl.getSemantic()+"!"+time;
				if(SameCompare(u2list,semantictime)){
					u2list.add(semantictime);
				}
				}
			

			}
		System.out.println(u2list.size()+"$$$");
		String str = "";
		if(u1list.size() != 0 && u2list.size() != 0){   //如果u1list和u2list不为空
			
		for(int k = 0; k<u1list.size();k++){  //这个函数取u1list的每一个和u2list的每一个进行比较
			String a = u1list.get(k);
			  System.out.println(a+"输出了");
			for(int l = 0;l<u2list.size();l++){
			    String b = u2list.get(l);
			    System.out.println(b+"输出");
			    if(a.equals(b)){     //如果a=b
			    	PlaceTime = b;
			    	ii = ii + slDAO.getweights(PlaceTime); //用这个函数获取权重并累加
			    	str = str +"#"+PlaceTime;  //将相同语义位置的字符串累加，并用#字符分隔
			    	
			    }
			    System.out.println(str+"语义位置串");
			    System.out.println(ii+"权重");
			 
			}
			  
		}
		 if(str != ""){
		    	pt.add(str);
		    }
		}
		return ii;
	}
	
	
	public Boolean SameCompare(ArrayList<String> a,String s){  //查询是否有相同字符串
		if(a.size() != 0){
			for(int i = 0;i<a.size();i++){
				String ss =a.get(i);
				if(s.equals(ss)){
					return false;
				}
			}
			return true;
		}
		return true;
	}
//    public int getweight(String s){
//    	int i = 0 ;
//    	if(s.equals("操场22点到8点")){
//    		i= 0;
//    	}
//    	else if(s.equals("操场8点到11点")){
//    		i= 4;
//    	}
//    	else if(s.equals("操场11点到13点")){
//    		i= 5;
//    	}
//    	else if(s.equals("操场13点到17点")){
//    		i= 4;
//    	}
//    	else if(s.equals("操场17点到19点")){
//    		i= 5;
//    	}
//    	else if(s.equals("操场19点到22点")){
//    		i= 2;
//    	}
//    	else if(s.equals("食堂22点到8点")){
//    		i= 0;
//    	}
//    	else if(s.equals("食堂8点到11点")){
//    		i= 2;
//    	}
//    	else if(s.equals("食堂11点到13点")){
//    		i= 2;
//    	}
//    	else if(s.equals("食堂13点到17点")){
//    		i= 2;
//    	}
//    	else if(s.equals("食堂17点到19点")){
//    		i= 2;
//    	}
//    	else if(s.equals("食堂19点到22点")){
//    		i= 5;
//    	}
//    	else if(s.equals("图书馆22点到8点")){
//    		i= 0;
//    	}
//    	else if(s.equals("图书馆8点到11点")){
//    		i= 5;
//    	}
//    	else if(s.equals("图书馆11点到13点")){
//    		i= 3;
//    	}
//    	else if(s.equals("图书馆13点到17点")){
//    		i= 5;
//    	}
//    	else if(s.equals("图书馆17点到19点")){
//    		i= 3;
//    	}
//    	else if(s.equals("图书馆19点到22点")){
//    		i= 4;
//    	}
//    	else if(s.equals("教室22点到8点")){
//    		i= 0;
//    	}
//    	else if(s.equals("教室8点到11点")){
//    		i= 3;
//    	}
//    	else if(s.equals("教室11点到13点")){
//    		i= 4;
//    	}
//    	else if(s.equals("教室13点到17点")){
//    		i= 3;
//    	}
//    	else if(s.equals("教室17点到19点")){
//    		i= 4;
//    	}
//    	else if(s.equals("教室19点到22点")){
//    		i= 3;
//    	}
//		return i;
//    	
//    }
	public int timecompare(int i){
        int timeQ =0;
		if(i>=22 && i<8){
			timeQ =1;
			return timeQ;
		}
		else if(i>=8 && i<11){
			timeQ =2;
			return timeQ;
		}
		else if(i>=11 && i<13){
			timeQ =3;
			return timeQ;
		}
		else if(i>=13 && i<17){
			timeQ =4;
			return timeQ;
		}
		else if(i>=17 && i<19){
			timeQ =5;
			return timeQ;
		}
		else if(i>=19 && i<22){
			timeQ =6;
			return timeQ;
		}
		return 0;
		
	}
	@SuppressWarnings("deprecation")
	public double thePercentage(User u1, User u2) {
		// user1=ldao.getLocationByUser(u1);
		// user2=ldao.getLocationByUser(u2);
		us1 = user1.size();
		us2 = user2.size();
//		System.out.println(user1.get(0).getTime()+"#####"+user1.get(0).getTime().getHours()+"####"+user1.get(0).getTime().getTime());
		Double result = 0.0;
		int flag = 0;
//		 System.out.println(user1.size()+"-"+user2.size());
		for (int i = 0; i < us1; i++) {
			if (flag == 1) {
				break;
			} else {
				for (int j = 0; j < us2; j++) {
//					System.out.println(user1.get(0).getTime().getHours()+"@@@@"+user1.get(0).getLatitude()+"@@@@"+user1.get(0).getLongitude());
					double abs_x = Math.abs(user1.get(i).getLongitude()
							- user2.get(j).getLongitude());
					double abs_y = Math.abs(user1.get(i).getLatitude()
							- user2.get(j).getLatitude());
//					 System.out.println(aaa+"aaaaaaaaaaaaaaaaaaa"+abs_x+"x|y"+abs_y);
					if (abs_x <= 25.0 && abs_y <= 25.0 && i != us1 - 1
							&& j != us2 - 1) {
						startpoint1 = i;
						startpoint2 = j;
						flag = 1;
						break;
					}
				}
			}
		}
		if (startpoint1 == -1 && startpoint2 == -1) {
			result = 0.0;
		} else {
			if (us1 - startpoint1 <= us2 - startpoint2) {
				max = us1 - startpoint1;
			} else {
				max = us2 - startpoint2;
			}
			if (max >= 10) {
				max = 10;
			}
			result = theAlgorithm(startpoint1, startpoint2, max);
			// System.out.println(startpoint1 + "|" + startpoint2
			// + "|" + max);
		}

		 //System.out.println("|"+startpoint1+"|"+startpoint2);
		return result;
	}

	// 算法
	public double theAlgorithm(int startp1, int startp2, int tmax) {
		Location st1 = user1.get(startpoint1);
		Location st2 = user2.get(startpoint2);
		double a = 0.0, b = 0.0, c = 0.0;
		double result = 0.0;
		for (int i = 0; i < max; i++) {
			Location temp1 = user1.get(startp1 + i);
			Location temp2 = user2.get(startp2 + i);
			user1.get(startp1 + i).setLongitude(
					temp1.getLongitude() - st1.getLongitude());
			user1.get(startp1 + i).setLatitude(
					temp1.getLatitude() - st1.getLatitude());
			user2.get(startp2 + i).setLongitude(
					temp2.getLongitude() - st2.getLongitude());
			user2.get(startp2 + i).setLatitude(
					temp2.getLatitude() - st2.getLatitude());
		}
		for (int i = 0; i < max; i++) {
			float x1, x2, y1, y2;
			x1 = user1.get(startp1 + i).getLongitude();
			y1 = user1.get(startp1 + i).getLatitude();
			x2 = user2.get(startp2 + i).getLongitude();
			y2 = user2.get(startp2 + i).getLatitude();
			c += Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
			a += Math.pow(x1, 2) + Math.pow(y1, 2);
			b += Math.pow(x2, 2) + Math.pow(y2, 2);
		}
		// System.out.println(a+"|"+b+"|"+c);
		result = 1 - (c / (a + b));
		return result;
	}

	// 对比前校验
	public int beforetocount(User u1, User u2) {
		user1 = ldao.getLocationByUser(u1);
		user2 = ldao.getLocationByUser(u2);
		startpoint1 = -1;
		startpoint2 = -1;
		us1 = 0;
		us2 = 0;
		max = 0;
//		 System.out.println(user1.size()+"-"+user2.size());
		if (user1.size() <= 2 || user2.size() <= 2) {//记录小于等于两条则不取
			return 0;
		}
		return 1;
	}
}
