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

public class UpdateInfoServlet extends HttpServlet{
	UserDAO ud=new UserDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		String info=new String(request.getParameter("info").getBytes("iso-8859-1"),"utf-8");
		User u1=ud.getUserByName(name);
		ud.updateInfo(u1, info);
		//System.out.println(name+"|"+lng+"|"+lat+"|"+time);
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
