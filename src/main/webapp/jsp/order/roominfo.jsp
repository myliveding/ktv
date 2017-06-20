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
     <div id="header" class="color-pink">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1>包厢信息</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <input type="hidden" class="iid" value="${roomInfo.iid}" />
             <h2>${roomInfo.shop_name}</h2>
             <p>房间号：${roomInfo.room_type_name}/${roomInfo.room_num}（${roomInfo.room_peoples}人）</p>
             <span>状态：
                 <c:if test="${roomInfo.tag == 1}">
                    未被预订
                </c:if>
                <c:if test="${roomInfo.tag != 1}">
                    已被预订
                </c:if>
             </span>
             <a href="${pageContext.request.contextPath}/personorder/gotoPackages.do?iid=${roomInfo.iid}">立即预订</a>
         </div>
         <div class="room-img">
             ${roomInfo.summary}
         </div>
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
</html>
