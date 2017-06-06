package com.st.service;

import com.st.javabean.pojo.wxtour.TBWechatMessage;

import java.util.List;

public interface TBWechatMessageService {
	public List<TBWechatMessage> getMessageList();
	public List<TBWechatMessage> getMessageListByKeyLike(String key);
	public TBWechatMessage getMessageListByKey(String key);
	public List<TBWechatMessage> getMessageListByType(String type);

}
