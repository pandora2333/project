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
Statement st  = DB.createStmt(DB.getConn());
ResultSet rs = st.executeQuery("select * from t_goods");
List<Goods> list = new ArrayList<Goods>();
while(rs.next()){
	Goods good  = new Goods();
	good.setId(rs.getInt("id"));
	good .setBrand(rs.getString("brand"));
	good.setGoods_name(rs.getString("goods_name"));
	good.setPic(rs.getString("pic"));
	String money = rs.getString("sale_money");
	if(!StringUtils.isNullOrEmpty(money))
	good.setSale_money(new BigDecimal(money));
	good.setCount(rs.getInt("count"));
	list.add(good);
}
if(rs!=null) rs.close();
if(st!=null) st.close();
if(!list.isEmpty())
request.setAttribute("goods_show",list);
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
	function goto_check(){
		$.ajax({
			   type: "POST",
			   url: "put",
			   data: "goods="+$.trim(carStr),
			   success: function(msg){
			     if(msg=="更新成功!"||msg=="更新失败!"){
			    	 alert(msg);
			    	 window.location.href = 'http://localhost:9000/ddnet/show.jsp';
			     }else{
			    	 window.location.href = 'http://localhost:9000/ddnet/'+msg;
			     }
			   }
			});
	}
	function goto_car(){
		window.location.href = 'http://localhost:9000/ddnet/carList.jsp';
	}
</script>
<title>当当网购物商品页面</title>
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
		 	<c:forEach items="${goods_show}" var="good">
			     <tr>
			       <td><input onclick="change_shfxz(this.checked,${good.id})" class="${good.id}" type="checkbox"/><img src="images/${good.pic}" alt="暂无图片" width="80px"></td>
			       <td>${good.goods_name}</td>
			       <td>
			       		颜色：<span style='color:#ccc'>无</span><br>
			       		尺码：<span style='color:#ccc'>无</span>
			       </td>
			       <td>${good.sale_money}</td>
			       <td><input type="text" name="min" id="id_${good.id}" value="${good.count}" style="width:50px;text-align:center"></td>
			     </tr>
			     </c:forEach>
		   </tbody>
	 	</table>
	</div>
	
	<div class="Cprice">
	<div class="price" onclick="goto_car()">我的购物车</div>
	<div class="jiesuan" onclick="goto_check()" id="jiesuan">更新购物车</div>
	</div>
</body>
</html>