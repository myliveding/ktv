<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.constant.Constant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<!DOCTYPE html>
<html>
  <head>
    <title>${meta_title}</title>
    <meta charset="utf-8">
    <meta name="keywords" content="${meta_keyword}"> 
    <meta name="description" content="${meta_description}">
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <link href="<%=JsonUtil.getJsonFile("css/basic.css") %>" type="text/css" rel="stylesheet" />
  </head>
<body>
<div class="statusbar-overlay"></div>
<div class="views">
	<div class="view view-main">
		<div class="pages toolbar-through">
			<div data-page="home" class="page navbar-fixed">
				<div class="navbar navbar-white">
					<div class="navbar-inner">
						<div class="left">
							<a href="#" class="link icon icon-only J-back">
								<i class="icon icon-back"></i>
							</a>
						</div>
						<div class="center">详情</div>
						<div class="right">
							<span class="js-showMenu"><i class="icon icon-bars"></i></span>
						</div>
					</div>
				</div>
				<div class="page-content">
					<article class="article-box">
				        <h1 class="tleft pl10">${title}</h1>
				        <div class='tleft pl10'>${create_time}</div>
				        <div class="article-info">
				            <p>${description}</p>
				        </div>
				    </article>
				</div>
				<jsp:include page="/jsp/layouts/nav.jsp" flush="true"/>
				<!-- <div class="toolbar"><a class="share-btn js-share" href="javascript:;">分享</a></div> -->
			</div>
		</div>
	</div>
</div>



<style>
.article-info {
	padding: 5px;
	margin: 15px 10px 0;
	border-top: 1px solid #E7E7E7;
}
.toolbar.activity {
    position: absolute;
    height: 60px;
    
}
.toolbar .share-btn {
	display: block;
	height: 100%;
	background-color: #2e6eb7;
	color: #FFF;
	line-height: 44px;
	text-align: center;
}

.article-box {
	padding-top: 0.2rem;
}

.article-box h2{
	padding: 0;
	margin: 10px 0 15px;
	font-weight: bold;
}

a.activity-url {
    
    width: 90%;
    height: 44px;
    line-height: 44px;
    font-size: 18px;
    background-color: #FF6000;
    border: 1px solid #FF6000;
    margin-left: 5%;
    text-align: center;
    color: #fff;
    border-radius: 5px;
    margin-top: 5px;
    display: block;
}
.article-box h2 {
	margin-left:0;
	white-space: initial;
	width:100%;
}

article h2.tleft,.tleft {
	text-align: center;
	margin: 0;
	padding-top: 0.1rem;
}
img {
    width: 100%;
}

.article-info p {
	margin: 0;
}
.article-info table {
	margin: 0 auto;
	text-align: center;
}
.article-info img {
	max-width: 7rem;
	margin: 0.2rem auto;
	display: block;
}

</style>





<script type="text/javascript" src="js/jquery/jquery-1.8.0.js"" ></script>
<script src="//res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
var myApp = new Framework7();
var $$ = Dom7;
//添加菜单功能
jyApp.showMenu();
//背景白色
$$('.page-content p').css('background-color', '#fff');
$$(".activity-url").parent().addClass("toolbar activity");
window.onscroll=function(){
	$(".activity-url").parent().css("top",$(window).scrollTop()+$(window).height());

}
//分享功能
wx.config({
	debug: false,//是否开启调试模式
	appId: "${appid}",// 必填，公众号的唯一标识
	timestamp: "${timestamp}", // 必填，生成签名的时间戳
	nonceStr: "${noncestr}", // 必填，生成签名的随机串
	signature: "${signature}",// 必填，签名
	jsApiList: [
	    'checkJsApi',
	    'onMenuShareTimeline',
	    'onMenuShareAppMessage',
	    'onMenuShareQQ',
	    'onMenuShareWeibo'
	]
});

var _title = '${title}',
	_desc = '无忧保-提供微信在线自助缴纳社保，免费专家咨询等全方位社保服务，让你社保无忧！',
	_link = '${shareurl}',
	_articleId = util.getUrlParam('articleId') || '';

if(_articleId == 1003){
	//关于我们
	_title = '无忧保——个体在线自助缴纳平台，你身边的社保管家';
	_desc = '无忧保-为个体提供微信在线自助缴纳社保，免费专家咨询等全方位专业服务，开启社保服务新时代！';
}else if(_articleId == 1002){
	//服务套餐
	_title = '还在为交社保而烦恼？用无忧保躺着就能把社保交了，赶紧来试试吧';
	_desc = '无忧保3/6/12个月服务套餐，解决您社保缴纳的烦恼';
}

jyApp.share({
	title: _title,
   	desc: _desc,
   	imgUrl: "${shareImg}" || (packageJson.JAVA_STATICURL + "/images/share_logo.png"),
   	link:_link
});




$$('title').text("${meta_title}");
$$('meta[name="keywords"]').attr('content',"${meta_keyword}");
$$('meta[name="description"]').attr('content',"${meta_description}");



// 使文章内部的a标签生效
$$('.article-box a').each(function(){
	$$(this).addClass('external');
})


</script>


<jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
<jsp:include page="/jsp/layouts/pv.jsp" flush="true"/>
</body>
</html>
