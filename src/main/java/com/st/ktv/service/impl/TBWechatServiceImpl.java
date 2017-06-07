package com.st.ktv.service.impl;

import com.st.ktv.entity.TBWechat;
import com.st.ktv.mapper.TBWechatMapper;
import com.st.ktv.service.TBWechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("tBWechatService")
@Transactional
public class TBWechatServiceImpl implements TBWechatService {

	@Autowired
	TBWechatMapper tBWechatMapper;
	
	@Override
	public int insert(TBWechat record) {
		return tBWechatMapper.insert(record);
	}

	@Override
	public int insertSelective(TBWechat record) {
		return tBWechatMapper.insertSelective(record);
	}

	@Override
	public TBWechat selectByPrimaryKey(String appid) {
		return tBWechatMapper.selectByPrimaryKey(appid);
	}

	@Override
	public int updateByPrimaryKeySelective(TBWechat record) {
		return tBWechatMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TBWechat record) {
		return tBWechatMapper.updateByPrimaryKey(record);
	}

}
