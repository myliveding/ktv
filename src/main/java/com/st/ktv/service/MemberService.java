package com.st.ktv.service;

import com.st.ktv.entity.WechatMember;
import net.sf.json.JSONObject;

/**
 * @Description
 * @FileName MemberService
 * @Author dingzr
 * @CreateTime 2017/6/10 14:21 六月
 */
public interface MemberService {

    /**
     * 校验登录
     * @param openid
     * @return
     */
    public void checkLogin(String openid, String appid);

    public WechatMember getObjectByOpenid(String openid);

    public JSONObject getJsonByOpenid(String openid);

    /**
     * 执行手机号码的校验、新增绑定和修改
     * @param openid
     * @param phone
     * @param type 1校验2新增3修改
     * @return
     */
    public JSONObject checkAndUpdateMobile(String openid, String phone, String type);

}
