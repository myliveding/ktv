package com.st.ktv.controller.wechat.common;

import com.st.utils.Constant;

/**
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */
public class Configure {

	//sdk的版本号
	public static final String sdkVersion = "java sdk 1.0.1";

	//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	public static final String key = "DShengshiKTVwechat4501AToFu0N5jD";

	//微信分配的公众号ID（开通公众号之后可以获取到）
//	public static String appID = "wxbb336e8a40b636d6";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
//	public static String mchID = "1361903302";

	public  static final String notify_url=Constant.URL+"/wechat/pay/notify.do";
	
	public  static final String index_url=Constant.URL+"/wechat/pay/index.do";

}
