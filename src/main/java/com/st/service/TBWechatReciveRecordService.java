package com.st.service;

import com.st.javabean.pojo.wxtour.TBWechatReciveRecord;

import java.util.List;

public interface TBWechatReciveRecordService {
	public int insert(TBWechatReciveRecord record);
	public int insertSelective(TBWechatReciveRecord record);
	public TBWechatReciveRecord selectByPrimaryKey(Integer id);

	public int updateByPrimaryKeySelective(TBWechatReciveRecord record);
	public int updateByPrimaryKey(TBWechatReciveRecord record);

	public List<TBWechatReciveRecord> selectByOpenid(String openid);
}
