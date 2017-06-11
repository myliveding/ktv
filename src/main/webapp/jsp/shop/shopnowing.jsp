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
<body > 
     <div id="header" style="border-bottom:1px solid #ccc;">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">盛世在线超市</h1>
     </div> 
     <div class="main">
     	<div class="select-nav">
	      <div class="select-list select-listtab">
	      	   <a href="">精选</a>
	      </div>
	      <div class="select-list">
	      	   <a href="">啤酒饮料</a>
	      </div>
	      <div class="select-list">
	      	   <a href="">小碟/卤味</a>
	      </div>
	      <div class="select-list">
	      	   <a href="">其他酒类</a> 
	      </div>
	      <div class="select-list">
	      	   <a href="">零食</a>
	      </div> 
	      <div class="select-img">
	      	   <img src="jsp/resources/img/b4.png" >
	      </div> 
	  	  <div class="clear"></div>	
	    </div>
	    <div class="adv">
	    	<img src="jsp/resources/img/3.png">
	    </div>
	    <div class="service">
	    	<div class="service-item">
	    		<img src="jsp/resources/img/s11.png">
	    		<span>便捷点单</span>
	    	</div>
	    	<div class="service-item">
	    		<img src="jsp/resources/img/s11.png">
	    		<span>快速送达</span> 
	    	</div>
	    	<div class="service-item">
	    		<img src="jsp/resources/img/s11.png">
	    		<span>尽享优惠</span>
	    	</div>
	    	<div class="clear"></div>
	    </div>
	    <div class="goods">
	    	<ul>
	    		<li>
	    			<img src="jsp/resources/img/2.png">
	    			<p>百威啤酒一箱500ML，美味 啤酒。</p>
	    			<span>162元</span>
	    			<em>添加</em>
	    		</li>
	    		<li>
	    			<img src="jsp/resources/img/2.png">
	    			<p>百威啤酒一箱500ML，美味 啤酒。</p>
	    			<span>162元</span>
	    			<em>添加</em>
	    		</li>
	    		<li>
	    			<img src="jsp/resources/img/2.png">
	    			<p>百威啤酒一箱500ML，美味 啤酒。</p>
	    			<span>162元</span>
	    			<em>添加</em>
	    		</li>
	    		<div class="clear"></div>
	    	</ul>
	    </div>
     </div>

	 <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
    <div id="trolley">
    	<a href="shoppingtrolley.jsp">
			<span>
    			<img src="jsp/resources/img/g2.png">
    		</span>
			<em><label>23</label>件</em> 
    	</a>
    	<p>
    		<span>
    			<img src="jsp/resources/img/g1.png">
    		</span>
			<i>提交</i>  
    	</p>
    </div>
</body> 
<script src='jsp/resources/js/jquery.min.js'></script>
<script src='jsp/resources/js/rem.js'></script>
<script>
	$(function(){ 
        //购物车加一
        $('.goods li em').click(function(){
            var $num=parseFloat($('#trolley').find('label').html()); 
            $('#trolley').find('label').html($num+1);
        });
    });
    
    
</script>
</html>
