<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.utils.Constant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <title>盛世欢唱ktv</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""> 
    <meta name="description" content="">
    <link href="<%=basePath%>jsp/resources/css/basic.css" type="text/css" rel="stylesheet" />
  </head>
<body class="gray" >
<div class="statusbar-overlay"></div>
<div class="views">
	<div class="view view-main">
		<div class="pages">
			<div data-page="home" class="page navbar-fixed">
				<div class="navbar">
					<div class="navbar-inner">
						<div class="left"></div>
						<div class="center">支付成功</div>
						<div class="righjs-showMenuink icon-only js-showMenu"><i class="icon icon-bars"></i></span>
						</div>
					</div>
				</div>
				<div class="page-content">
						<section class="refundsuc white">
							<div class="row">
								<div class="col-25 tright"><i></i></div>
								<div class="col-75">
									<h3>支付成功</h3>
									<p class="pb10">我们已收到您的订单，新增客户的参保月以客服的确认为准，请保持手机畅通以便服务人员能及时联系到您！</p>
									<button class="js-detail">查看订单</button>
									<button class="js-share">分享</button>
								</div>
							</div>
						</section>
						<div class="list-block">
			                <ul>
			                   <li>
			                		<a href="" class="item-link item-content external">
			                    		<div class="item-inner">
			                        		<div class="item-title">了解首次参保需准备的资料</div>
			                      		</div>
			                      	</a>
			                   </li>
			                </ul>
			              </div>
						<p class="p10 tcenter tgray">平台咨询电话：<a class="phoneinfo"></a></p>
				</div>
			</div>
		</div>
	</div>
</div>

<style>
.setEmail {
	display: none;
	height: .6rem;
	line-height: .6rem;
	text-align: center;
	background-color: #FF9;
	margin: 0;	
	padding-right: 35px;
	background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20viewBox%3D'0%200%2060%20120'%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%3E%3Cpath%20d%3D'm60%2061.5-38.25%2038.25-9.75-9.75%2029.25-28.5-29.25-28.5%209.75-9.75z'%20fill%3D'%23c7c7cc'%2F%3E%3C%2Fsvg%3E");
	background-size: 10px 20px;
	background-repeat: no-repeat;
	background-position: 95% center;
	background-position: -webkit-calc(100% - 15px) center;
	background-position: calc(100% - 15px) center;
}
.setEmail, .setEmail:hover, .setEmail:link, .setEmail:visited {
	color: #F60;
	font-size: .22rem;
}

.list-block {
	margin: 20px 0;
}
.list-block .item-inner {
	padding: 0;
	height:.86rem;
	line-height: .86rem;
}
.list-block .item-title {
	background-image: url(<%=basePath%>jsp/resources/css/img/1.png);
	background-repeat: no-repeat;
	background-size: .38rem .44rem;
	background-position: .56rem center;
	text-indent: 1rem;
	font-size: .24rem;
}
.refundsuc button {
	width: auto;
	padding: 0 10px;
}
/* .modal { */
/*     width: 310px; */
/*     margin-left: -155px; */
/* } */
</style>




<script type="text/javascript" src="<%=basePath%>jsp/resources/js/jquery/jquery-1.8.0.js"></script>
<script src="//res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
var myApp = new Framework7();
var $$ = Dom7;
//读取公司信息
$$('.phoneinfo').attr('href','tel:' + CONFIG.TEL).text(CONFIG.TEL);
//添加菜单功能
jyApp.showMenu();

$$('.js-detail').click(function(){
	var OredrId = util.getUrlParam('param1');
	window.location.href="${pageContext.request.contextPath}/jsp/my/myorderdetail.jsp?OredrId=" + OredrId;
});
//添加菜单功能
jyApp.showMenu();
//判断是否显示添加邮箱
$$.ajax({
	'url': '${pageContext.request.contextPath}/personsocial/userinfo.do',
	success: function(d){
		if(d.status == 0){
			if(d.data.member_email == ''){
				$$('.js-setEmail').show();
			}
			
		}
	},error: function(){
        myApp.hideIndicator();
        myApp.alert('加载失败，请刷新重试！');
    }
});
$$.ajax({
    url: packageJson.JAVA_DOMAIN+util.ajaxUrl.getActivityRemark,
    data: {triggerSence:3},//1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
    success: function(d){
    	if (d.status == "0") {
    		var activitytxt= d.data.remark;
    		myApp.params.modalButtonCancel="<i>取消</i>";
			myApp.params.modalButtonOk="给伙伴送代金券";
			myApp.confirm('<div class="share"><i class="alert-icon alert-icon-03"></i><p>恭喜您获得给伙伴发礼包的机会</p><span>代金券可用于抵服务费，赶快分享吧！</span></div>', 
				function () {
		        //clicked Ok button
		        $(".js-share").click();
		      },
		      function () {
		        //clicked Cancel button
		    	   
		      }
		    );
			myApp.params.modalButtonOk="Ok";
			myApp.params.modalButtonCancel="Cancel";
    	}
    	
    },error: function(){
        myApp.hideIndicator();
        myApp.alert('加载失败，请刷新重试！');
    }
})
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
	
	var _title = "${title}",
	_desc = "${desc}",
	_link = '${shareurl}';
	
	_title = _title == ""?'推荐您一个在线缴社保工具，躺着就能把社保交了，赶紧来试试吧':_title;
	_desc = _desc == ""?'无忧保-为个体提供微信在线自助缴纳社保，免费专家咨询等全方位专业服务，开启社保服务新时代！':_desc;
	
	jyApp.share({
	title: _title,
	desc: _desc,
	imgUrl: packageJson.JAVA_STATICURL + "/images/share_index.png",
	link: _link,
	type: "1"
	});
dplus.track("支付成功", {"用户名" : packageJson.userName});//dplus埋点-确认支付
</script>


<jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body>
</html>
