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

import com.ddnet.pojo.Account;
import com.ddnet.util.CarStrUtil;
import com.ddnet.util.DB;
import com.mysql.cj.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 更新购物车中商品
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/put",loadOnStartup=-1)
@Slf4j
public class PutServlet extends HttpServlet{
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
		String strs = req.getParameter("goods");
		System.out.println(strs);
		Account account=(Account) req.getSession().getAttribute("user");
		if(account==null){
//			resp.sendRedirect("index.jsp");//ajax模式下失效,会返回整个页面代码，如不能局部刷新，则不会刷新页面
//			req.getRequestDispatcher("login2.jsp").forward(req, resp);
			resp.getWriter().print("index.jsp");
			return;
		}
		int flag = -1;
		if(!StringUtils.isNullOrEmpty(strs)){
			try(Statement st = conn.createStatement();Statement st2 = conn.createStatement();ResultSet rs = st.executeQuery("select use_car from t_account where id="+account.getId())){
				rs.next();
				String originStr = rs.getString(1);
				String destStr = CarStrUtil.fixedStr(originStr, 0, strs);
				flag = st2.executeUpdate("update t_account set use_car = '"+destStr+"' where id= "+account.getId());
				account.setUse_car(CarStrUtil.parseStr(destStr,account.getName()));
				req.getSession().setAttribute("user",account);//更新account
			}catch(Exception e){
//				System.out.println("error in putservlet:"+e);
				log.info("error in putservlet:{}",e);
			}
		}
		if(flag<0)
			resp.getWriter().print("更新失败!");
		else
			resp.getWriter().print("更新成功!");
	}

}
