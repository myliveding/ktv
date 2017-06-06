package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.javabean.pojo.wxtour.PseudoStatic;
import com.st.service.ArticleService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private WeixinAPIService weixinAPIService;
	@Resource
	private ArticleService articleService;
	@Resource
	private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
	/**
	 * 文章类别查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/typedemand")
	public @ResponseBody Object typedemand(HttpServletRequest request, HttpServletResponse response) throws IOException{

		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String code = request.getParameter("code");
		int pageSize = 20;
		int page = 1;
		String pageSizeString = request.getParameter("pageSize");
		if (!StringUtils.isEmpty(pageSizeString)) {
			pageSize = Integer.parseInt(pageSizeString);
		}
		String pageString = request.getParameter("page");
		if (!StringUtils.isEmpty(pageString)) {
			page = Integer.parseInt(pageString);
		}
		String needFeatured = request.getParameter("needFeatured");//是1的时候代表需要获取推荐列表
		String excludedId = request.getParameter("excludedId");//是1的时候代表需要获取推荐列表
		resultStr = articleService.typeDemand(request,code,needFeatured,excludedId,page,pageSize,null);
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}

	/**
	 * 文章查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/demand")
	public @ResponseBody Object demand(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String catId = request.getParameter("catId");//栏目id
		String areaName = request.getParameter("areaName");//定位的城市
		String code = request.getParameter("code");
		int pageSize =20;
		int page = 1;
		String pageSizeString = request.getParameter("pageSize");
		if (!StringUtils.isEmpty(pageSizeString)) {
			pageSize = Integer.parseInt(pageSizeString);
		}
		String pageString = request.getParameter("page");
		if (!StringUtils.isEmpty(pageString)) {
			page = Integer.parseInt(pageString);
		}
		String isHomePage = request.getParameter("isHomePage");
		String title = request.getParameter("title");//搜索
		String needSubcat = request.getParameter("needSubcat");//需要查询全部子类下的文章(1需要)
		resultStr=articleService.articleDemend(request,catId,areaName,code,page,pageSize,isHomePage,title,needSubcat);
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}


	/**
	 * 首页banner查询
	 * @param request
	 * @return
	 */
	@RequestMapping("banDemand")
	public @ResponseBody Object banDemand(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String code = request.getParameter("code");
		JSONObject resultStr =articleService.bannerDemend(request,code);
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}
	/**
	 * 文章详情,没有分享的文章详情
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/detail")
	public @ResponseBody Object detail(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String articleId=request.getParameter("articleId");
		String[] arr=new String[] {"article_id"+articleId};;
		String mystr="article_id="+articleId;
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		try {
			//文章详情
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_DETAIL_URL,mystr,arr));
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
			return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
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
		try {
			//文章详情
			resultStr = articleRedisHandleServiceImpl.read(key.toString());
			logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
			if(resultStr == null){
				resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_DETAIL_URL,mystr,arr));
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		if(0 == resultStr.getInt("status")){
			JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
			if(message != null){
				//存储进入redis
				articleRedisHandleServiceImpl.save(key.toString(),resultStr,0);
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
				if(PropertiesUtils.findPropertiesKey("ACTIVITY_CODE",Constant.SEO_FILE_NAME)
						.equals(message.getString("cat_id"))){
						param = "more/activityinfo";
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
//            + "?" + (request.getQueryString()); //参数
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
            List<PseudoStatic> urlList = PseudoStaticUrl.getInstance().getStaticUrl();
            for (PseudoStatic pseudoStatic : urlList) {
                if(null != pseudoStatic.getRealUrl()
                        && !"".equalsIgnoreCase(pseudoStatic.getRealUrl()) 
                        && pseudoStatic.getRealUrl().indexOf(param)>-1){
                    String urlTemp = "";
                    String[] temp = pseudoStatic.getSeoUrl().split("_");
                    urlTemp = temp[0].replaceAll("\\^", "") + "_" + articleId + "/";
                    url = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://"  +  request.getServerName() + request.getContextPath()
                            + urlTemp;
                    break;
                }
            }
        }
		//分享参数
		String timestamp =Constant.TIME_STAMP;
		String noncestr = Constant.NONCESTR;
		if(StringUtils.isNotEmpty(uId)){
			userId=uId;
		}
//		logger.info("url:" + url);
//		String shareurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotojsq.do"+Constant.APP_ID+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
//		String shareurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoregister.do"+Constant.APP_ID+"invitCode="+invitationCode+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
//		shareurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=personsocial/gotoindex.do"+Constant.APP_ID+"invitCode="+invitationCode+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
		logger.info("shareurl:" + strBackUrl);
		Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
	    String jsapi_ticket = weixin.getJsapiTicket();
//		logger.info("jsapi_ticket:" + jsapi_ticket);
		String signature = SignUtil.getSignature(jsapi_ticket, timestamp, noncestr, url);
//		logger.info("signature:" + signature);
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("noncestr", noncestr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		request.setAttribute("appid", Constant.APP_ID);
		request.setAttribute("shareurl", strBackUrl);
		logger.info("文章跳转"+param);
		return param;
	}
    
	/**
	 * 进入文章分类中
	 * @param request
	 * catId
	 * catName
	 */
	@RequestMapping("/articletype")
	public String articleType(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String catId = request.getParameter("catId");
		String catName = request.getParameter("catName");
		String catNameL = request.getParameter("catNameL");
		String param = request.getParameter("param");
		if(StringUtils.isEmpty(param)){
			logger.info("进入文章分类articletype.do中获取到的param为：" + param);
			param = "more/zhishebaolist";
		}
		String title = request.getParameter("title");
		String code = request.getParameter("code");
		String sort = request.getParameter("sort");
		String cityId = request.getParameter("cityid");//查社保中的
		Object seoArticlePageObject =session.getAttribute("seoArticlePage");
		if (StringUtils.isNotEmpty(sort)){
			catId=sort;
		}
		Integer page=1;
		int pageSize =20;
		Integer totalPage=1;
		if(seoArticlePageObject!=null&&!"".equals(seoArticlePageObject)){
			page=Integer.parseInt(seoArticlePageObject.toString());
			param= "maps/maps-list";//站点地图分页页面
		}
		if (code!=null){
			JSONObject typeObject=articleService.typeDemand(request,code,null,null,1,100,sort);
			request.setAttribute("articleType", typeObject);
			if (typeObject.containsKey("data")&&typeObject.getJSONObject("data").containsKey("level_cat")){
				request.setAttribute("headTitle", typeObject.getJSONObject("data").getJSONObject("level_cat").getString("meta_title"));
				request.setAttribute("keywords",  typeObject.getJSONObject("data").getJSONObject("level_cat").getString("meta_keyword"));
				request.setAttribute("description",  typeObject.getJSONObject("data").getJSONObject("level_cat").getString("meta_description"));
			}
		}
		JSONObject seoObject=JSONObject.fromObject("{\"status\":0,\"msg\":\"ok\"}");
		if (StringUtils.isEmpty(title)){
			seoObject=articleService.articleDemend(request,catId,null,code,page.intValue(),pageSize,null,title,"1");
		}
		if(seoArticlePageObject!=null&&!"".equals(seoArticlePageObject)&&0 == seoObject.getInt("status")){
			totalPage=seoObject.getInt("all_page");
			session.setAttribute("seoArticlePage","");
		}
		request.setAttribute("totalPage",totalPage);
		request.setAttribute("page",page);
		request.setAttribute("catId", catId);
		request.setAttribute("catName", catName);
		request.setAttribute("catNameL", catNameL);
		request.setAttribute("title", title);
		request.setAttribute("code", code);
		request.setAttribute("sort", sort);
		request.setAttribute("cityId", cityId);
		request.setAttribute("article", seoObject);
		return param;
	}
	
	/**
	 * 进入文章分类中
	 * catId
	 * catName
	 */
	@RequestMapping("/serachSocialCity")
	public String serachSocialCity(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String cityId = request.getParameter("cityId");
		String param = request.getParameter("param");
		if(StringUtils.isEmpty(cityId)){
			logger.info("查社保进入城市时获取到城市ID为空，这里默认为杭州的ID,cityId为：" + cityId);
			cityId = "383";
		}
        try {
            String[] arr = new String[]{"area_id" + cityId};
            String mystr = "area_id=" + cityId;
            //城市列表查询
            JSONObject resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_CITY_TEL_URL, mystr, arr));
            if(0 == resultStr.getInt("status")){
    			JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
    			if(message!=null){
    				request.setAttribute("cityId", cityId);//城市ID
    				request.setAttribute("cityName", message.getString("city_name"));//城市名字
    				request.setAttribute("fiveTel", message.getString("five_tel"));//社保电话
    				request.setAttribute("fiveUrl", message.getString("five_url"));//社保网站链接
    				request.setAttribute("fundTel", message.getString("fund_tel"));//公积金电话
    				request.setAttribute("fundUrl", message.getString("fund_url"));//公积金网站链接
    			}else {
    				logger.info("根据城市ID进入该城市的社保公积金查询页面出错,默认返回首页,data：" + message);
        			return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
    			}
    		}else {
    			logger.info("根据城市ID进入该城市的社保公积金查询页面出错,默认返回首页,status：" + resultStr.getInt("status"));
    			return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
    		}
        } catch (Exception e) {
            logger.error("根据城市ID进入该城市的社保公积金查询页面出错,默认返回首页" + e.getMessage(), e);
            return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=index/index";
        }
        if(StringUtils.isEmpty(param)){
			logger.info("查社保时进入不同城市的社保公积金页面：" + param);
			param = "more/chashebaodetail";
		}
		return param;
	}
	
	
	/**
	 * 文章大类的搜索
	 * @param request
	 * @return
	 */
	@RequestMapping("/search")
	public @ResponseBody Object search(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");

		String catId = request.getParameter("catId");//栏目id
		int pageSize =50;
		int page = 1;
		String pageSizeString = request.getParameter("pageSize");
		if (!StringUtils.isEmpty(pageSizeString)) {
			pageSize = Integer.parseInt(pageSizeString);
		}
		String pageString = request.getParameter("page");
		if (!StringUtils.isEmpty(pageString)) {
			page = Integer.parseInt(pageString);
		}
		String title = request.getParameter("title");
		if (StringUtils.isNotEmpty(title)){
			title=title.trim();
		}
		resultStr=articleService.search(catId,title,page,pageSize);
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
	/**
	 * 文章类别查询跳转
	 * @param request
	 * @return object
	 */
	@RequestMapping("/typeDemandUrl")
	public String typeDemandUrl(HttpServletRequest request) {
		String param = request.getParameter("param");
		String code = request.getParameter("code");
		String needFeatured = request.getParameter("needFeatured");
		String excludedId = request.getParameter("excludedId");
		JSONObject jsonObject=articleService.typeDemand(request,code,needFeatured,excludedId,1,20,null);
		request.setAttribute("article",jsonObject);
		if(0 == jsonObject.getInt("status")){
			if (jsonObject.getJSONObject("date")!=null){
				JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("cat");
				if(jsonArray!=null&&jsonArray.size()>0){
					request.setAttribute("catId",jsonArray.getJSONObject(0).getString("parent_id"));
				}
			}
		}
		if(StringUtils.isEmpty(param)){
			logger.info("进入文章分类articletype.do中获取到的param为：" + param);
			param = "servicehall/serviceHall";
		}
		return param;
	}
}