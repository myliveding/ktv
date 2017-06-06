package com.st.service;

import java.util.List;

public interface IBaseService<T> {
	
	
	public List<T> getList(T vo);
	
	public int getListCount(T vo);
	/**
	 * 根据id获取列表
	 * 
	 * @param pojo
	 * @return
	 */
	public T selectByPrimaryKey(T vo);
	/**
	 * 根据id删除
	 * 
	 * @param id
	 */
	public String delete(T vo);

	/**
	 * 新增产品
	 * 
	 * @param FeedbackDefineVO
	 */
	public String insert(T vo);

	/**
	 * 更新产品
	 * 
	 * @param FeedbackDefineVO
	 */
	public String update(T vo);
}
