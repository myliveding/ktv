package com.st.ktv.service.impl;

import com.st.ktv.entity.WechatMember;
import com.st.ktv.mapper.WechatMemberMapper;
import com.st.ktv.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description
 * @FileName MemberServiceImpl
 * @Author dingzr
 * @CreateTime 2017/6/10 14:21 六月
 */
@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Resource
    private WechatAPIServiceImpl WechatAPIServiceImpl;

    @Autowired
    WechatMemberMapper wechatMemberMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 微信进入之后校验是否绑定手机号码
     * @param openid
     * @return
     */
    public String wechatLogin(String openid){

        WechatMember wechatMember = wechatMemberMapper.getObjectByOpenid(openid);
        if(null != wechatMember){
            if(null != wechatMember.getMobile()){

            }else{

            }
        }else{
            logger.info("openid：" + openid + "在数据库中不存在，表示是个新的关注用户，需要去插入数据库");
        }
        return "";
    }
}
