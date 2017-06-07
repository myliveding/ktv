package com.st.ktv.entity.resp;

import com.st.ktv.entity.req.BaseMessage;

public class ImageMessage extends BaseMessage {
	// 图片链接
	private Image Image;

	public com.st.ktv.entity.resp.Image getImage() {
		return Image;
	}

	public void setImage(com.st.ktv.entity.resp.Image image) {
		Image = image;
	}
}