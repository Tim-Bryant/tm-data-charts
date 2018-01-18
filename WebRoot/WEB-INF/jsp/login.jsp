<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<!-- 获取网站根目录，以防止定位问题导致资源找不到 -->
<%
	String ctx = request.getContextPath();
%>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>登录</title>
<link rel="icon" href="${ctx }/images/ic.ico">
<link rel="stylesheet" type="text/css" href="<%=ctx %>/css/styles.css">
</head>
<body>
<!-- 代码 开始 -->
<div class="wrapper">
	<div class="container">
		<h1>欢迎登录</h1>
		<form id="loginform" class="form" action="./validateLogin" method="post" enctype="application/x-www-form-urlencoded">
			<input type="text" id="username" name="username" placeholder="用户名">
			<input type="password" id="password" name="password" placeholder="密码">
			<button type="submit" id="login-button">登录</button>
			<br>
			<span style="color: #fff;"><c:out value="${message}" /> </span>
		</form>
	</div>
	
	<ul class="bg-bubbles">
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
	</ul>
</div>

<script src="<%=ctx %>/js/core/jquery-1.11.0.js"  type="text/javascript"></script>
<script src="<%=ctx %>/js/layui_2.1.7/layui.all.js"  type="text/javascript"></script>
<script>
$('#login-button').click(function (event) {
	//preventDefault() 方法阻止元素发生默认的行为（例如，当点击提交按钮时阻止对表单的提交）。
	//如果 type 属性是 "submit"，在事件传播的任意阶段可以调用任意的事件句柄，通过调用该方法，可以阻止提交表单。注意，如果 Event 对象的 cancelable 属性是 fasle，
	//那么就没有默认动作，或者不能阻止默认动作。无论哪种情况，调用该方法都没有作用。
	event.preventDefault();
	//alert(event.cancelable); true
	var username=$("#username").val();
	var password=$("#password").val();
	if(username && password){
		$('form').fadeOut(300);
		$('.wrapper').addClass('form-success');
		setTimeout(function(){
			$("#loginform").submit();
		},800);
	}else{
		if(username==null||username==""){
			$("#username").attr("placeholder","请输入用户名");
			layer.tips('请输入用户名', '#username');
			return;
		}
		if(password==null||password==""){
			$("#password").attr("placeholder","请输入密码");
			layer.tips('请输入密码', '#password');
			return;
		}
	}
	//$('form').fadeIn(300);
});
</script>
<!-- 代码 结束 -->
</body>
</html>