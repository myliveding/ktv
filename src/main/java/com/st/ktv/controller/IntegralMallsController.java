package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
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
import java.io.PrintWriter;

/**
 * @Description 积分商城控制器
 * @FileName IntegralMallsController
 * @Author dingzr
 * @CreateTime 2017/6/27 22:25 六月
 */

@Controller
@RequestMapping("/mall")
public class IntegralMallsController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取积分商城
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoMalls")
    public String gotoMalls(HttpServletRequest request, HttpServletResponse response) {

        try {

            //默认礼品类（分类 1礼品类 2包厢类 3酒水类）
            String[] arr = new String[]{"cate" + 1};
            String mystr = "cate=" + 1;
            JSONObject mall = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INTEGRAL_MALLS, mystr, arr));
            if (0 == mall.getInt("error_code")) {
                request.setAttribute("malls", mall.get("result"));
            }

        } catch (Exception e) {
            logger.error("获取积分商城出错:" + e.getMessage(), e);
        }
        return "mall/integralmall";
    }

    /**
     * 获取积分商城的信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getCate")
    public @ResponseBody Object getCate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cateId = request.getParameter("cateId");
        JSONObject malls = null;
        try {

            //默认礼品类（分类 1礼品类 2包厢类 3酒水类）
            String[] arr = new String[]{"cate" + cateId};
            String mystr = "cate=" + cateId;
            malls = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INTEGRAL_MALLS, mystr, arr));

        } catch (Exception e) {
            logger.error("取积分商城的信息出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(malls);
        return null;
    }


    /**
     * 进入积分详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoMallDetail")
    public String gotoMallDetail(HttpServletRequest request, HttpServletResponse response) {

        try {
            String integralMallId = request.getParameter("integralMallId");
            String[] arr = new String[]{"integral_mall_id" + integralMallId};
            String mystr = "integral_mall_id=" + integralMallId;
            JSONObject mall = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INTEGRAL_MALLS_DETAIL, mystr, arr));
            if (0 == mall.getInt("error_code")) {
                request.setAttribute("info", mall.get("result"));
            }

        } catch (Exception e) {
            logger.error("进入积分详情出错:" + e.getMessage(), e);
        }
        return "mall/intergraldetail";
    }

}
