package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.entity.Store;
import com.st.utils.Constant;
import com.st.ktv.entity.City;
import com.st.utils.IPUtil;
import com.st.ktv.entity.wx.Weixin;
import com.st.ktv.service.ArticleService;
import com.st.ktv.service.impl.WechatAPIServiceImpl;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/weixin")
public class WeixinController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WechatAPIServiceImpl weixinAPIService;
    @Resource
    private ArticleService articleService;

    /**
     * 获取微信js调用所需的变量
     * 
     * @param request
     * @return object
     */
    @RequestMapping("/getweixin")
    public String getWeixin(HttpServletRequest request) {
    	HttpSession session= ContextHolderUtils.getSession();
		
	    String param = request.getParameter("name");//获取跳转首个参数，该字段代表为想要跳转到的页面
        String param1 = request.getParameter("param1");//该参数不是必须参数，座位某些页面的关键字段，留待哪些页面去使用
        String invitCode=request.getParameter("invitCode");
        if(DataUtil.isNotEmpty(invitCode)){
            session.setAttribute("regInvitCode", invitCode);
            session.setAttribute("invitCode", invitCode);
        }
        String userId = "";
	    String invitationCode="";
	    Object userIdObject=session.getAttribute("userId");
	    if ((userIdObject==null||"".equals(userIdObject)) && "payshebao/addperson".equalsIgnoreCase(param)) {
	    	logger.info("当进入新增参保人页面时，需先去判断是否为登录状态，未登录进入登录页面");
	    	return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
		}
	    if (null != userIdObject && !"".equals(userIdObject)) {
	    	userId = userIdObject.toString();
	    	
	        if(userIdObject!=null&&!"".equals(userIdObject)){
	            userId=userIdObject.toString();
	            JSONObject userInfo = JSONObject.fromObject(JoYoUtil.sendGet("","policyHolderId="+userId));
    	        if(userInfo.containsKey("status")){
    	            JSONArray jsonArray=userInfo.getJSONArray("data");
    	            if(jsonArray.size()>0){
    	                for(int i=0;i<jsonArray.size();i++){
    	                    invitationCode=jsonArray.getJSONObject(i).getString("invitationCode");
    	                }
    	            }
    	        }
	        }
		}
		
        String url = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() // 服务器地址
                + request.getContextPath() // 项目名称
                + request.getServletPath(); // 请求页面或其他地址
        if (DataUtil.isNotEmpty(request.getQueryString())) {
            url = url + "?" + (request.getQueryString());//url后面的参数
        }
        logger.info("JS调用时的原始：" + url);// 当前网页的URL，不包含#及其后面部分
        //微信分享的伪静态
        String shareUrl=url;
        if(request.getServletPath().equalsIgnoreCase("/weixin/getweixin.do")
        		&& null != param && !"".equalsIgnoreCase(param) ){

        }
        // 分享参数
        logger.info("JS调用时的确切路径，需要在加密时使用：" + url);// 当前网页的URL，不包含#及其后面部分
        try{
        	 Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
             String jsapiTicket = weixin.getJsapiTicket();
             logger.info("jsapi_ticket:" + jsapiTicket);
             String signature = SignUtil.getSignature(jsapiTicket,
                     Constant.TIME_STAMP, Constant.NONCESTR, url);
             logger.info("signature:" + signature);

             request.setAttribute("signature", signature);
             request.setAttribute("jsapi_ticket", jsapiTicket);
        }catch (Exception e) {//出错时该功能肯定是不能使用了，所以直接跳转到首页，此时不能分享
			logger.error("获取微信调用出错：" + e.getMessage(), e);
			return "index/index";
		}
        if(param!=null&&param.indexOf("calculator")>-1){
            shareUrl= "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotojsq.do"+Constant.APP_ID+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        }
        if(param!=null&&param.indexOf("index")>-1){
            shareUrl= "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoindex.do"+Constant.APP_ID+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        }
        request.setAttribute("timestamp", Constant.TIME_STAMP);
        request.setAttribute("noncestr", Constant.NONCESTR);
        request.setAttribute("url", url);
        request.setAttribute("shareUrl", shareUrl);
        request.setAttribute("appid", Constant.APP_ID);
        request.setAttribute("param1", param1);
        request.setAttribute("userId", userId);
        request.setAttribute("invCode",invitationCode);
        logger.info("获取到的跳转页面参数为：" + param);
        if (null==param||DataUtil.isEmpty(param)||(param.indexOf("index/index")>-1)) { //参数为空时，直接跳转到首页，一般参数会有
            JSONObject jsonObjectBanner=articleService.bannerDemend(request,"wyb_wechat_banner");
            JSONObject jsonObjectzk=articleService.articleDemend(request,"181",null,null,1,5,null,null,null);//社保周刊
            JSONObject jsonObjectwt=articleService.articleDemend(request,"113",null,null,1,3,"1",null,"1");//常见问题
            JSONObject jsonObjecttj=articleService.articleDemend(request,null,null,null,1,5,null,null,null);//推荐文章
            JSONObject indexInfo=new JSONObject();
            try{
                JSONObject jsonStr = JSONObject.fromObject(JoYoUtil.sendGet("", "" ));
                if (jsonStr.getInt("status") == 0) {
                    indexInfo.put("buyList",jsonStr.getJSONArray("data"));
                }
            }catch (Exception e) {
                logger.error("首页信息:" + e.getMessage(), e);
                indexInfo= JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            }
            request.setAttribute("banner",jsonObjectBanner);
            request.setAttribute("zk",jsonObjectzk);
            request.setAttribute("wt",jsonObjectwt);
            request.setAttribute("tj",jsonObjecttj);
            request.setAttribute("info",indexInfo);
            return "index/index";
        } else {
            return param;
        }
    }


    /**
     * 直接访问域名跳转首页推送相关数据
     * @param request
     * @return object
     */
    @RequestMapping("/getWeixintoIndex")
    public String getWeixintoIndex(HttpServletRequest request) {

        List<Store> list = new ArrayList<Store>();
        for(int i =1 ; i<5 ; i++){
            Store store = new Store();
            store.setName("test");
            store.setBigNum(3);
            store.setDistance(BigDecimal.TEN);
            store.setMediumNum(7);
            store.setSmallNum(3);
            store.setVipNum(8);
            list.add(store);
        }
        request.setAttribute("storeList",list);
        return "index";
    }


    /**
     * 发送微信m模版消息
     * 
     * @param request
     * @return object
     */
    @RequestMapping("/sendTemplateMessage")
    public @ResponseBody Object sendTemplateMessage(HttpServletRequest request) {
        String type=request.getParameter("type");  //0 业务服务提醒 ；  1 认证通知；2 消息提醒 ；3 获得代金券通知；4注册通知；
        //5 参保成功通知；6参保失败通知；7停保成功通知；8停保失败通知；9退款成功通知；10服务到期提醒；11业务办理取消通知；12订单未支付通知;13订单支付成功;14业务动态提醒;15手机号绑定
        String firstStr=request.getParameter("first")==null?"":request.getParameter("first");
        String keyword1Str=request.getParameter("keyword1")==null?"":request.getParameter("keyword1");
        String keyword2Str=request.getParameter("keyword2")==null?"":request.getParameter("keyword2");
        String keyword3Str=request.getParameter("keyword3")==null?"":request.getParameter("keyword3");
        String keyword4Str=request.getParameter("keyword4")==null?"":request.getParameter("keyword4");
        String keyword5Str=request.getParameter("keyword5")==null?"":request.getParameter("keyword5");
        String remarkStr=request.getParameter("remark")==null?"":request.getParameter("remark");
        String url=request.getParameter("url")==null?"":request.getParameter("url");
        String openId=request.getParameter("openId")==null?"":request.getParameter("openId");
  //      openId="on7Tqvg7aZC7syuOTTYG60aPD3Ig";
        JSONObject resultStr=new JSONObject();
        if(null!=type){
            resultStr=weixinAPIService.sendTemplateMessageByType(type,firstStr,keyword1Str,keyword2Str,keyword3Str,keyword4Str,keyword5Str,openId,remarkStr, url);
        }
        return resultStr;
    }


    /**
     * 根据IP地址解析获取城市名
     * @param request
     * @param response
     * @return json
     * @throws Exception
     */
    @RequestMapping("/getCityNameByIP")
    public @ResponseBody Object getCityNameByIP(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            //城市列表查询
            City city = IPUtil.getCityByIP(request);
            if(city.isFlag() && DataUtil.isNotEmpty(city.getCity())){
                logger.info("根据IP获取到的信息为country：" + city.getCountry()
                        + ",city：" + city.getCity() + ",province：" + city.getProvince());
                String address = city.getCity().replace("市", "").trim();
                resultStr.put("status",0);
                resultStr.remove("msg");
                resultStr.put("data",address);
            }
        } catch (Exception e) {
            logger.error("城市列表查询出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    /**
     * 根据手机号获取城市信息
     * @param request
     * @param response
     * @return json
     * @throws Exception
     */
    @RequestMapping("/getCityNameByMobile")
    public @ResponseBody Object getCityNameByMobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String memberMobile=request.getParameter("memberMobile");
        if (DataUtil.isEmpty(memberMobile)){
            HttpSession session = ContextHolderUtils.getSession();
            memberMobile=(String)session.getAttribute("memberMobile");
        }
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            //城市列表查询
            if (DataUtil.isNotEmpty(memberMobile)&&DataUtil.isMobileNo(memberMobile)) {
                City city = IPUtil.getCityByPhone(memberMobile);
                if (city.isFlag() && DataUtil.isNotEmpty(city.getCity())) {
                    logger.info("根据手机号获取到的信息为city：" + city.getCity());
                    String address = city.getCity().replace("市", "").trim();
//					mystr = "city_name=" + address;
//					arr = new String[]{"city_name" + address};
                    resultStr.put("status",0);
                    resultStr.remove("msg");
                    resultStr.put("data",address);
                }
//				resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_GETCITY_URL, mystr, arr));
            }
        } catch (Exception e) {
            logger.error("城市列表查询出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    /**
     * 页面弹窗关注这个session的修改
     * @param request
     * @param response
     * @return json
     * @throws Exception
     */
    @RequestMapping("/updateSession")
    public @ResponseBody Object updateSession(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String name=request.getParameter("name");
    	JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
    	try{
    		resultStr = JSONObject.fromObject("{\"status\":0,\"data\":\"\"}");
    		HttpSession session = ContextHolderUtils.getSession();
        	session.setAttribute(name, "1");
    	}catch (Exception e) {
			logger.error("公众号是否关注的弹出关闭时，需要去修改这个关键字的session出错！" + e.getMessage(),e);
		}
    	response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    
    /**
     * 图片验证码
     * @param request
     * @param response
     * @return json
     * @throws Exception
     */
    @RequestMapping("/verifyCode")
    public @ResponseBody Object verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //生成随机字串
        String verifyCode = VerificationCode.generateVerifyCode(4);
        //存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute("randCode", verifyCode);
        session.setAttribute("randCodeMust", "Y");
        //生成图片
        int w = 200, h = 80;
        VerificationCode.outputImage(w, h, response.getOutputStream(), verifyCode);
        return null;
    }

}
