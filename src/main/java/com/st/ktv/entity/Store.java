package com.st.ktv.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/8.
 */
@Data
public class Store {

    private String name;
    private Integer smallNum;
    private Integer vipNum;
    private Integer mediumNum;
    private Integer bigNum;
    private BigDecimal distance;


}
