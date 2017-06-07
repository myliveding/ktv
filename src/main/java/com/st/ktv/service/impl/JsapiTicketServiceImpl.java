package com.st.ktv.service.impl;

import com.st.ktv.entity.JsapiTicket;
import com.st.ktv.mapper.JsapiTicketMapper;
import com.st.ktv.service.JsapiTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("jsapiTicketService")
public class JsapiTicketServiceImpl implements JsapiTicketService {

	@Autowired
	private JsapiTicketMapper jsapiTicketMapper;
	
	@Override
	public int insertSelective(JsapiTicket record) {
		// TODO Auto-generated method stub
		return jsapiTicketMapper.insertSelective(record);
	}

	@Override
	public JsapiTicket selectByPrimaryKey(String appid) {
		// TODO Auto-generated method stub
		return jsapiTicketMapper.selectByPrimaryKey(appid);
	}

	@Override
	public int updateByPrimaryKeySelective(JsapiTicket record) {
		// TODO Auto-generated method stub
		return jsapiTicketMapper.updateByPrimaryKeySelective(record);
	}

}
