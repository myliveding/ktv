package com.st.utils.web;

import org.apache.commons.lang.StringUtils;



/**
 * Html工具类
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2014-2-20下午07:19:29]gaozhanglei<br/>
 * </p>
 * </li>
 * </ul>
 */

public class HtmlUtil {
	/**
	 * 替换机密文本(替换从第几位开始多少个字符)
	 * @author sunju
	 * @creationDate. 2012-7-13 上午09:57:31 
	 * @param str 字符串
	 * @param fromIndex 开始位数(从0开始，表示从传入下标开始截取，比如传2表示，下标为2开始截取，即从第三位开始替换)
	 * @param num 替换多少位
	 * @param newStr 替换字符串
	 * @return 机密文本
	 */
	public static String replaceSecretText(String str, int fromIndex, int num, String newStr) {
		if (StringUtils.isEmpty(str)) return str;
		int len = str.length();
		if (fromIndex + num < 0 || fromIndex + num > len) return str;
		StringBuilder resultStr=new StringBuilder("");
		resultStr.append(str.substring(0, fromIndex));
		for (int i = 0; i < num; i++) {
			resultStr.append(newStr);
		}
		resultStr.append(str.substring((fromIndex + num), len));
		str = String.valueOf(resultStr);
		return str;
	}
    /**
     * 测试
     * @author sunju
     * @creationDate. 2011-6-21 下午07:56:58 
     * @param args
     */
    public static void main(String[] args) {
    	String phoneNo = "13567890123";
//    	System.out.println(replaceSecretText(phoneNo, 3, 4, "*"));
	}
}
