package com.st.ktv.service;

import com.st.ktv.entity.TBWechatMessage;

import java.util.List;

public interface TBWechatMessageService {
	public List<TBWechatMessage> getMessageList();
	public List<TBWechatMessage> getMessageListByKeyLike(String key);
	public TBWechatMessage getMessageListByKey(String key);
	public List<TBWechatMessage> getMessageListByType(String type);

}
