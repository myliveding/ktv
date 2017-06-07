package com.st.ktv.service;

import com.st.ktv.entity.TBAttribute;

public interface TBAttributeService {
   
	/**
     * @Title selectByKey
     * @Description 根据key查询对象信息
     * @return TBAttribute
     */
    TBAttribute selectByKey(String key);
    
    /**
     * @Title updateUserNum
     * @Description 根据key更新
     */
    public void updateUserNum();
}
