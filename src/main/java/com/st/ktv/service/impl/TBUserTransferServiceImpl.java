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
		// TODO Auto-generated method stub
		return tBUserTransferMapper.insert(record);
	}

	@Override
	public int insertSelective(TBUserTransfer record) {
		// TODO Auto-generated method stub
		return tBUserTransferMapper.insertSelective(record);
	}

	@Override
	public TBUserTransfer selectByPrimaryKey(String openid) {
		// TODO Auto-generated method stub
		return tBUserTransferMapper.selectByPrimaryKey(openid);
	}

	@Override
	public int updateByPrimaryKeySelective(TBUserTransfer record) {
		// TODO Auto-generated method stub
		return tBUserTransferMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TBUserTransfer record) {
		// TODO Auto-generated method stub
		return tBUserTransferMapper.updateByPrimaryKey(record);
	}

}
