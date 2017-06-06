package com.st.javabean.pojo.req;

/**
 * 文本消息
 * 
 * @author wuhao
 * @date 2015-06-01
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}