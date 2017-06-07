package com.st.ktv.entity;

import java.util.Date;

public class TBWechat {

    private String appid;


    private String appsecret;


    private String wxname;

    private String accessToken;


    private Date creattime;


    private String other;

    public String getAppid() {
        return appid;
    }


    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }


    public String getAppsecret() {
        return appsecret;
    }


    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret == null ? null : appsecret.trim();
    }


    public String getWxname() {
        return wxname;
    }


    public void setWxname(String wxname) {
        this.wxname = wxname == null ? null : wxname.trim();
    }


    public String getAccessToken() {
        return accessToken;
    }


    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }


    public Date getCreattime() {
        return creattime;
    }


    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }


    public String getOther() {
        return other;
    }


    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

}