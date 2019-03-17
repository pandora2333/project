package com.ddnet.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddnet.pojo.Account;
import com.ddnet.pojo.Car;
import com.ddnet.util.DB;
import com.ddnet.util.MD5Util;
import com.mysql.cj.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户注册
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/register",loadOnStartup=-1)
@Slf4j
public class RegisterServlet extends HttpServlet{

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
		
		//普通用户注册
		String username = (String)req.getParameter("username");
		String password = (String)req.getParameter("pwd");
		String pic = (String)req.getParameter("pic");
		String addr = (String)req.getParameter("addr");
		String phone = (String)req.getParameter("phone");
		if(!StringUtils.isNullOrEmpty(username)&&!StringUtils.isNullOrEmpty(password)){
			try(Statement st1 = conn.createStatement();Statement st2 = conn.createStatement()) {
				//先搜索是否存在同名用户
				if(!st2.executeQuery("select id from t_account where name ='"+username+"'").next()){
					String insertSql = "insert into t_account(name,pwd,pic,phone,addr) values('"+username+"','"+MD5Util.md5(password)+"','"+pic+"','"+phone+"','"+addr+"')";
					st1.execute(insertSql);
					req.getRequestDispatcher("/index.jsp").forward(req, resp);//到登录页面
					return;
				}
			} catch (SQLException e) {
//				System.out.println(e);
				log.info("error in registerServlet:{}",e);
			}
			req.setAttribute("error", "该用户已存在,不能重复注册");//返回错误信息
			req.getRequestDispatcher("/register.jsp").forward(req, resp);//到登录页面;//响应注册失败
		}
		
	}
}
