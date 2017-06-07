package com.st.ktv.service;

import com.st.ktv.entity.wx.JsapiTicket;

public interface JsapiTicketService {
	public int insertSelective(JsapiTicket record);
	public JsapiTicket selectByPrimaryKey(String appid);
	public int updateByPrimaryKeySelective(JsapiTicket record);
}
