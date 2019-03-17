<%@page import="com.ddnet.pojo.Car"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.ddnet.pojo.Order"%>
<%@page import="com.ddnet.pojo.Account"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.mysql.cj.util.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ddnet.pojo.Goods"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.ddnet.util.DB"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%//直接访问数据库
Account account = (Account)session.getAttribute("user");
if(account==null){
	response.sendRedirect("index.jsp");
	return;
}
Statement st  = DB.createStmt(DB.getConn());
StringBuilder sb = new StringBuilder("select * from t_goods where id in(");
Car car = account.getUse_car();
if(car!=null){
int num = car.getOrders().size();
if(num != 0){
	Map<Integer,Goods> map = new HashMap<>();
for(int index = 0;index<num;++index){
	Goods good  = new Goods();
	Order order = account.getUse_car().getOrders().get(index);
	Integer id = order.getGoods_id();
	good.setId(id);
	good.setCount(order.getBuy_count());
	sb.append(id);
	if(index!=num-1)sb.append(",");
	map.put(id,good);
}
sb.append(")");
ResultSet rs = st.executeQuery(sb.toString());
while(rs.next()){
	Goods good = map.get(rs.getInt("id"));
	if(good==null) continue;
	good .setBrand(rs.getString("brand"));
	good.setGoods_name(rs.getString("goods_name"));
	good.setPic(rs.getString("pic"));
	String money = rs.getString("sale_money");
	if(!StringUtils.isNullOrEmpty(money))
	good.setSale_money(new BigDecimal(money));
}
if(rs!=null) rs.close();
if(st!=null) st.close();
if(!map.isEmpty())
request.setAttribute("car_show",map.values());
}
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/css.css">	
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript">
var carStr = '';
function change_shfxz(checked,sku_id){
	var regPos = /^\d+$/;
	if(checked){
		if(!regPos.test($("#id_"+sku_id).val())){
			alert( "请输入整数 ");
		    $("."+sku_id).prop("checked", false);
			return false;
		}
		carStr+=sku_id+"x"+$("#id_"+sku_id).val()+" ";
	}
}
function goto_del(){
	$.ajax({
		   type: "POST",
		   url: "del",
		   data: "goods="+$.trim(carStr),
		   success: function(msg){
		     alert(msg);
		     carStr='';
		     window.location.href='http://localhost:9000/ddnet/carList.jsp';
		   }
		});
}
function goto_check(){
	$.ajax({
		   type: "POST",
		   url: "pay",
		   data: "goods="+$.trim(carStr),
		   success: function(msg){
		     alert(msg);
		     carStr='';
		     window.location.href='http://localhost:9000/ddnet/carList.jsp';
		   }
		});
}
</script>
<title>当当网购物车页面</title>
</head>
<body>
	<div class="Cbox">
		<table class="table table-striped table-bordered table-hover">
		   <thead>
		     <tr>
		       <th>商品图片</th>
		       <th>商品名称</th>
		       <th>商品属性</th>
		       <th>商品价格</th>
		       <th>商品数量</th>
		     </tr>
		   </thead>
		   <tbody>
		   <c:if test="${!empty car_show}">
		 	<c:forEach items="${car_show}" var="good">
			     <tr>
			       <td><input onclick="change_shfxz(this.checked,${good.id})" class="${good.id}" type="checkbox"/><img src="images/${good.pic}" alt="" width="80px"></td>
			       <td>${good.goods_name}</td>
			       <td>
			       		颜色：<span style='color:#ccc'>无</span><br>
			       		尺码：<span style='color:#ccc'>无</span>
			       </td>
			       <td>${good.sale_money}</td>
			       <td><input type="text" name="min" id="id_${good.id}" value="${good.count}" style="width:50px;text-align:center"></td>
			     </tr>
			     </c:forEach>
			    </c:if><a href="show.jsp">></a>
		   </tbody>
	 	</table>
	</div>
	
	<div class="Cprice">
		<div class="price" onclick="goto_del()">删除商品</div>
	    <div class="jiesuan" onclick="goto_check()">结算</div>
	</div>
</body>
</html>