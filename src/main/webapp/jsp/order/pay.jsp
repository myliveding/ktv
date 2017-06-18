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
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1 >我的订单</h1>
     </div> 
    <div class="main" style="background: #f2f2f4;">
        <input type="hidden" class="order_id" value="${orderId}" />
         <div class="pay-price">
             <img src="<%=basePath%>jsp/resources/img/p1.png" >
             <div class="pay-price-info">
                 <h1>￥${money}</h1>
                 <h3>在线消费订单</h3>
             </div>
             <div class="clear"></div>
         </div>
         <div class="pay-form">
             <div class="pay-group pay-group-act"> 
                 <img src="<%=basePath%>jsp/resources/img/w1.png">
                 <div class="payment">
                     <h2>微信支付</h2>
                     <span>推荐微信安装5.0及以上版本的用户使用</span>
                 </div>
                 <div class="clear"></div> 
             </div>
            <%--<div class="pay-group">--%>
                 <%--<img src="<%=basePath%>jsp/resources/img/w2.png">--%>
                 <%--<div class="payment">--%>
                     <%--<h2>支付宝支付</h2>--%>
                     <%--<span>推荐有支付宝账号的用户使用</span>--%>
                 <%--</div>--%>
                 <%--<div class="clear"></div>--%>
             <%--</div>--%>
         </div>
         <div class="pay-submit">
             确认支付<span>￥138.00</span>
         </div>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    //初始化页面是选中第一个包厢类型
    $(document).ready(function(){
        $('.pay-submit span').html($('.pay-price-info h1').text());
        $('.pay-form .pay-group:eq(0)').addClass('pay-group-act');
    });

    $('.pay-group').click(function(){
        $('.pay-group').removeClass('pay-group-act');
        $(this).addClass('pay-group-act');
    })

    $('.pay-submit').click(function(){
        if (!util.isWeixnOpen()) {
            alert('<div><div><img src="<%=basePath%>jsp/resources/img/10.png"></div><div style="color:#2cca6f;">微信不支持在浏览器端的支付！</div><div style="color:#333;">可通过以下方式完成支付</div><div style="color:#999; text-align:left;">方式一：打开微信，关注“盛世欢唱”公众号，进入“我的>我的订单”中完成交易</div><div style="color:#999; text-align:left;">方式二：选择其他支付方式完成交易</div></div>');
            return false;
        } else {
            setTimeout(function () {
                window.location.href = packageJson.JAVA_DOMAIN + "/wechat/pay/index.do?orderId=" + $('.order_id').val();
            }, 1000);
        }
    })
</script>
</html>
