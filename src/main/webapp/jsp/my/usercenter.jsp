<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div class="main" style="padding-top: 0;">
        <div class="user">
            <div class="user-top">
                <span>普通会员</span> 
                <a href="${pageContext.request.contextPath}/member/gotoMyMessages.do">
                    <img src="<%=basePath%>jsp/resources/img/message.png">
                    <i>${num}</i>
                </a>
                <a href="${pageContext.request.contextPath}/member/gotoUserSet.do">
                    <img src="<%=basePath%>jsp/resources/img/set.png" >
                </a>
                <div class="clear"></div>
            </div>
            <div class="header-img">
                <c:choose>
                    <c:when test="${member.headPortrait eq 'null' || member.headPortrait eq ''}">
                        <img src="<%=basePath%>jsp/resources/img/1.png">
                    </c:when>
                    <c:otherwise>
                        <c:if test="${fn:startsWith(member.headPortrait, 'http://w')}">
                            <img src="${member.headPortrait}">
                        </c:if>
                        <c:if test="${!fn:startsWith(member.headPortrait, 'http://w')}">
                            <img src="${pageContext.request.contextPath}/member/showHeadPortrait.do">
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="user-account">
                <ul>
                    <li>
                        <span>余额：</span>
                        <em>${member.balance}</em>
                    </li>
                    <li>
                        <span>佣金：</span>
                        <em>${member.commission}</em>
                    </li>
                    <li>
                        <span>总积分/可用积分：</span>
                        <em>${member.totalScore}/${member.availableScore}</em>
                    </li>
                    <div class="clear"></div>
                </ul>
            </div>
        </div>
        <div class="user-list">
            <ul>
                <%-- <li>
                    <a href="javascript:void(0);">
                        <img src="<%=basePath%>jsp/resources/img/u1.png">
                        <span>我的团队</span> 
                    </a>
                    <div class="user-list-item">
                        <em><i>●</i>一级用户(2人)</em>
                        <em><i>●</i>二级用户(3人)</em>
                    </div> 
                </li> --%>
                 <li>
                    <a href="${pageContext.request.contextPath}/personorder/getOrderList.do">
                        <img src="<%=basePath%>jsp/resources/img/u2.png">
                        <span>我的预订</span>
                    </a>
                </li>
                <%-- <li>
                    <a href="../order/myscoreorder.jsp">
                        <img src="<%=basePath%>jsp/resources/img/u3.png">
                        <span>积分订单</span>
                    </a>
                </li>
                 <li>
                    <a href="../commission/commission.jsp">
                        <img src="<%=basePath%>jsp/resources/img/u4.png">
                        <span>佣金提现</span>
                    </a>
                </li> --%>
            </ul>
            <ul>
                <%-- <li>
                    <a href="../commission/commissiondetails.jsp">
                        <img src="<%=basePath%>jsp/resources/img/u5.png">
                        <span>佣金明细</span>
                    </a>
                </li> --%>
                 <li>
                    <a href="${pageContext.request.contextPath}/personorder/getShopOrderList.do">
                        <img src="<%=basePath%>jsp/resources/img/u6.png">
                        <span>我的超市订单</span>
                    </a>
                </li>
                    <%--  <li>
                        <a href="javascript:void(0);">
                            <img src="<%=basePath%>jsp/resources/img/u7.png">
                            <span>我的抵用券</span>
                        </a>
                        <div class="user-list-item">
                            <em><i>●</i>抽奖</em>
                            <em><i>●</i>已使用(7张)</em>
                            <em><i>●</i>未使用(7张)</em>
                        </div>
                    </li> --%>
                 <li>
                    <a href="${pageContext.request.contextPath}/jsp/my/code.jsp">
                        <img src="<%=basePath%>jsp/resources/img/u8.png">
                        <span>我的邀请二维码</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
</html>
<script>
    $('.user-list a').click(function() {
        $li=$(this).parent('li');
        if ($(this).hasClass('d-menu')) {
            $li.find('.user-list-item').slideUp();
            $(this).removeClass('d-menu');
        }else{
            $li.find('.user-list-item').slideDown();
            $(this).addClass('d-menu');
        } 
    })
</script>
