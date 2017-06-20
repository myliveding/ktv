package com.st.ktv.mapper;

import com.st.ktv.entity.WechatMember;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface WechatMemberMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WechatMember record);

    int insertSelective(WechatMember record);

    WechatMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatMember record);

    int updateByPrimaryKey(WechatMember record);

    WechatMember getObjectByOpenid(String openid);

    WechatMember getObjectByMobile(String mobile);

    int updateHeadPortrait(Map<String, Object> map);

}