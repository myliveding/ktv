package com.st.service;

import com.st.javabean.pojo.wxtour.TBWechat;

public interface TBWechatService {
	public int insert(TBWechat record);
	public int insertSelective(TBWechat record);
	public TBWechat selectByPrimaryKey(String appid);
	public int updateByPrimaryKeySelective(TBWechat record);
	public int updateByPrimaryKey(TBWechat record);
}
