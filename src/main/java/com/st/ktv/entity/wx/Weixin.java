package com.st.ktv.entity.wx;

public class Weixin {

	private String jsapiTicket; //js调用的凭据
	private String accessToken;	//accesstoken
	
	public String getJsapiTicket() {
		return jsapiTicket;
	}
	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public Weixin() {
		super();
	}

	public Weixin(String jsapiTicket, String accessToken) {
		super();
		this.jsapiTicket = jsapiTicket;
		this.accessToken = accessToken;
	}
	
}
