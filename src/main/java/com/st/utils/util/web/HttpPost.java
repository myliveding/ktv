package com.st.utils.util.web;

import com.st.utils.util.date.DateUtil;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author gaozl
 *
 */
public class HttpPost {
	private static final Logger logger = LoggerFactory.getLogger(HttpPost.class);
	
	/**
	 * 根据url地址和要发送的字符串通过http协议用post方法把字符
	 * 串用流格式发送到指定url的网页，并获取应答信息。
	 * @param pathUrl：查询地址
	 * @param inXml：上行报文
	 * @param outTime：超时上限时间
	 * @param username：用户名
	 * @param password：密码
	 * @return 查询输出报文XML
	 */
	@SuppressWarnings("deprecation")
	public static String doHttpPost(String pathUrl, String inXml, int outTime,
			String charsets, String username, String password) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("Post requst content time:" + DateUtil.getNowStringTime());
			logger.debug("发送的内容:" + inXml);
			logger.debug("发送数据到: " + pathUrl);
		}
		logger.info("发送的内容:" + inXml);
		logger.info("发送数据到: " + pathUrl);
		username = username == null?"":username ;
		password = password == null?"":password ;
		
		HttpClient httpClient = new HttpClient();
		
		//为目标服务器或是代理服务器设置默认证书
		Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
		
		httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);

		httpClient.getParams().setParameter("http.protocol.version",HttpVersion.HTTP_1_1);

		httpClient.getParams().setParameter("http.socket.timeout",new Integer(outTime * 1000));

		httpClient.getParams().setParameter("http.protocol.content-charset","GBK");
		
		PostMethod postMethod = new PostMethod(pathUrl);
//
//		NameValuePair[] data = {new NameValuePair("program", inXml)};    
//				
//		postMethod.setRequestBody(data);   
		
		postMethod.setRequestBody(inXml);
		
		try {
			httpClient.executeMethod(postMethod);
			
			if(postMethod.getStatusCode()==HttpStatus.SC_OK){
				InputStream in = postMethod.getResponseBodyAsStream();
	  
				BufferedReader rd = new BufferedReader(new InputStreamReader(in,charsets));

				StringBuffer sb = new StringBuffer();
				
				int ch=-1;
				while((ch = rd.read()) > -1) 
					sb.append((char)ch);
				if (logger.isDebugEnabled()) {
					logger.debug("收到的报文:" + sb.toString());
				}
				return removeWenHao(sb.toString());
			}else{
				throw new Exception("Unexpected failure: "+ postMethod.getStatusLine().toString());
			}
		} catch (Exception e) {
			logger.error("Http协议post方法发送字符流时候出现异常：", e);
			throw new Exception("Http协议post方法发送字符流时候出现异常：", e);
		} finally{
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static InputStream doGroupHttpPost(String pathUrl, String inXml,
			int outTime, String charsets, String username, String password)
			throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("Post requst content time:" + DateUtil.getNowStringDate());
			logger.debug("发送的内容:" + inXml);
			logger.debug("发送数据到: " + pathUrl);
		}
		username = username == null?"":username ;
		password = password == null?"":password ;
		
		HttpClient httpClient = new HttpClient();
		
		//为目标服务器或是代理服务器设置默认证书
		Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
		
		httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);

		httpClient.getParams().setParameter("http.protocol.version",HttpVersion.HTTP_1_1);

		httpClient.getParams().setParameter("http.socket.timeout",new Integer(outTime * 1000));

		httpClient.getParams().setParameter("http.protocol.content-charset","GBK");
		
		PostMethod postMethod = new PostMethod(pathUrl);
//
//		NameValuePair[] data = {new NameValuePair("program", inXml)};    
//				
//		postMethod.setRequestBody(data);   
		
		postMethod.setRequestBody(inXml);
		
		try {
			httpClient.executeMethod(postMethod);
			
			if(postMethod.getStatusCode()==HttpStatus.SC_OK){
				
				InputStream in = postMethod.getResponseBodyAsStream();
	  
				BufferedReader rd = new BufferedReader(new InputStreamReader(in,charsets));

				StringBuffer sb = new StringBuffer();
				
				int ch=-1;
				while((ch = rd.read()) > -1) 
					sb.append((char)ch);
				if (logger.isDebugEnabled()) {
					logger.debug("收到的报文:" + sb.toString());
				}
				return in;
			}else{
				throw new Exception("Unexpected failure: "+ postMethod.getStatusLine().toString());
			}
		} catch (Exception e) {
			logger.error("Http协议post方法发送字符流时候出现异常：", e);
			throw new Exception("Http协议post方法发送字符流时候出现异常：", e);
		} finally{
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
	}
	
	private static String removeWenHao(String sb){
		
		sb = sb.replaceAll("&lt;a？", "&lt;a ") ;
	
		return sb ;
	}


}
