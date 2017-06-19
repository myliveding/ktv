<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html style="background: #f8f8f8;">
<head>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="author" content="linx" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>盛世欢唱ktv</title>
    <link rel="stylesheet" href="jsp/resources/css/mui.min.css">
    <link rel="stylesheet" href="jsp/resources/css/app.css">
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body style="background: #f8f8f8;"> 
     <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世在线超市-购物车</h1>
     </div> 
      <div class="codebanner"></div>
      <div class="code">
          <img src="jsp/resources/img/code.png">
      </div>
      <div class="code-join">扫描二维码，加入注册成为盛世欢唱会员</div>
      <div class="share">分&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;享</div> 
</body>  
<script src='jsp/resources/js/rem.js'></script>
</html>
