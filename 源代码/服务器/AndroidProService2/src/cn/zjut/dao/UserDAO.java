package cn.zjut.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Source;

import org.hibernate.Query;
import org.hibernate.Session;
import cn.zjut.entity.Relationship;
import cn.zjut.entity.User;
import cn.zjut.util.AlgorithmImpl;
import cn.zjut.util.HibernateSessionFactory;
import cn.zjut.util.ToJson;
import cn.zjut.util.ToJsonImpl;
public class UserDAO {
	// private User u=new User();
	public ToJson tj=new ToJsonImpl();
	//注册用户
	public int addUser(String name,String password){		
		if(ifexist(name)==true){
			Session s=HibernateSessionFactory.getSession();
			try{
				s.beginTransaction();
				User u=new User();
				u.setName(name);
				u.setPassword(password);
				s.save(u);
				s.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				return -1;//出错
			}finally{
				HibernateSessionFactory.closeSession();
			}
			return 1;//注册成功
		}
		else{
			return 0;//已存在用户
		}
		
	}
	//判断用户是否已存在
	public boolean ifexist(String name){
		User u=getUserByName(name);
		if(u==null){
			return true;//不存在
		}else{
			return false;//已存在
		}
	}
	//通过用户名获取用户数据
	public User getUserByName(String name){
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql="select u from User u where u.name='"+name+"'";
		Query q=s.createQuery(sql);
		User u;
		List list=q.list();
		if(list.size()!=0){
			Iterator it=list.iterator();
			u=(User)it.next();
		}
		else{
			u=null;
		}
		s.close();
		return u;
	}
	//通过ID获取用户数据
	public User getUserByID(int id){
		Session s=HibernateSessionFactory.getSession();
		s.beginTransaction();
		User u=(User)s.get(User.class, id);
		s.close();
		return u;
	}
	//得到所有用户
	public List<User> getAllUsers(){
		Session s=HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql="select u from User u";
		Query q=s.createQuery(sql);
		List<User> lu=q.list();
		s.close();
		return lu;
	}
	//得到用户的所有好友（JSON）
	public String getFriends(User u) {
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql = "select r from Relationship r where r.user=?";
		Query q = s.createQuery(sql);
		q.setParameter(0, u);
		List list = q.list();
		//System.out.println(list.size()+"");
		if(list.size()!=0){
			List<User> result=new ArrayList<User>();
			int[] a=new int[list.size()];
			Iterator it=list.iterator();
			int i=0;
			while(it.hasNext()){
				Relationship rs=(Relationship)it.next();
				a[i]=rs.getUser2();
				i++;
			}
			s.close();
			for(int j=0;j<i;j++){
				User tempu=getUserByID(a[j]);
				result.add(tempu);
			}
			return "\n"+tj.toJson(1,result,null,null);
		}
		else{
			return "";
		}
	}
	//更新状态信息
	public void updateInfo(User u,String info){
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		u.setInfo(info);
		s.update(u);
		s.getTransaction().commit();
		s.close();
	}
	//得到用户的所有好友（User类）
	public List<User> getUserFriends(User u){
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		String sql = "select r from Relationship r where r.user=?";
		Query q = s.createQuery(sql);
		q.setParameter(0, u);
		List<Relationship> list = q.list();
		List<User> lu=new ArrayList<User>();
		s.close();
		for(int i=0;i<list.size();i++){
			User user=getUserByID(list.get(i).getUser2());
			lu.add(user);
		}
		return lu;
	}
	//获得用户的非好友
	public List<User> getUnFriends(User u){
		List<User> fri=getUserFriends(u);
		fri.add(u);
		List<User> alluser=getAllUsers();
		List<User> unfri=new ArrayList<User>();
		//System.out.println(alluser.size()+"|"+fri.size());
		for(int i=0;i<alluser.size();i++){
			int k=0;
			for(int j=0;j<fri.size();j++){
				if(fri!=null){
					if(alluser.get(i).getName().equals(fri.get(j).getName())){
						k=1;
					}
				}			
			}
			if(k==0){
				unfri.add(alluser.get(i));
			}
		}
		return unfri;
	}
	//获得推荐好友（JSON）
	public String getRecommandFriends(User u,List<User> lu){//u用户，lu用户的非好友
		ToJson tj=new ToJsonImpl();
		List<User> tre=new ArrayList<User>();
		List<Integer> l=new ArrayList<Integer>();
		List<String> ll = new ArrayList<String>();
		AlgorithmImpl ai=new AlgorithmImpl();
		for(int i=0;i<lu.size();i++){
			User fu=lu.get(i);
			if(ai.beforetocount(u, fu)==1){
				int score = ai.newalgorithm(u, fu);
				ll = ai.getPlaceAndTime();
				System.out.println("@@@"+score+"@@@");	
//				double tempr=ai.thePercentage(u, fu);
//				System.out.println("---"+tempr);
				if(score >= 10){
					tre.add(fu);
					l.add(score);
				}
//				System.out.println(tre.size()+"---------"+l.size());
				
			}
		}
		for(int i=0;i<l.size()-1;i++){//将结果排序
			for(int j=i+1;j<l.size();j++){
				User tempu;
				String pts;
				if(l.get(i)<l.get(j)){
					tempu=tre.get(j);
					pts = ll.get(j);
					int tl=l.get(j);
					l.set(j, l.get(i));
					l.set(i, tl);
					tre.set(j, tre.get(i));
					tre.set(i, tempu);
					ll.set(j, ll.get(i));
					ll.set(i, pts);
				}
			}
		}
		return tj.toJson(2, tre,l,ll);
	}
	//判断帐号密码的正确性
	public User doLogin(String name,String password){
		Session s = HibernateSessionFactory.getSession();
		s.beginTransaction();
		//System.out.println(name+"]]");
		String sql="select u from User u where u.name='"+name+"' and u.password='"+password+"'";
		Query q = s.createQuery(sql);
		List list=q.list();
		Iterator it=list.iterator();
		System.out.println(list.size());
		s.close();
		while(it.hasNext()){
			User u=(User)it.next();
			System.out.println(u.getName()+"|"+u.getPassword()+"|"+u.getId());
			return u;
			
		}
		return null;
	}
}
