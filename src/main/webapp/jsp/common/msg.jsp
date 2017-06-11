<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Exception"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>错误页面</title>
</head>

<body>
<h1>出错了</h1>
<%
    String tv_message = (String)request.getAttribute("tv_message");
    out.print(tv_message);
    Exception e = (Exception) request.getAttribute("exception");
    out.print(e.getMessage());
%>
${msg}
</body>
</html>