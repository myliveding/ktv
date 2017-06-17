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
    <title>ktv</title>
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body> 
     <div id="header"  style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1 >订单详情</h1>
     </div> 
     <div class="main"> 
          <div class="exchange-info">
              <ul>
                  <li>
                      <span>兑换编号：</span>
                      <label >${order.order_code}</label>
                  </li>
                  <li>
                      <span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
                      <label >${member.nickName}</label>
                  </li>
                  <li>
                      <span>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</span>
                      <label >${member.mobile}</label>
                  </li>
                  <li>
                      <span>选择区域：</span>
                      <label >${order.shop_name}</label>
                  </li>
                  <li>
                      <span>门店地址：</span>
                      <label >${order.shop_address}</label>
                  </li> 
                  <li>
                      <span>包&nbsp;&nbsp;厢&nbsp;号：</span>
                      <label >${order.room_num}</label>
                  </li>
                  <li>
                      <span>场&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：</span>
                      <label>晚场</label>
                  </li>
                  <li>
                      <span>到店时间：</span>
                      <label >${order.arrival_time}</label>
                  </li>
                  <li>
                      <span>包厢类型：</span>
                      <label >${order.room_type_name}</label>
                  </li>
                  <li>
                      <span>订单提交时间：</span>
                      <label >${order.create_time}</label>
                  </li>
              </ul>
          </div> 
          <div class="score-consume">
              <div class="score-consume-num">
                  订单金额：<span>${order.money}元</span>
              </div>
              <div class="score-consume-status">
                  <span>订单状态：
                   <c:if test="${order.order_status == 1}">
                       未支付
                       <label>取消订单</label>
                   </c:if>
                    <c:if test="${order.order_status == 2}">
                        已支付
                        <label>退款</label>
                    </c:if>
                    <c:if test="${order.order_status == 3}">
                        已确认消费
                    </c:if>
                    <c:if test="${order.order_status == 4}">
                        取消预订
                    </c:if>
                    <c:if test="${order.order_status == 5}">
                        系统取消
                    </c:if>
                    <c:if test="${order.order_status == 6}">
                        待退款
                    </c:if>
                    <c:if test="${order.order_status == 7}">
                        已退款
                    </c:if>
                  </span>
              </div>
              <div class="clear"></div>
          </div>   
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    $('.score-consume-status label').click(function(){
        cancleOrder();
    })

    function cancleOrder(){
        $.ajax({
            'url': "${pageContext.request.contextPath}/personorder/cancleOrder.do",
            'type': 'post',
            'dataType': 'json',
            'data': {
                orderId: ${order.order_id}
            },
            success: function success(d) {
                if (d.status == 0) {
                    alert(d.msg);
                    window.location.reload();
                } else {
                    alert(d.msg);
                }
            }
        });
    }

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


</script>
</html>
