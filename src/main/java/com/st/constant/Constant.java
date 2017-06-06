package com.st.constant;

import com.st.utils.PropertiesUtils;
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
    
    /**
     * 文件上传
 */
    public static final String WYB_POSITIVE = PropertiesUtils.findPropertiesKey("WYB_POSITIVE",Constant.CONFIG_FILE_NAME);//身份证正面路径
    public static final String WYB_NEGATIVE = PropertiesUtils.findPropertiesKey("WYB_NEGATIVE",Constant.CONFIG_FILE_NAME);//身份证反面路径
    public static final String WYB_BUSINESS = PropertiesUtils.findPropertiesKey("WYB_BUSINESS",Constant.CONFIG_FILE_NAME);//营业执照路径
    public static final String WYB_ADVICE = PropertiesUtils.findPropertiesKey("WYB_ADVICE",Constant.CONFIG_FILE_NAME);//吐槽图片路径
    public static final String WYB_POSITIVE_ALIOSS_PATH = PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_PATH",Constant.CONFIG_FILE_NAME);//身份证正面路径
    public static final String WYB_NEGATIVE_ALIOSS_PATH = PropertiesUtils.findPropertiesKey("WYB_NEGATIVE_ALIOSS_PATH",Constant.CONFIG_FILE_NAME);//身份证反面路径
    public static final String WYB_BUSINESS_ALIOSS_PATH = PropertiesUtils.findPropertiesKey("WYB_BUSINESS_ALIOSS_PATH",Constant.CONFIG_FILE_NAME);//营业执照路径
    public static final String WYB_ADVICE_ALIOSS_PATH = PropertiesUtils.findPropertiesKey("WYB_ADVICE_ALIOSS_PATH",Constant.CONFIG_FILE_NAME);//吐槽图片路径
    public static final String ALIOSS_URL=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_URL",Constant.CONFIG_FILE_NAME);//阿里云图片访问地址
    public static final String ALIOSS_ID=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_ID",Constant.CONFIG_FILE_NAME);//阿里云AccessKey ID
    public static final String ALIOSS_SECRET=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_SECRET",Constant.CONFIG_FILE_NAME);//阿里云AccessKey密码
    public static final String ALIOSS_BUCKET=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_BUCKET",Constant.CONFIG_FILE_NAME);//阿里云BUCKET
    public static final String ALIOSS_ENDPOINT=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_ENDPOINT",Constant.CONFIG_FILE_NAME);//阿里云ENDPOINT内网
    public static final String ALIOSS_ENDPOINT_PUBLIC=PropertiesUtils.findPropertiesKey("WYB_POSITIVE_ALIOSS_ENDPOINT_PUBLIC",Constant.CONFIG_FILE_NAME);//阿里云ENDPOINT外网
        
    public static final Integer WIDTH=Integer.getInteger(PropertiesUtils.findPropertiesKey("WIDTH",Constant.CONFIG_FILE_NAME));//缩放后的图片宽度  比例不变，自适应
    
    public static final Integer HEIGHT=Integer.getInteger(PropertiesUtils.findPropertiesKey("HEIGHT",Constant.CONFIG_FILE_NAME));//缩放后的图片高度   比例不变，自适应
    
    public static final boolean PROPORTION=Boolean.getBoolean(PropertiesUtils.findPropertiesKey("PROPORTION",Constant.CONFIG_FILE_NAME));//是否等比例缩放
    
    /**
     * 页面埋点的dplus值
     */
    public static final String DPLUS_KEY = PropertiesUtils.findPropertiesKey("DPLUS_KEY",Constant.CONFIG_FILE_NAME);//测试环境
    
   /**
    * 微信消息模板*/
    public static final String TEMPLATE_REGISTERED=PropertiesUtils.findPropertiesKey("TEMPLATE_REGISTERED",Constant.CONFIG_FILE_NAME);//注册成功通知
    public static final String TEMPLATE_SERVICE=PropertiesUtils.findPropertiesKey("TEMPLATE_SERVICE",Constant.CONFIG_FILE_NAME);//业务服务提醒
    public static final String TEMPLATE_PAY_SUCC=PropertiesUtils.findPropertiesKey("TEMPLATE_PAY_SUCC",Constant.CONFIG_FILE_NAME);//订单支付成功
  //  public static final String TEMPLATE_ID_04=PropertiesUtils.findPropertiesKey("TEMPLATE_ID_04",Constant.CONFIG_FILE_NAME);//订单未支付通知
    public static final String TEMPLATE_AUTHENTICATION=PropertiesUtils.findPropertiesKey("TEMPLATE_AUTHENTICATION",Constant.CONFIG_FILE_NAME);//认证通知
    public static final String TEMPLATE_REMIND=PropertiesUtils.findPropertiesKey("TEMPLATE_REMIND",Constant.CONFIG_FILE_NAME);//消息提醒
    public static final String TEMPLATE_VOUCHER=PropertiesUtils.findPropertiesKey("TEMPLATE_VOUCHER",Constant.CONFIG_FILE_NAME);//获得代金券通知
    public static final String TEMPLATE_UNPAY=PropertiesUtils.findPropertiesKey("TEMPLATE_UNPAY",Constant.CONFIG_FILE_NAME);//订单未支付通知v2
    public static final String TEMPLATE_SERVICE_SUCC=PropertiesUtils.findPropertiesKey("TEMPLATE_SERVICE_SUCC",Constant.CONFIG_FILE_NAME);//参保成功通知
    public static final String TEMPLATE_SERVICE_FAIL=PropertiesUtils.findPropertiesKey("TEMPLATE_SERVICE_FAIL",Constant.CONFIG_FILE_NAME);//参保失败通知
    public static final String TEMPLATE_STOP_SUCC=PropertiesUtils.findPropertiesKey("TEMPLATE_STOP_SUCC",Constant.CONFIG_FILE_NAME);//停保成功通知
    public static final String TEMPLATE_STOP_FAIL=PropertiesUtils.findPropertiesKey("TEMPLATE_STOP_FAIL",Constant.CONFIG_FILE_NAME);//停保失败通知
    public static final String TEMPLATE_REFUND_SUCC=PropertiesUtils.findPropertiesKey("TEMPLATE_REFUND_SUCC",Constant.CONFIG_FILE_NAME);//退款成功通知
    public static final String TEMPLATE_SERVICE_EXPIRE=PropertiesUtils.findPropertiesKey("TEMPLATE_SERVICE_EXPIRE",Constant.CONFIG_FILE_NAME);//服务到期提醒
    public static final String TEMPLATE_DEAL_CANCEL=PropertiesUtils.findPropertiesKey("TEMPLATE_DEAL_CANCEL",Constant.CONFIG_FILE_NAME);//业务办理取消通知
    public static final String TEMPLATE_BUS_DYNAMICS=PropertiesUtils.findPropertiesKey("TEMPLATE_BUS_DYNAMICS",Constant.CONFIG_FILE_NAME);//业务动态提醒
    public static final String TEMPLATE_BUILD_MOBILE=PropertiesUtils.findPropertiesKey("TEMPLATE_BUILD_MOBILE",Constant.CONFIG_FILE_NAME);//手机号绑定提醒
    public static final String TEMPLATE_INSURED_MATERIAL=PropertiesUtils.findPropertiesKey("TEMPLATE_INSURED_MATERIAL",Constant.CONFIG_FILE_NAME);//参保材料提醒
    public static final String CONFIG_FILE_NAME = "config.properties";//配置文件名称
    public static final String SEO_FILE_NAME = "staticUrl.properties";//SEO配置文件名称

    public static void main(String[] args) {
    }
}

