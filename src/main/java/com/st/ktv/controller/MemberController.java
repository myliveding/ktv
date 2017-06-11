package com.st.ktv.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.st.core.ContextHolderUtils;
import com.st.ktv.controller.alipay.config.AlipayConfig;
import com.st.ktv.entity.Store;
import com.st.ktv.entity.WechatMember;
import com.st.ktv.service.MemberService;
import com.st.utils.Constant;
import com.st.utils.DataUtil;
import com.st.utils.JoYoUtil;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @FileName MemberController
 * @Author dingzr
 * @CreateTime 2017/6/10 14:23 六月
 */

@Controller
@RequestMapping("/member")
public class MemberController {

    @Resource
    MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 直接访问域名跳转首页推送相关数据
     * @param request
     * @return object
     */
    @RequestMapping("/gotoIndexDomain")
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
     * 跳转到首页
     * @param request
     * @return
     */
    @RequestMapping("/gotoIndex")
    public String gotoindex(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "index";
    }

    /**
     * 跳转到积分兑换页面
     * @param request
     * @return
     */
    @RequestMapping("/gotoIntegralMall")
    public String gotoIntegralMall(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        //去获取积分的数据库集合并返回给页面  礼品、包厢、酒水 分区
//        request.setAttribute("storeList",list);
//        request.setAttribute("storeList",list);
//        request.setAttribute("storeList",list);
        return "redirect:"+ Constant.URL + "my/integralmall";
    }

    /**
     * 跳转到在线超市
     * @param request
     * @return
     */
    @RequestMapping("/gotoShop")
    public String gotoShop(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        //去获取数据库集合并返回给页面  超市
//        request.setAttribute("storeList",list);
//        request.setAttribute("storeList",list);
//        request.setAttribute("storeList",list);
        return "redirect:"+ Constant.URL + "shop/shopnowing";
    }

    /**
     * 跳转到优惠活动
     * @param request
     * @return
     */
    @RequestMapping("/gotoDiscount")
    public String gotoDiscount(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "discount/discount";
    }

    /**
     * 跳转到佣金提现
     * @param request
     * @return
     */
    @RequestMapping("/gotoCommission")
    public String gotoCommission(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "commission/commission";
    }

    /**
     * 跳转到我的预定订单
     * @param request
     * @return
     */
    @RequestMapping("/gotoMyOrder")
    public String gotoMyOrder(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "order/myorder";
    }

    /**
     * 进入用户中心
     * @param request
     * @return object
     */
    @RequestMapping("/gotoUserCenter")
    public @ResponseBody String gotoUserCenter(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
        Object appidObj =  session.getAttribute("appid");

        if ( !"".equals(openidObj) && openidObj != null && !"".equals(appidObj) && appidObj != null) {
            memberService.checkLogin(openidObj.toString(), appidObj.toString());
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            request.setAttribute("member",member);
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:"+ Constant.URL + "my/usercenter";
    }

    /**
     * 进入错误提示页面
     * @param request
     * @return
     */
    @RequestMapping("/gotoErrorMsg")
    public String gotoErrorMsg(HttpServletRequest request) {
        request.setAttribute("msg", "请重新登录");
        return "other/code";
    }

    /**
     * 校验保存和更新手机号码
     * @param request
     * @return
     */
    @RequestMapping("/checkAndUpdateMobile")
    public @ResponseBody Object checkAndUpdateMobile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String phone = request.getParameter("mobile");//手机号码
        String openid = request.getParameter("openid");//openid
        String type = request.getParameter("type");//校验保存和更新手机号码
        JSONObject resultObject = memberService.checkAndUpdateMobile(openid, phone, type);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultObject);
        return null;
    }

    /**
     * 进入用户设置
     * @param request
     * @return object
     */
    @RequestMapping("/gotoUserSet")
    public @ResponseBody String gotoUserSet(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
        Object appidObj =  session.getAttribute("appid");

        if ( !"".equals(openidObj) && openidObj != null && !"".equals(appidObj) && appidObj != null) {
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            request.setAttribute("member",member);
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "redirect:my/edit";
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
                JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendPost("", mystr));
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

}
