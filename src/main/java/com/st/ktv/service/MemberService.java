package com.st.ktv.service;

/**
 * @Description
 * @FileName MemberService
 * @Author dingzr
 * @CreateTime 2017/6/10 14:21 六月
 */
public interface MemberService {

    /**
     * 微信进入之后校验是否绑定手机号码
     * @param openid
     * @return
     */
    public String wechatLogin(String openid);

}
