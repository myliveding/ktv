/**
 * Copyright 2005-2012 insigmaedu.com
 * All rights reserved.
 * 
 * @project
 * @author Flouny.Caesar
 * @version 1.0
 * @data 2011-08-22
 */
package com.st.core.http;

import com.st.utils.CheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * XSS 过滤器
 * @author Flouny.Caesar
 *
 */
public class XssFilter implements Filter {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	FilterConfig filterConfig = null;
	private String excludedPages;
	private String[] excludedPageArray;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		excludedPages = filterConfig.getInitParameter("excludedPages");
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.split(",");
		}
		return;
	}
	
	public void destroy() {
		this.filterConfig = null;
		this.excludedPages = null;
		this.excludedPageArray = null;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession session = httpServletRequest.getSession();
		//获取访问的url路径
		String uri = httpServletRequest.getServletPath();
		if (uri.indexOf("weixin/getWeixintoIndex.do")>-1){
			uri=uri.substring(0,uri.indexOf("weixin/getWeixintoIndex.do"));
		}
		String wybSource=httpServletRequest.getParameter("wybSource");
		if (com.st.utils.StringUtils.isNotEmpty(wybSource)){
			if (CheckUtil.isWord(wybSource)){
				session.setAttribute("wybSource",wybSource);
			}
		}
		String invitCode=httpServletRequest.getParameter("invitCode");
		if (com.st.utils.StringUtils.isNotEmpty(invitCode)){
			if (CheckUtil.isWord(invitCode)){
				session.setAttribute("regInvitCode",invitCode);
			}
		}
		logger.info("正常拦截器中,uri：" + uri);
		boolean isExcludedPage = false;
		for (String page : excludedPageArray) {// 遍历例外url数组
			// 判断当前URL是否与例外配置相同
			if(uri.contains(page)){
				logger.info("访问路径：" + uri + ",XssFilter例外配置：" + page + ",匹配成功!");
				isExcludedPage = true;
				break;
			}
		}
		if (!isExcludedPage) { //需要去判断的
			chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
		}else{
			chain.doFilter(request, response);
		}
	}
}