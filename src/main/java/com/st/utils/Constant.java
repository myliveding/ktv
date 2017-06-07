package com.st.utils;

import org.springframework.stereotype.Component;


/**
 * 域名常量类
 *
 * Created by shenwf 20150826.
 */
@Component
public final class Constant {

	/**
     * 域名
     */
    public static final String URL=PropertiesUtils.findPropertiesKey("URL",Constant.CONFIG_FILE_NAME);
    /*
    * 支付宝异步通知地址
    * */
    public static final String ALIPAYNOTIFYURL=PropertiesUtils.findPropertiesKey("ALIPAY_NOTIFYURL",Constant.CONFIG_FILE_NAME);

    /**
     * 静态文件保存域名
     */
    public static final String staticUrl=PropertiesUtils.findPropertiesKey("staticUrl",Constant.CONFIG_FILE_NAME);
      /**
       * 环境
       */
    public static final String ENVIROMENT=PropertiesUtils.findPropertiesKey("ENVIROMENT",Constant.CONFIG_FILE_NAME);
  
    /**
     * 微信appid
     */
    public static final String APP_ID=PropertiesUtils.findPropertiesKey("APP_ID",Constant.CONFIG_FILE_NAME);
    /**
     * 微信商户号
     */
    public static final String MCH_ID=PropertiesUtils.findPropertiesKey("MCH_ID",Constant.CONFIG_FILE_NAME);

    /**
     * 微信app密钥
     */
    public static final String APP_SECRET=PropertiesUtils.findPropertiesKey("APP_SECRET",Constant.CONFIG_FILE_NAME);
    
    /**
     * 微信token
     */
    public static final String APP_TOKEN =PropertiesUtils.findPropertiesKey("APP_TOKEN",Constant.CONFIG_FILE_NAME);
    
    /**
     * php接口密钥
     */
    public static final String PHP_APP_SECRET =PropertiesUtils.findPropertiesKey("PHP_APP_SECRET",Constant.CONFIG_FILE_NAME);
    
    /**
     * php接口api
     */
    public static final String PHP_API_URL =PropertiesUtils.findPropertiesKey("PHP_API_URL",Constant.CONFIG_FILE_NAME);
    /**
    * java接口api
    */
    public static final String JAVA_API_URL =PropertiesUtils.findPropertiesKey("JAVA_API_URL",Constant.CONFIG_FILE_NAME);
    /**
     *java城市代理商接口api
     */
    public static final String JAVA_AGENT_API_URL =PropertiesUtils.findPropertiesKey("JAVA_AGENT_API_URL",Constant.CONFIG_FILE_NAME);
    /**
    * 代金券接口api
    */
    public static final String VOUCHER_API_URL =PropertiesUtils.findPropertiesKey("VOUCHER_API_URL",Constant.CONFIG_FILE_NAME);

    /**
     * 京东支付商户md5密钥
     */
    public static final String MERCHANT_MD5KKEY=PropertiesUtils.findPropertiesKey("MERCHANT_MD5KKEY",Constant.CONFIG_FILE_NAME);

    /**
     * 京东支付商户des密钥
     */
    public static final String MERCHANT_DESKEY=PropertiesUtils.findPropertiesKey("MERCHANT_DESKEY",Constant.CONFIG_FILE_NAME);
    
    /**
     * 微信JS调用
     * 这部分变量不需要根据环境去修改
     */
    public static final String TIME_STAMP =PropertiesUtils.findPropertiesKey("TIME_STAMP",Constant.CONFIG_FILE_NAME);
    //生成签名的时间戳10位，一般是取时间里面的一段;


    public static final String NONCESTR =PropertiesUtils.findPropertiesKey("NONCESTR",Constant.CONFIG_FILE_NAME);//生成签名的随机串

    public static final String CONFIG_FILE_NAME = "config.properties";//配置文件名称


}

