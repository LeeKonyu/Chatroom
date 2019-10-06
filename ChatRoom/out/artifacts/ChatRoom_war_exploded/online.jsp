<%@page import="com.lky.service.UserService"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link href="CSS/style.css" rel="stylesheet">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr><td height="32" align="center">欢迎在线群聊聊天室！</td></tr>
  <tr>
  <td height="23" align="center"><a href="#" onclick="set('所有人')">所有人</a></td>
  </tr>  
<c:forEach var="entry" items="${ userMap }">
  <tr>
    <td height="23" align="center">
    
    	<a href="#" onclick="set('${ entry.key.username }')">${ entry.key.username }</a>

	</td>
  </tr>
</c:forEach>
<tr><td height="30" align="center">当前在线[<font color="#FF6600">${ fn:length(userMap) }</font>]人</td></tr>
</table>