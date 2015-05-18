package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

import cn.zjut.dao.RelationshipDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.Relationship;
import cn.zjut.entity.User;

public class DelFriendsServlet extends HttpServlet{
	UserDAO ud=new UserDAO();
	RelationshipDAO rd=new RelationshipDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name11=new String(request.getParameter("name11").getBytes("iso-8859-1"),"utf-8");
		String name22=new String(request.getParameter("name22").getBytes("iso-8859-1"),"utf-8");
		User u1=ud.getUserByName(name11);
		User u2=ud.getUserByName(name22);
		System.out.println(name11+name22);
//		Relationship r = new Relationship();
//		r.setUser(u1);
//		r.setUser2(u2.getId());
		rd.DelRelationship(u1,u2);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String msg="已删除";
		out.print(msg);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		
	}
}
