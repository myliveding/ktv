package com.st.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.st.constant.Constant;
import com.st.controller.alipay.config.AlipayConfig;
import com.st.core.util.CookieUtil;
import com.st.core.util.DataUtil;
import com.st.core.util.date.DateUtil;
import com.st.core.util.iputil.IPUtil;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.service.WeixinAPIService;
import com.st.service.impl.ArticleRedisHandleServiceImpl;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import com.st.utils.PropertiesUtils;
import com.st.utils.SignUtil;
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
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/personorder")
public class PersonorderController {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private WeixinAPIService weixinAPIService;
    @Resource
    private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
	
	/**
	 * 订单生成接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderCreate")
	public @ResponseBody Object orderCreate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		JSONObject voucherStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (null==userIdObject||"".equals(userIdObject)) {
		    return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String userId=userIdObject.toString();//Integer
		String userName=(String) session.getAttribute("userName")==null?"":(String) session.getAttribute("userName");
		String orderAmt = request.getParameter("orderAmt")==null?"0.0":request.getParameter("orderAmt");   //"订单金额" Double
		String orderType="1";//"订单类型"Integer 
		String bonusId=request.getParameter("bonusId")==null?"":request.getParameter("bonusId"); //"代金券id" 
		String bonus=request.getParameter("bonus")==null?"0.0":request.getParameter("bonus");//"代金券金额" Double
		String remark=request.getParameter("remark")==null?"":request.getParameter("remark"); //"用户备注"
		String insurerId=(String) session.getAttribute("insurerId")==null?"":(String) session.getAttribute("insurerId");//"参保人id"Integer
		String productId=(String) session.getAttribute("productId")==null?"0":(String) session.getAttribute("productId");//"产品id" Integer
		String cityId=(String) session.getAttribute("cityId")==null?"0":(String) session.getAttribute("cityId"); //"参保城市id" Integer
		String cityName=(String) session.getAttribute("cityName");//"城市名称" 
		String socialType=(String) session.getAttribute("socialType")==null?"":(String) session.getAttribute("socialType");;//"参保类型"
		String fundType=(String) session.getAttribute("fundType")==null?"":(String) session.getAttribute("fundType");;//"参保类型"
		String socialTypeName=(String) session.getAttribute("socialTypeName")==null?"":(String) session.getAttribute("socialTypeName");;//"参保类型"
        String fundTypeName=(String) session.getAttribute("fundTypeName")==null?"":(String) session.getAttribute("fundTypeName");;//"参保类型"
		String serviceCharge=(String) session.getAttribute("discountPrice")==null?"0.0":(String) session.getAttribute("discountPrice");//"服务费" Double
		String productPrice=(String) session.getAttribute("costPrice")==null?"0.0":(String) session.getAttribute("costPrice"); //"产品原价"  Double
		String isInsurance=(String) session.getAttribute("socialNeed")==null?"0":(String) session.getAttribute("socialNeed");//"是否缴纳社保" 0否  1是
		String isHousingFund=(String) session.getAttribute("fundNeed")==null?"0":(String) session.getAttribute("fundNeed");//"是否缴纳公积金"
		String insuranceBase=(String) session.getAttribute("socialBase")==null?"0.0":(String) session.getAttribute("socialBase");//"社保缴纳基数" Double
		String insuranceStartMonth=(String) session.getAttribute("insuranceStartMonth")==null?"0":(String) session.getAttribute("insuranceStartMonth");//"社保期缴月份"Integer
		String insuranceEndMonth=(String) session.getAttribute("insuranceEndMonth")==null?"0":(String) session.getAttribute("insuranceEndMonth");//"社保截止月份" Integer
		String housingFundBase=(String) session.getAttribute("fundBase")==null?"0.0":(String) session.getAttribute("fundBase");//"公积金缴纳基数" Double
		String housingFundStartMonth=(String) session.getAttribute("housingFundStartMonth")==null?"0":(String) session.getAttribute("housingFundStartMonth");//"公积金期缴月份" Integer
		String housingFundEndMonth=(String) session.getAttribute("housingFundEndMonth")==null?"0":(String) session.getAttribute("housingFundEndMonth");//"公积金截止月份" Integer
		String hosusingFundProportion=request.getParameter("hosusingFundProportion")==null?"0.0":request.getParameter("hosusingFundProportion");//"公积金比例" Double
		String insuranceCost=(String) session.getAttribute("insuranceCost")==null?"0.0":(String) session.getAttribute("insuranceCost");//"社保费用" Double
		String housingFundCost=(String) session.getAttribute("housingFundCost")==null?"0.0":(String) session.getAttribute("housingFundCost");//"公积金费用" Double
        String ensureCost=(String) session.getAttribute("ensureTotal")==null?"0.0":(String) session.getAttribute("ensureTotal");//"残保金费用" Double

		String productMonth=(String) session.getAttribute("productMonth")==null?"0":(String) session.getAttribute("productMonth");//"产品购买月份" 
//		String discountAmt=discountPrice;//request.getParameter("discountAmt")==null?"0.0":request.getParameter("discountAmt");  //"优惠金额" Double
		if(StringUtils.isEmpty(insurerId)){
		    return JSONObject.fromObject("{\"status\":1,\"msg\":\"参保人ID不能为空\"}");
		}
        int insuranceMonths=0;
        int housingFundMonths=0;
        if (!insuranceStartMonth.equals("0")&&!insuranceEndMonth.equals("0")){
            insuranceMonths=DateUtil.differMonth(Integer.parseInt(insuranceStartMonth)*1000L,Integer.parseInt(insuranceEndMonth)*1000L)+1;
        }
        if (!housingFundStartMonth.equals("0")&&!housingFundEndMonth.equals("0")){
            housingFundMonths=DateUtil.differMonth(Integer.parseInt(housingFundStartMonth)*1000L,Integer.parseInt(housingFundEndMonth)*1000L)+1;
        }
        insuranceMonths=insuranceMonths>housingFundMonths?insuranceMonths:housingFundMonths;
        if (Integer.parseInt(productMonth)!=1&&Integer.parseInt(productMonth)!=insuranceMonths){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"购买月与套餐<span class=\\\"orange\\\">不一致</span>，请返回参保方案页重新选择您需要购买的套餐\"}");
        }
        String totalExpenses=(String) session.getAttribute("totalExpenses");//session中的社保总费用，不包括服务费和代金券抵扣额
        String totala=new java.text.DecimalFormat("#0.00").format(Double.parseDouble(totalExpenses)+Double.parseDouble(serviceCharge));
		if(StringUtils.isNotEmpty(bonusId)&&!bonusId.equals("-1")){
		    voucherStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_INFO, "voucherId="+bonusId));//校验代金券是否有效
		    if(voucherStr.getInt("errcode")==0){
		        JSONObject data=voucherStr.getJSONObject("data");
		        if(data==null){
		            return JSONObject.fromObject("{\"status\":1,\"msg\":\"代金券有误\"}");
		        }else{
		            if(!data.getString("userId").equals(userId)){
		                return JSONObject.fromObject("{\"status\":1,\"msg\":\"代金券有误\"}");
                    }else if(DataUtil.doubleCompare(Double.parseDouble(data.getString("bills")),Double.parseDouble(bonus))!=0){
		                return JSONObject.fromObject("{\"status\":1,\"msg\":\"代金券有误\"}");
		            }else if(!data.getString("voucherStatus").equals("0")){
		                return JSONObject.fromObject("{\"status\":1,\"msg\":\"代金券有误\"}");
		            }else if(data.getJSONArray("voucherUseRuleExtList").size()>0){
		                boolean flag=false;
		                boolean flag2=false;
		                int f1=0;
		                int f2=0;
		                JSONArray voucherUseRuleExtList=data.getJSONArray("voucherUseRuleExtList");
                        for(int j=0;j<voucherUseRuleExtList.size();j++){
                            if(voucherUseRuleExtList.getJSONObject(j).getString("entityCode").equals("appointProduct")){
                                f1++;
                                if(voucherUseRuleExtList.getJSONObject(j).getString("entityValue").equals(productId)){
                                    flag=true;
                                }
                            }
                            if(voucherUseRuleExtList.getJSONObject(j).getString("entityCode").equals("giveNum")){
                                f2++;
                                if(Double.parseDouble(voucherUseRuleExtList.getJSONObject(j).getString("entityValue"))<Double.parseDouble(totala)){
                                    flag2=true;
                                }
                            }
                        }
                        if(f1==0){
                            flag=true;
                        }
                        if(f2==0){
                            flag2=true;
                        }
                        if(!flag||!flag2){
                            return JSONObject.fromObject("{\"status\":1,\"msg\":\"代金券有误\"}");
                        }
		            }
		        }
		    }else{
		        return JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		    }
		}
		if(Double.parseDouble(bonus)>Double.parseDouble(serviceCharge)){
		    bonus=serviceCharge;
		}
		String total=new java.text.DecimalFormat("#0.00").format(Double.parseDouble(totala)-Double.parseDouble(bonus));
		logger.info("-sessions中金额数据 保费"+totalExpenses+" 服务费  "+serviceCharge+" 代金券 "+bonus+" 合计 "+total+" 前台给值  "+orderAmt);
		if(!total.equals(orderAmt)){
		    return JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		}
		String discountAmt=new java.text.DecimalFormat("#0.00").format(Double.parseDouble(productPrice)-Double.parseDouble(serviceCharge));
		if(remark.equals("null")){
		    remark="";
		}
		String mystr="userId="+userId+"&userName="+userName+"&policyHolderId="+userId+"&orderAmt="+orderAmt+"&orderType="+orderType+"&bonusId="+bonusId+"&bonus="+bonus+"&remark="+remark
		        +"&insurerId="+insurerId+"&productId="+productId+"&cityId="+cityId+"&cityName="+cityName+"&socialType="+socialType+"&fundType="+fundType+"&serviceCharge="+serviceCharge
		        +"&productPrice="+productPrice+"&isInsurance="+isInsurance+"&isHousingFund="+isHousingFund+"&insuranceBase="+insuranceBase+
		        "&insuranceStartMonth="+insuranceStartMonth+"&insuranceEndMonth="+insuranceEndMonth+"&housingFundBase="+housingFundBase+"&housingFundStartMonth="+housingFundStartMonth
		        +"&housingFundEndMonth="+housingFundEndMonth+"&hosusingFundProportion="+hosusingFundProportion+"&insuranceCost="+insuranceCost+"&housingFundCost="+housingFundCost
		        +"&productMonth="+productMonth+"&discountAmt="+discountAmt+"&socialTypeName="+socialTypeName+"&fundTypeName="+fundTypeName+"&ensureAmt="+ensureCost;
		try {
			resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CREATE,mystr));
		} catch (Exception e) {
            logger.error("获取年付信息出错:" + e.getMessage(), e);
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
     * 确认到款接口
     * @param request
     * @return
     */
    @RequestMapping("/confirmation")
    public @ResponseBody Object confirmation(HttpServletRequest request,HttpServletResponse response) throws IOException{
        HttpSession session=ContextHolderUtils.getSession();
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        Object userIdObject=session.getAttribute("userId");
        if (userIdObject==null||"".equals(userIdObject)) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String orderNo=request.getParameter("orderNo");
        if (StringUtils.isEmpty(orderNo)) {
            resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"订单号不能为空\"}");
            return resultStr;
        }
        String thirdPayAmt=request.getParameter("thirdPayAmt");
        if(StringUtils.isEmpty(thirdPayAmt)){
            resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"到款金额不能为空\"}");
            return resultStr;
        }
        String serialNo = request.getParameter("serialNo");
        if(StringUtils.isEmpty(serialNo)){
            resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"交易流水号不能为空\"}");
            return resultStr;
        }
        String payType = request.getParameter("payType");
        if(StringUtils.isEmpty(payType)){
            resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"支付方式不能为空\"}");
            return resultStr;
        }
        String mystr="orderNo="+orderNo+"&payType="+payType+"&serialNo="+serialNo+"&thirdPayAmt="+thirdPayAmt;
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
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
     * 判断参保人是否有未支付订单
     * @param request
     * @return
     */
    @RequestMapping("/isExistUnpayOrder")
    public @ResponseBody Object isExistUnpayOrder(HttpServletRequest request,HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String userId="";//user_id      int 是   用户id
        String insurerId=request.getParameter("insurerId");//employee_id    int 是   参保人id
        String type=request.getParameter("type")==null?"1":request.getParameter("type");
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        userId = userIdObject.toString();
        if (StringUtils.isEmpty(insurerId)) {
            resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"参保人不能为空\"}");
            return resultStr;
        }
        String mystr = "policyHolderId=" + userId+"&insurerId=" +insurerId+"&type="+type;
        try {
            //参保人删除
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_UNPAY, mystr));
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
	 * 订单的数据校验接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderCheck")
	public @ResponseBody Object orderCheck(HttpServletRequest request,String employ,String activityIds,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
		    return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String userId=userIdObject.toString();
		String orderNo = request.getParameter("orderNo");
		if(StringUtils.isEmpty(orderNo)){
		    return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择订单\"}");
		}
		try {
            //参保人删除
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CHECK, "policyHolderId=" + userId+"&orderNo=" +orderNo));
        } catch (Exception e) {
            logger.error("订单的数据校验接口出错:" + e.getMessage(), e);
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
	 * 订单明细查询接口
	 * payStatus 0为未付款  3为全部订单列表查询是传值
	 * page 页码
	 * pageSize 每页条数
	 * @return Object
	 */
	@RequestMapping("/orderInfo")
	public @ResponseBody Object orderInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
		    return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		String userId=userIdObject.toString();
		String orderNo = request.getParameter("orderNo");
		if(StringUtils.isEmpty(orderNo)){
		    return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择订单\"}");
		}
		String mystr="policyHolderId="+userId+"&orderNo="+orderNo;
		try {
			resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
		} catch (Exception e) {
			logger.error("订单明细查询接口出错:" + e.getMessage(), e);
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
     * 订单列表查询接口
     * payStatus 0为未付款  3为全部订单列表查询是传值
     * page 页码
     * pageSize 每页条数
     * @return Object
     */
    @RequestMapping("/orderList")
    public @ResponseBody Object orderList(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session=ContextHolderUtils.getSession();
        Object userIdObject=session.getAttribute("userId");
        if (userIdObject==null||"".equals(userIdObject)) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String userId=userIdObject.toString();
        String orderStatus=request.getParameter("orderStatus")==null?"":request.getParameter("orderStatus");//订单状态
        String pageNow=request.getParameter("pageNow");//当前查询第几页
        String pageSize = request.getParameter("pageSize");//每页数据条数,此参数有默认值为10
        StringBuffer buffer=new StringBuffer();
        buffer.append("policyHolderId=").append(userId);
        buffer.append("&orderStatus=").append(orderStatus);
        if(StringUtils.isNotEmpty(pageNow)){
            buffer.append("&start=").append(pageNow);
        }else{
            buffer.append("&start=").append(1);
        }
        if(StringUtils.isNotEmpty(pageSize)){
            buffer.append("&length=").append(pageSize);
        }else{
            buffer.append("&length=").append(10);
        }
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_LIST,buffer.toString()));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
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
	 * 未付款账单取消
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderCancel")
	public @ResponseBody Object orderCancel(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session=ContextHolderUtils.getSession();
        Object userIdObject=session.getAttribute("userId");
        String userName=(String) session.getAttribute("userName")==null?"":(String) session.getAttribute("userName");
        if (userIdObject==null||"".equals(userIdObject)) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String userId=userIdObject.toString();
        String orderNo = request.getParameter("orderNo");
        if(StringUtils.isEmpty(orderNo)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择订单\"}");
        }
        String cancelReason=request.getParameter("cancelReason");
        
        String mystr="policyHolderId="+userId+"&orderNo="+orderNo+"&userName="+userName;
        if(StringUtils.isNotEmpty(cancelReason)){
            mystr+="&cancelReason="+cancelReason;
        }
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CANCEL, mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
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
     * 保存用户token
     * @param request
     * @author shenwf
     * @return
     */
	@RequestMapping("/saveToken")
    public String saveToken(HttpServletRequest request){
        String token=request.getParameter("token");
        String tradeNum=request.getParameter("tradeNum");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            logger.error("保存用户jdtoken延迟1秒响应出现异常",e);
        }
        logger.info("保存用户jdtoken信息：token="+token+";orderNo="+tradeNum);
        String mystr="jdToken="+token+"&orderNo="+tradeNum;
        JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_JDTOKEN_SAVE,mystr));
        return "redirect:"+Constant.URL+"/personorder/gotoPaysuc.do?orderNo="+tradeNum;
    }
    
/**
  * 本地测试方法
  * @param request
  * @author shenwf
  * @return
  */
	@RequestMapping("/testmclientpay")
 public String testMclientPay(HttpServletRequest request){
     String orderNo=request.getParameter("orderNo");
     return "redirect:"+Constant.URL+"/personorder/gotoPaysuc.do?orderNo="+orderNo;
 }
    /**
     * 跳转到退款详情页
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gototdetail")
    public String gototdetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String refundIds=request.getParameter("refundId");
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
                return "redirect:"+Constant.URL+"/jsp/order/orderrefunddetail.jsp?refundId="+refundIds;
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
        
    }
    /**
     * 跳转到订单详情页
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotoddetail")
    public String gotoddetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orderNo=request.getParameter("orderNo");
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
                if(StringUtils.isNotEmpty(memberTruename)&&!"null".equals(memberTruename)){
                    session.setAttribute("memberTruename", memberTruename);
                }
                CookieUtil.getUserInfo();//更新用户的消息标志session
                request.setAttribute("type", 6);
                return "redirect:"+Constant.URL+"/jsp/my/orderdetail.jsp?orderNo="+orderNo;
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
        
    }
    /**
     * 跳转到续保缴费窗口
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/gotorenewal")
    public String gotorenewal(HttpServletRequest request, HttpServletResponse response) throws IOException{
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
                return "my/myinsured";
            }else {
                return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:"+Constant.URL+"/weixin/getweixin.do?name=account/login";
        
    }
    
    /**
  * 保存用户token
  * @param request
  * @author shenwf
  * @return
  */
 @RequestMapping("/alipayReturn")
 public String alipayReturn(HttpServletRequest request){
     logger.info("----------------支付宝同步回调开始----------------");
     Map<String,String> params = new HashMap<String,String>();
     Map requestParams = request.getParameterMap();
     for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
         String name = (String) iter.next();
         String[] values = (String[]) requestParams.get(name);
         String valueStr = "";
         for (int i = 0; i < values.length; i++) {
             valueStr = (i == values.length - 1) ? valueStr + values[i]
                     : valueStr + values[i] + ",";
         }
         //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//         try {
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
             //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
             //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//        } catch (UnsupportedEncodingException e) {
//             logger.error("转换格式出错:" + e.getMessage(), e);
//        }
         logger.info("name="+name+"   valueStr="+valueStr);
         params.put(name, valueStr);
     }
     
     //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
     //商户订单号

        String outTradeNo = "";
        String tradeNo = "";
        String tradeStatus = "";
        String totalAmount = "";
        try {
            outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("转换格式出错:" + e1.getMessage(), e1);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        logger.info("支付宝同步通知 订单号=" + outTradeNo + " 流水=" + tradeNo + " 金额=" + totalAmount);
//     boolean verify_result = AlipayNotify.verify(params);
        boolean verify_result = false;
        try {
            verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.info("verify_result  " + verify_result);
        if (verify_result) { //验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            logger.info("trade_status  " + tradeStatus);
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
            String mystr = "orderNo=" + outTradeNo + "&payType=alipay&serialNo=" + tradeNo + "&thirdPayAmt=" + totalAmount;
            logger.info("mystr=" + mystr);
            try {
                JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
//                 if(resultStr.containsKey("status")||(resultStr.containsKey("errcode")&&resultStr.getString("errcode").equals("20095"))){
                 if(resultStr.containsKey("status")){
                     logger.info("支付宝支付确认订单完成同步通知成功："+outTradeNo);
//                 }else if(resultStr.containsKey("errcode")&&!resultStr.getString("errcode").equals("20095")){
                 }else{
                     logger.info("订单号"+outTradeNo+"支付宝支付成功，确认订单发生异常:"+resultStr.getString("errcode"));
                     request.setAttribute("msg", "订单号："+outTradeNo+" 支付发生异常，请联系无忧保客服400-111-8900");
                     request.setAttribute("orderNo", outTradeNo);
                     request.setAttribute("type", 0);
                     return "order/orderfail";
                 }
             }catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            return "redirect:" + Constant.URL + "/personorder/gotoPaysuc.do?orderNo=" + outTradeNo;
            //该页面可做页面美工编辑
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
        } else {
            return "redirect:" + Constant.URL + "/jsp/my/allorderlist.jsp";
            //该页面可做页面美工编辑
        }
    }

    /**
     * 跳转到分享页
     *
     * @param request
     * @param response
     * @return
     * @throws UnknownHostException
     */
    @RequestMapping("/gotoshare")
    public String gotoshare(HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
        HttpSession session = ContextHolderUtils.getSession();
        JSONObject resultStr = new JSONObject();
        JSONObject resultStr2 = new JSONObject();
//         logger.info("跳转到分享页639");
         String userId="";
         String status="0"; //0 活动有效 未领过   1 已领过   2 未领过，活动失效   3 领取异常
         String openid="";
         Object openidObject =  session.getAttribute("openid");
         String user="";
         String mobile="";
         String[] arr;
         String mystr="";
         String errorMsg="";
         String isReceive="";
         if(openidObject!=null&&!"".equals(openidObject)){
             openid= openidObject.toString();
         }
         if(StringUtils.isNotEmpty(openid)){
             arr=new String[]{"openid"+openid};
             mystr="openid="+openid;
             try {
                 resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_FIND_URL,mystr,arr));//获取手机号
                 if(0 == resultStr.getInt("status")){
                     JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
                     mobile=data.getString("member_mobile");
                     user=data.getString("member_id");
//                     logger.info("跳转到分享页  MOBILE_FIND_URL "+mobile+" user "+user);
                }
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }

        }
        String triggerSence = "3";//触发场景 1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
        String shareUserId = request.getParameter("shareUserId");
        String orderNo = request.getParameter("orderNo");
        String payTime = "";
        String coupons = "";
        mystr = "policyHolderId=" + shareUserId + "&orderNo=" + orderNo;
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
        }
        if (0 == resultStr.getInt("status")) {
            JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
            payTime = data.getString("payTime");
        }
        if (StringUtils.isNotEmpty(mobile)) {
            mystr = "orderNo=" + orderNo + "&mobile=" + mobile;
            if (StringUtils.isNotEmpty(openid)) {
                mystr += "&openId=" + openid;
            }
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ISHADSHARE, mystr));//判断是否享受过活动或者领用过代金券 0未领过
            if (0 == resultStr.getInt("data")) {
                status = "0";
                mystr = "triggerSence=" + triggerSence;
                if (StringUtils.isNotEmpty(orderNo)) {
                    mystr += "&orderNo=" + orderNo;
                }
                if (StringUtils.isNotEmpty(payTime)) {
                    mystr += "&orderTime=" + payTime;
                }
                resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY, mystr));//判断是否享有活动
                if (resultStr.getString("errcode").equals("0")) {
                    if (resultStr.getString("data") != null && resultStr.getString("data").equals("false")) {
                        status = "2";
                    }
                } else {
                    request.setAttribute("memberMobile", mobile);
                    request.setAttribute("voucherStatus", status);
                    request.setAttribute("errmsg", resultStr.getString("errmsg"));
                }
            } else {
                status = "1";
            }
            if (status.equals("0")) {
                if (StringUtils.isNotEmpty(mobile)) {
                    try {
                        mystr = "openid=" + openid + "&member_mobile=" + mobile;
                        arr = new String[]{"openid" + openid, "member_mobile" + mobile};
                        resultStr2 = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.MOBILE_SAVE_URL, mystr.toString(), arr));
                    } catch (Exception e) {
                        logger.error(" 微信分享得代金卷获取手机添加出错：" + e.getMessage(), e);
                    }
                }
                String ipAddress = IPUtil.toIpAddr(request);
                String operateUser = "0";
                mystr = "ipAddress=" + ipAddress + "&operateUser=" + operateUser + "&triggerSence=" + triggerSence;
                if (StringUtils.isNotEmpty(orderNo)) {
                    mystr += "&orderNo=" + orderNo;
                }
                if (StringUtils.isNotEmpty(openid)) {
                    mystr += "&openId=" + openid;
                }
                if (StringUtils.isNotEmpty(mobile)) {
                    mystr += "&mobile=" + mobile;
                }
                if (StringUtils.isNotEmpty(payTime)) {
                    mystr += "&orderTime=" + payTime;
                }
                if (StringUtils.isNotEmpty(user)) {
                    mystr += "&enjoyer=" + user;
                }
                //  busiOperate=1"; 0注册    1下单    2其他
                resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, mystr));//领取代金券
                if (resultStr.getString("errcode").equals("0")) {
                    if (resultStr.getString("data") != null && !resultStr.getString("data").equals("null")) {
                        isReceive = resultStr.getJSONObject("data").getString("isReceive");
                        coupons = resultStr.getJSONObject("data").getString("bills");
                    }
                    if (!isReceive.equals("Y")) {
                        String coupon = resultStr.getJSONObject("data").getString("bills") + "元";
                        String expDate = DateUtil.convertToDate(resultStr.getJSONObject("data").getInt("endDate"));
                        if (StringUtils.isNotEmpty(openid) && StringUtils.isNotEmpty(coupons)) {
                            weixinAPIService.sendTemplateMessageByType("3", "", coupon, expDate, "", "", "", openid, "", "");
                        }
                        if (StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(coupons)) {
                            String[] arrs;
                            StringBuffer mystrs = new StringBuffer("member_id=" + user + "&type=5");
                            List<String> list = new ArrayList<String>();
                            list.add("member_id" + user);
                            list.add("type5");
                            if (StringUtils.isNotEmpty(expDate)) {
                                mystrs.append("&effective_time=" + expDate);
                                list.add("effective_time" + expDate);
                            }
                            if (StringUtils.isNotEmpty(coupon)) {
                                mystrs.append("&money=" + coupon);
                                list.add("money" + coupon);
                            }
                            mystrs.append("&url=" + Constant.URL + "/jsp/my/mytickets.jsp");
                            list.add("url" + Constant.URL + "/jsp/my/mytickets.jsp");
                            arrs = list.toArray(new String[list.size()]);
                            try {
                                JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SEND_STATIONLETTER_URL, mystrs.toString(), arrs));
                            } catch (Exception e) {
                                 logger.error("发送站内信出错:" + e.getMessage(), e);
                            }
                         }
                     }
                     status="1";
                 }else{
                     status="3";
                     String code =resultStr.getString("errcode");
                     String msg[]=resultStr.getString("errmsg").split(",");
                     String msgType="";
                     if(code.equals("20035")){
                         errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”今天的数量已发放完，请关注我们其他的活动，如有疑义请详询400-111-8900";
                     }else if(code.equals("20036")){
                         errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”已全部被领光啦，请关注我们其他的活动，如有疑义请详询400-111-8900";
                         
                     }else if(code.equals("20037")){
                         String number=resultStr.getString("errmsg").split(",")[1];
                         errorMsg="很抱歉，您参与的活动“"+msg[0]+"”已超过每人限领 "+number+" 张，请关注我们其他的活动，如有疑义请详询400-111-8900";
                     }
                     if(StringUtils.isNotEmpty(user)){
                         String reason="";// 原因
                         String activityTitle=resultStr.getString("errmsg").split(",")[0];//活动标题
                         String number="";//每人限领张数
                         if(code.equals("20035")){
                             msgType="2";
                         }else if(code.equals("20036")){
                             msgType="3";
                         }else if(code.equals("20037")){
                             msgType="4";
                             number=resultStr.getString("errmsg").split(",")[1];
                         }
                         String[] arrs;
                         StringBuffer mystrs = new StringBuffer("member_id="+user+"&type="+msgType);
                         List<String> list=new ArrayList<String>();
                         list.add("member_id"+user);
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
             }else if(status.equals("1")){
                 String ipAddress=IPUtil.toIpAddr(request);
                 String operateUser="0";
                 mystr="ipAddress="+ipAddress+"&operateUser="+operateUser+"&triggerSence="+triggerSence;
                 if(StringUtils.isNotEmpty(orderNo)){
                     mystr+="&orderNo="+orderNo;
                 }
                 if(StringUtils.isNotEmpty(openid)){
                     mystr+="&openId="+openid;
                 }
                 if(StringUtils.isNotEmpty(mobile)){
                     mystr+="&mobile="+mobile;
                 }
                 if(StringUtils.isNotEmpty(payTime)){
                     mystr+="&orderTime="+payTime;
                 }
                 if(StringUtils.isNotEmpty(user)){
                     mystr+="&enjoyer="+user;
                 }
                 resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SEARCH,mystr));//领取代金券
                 if(resultStr.getString("errcode").equals("0")){
                     if(resultStr.getString("data")!=null&&!resultStr.getString("data").equals("null")){
                         coupons=resultStr.getJSONObject("data").getString("bills");
                         isReceive=resultStr.getJSONObject("data").getString("isReceive");
                     }
                     if(!isReceive.equals("Y")){
                         String coupon=resultStr.getJSONObject("data").getString("bills")+"元";
                         String expDate=DateUtil.convertToDate(resultStr.getJSONObject("data").getInt("endDate"));
                         if(StringUtils.isNotEmpty(openid)&&StringUtils.isNotEmpty(coupons)){
                             weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                         }
                         if(StringUtils.isNotEmpty(user)&&StringUtils.isNotEmpty(coupons)){
                             String[] arrs;
                             StringBuffer mystrs = new StringBuffer("member_id="+user+"&type=5");
                             List<String> list=new ArrayList<String>();
                             list.add("member_id"+user);
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
                 }else{
                     status="3";
                     String code =resultStr.getString("errcode");
                     String msg[]=resultStr.getString("errmsg").split(",");
                     if(code.equals("20035")){
                         errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”今天的数量已发放完，请关注我们其他的活动，如有疑义请详询400-111-8900";
                     }else if(code.equals("20036")){
                         errorMsg="很抱歉，您参与的代金券活动“"+msg[0]+"”已全部被领光啦，请关注我们其他的活动，如有疑义请详询400-111-8900";
                         
                     }else if(code.equals("20037")){
                         String number=resultStr.getString("errmsg").split(",")[1];
                         errorMsg="很抱歉，您参与的活动“"+msg[0]+"”已超过每人限领 "+number+" 张，请关注我们其他的活动，如有疑义请详询400-111-8900";
                     }
                 }
             }
         }
         if (status.equals("2")){
             errorMsg="很抱歉，当前没有代金券活动，请关注我们其他的活动，如有疑义请详询400-111-8900";
         }
         request.setAttribute("orderId", orderNo);
         request.setAttribute("payTime", payTime);
         request.setAttribute("voucherStatus", status);
         if(status.equals("0")){
             mobile="";
         }
         request.setAttribute("memberMobile", mobile);
         request.setAttribute("userId", user);
         request.setAttribute("bills", coupons);
         request.setAttribute("errorMsg", errorMsg);
//         logger.info("跳转到分享页status"+status+"userId"+user+"memberMobile"+mobile+"payTime"+payTime+"orderId"+orderNo+"bills"+coupons);
         return "activity/wuyouactivities";
     }
     
     /**
      * 跳转到订单详情
      * @param request
      * @param response
      * @return
      */
     @RequestMapping("/myOrderDetial")
     public String myOrderDetial(HttpServletRequest request, HttpServletResponse response){
         HttpSession session = ContextHolderUtils.getSession();
         JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         String orderNo=request.getParameter("orderNo");
         String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
                 + request.getContextPath()      //项目名称  
                 + request.getServletPath()      //请求页面或其他地址  
             + "?" + (request.getQueryString()); //参数  
         String userId="1";
         Object userIdObject = session.getAttribute("userId");
         Object openidObject = session.getAttribute("openid");
         if(userIdObject!=null&&!"".equals(userIdObject)){
             userId=userIdObject.toString();
         }

         //将session保存，以备后面用到
         if(openidObject!=null&&!"".equals(openidObject)){
             //分享参数
             String shareFlag="0";
             String timestamp =Constant.TIME_STAMP;
             String noncestr = Constant.NONCESTR;
             String url = strBackUrl;
             String payTime="";
             String orderType="2";
             String mystr="policyHolderId="+userId+"&orderNo="+orderNo;
             try {
                 resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
             } catch (Exception e) {
                 logger.error("订单明细查询接口出错:" + e.getMessage(), e);
             }
             if(0 == resultStr.getInt("status")){
                 JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
                 payTime=data.getString("payTime");
                 orderType=data.getString("orderType");
             }
             mystr="triggerSence=3";
             if(StringUtils.isNotEmpty(orderNo)){
                 mystr+="&orderNo="+orderNo;
             }
             if(StringUtils.isNotEmpty(payTime)){
                 mystr+="&orderTime="+payTime;
             }
             String title="";
             String desc="";
             if(orderType.equals("1")){
                 resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY,mystr));//判断是否享有活动
                 if(resultStr.getString("errcode").equals("0")){
                     if(resultStr.getString("data")!=null&&resultStr.getString("data").equals("true")){
                         shareFlag="1";//可以分享
                         JSONObject voucherObject =new JSONObject();
                         try {
                             String voucherremark="triggerSence=3";
                             voucherObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ACTIVITY_INFO, voucherremark));
                         } catch (Exception e) {
                             logger.error(" 判断是否享有活动：" + e.getMessage(), e);
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
                     }
                 }
             }
                 
//             logger.info("myorderdetail strBackUrl="+strBackUrl);
//             String shareurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openidshare.do?next=personorder/gotoshare.do"+Constant.APP_ID+"ORDERNO"+orderNo+"USERID"+userId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
             String shareUrl=Constant.URL+"/jsp/middlepage/index.jsp?name=orderShare&orderNo="+orderNo+"&memberId="+userId;
             Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
             String jsapiTicket = weixin.getJsapiTicket();
             String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
             request.setAttribute("timestamp", timestamp);
             request.setAttribute("noncestr", noncestr);
             request.setAttribute("url", url);
             request.setAttribute("signature", signature);
             request.setAttribute("appid", Constant.APP_ID);
             request.setAttribute("shareurl", shareUrl);
             request.setAttribute("orderNo", orderNo);
             request.setAttribute("shareFlag", shareFlag);
             request.setAttribute("title", title);
             request.setAttribute("desc", desc);
         }
         return "my/orderdetail";
     } 
 
     /**
      * 跳转到支付页
      * @param request
      * @param response
      * @return
      */
     @RequestMapping("/gotoPay")
     public String gotoPay(HttpServletRequest request, HttpServletResponse response){
         String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
                 + request.getContextPath()      //项目名称  
                 + request.getServletPath()      //请求页面或其他地址  
             + "?" + (request.getQueryString()); //参数  
         //分享参数
         String timestamp =Constant.TIME_STAMP;
         String noncestr = Constant.NONCESTR;
         String orderNo=request.getParameter("orderNo");
         String url = strBackUrl;
//         logger.info("gotoPay strBackUrl="+strBackUrl);
         Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
         String jsapiTicket = weixin.getJsapiTicket();
         String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
         request.setAttribute("timestamp", timestamp);
         request.setAttribute("noncestr", noncestr);
         request.setAttribute("url", url);
         request.setAttribute("signature", signature);
         request.setAttribute("appid", Constant.APP_ID);
         request.setAttribute("orderNo", orderNo);
         return "order/orderpay";
     } 
     /**
      * 跳转到支付页
      * @param request
      * @param response
      * @return
      */
     @RequestMapping("/gotodetailunPay")
     public String gotodetailunPay(HttpServletRequest request, HttpServletResponse response){
         String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
                 + request.getContextPath()      //项目名称  
                 + request.getServletPath()      //请求页面或其他地址  
             + "?" + (request.getQueryString()); //参数  
         //分享参数
         String orderNo=request.getParameter("orderNo");
         String type=request.getParameter("type");
         String shareurl=Constant.URL+"/personorder/gotodetailunPay.do?orderNo="+orderNo;
         if(StringUtils.isNotEmpty(type)){
             shareurl+="&type="+type;
         }
         String shortUrl="";
         logger.info("shareurl:"+shareurl);
         try {
             shareurl=  java.net.URLEncoder.encode(shareurl,"utf-8");   
             String mystr="url="+shareurl;
             String arr[]= new String[] {"url"+shareurl};
             JSONObject resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, mystr, arr));
             if(0 == resultObject.getInt("status")){
                 JSONObject data = JSONObject.fromObject(resultObject.getString("data"));
                 shortUrl=data.getString("short_url");
             }
         } catch (Exception e) {
             logger.error(" 订单短链接出错：" + e.getMessage(), e);
         }
         request.setAttribute("orderUrl", shortUrl);
         String timestamp =Constant.TIME_STAMP;
         String noncestr = Constant.NONCESTR;
         String url = strBackUrl;
//         logger.info("gotodetailunPay strBackUrl="+strBackUrl);
         Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
         String jsapiTicket = weixin.getJsapiTicket();
         String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
         request.setAttribute("timestamp", timestamp);
         request.setAttribute("noncestr", noncestr);
         request.setAttribute("url", url);
         request.setAttribute("signature", signature);
         request.setAttribute("appid", Constant.APP_ID);
         request.setAttribute("orderNo", orderNo);
         request.setAttribute("type", type);
//         return "my/orderdetail";
        return "redirect:" + Constant.URL + "/jsp/my/orderdetail.jsp?orderNo=" + orderNo;
    }

    /**
     * 跳转到支付页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoPaysuc")
    public String gotoPaysuc(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ContextHolderUtils.getSession();
        String strBackUrl = Constant.URL.substring(0, Constant.URL.indexOf(":")) + "://" + request.getServerName() //服务器地址
                + request.getContextPath()      //项目名称
                + request.getServletPath()      //请求页面或其他地址
                + "?" + (request.getQueryString()); //参数
        //分享参数
        Object userIdObject = session.getAttribute("userId");
        String userId = "";
        if (userIdObject != null && !"".equals(userIdObject)) {
            userId = userIdObject.toString();
        }
        String orderNo = request.getParameter("orderNo");
        String payTime = "";
        JSONObject orderresultStr = new JSONObject();
        String mystr = "policyHolderId=" + userId + "&orderNo=" + orderNo;
        String orderType = "2";
        Integer needFile = 0;
        try {
            orderresultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
        }

        logger.info(orderresultStr.toString());
        if (0 == orderresultStr.getInt("status")) {
            JSONObject data = JSONObject.fromObject(orderresultStr.getString("data"));
            if (data.getString("orderStatus").equals("10")) {//跳转支付成功页面时，查询订单状态，如果未未支付，跳转到提示页面
                request.setAttribute("msg", "订单号：" + orderNo + " 支付发生异常，请联系无忧保客服400-111-8900");
                request.setAttribute("orderNo", orderNo);
                request.setAttribute("type", 0);
                return "order/orderfail";
            }
            payTime = data.getString("payTime");
            orderType = data.getString("orderType");
            needFile = data.getInt("needFile");
        }
        String title = "";
        String desc = "";
        int shareFlag = 0;
        String shareIco = "share_index.png";
        String orderTime = payTime;
        mystr = "triggerSence=3";
        if (StringUtils.isNotEmpty(orderNo)) {
            mystr += "&orderNo=" + orderNo;
        }
        if (StringUtils.isNotEmpty(orderTime)) {
            mystr += "&orderTime=" + orderTime;
        }
        if (orderType.equals("1")) {
            JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY, mystr));//判断是否享有活动
            if (resultStr.getString("errcode").equals("0")) {
                if (resultStr.getString("data") != null && resultStr.getString("data").equals("true")) {
                    shareFlag = 1;//可以分享
                } else {
                    shareFlag = 0;//不能分享
                }
            }
            JSONObject voucherObject = new JSONObject();
            try {
                String voucherremark = "triggerSence=3";
                voucherObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ACTIVITY_INFO, voucherremark));
            } catch (Exception e) {
                logger.error(" 查找用户所有拥有的代金券出错：" + e.getMessage(), e);
            }
            if (voucherObject.getString("errcode").equals("0")) {
                JSONArray titles = voucherObject.getJSONArray("data");
                int length = titles.size();
                title = "推荐您一个在线缴社保工具，躺着就能把社保交了，赶紧来试试吧";
                desc = "无忧保-为个体提供微信在线自助缴纳社保，免费专家咨询等全方位专业服务，开启社保服务新时代！";
                for (int i = 0; i < length; i++) {//遍历JSONArray
                    JSONObject oj = titles.getJSONObject(i);
                    if (oj.getString("entityCode").trim().equals("title")) {
                        if (StringUtils.isNotEmpty(oj.getString("entityValue"))) {
                            title = oj.getString("entityValue");
                        }
                    }
                    if (oj.getString("entityCode").trim().equals("description")) {
                        if (StringUtils.isNotEmpty(oj.getString("entityValue"))) {
                            desc = oj.getString("entityValue");
                        }
                    }
                }
            }
        }
        String shareurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APP_ID + "&redirect_uri=" + Constant.URL + "/scope/openidshare.do?next=personorder/gotoshare.do" + Constant.APP_ID + "ORDERNO" + orderNo + "USERID" + userId + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        if (session.getAttribute("openid") == null || shareFlag == 0) {//不送券
            shareIco = "share_index_1.png";
            shareurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APP_ID + "&redirect_uri=" + Constant.URL + "/scope/openid.do?next=personsocial/gotoindex.do" + Constant.APP_ID + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        }
        String timestamp = Constant.TIME_STAMP;
        String noncestr = Constant.NONCESTR;
        String url = strBackUrl;
        logger.info("gotoPaysuc url=" + strBackUrl);
        Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
        String jsapiTicket = weixin.getJsapiTicket();
        String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
        request.setAttribute("timestamp", timestamp);
        request.setAttribute("noncestr", noncestr);
        request.setAttribute("url", url);
        request.setAttribute("signature", signature);
        request.setAttribute("appid", Constant.APP_ID);
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("param1", orderNo);
        request.setAttribute("shareurl", shareurl);
        request.setAttribute("shareFlag", shareFlag);
        request.setAttribute("shareIco", shareIco);
        request.setAttribute("title", title);
        request.setAttribute("desc", desc);
        request.setAttribute("needFile", needFile);
        return "order/ordersuccess";
    }

    /**
     * 余额支付校验
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/leftCheck")
    public
    @ResponseBody
    Object leftCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = ContextHolderUtils.getSession();
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        Object userIdObject = session.getAttribute("userId");
        if (userIdObject == null || "".equals(userIdObject)) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String userId = userIdObject.toString();
        String orderNo = request.getParameter("orderNo");
        String payTime = "";
//         JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BALANCE, "policyHolderId=" +userId));
         JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURERBALANCE_BY_ORDER, "orderNo=" +orderNo));//取参保人可用余额
         double leftAmt=balance.getJSONObject("data").getDouble("leftAmt");
         JSONObject orderresultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, "policyHolderId="+userId+"&orderNo="+orderNo));
         double orderAmt=orderresultStr.getJSONObject("data").getDouble("orderAmt");
         if(leftAmt<orderAmt){
             resultStr.put("msg", "余额不足");
         }
         response.setContentType("text/html; charset=utf-8");  
         PrintWriter out=response.getWriter();
         out.println(resultStr);
         return null;
     }
     /**
      * 余额支付
      * @param request
      * @param response
      * @return
      */
     @RequestMapping("/payByLeft")
     public String payByLeft(HttpServletRequest request, HttpServletResponse response){
         HttpSession session=ContextHolderUtils.getSession();
         String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
                 + request.getContextPath()      //项目名称  
                 + request.getServletPath()      //请求页面或其他地址  
             + "?" + (request.getQueryString()); //参数  
         //分享参数
         Object userIdObject=session.getAttribute("userId");
         String orderNo=request.getParameter("orderNo");
         if (userIdObject==null||"".equals(userIdObject)) {
             request.setAttribute("orderNo", orderNo);
             request.setAttribute("msg", "订单号："+orderNo+" 支付失败，请重式");
             request.setAttribute("type", 0);
             return "order/orderfail";
         }
         String userId=userIdObject.toString();
         
         String payTime="";
//         JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BALANCE, "policyHolderId=" +userId));
         JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURERBALANCE_BY_ORDER, "orderNo=" +orderNo));//取参保人可用余额
         double leftAmt=balance.getJSONObject("data").getDouble("leftAmt");
         JSONObject orderresultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, "policyHolderId="+userId+"&orderNo="+orderNo));
         double orderAmt=orderresultStr.getJSONObject("data").getDouble("orderAmt");
         Integer needFile=orderresultStr.getJSONObject("data").getInt("needFile");
         if(leftAmt<orderAmt){//余额不足
             request.setAttribute("orderNo", orderNo);
             request.setAttribute("msg", "订单号："+orderNo+" 支付失败，请重式");
             request.setAttribute("type", 0);
             return "order/orderfail";
         }
         JSONObject resultStr=new JSONObject();
         Date now =new Date();
         SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
         String serialNo =sdf.format(now)+(int)(Math.random()*1000000); // 交易流水号
         String mystr="orderNo="+orderNo+"&payType=cashaccount&serialNo="+serialNo+"&thirdPayAmt=0";
         try {
             resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
         } catch (Exception e) {
             logger.error("获取数据出错:" + e.getMessage(), e);
         }
         if(resultStr.containsKey("errcode")){
             logger.info("订单号"+orderNo+"余额支付成功，确认订单发生异常:"+resultStr.getString("errcode"));
             request.setAttribute("orderNo", orderNo);
//             if(resultStr.getString("errcode").equals("20095")){
//                 request.setAttribute("msg", resultStr.getString("errmsg"));
//                 request.setAttribute("type", 1);
//             }else{
                 request.setAttribute("msg", "订单号："+orderNo+" 支付发生异常，请联系无忧保客服400-111-8900");
                 request.setAttribute("type", 0);
//             }
             return "order/orderfail";
         }
         orderresultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, "policyHolderId="+userId+"&orderNo="+orderNo));
         if(0 == orderresultStr.getInt("status")){
             JSONObject data = JSONObject.fromObject(orderresultStr.getString("data"));
             payTime=data.getString("payTime");
         }
         
         String shareFlag="1";
         String orderTime=payTime;
         mystr="triggerSence=3";
         if(StringUtils.isNotEmpty(orderNo)){
             mystr+="&orderNo="+orderNo;
         }
         if(StringUtils.isNotEmpty(orderTime)){
             mystr+="&orderTime="+orderTime;
         }
         resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY,mystr));//判断是否享有活动
         if(resultStr.getString("errcode").equals("0")){
             if(resultStr.getString("data")!=null&&resultStr.getString("data").equals("true")){
                 shareFlag="0";//可以分享
             }else{
                 shareFlag="1";//不能分享
             }
         }
         String title="";
         String desc="";
         JSONObject voucherObject =new JSONObject();
         try {
             String voucherremark="triggerSence=3";
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
         String shareurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openidshare.do?next=personorder/gotoshare.do"+Constant.APP_ID +"ORDERNO"+orderNo+"USERID"+userId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
         String timestamp =Constant.TIME_STAMP;
         String noncestr = Constant.NONCESTR;
         String url = strBackUrl;
         logger.info("gotoPaysuc url="+strBackUrl);
         Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
         String jsapiTicket = weixin.getJsapiTicket();
         String signature = SignUtil.getSignature(jsapiTicket, timestamp, noncestr, url);
         request.setAttribute("timestamp", timestamp);
         request.setAttribute("noncestr", noncestr);
         request.setAttribute("url", url);
         request.setAttribute("signature", signature);
         request.setAttribute("appid", Constant.APP_ID);
         request.setAttribute("orderNo", orderNo);
         request.setAttribute("param1", orderNo);
         request.setAttribute("shareurl", shareurl);
         request.setAttribute("shareFlag", shareFlag);
         request.setAttribute("title", title);
         request.setAttribute("desc", desc);
         request.setAttribute("needFile", needFile);
         return "order/ordersuccess";
     } 
     
     /**
      * 余额支付发送验证码短信
      * @param request
      * @param response
      * @return JSON
      * @author swf
      * @throws IOException
      */
     @RequestMapping("/sendOrderCode")
     public @ResponseBody Object sendOrderCode(HttpServletRequest request,HttpServletResponse response) throws IOException{
         JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         String mobile=request.getParameter("mobile");
         String requestType = request.getHeader("X-Requested-With");  
         logger.info("手机号："+mobile+"余额支付发送短信请求类型："+requestType);
         if(null!=requestType&&requestType.trim().equals("XMLHttpRequest")){
             HttpSession session = ContextHolderUtils.getSession();
             Object userIdObject =  session.getAttribute("userId");
             if(userIdObject ==null ){
                 return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
             }
             if(StringUtils.isEmpty(mobile)){
                 return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
             }
             try {
                 String[] arr = new String[] {"phone"+mobile};
                 String mystr = "phone="+mobile;
                 resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_SEND_CODE, mystr.toString(), arr));
             } catch (Exception e) {
                 logger.error(" 发送短信出错：" + e.getMessage(), e);
             }
             //更新redis中的发送记录  shenwf 20170401 V3.52
             String sendMsgCountKey="sendMsgCount";
             Integer sendMsgCount=0;
             Integer sendMsgCountLeftTime=3600;
             JSONObject resultStr =  articleRedisHandleServiceImpl.readVCode(sendMsgCountKey);
             if(resultStr != null) {
                 sendMsgCount = resultStr.getInt("count");
                 sendMsgCountLeftTime=resultStr.getInt("redisTimeleft");
             }else {
                 resultStr = JSONObject.fromObject("{\"count\":0}");
             }
             sendMsgCount++;
             resultStr.put("count", sendMsgCount);
             articleRedisHandleServiceImpl.saveVCode(sendMsgCountKey, resultStr, sendMsgCountLeftTime);
         }
         response.setContentType("text/html; charset=utf-8");  
         PrintWriter out=response.getWriter();
         out.println(resultObject);
         return null;
     }
     
     /**
      * 验证校验码
      * @param request
      * @param response
      * @return JSON
      * @author swf
      * @throws IOException
      */
     @RequestMapping("/checkOrderCode")
     public @ResponseBody Object checkOrderCode(HttpServletRequest request,HttpServletResponse response) throws IOException{
         JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
         String mobile=request.getParameter("mobile");
         String code=request.getParameter("code");
         String requestType = request.getHeader("X-Requested-With");  
         logger.info("手机号："+mobile+"余额支付发送短信请求类型："+requestType);
         if(null!=requestType&&requestType.trim().equals("XMLHttpRequest")){
             HttpSession session = ContextHolderUtils.getSession();
             Object userIdObject =  session.getAttribute("userId");
             if(userIdObject ==null ){
                 return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
             }
             if(StringUtils.isEmpty(mobile)){
                 return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
             }
             if(StringUtils.isEmpty(code)){
                 return JSONObject.fromObject("{\"status\":1,\"msg\":\"验证码不能为空\"}");
             }
             try {
                 String[] arr = new String[] {"mobile"+mobile,"security_code"+code};
                 String mystr = "mobile="+mobile+"&security_code="+code;
                 resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_SECURITY_CODE, mystr.toString(), arr));
             } catch (Exception e) {
                 logger.error(" 发送短信出错：" + e.getMessage(), e);
             }
         }
         response.setContentType("text/html; charset=utf-8");  
         PrintWriter out=response.getWriter();
         out.println(resultObject);
         return null;
     }
}
