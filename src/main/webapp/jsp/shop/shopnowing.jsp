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
    <title>盛世欢唱ktv</title> 
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body > 
     <div id="header" style="border-bottom:1px solid #ccc;">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世在线超市</h1>
     </div> 
     <div class="main">
     	<div class="select-nav">
	      <div class="select-list select-listtab">
	      	   <a href="javascript:void(0);">
                   <input type="hidden" class="cate_id" value="0" />
                   精选
               </a>
	      </div>
			<c:forEach var = "cate" items ="${cates}">
                <div class="select-list">
                    <a href="javascript:void(0);">
                        <input type="hidden" class="cate_id" value="${cate.id}" />
                    ${cate.name}
                    </a>
                </div>
			</c:forEach>
	      <div class="select-img">
	      	   <img src="<%=basePath%>jsp/resources/img/b4.png" >
	      </div> 
	  	  <div class="clear"></div>	
	    </div>
	    <div class="adv">
	    	<img src="<%=basePath%>jsp/resources/img/3.png">
	    </div>
	    <div class="service">
	    	<div class="service-item">
	    		<img src="<%=basePath%>jsp/resources/img/s11.png">
	    		<span>便捷点单</span>
	    	</div>
	    	<div class="service-item">
	    		<img src="<%=basePath%>jsp/resources/img/s11.png">
	    		<span>快速送达</span> 
	    	</div>
	    	<div class="service-item">
	    		<img src="<%=basePath%>jsp/resources/img/s11.png">
	    		<span>尽享优惠</span>
	    	</div>
	    	<div class="clear"></div>
	    </div>
	    <div class="goods">
	    	<ul>
                <c:forEach var = "good" items ="${goods}">
                    <li>
                        <img src="${good.image_url}">
                        <p>${good.title}</p>
                        <span>${good.price}</span>
                        <em><input type="hidden" class="good_id" value="${good.id}" />
                            添加</em>
                    </li>
                </c:forEach>
	    		<div class="clear"></div>
	    	</ul>
	    </div>
     </div>

    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
    <div id="trolley">
    	<a>
			<span>
    			<img src="<%=basePath%>jsp/resources/img/g2.png">
    		</span>
			<em><label>${cart.goods_count}</label>件</em>
    	</a>
    	<p class = "tijiao">
    		<span>
    			<img src="<%=basePath%>jsp/resources/img/g1.png">
    		</span>
			<i>提交</i>
    	</p>
    </div>
</body> 
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script>
	$(function(){
        updateTrollerNum();
    });

    var goodsId;
    function updateTrollerNum(){
        //购物车加一
        $('.goods li em').click(function(){
            var $num = parseFloat($('#trolley').find('label').html());
            //$('#trolley').find('label').html($num + 1);
            goodsId = $(this).find('.good_id').val();
            updateNum(goodsId);
        });
    }

    //添加商品并获取购物车里面的数量
    function updateNum(goodsId){
        $.ajax({
            'url': "${pageContext.request.contextPath}/shop/addShop.do",
            'type': 'post',
            'dataType': 'json',
            'data': {
                goodsId: goodsId,
                type: 1
            },
            success: function success(d) {
                if (d.error_code == 0) {
                    $('#trolley').find('label').html(d.result.goods_count);
                } else {
                    alert(d.msg);
                }
            }
        });
    }


    //选择超市商品分类
    $('.select-nav .select-list').click(function() {
        $('.select-nav .select-list').removeClass('select-listtab');
        $(this).addClass('select-listtab');
        //调用去获取
        cateId = $(this).find('.cate_id').val();
        getGoods(cateId);
    })

	//获取分类下面的商品
	function getGoods(cateId){
		$.ajax({
			'url': "${pageContext.request.contextPath}/shop/getCateGoods.do",
			'type': 'post',
			'dataType': 'json',
			'data': {
                id: cateId,
			},
			success: function success(d) {
				if (d.error_code == 0) {
					$(".goods ul").html('');
					var str = '';
					if(typeof(d.result) != "undefined"){
						for(var i=0; i< d.result.length; i++){
							str = str + '<li>'
                                    + '<img src="' + d.result[i].image_url + '">'
									+ '<p>' + d.result[i].title + '</p><span>'
                                    + d.result[i].price
                                    + '</span><em><input type="hidden" class="good_id" value="' + d.result[i].id + '"/>添加</em></li>';
						}
						str += '<div class="clear"></div>';
						$(".goods ul").html(str);
                        updateTrollerNum();
					}
				} else {
					alert(d.msg);
				}
			}
		});
	}

    $('.tijiao').click(function() {
        window.location.href = '${pageContext.request.contextPath}/shop/gotoTrolley.do';
    })
</script>
</html>
