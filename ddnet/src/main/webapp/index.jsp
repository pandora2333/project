<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<title>欢迎来到当当网购物登录页面</title>
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
				欢迎登录
			</div>
		</div>
		<div class="middle">
			<div class="login">
				<div class="l1 ">
					<div class="l1_font_01 ">当当网会员</div>
					<a class="l1_font_02 " href="<%=application.getContextPath() %>/register.jsp">用户注册</a>
				</div>
				<div class="blank_01"></div>
				<div class="ts">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${err}
				</div>
				<div class="blank_01"></div>
				<form action="login" id="login_form" method="post">
				用户名:
					<div class="input1">
						<input type="text" class="input1_01" name="username"/>
					</div>
					<div class="blank_01"></div>
					<div class="blank_01"></div>
					密码:
					<div class="input2">
						<input type="password" class="input1_01" name="pwd"/>
					</div>
				
				<div class="blank_01"></div>
				<div class="blank_01"></div>
				</form>
				<div class="blank_01"></div>
				<a href="javascript:;" class="aline">
					<div class="red_button" onclick="to_submit()">
						登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录
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