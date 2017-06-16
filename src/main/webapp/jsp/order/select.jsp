<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html   style="background: #fbd1c1;">
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
     <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1>套餐选择</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <h2>小包套餐（邵武区）</h2>
             <p class="gray" >${roomInfo.room_type_name}(${roomInfo.room_peoples}人)</p>
             <p class="gray">今天<i>(01:00)</i>&nbsp;&nbsp;&nbsp;<i>21:00</i>开唱-<i>次日01:00</i>结束，唱<i>4</i>小时</p>
             <p class="gray"><i>不足7小时按照7小时计算</i></p>
         </div>
         <div class="select-meal">
             <ul>
                 <c:forEach var="pack" items="${packages}">
                     <li class="act">
                         <h1>${pack.name}</h1>
                         <p>${pack.summary}</p>
                         <div class="moneynum">￥${pack.price}</div>
                     </li>
                 </c:forEach>
             </ul>
             <a href="">抵用券</a>
             <div class="select-tel">
                 手机号码<span>${mobile}</span>
             </div>
             <div class="go-pay">
                 <span>￥<i>977.00</i></span>
                 <label>去支付</label>
             </div>
         </div>
          
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    $('.select-meal li').click(function() {
        $('.select-meal li').removeClass('act');
        $(this).addClass('act');
    })
</script>
</html>
