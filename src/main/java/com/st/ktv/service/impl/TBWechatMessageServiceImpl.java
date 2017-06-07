package com.st.ktv.service.impl;

import com.st.ktv.entity.TBWechatMessage;
import com.st.ktv.mapper.TBWechatMessageMapper;
import com.st.ktv.service.TBWechatMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("tBWechatMessageService")
@Transactional
public class TBWechatMessageServiceImpl implements TBWechatMessageService {

	@Resource
	TBWechatMessageMapper tbWechatMessageMapper;
	@Override
	public List<TBWechatMessage> getMessageList() {
//		List<TBWechatMessage> list=tbWechatMessageMapper.getMessageList();
//		Map<String,String> map=new HashMap<String,String>();
//		if(list.size()>0){
//			for (TBWechatMessage tbWechatMessage:list){
//				map.put(tbWechatMessage.getContent(),tbWechatMessage.getRespContent());
//			}
//		}
		return tbWechatMessageMapper.getMessageList();
	}

	@Override
	public List<TBWechatMessage> getMessageListByKeyLike(String key) {
		return tbWechatMessageMapper.getMessageListByKeyLike("%"+key+"%");
	}

	@Override
	public TBWechatMessage getMessageListByKey(String key) {
		return tbWechatMessageMapper.getMessageListByKey(key);
	}

	@Override
	public List<TBWechatMessage> getMessageListByType(String type) {
		return tbWechatMessageMapper.getMessageListByType(type);
	}
}
