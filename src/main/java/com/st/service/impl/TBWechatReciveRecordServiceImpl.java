package com.st.service.impl;

import com.st.javabean.pojo.wxtour.TBWechatReciveRecord;
import com.st.mapper.wxtour.TBWechatReciveRecordMapper;
import com.st.service.TBWechatReciveRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tBWechatReciveRecordService")
@Transactional
public class TBWechatReciveRecordServiceImpl implements TBWechatReciveRecordService {
	private static Logger logger = LoggerFactory.getLogger(TBWechatReciveRecordServiceImpl.class);
	@Autowired
	public TBWechatReciveRecordMapper tbWechatReciveRecordMapper;


	public int insert(TBWechatReciveRecord record) {
		return tbWechatReciveRecordMapper.insert(record);
	}

	public int insertSelective(TBWechatReciveRecord record) {
		return tbWechatReciveRecordMapper.insertSelective(record);
	}

	public TBWechatReciveRecord selectByPrimaryKey(Integer id) {
		return tbWechatReciveRecordMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(TBWechatReciveRecord record) {
		return tbWechatReciveRecordMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(TBWechatReciveRecord record) {
		return tbWechatReciveRecordMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<TBWechatReciveRecord> selectByOpenid(String openid) {
		return tbWechatReciveRecordMapper.selectByOpenid(openid);
	}
}
