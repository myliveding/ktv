package com.st.ktv.service;

import com.st.ktv.entity.TBUserTransfer;

public interface TBUserTransferService {
	int insert(TBUserTransfer record);

    int insertSelective(TBUserTransfer record);

    TBUserTransfer selectByPrimaryKey(String openid);

    int updateByPrimaryKeySelective(TBUserTransfer record);

    int updateByPrimaryKey(TBUserTransfer record);
}
