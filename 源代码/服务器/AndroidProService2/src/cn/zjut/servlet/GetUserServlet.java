package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.RelationshipDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;

public class GetUserServlet extends HttpServlet{
	UserDAO ud=new UserDAO();
	RelationshipDAO rd=new RelationshipDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		User u = ud.getUserByName(name);
		String msg = "";
		if(u == null){
			msg ="查无此人";
		}else{
			msg ="用户名:"+ u.getName()+","+"用户签名:"+u.getInfo();
		}
		System.out.println("连接到后台了");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.print(msg);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		
	}
}
