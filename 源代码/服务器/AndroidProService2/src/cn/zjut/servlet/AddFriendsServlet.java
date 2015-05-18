package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.RelationshipDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;

public class AddFriendsServlet extends HttpServlet{
	UserDAO ud=new UserDAO();
	RelationshipDAO rd=new RelationshipDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name1=new String(request.getParameter("name1").getBytes("iso-8859-1"),"utf-8");
		String name2=new String(request.getParameter("name2").getBytes("iso-8859-1"),"utf-8");
		User u1=ud.getUserByName(name1);
		User u2=ud.getUserByName(name2);
		rd.addRelationship(u1, u2);
		//System.out.println(name+"|"+lng+"|"+lat+"|"+time);
		System.out.println("cccccccccccccccccccccccccccccccccccc");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String msg="OK";
		out.print(msg);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		
	}
}
