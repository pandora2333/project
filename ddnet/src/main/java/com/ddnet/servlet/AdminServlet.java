package com.ddnet.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddnet.util.DB;

import lombok.extern.slf4j.Slf4j;

/**
 * 管理后台,添加商品
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/admin",loadOnStartup=-1)
@Slf4j
public class AdminServlet extends HttpServlet {
	

	Connection conn;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().print("unsupported get method  to access");
	}
	{
		conn = DB.getConn();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(req.getSession().getAttribute("admin")==null) resp.sendRedirect("login2.jsp");
		String goods_name = req.getParameter("goods_name");
		String brand = req.getParameter("brand");
		String count = req.getParameter("count");
		String sale_money = req.getParameter("sale_money");
		try(Statement st = conn.createStatement();Statement st2 = conn.createStatement()){
			//先查询商品是否已存在
			if(!st2.executeQuery("select id from t_goods where goods_name ='"+goods_name+"'").next())
				st.execute("insert into t_goods(goods_name,brand,count,sale_money)  values('"+goods_name+"','"+brand+"',"+count+","+"'"+sale_money+"')");
			else
				req.setAttribute("add_error","商品已经存在，不能重复添加");
		}catch(Exception e){
			log.info("error in adminservlet:{}",e);
//			System.out.println(e);
		}
		req.getRequestDispatcher("/admin.jsp").forward(req, resp);;
	}

}
