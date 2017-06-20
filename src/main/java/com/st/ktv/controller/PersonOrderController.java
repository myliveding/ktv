package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.entity.WechatMember;
import com.st.ktv.service.MemberService;
import com.st.utils.Constant;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description
 * @FileName OrderController
 * @Author dingzr
 * @CreateTime 2017/6/15 23:04 六月
 */

@Controller
@RequestMapping("/personorder")
public class PersonOrderController {

    @Resource
    MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 进入包厢详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoRoomInfo")
    public String gotoRoomInfo(HttpServletRequest request, HttpServletResponse response) {
        String iid = request.getParameter("iid");
        try {
            //获取包厢详情
            JSONObject goods = getRoomInfo(iid);
            if (0 == goods.getInt("error_code")) {
                request.setAttribute("roomInfo", goods.get("result"));
            }
        } catch (Exception e) {
            logger.error("进入选择包厢详情出错:" + e.getMessage(), e);
        }
        return "order/roominfo";
    }

    /**
     * 获取包厢详情
     * @param iid
     * @return
     */
    private JSONObject getRoomInfo(String iid){

        String[] arr = new String[]{"iid" + iid};
        String mystr = "iid=" + iid;
        return JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ROOM_DETAIL, mystr, arr));
    }

    /**
     * 进入套餐选择
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoPackages")
    public String gotoPackages(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
        if ( !"".equals(openidObj) && openidObj != null) {
            String iid = request.getParameter("iid");
            try {
                //用户信息
                WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
                if(null != member){
                    System.err.println(member.getMobile());
                    request.setAttribute("mobile", member.getMobile());
                }

                //获取包厢详情
                request.setAttribute("iid", iid);
                String roomTypeId = "";
                JSONObject room = getRoomInfo(iid);
                if (0 == room.getInt("error_code")) {
                    request.setAttribute("roomInfo", room.get("result"));
                    JSONObject data = JSONObject.fromObject(room.get("result"));
                    roomTypeId = data.getString("room_type_id");
                }

                //套餐选择
                String[] arr = new String[]{"room_type_id" + roomTypeId};
                String mystr = "room_type_id=" + roomTypeId;
                JSONObject packages = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.GET_PACKAGES, mystr, arr));
                if(0 == packages.getInt("error_code")){
                    request.setAttribute("packages", packages.get("result"));
                }
            } catch (Exception e) {
                logger.error("进入套餐选择出错:" + e.getMessage(), e);
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/select";
    }

    /**
     * 去支付页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoPay")
    public String gotoPay(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "11111111";
        if ( !"".equals(openidObj) && openidObj != null) {
            String iid = request.getParameter("iid");
            String packageId = request.getParameter("packageId");
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                request.setAttribute("memberId", memberId);
            }
            try {
                //创建订单并跳转到支付页面
                String[] arr = new String[]{"member_id" + memberId, "iid" + iid, "package_id" + packageId};
                String mystr = "member_id=" + memberId + "&iid=" + iid + "&package_id=" + packageId;
                JSONObject packages = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CREATRE_ORDER, mystr, arr));
                if(0 == packages.getInt("error_code")){
                    request.setAttribute("orderId", packages.get("order_id"));
                    request.setAttribute("money", packages.get("money"));
                }
            } catch (Exception e) {
                logger.error("去支付页面出错:" + e.getMessage(), e);
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/pay";
    }

    /**
     * 去支付页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoPayForList")
    public String gotoPayForList(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "11111111";
        if ( !"".equals(openidObj) && openidObj != null) {
            String orderId = request.getParameter("orderId");
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                request.setAttribute("memberId", memberId);
            }
            try {
                //创建订单并跳转到支付页面
                String[] arr = new String[]{"member_id" + memberId, "order_id" + orderId};
                String mystr = "member_id=" + memberId + "&order_id=" + orderId;
                JSONObject order = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_DETAIL, mystr, arr));
                if(0 == order.getInt("error_code")){
                    JSONObject data = JSONObject.fromObject(order.get("result"));
                    request.setAttribute("orderId", data.get("order_id"));
                    request.setAttribute("money", data.get("money"));
                }
            } catch (Exception e) {
                logger.error("去支付页面出错:" + e.getMessage(), e);
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/pay";
    }


    /**
     * 获取我的订单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getOrderList")
    public String getOrderList(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj = session.getAttribute("openid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
        if ( !"".equals(openidObj) && openidObj != null) {
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                request.setAttribute("memberId", memberId);
            }
            try {
                //获取我的订单
                String[] arr = new String[]{"member_id" + memberId};
                String mystr = "member_id=" + memberId;
                JSONObject orders = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_LIST, mystr, arr));
                if(0 == orders.getInt("error_code")){
                    request.setAttribute("orders", orders.get("result"));
                }
            } catch (Exception e) {
                logger.error("获取我的订单列表出错:" + e.getMessage(), e);
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/myorder";
    }

    /**
     * 订单详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getOrderDetail")
    public String getOrderDetail(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "11111111";
        if ( !"".equals(openidObj) && openidObj != null) {
            String orderId = request.getParameter("orderId");
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                request.setAttribute("member", member);
            }

            JSONObject order = getOrderDetail(memberId, orderId);
            if(0 == order.getInt("error_code")){
                request.setAttribute("order", order.get("result"));
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/myorderdetail";
    }


    private JSONObject getOrderDetail(Integer memberId, String orderId){
        JSONObject order = null;
        try {
            //创建订单并跳转到支付页面
            String[] arr = new String[]{"member_id" + memberId, "order_id" + orderId};
            String mystr = "member_id=" + memberId + "&order_id=" + orderId;
            order = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_DETAIL, mystr, arr));
        } catch (Exception e) {
            logger.error("去订单详情出错:" + e.getMessage(), e);
        }
        return order;
    }

    /**
     * 订单取消或者申请退款
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cancleOrder")
    public String cancleOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{

        JSONObject resultObject = JSONObject.fromObject("{\"status\":0,\"msg\":\"成功\"}");
        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "11111111";
        if ( !"".equals(openidObj) && openidObj != null) {
            String orderId = request.getParameter("orderId");
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                JSONObject order = getOrderDetail(memberId, orderId);
                if(null != order){
                    JSONObject info = JSONObject.fromObject(order.get("result"));
                    //订单状态 1未支付 2已支付 3已确认消费 4取消预订 5系统取消6待退款 7已退款
                    String[] arr = new String[]{"member_id" + memberId, "order_id" + orderId};
                    String mystr = "member_id=" + memberId + "&order_id=" + orderId;
                    String status = info.getString("order_status");
                    if("1".equals(status) || "2".equals(status)){
                        if("1".equals(status)){
                            //取消预定
                            info = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CANCLE_ORDER, mystr, arr));
                        }else if("2".equals(status)){
                            //退款
                            info = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ORDER_REFUND, mystr, arr));
                        }
                        if(0 == info.getInt("error_code")){
                        }else{
                            resultObject = JSONObject.fromObject("{\"status\":1,\"msg\": " + info.getString("msg") + "}");
                        }
                    }else{
                        resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"不合法的申请\"}");
                    }
                }
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
            resultObject = JSONObject.fromObject("{\"status\":1,\"msg\":\"请在微信中访问\"}");
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultObject);
        return null;
    }

    /**
     * 确认开机
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/confirmBoot")
    public String confirmBoot(HttpServletRequest request, HttpServletResponse response) throws IOException{

        JSONObject resultObject = JSONObject.fromObject("{\"error_code\":0,\"msg\":\"成功\"}");
        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
        if ( !"".equals(openidObj) && openidObj != null) {
            String orderId = request.getParameter("orderId");
            Integer memberId = 0;
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member){
                memberId = member.getId();
                //订单状态 1未支付 2已支付 3已确认消费 4取消预订 5系统取消6待退款 7已退款
                String[] arr = new String[]{"member_id" + memberId, "order_id" + orderId};
                String mystr = "member_id=" + memberId + "&order_id=" + orderId;
                resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.CONFIRM_BOOT, mystr, arr));
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
            resultObject = JSONObject.fromObject("{\"error_code\":1,\"msg\":\"请在微信中访问\"}");
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultObject);
        return null;
    }

}
