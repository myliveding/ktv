package com.st.ktv.service;

import com.st.ktv.entity.TBWechatReciveRecord;

import java.util.List;

public interface TBWechatReciveRecordService {
	public int insert(TBWechatReciveRecord record);
	public int insertSelective(TBWechatReciveRecord record);
	public TBWechatReciveRecord selectByPrimaryKey(Integer id);

	public int updateByPrimaryKeySelective(TBWechatReciveRecord record);
	public int updateByPrimaryKey(TBWechatReciveRecord record);

	public List<TBWechatReciveRecord> selectByOpenid(String openid);
}
