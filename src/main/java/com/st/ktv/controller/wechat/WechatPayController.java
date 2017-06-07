package com.st.ktv.controller.wechat;

import com.st.utils.Constant;
import com.st.ktv.controller.wechat.common.Configure;
import com.st.ktv.controller.wechat.common.MD5;
import com.st.ktv.controller.wechat.common.report.protocol.ReportReqData;
import com.st.utils.DataUtil;
import com.st.utils.ParseXmlUtil;
import com.st.ktv.service.impl.WeixinAPIServiceImpl;
import com.st.utils.JoYoUtil;
import com.st.utils.wx.WeixinUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;



@SuppressWarnings("deprecation")
@RequestMapping("/wechat/pay")
@Controller
public class WechatPayController {
	
	private static final Logger logger = Logger.getLogger(WechatPayController.class);
	
	@SuppressWarnings("unused")
	@Resource
	private WeixinAPIServiceImpl weixinAPIService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public  String pay(HttpServletRequest req,Model model) {
		String openid=(String) req.getSession().getAttribute("openid");
		String userId=(String) req.getSession().getAttribute("userId");
		logger.info("openid="+openid);
		String orderNo =req.getParameter("orderNo");
		String leftAmtStr=req.getParameter("leftAmt");
        logger.info(orderNo+"微信支付传入余额:"+leftAmtStr);
        double leftAmtD=0.0;
        if(DataUtil.isNotEmpty(leftAmtStr)){
            leftAmtD=Double.parseDouble(leftAmtStr);
			JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet("", "orderNo=" +orderNo));//取参保人可用余额
            double leftAmt=balance.getJSONObject("data").getDouble("leftAmt");
            logger.info("微信支付获取余额:"+leftAmt);
            if(leftAmtD>leftAmt){
                req.setAttribute("error", "余额信息有误");
                return "error：余额信息有误";
            }
        }
		
		if (StringUtils.isEmpty(orderNo)) {
				req.setAttribute("error", "订单号不能为空");
				return "error";
		}
		if (StringUtils.isEmpty(userId)) {
			req.setAttribute("error", "获取用户出错了");
			return "error";
		}
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String mystr="policyHolderId="+userId+"&orderNo="+orderNo;
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet("", mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
        }
		String productName = "无忧保订单-"+orderNo;
		String totalFee   ="0";
		model.addAttribute("orderId", orderNo);
		if(0 == resultStr.getInt("status")){
			JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
//			String order_money = message.getString("true_money");//获取实付金额
//			model.addAttribute("order_money", order_money);
//			logger.info("order_money（元）:"+order_money);
			double orderAmt = message.getDouble("orderAmt");
//            double payBalanceAmt = message.getDouble("payBalanceAmt");
            String payAmt=new java.text.DecimalFormat("#0.00").format(orderAmt-leftAmtD);
            logger.info("ordermoney（元）:"+payAmt);
            String orderStatus = message.getString("orderStatus");
            if(!"10".equals(orderStatus)){
                req.setAttribute("error", "请勿重复支付");
                return "error：请勿重复支付";
            }
			model.addAttribute("pay_method", "wechatpay");
			if(StringUtils.isEmpty(payAmt)){
			    payAmt="0";
			}else {
				int index = payAmt.indexOf(".");    
		        int length = payAmt.length();    
		        Long amLong = 0l;    
		        if(index == -1){    
		            amLong = Long.valueOf(payAmt+"00");    
		        }else if(length - index >= 3){    
		            amLong = Long.valueOf((payAmt.substring(0, index+3)).replace(".", ""));    
		        }else if(length - index == 2){    
		            amLong = Long.valueOf((payAmt.substring(0, index+2)).replace(".", "")+0);    
		        }else{    
		            amLong = Long.valueOf((payAmt.substring(0, index+1)).replace(".", "")+"00");    
		        } 
		        payAmt=amLong+"";
//                payAmt="1";
				logger.info("order_money（分）:"+payAmt);
			}
			totalFee=payAmt;
		}else {
			req.setAttribute("error", "获取订单详情出错了");
			return "error";
		}
        try {
			// 参数
			String nonceStr = RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm"); // 8位随机数
			ReportReqData reportReqData=new ReportReqData(productName, openid, orderNo, getIp(req), totalFee,nonceStr);
			XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
			Annotations.configureAliases(xStreamForRequestPostData,ReportReqData.class );
			String postDataXML = xStreamForRequestPostData.toXML(reportReqData);
			String jsonObject=WeixinUtil.wxpayRequset("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", postDataXML);
			logger.info("jsonObject:"+jsonObject);
			Map<String, String> map=ParseXmlUtil.parseXmlText(jsonObject);
			String prepayId=map.get("prepay_id");
			// appId
			Map<String, String> paymap=new HashMap<String, String>();
			String paytimeStamp=new Date().getTime()+"";
			String paynonceStr=RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm");
			paymap.put("appId", Constant.APP_ID);
			paymap.put("timeStamp", paytimeStamp);
			paymap.put("nonceStr", paynonceStr);
			paymap.put("package", "prepay_id="+prepayId);
			paymap.put("signType", "MD5");
			String pay2sign = getSign(paymap);
			model.addAttribute("appid",Constant.APP_ID);
			model.addAttribute("timeStamp", paytimeStamp);
			model.addAttribute("nonceStr", paynonceStr);
			model.addAttribute("_package", "prepay_id="+prepayId);
			model.addAttribute("paySign", pay2sign);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	return "wechat/pay";
	}
  public  String getSign(Map<String,String> map){
	        ArrayList<String> list = new ArrayList<String>();
	        for(Map.Entry<String,String> entry:map.entrySet()){
	            if(entry.getValue()!=""){
	                list.add(entry.getKey() + "=" + entry.getValue() + "&");
	            }
	        }
	        int size = list.size();
	        String [] arrayToSort = list.toArray(new String[size]);
	        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
	        StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < size; i ++) {
	            sb.append(arrayToSort[i]);
	        }
	        String result = sb.toString();
	        result += "key=" + Configure.key;
	        result = MD5.MD5Encode(result).toUpperCase();
	        return result;
	    }
	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		if (request == null)
			return "";
		String ip = request.getHeader("X-Requested-For");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	@SuppressWarnings({"unused" })
	public static void main(String[] args) {
//		String openid="o6C6fs8DXl59wkjQduZDwQNpcCGY";
//		logger.info("openid="+openid);
//		String productName = "测试商品001";
//		String totalFee   = "1";
//		String orderid     = RandomStringGenerator.getRandomStringByLength(32);
//        try {
//			// 对商品名截取, 去除空格
//			productName = productName.replaceAll(" ", "");
//			productName = productName.length() > 17 ? productName.substring(0, 17) + "..." : productName;
//			// 参数
//			String timeStamp = new Date().getTime() + "";
//			String nonceStr = RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm"); // 8位随机数
//			ReportReqData reportReqData=new ReportReqData(productName, openid, orderid, "127.0.0.1", total_fee,nonceStr);
//			XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
//			Annotations.configureAliases(xStreamForRequestPostData,ReportReqData.class );
//			String postDataXML = xStreamForRequestPostData.toXML(reportReqData);
//			String jsonObject=WeixinUtil.wxpayRequset("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", postDataXML);
//			logger.info("jsonObject:"+jsonObject);
//			Map<String, String> map=ParseXmlUtil.parseXmlText(jsonObject);
//			String paysign=	map.get("sign");
//			String paynonce_str=map.get("nonce_str");
//			String prepay_id=map.get("prepay_id");
//			logger.info("paysign:"+paysign);
//			// appId
//			Map<String, String> paymap=new HashMap<String, String>();
//			String paytimeStamp=new Date().getTime()+"";
//			String paynonceStr=RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm");
//			paymap.put("appid", Constant.APP_ID);
//			paymap.put("timeStamp", paytimeStamp);
//			paymap.put("nonceStr", paynonceStr);
//			paymap.put("signType", "MD5");
//			paymap.put("package", "prepay_id="+prepay_id);
//			String pay2sign = Signature.getSign(paymap);
//			System.out.println("prepay_id--"+prepay_id);
//			System.out.println("appid---"+ Constant.APP_ID);
//			System.out.println("timeStamp--"+paytimeStamp);
//			System.out.println("nonceStr---"+paynonceStr);
//			System.out.println("==="+pay2sign);
//		} catch (Exception e) {
//			logger.error(e.getMessage(),e);
//		}
	}
}
