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
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />

    <title>ktv</title> 
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/reset.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/pullToRefresh.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div class="main" style="padding-top: 0;">
        <div id="bg"></div>
        <div id="nav">
            <ul>
                <li>
                    <a href="orderselect.html">
                        <div class="nav-item nav-item1">
                            <img src="<%=basePath%>jsp/resources/img/s1.png">
                        </div>
                        <span>在线预定</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/shop/getShopping.do">
                        <div class="nav-item nav-item2">
                            <img src="<%=basePath%>jsp/resources/img/s2.png">
                        </div>
                        <span>在线超市</span>
                    </a>
                </li>
                <li>
                    <a href="exchangescore.html">
                        <div class="nav-item nav-item3">
                            <img src="<%=basePath%>jsp/resources/img/s3.png">
                        </div>
                        <span>积分兑换</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/other/getCompanyRecruits.do">
                        <div class="nav-item nav-item4">
                            <img src="<%=basePath%>jsp/resources/img/s4.png">
                        </div>
                        <span>盛世招聘</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/discount/getDiscountList.do">
                        <div class="nav-item nav-item5">
                            <img src="<%=basePath%>jsp/resources/img/s5.png">
                        </div>
                        <span>优惠活动</span>
                    </a>
                </li>
                <li>
                    <a href="">
                        <div class="nav-item nav-item6">
                            <img src="<%=basePath%>jsp/resources/img/s6.png">
                        </div>
                        <span>盛世会员</span>
                    </a>
                </li>
                <li>
                    <a href="select.html">
                        <div class="nav-item nav-item7">
                            <img src="<%=basePath%>jsp/resources/img/s7.png">
                        </div>
                        <span>电话</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/other/getCompanyInfo.do">
                        <div class="nav-item nav-item8">
                            <img src="<%=basePath%>jsp/resources/img/s8.png">
                        </div>
                        <span>关于盛世</span>
                    </a>
                </li>
                <div class="clear"></div>
            </ul>
        </div>
         <div class="shop-list" id="wrapper">
            <ul>
            <h2>距离我最近↓</h2>
                <c:forEach var = "data" items ="${storeList}">
                    <li>
                        <a href="${pageContext.request.contextPath}/shop/gotoStoreDetail.do?id=${data.id}">
                            <img src="${data.image_url}">
                            <div class="shop-info">
                                <h3>${data.name}</h3>
                                <c:forEach var="room" items="${data.room_type}" varStatus="i" >
                                    <c:if test="${i.index%2 == 0}">
                                        <p>
                                    </c:if>
                                        <span>【剩余${room.name}】${room.count}个</span>
                                    <c:if test="${i.index%2 == 1}">
                                        </p>
                                    </c:if>
                                </c:forEach>
                            </div>
                            <div class="recent">距离我${data.distance}KM</div>
                            <em><a href="${pageContext.request.contextPath}/shop/gotoStoreDetail.do?id=${data.id}">进入预定</a></em>
                        </a>
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
        id:"wrapper",//<------------------------------------------------------------------------------------┐
        pullDownAction:Refresh,
        pullUpAction:Load
        });
    function Refresh() {
        setTimeout(function () {    // <-- Simulate network congestion, remove setTimeout from production!
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
            var el ;
            el =document.querySelector("#wrapper ul");
            $.ajax({
                type:"POST",
                url:"../ktv/php/index.php", 
                success:function(data)
                { 
                    $("#wrapper ul").append(data);
                    wrapper.refresh(); 
                }, 
            });
        },2000);
    } 
</script>
</html>
