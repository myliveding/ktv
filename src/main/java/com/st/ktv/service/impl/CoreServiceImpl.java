package com.st.ktv.service.impl;

import com.st.ktv.entity.wx.RespObject;
import com.st.ktv.service.TBUserTransferService;
import com.st.ktv.service.TBWechatMessageService;
import com.st.ktv.service.TBWechatReciveRecordService;
import com.st.ktv.service.TBWechatService;
import com.st.utils.Constant;
import com.st.core.handle.SpringContextHolder;
import com.st.utils.DataUtil;
import com.st.utils.DateUtil;
import com.st.ktv.entity.wx.AccessToken;
import com.st.ktv.entity.resp.*;
import com.st.ktv.entity.TBUserTransfer;
import com.st.ktv.entity.TBWechat;
import com.st.ktv.entity.TBWechatMessage;
import com.st.ktv.entity.TBWechatReciveRecord;
import com.st.utils.wx.MessageUtil;
import com.st.utils.wx.WeixinUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 处理微信输入窗口发来的请求
 * @author wuhao
 * @date 2015-06-01
 */
public class CoreServiceImpl {

	private static Logger logger =LoggerFactory.getLogger(CoreServiceImpl.class);

	/**
	 * 处理微信发来的请求
	 *
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {

		String respMessage = null;
		String respType = MessageUtil.RESP_MESSAGE_TYPE_TEXT;//默认回复文本消息
        boolean send = true;
		TBUserTransferService tBUserTransferService = SpringContextHolder.getBean("tBUserTransferService");
		TBWechatMessageService tbWechatMessageService = SpringContextHolder.getBean("tBWechatMessageService");
		TBWechatReciveRecordService tBWechatReciveRecordService = SpringContextHolder.getBean("tBWechatReciveRecordService");

		try {
			// 默认返回的文本消息内容
//		    String respContent="如需在线客服帮助，请回复“客服”或者“KF”，也可点击“微服务”菜单中“在线客服”与客服取得联系。客服工作时间：上午9:00-下午6:00，全年无休。";
//		    String respContent = "HI，请问有什么可以帮助您的吗？<a href=\"http://wybhotline.udesk.cn/im_client/?group_id=18040&channel=wyb\">" +
			String respContent = "HI，请问有什么可以帮助您的吗？<a href=\"http://joyowo.udesk.cn/im_client?group_id=1608&channel=wyb\">" +
					"戳这</a>即可联系客服人员，也可拨打客服电话：400-111-8900。客服工作时间是上午9:00-下午5:30。（周末正常上班，法定节假日除外）";

            List<TBWechatMessage> defaultRC = tbWechatMessageService.getMessageListByKeyLike("默认回复内容");
			if (defaultRC.size()==1){
				respContent=defaultRC.get(0).getRespContent();
			}

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			//消息内容
			String countent = requestMap.get("Content");
			List<Article> articles = new ArrayList<Article>();
			Image image=new Image();
			//判断是否是多客服消息
			try {
				logger.info("判断多客服消息");
				TBUserTransfer tBUserTransfer = tBUserTransferService.selectByPrimaryKey(fromUserName);
				if (tBUserTransfer!=null&&"1".equals(tBUserTransfer.getState())&&!(MessageUtil.REQ_MESSAGE_TYPE_EVENT).equals(msgType)) {
					Date date = new Date();//创建现在的日期
					long timeDB = tBUserTransfer.getUpdateTime().getTime();
					long timeService = date.getTime();//获得毫秒数
					//判断客服消息是否过期
					if((timeService-timeDB) < 7200000){
					}else {
						logger.info("客服对话已过期");
					}
				}
			} catch (Exception e) {
				logger.error("判断是否是多客服消息出错"+e.getMessage(), e);
			}

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				if("客服".equals(countent)||"KF".equals(countent.toUpperCase())){ //用户发送“客服”或者“KF”，接入多客服
					logger.info("客服事件-----");
					try {
                        Date date = new Date();
						TBUserTransfer tBUserTransferTempDB = tBUserTransferService.selectByPrimaryKey(fromUserName);
						if(!"".equals(tBUserTransferTempDB) && tBUserTransferTempDB!=null){
							tBUserTransferTempDB.setState("1");
							tBUserTransferTempDB.setUpdateTime(date);
							tBUserTransferService.updateByPrimaryKeySelective(tBUserTransferTempDB);
						}else {
							tBUserTransferTempDB = new TBUserTransfer();
							tBUserTransferTempDB.setState("1");
							tBUserTransferTempDB.setOpenid(fromUserName);
							tBUserTransferTempDB.setCreateTime(date);
							tBUserTransferTempDB.setUpdateTime(date);
							tBUserTransferService.insertSelective(tBUserTransferTempDB);
						}
					} catch (Exception e) {
						logger.error("打开客服通道1失败"+e.getMessage(), e);
					}
				}else {
					if (Pattern.matches(DataUtil.ZSZ, countent)){ //只允许中文，数值，字母
						countent = countent.replaceAll("\\s*", "");
						logger.info("自动回复关键词：" + countent);
                        RespObject respObject = getRespContent(tbWechatMessageService, tBWechatReciveRecordService,
                                articles, image, fromUserName, respType,  respContent,  countent);
                        respContent = respObject.getRespContent();
                        respType = respObject.getRespType();
					}
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
//				respContent = "您发送的是图片消息！";
				TBWechatReciveRecord tbWechatReciveRecord=new TBWechatReciveRecord();
				tbWechatReciveRecord.setOpenid(fromUserName);
				tbWechatReciveRecord.setContext(requestMap.get("PicUrl"));
				tbWechatReciveRecord.setTypes(msgType);
				tbWechatReciveRecord.setCreateTime(DateUtil.getNowTime());
				tBWechatReciveRecordService.insertSelective(tbWechatReciveRecord);
				List<TBWechatMessage> list=tbWechatMessageService.getMessageListByType("imageRecive");
				if (list.size()>0) {
					respContent = list.get(0).getRespContent();
				}
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else {
				if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                    Date date = new Date();

					// 事件类型
					String eventType = requestMap.get("Event");
					// 订阅
					if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
						logger.info("用户关注，openid=" + fromUserName);
						respContent = "缴社保，找无忧保就对了！\n\n"
								+ "无忧保为你提供社保代缴服务。由中国人社部认证许可，由招商银行进行资金监管，3分钟解决你的社保难题。\n\n"
								+ "☞买社保，<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APP_ID + "&redirect_uri=" + Constant.URL + "/scope/openid.do?next=personsocial/gotoindex.do" + Constant.APP_ID + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\">点击这里！</a>\n"
								+ "☞算保费，<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APP_ID + "&redirect_uri=" + Constant.URL + "/scope/openid.do?next=personsocial/gotojsq.do" + Constant.APP_ID + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\">点击这里！</a>\n"
								+ "☞学社保，<a href=\"http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzAxMzU1Mzg5Ng==#wechat_webview_type=1&wechat_redirect\">点击这里！</a>\n"
								+ "☞要提问，<a href=\"http://joyowo.udesk.cn/im_client?group_id=1608&channel=wyb\">点击这里！</a>\n\n"
								+ "查看往期社保干货内容，<a href=\"http://mp.weixin.qq.com/mp/homepage?__biz=MzAxMzU1Mzg5Ng==&hid=1&sn=9e7716381c377e6928a341ca18ef1cf1#wechat_redirect\">点击这里！</a>"
								+ "成为无忧保社保代理商，<a href=\"http://form.mikecrm.com/EDfP0u\">点击这里！</a>";
						String eventKey = requestMap.get("EventKey");
						if (DataUtil.isNotEmpty(eventKey) && eventKey.indexOf("qrscene") > -1) {
							String scene = eventKey.substring(eventKey.indexOf("_") + 1, eventKey.length());
							String jsonStr = "{\"openid\":\"" + fromUserName + "\",\"to_groupid\":" + Integer.parseInt(scene) + "}";
							String accessToken = CoreServiceImpl.getAccessToken();
                            logger.info("Core获取accessToken：" + accessToken);
							JSONObject jsonObject = WeixinUtil.updateUserGroup(accessToken,jsonStr);
							if (jsonObject != null && jsonObject.containsKey("errcode")) {
								if (jsonObject.getInt("errcode") == 40001) {
                                    TBWechatService tBWechatService = SpringContextHolder.getBean("tBWechatService");
                                    accessToken = getAccessTokenIm(tBWechatService, date);
                                    logger.info("数据库中值有误重新获取accesstoken：" + accessToken);
                                    WeixinUtil.updateUserGroup(accessToken, jsonStr);
								}
							}
						}
					}
					// 取消订阅
					else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
						// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					}
					// 自定义菜单点击事件
					else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
						String eventKey = requestMap.get("EventKey");
						logger.info("自定义菜单点击事件-----" + eventKey);
						if ("在线咨询".equals(eventKey)) {
							try {
								TBUserTransfer tBUserTransferTempDB = tBUserTransferService.selectByPrimaryKey(fromUserName);
								if (!"".equals(tBUserTransferTempDB) && tBUserTransferTempDB != null) {
									tBUserTransferTempDB.setState("1");
									tBUserTransferTempDB.setUpdateTime(new Date());
									tBUserTransferService.updateByPrimaryKeySelective(tBUserTransferTempDB);
								} else {
									tBUserTransferTempDB = new TBUserTransfer();
									tBUserTransferTempDB.setState("1");
									tBUserTransferTempDB.setOpenid(fromUserName);
									tBUserTransferTempDB.setCreateTime(new Date());
									tBUserTransferTempDB.setUpdateTime(new Date());
									tBUserTransferService.insertSelective(tBUserTransferTempDB);
								}
							} catch (Exception e) {
								logger.error("打开客服通道2失败" + e.getMessage(), e);
							}
//						respContent="HI,我是小忧，请问有什么可以帮您的吗？\n在这里直接输入问题即可咨询，也可以直接拨打客服电话：400-111-8900\n我的工作时间是上午9:00-下午6:00，全年无休。";
						} else {
                            RespObject respObject = getRespContent(tbWechatMessageService, tBWechatReciveRecordService,
                                    articles, image, fromUserName, respType,  respContent,  eventKey);
                            respContent = respObject.getRespContent();
                            respType = respObject.getRespType();
						}
					}
				}
			}
			if (respType.equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)&&send){
				// 回复文本消息
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
				logger.info("回复文本消息 "+fromUserName+" 内容 "+respContent);
			}else if(respType.equals(MessageUtil.RESP_MESSAGE_TYPE_NEWS)){
				//回复图文消息
				NewsMessage newsMessage=new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setArticleCount(articles.size());
				newsMessage.setArticles(articles);
				respMessage=MessageUtil.newsMessageToXml(newsMessage);
				for(Article article:articles){
					logger.info("回复图文消息 "+fromUserName+" 数量 "+articles.size()+" 标题 "+article.getTitle());
				}
			}else if (respType.equals(MessageUtil.RESP_MESSAGE_TYPE_IMAGE)){
				ImageMessage imageMessage=new ImageMessage();
				imageMessage.setToUserName(fromUserName);
				imageMessage.setFromUserName(toUserName);
				imageMessage.setCreateTime(new Date().getTime());
				imageMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
				imageMessage.setImage(image);
				respMessage=MessageUtil.imageMessageToXml(imageMessage);
				logger.info("回复图片消息 "+fromUserName+" 内容 "+image.getMediaId());
			}
		} catch (Exception e) {
			logger.error("打开客服通道3失败"+e.getMessage(), e);
		}
		return respMessage;
	}

    /**
     * 获取应答相关信息
     * @param tbWechatMessageService
     * @param tBWechatReciveRecordService
     * @param articles
     * @param image
     * @param fromUserName
     * @param respType
     * @param respContent
     * @param eventKey
     * @return
     */
    private static RespObject getRespContent(TBWechatMessageService tbWechatMessageService,
                                             TBWechatReciveRecordService tBWechatReciveRecordService,
                                             List<Article> articles, Image image, String fromUserName,
                                             String respType, String respContent, String eventKey){

        RespObject respObject = new RespObject();

        List<TBWechatMessage> list = tbWechatMessageService.getMessageListByKeyLike(eventKey);
        if (!list.isEmpty()) {
            for (TBWechatMessage tbWechatMessage : list) {
                if (tbWechatMessage.getContent().equals(eventKey)) {
                    if (tbWechatMessage.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)) {
                        respContent = tbWechatMessage.getRespContent();
                    } else if (tbWechatMessage.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_NEWS)) {
                        respType = MessageUtil.RESP_MESSAGE_TYPE_NEWS;
                        Article article = new Article();
                        article.setTitle(tbWechatMessage.getTitle());
                        article.setDescription(tbWechatMessage.getRespContent());
                        article.setPicUrl(tbWechatMessage.getFirstUrl());
                        article.setUrl(tbWechatMessage.getSecondUrl());
                        articles.add(article);
                    }else if (tbWechatMessage.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_IMAGE)) {
                        respType=MessageUtil.RESP_MESSAGE_TYPE_IMAGE;
                        image.setMediaId(tbWechatMessage.getRespContent());
                    }
                } else if (tbWechatMessage.getContent().equals("1") && eventKey.indexOf(tbWechatMessage.getContent()) > -1) {
                    if (tbWechatMessage.getThumb() != null && tbWechatMessage.getThumb().equals("openid")) {
                        List<TBWechatReciveRecord> tbWechatReciveRecordList = tBWechatReciveRecordService.selectByOpenid(fromUserName);
                        if (tbWechatReciveRecordList.size() > 0) {
                            respContent = tbWechatMessage.getRespContent();
                        }
                    } else {
                        respContent = tbWechatMessage.getRespContent();
                    }
                }
            }
        }
        respObject.setRespType(respType);
        respObject.setRespContent(respContent);
        return respObject;
    }

    /**
     * 去获取AccessToken，同时判断是否过期
     * @return
     */
	public static String getAccessToken() {

		TBWechatService tBWechatService = SpringContextHolder.getBean("tBWechatService");

		TBWechat tBWechat = tBWechatService.selectByPrimaryKey(Constant.APP_ID);
		String token = "-1";
		Date date = new Date();//创建现在的日期
		if (!"".equals(tBWechat) && tBWechat != null) {
			long timeDB = tBWechat.getCreattime().getTime();
			long timeService = date.getTime();//获得毫秒数
			if (!"".equals(tBWechat.getAccessToken()) && tBWechat.getAccessToken() != null) {
				if ((timeService - timeDB) < 5000000) {
					token = tBWechat.getAccessToken();
					logger.info("获取到数据库accesstoken：" + token);
				} else {
					AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
					if (null != accessToken) {
						token = accessToken.getToken();
                        logger.info("获取到服务器的accesstoken：" + token);
						tBWechat.setAccessToken(token);
						tBWechat.setCreattime(date);
						tBWechatService.updateByPrimaryKeySelective(tBWechat);
					}
				}
			} else {
				AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
				if (null != accessToken) {
					token = accessToken.getToken();
					logger.info("数据库中有记录但是没有该值,此公众号获取accesstoken=" + token);
					tBWechat.setAccessToken(token);
					tBWechat.setCreattime(date);
					tBWechatService.updateByPrimaryKeySelective(tBWechat);
				}
			}
		} else {
			//appid对应的微信公众平台为空，获取并插入
            token = getAccessTokenIm(tBWechatService, date);
            logger.info("数据库中没有初始值,此公众号第一次获取accesstoken=" + token);
		}
		return token;
	}

    /**
     * 再次获取token
     * @return
     */
	private static String getAccessTokenIm(TBWechatService tBWechatService, Date date) {
//		TBWechatService tBWechatService = SpringContextHolder.getBean("tBWechatService");
		String token = "-1";
//		Date date = new Date();
		//appid对应的微信公众平台为空
		TBWechat tBWechat = new TBWechat();
		AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
		if (null != accessToken) {
			token = accessToken.getToken();
			tBWechat.setAccessToken(token);
			tBWechat.setCreattime(date);
			tBWechat.setAppid(Constant.APP_ID);
			tBWechat.setAppsecret(Constant.APP_SECRET);
			tBWechat.setWxname("ktv");
			tBWechatService.updateByPrimaryKeySelective(tBWechat);
		}
		return token;
	}
}