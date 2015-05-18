package cn.zjut.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.zjut.entity.Location;
import cn.zjut.entity.User;
import cn.zjut.util.HibernateSessionFactory;
import cn.zjut.util.ToJson;
import cn.zjut.util.ToJsonImpl;

public class LocationDAO {
	public List<Location> getLocationByUser(User u){
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql = "select l from Location l where l.user=? order by time";
		Query q = s.createQuery(sql);
		q.setParameter(0, u);
		List<Location> list = q.list();
		s.close();
		return list;
	}
	//记录新的位置
	public void addNewLocation(User u,float lng,float lat,Date time){
		Session s = HibernateSessionFactory.getSession();
		try{
			s.beginTransaction();
			Location l=new Location();
			l.setLongitude(lng);
			l.setLatitude(lat);
			l.setTime(time);
			l.setTurn(1);
			l.setUser(u);
			s.save(l);
			s.getTransaction().commit();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			s.close();
		}	
	}
	//获得两个用户的位置轨迹（JSON）
	public String getLocationPoint(User u1,User u2,String ssm){
		int s1,s2,max;
		String result="";
		ToJson tj=new ToJsonImpl();;
		List<Location> l1,l2;
//		List<Location> l1 = new ArrayList<Location>();
//		List<Location> l2 = new ArrayList<Location>();
		List<Location>rel1=new ArrayList<Location>();
		List<Location>rel2=new ArrayList<Location>();
		s1=Integer.parseInt(ssm.substring(0, 1));
		s2=Integer.parseInt(ssm.substring(1, 2));
		max=Integer.parseInt(ssm.substring(2, 3));
		l1=getLocationByUser(u1);
		l2=getLocationByUser(u2);
		//System.out.println(l1.size()+"]]"+l2.size()+"==="+s1+s2+max);
		for(int i=s1;i<s1+max;i++){
			//rel1.add(l1.get(i));
			Location l=l1.get(i);
			rel1.add(l);
			//System.out.println(i+""+rel1.get(i));
		}
		for(int i=s2;i<s2+max;i++){
			rel2.add(l2.get(i));
		}
		result=tj.toLocationJson(rel1, rel2);
		//System.out.println(s1+"=="+s2+"=="+max);
		return result;
	}
}
