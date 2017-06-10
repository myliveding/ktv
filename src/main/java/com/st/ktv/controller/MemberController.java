package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.entity.Store;
import com.st.ktv.service.MemberService;
import com.st.utils.DataUtil;
import com.st.utils.JoYoUtil;
import com.st.utils.PropertiesUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 直接访问域名跳转首页推送相关数据
     * @param request
     * @return object
     */
    @RequestMapping("/wechatLogin")
    public String getWeixintoIndex(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        String openid = request.getParameter("openid");
        String appid = request.getParameter("appid");

        try {
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
            if (DataUtil.isNotEmpty(openid)) {
                String res = memberService.wechatLogin(openid);
                    //判断是否绑定手机
//                    if ((isMobileBind == null) || ("".equals(isMobileBind)) || PropertiesUtils.findPropertiesKey("UNBIND_MOBILE_CODE", "config.properties").equals(isMobileBind)){
//                        return "account/bindmobile";
//                    }
                    return "redirect:/weixin/getweixin.do?name=index/index";
            }else{
                request.setAttribute("error", "请在微信中访问");
            }
        } catch (Exception e) {

        }finally{
        }
        return "redirect:/weixin/getweixin.do?name=index/index";
    }

}
