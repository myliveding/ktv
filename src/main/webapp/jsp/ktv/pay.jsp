<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html style="background: #fbd1c1;">
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
         <h1 >我的超市订单</h1>
     </div> 
    <div class="main" style="background: #f2f2f4;">
         <div class="pay-price">
             <img src="jsp/resources/img/p1.png" >
             <div class="pay-price-info">
                 <h1>￥138.00</h1>
                 <h3>在线超市消费订单</h3>
             </div>
             <div class="clear"></div>
         </div>
         <div class="pay-form">
             <div class="pay-group pay-group-act"> 
                 <img src="jsp/resources/img/w1.png">
                 <div class="payment">
                     <h2>微信支付</h2>
                     <span>推荐微信安装5.0及以上版本的用户使用</span>
                 </div>
                 <div class="clear"></div> 
             </div>
            <div class="pay-group"> 
                 <img src="jsp/resources/img/w2.png">
                 <div class="payment">
                     <h2>支付宝支付</h2>
                     <span>推荐有支付宝账号的用户使用</span>
                 </div>
                 <div class="clear"></div> 
             </div>
         </div>
         <div class="pay-submit">
             确认支付<span>￥138.00</span>
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
                <a href="about.jsp" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="integralmall.jsp" class="red">
                    <img src="jsp/resources/img/h3.png">
                    <span>积分商城</span>
                </a>
            </li>
            <li>
                <a href="usercenter.jsp" class="red">
                    <img src="jsp/resources/img/h4.png">
                    <span>我的盛世</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
</body> 
<script src='jsp/resources/js/rem.js'></script>
<script src='jsp/resources/js/jquery.min.js'></script>
<script>
    $('.pay-group').click(function(){
        $('.pay-group').removeClass('pay-group-act');
        $(this).addClass('pay-group-act');
    })
</script>
</html>
