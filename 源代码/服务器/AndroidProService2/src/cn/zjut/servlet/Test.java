package cn.zjut.servlet;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.zjut.dao.UserDAO;
import cn.zjut.util.HibernateSessionFactory;

public class Test {
	public static void main(String args[]){
		UserDAO ud=new UserDAO();
		ud.doLogin("法官","abc");
		/*Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql="select u from User u where u.name='开'";
		Query q=s.createQuery(sql);
		List list=q.list();
		Iterator it=list.iterator();
		System.out.println(list.size());
		s.close();*/
	}
	
}
