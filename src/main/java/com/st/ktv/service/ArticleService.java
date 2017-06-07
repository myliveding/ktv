package com.st.ktv.service;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface ArticleService {
    /**
     * 文章查询
     * @param request
     * @return
     */
    JSONObject articleDemend(HttpServletRequest request,String catId, String areaName, String code, int page, int pageSize, String isHomePage, String title, String needSubcat);
    /**
     * 首页banner查询
     * @param request
     * @return
     */
    JSONObject bannerDemend(HttpServletRequest request,String code);
    /**
     * 文章类别查询
     * @param request
     * @return
     */
    JSONObject typeDemand(HttpServletRequest request,String code,String needFeatured,String excludedId,int page, int pageSize,String sort);
    /**
     * 所有文章类别查询
     * @param request
     * @return
     */
    JSONObject typeAll(HttpServletRequest request,String code);
    /**
     * 参保城市分级列表查询
     * @return
     */
    JSONObject cityLevelRedis();

    /**
     * 文章查询
     * @return
     *  */
    JSONObject search(String catId, String title, int page, int pageSize);
}
