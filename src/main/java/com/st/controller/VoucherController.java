package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.date.DateUtil;
import com.st.core.util.iputil.IPUtil;
import com.st.core.util.text.StringUtils;
import com.st.service.WeixinAPIService;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    private Logger logger=LoggerFactory.getLogger(this.getClass());
    @Resource
    private WeixinAPIService weixinAPIService;
    /**
     * 查找用户所有拥有的代金券
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/findUserVouchers")
    public @ResponseBody Object findUserVouchers(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject !=null ){
            userId = userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String voucherStatus=request.getParameter("voucherStatus");
        String sortField=request.getParameter("sortField");
        String pageNow=request.getParameter("pageNow");
        String pageSize=request.getParameter("pageSize");
        String userType=request.getParameter("userType")==null?"":request.getParameter("userType");
        String productTypes=request.getParameter("productTypes")==null?"":request.getParameter("productTypes");
        String orderAmtv=request.getParameter("orderAmtv")==null?"":request.getParameter("orderAmtv");
        String sortType=request.getParameter("sortType")==null?"desc":request.getParameter("sortType");
        
        try {
            JSONObject str = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_PACKAGES_URL, "", null));
            JSONArray packageArr=str.getJSONArray("data");
            HashMap<String, String> packageMap=new HashMap<String, String>();//公积金续费月份
            for(int i=0;i<packageArr.size();i++){
                packageMap.put((packageArr.getJSONObject(i)).getString("product_id"), (packageArr.getJSONObject(i)).getString("name"));
            }
            StringBuffer mystr = new StringBuffer("userId="+userId+"&sortType="+sortType+"&userType="+userType+"&productTypes="+productTypes+"&orderAmt="+orderAmtv);
            if(StringUtils.isNotEmpty(voucherStatus)){
                mystr.append("&voucherStatus="+voucherStatus);
            }
            if(StringUtils.isNotEmpty(sortField)){
                mystr.append("&sortField="+sortField);
            }
            if(StringUtils.isNotEmpty(pageNow)){
                mystr.append("&pageNow="+pageNow);
            }
            if(StringUtils.isNotEmpty(pageSize)){
                mystr.append("&pageSize="+pageSize);
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_FIND_USER, mystr.toString()));
            JSONArray records=resultObject.getJSONObject("data").getJSONArray("records");
            if(records.size()>0){
                for(int i=0;i<records.size();i++){
                    String voucherUseRuleExt="";
                    String[] person = new String[]{"","个人用户","个体工商户用户","企业客户用户"};
                    JSONArray voucherUseRuleExtList=records.getJSONObject(i).getJSONArray("voucherUseRuleExtList");
                    if(voucherUseRuleExtList.size()>0){//组装代金券使用门槛
                        for(int j=0;j<voucherUseRuleExtList.size();j++){
                            if(voucherUseRuleExtList.getJSONObject(j).getString("entityCode").equals("appointPerson")){
                                if(voucherUseRuleExt.indexOf("用户")>-1){
                                    voucherUseRuleExt+="/";
                                }
                                voucherUseRuleExt+=person[voucherUseRuleExtList.getJSONObject(j).getInt("entityValue")];
                            }
                            if(voucherUseRuleExtList.getJSONObject(j).getString("entityCode").equals("appointProduct")){
                                if(voucherUseRuleExt.indexOf("个月")<0){
                                    voucherUseRuleExt+="仅购买"+packageMap.get(voucherUseRuleExtList.getJSONObject(j).getString("entityValue"));
                                }else{
                                    voucherUseRuleExt+="/"+packageMap.get(voucherUseRuleExtList.getJSONObject(j).getString("entityValue"));
                                }
                            }
                            
                            if(voucherUseRuleExtList.getJSONObject(j).getString("entityCode").equals("giveNum")){
                                if(voucherUseRuleExt.equals("")){
                                    voucherUseRuleExt+="订单服务费金额满" + voucherUseRuleExtList.getJSONObject(j).getString("entityValue") + "元";
                                }else{
                                    voucherUseRuleExt+="且订单服务费金额满" + voucherUseRuleExtList.getJSONObject(j).getString("entityValue") + "元";
                                }
                            }
                        }
                    }
                    if(voucherUseRuleExt.equals("")){
                        voucherUseRuleExt="<p>• 无使用门槛</p>";
                    }else{
                        voucherUseRuleExt="<p>• " +voucherUseRuleExt.replaceAll("（个人）", "") + "可用</p>";
                    }
                    records.getJSONObject(i).put("voucherUseRuleExt", voucherUseRuleExt);
                }
            }
            resultObject.getJSONObject("data").put("records", records);
        } catch (Exception e) {
            logger.error(" 查找用户所有拥有的代金券出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getJSONObject("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    /**
     * 获取有效活动的描述说明
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/getActivityRemark")
    public @ResponseBody Object getActivityRemark(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        logger.info("getActivityRemark");
        String triggerSence=request.getParameter("triggerSence");
        try {
            StringBuffer mystr = new StringBuffer();
            if(StringUtils.isNotEmpty(triggerSence)){
                mystr.append("triggerSence="+triggerSence);//1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ACTIVITY_REMARK, mystr.toString()));
        } catch (Exception e) {
            logger.error(" 查找用户所有拥有的代金券出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getString("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    /**
     * 判断是否享有活动
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/isShareActivity")
   public @ResponseBody Object isShareActivity(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        String triggerSence=request.getParameter("triggerSence");//触发场景 1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
        String busiOperate=request.getParameter("busiOperate");//业务动作 0 注册  1下单  2其他
        String orderNo=request.getParameter("orderNo");//订单号
        String orderTime=request.getParameter("orderTime");//下单时间
        try {
            StringBuffer mystr = new StringBuffer("userId="+userId+"&triggerSence="+triggerSence);
            if(StringUtils.isNotEmpty(orderNo)){
                mystr.append("&orderNo="+orderNo);
            }
            if(StringUtils.isNotEmpty(orderTime)){
                mystr.append("&orderTime="+orderTime);
            }
            if(StringUtils.isNotEmpty(busiOperate)){
                mystr.append("&busiOperate="+busiOperate);
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_SHARE_ACTIVITY, mystr.toString()));
        } catch (Exception e) {
            logger.error(" 判断是否享有活动出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getString("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    /**
     * 判断是否享受过活动或者领用过代金券
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/isHadShare")
    public @ResponseBody Object isHadShare(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject !=null ){
            userId = userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String actId=request.getParameter("actId");//活动id
        String voucherId=request.getParameter("voucherId");//代金券id
        String orderNo=request.getParameter("orderNo");//订单号
        String sponsor=request.getParameter("sponsor");//发起人id
        String enjoyer=request.getParameter("enjoyer");//享受人id
        String mobile=request.getParameter("mobile");//手机号
        try {
            StringBuffer mystr = new StringBuffer();
            if(StringUtils.isNotEmpty(actId)){
                mystr.append("&actId="+actId);
            }
            if(StringUtils.isNotEmpty(openid)){
                mystr.append("&openId="+openid);
            }
            if(StringUtils.isNotEmpty(orderNo)){
                mystr.append("&orderNo="+orderNo);
            }
            if(StringUtils.isNotEmpty(voucherId)){
                mystr.append("&voucherId="+voucherId);
            }
            if(StringUtils.isNotEmpty(sponsor)){
                mystr.append("&sponsor="+sponsor);
            }
            if(StringUtils.isNotEmpty(enjoyer)){
                mystr.append("&enjoyer="+enjoyer);
            }
            if(StringUtils.isNotEmpty(mobile)){
                mystr.append("&mobile="+mobile);
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.VOUCHERS_ISHADSHARE, mystr.toString()));
        } catch (Exception e) {
            logger.error(" 判断是否享有活动出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getString("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    /**
     * 领取代金券
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/receiveVoucher")
    public @ResponseBody Object receiveVoucher(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject !=null ){
            userId = userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String ipAddress=IPUtil.toIpAddr(request);
        String triggerSence=request.getParameter("triggerSence");//触发场景  发放场景:1:会员注册成功，2:个体工商户认证通过，3:下单成功，4:推荐好友
        String actId=request.getParameter("actId");//活动id
        String voucherId=request.getParameter("voucherId");//代金券id
        String orderNo=request.getParameter("orderNo");//订单号
        String sponsor=request.getParameter("sponsor");//发起人id
        String enjoyer=request.getParameter("enjoyer");//享受人id
        String mobile=request.getParameter("mobile");
        try {
            StringBuffer mystr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId+"&triggerSence="+triggerSence);
            if(StringUtils.isNotEmpty(actId)){
                mystr.append("&actId="+actId);
            }
            if(StringUtils.isNotEmpty(openid)){
                mystr.append("&openid="+openid);
            }
            if(StringUtils.isNotEmpty(orderNo)){
                mystr.append("&orderNo="+orderNo);
            }
            if(StringUtils.isNotEmpty(voucherId)){
                mystr.append("&voucherId="+voucherId);
            }
            if(StringUtils.isNotEmpty(sponsor)){
                mystr.append("&sponsor="+sponsor);
            }
            if(StringUtils.isNotEmpty(enjoyer)){
                mystr.append("&enjoyer="+enjoyer);
            }
            if(StringUtils.isNotEmpty(mobile)){
                mystr.append("&mobile="+mobile);
            }
            String isReceive="";
            resultObject = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_RECEIVE, mystr.toString()));
            if(resultObject.getString("errcode").equals("0")){
                String coupons="";
                if(resultObject.getString("data")!=null&&!resultObject.getString("data").equals("null")){
                    coupons=resultObject.getJSONObject("data").getString("bills");
                    isReceive=resultObject.getJSONObject("data").getString("isReceive");
                }
                if(!isReceive.equals("Y")){
                    String coupon=resultObject.getJSONObject("data").getString("bills")+"元";
                    String expDate=DateUtil.convertToDate(resultObject.getJSONObject("data").getInt("endDate"));
                    if(StringUtils.isNotEmpty(openid)&&StringUtils.isNotEmpty(coupons)){
                        weixinAPIService.sendTemplateMessageByType("3","",coupon,expDate,"","","",openid,"", "");
                    }
                    if(StringUtils.isNotEmpty(userId)){
                        String[] arrs;
                        StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type=5");
                        List<String> list=new ArrayList<String>();
                        list.add("member_id"+userId);
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
                if(StringUtils.isNotEmpty(userId)){
                    String code =resultObject.getString("errcode");
                    String msg[]=resultObject.getString("errmsg").split(",");
                    String msgType="";
                    String reason="";// 原因
//                  String url=Constant.URL+"/huodong/";
                    String activityTitle=resultObject.getString("errmsg").split(",")[0];//活动标题
                    String number="";//每人限领张数
                    if(code.equals("20035")){
                        msgType="2";
                    }else if(code.equals("20036")){
                        msgType="3";
                    }else if(code.equals("20037")){
                        msgType="4";
                        number=resultObject.getString("errmsg").split(",")[1];
                    }
                    String[] arrs;
                    StringBuffer mystrs = new StringBuffer("member_id="+userId+"&type="+msgType);
                    List<String> list=new ArrayList<String>();
                    list.add("member_id"+userId);
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
        } catch (Exception e) {
            logger.error(" 判断是否享有活动出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getJSONObject("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }

    /**
     * 使用代金券
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/useVoucher")
    public @ResponseBody Object useVoucher(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";
        Object openidObject =  session.getAttribute("openid");
        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject !=null ){
            userId = userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }

        String ipAddress=IPUtil.toIpAddr(request); 
//        String operateUser=request.getParameter("operateUser");//操作者账号
        String id=request.getParameter("id");//代金券id
        String coupons=request.getParameter("coupons");//实际抵扣金额
        String userType=request.getParameter("userType");//用户类型
        String productType=request.getParameter("productType");//产品类型
        String orderAmt=request.getParameter("orderAmt");//订单金额
        try {
            StringBuffer mystr = new StringBuffer("ipAddress="+ipAddress+"&operateUser="+userId);
            if(StringUtils.isNotEmpty(id)){
                mystr.append("&id="+id);
            }
            if(StringUtils.isNotEmpty(coupons)){
                mystr.append("&coupons="+coupons);
            }
            if(StringUtils.isNotEmpty(userType)){
                mystr.append("&userType="+userType);
            }
            if(StringUtils.isNotEmpty(productType)){
                mystr.append("&productType="+productType);
            }
            if(StringUtils.isNotEmpty(orderAmt)){
                mystr.append("&orderAmt="+orderAmt);
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_USE, mystr.toString()));
        } catch (Exception e) {
            logger.error(" 判断是否享有活动出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getString("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
    /**
     * 手动兑换代金券
     * @param request
     * @param response
     * @return JSON
     * @author swf
     * @throws IOException
     */
    @RequestMapping("/exchangeVoucher")
    public @ResponseBody Object exchangeVoucher(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        HttpSession session = ContextHolderUtils.getSession();
        String openid="";

        Object openidObject =  session.getAttribute("openid");

        if(openidObject!=null&&!"".equals(openidObject)){
            openid= openidObject.toString();
        }
        String operateUser="";//操作者账号
        Object nameObject=session.getAttribute("memberName");
        if(nameObject!=null&&!"".equals(nameObject)){
            operateUser= nameObject.toString();
        }
        String userId="";
        Object userIdObject =  session.getAttribute("userId");
        if(userIdObject !=null ){
            userId = userIdObject.toString();
        }else{
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String ipAddress=IPUtil.toIpAddr(request);
        String voucherNo=request.getParameter("voucherNo");//代金券id
        try {
            StringBuffer mystr = new StringBuffer("voucherNo="+voucherNo+"&userId="+userId);
            if(StringUtils.isNotEmpty(ipAddress)){
                mystr.append("&ipAddress="+ipAddress);
            }
            if(StringUtils.isNotEmpty(operateUser)){
                mystr.append("&operateUser="+operateUser);
            }
            resultObject = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.VOUCHERS_EXCHANGE, mystr.toString()));
        } catch (Exception e) {
            logger.error(" 判断是否享有活动出错：" + e.getMessage(), e);
        }
        JSONObject jsonObj = new JSONObject();
        if(resultObject.getString("errcode").equals("0")){
            jsonObj.put("status", 0);
            jsonObj.put("data",resultObject.getString("data"));
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", resultObject.getString("errmsg"));
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(jsonObj);
        return null;
    }
}
