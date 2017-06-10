package com.st.ktv.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TBUserTransfer {

    private String openid;

    private String state;

    private Date createTime;

    private Date updateTime;

}