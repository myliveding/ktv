package com.st.core;

import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author HuifengWang 操作cookie的工具类
 * 
 */
public class CookieUtil {
	
	private static Logger logger = LoggerFactory.getLogger(CookieUtil.class);
	
	
    /**
     * 根据cookie的名称获取cookie
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())){
                    //&& request.getServerName().equals(cookies[i].getDomain())) {
                return cookies[i];
            }
        }
        return null;
    }
 
    public static String getCookieValue(HttpServletRequest request, String name){
        Cookie ck = getCookie(request, name);
        if(ck!=null){
            return ck.getValue();
        }else{
            return null;
        }
    }
     
    /**
     * 删除cookie
     * @param request
     * @param response
     * @param cookie
     */
    public static void deleteCookie(HttpServletRequest request,
            HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setPath(getPath(request));
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
 
    /**
     * 设置cookie
     * @param request
     * @param response
     * @param name
     * @param value
     * 如果不设置时间，默认永久
     */
    public static void setCookie(HttpServletRequest request,
            HttpServletResponse response, String name, String value) {
        setCookie(request, response, name, value, 0x278d00);
    }
 
    /**
     * @param request
     * @param response
     * @param name
     * @param value
     * @param maxAge
     * 设置cookie，设定时间
     */
    public static void setCookie(HttpServletRequest request,
            HttpServletResponse response, String name, String value, int maxAge) {
        ;
        Cookie cookie = new Cookie(name, value == null ? "" : value.replaceAll("\r\n", ""));
        cookie.setMaxAge(maxAge);
        cookie.setPath(getPath(request));
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
 
    private static String getPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }
    
	/**
	 * 用户个人资料内部调用
	 * @throws IOException 
	 */
	public static void getUserInfo() throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		String memberId="";
		try {
			if(userIdObject==null||"".equals(userIdObject)){
				logger.info("从sessoin中获取的用户id不存在：" + userIdObject);
			}else{
				memberId = userIdObject.toString();
				String[] arr = new String[] {"member_id"+memberId};
				String mystr = "member_id="+memberId;
				//调用后台去获取用户信买这里等同于调用页面调用接口
//				JSONObject resultObject = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL,mystr,arr));
			}
			
		} catch (Exception e) {
//			logger.error("调用接口：" + JoYoUtil.PERSONSOCIAL_PERSONALINFORMATION_URL  + ",获取数据出错:" + e.getMessage(), e);
		}
	}
    
    public static void main(String[] args) {
     
    }
}