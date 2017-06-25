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
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <title>盛世欢唱ktv</title>
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
       <label>请填写包房号：<a></a></label>
       <input type="text" placeholder="如：B27" id="room_num">
     </div>
     <div class="mui-content order-list"> 
        <ul id="OA_task_1" class="mui-table-view">
            <c:forEach var = "trolley" items="${trolleys}">
                <li class="mui-table-view-cell" id="li${trolley.goods_id}">
                    <div class="mui-slider-handle">
                        <img src="<%=basePath%>jsp/resources/img/2.png">
                        <div class="order-num">
                            <input type="hidden" id = "good_id" class="good_id" value="${trolley.goods_id}" />
                            <input type="hidden" class="price" value="${trolley.price}" />
                            <div class="order-num-info">
                                <em>+</em>
                                <div class="list-num"
                                     contenteditable="true">${trolley.num}</div>
                                <i>-</i>
                                <div class="clear"></div>
                            </div>
                            <p>
                                <span>${trolley.title}</span>
                                <img src="${trolley.image_url}">
                            <div class="clear"></div>
                            </p>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div class="mui-slider-right mui-disabled delete">
                        <a class="mui-btn mui-btn-red" onclick="delGoods(${trolley.goods_id})">删除</a>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="select-meal">
     <div class="go-pay">
         <span>￥<i>0.00</i></span>
         <label>结算</label>
     </div>
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
        var $listnum = $(this).parents('li').find('.list-num');
        var num = parseInt($listnum.html());
        if (num>0) {
            $listnum.html(num-1);
            //调用后台去减一

        }else{
            num=0;
        }
    });

    //加一
    $('.order-num em').click(function(){
        var $listnum = $(this).parents('li').find('.list-num');
        var num = parseInt($listnum.html());
        $listnum.html(num+1);
        //调用后台去加一
        var goodsId = $(this).parents('li').find('.good_id').val();
        updateNum(goodsId);
    });
})

//添加商品并
function updateNum(goodsId){
    $.ajax({
        'url': "${pageContext.request.contextPath}/shop/addShop.do",
        'type': 'post',
        'dataType': 'json',
        'data': {
            goodsId: goodsId,
        },
        success: function success(d) {
        }
    });
}

//删除种类
function delGoods(goodsId){
    $.ajax({
        'url': "${pageContext.request.contextPath}/shop/delCart.do",
        'type': 'post',
        'dataType': 'json',
        'data': {
            goodsId: goodsId,
            type: 1
        },
        success: function success(d) {
            //删除成功之后隐藏操作
            $('#li'+goodsId).hide();
        }
    });
}
//数量减一

//结算
$('.go-pay label').click(function() {
    var roomNum = $('#room_num').val();
    alert(roomNum);
    return false;
    window.location.href = packageJson.JAVA_DOMAIN  + '/shop/gotoPay.do?roomNum=' + roomNum;
})
    
</script>
</html>
