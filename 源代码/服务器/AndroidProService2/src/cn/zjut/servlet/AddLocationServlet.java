package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.LocationDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;

public class AddLocationServlet extends HttpServlet{
	private LocationDAO ld=new LocationDAO(); 
	private UserDAO ud=new UserDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		float lng=Float.parseFloat(request.getParameter("x"));
		float lat=Float.parseFloat(request.getParameter("y"));
		Long tl=Long.parseLong(request.getParameter("time"));
		Date time=new Date(tl);
		User u=ud.getUserByName(name);
		ld.addNewLocation(u, lng, lat, time);
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
