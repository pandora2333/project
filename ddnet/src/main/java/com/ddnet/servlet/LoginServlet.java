package com.ddnet.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddnet.pojo.Account;
import com.ddnet.pojo.Car;
import com.ddnet.util.CarStrUtil;
import com.ddnet.util.DB;
import com.ddnet.util.MD5Util;
import com.mysql.cj.util.StringUtils;

import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户登录
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/login",loadOnStartup=-1)
@Slf4j
public class LoginServlet extends HttpServlet {

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
		
		//普通用户登录
		String username = (String)req.getParameter("username");
		//管理员登录
		String adminname = (String)req.getParameter("adminname");
		String password = (String)req.getParameter("pwd");
		if((!StringUtils.isNullOrEmpty(username)||!StringUtils.isNullOrEmpty(adminname))&&!StringUtils.isNullOrEmpty(password)){
			String querySql = null;
			try(Statement statement = conn.createStatement()) {
				Account account = new Account();
				if(!StringUtils.isNullOrEmpty(username)){//user登录
					querySql = "select * from t_account where name = '"+username+"'";
					ResultSet rs = statement.executeQuery(querySql);
					while(rs.next()){
						account.setId(rs.getInt("id"));
						account.setAddr(rs.getString("addr"));
						String money = rs.getString("money");
						if(!StringUtils.isNullOrEmpty(money))
						account.setMoney(new BigDecimal(money));
						account.setPhone(rs.getString("phone"));
						account.setPic(rs.getString("pic"));
						account.setPwd(rs.getString("pwd"));
						account.setName(rs.getString("name"));
						account.setUse_car(fzComplex(rs.getString("use_car"),account.getName()));
					}
					if(rs!=null) rs.close();
					if(account.getPwd()!=null&&account.getPwd().equals(MD5Util.md5(password))){
						req.getSession().setAttribute("user",account);//缓存到session中
						resp.sendRedirect("show.jsp");
						return;
					}
				}else{//admin登录
					querySql = "select * from t_admin where name = '"+adminname+"'";
					ResultSet rs = statement.executeQuery(querySql);
					while(rs.next()){
						account.setId(rs.getInt("id"));
						account.setPwd(rs.getString("pwd"));
						account.setName(rs.getString("name"));
					}
					if(rs!=null) rs.close();
					if(account.getPwd()!=null&&account.getPwd().equals(MD5Util.md5(password))){//
						req.getSession().setAttribute("admin",account);//缓存到session中
						req.getRequestDispatcher("/admin.jsp").forward(req, resp);//到商品添加详情页面
					}else
						req.getRequestDispatcher("/login2.jsp").forward(req, resp);
					return;
				}
			} catch (SQLException e) {
//				System.out.println(e);
				log.info("error in loginservlet:{}",e);
			}
			
		}
		resp.sendRedirect("index.jsp");
		
	}
	//解析字符串
	private Car fzComplex(String carStr,String username) {//1x2 2x3 3x4 -> Car
		Car car  = null;
		if(!StringUtils.isNullOrEmpty(carStr)){
			car = CarStrUtil.parseStr(carStr, username);
		}
		return car;
	}

}
