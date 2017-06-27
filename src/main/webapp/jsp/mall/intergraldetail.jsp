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
     <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b2.png">
         </a>
         <h1>积分商品信息</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <h2>${info.name}</h2>
             <p>价值： <i>${info.face_value}</i>元</p>
             <span>所需积分：<i>${info.need_integral}</i>分</span>
             <a onclick="exchange(${info.id})">立即兑换</a>
         </div>
         <div class="shop-detail-content">
             <套餐包含内容>
         </div>
         <div class="room-img">
             ${info.content}
             <img src="${info.image_url}">
         </div>
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    //兑换商品
    function exchange(id){
        $.ajax({
            'url': "${pageContext.request.contextPath}/mall/getCate.do",
            'type': 'post',
            'dataType': 'json',
            'data': {
                cateId: cateId
            },
            success: function success(d) {
                if (d.error_code == 0) {
                    //兑换成功跳转
                    window.location.href = '${pageContext.request.contextPath}/mall/gotoMallDetail.do?integralMallId=' + integralMallId;
                } else {
                    alert(d.msg);
                }
            }
        });
    }

</script>
</html>
