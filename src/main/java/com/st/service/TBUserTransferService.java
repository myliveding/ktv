package com.st.service;

import com.st.javabean.pojo.wxtour.TBUserTransfer;

public interface TBUserTransferService {
	int insert(TBUserTransfer record);

    int insertSelective(TBUserTransfer record);

    TBUserTransfer selectByPrimaryKey(String openid);

    int updateByPrimaryKeySelective(TBUserTransfer record);

    int updateByPrimaryKey(TBUserTransfer record);
}
