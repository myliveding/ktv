package com.st.controller;

import com.st.constant.Constant;
import com.st.core.javabean.Result;
import com.st.core.util.date.DateUtil;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.service.InsurerService;
import com.st.service.WeixinAPIService;
import com.st.utils.CheckUtil;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import com.st.utils.WeixinUtil;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
*
* 类描述：  参保模块
* 创建人：wuhao
* 创建时间：2015-6-10
*
 */
@Controller
@RequestMapping("/personinsurance")
public class PersoninsuranceController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private WeixinAPIService weixinAPIService;
	@Resource
	private InsurerService insurerService;

	@RequestMapping("/citydemandindex")
	public String getmenu(HttpServletRequest request) {
		return "insurance/citydemand";
	}

	/**
	 * 查询用户参保人
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/demand")
	public @ResponseBody Object demand(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");//4125
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
			return resultStr;
		}
		String userId= userIdObject.toString();
		String isInsured=request.getParameter("isInsured");
		String type=request.getParameter("type");//判断是选择参保人还是我的参保人，排序规则规则不相同,0选择1我的
        StringBuffer buffer=new StringBuffer();
        buffer.append("policyHolderId=").append(userId);
        if(StringUtils.isNotEmpty(isInsured)){
            buffer.append("&isInsured=").append(isInsured);
        }
        buffer.append("&type=").append(type);
		try {
			// 查询参保人
			resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURER_LIST,buffer.toString()));
		} catch (Exception e) {
			logger.error("当前参保人查询出错:" + e.getMessage(), e);
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
	 * 城市列表查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/citydemand")
	public @ResponseBody Object citydemand(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String[] arr = null;
		String mystr = "";
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		try {
			//城市列表查询
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_CITYDEMAND_URL, mystr, arr));
		} catch (Exception e) {
			logger.error("城市列表查询出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
	   /**
     * 城市分级列表查询
     * @param request
     * @return
     */
    @RequestMapping("/cityleveldemand")
    public @ResponseBody Object cityleveldemand(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String[] arr = null;
        String mystr = "";
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            //城市列表查询
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_CITYLEVELDEMAND_URL, mystr, arr));
        } catch (Exception e) {
            logger.error("城市列表查询出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }

	/**
	 * 保存修改当前参保人
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveInsurer")
	public @ResponseBody Object saveInsurer(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId = ""; 	//	user_id	String	是（是）	用户id
		String insurerId = request.getParameter("insurerId");//		insured_id	int	否（是）	参保人id 修改请带上
		String insurerName = request.getParameter("insurerName");//		insured_name	string	是（否）	参保人姓名
		String identityCard = request.getParameter("identityCard");//		identity_card	string	是（否）	身份证
		String mobile = request.getParameter("mobile");//		mobile	Sting	是（否）	手机号
		String householdCity = request.getParameter("householdCity");//		city_id	Int	是（否）	户口城市id
		String householdCityName = request.getParameter("householdCityName");//     city_id Int 是（否）    户口城市名
		String cityId = (String) session.getAttribute("cityId");//     city_id Int 是（否） 参保城市名
		String cityName = (String) session.getAttribute("cityName");//     city_id Int 是（否） 参保城市名
		String householdRegistration = request.getParameter("householdRegistration");//		Int	是（否）	户口性质id
		String identityCardFront = request.getParameter("identityCardFront");//	String	是	身份证正面
		String identityCardBack = request.getParameter("identityCardBack");//	String	是	身份证背面
		String insuranceStartMonth = request.getParameter("insuranceStartMonth");// int 是（否）    社保起缴月份
		String insuranceEndMonth = request.getParameter("insuranceEndMonth");// int 是（否）    社保结束月份
		String housingFundStartMonth = request.getParameter("housingFundStartMonth");// int 是（否）   公积金起缴月份
		String housingFundEndMonth = request.getParameter("housingFundEndMonth");// int 是（否）    公积金结束月份
		String type = request.getParameter("type")==null?"0":request.getParameter("type");// 来源0首页缴社保  1我的参保人系猛增
		if(StringUtils.isEmpty(householdCity)){
		    return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择户口城市\"}");
        }
		String isWeixin = request.getParameter("isWeixin")==null?"true":request.getParameter("isWeixin").trim();//判断是否是微信传过来的
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if (StringUtils.isNotEmpty(mobile)) {
		    mobile=mobile.replaceAll(" ", "");
            if(!CheckUtil.isMobileNo(mobile)){
                return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式有误！\"}");
            }
        }
		logger.info("身份证正面:"+identityCardFront+" 背面 "+identityCardBack);
		if("true".equalsIgnoreCase(isWeixin)){//微信端

    		Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
            String accessToken = weixin.getAccessToken();
//    		 try {
			identityCardFront=weixinAPIService.downloadFromWxAndUploadToAliYun(identityCardFront,accessToken,Constant.WYB_POSITIVE);
			if(identityCardFront==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证正面上传出错，请重试！！\"}");
			}
			identityCardBack=weixinAPIService.downloadFromWxAndUploadToAliYun(identityCardBack,accessToken,Constant.WYB_NEGATIVE);
			if(identityCardBack==null){
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证反面上传出错，请重试！！\"}");
			}
//    		     if(StringUtils.isNotEmpty(identityCardFront)&&identityCardFront.indexOf("http")>-1){
//    		         identityCardFront="";
//    		     }else if(StringUtils.isNotEmpty(identityCardFront)){
//    		         if(WeixinUtil.checkAccessToken(accessToken, identityCardFront)){//判断accexxTkoen的有效性，无效直接重新获取
//                         weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                         accessToken = weixin.getAccessToken();
//                    }
//    	            String frontPath = WeixinUtil.downloadMediaFromWx(accessToken, identityCardFront, Constant.WYB_POSITIVE);//将图片下载到本地服务器
//    	            identityCardFront=frontPath;
//    	            if(frontPath==null){
//                        return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！！\"}");
//                    }
//    		     }
//    		     if(StringUtils.isNotEmpty(identityCardBack)&&identityCardBack.indexOf("http")>-1){
//    		         identityCardBack="";
//                 }else if(StringUtils.isNotEmpty(identityCardBack)){
//        	         if(WeixinUtil.checkAccessToken(accessToken, identityCardBack)){//判断accexxTkoen的有效性，无效直接重新获取
//                         weixin = weixinAPIService.getJSAPITicketIm(Constant.APP_ID);
//                         accessToken = weixin.getAccessToken();
//                    }
//    	            String backPath = WeixinUtil.downloadMediaFromWx(accessToken, identityCardBack, Constant.WYB_NEGATIVE);//将图片下载到本地服务器
//    	            identityCardBack=backPath;
//                    if(backPath==null){
//                        return JSONObject.fromObject("{\"status\":1,\"msg\":\"图片上传出错，请重试！！\"}");
//                    }
//    		     }
//    	        } catch (IOException e1) {
//    	            logger.error("下载图片出错:" + e1.getMessage(), e1);
//    	        }
		 }
		Object userIdObject =  session.getAttribute("userId");//4125
		if(userIdObject==null||"".equals(userIdObject)){
			resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
			return resultStr;
		}
		userId = userIdObject.toString();
		String insuranceLastDate="";
		String housingFundLastDate="";
		String mystrs="";
		StringBuffer buffer = new StringBuffer();
		buffer.append("policyHolderId=").append(userId);
		if (StringUtils.isEmpty(insurerId)) {
			//保存参保人
		    buffer.append("&insurerName=").append(insurerName);
		    buffer.append("&identityCard=").append(identityCard.replaceAll(" ",""));
		    buffer.append("&mobile=").append(mobile);
		    buffer.append("&householdCity=").append(householdCity);
            buffer.append("&householdCityName=").append(householdCityName);
            buffer.append("&householdRegistration=").append(householdRegistration);
            if (StringUtils.isNotEmpty(cityId)) {
                buffer.append("&cityId=").append(cityId);
            }
            if (StringUtils.isNotEmpty(cityName)) {
                buffer.append("&cityName=").append(cityName);
            }
		} else {
			//修改参保人
			if (StringUtils.isNotEmpty(insurerId)) {
				buffer.append("&id=").append(insurerId);
			}
			if (StringUtils.isNotEmpty(insurerName)) {
				buffer.append("&insurerName=").append(insurerName);
			}
			if (StringUtils.isNotEmpty(identityCard)) {
				buffer.append("&identityCard=").append(identityCard.replaceAll(" ",""));
			}
			if (StringUtils.isNotEmpty(mobile)) {
				buffer.append("&mobile=").append(mobile);
			}
			if (StringUtils.isNotEmpty(householdCity)) {
				buffer.append("&householdCity=").append(householdCity);
			}
			if (StringUtils.isNotEmpty(householdCityName)) {
                buffer.append("&householdCityName=").append(householdCityName);
            }
			if (StringUtils.isNotEmpty(householdRegistration)) {
				buffer.append("&householdRegistration=").append(householdRegistration);
			}

		}
		if (StringUtils.isNotEmpty(identityCardFront)) {
			buffer.append("&identityCardFileUp=").append(identityCardFront);
		}
		if (StringUtils.isNotEmpty(identityCardBack)) {
			buffer.append("&identityCardFileDown=").append(identityCardBack);
		}
		if (StringUtils.isNotEmpty(insuranceStartMonth)) {
			buffer.append("&insuranceStartMonth=").append(insuranceStartMonth);
		}
		if (StringUtils.isNotEmpty(insuranceEndMonth)) {
			buffer.append("&insuranceEndMonth=").append(insuranceEndMonth);
		}
		if (StringUtils.isNotEmpty(housingFundStartMonth)) {
			buffer.append("&housingFundStartMonth=").append(housingFundStartMonth);
		}
		if (StringUtils.isNotEmpty(housingFundEndMonth)) {
			buffer.append("&housingFundEndMonth=").append(housingFundEndMonth);
		}
		if (StringUtils.isNotEmpty(housingFundLastDate)) {
			buffer.append("&housingFundLastDate=").append(housingFundLastDate);
		}
		if (StringUtils.isNotEmpty(insuranceLastDate)) {
			buffer.append("&insuranceLastDate=").append(insuranceLastDate);
		}
		mystrs=buffer.toString();
		try {
		    resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_INSURER_SAVE, mystrs));
			// 保存修改当前参保人
		} catch (Exception e) {
			logger.error("保存、修改当前参保人出错:" + e.getMessage(), e);
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
	 * 保存参保人信息到session
	 * @param request
	 * @return
	 */
	@RequestMapping("/savesession")

//	 @ResponseBody Object
	public @ResponseBody Object savesession(HttpServletRequest request) {
	      HttpSession session = ContextHolderUtils.getSession();
	        Object userIdObject =  session.getAttribute("userId");//4125
	        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
	        if(userIdObject==null||"".equals(userIdObject)){
	            resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
	            return resultStr;
	        }
		Result result = new Result();
		result.setSuccess(false);
		String insurerId = request.getParameter("insurerId");//		insured_id	int	否（是）	参保人id 修改请带上
		String insurerName = request.getParameter("insurerName");//		insured_name	string	是（否）	参保人姓名
		String identityCard = request.getParameter("identityCard");//		identity_card	string	是（否）	身份证
		String mobile = request.getParameter("mobile");//		mobile	Sting	是（否）	手机号
		String cityId = request.getParameter("cityId");//		city_id	Int	是（否）	缴纳地id
		String cityName = request.getParameter("cityName");//
		if (StringUtils.isNotEmpty(insurerId)) {
			session.setAttribute("insurerId", insurerId);
		}
		if (StringUtils.isNotEmpty(insurerName)) {
			session.setAttribute("insurerName", insurerName);
		}
		if (StringUtils.isNotEmpty(identityCard)) {
			session.setAttribute("identityCard", identityCard);
		}
		if (StringUtils.isNotEmpty(mobile)) {
			session.setAttribute("mobile", mobile);
		}
		if (StringUtils.isNotEmpty(cityId)) {
			session.setAttribute("cityId", cityId);
			session.setAttribute("cityName", cityName);
		}
		result.setSuccess(true);
		if(result.isSuccess()){
		    resultStr= JSONObject.fromObject("{\"status\":0,\"msg\":\"保存完成\"}");;
		}
		return resultStr;
	}

	/**
	 * 清空session中的参保人信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/clearsession")
	public @ResponseBody Object clearsession(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		session.setAttribute("insurerId", "");
		session.setAttribute("insurerName", "");
		session.setAttribute("identityCard", "");
		session.setAttribute("mobile", "");
		session.setAttribute("cityId", "");
		session.setAttribute("cityName", "");
		session.setAttribute("householdRegistration", "");
		session.setAttribute("householdRegistrationName", "");
		session.setAttribute("socialSecurityBase", "");
		session.setAttribute("housingFoundBase", "");
		session.setAttribute("housingFoundPersonPro", "");
		session.setAttribute("needSocial", "");
		session.setAttribute("needHousingFound", "");
		session.setAttribute("isContributeFund", "");
		session.setAttribute("startMonth", "");
		session.setAttribute("payMonth", "");
		session.setAttribute("productId", "");
		session.setAttribute("shopPrice", "");
		session.setAttribute("socialMoney", "");
		session.setAttribute("fundMoney", "");
		session.setAttribute("isSocialNotPay", "");
		session.setAttribute("totalMoney", "");
		session.setAttribute("socialStatus", "");
		session.setAttribute("socialSecurityStatus", "");
		session.setAttribute("identityCardFront", "");
		session.setAttribute("identityCardBack", "");
		resultStr = JSONObject.fromObject("{\"status\":0,\"msg\":\"清理完成\"}");
		return resultStr;
	}

	/**
	 * 参保人删除
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteInsurer")
	public @ResponseBody Object deleteInsurer(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String userId="";//user_id   	int	是	用户id
		String insurerId=request.getParameter("insurerId");//employee_id	int	是	参保人id
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject==null||"".equals(userIdObject)){
			resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
			return resultStr;
		}
		userId = userIdObject.toString();
		if (StringUtils.isEmpty(insurerId)) {
			resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"参保人不能为空\"}");
			return resultStr;
		}

		String mystr = "policyHolderId=" + userId+"&id=" +insurerId;
		try {
			//参保人删除
			resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_INSURER_DEL, mystr));
		} catch (Exception e) {
			logger.error("参保人删除出错:" + e.getMessage(), e);
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
	 * 套餐
	 * @param request
	 * @return
	 */
	@RequestMapping("/packages")
	public @ResponseBody Object packages(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String[] arr = null;
		String mystr = "";
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		try {
			//套餐
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_PACKAGES_URL, mystr, arr));
		} catch (Exception e) {
			logger.error("套餐出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}

	/**
	 * 我的 页面信息 3.0
	 * @param request
	 * @return
	 */
	@RequestMapping("/mydetail")// 3.0使用 home/getuserinfo
	public @ResponseBody Object mydetail(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":0}");
		if(userIdObject!=null&&!"".equals(userIdObject)){
			userId=userIdObject.toString();
		}
		resultStr.put("inviteFriend","");
		resultStr.put("businessStr", "");
		resultStr.put("renewNum", 0);
        resultStr.put("voucherNum", 0);
        resultStr.put("leftAmt", 0);
        resultStr.put("unpayNum", 0);
        resultStr.put("msgNum", 0);
        resultStr.put("agent",0);
		try{
            JSONObject jsonStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY,"triggerSence=4"));//判断是否享有活动
            if(jsonStr.getString("errcode").equals("0")){
                if(jsonStr.getString("data")!=null&&jsonStr.getString("data").equals("true")){
//                    shareFlag="0";//可以分享
                    JSONObject voucherObject =new JSONObject();
                    try {
                        voucherObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ACTIVITY_INFO, "triggerSence=4"));
                    } catch (Exception e) {
                        logger.error(" 判断是否享有活动：" + e.getMessage(), e);
                    }
                    if(voucherObject.getString("errcode").equals("0")){
                        JSONArray titles=voucherObject.getJSONArray("data");
                        int length = titles.size();
                        for(int i = 0; i < length; i++){//遍历JSONArray
                            JSONObject oj = titles.getJSONObject(i);
                            if(oj.getString("entityCode").trim().equals("title")){
//                                title=oj.getString("entityValue");
                            }
                            if(oj.getString("entityCode").trim().equals("description")){
//                                desc=oj.getString("entityValue");
                                resultStr.put("inviteFriend",oj.getString("entityValue"));
                            }
                        }
                    }
                }
            }
            int discount=10;
			jsonStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_PACKAGES_URL, "", null));
            if(jsonStr.getInt("status")==0){
                JSONArray packageArr=jsonStr.getJSONArray("data");
                discount=(int)(packageArr.getJSONObject(0).getDouble("cost_price")*10/packageArr.getJSONObject(0).getDouble("shop_price"));
            }
            if(discount!=10){
                resultStr.put("businessStr", "服务费"+discount+"折");
            }

            if (StringUtils.isNotEmpty(userId)) { // 已登录
				jsonStr=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_CAN_RENEW_NUM,"policyHolderId="+userId));//可续费数量
                if(jsonStr.getInt("status")==0){
                    resultStr.put("renewNum", jsonStr.getInt("data"));
                }
				jsonStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_IDENTITY_OF_USER, "memberId=" + userId));
				JSONObject data = JSONObject.fromObject(jsonStr.getString("data"));
				if(jsonStr.getInt("status")==0){
					resultStr.put("agent", data.get("agent"));
					session.setAttribute("agentType", data.getString("agent"));
					session.setAttribute("agentId", data.getString("agentId"));
				}
				jsonStr=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_FIND_USER,"userId="+userId+"&voucherStatus=2&sortType=desc"));//代金券数量
                if(jsonStr.getInt("errcode")==0){
                    resultStr.put("voucherNum", jsonStr.getJSONObject("data").getInt("rowCount"));
                }
				jsonStr=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_UNPAYNUM,"policyHolderId="+userId));
                if(jsonStr.getInt("status")==0){
                    resultStr.put("unpayNum", jsonStr.getInt("data"));
                }
//                JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BALANCE, "policyHolderId=" +userId));
                resultStr.put("leftAmt", 0);
                JSONObject userInfo = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_MY_URL, "user_id=" + userId, new String[] {"user_id" + userId}));
                resultStr.put("msgNum", userInfo.getJSONObject("data").getInt("unread_number"));
            }
		}catch (Exception e) {
			logger.error("我的页面查询出错:" + e.getMessage(), e);
		    resultStr= JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        }
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}

	/**
	 * 我的参保人详情
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/myinsurance")
	public @ResponseBody Object myInsurance(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject!=null&&!"".equals(userIdObject)){
			userId=userIdObject.toString();
		}
		if(StringUtils.isEmpty(userId)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String insurerId=request.getParameter("insurerId");//employee_id	int	是	参保人id
		if(StringUtils.isEmpty(insurerId)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
        }
		try {
            // 查询参保人
            resultStr=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURER_INFO,"id="+insurerId+"&policyHolderId="+userId));//参保人详情
        } catch (Exception e) {
            logger.error("当前参保人查询出错:" + e.getMessage(), e);
        }
		if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }else{
            JSONObject data=resultStr.getJSONObject("data");
			data.put("identityCardFileUpLink", "");
			data.put("identityCardFileDownLink", "");
        }
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取该城市的基础信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/cityinformation")
	public @ResponseBody Object cityinformation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");

		String cityId=request.getParameter("cityId");//城市id
		String houseHold=request.getParameter("houseHold");//户籍id

		String[] arr = null;
		String mystr = "";
		try {
			if(StringUtils.isEmpty(houseHold)){
				arr = new String[] {"city"+cityId};
				mystr = "city="+cityId;
			}else{
				arr = new String[] {"city"+cityId,"household"+houseHold};
				mystr = "city="+cityId + "&household="+houseHold;
			}
			resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CITY_INFORMATION_URL, mystr, arr));
		} catch (Exception e) {
			logger.error("获取该城市的基础信息:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}

	/**
	 * 参保人费用的计算
	 * @param request
	 * @return
	 */
	@RequestMapping("/socialitem")
	public @ResponseBody Object socialItem(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		String sourceFlag = request.getParameter("sourceFlag");
		Object userIdObject =  session.getAttribute("userId");
		String employeeId = (String) session.getAttribute("insurerId");
		String type=request.getParameter("type");
		if(null!=sourceFlag&&!sourceFlag.equals("calc")){
		    if(userIdObject==null||"".equals(userIdObject)){
	            resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
	            return resultStr;
	        }
		    userId = userIdObject.toString();
		    if(StringUtils.isEmpty(employeeId)){
	            return JSONObject.fromObject("{\"status\":1,\"msg\":\"参保人不能为空\"}");
	        }
		}

		String socialType = (String) session.getAttribute("socialType");//社保参保类型
		String socialBase = (String) session.getAttribute("socialBase");//社保基数
		String fundBase = (String) session.getAttribute("fundBase");//公积金基数
		String socialNeed=(String) session.getAttribute("socialNeed");//是否缴纳社保 1 是 0 否
		String fundNeed=(String) session.getAttribute("fundNeed");//是否缴纳公积金  1 是 0 否
		String fundType=(String) session.getAttribute("fundType");//公积金参保类型
		String cityId =(String) session.getAttribute("cityId");//城市ID
		String socialInsured=(String) session.getAttribute("socialInsured");//社保是否续保 1是 0否
	    String fundInsured=(String) session.getAttribute("fundInsured");//公积金是否续保 1是 0否
		int insuranceStartMonth = Integer.parseInt((String) session.getAttribute("insuranceStartMonth")==null?"0":(String) session.getAttribute("insuranceStartMonth"));//社保起始月份
        int housingFundStartMonth = Integer.parseInt((String) session.getAttribute("housingFundStartMonth")==null?"0":(String) session.getAttribute("housingFundStartMonth"));//公积金起始月份
        int insuranceEndMonth = Integer.parseInt((String) session.getAttribute("insuranceEndMonth")==null?"0":(String) session.getAttribute("insuranceEndMonth"));//社保起始月份
        int housingFundEndMonth = Integer.parseInt((String) session.getAttribute("housingFundEndMonth")==null?"0":(String) session.getAttribute("housingFundEndMonth"));//公积金起始月份
		int insuranceMonths=DateUtil.getMonths(DateUtil.convertToDate(insuranceStartMonth), DateUtil.convertToDate(insuranceEndMonth))+1;
		int fundMonths=DateUtil.getMonths(DateUtil.convertToDate(housingFundStartMonth), DateUtil.convertToDate(housingFundEndMonth))+1;
		if(null!=sourceFlag&&sourceFlag.equals("calc")){
			employeeId="";
			if(insuranceMonths<1){//解决往东时区月数异常问题
				insuranceMonths=1;
			}else if(insuranceMonths>1&&insuranceMonths<3){//解决往东时区月数异常问题
				insuranceMonths=3;
			}else if(insuranceMonths>3&&insuranceMonths<6){
				insuranceMonths=6;
			}else if (insuranceMonths>6){
				insuranceMonths=12;
			}
			if(fundMonths<1){
				fundMonths=1;
			}else if(fundMonths>1&&fundMonths<3){
				fundMonths=3;
			}else if(fundMonths>3&&fundMonths<6){
				fundMonths=6;
			}else if (fundMonths>6){
				fundMonths=12;
			}
		}
		insuranceEndMonth=insuranceEndMonth+172800;
		housingFundEndMonth=housingFundEndMonth+172800;
		String yearPay="";
		int iStartMonth=insuranceStartMonth;
		if(null==sourceFlag||!sourceFlag.equals("calc")){
    		JSONObject yearPayInfo=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_YEAR_PAY,"insurerId="+employeeId+"&cityId="+cityId));//年付信息
    		if(yearPayInfo.containsKey("data")){
    		    yearPay=yearPayInfo.getString("data");
    		}
		}
		if(StringUtils.isNotEmpty(type)&&type.equals("1")){
		    String serviceType=request.getParameter("serviceType");
		    if(StringUtils.isNotEmpty(serviceType)&&serviceType.trim().equals("1")){
		        socialNeed="1";
		        fundNeed="0";
		    }else{
		        socialNeed="0";
                fundNeed="1";
		    }
		    fundMonths=insuranceMonths= Integer.parseInt(request.getParameter("months")==null?"0":request.getParameter("months"));
		    String startMonth=request.getParameter("startMonth");
		    if(StringUtils.isNotEmpty(startMonth)){
		        insuranceStartMonth=housingFundStartMonth=DateUtil.convertToInt0(startMonth);
		    }
		}
		StringBuffer buffer=new StringBuffer();
		List<String> list=new ArrayList<String>();
        list.add("employee_id" +employeeId);
        buffer.append("employee_id=").append(employeeId);
        list.add("city_id" +cityId);
        buffer.append("&city_id=").append(cityId);
        if (fundNeed.equals("1")) {
            list.add("fund_need" +fundNeed);
            buffer.append("&fund_need=").append(fundNeed);
            list.add("fund_month_count" +fundMonths);
            buffer.append("&fund_month_count=").append(fundMonths);
            list.add("fund_base" +fundBase);
            buffer.append("&fund_base=").append(fundBase);
            list.add("fund_start_month" +DateUtil.convertToDate(housingFundStartMonth));
            buffer.append("&fund_start_month=").append(DateUtil.convertToDate(housingFundStartMonth));
            list.add("fund_type" +fundType);
            buffer.append("&fund_type=").append(fundType);
            if(StringUtils.isNotEmpty(employeeId)){
                list.add("fund_insured" +fundInsured);
                buffer.append("&fund_insured=").append(fundInsured);
            }
        }
        if (socialNeed.equals("1")) {
            list.add("social_need" +socialNeed);
            buffer.append("&social_need=").append(socialNeed);
            list.add("social_month_count" +insuranceMonths);
            buffer.append("&social_month_count=").append(insuranceMonths);
            list.add("social_base" +socialBase);
            buffer.append("&social_base=").append(socialBase);
            list.add("social_start_month" +DateUtil.convertToDate(insuranceStartMonth));
            buffer.append("&social_start_month=").append(DateUtil.convertToDate(insuranceStartMonth));
            list.add("social_type" +socialType);
            buffer.append("&social_type=").append(socialType);
            if(StringUtils.isNotEmpty(employeeId)){
                list.add("social_insured" +socialInsured);
                buffer.append("&social_insured=").append(socialInsured);
            }
            list.add("start_month" +DateUtil.convertToDate(iStartMonth));//用于判断年付的时间区间
            buffer.append("&start_month=").append(DateUtil.convertToDate(iStartMonth));
            list.add("end_month" +DateUtil.convertToDate(insuranceEndMonth));
            buffer.append("&end_month=").append(DateUtil.convertToDate(insuranceEndMonth));
        }
        if(StringUtils.isNotEmpty(yearPay)&&yearPay.length()>2){
            buffer.append("&year_pay=").append(yearPay);
            list.add("year_pay" +yearPay);
        }
        String[] arr = list.toArray(new String[list.size()]);
        String mystr=buffer.toString();
		try {
		    if(StringUtils.isNotEmpty(type)&&type.equals("1")){
		        resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSURANCEDETAIL, mystr, arr));//社保险种收费明细
			}else{
		        resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSUREDDETAIL, mystr, arr));//月参保保费列表
		        if(resultStr.getInt("status")==0){
		            if(resultStr.getJSONObject("data").containsKey("total")){
		                session.setAttribute("totalExpenses", resultStr.getJSONObject("data").getString("total"));
		            }
					if(resultStr.getJSONObject("data").containsKey("ensureTotal")){
						session.setAttribute("ensureTotal", resultStr.getJSONObject("data").getString("ensureTotal"));
					}
		            if(null!=sourceFlag&&sourceFlag.equals("calc")){
		               String cityName=(String) session.getAttribute("cityName")==null?"":(String) session.getAttribute("cityName");//参保城市
		               String householdCity=(String) session.getAttribute("householdCity")==null?"":(String) session.getAttribute("householdCity");//户口城市id
		               String householdCityName=(String) session.getAttribute("householdCityName")==null?"":(String) session.getAttribute("householdCityName");//户口城市
		               String householdRegistration=(String)session.getAttribute("householdRegistration")==null?"0":(String)session.getAttribute("householdRegistration");//户口性质  0城镇 1农村
		               String deadlineDay=(String) session.getAttribute("deadlineDay")==null?"":(String) session.getAttribute("deadlineDay");//deadlineDay
		               String specialDeadlineDay=(String) session.getAttribute("specialDeadlineDay")==null?"":(String) session.getAttribute("specialDeadlineDay");//specialDeadlineDay
		               JSONObject data=resultStr.getJSONObject("data");
		               data.put("cityName", cityName);
		               data.put("householdCity", householdCity);
		               data.put("householdCityName", householdCityName);
		               data.put("householdRegistration",Integer.parseInt(householdRegistration));
		               data.put("deadlineDay", deadlineDay);
		               data.put("specialDeadlineDay", specialDeadlineDay);
		               data.put("insuranceStartMonth", insuranceStartMonth);
		               data.put("insuranceEndMonth", insuranceEndMonth);
		               data.put("housingFundStartMonth", housingFundStartMonth);
		               data.put("housingFundEndMonth", housingFundEndMonth);
		               data.put("insuranceMonths", insuranceMonths);
		               resultStr.put("data", data);
		            }
		        }
		    }
		} catch (Exception e) {
			logger.error("参保人费用的计算:" + e.getMessage(), e);
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
     * 户口城市列表查询
     * @param request
     * @return
     */
    @RequestMapping("/residenceCity")
    public @ResponseBody Object residenceCity(HttpServletRequest request,HttpServletResponse response) throws IOException{
        int id=Integer.parseInt(request.getParameter("parentId")==null?"0":request.getParameter("parentId"));
        String[] arr = new String[]{"parent_id"+id};
        String mystr = "parent_id="+id;
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            //城市列表查询
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ALLCITY, mystr, arr));
        } catch (Exception e) {
            logger.error("城市列表查询出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }

        /**
      * 参保方案页内容展现
      * @param request
      * @return
         * @throws ParseException
      */
     @RequestMapping("/projectInfo")
     public @ResponseBody Object projectInfo(HttpServletRequest request,HttpServletResponse response){
         HttpSession session = ContextHolderUtils.getSession();
         Object userIdObject =  session.getAttribute("userId");//4125
         JSONObject resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         if(userIdObject==null||"".equals(userIdObject)){
             return  JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
         }
         String employId=request.getParameter("employId");
         if(employId==null||employId.trim().equals("")){
             return  JSONObject.fromObject("{\"status\":2,\"msg\":\"请选择参保人\"}");
         }

         String userId=userIdObject.toString();
         JSONObject insurerInfo=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURER_INFO,"policyHolderId="+userId+"&id="+employId));//参保人详情
         int insuranceRenew=0;//社保可续费状态  1 报增中；2报减中；3时间没到3个月内
         int insuranceRe=0;
         int fundRenew=0;//公积金可续费状态  1 报增中；2报减中；3时间没到3个月内
         int fundRe=0;
         String cityId=(String) session.getAttribute("cityId");//参保城市
         if(cityId==null||cityId.trim().equals("")){
             return JSONObject.fromObject("{\"status\":3,\"msg\":\"请选择参保城市\"}");
         }
         String householdCity=insurerInfo.getJSONObject("data").getString("householdCity");//户口城市
         String householdRegistration=insurerInfo.getJSONObject("data").getString("householdRegistration");//户口类型
         if(StringUtils.isEmpty(householdCity)||StringUtils.isEmpty(householdRegistration)||householdCity.equals("null")||householdRegistration.equals("null")){
             return JSONObject.fromObject("{\"status\":1,\"msg\":\"参保人信息缺失，请返回选择参保人页面完善参保人户口城市和户籍性质信息\"}");
         }
         String inStart=insurerInfo.getJSONObject("data").getString("insuranceStartMonth");
         String inEnd=insurerInfo.getJSONObject("data").getString("insuranceEndMonth");
         String fuStart=insurerInfo.getJSONObject("data").getString("housingFundStartMonth");
         String fuEnd=insurerInfo.getJSONObject("data").getString("housingFundEndMonth");
		 String insuranceBusinessStatus=insurerInfo.getJSONObject("data").getString("insuranceBusinessStatus");
		 String housingFundBusinessStatus=insurerInfo.getJSONObject("data").getString("housingFundBusinessStatus");
         int insuranceStartMonth=0;
         int insuranceEndMonth=0;
         int housingFundStartMonth=0;
         int housingFundEndMonth=0;
         int stypeNameChange=0;//社保参保类型中文名石佛变更 0未变；1变更
		 int ftypeNameChange=0;//公积金参保类型中文名石佛变更 0未变；1变更
         if(StringUtils.isNotEmpty(inStart)&&!inStart.equals("null")){
             insuranceStartMonth=Integer.parseInt(inStart);
         }
         if(StringUtils.isNotEmpty(inEnd)&&!inEnd.equals("null")){
             insuranceEndMonth=Integer.parseInt(inEnd);
         }
         if(StringUtils.isNotEmpty(fuStart)&&!fuStart.equals("null")){
             housingFundStartMonth=Integer.parseInt(fuStart);
         }
         if(StringUtils.isNotEmpty(fuEnd)&&!fuEnd.equals("null")){
             housingFundEndMonth=Integer.parseInt(fuEnd);
         }
         String isPersonBusiness=(String) (session.getAttribute("isBusiness")==null?"":session.getAttribute("isBusiness"));//是否是个体工商户  1 是 0 否
         if(StringUtils.isEmpty(isPersonBusiness)||isPersonBusiness.equals("0")){
             try {
                 String[] arr = new String[] {"member_id"+userId};
                 String mystr = "member_id="+userId;
                 //用户个人资料
                 JSONObject resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr,arr));
                 if(0 == resultObject.getInt("status")){
                     JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
                     session.setAttribute("isBusiness", data.getString("is_person_business"));
                     isPersonBusiness= data.getString("is_person_business");
                 }
             } catch (Exception e) {
                 logger.error("获取数据出错:" + e.getMessage(), e);
             }
         }
         JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         String[] arr = new String[]{};
         String mystr ="";
         StringBuffer buffer = new StringBuffer();
         List<String> list=new ArrayList<String>();
         list.add("city_id" +cityId);
         list.add("register_city" +householdCity);
         list.add("household_nature" +householdRegistration);
         buffer.append("city_id=").append(cityId).append("&register_city=").append(householdCity).append("&household_nature=").append(householdRegistration);
         mystr=buffer.toString();
         arr = list.toArray(new String[list.size()]);
         try {
             resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSUREDBASE, mystr, arr));

         } catch (Exception e) {
             logger.error("获取基础参保信息出错:" + e.getMessage(), e);
         }
         int isConsistent=resultStr.getJSONObject("data").getInt("is_consistent");//是否一致 0:不一致 1:一致
         int insuranceFlag=resultStr.getJSONObject("data").getInt("insurance_flag");//社保缴纳要求,0可选1强制',
         int fundFlag=resultStr.getJSONObject("data").getInt("housing_fund_flag");// '公积金缴纳要求，0可选1强制',
         String insuranceStart =resultStr.getJSONObject("data").getString("social_start_month");
         String fundStart =insuranceStart;
         String deadlineDay ="每月<span>"+resultStr.getJSONObject("data").getString("deadline_day")+"</span>号";//截点日
         if(resultStr.getJSONObject("data").containsKey("remark")){
             deadlineDay = resultStr.getJSONObject("data").getString("remark");//特殊截点日
         }

         ArrayList<HashMap<String, String>> packageList=new ArrayList<HashMap<String,String>>();
         try {//获取套餐
			 String packStr="city_id="+cityId;
			 String[] packArr= new String[] {"city_id"+cityId};
			 resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_PACKAGES_URL,packStr, packArr));
        } catch (Exception e) {
			 logger.error("获取套餐出错:" + e.getMessage(), e);
        }
         int discount=10;
         JSONArray packageArr=resultStr.getJSONArray("data");
         for(int i=0;i<packageArr.size();i++){
             HashMap<String, String> packageMap=new HashMap<String, String>();//公积金续费月份
             packageMap.put("id", (packageArr.getJSONObject(i)).getString("month"));
             packageMap.put("productId", (packageArr.getJSONObject(i)).getString("product_id"));
             packageMap.put("costPrice", (packageArr.getJSONObject(i)).getString("shop_price"));
             if(isPersonBusiness.equals("1")){
                 discount=5;
                 packageMap.put("discountPrice", packageArr.getJSONObject(i).getString("cost_price"));
                 packageMap.put("text", (packageArr.getJSONObject(i)).getString("month")+"个月(&yen;"+(packageArr.getJSONObject(i)).getString("cost_price")+"/人)");
             }else{
                 packageMap.put("discountPrice", (packageArr.getJSONObject(i)).getString("shop_price"));
                 packageMap.put("text", (packageArr.getJSONObject(i)).getString("month")+"个月(&yen;"+(packageArr.getJSONObject(i)).getString("shop_price")+"/人)");
             }
            packageList.add(packageMap);
         }
         resultObjStr.put("common_packages_discount", discount);//折扣，5折,munber类型。
         session.setAttribute("fundFlag", fundFlag);
         session.setAttribute("insuranceFlag", insuranceFlag);
         JSONObject insunersRecordList=new JSONObject();
         JSONObject insunerfRecordList=new JSONObject();
         String oldCity="";
         int stype=0;//社保参保类型
         int ftype=0;//公积金参保类型
         String stypeName="";//社保参保类型名称
         String ftypeName="";//公积金参保类型名称
         double spayBase=0.00;//社保参保基数
         double fpayBase=0.00;//公积金参保基数
         try {
			 ArrayList<HashMap<String, String>> socialList=new ArrayList<HashMap<String,String>>();
			 ArrayList<HashMap<String, String>> socialList2=new ArrayList<HashMap<String,String>>();
			 try {
				 resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CITY_SOCIAL_SET, mystr, arr));
			 } catch (Exception e) {
				 logger.error("获取基础参保信息出错:" + e.getMessage(), e);
			 }
			 JSONArray socialArr=resultStr.getJSONArray("data");
			 for(int i=0;i<socialArr.size();i++){
				 HashMap<String, String> socialMap=new HashMap<String, String>();//
				 if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("0")){//社保参保类型
					 socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
					 socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
					 socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
					 socialList.add(socialMap);
				 }
				 if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("1")){//公积金参保类型
					 socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
					 socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
					 socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
					 socialList2.add(socialMap);
				 }
			 }

             if(insuranceStartMonth>0||housingFundStartMonth>0){ //续保
                 insunersRecordList=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_RECORD_LIST,"policyHolderId="+userIdObject.toString()+"&insurerId="+employId+"&serviceType=1"));//社保记录
                 insunerfRecordList=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_RECORD_LIST,"policyHolderId="+userIdObject.toString()+"&insurerId="+employId+"&serviceType=2"));//公积金记录
                 JSONArray socialArray = insunersRecordList.getJSONArray("data");
                 JSONArray fundArray  = insunerfRecordList.getJSONArray("data");
                 if(socialArray.size()>0){
                     for (int i = 0; i < socialArray.size(); i++) {
                         JSONObject jsonObject = socialArray.getJSONObject(i);
						 if (jsonObject.getInt("serviceStatus")!=5&&jsonObject.getInt("serviceStatus")!=6) {
							 if (insuranceStartMonth > 0 && DateUtil.monthCompare(DateUtil.convertToDate(jsonObject.getInt("insuranceMonth")), DateUtil.convertToDate(insuranceStartMonth)) == 0) {
								 if (jsonObject.getInt("recordType")==0&&(jsonObject.getInt("serviceStatus") == 2 || jsonObject.getInt("serviceStatus") == 1)) {
									 insuranceRenew = 1; //社保可续费状态  1 报增中；2报减中；3时间没到3个月内
								 }
							 }
							 if (insuranceRenew == 0 && jsonObject.getInt("serviceStatus") == 4) {
								 insuranceRenew = 2;
							 }
							 oldCity = jsonObject.getString("cityName");
							 if (DateUtil.monthCompare(DateUtil.convertToDate(jsonObject.getInt("insuranceMonth")), DateUtil.convertToDate(insuranceEndMonth)) == 0) {
								 spayBase = jsonObject.getDouble("padinBase");//已参保业务最后一个月的实收基数
							 }
							 String newStypeName = "";
							 if (jsonObject.getInt("serviceStatus")==2||jsonObject.getInt("serviceStatus")==1||jsonObject.getInt("serviceStatus")==3){
								 stype=jsonObject.getInt("insureType");
								 for (int j = 0; j < socialList.size(); j++) {
									 if (socialList.get(j).get("id").equals(String.valueOf(stype))) {
										 newStypeName = socialList.get(j).get("title");
									 }
								 }
							 }
							 if (!newStypeName.equals("") && !newStypeName.trim().equals(jsonObject.getString("insureTypeName").trim())) {
								 stypeName = newStypeName;
								 stypeNameChange = 1;
							 } else {
								 stypeName = jsonObject.getString("insureTypeName");
							 }
						 }
                     }
                 }
                 if(fundArray.size()>0){
                     for (int i = 0; i < fundArray.size(); i++) {
                         JSONObject jsonObject = fundArray.getJSONObject(i);
						 if (jsonObject.getInt("serviceStatus")!=5&&jsonObject.getInt("serviceStatus")!=6) {
							 if(housingFundStartMonth>0&&DateUtil.monthCompare(DateUtil.convertToDate(jsonObject.getInt("insuranceMonth")),DateUtil.convertToDate(housingFundStartMonth))==0){
								 if(jsonObject.getInt("recordType")==0&&(jsonObject.getInt("serviceStatus")==2||jsonObject.getInt("serviceStatus")==1)){
									 fundRenew=1;
								 }
							 }
							 if(fundRenew==0&&jsonObject.getInt("serviceStatus")==4){
								 fundRenew=2;
							 }
							 if(DateUtil.monthCompare(DateUtil.convertToDate(jsonObject.getInt("insuranceMonth")),DateUtil.convertToDate(housingFundEndMonth))==0){
								 fpayBase=jsonObject.getDouble("padinBase");//已参保业务最后一个月的实收基数
							 }
							 String newFtypeName="";
							 if (jsonObject.getInt("serviceStatus")==2||jsonObject.getInt("serviceStatus")==1||jsonObject.getInt("serviceStatus")==3){
								 ftype=jsonObject.getInt("insureType");
								 for(int j=0;j<socialList2.size();j++){
									 if (socialList2.get(j).get("id").equals(String.valueOf(ftype))){
										 newFtypeName=socialList2.get(j).get("title");
									 }
								 }
							 }

							 if(!newFtypeName.equals("")&&!newFtypeName.trim().equals(jsonObject.getString("insureTypeName").trim())){
								 ftypeName=newFtypeName;
								 ftypeNameChange=1;
							 }else{
								 ftypeName=jsonObject.getString("insureTypeName");
							 }
						 }
                     }
                 }
				 if (insuranceBusinessStatus.equals("5")){
					 insuranceRenew=2;
				 }
				 if (housingFundBusinessStatus.equals("5")){
					 fundRenew=2;
				 }
				 session.setAttribute("insuranceRenew",insuranceRenew);
				 session.setAttribute("fundRenew",fundRenew);
             }
			 resultObjStr.put("ftypeNameChange", ftypeNameChange);
			 resultObjStr.put("stypeNameChange", stypeNameChange);
             if(insuranceFlag==1||fundFlag==1){//两个都强制缴纳，计算月份，否则，获取前端数据交互计算
                     HashMap<String, Integer> resultMap=new HashMap<String, Integer>();
                     resultMap=DateUtil.calcMonth(insuranceStart, fundStart, insuranceEndMonth, housingFundEndMonth, insuranceFlag, fundFlag, 1);
                     resultObjStr.put("common_packages_minMonth", resultMap.get("months"));
             }
             ArrayList<HashMap<String, Integer>> baseList=new ArrayList<HashMap<String,Integer>>();
			 insuranceStart=DateUtil.dateFormat(DateUtil.dateAdd(DateUtil.DATE_INTERVAL_DAY,DateUtil.dateFormat(insuranceStart,"yyyy-MM-dd"),2),"yyyy-MM-dd");//解决欧美时区问题
             for(int i=0;i<3;i++){
                 HashMap<String, Integer> baseMap=new HashMap<String, Integer>();//基础可缴月份
                 baseMap.put("value", DateUtil.convertToInt(DateUtil.addMonth(insuranceStart, i)));
                 baseList.add(baseMap);
             }
             String inLastDate=insurerInfo.getJSONObject("data").getString("insuranceLastDate");
             String fuLastDate=insurerInfo.getJSONObject("data").getString("housingFundLastDate");
             int insuranceLastDate=0;
             int housingFundLastDate=0;
             if(StringUtils.isNotEmpty(inLastDate)&&!inLastDate.equals("null")){
                 insuranceLastDate=Integer.parseInt(inLastDate);
             }
             if(StringUtils.isNotEmpty(fuLastDate)&&!fuLastDate.equals("null")){
                 housingFundLastDate=Integer.parseInt(fuLastDate);
             }
             if(insuranceRenew==0&&insuranceLastDate>0&&DateUtil.compareToday(DateUtil.addMonth(DateUtil.convertToDate(insuranceLastDate), -3))>0){//3个月内允许续费
                 insuranceRenew=3;
             }
             if(fundRenew==0&&housingFundLastDate>0&&DateUtil.compareToday(DateUtil.addMonth(DateUtil.convertToDate(housingFundLastDate), -3))>0){
                 fundRenew=3;
             }
             resultObjStr.put("status", 0);
             resultObjStr.put("name", insurerInfo.getJSONObject("data").getString("insurerName"));
             resultObjStr.put("oldCity",oldCity );//已参保的城市
             resultObjStr.put("householdCity", householdCity);//户口城市
             resultObjStr.put("householdRegistration", householdRegistration);//户口类型  0 城镇 ； 1农村
             resultObjStr.put("newCity", session.getAttribute("cityName"));//当前参保城市
             resultObjStr.put("deadlineText", deadlineDay);//截点日提示文案
             if(isConsistent==0){
                 resultObjStr.put("tipText", "");//类型：字符串 or '',不允许出现undefined null 等字段
                 resultObjStr.put("baseNumber", false);//社保和公积金基数是否一致，true or false
                 if(insuranceFlag==1&&fundFlag==1){
                     resultObjStr.put("tipText", "当前城市，社保与公积金强制缴纳");
                 }else if(insuranceFlag==1){
                     resultObjStr.put("tipText", "当前城市，社保强制缴纳");
                 }else if(fundFlag==1){
                     resultObjStr.put("tipText", "当前城市，公积金强制缴纳");
                 }if (fundFlag==2){
					 resultObjStr.put("tipText", "当前城市，暂不支持缴纳公积金");
				 }
             }else{
                 resultObjStr.put("tipText", "当前城市，参保基数须一致");//类型：字符串 or '',不允许出现undefined null 等字段
                 if(insuranceFlag==1&&fundFlag==1){
                     resultObjStr.put("tipText", "当前城市，社保与公积金强制缴纳且参保基数须一致");
                 }else if(insuranceFlag==1){
                     resultObjStr.put("tipText", "当前城市，社保强制缴纳且参保基数须一致");
                 }else if(fundFlag==1){
                     resultObjStr.put("tipText", "当前城市，公积金强制缴纳且参保基数须一致");
                 }
                 resultObjStr.put("baseNumber", true);//社保和公积金基数是否一致，true or false
             }
             resultObjStr.put("common_packages", packageList);//公共部分的选择套餐，始终需要，如有折扣价格按照折后返回过来。
             if(insuranceFlag==1||fundFlag==1){
                 resultObjStr.put("common_start_month", baseList);//公共部分的起缴月份，只有社保公积金都强制缴纳的时候才返回过来。否则整个字段不返回过来
             }
             //社保部分
             if(insuranceFlag==1){
                 insuranceRe=1;
             }
             if(insuranceRenew!=0){
                 insuranceRe=2;
             }
			 if(insuranceFlag==2){
				 insuranceRe=3;
			 }
             resultObjStr.put("socialSecurity_must", insuranceRe);
             resultObjStr.put("socialSecurity_flag", insuranceFlag);
			 if ( insuranceEndMonth>0&&DateUtil.differDays(DateUtil.getNowIntTime(),insuranceLastDate)>=0){//续费截止日大于今天，该业务在保
//             if(insuranceStartMonth>0){
				 resultObjStr.put("socialSecurity_join", true);
                 resultObjStr.put("socialSecurity_on_start",  insuranceStartMonth);
                 resultObjStr.put("socialSecurity_on_end", insuranceEndMonth);
                 resultObjStr.put("socialSecurity_on_tip", insurerInfo.getJSONObject("data").getString("insuranceLastTips"));
             }else{
				 resultObjStr.put("socialSecurity_join", false);
                 resultObjStr.put("socialSecurity_on_start",  0);
                 resultObjStr.put("socialSecurity_on_end",  0);
                 resultObjStr.put("socialSecurity_on_tip", "");
             }
             resultObjStr.put("socialSecurity_on_baseNumber", spayBase);
             resultObjStr.put("socialSecurity_cannotpay_tip", "");
             if(insuranceRenew==1){
                 resultObjStr.put("socialSecurity_cannotpay_tip", "当前参保人该业务正在办理中，暂不能续费");//不能续费的提示：包括三种情况
             }if(insuranceRenew==2){
                 resultObjStr.put("socialSecurity_cannotpay_tip", "当前参保人该业务正在办理停保手续，暂不能续费");//不能续费的提示：包括三种情况
             }if(insuranceRenew==3){
                 resultObjStr.put("socialSecurity_cannotpay_tip",DateUtil.addMonth(DateUtil.convertToDate(insuranceLastDate), -3)
                         +"至"+DateUtil.convertToDate(insuranceLastDate)+"可续费，请勿错过");//不能续费的提示：包括三种情况
             }
             resultObjStr.put("socialSecurity_type_selected", stype);
             resultObjStr.put("socialSecurity_type_selected_name", stypeName);

             resultObjStr.put("socialSecurity_type", socialList);
             resultObjStr.put("socialSecurity_start_month", baseList);
//             if(housingFundStartMonth>0){
			 if (housingFundEndMonth>0&& DateUtil.differDays(DateUtil.getNowIntTime(),housingFundLastDate)>=0){//续费截止日大于今天，该业务在保
                 resultObjStr.put("fund_join", true);//公积金是否已缴，true or false
                 resultObjStr.put("fund_on_start",  housingFundStartMonth);//已缴纳公积金时候，返回开始月份
                 resultObjStr.put("fund_on_end",  housingFundEndMonth);//已缴纳公积金时候，返回结束月份
                 resultObjStr.put("fund_on_tip", insurerInfo.getJSONObject("data").getString("housingFundLastTips"));//已缴纳公积金，快结束时候的提示文案
             }else{
                 resultObjStr.put("fund_join", false);//公积金是否已缴，true or false
                 resultObjStr.put("fund_on_start",  0);//已缴纳公积金时候，返回开始月份
                 resultObjStr.put("fund_on_end",  0);//已缴纳公积金时候，返回结束月份
                 resultObjStr.put("fund_on_tip", "");//已缴纳公积金，快结束时候的提示文案
             }
             resultObjStr.put("fund_on_baseNumber", fpayBase);//已参保时候，返回参保时的基数

             resultObjStr.put("fund_cannotpay_tip", "");
             if(fundRenew==1){
                 resultObjStr.put("fund_cannotpay_tip", "当前参保人该业务正在办理中，暂不能续费");//不能续费的提示：包括三种情况
             }if(fundRenew==2){
                 resultObjStr.put("fund_cannotpay_tip", "当前参保人该业务正在办理停保手续，暂不能续费");//不能续费的提示：包括三种情况
             }if(fundRenew==3){
                 resultObjStr.put("fund_cannotpay_tip", DateUtil.addMonth(DateUtil.convertToDate(housingFundLastDate), -3)
                         +"至"+DateUtil.convertToDate(housingFundLastDate)+"可续费，请勿错过");//不能续费的提示：包括三种情况
             }
             if(fundFlag==1){
                 fundRe=1;
             }
             if(fundRenew!=0){
                 fundRe=2;
             }
			 if(fundFlag==2){
				 fundRe=3;
			 }
             resultObjStr.put("fund_must", fundRe);//公积金强制是否可续费，0是，2不能续费
             resultObjStr.put("fund_flag", fundFlag);//公积金强制缴纳情况， 0否，1是
    //         resultObjStr.put("fund_cannotpay_tip", "");//不能续费的提示：包括三种情况
             //1.该业务正在办理中时，提示“当前参保人该业务正在办理中，暂不能续费”
             //2.该业务正在停保手续时，提示“当前参保人该业务正在办理停保手续，暂不能续费”
             //3.该业务的续费截止日－3个月的日子>当天的日期，提示“XXXX-XX-XX至XXXX-XX-XX可续费，请勿错过”，显示允许续费的日期区间。
             resultObjStr.put("fund_type_selected", ftype);//公积金的缴纳类型。已缴纳的情况下把已选类型放到最前面:::::如果非续保情况下只返回一组，则当条不展示。
             resultObjStr.put("fund_type_selected_name", ftypeName);
             resultObjStr.put("fund_type", socialList2);//公积金的缴纳类型。已缴纳的情况下把已选类型放到最前面:::::如果非续保情况下只返回一组，则当条不展示。
             resultObjStr.put("fund_start_month", baseList);//公积金起缴月份
         }catch (Exception e) {
             resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         }
         response.setContentType("text/html; charset=utf-8");
         PrintWriter out;
        try {
            out = response.getWriter();
            resultObjStr.put("msg", "");
            out.println(resultObjStr);
        } catch (IOException e) {
			logger.error("获取基础参保信息出错:" + e.getMessage(), e);
        }
         return null;
     }


     /**
    * 参保方案中获取参保起止月
    * @param request
    * @return
     * @throws ParseException
    */
    @RequestMapping("/projectConditions")
    public @ResponseBody Object projectConditions(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");//4125
        JSONObject resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        if(userIdObject==null||"".equals(userIdObject)){
            resultObjStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            return resultObjStr;
        }
        int insuranceStart=Integer.parseInt(request.getParameter("insuranceStart")==null?"0":request.getParameter("insuranceStart"));
        int fundStart=Integer.parseInt(request.getParameter("fundStart")==null?"0":request.getParameter("fundStart"));
        int insuranceingEnd=Integer.parseInt(request.getParameter("insuranceingEnd")==null?"0":request.getParameter("insuranceingEnd"));
        int fundingEnd=Integer.parseInt(request.getParameter("fundingEnd")==null?"0":request.getParameter("fundingEnd"));
        int insuranceSelect=Integer.parseInt(request.getParameter("insuranceSelect")==null?"0":request.getParameter("insuranceSelect"));
        int fundSelect=Integer.parseInt(request.getParameter("fundSelect")==null?"0":request.getParameter("fundSelect"));
        int months=Integer.parseInt(request.getParameter("months")==null?"3":request.getParameter("months"));
        int insuranceFlag=Integer.parseInt(request.getParameter("insuranceFlag")==null?"0":request.getParameter("insuranceFlag"));
        int fundFlag=Integer.parseInt(request.getParameter("fundFlag")==null?"0":request.getParameter("fundFlag"));
		Object insuranceRenewObj=session.getAttribute("insuranceRenew");
		Object fundRenewObj=session.getAttribute("fundRenew");
		String insuranceRenew="0";
		String fundRenew="0";
		if(insuranceRenewObj!=null&&!"".equals(insuranceRenewObj)){
			insuranceRenew=insuranceRenewObj.toString();
		}
		if(fundRenewObj!=null&&!"".equals(fundRenewObj)){
			fundRenew=fundRenewObj.toString();
		}
		if (months==1){
			resultObjStr=insurerService.getMonthBySinbleCondition(insuranceStart, fundStart, insuranceingEnd, fundingEnd, insuranceFlag, fundFlag, insuranceSelect, fundSelect,insuranceRenew,fundRenew,months);
		}else {
			resultObjStr=insurerService.getMonthByCondition(insuranceStart, fundStart, insuranceingEnd, fundingEnd, insuranceFlag, fundFlag, insuranceSelect, fundSelect,insuranceRenew,fundRenew,months);
		}
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultObjStr);
        return null;

    }
    /**
   * 参保方案中获取参保起止月
   * @param request
   * @return
     * @throws IOException
    * @throws ParseException
   */
   @RequestMapping("/saveProject")
   public @ResponseBody Object saveProject(HttpServletRequest request,HttpServletResponse response) throws IOException{
       HttpSession session = ContextHolderUtils.getSession();
       JSONObject resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
       Result result = new Result();
       result.setSuccess(false);
       String insurerId = request.getParameter("insurerId");//     insured_id  int 否（是）    参保人id 修改请带上
       String insurerName = request.getParameter("insurerName");//     insured_id  int 否（是）    参保人id 修改请带上
       String socialType = request.getParameter("socialType");//参保类型
       String socialBase = request.getParameter("socialBase");//社保基数
       String fundBase = request.getParameter("fundBase");//公积金基数
       String socialNeed=request.getParameter("socialNeed");//是否缴纳社保 1 是 0 否
       String fundNeed=request.getParameter("fundNeed");//是否缴纳公积金  1 是 0 否
       String fundType=request.getParameter("fundType");//公积金参保类型
       String socialTypeName = request.getParameter("socialTypeName");//参保类型
       String fundTypeName=request.getParameter("fundTypeName");//公积金参保类型
       String insuranceStartMonth=request.getParameter("insuranceStartMonth");
       String housingFundStartMonth=request.getParameter("housingFundStartMonth");
       String insuranceEndMonth=request.getParameter("insuranceEndMonth");
       String housingFundEndMonth=request.getParameter("housingFundEndMonth");
       String productId=request.getParameter("productId");//产品ID
       String productMonth=request.getParameter("productMonth");//产品购买月份
       String discount=request.getParameter("discount");//服务费折扣
       String costPrice=request.getParameter("costPrice");//服务费原件
       String discountPrice=request.getParameter("discountPrice");//服务费优惠后金额
       String subTotal=request.getParameter("subTotal");//社保费用小计
       String housingFundCost=request.getParameter("housingFundCost");//公积金总金额
       String insuranceCost=request.getParameter("insuranceCost");//社保总金额
       String totalAmt=request.getParameter("totalAmt");//订单总金额
       String bonusId=request.getParameter("bonusId");//代金券ID
       String bonus=request.getParameter("bonus");//代金券金额

       String cityId=request.getParameter("cityId");//参保城市id
       String cityName=request.getParameter("cityName");//参保城市
       String householdCity=request.getParameter("householdCity");//户口城市id
       String householdCityName=request.getParameter("householdCityName");//户口城市
       String householdRegistration=request.getParameter("householdRegistration");//户口性质  0城镇 1农村
       String socialInsured=request.getParameter("shebaoInsured");//社保是否续保 1是 0否
       String fundInsured=request.getParameter("fundInsured");//公积金是否续保 1是 0否
       if (StringUtils.isNotEmpty(socialInsured)) {
           session.setAttribute("socialInsured", socialInsured);
       }
       if (StringUtils.isNotEmpty(fundInsured)) {
           session.setAttribute("fundInsured", fundInsured);
       }

       if (StringUtils.isNotEmpty(insurerId)) {
           session.setAttribute("insurerId", insurerId);
       }
       if (StringUtils.isNotEmpty(insurerName)) {
           session.setAttribute("insurerName", insurerName);
       }
       if (StringUtils.isNotEmpty(fundType)) {
           session.setAttribute("fundType", fundType);
           session.setAttribute("fundTypeName", fundTypeName);
       }
       if (StringUtils.isNotEmpty(socialType)) {
           session.setAttribute("socialType", socialType);
           session.setAttribute("socialTypeName", socialTypeName);
       }
       if (StringUtils.isNotEmpty(socialNeed)) {
           session.setAttribute("socialNeed", socialNeed);
           if(socialNeed.equals("0")){
               session.setAttribute("socialBase", "0");
               session.setAttribute("socialType", "");
               session.setAttribute("socialTypeName", "");
           }else if (StringUtils.isNotEmpty(socialBase)) {
               session.setAttribute("socialBase", socialBase);
           }
       }
       if (StringUtils.isNotEmpty(fundNeed)) {
           session.setAttribute("fundNeed", fundNeed);
           if(fundNeed.equals("0")){
               session.setAttribute("fundBase", "0");
               session.setAttribute("fundType", "");
               session.setAttribute("fundTypeName", "");
           }else if (StringUtils.isNotEmpty(fundBase)) {
               session.setAttribute("fundBase", fundBase);
           }
       }
       if (StringUtils.isNotEmpty(insuranceStartMonth)) {
           session.setAttribute("insuranceStartMonth", insuranceStartMonth);
       }
       if (StringUtils.isNotEmpty(housingFundStartMonth)) {
           session.setAttribute("housingFundStartMonth", housingFundStartMonth);
       }
       if (StringUtils.isNotEmpty(insuranceEndMonth)) {
           session.setAttribute("insuranceEndMonth", insuranceEndMonth);
       }
       if (StringUtils.isNotEmpty(housingFundEndMonth)) {
           session.setAttribute("housingFundEndMonth", housingFundEndMonth);
       }
       if (StringUtils.isNotEmpty(productId)) {
           session.setAttribute("productId", productId);
       }
       if (StringUtils.isNotEmpty(discount)) {
           session.setAttribute("discount", discount);
       }
       if (StringUtils.isNotEmpty(costPrice)) {
           session.setAttribute("costPrice", costPrice);
       }
       if (StringUtils.isNotEmpty(discountPrice)) {
           session.setAttribute("discountPrice", discountPrice);
       }
       if (StringUtils.isNotEmpty(productMonth)) {
           session.setAttribute("productMonth", productMonth);
       }
       if (StringUtils.isNotEmpty(subTotal)) {
           session.setAttribute("subTotal", subTotal);
       }
       if (StringUtils.isNotEmpty(housingFundCost)) {
           session.setAttribute("housingFundCost", housingFundCost);
       }
       if (StringUtils.isNotEmpty(insuranceCost)) {
           session.setAttribute("insuranceCost", insuranceCost);
       }
       if (StringUtils.isNotEmpty(totalAmt)) {
           session.setAttribute("totalAmt", totalAmt);
       }
       if (StringUtils.isNotEmpty(bonusId)) {
           session.setAttribute("bonusId", bonusId);
       }
       if (StringUtils.isNotEmpty(bonus)) {
           session.setAttribute("bonus", bonus);
       }
       if (StringUtils.isNotEmpty(cityId)) {
           session.setAttribute("cityId", cityId);
       }
       if (StringUtils.isNotEmpty(cityName)) {
           session.setAttribute("cityName", cityName);
       }
       if (StringUtils.isNotEmpty(householdCity)) {
           session.setAttribute("householdCity", householdCity);
       }
       if (StringUtils.isNotEmpty(householdCityName)) {
           session.setAttribute("householdCityName", householdCityName);
       }
       if (StringUtils.isNotEmpty(householdRegistration)) {
           session.setAttribute("householdRegistration", householdRegistration);
       }
       result.setSuccess(true);
       if(result.isSuccess()){
           resultObjStr = JSONObject.fromObject("{\"status\":0,\"msg\":\"OK\"}");
       }
       if(resultObjStr.containsKey("errcode")){
           JSONObject data=new JSONObject();
           data.put("status", 1);
           data.put("errcode", resultObjStr.getString("errcode"));
           data.put("msg", resultObjStr.getString("errmsg"));
           resultObjStr=data;
       }
       response.setContentType("text/html; charset=utf-8");
       PrintWriter out=response.getWriter();
       out.println(resultObjStr);
       return null;

   }
       /**
      * 获取基数
      * @param request
      * @return
      * @throws IOException
      */
      @RequestMapping("/getBase")
      public @ResponseBody Object getBase(HttpServletRequest request,HttpServletResponse response) throws IOException{
          HttpSession session = ContextHolderUtils.getSession();
          JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
          String cityId=request.getParameter("cityId");
          if(StringUtils.isEmpty(cityId)){
              cityId=(String) session.getAttribute("cityId");//参保城市
          }else{
              session.setAttribute("cityId", cityId);
          }
          String socialType=request.getParameter("socialType")==null?"-1":request.getParameter("socialType");
          String fundType=request.getParameter("fundType")==null?"-1":request.getParameter("fundType");
          String isConsistent=request.getParameter("isConsistent")==null?"0":request.getParameter("isConsistent");
          String insuranceSelect=request.getParameter("insuranceSelect")==null?"0":request.getParameter("insuranceSelect");
          String fundSelect=request.getParameter("fundSelect")==null?"0":request.getParameter("fundSelect");
          String insuranceStartMonth=request.getParameter("insuranceStart");
          String housingFundStartMonth=request.getParameter("fundStart");
          String insuranceingEnd=request.getParameter("insuranceingEnd");
          String fundingEnd=request.getParameter("fundingEnd");
          String householdCity=request.getParameter("householdCity");
          String householdRegistration=request.getParameter("householdRegistration");
          String insuranceBase=request.getParameter("insuranceBase")==null?"0.0":request.getParameter("insuranceBase");
          String fundBase=request.getParameter("fundBase")==null?"0.0":request.getParameter("fundBase");
          double inBase=Double.parseDouble(insuranceBase);
          double fuBase=Double.parseDouble(fundBase);
          if(insuranceStartMonth.length()<9){
              insuranceStartMonth=insuranceingEnd;
          }
          if(housingFundStartMonth.length()<9){
              housingFundStartMonth=fundingEnd;
          }
          int insuranceStart=0;
          int housingFundStart=0;
          String[] arr = new String[]{};
          String mystr ="";
          StringBuffer buffer = new StringBuffer();
          List<String> list=new ArrayList<String>();
          list.add("city_id" +cityId);
          list.add("register_city" +householdCity);
          list.add("household_nature" +householdRegistration);
          buffer.append("city_id=").append(cityId).append("&register_city=").append(householdCity).append("&household_nature=").append(householdRegistration);
          if(insuranceSelect.equals("1")&&insuranceStartMonth.length()>9){
              insuranceStart=Integer.parseInt(insuranceStartMonth);
              buffer.append("&social_month=").append(DateUtil.convertToDate(insuranceStart));
              list.add("social_month" +DateUtil.convertToDate(insuranceStart));
              if(StringUtils.isNotEmpty(socialType)&&Integer.parseInt(socialType)>=0){
                  buffer.append("&social_type=").append(socialType);
                  list.add("social_type" +socialType);
              }
          }
          if(fundSelect.equals("1")&&housingFundStartMonth.length()>9){
              housingFundStart=Integer.parseInt(housingFundStartMonth);
              buffer.append("&fund_month=").append(DateUtil.convertToDate(housingFundStart));
              list.add("fund_month" +DateUtil.convertToDate(housingFundStart));
              if(StringUtils.isNotEmpty(fundType)&&Integer.parseInt(fundType)>=0){
                  buffer.append("&fund_type=").append(fundType);
                  list.add("fund_type" +fundType);
              }
          }
          mystr=buffer.toString();
          arr = list.toArray(new String[list.size()]);
          try {
              JSONObject resJsonObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSUREDBASE, mystr, arr));
              resultStr=new JSONObject();
              resultStr.put("status", 0);
              int shebaoChange=0;//0不可修改  1可修改
              int fundChange=0;
              int shebaoDisplay=1;//0不显示基数范围  1显示
              int fundDisplay=1;
              if(resJsonObject.getJSONObject("data").containsKey("insurance_base_low")&&Integer.parseInt(socialType)>=0){
				  double minBase=resJsonObject.getJSONObject("data").getDouble("insurance_base_low");
				  double maxBase=resJsonObject.getJSONObject("data").getDouble("insurance_base_high");
                  HashMap<String, Double> range=new HashMap<String, Double>();
				  range.put("max_range", maxBase);
				  if(fuBase<1&&inBase<1){
					  range.put("min_range", minBase);
					  range.put("currentRange",minBase);
				  }else{
					  if(isConsistent.equals("1")){
						  inBase= DateUtil.double2Compare(fuBase,inBase);
					  }
					  range.put("min_range", minBase);
					  if (inBase>minBase){
						  range.put("currentRange",inBase);
					  }else {
						  range.put("currentRange",minBase);
					  }
					  if(inBase>maxBase){
						  range.put("currentRange", maxBase);
					  }
				  }
                  resultStr.put("shebaoBaseNumber", range);
              }
              if(resJsonObject.getJSONObject("data").containsKey("housing_fund_base_low")&&Integer.parseInt(fundType)>=0){
                  HashMap<String, Double> range=new HashMap<String, Double>();
				  double minBase=resJsonObject.getJSONObject("data").getDouble("housing_fund_base_low");
				  double maxBase=resJsonObject.getJSONObject("data").getDouble("housing_fund_base_high");
				  range.put("max_range", maxBase);
				  if(fuBase<1&&inBase<1){
					  range.put("min_range", minBase);
					  range.put("currentRange",minBase);
				  }else{
					  if(isConsistent.equals("1")){
						  fuBase= DateUtil.double2Compare(fuBase,inBase);
					  }
					  range.put("min_range", minBase);
					  if (fuBase>minBase){
						  range.put("currentRange",fuBase);
					  }else {
						  range.put("currentRange",minBase);
					  }
					  if(fuBase>maxBase){
						  range.put("currentRange", maxBase);
					  }
				  }
                  resultStr.put("fundBaseNumber", range);
              }
			  if(fuBase<1&&inBase<1){
				  shebaoChange=fundChange=1;
			  }else if(!isConsistent.equals("1")){
                  if(fuBase<1){
                      fundChange=1;
                  }
                  if(inBase<1){
                      shebaoChange=1;
                  }
              }

              resultStr.put("shebaoChange", shebaoChange);//0不可修改  1可修改
              resultStr.put("fundChange", fundChange);
              resultStr.put("shebaoDisplay", shebaoDisplay);//0不显示基数范围  1显示
              resultStr.put("fundDisplay", fundDisplay);//0不显示基数范围  1显示

          } catch (Exception e) {
              logger.error("获取基础参保信息出错:" + e.getMessage(), e);
          }
          response.setContentType("text/html; charset=utf-8");
          PrintWriter out=response.getWriter();
          out.println(resultStr);
          return null;
      }


      /**
     * 计算器获取月份
     * @param request
     * @return
     * @throws IOException
     */
     @RequestMapping("/getCalcMonth")
     public @ResponseBody Object getCalcMonth(HttpServletRequest request,HttpServletResponse response) throws IOException{
         HttpSession session = ContextHolderUtils.getSession();
         JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         String cityId=request.getParameter("cityId");//参保城市
         if(StringUtils.isEmpty(cityId)){
             cityId=(String) session.getAttribute("cityId");
         }else{
             session.setAttribute("cityId", cityId);
         }
         if(StringUtils.isEmpty(cityId)){
             return JSONObject.fromObject("{\"status\":1,\"msg\":\"请返回选择参保城市\"}");
         }
         String residenceCityId=request.getParameter("householdCity");//户口城市
         if(StringUtils.isEmpty(residenceCityId)){
             return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择户口城市\"}");
         }
         String property=request.getParameter("householdRegistration");//户口类型  0城镇； 1农村
         if(StringUtils.isEmpty(property)){
             return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择户口类型\"}");
         }
         StringBuffer buffer = new StringBuffer();
         List<String> list=new ArrayList<String>();
         list.add("city_id" +cityId);
         list.add("register_city" +residenceCityId);
         list.add("household_nature" +property);
         buffer.append("city_id=").append(cityId).append("&register_city=").append(residenceCityId).append("&household_nature=").append(property);
		 String mystr=buffer.toString();
		 String[] arr = list.toArray(new String[list.size()]);
         try {
             JSONObject resJsonObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSUREDBASE, mystr, arr));
             resultStr=new JSONObject();
             if(resJsonObject.getInt("status")==0){
                 resultStr.put("status", 0);
                 resultStr.put("isConsistent", resJsonObject.getJSONObject("data").getInt("is_consistent"));//是否一致 0:不一致 1:一致
                 resultStr.put("insuranceFlag", resJsonObject.getJSONObject("data").getInt("insurance_flag"));//社保缴纳要求,0可选1强制',
                 resultStr.put("fundFlag",resJsonObject.getJSONObject("data").getInt("housing_fund_flag"));// '公积金缴纳要求，0可选1强制2不缴',
				 String insuranceStart=resJsonObject.getJSONObject("data").getString("social_start_month");
                 ArrayList<HashMap<String, Integer>> baseList=new ArrayList<HashMap<String,Integer>>();
				 insuranceStart=DateUtil.dateFormat(DateUtil.dateAdd(DateUtil.DATE_INTERVAL_DAY,DateUtil.dateFormat(insuranceStart,"yyyy-MM-dd"),2),"yyyy-MM-dd");//解决欧美时区问题
				 for(int i=0;i<3;i++){
                     HashMap<String, Integer> baseMap=new HashMap<String, Integer>();//基础可缴月份
                     baseMap.put("value", DateUtil.convertToInt(DateUtil.addMonth(insuranceStart, i)));
                     baseList.add(baseMap);
                 }
                 resultStr.put("insuranceStart",baseList);
                 String deadlineDay =resJsonObject.getJSONObject("data").getString("deadline_day");//截点日
                 String specialDeadlineDay="";
                 if(resJsonObject.getJSONObject("data").containsKey("special_deadline_day")&&resJsonObject.getJSONObject("data").containsKey("remark")){
                     String remark=resJsonObject.getJSONObject("data").getString("remark");
                     specialDeadlineDay=remark.substring(remark.indexOf(",")+1,remark.length());//截点日
                 }
                 resultStr.put("deadlineDay", deadlineDay);//截点日
                 session.setAttribute("deadlineDay", deadlineDay);
                 session.setAttribute("specialDeadlineDay", specialDeadlineDay);
                 resultStr.put("specialDeadlineDay", specialDeadlineDay);//截点日
             }
             resJsonObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CITY_SOCIAL_SET, mystr, arr));
             if(resJsonObject.getInt("status")==0){
                 ArrayList<HashMap<String, String>> socialList=new ArrayList<HashMap<String,String>>();
                 ArrayList<HashMap<String, String>> socialList2=new ArrayList<HashMap<String,String>>();
                 JSONArray socialArr=resJsonObject.getJSONArray("data");
                 for(int i=0;i<socialArr.size();i++){
                     HashMap<String, String> socialMap=new HashMap<String, String>();//
                     if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("0")){//社保参保类型
                         socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
                         socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
                         socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
                         socialList.add(socialMap);
                     }
                     if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("1")){//公积金参保类型
                         socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
                         socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
                         socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
                         socialList2.add(socialMap);
                     }
                 }
                 if(socialList.size()>0){
                     resultStr.put("status", 0);
                     resultStr.put("socialSecurity_type", socialList);
                 }
                 if(socialList2.size()>0){
                     resultStr.put("status", 0);
                     resultStr.put("fund_type", socialList2);//公积金的缴纳类型。已缴纳的情况下把已选类型放到最前面:::::如果非续保情况下只返回一组，则当条不展示
                 }
             }
			 String packStr="city_id="+cityId;
			 String[] packArr= new String[] {"city_id"+cityId};
             resJsonObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_PACKAGES_URL,packStr, packArr));
             if(resJsonObject.getInt("status")==0){
                 ArrayList<HashMap<String, String>> packageList=new ArrayList<HashMap<String,String>>();
                 JSONArray packageArr=resJsonObject.getJSONArray("data");
                 for(int i=0;i<packageArr.size();i++){
                     HashMap<String, String> packageMap=new HashMap<String, String>();//公积金续费月份
                     packageMap.put("id", (packageArr.getJSONObject(i)).getString("month"));
                     packageList.add(packageMap);
                 }
                 resultStr.put("common_packages", packageList);//公共部分的选择套餐，始终需要，如有折扣价格按照折后返回过来。
             }
         } catch (Exception e) {
             logger.error("获取基础参保信息出错:" + e.getMessage(), e);
         }
         response.setContentType("text/html; charset=utf-8");
         PrintWriter out=response.getWriter();
         out.println(resultStr);
         return null;
     }

      /**
     * 获取参保类型
     * @param request
     * @return
      * @throws ParseException
     */
     @RequestMapping("/socialSet")
     public @ResponseBody Object socialSet(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
         HttpSession session = ContextHolderUtils.getSession();
         Object userIdObject =  session.getAttribute("userId");//4125
         JSONObject resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         if(userIdObject==null||"".equals(userIdObject)){
             resultObjStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
             return resultObjStr;
         }
         String cityId=request.getParameter("cityId");
         if(StringUtils.isEmpty(cityId)){
             resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择参保城市\"}");
             return resultObjStr;
         }
         String residenceCityId=request.getParameter("householdCity");//户口城市
         if(StringUtils.isEmpty(residenceCityId)){
             resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择户口城市\"}");
             return resultObjStr;
         }
         String property=request.getParameter("householdRegistration");//户口类型  0城镇； 1农村
         if(StringUtils.isEmpty(property)){
             resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择户口类型\"}");
             return resultObjStr;
         }
         JSONObject resultStr = new JSONObject();
         String[] arr = new String[]{"city_id"+cityId,"register_city"+residenceCityId,"household_nature"+property};
         String mystr = "city_id="+cityId+"&register_city="+residenceCityId+"&household_nature="+property;
         ArrayList<HashMap<String, String>> socialList=new ArrayList<HashMap<String,String>>();
         ArrayList<HashMap<String, String>> socialList2=new ArrayList<HashMap<String,String>>();
         try {
             resultObjStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CITY_SOCIAL_SET, mystr, arr));
         } catch (Exception e) {
             logger.error("获取基础参保信息出错:" + e.getMessage(), e);
         }
         JSONArray socialArr=resultObjStr.getJSONArray("data");
         for(int i=0;i<socialArr.size();i++){
             HashMap<String, String> socialMap=new HashMap<String, String>();//
             if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("0")){//社保参保类型
                 socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
                 socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
                 socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
                 socialList.add(socialMap);
             }
             if(((JSONObject)socialArr.get(i)).getString("belong_busi").equals("1")){//公积金参保类型
                 socialMap.put("id", ((JSONObject)socialArr.get(i)).getString("id"));
                 socialMap.put("title", ((JSONObject)socialArr.get(i)).getString("name"));
                 socialMap.put("description", ((JSONObject)socialArr.get(i)).getString("description"));
                 socialList2.add(socialMap);
             }
         }
         if(socialList.size()>0){
             resultObjStr.put("status", 0);
             resultObjStr.put("socialSecurity_type", socialList);
             resultObjStr.put("fund_type", socialList2);//公积金的缴纳类型。已缴纳的情况下把已选类型放到最前面:::::如果非续保情况下只返回一组，则当条不展示
         }
         response.setContentType("text/html; charset=utf-8");
         PrintWriter out=response.getWriter();
         out.println(resultObjStr);
         return null;
     }

     /**
    * 获取参保记录
    * @param request
    * @return
     * @throws ParseException
    */
    @RequestMapping("/getRecordInfo")
    public @ResponseBody Object getRecordInfo(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
        HttpSession session = ContextHolderUtils.getSession();
        Object userIdObject =  session.getAttribute("userId");//4125
        JSONObject resultObjStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        JSONObject resultObjStr2 = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String insurerId=request.getParameter("insurerId");//参保人ID
        String pageNow=request.getParameter("pageNow");//当前查询第几页
        String pageSize = request.getParameter("pageSize");//每页数据条数,此参数有默认值为10
        if(StringUtils.isEmpty(insurerId)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
        }
        String serviceType=request.getParameter("serviceType");//公积金或者社保的区分字段 1社保2公积金
        if(StringUtils.isEmpty(serviceType)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保类别\"}");
        }
        StringBuffer buffer=new StringBuffer();
        buffer.append("insurerId=").append(insurerId).append("&policyHolderId=").append(userIdObject.toString()).append("&serviceType=").append(serviceType);
        if(StringUtils.isNotEmpty(pageNow)){
            buffer.append("&pageNow=").append(pageNow);
        }
        if(StringUtils.isNotEmpty(pageSize)){
            buffer.append("&pageSize=").append(pageSize);
        }
        try {
            resultObjStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_RECORD_INFO, buffer.toString()));
            resultObjStr2=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_CURRENT_PROGRESS, buffer.toString()));
        } catch (Exception e) {
            logger.error("获取基础参保信息出错:" + e.getMessage(), e);
        }
        if(resultObjStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultObjStr.getString("errcode"));
            data.put("msg", resultObjStr.getString("errmsg"));
            resultObjStr=data;
        }
        if(resultObjStr2.containsKey("data")){
            resultObjStr.put("progress", resultObjStr2.getJSONObject("data"));
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(resultObjStr);
        return null;
    }
    /**
   * 是否可续保
   * @param request
   * @return
    * @throws ParseException
   */
   @RequestMapping("/isRenew")
   public @ResponseBody Object isRenew(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
       HttpSession session = ContextHolderUtils.getSession();
       Object userIdObject =  session.getAttribute("userId");//4125
       JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
       if(userIdObject==null||"".equals(userIdObject)){
           return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
       }
       String insurerId=request.getParameter("insurerId");//参保人ID
       if(StringUtils.isEmpty(insurerId)){
           return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
       }
       String serviceType=request.getParameter("serviceType");//公积金或者社保的区分字段 1社保2公积金
       if(StringUtils.isEmpty(serviceType)){
           return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保类别\"}");
       }
       String cityId=request.getParameter("cityId");//参保城市
       if(StringUtils.isEmpty(cityId)){
           return JSONObject.fromObject("{\"status\":3,\"msg\":\"请选择参保城市\"}");
       }
       try {
           resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURER_RENEW, "cityId="+cityId+"&insurerId="+insurerId+"&serviceType="+serviceType));
       } catch (Exception e) {
           logger.error("获取基础参保信息出错:" + e.getMessage(), e);
       }
       if(resultStr.containsKey("errcode")){
           JSONObject data=new JSONObject();
           data.put("status", resultStr.getString("errcode"));
           data.put("msg", resultStr.getString("errmsg"));
           resultStr=data;
       }
       response.setContentType("text/html; charset=utf-8");
       PrintWriter out=response.getWriter();
       out.println(resultStr);
       return null;
   }

   /**
  * 查询参保人是否已存在其他帐号内在保
  * @param request
  * @return
  * @throws IOException
  */
  @RequestMapping("/checkInsurer")
  public @ResponseBody Object checkInsurer(HttpServletRequest request,HttpServletResponse response) throws IOException{
      JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
      String insurerId=request.getParameter("insurerId");//参保人ID
      if(StringUtils.isEmpty(insurerId)){
          return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
      }
      try {
          resultStr=JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURER_CHECK,"insurerId="+insurerId));//参保人详情
          if(resultStr.getJSONObject("data").getInt("isInsurered")==1){
              resultStr=JSONObject.fromObject("{\"status\":1,\"msg\":\"您选择的参保人<span style='color:#F60;font-size: 14px;'>已在其他投保人（"+resultStr.getJSONObject("data").getString("policyHolder")+"）账户下参保</span>，不可重复参保，具体可详询：400-111-8900\"}");
          }
      } catch (Exception e) {
          logger.error("查询参保人是否已存在其他帐号内在保出错:" + e.getMessage(), e);
      }
      if(resultStr.containsKey("errcode")){
          JSONObject data=new JSONObject();
          data.put("status", resultStr.getString("errcode"));
          data.put("msg", resultStr.getString("errmsg"));
          resultStr=data;
      }
      response.setContentType("text/html; charset=utf-8");
      PrintWriter out=response.getWriter();
      out.println(resultStr);
      return null;
  }

	/**
	 * 首页信息 3.4
	 * @param request
	 * @return
	 */
	@RequestMapping("/indexInfo")//
	public @ResponseBody Object indexInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":0}");
		if(userIdObject!=null&&!"".equals(userIdObject)){
			userId=userIdObject.toString();
		}
		resultStr.put("renewNum", 0);
		resultStr.put("msgNum", 0);
		try{
			if (StringUtils.isNotEmpty(userId)) { // 已登录
				JSONObject jsonStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_CAN_RENEW_NUM, "policyHolderId=" + userId));//可续费数量
				if (jsonStr.getInt("status") == 0) {
					resultStr.put("renewNum", jsonStr.getInt("data"));
				}
				jsonStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_MY_URL, "user_id=" + userId, new String[] {"user_id" + userId}));
				if (jsonStr.getInt("status") == 0) {
					resultStr.put("msgNum", jsonStr.getJSONObject("data").getInt("unread_number"));
				}
			}
		}catch (Exception e) {
			logger.error("首页信息:" + e.getMessage(), e);
			resultStr= JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 缴社保第一步增加截点信息 3.4
	 * @param request
	 * @return
	 */
	@RequestMapping("/cityInfo")
	public @ResponseBody Object cityInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		String cityId=request.getParameter("cityId");
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String[] arr = new String[] { "city_id" + cityId, "register_city383" , "household_nature1"};
		String mystr="city_id="+cityId+"&register_city=383&household_nature=1";
		try {
			JSONObject infoStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INSUREDBASE, mystr, arr));
			resultStr.put("deadlineDay",infoStr.getJSONObject("data").getString("deadline_day"));//截点日
			if(infoStr.getJSONObject("data").containsKey("remark")){
				resultStr.put("deadlineDay",infoStr.getJSONObject("data").getString("remark"));//特殊截点日
			}
			resultStr.put("status",0);
			resultStr.remove("msg");
			resultStr.put("insuranceStart",infoStr.getJSONObject("data").getString("social_start_month"));
		} catch (Exception e) {
			logger.error("获取缴社保第一步截点信息出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}
	/**
	 * 支付成功后查询参保材料 3.4
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCityData")
	public @ResponseBody Object getCityData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		String userId="";
		String orderNo=request.getParameter("orderNo");
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}else{
			userId=userIdObject.toString();
		}
		try {
			JSONObject dataStr =new JSONObject();
			JSONObject javaStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_DOCINFO, "orderNo="+orderNo ));
			if (0 == javaStr.getInt("status") && javaStr.has("data")){
				JSONObject data = JSONObject.fromObject(javaStr.getString("data"));
				if (!data.getString("policyHolderId").equals(userId)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"非当前用户订单！\"}");
				}
				dataStr.put("cityName",data.getString("cityName"));
				dataStr.put("insurerName",data.getString("insurerName"));
				dataStr.put("insuranceStartMonth",data.getInt("insuranceStartMonth"));
				dataStr.put("insuranceEndMonth",data.getInt("insuranceEndMonth"));
				dataStr.put("housingFundStartMonth",data.getInt("housingFundStartMonth"));
				dataStr.put("housingFundEndMonth",data.getInt("housingFundEndMonth"));
				String cityId=data.getString("cityId");
				String[] arr = new String[] { "city_id" + cityId};
				String mystr="city_id="+cityId;
				JSONObject infoStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSON_CITY_DATA, mystr, arr));
				if(0 == infoStr.getInt("status") && infoStr.has("data")){
					data = JSONObject.fromObject(infoStr.getString("data"));
					dataStr.put("fiveTel",data.getString("five_tel"));
					dataStr.put("fiveUrl",data.getString("five_url"));
					dataStr.put("fundTel",data.getString("fund_tel"));
					dataStr.put("fundUrl",data.getString("fund_url"));
					dataStr.put("emailContent",data.getString("email_content"));
				}
				resultStr.put("data",dataStr);
				resultStr.remove("msg");
				resultStr.put("status",0);
			}
		} catch (Exception e) {
			logger.error("支付成功后查询参保材料出错:" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}
}
