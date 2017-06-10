package com.st.ktv.entity;

import lombok.Data;

@Data
public class TBWechatReciveRecord {

    private Integer id;

    private String types;

    private String openid;

    private String context;

    private String scene;

    private Integer parentid;

    private Integer createTime;

}