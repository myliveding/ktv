package com.st.service;

import java.util.List;

import com.st.javabean.pojo.wxtour.PseudoStatic;

public interface PseudoStaticService {
	
	/**
	 * @Title selectAll
	 * @Description 获取所有的伪静态配置信息集合
	 * @param
	 * @return List<PseudoStatic>
	 */
	public List<PseudoStatic> selectAll();
}
