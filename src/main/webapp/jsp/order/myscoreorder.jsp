<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.utils.Constant"%>
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
    <link rel="stylesheet" href="jsp/resources/css/reset.css">
    <link rel="stylesheet" href="jsp/resources/css/pullToRefresh.css">
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body>  
    <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b2.png">
         </a>
         <h1 >我的积分兑换订单</h1>
     </div> 
    <div class="main" >
        <div class="scoreorder" id="wrapper">
            <ul>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div>
                    <div class="scoreorder-detail"> 
                        <a href="">查看详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div> 
                    <div class="scoreorder-detail"> 
                         <a href="orderselect.jsp" class="scoreorder-go">确认开机</a>
                         <a class="noact" href="">详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div> 
                    <div class="scoreorder-detail"> 
                         <a href="orderselect.jsp" class="scoreorder-go">确认开机</a>
                         <a class="noact" href="">详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div> 
                    <div class="scoreorder-detail"> 
                         <a href="orderselect.jsp" class="scoreorder-go">确认开机</a>
                         <a class="noact" href="">详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div> 
                    <div class="scoreorder-detail"> 
                         <a href="orderselect.jsp" class="scoreorder-go">确认开机</a>
                         <a class="noact" href="">详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <img src="jsp/resources/img/p1.png">
                    <div class="scoreorder-info">
                        <p>兑换编号20170218204956545</p>
                        <h1>小包套餐（邵武区）</h1>
                        <span>消耗积分：<i>6760分</i></span>
                    </div> 
                    <div class="scoreorder-detail"> 
                         <a href="orderselect.jsp" class="scoreorder-go">确认开机</a>
                         <a class="noact" href="">详情></a>
                    </div>
                    <div class="clear"></div>
                </li>
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
                <a href="../ktv/about.jsp" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="../my/integralmall.jsp" class="red">
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
<script src='jsp/resources/js/jquery.min.js'></script>
<script src='jsp/resources/js/iscroll.js'></script>
<script src='jsp/resources/js/pullToRefresh.js'></script>
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
            var el, li, i;
            el =document.querySelector("#wrapper ul");
            $.ajax({
                type:"POST",
                url:"../ktv/php/local.php", 
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
