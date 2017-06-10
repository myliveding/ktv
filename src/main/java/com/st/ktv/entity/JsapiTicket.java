package com.st.ktv.entity;

import lombok.Data;

import java.util.Date;

@Data
public class JsapiTicket {

    private String appid;


    private String ticket;


    private Date creatTime;

}