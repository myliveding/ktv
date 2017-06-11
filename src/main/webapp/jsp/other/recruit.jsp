<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html  id="line-bg">
<head>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="author" content="linx" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>ktv</title>
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body>  
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世招聘</h1>
     </div> 
    <div class="main" >
        <div class="message-list">
            <ul> 
                <div class="line"></div>
                <c:forEach var = "data" items ="${recruits}">
                    <li>
                        <h1>${data.title}</h1>
                        <p>${data.content}</p>
                    </li>
                </c:forEach>
                    <%--<li>--%>
                        <%--<h1>盛世欢唱邵武店劳动节活动</h1>--%>
                        <%--<h2>18-25岁  男女皆可</h2>--%>
                        <%--<p>五官端正，亲和力强，具有餐饮服务行业经 验者优先。五官端正， 副本</p>--%>
                    <%--</li>--%>
            </ul>
        </div>
    </div>
   
    <div class="footer">
        <ul>
            <li>
                <a href="" class="footer-now red">
                    <img src="jsp/resources/img/h1.png">
                    <span>在线定包</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h3.png">
                    <span>积分商城</span>
                </a>
            </li>
            <li>
                <a href="../my/usercenter.jsp" class="red">
                    <img src="jsp/resources/img/h4.png">
                    <span>我的盛世</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
</body> 
<script src="jsp/resources/js/rem.js"></script>
</html>
