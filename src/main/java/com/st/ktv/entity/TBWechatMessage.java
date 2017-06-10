package com.st.ktv.entity;

import lombok.Data;

@Data
public class TBWechatMessage {

    private Integer id;
    private String content;
    private String respContent;
    private String msgType;
    private String title;
    private String firstUrl;
    private String secondUrl;
    private String thumb;

}