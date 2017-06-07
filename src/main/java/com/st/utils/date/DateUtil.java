/*
 * @(#)java 2010-8-27下午04:06:10
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.utils.date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;





/**
 * 日期工具类,提供时间转换、比较、格式化等各种常用方法
 * @modificationHistory.  
 * <ul>
 * <li>gaozhanglei 2010-8-27下午04:06:10 </li>
 * <li>
 * sunju 2012-7-31 下午03:06:05 新增getDate方法，
 * 修改getNowDate方法以及其它几个使用了toLocaleString()的方法，解决Linux下时间错误问题
 * </li>
 * </ul>
 */

public class DateUtil {
	private static Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * 时间间隔：日
	 */
	public final static int DATE_INTERVAL_DAY = 1;
	/**
	 * 时间间隔：周
	 */
	public final static int DATE_INTERVAL_WEEK = 2;
	/**
	 * 时间间隔：月
	 */
	public final static int DATE_INTERVAL_MONTH = 3;
	/**
	 * 时间间隔：年
	 */
	public final static int DATE_INTERVAL_YEAR = 4;
	/**
	 * 时间间隔：小时
	 */
	public final static int DATE_INTERVAL_HOUR = 5;
	/**
	 * 时间间隔：分钟
	 */
	public final static int DATE_INTERVAL_MINUTE = 6;
	/**
	 * 时间间隔：秒
	 */
	public final static int DATE_INTERVAL_SECOND = 7;
	/**
	 * 时间格式：年月日
	 */
	public final static String DATE_FORMAT_YMD = "yyyy-MM-dd";
	public final static String DATE_FORMAT_YMDHMS_ZH = "yyyy年MM月dd日 ";
	/**
	 * 时间格式：年月日时分秒
	 */
	public final static String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	public final static String DATE_FORMATE_LX_YMDHMS = "yyyyMMddHHmmss";
	public final static String DATE_FORMATE_LX_YMDHMSSS = "yyyyMMddHHmmssSSS";
	public final static String DATE_FORMATE_YMDD = "yyyy.MM.dd";
	/**
	 * 获得时间
	 * @author sunju
	 * @creationDate. 2012-7-31 下午03:06:05 
	 * @param date 时间
	 * @param dateFormat 时间格式
	 * @return 时间
	 */
	public static Date getDate(Date date, String dateFormat) {
		return dateFormat(dateFormat(date, dateFormat), dateFormat);
	}
	/**
	 * 获得当前日期(年月日)
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:11:23
	 * @return 当前时间（yyyy-MM-dd）
	 */
	public static Date getNowDate() {
		return dateFormat(dateFormat(new Date(), DATE_FORMAT_YMD), DATE_FORMAT_YMD);
	}
	/**
	 * 获取当前时间字符串(年月日)
	 * @author sunju
	 * @creationDate. 2011-5-4 下午08:22:34 
	 * @return 时间字符串
	 */
	public static String getNowStringDate() {
	    return dateFormat(new Date(), DATE_FORMAT_YMD);
	}
	/**
	 * 获得当前时间(年月日时分秒)
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:12:57
	 * @return 当前时间（yyyy-MM-dd HH:mm:ss）
	 */
	public static Date getNowTime() {
		return dateFormat(dateFormat(new Date(), DATE_FORMAT_YMDHMS), DATE_FORMAT_YMDHMS);
	}
	public static int getNowIntTime() {
		Long current = System.currentTimeMillis();
		String currentTime = String.valueOf(current / 1000);
		return Integer.parseInt(currentTime);
	}
	/**
	 * 获取当前时间字符串(年月日时分秒)
	 * @author sunju
	 * @creationDate. 2014-3-10 下午03:16:42 
	 * @return 时间字符串
	 */
	public static String getNowStringTime() {
		 return dateFormat(new Date(), DATE_FORMAT_YMDHMS);
	}
	/**
	 * 获得明天的日期字符串(格式年月日)
	 * @author sunju
	 * @creationDate. 2011-5-4 下午08:19:28 
	 * @return 明天的日期
	 */
	public static String getTomorrowStringDate() {
	    return dateFormat(getTomorrowTime(), DATE_FORMAT_YMD);
	}
	/**
	 * 获得明天的日期(年月日)
	 * @author sunju
	 * @creationDate. 2011-5-4 下午08:19:57 
	 * @return 明天的日期
	 */
	public static Date getTomorrowDate() {
		return dateAdd(DATE_INTERVAL_DAY, getNowDate(), 1);
	}
	/**
	 * 获得明天的时间(年月日时分秒)
	 * @author sunju
	 * @creationDate. 2011-5-4 下午08:20:19 
	 * @return 明天的时间
	 */
	public static Date getTomorrowTime() {
	    return dateAdd(DATE_INTERVAL_DAY, getNowTime(), 1);
	}
	/**
	 * 获得昨天的日期
	 * @author sunju
	 * @creationDate. 2013-10-22 下午03:54:48 
	 * @return 昨天的日期
	 */
	public static Date getYesterdayDate() {
		return dateAdd(DATE_INTERVAL_DAY, getNowDate(), -1);
	}
	/**
	 * 获取当月第一天   
	 * @author sunju
	 * @creationDate. 2013-10-22 下午03:45:53 
	 * @return 日期
	 */
    public static Date getMonthFirst() {
    	Calendar lastDate = Calendar.getInstance();
    	lastDate.set(Calendar.DATE, 1); // 设为当前月的1号
    	return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }
    /**
     * 获得下个月第一天的日期
     * @author sunju
     * @creationDate. 2013-10-22 下午03:52:38 
     * @return 日期
     */
    public static Date getNextMonthFirst() {
    	Calendar lastDate = Calendar.getInstance();
    	lastDate.add(Calendar.MONTH, 1); // 加一个月
    	lastDate.set(Calendar.DATE, 1);  // 把日期设置为当月第一天
    	return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }
	/**
	 * 取得当前星期几
	 * @author sunju
	 * @creationDate. 2010-9-20 下午05:34:36 
	 * @param date 时间
	 * @return 星期
	 */
	public static String getWeekOfDate(Date date) {
		if (date == null) return null;
	    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"}; 
	    Calendar cal = Calendar.getInstance(); 
	    cal.setTime(date); 
	    int w = cal.get(Calendar.DAY_OF_WEEK) - 1; 
	    if (w < 0) w = 0; 
	    return weekDays[w]; 
	}
	/**
	 * 时间类型转换返回字符串
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:18:37
	 * @param date 时间
	 * @param dateFormat 时间格式
	 * @return 转换后的时间字符串
	 */
	public static String dateFormat(Date date, String dateFormat) {
		if (date == null) return null;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}
	/**
	 * 字符串时间类型转换返回Date类型
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:23:35
	 * @param date 字符串时间
	 * @param dateFormat 时间格式
	 * @return 转换后的时间
	 */
	public static Date dateFormat(String date, String dateFormat) {
		if (StringUtils.isEmpty(date)) return null;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(date);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return null;
		}
	}
	/**
	 * 加时间
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:28:06
	 * @param interval 增加项，可以是天数、月份、年数、时间、分钟、秒
	 * @param date 时间
	 * @param num 加的数目
	 * @return 时间加后的时间
	 */
	public static Date dateAdd(int interval, Date date, int num) {
		if (date == null) return null;
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		switch (interval) {
		case DATE_INTERVAL_DAY:
			calendar.add(Calendar.DATE, num);
			break;
		case DATE_INTERVAL_WEEK:
			calendar.add(Calendar.WEEK_OF_MONTH, num);
			break;
		case DATE_INTERVAL_MONTH:
			calendar.add(Calendar.MONTH, num);
			break;
		case DATE_INTERVAL_YEAR:
			calendar.add(Calendar.YEAR, num);
			break;
		case DATE_INTERVAL_HOUR:
			calendar.add(Calendar.HOUR, num);
			break;
		case DATE_INTERVAL_MINUTE:
			calendar.add(Calendar.MINUTE, num);
			break;
		case DATE_INTERVAL_SECOND:
			calendar.add(Calendar.SECOND, num);
			break;
		default:
		}
		return calendar.getTime();
	}
	/**
	 * 两个时间时间差[前面时间和比较时间比,小于比较时间返回负数]
	 * @author sunju
	 * @creationDate. 2010-8-27 下午05:26:13
	 * @param interval 比较项，可以是天数、月份、年数、时间、分钟、秒
	 * @param date 时间
	 * @param compare 比较的时间
	 * @return 时间差(保留两位小数点,小数点以后两位四舍五入)
	 */
	public static double getDateDiff(int interval, Date date, Date compare) {
		if (date == null || compare == null) return 0;
		double result = 0;
		double time = 0;
		Calendar calendar = null;
		switch (interval) {
		case DATE_INTERVAL_DAY:
			time = date.getTime() - compare.getTime();
			result = time / 1000d / 60d / 60d / 24d;
		    break;
		case DATE_INTERVAL_HOUR:
			time = date.getTime() - compare.getTime();
			result = time / 1000d / 60d / 60d;
			break;
		case DATE_INTERVAL_MINUTE:
			time = date.getTime() / 1000d / 60d;
			result = time - compare.getTime() / 1000d / 60d;
			break;
		case DATE_INTERVAL_MONTH:
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		    time = calendar.get(Calendar.YEAR) * 12d;
		    calendar.setTime(compare);
		    time -= calendar.get(Calendar.YEAR) * 12d;
		    calendar.setTime(date);
		    time += calendar.get(Calendar.MONTH);
		    calendar.setTime(compare);
		    result = time - calendar.get(Calendar.MONTH);
			break;
		case DATE_INTERVAL_SECOND:
			time = date.getTime() - compare.getTime();
			result = time / 1000d;
			break;
		case DATE_INTERVAL_WEEK:
			calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    time = calendar.get(Calendar.YEAR) * 52d;
		    calendar.setTime(compare);
		    time -= calendar.get(Calendar.YEAR) * 52d;
		    calendar.setTime(date);
		    time += calendar.get(Calendar.WEEK_OF_YEAR);
		    calendar.setTime(compare);
		    result = time - calendar.get(Calendar.WEEK_OF_YEAR);
			break;
		case DATE_INTERVAL_YEAR:
			calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    time = calendar.get(Calendar.YEAR);
		    calendar.setTime(compare);
		    result = time - (double)calendar.get(Calendar.YEAR);
			break;
		default:
			break;
		}
		return BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 获取时间差[前面时间和比较时间比,小于比较时间返回负数]
	 * @author sunju
	 * @creationDate. 2010-9-1 下午04:36:07 
	 * @param level 返回时间等级(1:返回天;2:返回天-小时;3:返回天-小时-分4:返回天-小时-分-秒;)
	 * @param date 时间
	 * @param compare 比较的时间
	 * @return 时间差(保留两位小数点,小数点以后两位四舍五入)
	 */
	public static String getDateBetween(Integer level, Date date, Date compare) {
		if (date == null || compare == null) return null;
		long s = BigDecimal.valueOf(getDateDiff(DATE_INTERVAL_SECOND, date, compare)).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
		int ss = 1;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;
	   
		long day = s / dd;
		long hour = (s - day * dd) / hh;
		long minute = (s - day * dd - hour * hh) / mi;
		long second = (s - day * dd - hour * hh - minute * mi) / ss;
		String flag =(day < 0 || hour < 0 || minute < 0 || second < 0) ? "-" : "";
		day = Math.abs(day);
		hour = Math.abs(hour);
		minute = Math.abs(minute);
		second = Math.abs(second);
		StringBuilder result = new StringBuilder(flag);
		switch (level) {
		case 1:
			if (day != 0)result.append(day).append("天");
			break;
		case 2:
			if (day != 0)result.append(day).append("天");
			if (hour != 0)result.append(hour).append("小时");
			break;
		case 3:
			if (day != 0)result.append(day).append("天");
			if (hour != 0)result.append(hour).append("小时");
			if (minute != 0)result.append(minute).append("分");
			break;
		case 4:
			if (day != 0)result.append(day).append("天");
			if (hour != 0)result.append(hour).append("小时");
			if (minute != 0)result.append(minute).append("分");
			if (second != 0)result.append(second).append("秒");
			break;
		default:
			break; 
		}
		return result.toString();
	}
	/**
	 * 时间是否是今天
	 * @author sunju
	 * @creationDate. 2011-5-4 下午08:24:58 
	 * @param date 时间
	 * @return 布尔
	 */
	public static boolean isToday(Date date) {
		if (date == null) return false;
	    return getNowStringDate().equals(dateFormat(date, DATE_FORMAT_YMD));
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
	 * 
	 * 是否大于现在的时间  
	 * true 大于
	 * <ul>
	 * <li>
	 * <b>原因：<br/>
	 * <p>
	 * [2014-8-27]gaozhanglei<br/>
	 * @param date
	 * @param dateFormate
	 * @return
	 * TODO
	 * </p>
	 * </li>
	 * </ul>
	 */
	public static boolean isgtnow(String date,String dateFormate) {
		  boolean flag=false;
		try {
			Date nowdt=new Date();
			Date  compt=DateUtil.dateFormat(date, dateFormate);
			long nowtm=nowdt.getTime();
			long comptm=compt.getTime();
			if(comptm > nowtm) {
				flag=true;
			}
		}catch (Exception e) {
			flag=false;
		}
		
		return flag;
	}
	/**   
	 * 得到本月的最后一天   
	 *    
	 * @return   
	 */    
	public static String getMonthLastDay() {     
	    Calendar calendar = Calendar.getInstance();     
	    calendar.set(Calendar.DAY_OF_MONTH, calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));     
	    return dateFormat(calendar.getTime(),DATE_FORMAT_YMD);     
	}
	
	/**
	 * 转换成yyyy-MM-dd-
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
//   /**
//    * 转换成yyyyy-MM-dd
//    * @param time
//    * @return
//    */
//   public static String convertToInt(String time){
//       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//       Calendar cal = Calendar.getInstance();    
//       try {
//    	   cal.setTime(sdf.parse(time));
//	   } catch (ParseException e) {
//	       e.printStackTrace();
//	   }    
//       long time1 = cal.getTimeInMillis();
//       String date =String.valueOf(time1/1000);
//       return date;
//   }
	
   /**
    * 转换成yyyyy-MM-dd
    * @param time
    * @return
    */
   public static int convertToInt(String time){
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
       Calendar cal =  getCalendar(time);  ;    
       long time1 = cal.getTimeInMillis();
       return (int) (time1/1000);
   }
   
   /**
    * 转换成yyyyy-MM-dd
    * @param time
    * @return
    */
   public static int convertToInt0(String time){
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
       Calendar cal =  getCalendar(time);  ;    
       long time1 = cal.getTimeInMillis();
       if((int) (time1/1000)<10000000){
           return 0; 
       }else{
           return (int) (time1/1000);
       }
       
   }
	
   /**
   * 两个日期相差月数 
   * @param time1
   * @param time2
   * @return months
   * @throws  
   */
  public static int getMonths(String time1,String time2){
      int months=0;
      int years=0;
      Calendar c1 = getCalendar(time1);  
      Calendar c2 = getCalendar(time2);  
      int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);  
      if (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR) == 0)  
      {  
          years=0;
      }else{  
          years=c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
      }  
      if (month != 0)  
      {  
          months=(c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH))+years*12;
      }  
      if(months<0){
          months+=12;
      }
      return months;
  }
  /**
   * 两个日期比较月份大小（后者大 返回正数；前者大负数）
   * @param time1
   * @param time2
   * @return months
   * @throws  
   */
 public static int monthCompare(String time1,String time2){
      Calendar c1 = getCalendar(time1);  
      Calendar c2 = getCalendar(time2);  
      int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);  
      int years=c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
      int months=month+years*12;
      return months;
  }
  
 public static Calendar getCalendar(String time)  
 {  
     if (null == time)  
     {  
         return null;  
     }  
     String[] times = time.split("-");  
     Calendar calendar = Calendar.getInstance();  
     if(times.length>2){
         calendar.set(Integer.valueOf(times[0]), Integer.valueOf(times[1])-1, Integer.valueOf(times[2]));  
     }else{
         calendar.set(Integer.valueOf(times[0]), Integer.valueOf(times[1])-1, 10);  
     }
     return calendar;  
 } 
  
  public static int getLastDay(int time,String day)  
  {  
      if (0 == time)  
      {  
          return 0;  
      }  
      String month=addMonth(convertToDate(time), 1);
      String[] times = month.split("-");  
      return convertToInt(times[0]+"-"+times[1]+"-"+day);  
  } 
  
  /**
   * 日期的月份计算
   * @param time1
   * @param months
   * @return time2
   * @throws  
   */
  public static String addMonth(String time1,int months){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
      Calendar c1 = getCalendar(time1);  
      c1.add(Calendar.MONTH, months);
      return sdf.format(c1.getTime());
  }
  
  
  
  public static double compareToday(String time) throws ParseException{
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YMD);
      Date date = sdf.parse(time);
      return getDateDiff(1,date,getNowDate());
  }
  
  /**
   * 两个都强制时直接用于展现
   * @param insuranceStart   公积金起缴月
   * @param fundStart   社保起缴月
   * @param insuranceEndMonth   已保社保结束月
   * @param housingFundEndMonth   已保公积金结束月
   * @param insuranceFlag   社保强制
   * @param fundFlag   公积金强制
   * @param months  套餐月数
   * @return HashMap
   * @throws  
   */
  
  //两个都强制时直接用于展现
  public static HashMap<String, Integer> calcMonth(String insuranceStart,String fundStart,
          int insuranceEndMonth,int housingFundEndMonth,int insuranceFlag,int fundFlag,int months) throws ParseException{
      HashMap<String,Integer> result =new HashMap<String, Integer>();
      HashMap<String,String> serviceMonth =new HashMap<String, String>();
      String insuranceEnd=addMonth(insuranceStart, months);
      String fundEnd=addMonth(fundStart, months);
      String insuranceingEnd="";//在保的社保截止月
      String housingFundingEnd="";//在保的公积金截止月
      
      if(insuranceEndMonth>0){//有在保业务时，续保起始月=在保截止月的下月
          insuranceingEnd=convertToDate(insuranceEndMonth, "yyyy-MM-dd");
      }else{
          insuranceingEnd=addMonth(insuranceStart, -1);
      }
      if(housingFundEndMonth>0){//有在保业务时，续保起始月=在保截止月的下月
          housingFundingEnd=convertToDate(housingFundEndMonth, "yyyy-MM-dd");
      }else{
          housingFundingEnd=addMonth(fundStart, -1);
      }
//      System.out.println(insuranceingEnd+" insuranceingEnd 8 housingFundingEnd "+housingFundingEnd);
      int insuranceFlag2=0;//社保续保标志 3不可续  ；0可续
      int fundFlag2=0;//公积金续保标志 3不可续；0可续
      if(!insuranceingEnd.equals("")&&monthCompare(insuranceEnd,insuranceingEnd)>=0){
          insuranceFlag2=3;
//          System.out.println("insuranceFlag2="+insuranceFlag2);
      }
      if(!housingFundingEnd.equals("")&&monthCompare(fundEnd,housingFundingEnd)>=0){
          fundFlag2=3;
//          System.out.println("fundFlag2="+fundFlag2);
      }//是否可续费
//      System.out.println(insuranceEnd+" insuranceEnd fundEnd "+fundEnd);
      int m=0;//续保允许选择的最小套餐月数
      if(insuranceFlag2!=3||fundFlag2!=3){
          if(fundFlag==1&&insuranceFlag==1){
              if(insuranceEndMonth>0||housingFundEndMonth>0){
                  serviceMonth=serviceMonthCalc(insuranceingEnd,housingFundingEnd,months);
                  m=Integer.parseInt(serviceMonth.get("months"));
              }
//              System.out.println("mm11211m=="+m);
          }else if(fundFlag==1){
              if(insuranceEndMonth>0&&insuranceEndMonth>housingFundEndMonth){
                  serviceMonth=serviceMonthCalc(insuranceingEnd,housingFundingEnd,months);
                  m=Integer.parseInt(serviceMonth.get("months"));
              }
//              System.out.println("mm1311m=="+m);
          }
          else if(insuranceFlag==1){
              if(housingFundEndMonth>0&&insuranceEndMonth<housingFundEndMonth){
                  serviceMonth=serviceMonthCalc(insuranceingEnd,housingFundingEnd,months);
                  m=Integer.parseInt(serviceMonth.get("months"));
//                  System.out.println("mm11411m=="+m);
              }
          }
      }
      result.put("months", m);//续保允许选择的最小套餐月数
      return result;
  } 
  /**
   * 前台交互计算月份时 得到截止月和允许选择的最小套餐月  
   * @param time1 月份1
   * @param time2 月份2
   * @param months  套餐月数
   * @return HashMap
   * @throws  
   * //月份2大于月份1时，返回值大于0，小于则值小于0
   */
  public static HashMap<String, String> serviceMonthCalc(String time1,String time2,int months){
      HashMap<String,String> result =new HashMap<String, String>();
      int m=0;
      int i=Math.abs(monthCompare(time1,time2));
	  if (i<=1){ // 支持单月套餐
		  m=1;
	  }else if(i<=3){
          m=3;
      }else if(i>3&&i<=6){
          m=6;
      }else{
          m=12;
      }
      if(m>months){
          months=m;
      }
      result.put("months", String.valueOf(months));
      if(monthCompare(time1,time2)>0){//在保社保截止月 和在保公积金截止月比较取小者
          result.put("timeEnd", addMonth(time1, months));
      }else{
          result.put("timeEnd", addMonth(time2, months));
      }
      return result;
  }
  
  /**
   * 三个树比大小返回最大值
   * @param d1
   * @param d2 
   * @param d3 
   * @return double 
   * @throws  
   * 
   */
  public static double double3Compare(double d1,double d2,double d3){
      double temp = d1>d2?d1:d2 ;
      temp=temp>d3?temp:d3;
      return temp;
  }
  
  /**
   * 两个树比大小返回最大值
   * @param d1
   * @param d2 
   * @return double 
   * @throws  
   * 
   */
  public static double double2Compare(double d1,double d2){
      return  d1>d2?d1:d2;
  }
  
  /**
   * 前台交互计算月份  
   * @param insuranceStart   公积金起缴月
   * @param fundStart   社保起缴月
   * @param insuranceingEnd   已保社保结束月
   * @param fundingEnd   已保公积金结束月
   * @param insuranceFlag   社保强制
   * @param fundFlag   公积金强制
   * @param fundSelect   勾选公积金
   * @param insuranceSelect   勾选社保
   * @param months  套餐月数
   * @return HashMap
   * @throws  
   */
  public static HashMap<String, Integer> calcMonths(int insuranceStart,int fundStart,int insuranceingEnd,int fundingEnd,
          int insuranceFlag,int fundFlag,int insuranceSelect,int fundSelect,int months) throws ParseException{
      HashMap<String,Integer> result =new HashMap<String, Integer>();
      HashMap<String,String> serviceMonth =new HashMap<String, String>();
      String insuranceStartStr=convertToDate(insuranceStart);//社保起缴月
      String fundStartStr=convertToDate(fundStart);//公积金起缴月
      String insuranceingEndStr=convertToDate(insuranceingEnd);//在保的社保结束月
      String fundingEndStr=convertToDate(fundingEnd);//在保公积金结束月
      String insuranceStartResult="1970-01-01";//计算结果的社保起缴月，附初始值
      String fundStartResult="1970-01-01";//计算结果的公积金起缴月，附初始值
      String insuranceEndResult="1970-01-01";//计算结果的社保截止月，附初始值
      String fundEndResult="1970-01-01";//计算结果的公积金截止月，附初始值
      String tempStart="";
      //截止月规则：
      //两个都强制，取最小的截止月+套餐（套餐月数>=大的截止月-小的截止月）；
      //一个强制缴纳是：续在可选，强制跟随；续在强制，可选不跟；
      //两个都不强制：各自加选择的套餐数
      if(monthCompare(insuranceStartStr, fundStartStr)>0){//比较传入的社保和公积金起缴月，取大者
          tempStart=fundStartStr;
      }else{
          tempStart=insuranceStartStr;
      }
      if(insuranceFlag==1&&fundFlag==1){//两个都强制时
          if(insuranceingEnd==0&&fundingEnd==0){//两个都未在保，直接取选择的大的月，加上套餐
              insuranceStartResult=tempStart;
              fundStartResult=tempStart;
              insuranceEndResult=addMonth(tempStart, months-1);
              fundEndResult=addMonth(tempStart, months-1);
          }else if(insuranceingEnd==0){//社保未在保
              insuranceStartResult=tempStart;
              fundStartResult=addMonth(fundingEndStr, 1);//公积金起缴月为在保截止月的下一个月
              serviceMonth=serviceMonthCalc(insuranceStartStr,fundingEndStr,months);//计算最小允许套餐和两个项目的公共截止月
              months=Integer.valueOf(serviceMonth.get("months"));//最小允许选择的套餐月数
              insuranceEndResult=serviceMonth.get("timeEnd");
              fundEndResult=serviceMonth.get("timeEnd");
          }else if(fundingEnd==0){//公积金未在保的情况，计算方法同上
              fundStartResult=tempStart;
              insuranceStartResult=addMonth(insuranceingEndStr, 1);
              serviceMonth=serviceMonthCalc(fundStartStr,insuranceingEndStr,months);
              months=Integer.valueOf(serviceMonth.get("months"));
              insuranceEndResult=serviceMonth.get("timeEnd");
              fundEndResult=serviceMonth.get("timeEnd");
          }else{//两个都已在保情况
              insuranceStartResult=addMonth(insuranceingEndStr, 1);//开始月取各自在保结束月的下个月
              fundStartResult=addMonth(fundingEndStr, 1);
              serviceMonth=serviceMonthCalc(insuranceingEndStr,fundingEndStr,months);
              months=Integer.valueOf(serviceMonth.get("months"));
              insuranceEndResult=serviceMonth.get("timeEnd");
              fundEndResult=serviceMonth.get("timeEnd");
          }
      }
      if(insuranceFlag==1&&fundFlag==0){//社保强制，公积金不强制
          if(insuranceingEnd>0){
              insuranceStartResult=addMonth(insuranceingEndStr, 1);
          }else{
              insuranceStartResult=insuranceStartStr;
          }
          String insuranceEndTemp="1970-01-01";
          if(insuranceSelect==1){//勾选强制项
              if(insuranceingEnd>0){//有在保时，续保起始月为在保截止月的下个月
                  insuranceStartResult=addMonth(insuranceingEndStr, 1);
              }else{//未在保时 起始月为客户选择的起始月
                  insuranceStartResult=insuranceStartStr;//ttttt
              }
              if(fundingEnd>0&&monthCompare(insuranceStartResult,fundingEndStr)>0){//非强制项有在保。且结束月大于强制项的起缴月。强制项的结束月=非强制项的结束月，否则加上套餐月数
                  insuranceEndTemp=fundingEndStr;
              }else{
                  insuranceEndTemp=addMonth(insuranceStartResult, months-1);
              }
              if(insuranceingEnd>0){//强制项有在保时，计算临时结束月和在保的结束月之间取最小的可选套餐月数
                  serviceMonth=serviceMonthCalc(insuranceingEndStr,insuranceEndTemp,months);
                  months=Integer.valueOf(serviceMonth.get("months"));
                  insuranceEndResult=serviceMonth.get("timeEnd");
              }else{//强制项不在保时,截止月取临时截至月
//                  serviceMonth=serviceMonth(insuranceingEndStr,insuranceEndTemp,months);
//                  months=Integer.valueOf(serviceMonth.get("months"));
//                  insuranceEndResult=serviceMonth.get("timeEnd");
//                  insuranceEndResult=addMonth(insuranceStartResult, months-1);
                  insuranceEndResult=insuranceEndTemp;
              }
//              insuranceEndTemp=insuranceEndResult;
          }
          if(fundSelect==1){//续费非强制项时
              if(fundingEnd>0){//有在保,取在保结束月的下个月为起缴月
                  fundStartResult=addMonth(fundingEndStr, 1);
                  fundEndResult=addMonth(fundStartResult, months-1);
              }else{//无在保取选择的起缴月
                  fundStartResult=fundStartStr;
                  fundEndResult=addMonth(fundStartResult, months-1);
              }
              if(monthCompare(insuranceEndResult, fundEndResult)>0){//比较强制项的结束月与非强制项的结束月,强制项跟齐非强制项
                  insuranceEndResult=fundEndResult;
              }
          }
      } 
      if(fundFlag==1&&insuranceFlag==0){//公积金强制，社保不强制 (逻辑同社保强制，公积金不强制)
          if(fundingEnd>0){
              fundStartResult=addMonth(fundingEndStr, 1);
          }else{
              fundStartResult=fundStartStr;
          }
          String fundEndTemp="1970-01-01";
          if(fundSelect==1){//勾选强制项
              if(fundingEnd>0){
                  fundStartResult=addMonth(fundingEndStr, 1);
              }else{
                  fundStartResult=fundStartStr;//ttttt
              }
              if(insuranceingEnd>0&&monthCompare(fundStartResult,insuranceingEndStr)>0){
                  fundEndTemp=insuranceingEndStr;
              }else{
                  fundEndTemp=addMonth(fundStartResult, months-1);
              }
              if(fundingEnd>0){
                  serviceMonth=serviceMonthCalc(fundingEndStr,fundEndTemp,months);
                  months=Integer.valueOf(serviceMonth.get("months"));
                  fundEndResult=serviceMonth.get("timeEnd");
              }else{
//                  fundEndResult=addMonth(fundStartResult, months-1);
                  fundEndResult=fundEndTemp;
              }
          }
          if(insuranceSelect==1){
              if(insuranceingEnd>0){
                  insuranceStartResult=addMonth(insuranceingEndStr, 1);
                  insuranceEndResult=addMonth(insuranceStartResult, months-1);
              }else{
                  insuranceStartResult=insuranceStartStr;
                  insuranceEndResult=addMonth(insuranceStartResult, months-1);
              }
              if(monthCompare(fundEndResult, insuranceEndResult)>0){
                  fundEndResult=insuranceEndResult;
              }
          }
      } 
      if(insuranceFlag==0&&fundFlag==0){//两项都不强制时，判断有无在保，有取结束月的下一个月为续费起缴月，没有取用户选择的起缴月
          if(insuranceSelect==1){
              if(insuranceingEnd>0){
                  insuranceStartResult=addMonth(insuranceingEndStr, 1);
                  insuranceEndResult=addMonth(insuranceStartResult, months-1);
              }else{
                  insuranceStartResult=insuranceStartStr;
                  insuranceEndResult=addMonth(insuranceStartStr, months-1);
              }
          }
          if(fundSelect==1){
              if(fundingEnd>0){
                  fundStartResult=addMonth(fundingEndStr, 1);
                  fundEndResult=addMonth(fundStartResult, months-1);
              }else{
                  fundStartResult=fundStartStr;
                  fundEndResult=addMonth(fundStartStr, months-1);
              }
          }
      }
      if(monthCompare(insuranceStartResult, insuranceEndResult)<0){//比较计算得到的起止月，当起始月大于截止月时，取0值
          result.put("insuranceStart", 0);
          result.put("insuranceEnd", 0);
      }else{
          result.put("insuranceStart", convertToInt0(insuranceStartResult));
          result.put("insuranceEnd", convertToInt0(insuranceEndResult));
      }
      if(monthCompare(fundStartResult, fundEndResult)<0){
          result.put("fundStart", 0);
          result.put("fundEnd", 0);
      }else{
          result.put("fundStart", convertToInt0(fundStartResult));
          result.put("fundEnd", convertToInt0(fundEndResult));
      }
      result.put("months", months);//续保允许选择的最小套餐月数
//      System.out.println("m续保允许选择的最小套餐月数m "+months+convertToInt0(fundStartResult));
//      System.out.println("ss "+ convertToDate(result.get("insuranceStart"))+"  se "+convertToDate(result.get("insuranceEnd")));
//      System.out.println("fs "+ convertToDate(result.get("fundStart"))+"  fe "+convertToDate(result.get("fundEnd")));
      return result;
  }

	/**
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException int
	 * @Title: daysBetween
	 * @Description: 两个日期相差天数
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @return int
	 * @Title: differDays
	 * @Description: 两时期时间差天数
	 */
	public static int differDays(int startTime, int endTime) {
		String smdate = DateUtil.convertToDate(startTime);
		String bdate = DateUtil.convertToDate(endTime);
		try {
			return DateUtil.daysBetween(smdate, bdate);
		} catch (ParseException e) {
			LOG.error("两时期时间差天数出错" + e.getMessage());
		}
		return -1;
	}

	/**
	 * 计算相差月数
	 *
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static int differMonth(Long beginTime, Long endTime) {

		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		begin.setTimeInMillis(beginTime);
		end.setTimeInMillis(endTime);

		int differ = (end.get(Calendar.YEAR) - begin.get(Calendar.YEAR)) * 12 +
				(end.get(Calendar.MONTH) - begin.get(Calendar.MONTH));

		return differ;
	}
      public static void main(String[] args) throws ParseException{
            String insuranceStart="2016-04";
            String fundStart="2016-03";
            int insuranceFlag=1;
            int fundFlag=0;
            int insuranceSelect=1;
            int fundSelect=0;
            int months=3;
            String ggg=new java.text.DecimalFormat("#0.00").format(0.10);
//            System.out.println(ggg);
//
//            System.out.println(convertToDate(1455811200));
//            System.out.println(convertToDate(1449244800));
//            System.out.println(convertToDate(1449331200));
            //    System.out.println(compareToday("2016-02-25"));
            //    System.out.println(compareToday("2016-02-25")>0);
//                String insuranceingStart="2016-02-01";//在保的社保起始月
            String insuranceingEnd="2016-04-01";//在保的社保截止月
            String insuranceingEnd2="2016-04-22";//在保的社保截止月
                String housingFundingStart="2016-03";//在保的公积金起始月
            String fundingEnd="2016-05";//在保的公积金截止月
            String fundingEnd2="2016-05-22";//在保的公积金截止月


//            System.out.println(double3Compare(888.0, 5.5, 0.0));
//            //    System.out.println(insuranceingStart+"="+convertToInt(insuranceingStart));
//            System.out.println("s="+convertToInt(insuranceingStart));
//            System.out.println("e="+convertToInt(insuranceingEnd));
//            System.out.println("ddd="+convertToInt(insuranceingEnd2));
//            String a="0.00";
//            System.out.println(Double.parseDouble(a));
//            System.out.println("s="+convertToInt(housingFundingStart));
//            System.out.println("e="+convertToInt(fundingEnd));
//            //    System.out.println(housingFundingStart+"="+convertToInt(housingFundingStart));
//            System.out.println("ddd="+convertToInt(fundingEnd2));
            //    calcMonth("2016-03", "2016-03",convertToInt(insuranceingEnd),convertToInt(housingFundingEnd), 1, 1,12);
//            calcMonth("2016-03", "2016-03",0,0, 1, 1,3);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), 0, 0, insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), 0,  convertToInt(fundingEnd), insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), convertToInt(insuranceingEnd), 0, insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), convertToInt(insuranceingEnd), convertToInt(fundingEnd), insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), convertToInt(insuranceingEnd2), convertToInt(fundingEnd), insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
            calcMonths(convertToInt(insuranceStart), convertToInt(fundStart), convertToInt(insuranceingEnd), convertToInt(fundingEnd2), insuranceFlag, fundFlag, insuranceSelect, fundSelect, months);
    }
}