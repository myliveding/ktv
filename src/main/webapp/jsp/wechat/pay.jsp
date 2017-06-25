<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.utils.Constant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html style="background: #f8f8f8;">
  <head>
    <title>盛世欢唱ktv</title>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <link href="<%=basePath%>jsp/resources/css/basic.css %>" type="text/css" rel="stylesheet" />
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
  </head>
<body>

<%--<style>--%>
	<%--.bgorange{--%>
		<%--background-color: #2CCA6F;--%>
	<%--}--%>
	<%--.bgorange .box:first-child{--%>
		<%--border-bottom:1px solid #14EB1A;--%>
	<%--}--%>
<%--</style>--%>
<%--<section class="bgorange" style="background: #f2f2f4;">--%>
    <%--<div class="box">--%>
        <%--<label class="about-item-box1">订单号：<em class="orderCode"></em></label>--%>
        <%--<span class="about-item-box1">待支付</span>--%>
    <%--</div>--%>
    <%--<div class="box">总金额：￥<em class="zjine"></em></div>--%>
<%--</section>--%>
<%--<div class="bgorange">--%>
    <%--<P class="box">手机号：<em class="payMethod"></em></P>--%>
    <%--<P class="box">订单提交时间：<em class="ddtime"></em></P>--%>
<%--</div>--%>
<div id="header"  style="background: #fbd1c1;">
    <a href="javascript:history.go(-1);">
        <img src="<%=basePath%>jsp/resources/img/b2.png">
    </a>
    <h1 >支付详情</h1>
</div>
<div class="main">
    <div class="exchange-info">
        <ul>
            <li>
                <span>订单编号：</span>
                <label ><em class="orderCode"></em></label>
            </li>

            <li>
                <span>电话：</span>
                <label ><em class="payMethod"></em></label>
            </li>
            <li>
                <span>订单提交时间：</span>
                <label ><em class="ddtime"></em></label>
            </li>
            <li>
                <span>订单金额：</span>
                <label >￥<em class="zjine"></em></label>
            </li>
        </ul>
    </div>
</div>
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script type="text/javascript" src="<%=basePath%>jsp/resources/js/jquery-1.8.0.js" ></script>
<script type="text/javascript">
    function loadinfom(){
        var orderCode ="${orderCode}";
        var orderMoney="${order_money}";
        var createTime = "${create_time}";
        var payMethod="${pay_method}";

        $(".orderCode").html(orderCode);
        $(".zjine").html(orderMoney);
        $(".ddtime").html(createTime);
        $(".payMethod").html(payMethod);
    }
    loadinfom();

	function onBridgeReady(){
       WeixinJSBridge.invoke(
           'getBrandWCPayRequest', {
               "appId" : "${appid}",     //公众号名称，由商户传入
               "timeStamp":"${timeStamp}", //时间戳，自1970年以来的秒数
               "nonceStr" : "${nonceStr}", //随机串
               "package" : "${_package}",
               "signType" : "MD5",         //微信签名方式:
               "paySign" : "${paySign}" //微信签名
           },

           function(res){
               //alert(res.err_msg);
               //alert(JSON.stringify(res));
               var orderId = "${orderId}";
               var type = "${type}";
               if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                  alert("付款成功，正在跳转到首页");
                  //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
               }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                  alert("取消支付，正在跳转到订单列表");
               }else{
                  alert("支付失败，正在跳转到订单列表");
               }

               if(type == 1){
                   window.location.href="${pageContext.request.contextPath}/personorder/getOrderDetail.do?orderId=" + orderId;
               }else{
                   window.location.href="${pageContext.request.contextPath}/personorder/getShopOrderList.do";
               }
           }
       );
    }

    if (typeof WeixinJSBridge == "undefined"){
       if( document.addEventListener ){
           document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
       }else if (document.attachEvent){
           document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
           document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
       }
    }else{
       onBridgeReady();
    }
</script>
</body>
</html>