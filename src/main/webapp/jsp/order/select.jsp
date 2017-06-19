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
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <title>盛世欢唱ktv</title>
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
         <input type="hidden" class="iid" value="${iid}" />
         <div class="shop-detail">
             <h2>${roomInfo.shop_name}</h2>
             <p class="gray" >${roomInfo.room_type_name}(${roomInfo.room_peoples}人)</p>
             <p class="gray">今天&nbsp;<i>19:00</i>开唱-<i>次日02:00</i>结束，唱<i>7</i>小时</p>
             <p class="gray"><i>不足7小时按照7小时计算</i></p>
         </div>
         <div class="select-meal">
             <ul>
                 <c:forEach var="data" items="${packages}">
                     <li>
                         <input type="hidden" class="price" value="${data.price}" />
                         <input type="hidden" class="packageId" value="${data.package_id}" />
                         <h1>${data.name}</h1>
                         <p>${data.summary}</p>
                         <div class="moneynum">￥${data.price}</div>
                     </li>
                 </c:forEach>
             </ul>
             <a href="">抵用券</a>
             <div class="select-tel">
                 手机号码<span>${mobile}</span>
             </div>
             <div class="go-pay">
                 <span>￥<i>0.00</i></span>
                 <label>去支付</label>
             </div>
         </div>
          
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    var packageId;
    //初始化页面是选中第一个包厢类型
    $(document).ready(function(){
        $('.select-meal li:eq(0)').addClass('act');
        $('.go-pay span i').html($('.select-meal li:eq(0)').find('.price').val());
        packageId = $('.select-meal li:eq(0)').find('.packageId').val();
    });


    $('.select-meal li').click(function() {
        $('.select-meal li').removeClass('act');
        $(this).addClass('act');
        $('.go-pay span i').html($(this).find('.price').val());
        packageId = $(this).find('.packageId').val();
    })

    $('.go-pay label').click(function() {
        <%--$.ajax({--%>
            <%--'url': "${pageContext.request.contextPath}/personorder/gotoPay.do",--%>
            <%--'type': 'post',--%>
            <%--'dataType': 'json',--%>
            <%--'data': {--%>
                <%--iid: $('.iid').val(),--%>
                <%--packageId: packageId,--%>
            <%--},--%>
            <%--success: function success(d) {--%>
                <%--if (d.status == 0) {--%>
                    window.location.href = packageJson.JAVA_DOMAIN  + '/personorder/gotoPay.do?iid='
                            + $('.iid').val() + "&packageId=" + packageId;
//                } else {
//                    alert(d.msg);
//                }
//            }
//        });
    })
</script>
</html>
