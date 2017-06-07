package com.st.ktv.service.impl;

import com.st.ktv.service.AgentCompanyService;
import com.st.core.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

/**
 * 代理商服务
 *
 * @author wanglei
 * @since 01.09.2016
 */
@Transactional
@Service("agentCompanyService")
public class AgentCompanyServiceImpl implements AgentCompanyService {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /**
     * 控制权限，验证用户身份是否匹配(userId与agentId是否匹配)
     * @return
     */
    public Boolean isTrueAgent(){
        Boolean flag = false;
        HttpSession session = ContextHolderUtils.getSession();
        Object agentIdObject = session.getAttribute("agentId");
        Object agentTypeObject = session.getAttribute("agentType");
        Object userIdObject = session.getAttribute("userId");
        String myStr = "agentType=" + agentTypeObject.toString() + "&memberId=" + userIdObject.toString();

        try {
            JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_IF_TRUE_USER, myStr));
            if(resultStr.getString("data").equals(agentIdObject.toString())){
                flag = true;
            }
        }catch (Exception e){
            logger.error("验证用户真实身份出错:" + e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 判断团队成员与代理商是否存在关系
     * @param cityAgentId
     * @param agentMemberId
     * @return
     */
    public Boolean getIfMemberInCityAgent(String cityAgentId, String agentMemberId ){
        Boolean flag = false;
        String myStr = "cityAgentId=" + cityAgentId + "&agentMemberId=" + agentMemberId;
        try {
            JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_IF_MEMBER_IN_CITY_AGENT, myStr));
            if(resultStr.getInt("data") > 0){
                flag = true;
            }
        }catch (Exception e){
            logger.error("判断团队成员与代理商是否存在关系出错:" + e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 获取用户身份 0普通用户 1个人代理商 2城市代理商 3团队成员
     *
     * @param memberId  用户id
     * @return          用户身份
     */
    public String getIdentityOfUser(String memberId) {
        String agentType = "";
        try {
            JSONObject jsonStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_AGENT_IDENTITY_OF_USER, "memberId=" + memberId));
            JSONObject data = JSONObject.fromObject(jsonStr.getString("data"));
            if (jsonStr.getInt("status") == 0) {
                agentType = data.getString("agent");
                HttpSession session = ContextHolderUtils.getSession();
                session.setAttribute("agentType", agentType);
                session.setAttribute("agentId", data.getString("agentId"));
            }
        } catch (Exception e) {
            logger.error("获取用户代理商身份出错:" + e.getMessage(), e);
        }
        return agentType;
    }
}
