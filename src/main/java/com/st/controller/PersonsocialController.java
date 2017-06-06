package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.CookieUtil;
import com.st.core.util.date.DateUtil;
import com.st.core.util.iputil.City;
import com.st.core.util.iputil.IPUtil;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.service.WeixinAPIService;
import com.st.service.impl.ArticleRedisHandleServiceImpl;
import com.st.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：  账户
 * 创建人：wuhao
 * 创建时间：2015-6-10
 */
@Controller
@RequestMapping("/personsocial")
public class PersonsocialController {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private WeixinAPIService weixinAPIService;
	@Resource
	private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
	
	/**
	 * 跳转到首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		try {
			//判断是否需要保存session
			if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
				session.setAttribute("openid", openid);
				session.setAttribute("appid", appid);
			}else{
				Object openidObj =  session.getAttribute("openid");
				Object appidObj =  session.getAttribute("appid");
				if (!"".equals(openidObj)&&openidObj!=null) {
					openid = openidObj.toString();
				}
				if (!"".equals(appidObj)&&appidObj!=null) {
					appid = appidObj.toString();
				}
			}
			if (StringUtils.isNotEmpty(openid)) {
				String[] arr = new String[] {"openid"+openid};
				String mystr = "openid="+openid;
				JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
				try {
					//根据openid判断是否绑定
					result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
				} catch (Exception e) {
					logger.error("获取登录出错:" + e.getMessage(), e);
				}
				if(0 == result.getInt("status")){
					JSONObject data = JSONObject.fromObject(result.getString("data"));
					String userId=data.getString("user_id");
					String memberTruename=data.getString("member_truename");
					session.setAttribute("userId", userId);
					String isMobileBind =data.getString("is_mobile_bind");
					session.setAttribute("isMobileBind", isMobileBind);
					//判断是否绑定手机
					if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
						return "account/bindmobile";
					}
					//判断资料是否完善
					if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
						session.setAttribute("memberTruename", memberTruename);
					}
					CookieUtil.getUserInfo();//更新用户的消息标志session
					
					return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
				}else if(1100006 == result.getInt("status")){
					logger.info("这时候表示未注册过,即数据库中没有存储对应的openid,userid，信息如下：" + result.getString("msg"));
					return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
				}else {
					return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
				}
			}else{
				request.setAttribute("error", "请在微信中访问");
			}
		} catch (Exception e) {
			logger.info("出现异常，跳转到首页" + e.getMessage(),e);
		}finally{
		}

		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
	}
	
	/**
	 * 跳转到我的
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/myinfo")
	public String myinfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		//判断是否需要保存session
		if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}
		Object openidObj =  session.getAttribute("openid");
		if (!"".equals(openidObj)&&openidObj!=null) {
			openid = openidObj.toString();
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				String memberTruename=data.getString("member_truename");
				session.setAttribute("userId", userId);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				//判断是否绑定手机
				if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
					return "account/bindmobile";
				}
				//判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
				CookieUtil.getUserInfo();//更新用户的消息标志session
				return "redirect:"+Constant.URL+"" + PseudoStaticUrl.getSeourlByRealurl("/jsp/my/my.jsp");
			}else {
			    return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
			}
		}
		request.setAttribute("error", "请在微信中访问");
		return "error";
	}
	
	/**
	 * 跳转到我的参保人
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/gotomycbr")
	public String gotomycbr(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		//判断是否需要保存session
		if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				String memberTruename=data.getString("member_truename");
				session.setAttribute("userId", userId);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				//判断是否绑定手机
				if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
					return "account/bindmobile";
				}
				//判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
				CookieUtil.getUserInfo();//更新用户的消息标志session
				return "my/myinsured";//已参保页
			}else {
			    return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
			}
		}else{
			request.setAttribute("error", "请在微信中访问");
		}
		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
	}
	
	/**
	 * 跳转到我的订单 3.0
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/gotomydd")
	public String gotomydd(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		//判断是否需要保存session
		if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				String memberTruename=data.getString("member_truename");
				session.setAttribute("userId", userId);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				//判断是否绑定手机
				if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
					return "account/bindmobile";
				}
				//判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
				CookieUtil.getUserInfo();//更新用户的消息标志session
				return "my/allorderlist";
			}else {
			    return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
			}
		}else{
			request.setAttribute("error", "请在微信中访问");
		}
		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
	}
	
	/**
	 * 跳转到公司简介
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotojoyo")
	public String gotojoyo(HttpServletRequest request, HttpServletResponse response){
		return "redirect:"+Constant.URL+"" + PseudoStaticUrl.getSeourlByRealurl("/article/detailinfo.do?articleId=1003&param=detailinfo");
	}
	
	/**
	 * 跳转到关于我们
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/gotome")
	public String gotome(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
        if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                return "redirect:"+Constant.URL+"/guanyuwomen/";
            }else {
                return "redirect:"+Constant.URL+"/guanyuwomen/";
            }
        }
        return "redirect:"+Constant.URL+"/guanyuwomen/";	    
		
	}
	
	/**
	 * 跳转到无忧保服务落地页3.0
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/gotoservice")
	public String gotoservice(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {

            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                return "redirect:"+Constant.URL+"/shebaofuwu/";
            }else {
                return "redirect:"+Constant.URL+"/shebaofuwu/";
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:"+Constant.URL+"/shebaofuwu/";
	}
	
	/**
	 * 跳转到知社保
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gototaozsb")
	public String gototaozsb(HttpServletRequest request, HttpServletResponse response){
		return "redirect:"+Constant.URL+"" + PseudoStaticUrl.getSeourlByRealurl("/jsp/zhishebao.jsp");
	}

	/**
	 * 跳转到查社保 3.41
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoChaSheBao")
	public String gotoChaSheBao(HttpServletRequest request){
		String jsonStr="[{\"id\":52,\"name\":\"\\u5317\\u4eac\",\"city_name_pinyin\":\"beijing\"},{\"id\":321,\"name\":\"\\u4e0a\\u6d77\",\"city_name_pinyin\":\"shanghai\"}," +
				"{\"id\":76,\"name\":\"\\u5e7f\\u5dde\",\"city_name_pinyin\":\"guangzhou\"},{\"id\":77,\"name\":\"\\u6df1\\u5733\",\"city_name_pinyin\":\"shenzhen\"}," +
				"{\"id\":383,\"name\":\"\\u676d\\u5dde\",\"city_name_pinyin\":\"hangzhou\"},{\"id\":221,\"name\":\"\\u82cf\\u5dde\",\"city_name_pinyin\":\"suzhou\"}]";
		JSONArray citys=JSONArray.fromObject(jsonStr);
		request.setAttribute("citys",citys);
		return "more/chashebao";
	}
	/**
	 * 跳转到了解
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotoliaojie")
	public String gotoliaojie(HttpServletRequest request, HttpServletResponse response){
		return "redirect:"+Constant.URL+"" + PseudoStaticUrl.getSeourlByRealurl("/jsp/activity/howtobuy.jsp");
	}
	
	
	   /**
     * 跳转到参保人的参保详情页
     * @param request
     * @param response
     * @return
	 * @throws IOException 
     */
    @RequestMapping("/gotopdetail")
    public String gotopdetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String employeeId=request.getParameter("employeeId");
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断是否绑定手机
                if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
                    return "account/bindmobile";
                }
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                return "redirect:"+Constant.URL+"/jsp/my/insuredinfo.jsp?id="+employeeId;
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
        
    }
	
 
    /**
     * 跳转到我的代金券页
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotovoucher")
    public String gotovoucher(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断是否绑定手机
                if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
                    return "account/bindmobile";
                }
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "my/myvouchers";
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
    }  
    /**
     * 跳转到账号设置页
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotoset")
    public String gotoset(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断是否绑定手机
                if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
                    return "account/bindmobile";
                }
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "my/accountset";
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
        
    }  
    /**
	 * 跳转到邀请注册页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotoinvitation")
	public String gotoinvitation(HttpServletRequest request, HttpServletResponse response){
		String memberId=request.getParameter("memberId");
		String memberName="";
		String invitCode="";
		try {
			String[] arr = new String[] {"member_id"+memberId};
			String mystr = "member_id="+memberId;

			//用户个人资料
			JSONObject resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr,arr));
			if(0 == resultObject.getInt("status")){
				JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
				memberName=data.getString("member_truename");
				invitCode=data.getString("invitation_code");
				if(StringUtils.isNotEmpty(memberName)){
					memberName=java.net.URLEncoder.encode(memberName,"utf-8");
				}
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}

		String shareUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoinvitation.do"+Constant.APP_ID+"memberId="+memberId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";//二维码用链接

		String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
				+ request.getContextPath()      //项目名称
				+ request.getServletPath()      //请求页面或其他地址
				+ "?" + (request.getQueryString()); //参数
		String timestamp =Constant.TIME_STAMP;
		String noncestr = Constant.NONCESTR;
		Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
		String jsapi_ticket = weixin.getJsapiTicket();
		logger.info("jsapi_ticket:" + jsapi_ticket);
		String signature = SignUtil.getSignature(jsapi_ticket, timestamp, noncestr, strBackUrl);
		logger.info("signature:" + signature);
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("noncestr", noncestr);
		request.setAttribute("url", strBackUrl);
		request.setAttribute("signature", signature);
		request.setAttribute("appid", Constant.APP_ID);
		request.setAttribute("shareUrl",shareUrl);
		request.setAttribute("invitCode",invitCode);
		request.setAttribute("memberId",memberId);
		logger.info("跳转到邀请注册页/jsp/my/invitefriend.jsp?memberId="+memberId+"&invitCode="+invitCode);
//		return "redirect:"+Constant.URL+"/jsp/my/invitefriend.jsp?memberId="+memberId+"&invitCode="+invitCode;
		return "my/invitefriend";
	}

	/**
	 * 跳转到带邀请码注册页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotoinvReg")
	public String gotoinvReg(HttpServletRequest request, HttpServletResponse response){
		String invitCode=request.getParameter("invitCode");
		logger.info("跳转到邀请注册页/weixin/getweixin.do?name=account/register&invitCode="+invitCode);
		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/register&invitCode="+invitCode;
	}
    /**
     * 跳转到计算器 3.0
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotojsq")
    public String gotojsq(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
        if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/jisuanqi/";
            }else {
                return "redirect:"+Constant.URL+"/jisuanqi/";
            }
        }
        return "redirect:"+Constant.URL+"/jisuanqi/";
    }  
    
    /**
     * 外部公众号跳转到计算器 3.0 带来源标识
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/gotowybcalc")
    public String gotowybcalc(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String code=request.getParameter("code");
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (null!=openid&&null!=appid&&!"".equals(openid)&&!"".equals(appid)) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/jsp/calculator/calculator.jsp?kw="+code;
            }else {
                return "redirect:"+Constant.URL+"/jsp/calculator/calculator.jsp?kw="+code;
            }
        }
        return "redirect:"+Constant.URL+"/jsp/calculator/calculator.jsp?kw="+code;
    }
    
    /**
     * 外部公众号跳转到首页 3.0 带来源标识
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/gotowybindex")
    public String gotowybindex(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String code=request.getParameter("code");
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (null!=openid&&null!=appid&&!"".equals(openid)&&!"".equals(appid)) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String url = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() // 服务器地址
                    + request.getContextPath() // 项目名称
                    + request.getServletPath(); // 请求页面或其他地址
            request.setAttribute("timestamp", Constant.TIME_STAMP);
            request.setAttribute("noncestr", Constant.NONCESTR);
            request.setAttribute("url", url);
            request.setAttribute("appid", Constant.APP_ID);
            Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
            String jsapiTicket = weixin.getJsapiTicket();
            logger.info("jsapi_ticket:" + jsapiTicket);
            String signature = SignUtil.getSignature(jsapiTicket,
                    Constant.TIME_STAMP, Constant.NONCESTR, url);
            logger.info("signature:" + signature);
            request.setAttribute("signature", signature);
            request.setAttribute("jsapi_ticket", jsapiTicket);
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/?kw="+code;
            }else {
                return "redirect:"+Constant.URL+"/?kw="+code;
            }
        }
        return "redirect:"+Constant.URL+"/?kw="+code;
    }
    
    /**
     * 跳转到首页 3.0
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotoindex")
    public String gotoindex(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String str=httpServletRequest.getQueryString();
		if (StringUtils.isNotEmpty(str)){
			str="&"+str;
		}else{
			str="";
		}
		//判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		String invitCode=request.getParameter("invitCode");
        if(StringUtils.isNotEmpty(invitCode)){
            session.setAttribute("regInvitCode", invitCode);
            session.setAttribute("invitCode", invitCode);
        }
        if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index"+str;
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index"+str;
            }
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index"+str;
       
    } 
    
    /**
     * 跳转到在保状态查询 3.0
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotosearch")
    public String gotosearch(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/shebaochaxun/";
            }else {
                return "redirect:"+Constant.URL+"/shebaochaxun/";
            }
        }
        return "redirect:"+Constant.URL+"/shebaochaxun/";
    }
    
    /**
     * 跳转到注册页 3.0
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotoregister")
    public String gotoregister(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");
        String invitCode=request.getParameter("invitCode");
        //判断是否需要保存session
        if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
        }else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
            String[] arr = new String[] {"openid"+openid};
            String mystr = "openid="+openid;
            JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                //根据openid判断是否绑定
                result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
            } catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
            }
            if(0 == result.getInt("status")){
                JSONObject data = JSONObject.fromObject(result.getString("data"));
                String userId=data.getString("user_id");
                String memberTruename=data.getString("member_truename");
                session.setAttribute("userId", userId);
                String isMobileBind =data.getString("is_mobile_bind");
                session.setAttribute("isMobileBind", isMobileBind);
                //判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=payshebao/selectcity";
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/register&invitCode="+ invitCode;
            }
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/register&invitCode="+ invitCode;
    }


	/**
	 * 微信提醒中的链接跳转到相应页面 3.1
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/gotoRedirect")
	public String gotoRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		String type=request.getParameter("type");
		String param=request.getParameter("param");
		logger.info("-----------gotoRedirect?type="+type+"param="+param);
		//判断是否需要保存session
		if (null!=openid&&!"".equals(openid)&&null!=appid&&!"".equals(appid)&&null!=type) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}else{
			Object openidObj =  session.getAttribute("openid");
			Object appidObj =  session.getAttribute("appid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
			if (!"".equals(appidObj)&&appidObj!=null) {
				appid = appidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				String memberTruename=data.getString("member_truename");
				session.setAttribute("userId", userId);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				//判断资料是否完善
				if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
					session.setAttribute("memberTruename", memberTruename);
				}
				CookieUtil.getUserInfo();//更新用户的消息标志session
			}
			if(type.equals("1")){
				return "redirect:"+Constant.URL+"/jsp/my/insuredinfo.jsp?id="+param;
			}else if(type.equals("2")){
				return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=payshebao/selectcity";
			}else if(type.equals("3")){
				return "redirect:"+Constant.URL+"/jsp/my/orderdetail.jsp?orderNo="+param;
			}else if(type.equals("4")){
				return "redirect:"+Constant.URL+"/jsp/order/orderrefunddetail.jsp?refundId="+param;
			}else if(type.equals("5")){
				return "redirect:"+Constant.URL+"/personorder/myOrderDetial.do?orderNo="+param;
			}else if(type.equals("6")){//参保材料 v3.52
				return "redirect:"+Constant.URL+"/jsp/order/ordernotice.jsp?orderNo="+param;
			}else{
				return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
			}
		}
		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
	}

	/**
	 * 用户个人资料
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/userinfo")
	public @ResponseBody Object userinfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		String memberId="";//用户id
		JSONObject resultObject = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		try {
			if(userIdObject==null||"".equals(userIdObject)){
				return resultObject;
			}
			memberId = userIdObject.toString();
			String[] arr = new String[] {"member_id"+memberId};
			String mystr = "member_id="+memberId;
			
			//用户个人资料
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr,arr));
			if(0 == resultObject.getInt("status")){
				JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
				String userId=data.getString("member_id");
				session.setAttribute("userId", userId);
				if(null == data.getString("member_truename")||data.getString("member_truename").equals("null")) {
					data.put("member_truename","");
                }
				session.setAttribute("memberName",data.getString("member_name"));
				String memberTruename=data.getString("member_truename");
				session.setAttribute("memberTruename", memberTruename);
				String memberAvatar =data.getString("member_avatar");
				if(StringUtils.isEmpty(memberAvatar) || memberAvatar.equals("null")) {
					data.put("member_avatar",Constant.staticUrl + "/css/images/head.jpg");
                }
				//是否有未读消息(1是0否)
				session.setAttribute("isBusiness", data.getString("is_person_business"));
				session.setAttribute("isAudit", data.getString("is_audit"));
				logger.info(userId+" 查询用户个体工商户字段值："+data.getString("is_person_business"));
				String isNews = data.getString("is_news");
				if(null == isNews || isNews.equals("null")) {
					session.setAttribute("isNews","");
                }else{
                	session.setAttribute("isNews",isNews);
                }
				if(data.containsKey("identity_card_file_up")){
				    data.put("identity_card_file_up_name", data.getString("identity_card_file_up"));
	                data.put("identity_card_file_up", "");
	            }
				if(data.containsKey("identity_card_file_down")){
				    data.put("identity_card_file_down_name", data.getString("identity_card_file_down"));
	                data.put("identity_card_file_down", "");
	            }
                if(data.containsKey("file")){
                    data.put("file_name", data.getString("file"));
                    data.put("file", "");
                }
                resultObject.put("data", data);
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		
		response.setContentType("text/html; charset=utf-8");  
		PrintWriter out=response.getWriter();
		out.println(resultObject);
		return null;
	}
	
	/**
	 * 校验手机号
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkmobile")
	public @ResponseBody Object checkmobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String phone=request.getParameter("phone");//手机号码
		String memberName=request.getParameter("memberName");//用户名
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if (StringUtils.isNotEmpty(phone)) {
		    phone=phone.replaceAll(" ", "");
            if(!CheckUtil.isMobileNo(phone)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式有误！\"}");
            }
        }
        
		try {
			if("".equals(phone)||phone==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"输入手机号码为空\"}");
			}
			String[] arr = new String[] {"phone"+phone,"member_name"+memberName};
			String mystr = "phone="+phone+"&member_name="+memberName;
			//发送手机验证码
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_CHECKMOBILE_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	/**
	 * 注册绑定
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/register")
	public @ResponseBody Object register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		String phone=request.getParameter("phone").trim();
		String password=request.getParameter("password");
		String securityCode=request.getParameter("securityCode");
		String registerInvitationCode=request.getParameter("registerInvitationCode");//注册邀请码
		logger.info("邀请码registerInvitationCode="+registerInvitationCode);
		String openid="";
		String appid="";
		Object openidObject =  session.getAttribute("openid");
		Object appidObject =  session.getAttribute("appid");
		Object wybSourceObject= session.getAttribute("wybSource");
		String wybSource="";
		if (wybSourceObject!=null&&!"".equals(wybSourceObject)){
			wybSource=wybSourceObject.toString();
		}
		String nickName="";
		String sex="";
		String address=request.getParameter("address");
		String headPortrait="";
		String groupId="";
		if(openidObject!=null&&!"".equals(openidObject)&&appidObject!=null&&!"".equals(appidObject)){
			openid = openidObject.toString();
			appid=appidObject.toString();
			JSONObject jSONObject = weixinAPIService.getUserInfoOfOpenId(appid, openid);
			if(jSONObject.getString("subscribe").equalsIgnoreCase
					(PropertiesUtils.findPropertiesKey("USER_SUBSCRIBE", Constant.CONFIG_FILE_NAME))){
				nickName=jSONObject.getString("nickname");
				sex=jSONObject.getString("sex");
				headPortrait=jSONObject.getString("headimgurl");
			}
			if (StringUtils.isEmpty(wybSource)){//优先现有的“渠道来源”（wybSource），若为空，再判断和获取公众号关注分组。
				groupId=weixinAPIService.getUserGroup(openid);
				if (StringUtils.isNotEmpty(groupId)){
					wybSource="wxfz"+groupId;
				}
			}
		}
		if (StringUtils.isEmpty(wybSource)){
			wybSource="mweb";
		}
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		try {
			if(StringUtils.isEmpty(phone)){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
			}
			if(StringUtils.isEmpty(password)){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"密码不能为空\"}");
			}
			if(StringUtils.isEmpty(securityCode)){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
			}
			phone=phone.replaceAll(" ", "");
            if(!CheckUtil.isMobileNo(phone)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式有误！\"}");
            }
            String mystr="";
            String[] arr;
            if(StringUtils.isNotEmpty(nickName)){
                nickName=nickName.trim().replaceAll("&", "").replaceAll("%", "").replaceAll("\\+", "").replaceAll("<", "");
            }
            StringBuffer mystrb = new StringBuffer( "phone="+phone+"&password="+password+"&security_code="+securityCode+"&openid="+openid+"&nick_name="+nickName.trim()+"&sex="+sex.trim()+"&register_invitation_code="+registerInvitationCode);
            List<String> listb=new ArrayList<String>();
            listb.add("phone"+phone);
            listb.add("password"+password);
            listb.add("security_code"+securityCode);
            listb.add("openid"+openid);
            listb.add("nick_name"+nickName.trim());
            listb.add("sex"+sex.trim());
            listb.add("register_invitation_code"+registerInvitationCode);
            if(StringUtils.isNotEmpty(address)){
                mystrb.append("&address="+address.trim());
                listb.add("address"+address.trim());
            }else{
				City city = IPUtil.getCityByIP(request);
				if(city.isFlag() && StringUtils.isNotEmpty(city.getCity())){
					logger.info("注册根据IP获取到的信息为country：" + city.getCountry()
							+ ",city：" + city.getCity() + ",province：" + city.getProvince());
					address = city.getCity().replace("市", "").trim();
					mystrb.append("&address="+address);
					listb.add("address"+address);
				}else{
					city=IPUtil.getCityByPhone(phone);
					if(city.isFlag() && StringUtils.isNotEmpty(city.getCity())){
						logger.info("注册根据手机号获取到的信息为city：" + city.getCity());
						address = city.getCity().replace("市", "").trim();
						mystrb.append("&address="+address);
						listb.add("address"+address);
					}
				}
			}
            if(StringUtils.isNotEmpty(headPortrait)){
                mystrb.append("&head_portrait="+headPortrait.trim());
                listb.add("head_portrait"+headPortrait.trim());
            }
			if (StringUtils.isNotEmpty(wybSource)){//来源渠道
				mystrb.append("&wyb_source="+wybSource);
				listb.add("wyb_source"+wybSource);
			}
            arr = listb.toArray(new String[listb.size()]);
			//注册绑定
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_REGISTER_URL,mystrb.toString(),arr));
			if(0 == resultObject.getInt("status")){
				JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
				String userId=data.getString("user_id");
				session.setAttribute("userId", userId);
				session.setAttribute("isMobileBind", "0");
				CookieUtil.getUserInfo();//更新用户的消息标志session
				if(StringUtils.isNotEmpty(openid)){
				    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分"); 
				    Date date=new Date();
				    String regTime=sdf.format(date);
				    String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoindex.do"+Constant.APP_ID+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				    try {
				        url=  java.net.URLEncoder.encode(url,"utf-8");   
			            mystr="url="+url;
			            arr= new String[] {"url"+url};
			            JSONObject resultObject2 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, mystr.toString(), arr));
			            if(0 == resultObject2.getInt("status")){
			                JSONObject data1 = JSONObject.fromObject(resultObject2.getString("data"));
			                url=data1.getString("short_url");
			            }
			        } catch (Exception e) {
			            logger.error(" 短链接出错：" + e.getMessage(), e);
			        }
				    weixinAPIService.sendTemplateMessageByType("4","",phone,regTime,"","","",openid,"", url);
				}
				JSONObject voresult=JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
				String ipAddress=IPUtil.toIpAddr(request);
				boolean flag=true;
                if(StringUtils.isNotEmpty(registerInvitationCode)){
                    logger.info("给邀请人发券"+registerInvitationCode);
                    String inUserId="";
                    String inOpenId="";
                    resultObject=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_HOLDER_INFO_BY_REGCODE, "invCode="+registerInvitationCode));
                    boolean agentFlag=false;
                    if(resultObject.containsKey("status")&&resultObject.getJSONArray("data")!=null){
                        JSONArray jsonArray=resultObject.getJSONArray("data");
                        if(jsonArray.size()>0){
                            inUserId=jsonArray.getJSONObject(0).getString("memberId"); 
                            inOpenId=jsonArray.getJSONObject(0).getString("openid"); 
                            for(int i=0;i<jsonArray.size();i++){
                                if(null!=jsonArray.getJSONObject(i).getString("type")&&jsonArray.getJSONObject(i).getString("type").equals("8")){
                                    agentFlag=true;
                                }
                            }
                        }
                    }
                    if(agentFlag){//推荐人是代理商的，注册后领取代金券
                        StringBuffer vostr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence=5&enjoyer="+userId);
                        if(StringUtils.isNotEmpty(openid)){
                            vostr.append("&openid="+openid);
                        }
                        voresult=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, vostr.toString()));
                        if(voresult.getString("errcode").equals("0")){
                            flag=false;
                            logger.info(userId+"注册成功开始发放被代理商邀请场景代金券");
                            JSONObject vd=voresult.getJSONObject("data");
                            String coupon=vd.getString("bills")+"元";
                            String expDate=DateUtil.convertToDate(vd.getInt("endDate"));
                            if(StringUtils.isNotEmpty(openid)){
                                weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                            }
                            if(StringUtils.isNotEmpty(userId)){
                                String[] arrs;
                                StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
                                List<String> list=new ArrayList<String>();
                                list.add("member_id"+userId);
                                list.add("type5");
                                if(StringUtils.isNotEmpty(expDate)){
                                    mystrs.append("&effective_time="+expDate);
                                    list.add("effective_time"+expDate);
                                }
                                if(StringUtils.isNotEmpty(coupon)){
                                    mystrs.append("&money="+coupon);
                                    list.add("money"+coupon);
                                }
                                mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
                                list.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
                                arrs = list.toArray(new String[list.size()]);
                                try {
                                   JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                               } catch (Exception e) {
									logger.error("发送站内信出错:" + e.getMessage(), e);
                               }
                            }
                        }
                    }
                    
                    String voustr = "ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence=4&enjoyer="+inUserId+"&sponsor="+inUserId+"&busiOperate=0";
                    voresult=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, voustr));
                    if(voresult.getString("errcode").equals("0")){
                        logger.info(inUserId+"推荐注册成功发放代金券");
                        JSONObject vd=voresult.getJSONObject("data");
                        String coupon=vd.getString("bills")+"元";
                        String expDate=DateUtil.convertToDate(vd.getInt("endDate"));
                        if(StringUtils.isNotEmpty(openid)){
                            weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",inOpenId,"", "");
                        }
                        if(StringUtils.isNotEmpty(inUserId)){
                            String[] arrs;
                            StringBuffer mystrs = new StringBuffer("member_id="+inUserId+"&type=5");
                            List<String> list=new ArrayList<String>();
                            list.add("member_id"+inUserId);
                            list.add("type5");
                            if(StringUtils.isNotEmpty(expDate)){
                                mystrs.append("&effective_time="+expDate);
                                list.add("effective_time"+expDate);
                            }
                            if(StringUtils.isNotEmpty(coupon)){
                                mystrs.append("&money="+coupon);
                                list.add("money"+coupon);
                            }
                            mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
                            list.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
                            arrs = list.toArray(new String[list.size()]);
                            try {
                               JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                           } catch (Exception e) {
								logger.error("发送站内信出错:" + e.getMessage(), e);
                           }
                        }
                    }else{
                        logger.info(inUserId+"推荐注册成功已发放代金券失败");
                        if(StringUtils.isNotEmpty(userId)){
                            String code =voresult.getString("errcode");
                            String msgType="";
                            String reason="";// 原因
                            String activityTitle=voresult.getString("errmsg").split(",")[0];//活动标题
                            String number="";//每人限领张数
                            if(code.equals("20035")){
                                msgType="2";
                            }else if(code.equals("20036")){
                                msgType="3";
                            }else if(code.equals("20037")){
                                msgType="4";
                                number=voresult.getString("errmsg").split(",")[1];
                            }
                            String[] arrs;
                            StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type="+msgType);
                            List<String> list=new ArrayList<String>();
                            list.add("member_id"+userId);
                            list.add("type"+msgType);
                            if(StringUtils.isNotEmpty(activityTitle)){
                                mystrs.append("&activity_title="+activityTitle);
                                list.add("activity_title"+activityTitle);
                            }
                            if(StringUtils.isNotEmpty(reason)){
                                mystrs.append("&reason="+reason);
                                list.add("reason"+reason);
                            }
                            if(StringUtils.isNotEmpty(number)){
                                mystrs.append("&number="+number);
                                list.add("number"+number);
                            }
                            mystrs.append("&url="+Constant.URL+"/huodong/");
                            list.add("url"+Constant.URL+"/huodong/");
                            arrs = list.toArray(new String[list.size()]);
                            try {
                               JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                           } catch (Exception e) {
								logger.error("发送站内信出错:" + e.getMessage(), e);
                           }
                        }
                    }
                }
				if(flag){
				    StringBuffer vostr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence=1&enjoyer="+userId);
	                if(StringUtils.isNotEmpty(openid)){
	                    vostr.append("&openid="+openid);
	                }
	                voresult=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, vostr.toString()));
	                if(voresult.getString("errcode").equals("0")){
	                    logger.info(userId+"注册成功开始发放代金券");
	                    JSONObject vd=voresult.getJSONObject("data");
	                    String coupon=vd.getString("bills")+"元";
	                    String expDate=DateUtil.convertToDate(vd.getInt("endDate"));
	                    if(StringUtils.isNotEmpty(openid)){
	                        weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
	                    }
	                    if(StringUtils.isNotEmpty(userId)){
	                        String[] arrs;
	                        StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
	                        List<String> list=new ArrayList<String>();
	                        list.add("member_id"+userId);
	                        list.add("type5");
	                        if(StringUtils.isNotEmpty(expDate)){
	                            mystrs.append("&effective_time="+expDate);
	                            list.add("effective_time"+expDate);
	                        }
	                        if(StringUtils.isNotEmpty(coupon)){
	                            mystrs.append("&money="+coupon);
	                            list.add("money"+coupon);
	                        }
	                        mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
	                        list.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
	                        arrs = list.toArray(new String[list.size()]);
	                        try {
	                           JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
	                       } catch (Exception e) {
								logger.error("发送站内信出错:" + e.getMessage(), e);
	                       }
	                    }
	                }else{
	                    logger.info(userId+"注册成功，发放代金券失败");
	                    if(StringUtils.isNotEmpty(userId)){
	                         String code =voresult.getString("errcode");
	                         String msgType="";
	                         String reason="";// 原因
//	                       String url=Constant.URL+"/huodong/";
	                         String activityTitle=voresult.getString("errmsg").split(",")[0];//活动标题
	                         String number="";//每人限领张数
	                         if(code.equals("20035")){
	                             msgType="2";
	                         }else if(code.equals("20036")){
	                             msgType="3";
	                         }else if(code.equals("20037")){
	                             msgType="4";
	                             number=voresult.getString("errmsg").split(",")[1];
	                         }
	                         String[] arrs;
	                         StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type="+msgType);
	                         List<String> list=new ArrayList<String>();
	                         list.add("member_id"+userId);
	                         list.add("type"+msgType);
	                         if(StringUtils.isNotEmpty(activityTitle)){
	                             mystrs.append("&activity_title="+activityTitle);
	                             list.add("activity_title"+activityTitle);
	                         }
	                         if(StringUtils.isNotEmpty(reason)){
	                             mystrs.append("&reason="+reason);
	                             list.add("reason"+reason);
	                         }
	                         if(StringUtils.isNotEmpty(number)){
	                             mystrs.append("&number="+number);
	                             list.add("number"+number);
	                         }
	                         mystrs.append("&url="+Constant.URL+"/huodong/");
	                         list.add("url"+Constant.URL+"/huodong/");
	                         arrs = list.toArray(new String[list.size()]);
	                         try {
	                             if(StringUtils.isNotEmpty(msgType)){
	                            JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
	                             }
	                        } catch (Exception e) {
								 logger.error("发送站内信出错:" + e.getMessage(), e);
	                        }
	                     }
	                }
				}
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	/**
	 * 完善资料
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/change")
	public @ResponseBody Object change(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		String memberTruename=request.getParameter("trueName");//真实姓名
		String emergencyContactTel=request.getParameter("contactTel");//紧急联系人电话
		String emergencyContactName=request.getParameter("contactName");//紧急联系人
		String memberEmail=request.getParameter("email");//邮箱
		String invitationCode=request.getParameter("invitationCode");//注册邀请码
		String isPersonBusiness=request.getParameter("isBusiness")==null?"0":request.getParameter("isBusiness");//是否是个体工商户  1 是 0 否
		String businessName = request.getParameter("businessName");//商户名称
		//这三个字段需要从微信上面去下载图片
		String fileName=request.getParameter("fileName");//图片名称
		String picId=request.getParameter("picId");//图片 shenwf 图片ID 20150907
		String legalPerson = request.getParameter("legalPerson");//法人姓名
		String isWeixin = request.getParameter("isWeixin")==null?"true":request.getParameter("isWeixin").trim();//判断是否是微信传过来的
		String memberProvinceId = request.getParameter("memberProvinceId");//省份ID
		String memberCityId = request.getParameter("memberCityId");//城市ID
		String memberAreaId = request.getParameter("memberAreaId");//地区ID
		String areaInfo = request.getParameter("areaInfo");//省市区
		String address = request.getParameter("address");//地区内容
		String fileSize="";
		if("true".equalsIgnoreCase(isWeixin)){//微信端
		    Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
	        String accessToken = weixin.getAccessToken();
	        String filepath="";
    		if(isPersonBusiness.equals("1")&&StringUtils.isNotEmpty(picId)){
				picId=weixinAPIService.downloadFromWxAndUploadToAliYun(picId,accessToken,Constant.WYB_BUSINESS);
				if(picId==null){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"营业执照上传出错，请重试！！\"}");
				}
//                try {
//                    if(WeixinUtil.checkAccessToken(accessToken, picId)){//判断accexxTkoen的有效性，无效直接重新获取
//                        weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                        accessToken = weixin.getAccessToken();
//                    }
//                    filepath = WeixinUtil.downloadMediaFromWx(accessToken, picId, Constant.WYB_BUSINESS);//将图片下载到本地服务器
//                    picId=filepath;
//                    logger.info("filepath:"+filepath);
//                    if(filepath==null){
//                        return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！\"}");
//                    }
//                } catch (IOException e1) {
//                    logger.error("下载图片出错:" + e1.getMessage(), e1);
//                }
    		}
		}
		Object userIdObject =  session.getAttribute("userId");//4125
		String memberId="";//用户id
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		if(StringUtils.isNotEmpty(memberEmail)){
		    memberEmail=memberEmail.replaceAll(" ", "");
    		if(!CheckUtil.isEmail(memberEmail)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"邮箱格式有误！\"}");
            }
		}
		memberId = userIdObject.toString();
		try {
			if(StringUtils.isEmpty(memberId)){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"获取用户信息异常\"}");
			}
			if(StringUtils.isEmpty(memberTruename)){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"真实姓名不能为空\"}");
			}
			String[] arr;
			
			List<String> list=new ArrayList<String>();
			list.add("member_id"+memberId);
			if(StringUtils.isNotEmpty(memberTruename)&&!memberTruename.equals("null")){
			    memberTruename=memberTruename.trim();
			}
			list.add("member_truename"+memberTruename);
			StringBuffer mystr = new StringBuffer("member_id="+memberId).append("&member_truename="+memberTruename);
			if(StringUtils.isNotEmpty(emergencyContactTel)){
				mystr.append("&emergency_contact_tel="+emergencyContactTel);
				list.add("emergency_contact_tel"+emergencyContactTel);
			}
			if(StringUtils.isNotEmpty(emergencyContactName)){
				mystr.append("&emergency_contact_name="+emergencyContactName.trim());
				list.add("emergency_contact_name"+emergencyContactName.trim());
			}
			if(StringUtils.isNotEmpty(memberEmail)){
				mystr.append("&member_email="+memberEmail);
				list.add("member_email"+memberEmail);
			}
			if(StringUtils.isNotEmpty(invitationCode)){
				mystr.append("&register_invitation_code="+invitationCode);
				list.add("register_invitation_code"+invitationCode);
			}
			if(StringUtils.isNotEmpty(isPersonBusiness)){
				mystr.append("&is_person_business="+isPersonBusiness);
				list.add("is_person_business"+isPersonBusiness);
			}
			if(StringUtils.isNotEmpty(businessName)){
				mystr.append("&business_name="+businessName.trim());
				list.add("business_name"+businessName.trim());
			}
			if(StringUtils.isNotEmpty(picId)){
				mystr.append("&file="+picId);
				list.add("file"+picId);
			}
			if(StringUtils.isNotEmpty(fileName)){
				mystr.append("&file_name="+fileName.trim());
				list.add("file_name"+fileName.trim());
			}
			if(StringUtils.isNotEmpty(fileSize)){
				mystr.append("&file_size="+fileSize);
				list.add("file_size"+fileSize);
			}
			if(StringUtils.isNotEmpty(legalPerson)){
                mystr.append("&legal_person="+legalPerson.trim());
                list.add("legal_person"+legalPerson.trim());
            }
			if(StringUtils.isNotEmpty(memberProvinceId)){
                mystr.append("&member_provinceid="+memberProvinceId);
                list.add("member_provinceid"+memberProvinceId);
            }
			if(StringUtils.isNotEmpty(memberCityId)){
                mystr.append("&member_cityid="+memberCityId);
                list.add("member_cityid"+memberCityId);
            }
			if(StringUtils.isNotEmpty(memberAreaId)){
                mystr.append("&member_areaid="+memberAreaId);
                list.add("member_areaid"+memberAreaId);
            }else{
                mystr.append("&member_areaid=");
                list.add("member_areaid");
            }
			if(StringUtils.isNotEmpty(address)){
                mystr.append("&address="+address.trim());
                list.add("address"+address.trim());
            }
			if(StringUtils.isNotEmpty(areaInfo)){
                mystr.append("&areainfo="+areaInfo.trim());
                list.add("areainfo"+areaInfo.trim());
            }
			arr = list.toArray(new String[list.size()]);
			//完善资料
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_CHANGE_URL,mystr.toString(),arr));
			if(0 == resultObject.getInt("status")){
				JSONObject date = JSONObject.fromObject(resultObject.getString("data"));
				String userId=date.getString("user_id");
				session.setAttribute("userId", userId);
				memberTruename=date.getString("member_truename");
				session.setAttribute("memberTruename", memberTruename);
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	/**
	 * 帐号设置
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/saveaccounts")
	public @ResponseBody Object saveAccounts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object openidObject =  session.getAttribute("openid");
		String memberName=request.getParameter("memberName");//用户名
		String memberTruename=request.getParameter("trueName");//真实姓名
		String memberEmail=request.getParameter("email");//真实姓名
		String emergencyContactTel=request.getParameter("contactTel");//紧急联系人电话
		String emergencyContactName=request.getParameter("contactName");//紧急联系人
		String invitationCode=request.getParameter("invitationCode");//注册邀请码
		String businessName = request.getParameter("businessName");//商户名称
		String legalPerson = request.getParameter("legalPerson");//姓名
		String isWeixin = request.getParameter("isWeixin")==null?"true":request.getParameter("isWeixin").trim();//判断是否是微信传过来的
		String identityCard = request.getParameter("identityCard");//身份证号（个体工商户认证页）
		String identityCardFront = request.getParameter("identityCardFront");//身份证正面照片地址（个体工商户认证页）
		String identityCardBack = request.getParameter("identityCardBack");//身份证反面照片地址（个体工商户认证页）
		String isAudit = request.getParameter("is_audit");
        String memberProvinceId = request.getParameter("memberProvinceId");//省份ID
        String memberCityId = request.getParameter("memberCityId");//城市ID
        String memberAreaId = request.getParameter("memberAreaId");//地区ID
        String areaInfo = request.getParameter("areaInfo");//省市区
        String address = request.getParameter("address");//地区内容
		if(StringUtils.isNotEmpty(memberEmail)){
		    memberEmail=memberEmail.replaceAll(" ", "");
            if(!CheckUtil.isEmail(memberEmail)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"邮箱格式有误！\"}");
            }
        }
		if (StringUtils.isNotEmpty(emergencyContactTel)) {
		    emergencyContactTel=emergencyContactTel.replaceAll(" ", "");
            if(!(CheckUtil.isMobileNo(emergencyContactTel)||(CheckUtil.isTelNo(emergencyContactTel)))){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"紧急联系人电话格式有误！\"}");
            }
        }
		String openid="";
		if(openidObject!=null&&!"".equals(openidObject)){
            openid = openidObject.toString();
		}
		//这三个字段需要从微信上面去下载图片
		String fileName=request.getParameter("fileName");//图片名称
//		String[] picId = request.getParameterValues("picId");//图片ID
        String picId=request.getParameter("picId");//图片 shenwf 图片ID 20150907
        String fileSize="";
        if("true".equalsIgnoreCase(isWeixin)){//微信端
            Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
            String accessToken = weixin.getAccessToken();
//            String filepath="";
			picId=weixinAPIService.downloadFromWxAndUploadToAliYun(picId,accessToken,Constant.WYB_BUSINESS);
			if(picId==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"营业执照上传出错，请重试！！\"}");
			}
			logger.info("个体工商户执照:"+picId);
			identityCardFront=weixinAPIService.downloadFromWxAndUploadToAliYun(identityCardFront,accessToken,Constant.WYB_POSITIVE);
			if(identityCardFront==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证正面上传出错，请重试！！\"}");
			}
			logger.info("身份证正面:"+identityCardFront);
			identityCardBack=weixinAPIService.downloadFromWxAndUploadToAliYun(identityCardBack,accessToken,Constant.WYB_NEGATIVE);
			if(identityCardBack==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证反面上传出错，请重试！！\"}");
			}
			logger.info("身份证背面:"+identityCardBack);
//            try {
//                if(StringUtils.isNotEmpty(picId)){
//                    if(WeixinUtil.checkAccessToken(accessToken, picId)){//判断accexxTkoen的有效性，无效直接重新获取
//                        weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                        accessToken = weixin.getAccessToken();
//                    }
//                    filepath = WeixinUtil.downloadMediaFromWx(accessToken, picId, Constant.WYB_BUSINESS);//将图片下载到本地服务器
//                    picId=filepath;
//                    logger.info("个体工商户执照:"+filepath);
//                    if(filepath==null){
//                        return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！！\"}");
//                    }
//                }

//                if(StringUtils.isNotEmpty(identityCardFront)){
//                    if(WeixinUtil.checkAccessToken(accessToken, identityCardFront)){//判断accexxTkoen的有效性，无效直接重新获取
//                        weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                        accessToken = weixin.getAccessToken();
//                   }
//                   String frontPath = WeixinUtil.downloadMediaFromWx(accessToken, identityCardFront, Constant.WYB_POSITIVE);//将图片下载到本地服务器
//                   identityCardFront=frontPath;
//                   logger.info("身份证正面:"+frontPath);
//                   if(frontPath==null){
//                       return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！！\"}");
//                   }
//                }
//                if(StringUtils.isNotEmpty(identityCardBack)){
//                    if(WeixinUtil.checkAccessToken(accessToken, identityCardBack)){//判断accexxTkoen的有效性，无效直接重新获取
//                        weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                        accessToken = weixin.getAccessToken();
//                   }
//                   String backPath = WeixinUtil.downloadMediaFromWx(accessToken, identityCardBack, Constant.WYB_NEGATIVE);//将图片下载到本地服务器
//                   identityCardBack=backPath;
//                   logger.info("身份证背面:"+backPath);
//                   if(backPath==null){
//                       return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！！\"}");
//                   }
//                }
//            }catch (IOException e1) {
//                    logger.error("下载图片出错:" + e1.getMessage(), e1);
//            }
        }
		Object userIdObject =  session.getAttribute("userId");
		String memberId="";//用户id
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		memberId = userIdObject.toString();
		try {
			String[] arr;
			StringBuffer mystr = new StringBuffer("member_id="+memberId);
			List<String> list=new ArrayList<String>();
			list.add("member_id"+memberId);
			if(StringUtils.isNotEmpty(memberTruename)){
				mystr.append("&member_truename="+memberTruename.trim());
				list.add("member_truename"+memberTruename.trim());
			}
			if(StringUtils.isNotEmpty(emergencyContactTel)){
				mystr.append("&emergency_contact_tel="+emergencyContactTel);
				list.add("emergency_contact_tel"+emergencyContactTel);
			}
			if(StringUtils.isNotEmpty(emergencyContactName)){
				mystr.append("&emergency_contact_name="+emergencyContactName.trim());
				list.add("emergency_contact_name"+emergencyContactName.trim());
			}
			if(StringUtils.isNotEmpty(memberEmail)){
				mystr.append("&member_email="+memberEmail);
				list.add("member_email"+memberEmail);
			}
			if(StringUtils.isNotEmpty(invitationCode)){
				mystr.append("&register_invitation_code="+invitationCode);
				list.add("register_invitation_code"+invitationCode);
			}
			if(StringUtils.isNotEmpty(businessName)){
				mystr.append("&business_name="+businessName.trim());
				list.add("business_name"+businessName.trim());
			}
			if(StringUtils.isNotEmpty(picId)){
				mystr.append("&file="+picId);
				list.add("file"+picId);
			}
			if(StringUtils.isNotEmpty(fileName)){
				mystr.append("&file_name="+fileName.trim());
				list.add("file_name"+fileName.trim());
			}
			if(StringUtils.isNotEmpty(memberName)){
				mystr.append("&member_name="+memberName.trim());
				list.add("member_name"+memberName.trim());
			}
			if(StringUtils.isNotEmpty(fileSize)){
				mystr.append("&file_size="+fileSize);
				list.add("file_size"+fileSize);
			}
			if(StringUtils.isNotEmpty(legalPerson)){
                mystr.append("&legal_person="+legalPerson.trim());
                list.add("legal_person"+legalPerson.trim());
            }
			
			if(StringUtils.isNotEmpty(identityCard)){
                mystr.append("&identity_card="+identityCard.trim());
                list.add("identity_card"+identityCard.trim());
            }
            if(StringUtils.isNotEmpty(identityCardFront)){
                mystr.append("&identity_card_file_up="+identityCardFront);
                list.add("identity_card_file_up"+identityCardFront);
            }
            if(StringUtils.isNotEmpty(identityCardBack)){
                mystr.append("&identity_card_file_down="+identityCardBack);
                list.add("identity_card_file_down"+identityCardBack);
            }
            if(StringUtils.isNotEmpty(isAudit)){
                mystr.append("&is_audit="+isAudit);
                list.add("is_audit"+isAudit);
            }
            if(StringUtils.isNotEmpty(memberProvinceId)){
                mystr.append("&member_provinceid="+memberProvinceId);
                list.add("member_provinceid"+memberProvinceId);
            }
            if(StringUtils.isNotEmpty(memberCityId)){
                mystr.append("&member_cityid="+memberCityId);
                list.add("member_cityid"+memberCityId);
            }
            if(StringUtils.isNotEmpty(memberAreaId)){
                mystr.append("&member_areaid="+memberAreaId);
                list.add("member_areaid"+memberAreaId);
            }else{
                mystr.append("&member_areaid=");
                list.add("member_areaid");
            }
            if(StringUtils.isNotEmpty(address)){
                mystr.append("&address="+address.trim());
                list.add("address"+address.trim());
            }
            if(StringUtils.isNotEmpty(areaInfo)){
                mystr.append("&areainfo="+areaInfo.trim());
                list.add("areainfo"+areaInfo.trim());
            }
			arr = list.toArray(new String[list.size()]);
			//账号设置
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_SAVE_URL,mystr.toString(),arr));
			if(0 == resultObject.getInt("status")){
				JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
				String userId=data.getString("user_id");
				session.setAttribute("userId", userId);
				if(StringUtils.isNotEmpty(invitationCode)){
				    String ipAddress=IPUtil.toIpAddr(request);
				    String type="";
				    resultObject=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_HOLDER_INFO_BY_REGCODE, "invCode="+invitationCode));
				    boolean agentFlag=false;
                    if(resultObject.containsKey("status")&&resultObject.getJSONArray("data")!=null){
                        JSONArray jsonArray=resultObject.getJSONArray("data");
                        if(jsonArray.size()>0){
                            for(int i=0;i<jsonArray.size();i++){
                                if(null!=jsonArray.getJSONObject(i).getString("type")&&jsonArray.getJSONObject(i).getString("type").equals("8")){
                                    agentFlag=true;
                                }
                            }
                        }
                    }
                    if(agentFlag){//推荐人是代理商的，注册后领取代金券
                        StringBuffer vostr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence=5&enjoyer="+userId);
                        if(StringUtils.isNotEmpty(openid)){
                            vostr.append("&openid="+openid);
                        }
                        JSONObject voresult=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, vostr.toString()));
                        if(voresult.getString("errcode").equals("0")){
                            logger.info(userId+"注册成功开始发放被代理商邀请场景代金券");
                            JSONObject vd=voresult.getJSONObject("data");
                            String coupon=vd.getString("bills")+"元";
                            String expDate=DateUtil.convertToDate(vd.getInt("endDate"));
                            if(StringUtils.isNotEmpty(openid)){
                                weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                            }
                            if(StringUtils.isNotEmpty(userId)){
                                String[] arrs;
                                StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
                                List<String> lists=new ArrayList<String>();
                                lists.add("member_id"+userId);
                                lists.add("type5");
                                if(StringUtils.isNotEmpty(expDate)){
                                    mystrs.append("&effective_time="+expDate);
                                    lists.add("effective_time"+expDate);
                                }
                                if(StringUtils.isNotEmpty(coupon)){
                                    mystrs.append("&money="+coupon);
                                    lists.add("money"+coupon);
                                }
                                mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
                                lists.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
                                arrs = lists.toArray(new String[lists.size()]);
                                try {
                                   JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                               } catch (Exception e) {
									logger.error("发送站内信出错:" + e.getMessage(), e);
                               }
                            }
                        }
                    }
				}
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	

	/**
	 * 用户反馈功能
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/feedback")
	public @ResponseBody Object feedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null|| "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String memberId = userIdObject.toString();//用户id
		String content = request.getParameter("content");//吐槽内容
		String contactWay = request.getParameter("contactWay");//联系方式
		if(content == null|| "".equals(content)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"吐槽内容不能为空\"}");
		}
		if(contactWay == null|| "".equals(contactWay)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"联系方式不能为空\"}");
		}
		String[] picId = request.getParameterValues("picId");//图片ID或者路径什么的
		String imgs = "";
		if(picId == null|| picId.length == 0){
			logger.info("从前端获取到的图片数组为空");
		}else{
			String isWeixin = request.getParameter("isWeixin");//判断是否是微信传过来的
			if("true".equalsIgnoreCase(isWeixin)){//微信端
				Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
		        String accessToken = weixin.getAccessToken();
				boolean flag = false;
				for (int i = 0; i < picId.length; i++) {
					if(WeixinUtil.checkAccessToken(accessToken, picId[i])){//判断accessTkoen的有效性，无效直接重新获取
	                    weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
	                    accessToken = weixin.getAccessToken();
						}
		            String picPath = WeixinUtil.downloadMediaFromWx(accessToken, picId[i], Constant.WYB_ADVICE);//将图片下载到本地服务器
		            logger.info("微信获取图片" + picId[i] + "的路径：" + picPath);
		            if(picPath == null){
		            	flag = true;
		            	break;
		            }else{
	            	   if(i == 0){
	            		   imgs = picPath;
	            	   }else{
	            		   imgs = imgs + ";" + picPath;
	            	   }
		            }
				}
				if(flag){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！\"}");
				}
			}else{//web端
				for (int i = 0; i < picId.length; i++) {
					if(i == 0){
	         		   imgs = picId[i];
	         	   }else{
	         		   imgs = imgs + ";" + picId[i];
	         	   }
				}
			}
		}
		
		//调用PHP
		try {
			String[] arr;
			StringBuffer mystr = new StringBuffer("member_id=" + memberId);
			List<String> list=new ArrayList<String>();
			list.add("member_id" + memberId);
			
			if(StringUtils.isNotEmpty(content)){
				mystr.append("&content=" + content.trim());
				list.add("content" + content.trim());
			}
			if(StringUtils.isNotEmpty(imgs)){
				mystr.append("&imgs=" + imgs);
				list.add("imgs" + imgs);
			}
			if(StringUtils.isNotEmpty(contactWay)){
				mystr.append("&contact_way=" + contactWay.trim());
				list.add("contact_way" + contactWay.trim());
			}
			arr = list.toArray(new String[list.size()]);
			//用户反馈功能
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.USER_FEEDBACK,mystr.toString(),arr));
		} catch (Exception e) {
			logger.error("用户反馈功能出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
    
	/**
	 * 登陆
	 * @param request
	 * @return
	 * @throws IOException 
	 */
//	@SuppressWarnings("unused")
	@RequestMapping("/login")
	public @ResponseBody Object login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		String phone=request.getParameter("phone").trim();
		String password=request.getParameter("password");
		String address=request.getParameter("address");
		String openid="";
		Object openidObject =  session.getAttribute("openid");
		if(openidObject!=null&&!"".equals(openidObject)){
			openid= openidObject.toString();
		}
		Object appidObject =  session.getAttribute("appid");
		String nickName="";
		String sex="";
		String headPortrait="";
		if(openidObject!=null&&!"".equals(openidObject)&&appidObject!=null&&!"".equals(appidObject)){
			openid = openidObject.toString();
			JSONObject jSONObject = weixinAPIService.getUserInfoOfOpenId(appidObject.toString(), openid);
			if(jSONObject.getString("subscribe").equalsIgnoreCase
					(PropertiesUtils.findPropertiesKey("USER_SUBSCRIBE", Constant.CONFIG_FILE_NAME))){
				headPortrait=jSONObject.getString("headimgurl");
			}
		}
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		try {
			String[] arr;
			String mystr;		
            StringBuffer mystrb = new StringBuffer( "phone="+phone+"&password="+password+"&openid="+openid);
            List<String> listb=new ArrayList<String>();
            listb.add("phone"+phone);
            listb.add("password"+password);
            listb.add("openid"+openid);
            if(StringUtils.isNotEmpty(address)){
                mystrb.append("&member_areainfo="+address.trim());
                listb.add("member_areainfo"+address.trim());
            }
            if(StringUtils.isNotEmpty(headPortrait)){
                mystrb.append("&head_portrait="+headPortrait.trim());
                listb.add("head_portrait"+headPortrait.trim());
            }
            arr = listb.toArray(new String[listb.size()]);
			//登陆
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystrb.toString(),arr));
			if(0 == resultObject.getInt("status")){
				JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
				String userId=data.getString("user_id");
				session.setAttribute("userId", userId);
				if(null == data.getString("member_truename")||data.getString("member_truename").equals("null")) {
					data.put("member_truename","");
                }
				String memberTruename=data.getString("member_truename");
				session.setAttribute("memberTruename", memberTruename);
				session.setAttribute("userName", memberTruename);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				resultObject.put("data", data);
				CookieUtil.getUserInfo();//更新用户的消息标志session
				session.setAttribute("backUrl","");
				session.setAttribute("memberMobile",phone);
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	   /**
     * 退出
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping("/logout")
    public @ResponseBody Object logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");
        session.setAttribute("memberTruename", "");
        session.setAttribute("isMobileBind", "");
        session.setAttribute("userId", "");
        session.setAttribute("identityCard", "");
        session.setAttribute("mobile", "");
        session.setAttribute("identityCardFront", "");
        session.setAttribute("identityCardBack", "");
        session.setAttribute("isNews", "");
        session.setAttribute("socialInsured", "");
        session.setAttribute("fundInsured", "");
        session.setAttribute("insurerId", "");
        session.setAttribute("insurerName", "");
        session.setAttribute("fundType", "");
        session.setAttribute("fundTypeName", "");
        session.setAttribute("socialType", "");
        session.setAttribute("socialTypeName", "");
        session.setAttribute("socialNeed", "");
        session.setAttribute("socialBase", "");
        session.setAttribute("socialType", "");
        session.setAttribute("socialTypeName", "");
        session.setAttribute("socialBase", "");
        session.setAttribute("fundNeed", "");
        session.setAttribute("fundBase", "");
        session.setAttribute("fundType", "");
        session.setAttribute("fundTypeName", "");
        session.setAttribute("fundBase", "");
        session.setAttribute("insuranceStartMonth", "");
        session.setAttribute("housingFundStartMonth", "");
        session.setAttribute("insuranceEndMonth", "");
        session.setAttribute("housingFundEndMonth", "");
        session.setAttribute("productId", "");
        session.setAttribute("discount", "");
        session.setAttribute("costPrice", "");
        session.setAttribute("discountPrice", "");
        session.setAttribute("productMonth", "");
        session.setAttribute("subTotal", "");
        session.setAttribute("housingFundCost", "");
        session.setAttribute("insuranceCost", "");
        session.setAttribute("totalAmt", "");
        session.setAttribute("bonusId", "");
        session.setAttribute("bonus", "");
        session.setAttribute("cityId", "");
        session.setAttribute("cityName", "");
        session.setAttribute("householdCity", "");
        session.setAttribute("householdCityName", "");
        session.setAttribute("isBusiness", "");
        
        String memberId="";//用户id
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        memberId = userIdObject.toString();
        
        try {
            String[] arr= new String[] {"member_id"+memberId,"openid0"};
            String mystr = "member_id="+memberId+"&openid=0";
            //账号设置
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_SAVE_URL,mystr,arr));
            
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
	
	/**
	 * 绑定手机号码
	 * @param request
	 * @return
	 */
	@RequestMapping("/bindphone")
	public @ResponseBody Object bindPhone(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
		String memberId=userIdObject.toString();
		String memberMobile=request.getParameter("memberMobile");
		String securityCode=request.getParameter("securityCode");
		if(StringUtils.isEmpty(memberMobile)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
        }
		if(StringUtils.isEmpty(securityCode)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
        }
		try {
			String[] arr= new String[] {"member_id"+memberId,"member_mobile"+memberMobile,"security_code"+securityCode};
			String mystr="member_id="+memberId+"&member_mobile="+memberMobile+"&security_code="+securityCode;
			//修改密码
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_BINDMOBILE_URL,mystr,arr));
			
			if(0 == resultObject.getInt("status")){
				String userId1=resultObject.getString("data");
				session.setAttribute("userId", userId1);
				session.setAttribute("isMobileBind", PropertiesUtils.findPropertiesKey("BIND_MOBILE_CODE", "config.properties"));
			}
			
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}

	/**
	 * 重新绑定手机号码
	 * @param request
	 * @return
	 */
	@RequestMapping("/reBindPhone")
	public @ResponseBody Object reBindPhone(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String memberId=userIdObject.toString();
		String memberMobile=request.getParameter("memberMobile").trim();
		String securityCode=request.getParameter("securityCode");
		String type=request.getParameter("type");// 第一次调用0 第二次确认1
		if(StringUtils.isEmpty(memberMobile)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
		}
		if(StringUtils.isEmpty(securityCode)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
		}
		try {
			String[] arr= new String[] {"member_id"+memberId,"member_mobile"+memberMobile,"security_code"+securityCode,"flag"+type};
			String mystr="member_id="+memberId+"&member_mobile="+memberMobile+"&security_code="+securityCode+"&flag="+type;
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_REBINDMOBILE_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		if(0 == resultObject.getInt("status")) {
			Object openidObj = session.getAttribute("openid");
			String memberName="";//操作者账号
			Object nameObject=session.getAttribute("memberName");
			if(nameObject!=null&&!"".equals(nameObject)){
				memberName= nameObject.toString();
			}
			if (!"".equals(openidObj) && openidObj != null) {
				weixinAPIService.sendTemplateMessageByType("15", "您好，您的无忧保账号"+memberName+"已成功绑定手机号。", "尾号" + memberMobile.substring(memberMobile.length() - 4, memberMobile.length()), DateUtil.convertToDate(DateUtil.getNowIntTime(), "yyyy-MM-dd HH:mm:ss"), "", "", "", openidObj.toString(), "如有疑问请致电400-111-8900或直接点击微信菜单“在线咨询”，小忧将第一时间为您服务！", "");
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultObject);
		return null;
	}

	/**
	 * 重新绑定手机号发送短信验证码
	 * @param request
	 * @return
	 */
	@RequestMapping("/reBindPhoneSend")
	public @ResponseBody Object reBindPhoneSend(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String randCodeMust = (String) session.getAttribute("randCodeMust");
		String sendMsgCountKey="sendMsgCount";
		Integer sendMsgCount=0;
		Integer sendMsgCountLeftTime=3600;
		boolean flag=true;
		JSONObject resultStrS = articleRedisHandleServiceImpl.readVCode(sendMsgCountKey);
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码错误，请重试\"}");
		if(resultStrS != null){
			sendMsgCount=resultStrS.getInt("count");
			sendMsgCountLeftTime=resultStrS.getInt("redisTimeleft");
			if (sendMsgCount>99&&StringUtils.isEmpty(randCodeMust)){
				flag=false;
			}else if (sendMsgCount<100){
				randCodeMust="";
			}
		}else {
			resultStrS = JSONObject.fromObject("{\"status\":0}");
			randCodeMust="";
		}
		if (flag) {
			String memberMobile = request.getParameter("memberMobile").trim();
			String vcode = request.getParameter("vCode");
//		String type=request.getParameter("type");//type=0 不需要图片验证码
			String randCode = (String) session.getAttribute("randCode");
			if ((StringUtils.isEmpty(randCodeMust) && StringUtils.isEmpty(randCode)) || (null != vcode && (randCode.equals(vcode.trim())))) {
				session.setAttribute("randCode", "");
				resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
				String requestType = request.getHeader("X-Requested-With");
				logger.info("手机号：" + memberMobile + " 发送短信请求类型：" + requestType);
				if (null != requestType && requestType.trim().equals("XMLHttpRequest")) {
					try {
						if ("".equals(memberMobile) || memberMobile == null) {
							return JSONObject.fromObject("{\"status\":1,\"msg\":\"输入手机号码不合法\"}");
						}
						String[] arr = new String[]{"phone" + memberMobile, "type2"};
						String mystr = "phone=" + memberMobile + "&type=2";
						//发送手机验证码
						resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_REBINDMOBILESEND_URL, mystr, arr));
					} catch (Exception e) {
						logger.error("获取数据出错:" + e.getMessage(), e);
					}
					//更新redis中的发送记录  shenwf 20170401 V3.52
//					String sendMsgCountKey = "sendMsgCount";
//					Integer i = 0;
//					JSONObject resultStr = articleRedisHandleServiceImpl.read(key);
//					if (resultStr != null) {
//						i = resultStr.getInt("count");
//					} else {
//						resultStr = JSONObject.fromObject("{\"count\":0}");
//					}
					sendMsgCount++;
					resultStrS.put("count", sendMsgCount);
					articleRedisHandleServiceImpl.saveVCode(sendMsgCountKey, resultStrS, sendMsgCountLeftTime);
				}
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultObject);
		return null;
	}

	/**
	 * 重新绑定手机号,原号码验证
	 * @param request
	 * @return
	 */
	@RequestMapping("/reBindPhoneCheck")
	public @ResponseBody Object reBindPhoneCheck(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String memberId=userIdObject.toString();
		String memberMobile=request.getParameter("memberMobile").trim();
		String securityCode=request.getParameter("securityCode");
		if(StringUtils.isEmpty(memberMobile)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
		}
		if(StringUtils.isEmpty(securityCode)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
		}
		try {
			String[] arr= new String[] {"member_mobile"+memberMobile,"security_code"+securityCode};
			String mystr="member_mobile="+memberMobile+"&security_code="+securityCode;
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_REBINDMOBILE_CHECK_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultObject);
		return null;
	}
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/changepassword")
	public @ResponseBody Object changepassword(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		Object userIdObject =  session.getAttribute("userId");
		String oldPassword=request.getParameter("oldPassword");
		
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(StringUtils.isNotEmpty(oldPassword)){
			if(userIdObject==null||"".equals(userIdObject)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
			}
		}
		if(userIdObject !=null ){
			userId = userIdObject.toString();
		}
		
		String password=request.getParameter("password");
		String phone=request.getParameter("phone");
		String securityCode=request.getParameter("securityCode");//验证码
		try {
			String[] arr= new String[] {"password"+password ,"phone"+phone,"security_code"+securityCode};
			String mystr="password="+password+"&phone="+phone+"&security_code=" + securityCode;
			if(StringUtils.isNotEmpty(oldPassword)){
				arr= new String[] {"user_id"+userId, "old_password"+oldPassword, "password"+password };
				mystr="user_id="+userId+"&old_password="+oldPassword+"&password="+password;
			}
			
			//修改密码
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_CHANGEPASSWORD_URL,mystr,arr));
			if(0 == resultObject.getInt("status")){
				JSONObject date = JSONObject.fromObject(resultObject.getString("data"));
				String userId1=date.getString("user_id");
				session.setAttribute("userId", userId1);
				session.setAttribute("isMobileBind", "0");
				if(!StringUtils.isNotEmpty(oldPassword)&&date.has("member_truename")){
                    String memberTruename=date.getString("member_truename");
                    session.setAttribute("memberTruename", memberTruename);
                }
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	/**
	 * 找回密码
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/forgetpassword")
	public @ResponseBody Object forgetpassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    HttpSession session = ContextHolderUtils.getSession();
		String randCodeMust = (String) session.getAttribute("randCodeMust");
		String sendMsgCountKey="sendMsgCount";
		Integer sendMsgCount=0;
		Integer sendMsgCountLeftTime=3600;
		boolean flag=true;
		JSONObject resultStrS = articleRedisHandleServiceImpl.readVCode(sendMsgCountKey);
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码错误，请重试\"}");
		if(resultStrS != null){
			sendMsgCount=resultStrS.getInt("count");
			sendMsgCountLeftTime=resultStrS.getInt("redisTimeleft");
			if (sendMsgCount>99&&StringUtils.isEmpty(randCodeMust)){
				flag=false;
			}else if (sendMsgCount<100){
				randCodeMust="";
			}
		}else {
			resultStrS = JSONObject.fromObject("{\"status\":0}");
			randCodeMust="";
		}
		if (flag) {
			String phone = request.getParameter("phone").trim();
			String vcode = request.getParameter("vCode");
			String randCode = (String) session.getAttribute("randCode");
			if ((StringUtils.isEmpty(randCode) && StringUtils.isEmpty(randCodeMust)) || (null != vcode && (randCode.equals(vcode.trim())))) {
				String ipAddress = IPUtil.toIpAddr(request);
				JSONObject resultStr = JSONObject.fromObject("{\"count\":0}");
				String key = "forgetPWSend" + ipAddress;
				Integer forgetPWSend = 0;
				resultStr = articleRedisHandleServiceImpl.read(key);
				if (resultStr != null) {
					forgetPWSend = resultStr.getInt("count");
				} else {
					resultStr = JSONObject.fromObject("{\"count\":0}");
				}
				if (forgetPWSend < 5) {
					session.setAttribute("randCode", "");
					resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
					String requestType = request.getHeader("X-Requested-With");
					logger.info("手机号：" + phone + " 找回密码发送短信请求类型：" + requestType);
					if (null != requestType && requestType.trim().equals("XMLHttpRequest")) {
						try {
							String[] arr = new String[]{"phone" + phone};
							String mystr = "phone=" + phone;
							//找回密码
							resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_FORGETPASSWORD_URL, mystr, arr));
							if (1100006 == resultObject.getInt("status")) {
								if (forgetPWSend < 4) {
									resultObject.put("msg", "手机号错误，您还可以输入" + (4 - forgetPWSend) + "次，" + (4 - forgetPWSend) + "次错误后找回密码将锁定2小时不能使用");
								} else if (forgetPWSend == 4) {
									resultObject.put("msg", "输入错误次数太多，找回密码功能将被锁定2小时，请稍后再试");
								}
								forgetPWSend++;
								resultStr.put("count", forgetPWSend);
								articleRedisHandleServiceImpl.save(key, resultStr, 2);
							}
							//更新redis中的发送记录  shenwf 20170401 V3.52
//							String sendMsgCountKey = "sendMsgCount";
//							Integer sendMsgCount = 0;
//							resultStr = articleRedisHandleServiceImpl.read(sendMsgCountKey);
//							if (resultStr != null) {
//								sendMsgCount = resultStr.getInt("count");
//							} else {
//								resultStr = JSONObject.fromObject("{\"count\":0}");
//							}
							sendMsgCount++;
							resultStrS.put("count", sendMsgCount);
							articleRedisHandleServiceImpl.saveVCode(sendMsgCountKey, resultStrS,sendMsgCountLeftTime);
						} catch (Exception e) {
							logger.error("获取数据出错:" + e.getMessage(), e);
						}
					}
				} else {
					resultObject.put("msg", "抱歉，由于您输入错误太多，找回密码功能已被锁定2小时，请稍后再试");
				}
			}
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	
	/**
	 * 发送短信验证码
	 * @param request
	 * @return
	 */
	@RequestMapping("/registersend")
	public @ResponseBody Object registersend(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    HttpSession session = ContextHolderUtils.getSession();
		String randCodeMust = (String) session.getAttribute("randCodeMust");
		String sendMsgCountKey="sendMsgCount";
		Integer sendMsgCount=0;
		Integer sendMsgCountLeftTime=3600;
		boolean flag=true;
		JSONObject resultStr = articleRedisHandleServiceImpl.readVCode(sendMsgCountKey);
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码错误，请重试\"}");
		if(resultStr != null){
			sendMsgCount=resultStr.getInt("count");
			sendMsgCountLeftTime=resultStr.getInt("redisTimeleft");
			if (sendMsgCount>99&&StringUtils.isEmpty(randCodeMust)){
				flag=false;
			}else if (sendMsgCount<100){
				randCodeMust="";
			}
		}else {
			resultStr = JSONObject.fromObject("{\"status\":0}");
			randCodeMust="";
		}
		if (flag) {
			String phone = request.getParameter("phone").trim();
			String vcode = request.getParameter("vCode");
			String type = request.getParameter("type");//type=0 不需要图片验证码
//			JSONObject resultObject = JSONObject.fromObject("{\"status\":2,\"msg\":\"验证码错误，请重试\"}");
			String randCode = (String) session.getAttribute("randCode");
//			String randCodeMust = (String) session.getAttribute("randCodeMust");
			if ((null != type && type.equals("0")) || (StringUtils.isEmpty(randCode) && StringUtils.isEmpty(randCodeMust)) || (null != vcode && (randCode.equals(vcode.trim())))) {//没有图片验证是加=null判断表示通过，下面发送后增加网redis的存储
				session.setAttribute("randCode", "");
				String requestType = request.getHeader("X-Requested-With");
				logger.info("手机号：" + phone + " 发送短信请求类型：" + requestType);
				if (null != requestType && requestType.trim().equals("XMLHttpRequest")) {
					try {
						if ("".equals(phone) || phone == null) {
							return JSONObject.fromObject("{\"status\":1,\"msg\":\"输入手机号码不合法\"}");
						}
						String[] arr = new String[]{"phone" + phone};
						String mystr = "phone=" + phone;
						//发送手机验证码
						resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_REGISTERSEND_URL, mystr, arr));

					} catch (Exception e) {
						logger.error("获取数据出错:" + e.getMessage(), e);
					}
					//更新redis中的发送记录  shenwf 20170401 V3.52
//				String key="sendMsgCount";
//				Integer i=0;
//					JSONObject resultStr = articleRedisHandleServiceImpl.read(key);
//					if (resultStr != null) {
//						i = resultStr.getInt("count");
//					} else {
//						resultStr = JSONObject.fromObject("{\"count\":0}");
//					}
					sendMsgCount++;
					resultStr.put("count", sendMsgCount);
					articleRedisHandleServiceImpl.saveVCode(sendMsgCountKey, resultStr,sendMsgCountLeftTime);
				}
			}
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	
	/**
	 * 此方法暂时没有调用的位置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loginindex")
	public String loginindex(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = ContextHolderUtils.getSession();
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		//判断是否需要保存session
		if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}
		Object openidObj =  session.getAttribute("openid");
		if (!"".equals(openidObj)&&openidObj!=null) {
			openid = openidObj.toString();
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":-1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("注册出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				session.setAttribute("userId", userId);
				if(null == data.getString("member_truename")||data.getString("member_truename").equals("null")) {
					data.put("member_truename","");
                }
				String memberTruename=data.getString("member_truename");
				session.setAttribute("memberTruename", memberTruename);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				//判断是否绑定手机
				if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
					return "account/bindmobile";
				}
				result.put("data", data);
				
				Cookie cookie = new Cookie("userId",userId);
				cookie.setMaxAge(120);
				//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
				cookie.setSecure(true);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
	}
	
	/**
	 * 是否阅读支付协议的接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/savereadagreement")
	public @ResponseBody Object saveReadAgreement(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		
		if(userIdObject !=null ){
			userId = userIdObject.toString();
		}else{
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		
		String memberId=userId;
		String isRemind=request.getParameter("isRemind");
		
		if(StringUtils.isEmpty(isRemind)){
			return JSONObject.fromObject("{\"status\":1,\"msg\":\"是否阅读标志为空\"}");
		}
		
		try {
			String[] arr= new String[] {"member_id"+memberId,"is_remind"+isRemind};
			String mystr="member_id="+memberId+"&is_remind="+isRemind;
			//修改密码
			resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SAVE_READ_AGREE_URL,mystr,arr));
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
	}
	
	/**
     * 手机验证登录
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/mobilelogin")
    public @ResponseBody Object mobilelogin(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        Object appidObject =  session.getAttribute("appid");
        Object regInvitCode=session.getAttribute("regInvitCode");
		Object wybSourceObject= session.getAttribute("wybSource");
		String wybSource="";
		if (wybSourceObject!=null&&!"".equals(wybSourceObject)){
			wybSource=wybSourceObject.toString();
		}
        String headPortrait="";
        String regInvCode="";
        String groupId="";
        if(openidObject!=null&&!"".equals(openidObject)&&appidObject!=null&&!"".equals(appidObject)){
            openid = openidObject.toString();
            JSONObject jSONObject = weixinAPIService.getUserInfoOfOpenId(appidObject.toString(), openid);
            if(jSONObject.getString("subscribe").equalsIgnoreCase
					(PropertiesUtils.findPropertiesKey("USER_SUBSCRIBE", Constant.CONFIG_FILE_NAME))){
				headPortrait=jSONObject.getString("headimgurl");
			}
			if (StringUtils.isEmpty(wybSource)){//优先现有的“渠道来源”（wybSource），若为空，再判断和获取公众号关注分组。
				groupId=weixinAPIService.getUserGroup(openid);
				if (StringUtils.isNotEmpty(groupId)){
					wybSource="wxfz"+groupId;
				}
			}
        }
		if (StringUtils.isEmpty(wybSource)){
			wybSource="mweb";
		}
        if(regInvitCode!=null&&!"".equals(regInvitCode)){
            regInvCode=regInvitCode.toString();
        }
        String memberMobile=request.getParameter("memberMobile").trim();
        String securityCode=request.getParameter("securityCode");
        String memberAvatar=headPortrait;
        String nickName=request.getParameter("nickName");
        String address=request.getParameter("address");
        String memberSex=request.getParameter("memberSex");
        if(StringUtils.isEmpty(memberMobile)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
        }
        if(StringUtils.isEmpty(securityCode)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
        }
        try {
            String[] arr;
            StringBuffer mystr = new StringBuffer("member_mobile="+memberMobile+"&security_code="+securityCode);
            List<String> list=new ArrayList<String>();
            list.add("member_mobile"+memberMobile);
            list.add("security_code"+securityCode);
            
            if(StringUtils.isNotEmpty(memberAvatar)){
                mystr.append("&member_avatar="+memberAvatar);
                list.add("member_avatar"+memberAvatar);
            }
            if(StringUtils.isNotEmpty(nickName)){
                nickName=nickName.trim().replaceAll("&", "").replaceAll("%", "").replaceAll("\\+", "").replaceAll("<", "");
                mystr.append("&nick_name="+nickName);
                list.add("nick_name"+nickName);
            }
            if(StringUtils.isNotEmpty(memberSex)){
                mystr.append("&member_sex="+memberSex);
                list.add("member_sex"+memberSex);
            }
            if(StringUtils.isNotEmpty(address)){
                mystr.append("&address="+address.trim());
                list.add("address"+address.trim());
            }else{
				City city = IPUtil.getCityByIP(request);
				if(city.isFlag() && StringUtils.isNotEmpty(city.getCity())){
					logger.info("注册根据IP获取到的信息为country：" + city.getCountry()
							+ ",city：" + city.getCity() + ",province：" + city.getProvince());
					address = city.getCity().replace("市", "").trim();
					mystr.append("&address="+address);
					list.add("address"+address);
				}else{
					city=IPUtil.getCityByPhone(memberMobile);
					if(city.isFlag() && StringUtils.isNotEmpty(city.getCity())){
						logger.info("注册根据手机号获取到的信息为city：" + city.getCity());
						address = city.getCity().replace("市", "").trim();
						mystr.append("&address="+address);
						list.add("address"+address);
					}
				}
			}
            if(StringUtils.isNotEmpty(openid)){
                mystr.append("&openid="+openid);
                list.add("openid"+openid);
            }
            if(StringUtils.isNotEmpty(regInvCode)){
                mystr.append("&register_invitation_code="+regInvCode);
                list.add("register_invitation_code"+regInvCode);
            }
			if (StringUtils.isNotEmpty(wybSource)){//来源渠道
				mystr.append("&wyb_source="+wybSource);
				list.add("wyb_source"+wybSource);
			}
            arr = list.toArray(new String[list.size()]);
            String newMember="0";
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_MOBILELOGIN_URL, mystr.toString(), arr));
            if(0 == resultObject.getInt("status")){
                JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
                String userId=data.getString("user_id");
                newMember=data.getString("new_member");
                session.setAttribute("userId", userId);
				session.setAttribute("memberMobile",memberMobile);
                arr = new String[] {"member_id"+userId};
                mystr= new StringBuffer("member_id="+userId);
                session.setAttribute("isMobileBind", PropertiesUtils.findPropertiesKey("BIND_MOBILE_CODE", "config.properties"));
                CookieUtil.getUserInfo();
                if(StringUtils.isNotEmpty(openid)&&newMember.equals("1")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分"); 
                    Date date=new Date();
                    String regTime=sdf.format(date);
                    String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoindex.do"+Constant.APP_ID+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
                    try {
                        url=  java.net.URLEncoder.encode(url,"utf-8");   
                        String urlStr="url="+url;
                        String[]  urlarr= new String[] {"url"+url};
                        JSONObject resultObject2 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, urlStr, urlarr));
                        if(0 == resultObject2.getInt("status")){
                            JSONObject data1 = JSONObject.fromObject(resultObject2.getString("data"));
                            url=data1.getString("short_url");
                        }
                    } catch (Exception e) {
                        logger.error(" 短链接出错：" + e.getMessage(), e);
                    }
                    weixinAPIService.sendTemplateMessageByType("4","",memberMobile,regTime,"","","",openid,"", url);
                }
              //用户个人资料
                JSONObject resultStr =new JSONObject();
                try {
                    resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr.toString(),arr));
                } catch (Exception e1) {
					logger.error("获取用户信息出错:" + e1.getMessage(), e1);
                }
                if(0 == resultStr.getInt("status")&&newMember.equals("1")){
                    JSONObject data2 = JSONObject.fromObject(resultStr.getString("data"));
                    if(null == data2.getString("member_truename")||data2.getString("member_truename").equals("null")) {
                        data2.put("member_truename","");
                    }
                    String memberTruename=data2.getString("member_truename");
                    session.setAttribute("memberTruename", memberTruename);
					session.setAttribute("userName", memberTruename);
                    String ipAddress=IPUtil.toIpAddr(request);
                    StringBuffer vostr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence=1&enjoyer="+userId);
                    if(StringUtils.isNotEmpty(openid)){
                        vostr.append("&openid="+openid);
                    }
                    JSONObject voresult=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, vostr.toString()));
                    if(voresult.getString("errcode").equals("0")){
                        logger.info(userId+"注册成功已发放代金券");
                        JSONObject vd=voresult.getJSONObject("data");
                        String coupon=vd.getString("bills")+"元";
                        String expDate=DateUtil.convertToDate(vd.getInt("endDate"));
                        String isReceive=vd.getString("isReceive");
                        if(StringUtils.isNotEmpty(openid)&&!isReceive.equals("Y")){
                            weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                        }
                        if(StringUtils.isNotEmpty(userId)&&!isReceive.equals("Y")){
                            String[] arrs;
                            StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
                            List<String> list2=new ArrayList<String>();
                            list2.add("member_id"+userId);
                            list2.add("type5");
                            if(StringUtils.isNotEmpty(expDate)){
                                mystrs.append("&effective_time="+expDate);
                                list2.add("effective_time"+expDate);
                            }
                            if(StringUtils.isNotEmpty(coupon)){
                                mystrs.append("&money="+coupon);
                                list2.add("money"+coupon);
                            }
                            mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
                            list2.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
                            arrs = list2.toArray(new String[list2.size()]);
                            try {
                               JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                           } catch (Exception e) {
								logger.error("发送站内信出错:" + e.getMessage(), e);
                           }
                        }
                    }else{
                        logger.info(userId+"注册成功，发放代金券失败");
                        if(StringUtils.isNotEmpty(userId)){
                             String code =voresult.getString("errcode");
                             String msgType="";
                             String reason="";// 原因
                             String activityTitle=voresult.getString("errmsg").split(",")[0];//活动标题
                             String number="";//每人限领张数
                             if(code.equals("20035")){
                                 msgType="2";
                             }else if(code.equals("20036")){
                                 msgType="3";
                             }else if(code.equals("20037")){
                                 msgType="4";
                                 number=voresult.getString("errmsg").split(",")[1];
                             }
                             String[] arrs;
                             StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type="+msgType);
                             List<String> list2=new ArrayList<String>();
                             list2.add("member_id"+userId);
                             list2.add("type"+msgType);
                             if(StringUtils.isNotEmpty(activityTitle)){
                                 mystrs.append("&activity_title="+activityTitle);
                                 list2.add("activity_title"+activityTitle);
                             }
                             if(StringUtils.isNotEmpty(reason)){
                                 mystrs.append("&reason="+reason);
                                 list2.add("reason"+reason);
                             }
                             if(StringUtils.isNotEmpty(number)){
                                 mystrs.append("&number="+number);
                                 list2.add("number"+number);
                             }
                             mystrs.append("&url="+Constant.URL+"/huodong/");
                             list2.add("url"+Constant.URL+"/huodong/");
                             arrs = list2.toArray(new String[list2.size()]);
                             try {
                                JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                            } catch (Exception e) {
                                logger.error("发送站内信出错:" + e.getMessage(), e);
                            }
                         }
                    }
                    
                }
                resultObject.put("data", data);
				session.setAttribute("backUrl","");
            }
        } catch (Exception e) {
            logger.error(" 手机验证登录出错：" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
    /**
     * 发送短信
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/sendsms")
    public @ResponseBody Object sendsms(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String memberMobile=request.getParameter("memberMobile").trim();
        String requestType = request.getHeader("X-Requested-With");  
        logger.info("手机号："+memberMobile+" 推荐好友发送短信请求类型："+requestType);
        if(null!=requestType&&requestType.trim().equals("XMLHttpRequest")){
            HttpSession session = ContextHolderUtils.getSession();
            String userId="";
            Object userIdObject =  session.getAttribute("userId");
            if(userIdObject !=null ){
                userId = userIdObject.toString();
            }else{
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            String memberTruename=request.getParameter("memberTruename");
            String shortUrl=request.getParameter("shortUrl");
            if(StringUtils.isEmpty(memberMobile)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
            }
            if(StringUtils.isEmpty(memberTruename)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"真实姓名不能为空\"}");
            }        
            if(StringUtils.isEmpty(shortUrl)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"获取分享链接失败\"}");
            }
            try {
                String[] arr = new String[] {"member_id"+userId,"member_mobile"+memberMobile, "member_truename"+memberTruename, "short_url"+shortUrl};
                String mystr = "member_id="+userId+"&member_mobile="+memberMobile+"&member_truename="+memberTruename+"&short_url="+shortUrl;
                resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SENDSMS_URL, mystr.toString(), arr));
            } catch (Exception e) {
                logger.error(" 发送短信出错：" + e.getMessage(), e);
            }
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
    /**
     * 我的邀请记录
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/invitationrecord")
    public @ResponseBody Object invitationRecord(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String registerInvitationCode=request.getParameter("registerInvitationCode");
        String page=request.getParameter("page");
        String pageSize=request.getParameter("pageSize");
        
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }

        if(StringUtils.isEmpty(registerInvitationCode)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
        }
        try {
            String[] arr;
            StringBuffer mystr = new StringBuffer("register_invitation_code="+registerInvitationCode);
            List<String> list=new ArrayList<String>();
            list.add("register_invitation_code"+registerInvitationCode);
            
            if(StringUtils.isNotEmpty(page)){
                mystr.append("&page="+page);
                list.add("page"+page);
            }
            if(StringUtils.isNotEmpty(pageSize)){
                mystr.append("&page_size="+pageSize);
                list.add("page_size"+pageSize);
            }
            arr = list.toArray(new String[list.size()]);
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INVITATIONRECORD_URL, mystr.toString(), arr));
        } catch (Exception e) {
            logger.error(" 手机验证登录出错：" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
	
    
    /**
     * 推荐好友
     * @param request
     * @return
     */
    @RequestMapping("/recommendfriend")
    public String recommendfriend(HttpServletRequest request){
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject voucherObject =new JSONObject();
        //查询当前的活动接口
        HttpSession session = ContextHolderUtils.getSession();
        String memberId="1";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject!=null&&!"".equals(userIdObject)){
            memberId=userIdObject.toString();
        }
        //将session保存，以备后面用到
        String url = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://"+request.getServerName() // 服务器地址
                + request.getContextPath() // 项目名称
                + request.getServletPath(); // 请求页面或其他地址
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            url = url + "?" + (request.getQueryString());//url后面的参数
        }
        String[] arr = new String[] {"member_id"+memberId};
        String mystr = "member_id="+memberId;
        String memberName="";
        String invitCode="";
        String title="";
        String desc="";
      //用户个人资料
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr,arr));
        } catch (Exception e1) {
            logger.error(e1.getMessage(),e1);
        }
        if(0 == resultStr.getInt("status")){
            JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
            if(null == data.getString("member_truename")||data.getString("member_truename").equals("null")) {
                data.put("member_truename",data.getString("member_name"));
                
            }
            if(null == data.getString("invitation_code")||data.getString("invitation_code").equals("null")) {
                data.put("invitation_code","000000");
            }
            if(null == data.getString("member_name")||data.getString("member_mobile").equals("null")) {
                data.put("member_mobile","");
            }
            memberName=data.getString("member_truename");
            invitCode=data.getString("invitation_code");
            
        }
        //分享参数
        String shareurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoinvitation.do"+Constant.APP_ID+"memberId="+memberId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";//缩略图
        //分享参数
        String timestamp =Constant.TIME_STAMP;
        String noncestr = Constant.NONCESTR;
        logger.info("shareurl:"+shareurl);
        String shortUrl="";
        try {
            String shareurl2=  java.net.URLEncoder.encode(shareurl,"utf-8");   
            mystr="url="+shareurl2;
            arr= new String[] {"url"+shareurl2};
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, mystr.toString(), arr));
            if(0 == resultStr.getInt("status")){
                JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
                shortUrl=data.getString("short_url");
            }
        } catch (Exception e) {
            logger.error(" 手机验证登录出错：" + e.getMessage(), e);
        }
        
        try {
            String voucherremark="triggerSence=4";
            voucherObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ACTIVITY_INFO, voucherremark));
        } catch (Exception e) {
            logger.error(" 查找用户所有拥有的代金券出错：" + e.getMessage(), e);
        }
        if(voucherObject.getString("errcode").equals("0")){
            JSONArray titles=voucherObject.getJSONArray("data");
            int length = titles.size();
            for(int i = 0; i < length; i++){//遍历JSONArray
                JSONObject oj = titles.getJSONObject(i);
                if(oj.getString("entityCode").trim().equals("title")){
                    title=oj.getString("entityValue");
                }
                if(oj.getString("entityCode").trim().equals("description")){
                    desc=oj.getString("entityValue");
                }
            }
        }
        
        logger.info("shortUrl:"+shortUrl);
        Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
        String jsapiTicket = weixin.getJsapiTicket();
        logger.info("jsapi_ticket:"+jsapiTicket);
        String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
        logger.info("signature:"+signature);
        request.setAttribute("timestamp", timestamp);
        request.setAttribute("noncestr", noncestr);
        request.setAttribute("signature", signature);
        request.setAttribute("appid", Constant.APP_ID);
        request.setAttribute("shareurl", shareurl);
        request.setAttribute("shortUrl", shortUrl);
        request.setAttribute("title", title);
        request.setAttribute("desc", desc);
        return "my/recommendfriend";
    }
    /**
     * 推荐好友短链接
     * @param request
     * @return
     */
    @RequestMapping("/inviteShortUrl")
    public @ResponseBody Object  inviteShortUrl(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject resultStr =new JSONObject();
        //查询当前的活动接口
        HttpSession session = ContextHolderUtils.getSession();
        String memberId="1";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject!=null&&!"".equals(userIdObject)){
            memberId=userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        //分享参数
        String shareUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoinvitation.do"+Constant.APP_ID+"memberId="+memberId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";//二维码用链接
        String smsUrl=Constant.URL + "/personsocial/gotoinvitation.do?memberId="+memberId;//发短信用链接
        String shortUrl = "";
        logger.info("shareurl:" + shareUrl);
        logger.info("smsurl:" + shareUrl);
        try {
			shareUrl = java.net.URLEncoder.encode(shareUrl,"utf-8");
            String myStr = "url=" + shareUrl;
            String[] arr= new String[] {"url"+shareUrl};
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, myStr, arr));
            if(0 == resultObject.getInt("status")){
                resultStr.put("status", 0);
                shortUrl=JSONObject.fromObject(resultObject.getString("data")).getString("short_url");
                resultStr.put("short_url", JSONObject.fromObject(resultObject.getString("data")).getString("short_url"));
            }

			myStr = "url=" + smsUrl;
            arr= new String[] {"url" + smsUrl};
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SINA_SHORT_URL, myStr, arr));
            if(0 == resultObject.getInt("status")){
                resultStr.put("status", 0);
                resultStr.put("smsUrl", JSONObject.fromObject(resultObject.getString("data")).getString("short_url"));
            }

			resultObject=JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_INVITATION_CODE, "memberId=" + memberId));
			if(0 == resultObject.getInt("status")){
				resultStr.put("status", 0);
				resultStr.put("invitationCode", resultObject.get("data"));
			}
        } catch (Exception e) {
            logger.error(" 推荐好友短链接出错：" + e.getMessage(), e);
        }
        request.setAttribute("shortUrl", shortUrl);
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    
    /**
     * 通过手机查询用户信息
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/memberbymobile")
    public @ResponseBody Object memberbymobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String memberMobile=request.getParameter("memberMobile");
        if(StringUtils.isEmpty(memberMobile)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
        }
        try {
            String mystr="member_mobile="+memberMobile;
            String[] arr= new String[] {"member_mobile"+memberMobile};
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MEMBERBYMOBILE_URL, mystr.toString(), arr));
        } catch (Exception e) {
            logger.error(" 手机查询用户信息出错：" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
    /**
     * 微信分享得代金卷获取手机查询
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/findmobile")
    public @ResponseBody Object findmobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String memberMobile=request.getParameter("memberMobile");
        try {
            StringBuffer mystr = new StringBuffer("openid="+openid);
            List<String> list=new ArrayList<String>();
            list.add("openid"+openid);
            if(StringUtils.isNotEmpty(memberMobile)){
                mystr.append("&member_mobile="+memberMobile);
                list.add("member_mobile"+memberMobile);
            }
            String[]  arr = list.toArray(new String[list.size()]);
            resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_FIND_URL, mystr.toString(), arr));
            if(0 == resultObject.getInt("status")){
                JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
                memberMobile=data.getString("member_mobile");
            }
        } catch (Exception e) {
            logger.error(" 微信分享得代金卷获取手机查询出错：" + e.getMessage(), e);
        }
        request.setAttribute("memberMobile", memberMobile);
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultObject);
        return null;
    }
    /**
     * 微信分享得代金卷获取手机添加
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/savemobile")
    public @ResponseBody Object savemobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject resultStr2 = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject mmresult = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject jsonObj = new JSONObject();
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        String mystr="";
        String errorMsg="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }else{
            openid= request.getParameter("openid");
        }
        String memberId="";
        if(StringUtils.isNotEmpty(openid)){
            String[] arrf=new String[]{"openid"+openid};
            String mystrf="openid="+openid;
            try {
               JSONObject resultStr3 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_FIND_URL,mystrf,arrf));//获取手机号
                if(0 == resultStr3.getInt("status")){
                    JSONObject data = JSONObject.fromObject(resultStr3.getString("data"));
                    memberId=data.getString("member_id");
                }
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
        }
        String memberMobile=request.getParameter("memberMobile");
        String orderId=request.getParameter("orderId");
        String payTime=request.getParameter("payTime");
        String type=request.getParameter("type");
        String userId="";
        String[] arrp = new String[] {"openid"+openid};
        String mystrp = "openid="+openid;
        JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String ipAddress=IPUtil.toIpAddr(request);
        String operateUser="0";
        String triggerSence="3";
        String rvmystr="ipAddress="+ipAddress+"&operateUser="+operateUser+"&triggerSence="+triggerSence;
        if(StringUtils.isNotEmpty(orderId)){
            rvmystr+="&orderNo="+orderId;
        }
        if(StringUtils.isNotEmpty(openid)){
            rvmystr+="&openId="+openid;
        }
        if(StringUtils.isNotEmpty(memberMobile)){
            rvmystr+="&mobile="+memberMobile;
        }
        if(StringUtils.isNotEmpty(payTime)){
            rvmystr+="&orderTime="+payTime;
        }
        try {
            //根据openid判断是否绑定
            result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystrp,arrp));
        } catch (Exception e) {
			logger.error("微信分享得代金卷获取手机添加登录出错:" + e.getMessage(), e);
        }
        if(0 == result.getInt("status")){
            JSONObject data = JSONObject.fromObject(result.getString("data"));
            userId=data.getString("user_id");
        }
        mystr="orderNo="+orderId;
        if(StringUtils.isNotEmpty(openid)){
            mystr+="&openId="+openid;
        }
        if(StringUtils.isNotEmpty(memberMobile)){
            mystr+="&mobile="+memberMobile;
        }
        String mmystr="member_mobile="+memberMobile;
        String[] marr= new String[] {"member_mobile"+memberMobile};
        String muserId="";
        try {
            mmresult = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MEMBERBYMOBILE_URL, mmystr.toString(), marr));//通过手机查询用户信息
        } catch (Exception e) {
            logger.error(" 手机查询用户信息出错：" + e.getMessage(), e);
        }
        if(0 == mmresult.getInt("status")){
            JSONObject data = JSONObject.fromObject(mmresult.getString("data"));
            muserId=data.getString("member_id");
        }
        String status="0"; //0 活动有效 未领过   1 已领过   2 未领过，活动失效
        if(type.equals("0")){
            resultStr2 = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ISHADSHARE,mystr));//判断是否享受过活动或者领用过代金券 0未领过
            if(0 == resultStr2.getInt("data")){
                status="0";
                String orderTime=payTime;
                mystr="triggerSence=3";//触发场景 1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
                if(StringUtils.isNotEmpty(orderId)){
                    mystr+="&orderNo="+orderId;
                }
                if(StringUtils.isNotEmpty(orderTime)){
                    mystr+="&orderTime="+orderTime;
                }
                resultStr2 = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY,mystr));//判断是否享有活动
                if(resultStr2.getString("errcode").equals("0")){
                    if(resultStr2.getString("data")!=null&&resultStr2.getString("data").equals("false")){
                        status="2";
						return JSONObject.fromObject("{\"status\":1,\"msg\":\"很抱歉，当前没有代金券活动，请关注我们其他的活动，如有疑义请详询400-111-8900\"}");//2017-3-20 shenwf 没有活动的提示
                    }
                }else{
                    request.setAttribute("memberMobile", memberMobile);
                    request.setAttribute("voucherStatus", status);
                    return JSONObject.fromObject("{\"status\":1,\"msg\":\""+resultStr2.getString("errmsg")+"\"}");
                }
            }else{
            	if(StringUtils.isNotEmpty(userId)){
                    rvmystr+="&enjoyer="+userId;
                }else if(StringUtils.isNotEmpty(muserId)){
                    rvmystr+="&enjoyer="+muserId;
                }
                logger.info("savemoble 验证  领取  fail");
                JSONObject resultStrv = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SEARCH,rvmystr));//查询代金券
                if(resultStrv.getString("errcode").equals("0")){
                    jsonObj.put("status", "0");
                    jsonObj.put("data",resultStrv.getString("data"));
                 }
                status="1";
                request.setAttribute("voucherStatus", status);
                response.setContentType("text/html; charset=utf-8");  
                PrintWriter out=response.getWriter();
                out.println(jsonObj);
                return null;
            }
            if(status.equals("0")){
                if(StringUtils.isEmpty(memberMobile)){
                    return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
                }
                try {
                    mystr="openid="+openid+"&member_mobile="+memberMobile;
                    String[] arr= new String[] {"openid"+openid,"member_mobile"+memberMobile};
                    resultStr2 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_SAVE_URL, mystr.toString(), arr));
                } catch (Exception e) {
                    logger.error(" 微信分享得代金卷获取手机添加出错：" + e.getMessage(), e);
                }
                //不知道上面的userid有什么作用，这里代金券领取时使用的享受人ID是使用上面手机号码获取对应的用户ID
                rvmystr+="&enjoyer="+muserId;
                resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE,rvmystr));//领取代金券
                if(resultStr.getString("errcode").equals("0")){
                    jsonObj.put("status", "0");
                    jsonObj.put("data",resultStr.getString("data"));
                    String coupons="";
                    String isReceive="";
                    if(resultStr.getString("data")!=null&&!resultStr.getString("data").equals("null")){
                        coupons=resultStr.getJSONObject("data").getString("bills");
                        isReceive=resultStr.getJSONObject("data").getString("isReceive");
                    }
                    if(!isReceive.equals("Y")){
                        String coupon=coupons+"元";
                        String expDate=DateUtil.convertToDate(resultStr.getJSONObject("data").getInt("endDate"));
                        if(StringUtils.isNotEmpty(openid)&&StringUtils.isNotEmpty(coupons)){
                            weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                        }
                        if(StringUtils.isNotEmpty(userId)){
                            String[] arrs;
                            StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
                            List<String> list=new ArrayList<String>();
                            list.add("member_id"+userId);
                            list.add("type5");
                            if(StringUtils.isNotEmpty(expDate)){
                                mystrs.append("&effective_time="+expDate);
                                list.add("effective_time"+expDate);
                            }
                            if(StringUtils.isNotEmpty(coupon)){
                                mystrs.append("&money="+coupon);
                                list.add("money"+coupon);
                            }
                            mystrs.append("&url="+Constant.URL+"/jsp/my/mytickets.jsp");
                            list.add("url"+Constant.URL+"/jsp/my/mytickets.jsp");
                            arrs = list.toArray(new String[list.size()]);
                            try {
                               JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                           } catch (Exception e) {
								logger.error("发送站内信出错:" + e.getMessage(), e);
                           }
                        }
                    }
                    if(StringUtils.isEmpty(memberId)){
                        String[] smsarr = new String[] {"member_mobile"+memberMobile, "money"+coupons};
                        String smsmystr = "member_mobile="+memberMobile+"&money="+coupons;
                        try {
                            JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SENDSMSV_URL, smsmystr.toString(), smsarr));
                        } catch (Exception e) {
							logger.error("发送短信出错:" + e.getMessage(), e);
                        }
                    }
                }else{
                    logger.info("status  ==1     user= "+userId);
                    jsonObj.put("status", "2");
                    status="2";
                    jsonObj.put("msg", resultStr.getString("errmsg"));
//                    if(StringUtils.isNotEmpty(userId)){
                        String code =resultStr.getString("errcode");
                        String msg[]=resultStr.getString("errmsg").split(",");
                        String msgType="";
                        String reason="";// 原因
//                      String url=Constant.URL+"/huodong/";
                        String activityTitle=resultStr.getString("errmsg").split(",")[0];//活动标题
                        String number="";//每人限领张数
                        if(code.equals("20035")){
                            msgType="2";
                            errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”今天的数量已发放完，请关注我们其他的活动，如有疑义请详询400-111-8900";
                        }else if(code.equals("20036")){
                            msgType="3";
                            errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”已全部被领光啦，请关注我们其他的活动，如有疑义请详询400-111-8900";

                        }else if(code.equals("20037")){
                            msgType="4";
                            number=resultStr.getString("errmsg").split(",")[1];
                            errorMsg="很抱歉，您参与的活动“"+msg[0]+"”已超过每人限领 "+number+" 张，请关注我们其他的活动，如有疑义请详询400-111-8900";
                        }
					if(StringUtils.isNotEmpty(userId)){
                        String[] arrs;
                        StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type="+msgType);
                        List<String> list=new ArrayList<String>();
                        list.add("member_id"+userId);
                        list.add("type"+msgType);
                        if(StringUtils.isNotEmpty(activityTitle)){
                            mystrs.append("&activity_title="+activityTitle);
                            list.add("activity_title"+activityTitle);
                        }
                        if(StringUtils.isNotEmpty(reason)){
                            mystrs.append("&reason="+reason);
                            list.add("reason"+reason);
                        }
                        if(StringUtils.isNotEmpty(number)){
                            mystrs.append("&number="+number);
                            list.add("number"+number);
                        }
                        mystrs.append("&url="+Constant.URL+"/huodong/");
                        list.add("url"+Constant.URL+"/huodong/");
                        arrs = list.toArray(new String[list.size()]);
                        try {
                           JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                       } catch (Exception e) {
							logger.error("发送站内信出错:" + e.getMessage(), e);
                       }
                    }
                    logger.info("领取失败 退出 原因"+errorMsg);
                    return JSONObject.fromObject("{\"status\":1,\"msg\":\""+errorMsg+"\"}");
                }
            }
            logger.info("savemobile status="+status);
        }else{
            logger.info("只是修改手机号");
            if(StringUtils.isEmpty(memberMobile)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
            }
            try {
                mystr="openid="+openid+"&member_mobile="+memberMobile;
                String[] arr= new String[] {"openid"+openid,"member_mobile"+memberMobile};
                resultStr2 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_SAVE_URL, mystr.toString(), arr));
            } catch (Exception e) {
                logger.error(" 微信分享得代金卷获取手机添加出错：" + e.getMessage(), e);
            }
        }
        request.setAttribute("memberMobile", memberMobile);
        if(!type.equals("0")){
            jsonObj=resultStr2;
        }
        request.setAttribute("voucherStatus", status);
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    
    /**
     * 获取代理商所属用户数量统计
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping("/agentUserNum")
    public @ResponseBody Object agentUserNum(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_USERS_COUNT,"policyHolderId="+userIdObject.toString()));
        } catch (Exception e) {
            logger.error("获取代理商所属用户数量统计:" + e.getMessage(), e);
        }
        if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    
    /**
     * 获取代理商所属用户
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping("/agentUsers")
    public @ResponseBody Object agentUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
//        userIdObject=request.getParameter("memberID");//代理商下投保人ID
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String type=request.getParameter("type");//0未购买1已购买
        
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_USERS,"policyHolderId="+userIdObject.toString()+"&type="+type));
        } catch (Exception e) {
            logger.error("获取代理商所属用户:" + e.getMessage(), e);
        }
        if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    
    /**
     * 获取代理商所属已购买的用户信息
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping("/agentBought")
    public @ResponseBody Object agentBought(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject resultStrDetail = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        if(userIdObject==null||"".equals(userIdObject)){
        //    return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String memberID=request.getParameter("memberID");//代理商下投保人ID
        try {
            resultStrDetail = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_BOUGHT_DETAIL,"policyHolderId="+memberID));
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_BOUGHT_COUNT,"policyHolderId="+memberID));
            if(resultStr.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr=data;
            }else if(resultStrDetail.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStrDetail.getString("errcode"));
                data.put("msg", resultStrDetail.getString("errmsg"));
                resultStr=data;
            }else{
                resultStr.getJSONObject("data").put("records", resultStrDetail.getJSONArray("data"));
            }
        } catch (Exception e) {
            logger.error("获取代理商所属已购买的用户信息:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }

	/**
	 * 微信更新文章点击量
	 * @param request
	 * @param response
	 * @author swf
	 * @throws IOException
	 */
	@RequestMapping("/clickRate")
	public @ResponseBody Object clickRate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String requestType = request.getHeader("X-Requested-With");
		if(null!=requestType&&requestType.trim().equals("XMLHttpRequest")) {
			String advId=request.getParameter("advId");
			try {
				StringBuffer mystr = new StringBuffer("adv_id=" + advId);
				String[] arr = new String[]{"adv_id" + advId};
				resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.BANNER_CLICK_RATE, mystr.toString(), arr));
			} catch (Exception e) {
				logger.error(" 微信分享得代金卷获取手机查询出错：" + e.getMessage(), e);
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultObject);
		return null;
	}

}