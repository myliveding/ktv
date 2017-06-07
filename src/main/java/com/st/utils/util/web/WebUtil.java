/*
 * @(#)WebUtil.java 2013-12-2下午06:04:44
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.utils.util.web;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;



/**
 * Web工具类
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2013-12-2下午06:04:44]gaozhanglei<br/>
 * </p>
 * </li>
 * </ul>
 */
@SuppressWarnings("unchecked")
public class WebUtil {

	/**
	 * 获得请求URL
	 * @author sunju
	 * @creationDate. 2011-5-10 下午06:39:11 
	 * @param request 请求
	 * @return 请求URL
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuilder url = new StringBuilder(request.getRequestURI());
		Map<String, String[]> parameterMap = request.getParameterMap();
		String key = null;
		String[] value = null;
		if (parameterMap != null && parameterMap.size() > 0) {
			url.append("?");
			for(Iterator<?> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
				key = (String)it.next();
				value = parameterMap.get(key);
				if (value != null && value.length > 0) {
					for (String val : value) {
						url.append(key).append("=").append(val).append("&");
					}
				}
			}
			url.delete(url.length() - 1, url.length());
		}
		return url.toString();
	}
	/**
	 * 获得基路径
	 * @author sunju
	 * @creationDate. 2012-7-5 下午10:08:02 
	 * @param request 请求
	 * @return 基路径
	 */
	public static String getBasePath(HttpServletRequest request) {
		int port = request.getServerPort();
		return request.getScheme() + "://" + request.getServerName()
					+ ((port == 80) ? "" : (":" + port)) + request.getContextPath() + "/";
	}
	/**
	 * 是否为Ajax请求
	 * @author sunju
	 * @creationDate. 2011-8-24 下午08:46:46 
	 * @param request 请求
	 * @return 布尔
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		/*
		 * iframe提交特殊处理，当作ajax请求处理
		 */
		String iframe = (String) request.getParameter("iframe");
		boolean isIframe = (StringUtils.isNotEmpty(iframe)) ? Boolean.valueOf(iframe) : false;
		if (isIframe) return true;
		return (request.getHeader("x-requested-with") == null) ? false : true;
	}
	/**
	 * 向客户端输出
	 * @author sunju
	 * @creationDate. 2010-9-18 上午00:19:16 
	 * @response 响应对象
	 * @param outObj 输出的Object
	 * @param outEncoding 输出编码
	 * @throws IOException 
	 */
	public static void write(HttpServletResponse response, Object outObj, String outEncoding) throws IOException {
		// 设置默认响应类型
		if (response.getContentType() == null) response.setContentType("text/html");
		response.setCharacterEncoding(outEncoding);
		PrintWriter out = null;
		out = response.getWriter();
		out.print(outObj);
		out.flush();
		out.close();
	}
}
