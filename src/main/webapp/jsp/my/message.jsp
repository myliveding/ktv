<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html >
<head>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="author" content="linx" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>盛世欢唱ktv</title>
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">我的消息</h1>
     </div> 
    <div class="main" >
        <div class="message-list"> 
            <ul>
                <div class="line"></div>
                <c:forEach var = "msg" items ="${msgs}">
                    <li>
                        <%--<a href="${msg.id}"></a>--%>
                        <span>${msg.create_time}</span>
                        <p>${msg.content}</p>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>

    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
</html>
