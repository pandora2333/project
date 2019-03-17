<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%if(session.getAttribute("admin")==null)request.getRequestDispatcher("/login2.jsp").forward(request, response);%>
<html>
<head>
	<title>欢迎来到当当网购物后台页面</title>
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/icon" href="images/jd.ico">
<link rel="stylesheet" type="text/css" href="css/login.css">
<script type="text/javascript">
	function to_submit(){
		$("#login_form").submit();
	}
</script>
</head>
<body>
	<div class="up">
			<img src="images/logo.png" height="45px;" class="hy_img"/>
			<div class="hy">
				欢迎添加商品
			</div>
		</div>
		<div class="middle">
			<div class="login">
				<div class="l1 ">
					<div class="l1_font_01 ">当当网管理员</div>
					<font color="red">><c:if test="${add_error!=null}">${add_error}</c:if></font>
				</div>
				<div class="blank_01"></div>

				<div class="blank_01"></div>
				<form action="admin" id="login_form" method="post">
				商品名:
					<div class="input1">
						<input type="text" class="input1_01" name="goods_name"/>
					</div>
					<div class="blank_01"></div>
					商标:
					<div class="input2">
						<input type="text" class="input1_01" name="brand"/>
					</div>
					库存:
					<div class="input2">
						<input type="text" class="input1_01" name="count"/>
					</div>

					售价:
					<div class="input2">
						<input type="text" class="input1_01" name="sale_money"/>
					</div>
				<div class="blank_01"></div>
				<div class="blank_01"></div>
				</form>
				<div class="blank_01"></div>
				<a href="javascript:;" class="aline">
					<div class="red_button" onclick="to_submit()">
						添&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;加
					</div>
				</a>
				<div class="blank_01"></div>
				<div class="box_down">
					<div class="box_down_1">使用合作网站账号登录京东：</div>
					<div class="box_down_1">京东钱包&nbsp;&nbsp;|&nbsp;&nbsp;QQ 
					&nbsp;&nbsp;|&nbsp;&nbsp;微信
					</div>
				</div>
			</div>	
		</div>
		
		<div class="down">
			<br />
			Copyright©2004-2015  xu.jb.com 版权所有
		</div>
</body>
</html>