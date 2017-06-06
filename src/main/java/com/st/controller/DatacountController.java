package com.st.controller;

import com.st.service.WeixinAPIService;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/datacount")
public class DatacountController {
private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	@Resource
	private WeixinAPIService weixinAPIService;
	
	/**
	 * 抓取数据---暂时未使用
	 * @param request
	 * @return
	 */
	@RequestMapping("/crawl")
	public @ResponseBody Object crawl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session=ContextHolderUtils.getSession();
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");

		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String openId = "";
		Object openidObj =  session.getAttribute("openid");
		if (openidObj==null||"".equals(openidObj)) {
		}else{
			openId = openidObj.toString();
		}
		
		String userId=userIdObject.toString();
		String ip=request.getParameter("ip");
		String viewId=request.getParameter("viewId");
		
		String[] arr={"openid"+openId,"user_id"+userId,"ip"+ip,"viewId"+viewId};
		String mystr="openid="+openId+"&user_id="+userId+"&ip="+ip+"&viewId="+viewId;
		try {
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.DATACOUNT_CRAWL_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
}
