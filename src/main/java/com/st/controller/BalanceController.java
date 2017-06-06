package com.st.controller;

import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by shangguanguangbo on 2016/2/25.
 */
@Controller
@RequestMapping("/balance")
public class BalanceController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("getBalance")
    public @ResponseBody Object getBalance(HttpServletResponse response) throws IOException {

        HttpSession session = ContextHolderUtils.getSession();
        if (session.getAttribute("userId") == null) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }

        String userId = session.getAttribute("userId").toString();
        String param = "policyHolderId=" + userId;
        JSONObject resultStr;
//        resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BALANCE, param));
        resultStr=JSONObject.fromObject("{\"msg\":\"ok\",\"data\":{\"leftAmt\":0},\"status\":0}");
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(resultStr);

        return null;
    }
    /**
     * 根据订单号获取参保人余额
     * @param request
     * @return
     */
    @RequestMapping("getInsurerBalance")
    public @ResponseBody Object getInsurerBalance(HttpServletRequest request,HttpServletResponse response) throws IOException {

        HttpSession session = ContextHolderUtils.getSession();
        if (session.getAttribute("userId") == null) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String userId = session.getAttribute("userId").toString();
        String orderNo=request.getParameter("orderNo");
        String param = "policyHolderId=" + userId;
        JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_INSURERBALANCE_BY_ORDER, "orderNo=" +orderNo));//取参保人可用余额
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(resultStr);
        return null;
    }
}
