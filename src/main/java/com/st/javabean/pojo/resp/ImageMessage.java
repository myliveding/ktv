package com.st.javabean.pojo.resp;

import com.st.javabean.pojo.req.BaseMessage;

public class ImageMessage extends BaseMessage {
	// 图片链接
	private Image Image;

	public com.st.javabean.pojo.resp.Image getImage() {
		return Image;
	}

	public void setImage(com.st.javabean.pojo.resp.Image image) {
		Image = image;
	}
}