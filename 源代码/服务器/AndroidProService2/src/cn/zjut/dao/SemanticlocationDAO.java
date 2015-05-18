package cn.zjut.dao;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.Session;

import antlr.collections.List;

import com.sun.org.apache.regexp.internal.recompile;

import sun.util.logging.resources.logging;

import cn.zjut.entity.Relationship;
import cn.zjut.entity.Semanticlocation;
import cn.zjut.entity.User;
import cn.zjut.util.HibernateSessionFactory;

public class SemanticlocationDAO {  
   public Semanticlocation getsl(double x,double y,int t){
	   System.out.println(x+"!"+y+"!"+t+"测试参数");
	   Session s = HibernateSessionFactory.getSession();
	   s.beginTransaction();
	   System.out.print("1111!!!");
	   String sql="select sl from Semanticlocation sl where sl.maxlatitude>='"+x+"'  and sl.minlatitude<='"+x+"' and sl.maxlongitude>='"+y+"' and sl.minlongitude<='"+y+"' and sl.time='"+t+"'";
//	   and sl.Minlatitude<='"+M+"' and sl.Maxlongitude>='"+l+"' and sl.Minlongitude<='"+l+"' and sl.time='"+time+"'
	   Query q  = s.createQuery(sql);
	   Semanticlocation sl;
	   java.util.List list=q.list();
	   if(list.size()!=0){
			Iterator it=list.iterator();
			sl=(Semanticlocation)it.next();
			System.out.print("测试2");
		}
		else{
			sl=null;
		}
		s.close();
		return sl;
	   
   }
   public Semanticlocation text(){
	  double x =  120.03099822998047;
	 double y =  30.227100372314453;
	 int t = 1;
	   Session s = HibernateSessionFactory.getSession();
	   s.beginTransaction();
	   System.out.print("1111!!!");
	   String sql="select sl from Semanticlocation sl where sl.maxlatitude>='"+y+"'  and sl.minlatitude<='"+y+"' and sl.maxlongitude>='"+x+"' and sl.minlongitude<='"+x+"' and sl.time='"+t+"'";
//	   and sl.Minlatitude<='"+M+"' and sl.Maxlongitude>='"+l+"' and sl.Minlongitude<='"+l+"' and sl.time='"+time+"'
	   System.out.print("2222!!!");
	   Query q  = s.createQuery(sql);
	   System.out.print("!!!");
	   Semanticlocation sl;
	   System.out.print("3333!!!");
	   java.util.List list=q.list();
	   if(list.size()!=0){
			Iterator it=list.iterator();
			sl=(Semanticlocation)it.next();
			System.out.print("测试2");
		}
		else{
			sl=null;
		}
		s.close();
		return sl;
	   
   }
   public int getweights(String pt){  //获取权重
	   int weights =0;
	   String[] str2 =pt.split("!");
	   Session s = HibernateSessionFactory.getSession();
	   s.beginTransaction();
	   System.out.print("1111!!!");
	   String sql="select sl from Semanticlocation sl where sl.semantic='"+str2[0]+"' and sl.time='"+str2[1]+"'";
//	   and sl.Minlatitude<='"+M+"' and sl.Maxlongitude>='"+l+"' and sl.Minlongitude<='"+l+"' and sl.time='"+time+"'
	   System.out.print("2222!!!");
	   Query q  = s.createQuery(sql);
	   System.out.print("!!!");
	   Semanticlocation sl;
	   System.out.print("3333!!!");
	   java.util.List list=q.list();
	   if(list.size()!=0){
			Iterator it=list.iterator();
			sl=(Semanticlocation)it.next();
			weights = sl.getWeight();
			System.out.print("!!!");
		}
		
		s.close();
		return weights;
	   
   }


}
