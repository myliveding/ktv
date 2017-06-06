package com.st.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.st.controller.alipay.config.AlipayConfig;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by swf on 2017/4/21.
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController {
    private static final Logger logger = Logger.getLogger(AlipayController.class);
    /**
     * 未付款账单取消
     *
     * @param request
     * @return
     */
    @RequestMapping("/orderCancel")
    public
    @ResponseBody
    Object orderCancel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":0,\"msg\":\"OK\"}");
        //商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
        //商户订单号，和支付宝交易号二选一
        String out_trade_no = new String(request.getParameter("orderNo").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号，和商户订单号二选一
//        String trade_no = new String(request.getParameter("WIDtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        String trade_no="";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeCloseRequest alipay_request=new AlipayTradeCloseRequest();

        AlipayTradeCloseModel model =new AlipayTradeCloseModel();
        model.setOutTradeNo(out_trade_no);
        model.setTradeNo(trade_no);
        alipay_request.setBizModel(model);
        AlipayTradeCloseResponse alipay_response= null;
        try {
            alipay_response = client.execute(alipay_request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.info("取消支付宝订单返回结果"+alipay_response.getBody());

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultStr);
        return null;
    }
}
