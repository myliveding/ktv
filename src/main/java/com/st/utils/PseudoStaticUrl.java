package com.st.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.st.utils.util.text.StringUtils;
import com.st.javabean.pojo.wxtour.PseudoStatic;
import com.st.service.impl.PseudoStaticServiceImpl;

/**
 * @Title	StaticUrl
 * @Desc	单例获取数据库中的伪静态对象
 * @author	dingzr
 */
public class PseudoStaticUrl {
	private static Logger logger = LoggerFactory.getLogger(PseudoStaticUrl.class);
	
    private volatile static PseudoStaticUrl instance;
    
    private static List<PseudoStatic> urlList = new ArrayList<PseudoStatic>();
    
    /**
     * 获取伪静态对象
     */
    private PseudoStaticUrl() {
        try {
        	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        	PseudoStaticServiceImpl service = (PseudoStaticServiceImpl) context.getBean("pseudoStaticService");
        	urlList = service.selectAll();
        } catch (Exception e1) {
			logger.error("获取伪静态对象出现异常！"+e1.getMessage(),e1);
        }
    }

    /**
     * @Desc 单例获取
     * @return instance
     */
	public static PseudoStaticUrl getInstance() {
        if (instance == null) {
        	synchronized (PseudoStaticUrl.class) {
                if (instance == null) {
                	try{
    	        		instance = new PseudoStaticUrl();
    	        		logger.info("从单例中获取时,使用一次调用就打印一次");
    	        	}catch (Exception e) {
    	        		logger.info("单例进行实例化时出错" + e.getMessage());
    				}
                } 
            } 
        }
        return instance;
    }
		
	/**
     * @Desc 定时器定时去更新或者刷新缓存
     * @return instance
     */
	public static PseudoStaticUrl updateInstance() {
        return (new PseudoStaticUrl());
    }
	
	/**
     * 返回获取到的静态url
     * @return  
     */  
    public List<PseudoStatic> getStaticUrl(){  
        return urlList;  
    }  
	
    /**
     * @Title getSeourlByRealurl
     * @Desc 根据真实的url去获取对应的seoUrl
     * @param realUrl
     * @return String
     */
	public static String getSeourlByRealurl(String realUrl){
    	if(null != realUrl && !"".equals(realUrl)){
    		for (PseudoStatic pseudoStatic : PseudoStaticUrl.getInstance().getStaticUrl()) {
    			if(pseudoStatic.getDescription()
						.contains(PropertiesUtils.findPropertiesKey("ARTICLE_DESC",Constant.SEO_FILE_NAME))){//文章详情
    				if(realUrl.startsWith(PropertiesUtils.findPropertiesKey("ARTICLE_INFO_DESC",Constant.SEO_FILE_NAME))){
    					String[] temp = realUrl.split("=");
        				String seoUrl = getSeo(temp[temp.length-1]);
        				String artId = StringUtils.getValue(realUrl,pseudoStatic.getParam());
    					return "/" + seoUrl + "_" + artId + "/";
    				}
				}else{
					if(realUrl.startsWith(pseudoStatic.getRealUrl())){
						return pseudoStatic.getSeoUrl();
					}
    			}
			}
    	}else{
    		logger.info("真实的url为空");
    	}
    	return realUrl;
    }
	
	/**
	 * 查找文章详情的对应二级域名
	 * @param param
	 * @return
	 */
	public static String getSeo(String param){
		String res = "";
		String codes = PropertiesUtils.findPropertiesKey("ARTICLE_INFO_PARAM", Constant.SEO_FILE_NAME);
		if(StringUtils.isNotEmpty(codes)){//code不为空
			String[] code = codes.split(",");
			for (int i=0;i<code.length;i++) {
				if(param.equalsIgnoreCase(code[i])){
					res = PropertiesUtils.findPropertiesKey(code[i].toUpperCase(), Constant.SEO_FILE_NAME);
					break; 
				}
			}
		}
		return res;
	}
}
