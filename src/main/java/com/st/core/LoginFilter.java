package com.st.core;

import com.st.utils.Constant;
import com.st.utils.DataUtil;
import com.st.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *  登陆的校验
 */
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

		//获取访问的url路径
		String uri = httpServletRequest.getServletPath();
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
            //        HttpSession session = ContextHolderUtils.getSession();
            String openid = (String)httpServletRequest.getSession().getAttribute("openid");
            String appid = (String)httpServletRequest.getSession().getAttribute("appid");

        	if (null != openid && !"".equals(openid) && null != appid && !"".equals(appid)){
			}else{
                logger.info("openid或者appid不存在不能进入相应上述页面，需要去重新获取并加载到session里面");
                httpServletRequest.setAttribute("error", "请重新登录");
                request.setAttribute("error", "请重新登录");
                RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/member/gotoErrorMsg.do");
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
