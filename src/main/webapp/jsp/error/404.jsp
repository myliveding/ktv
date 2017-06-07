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
	<title>盛世</title>
	<meta charset="utf-8">
	<meta name="keywords" content="">
	<meta name="description" content="">
  </head>
<body class="home gray">
<div class="statusbar-overlay"></div>
<div class="views">
	<div class="view view-main">
		<div class="pages">
			<div data-page="home" class="page">
				<div class="page-content">
					<img class="img-404" src="jsp/resources/img/404.png">
					<a href="${pageContext.request.contextPath}/weixin/getWeixintoIndex.do" class="go-to-index external">返回首页</a>
				</div>
			</div>
		</div>
	</div>
</div>

<style type="text/css">
.page { background: #FFF; }
.img-404 { width: 100%; display: block; }
.go-to-index {
	position: fixed;
	top: 80%;
	left: 50%;
	margin-left: -1.9rem;
	width: 3.8rem;
	height: .8rem;
	background: #2cca6f;
	color: #FFF;
	border-radius: 4px;
	text-align: center;
	line-height: .8rem;
	font-size: .34rem;
}
</style>

</body>
</html>
