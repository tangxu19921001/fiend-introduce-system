package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;
public class RecommandServlet extends HttpServlet{
	UserDAO ud=new UserDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		User u=ud.getUserByName(name);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String msg=ud.getRecommandFriends(u,ud.getUnFriends(u));
		System.out.println(msg);
		out.print(msg);
		out.flush();
		out.close();
		 
	}
}
