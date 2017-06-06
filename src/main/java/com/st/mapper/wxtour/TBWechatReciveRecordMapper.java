package com.st.mapper.wxtour;

import com.st.javabean.pojo.wxtour.TBWechatReciveRecord;

import java.util.List;

public interface TBWechatReciveRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TBWechatReciveRecord record);

    int insertSelective(TBWechatReciveRecord record);

    TBWechatReciveRecord selectByPrimaryKey(Integer id);

    List<TBWechatReciveRecord> selectByOpenid(String openid);

    int updateByPrimaryKeySelective(TBWechatReciveRecord record);

    int updateByPrimaryKey(TBWechatReciveRecord record);
}