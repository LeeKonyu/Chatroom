<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	String username = "";
	String password = "";
	//获取当前站点的所有Cookie
	Cookie[] cookies = request.getCookies();

	for (int i = 0; i < cookies.length; i++) {
		//对cookies中的数据进行遍历，找到用户名、密码的数据
		if ("username".equals(cookies[i].getName())) {
			//读取时URLDecoder.decode进行解码(PS:Cookie存取时用URLEncoder.encode进行编码)
			username = java.net.URLDecoder.decode(cookies[i].getValue(),"UTF-8");
		} else if ("password".equals(cookies[i].getName())) {
			password =  java.net.URLDecoder.decode(cookies[i].getValue(),"UTF-8");
		}
	}
%>

<!DOCTYPE html>
<html >
<title>Chat room login interface</title>
<link href="CSS/style.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-3.4.1.js"></script>
<script type="text/javascript">
	// JQuery
	// $("#online") == document.getElementById("online");
	function check(){
		if($("#form1").username.value==""){
			alert("请输入用户名！");
			$("#form1").username.focus();
			return false;
		}
		
		if($("#form1").password.value==""){
			alert("请输入密码名！");
			$("#form1").password.focus();
			return false;
		}
	}
	$(function(){

		//onmousedown：鼠标按下时事件
		$("#btn").mousedown(function(){
			$("#pwd").attr("type", "text");
		});

		//onmouseup：鼠标按键被松开时事件
		$("#btn").mouseup(function(){
			$("#pwd").attr("type", "password");
		});

	});

	// 使用href来跳转
	// window.location.href = "reg.jsp";
	// 使用jQuery的属性替换方法
	// $(location).attr('href', 'reg.jsp');

	function toreg(){
		// var url = "reg.jsp";
		// window.location.href= url;
		$(location).attr('href','reg.jsp')
	}
</script>
<style>
	*{
		margin: 0;
		padding:0;
	}
	body{
		background: rgba(0,0,0,0);
		color: whitesmoke;
		font-size: 20px;
	}
	input{
		margin-top: 5px;
	}
</style>
<body>

<%--<%
	//注册错误提示信息
	String info = (String) request.getAttribute("info");
	if (info != null) {
		out.print(info);
	}

%>--%>
	<br>
	<%--注册错误提示--%>
	<div class="tscenter">${ info }</div>
	<%--登陆错误提示--%>
	<div class="tscenter">${ msg }</div>

	<form id="form1" name="form1" method="post" action="${pageContext.request.contextPath }/user" onSubmit="return check()">

		<input type="hidden" name="method" value="login">

		<div class="indexdiv1" >网上聊天室</div>
		<div class="indexdiv2">
			<span>用户名:</span><input type="text" name="username"  value="<%=username%>" class="indexdiv3" ></div>
		<div class="indexdiv2">
			<span>密&nbsp;&nbsp;&nbsp;码:</span><input type="password" id="pwd" name="password"  value="<%=password%>" class="indexdiv3" ></div>
		<div class="indexdiv4">
			<input type="checkbox" value="save" name="save_password">记住密码
			<input name="Submit" type="submit" value="登 陆">
			<input type="button" onclick="toreg()" value="注册">
			<input type="button" name="" id="btn" value="点击显示密码" />
		</div>
	</form>
</body>
</html>
