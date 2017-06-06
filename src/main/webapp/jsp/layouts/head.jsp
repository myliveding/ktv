<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.st.utils.JsonUtil"%>
<%@ page import="com.st.constant.Constant"%>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">

<link href="<%=JsonUtil.getJsonFile("framework7/css/framework7.ios.css") %>" type="text/css" rel="stylesheet" />
<link href="<%=JsonUtil.getJsonFile("css/framework7-wuyou-theme.css") %>" type="text/css" rel="stylesheet" />


<script type="text/javascript">
	var packageJson = {
		userId: "${sessionScope.userId}",//用户ID
		isNews: "${sessionScope.isNews}",//是否有未读消息（1是，0否，未登录为空）
		subscribe: "${sessionScope.subscribe}",//是否关注公众号，返回状态四种状态（'true','false','',1）,值为1的情况代表页面已弹窗
		checkuser: false,
		JAVA_STATICURL: window.location.protocol + '<%=Constant.staticUrl %>',//js,css地址前缀
		JAVA_DOMAIN: '${pageContext.request.contextPath}',//java链接前缀
		IMG_STATICURL: 'http://img.joyowo.com/',//上传后图片域名
		openId: '${sessionScope.openid}',//空或者字符串
		DPLUS_KEY: '<%=Constant.DPLUS_KEY %>',
		userName: '${sessionScope.memberTruename}'
	}
</script>

<script type="text/javascript" src="<%=JsonUtil.getJsonFile("framework7/js/framework7.js") %>"></script>

<script type="text/javascript" src="<%=JsonUtil.getJsonFile("js/common/common.js") %>"></script>
<script type="text/javascript" src="<%=JsonUtil.getJsonFile("js/jyApp/jyApp.js") %>"></script>
<script type="text/javascript" src="<%=JsonUtil.getJsonFile("js/util/util.js") %>"></script>