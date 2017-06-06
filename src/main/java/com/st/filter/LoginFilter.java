package com.st.filter;

import com.st.constant.Constant;
import com.st.utils.CheckUtil;
import com.st.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;



public class LoginFilter implements Filter {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private String excludedPages;       
    private String[] excludedPageArray;
    
	public void destroy() {
		this.excludedPages = null; 
        this.excludedPageArray = null; 
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession session = httpServletRequest.getSession();
		String userId = (String)httpServletRequest.getSession().getAttribute("userId");
		String isMobileBind = (String)httpServletRequest.getSession().getAttribute("isMobileBind");
		//获取访问的url路径
		String uri = httpServletRequest.getServletPath();
		if (uri.indexOf("weixin/getWeixintoIndex.do")>1){
			uri=uri.substring(0,uri.indexOf("weixin/getWeixintoIndex.do"));
		}
		String backUrl=httpServletRequest.getParameter("backUrl");
		if (com.st.core.util.text.StringUtils.isNotEmpty(backUrl)){
			session.setAttribute("backUrl",backUrl);
		}
		logger.info("正常拦截器中,项目访问路径getServletPath：" + uri);
        boolean isExcludedPage = false;  
        
        for (String page : excludedPageArray) {// 遍历例外url数组   
            // 判断当前URL是否与例外配置相同
            if(uri.contains(page)){
            	logger.info("访问路径：" + uri + ",例外配置：" + page + ",匹配成功!");
                isExcludedPage = true;     
                break;     
            }     
        }
        
        if (!isExcludedPage) { //需要去判断的
        	if ((userId == null) || ("".equals(userId))){
				httpServletRequest.setAttribute("error", "请重新登录");
				request.setAttribute("error", "请重新登录");
//				RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/weixin/getweixin.do?name=account/login");
				RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/weixin/getweixin.do?name=account/fasterlogin");
				rd.forward(httpServletRequest, response);
				return;
			}else if ((isMobileBind == null) || ("".equals(isMobileBind)) 
					|| PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", Constant.CONFIG_FILE_NAME).equals(isMobileBind)){
				httpServletRequest.setAttribute("error", "请绑定手机");
				RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/jsp/account/bindmobile.jsp");
				rd.forward(httpServletRequest, response);
				return;
			}
        }
		chain.doFilter(request, response);
	}

	
	
	/**
	 * 在初始化容器的时候需要去获取例外的配置并存入数组中
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	     excludedPages = filterConfig.getInitParameter("excludedPages");  
	     if (StringUtils.isNotEmpty(excludedPages)) {  
	     excludedPageArray = excludedPages.split(",");  
	     }  
	     return;
	}

	
}
