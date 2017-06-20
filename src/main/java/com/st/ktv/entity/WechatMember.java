package com.st.ktv.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WechatMember {

    private Integer id;

    private String nickName;

    private String headPortrait;

    private String mobile;

    private String email;

    private Integer level;

    private String openid;

    private Integer availableScore;

    private Integer totalScore;

    private Double commission;

    private Double balance;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;

    private Boolean isDelete;

}