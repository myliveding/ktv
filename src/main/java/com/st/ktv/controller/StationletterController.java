package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.service.impl.WechatAPIServiceImpl;
import com.st.utils.DataUtil;
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
	private WechatAPIServiceImpl weixinAPIService;
	
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
		HttpSession session= ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		int userId=Integer.parseInt(userIdObject.toString());
		String messageId=request.getParameter("messageId");
		if (!DataUtil.isEmpty(messageId)) {
			arr=new String[]{"user_id"+userId,"message_id"+messageId};
			mystr="user_id="+userId+"&message_id="+messageId;
		}else{
			String pageSize=request.getParameter("pageSize");
			String page=request.getParameter("page");
			String messageOpen=request.getParameter("");
			if (DataUtil.isEmpty(pageSize)||DataUtil.isEmpty(page)) {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"每页显示多少条记录和当前页数均不能为空\"}");
			}
			if (DataUtil.isEmpty(messageOpen)) {
				arr=new String[]{"user_id"+userId,"page_size"+pageSize,"page"+page};
				mystr="user_id="+userId+"&page_size="+pageSize+"&page="+page;
			}else {
				arr=new String[]{"user_id"+userId,"page_size"+pageSize,"page"+page,"message_open"+messageOpen};
				mystr="user_id="+userId+"&page_size="+pageSize+"&page="+page+"&message_open="+messageOpen;
			}
		}
		try {
			//站内信查询
//			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.STATIONLETTER_DEMAND_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}

}
