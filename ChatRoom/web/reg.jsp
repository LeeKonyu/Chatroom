<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<title>Registration interface</title>
	<link href="CSS/style.css" rel="stylesheet">
	<script type="text/javascript" src="js/jquery-3.4.1.js"></script>
	<script type="text/javascript">
		// Jquery:JS框架.
		// 相当于window.onload
		$(function(){
			//onmousedown：鼠标按下时事件
			$("#btn").mousedown(function(){
				$("#pwd").attr("type", "text");
			});
			//onmouseup：鼠标按键被松开时事件
			$("#btn").mouseup(function(){
				$("#pwd").attr("type", "password");
			});
		})

	</script>
	<style>
		*{
			margin: 0;
			padding:0;
		}

	</style>
</head>
<body>

		<%--//将注册功能实现--%>
			<form  id="form1" name="form1" method="post" action="${pageContext.request.contextPath }/user">
			<input type="hidden" name="method" value="reg"  />
				<div class="indexdiv2">
					<span>用户名:</span><input type="text" name="username" required="required" placeholder="请输入用户名">
				</div>
				<div class="indexdiv2">
					<span>密&nbsp;&nbsp;&nbsp;码:</span><input type="password" id="pwd" name="password"  placeholder="请输入密码"></div>
				<div class="indexdiv4">
					<input type="submit" value="注册" />
					<input type="reset" value="重置" />
					<input type="button" name="" id="btn" value="点击显示密码" />
					<input type="button" name="" id="" value="返回上一界面" onclick="javascript:history.go(-1);" />
				</div>

		</form>




</body>
</html>