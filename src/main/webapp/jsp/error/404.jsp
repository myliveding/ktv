<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.st.utils.JsonUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.st.constant.Constant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
	<title>无忧保_个体社保代缴-无忧保</title>
	<meta charset="utf-8">
	<meta name="keywords" content="">
	<meta name="description" content="">
	<jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
  </head>
<body class="home gray">
<div class="statusbar-overlay"></div>
<div class="views">
	<div class="view view-main">
		<div class="pages">
			<div data-page="home" class="page">
				<div class="page-content">
					<img class="img-404" src="<%=Constant.staticUrl %>/images/error/404.png">
					<a href="${pageContext.request.contextPath}/weixin/getweixin.do?name=index/index" class="go-to-index external">返回首页</a>
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

<jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
<jsp:include page="/jsp/layouts/pv.jsp" flush="true"/>
</body>
</html>
