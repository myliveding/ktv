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
    <title>盛世欢唱ktv</title>
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">我的超市订单</h1>
     </div> 
    <div class="main" >
        <ul>
            <c:forEach var="order" items="${orders}">
                <div class="shop-order">
                    <ul>
                        <c:forEach var="info" items="${order.goods_detail}">
                            <li>
                                <img src="${info.image_url}">
                                <p>${info.title}</p>
                                <span>￥${info.price}</span>
                                <i>x ${info.num}</i>
                            </li>
                        </c:forEach>
                    </ul>
                    <div class="shop-order-total">
                        <h1>共<i>${order.goods_count}</i>件商品  合计：￥<i>${order.all_price}</i>元</h1>
                        <h3>包厢号：${order.room_num}</h3>
                        <h2>
                            <p>订单编号：${order.order_code}</p>
                            <c:if test="${data.order_status == 1}">
                                <span href="${pageContext.request.contextPath}/personorder/gotoPayForList.do?orderId=${order.order_id}&type=2" class="shop-order-go">去支付</span>
                                <span onclick="handelShopOrder(${order.order_id},2);" class="shop-order-no">取消订单</span>
                                <span onclick="handelShopOrder(${order.order_id},3);" class="shop-order-no">删除订单</span>
                            </c:if>
                            <c:if test="${order.order_status == 2}">
                                <span onclick="handelShopOrder(${order.order_id},1);" class="shop-order-go">确认订单</span>
                                <span onclick="handelShopOrder(${order.order_id},4);" class="shop-order-no">退款</span>
                            </c:if>
                            <c:if test="${order.order_status == 3}">
                                <span onclick="handelShopOrder(${order.order_id},3);" class="shop-order-no">删除订单</span>
                            </c:if>
                        </h2>
                    </div>
                </div>
            </c:forEach>
        </ul>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script>
//确认订单
function handelShopOrder(orderId, type){
    $.ajax({
        'url': "${pageContext.request.contextPath}/personorder/handelShopOrder.do",
        'type': 'post',
        'dataType': 'json',
        'data': {
            orderId: orderId,
            type: type
        },
        success: function success(d) {
            alert(d.msg);
            if (d.error_code == 0) {
                window.location.reload();
            }
        }
    });
}
</script>
</html>
