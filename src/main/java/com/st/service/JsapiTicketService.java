package com.st.service;

import com.st.javabean.pojo.wxtour.JsapiTicket;

public interface JsapiTicketService {
	public int insertSelective(JsapiTicket record);
	public JsapiTicket selectByPrimaryKey(String appid);
	public int updateByPrimaryKeySelective(JsapiTicket record);
}
