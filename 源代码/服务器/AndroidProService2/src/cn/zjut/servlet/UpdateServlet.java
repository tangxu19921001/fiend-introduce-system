package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;

public class UpdateServlet extends HttpServlet{
	UserDAO userDAO=new UserDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		User u=userDAO.getUserByName(name);
		String msg=userDAO.getFriends(u);
		out.print(msg);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		
	}
}
