package com.st.ktv.mapper;

import com.st.ktv.entity.TBWechatReciveRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TBWechatReciveRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TBWechatReciveRecord record);

    int insertSelective(TBWechatReciveRecord record);

    TBWechatReciveRecord selectByPrimaryKey(Integer id);

    List<TBWechatReciveRecord> selectByOpenid(String openid);

    int updateByPrimaryKeySelective(TBWechatReciveRecord record);

    int updateByPrimaryKey(TBWechatReciveRecord record);
}