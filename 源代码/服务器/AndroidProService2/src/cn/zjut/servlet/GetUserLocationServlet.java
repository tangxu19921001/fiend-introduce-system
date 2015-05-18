package cn.zjut.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zjut.dao.LocationDAO;
import cn.zjut.dao.RelationshipDAO;
import cn.zjut.dao.UserDAO;
import cn.zjut.entity.User;
import cn.zjut.util.AlgorithmImpl;

public class GetUserLocationServlet extends HttpServlet {
	UserDAO ud=new UserDAO();
	LocationDAO ld=new LocationDAO();
	RelationshipDAO rd=new RelationshipDAO();
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)throws ServletException,
			IOException{
		String name1=new String(request.getParameter("name1").getBytes("iso-8859-1"),"utf-8");
		String name2=new String(request.getParameter("name2").getBytes("iso-8859-1"),"utf-8");
		User u1=ud.getUserByName(name1);
		User u2=ud.getUserByName(name2);
		//System.out.println(name+"|"+lng+"|"+lat+"|"+time);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		AlgorithmImpl ai=new AlgorithmImpl();
		String msg=ld.getLocationPoint(u1,u2,ai.getLocation(u1, u2));
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
