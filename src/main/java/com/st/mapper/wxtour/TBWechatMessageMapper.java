package com.st.mapper.wxtour;

import com.st.javabean.pojo.wxtour.TBWechatMessage;

import java.util.List;

public interface TBWechatMessageMapper {
    //获取所有有效回复
    List<TBWechatMessage> getMessageList();
    //模糊匹配
    List<TBWechatMessage> getMessageListByKeyLike(String keyStr);
    //精确查找
    TBWechatMessage getMessageListByKey(String keyStr);
    //类型查找
    List<TBWechatMessage> getMessageListByType(String type);

}