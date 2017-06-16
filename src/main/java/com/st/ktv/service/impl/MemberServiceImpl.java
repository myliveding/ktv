package com.st.ktv.service.impl;

import com.st.ktv.entity.WechatMember;
import com.st.ktv.mapper.WechatMemberMapper;
import com.st.ktv.service.MemberService;
import com.st.utils.DataUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

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
     * 校验登录
     * @param openid
     * @return
     */
    public void checkLogin(String openid, String appid){
        Date nowTime = new Date();
        WechatMember updateMember = new WechatMember();
        String nickName = "";
        String headPortrait = "";
        JSONObject jSONObject = WechatAPIServiceImpl.getUserInfoOfOpenId(appid, openid);
        if(jSONObject.getString("subscribe").equalsIgnoreCase("1")){
            nickName=jSONObject.getString("nickname");
            headPortrait=jSONObject.getString("headimgurl");
            updateMember.setNickName(nickName);
            updateMember.setHeadPortrait(headPortrait);
            updateMember.setUpdateTime(nowTime);
        }else{
            logger.info("openid：" + openid + "还未关注公众号，所以获取不到头像信息");
        }
        WechatMember wechatMember = wechatMemberMapper.getObjectByOpenid(openid);
        if(null != wechatMember){
            updateMember.setId(wechatMember.getId());
            updateMember.setLastLoginTime(nowTime);
            updateMember.setLastLoginIp(getRemoteAddr());
            wechatMemberMapper.updateByPrimaryKeySelective(updateMember);
        }else{
            logger.info("openid：" + openid + "在数据库中不存在，表示是个新的关注用户，需要去插入数据库");
            updateMember.setOpenid(openid);
            updateMember.setLastLoginTime(nowTime);
            updateMember.setLastLoginIp(getRemoteAddr());
            updateMember.setCreateTime(nowTime);
            wechatMemberMapper.insertSelective(updateMember);
        }
    }

    /**
     * 获取远程IP地址
     * @return
     */
    private String getRemoteAddr(){
        String remoteAddr = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            remoteAddr = addr.getHostAddress();//获得本机IP
        } catch (UnknownHostException e) {
            logger.error("获取服务器IP地址出错！" + e.getMessage());
        }
        return remoteAddr;
    }

    public WechatMember getObjectByOpenid(String openid){
        return wechatMemberMapper.getObjectByOpenid(openid);
    }

    public WechatMember getObjectByMobile(String mobile){
        return wechatMemberMapper.getObjectByMobile(mobile);
    }

    /**
     * 执行手机号码的校验、新增绑定和修改
     * @param openid
     * @param phone
     * @param type 1校验2新增3修改
     * @return
     */
    public JSONObject checkAndUpdateMobile(String openid, String phone, String type){
        JSONObject resultObject = JSONObject.fromObject("{\"status\":0,\"data\":\"OK\"}");
        logger.info("openid：" + openid + "--phone：" + phone + "--type：" + type);
        Date nowTime = new Date();
        WechatMember wechatMember = wechatMemberMapper.getObjectByOpenid(openid);
        if(null != wechatMember){
            if(type.equals("1")){
                if(wechatMember.getMobile() != null && !"".equals(wechatMember.getMobile())){
                    resultObject = JSONObject.fromObject("{\"status\":0,\"data\":\"已存在\"}");
                }else{
                    resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"请绑定手机号码\"}");
                }
            }else if(type.equals("2") || type.equals("3")){
                if (DataUtil.isNotEmpty(phone)) {
                    phone = phone.replaceAll(" ", "");
                    if(!DataUtil.isMobileNo(phone)){
                        resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号格式有误！\"}");
                    }else{
                        WechatMember mobile = wechatMemberMapper.getObjectByMobile(phone);
                        if(mobile != null){
                            resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"手机号已存在！\"}");
                        }else{
                            //更新手机号码
                            WechatMember member = new WechatMember();
                            member.setId(wechatMember.getId());
                            member.setMobile(phone);
                            member.setUpdateTime(nowTime);
                            wechatMemberMapper.updateByPrimaryKeySelective(member);
                        }
                    }
                }else{
                    resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"输入手机号码为空\"}");
                }
            }else{
                resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"输入错误\"}");
            }
        }else{
            resultObject =  JSONObject.fromObject("{\"status\":1,\"msg\":\"请先登录\"}");
        }
        return resultObject;
    }

}
