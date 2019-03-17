package com.ddnet.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import com.ddnet.pojo.Account;
import com.ddnet.pojo.Order;
import com.ddnet.util.CarStrUtil;
import com.ddnet.util.DB;
import com.mysql.cj.util.StringUtils;
/**
 * 商品展示
 * @author pandora
 *
 */
@WebServlet(urlPatterns="/pay",loadOnStartup=-1)
@Slf4j
public class PayServlet extends HttpServlet {
	
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
		Account account = (Account) req.getSession().getAttribute("user");
		String newStr = req.getParameter("goods");//1x2 3x5
		if(account == null) {
			resp.sendRedirect("login");
			return;
		}
		//更新账户余额
		try(Statement m = conn.createStatement();ResultSet rs = m.executeQuery("select money from t_account where name = '"+account.getName()+"'");){
			rs.next();
			String upMoney = rs.getString(1);
			if(!StringUtils.isNullOrEmpty(upMoney))
				account.setMoney(new BigDecimal(upMoney));
		}catch(Exception e){
//			System.out.println("up money in payservlet");
			log.info("error in payservlet-up money:{}",e);
		}
		//支付结算,清空购物车
		BigDecimal use_money = account.getMoney();
		if(use_money==null){
			resp.getWriter().print("结算失败，余额/库存不足");
			return;
		}
		float origin_money = use_money.floatValue();
		List<Order> pay_list = CarStrUtil.parseStr(newStr, account.getName()).getOrders();//需购买订单
		boolean flag = true;//非数据库测试，预判断
		for(Order order:pay_list){//
			int free_count = 0;//商品实际剩余量
			String tmp = null;
			try(Statement st = conn.createStatement();ResultSet rs = st.executeQuery("select sale_money,count from t_goods where id = "+order.getGoods_id())){
				rs.next();
				tmp = rs.getString(1);
				free_count = rs.getInt(2);
			}catch(Exception e){
//				System.out.println(e);
				log.info("error in payservlet-pay and clear car:{}",e);
			}
			if(!StringUtils.isNullOrEmpty(tmp)){
				use_money = use_money.subtract(new BigDecimal(tmp).multiply(new BigDecimal(order.getBuy_count())));
				if(use_money.floatValue()<0.0f||free_count-order.getBuy_count()<0)//余额/库存不足，购买失败
					flag = false;
			}
		}
		if(origin_money==use_money.floatValue()) flag = false;
		if(flag){
			try(Statement st = conn.createStatement();Statement st2 = conn.createStatement();Statement st3 = conn.createStatement()){
				conn.setAutoCommit(false);//进行事务性操作
				st.execute("update t_account set money = '"+use_money.floatValue()+"',use_car = '"+CarStrUtil.fixedStr(CarStrUtil.parseCar(account.getUse_car()), 1,  newStr)+"' where id="+account.getId());//
				ResultSet rs = st2.executeQuery("select use_car from t_account where id= "+account.getId());
				rs.next();
				account.setUse_car(CarStrUtil.parseStr(rs.getString(1),account.getName()));//
				//更新各大商品库存
				for(Order order:pay_list)
					st3.executeUpdate("update t_goods set count = count -"+order.getBuy_count()+" where id ="+order.getGoods_id());
				if(rs!=null)rs.close();
				req.getSession().setAttribute("user",account);//更新account
			}catch(Exception e){
//				System.out.println("error in ListServlrt:"+e);
				log.info("error in payservlet-persist into database:{}",e);
			}
				try {
					conn.commit();
					conn.setAutoCommit(true);//恢复自动提交
				} catch (SQLException e) {
					try {
						conn.rollback();//事务回滚
					} catch (SQLException e1) {
//						System.out.println("error in payservlet:事务回滚异常");
						log.info("error in payservlet:事务回滚异常");
					}
				}
				resp.getWriter().print("结算成功");
				return;
		}else
			resp.getWriter().print("结算失败，余额/库存不足");
	}

}
