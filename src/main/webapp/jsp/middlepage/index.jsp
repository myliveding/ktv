<%@ page language="java" import="com.st.constant.Constant" pageEncoding="utf-8"%>
<%@ page import="com.st.utils.JsonUtil"%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
	<title>无忧保</title>
	<meta charset="utf-8">
  </head>
<body>
<script>

var getUrlParam = function (name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r !== null) return r[2];
	return null;
}

var name = getUrlParam('name');

var goUrl = '';

if ( name == 'index' ) {
	goUrl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=Constant.APP_ID%>&redirect_uri=<%=Constant.URL%>/scope/openid.do?next=personsocial/gotoindex.do<%=Constant.APP_ID%>&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect'
}else if ( name == 'calculator' ) {
	goUrl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=Constant.APP_ID%>&redirect_uri=<%=Constant.URL%>/scope/openid.do?next=personsocial/gotojsq.do<%=Constant.APP_ID%>&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect';
}else if (name=='customizedpage'){
	var memberId=getUrlParam('memberId');
	goUrl='https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=Constant.APP_ID%>&redirect_uri=<%=Constant.URL%>/scope/openid.do?next=agentCompany/gotoPromotionPage.do<%=Constant.APP_ID%>memberId='+memberId+'&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect';
}else if(name='orderShare'){
	var orderNo=getUrlParam('orderNo');
	var memberId=getUrlParam('memberId');
	goUrl='https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=Constant.APP_ID%>&redirect_uri=<%=Constant.URL%>/scope/openidshare.do?next=personorder/gotoshare.do<%=Constant.APP_ID%>ORDERNO'+orderNo+'USERID'+memberId+'&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect';
}



window.location.href = goUrl;


</script>
</body>
</html>
