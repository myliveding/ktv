package com.st.ktv.service.impl;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class TaskImpl {
	
	/**
	 * @Title updateInstance
	 * @Description 更新缓存
	 */
	public void updateInstance(){
	}
	
	/**
	 * @Title updateUserNum
	 * @Description 更新用户统计数量
	 */
	public void updateUserNum(){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TBAttributeServiceImpl attributeService = (TBAttributeServiceImpl) context.getBean("attributeService");
		attributeService.updateUserNum();
	}
}
