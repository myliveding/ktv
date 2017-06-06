package com.st.controller;

import com.st.core.util.CookieUtil;
import com.st.core.util.text.StringUtils;
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
@RequestMapping("/stationletter")
public class StationletterController {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	@Resource
	private WeixinAPIService weixinAPIService;
	
	/**
	 *站内信查询
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/demand")
	public @ResponseBody Object demand(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String[] arr;
		String mystr="";
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		int userId=Integer.parseInt(userIdObject.toString());
		String messageId=request.getParameter("messageId");
		if (!StringUtils.isEmpty(messageId)) {
			arr=new String[]{"user_id"+userId,"message_id"+messageId};
			mystr="user_id="+userId+"&message_id="+messageId;
		}else{
			String pageSize=request.getParameter("pageSize");
			String page=request.getParameter("page");
			String messageOpen=request.getParameter("");
			if (StringUtils.isEmpty(pageSize)||StringUtils.isEmpty(page)) {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"每页显示多少条记录和当前页数均不能为空\"}");
			}
			if (StringUtils.isEmpty(messageOpen)) {
				arr=new String[]{"user_id"+userId,"page_size"+pageSize,"page"+page};
				mystr="user_id="+userId+"&page_size="+pageSize+"&page="+page;
			}else {
				arr=new String[]{"user_id"+userId,"page_size"+pageSize,"page"+page,"message_open"+messageOpen};
				mystr="user_id="+userId+"&page_size="+pageSize+"&page="+page+"&message_open="+messageOpen;
			}
		}
		try {
			//站内信查询
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.STATIONLETTER_DEMAND_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}
	
	/**
	 * 站内信已读消息接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/read")
	public @ResponseBody Object read(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String[] arr;
		String mystr="";
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		int userId=Integer.parseInt(userIdObject.toString());
		String messageId=request.getParameter("messageId");
		if(StringUtils.isEmpty(messageId)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"用户ID不能为空\"}");
		}
		arr=new String[]{"user_id"+userId,"message_id"+messageId};
		mystr="user_id="+userId+"&message_id="+messageId;
		try {
			//站内信已读消状态修改
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.STATIONLETTER_READ_URL,mystr,arr));
			CookieUtil.getUserInfo();//更新用户的消息标志session
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
}
