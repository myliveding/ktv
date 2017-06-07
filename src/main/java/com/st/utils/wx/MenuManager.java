package com.st.utils.wx;

import com.st.ktv.entity.wx.*;
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
		btn11.setName("缴/查社保");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=personsocial/gotoindex.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

        ViewButton btn21 = new ViewButton();
        btn21.setName("无忧保介绍");
        btn21.setType("view");
//        btn21.setUrl(url+"/guanyuwomen/");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openidC.do?next="+appid+"%2Fguanyuwomen%2F&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		CommonButton btn22 = new CommonButton();
		btn22.setName("关键词回复");
		btn22.setType("click");
		btn22.setKey("关键词回复");
		CommonButton btn23 = new CommonButton();
		btn23.setName("向我提问");
		btn23.setType("click");
		btn23.setKey("向我提问");
		ViewButton btn24 = new ViewButton();
		btn24.setName("社保周刊");
		btn24.setType("view");
//		btn24.setUrl(url+"/zhuanti/");
		btn24.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openidC.do?next="+appid+"%2Fzhoukan%2F&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
//		ViewButton btn25 = new ViewButton();
		CommonButton btn25 = new CommonButton();
		btn25.setName("社保FM");
		btn25.setType("click");
		btn25.setKey("社保FM");
//		btn25.setType("view");
//		btn25.setUrl("http://m.ximalaya.com/45272362/album/3920867");
//
		ViewButton btn31 = new ViewButton();
		btn31.setName("在线咨询");
		btn31.setType("view");
		btn31.setUrl("http://joyowo.udesk.cn/im_client?web_plugin_id=23117&group_id=1608&channel=wyb");
		ViewButton btn32 = new ViewButton();
		btn32.setName("成为代理");
		btn32.setType("view");
//		btn32.setUrl(url+"/chengshidaili/");
		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openidC.do?next="+appid+"%2Fchengshidaili%2F&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		CommonButton btn33 = new CommonButton();
		btn33.setName("商务合作");
		btn33.setType("click");
		btn33.setKey("商务合作");
		ViewButton btn34 = new ViewButton();
		btn34.setName("社保计算器");
		btn34.setType("view");
		btn34.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=personsocial/gotojsq.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		ViewButton btn35 = new ViewButton();
		btn35.setName("代理商中心");
		btn35.setType("view");
		btn35.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"/scope/openid.do?next=agentCompany/goToAgentCompany.do"+appid+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("缴/查社保");
		mainBtn1.setSub_button(new Button[] { btn11});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("社保知识库");
		mainBtn2.setSub_button(new Button[] { btn21,btn22,btn23,btn24,btn25});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("在线服务");
		mainBtn3.setSub_button(new Button[] { btn31,btn32,btn33,btn34,btn35});

		/**
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 *
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
//		menu.setButton(new Button[] { btn11, btn21, btn31 });
		menu.setButton(new Button[] { btn11, mainBtn2, mainBtn3 });
		return menu;
	}

    public static void main(String[] args) {
        String accessToken = "2P5OdhA1K3dqH6eLjF1sYdovG9W6mOozTHjZnAmfaetee0SJoEgxrnU6sVdy-4KlpgdeooSdMmjpJzMiveuBGKnCHKq_OIXNutrsB9JjcLLNIKMN49q2GV65LKpWkYXhXBBcAAARVV";
        String url="";
        String appId="";

        appId="wx07ae659c009c0d6b";
        url="https://m.shebaoonline.com";

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