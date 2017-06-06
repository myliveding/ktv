package com.st.utils;

import com.st.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 金柚个人社保通接口URL
 * <p/>
 * 创建人：wuhao 创建时间：2015-6-11
 */
public class JoYoUtil {
    private static Logger logger = LoggerFactory.getLogger(JoYoUtil.class);
    /**
     * 注册绑定URL
     */
    public static final String PERSONSOCIAL_REGISTER_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/register/client_id/social10001";
    /**
     * 手机验证登录URL
     */
    public static final String PERSONSOCIAL_MOBILELOGIN_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/mobilelogin/client_id/social10001";
    /**
     * 验证手机号码或者用户名URL
     */
    public static final String PERSONSOCIAL_CHECKMOBILE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/check/client_id/social10001";
    /**
     * 完善资料
     */
    public static final String PERSONSOCIAL_CHANGE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/change/client_id/social10001";
    /**
     * 我的账号设置
     */
    public static final String PERSONSOCIAL_SAVE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/save/client_id/social10001";
    /**
     * 登录URL
     */
    public static final String PERSONSOCIAL_LOGIN_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/login/client_id/social10001";
    /**
     * 修改密码URL
     */
    public static final String PERSONSOCIAL_CHANGEPASSWORD_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/changepassword/client_id/social10001";
    /**
     * 绑定手机号(员工登入)
     */
    public static final String PERSONSOCIAL_BINDMOBILE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/bindmobile/client_id/social10001";
    /**
     * 重新绑定手机号 3.3
     */
    public static final String PERSONSOCIAL_REBINDMOBILE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/editmobile/client_id/social10001";
    /**
     * 重新绑定手机号,原号码验证 3.3
     */
    public static final String PERSONSOCIAL_REBINDMOBILE_CHECK_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/vermobile/client_id/social10001";
    /**
     * 找回密码URL
     */
    public static final String PERSONSOCIAL_FORGETPASSWORD_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/forgetpassword/client_id/social10001";
    /**
     * 验证码发送URL
     */
    public static final String PERSONSOCIAL_REGISTERSEND_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/registersend/client_id/social10001";
    /**
     * 重新绑定手机号验证码发送URL 3.3
     */
    public static final String PERSONSOCIAL_REBINDMOBILESEND_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/sendcode/client_id/social10001";
    /**
     * 用户个人资料URL
     */
    public static final String PERSONSOCIAL_PERSONALINFORMATION_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/personalinformation/client_id/social10001";
    /**
     * 城市列表查询URL 3.0
     */
    public static final String PERSONINSURANCE_CITYDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/citydemand/client_id/social10001";
    /**
     * 城市列表查询URL 3.0
     */
    public static final String PERSONINSURANCE_CITYLEVELDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/cityleveldemand/client_id/social10001";
    /**
     * 城市列表查询URL 平级 3.51
     */
    public static final String PERSONINSURANCE_ALLCITY_URL = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/getallcitylist/client_id/social10001";


//    /**
//     * 保存、修改当前参保人URL 3.0
//     */
//    public static final String PERSONINSURANCE_SAVE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/save/client_id/social10001";
    /**
     * 套餐URL 3.0
     */
    public static final String PERSONINSURANCE_PACKAGES_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/packages/client_id/social10001";
    /**
     * 查询当前的活动URL
     */
    public static final String PERSONRED_DEMANDACTIVITY_URL = Constant.PHP_API_URL + "/openapi/personsocial/personred/demandactivity/client_id/social10001";
     /**
     * 站内信查询URL
     */
    public static final String STATIONLETTER_DEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/stationletter/demand/client_id/social10001";
    /**
     * 站内信已读消息接口URL
     */
    public static final String STATIONLETTER_READ_URL = Constant.PHP_API_URL + "/openapi/personsocial/stationletter/read/client_id/social10001";
    /**
     * 抓取数据URL
     */
    public static final String DATACOUNT_CRAWL_URL = Constant.PHP_API_URL + "/openapi/personsocial/datacount/crawl/client_id/social10001";
    /**
     * 文章类别查询URL
     */
    public static final String ARTICLE_TYPEDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/typedemand/client_id/social10001";
    /**
     * 文章类别查询URL 平级 3.51
     */
    public static final String ARTICLE_TYPEALL_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/getalltypedemand/client_id/social10001";
    /**
     * 首页banner查询 3.4
     */
    public static final String ARTICLE_BANNERDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/advdemand/client_id/social10001";
    /**
     * 伪静态查询接口
     */
    public static final String ARTICLE_STATIC_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/alltypedemand/client_id/social10001";
    /**
     * 文章查询URL
     */
    public static final String ARTICLE_DEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/demand/client_id/social10001";
    /**
     * 文章大类搜索URL
     */
    public static final String ARTICLE_SEARCH_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/searcharticle/client_id/social10001";
    /**
     * 文章详情URL
     */
    public static final String ARTICLE_DETAIL_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/detail/client_id/social10001";
    /**
     * 我的页面 查询
     */
    public static final String PERSONSOCIAL_MY_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/mine/client_id/social10001";
    /**
     * 获取该城市的基础信息
     */
    public static final String CITY_INFORMATION_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/cityinformation/client_id/social10001";
    /**
     * 保存是否阅读退款和服务协议
     */
    public static final String SAVE_READ_AGREE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/savereadagreement/client_id/social10001";
    /**
     * 短网址生成接口
     */
    public static final String SHORT_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/shorturl/client_id/social10001";
    /**
     * 短网址生成接口(新浪)
     */
    public static final String SINA_SHORT_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/SinaShortUrl/client_id/social10001";
    /**
     * 发送短信接口
     */
    public static final String SENDSMS_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/sendsms/client_id/social10001";
    /**
     * 发送短信接口（代金券）
     */
    public static final String SENDSMSV_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/sendvouchersms/client_id/social10001";
    /**
     * 我的邀请记录接口
     */
    public static final String INVITATIONRECORD_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/invitationrecord/client_id/social10001";
    /**
     * 通过手机查询用户信息接口
     */
    public static final String MEMBERBYMOBILE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/memberbymobile/client_id/social10001";
    /**
     * 微信分享得代金卷获取手机查询
     */
    public static final String MOBILE_FIND_URL = Constant.PHP_API_URL + "/openapi/personsocial/personred/find/client_id/social10001";
    /**
     * 微信分享得代金卷获取手机添加
     */
    public static final String MOBILE_SAVE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personred/save/client_id/social10001";
    /**
     * 通过邀请码查询用户信息
     */
    public static final String MEMBERBYCODE_URL = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/memberbycode/client_id/social10001";
    /**
     * 通过邀请码查询用户信息
     */
    public static final String SEND_STATIONLETTER_URL = Constant.PHP_API_URL + "/openapi/personsocial/stationletter/send/client_id/social10001";
    /**
     * 查询无忧保城市电话和网址
     */
    public static final String PERSONINSURANCE_CITY_TEL_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/citytelandurl/client_id/social10001";
    /**
     * 用户反馈功能
     */
    public static final String USER_FEEDBACK = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/personalfeedback/client_id/social10001";
    /**
     * 社保计算器获取服务的城市 3.0 定位用
     */
    public static final String PERSONINSURANCE_GETCITY_URL = Constant.PHP_API_URL + "/openapi/personsocial/personinsurance/getcity/client_id/social10001";
    /**
     * 查询参保基数 3.0
     */
    public static final String INSUREDBASE = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/insuredbase/client_id/social10001";
    /**
     * 计算参保明细 3.0
     */
    public static final String INSUREDDETAIL = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/insureddetail/client_id/social10001";
    /**
     * 所有城市 3.0
     */
    public static final String ALLCITY = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/allcity/client_id/social10001";
    /**
     * 查询参保类型 3.0
     */
    public static final String CITY_SOCIAL_SET = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/getcitysocialset/client_id/social10001";
    /**
     * 查询参保基数范围 3.0
     */
    public static final String INSUREDBASE_RANGE = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/insuredbaserange/client_id/social10001";
    /**
     * 订单发送验证码 3.0
     */
    public static final String ORDER_SEND_CODE = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/ordersendcode/client_id/social10001";
    /**
     * 订单验证码验证3.0
     */
    public static final String ORDER_SECURITY_CODE = Constant.PHP_API_URL + "/openapi/personsocial/personsocial/ordersecuritycode/client_id/social10001";
    /**
     * 参保明细
     */
    public static final String INSURANCEDETAIL = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/insurancedetail/client_id/social10001";
    /**
     * 获取参保材料接 3.4
     */
    public static final String PERSON_CITY_DATA = Constant.PHP_API_URL + "/openapi/personsocial/personcitydata/getpersoncitydata/client_id/social10001";
    /**
     * 首页banner统计点击率的接口 3.41
     */
    public static final String BANNER_CLICK_RATE = Constant.PHP_API_URL + "/openapi/personsocial/article/updateclicknum/client_id/social10001";
    /**
     * 查找用户所有拥有的代金券
     */
    public static final String VOUCHERS_FIND_USER = Constant.VOUCHER_API_URL + "/voucher/findUserVouchers";
    /**
     * 查代金券详情
     */
    public static final String VOUCHERS_INFO = Constant.VOUCHER_API_URL + "/voucher/queryVoucherInfo";
    /**
     * 获取有效活动的描述说明
     */
    public static final String VOUCHERS_ACTIVITY_REMARK = Constant.VOUCHER_API_URL + "/voucher/activity/getActivityRemark";
    /**
     * 获取有效活动的描述说明
     */
    public static final String VOUCHERS_ACTIVITY_INFO = Constant.VOUCHER_API_URL + "/voucher/activity/getActivityShareInfo";
    /**
     * 判断是否享有活动
     */
    public static final String VOUCHERS_SHARE_ACTIVITY = Constant.VOUCHER_API_URL + "/voucher/isShareActivity";
    /**
     * 是否享受过活动或者领用过代金券
     */
    public static final String VOUCHERS_ISHADSHARE = Constant.VOUCHER_API_URL + "/voucher/record/isHadShare";
//    /**
//     * 代金券列表分为全部列表和根据活动ID查询列表
//     */
//    public static final String VOUCHERS_QUERY = Constant.VOUCHER_API_URL + "/voucher/query";
    /**
     * 领取代金券
     */
    public static final String VOUCHERS_RECEIVE = Constant.VOUCHER_API_URL + "/voucher/receiveVoucher";
//    /**
//     * 更新代金券状态
//     */
//    public static final String VOUCHERS_UPDATE_STATUS = Constant.VOUCHER_API_URL + "/voucher/updateStatus";
//    /**
//     * 使用代金券
//     */
//    public static final String VOUCHERS_UPDATE = Constant.VOUCHER_API_URL + "/voucher/updateVoucher";
    /**
     * 使用代金券
     */
    public static final String VOUCHERS_USE = Constant.VOUCHER_API_URL + "/voucher/useVoucher";
    /**
     * 手动兑换代金券
     */
    public static final String VOUCHERS_EXCHANGE = Constant.VOUCHER_API_URL + "/voucher/exchangeVoucher";
//    /**
//     * 锁定代金券
//     */
//    public static final String VOUCHERS_LOCK = Constant.VOUCHER_API_URL + "/voucher/lockVoucher";
//    /**
//     * 解锁代金券
//     */
//    public static final String VOUCHERS_UNLOCK = Constant.VOUCHER_API_URL + "/voucher/unlockVoucher";
    /**
     * 领取代金券
     */
    public static final String VOUCHERS_SEARCH = Constant.VOUCHER_API_URL + "/voucher/getReceiveVoucher";
    /**
     * 保存修改参保人 java
     */
    public static final String JAVA_INSURER_SAVE = Constant.JAVA_API_URL + "/wx/insurer/saveInsurer";
    /**
     * 查询参保人列表 java
     */
    public static final String JAVA_INSURER_LIST = Constant.JAVA_API_URL + "/wx/insurer/getInsurerByHolderId";
    /**
     * 删除参保人 java
     */
    public static final String JAVA_INSURER_DEL = Constant.JAVA_API_URL + "/wx/insurer/deleteInsurer";
    /**
     * 校验是否有相同身份证的参保人已在保 java
     */
    public static final String JAVA_INSURER_CHECK = Constant.JAVA_API_URL + "/wx/insurer/checkInsurer";
    /**
     * 参保人在保记录 java
     */
    public static final String JAVA_RECORD_LIST = Constant.JAVA_API_URL + "/wx/insurer/getRecordListByInsurerId";
    /**
     * 查询参保人信息 java
     */
    public static final String JAVA_INSURER_INFO = Constant.JAVA_API_URL + "/wx/insurer/getInsurerInfo";
    /**
     * 查询参保人参保记录信息 java
     */
    public static final String JAVA_RECORD_INFO = Constant.JAVA_API_URL + "/wx/insurer/getInsurerRecordByInsurerId";
    /**
     * 查询参保人业务状态 java
     */
    public static final String JAVA_CURRENT_PROGRESS = Constant.JAVA_API_URL + "/wx/insurer/getCurrentProgress";
    /**
     * 查询是否可续费 java
     */
    public static final String JAVA_INSURER_RENEW = Constant.JAVA_API_URL + "/wx/insurer/isRenew";
    /**
     * 根据投保人ID查询投保人信息
     */
    public static final String JAVA_HOLDER_INFO = Constant.JAVA_API_URL + "/wx/insurer/getHolderInfoByID";
    /**
     * 根据邀请码查询投保人信息
     */
    public static final String JAVA_HOLDER_INFO_BY_REGCODE = Constant.JAVA_API_URL + "/wx/insurer/getHolderInfoByRegCode";

    /**
     * 根据投保人id获取邀请码
     */
    public static final String JAVA_INVITATION_CODE = Constant.JAVA_API_URL + "/wx/insurer/getInvitationCodeByMemberId";

    /**
     * 查询账户余额 java
     */
    public static final String JAVA_BALANCE = Constant.JAVA_API_URL + "/wx/balance/getBalanceByPolicyHolderId";
    /**
     * 查询参保人余额 java 3.5
     */
    public static final String JAVA_INSURERBALANCE = Constant.JAVA_API_URL + "/wx/balance/getBalanceByInsurerId";
    /**
     * 查询参保人余额 java 3.5
     */
    public static final String JAVA_INSURERBALANCE_BY_ORDER = Constant.JAVA_API_URL + "/wx/balance/getBalanceByOrderNo";
    /**
     * 订单列表 java
     */
    public static final String JAVA_ORDER_LIST = Constant.JAVA_API_URL + "/wx/order/queryOrderList";
    /**
     * 订单详情 java
     */
    public static final String JAVA_ORDER_INFO = Constant.JAVA_API_URL + "/wx/order/queryOrderByNo";
    /**
     * 创建订单 java
     */
    public static final String JAVA_ORDER_CREATE = Constant.JAVA_API_URL + "/wx/order/createOrder";
    /**
     * 确认订单 java
     */
    public static final String JAVA_ORDER_CONFIRM = Constant.JAVA_API_URL + "/wx/order/payOrder";
    /**
     * 取消订单 java
     */
    public static final String JAVA_ORDER_CANCEL = Constant.JAVA_API_URL + "/wx/order/cancelOrder";
//    /**
//     * 获取JD token java
//     */
//    public static final String JAVA_ORDER_JDTOKEN = Constant.JAVA_API_URL + "/wx/order/getJdToken";
    /**
     * 保存JD token java
     */
    public static final String JAVA_ORDER_JDTOKEN_SAVE = Constant.JAVA_API_URL + "/wx/order/updateJdToken";
    /**
     * 年付 java
     */
    public static final String JAVA_YEAR_PAY = Constant.JAVA_API_URL + "/wx/order/getYearpay";
    /**
     * 校验订单 java
     */
    public static final String JAVA_ORDER_CHECK = Constant.JAVA_API_URL + "/wx/order/checkOrderPay";
    /**
     * 未支付订单数量 java
     */
    public static final String JAVA_ORDER_UNPAYNUM = Constant.JAVA_API_URL + "/wx/order/unpayOrder";
    /**
     * 判断参保人是否有未支付订单 java
     */
    public static final String JAVA_ORDER_UNPAY = Constant.JAVA_API_URL + "/wx/order/isExistUnpayOrder";
    /**
     * 获取首页显示订单信息 java 3.4
     */
    public static final String JAVA_ORDER_INDEX = Constant.JAVA_API_URL + "/wx/order/getIndexOrder";
    /**
     * 获取用于查询参保材料的信息 java 3.4
     */
    public static final String JAVA_ORDER_DOCINFO = Constant.JAVA_API_URL + "/wx/order/getOrderInfoForDoc";
    /**
     * 更新订单支付方式 java 3.53
     */
    public static final String JAVA_ORDER_UPDATE_PAYTYPE = Constant.JAVA_API_URL + "/wx/order/updateOrderPayType";
    /**
     * 退款初始 java
     */
    public static final String JAVA_REFUND_INIT = Constant.JAVA_API_URL + "/wx/refund/getInitData";
    /**
     * 退款申请 java
     */
    public static final String JAVA_REFUND_ADD = Constant.JAVA_API_URL + "/wx/refund/addRefund";
    /**
     * 退款保存 java
     */
    public static final String JAVA_REFUND_SAVE = Constant.JAVA_API_URL + "/wx/refund/editRefundForWx";
    /**
     * 退款取消 java
     */
    public static final String JAVA_REFUND_CANCEL = Constant.JAVA_API_URL + "/wx/refund/cancelRefund";
    /**
     * 退款列表 java
     */
    public static final String JAVA_REFUND_LIST = Constant.JAVA_API_URL + "/wx/refund/getRefundList";
    /**
     * 是否可退款 java
     */
    public static final String JAVA_REFUND_ABLE = Constant.JAVA_API_URL + "/wx/refund/isAbleRefund";
    /**
     * 退款明细 java
     */
    public static final String JAVA_REFUND_INFO = Constant.JAVA_API_URL + "/wx/refund/findRefundDetail";
    /**
     * 修改退款初始数据 java
     */
    public static final String JAVA_REFUND_UPDATE_INIT = Constant.JAVA_API_URL + "/wx/refund/getUpdateInitData";
    /**
     * 添加银行卡 java
     */
    public static final String JAVA_BANKCARD_ADD = Constant.JAVA_API_URL + "/wx/bank/addBank";
    /**
     * 获取银行卡列表java
     */
    public static final String JAVA_BANKCARD_LIST = Constant.JAVA_API_URL + "/wx/bank/getBankByPolicyHolderId";
    /**
     * 获取银行列表java
     */
    public static final String JAVA_BANK_LIST = Constant.JAVA_API_URL + "/wx/bank/getBankList";
    /**
     * 根据投保人ID查询所有的可续费的参保人数量 java
     */
    public static final String JAVA_CAN_RENEW_NUM = Constant.JAVA_API_URL + "/wx/insurer/getCanRenewNum";
    /**
     * 获取参数列表 java
     */
    public static final String JAVA_PARAM_TYPE = Constant.JAVA_API_URL + "/wx/param/getValuesByType";
    /**
     * 获取参数对象  java
     */
    public static final String JAVA_PARAM_CODE = Constant.JAVA_API_URL + "/wx/param/getItemByCode";
    /**
     * 获取代理商所属用户信息  java
     * 个人代理商老接口
     */
    public static final String JAVA_AGENT_USERS = Constant.JAVA_API_URL + "/wx/agent/queryAgentUsers";
    /**
     * 获取代理商所属用户信息统计  java
     * 个人代理商老接口
     */
    public static final String JAVA_AGENT_USERS_COUNT = Constant.JAVA_API_URL + "/wx/agent/queryAgentUsersCount";
    /**
     * 获取代理商已购买用户信息统计  java
     * 个人代理商老接口
     */
    public static final String JAVA_AGENT_BOUGHT_COUNT = Constant.JAVA_API_URL + "/wx/agent/queryAgentBoughtCount";
    /**
     * 获取代理商已购买用户信息  java
     * 个人代理商老接口
     */
    public static final String JAVA_AGENT_BOUGHT_DETAIL = Constant.JAVA_API_URL + "/wx/agent/queryAgentBoughtDetail";
    /**
     * 判断用户身份
     */
    public static final String JAVA_AGENT_IDENTITY_OF_USER = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getIdentityOfUser";
    /**
     * 身份验证
     */
    public static final String JAVA_AGENT_IF_TRUE_USER = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getIfTrueUser";
    /**
     * 判断团队成员与代理商是否存在关系
     */
    public static final String JAVA_AGENT_IF_MEMBER_IN_CITY_AGENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getIfMemberInCityAgent";
    /**
     * 获取城市代理发展客户数、成交服务费
     */
    public static final String JAVA_AGENT_RESULTS = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getAgentResultsByMemberId";
    /**
     * 获取团队成员数据
     */
    public static final String JAVA_AGENT_MEMBER_DATA = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMemberDataById";
    /**
     * 查找团队成员
     */
    public static final String JAVA_AGENT_MEMBER_DATA_SEARCH = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMemberDataByMobileOrName";
    /**
     * 验证能否添加用户
     */
    public static final String JAVA_AGENT_CHECK_IF_CAN_ADD_TEAM_MEMBER = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getCheckIfCanAddTeamMember";
    /**
     * 新增团队成员
     */
    public static final String JAVA_AGENT_ADD_TEAM_MEMBER = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/addTeamMember";
    /**
     * 获取团队成员业绩
     */
    public static final String JAVA_AGENT_TEAM_MEMBER_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getTeamMemberAchievement";
    /**
     * 获取团队成员月度业绩
     */
    public static final String JAVA_AGENT_TEAM_MEMBER_MONTH_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getTeamMemberMonthAchievement";
    /**
     * 获取团队成员注册信息
     */
    public static final String JAVA_AGENT_TEAM_MEMBER_INFO = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getTeamMemberInfo";
    /**
     * 更新团队成员信息
     */
    public static final String JAVA_AGENT_UPDATE_TEAM_MEMBER_INFO = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/updateTeamMemberInfo";
    /**
     * 获取成员激活页面信息
     */
    public static final String JAVA_AGENT_ACTIVE_INFO = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getActiveInfo";
    /**
     * 激活成员状态
     */
    public static final String JAVA_AGENT_UPDATE_ACTIVE_STATUS = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/updateActiveStatus";
    /**
     * 获取总业绩
     */
    public static final String JAVA_AGENT_TOTAL_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getTotalAchievement";
    /**
     * 获取每月总业绩
     */
    public static final String JAVA_AGENT_MONTH_TOTAL_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMonthTotalAchievement";
    /**
     * 获取总体月度详细业绩
     */
    public static final String JAVA_AGENT_MONTH_ACHIEVEMENT_DETAIL = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMonthAchievementDetail";
    /**
     * 获取个人代理商跟城市代团队成员的购买用户数量
     */
    public static final String JAVA_AGENT_USER_COUNT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getUserCountOfPersonAgentAndTeamMember";
    /**
     * 获取代理商所属用户购买信息
     */
    public static final String JAVA_AGENT_CUSTOMER_PURCHASE_INFO = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMyCustomerPurchaseInfo";
    /**
     * 获取我的客户购买记录
     */
    public static final String JAVA_AGENT_CUSTOMER_PURCHASE_HISTORY = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getMyCustomerPurchaseHistory";
    /**
     * 获取团队成员或个人代理商的我的业绩
     */
    public static final String JAVA_AGENT_MEMBER_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getAgentMemberAchievement";
    /**
     * 获取团队成员或者个人代理商的月度业绩
     */
    public static final String JAVA_AGENT_MEMBER_MONTH_ACHIEVEMENT = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getAgentMemberMonthAchievement";
    /**
     * 代理商推广页获取代理商状态及信息
     */
    public static final String JAVA_AGENT_STATUS_AND_INFO = Constant.JAVA_AGENT_API_URL + "/wybApi/agent/agentCompany/getAgentStatusAndInfo";
    /**
     * 将数据提交到金柚服务器
     *
     * @param urlPath 接口地址
     * @param mystr   待提交的数据，包括数据以及密文
     * @return 返回json字符串 error为0正常
     * @throws Exception
     */
    public static String getInterface(String urlPath, String mystr, String[] arr) {
        StringBuffer sb = new StringBuffer();
        try {
            String signature = SignUtil.getSignature(arr, urlPath, Constant.PHP_APP_SECRET);
            mystr += "&signature=" + signature;
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(1000 * 50);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.getOutputStream().write(mystr.toString().getBytes("UTF-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            logger.info("提交的数据为：" + urlPath + mystr);

            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str = null;

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            connection.disconnect();
            logger.info("返回的数据为：" + sb);
        } catch (Exception e) {
            logger.error("获取数据出错,提交的数据为：" + urlPath + mystr, e);
        }
        return sb.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        logger.info("提交的数据为：" + url + "?" + param);
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("返回的数据为：" + result);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            logger.info("提交的数据为：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("返回的数据为：" + result);
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e.getMessage(), e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return result;
    }

}
