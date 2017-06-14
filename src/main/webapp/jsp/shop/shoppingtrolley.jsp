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
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/mui.min.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/app.css">
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body > 
     <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世在线超市-购物车</h1>
     </div> 
     <div class="main" style="background: #f8f8f8;">
         <div class="writeroomnum">
         <label for="">请填写包房号：</label>
         <input type="text" placeholder="如：B27">
     </div>
     <div class="mui-content order-list"> 
        <ul id="OA_task_1" class="mui-table-view">
            <li class="mui-table-view-cell">
                <div class="mui-slider-right mui-disabled delete">
                    <a class="mui-btn mui-btn-red">删除</a>
                </div>
                <div class="mui-slider-handle">
                   <img src="<%=basePath%>jsp/resources/img/2.png">
                   <div class="order-num">
                        <div class="order-num-info">
                            <em>+</em>
                            <div class="list-num" 
                           contenteditable="true">0</div>
                            <i>-</i>
                            <div class="clear"></div>
                        </div>
                        <p>
                            <span>雪津啤酒散装1支装1支装1支装1支装</span>
                            <img src="<%=basePath%>jsp/resources/img/b4.png">
                            <div class="clear"></div>
                        </p>
                   </div>
                   <div class="clear"></div>
                </div>
            </li>
             <li class="mui-table-view-cell">
                <div class="mui-slider-right mui-disabled delete">
                    <a class="mui-btn mui-btn-red">删除</a>
                </div>
                <div class="mui-slider-handle">
                   <img src="<%=basePath%>jsp/resources/img/2.png">
                   <div class="order-num">
                        <div class="order-num-info">
                            <em>+</em>
                            <div class="list-num" 
                           contenteditable="true">0</div>
                            <i>-</i>
                            <div class="clear"></div>
                        </div>
                        <p>
                            <span>雪津啤酒散装1支装</span>
                            <img src="<%=basePath%>jsp/resources/img/b4.png">
                            <div class="clear"></div>
                        </p>
                   </div>
                   <div class="clear"></div>
                </div>
            </li>
        </ul>
    </div>
     </div>

     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script src='<%=basePath%>jsp/resources/js/mui.min.js'></script>
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script>
$(function(){

    $('.list-num').keyup(function(){ 

        var re=new RegExp('^\d+$');
        var $this=$(this);
        if (re.test($this.html())) { 
        }else{
            alert('必须为整数！');
            $this.html('0');
        }
    })
    //减一
        $('.order-num i').click(function(){
            var $listnum=$(this).parents('li').find('.list-num');
            var num=parseInt($listnum.html());
            if (num>0) {
                $listnum.html(num-1);
            }else{
                num=0;
            }
        });

        //加一
        $('.order-num em').click(function(){
            var $listnum=$(this).parents('li').find('.list-num');
            var num=parseInt($listnum.html());
            $listnum.html(num+1); 
        });
    })

    mui.init();
    (function($) {
         
        $('.mui-table-view').on('tap', '.mui-btn', function(event) {
            var elem = this;
            var li = elem.parentNode.parentNode;
            // mui.confirm('确认删除该条记录？', 'Hello MUI', btnArray, function(e) {
                // if (e.index == 0) {
                    li.parentNode.removeChild(li);
            //  } else {
            //      setTimeout(function() {
            //          $.swipeoutClose(li);
            //      }, 0);
            //  }
            // });
        });
        var btnArray = ['确认', '取消'];
    })(mui);

    //数量减一

    
</script>
</html>
