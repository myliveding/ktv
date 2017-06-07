package com.st.utils.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * http 协议
 * 
 * @author gaozhanglei
 * @since JDK1.5
 * @date 2011-8-25
 * @time 上午09:38:39
 */
public class HttpAgreementClient {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(HttpAgreementClient.class);

	/**
	 * http 协议 根据URL 发送消息数据,得到返回结果
	 * 
	 * @param urlStr
	 *            url地址
	 * @param message
	 *            消息
	 * @param charSet
	 *            字符编码
	 * @param method
	 *            方式（GET/POST）
	 * @return
	 * @throws Exception
	 * @author xiaomeng.zou
	 * @since JDK1.5
	 * @date 2011-9-6
	 * @time 下午03:50:24
	 */
	public static String sendMessage(String urlStr, String message,
			String charSet, String method) throws MalformedURLException,IOException {
		if (StringUtils.isBlank(charSet)) {
			charSet = "UTF-8";
		}
		URL url = new URL(urlStr);
		HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
		urlConnect.setRequestMethod(method);
		return sendMessage(message, charSet, urlConnect);
	}
	
	public static String sendMessageMock(String urlStr, String message,
			String charSet, String method) throws MalformedURLException,IOException {
		if (StringUtils.isBlank(charSet)) {
			charSet = "UTF-8";
		}
		URL url = new URL(urlStr);
		HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
		urlConnect.setRequestMethod(method);
		return sendMessageMock(message, charSet, urlConnect);
	}

	/**
	 * http 协议 根据URL send数据,得到返回结果
	 * 
	 * @param urlConnect
	 *            URL地址
	 * @param message
	 *            发送字符串
	 * @return 返回字符串
	 * @throws Exception
	 * @author xiaomeng.zou
	 * @since JDK1.5
	 * @date 2011-8-25
	 * @time 下午12:03:34
	 */
	private static String sendMessage(String message, String charSet,
			HttpURLConnection urlConnect) throws IOException {
		logger.info("Need Send Message is ( " + message + " )");
		urlConnect.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] b = new byte[1024 * 8];
		StringBuffer sb = new StringBuffer();
		try {
			if (StringUtils.isNotBlank(message)) {
				if (!urlConnect.getDoOutput()) {
					urlConnect.setDoOutput(true);
				}
				bos = new BufferedOutputStream(urlConnect.getOutputStream());
				bos.write(message.getBytes(charSet));
				bos.flush();
			}
			if (!urlConnect.getDoInput()) {
				urlConnect.setDoInput(true);
			}
			bis = new BufferedInputStream(urlConnect.getInputStream());
			int i = 0;
			while ((i = bis.read(b)) != -1) {
				sb.append(new String(b, 0, i, charSet));
			}
			logger.info("Recive Message is " + sb.substring(0, sb.length()));
			return sb.substring(0, sb.length());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			try {
				bis = new BufferedInputStream(urlConnect.getErrorStream());
				int i = 0;
				sb = new StringBuffer();
				while ((i = bis.read(b)) != -1) {
					sb.append(new String(b, 0, i, charSet));
				}
				logger.error(sb.toString());
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
			throw e;
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	private static String sendMessageMock(String message, String charSet,
			HttpURLConnection urlConnect) {
		logger.info("Need Send Message is ( " + message + " )");
		urlConnect.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESPONSE><HEAD><ERROR_CODE>0</ERROR_CODE><ERROR_MSG></ERROR_MSG><ERROR_HINT></ERROR_HINT></HEAD><BODY><RES_STAT>2</RES_STAT><RES_ORG_ID>10204259</RES_ORG_ID><CITY_NO>571</CITY_NO><SPECIAL_TYPE>0</SPECIAL_TYPE><ADDRESS>20100920</ADDRESS><DONE_CODE>11111111</DONE_CODE></BODY><RET_INFO><DONE_CODE>22222222</DONE_CODE></RET_INFO></RESPONSE>");
		logger.info("Recive Message is " + sb.substring(0, sb.length()));
		return sb.substring(0, sb.length());
	}

	/**
	 * http 协议 根据URL 发送消息数据,与GET方式发送。得到返回结果
	 * 
	 * @param urlStr
	 *            URL地址
	 * @param message
	 *            消息数据
	 * @param charSet
	 *            字符编码
	 * @return
	 * @throws Exception
	 * @author xiaomeng.zou
	 * @since JDK1.5
	 * @date 2011-9-6
	 * @time 下午03:53:28
	 */
	public static String sendMessageByGet(String urlStr, String message,
			String charSet) throws IOException {
		return sendMessage(urlStr, message, charSet, "GET");
	}

	/**
	 * http 协议 根据URL 发送消息数据,与POST方式发送。得到返回结果
	 * 
	 * @param urlStr
	 *            URL地址
	 * @param message
	 *            消息数据
	 * @param charSet
	 *            字符编码
	 * @return
	 * @throws Exception
	 * @author xiaomeng.zou
	 * @since JDK1.5
	 * @date 2011-9-6
	 * @time 下午03:54:38
	 */
	public static String sendMessageByPost(String urlStr, String message,
			String charSet) throws IOException {
		return sendMessage(urlStr, message, charSet, "POST");
	}
	
	public static String sendMessageByPostMock(String urlStr, String message,
			String charSet) throws IOException {
		return sendMessageMock(urlStr, message, charSet, "POST");
	}
}
