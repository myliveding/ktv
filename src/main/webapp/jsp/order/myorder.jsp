<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.utils.Constant"%>
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
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/reset.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/pullToRefresh.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1>我的预订</h1>
     </div> 
    <div class="main" >
        <div class="scoreorder" id="wrapper">
            <ul>
                <c:forEach var="data" items="${orders}">
                    <li>
                        <img src="<%=basePath%>jsp/resources/img/p1.png">
                        <div class="scoreorder-info">
                            <p>${data.order_code}</p>
                            <h1>${data.room_type_name}/${data.room_num}</h1>
                            <span>订单金额：<i>${data.money}元</i></span>
                            <%--订单状态 1未支付 2已支付 3已确认消费 4取消预订 5系统取消6待退款 7已退款--%>
                            <span>状态：
                                <i>
                                 <c:if test="${data.order_status == 1}">
                                     未支付
                                 </c:if>
                                <c:if test="${data.order_status == 2}">
                                    已支付
                                </c:if>
                                <c:if test="${data.order_status == 3}">
                                    已确认消费
                                </c:if>
                                <c:if test="${data.order_status == 4}">
                                    取消预订
                                </c:if>
                                <c:if test="${data.order_status == 5}">
                                    系统取消
                                </c:if>
                                <c:if test="${data.order_status == 6}">
                                    待退款
                                </c:if>
                                <c:if test="${data.order_status == 7}">
                                    已退款
                                </c:if>
                                </i>
                            </span>
                        </div>
                        <div class="scoreorder-detail">
                            <c:if test="${data.order_status == 2 &&  (data.arrival_time eq 'null' || data.arrival_time eq '')}">
                                <a onclick="confirmBoot(${data.order_id});" id ="boot" class="scoreorder-go">确认开机</a>
                            </c:if>
                            <c:if test="${data.order_status == 1}">
                                <a href="${pageContext.request.contextPath}/personorder/gotoPayForList.do?orderId=${data.order_id}&type=1" class="scoreorder-go">去支付</a>
                            </c:if>
                            <a class="noact" href="${pageContext.request.contextPath}/personorder/getOrderDetail.do?orderId=${data.order_id}" >详情></a>
                        </div>
                        <div class="clear"></div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script src='<%=basePath%>jsp/resources/js/iscroll.js'></script>
<script src='<%=basePath%>jsp/resources/js/pullToRefresh.js'></script>
<script>
   refresher.init({
        id:"wrapper",
        pullDownAction:Refresh,
        pullUpAction:Load
        });
    function Refresh() {
        setTimeout(function () {
            var el, li, i;
            el =document.querySelector("#wrapper ul");
            //这里写你的刷新代码
            document.getElementById("wrapper").querySelector(".pullDownIcon").style.display="none";
            document.getElementById("wrapper").querySelector(".pullDownLabel").innerHTML="刷新成功";
            location.reload(); 
        }, 1000);
    }
     
    var page=2;
    function Load() {
        setTimeout(function () { 
            var el, li, i;
            el =document.querySelector("#wrapper ul");
            $.ajax({
                type:"POST",
                url:"../ktv/php/local.php", 
                success:function(data)
                { 
                    $("#wrapper ul").append(data);
                    wrapper.refresh(); 
                }, 
            });
        },2000);
    }

function confirmBoot(orderId){
    $.ajax({
        'url': "${pageContext.request.contextPath}/personorder/confirmBoot.do",
        'type': 'post',
        'dataType': 'json',
        'data': {
            orderId: orderId,
        },
        success: function success(d) {
            if (d.error_code == 0) {
                alert(d.msg);
                window.location.reload();
                //$(this).find('.scoreorder-detail a').hide();
            } else {
                alert(d.msg);
            }
        }
    });
}
</script>
</html>
