package com.st.service.impl;

import com.st.javabean.pojo.wxtour.TBWechat;
import com.st.mapper.wxtour.TBWechatMapper;
import com.st.service.TBWechatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("tBWechatService")
@Transactional
public class TBWechatServiceImpl implements TBWechatService {

	@Resource
	TBWechatMapper tBWechatMapper;
	
	@Override
	public int insert(TBWechat record) {
		// TODO Auto-generated method stub
		return tBWechatMapper.insert(record);
	}

	@Override
	public int insertSelective(TBWechat record) {
		// TODO Auto-generated method stub
		return tBWechatMapper.insertSelective(record);
	}

	@Override
	public TBWechat selectByPrimaryKey(String appid) {
		// TODO Auto-generated method stub
		return tBWechatMapper.selectByPrimaryKey(appid);
	}

	@Override
	public int updateByPrimaryKeySelective(TBWechat record) {
		// TODO Auto-generated method stub
		return tBWechatMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TBWechat record) {
		// TODO Auto-generated method stub
		return tBWechatMapper.updateByPrimaryKey(record);
	}

}
