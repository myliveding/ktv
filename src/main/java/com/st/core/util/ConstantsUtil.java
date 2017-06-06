/*
 * @(#)ConstantsUtil.java 2013-11-12下午03:25:14
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.core.util;





/**
 * 全局常量工具类
 * @modificationHistory.  
 * <ul>
 * <li>gaozhanglei 2013-11-12下午03:25:14 </li>
 * </ul>
 */

public class ConstantsUtil {
	
	/**
	 * 默认字符编码集
	 */
	public static final String SYSTEM_DEFAULT_ENCODING = "utf-8";
	
	/**
	 * 每页多少
	 */
	public static final int PAGE_SIZE = 8;
	
	/**
	 * 中奖范围
	 */
	public final static Double WIN_PRIZE_RANGE = 100000.0;
	
    /**
     * 淘宝的ip地址解析接口
     */
    public static final String TAOBAO_IP = "http://ip.taobao.com/service/getIpInfo.php?ip=IP";
    
    /**
     * 百度的ip地址解析接口
     */
    public static final String BAIDU_IP = "http://api.map.baidu.com/location/ip?ak=AK&ip=IP";
    
    /**
     * 百度IP地址解析的密钥
     */
    public static final String[] AK_S = {"E5GzF0Ha4sXCfOKIkn0fMUmf","c8t2TddjenpVGWf89aQnvz8l","T5Qt6y4rGOdmMZaoEjWyhX4O",
		"WwSfPk5df4xzDyNo4GVegqyA","BDE0SAW6EHBuaU1bLpNHER3q","YGuumjDMCpz8euqUDdiO7zOb","9hxGcYW3XBddOcW2QNrcwzrz",
		"CppWNXhOr1D2j1SXY0DuB5OH","C5tpP4CYTs9l1R5eyoShufQy","QFZEYB0t9kV1mfEAodBZm9WQ"};

}
