<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.st.utils.JsonUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.constant.Constant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>




<!DOCTYPE html>
<html>
  <head>
    <title>无忧保</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""> 
    <meta name="description" content="">
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <link href="<%=JsonUtil.getJsonFile("css/basic.css") %>" type="text/css" rel="stylesheet" />
  </head>
<body class="jsb_add1 gray pb40">

<style>
	.bgorange{
		background-color: #2CCA6F;
	}
	.bgorange .box:first-child{
		border-bottom:1px solid #14EB1A;
	}
</style>

	<section class="bgorange p10">
		<div class="box">
			<label class="fl">订单号：<em class="orderCode"></em></label>
			<span class="fr">待支付</span>
		</div>
		<div class="box c">总金额：￥<em class="zjine"></em></div>
	</section>
	<div class="c mt10 p10 tgray white bt bb">
		<P class="mb10">支付方式：<em class="payMethod"></em></P>
		<P class="mb10">订单提交时间：<em class="ddtime"></em></P>
	</div>



	
	<script type="text/javascript" src="<%=JsonUtil.getJsonFile("js/jquery/jquery-1.8.0.js") %>" ></script>
	<script type="text/javascript">
	function jiesuan(){
		var orderCode =${orderId};
		window.location.href="${pageContext.request.contextPath}/jsp/my/myorderdetailunpay.jsp?OredrId="+orderCode;
	}
       
       function loadinfom(){
         var OredrId ="${orderId}";
         var order_money="${order_money}";
         var order_ervice_fee="${order_ervice_fee}";
         var create_time = "${create_time}";
         var pay_method="${pay_method}";	
         
		$(".orderCode").html(OredrId);
		$(".zjine").html(order_money);
		$(".zfuwufei").html(order_ervice_fee);
		$(".ddtime").html(create_time);
		if(pay_method=="wechatpay"){
			$(".payMethod").html("微信支付");
		}else if(pay_method=="unionpay"){
			$(".payMethod").html("网银在线");
		}else{
			$(".payMethod").html("未支付");
		}
              
       }
		loadinfom();
	function onBridgeReady(){
		   WeixinJSBridge.invoke(
		       'getBrandWCPayRequest', {
		           "appId" : "${appid}",     //公众号名称，由商户传入     
		           "timeStamp":"${timeStamp}",         //时间戳，自1970年以来的秒数     
		           "nonceStr" : "${nonceStr}", //随机串     
		           "package" : "${_package}",
		           "signType" : "MD5",         //微信签名方式:     
		           "paySign" : "${paySign}" //微信签名 
		       },
		       function(res){     
					//alert(res.err_msg);
		           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
						//alert("付款成功，正在跳转到首页");
		              var orderId ="${orderId}";
		              //window.location.href="${pageContext.request.contextPath}/jsp/wechat/paysuc.jsp?OredrId="+OredrId;	
		              window.location.href="${pageContext.request.contextPath}/personorder/gotoPaysuc.do?orderNo="+orderId;
		              //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
		           }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
		              alert("取消支付，正在跳转到订单列表");
		              window.location.href="${pageContext.request.contextPath}/jsp/my/orderlistunpay.jsp";
		           }else{
		              alert("支付失败，正在跳转到订单列表");
                      window.location.href="${pageContext.request.contextPath}/jsp/my/orderlistunpay.jsp";
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


<jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
<jsp:include page="/jsp/layouts/pv.jsp" flush="true"/>
</body>
</html>