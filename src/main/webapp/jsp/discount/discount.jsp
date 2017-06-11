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
         <h1 class="color-red">优惠活动/一网打尽</h1>
     </div> 
    <div class="main" >
        <div class="message-list"> 
            <ul> 
                <div class="line"></div>
                <li>
                    <h1>盛世欢唱邵武店劳动节活动</h1>
                    <h2>劳动节当天全全场消费300送300</h2>
                    <img src="jsp/resources/img/1.png">
                    <a href="discountdetail.jsp">查看详情</a>
                    <div class="clear"></div>
                </li>
                <li>
                    <h1>盛世欢唱邵武店劳动节活动</h1>
                    <h2>劳动节当天全全场消费300送300</h2>
                    <img src="jsp/resources/img/1.png">
                    <a href="discountdetail.jsp">查看详情</a>
                    <div class="clear"></div>
                </li>
                 
            </ul>
        </div>
    </div>

    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='jsp/resources/js/rem.js'></script>
</html>
