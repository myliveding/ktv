package com.st.controller.swiftpass.config;

import com.st.constant.Constant;

/**
 * <一句话功能简述>
 * <功能详细描述>配置信息
 * 
 * @author  Administrator
 * @version  [版本号, 2014-8-29]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SwiftpassConfig {
    
    /**
     * 威富通交易密钥
     */
    public static String key ="3bf352c81b4afd1dce7bbb9beaf63aeb";
    
    /**
     * 威富通商户号
     */
    public static String mch_id="106530000053";
    
    /**
     * 威富通请求url
     */
    public static String req_url="https://pay.swiftpass.cn/pay/gateway";;
    
    /**
     * 通知url
     */
    public static String notify_url= Constant.URL+"/swiftpass/pay/notify.do";
    
//    static{
//        Properties prop = new Properties();
//        InputStream in = SwiftpassConfig.class.getResourceAsStream("/config.properties");
//        try {
//            prop.load(in);
//            key = prop.getProperty("key").trim();
//            mch_id = prop.getProperty("mch_id").trim();
//            req_url = prop.getProperty("req_url").trim();
//            notify_url = prop.getProperty("notify_url").trim();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
