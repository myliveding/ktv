<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html >
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
<body id="about">  
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">关于盛世</h1>
     </div> 
    <div class="main" >
          <div class="about">
              <div class="logo">
                  <img src="resources/img/1.png">
              </div> 
               <div class="about-item">
                   <h1>盛世欢唱</h1>
                   <h2>我们专注KTV 却不只是KTV</h2>
                   <span class="about-item-box1"></span>
                   <span class="about-item-box2"></span>
                   <span class="about-item-box3"></span>
                   <label class="about-item-box1"></label>
                   <label class="about-item-box2"></label>
                   <label class="about-item-box3"></label>
               </div>
               <div class="about-info">
                   <img src="jsp/resources/img/5.png">
                   <p> 福建ktv介绍介绍介绍福建ktv介绍福建ktv介 绍介绍介绍福建ktv介绍介绍介绍福建ktv介绍介 绍介绍福建ktv介</p>
                   <h1>盛世大家庭</h1>
                   <ul>
                       <li>
                           <h4>(1)西门店</h4>
                           <p> 福建ktv介绍介绍介绍福建ktv介绍福建ktv介 绍介绍介绍福建ktv介绍介绍介绍福建ktv介绍介 绍介绍福建ktv介</p>
                           <img src="jsp/resources/img/6.png">
                       </li>
                       <li>
                           <h4>(1)西门店</h4>
                           <p> 福建ktv介绍介绍介绍福建ktv介绍福建ktv介 绍介绍介绍福建ktv介绍介绍介绍福建ktv介绍介 绍介绍福建ktv介</p>
                           <img src="jsp/resources/img/6.png">
                       </li>
                   </ul>
                   
               </div>
          </div>
    </div>
   
    <div class="footer">
        <ul>
            <li>
                <a href="jsp/index.jsp" class="footer-now red">
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
<script src='jsp/resources/js/rem.js'></script>
</html>
