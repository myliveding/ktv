package com.st.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.st.javabean.pojo.AccessToken;
import com.st.javabean.pojo.Menu;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;

public class WeixinUtil {
	 
private static Logger logger =LoggerFactory.getLogger(WeixinUtil.class);
	/**
	 * 调用access_token接口URL
	 */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
	 * 调用创建菜单接口URL
	 */
    public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
 
    /**
	 * 调用获取菜单接口URL
	 */
    public static final String MENU_GET_URL ="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
  
    /**
	 * 调用获取用户信息接口URL
	 */
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
 
    /**
	 * 调用发送消息接口URL
	 */
    public static final String MESSAGE_CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    /**
    * 获取消息模板ID
    */
    public static final String MESSAGE_TEMPLATE_ID_URL="https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
    /**
    * 发送消息模板消息
    */
    public static final String MESSAGE_TEMPLATE_SEND_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    /**
	 * 调用生成二维码接口URL
	 */
    public static final String QRCODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    /**
	 * 调用网页授权接口URL
	 */
    public static final String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /**
     * 调用微信JS接口的临时票据
     */
    public static final String  TICKET_GETTICKET_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
   
    // 上传多媒体文件到微信服务器
    public static final String UPLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    public static final String DOWNLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    //通过用户的OpenID查询其所在的GroupID
    public static final String USER_GROUP="https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
    //移动用户分组
    public static final String USER_GROUP_MOVE="https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";


    public static String sentRequset(String requestUrl, String requestMethod, String outputStr) {
		String jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
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
			jsonObject = buffer.toString();
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.",ce);
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
   /**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
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
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.",ce);
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
	/**
	 * 刷新accessToken
	 * @param appid 应用ID
	 * @param appsecret 应用密钥
	 * @return
	 */
	public static  AccessToken getAccessTokenForWXService(String appid ,String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				logger.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
  
 
	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				logger.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}

		return result;
	}
    
	
	/**
	 * 获取菜单
	 * 
	 * @param accessToken 有效的access_token
	 * @return 
	 */
	public static JSONObject getMenu(String accessToken) {
		String userUrl = MENU_GET_URL.replace("ACCESS_TOKEN", accessToken);
		   JSONObject jsonObject = httpRequest(userUrl, "GET", null);
		   return jsonObject;
	}
 
	/**
	 * 获取用户详细信息
	 * 
	 * @param accessToken 有效的access_token
	 * 
	 * @param openid 对应的openid
	 * 
	 * @return
	 */
   public static JSONObject getUserInfo(String accessToken, String openid){
	   String userUrl = USER_INFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
	   JSONObject jsonObject = httpRequest(userUrl, "GET", null);
	   return jsonObject;
   }
 
   /**
    * 发送文本消息
    * 
    * @param accessToken 有效的access_token
    * 
    * @param context 待发送的文本对象
    * 
    * @return
    */
   public static JSONObject setSendMessage(String accessToken, String context) {
//     int result = 0;
     //拼装发送消息的URL
     
     
     String url = MESSAGE_TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", accessToken);
     // 将文本对象转换成json字符串
     // 调用接口发送消息
     logger.info("发送文本消息00："+context);
     JSONObject jsonObject = httpRequest(url, "POST", context);
     logger.info("发送文本消息结果："+jsonObject.toString());
     return jsonObject;
   }
 
//   /**
//    * 生成带参数的二维码
//    * 
//    * @param accessToken 有效的access_token
//    * 
//    * @param qRCode 二维码参数信息对象
//    * 
//    * @return
//    */
//   public static JSONObject getEWM(String accessToken, QRCode qRCode) {
//	   int result = 0;
//	   String url = QRCODE_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
//	   String jsonQRCode = JSONObject.fromObject(qRCode).toString();
//	   JSONObject jsonObject = httpRequest(url, "POST", jsonQRCode);
//	   if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0)) {
//		   result = jsonObject.getInt("errcode");
//	   }
//	   return jsonObject;
//   }
//   
   /**
    * 网页授权
    * @param appid 应用ID
    * @param appsecret 应用密钥
    * @param code 填写第一步获取的code参数
    * @return
    */
   public static JSONObject getUserInfoOfScope( String appid ,String appsecret ,String code){
	   String userUrl = OAUTH2_ACCESS_TOKEN_URL.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
	   JSONObject jsonObject = httpRequest(userUrl, "GET", null);
	   return jsonObject;
   }
   
   /**
    * 获取微信JS接口的临时票据
    * 
    * @param accessToken 有效的access_token
    * 
    * @return
    */
   public static JSONObject getJSAPITicket(String accessToken){
	   String url = TICKET_GETTICKET_URL.replace("ACCESS_TOKEN", accessToken);
	   JSONObject jsonObject = httpRequest(url, "GET", null);
	   logger.info("获取微信JS接口的临时票据:"+jsonObject.toString());
	   return jsonObject;
   }
 
   

   // 存放：1.token，2：获取token的时间,3.过期时间  
   public final static Map<String,Object> accessTokenMap = new HashMap<String,Object>();  
   /** 
    * 发起https请求并获取结果 
    *  
    * @param requestUrl 请求地址 
    * @param requestMethod 请求方式（GET、POST） 
    * @param outputStr 提交的数据 
    * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
    */  
   public static JSONObject handleRequest(String requestUrl,String requestMethod,String outputStr) {  
       JSONObject jsonObject = null;  
         
       try {  
           URL url = new URL(requestUrl);  
           HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
           SSLContext ctx = SSLContext.getInstance("SSL", "SunJSSE");  
           TrustManager[] tm = {new MyX509TrustManager()};  
           ctx.init(null, tm, new SecureRandom());  
           SSLSocketFactory sf = ctx.getSocketFactory();  
           conn.setSSLSocketFactory(sf);  
           conn.setDoInput(true);  
           conn.setDoOutput(true);  
           conn.setRequestMethod(requestMethod);  
           conn.setUseCaches(false);  
             
           if ("GET".equalsIgnoreCase(requestMethod)) {  
               conn.connect();  
           }  
             
           if (StringUtils.isNotEmpty(outputStr)) {  
               OutputStream out = conn.getOutputStream();  
               out.write(outputStr.getBytes("utf-8"));  
               out.close();  
           }  
             
           InputStream in = conn.getInputStream();  
           BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));  
           StringBuffer buffer = new StringBuffer();  
           String line = null;  
             
           while ((line = br.readLine()) != null) {  
               buffer.append(line);  
           }  
             
           in.close();  
           conn.disconnect();  
             
           jsonObject = JSONObject.fromObject(buffer.toString());  
       } catch (MalformedURLException e) {  
           logger.error("URL错误！",e);
       } catch (IOException e) {
           logger.error("URL错误！",e);
       } catch (NoSuchAlgorithmException e) {
           logger.error("URL错误！",e);
       } catch (NoSuchProviderException e) {
           logger.error("URL错误！",e);
       } catch (KeyManagementException e) {
           logger.error("URL错误！",e);
       }  
       return jsonObject;  
   }  
     
//   /** 
//    * 获取access_token 
//    * 
//    * @author qincd 
//    * @date Nov 6, 2014 9:56:43 AM 
//    */  
//   public static AccessToken getAccessToken(String appid,String appSecret) {  
//       AccessToken at = new AccessToken();  
//       // 每次获取access_token时，先从accessTokenMap获取，如果过期了就重新从微信获取  
//       // 没有过期直接返回  
//       // 从微信获取的token的有效期为2个小时  
//       if (!accessTokenMap.isEmpty()) {  
//           Date getTokenTime = (Date) accessTokenMap.get("getTokenTime");  
//           Calendar c = Calendar.getInstance();  
//           c.setTime(getTokenTime);  
//           c.add(Calendar.HOUR_OF_DAY, 2);  
//             
//           getTokenTime = c.getTime();  
//           if (getTokenTime.after(new Date())) {  
//               logger.info("缓存中发现token未过期，直接从缓存中获取access_token");  
//               // token未过期，直接从缓存获取返回  
//               String token = (String) accessTokenMap.get("token");  
//               Integer expire = (Integer) accessTokenMap.get("expire");  
//               at.setToken(token);  
//               at.setExpiresIn(expire);  
//               return at;  
//           }  
//       }  
//       String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appSecret);  
//         
//       JSONObject object = handleRequest(requestUrl, "GET", null);  
//       String access_token = object.getString("access_token");  
//       int expires_in = object.getInt("expires_in");  
//         
//       logger.info("\naccess_token:" + access_token);  
//       logger.info("\nexpires_in:" + expires_in);  
//         
//       at.setToken(access_token);  
//       at.setExpiresIn(expires_in);  
//         
//       // 每次获取access_token后，存入accessTokenMap  
//       // 下次获取时，如果没有过期直接从accessTokenMap取。  
//       accessTokenMap.put("getTokenTime", new Date());  
//       accessTokenMap.put("token", access_token);  
//       accessTokenMap.put("expire", expires_in);  
//         
//       return at;  
//   }  

     

   /** 
    * 上传多媒体文件到微信服务器<br> 
    * http://www.oschina.net/code/snippet_1029535_23824
    * http://mp.weixin.qq.com/wiki/index.php?title=上传下载多媒体文件
    * 
    * @author  shenwf 
    */  
   public static JSONObject uploadMediaToWX(String filePath,String type,String accessToken) throws IOException{  
       File file = new File(filePath);  
       if (!file.exists()) {  
           logger.error("文件不存在！");  
           return null;  
       }  
         
       String url = UPLOAD_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);  
       URL urlObj = new URL(url);  
       HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();  
       conn.setDoInput(true);  
       conn.setDoOutput(true);  
       conn.setUseCaches(false);  
         
       conn.setRequestProperty("Connection", "Keep-Alive");  
       conn.setRequestProperty("Charset", "UTF-8");  
  
       // 设置边界  
       String boundary = "----------" + System.currentTimeMillis();
       conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="  
               + boundary);
  
       // 请求正文信息  
  
       // 第一部分：  
       StringBuilder sb = new StringBuilder();  
       sb.append("--"); // ////////必须多两道线  
       sb.append(boundary);
       sb.append("\r\n");  
       sb.append("Content-Disposition: form-data;name=\"file\";filename=\""  
               + file.getName() + "\"\r\n");  
       sb.append("Content-Type:application/octet-stream\r\n\r\n");  
  
       byte[] head = sb.toString().getBytes("utf-8");  
  
       // 获得输出流  
       OutputStream out = new DataOutputStream(conn.getOutputStream());  
       out.write(head);  
  
       // 文件正文部分  
       DataInputStream in = new DataInputStream(new FileInputStream(file));  
       int bytes = 0;  
       byte[] bufferOut = new byte[1024];  
       while ((bytes = in.read(bufferOut)) != -1) {  
           out.write(bufferOut, 0, bytes);  
       }  
       in.close();  
  
       // 结尾部分  
       byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
  
       out.write(foot);  
  
       out.flush();  
       out.close();  
  
       /** 
        * 读取服务器响应，必须读取,否则提交不成功 
        */  
        try {  
            // 定义BufferedReader输入流来读取URL的响应  
            StringBuffer buffer = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
            conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                buffer.append(line);  
            }  
              
            reader.close();  
            conn.disconnect();  
              
            return JSONObject.fromObject(buffer.toString());  
        } catch (Exception e) {  
            logger.error("发送POST请求出现异常！" + e.getMessage(),e);
        }
       return null;  
   }  
     

   /** 
    * 从微信服务器下载多媒体文件 
    * 
    * @author  shenwf 
    */  
   public static String downloadMediaFromWx(String accessToken,String mediaId,String path) throws IOException {  
       if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(mediaId)){
           return null; 
       } 
       if(com.st.utils.text.StringUtils.isNotEmpty(mediaId)&&(mediaId.indexOf("http")>=0||mediaId.endsWith(".jpg"))){
           return mediaId;
       }
       String requestUrl = DOWNLOAD_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
       logger.info("MEDIA_ID "+mediaId);
       URL url = new URL(requestUrl);  
       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
       conn.setRequestMethod("GET");  
       conn.setDoInput(true);  
       conn.setDoOutput(true);  
       InputStream in = conn.getInputStream();  
       logger.info("从微信服务器下载多媒体文件 路径"+url.toString()+" 大小"+conn.getContentLength());
       String ContentDisposition = conn.getHeaderField("Content-disposition");  
       if(com.st.utils.text.StringUtils.isEmpty(ContentDisposition)||ContentDisposition.equals("null")){
           return null;  
       }

       
      return "";
   }

   
   /**
    * 获取临时显示URL
    *
    * @author  shenwf
    */
   public static String getTempURLFromOSS(String filename,String path) {
       String url="";
       if(com.st.utils.text.StringUtils.isNotEmpty(filename)){
           if(filename.indexOf("http")>-1){
               filename=filename.substring(filename.lastIndexOf("/")+1,filename.length());
           }
            try {
                url = "";
            } catch (Exception e) {
                logger.error("阿里云获取临时显示URL 出现异常"+e.getMessage(),e);
                return null;
            }
       }
       return url;
   }
   /**
    * 检验AccessToken是否有效
    * @param accessToken，mediaId
    * @return boolean
    * @author shenwf
    */
   public static boolean checkAccessToken(String accessToken,String mediaId){
       boolean flag=true;
       if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(mediaId)){ 
           return false;
           }  
       logger.info("检验accessToken有效性");
       String requestUrl = DOWNLOAD_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);  
       URL url;
        try {
            url = new URL(requestUrl);
            logger.info("从微信服务器下载多媒体文件 路径"+url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setDoInput(true);  
            conn.setDoOutput(true);  
//            InputStream in = conn.getInputStream();  
            String ContentDisposition = conn.getHeaderField("Content-disposition");  
            if(com.st.utils.text.StringUtils.isNotEmpty(ContentDisposition)){
                return false;
            }
        } catch (Exception e) {
            logger.error("检验AccessToken是否有效出现错误！",e);
        }  
       return flag;
   }
   public static JSONObject getTemplateId(String accessToken,String shortId){
       String requestUrl = MESSAGE_TEMPLATE_ID_URL.replace("ACCESS_TOKEN", accessToken);  
       JSONObject jsonObject = httpRequest(requestUrl, "POST", shortId);
       return jsonObject;
   }
   /**
    * 发送模板消息
    * 
    * @param accessToken 有效的access_token
    * 
    * @param content 待发送的消息对象
    * 
    * @return
    */
   public static JSONObject sendTemplateMessage(String accessToken, String content) {
     String url = MESSAGE_TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", accessToken);
     // 调用接口发送消息
     JSONObject jsonObject = httpRequest(url, "POST", content);
     return jsonObject;
   }

   /**
    * 测试方法
    * @param args
    */
   public static void main(String[] args) {
	   String imgs = "";
	   String[] picId = {"1","2","3"};
	   for (int i = 0; i < picId.length; i++) {
		   if(i == 0){
			   imgs = picId[i];
		   }else{
			   imgs = imgs + "," + picId[i];
		   }
	   }
//	   System.err.println(imgs);
	   Random r = new Random();
	   for (int i = 0; i < 2; i++) {
//		   System.err.println(r.nextInt(5) + "--" + (r.nextInt(5) + 1));
	   }
//	   System.err.println("新版知社保文章详情".contains("文章详情"));
//	   System.err.println("/article/detailinfo.do?articleId=ID&param=questionDetail".replace("ID", "1106"));

   }
     /*
     * 微信支付在https环境下需要加载微信api安全证书（下载自微信商户平台）
     * */
   public static String wxpayRequset(String requestUrl, String requestMethod, String outputStr) {
        String jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 证书文件(微信商户平台-账户设置-API安全-API证书-下载证书)
            String keyStorePath =WeixinUtil.class.getResource("/").getPath()+"/apiclient_cert.p12";
            // 证书密码（默认为商户ID）
            String password = Constant.MCH_ID;
            // 实例化密钥库
            KeyStore ks = KeyStore.getInstance("PKCS12");
            // 获得密钥库文件流
            FileInputStream fis = new FileInputStream(keyStorePath);
            // 加载密钥库
            ks.load(fis, password.toCharArray());
            // 关闭密钥库文件流
            fis.close();
            // 实例化密钥库
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            // 初始化密钥工厂
            kmf.init(ks, password.toCharArray());

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
             httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
             OutputStream outputStream = httpUrlConn.getOutputStream();
             // 注意编码格式，防止中文乱码
             outputStream.write(outputStr.getBytes("UTF-8"));
             outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
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
            jsonObject = buffer.toString();
        } catch (ConnectException ce) {
            logger.error("Weixin server connection timed out.",ce);
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return jsonObject;
   }

    /**
     * 通过用户的OpenID查询其所在的GroupID
     * @param accessToken 有效的access_token
     * @param jsonStr 数据
     * @return
     */
    public static JSONObject getUserGroup(String accessToken,String jsonStr) {
        String userUrl = USER_GROUP.replace("ACCESS_TOKEN", accessToken);
//        String jsonStr = "{\"openid\":\""+openId+"\"}";
        JSONObject jsonObject = httpRequest(userUrl, "POST", jsonStr);
        logger.info("获取用户分组"+jsonObject);
        return jsonObject;
    }
    /**
     * 移动用户分组
     * @param accessToken 有效的access_token
     * @param jsonStr 数据
     * @return
     */
    public static JSONObject updateUserGroup(String accessToken,String jsonStr) {
        String userUrl = USER_GROUP_MOVE.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = httpRequest(userUrl, "GET", jsonStr);
        logger.info("移动用户分组"+jsonObject);
        return jsonObject;
    }
 }
