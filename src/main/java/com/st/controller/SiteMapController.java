package com.st.controller;

import com.st.service.ArticleService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/siteMap")
public class SiteMapController {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	@Resource
	private ArticleService articleService;

  /**
	 * 跳转到站点地图
	 * @param request
	 * @return object
	 */
	@RequestMapping("/maps")
	public String map(HttpServletRequest request) {
		JSONObject jsonObjectzk=articleService.articleDemend(request,"181",null,null,1,50,null,null,null);//社保周刊
		JSONObject jsonObjectzx=articleService.typeAll(request,"zhishebao");//社保资讯
		JSONObject jsonObjectcx=articleService.cityRedis();//社保查询
		JSONObject jsonObjectqa=articleService.typeDemand(request,"qa",null,null,1,100,null);//社保查询
		request.setAttribute("zk",jsonObjectzk);
		request.setAttribute("zx",jsonObjectzx);
		request.setAttribute("cx",jsonObjectcx);
		request.setAttribute("qa",jsonObjectqa);
		return "maps/maps";
	}
}
