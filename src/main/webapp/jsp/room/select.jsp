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
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body> 
     <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b2.png">
         </a>
         <h1>套餐选择</h1>
     </div> 
     <div class="main"> 
         <div class="shop-detail">
             <h2>小包套餐（邵武区）</h2>
             <p class="gray" >小包厢(5-10人)</p> 
             <p class="gray">今天<i>(01:00)</i>&nbsp;&nbsp;&nbsp;<i>21:00</i>开唱-<i>次日01:00</i>结束，唱<i>4</i>小时</p> 
             <p class="gray">小包厢(5-10人)</p> 
             <p class="gray"><i>不足7小时按照7小时计算</i></p> 
         </div>
         <div class="select-meal">
             <ul>
                 <li class="act">
                     <h1>7小时欢唱+238元酒水套餐</h1>
                     <p>酒水或饮品(雪花啤酒3瓶或大果粒橙一瓶+大椰汁套餐)2选1+瓜子1份+花生1份</p>
                     <div class="moneynum">￥190</div>
                 </li>
                 <li>
                     <h1>7小时欢唱+238元酒水套餐</h1>
                     <p>酒水或饮品(雪花啤酒3瓶或大果粒橙一瓶+大椰汁套餐)2选1+瓜子1份+花生1份</p>
                     <div class="moneynum">￥190</div>
                 </li>
             </ul>
             <a href="">抵用券</a>
             <div class="select-tel">
                 手机号码<span>13162731111</span>
             </div>
             <div class="go-pay">
                 <span>￥<i>977.00</i></span>
                 <label>去支付</label>
             </div>
         </div>
          
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='jsp/resources/js/rem.js'></script>
<script src='jsp/resources/js/jquery.min.js'></script>
<script>
    $('.select-meal li').click(function() {
        $('.select-meal li').removeClass('act');
        $(this).addClass('act');
    })
</script>
</html>
