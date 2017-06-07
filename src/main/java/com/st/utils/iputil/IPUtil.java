package com.st.utils.iputil;

import com.st.utils.ConstantsUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

public class IPUtil {
	
	private static Logger logger = LoggerFactory.getLogger(IPUtil.class);
	private static final String localIP="127.0.0.1";
	
	/**
	 * 获得真实IP地址--未使用
	 * @author sunju
	 * @param request 请求
	 * @creationDate. 2011-5-9 上午09:14:17 
	 * @return 真实IP地址
	 * @throws Exception 
	 */
	public static String getIpAddr(HttpServletRequest request) throws UnknownHostException {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("Proxy-Client-IP");
		}     
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
		    ip = request.getRemoteAddr();
		}
		if (localIP.equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) { // IPv4和IPv6的localhost
			// 客户端和服务端是在同一台机器上、获取本机的IP
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
		}
		
		return ip;
	}
	
	/**
	 * 返回IP地址--未使用
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		String result="";
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1){
	           result=ip.substring(0,index);
			}else{
	           result=ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			result=ip;
		}
		return result;
	}
	   
	/**
	 * 返回用户的IP地址
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	*/
	public static String toIpAddr(HttpServletRequest request) throws UnknownHostException {
       String ip = request.getHeader("X-Forwarded-For");
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	   ip = request.getHeader("Proxy-Client-IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	   ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	   ip = request.getHeader("HTTP_CLIENT_IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	   ip = request.getHeader("HTTP_X_FORWARDED_FOR");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	   ip = request.getRemoteAddr();
       }
       if(StringUtils.isNotEmpty(ip) &&ip.indexOf(",")>-1){
    	   ip=ip.substring(0,ip.indexOf(","));
       }
       if (localIP.equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) { //IPv4和IPv6的localhost
			// 客户端和服务端是在同一台机器上、获取本机的IP
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
		}
       return ip;
	}

	/**
	 * 返回IP地址解析的详情
	 * @param request
	 * @return City
	 */
	public static City getCityByIP(HttpServletRequest request) {
		City city = new City();
		city.setFlag(false);
		try {
			String ip = toIpAddr(request);
//			ip = "183.62.35.197";//广州
//			ip = "218.75.35.112";//杭州
//			ip = "218.58.51.254";//青岛
//			ip = "218.94.122.3";//南京
//			ip = "061.183.131.255";//武汉
//			ip = "210.51.167.169";//北京
//			ip = "114.80.163.38";//上海
//			ip = "218.4.58.41";//苏州
//			ip = "125.71.216.26";//成都
//			ip = "58.60.124.50";//深圳
			if(com.st.utils.text.StringUtils.isNotEmpty(ip)){
				String inputline="";
				String info="";
				try {
					Random r = new Random();
					int n = r.nextInt(10);
					String urlIP = ConstantsUtil.BAIDU_IP.replace("AK", ConstantsUtil.AK_S[n]).replace("IP", ip);
					logger.info("随机获取的AK为第：" + n + "个," +  "调用的完整url为：" + urlIP);
					URL url = new URL(urlIP);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setReadTimeout(1000);
					conn.setRequestMethod("GET");

					InputStreamReader  inStream = new InputStreamReader(conn.getInputStream(),"UTF-8");
					BufferedReader buffer=new BufferedReader(inStream);

					while((inputline=buffer.readLine())!=null){
						info += inputline;
					}
					logger.info("IP地址调用API解析后中获取的信息：" + info);
					if(com.st.utils.text.StringUtils.isEmpty(info)){
						logger.info("返回值为空,调用失败或者是调用次数超限了");
					}else{
						//解析api返回值
						JSONObject jsonob = JSONObject.fromObject(info);
						if(0 == jsonob.getInt("status")){
							String aa = jsonob.getString("address");
							String[] c = aa.split("\\|");
							city.setFlag(true);
							city.setCity(c[2].trim().equals("None")?"":c[2].trim());
							city.setProvince(c[1]);
							city.setCountry(c[0]);
						}else{
							logger.info("结果不正确，提示为：" + jsonob.getString("message"));
						}
					}
				} catch (MalformedURLException e) {
					logger.error("调用API出错,MalformedURLException：" + e.getMessage(),e);
				} catch (IOException e) {
					logger.error("调用API出错,IOException：" + e.getMessage(),e);
				}catch (Exception e) {
					logger.error("调用API出错,Exception：" + e.getMessage(),e);
				}
			}
		} catch (UnknownHostException e1) {
			logger.error("从request中获取ip地址出错,UnknownHostException：" + e1.getMessage(),e1);
		}
		return city;
	}

	/**
	 * 返回Phone解析的详情
	 * @param memberMobile
	 * @return City
	 */
	public static City getCityByPhone(String memberMobile) {
		City city = new City();
		city.setFlag(false);
		if(com.st.utils.text.StringUtils.isNotEmpty(memberMobile)){
			String inputline="";
			String info="";
			try {
				String urlIP="http://sj.apidata.cn/?mobile="+memberMobile;
				URL url = new URL(urlIP);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setReadTimeout(1000);
				conn.setRequestMethod("GET");
				InputStreamReader  inStream = new InputStreamReader(conn.getInputStream(),"UTF-8");
				BufferedReader buffer=new BufferedReader(inStream);
				while((inputline=buffer.readLine())!=null){
					info += inputline;
				}
				logger.info("手机号调用API解析后中获取的信息：" + info);
				if(com.st.utils.text.StringUtils.isEmpty(info)){
					logger.info("返回值为空,调用失败或者是调用次数超限了");
				}else{
//					Document document = DocumentHelper.parseText(info);
//					/* 得到xml根元素 */
//					Element root = document.getRootElement();
//					// 得到根元素的所有子节点
//					List<Element> elementList = root.elements();
//					// 遍历所有子节点
//					for (Element e : elementList) {
//						if (e.getName().trim().toLowerCase().equals("city")) {
//							city.setFlag(true);
//							city.setCity(e.getText());
//						}
//					}
					JSONObject resultObject = JSONObject.fromObject(info);
					if(1 == resultObject.getInt("status")){
						city.setFlag(true);
						city.setCity(resultObject.getJSONObject("data").getString("city"));
					}
				}
			}catch (Exception e) {
				logger.error("调用API出错,Exception：" + e.getMessage(),e);
			}
		}
		return city;
	}

}