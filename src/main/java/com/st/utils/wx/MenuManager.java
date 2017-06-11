package com.st.utils.wx;

import com.st.ktv.entity.wx.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuManager {
	private static Logger logger =LoggerFactory.getLogger(MenuManager.class);

	/**
	 * 组装菜单数据
	 * @return
	 */
	private static Menu getMenu(String appid, String url) {
		ViewButton btn11 = new ViewButton();
		btn11.setName("在线预定");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoIndex.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");


        ViewButton btn21 = new ViewButton();
        btn21.setName("积分兑换");
        btn21.setType("view");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoIntegralMall.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		ViewButton btn22 = new ViewButton();
		btn22.setName("在线超市");//shopnowing
		btn22.setType("view");
		btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoShop.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		ViewButton btn23 = new ViewButton();
		btn23.setName("优惠活动");//discount
		btn23.setType("view");
		btn23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoDiscount.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

//		CommonButton btn22 = new CommonButton();
//		btn22.setName("积分兑换");
//		btn22.setType("click");
//		btn22.setKey("积分兑换");

		ViewButton btn31 = new ViewButton();
		btn31.setName("佣金提现");//gotoCommission
		btn31.setType("view");
		btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoCommission.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		ViewButton btn32 = new ViewButton();
		btn32.setName("我的预定");//myorder
		btn32.setType("view");
		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoMyOrder.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		ViewButton btn33 = new ViewButton();
		btn33.setName("个人中心");//usercenter
		btn33.setType("view");
		btn33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=member/gotoUserCenter.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

		//主菜单
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("在线预定");
		mainBtn1.setSub_button(new Button[] { btn11});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("走进惠机");
		mainBtn2.setSub_button(new Button[] { btn21,btn22,btn23});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("我的惠机");
		mainBtn3.setSub_button(new Button[] { btn31,btn32,btn33});

		Menu menu = new Menu();
//		menu.setButton(new Button[] { btn11, btn21, btn31 });
		menu.setButton(new Button[] { btn11, mainBtn2, mainBtn3 });
		return menu;
	}

    public static void main(String[] args) {
//        System.err.println(WeixinUtil.getAccessTokenForWXService("wxbb336e8a40b636d6","aa389c3d29c333ebdad2d50b95160d64").getToken());

        String accessToken = "t94DYDIZnGVjMCCa_ZXuMjPN2nixXBmhA6ve2bDrF85FVWxZyMFgEKsqwam_l_0JiOOvO7W5yUCLfAgow4kv-n36Oks4u1sWYsB-0rBLLS4ToKHRh-rXv_E0FO9NSTJeIRAiAEANWF";
        String url= "http://www.hui75.com";
        String appId = "wxbb336e8a40b636d6";

        System.out.println(url+" = "+appId);
        // 调用接口创建菜单
        int result =1;
        result= WeixinUtil.createMenu(getMenu(appId,url), accessToken);
        // 判断菜单创建结果
        if (0 == result) {
            logger.info("菜单创建成功！");
        }else {
            logger.info("菜单创建失败，错误码：" + result);
        }
    }

}