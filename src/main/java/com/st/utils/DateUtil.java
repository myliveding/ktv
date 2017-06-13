/*
 * @(#)java 2010-8-27下午04:06:10
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



public class DateUtil {

    public static int getNowTime() {
        Long current = System.currentTimeMillis();
        String currentTime = String.valueOf(current / 1000);
        return Integer.parseInt(currentTime);
    }

	/**
	 * 时间是否合法
	 * @author sunju
	 * @creationDate. 2012-6-19 下午01:07:59 
	 * @param date 时间
	 * @param dateFormat 时间格式
	 * @return
	 */
	public static boolean isValidDate(String date, String dateFormat) {
		try {
			new SimpleDateFormat(dateFormat).parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 转换成yyyy-MM-dd
	 * @param time
	 * @return
	 */
	public static String convertToDate(int time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(time*1000L));
		return date;
	}
	
    /**
    * @Title: convertToDate
    * @Description: 数据库取出的int类型的日期
    * @param time
    * @param format
    * @return String
    */
    public static String convertToDate(int time,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date(time*1000L));
        return date;
    }


    public static String simpleDateFormat(String dateTime, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateTime); //Mon Jan 14 00:00:00 CST 2013
            System.err.println(date);
//            System.err.println(sdf.format(dateTime));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void handel(){
        ///   打印出2006年11月17日 15时19分56秒
        SimpleDateFormat myFmt=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        //打印   06/11/17 15:19
        SimpleDateFormat myFmt1=new SimpleDateFormat("yy/MM/dd HH:mm");

        //2006-11-17 15:19:56
        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2006年11月17日 15时19分56秒 星期五
        SimpleDateFormat myFmt3=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");

        // 一年中的第 321 天 一年中第46个星期 一月中第3个星期 在一天中15时 CST时区
        SimpleDateFormat myFmt4=new SimpleDateFormat(
                "一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 z时区");

        Date now=new Date();
        System.out.println(myFmt2.format(now));
        System.out.println(myFmt.format(now));
        System.out.println(myFmt1.format(now));

        System.out.println(myFmt3.format(now));
        System.out.println(myFmt4.format(now));
    }

    /**
     * 传入dateTime类型的时间，转换成你要的格式包含带有汉字的
     * @param dateTime
     * @param format
     * @return
     */
    public static String getFormatByDateTime(String dateTime, String format){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            SimpleDateFormat myFmt = new SimpleDateFormat(format);
            dateTime = myFmt.format(sdf.parse(dateTime));
        }catch (ParseException e){
            e.printStackTrace();
        }
        return dateTime;
    }

	public static void main(String[] args) throws ParseException{
		String str = "2013-01-14 12:12:12";
        System.err.println(getFormatByDateTime(str,"yyyy年MM月dd日"));
	}
}