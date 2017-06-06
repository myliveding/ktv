package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.text.StringUtils;
import com.st.service.WeixinAPIService;
import com.st.utils.ContextHolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;


@Controller
@RequestMapping("/scope")
public class ScopeController {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private WeixinAPIService weixinAPIService;
	
	@RequestMapping("/openid")
	public ModelAndView getUserOpenIdOfScope(HttpServletRequest request, HttpServletResponse response )throws IOException {
		String code = request.getParameter("code");
		String next = request.getParameter("next");
		logger.info("ScopeController：" + code+",cn：" + next);
		if (!"".equals(code)&&code!=null&&!"".equals(next)&&next!=null) {
			 String nextPage = next.substring(0, next.indexOf(".do"))+".do";
			 String appid=next.substring(next.indexOf(".do")+3,next.length() );
			 String pram="";
			 if(appid.length()>18){
			     pram=appid.substring(18, appid.length());
                 appid=appid.substring(0, 18);
			 }
			 logger.info("pram："+ pram + ",appid："+appid);
			 String openid =weixinAPIService.getUserOpenIdOfScope(appid,code);
			 logger.info("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid+"#"+pram);
			 HttpSession session = ContextHolderUtils.getSession();
			 session.setAttribute("openid", openid);
			 session.setAttribute("appid", appid);
			 boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
			 session.setAttribute("Subscribe", subscribe);
			 
			 if(StringUtils.isNotEmpty(pram)){
			     logger.info(nextPage+"?"+pram);
			     return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"?"+pram);
			     
			 }else{
			     return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage);
			 }
			
		}else {
			logger.error("未将对象引入到实例");
			request.setAttribute("error", "未将对象引入到实例");
			return new ModelAndView("redirect:error.do");
		}
	}

	
	@RequestMapping("/openidjsp")
	public ModelAndView getUserMapOpenIdOfScope(HttpServletRequest request, HttpServletResponse response )throws IOException {
		String code = request.getParameter("code");
		String next = request.getParameter("next");
		if (!"".equals(code)&&code!=null&&!"".equals(next)&&next!=null) {
			 String nextPage = next.substring(0, next.indexOf(".do"))+".jsp";
			 String appid=next.substring(next.indexOf(".do")+3,next.length());
			 String openid =weixinAPIService.getUserOpenIdOfScope(appid,code);
			 logger.info("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid);
			 HttpSession session = ContextHolderUtils.getSession();
			 session.setAttribute("openid", openid);
			 session.setAttribute("appid", appid);
			 boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
			 session.setAttribute("Subscribe", subscribe);
			 
			 return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage);
		}else {
			logger.error("未将对象引入到实例");
			request.setAttribute("error", "未将对象引入到实例");
			return new ModelAndView("redirect:error.do");
		}
	}
	
	@RequestMapping("/openidshare")
    public ModelAndView getUserOpenIdOfScopeForShare(HttpServletRequest request, HttpServletResponse response )throws IOException {
        String code = request.getParameter("code");
        String next = request.getParameter("next");
        String orderNo="";
        String userId="";
        logger.info("ScopeController：" + code+",cn：" + next);
        if (!"".equals(code)&&code!=null&&!"".equals(next)&&next!=null) {
             String nextPage = next.substring(0, next.indexOf(".do"))+".do";
             String appid=next.substring(next.indexOf(".do")+3,next.indexOf(".do")+21);
             logger.info("next:--"+next + "appid:--"+appid);
             if(next.indexOf("USERID")>-1){
                 orderNo=next.substring(next.indexOf("ORDERNO")+7,next.indexOf("USERID"));
                 userId=next.substring(next.indexOf("USERID")+6,next.length());
             }else{
                 orderNo=next.substring(next.indexOf("ORDERNO")+7,next.length());
             }
             String openid =weixinAPIService.getUserOpenIdOfScope(appid,code);
             logger.info("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid+"#orderNo="+orderNo+"&shareUserId="+userId);
             HttpSession session = ContextHolderUtils.getSession();
             session.setAttribute("openid", openid);
             session.setAttribute("appid", appid);
             boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
			 session.setAttribute("Subscribe", subscribe);
			 if(StringUtils.isNotEmpty(orderNo)&&StringUtils.isNotEmpty(userId)){
                 return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"?orderNo="+orderNo+"&shareUserId="+userId);
             }else if(StringUtils.isNotEmpty(orderNo)){
                 return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"?orderNo="+orderNo);
             }else{
                 return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage);
             }
        }else {
            logger.error("未将对象引入到实例");
            request.setAttribute("error", "未将对象引入到实例");
            return new ModelAndView("redirect:error.do");
        }
    }


	@RequestMapping("/openidTips")
	public ModelAndView getUserOpenIdOfScopeForTips(HttpServletRequest request, HttpServletResponse response )throws IOException {
		String code = request.getParameter("code");
		String next = request.getParameter("next");
		String type="";
		String param="";
		logger.info("ScopeController：" + code+",cn：" + next);
		if (!"".equals(code)&&code!=null&&!"".equals(next)&&next!=null) {
			String nextPage = next.substring(0, next.indexOf(".do"))+".do";
			String appid=next.substring(next.indexOf(".do")+3,next.indexOf(".do")+21);
			logger.info("next:--"+next + "appid:--"+appid);
			if(next.indexOf("TYPE")>-1){
				type=next.substring(next.indexOf("TYPE")+4,next.indexOf("PARAM"));
				param=next.substring(next.indexOf("PARAM")+5,next.length());
			}
			String openid =weixinAPIService.getUserOpenIdOfScope(appid,code);
			logger.info("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid+"#type="+type+"&param="+param);
			HttpSession session = ContextHolderUtils.getSession();
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
			boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
			session.setAttribute("Subscribe", subscribe);
			return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"?type="+type+"&param="+param+"&openid="+openid+"&appid="+appid);
		}else {
			logger.error("未将对象引入到实例");
			request.setAttribute("error", "未将对象引入到实例");
			return new ModelAndView("redirect:error.do");
		}
	}
	@RequestMapping("/openidC")//用于渠道分享的登录
	public ModelAndView getUserOpenIdOfScopeForChannel(HttpServletRequest request, HttpServletResponse response )throws IOException {
		String code = request.getParameter("code");
		String next = request.getParameter("next");
		logger.info("ScopeController：" + code+", next：" + next);
		if (!"".equals(code)&&code!=null&&!"".equals(next)&&next!=null) {
			String appid=next.substring(0,18);
			logger.info("1next:--"+next + "appid:--"+appid);
			String nextPage = next.substring(18);
			nextPage=nextPage.replaceAll("\\*\\*","=").replaceAll("\\*","&");
			logger.info("next:--"+next + "appid:--"+appid+"-nextPage-"+nextPage);
			String openid =weixinAPIService.getUserOpenIdOfScope(appid,code);
			logger.info("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid);
			HttpSession session = ContextHolderUtils.getSession();
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
			boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
			session.setAttribute("Subscribe", subscribe);
			return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage);
//			if (nextPage.indexOf("?")>-1){
//				return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"&openid="+openid+"&appid="+appid);
//			}else{
//				return new ModelAndView("redirect:"+Constant.URL+"/"+nextPage+"?openid="+openid+"&appid="+appid);
//			}
		}else {
			logger.error("未将对象引入到实例");
			request.setAttribute("error", "未将对象引入到实例");
			return new ModelAndView("redirect:error.do");
		}
	}
}
