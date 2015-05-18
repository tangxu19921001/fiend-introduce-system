package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;

public class RegisterServlet extends HttpServlet{
	UserDAO userDAO=new UserDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		String password=request.getParameter("password").trim();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		System.out.println(name+"||"+password);
		PrintWriter out=response.getWriter();
		String msg=null;
		int result=userDAO.addUser(name, password);
		if(result==1){
			msg="注册成功";
		}
		else if(result==0){
			msg="用户名已存在";
		}
		else{
			msg="注册失败";
		}
		out.print(msg);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		
	}
}
