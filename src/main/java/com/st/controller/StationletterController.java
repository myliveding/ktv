package com.st.controller;

import com.st.utils.Constant;
import com.st.core.CookieUtil;
import com.st.utils.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.service.WeixinAPIService;
import com.st.utils.*;
import net.sf.json.JSONArray;
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


    /**
     * 文章和活动详细信息
     * 根据cat_id判断,其中101代表的是活动的，已经写在配置文件中
     * @param request
     * @return
     */
    @RequestMapping("/detailinfo")
    public String detailinfo(HttpServletRequest request){
        String param = request.getParameter("param");
        if(StringUtils.isEmpty(param)){
            param = "detailinfo";
        }else if(param.indexOf("detailinfo/detailinfo")>-1){//安全需求，防止跨站点脚本编制
            param="detailinfo/detailinfo";
        }else if(param.indexOf("servicehall/questionDetail")>-1){
            param="servicehall/questionDetail";
        }else if(param.indexOf("more/activityinfo")>-1){
            param="more/activityinfo";
        }else if(param.indexOf("agent/agentbasedetail")>-1){
            param="agent/agentbasedetail";
        }else if(param.indexOf("agent/promotedetail")>-1){
            param="agent/promotedetail";
        }
        String articleId = request.getParameter("articleId");
        if(!CheckUtil.isNumber(articleId)){//文章参数不对跳转首页
            return "redirect:"+ Constant.URL+"/weixin/getweixin.do?name=index/index";
        }
        String uId=request.getParameter("uId");
        String openid=request.getParameter("openid");
        String invCode = request.getParameter("invCode");
        if(StringUtils.isNotEmpty(invCode)){
            request.setAttribute("invCode",invCode);
        }
        String[] arr;
        String mystr="";
        String linkActivityId ="1";
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        StringBuffer key = new StringBuffer("wyb_wechat_detailinfo");
        //查询当前的活动接口
        try {
            String[] arr1 = new String[] {};
            String mystr1 = "";
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONRED_DEMANDACTIVITY_URL,mystr1,arr1));
            if(0 == resultStr.getInt("status")){
                JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
                if(message!=null){
                    request.setAttribute("link_activity_id",message.getString("activity_id"));//活动id
                    linkActivityId = message.getString("activity_id");
                    logger.info("当前文章的编号："+linkActivityId);
                }else {
                    request.setAttribute("error","文章已删除");
                }
            }
        } catch (Exception e1) {
            logger.error("获取数据出错:" + e1.getMessage(), e1);
        }
        arr=new String[] {"article_id"+articleId};
        mystr="article_id="+articleId;
        key.append("_" + articleId);

        resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_DETAIL_URL,mystr,arr));

        if(0 == resultStr.getInt("status")){
            JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
            if(message != null){
                request.setAttribute("article_id",message.getString("article_id"));
                request.setAttribute("cat_id", message.getString("cat_id"));//分类ID,
                request.setAttribute("title", message.getString("title"));//标题，
                request.setAttribute("summary", message.getString("summary"));//内容，
                request.setAttribute("author", message.getString("author"));//作者，
                request.setAttribute("source",message.getString("source"));//来源,
                request.setAttribute("create_time",message.getString("create_time"));//创建日期
                request.setAttribute("description",message.getString("description"));//文章内容
                request.setAttribute("oimage",message.getString("o_image"));//原图
                request.setAttribute("ourl",message.getString("url"));//链接
                request.setAttribute("urltext",message.getString("url_text"));//链接描述
                request.setAttribute("meta_title",message.getString("meta_title"));//标题
                request.setAttribute("meta_keyword",message.getString("meta_keyword"));//关键字
                request.setAttribute("meta_description",message.getString("meta_description"));//内容描述
                if (message.containsKey("s_image")&&StringUtils.isNotEmpty(message.getString("s_image"))&&!"null".equals(message.getString("s_image"))){
                    request.setAttribute("shareImg",message.getString("s_image"));//缩略图
                }

            }else {
                request.setAttribute("error","文章已删除");
            }
        }else {
            request.setAttribute("error","文章出错了");
        }
        String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
                + request.getContextPath()      //项目名称
                + request.getServletPath();      //请求页面或其他地址
        String paramStr=request.getQueryString();
        if (paramStr.indexOf("param")>-1){//安全需求，防止跨站点脚本编制
            paramStr=paramStr.substring(0,paramStr.indexOf("param="))+"param="+param;
        }
        strBackUrl=strBackUrl+"?"+paramStr;
        logger.info("detailinfo strBackUrl="+strBackUrl);

        HttpSession session = ContextHolderUtils.getSession();
        String userId="1";
        String invitationCode="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject!=null&&!"".equals(userIdObject)){
            userId=userIdObject.toString();
            JSONObject userInfo = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_HOLDER_INFO,"policyHolderId="+userId));
            if(userInfo.containsKey("status")){
                JSONArray jsonArray=userInfo.getJSONArray("data");
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        invitationCode=jsonArray.getJSONObject(i).getString("invitationCode");
                    }
                }
            }
            strBackUrl=strBackUrl+"&invitCode="+invitationCode;
        }
        //将session保存，以备后面用到
        if(StringUtils.isNotEmpty(openid)){//不为空才放入session
            session.setAttribute("openid", openid);
        }
        String url = strBackUrl;
        //微信分享的伪静态
        if(request.getServletPath().equalsIgnoreCase("/article/detailinfo.do")
                && null != param && !"".equalsIgnoreCase(param) ){

        }
        //分享参数
        String timestamp =Constant.TIME_STAMP;
        String noncestr = Constant.NONCESTR;
        if(StringUtils.isNotEmpty(uId)){
            userId=uId;
        }
        logger.info("shareurl:" + strBackUrl);
        Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
        String jsapi_ticket = weixin.getJsapiTicket();
        String signature = SignUtil.getSignature(jsapi_ticket, timestamp, noncestr, url);
        request.setAttribute("timestamp", timestamp);
        request.setAttribute("noncestr", noncestr);
        request.setAttribute("url", url);
        request.setAttribute("signature", signature);
        request.setAttribute("appid", Constant.APP_ID);
        request.setAttribute("shareurl", strBackUrl);
        logger.info("文章跳转"+param);
        return param;
    }
}
