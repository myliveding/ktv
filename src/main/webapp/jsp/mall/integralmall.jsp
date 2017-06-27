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
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body> 
     <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="<%=basePath%>jsp/resources/img/b3.png">
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
          	 		<a><input type="hidden" class="cate_id" value="1" />礼品类</a>
      	 		</li>
          	 	<li>
          	 		<a><input type="hidden" class="cate_id" value="2" />包厢类</a>
      	 		</li>
          	 	<li>
          	 		<a><input type="hidden" class="cate_id" value="3" />酒水类</a>
      	 		</li>
          	 	<div class="clear"></div>
          	 </ul>
          </div>
          <div class="score-list">
          	 <ul>
                 <c:forEach var="mall" items="${malls}">
                     <li>
                         <img src="${mall.image_url}">
                         <div class="score-list-info">
                             <h2>${mall.name}</h2>
                             <span>积分：<i>${mall.need_integral}分</i></span>
                         </div>
                         <a onclick="gotoDetail(${mall.id})">查看详情</a>
                         <div class="clear"></div>
                     </li>
                 </c:forEach>
          	 </ul>
          </div>
     </div>
     <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
<script>
    $(document).ready(function(){
        $('.cate-mall a:eq(0)').addClass('act');
        //getIntegralMalls(1);
    });

    $('.cate-mall a').click(function() {
        $('.cate-mall a').removeClass('act');
        $(this).addClass('act');
        //调用去获取
        var cateId = $(this).find('.cate_id').val();
        getIntegralMalls(cateId);
    })

    //获取积分商品列表
    function getIntegralMalls(cateId){
        $.ajax({
            'url': "${pageContext.request.contextPath}/mall/getCate.do",
            'type': 'post',
            'dataType': 'json',
            'data': {
                cateId: cateId
            },
            success: function success(d) {
                if (d.error_code == 0) {
                    if(typeof(d.result) != "undefined"){
                        $(".score-list ul").html('');
                        var str = '';
                        for(var i=0; i< d.result.length; i++){

                            str += '<li>';
                            str += '<img src="' + d.result[i].image_url + '">';
                            str += '<div class="score-list-info">';
                            str += '<h2>' + d.result[i].name + '</h2>';
                            str += '<span>积分：<i>' + d.result[i].need_integral + '分</i></span>';
                            str += '</div>';
                            str += '<a onclick="gotoDetail(' + d.result[i].id + ')">查看详情</a>';
                            str += '<div class="clear"></div>';
                            str += '</li>';
                        }
                        $(".score-list ul").html(str);
                    }
                } else {
                    alert(d.msg);
                }
            }
        });
    }


    //进入积分详情
    function gotoDetail(integralMallId){
        window.location.href = '${pageContext.request.contextPath}/mall/gotoMallDetail.do?integralMallId=' + integralMallId;
    }
</script>
</html>
