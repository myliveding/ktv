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
    <title>ktv</title>
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body> 
     <div id="header"  style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b2.png">
         </a>
         <h1 >积分兑换订单详情</h1>
     </div> 
     <div class="main"> 
          <div class="exchange-info">
              <ul>
                  <li>
                      <span>兑换编号：</span>
                      <label >20170218204956545</label>
                  </li>
                  <li>
                      <span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
                      <label >1231**4564</label>
                  </li>
                  <li>
                      <span>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</span>
                      <label >182456456</label>
                  </li>
                  <li>
                      <span>选择区域：</span>
                      <label >邵武区</label>
                  </li>
                  <li>
                      <span>门店地址：</span>
                      <label >邵武区燕林广场</label>
                  </li> 
                  <li>
                      <span>包厢类型：</span>
                      <label >小包</label>
                  </li>
                  <li>
                      <span>套餐包含：</span>
                      <strong>啤酒12瓶，6个小碟，1个果盘，一盒纸巾，一套消毒杯子</strong>
                  </li>
                  <li>
                      <span>订单提交时间：</span>
                      <label >2017-02-23 19:22:23</label>
                  </li>
              </ul>
          </div> 
          <div class="score-consume">
              <div class="score-consume-num">
                  消耗积分：<span>5656分</span>
              </div>
              <div class="score-consume-status">
                  <span>订单状态：未消费</span>
                  <label>取消订单/退分</label>
              </div>
              <div class="clear"></div>
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
                <a href="" class="red">
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
