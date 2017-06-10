package com.st.ktv.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TBWechat {

    private String appid;


    private String appsecret;


    private String wxname;

    private String accessToken;


    private Date creattime;


    private String remark;


}