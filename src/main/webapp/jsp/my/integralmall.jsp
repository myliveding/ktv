<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
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
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body> 
     <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世积分商城</h1>
     </div> 
     <div class="main"> 
          <div class="area-select">
          	<div class="area-select-item">
          		<a href="">邵武区</a>
          	</div>
          	<div class="area-select-item ">
          	 	<a href="">武夷山区</a>
          	</div>
          	<div class="clear"></div>
          </div>
          <div class="cate-mall">
          	 <ul>
          	 	<li>
          	 		<a href="" class="act">礼品类</a>
      	 		</li>
          	 	<li>
          	 		<a href="">包厢类</a>
      	 		</li>
          	 	<li>
          	 		<a href="">酒水类</a>
      	 		</li>
          	 	<div class="clear"></div>
          	 </ul>
          </div>
          <div class="score-list">
          	 <ul>
          	 	<li>
          	 		<img src="jsp/resources/img/1.png">
          	 		<div class="score-list-info">
          	 			<h2>小包套餐（邵武区）</h2>
          	 			<span>积分：<i>6802分</i></span>
          	 		</div>
          	 		<a href="intergraldetail.jsp">查看详情</a>
          	 		<div class="clear"></div>
          	 	</li>
          	 	<li>
          	 		<img src="jsp/resources/img/1.png">
          	 		<div class="score-list-info">
          	 			<h2>小包套餐（邵武区）</h2>
          	 			<span>积分：<i>6802分</i></span>
          	 		</div>
          	 		<a href="intergraldetail.jsp">查看详情</a>
          	 		<div class="clear"></div>
          	 	</li>
          	 </ul>
          </div>
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='jsp/resources/js/rem.js'></script>
</html>
