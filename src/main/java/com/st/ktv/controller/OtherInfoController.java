package com.st.ktv.controller;

import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @FileName OtherInfoController
 * @Author dingzr
 * @CreateTime 2017/6/11 21:39 六月
 */
@Controller
@RequestMapping("/other")
public class OtherInfoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取公司信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getCompanyInfo")
    public String getCompanyInfo(HttpServletRequest request, HttpServletResponse response) {

        JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.COMPANY_INFO, "", new String[]{}));
        } catch (Exception e) {
            logger.error("获取登录出错:" + e.getMessage(), e);
        }
        if (0 == result.getInt("error_code")) {
            request.setAttribute("company",result.get("result"));
        }
        return "other/about";
    }

    /**
     * 获取网站参数
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getWebParams")
    public String getWebParams(HttpServletRequest request, HttpServletResponse response) {

        JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.WEB_PARAMS, "", new String[]{}));
        } catch (Exception e) {
            logger.error("获取登录出错:" + e.getMessage(), e);
        }
        if (0 == result.getInt("error_code")) {
            request.setAttribute("webParams",result.get("result"));
        }
        return "my/myinsured";
    }

    /**
     * 获取招聘信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getCompanyRecruits")
    public String getCompanyRecruits(HttpServletRequest request, HttpServletResponse response) {

        JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.COMPANY_RECRUITS, "", new String[]{}));
            if(result.getInt("error_code") == 0){
                request.setAttribute("recruits",result.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取登录出错:" + e.getMessage(), e);
        }
        return "other/recruit";
    }

}
