package com.ddnet.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import com.ddnet.util.DB;
import com.mysql.cj.util.StringUtils;
/**
 * 管理后台,修改商品
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/update",loadOnStartup=-1)
@Slf4j
public class UpdateServlet extends HttpServlet {

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
		StringBuilder sb = new StringBuilder("set");
		if(!StringUtils.isNullOrEmpty(goods_name)){
			if(!StringUtils.isNullOrEmpty(brand))
				sb.append(" brand='"+brand+"'");
			if(!StringUtils.isNullOrEmpty(count)){
				if(sb.indexOf("brand")>0)
					sb.append(",");
				sb.append(" count="+count);
			}
			if(!StringUtils.isNullOrEmpty(sale_money)){
				if(sb.indexOf("=")>0)
					sb.append(",");
				sb.append(" sale_money="+sale_money);
			}
			int isExists = -1;
			try(Statement st = conn.createStatement();){
//				System.out.println(sb);
				log.info("execute sql:{}",sb);
				if(!sb.equals("set"))
					isExists = st.executeUpdate("update t_goods "+sb+" where goods_name='"+goods_name+"'");
			}catch(Exception e){
				log.info("error in adminservlet:{}",e);
//				System.out.println(e);
			}
			if(isExists<0)
				req.setAttribute("update_error","没有此商品");
				
		}
		req.getRequestDispatcher("update.jsp").forward(req, resp);
	}

}
