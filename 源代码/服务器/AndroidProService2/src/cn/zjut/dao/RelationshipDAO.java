package cn.zjut.dao;

import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.Session;

import antlr.collections.List;

import com.sun.org.apache.regexp.internal.recompile;

import sun.util.logging.resources.logging;

import cn.zjut.entity.Relationship;
import cn.zjut.entity.User;
import cn.zjut.util.HibernateSessionFactory;

public class RelationshipDAO {
	public void addRelationship(User u1, User u2) {
		Session s = HibernateSessionFactory.getSession();
		try {
			s.beginTransaction();
			Relationship r1 = new Relationship();
			Relationship r2 = new Relationship();
			r1.setUser(u1);
			r1.setUser2(u2.getId());
			r2.setUser(u2);
			r2.setUser2(u1.getId());
			s.save(r1);
			//s.delete(r1);
			s.save(r2);
			s.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	} 
   public void DelRelationship(User u1 , User u2){
	   Session s = HibernateSessionFactory.getSession();
	   try{
		   s.beginTransaction();
		   Relationship r = new Relationship();
		   r.setUser(u1);
		   r.setUser2(u2.getId());
		   Integer in = r.getUser2();
		   String sql = "select r from Relationship r where r.user=? and r.user2=?";
		   Query q = s.createQuery(sql);
		   q.setParameter(0, u1);
		   q.setParameter(1, in);
		   java.util.List<Relationship> list = q.list();
		   System.out.println(list.size());
		   System.out.println(list.get(0));
//		   Relationship r =(Relationship)s.get(Relationship.class, r.getId());
//		   s.get(r.getClass(), r.getId());
		   s.delete(list.get(0));
		   s.getTransaction().commit();
		   
	   }catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
   }
}
