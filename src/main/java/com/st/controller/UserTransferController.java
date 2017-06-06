package com.st.controller;

import com.st.javabean.pojo.wxtour.TBUserTransfer;
import com.st.service.TBUserTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
@Controller
@RequestMapping("/usertransfer")
public class UserTransferController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private TBUserTransferService tBUserTransferService;

	@RequestMapping("/closeusertransfer")
	public ModelAndView update(Model model,HttpServletRequest request, HttpServletResponse response){
		String openid = request.getParameter("openid");
		TBUserTransfer tBUserTransfer = new TBUserTransfer();
		tBUserTransfer.setOpenid(openid);
		tBUserTransfer.setState("0");
		tBUserTransfer.setUpdateTime(new Date());
		try {
			tBUserTransferService.updateByPrimaryKeySelective(tBUserTransfer);
		} catch (Exception e) {
			logger.error("关闭客服消息失败：openid="+openid+",----"+e.getMessage(), e);
		}
		return new ModelAndView("user/usertransfer");
	}
}
