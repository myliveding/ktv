<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="author" content="linx" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>ktv</title>
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body> 
     <div id="header" class="color-pink">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b2.png">
         </a>
         <h1>包厢信息</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <h2>盛世欢唱武夷山西门头店</h2>
             <p>房间号：小包/B27（8-10人）</p>
             <span>状态：未被预定</span>
             <a href="../shop/pay.jsp">立即预定</a>
         </div>
         <div class="room-img">
             <img src="jsp/resources/img/r1.png">
             <img src="jsp/resources/img/r2.png">
             <img src="jsp/resources/img/r3.png">
         </div>
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='jsp/resources/js/rem.js'></script>
</html>
