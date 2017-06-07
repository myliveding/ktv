<%@ page language="java" import="com.st.constant.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
	<title>无忧保_个体社保代缴-无忧保</title>
	<meta charset="utf-8">
	<meta name="keywords" content="无忧保,个人社保代缴,社会保险，五险一金">
	<meta name="description" content="无忧保提供个人社保代缴(五险一金、社会保险)服务。无忧保由中国人社部认证许可，拥有完善的资金监管体系，社保资金由招商银行进行资金监管，无忧保关心每个个体对安定美好生活的追求，以微信端便捷操作，满足个人社保缴纳(五险一金、社会保险)需要。">
	<jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
	<link href="css/index/index.css") %>" type="text/css" rel="stylesheet" />
  </head>
<body class="home gray">
<div class="statusbar-overlay"></div>
<div class="views">
	<div class="view view-main">
		<div class="pages">
			<div data-page="home" class="page">
				<div class="page-content">
					<div class="top-message">
						<img class="top-message-icon" src="/img/icon/1.png">
							<div class="top-message-roll">
								<ul>
									<c:forEach var = "infodata" items ="${info.buyList}">
										<li>${infodata.insurerName}购买了${infodata.productMonth}个月套餐</li>
								</c:forEach>
							</ul>
						</div>
						<a class="external top-message-my" href="${pageContext.request.contextPath}/my/">
							<img src="<%=Constant.staticUrl %>/images/icon/30.png">
							<i class="prompt"></i>
						</a>
					</div>
					<div class="top-enter">
						<div class="top-enter-pay">
							<a class="external" href="${pageContext.request.contextPath}/jsp/payshebao/selectcity.jsp">
								<img src="<%=Constant.staticUrl %>/images/index/1.png" alt="无忧保"><h2>缴社保</h2>
							</a>
							<i class="dynamicTip icon-bounce">待续</i>
						</div>
						<div class="top-enter-calculator">
							<a class="external" href="${pageContext.request.contextPath}/jisuanqi/">
								<img src="<%=Constant.staticUrl %>/images/index/2.png" alt="无忧保"><h2>算保费</h2>
							</a>
						</div>
						<div class="top-enter-query">
							<a class="external" href="${pageContext.request.contextPath}/shebaochaxun/">
								<img src="<%=Constant.staticUrl %>/images/index/3.png" alt="无忧保"><h2>查社保</h2>
							</a>
						</div>
					</div>
					<div class="swiper-container banner-swiper">
						<div class="swiper-wrapper">
							<c:forEach var = "bannerdata" items ="${banner.data}">
								<div class="swiper-slide" data-href="${bannerdata.adv_url}" data-id="${bannerdata.adv_id}">
									<a class="" href="#" >
										<img src="${bannerdata.content}" alt="${bannerdata.title}"></a>
								</div>
							</c:forEach>
						</div>
						<div class="swiper-pagination banner-pagination"></div>
					</div>
					<div class="index-content">
						<a class="external content-left" href="${pageContext.request.contextPath}/shebaofuwu/">
							<img class="img" src="<%=Constant.staticUrl %>/images/index/4.png" alt="个人社保代缴">
							<div class="text">
								<p class="text-title">服务内容</p>
								<p class="text-content">社保难题一站搞定</p>
							</div>
						</a>
						<a class="external content-right" href="${pageContext.request.contextPath}/shebaodating/">
							<img class="img" src="<%=Constant.staticUrl %>/images/index/5.gif" alt="个人社保代缴">
							<div class="text">
								<p class="text-title">在线咨询</p>
								<p class="text-content">一对一专家解答</p>
							</div>
						</a>
					</div>
					<div class="index-content">
						<a class="external content-left" href="${pageContext.request.contextPath}/zhishebao/">
							<img class="img" src="<%=Constant.staticUrl %>/images/index/6.png" alt="个人社保代缴">
							<div class="text">
								<p class="text-title">社保资讯</p>
								<p class="text-content">各地政策解读</p>
							</div>
						</a>
						<a class="external content-right" href="${pageContext.request.contextPath}/canbaozhinan/">
							<img class="img" src="<%=Constant.staticUrl %>/images/index/7.png" alt="个人社保代缴">
							<div class="text">
								<p class="text-title">参保指南</p>
								<p class="text-content">如何3分钟缴社保</p>
							</div>
						</a>
					</div>
					<div class="thematic">
						<a class="external thematic-head" href="${pageContext.request.contextPath}/zhoukan/"><h3>社保周刊</h3></a>
						<div class="thematic-roll">
							<div class="swiper-container thematic-swiper">
								<div class="swiper-wrapper">
									<c:forEach var = "zkdata" items ="${zk.data}">
										<div class="swiper-slide">
											<a class="external" href="${pageContext.request.contextPath}/zhoukan_${zkdata.article_id}/">
												<img src="${zkdata.s_image}" alt="${zkdata.title}"></a>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
					<a class="external banner-recruit" href="${pageContext.request.contextPath}/chengshidaili/">
						<img class="recruit-img" src="<%=Constant.staticUrl %>/images/index/9.png" alt="个人社保代缴">
						<div class="recruit-text">
							<p class="recruit-title">社保代理商招募了！</p>
							<p class="recruit-des">一个轻松年入百万的机会  </p>
						</div>
					</a>
					<div class="common-problem">
						<a class="external problem-head" href="${pageContext.request.contextPath}/shebaodating/"><h3>常见问题</h3></a>
						<ul class="problem-list">
							<c:forEach var = "wtdata" items ="${wt.data}">
								<li>
									<a href="${pageContext.request.contextPath}/shebaodating_${wtdata.article_id}/" class="external"><p class="problem-q">
										<c:choose>
											<c:when test="${fn:length(wtdata.title)>19}">
												<span>Q：</span>${wtdata.title.substring(0,19)}...
											</c:when>
											<c:otherwise>
												<span>Q：</span>"${wtdata.title}"
											</c:otherwise>
										</c:choose>
									</p>
									<p class="problem-a"><!-- <i class="time">${wtdata.release_date.substring(0,10)}</i> -->
										<c:choose>
											<c:when test="${fn:length(wtdata.title)>17}">
												<span>A：</span>${wtdata.summary.substring(0,17)}...
											</c:when>
											<c:otherwise>
												<span>A：</span>${wtdata.summary}
											</c:otherwise>
										</c:choose>
									</p>
									</a>
								</li>
							</c:forEach>
						</ul>
					</div>
					<c:if test="${tj.total.count>0}">
					<div class="common-problem reading">
						<a class="external problem-head" href="${pageContext.request.contextPath}/zhishebao/"><h3>推荐阅读</h3></a>
						<ul class="problem-list">
							<c:forEach var = "tjdata" items ="${tj.data}">
								<li>
									<a href="${pageContext.request.contextPath}/zhishebao_${tjdata.article_id}/" class="external">
										<img src="${tjdata.s_image}">
										<p class="title">${tjdata.title}</p>
									</a>
								</li>
							</c:forEach>
						</ul>
					</div>
					</c:if>
					<div class="index-footer">
						<p class="footer-tel">咨询热线：<a href="tel:400-111-8900" class="external">400-111-8900</a></p>
						<h1 class="footer-text">无忧保, 社保资金由招商银行监管</h1>
						<p class="footer-text">&copy;2017 无忧保 浙ICP备15027503号-1</p>
						<a href="${pageContext.request.contextPath}/maps/" class="external footer-maps">网站导航</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="enter-fixed">
	<i class="dynamicTip icon-bounce">待续</i>
	<a class="external right-img" href="${pageContext.request.contextPath}/my/">
		<img src="<%=Constant.staticUrl %>/images/icon/30.png">
		<i class="prompt"></i>
	</a>
	<a class="external left-img" href="${pageContext.request.contextPath}/jsp/payshebao/selectcity.jsp">
		<img src="<%=Constant.staticUrl %>/images/index/10.png">
	</a>
	<a class="external left-img" href="${pageContext.request.contextPath}/jisuanqi/">
		<img src="<%=Constant.staticUrl %>/images/index/11.png">
	</a>
	<a class="external left-img" href="${pageContext.request.contextPath}/shebaochaxun/">
		<img src="<%=Constant.staticUrl %>/images/index/12.png">
	</a>
</div>

<script src="//res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
	//分享配置
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
	//分享功能
	jyApp.share({
		title: "手机也能缴社保了！",
	   	desc: "无忧保——你的掌上社保管家",
	   	imgUrl: packageJson.JAVA_STATICURL + "/images/share_index.jpg",
	   	link: "<%=Constant.URL%>/jsp/middlepage/index.jsp?name=index"
	   	// link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=Constant.APP_ID%>&redirect_uri=<%=Constant.URL%>/scope/openid.do?next=personsocial/gotoindex.do<%=Constant.APP_ID%>invitCode=${invCode}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
	});
</script>

<script src="<%=Constant.staticUrl %>/js/plugins/jquery/3.1.1/jquery.min.js"></script>
<script src="<%=JsonUtil.getJsonFile("js/index/index.js") %>"></script>


<jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
<jsp:include page="/jsp/layouts/pv.jsp" flush="true"/>
</body>
</html>
