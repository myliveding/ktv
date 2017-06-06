package com.st.service.impl;

import com.st.javabean.pojo.wxtour.JsapiTicket;
import com.st.mapper.wxtour.JsapiTicketMapper;
import com.st.service.JsapiTicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("jsapiTicketService")
@Transactional
public class JsapiTicketServiceImpl implements JsapiTicketService {

	@Resource 
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
