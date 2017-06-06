package com.st.service.impl;

import com.st.constant.Constant;
import com.st.core.util.date.DateUtil;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.wxtour.PseudoStatic;
import com.st.javabean.pojo.wxtour.TBArticleStatic;
import com.st.mapper.wxtour.PseudoStaticMapper;
import com.st.mapper.wxtour.TBArticleStaticMapper;
import com.st.service.PseudoStaticService;
import com.st.utils.JoYoUtil;
import com.st.utils.PropertiesUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("pseudoStaticService")
public class PseudoStaticServiceImpl implements PseudoStaticService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Resource
	private PseudoStaticMapper pseudoStaticMapper;
	
	@Resource
	private TBArticleStaticMapper articleStaticMapper;
	
	
	List<PseudoStatic> pseudoTemp = new ArrayList<PseudoStatic>();
	List<TBArticleStatic> articTemp = new ArrayList<TBArticleStatic>();
	
	/**
	 * @Title selectAll
	 * @Description 获取所有的伪静态配置信息集合
	 * @return List<PseudoStatic>
	 */
	public List<PseudoStatic> selectAll(){
		List<PseudoStatic> resList = pseudoStaticMapper.selectAll();
		logger.info("从JAVA数据库中获取非文章栏目的伪静态条数为：" + resList.size() + ",时间为：" + DateUtil.getNowStringTime());
		List<PseudoStatic> list = getStaticUrlByPhp();
		resList.addAll(list);
		return resList;
	}
	
	/**
	 * 处理去获取文章栏目模块的伪静态
	 * @return List<PseudoStatic>
	 */
	public List<PseudoStatic> getStaticUrlByPhp(){
		List<PseudoStatic> list = new ArrayList<PseudoStatic>();
		List<TBArticleStatic> articList = new ArrayList<TBArticleStatic>();
		
		try {
			String codes = PropertiesUtils.findPropertiesKey("ARTICLE_CODE", Constant.SEO_FILE_NAME);
			if(StringUtils.isNotEmpty(codes)){//code不为空
				String[] arr = {"code" + codes};
				String mystr="code=" + codes;
				JSONObject resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.ARTICLE_STATIC_URL,mystr,arr));
				String getData = resultStr.getString("data");
				if(0 == resultStr.getInt("status") && getData != null && !"".equals(getData)){
					JSONObject res = JSONObject.fromObject(getData);
					
					//循环取返回的json里面的各个大类，并进行相关的处理
					String[] code = codes.split(",");
					for (int i=0;i<code.length;i++) {
						String param = PropertiesUtils.findPropertiesKey(code[i].toUpperCase(), Constant.SEO_FILE_NAME);
						if(res.has(code[i])){
							JSONArray array = res.getJSONArray(code[i]);
							articTemp.clear();
							pseudoTemp.clear();
							handelFirstJson(array, "", param,"","");
							list.addAll(pseudoTemp);
							articList.addAll(articTemp);
						}
					}
				}else{
					logger.info("调用PHP获取文章大分类失败或者获取到的为空status：" + resultStr.getInt("status") + ",data：" + getData + ",code为：" + codes);
				}
				
			}else{
				logger.info("配置文件获取到的参数不正确codes：" + codes);
			}
		} catch (Exception e) {
			logger.error("调用PHP获取文章栏目分类出现异常" + e.getMessage(),e);
		}
		
		return updateDatabase(list, articList);
	}
	
	
	public void handelFirstJson(JSONArray array, String codePre, String param, String idPre, String namePre){
		for (int i = 0; i < array.size(); i++) {
			handelJson(JSONObject.fromObject(array.get(i)), codePre, param,idPre,namePre);
		}
	}
	
	/**
	 * 循环处理json
	 * @param msg
	 * @param codePre
	 * @param param
	 * @return StaticurlUtil
	 */
	public void handelJson(JSONObject msg,String codePre,String param, String idPre, String namePre){
			
			String catId = msg.getString("cat_id");
			String code = msg.getString("code");
			String catName = msg.getString("cat_name");

			if(null != catId && !"".equals(catId)
					&& null != code && !"".equals(code) 
					&& null != catName && !"".equals(catName)){

				
				//修改生成SEO的地址
				String codeTemp =  code;
				String idTemp = catId;
				String nameTemp = catName;
				String realUrl = "";
				if(null != codePre && !"".equals(codePre)){
					realUrl = PropertiesUtils.findPropertiesKey("REAL_URL", Constant.SEO_FILE_NAME)
							.replace("CAT_ID", idPre)
							.replace("SORT", catId)
							.replace("CODE", codePre)
							.replace("CAT_NAME", namePre)
							.replace("LAST_NAME", catName)
							.replace("PARAM", param);
					code = "/" + codePre + "_" + code + "/" ;
				}else{
					realUrl = PropertiesUtils.findPropertiesKey("REAL_URL", Constant.SEO_FILE_NAME)
							.replace("CAT_ID", catId)
							.replace("SORT", catId)
							.replace("CODE", codeTemp)
							.replace("CAT_NAME", catName)
							.replace("LAST_NAME", "")
							.replace("PARAM", param);
					code = "/" + code + "/" ;
				}
				
				//组装文章分类表的对象
				TBArticleStatic art = new TBArticleStatic();
				art.setCatId(catId);
				art.setCode(code);
				art.setCatName(catName);
				art.setUrl(realUrl);
				articTemp.add(art);
				
				//把文章分类获取到的信息组装到list中
				PseudoStatic st = new PseudoStatic();
				st.setSeoUrl(code);
				st.setRealUrl(realUrl);
				st.setDescription("栏目");
				pseudoTemp.add(st);
				
				if(msg.has("article_sub_cat") && msg.getString("article_sub_cat") != null && !"".equals(msg.getString("article_sub_cat"))){
					JSONArray array = msg.getJSONArray("article_sub_cat");
					handelFirstJson(array,codeTemp,param, idTemp, nameTemp);
				}
			}	
	} 
	
	
	/**
	 * 更新本地数据库和PHP异常时从我们数据库中获取数据
	 * @param list
	 * @param articList
	 * @return
	 */
	public List<PseudoStatic> updateDatabase(List<PseudoStatic> list, List<TBArticleStatic> articList){
		//下面的两个list是同步的，要是有值都有，没有就都没有
		//把获取到的栏目分类插入到我们自己的表中,需要先删除在添加
		if(articList.size() > 0){
			try{
				articleStaticMapper.deleteAll();
				for (TBArticleStatic tbArticleStatic : articList) {
					articleStaticMapper.insert(tbArticleStatic);
				}
			}catch (Exception e) {
				logger.info("把获取到的栏目分类插入到我们自己的表中(先删除在添加)出错：" + e.getMessage());
			}
		}
		//从PHP后台取不到的文章栏目集合，为了不影响系统使用，我们在这里去读取备份的数据
		if(list.size() <= 0){//没有值去我们自己的数据库中备份的表中取
			try{
				List<TBArticleStatic> backList = articleStaticMapper.selectAll();
				for (TBArticleStatic tbArticleStatic : backList) {
					//把文章分类获取到的信息组装到list中
					PseudoStatic p = new PseudoStatic();
					p.setSeoUrl(tbArticleStatic.getCode());
					p.setRealUrl(tbArticleStatic.getUrl());
					p.setDescription("分类");
					list.add(p);
				}
				logger.info("从JAVA备份数据表中获取到的文章分类态条数为：" + list.size() + ",时间为：" + DateUtil.getNowStringTime());
			}catch (Exception e) {
				logger.error("读取JAVA备份数据表中的文章分类出错：" + e.getMessage(),e);
			}
		}else{
			logger.info("从PHP数据库中获取到的文章分类态条数为：" + list.size() + ",时间为：" + DateUtil.getNowStringTime());
		}
		
		return list;
	}
	
}
