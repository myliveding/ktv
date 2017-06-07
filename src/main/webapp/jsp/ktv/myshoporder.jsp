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
    <title>ktv</title>
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body>  
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">我的超市订单</h1>
     </div> 
    <div class="main" >
         <div class="shop-order">
             <ul>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
             </ul>
             <div class="shop-order-total">
                 <h1>共<i>6</i>件商品  合计：￥<i>600</i>元</h1>
                 <h3>包厢号：B98</h3>
                 <h2>
                     <p>订单编号：109290897</p>
                     <span class="shop-order-go">去支付</span>
                     <span class="shop-order-no">取消订单</span>
                 </h2>
             </div>
         </div>
         <div class="shop-order">
             <ul>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
                 <li>
                     <img src="jsp/resources/img/2.png">
                     <p>雪津啤酒一件24瓶，雪津啤酒一件24瓶 雪津啤酒一件24瓶，</p>
                     <span>￥100</span>
                     <i>x 2</i>
                 </li>
             </ul>
             <div class="shop-order-total">
                 <h1>共<i>6</i>件商品  合计：￥<i>600</i>元</h1>
                 <h3>包厢号：B98</h3>
                 <h2>
                     <p>订单编号：109290897</p>
                     <span class="shop-order-go">去支付</span>
                     <span class="shop-order-no">取消订单</span>
                 </h2>
             </div>
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
                <a href="about.jsp" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="integralmall.jsp" class="red">
                    <img src="jsp/resources/img/h3.png">
                    <span>积分商城</span>
                </a>
            </li>
            <li>
                <a href="usercenter.jsp" class="red">
                    <img src="jsp/resources/img/h4.png">
                    <span>我的盛世</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
</body> 
<script src='jsp/resources/js/rem.js'></script>
</html>
