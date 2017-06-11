package com.st.ktv.mapper;

import com.st.ktv.entity.WechatMember;

public interface WechatMemberMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WechatMember record);

    int insertSelective(WechatMember record);

    WechatMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatMember record);

    int updateByPrimaryKey(WechatMember record);

    WechatMember getObjectByOpenid(String openid);

    WechatMember getObjectByMobile(String mobile);

}