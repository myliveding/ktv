<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<div class="footer">
    <ul>
        <li>
            <a href="<%=basePath%>member/gotoIndexDomain.do" class="footer-now red">
                <img src="<%=basePath%>/jsp/resources/img/h1.png">
                <span>在线定包</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/other/getCompanyInfo.do" class="red">
                <img src="<%=basePath%>/jsp/resources/img/h2.png">
                <span>关于盛世</span>
            </a>
        </li>
        <li>
            <a href="" class="red">
                <img src="<%=basePath%>/jsp/resources/img/h3.png">
                <span>积分商城</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/member/gotoUserCenter.do" class="red">
                <img src="<%=basePath%>/jsp/resources/img/h4.png">
                <span>我的盛世</span>
            </a>
        </li>
        <div class="clear"></div>
    </ul>
</div>