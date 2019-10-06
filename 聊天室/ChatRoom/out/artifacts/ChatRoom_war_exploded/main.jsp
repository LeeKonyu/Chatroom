<%--

测试单人Servlet+JSP聊天，尝试在本页面获取和发送消息并更新消息，仅作测试（能够发送和接收消息，但不能选择聊天对象）

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">

	<title>My JSP 'main.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<meta http-equiv="description" content="This is my page">
	<!--
    <link rel="stylesheet" type="text/css" href="css/style.css">
    -->

</head>

<body>
<form action="${pageContext.request.contextPath }/user" method=post>
	<input type="hidden" name="method" value="sendGetMessage"  />
	<textarea  cols="105" rows="25" name="show_textarea"><%=request.getAttribute("input_textarea")%></textarea>
	<br>
	<textarea  cols="105" rows="5"  name="input_textarea"></textarea><br>
	<input type="submit" value="发送" name="button_one"
		   style="width: 100px; height: 40px;font-size: 25px;"><br>
</form>
</body>
</html>
--%>

<%--使用Jquery，以黑马聊天室的教程为基础，可以选择聊天对象并发送消息--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="safe.jsp"%>
<html>
<head>
<title>Chat room</title>
<link href="CSS/style.css" rel="stylesheet">
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-3.4.1.js"></script>
<script type="text/javascript">
var sysBBS = "<span style='font-size:14px; line-height:30px;'>欢迎光临在线群聊聊天室，请遵守聊天室规则。</span><br><span style='line-height:22px;'>";
	window.setInterval("showContent();",1000);
	window.setInterval("showOnLine();",10000);

	// Jquery:JS框架.
	// 相当于window.onload
	$(function(){
		showOnLine();
		showContent();

	});


	// 显示在线人员列表
	function showOnLine(){
		// 异步发送请求 获取在线人员列表
		// Jquery发送异步请求
		$.post("${pageContext.request.contextPath}/online.jsp?"+new Date().getTime(),function(data){
			// $("#online") == document.getElementById("online");
			$("#online").html(data);
		});
	}

	// 显示聊天的内容
	function showContent(){
		$.post("${pageContext.request.contextPath}/user?"+new Date().getTime(),{'method':'getMessage'},function(data){
			$("#content").html(sysBBS+data);
		});
	}

	//当要聊天时，选择非自己的用户
	function set(selectPerson){	//自动添加聊天对象
		if(selectPerson != "${existUser.username}"){
				form1.to.value=selectPerson;
		}else{
			alert("请重新选择聊天对象！");
		}
	}

	//选择聊天对象并发送消息
	function send(){
		if(form1.to.value==""){
			alert("请选择聊天对象！");
			return false;
		}
		if(form1.content.value==""){
			alert("发送信息不可以为空！");
			form1.content.focus();
			return false;
		}
		// $("#form1").serialize():让表单中所有的元素都提交.
		// jquery提交数据.如{id:1,name:aa,age:25}
		$.post("${pageContext.request.contextPath}/user?"+new Date().getTime(),$("#form1").serialize(),function(data){
			$("#content").html(sysBBS+data+"</span>");
		});
	}

	function exit(){
		alert("欢迎您下次光临！");
		window.location.href="${pageContext.request.contextPath}/user?method=exit";
	}

	function checkScrollScreen(){
		if(!$("#scrollScreen").attr("checked")){
	    	$("#content").css("overflow","scroll");
	    }else{
	    	$("#content").css("overflow","hidden");
	        //alert($("#content").height());
	        $("#content").scrollTop($("#content").height()*2);
	    }
	    setTimeout('checkScrollScreen()',500);
	}


</script>

</head>
<body>

	<table width="942" height="169" border="0" align="center"
		cellpadding="0" cellspacing="0" background="images/tp.jpg">
		<tr>
			<td>&nbsp;</td>
		</tr>
	</table>
	<table width="942" height="290" border="0" align="center"
		cellpadding="0" cellspacing="0">
		<tr>
			<td width="165" valign="top" bgcolor="#D4E6F1" id="online" style="padding:5px">在线人员列表</td>
			<td width="613" height="200px" valign="top"
				 bgcolor="#D6EAF8"
				style="padding:5px; ">
				<div style="height:290px; overflow:hidden" id="content">聊天内容</div></td>

		</tr>
	</table>
	<table width="942" height="95" border="0" align="center"
		cellpadding="0" cellspacing="0" bordercolor="#D6D3CE"
		bgcolor="#EBF5FB">

		<form action="" id="form1" name="form1" method="post">
			<input type="hidden" name="method" value="sendMessage"/>
			<tr>
				<td height="30" align="left">&nbsp;</td>
				<td height="37" align="left">
                <%--谁发送消息--%>
				<input name="from" type="hidden" value="${existUser.username}">[${existUser.username}]对
                <%--谁接收消息--%>
				<input name="to" type="text" value="" size="35" readonly="readonly" > 说：
                </td>

				<td width="19" align="left">聊天信息超过一屏时，仍然显示最先发送的信息
                    <input name="scrollScreen" type="checkbox" class="noborder" id="scrollScreen"
					onClick="checkScrollScreen()" value="1" checked>
				</td>
			</tr>
			<tr>
				<td width="21" height="30" align="left">&nbsp;</td>
				<td width="549" align="left">
				<input name="content" type="text" size="70"
					onKeyDown="if(event.keyCode==13 && event.ctrlKey){send();}">
					<input name="Submit2" type="button" class="btn_grey" value="发送"
					onClick="send()">
				</td>
				<td align="right"><input name="button_exit" type="button"
					class="btn_grey" value="退出聊天室" onClick="exit()">
				</td>
				<td align="center">&nbsp;</td>
			</tr>
			<tr>
				<td height="30" align="left">&nbsp;</td>
				<td colspan="2" align="center" ><span>在线群聊聊天室</span><span>&nbsp;版权所有&nbsp;©&nbsp;2019&nbsp;</span>
                </td>
				<td align="center">&nbsp;</td>
			</tr>
		</form>

	</table>

</body>
</html>
