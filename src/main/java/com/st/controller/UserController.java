package com.st.controller;

import com.st.constant.Constant;
import com.st.javabean.pojo.wxtour.TBAttribute;
import com.st.javabean.pojo.wxtour.User;
import com.st.service.TBAttributeService;
import com.st.service.UserService;
import com.st.service.impl.ArticleRedisHandleServiceImpl;
import com.st.utils.ContextHolderUtils;
import com.st.utils.Md5Tool;
import com.st.utils.PropertiesUtils;
import com.st.utils.PseudoStaticUrl;
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
import java.util.List;

/**
 * 
*   
* 类描述：  用户
* 创建人：wuhao
* 创建时间：2015-6-17
*
 */
@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private UserService userService;
	
	@Resource
	private TBAttributeService attributeService;
	
	@Resource
	private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
    
	@RequestMapping("/register")
	public String register(HttpServletRequest request) {
		try {
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		return "user/register";
	}
	/**
	 * 检查用户是否登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkuser")
	public @ResponseBody Object demand(HttpServletRequest request){
		HttpSession session=ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (userIdObject==null||"".equals(userIdObject)) {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}else{
			return JSONObject.fromObject("{\"status\":0,\"msg\":\"已登录\"}");
		}
	}
	
	/**
	 * 调用接口去更新伪静态地址
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/updateurl")
	public @ResponseBody Object updateStaticurl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String key=request.getParameter("key");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"出错了\"}");
		try{
			long lastTime = Integer.valueOf(PropertiesUtils.findPropertiesKey("UPDATE_TIME", Constant.SEO_FILE_NAME));
			long nowTime = System.currentTimeMillis()/1000;
			if((nowTime - lastTime) > 
				Integer.valueOf(PropertiesUtils.findPropertiesKey("INTERVAL_TIME", Constant.SEO_FILE_NAME))){
				if(null != key && !"".equals(key)){
					List<User> resList = userService.selectByUsername("admin");
					if(resList.size() == 1){
						if(resList.get(0).getPassword().equals(Md5Tool.getMd5(key))){
							PseudoStaticUrl.updateInstance();
							resultStr = JSONObject.fromObject("{\"status\":0,\"msg\":\"OK\"}");
							//把配置文件中的更新时间进行修改
							PropertiesUtils.modifyProperties("UPDATE_TIME", String.valueOf(nowTime), Constant.SEO_FILE_NAME);
						}else{
							logger.info("口令不正确");
							resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"口令不正确\"}");
						}
					}else{
						logger.info("更新缓存,数据库不正确,口令的记录不存在");
						resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"数据库不正确,口令不存在\"}");
					}
				}else{
					logger.info("更新缓存,获取到的口令为空,如下：" + key);
					resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"口令不能为空\"}");
				}
			}else{
				logger.info("手动更新间隔时间太短");
				resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"手动更新间隔时间太短\"}");
			}
		}catch (Exception e) {
			logger.error("更新缓存出错！" + e.getMessage(),e);
		}
		response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
	
	//<!-- start 暂时不用了，或许以后会用 end -->
	/**
	 * 调用接口去获取用户数量
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/usernum")
	public @ResponseBody Object getUserNum(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"出错了\"}");
		try{
			TBAttribute res = attributeService.selectByKey("userNum");
			if(null != res && null != res.getValue()){
				resultStr = JSONObject.fromObject("{\"status\":0,\"data\":\"" + res.getValue() + "\"}");
			}else{
				logger.info("获取到的对象为空：" + res);
			}
		}catch (Exception e) {
			logger.info("调用接口去获取用户数量！" + e.getMessage());
		}
		
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
	
	/**
	 * 删除Radis记录
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/delrediskey")
	public @ResponseBody Object delRedisKey(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":-1,\"msg\":\"出错了\"}");
		try{
			String key = request.getParameter("key");
			String keyValue = request.getParameter("keyValue");
			System.err.println(keyValue);
			if(null != key && !"".equals(key)){
				List<User> resList = userService.selectByUsername("admin");
				if(resList.size() == 1){
					if(resList.get(0).getPassword().equals(Md5Tool.getMd5(key))){
						if(!"".equals(keyValue)){
							articleRedisHandleServiceImpl.delete(keyValue);
							resultStr = JSONObject.fromObject("{\"status\":0,\"msg\":\"OK\"}");
						}else{
							logger.info("获取到的Radis的关键字为空：" + keyValue);
						}
					}else{
						logger.info("口令不正确");
						resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"口令不正确\"}");
					}
				}else{
					logger.info("删除Radis记录,数据库不正确,口令的记录不存在");
					resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"数据库不正确,口令不存在\"}");
				}
			}else{
				logger.info("删除Radis记录,获取到的口令为空,如下：" + key);
				resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"口令不能为空\"}");
			}
			
		}catch (Exception e) {
			logger.error("调用接口去删除Key出错" + e.getMessage(),e);
		}
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
	}
	
}
