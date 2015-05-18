package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.SemanticlocationDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.Semanticlocation;
import cn.zjut.entity.User;

public class LoginServlet extends HttpServlet {
	UserDAO userDAO=new UserDAO();
	SemanticlocationDAO slDAO = new SemanticlocationDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		System.out.println("连接到后台");
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		String password=request.getParameter("password").trim();
		System.out.println(name+"_|"+password);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String msg=null;
		Semanticlocation sl = slDAO.text();
		System.out.println(sl.getSemantic()+sl.getWeight()+"###############");
		User result=userDAO.doLogin(name, password);
		if(result!=null){
			msg="登陆成功";
			if(result.getInfo()!=null){
				msg+="\n"+result.getInfo();
			}
			else{
				msg+="\n"+"";
			}
			msg+=userDAO.getFriends(result);
		}
		else{
			msg="用户名或密码错误";
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
