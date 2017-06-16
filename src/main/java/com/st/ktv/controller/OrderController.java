package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.entity.WechatMember;
import com.st.ktv.service.MemberService;
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

/**
 * @Description
 * @FileName OrderController
 * @Author dingzr
 * @CreateTime 2017/6/15 23:04 六月
 */

@Controller
@RequestMapping("/order")
public class OrderController {

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
     * 进入包厢详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoPackages")
    public String gotoPackages(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
        openidObj = "1111111";
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
                    request.setAttribute("packages", room.get("result"));
                }
            } catch (Exception e) {
                logger.error("进入套餐选择出错:" + e.getMessage(), e);
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "order/select";
    }

}
