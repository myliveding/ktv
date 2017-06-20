<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html style="background: #fc3f1f;">
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
    <div class="edit">
        <div class="user-top">
            <span>普通会员</span> 
            <a href="${pageContext.request.contextPath}/member/gotoMyMessages.do">
                <img src="<%=basePath%>jsp/resources/img/message.png">
                <i>${num}</i>
            </a>
            <div class="clear"></div>
        </div>
        <h1>设置中心</h1>
    </div>
    <div class="edit-info">
        <div class="edit-header">
            <c:if test="${member.headPortrait eq 'null' || member.headPortrait eq ''}">
                <img src="<%=basePath%>jsp/resources/img/1.png">
            </c:if>
            <c:if test="${member.headPortrait ne 'null' && member.headPortrait ne ''}">
                <img src="${member.headPortrait}">
            </c:if>
            <span>上传新头像</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/member/fileUpload.do" enctype="multipart/form-data">
            选择一个文件:
            <input type="file" name="uploadFile" />
            <br/><br/>
            <input type="submit" value="上传" />
        </form>
        <%-- <p>
            <span>线下会员绑定</span>
            <a href="">修改</a>  
            <a href="">查看</a>
            <div class="clear"></div>
        </p> --%>
        <p>
            <span>绑定手机</span>
            <em>${member.mobile}</em>
            <a href="">修改</a>
            <div class="clear"></div>
        </p>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
</body> 
<script src="<%=basePath%>jsp/resources/js/rem.js"></script>
</html> 
