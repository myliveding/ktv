package com.st.utils;

import org.springframework.stereotype.Component;


/**
 * 域名常量类
 *
 * Created by shenwf 20150826.
 */
@Component
public final class Constant {

    //配置文件名称
    public static final String CONFIG_FILE_NAME = "config.properties";

    //环境变量
    public static final String ENVIROMENT = PropertiesUtils.findPropertiesKey("ENVIROMENT",Constant.CONFIG_FILE_NAME);

    //服务器域名
    public static final String URL = PropertiesUtils.findPropertiesKey("URL",Constant.CONFIG_FILE_NAME);

    //支付宝异步通知地址
    public static final String ALIPAYNOTIFYURL = PropertiesUtils.findPropertiesKey("ALIPAY_NOTIFYURL",Constant.CONFIG_FILE_NAME);

    //微信appid
    public static final String APP_ID = PropertiesUtils.findPropertiesKey("APP_ID",Constant.CONFIG_FILE_NAME);

    //微信商户号
    public static final String MCH_ID = PropertiesUtils.findPropertiesKey("MCH_ID",Constant.CONFIG_FILE_NAME);

    //微信app密钥
    public static final String APP_SECRET = PropertiesUtils.findPropertiesKey("APP_SECRET",Constant.CONFIG_FILE_NAME);
    
    //微信token
    public static final String APP_TOKEN = PropertiesUtils.findPropertiesKey("APP_TOKEN",Constant.CONFIG_FILE_NAME);
    

    //php接口api
    public static final String PHP_API_URL = PropertiesUtils.findPropertiesKey("PHP_API_URL",Constant.CONFIG_FILE_NAME);

    //微信JS调用,这部分变量不需要根据环境去修改
    public static final String TIME_STAMP = PropertiesUtils.findPropertiesKey("TIME_STAMP",Constant.CONFIG_FILE_NAME);

    //生成签名的时间戳10位，一般是取时间里面的一段,生成签名的随机串
    public static final String NONCESTR = PropertiesUtils.findPropertiesKey("NONCESTR",Constant.CONFIG_FILE_NAME);

    //百度的ip地址解析接口
    public static final String BAIDU_IP = "http://api.map.baidu.com/location/ip?ak=AK&ip=IP";

    //百度IP地址解析的密钥
    public static final String[] AK_S = {"E5GzF0Ha4sXCfOKIkn0fMUmf","c8t2TddjenpVGWf89aQnvz8l","T5Qt6y4rGOdmMZaoEjWyhX4O",
            "WwSfPk5df4xzDyNo4GVegqyA","BDE0SAW6EHBuaU1bLpNHER3q","YGuumjDMCpz8euqUDdiO7zOb","9hxGcYW3XBddOcW2QNrcwzrz",
            "CppWNXhOr1D2j1SXY0DuB5OH","C5tpP4CYTs9l1R5eyoShufQy","QFZEYB0t9kV1mfEAodBZm9WQ"};

}

