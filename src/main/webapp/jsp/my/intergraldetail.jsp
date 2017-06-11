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
     <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b2.png">
         </a>
         <h1>积分商品信息</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <h2>小包套餐（邵武区）</h2>
             <p>价值： <i>3888</i>元</p>
             <span>所需积分：<i>3699</i>分</span>
             <a href="../order/exchangeorder.jsp">立即兑换</a>
         </div>
         <div class="shop-detail-content">
             <套餐包含内容>
         </div>         
         <div class="room-img"> 
             <span>雪津啤酒12瓶</span>
             <span>6个小蝶</span>
             <span>1份水果</span>
             <span>1盒纸巾</span>
             <span>1套消毒</span>
             <img src="jsp/resources/img/r1.png">
             <img src="jsp/resources/img/r2.png">
             <img src="jsp/resources/img/r3.png">
         </div>
     </div>
    <div class="footer">
        <ul>
            <li>
                <a href="" class="footer-now red">
                    <img src="jsp/resources/img/h1.png">
                    <span>在线定包</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h3.png">
                    <span>积分商城</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h4.png">
                    <span>我的盛世</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
</body> 
<script src='jsp/resources/js/rem.js'></script>
</html>
