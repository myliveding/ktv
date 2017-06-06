package com.st.controller.swiftpass;

import com.st.controller.swiftpass.config.SwiftpassConfig;
import com.st.controller.swiftpass.util.SignUtils;
import com.st.controller.swiftpass.util.XmlUtils;
import com.st.core.util.text.StringUtils;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping("/swiftpass/pay")
@Controller
public class SwiftPassNotifyController {
    private final Logger logger = Logger.getLogger(this.getClass());

    @SuppressWarnings("unused")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(request);
            logger.info("威富通通知内容：" + resString);
            String serialNo = ""; // 交易流水号
            String orderNo = ""; // 订单号
            String thirdPayAmt = "0";//订单金额
            String respString = "fail";
//				boolean flag=false;
            if (resString != null && !"".equals(resString)) {
                Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
                String res = XmlUtils.toXml(map);
                logger.info("威富通通知内容：" + res);
                if (map.containsKey("sign")) {
                    if (!SignUtils.checkParam(map, SwiftpassConfig.key)) {
                        res = "验证签名不通过";
                        respString = "fail";
                    } else {
                        String status = map.get("status");
                        if (status != null && "0".equals(status)) {
                            String result_code = map.get("result_code");
                            if (result_code != null && "0".equals(result_code)) {
                                if (StringUtils.isNotEmpty(map.get("transaction_id"))) {
                                    serialNo = map.get("transaction_id");
                                }
                                if (StringUtils.isNotEmpty(map.get("out_trade_no"))) {
                                    orderNo = map.get("out_trade_no");
                                    logger.info("威富通返回带随机订单号orderNo:" + orderNo);
                                    if (orderNo.indexOf("_SWP")>-1){
                                        orderNo=orderNo.substring(0,orderNo.indexOf("_SWP"));
                                    }
                                }
                                if (StringUtils.isNotEmpty(map.get("total_fee"))) {
                                    thirdPayAmt = map.get("total_fee");
                                    try {
                                        StringBuffer result = new StringBuffer();
                                        if (thirdPayAmt.length() == 1) {
                                            result.append("0.0").append(thirdPayAmt);
                                        } else if (thirdPayAmt.length() == 2) {
                                            result.append("0.").append(thirdPayAmt);
                                        } else {
                                            String intString = thirdPayAmt.substring(0, thirdPayAmt.length() - 2);
                                            for (int i = 1; i <= intString.length(); i++) {
                                                if ((i - 1) % 3 == 0 && i != 1) {
                                                    result.append(",");
                                                }
                                                result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
                                            }
                                            result.reverse().append(".").append(thirdPayAmt.substring(thirdPayAmt.length() - 2));
                                        }
                                        thirdPayAmt = result.toString();
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                    }
                                }
                                logger.info("威富通交易流水号serialNo:" + serialNo);
                                logger.info("威富通订单号orderNo:" + orderNo);
                                logger.info("威富通订单金额thirdPayAmt:" + thirdPayAmt);
                                if (!StringUtils.isEmpty(orderNo)) {
                                    JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
                                    thirdPayAmt = thirdPayAmt.replaceAll(",", "");
                                    String mystr = "orderNo=" + orderNo + "&payType=swiftpass&serialNo=" + serialNo + "&thirdPayAmt=" + thirdPayAmt;
                                    resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
                                    if (resultStr.containsKey("status")) {
                                        logger.info("威富通支付确认订单完成:" + orderNo);
                                        respString = "success";
//											flag = true;
                                    } else {
                                        respString = "fail";
//											respString = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[支付发生异常，请联系无忧保客服400-111-8900]]></return_msg></xml>";//支付成功，确认清单失败的处理
                                    }
                                }
                            }
                        }
                    }
                }
            }
//				if(flag) {
//					logger.info("微信支付确认订单完成通知微信成功" + orderNo);
//					respString  = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
//									orderNo = map.get("out_trade_no");
            //此处可以在添加相关处理业务，校验通知参数中的商户订单号out_trade_no和金额total_fee是否和商户业务系统的单号和金额是否一致，一致后方可更新数据库表中的记录。
//					respString = "success";
//					response.getWriter().write(respString);
//				}
            response.getWriter().write(respString);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}