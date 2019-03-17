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
 * 删除购物车中商品
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/del",loadOnStartup=-1)
@Slf4j
public class DelServlet extends HttpServlet{

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
		String destStr = req.getParameter("goods");
		Account account=(Account) req.getSession().getAttribute("user");
		if(account==null){
			resp.sendRedirect("login");
			return;
		}
		int delIndex = -1;//标记是否成功删除
		if(!StringUtils.isNullOrEmpty(destStr)){
			try(Statement st = conn.createStatement();Statement st2 = conn.createStatement();ResultSet rs = st.executeQuery("select use_car from t_account where id="+account.getId())){
				rs.next();
				String originStr = rs.getString(1);
				if(!StringUtils.isNullOrEmpty(originStr)){
					destStr = CarStrUtil.delStr(originStr, destStr);
					delIndex =  st2.executeUpdate("update t_account set use_car='"+destStr+"' where name='"+account.getName()+"'");
					if(delIndex > 0) account.setUse_car(CarStrUtil.parseStr(destStr,account.getName()));
				}
			}catch(Exception e){
				log.info("error in DelServlet:{}",e);
			}
		}
		if(delIndex < 0)
			resp.getWriter().print("删除失败");
		else
			resp.getWriter().print("删除成功");
	}
}
