package com.st.ktv.service;

/**
 * 代理商服务
 *
 * @author wanglei
 * @since 01.09.2016
 */
public interface AgentCompanyService {

    /**
     * 控制权限，验证用户身份是否匹配
     * @return
     */
    Boolean isTrueAgent();

    /**
     * 判断团队成员与代理商是否存在关系
     * @param cityAgentId
     * @param agentMemberId
     * @return
     */
    Boolean getIfMemberInCityAgent(String cityAgentId, String agentMemberId );

    /**
     * 获取用户身份 0普通用户 1个人代理商 2城市代理商 3团队成员
     *
     * @param memberId  用户id
     * @return          用户身份
     */
    String getIdentityOfUser(String memberId);
}
