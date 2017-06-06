package com.st.controller.mclient.web;

import com.st.constant.Constant;
import com.st.controller.mclient.constant.MerchantConstant;
import com.st.controller.mclient.domain.request.PaySignEntity;
import com.st.controller.mclient.domain.request.WebPayReqDto;
import com.st.core.util.date.DateUtil;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import com.st.utils.mclient.DESUtil;
import com.st.utils.mclient.SignUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


@RequestMapping("/mclient/pay")
@Controller
public class MclientPayController {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	@Resource
	private MerchantConstant merchantConstant;
	/**
	 * 
		 * <ul>
		 * <li>
		 * <b>功能：        tradeNum      <br/>
		 * <p>
		 * auther:gaozhanglei<br/>
		 * @param webPayReqDto 
		 * tradeNum--订单id   tradeName--商品名称 tradeAmount-商品金额,userid参数
		 * @return
		 * </p>
		 * </li>
		 * </ul>
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET, produces = "text/html;charset=UTF-8")
   public @ResponseBody String pay(HttpServletRequest req,WebPayReqDto webPayReqDto) {
		String openid=(String) req.getSession().getAttribute("openid");
		String userId=(String) req.getSession().getAttribute("userId");

		logger.info("openid="+openid);
		if (StringUtils.isEmpty(webPayReqDto.getTradeNum())) {
			req.setAttribute("error", "订单号不能为空");
			return "error：订单号不能为空";
		}
		if (StringUtils.isEmpty(userId)) {
			req.setAttribute("error", "获取用户出错了");
			return "error：获取用户出错了";
		}
		String leftAmtStr=req.getParameter("leftAmt");
		double leftAmtD=0.0;
		logger.info("京东支付传入余额:"+leftAmtStr);
		if(com.st.core.util.text.StringUtils.isNotEmpty(leftAmtStr)){
			leftAmtD=Double.parseDouble(leftAmtStr);
			JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURERBALANCE_BY_ORDER, "orderNo="+webPayReqDto.getTradeNum()));
			double leftAmt=balance.getJSONObject("data").getDouble("leftAmt");
			logger.info("京东支付获取余额:"+leftAmt);
			if(leftAmtD>leftAmt){
				req.setAttribute("error", "余额信息有误");
				return "error：余额信息有误";
			}
		}
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
	    String mystr="policyHolderId="+userId+"&orderNo="+webPayReqDto.getTradeNum();
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
        }
		webPayReqDto.setTradeName("无忧保订单-"+webPayReqDto.getTradeNum());
		webPayReqDto.setTradeAmount("0");
		String token=DateUtil.dateFormat(new Date(), DateUtil.DATE_FORMATE_LX_YMDHMSSS);//用户令牌 默认为时间  shenwf 20150826
		if(0 == resultStr.getInt("status")){
			JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
			logger.info("订单详情"+message);
			double orderAmt = message.getDouble("orderAmt");
			String payAmt=new java.text.DecimalFormat("#0.00").format(orderAmt-leftAmtD);
			logger.info("ordermoney（元）:"+payAmt);
			String orderStatus = message.getString("orderStatus");
			if(!"10".equals(orderStatus)){
				req.setAttribute("error", "请勿重复支付");
				return "error：请勿重复支付";
			}
			
			if(message.containsKey("jdToken")){
			    if(!StringUtils.isEmpty(message.getString("jdToken").trim())){
	                token=message.getString("jdToken");
	            }
			}
			
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
				if(Constant.ENVIROMENT.equals("test")){
//					payAmt="1";
				}
				logger.info("order_money（分）:"+payAmt);
			}
			webPayReqDto.setTradeAmount(payAmt);
		}else {
			req.setAttribute("error", "获取订单详情出错了");
			return "error：获取订单详情出错了";
		}
		
		
		Map<String, String> sParaTemp = new HashMap<String, String>();
		
		PaySignEntity wePayMerchantSignReqDTO = new PaySignEntity();
        wePayMerchantSignReqDTO.setVersion("2.0");
        wePayMerchantSignReqDTO.setToken(token);
        wePayMerchantSignReqDTO.setMerchantNum(merchantConstant.getMerchantNum());
        wePayMerchantSignReqDTO.setTradeNum(webPayReqDto.getTradeNum());
        wePayMerchantSignReqDTO.setTradeTime(DateUtil.dateFormat(new Date(), DateUtil.DATE_FORMAT_YMDHMS));
        wePayMerchantSignReqDTO.setTradeName(webPayReqDto.getTradeName());        
        wePayMerchantSignReqDTO.setCurrency("CNY");
        wePayMerchantSignReqDTO.setMerchantRemark(webPayReqDto.getTradeName());
        wePayMerchantSignReqDTO.setTradeAmount(webPayReqDto.getTradeAmount());
        wePayMerchantSignReqDTO.setTradeDescription(webPayReqDto.getTradeName());
        wePayMerchantSignReqDTO.setSuccessCallbackUrl(merchantConstant.getSuccessCallbackUrl());
        wePayMerchantSignReqDTO.setFailCallbackUrl(merchantConstant.getFailCallbackUrl());
        wePayMerchantSignReqDTO.setNotifyUrl(merchantConstant.getNotifyUrl());

        /**
         * 商户签名
         */
        String signStr = SignUtil.sign(wePayMerchantSignReqDTO, merchantConstant.getPayRSAPrivateKey());
        webPayReqDto.setMerchantSign(signStr);

        if ("1.0".equals(wePayMerchantSignReqDTO.getVersion())) {
            //敏感信息未加密
        } else if ("2.0".equals(wePayMerchantSignReqDTO.getVersion())) {
            //敏感信息加密
            try {
                //获取商户 DESkey
                String desKey = Constant.MERCHANT_DESKEY;
                //对敏感信息进行 DES加密
                webPayReqDto.setMerchantRemark(DESUtil.encrypt(wePayMerchantSignReqDTO.getMerchantRemark(), desKey, "UTF-8"));
                webPayReqDto.setTradeNum(DESUtil.encrypt(wePayMerchantSignReqDTO.getTradeNum(), desKey, "UTF-8"));
                webPayReqDto.setTradeName(DESUtil.encrypt(wePayMerchantSignReqDTO.getTradeName(), desKey, "UTF-8"));
                webPayReqDto.setTradeDescription(DESUtil.encrypt(wePayMerchantSignReqDTO.getTradeDescription(), desKey, "UTF-8"));
                webPayReqDto.setTradeTime(DESUtil.encrypt(wePayMerchantSignReqDTO.getTradeTime(), desKey, "UTF-8"));
                webPayReqDto.setTradeAmount(DESUtil.encrypt(wePayMerchantSignReqDTO.getTradeAmount(), desKey, "UTF-8"));
                webPayReqDto.setCurrency(DESUtil.encrypt(wePayMerchantSignReqDTO.getCurrency(), desKey, "UTF-8"));
                webPayReqDto.setNotifyUrl(DESUtil.encrypt(wePayMerchantSignReqDTO.getNotifyUrl(), desKey, "UTF-8"));
                webPayReqDto.setSuccessCallbackUrl(DESUtil.encrypt(wePayMerchantSignReqDTO.getSuccessCallbackUrl(), desKey, "UTF-8"));
                webPayReqDto.setFailCallbackUrl(DESUtil.encrypt(wePayMerchantSignReqDTO.getFailCallbackUrl(), desKey, "UTF-8"));
            } catch (Exception e) {
				logger.error("对敏感信息进行DES加密出错:" + e.getMessage(), e);
            }
        }
        sParaTemp.put("version", wePayMerchantSignReqDTO.getVersion());
        sParaTemp.put("token", wePayMerchantSignReqDTO.getToken());
        sParaTemp.put("merchantSign", webPayReqDto.getMerchantSign());
        sParaTemp.put("merchantNum", wePayMerchantSignReqDTO.getMerchantNum());
        sParaTemp.put("merchantRemark", webPayReqDto.getMerchantRemark());
        sParaTemp.put("tradeNum", webPayReqDto.getTradeNum());
        sParaTemp.put("tradeName", webPayReqDto.getTradeName());
        sParaTemp.put("tradeDescription", webPayReqDto.getTradeDescription());
        sParaTemp.put("tradeTime", webPayReqDto.getTradeTime());
        sParaTemp.put("tradeAmount", webPayReqDto.getTradeAmount());
        sParaTemp.put("currency", webPayReqDto.getCurrency());
        sParaTemp.put("notifyUrl", webPayReqDto.getNotifyUrl());
        sParaTemp.put("successCallbackUrl", webPayReqDto.getSuccessCallbackUrl());
        sParaTemp.put("failCallbackUrl", webPayReqDto.getFailCallbackUrl());
        String html="";
        if(Constant.ENVIROMENT.equals("test")){
            html=   buildRequest2(sParaTemp,"POST","确定",merchantConstant.getWangyinServerPayUrl());//测试环境
        }else{
            html=   buildRequest(sParaTemp,"POST","确定",merchantConstant.getWangyinServerPayUrl());//生产环境
        }
        logger.info(html);
        return html;
    
   }
	
	  public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName,String url) {
	        //待请求参数数组
	     
	        List<String> keys = new ArrayList<String>(sParaTemp.keySet());

	        StringBuffer sbHtml = new StringBuffer();

	        sbHtml.append("<form id=\"_submit\" name=\"_submit\" action=\"" + url
	                       + "\" method=\"" + strMethod  + "\">");

	        for (int i = 0; i < keys.size(); i++) {
	            String name = (String) keys.get(i);
	            String value = (String) sParaTemp.get(name);

	            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
	        }

	        //submit按钮控件请不要含有name属性
	        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
	        sbHtml.append("<script>document.forms['_submit'].submit();</script>");

	        return sbHtml.toString();
	    }
	  
	  @SuppressWarnings("unused")
	public static String buildRequest2(Map<String, String> sParaTemp, String strMethod, String strButtonName,String url) {
	        //测试环境使用直接更新订单状态
	        HttpSession session=ContextHolderUtils.getSession();
	        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
	        Object userIdObject=session.getAttribute("userId");
	        String userId=userIdObject.toString();
	        String userName=(String) session.getAttribute("userName");
	        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
	        StringBuffer sbHtml = new StringBuffer();
	       
	        //submit按钮控件请不要含有name属性
			String payType = "unionpay"; // 交易方式
			String thirdPayAmt = sParaTemp.get("tradeAmount");//订单金额
			Date now =new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			String serialNo =sdf.format(now)+(int)(Math.random()*1000000);//"20150812090101"; // 交易流水号
			String orderNo="";
			try {
			    orderNo=DESUtil.decrypt(sParaTemp.get("tradeNum"),Constant.MERCHANT_DESKEY,"UTF-8");
			    thirdPayAmt = DESUtil.decrypt(sParaTemp.get("tradeAmount"),Constant.MERCHANT_DESKEY,"UTF-8");
			} catch (Exception e1) {
			}
			sbHtml.append("<form id=\"_submit\" name=\"_submit\" action=\"" +Constant.URL+"/personorder/testmclientpay.do?orderNo="
					+ orderNo +  "\" method=\"" + strMethod  + "\">");
			try {
		        StringBuffer result = new StringBuffer();    
		        if(thirdPayAmt.length()==1){    
		            result.append("0.0").append(thirdPayAmt);    
		        }else if(thirdPayAmt.length() == 2){    
		            result.append("0.").append(thirdPayAmt);    
		        }else{    
		            String intString = thirdPayAmt.substring(0,thirdPayAmt.length()-2);    
		            for(int i=1; i<=intString.length();i++){    
		                if( (i-1)%3 == 0 && i !=1){    
		                //    result.append(",");    
		                }    
		                result.append(intString.substring(intString.length()-i,intString.length()-i+1));    
		            }    
		            result.reverse().append(".").append(thirdPayAmt.substring(thirdPayAmt.length()-2));    
		        }
		        thirdPayAmt = result.toString();

			} catch (Exception e) {
				
			}
			String mystr="orderNo="+orderNo+"&payType="+payType+"&serialNo="+serialNo+"&thirdPayAmt="+thirdPayAmt+"&jdToken=00";
	        try {
	            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
	        } catch (Exception e) {
	        }
	        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
	        sbHtml.append("<script>document.forms['_submit'].submit();</script>");
	        return sbHtml.toString();
	    }
	}
