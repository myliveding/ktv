package com.st.ktv.service.impl;

import com.st.ktv.entity.JsapiTicket;
import com.st.ktv.mapper.JsapiTicketMapper;
import com.st.ktv.mapper.WechatMemberMapper;
import com.st.ktv.service.JsapiTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("jsapiTicketService")
public class JsapiTicketServiceImpl implements JsapiTicketService {

	@Resource
	private JsapiTicketMapper jsapiTicketMapper;
	@Resource
	private WechatMemberMapper wechatMemberMapper;
	
	@Override
	public int insertSelective(JsapiTicket record) {
		return jsapiTicketMapper.insertSelective(record);
	}

	@Override
	public JsapiTicket selectByPrimaryKey(String appid) {
		return jsapiTicketMapper.selectByPrimaryKey(appid);
	}

	@Override
	public int updateByPrimaryKeySelective(JsapiTicket record) {
		return jsapiTicketMapper.updateByPrimaryKeySelective(record);
	}

}
