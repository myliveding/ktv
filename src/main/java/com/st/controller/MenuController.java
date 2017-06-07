package com.st.controller;

import com.st.utils.Constant;
import com.st.core.javabean.Result;
import com.st.javabean.pojo.*;
import com.st.service.WeixinAPIService;
import com.st.utils.ContextHolderUtils;
import com.st.utils.WeixinUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private WeixinAPIService weixinAPIService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = ContextHolderUtils.getSession();
		String appid = request.getParameter("appid");
		// 判断参数是否正确
		if (!"".equals(appid) && appid != null) {
			session.setAttribute("appid", appid);
		}
		Object appidObj = session.getAttribute("appid");
		if ("".equals(appidObj) || appidObj == null) {
			request.setAttribute("error", "数据异常");
			logger.error("菜单管理，数据异常");
			return "redirect:error.do";
		}
		return "user/info";
	}
	@RequestMapping("/getmenu")
	public @ResponseBody Result getmenu(HttpServletRequest request) {
		Result result = new Result();
		result.setSuccess(false);
		try {
			Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
	        String accessToken = weixin.getAccessToken();
			JSONObject  jSONObject =WeixinUtil.getMenu(accessToken);
			Object obj= jSONObject.get("menu");
			try {
				result.setObject(obj);
				result.setSuccess(true);
			} catch (Exception e) {
				logger.error("获取数据出错:" + e.getMessage(), e);
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		return result;
	}
	@RequestMapping("/update")
	public @ResponseBody Result update(HttpServletRequest request) {
		int i=1;
		int j=0;
		ViewButton[] buttons=new ViewButton[18];
		
		while(i<=53){
			ViewButton button=new ViewButton();
			String name=request.getParameter("button_name"+i);
			String view=request.getParameter("button_view"+i);
			String click=request.getParameter("button_click"+i);
			button.setName(name);
			if ("".equals(view)||view==null){
				button.setType("click");
				button.setUrl(click);
			}else {
				button.setType("view");
				button.setUrl(view);
			}
			buttons[j]=button;
			j++;
			if(i%10==3){
				i=i+8;
			}else{
				i=i+1;
			}
		}
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName(buttons[0].getName());
		mainBtn1.setSub_button(new Button[] { buttons[3], buttons[6], buttons[9], buttons[12], buttons[15] });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName(buttons[0].getName());
		mainBtn2.setSub_button(new Button[] { buttons[4], buttons[7], buttons[10], buttons[13], buttons[16] });

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName(buttons[0].getName());
		mainBtn3.setSub_button(new Button[] { buttons[5],buttons[8], buttons[11] , buttons[14] , buttons[17] });

		/**
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 *
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		
		
		Result result = new Result();
		result.setSuccess(false);
		try {
			Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
	        String accessToken = weixin.getAccessToken();
			int res = WeixinUtil.createMenu(menu, accessToken);
			try {
				result.setObject(res);
				result.setSuccess(true);
			} catch (Exception e) {
				logger.error("获取数据出错:" + e.getMessage(), e);
			}
		} catch (Exception e) {
			logger.error("获取数据出错:" + e.getMessage(), e);
		}
		return result;
	}
	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Menu getMenu() {
		ViewButton btn11 = new ViewButton();
		btn11.setName("我的周边");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/mapopenid.do?next=jsp/map/list.dowxf992427e5a93dd60&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

		ViewButton btn12 = new ViewButton();
		btn12.setName("临安推荐");
		btn12.setType("view");
		btn12.setUrl("http://www.xundatong.net/latourweb/info/index.do");
		
		ViewButton btn13 = new ViewButton();
		btn13.setName("景点导览");
		btn13.setType("view");
		btn13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/openid.do?next=map/scenicallmap.dowxf992427e5a93dd60&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
		
		ViewButton btn14 = new ViewButton();
		btn14.setName("城市服务");
		btn14.setType("view");
		btn14.setUrl("http://www.xundatong.net/latourweb/jsp/service/service.jsp");
		
		CommonButton btn15 = new CommonButton();
		btn15.setName("人工客服");
		btn15.setType("click");
		btn15.setKey("在线客服");
		
			
		


		ViewButton btn21 = new ViewButton();
		btn21.setName("临安e游");
		btn21.setType("view");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/openid.do?next=user/passport.dowxf992427e5a93dd60&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

		ViewButton btn22 = new ViewButton();
		btn22.setName("微会议");
		btn22.setType("view");
		btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=conference/index.dowxf992427e5a93dd60CATEG1&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

		ViewButton btn23 = new ViewButton();
		btn23.setName("微活动");
		btn23.setType("view");
		btn23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/openid.do?next=guess/index.dowxf992427e5a93dd60&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

		ViewButton btn24 = new ViewButton();
		btn24.setName("临安微信厅");
		btn24.setType("view");
		btn24.setUrl("http://www.xundatong.net/latourweb/hall/index.do?categoryid=1&areaid=1");

		CommonButton btn25 = new CommonButton();
		btn25.setName("摄影地图");
		btn25.setType("click");
		btn25.setKey("25");

		ViewButton btn31 = new ViewButton();
		btn31.setName("景点门票");
		btn31.setType("view");
		btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=product/index.dowxf992427e5a93dd60CATEG1&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		ViewButton btn32 = new ViewButton();
		btn32.setName("酒店预订");
		btn32.setType("view");
		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=product/index.dowxf992427e5a93dd60CATEG2&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		ViewButton btn33 = new ViewButton();
		btn33.setName("农家乐");
		btn33.setType("view");
		btn33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=product/index.dowxf992427e5a93dd60CATEG3&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		ViewButton btn34 = new ViewButton();
		btn34.setName("临安特产");
		btn34.setType("view");
		btn34.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=product/index.dowxf992427e5a93dd60CATEG4&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		ViewButton btn35 = new ViewButton();
		btn35.setName("特惠路线");
		btn35.setType("view");
		btn35.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf992427e5a93dd60&redirect_uri=http://www.xundatong.net/latourweb/scope/shopopenid.do?next=product/index.dowxf992427e5a93dd60CATEG5&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("微游临安");
		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13, btn14, btn15 });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("乐友临安");
		mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("临安e购");
		mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33 , btn34 , btn35 });

		/**
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 *
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
	}
	
	@RequestMapping("/updatemenu")
	public @ResponseBody Menu getMenu(HttpServletRequest request){
		int i=1;
		int j=0;
		ViewButton[] buttons=new ViewButton[18];
		ViewButton button=new ViewButton();
		while(i<=53){
			String name=request.getParameter("button_name"+i);
			String view=request.getParameter("button_view"+i);
			String click=request.getParameter("button_click"+i);
			button.setName(name);
			if (view==null||"".equals(view)) {
				button.setType("click");
				button.setUrl(click);
			}else {
				button.setType("view");
				button.setUrl(view);
			}
			buttons[j]=button;
			if(i%10==3){
				i=i+8;
			}else{
				i=i+1;
			}
		}

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName(buttons[0].getName());
		mainBtn1.setSub_button(new Button[] { buttons[3], buttons[6], buttons[9], buttons[12], buttons[15] });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName(buttons[0].getName());
		mainBtn2.setSub_button(new Button[] { buttons[4], buttons[7], buttons[10], buttons[13], buttons[16] });

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName(buttons[0].getName());
		mainBtn3.setSub_button(new Button[] { buttons[5],buttons[8], buttons[11] , buttons[14] , buttons[17] });

		/**
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 *
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		
		return menu;
	}
}
