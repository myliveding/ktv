/*
 * @(#)HttpRequestUtil.java 2014-3-27下午10:51:18
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.utils.util.web;

import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



/**
 * Http请求工具类
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2014-3-27下午10:51:18]sunju<br/>
 * </p>
 * </li>
 * </ul>
 */

public class HttpRequestUtil {
	/**
	 * 获得请求地址，参数经过URLEncoder处理，避免产生乱码以及转义字符
	 * @author sunju
	 * @creationDate. 2012-12-18 上午11:19:27 
	 * @param serverURL 服务URL
	 * @param inParams 请求输入参数集合
	 * @param enc 编码，用于参数URL编码
	 * @return 请求地址
	 * @throws Exception
	 */
	public static String getRequestURL(String serverURL, Map<String, String> inParams, String enc) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(serverURL)) return null;
        if (inParams != null && inParams.size() > 0) {
        	StringBuilder params = new StringBuilder();
        	Set<Entry<String, String>> entrys = inParams.entrySet();
            for (Entry<String, String> param : entrys) {
            	params.append(param.getKey()).append("=")
            	.append(URLEncoder.encode(param.getValue(), enc)).append("&");
    		}

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
                serverURL += ((serverURL.indexOf("?") == -1) ? "?" : "&") + params.toString();
            }
        }
        return serverURL;
	}
	/**
	 * get请求
	 * @author sunju
	 * @creationDate. 2012-12-18 上午11:22:16 
	 * @param requestURL 请求地址
	 * @param enc 编码，用于设置请求的编码
	 * @return 响应内容
	 * @throws Exception
	 */
	public static String doGet(String requestURL, String enc) throws IOException {
		if (StringUtils.isEmpty(requestURL)) return null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        
        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
           
             
           
            inputStream = conn.getInputStream();
            if (inputStream != null) {
				String line = ""; // 行字符串
				StringBuilder result = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(inputStream, enc));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				return result.toString();
			}
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
        	// 释放资源
        	if (reader != null) reader.close();
        	if (inputStream != null) inputStream.close();
            if (conn != null) conn.disconnect();
        }
        return null;
	}
	/**
	 * https请求
	 * @author sunju
	 * @creationDate. 2014-3-27 下午10:51:58 
	 * @param requestURL 请求地址
	 * @param requestMethod 请求方法（POST/GET）
	 * @param outputStr 提交数据字符串
	 * @param encoding 编码
	 * @return 响应字符串
	 * @throws Exception
	 */
	public static String doHttpsRequest(String requestURL,
			String requestMethod, String outputStr, String encoding) throws IOException,NoSuchAlgorithmException,KeyManagementException {
		StringBuffer buffer = new StringBuffer();
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		TrustManager[] atrustmanager = { trustManager };
		sslContext.init(null, atrustmanager, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		URL url = new URL(requestURL);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
				.openConnection();
		httpUrlConn.setSSLSocketFactory(ssf);

		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 设置请求方式（GET/POST）
		httpUrlConn.setRequestMethod(requestMethod);

		if ("GET".equalsIgnoreCase(requestMethod))
			httpUrlConn.connect();

		// 当有数据需要提交时
		if (StringUtils.isNotEmpty(outputStr)) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 注意编码格式，防止中文乱码
			outputStream.write(outputStr.getBytes(encoding));
			outputStream.close();
		}

		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, encoding);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		return buffer.toString();
	}
}
