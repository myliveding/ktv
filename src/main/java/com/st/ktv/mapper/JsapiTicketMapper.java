package com.st.ktv.mapper;

import com.st.ktv.entity.JsapiTicket;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface JsapiTicketMapper {

    int deleteByPrimaryKey(String appid);

    int insert(JsapiTicket record);

    int insertSelective(JsapiTicket record);

    JsapiTicket selectByPrimaryKey(String appid);

    int updateByPrimaryKeySelective(JsapiTicket record);

    int updateByPrimaryKey(JsapiTicket record);

}