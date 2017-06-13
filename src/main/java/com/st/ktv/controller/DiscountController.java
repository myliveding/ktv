package com.st.ktv.controller;

import com.st.utils.DateUtil;
import com.st.utils.JoYoUtil;
import lombok.Data;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Description
 * @FileName DiscountController
 * @Author dingzr
 * @CreateTime 2017/6/13 21:24 六月
 */

@Controller
@RequestMapping("/discount")
public class DiscountController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取优惠活动列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getDiscountList")
    public String getDiscountList(HttpServletRequest request, HttpServletResponse response) {

        JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        try {
            result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.DISCOUNT_LIST, "", new String[]{}));
            if (0 == result.getInt("error_code")) {
                request.setAttribute("list",result.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取优惠活动出错:" + e.getMessage(), e);
        }
        return "discount/discount";
    }

    /**
     * 获取优惠活动列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getDiscountDetail")
    public String getDiscountDetail(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");
        try {
            String[] arr = new String[]{"id" + id};
            String mystr = "id=" + id;
            JSONObject result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.DISCOUNT_DETAIL, mystr, arr));
            if (0 == result.getInt("error_code")) {
                request.setAttribute("detail", result.get("result"));
                JSONObject data = JSONObject.fromObject(result.get("result"));
                if(data.containsKey("create_time")
                        && !"".equals(data.get("create_time"))
                        && !"0".equals(data.get("create_time"))){
                    request.setAttribute("time", DateUtil.getFormatByDateTime(data.getString("create_time"),"yyyy年MM月dd日"));
                }
            }
        } catch (Exception e) {
            logger.error("获取优惠活动出错:" + e.getMessage(), e);
        }
        return "discount/discountdetail";
    }

}
