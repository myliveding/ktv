/*
 * @(#)java 2014-3-21下午01:49:57
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.utils.util.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类,提供常用字符串处理方法
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2014-3-21下午01:49:57]gaozhanglei<br/>
 * </p>
 * </li>
 * </ul>
 */

public class StringUtils {
	/**
	 * 验证字符串时候为空
	 * @author sunju
	 * @creationDate. 2010-12-3 下午04:47:52 
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if(null == str){
			return true;
		}else{
			return str.trim().equals("") ? true : false;
		}
	}
	
	/**
	 * 验证字符串非空
	 * @author sunju
	 * @creationDate. 2010-12-3 下午04:48:16 
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		if(null == str){
			return false;
		}else if(str.trim().equals("null")){
		    return false;
		}else{
			return str.trim().equals("") ? false : true;
		}
	}
	
	/**
	 * 验证字符串非 null
	 * @author sunju
	 * @creationDate. 2010-12-3 下午04:48:16 
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isNotNull(String str) {
		if(null == str){
			return false;
		}else{
			return null == str.trim() ? false : true;
		}
	}
	
	/**
	 * 获得带中文的字符串长度
	 * @author sunju
	 * @creationDate. 2010-11-2 上午11:36:30 
	 * @param str 字符串
	 * @return 字符串长度
	 */
	public static long getChineseTextLen(String str) {
		if (isEmpty(str)) return 0;
		return str.replaceAll("[^\\x00-\\xff]", "00").length();
	}
	
	/**
	 * 截取带中文的文本长度
	 * @author sunju
	 * @creationDate. 2010-11-2 上午11:37:35 
	 * @param str 字符串
	 * @param len 长度
	 * @param ext 截断后添加的标识(一般传省略号)
	 * @return 字符串
	 */
	public static String subChineseText(String str, int len, String ext) {
		if (isEmpty(str)) return "";
		if (getChineseTextLen(str) <= len) return str;
		int m = (int) Math.floor(len / 2D);
		int length = str.length();
		long subLen = 0;
		for(int i = m; i<length; i++) { 
			subLen = getChineseTextLen(str.substring(0, i));
			if (subLen >= len) {
				StringBuilder result = new StringBuilder(str.substring(0, (subLen>len) ? i - 1 : i));
				if (isNotEmpty(ext)) {
					result.append(ext);
				}
				return result.toString();
			}
		}
		return str; 
	}
	
	/**
	 * 文本转成全角字符串
	 * @author sunju
	 * @creationDate. 2010-11-2 下午05:29:16 
	 * @param str 待转换的字符串
	 * @return 全角字符串
	 */
	public static String text2sbcCase(String str) {
		if (isEmpty(str)) return "";
		char[] c = str.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}
	
	/**
	 * 文本转成半角字符串
	 * @author sunju
	 * @creationDate. 2010-11-2 下午05:28:31 
	 * @param str 待转换的字符串
	 * @return 半角字符串
	 */
	public static String text2dbcCase(String str) {
		if (isEmpty(str)) return "";
		char[] c = str.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
    
	/**
	 * 判断字符串是否为数字---使用转换的方法
	 * @author dingzr
	 * @param str 待转换的字符串
	 * @return boolean
	 */
	public static boolean isNum(String str) {
		try {   
			if(str.endsWith(".")){
				return false;
			}else{
				Integer.valueOf(str.replaceAll("\\.", ""));//把字符串强制转换为数字   
	            return true;//如果是数字，返回True   
			}
        } catch (Exception e) {  
            return false;//如果抛出异常，返回False   
        }  
	}
	
	/**
	 * 判断字符串是否为数字---使用正则的方法
	 * @author dingzr
	 * @param str 待转换的字符串
	 * @return boolean
	 */
	public static boolean isNum1(String str) {
		if(str.endsWith(".")){
			return false;
		}
		return str.replaceAll("\\.", "").matches("[0-9]+");
	}
	
	
   /**
    * 把汉字、字母、特殊字符转换成unicode
    * @param src
    * @return
    */
   public static String escape(String src) {  
       int i;  
       char j;  
       StringBuffer tmp = new StringBuffer();  
       tmp.ensureCapacity(src.length() * 6); 
       for (i = 0; i < src.length(); i++) {
           j = src.charAt(i);  
           if (Character.isDigit(j) || Character.isLowerCase(j)  
                   || Character.isUpperCase(j)) {//大小写字母或者数字
        	   if(Character.isLowerCase(j)){
        		   tmp.append(Character.toUpperCase(j));
        	   }else{
        		   tmp.append(j);  
        	   }
           }else if (j < 256) {//特殊字符
               tmp.append("%");  
               if (j < 16)  
                   tmp.append("0");  
               tmp.append(Integer.toString(j, 16).toUpperCase()); 
           }else{//汉字等
               tmp.append("%u");  
           }
       }  
       return tmp.toString();  
   }  
	
   /**
    * 使用正则表达式匹配出值
    * @return
    */
   public static String getValue(String str,String regular){
   	String res = "";
   	Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(str);

		while(matcher.find()) {
			res = matcher.group();
		} 
		return res;
   }   
   
}
