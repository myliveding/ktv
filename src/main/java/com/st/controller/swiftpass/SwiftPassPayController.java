package com.st.controller.swiftpass;

import com.st.constant.Constant;
import com.st.controller.swiftpass.config.SwiftpassConfig;
import com.st.controller.swiftpass.util.JsonUtil;
import com.st.controller.swiftpass.util.MD5;
import com.st.controller.swiftpass.util.SignUtils;
import com.st.controller.swiftpass.util.XmlUtils;
import com.st.core.util.iputil.IPUtil;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@SuppressWarnings("deprecation")
@RequestMapping("/swiftpass/pay")
@Controller
public class SwiftPassPayController {

    private static final Logger logger = Logger.getLogger(SwiftPassPayController.class);
    private final static String version = "1.1";
    private final static String charset = "UTF-8";
    private final static String sign_type = "MD5";

    /**
     * <一句话功能简述>
     * <功能详细描述>支付请求
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public void pay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String res = null;
        String openid = (String) req.getSession().getAttribute("openid");
        String userId = (String) req.getSession().getAttribute("userId");
        logger.info("openid=" + openid);
        String orderNo = req.getParameter("orderNo");
        String leftAmtStr = req.getParameter("leftAmt");
        logger.info(orderNo+"威富通支付传入余额:"+leftAmtStr);
        double leftAmtD = 0.0;
        if (com.st.core.util.text.StringUtils.isNotEmpty(leftAmtStr)) {
            leftAmtD = Double.parseDouble(leftAmtStr);
            JSONObject balance = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURERBALANCE_BY_ORDER, "orderNo=" + orderNo));//取参保人可用余额
            double leftAmt = balance.getJSONObject("data").getDouble("leftAmt");
            logger.info("威富通支付获取余额:" + leftAmt);
            if (leftAmtD > leftAmt) {
                req.setAttribute("error", "余额信息有误");
                res= "error：余额信息有误";
            }
        }

        if (StringUtils.isEmpty(orderNo)) {
            req.setAttribute("error", "订单号不能为空");
            res = "error：订单号不能为空";
        }
        if (StringUtils.isEmpty(userId)) {
            req.setAttribute("error", "获取用户出错了");
            res =  "error：获取用户出错了";
        }
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String mystr = "policyHolderId=" + userId + "&orderNo=" + orderNo;
        try {
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INFO, mystr));
        } catch (Exception e) {
            logger.error("订单明细查询接口出错:" + e.getMessage(), e);
        }
        String productName = "无忧保订单-" + orderNo;
        String totalFee = "0";
        if (0 == resultStr.getInt("status")) {
            JSONObject message = JSONObject.fromObject(resultStr.getString("data"));
            double orderAmt = message.getDouble("orderAmt");
            String payAmt = new java.text.DecimalFormat("#0.00").format(orderAmt - leftAmtD);
            logger.info("ordermoney（元）:" + payAmt);
            String orderStatus = message.getString("orderStatus");
            if (!"10".equals(orderStatus)) {
                req.setAttribute("error", "请勿重复支付");
                res =  "error：请勿重复支付";
            }
            if (StringUtils.isEmpty(payAmt)) {
                payAmt = "0";
            } else {
                int index = payAmt.indexOf(".");
                int length = payAmt.length();
                Long amLong = 0l;
                if (index == -1) {
                    amLong = Long.valueOf(payAmt + "00");
                } else if (length - index >= 3) {
                    amLong = Long.valueOf((payAmt.substring(0, index + 3)).replace(".", ""));
                } else if (length - index == 2) {
                    amLong = Long.valueOf((payAmt.substring(0, index + 2)).replace(".", "") + 0);
                } else {
                    amLong = Long.valueOf((payAmt.substring(0, index + 1)).replace(".", "") + "00");
                }
                payAmt = amLong + "";
                if (Constant.ENVIROMENT.equals("test")) {
                    payAmt = "1";
                }
                logger.info("order_money（分）:" + payAmt);
            }
            totalFee = payAmt;
        } else {
            req.setAttribute("error", "获取订单详情出错了");
            res= "error：获取订单详情出错了";
        }
        logger.debug("支付请求...");
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
//        SortedMap<String, String> map = XmlUtils.getParameterMap(req);
        SortedMap<String, String> map=new TreeMap<String,String>();
//        orderNo=orderNo+"SWP"+(int)(Math.random()*10000);
        map.put("out_trade_no", orderNo+"_SWP"+(int)(Math.random()*10000));
        map.put("body", productName);
//        openid="on7TqvhimXBaltSx9oB2Sm8_5CP4";
        map.put("total_fee", totalFee);
        String ipAddress = IPUtil.toIpAddr(req);
        map.put("mch_create_ip", ipAddress);
        map.put("sub_openid", openid);
        map.put("sub_appid", Constant.APP_ID);
        map.put("service", "pay.weixin.jspay");
        map.put("version", version);
        map.put("charset", charset);
        map.put("sign_type", sign_type);
        map.put("mch_id", SwiftpassConfig.mch_id);
        //重复提交的时候直接查询本地的状态
       /* if(orderResult != null && orderResult.containsKey(map.get("out_trade_no"))){
            String status = "0".equals(orderResult.get(map.get("out_trade_no"))) ? "未支付" : "已支付";
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            resp.getWriter().write(status);
        }else{*/
        map.put("notify_url", SwiftpassConfig.notify_url);
        map.put("is_raw", "1");
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
        map.put("sign", sign);

        String reqUrl = SwiftpassConfig.req_url;
        logger.info("reqUrl：" + reqUrl);
        logger.info("reqParams:" + XmlUtils.parseXML(map));
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;

        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null&&res==null) {
                Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                logger.info("请求结果：" + res);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, SwiftpassConfig.key)) {
                        res = "验证签名不通过";
                    } else {
                        if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
                            String pay_info = resultMap.get("pay_info");
                            Map payInfo = JsonUtil.jsonToMap(pay_info);
                            String appId = (String) payInfo.get("appId");
                            String timeStamp = (String) payInfo.get("timeStamp");
                            String nonceStr = (String) payInfo.get("nonceStr");
                            String package1 = (String) payInfo.get("package");
                            String signType = (String) payInfo.get("signType");
                            String paySign = (String) payInfo.get("paySign");
                            req.setAttribute("timeStamp", timeStamp);
                            req.setAttribute("nonceStr", nonceStr);
                            req.setAttribute("_package", package1);
                            req.setAttribute("signType", signType);
                            req.setAttribute("paySign", paySign);
                            req.setAttribute("appId", appId);
                            //System.out.println("code_img_url"+code_img_url);
                            //req.setAttribute("code_img_url", code_img_url);
//                            req.setAttribute("orderId", map.get("out_trade_no"));
                            req.setAttribute("orderId", orderNo);
                            req.setAttribute("order_money", map.get("total_fee"));
                            req.setAttribute("body", map.get("body"));
                            req.setAttribute("pay_method", "swiftpass");
//                            req.getRequestDispatcher("/jsp/wechat/pay.jsp").forward(req, resp);
                            req.getRequestDispatcher("/jsp/swiftpass/swiftpay.jsp").forward(req, resp);
                        } else {
                            req.setAttribute("result", res);
                        }
                    }
                }
                if (resultMap.containsKey("err_code")){
                    res=resultMap.get("err_msg")+"，如有疑义请详询400-111-8900";
                }
            } else {
//                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        if (res.startsWith("<")) {
            resp.setHeader("Content-type", "text/xml;charset=UTF-8");
        } else {
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
        }
        resp.getWriter().write(res);
    }
}