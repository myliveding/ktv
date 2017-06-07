package com.st.service.impl;

import com.st.utils.util.iputil.City;
import com.st.utils.util.iputil.IPUtil;
import com.st.utils.util.text.StringUtils;
import com.st.service.ArticleService;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
@Transactional
@Service("articleservie")
public class ArticleservieImpl implements ArticleService{
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private ArticleRedisHandleServiceImpl articleRedisHandleServiceImpl;
    @Override
    public JSONObject articleDemend(HttpServletRequest request,String catId, String areaName, String code, int page, int pageSize, String isHomePage, String title, String needSubcat) {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        StringBuffer key = new StringBuffer("wyb_wechat_demand");
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        String cityName = "";
        String[] arr;
        String mystr = "";

        if(StringUtils.isEmpty(areaName)){
            list.add("page_size" + pageSize);
            buffer.append("page_size=").append(pageSize);
            list.add("page" + page);
            buffer.append("&page=").append(page);
            key.append("_" + pageSize);
            key.append("_" + page);
            if(!StringUtils.isEmpty(catId)){
                key.append("_" + catId);
                key.append("_" + needSubcat);
                list.add("cat_id" + catId);
                buffer.append("&cat_id=").append(catId);
                list.add("need_subcat" + needSubcat);
                buffer.append("&need_subcat=").append(needSubcat);
                if(!StringUtils.isEmpty(title)){
                    list.add("title" + title);
                    buffer.append("&title=").append(title);
                    key.append("_" + title);
                }
            }
            if(StringUtils.isNotEmpty(code)){
                list.add("code" + code);
                buffer.append("&code=").append(code);
                key.append("_" + code);
            }
        }else{//城市不为空就代表是首页的调用
            City city = IPUtil.getCityByIP(request);
            if(city.isFlag() && StringUtils.isNotEmpty(city.getCity())){
                logger.info("根据IP获取到的信息为country：" + city.getCountry()
                        + ",city：" + city.getCity() + ",province：" + city.getProvince());
                areaName = city.getCity().replace("市", "");
                cityName = areaName;
            }else{
                cityName = "";
            }
            buffer.append("area_name=").append(cityName);
            list.add("area_name" + cityName);
            key.append("_" + cityName);
        }
        if(StringUtils.isNotEmpty(isHomePage)){
            list.add("is_home_page" + isHomePage);
            buffer.append("&is_home_page=").append(isHomePage);
            key.append("_" + isHomePage);
        }
        try {
            resultStr = articleRedisHandleServiceImpl.read(key.toString());
            logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
            if(resultStr == null){
                //文章查询
                mystr=buffer.toString();
                arr = list.toArray(new String[list.size()]);
                resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_DEMAND_URL,mystr,arr));
                if(0 == resultStr.getInt("status") && resultStr.has("data")){
                    String message = resultStr.getString("data");
                    if(message != null){
                        articleRedisHandleServiceImpl.save(key.toString(),resultStr,0);
                    }
                }
            }
            //处理首页的城市定位,返回城市名和知社保连接地址
            if(0 == resultStr.getInt("status")){
                if(resultStr.has("is_exist_city") && "0".equalsIgnoreCase(resultStr.getString("is_exist_city"))){
                    resultStr.put("cityName","");
                    resultStr.put("url","/zhishebao/");
                }else{
                    resultStr.put("cityName",cityName);
                    if(resultStr.has("city_code") && !"".equalsIgnoreCase( resultStr.getString("city_code"))){
                        resultStr.put("url","/" +  resultStr.getString("city_code") + "/");
                    }else{
                        resultStr.put("url","/zhishebao/");
                    }
                }
            }else{
                resultStr.put("cityName","");
                resultStr.put("url","/zhishebao/");
            }
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return resultStr;
    }

    @Override
    public JSONObject bannerDemend(HttpServletRequest request, String code) {
        String[] arr;
        String mystr="";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
//        StringBuffer key = new StringBuffer("wyb_wechat_bandemand");
        if (!StringUtils.isEmpty(code)) {
            list.add("code" + code);
            buffer.append("code=").append(code);
//			key.append("_" + code);
            mystr=buffer.toString();
            arr = list.toArray(new String[list.size()]);
        }else {
            logger.info("从页面获取到的code为：" + code);
            arr=new String[]{};
        }
        try {
//			resultStr = articleRedisHandleServiceImpl.read(key.toString());
//			logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
//			if(resultStr == null){
            //文章查询
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_BANNERDEMAND_URL,mystr,arr));
//				if(0 == resultStr.getInt("status") && resultStr.has("data")){
//					String message = resultStr.getString("data");
//					if(message != null){
//						articleRedisHandleServiceImpl.save(key.toString(),resultStr);
//					}
//				}
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return  resultStr;
    }

    /**
     * 文章类别查询
     *
     * @param request
     * @param code
     * @param needFeatured
     * @param excludedId
     * @param page
     * @param pageSize     @return
     */
    @Override
    public JSONObject typeDemand(HttpServletRequest request, String code, String needFeatured, String excludedId, int page, int pageSize,String sort) {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String[] arr;
        String mystr="";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        StringBuffer key = new StringBuffer("wyb_wechat_typedemand");
        if (!StringUtils.isEmpty(code)) {
            list.add("code" + code);
            buffer.append("code=").append(code);
            key.append("_" + code);
            if(StringUtils.isNotEmpty(excludedId)){
                list.add("not_contain_id" + excludedId);
                buffer.append("&not_contain_id=").append(excludedId);
                key.append("_" + excludedId);
            }
            if(StringUtils.isNotEmpty(needFeatured)){
                list.add("need_featured" + needFeatured);
                buffer.append("&need_featured=").append(needFeatured);
                key.append("_" + needFeatured);
            }

            if(StringUtils.isNotEmpty(excludedId) || StringUtils.isNotEmpty(needFeatured)){
                key.append("_" + pageSize);
                key.append("_" + page);
                list.add("page_size" + pageSize);
                buffer.append("&page_size=").append(pageSize);
                list.add("page" + page);
                buffer.append("&page=").append(page);
            }
            if (StringUtils.isNotEmpty(sort)){
                key.append("_" + sort);
                list.add("sort" + sort);
                buffer.append("&sort=").append(sort);
            }
            mystr=buffer.toString();
            arr = list.toArray(new String[list.size()]);
        }else {
            logger.info("从页面获取到的code为：" + code);
            arr=new String[]{};
        }
        try {
            resultStr = articleRedisHandleServiceImpl.read(key.toString());
            logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
            if(resultStr == null){
                //文章查询
                resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_TYPEDEMAND_URL,mystr,arr));
                if(0 == resultStr.getInt("status") && resultStr.has("data")){
                    String message = resultStr.getString("data");
                    if(message != null){
                        articleRedisHandleServiceImpl.save(key.toString(),resultStr,0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 所有文章类别查询
     *
     * @param request
     * @param code
     * @return
     */
    @Override
    public JSONObject typeAll(HttpServletRequest request, String code) {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String[] arr;
        String mystr="";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        StringBuffer key = new StringBuffer("wyb_wechat_typeall");
        if (!StringUtils.isEmpty(code)) {
            list.add("code" + code);
            buffer.append("code=").append(code);
            key.append("_" + code);
            mystr=buffer.toString();
            arr = list.toArray(new String[list.size()]);
        }else {
            logger.info("从页面获取到的code为：" + code);
            arr=new String[]{};
        }
        try {
            resultStr = articleRedisHandleServiceImpl.read(key.toString());
            logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
            if(resultStr == null){
                //文章查询
                resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_TYPEALL_URL,mystr,arr));
                if(0 == resultStr.getInt("status") && resultStr.has("data")){
                    String message = resultStr.getString("data");
                    if(message != null){
                        articleRedisHandleServiceImpl.save(key.toString(),resultStr,0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 参保城市分级列表查询
     *
     * @return
     */
    @Override
    public JSONObject cityLevelRedis() {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String key="wyb_wechat_citylevel";
        try {
            resultStr = articleRedisHandleServiceImpl.read(key);
            logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
            if(resultStr == null){
                //文章查询
                resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_CITYLEVELDEMAND_URL, "", null));
                if(0 == resultStr.getInt("status") && resultStr.has("data")){
                    String message = resultStr.getString("data");
                    if(message != null){
                        articleRedisHandleServiceImpl.save(key,resultStr,0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return resultStr;
    }
    /**
     *  参保城市列表查询
     *
     * @return
     */
    @Override
    public JSONObject cityRedis() {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String key="wyb_wechat_allCity";
        try {
            resultStr = articleRedisHandleServiceImpl.read(key);
            logger.info("redis缓存的返回值，key为："  + key + ",value：" + resultStr);
            if(resultStr == null){
                //文章查询
                resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONINSURANCE_ALLCITY_URL, "", null));
                if(0 == resultStr.getInt("status") && resultStr.has("data")){
                    String message = resultStr.getString("data");
                    if(message != null){
                        articleRedisHandleServiceImpl.save(key,resultStr,0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 文章查询
     *
     * @param catId
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public JSONObject search(String catId, String title, int page, int pageSize) {
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String[] arr;
        String mystr = "";
        if(StringUtils.isNotEmpty(catId) && StringUtils.isNotEmpty(title)){
            arr = new String[] { "cat_id" + catId, "title" + title, "page_size" + pageSize,"page" + page };
            mystr = "cat_id=" + catId  + "&title=" + title + "&page_size=" + pageSize + "&page="+ page;
        }else{
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"搜索关键字或者栏目id为空\"}");
        }
        try {
            //文章查询
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_SEARCH_URL,mystr,arr));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        return  resultStr;
    }


}
