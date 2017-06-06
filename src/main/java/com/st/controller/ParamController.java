package com.st.controller;

import com.st.core.util.text.StringUtils;
import com.st.service.impl.ArticleRedisHandleServiceImpl;
import com.st.utils.ContextHolderUtils;
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

/**
 * Created by shangguanguangbo on 2016/2/25.
 */
@Controller
@RequestMapping("/param")
public class ParamController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
    /** 
     * @Title: getParamByCode 
     * @Description: 获取参数对象
     * @param request
     * @return 
     */
    @RequestMapping("getParamByCode")
    public @ResponseBody Object getParamByCode(HttpServletRequest request,HttpServletResponse response) throws IOException{

        HttpSession session = ContextHolderUtils.getSession();
        if (session.getAttribute("userId") == null) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }

        String code = request.getParameter("code");
        String param = "code=" + code;
        JSONObject resultStr= JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_PARAM_CODE, param));
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(resultStr);
        return null;
    }

    /** 
     * @Title: getParamByType 
     * @Description: 获取参数列表
     * @return    
     */
    @RequestMapping("getParamByType")
    public @ResponseBody Object getParamByType(HttpServletRequest request,HttpServletResponse response) throws IOException{
        HttpSession session = ContextHolderUtils.getSession();
        if (session.getAttribute("userId") == null) {
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        String type = request.getParameter("type");
        String param = "type=" + type;
        JSONObject resultStr=JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");

        if (StringUtils.isNotEmpty(type)&&"payMethod".equals(type)){//支付方式存入redis缓存
            StringBuffer key = new StringBuffer("wyb_wechat_").append(type);
            try {
                resultStr = articleRedisHandleServiceImpl.read(key.toString());
                logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
                if(resultStr == null){
                    resultStr= JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_PARAM_TYPE, param));
                    if(0 == resultStr.getInt("status") && resultStr.has("data")){
                        String message = resultStr.getString("data");
                        if(message != null){
                            articleRedisHandleServiceImpl.save(key.toString(),resultStr,0);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
        }else {
            resultStr= JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_PARAM_TYPE, param));
        }
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(resultStr);
        return null;
    }
}
