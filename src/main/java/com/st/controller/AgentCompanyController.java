package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.CookieUtil;
import com.st.core.util.text.StringUtils;
import com.st.javabean.pojo.Weixin;
import com.st.service.AgentCompanyService;
import com.st.service.WeixinAPIService;
import com.st.utils.CheckUtil;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import com.st.utils.SignUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 城市代理商微信端控制器
 *
 * @author wanglei
 * @since 29.07.2016
 */
@Controller
@RequestMapping("/agentCompany")
public class AgentCompanyController {

	private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WeixinAPIService weixinAPIService;

	@Autowired
	private AgentCompanyService agentCompanyService;

	/**
	 * 跳转到代理商中心
     */
	@RequestMapping("/goToAgentCompany")
	public String goToAgentCompany(HttpServletRequest request) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");
		if (null!=userIdObject&&!"".equals(userIdObject)) {
			Object agentType = session.getAttribute("agentType");
			if (agentType == null || "".equals(agentType)) {
				agentType = agentCompanyService.getIdentityOfUser(userIdObject.toString());
			}

			if ("0".equals(agentType)) {
				return "agent/joinus";
			} else {
				return "agent/agentcentre";
			}
		}
		String openid = request.getParameter("openid");
		String appid = request.getParameter("appid");
		//判断是否需要保存session
		if (!"".equals(openid)&&openid!=null&&!"".equals(appid)&&appid!=null) {
			session.setAttribute("openid", openid);
			session.setAttribute("appid", appid);
		}else{
			Object openidObj =  session.getAttribute("openid");
			if (!"".equals(openidObj)&&openidObj!=null) {
				openid = openidObj.toString();
			}
		}
		if (StringUtils.isNotEmpty(openid)) {
			String[] arr = new String[] {"openid"+openid};
			String mystr = "openid="+openid;
			JSONObject result = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
			try {
				//根据openid判断是否绑定
				result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.PERSONSOCIAL_LOGIN_URL,mystr,arr));
			} catch (Exception e) {
				logger.error("获取登录出错:" + e.getMessage(), e);
			}
			if(0 == result.getInt("status")){
				JSONObject data = JSONObject.fromObject(result.getString("data"));
				String userId=data.getString("user_id");
				String memberTruename=data.getString("member_truename");
				session.setAttribute("userId", userId);
				String isMobileBind =data.getString("is_mobile_bind");
				session.setAttribute("isMobileBind", isMobileBind);
				if(StringUtils.isNotEmpty(memberTruename)&&!"null".equals(memberTruename)){
					session.setAttribute("memberTruename", memberTruename);
				}
				CookieUtil.getUserInfo();//更新用户的消息标志session
				request.setAttribute("type", 6);
				return "agent/agentcentre";
			}else {
				session.setAttribute("backUrl","/agentCompany/goToJoinUsPage.do");
				return "redirect:"+ Constant.URL+"/weixin/getweixin.do?name=account/login";
			}
		}
		session.setAttribute("backUrl","/agentCompany/goToJoinUsPage.do");
		return "redirect:"+ Constant.URL+"/weixin/getweixin.do?name=account/login";
	}

	/**
	 * 跳转到加入我们页面
	 */
	@RequestMapping("/goToJoinUsPage")
	public String goToJoinUsPage() throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject=session.getAttribute("userId");

		Object agentType = session.getAttribute("agentType");
		if (agentType == null || "".equals(agentType)) {
			agentType = agentCompanyService.getIdentityOfUser(userIdObject.toString());
		}

		if ("0".equals(agentType)) {
			return "agent/joinus";
		} else {
			return "agent/agentcentre";
		}
	}


	/**
	 * 如果是城市代则获取城市代理发展客户数、成交服务费，
	 * 如果是个人代或者二级代则获取购买人和未购买人
	 * @param response		response
	 * @return				Object
	 * @throws IOException
     */
	@RequestMapping("/getAgentResults")
	@ResponseBody
	public Object getAgentResults(HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		Object userIdObject =  session.getAttribute("userId");
		if (userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}
		Object agentType = session.getAttribute("agentType");
		resultStr.put("agent", agentType);

		if (!"".equals(agentType)) {
			if ("2".equals(agentType)) {
				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_RESULTS,"memberId=" + userIdObject.toString()));
				} catch (Exception e) {
					logger.error("获取城市代理发展客户数、成交服务费出错:" + e.getMessage(), e);
				}
			}else {
				StringBuilder buffer = new StringBuilder();
				buffer.append("memberId=").append(userIdObject.toString());
				buffer.append("&type=").append(agentType);

				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_USER_COUNT,buffer.toString()));
				} catch (Exception e) {
					logger.error("获取代理商所属用户数量统计出错:" + e.getMessage(), e);
				}
			}
			resultStr.put("agent", agentType);
			resultStr.put("memberId", userIdObject);
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员数据
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getMemberDataById")
	@ResponseBody
	public Object getMemberDataById(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if (userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			StringBuffer buffer = new StringBuffer();
			Object agentIdObject = session.getAttribute("agentId");
			String pageNow = request.getParameter("pageNow");
			String pageSize = request.getParameter("pageSize");
			buffer.append("cityAgentId=").append(agentIdObject.toString());
			if(StringUtils.isNotEmpty(pageNow)){
				buffer.append("&pageNow=").append(pageNow);
			}
			if(StringUtils.isNotEmpty(pageSize)){
				buffer.append("&pageSize=").append(pageSize);
			}
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MEMBER_DATA, buffer.toString()));
			}catch (Exception e) {
				logger.error("获取团队成员数据出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 查找团队成员
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getMemberDataByMobileOrName")
	@ResponseBody
	public Object getMemberDataByMobileOrName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String searchParam = request.getParameter("searchParam");
			String myStr = "cityAgentId=" + agentIdObject.toString() +"&searchParam=" + searchParam;
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MEMBER_DATA_SEARCH, myStr));
			}catch (Exception e) {
				logger.error("查找团队成员数据出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 校验手机号
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("checkMobile")
	@ResponseBody
	public Object checkMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String mobile = request.getParameter("mobile");
			if (StringUtils.isNotEmpty(mobile)) {
				mobile = mobile.replaceAll(" ", "");
				if(!CheckUtil.isMobileNo(mobile)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式不正确\"}");
				}
			}
			String cityAgentId = agentIdObject.toString();
			String myStr = "mobile=" + mobile + "&cityAgentId=" + cityAgentId;
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_CHECK_IF_CAN_ADD_TEAM_MEMBER, myStr));
				JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
				if(("-1").equals(data.getString("memberId"))) {
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机未注册过“无忧保”，请联系成员关注无忧保公众号并完成注册后再操作\"}");
				}
				if(!("0").equals(data.getString("isAgent"))){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机已是合伙人，无法再次添加\"}");
				}
				if(!("0").equals(data.getString("isAgentReletionExist"))){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机用户已在你的团队成员中，不需再次添加\"}");
				}
				if(!("0").equals(data.getString("isOtherMember"))) {
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机用户已完成与某个城市合伙人的绑定，无法再次添加\"}");
				}
			}catch (Exception e){
				logger.error("验证手机号出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 新增团队成员
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("addTeamMember")
	@ResponseBody
	public Object addTeamMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			StringBuffer buffer = new StringBuffer();
			String mobile = request.getParameter("mobile");
			if (StringUtils.isNotEmpty(mobile)) {
				mobile = mobile.replaceAll(" ", "");
				if(!CheckUtil.isMobileNo(mobile)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式不正确\"}");
				}
			}else {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号不能为空\"}");
			}

			String identityCard = request.getParameter("identityCard");
			if (StringUtils.isNotEmpty(identityCard)){
				identityCard = identityCard.replaceAll(" ", "");
				if(!CheckUtil.isIdentityNo(identityCard)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证号格式不正确\"}");
				}
			}else {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证号不能为空\"}");
			}

			String agentMemberName = request.getParameter("agentMemberName");
			if(StringUtils.isNotEmpty(agentMemberName)){
				agentMemberName = agentMemberName.replaceAll(" ", "");
				if(!CheckUtil.isMemberName(agentMemberName)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"成员名称必须为汉字或字母且长度不超过10\"}");
				}
				agentMemberName = UriUtils.encodeQueryParam(agentMemberName, "utf-8");
			}else {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"成员名称不能为空\"}");
			}

			buffer.append("cityAgentId=").append(agentIdObject.toString());
			buffer.append("&identityCard=").append(identityCard);
			buffer.append("&mobile=").append(mobile);
			buffer.append("&agentMemberName=").append(agentMemberName);

			String myStr = "mobile=" + mobile + "&cityAgentId=" + agentIdObject.toString();

			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_CHECK_IF_CAN_ADD_TEAM_MEMBER, myStr));
				JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
				if(("-1").equals(data.getString("memberId"))) {
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机未注册过“无忧保”，请联系成员关注无忧保公众号并完成注册后再操作\"}");
				}
				if(!("0").equals(data.getString("isAgent"))){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机已是合伙人，无法再次添加\"}");
				}
				if(!("0").equals(data.getString("isAgentReletionExist"))){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机用户已在你的团队成员中，不需再次添加\"}");
				}
				if(!("0").equals(data.getString("isOtherMember"))) {
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机用户已完成与某个城市合伙人的绑定，无法再次添加\"}");
				}
				buffer.append("&memberId=").append(data.getString("memberId"));

				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_ADD_TEAM_MEMBER, buffer.toString()));
			}catch (Exception e){
				logger.error("新增团队成员出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员业绩
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getTeamMemberAchievement")
	@ResponseBody
	public Object getTeamMemberAchievement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String agentMemberId = request.getParameter("memberId");
			if(StringUtils.isEmpty(agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择团队成员\"}");
			}

			if(!agentCompanyService.getIfMemberInCityAgent(agentIdObject.toString(), agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"团队成员与代理商不匹配\"}");
			}

			String myStr = "cityAgentId=" + agentIdObject.toString() + "&agentMemberId=" + agentMemberId;

			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_TEAM_MEMBER_ACHIEVEMENT, myStr));
			}catch (Exception e) {
				logger.error("获取团队成员业绩出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员月度业绩
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getTeamMemberMonthAchievenent")
	@ResponseBody
	public Object getTeamMemberMonthAchievenent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String agentMemberId = request.getParameter("agentMemberId");
			if(StringUtils.isEmpty(agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择团队成员\"}");
			}

			if(!agentCompanyService.getIfMemberInCityAgent(agentIdObject.toString(), agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"团队成员与代理商不匹配\"}");
			}

			String serviceMonth = request.getParameter("serviceMonth");
			String pageNow = request.getParameter("pageNow");
			String pageSize = request.getParameter("pageSize");
			StringBuffer buffer = new StringBuffer();
			buffer.append("cityAgentId=").append(agentIdObject.toString());
			buffer.append("&agentMemberId=").append(agentMemberId);
			buffer.append("&serviceMonth=").append(serviceMonth);
			if(StringUtils.isNotEmpty(pageNow)){
				buffer.append("&pageNow=").append(pageNow);
			}
			if(StringUtils.isNotEmpty(pageSize)){
				buffer.append("&pageSize=").append(pageSize);
			}

			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_TEAM_MEMBER_MONTH_ACHIEVEMENT, buffer.toString()));
			}catch (Exception e) {
				logger.error("获取团队成员月度业绩出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员注册信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getTeamMemberInfo")
	@ResponseBody
	public Object getTeamMemberInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String agentMemberId = request.getParameter("agentMemberId");
			if(StringUtils.isEmpty(agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择团队成员\"}");
			}

			if(!agentCompanyService.getIfMemberInCityAgent(agentIdObject.toString(), agentMemberId)){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"团队成员与代理商不匹配\"}");
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("cityAgentId=").append(agentIdObject.toString());
			buffer.append("&agentMemberId=").append(agentMemberId);
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_TEAM_MEMBER_INFO, buffer.toString()));
			}catch (Exception e) {
				logger.error("获取团队成员注册信息出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 编辑团队成员信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("updateTeamMemberInfo")
	@ResponseBody
	public Object updateTeamMemberInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			StringBuffer buffer = new StringBuffer();
			String identityCard = request.getParameter("identityCard");
			if (StringUtils.isNotEmpty(identityCard)){
				identityCard = identityCard.replaceAll(" ", "");
				if(!CheckUtil.isIdentityNo(identityCard)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证号格式不正确\"}");
				}
			}else {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"身份证号不能为空\"}");
			}

			String agentMemberName = request.getParameter("agentMemberName");
			if(StringUtils.isNotEmpty(agentMemberName)){
				agentMemberName = agentMemberName.replaceAll(" ", "");
				if(!CheckUtil.isMemberName(agentMemberName)){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"成员名称必须为汉字或字母且长度不超过10\"}");
				}
				agentMemberName = UriUtils.encodeQueryParam(agentMemberName, "utf-8");
			}else {
				return JSONObject.fromObject("{\"status\":1,\"msg\":\"成员名称不能为空\"}");
			}

			String id = request.getParameter("agentMemberId");
			String statusFlag = request.getParameter("statusFlag");
			String mobile = request.getParameter("mobile");
			String memberId = request.getParameter("memberId");
			buffer.append("cityAgentId=").append(agentIdObject.toString());
			buffer.append("&identityCard=").append(identityCard);
			buffer.append("&id=").append(id);
			buffer.append("&agentMemberName=").append(agentMemberName);
			buffer.append("&statusFlag=").append(statusFlag);
			buffer.append("&mobile=").append(mobile);
			buffer.append("&memberId=").append(memberId);

			if("1".equals(statusFlag)){
				String cooperateStatus = request.getParameter("cooperateStatus");
				buffer.append("&cooperateStatus=").append(cooperateStatus);
			}

			try{
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_UPDATE_TEAM_MEMBER_INFO, buffer.toString()));
				JSONObject data = JSONObject.fromObject(resultStr.getString("data"));
				if(data.getInt("flag") == 1){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"端口已使用完，无法恢复合作\"}");
				}else if(data.getInt("flag") == 2){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该手机用户已是合伙人，无法恢复合作\"}");
				}else if(data.getInt("flag") == 3){
					return JSONObject.fromObject("{\"status\":1,\"msg\":\"该成员已完成与某个城市合伙人的绑定，无法恢复合作\"}");
				}
			}catch (Exception e){
				logger.error("编辑团队成员信息出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取成员激活页面信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getActiveInfo")
	@ResponseBody
	public Object getActiveInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String id = request.getParameter("id");
		try {
			resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_ACTIVE_INFO, "id=" + id));
		} catch (Exception e){
			logger.error("获取激活信息出错：" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return  null;
	}

	/**
	 * 激活成员状态
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("updateAcitiveStatus")
	@ResponseBody
	public Object updateAcitiveStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String id = request.getParameter("id");
		String cityAgentName = request.getParameter("cityAgentName");
		cityAgentName = UriUtils.encodeQueryParam(cityAgentName, "utf-8");
		String cityName = request.getParameter("cityName");
		cityName = UriUtils.encodeQueryParam(cityName, "utf-8");
		StringBuffer buffer = new StringBuffer();
		buffer.append("id=").append(id);
		buffer.append("&cityName=").append(cityName);
		buffer.append("&cityAgentName=").append(cityAgentName);
		try {
			resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_UPDATE_ACTIVE_STATUS, buffer.toString()));
		} catch (Exception e){
			logger.error("激活成员状态出错：" + e.getMessage(), e);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return  null;
	}

	/**
	 * 获取总业绩
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getTotalAchievenent")
	@ResponseBody
	public Object getTotalAchievenent(HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_TOTAL_ACHIEVEMENT, "cityAgentId=" + agentIdObject.toString()));
			} catch (Exception e){
				logger.error("获取总业绩出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return  null;
	}

	/**
	 * 获取每月总业绩
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getMonthTotalAchievement")
	@ResponseBody
	public Object getMonthTotalAchievement(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String flag = request.getParameter("flag");
			String myStr = "cityAgentId=" + agentIdObject.toString();
			if(StringUtils.isNotEmpty(flag)){
				myStr = myStr + "&flag=" + flag;
			}
			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MONTH_TOTAL_ACHIEVEMENT, myStr));
			} catch (Exception e){
				logger.error("获取每月总业绩出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return  null;
	}

	/**
	 * 获取总体月度详细业绩
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getMonthAchievementDetail")
	@ResponseBody
	public Object getMonthAchievementDetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		if(userIdObject == null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject =  session.getAttribute("agentType");
		if (agentTypeObject != null && "2".equals(agentTypeObject.toString())){
			Object agentIdObject = session.getAttribute("agentId");
			String flag = request.getParameter("flag");
			String serviceMonth = request.getParameter("serviceMonth");
			String pageNow = request.getParameter("pageNow");
			String pageSize = request.getParameter("pageSize");
			StringBuffer buffer = new StringBuffer();
			buffer.append("cityAgentId=").append(agentIdObject.toString());
			buffer.append("&serviceMonth=").append(serviceMonth);
			if(StringUtils.isNotEmpty(flag)){
				buffer.append("&flag=").append(flag);
			}
			if(StringUtils.isNotEmpty(pageNow)){
				buffer.append("&pageNow=").append(pageNow);
			}
			if(StringUtils.isNotEmpty(pageSize)){
				buffer.append("&pageSize=").append(pageSize);
			}

			try {
				resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MONTH_ACHIEVEMENT_DETAIL, buffer.toString()));
			} catch (Exception e){
				logger.error("获取总体月度详细业绩出错：" + e.getMessage(), e);
			}
		}else {
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return  null;
	}

	/**
	 * 获取代理商所属用户
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/agentUsers")
	@ResponseBody
	public Object agentUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null||"".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject = session.getAttribute("agentType");
		if(agentTypeObject != null && !"".equals(agentTypeObject)){
			if ("0".equals(agentTypeObject.toString())){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
			}else {
				StringBuffer buffer = new StringBuffer();
				String isPurchaseFlag = request.getParameter("isPurchaseFlag");//0未购买1已购买2两个都查询
				buffer.append("memberId=").append(userIdObject.toString());
				buffer.append("&type=").append(agentTypeObject.toString());
				buffer.append("&isPurchaseFlag=").append(isPurchaseFlag);

				if ("2".equals(isPurchaseFlag)) {
					String searchParam=request.getParameter("searchParam");
					buffer.append("&searchParam=").append(searchParam);
				}

				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_CUSTOMER_PURCHASE_INFO,buffer.toString()));
				} catch (Exception e) {
					logger.error("获取代理商所属用户:" + e.getMessage(), e);
				}
			}
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取代理商所属已购买的用户信息
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/agentBought")
	@ResponseBody
	public Object agentBought(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject==null || "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject = session.getAttribute("agentType");
		if(agentTypeObject != null && !"".equals(agentTypeObject)){
			if ("0".equals(agentTypeObject.toString())){
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
			}else {
				StringBuffer buffer = new StringBuffer();
				String mobile = request.getParameter("mobile");//客户手机号
				String pageNow = request.getParameter("pageNow");
				String pageSize = request.getParameter("pageSize");
				buffer.append("memberId=").append(userIdObject.toString());
				buffer.append("&mobile=").append(mobile);
				buffer.append("&type=").append(agentTypeObject.toString());
				if(StringUtils.isNotEmpty(pageNow)){
					buffer.append("&pageNow=").append(pageNow);
				}
				if(StringUtils.isNotEmpty(pageSize)){
					buffer.append("&pageSize=").append(pageSize);
				}

				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_CUSTOMER_PURCHASE_HISTORY,buffer.toString()));
					JSONObject data = JSONObject.fromObject(resultStr.get("data"));
					JSONObject customerInfo = JSONObject.fromObject(data.get("customerInfo"));
					if(customerInfo.getInt("insuredNumber") == 0){
						return JSONObject.fromObject("{\"status\":-1,\"msg\":\"用户信息不匹配\"}");
					}
				} catch (Exception e) {
					logger.error("获取代理商所属已购买的用户信息:" + e.getMessage(), e);
				}
			}
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员或个人代理商的我的业绩
	 *
	 * @param response		response
	 * @return				map
     */
	@RequestMapping("getAgentMemberAchievement")
	@ResponseBody
	public Object getAgentMemberAchievement(HttpServletResponse response) throws IOException{
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject == null|| "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject = session.getAttribute("agentType");
		if (agentTypeObject != null && !"".equals(agentTypeObject)) {
			if ("1".equals(agentTypeObject.toString()) || "3".equals(agentTypeObject.toString())) {
				String agentMemberId = session.getAttribute("agentId").toString();
				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MEMBER_ACHIEVEMENT, "agentMemberId=" + agentMemberId));
				} catch (Exception e) {
					logger.error("获取团队成员或个人代理商的我的业绩出错:" + e.getMessage(), e);
				}
			}else {
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 获取团队成员或个人代理商的我的月度业绩
	 *
	 * @param request		request
	 * @param response		response
	 * @return				map
	 * @throws IOException
     */
	@RequestMapping("getAgentMemberMonthAchievement")
	@ResponseBody
	public Object getAgentMemberMonthAchievement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		if(userIdObject == null|| "".equals(userIdObject)){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
		}

		if(!agentCompanyService.isTrueAgent()){
			return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的信息不匹配\"}");
		}

		Object agentTypeObject = session.getAttribute("agentType");
		if (agentTypeObject != null && !"".equals(agentTypeObject)) {
			if ("1".equals(agentTypeObject.toString()) || "3".equals(agentTypeObject.toString())) {
				StringBuilder buffer = new StringBuilder();
				String agentMemberId = session.getAttribute("agentId").toString();
				String cityAgentId = request.getParameter("cityAgentId");
				String serviceMonth = request.getParameter("serviceMonth");
				String pageNow = request.getParameter("pageNow");
				String pageSize = request.getParameter("pageSize");
				buffer.append("agentMemberId=").append(agentMemberId);
				buffer.append("&cityAgentId=").append(cityAgentId);
				buffer.append("&serviceMonth=").append(serviceMonth);
				if(StringUtils.isNotEmpty(pageNow)){
					buffer.append("&pageNow=").append(pageNow);
				}
				if(StringUtils.isNotEmpty(pageSize)){
					buffer.append("&pageSize=").append(pageSize);
				}
				try {
					resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_MEMBER_MONTH_ACHIEVEMENT, buffer.toString()));
				} catch (Exception e) {
					logger.error("获取团队成员或个人代理商的我的月度业绩出错:" + e.getMessage(), e);
				}
			}else {
				return JSONObject.fromObject("{\"status\":-1,\"msg\":\"您的身份不匹配\"}");
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 代理商推广页获取代理商状态及信息
	 *
	 * @param request		request
	 * @param response		response
	 * @return				object
	 * @throws IOException
     */
	@RequestMapping("getAgentStatusAndInfo")
	@ResponseBody
	public Object getAgentStatusAndInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
		String memberId = request.getParameter("memberId");

		try {
			resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_AGENT_STATUS_AND_INFO, "memberId=" + memberId));

            JSONObject jsonStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_ORDER_INDEX, "" ));
            if (jsonStr.getInt("status") == 0) {
                resultStr.put("buyList",jsonStr.getJSONArray("data"));
            }
		} catch (Exception e) {
			logger.error("代理商推广页获取代理商状态及信息出错:" + e.getMessage(), e);
		}

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(resultStr);
		return null;
	}

	/**
	 * 跳转到代理商专属推广页
	 *
	 * @param request	request
     * @return			跳转路径
     */
	@RequestMapping("/gotoPromotionPage")
	public String gotoPromotionPage(HttpServletRequest request){
		String memberId = request.getParameter("memberId");
		HttpSession session = ContextHolderUtils.getSession();
		Object userIdObject =  session.getAttribute("userId");

		try {
			JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_AGENT_STATUS_AND_INFO, "memberId=" + memberId));

			if (resultStr.getInt("status") == 0) {
				request.setAttribute("isAgentSelf", -1);
				if (userIdObject != null && memberId.equals(userIdObject.toString())) {
					request.setAttribute("isAgentSelf", 0);
				}
				if (JSONObject.fromObject(resultStr.getString("data")).getInt("agentStatus") == -1) {
					return "agent/agentdisabled";
				}
			}

			resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_INVITATION_CODE, "memberId=" + memberId));
			if(0 == resultStr.getInt("status")){
				request.setAttribute("invitationCode", resultStr.get("data"));
			}
		} catch (Exception e) {
			logger.error("代理商推广页获取代理商状态及信息出错:" + e.getMessage(), e);
		}

		String shareUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constant.APP_ID+"&redirect_uri="+Constant.URL+"/scope/openid.do?next=agentCompany/gotoPromotionPage.do"+Constant.APP_ID+"memberId="+memberId+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";//二维码用链接
        JSONObject resultStr;
        String shortUrl = "";

        try {
            shareUrl = java.net.URLEncoder.encode(shareUrl,"utf-8");
            String myStr = "url=" + shareUrl;
            String[] arr= new String[] {"url" + shareUrl};
            resultStr = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHORT_URL, myStr, arr));
            if(0 == resultStr.getInt("status")){
                shortUrl=JSONObject.fromObject(resultStr.getString("data")).getString("short_url");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("代理商推广邀请页短链接出错：" + e.getMessage(), e);
        }

        String strBackUrl = Constant.URL.substring(0,Constant.URL.indexOf(":"))+"://" + request.getServerName() //服务器地址
				+ request.getContextPath()      //项目名称
				+ request.getServletPath()      //请求页面或其他地址
				+ "?" + (request.getQueryString()); //参数
		String timestamp =Constant.TIME_STAMP;
		String noncestr = Constant.NONCESTR;
		Weixin weixin = weixinAPIService.getJSAPITicket(Constant.APP_ID);
		String jsapi_ticket = weixin.getJsapiTicket();
		logger.info("jsapi_ticket:" + jsapi_ticket);
		String signature = SignUtil.getSignature(jsapi_ticket, timestamp, noncestr, strBackUrl);
		logger.info("signature:" + signature);
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("noncestr", noncestr);
		request.setAttribute("url", strBackUrl);
		request.setAttribute("signature", signature);
		request.setAttribute("appid", Constant.APP_ID);
		request.setAttribute("shareUrl",shortUrl);
		request.setAttribute("memberId",memberId);
		logger.info("跳转到代理商专属推广页/jsp/agent/customizedpage.jsp?memberId=" + memberId);
		return "agent/customizedpage";
	}

}
