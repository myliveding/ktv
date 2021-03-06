package com.st.ktv.service.impl;

import com.st.ktv.entity.TBUserTransfer;
import com.st.ktv.mapper.TBUserTransferMapper;
import com.st.ktv.service.TBUserTransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("tBUserTransferService")
@Transactional
public class TBUserTransferServiceImpl implements TBUserTransferService {
	@Resource
	private TBUserTransferMapper tBUserTransferMapper;

	@Override
	public int insert(TBUserTransfer record) {
		return tBUserTransferMapper.insert(record);
	}

	@Override
	public int insertSelective(TBUserTransfer record) {
		return tBUserTransferMapper.insertSelective(record);
	}

	@Override
	public TBUserTransfer selectByPrimaryKey(String openid) {
		return tBUserTransferMapper.selectByPrimaryKey(openid);
	}

	@Override
	public int updateByPrimaryKeySelective(TBUserTransfer record) {
		return tBUserTransferMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TBUserTransfer record) {
		return tBUserTransferMapper.updateByPrimaryKey(record);
	}

}
