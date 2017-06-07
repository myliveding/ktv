/*
 * @(#)BaseExceptionResolver.java 2013-12-2下午05:52:47
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.core.handle;

import com.st.utils.util.ConstantsUtil;
import com.st.utils.util.web.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




/**
 * 基本异常处理
 * <ul>
 * <li>
 * <b>修改历史�?/b><br/>
 * <p>
 * [2014-08-13]gaozhanglei<br/>
 * </p>
 * </li>
 * </ul>
 */
public class BaseExceptionResolver extends SimpleMappingExceptionResolver {
	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		// Expose ModelAndView for chosen error view.
		/**
		 * 记录异常信息
		 */
		LOG.error(ex.getMessage(), ex);
		
		/**
		 * 异常提示
		 */
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			if (WebUtil.isAjaxRequest(request)) { // ajax请求
				try {
					WebUtil.write(response, "{success:true,http_status_code_:\"exception\"}",
							ConstantsUtil.SYSTEM_DEFAULT_ENCODING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOG.error(e.getMessage(), e);
				}
				return null;
			} else {
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			}
		} else {
			return null;
		}
	}

}
