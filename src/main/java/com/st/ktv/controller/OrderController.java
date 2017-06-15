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
 * @FileName OrderController
 * @Author dingzr
 * @CreateTime 2017/6/15 23:04 六月
 */

@Controller
@RequestMapping("/order")
public class OrderController {

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
            String[] arr = new String[]{"iid" + iid};
            String mystr = "iid=" + iid;
            JSONObject goods = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ROOM_DETAIL, mystr, arr));
            if (0 == goods.getInt("error_code")) {
                request.setAttribute("roomInfo", goods.get("result"));
            }

        } catch (Exception e) {
            logger.error("进入选择包厢详情出错:" + e.getMessage(), e);
        }
        return "order/roominfo";
    }

}
