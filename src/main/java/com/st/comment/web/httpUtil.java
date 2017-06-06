package com.st.comment.web;

import com.st.core.util.ConstantsUtil;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class httpUtil {
	private  static Logger logger =LoggerFactory.getLogger(httpUtil.class);

	/**
	 * 
	 * 发送json请求
	 * <ul>
	 * <li>
	 * <b>原因：<br/>
	 * <p>
	 * [2014-8-18]gaozhanglei<br/>
	 * </p>
	 * </li>
	 * </ul>
	 */
	public static  String sentJson(String url, String jsonparam,String  encoding){
		HttpClient client=null;
		String str="";
		try {
			client=new DefaultHttpClient();
			logger.info("发送url："+url+"--start--");
			StringEntity entity=new StringEntity(jsonparam);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));//"application/octet-stream"  
		    entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING,ConstantsUtil.SYSTEM_DEFAULT_ENCODING));
		    HttpPost post = new HttpPost(url);  
	        post.setHeader("Accept", "application/json");  
	        post.setHeader("Content-Type", "application/json");  
	        post.setEntity(entity);  
	  
	        HttpResponse response = client.execute(post);  
	  
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	        	str = EntityUtils.toString(response.getEntity(), ConstantsUtil.SYSTEM_DEFAULT_ENCODING);  
	        }else{  
	            post.abort();  
	            str = "Error Response code "  
	                    + response.getStatusLine().getStatusCode() + " :"  
	                    + response.getStatusLine().toString();  
	            logger.info(str);  
	           
	        }  
	        logger.info("发送url："+url+"--end--");
	        return str;  
		} catch (Exception e) {
			logger.error("发送url:"+url+"出错："+e.getMessage(), e);
		}finally{
			if (null!=client){
				client.getConnectionManager().shutdown();
			}

		}
		
		return str;
		
	}
	
	/**
	 *
	 * 发送get请求
	 * <ul>
	 * <li>
	 * <b>原因：<br/>
	 * <p>
	 * [2014-8-18]gaozhanglei<br/>
	 * </p>
	 * </li>
	 * </ul>
	 */
	@SuppressWarnings("deprecation")
	public static String get(String url, String param) {
		HttpClient httpClient=null;
		String body = null;
		try {
			httpClient=new DefaultHttpClient();
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + param));
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
				HttpEntity entity = httpresponse.getEntity();
				body = EntityUtils.toString(entity);
				if (entity != null) {
					entity.consumeContent();
				}
			}else{
				httpget.abort();
				body = "Error Response code "  
                    + httpresponse.getStatusLine().getStatusCode() + " :"  
                    + httpresponse.getStatusLine().toString();  
            logger.info(body);
			}
		} catch (Exception e) {
			logger.error("发送url:"+url+"出错："+e.getMessage(), e);
		}finally{
			if (null!=httpClient){
				httpClient.getConnectionManager().shutdown();
			}
		}
		return body;
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	public static String getRedirectDatas(String url){
		
		String resultToken = null;
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		HttpMethod method = new GetMethod( url );
		method.setFollowRedirects(false);
		String location = null;
		try {
		    client.executeMethod( method );
		    if( method.getStatusCode( ) == HttpStatus.SC_MOVED_TEMPORARILY) {
			    org.apache.commons.httpclient.Header[] header=	method.getResponseHeaders();
			    
				for (org.apache.commons.httpclient.Header header2 : header) {
					if (header2.getName().equalsIgnoreCase("Location")){
						location = header2.getValue();
					}
				}
		    }
		}  catch( IOException ioe ) {
			logger.error( "IO Exeception: " + ioe.getMessage(),ioe);
		} finally {
		    method.releaseConnection( );
		    method.recycle( );
		}
		return location;
	}
}