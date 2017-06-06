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

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 
 * @author Flouny.Caesar
 *
 */
public class ShareRenderArgsVariableInterceptor extends HandlerInterceptorAdapter {
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if(viewName != null && !viewName.startsWith("redirect:")) {
				modelAndView.addObject("httpInclude", new HttpInclude(request, response));
			}
		}
	}
}