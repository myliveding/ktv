package com.st.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dingzr
 * @Description
 * @ClassName DataUtil
 * @since 2017/6/7 13:28
 */
public class DataUtil {
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

    /**
     * 验证是否是数字
     */
    public static final String NUMBER_PATTERN = "^\\d+$";

    /**
     * 身份证号码正则表达式
     */
    public static final String IDENTITYCARD_PATTERN = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

    /**
     * 移动手机号码正则表达式
     */
    public static final String MOBILENUM_PATTERN = "^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";

    /**
     * 运营 牛+手机号
     */
    public static final String YY = "^[\u725b][+]?13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
    /**
     * 运营 牛+手机号
     */
    public static final String YYCN = "^[\u725b][\\w]*";
    /**
     * 座机号码正则表达式
     */
    public static final String TEL_PATTERN = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";

    /**
     * Email正则表达式
     */
    public static final String EMAIL_PATTERN = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 密码正则表达式
     */
    public static final String MM_PATTERN="/[A-Za-z0-9]{6,20}$/";
    /**
     * 正则表达式
     * 验证数字是否连着升序，例如123456
     */
    public static final String ascPattern = "^(0(?=1|$)|1(?=2|$)|2(?=3|$)|3(?=4|$)|4(?=5|$)|5(?=6|$)|6(?=7|$)|7(?=8|$)|8(?=9|$)|9(?=0|$)|0(?=$))*$";
    /**
     * 正则表达式
     * 验证数字是否连着降序，例如654321
     */
    public static final String descPattern = "^(9(?=8|$)|8(?=7|$)|7(?=6|$)|6(?=5|$)|5(?=4|$)|4(?=3|$)|3(?=2|$)|2(?=1|$)|1(?=0|$)|0(?=$))*$";

    /**
     * 正则表达式
     * 验证数字是否连续相同数字，例如000000
     */
    public static final String samePattern = "^(9(?=9|$)|8(?=8|$)|7(?=7|$)|6(?=6|$)|5(?=5|$)|4(?=4|$)|3(?=3|$)|2(?=2|$)|1(?=1|$)|0(?=0|$))*$";

    /**
     * 成员姓名正则表达式
     */
    public static final String MEMBER_NAME = "^([A-Za-z]|[\\u4E00-\\u9FA5]){2,10}+$";

    /**
     * 只允许中文，数值，字母正则表达式
     */
    public static final String ZSZ = "^[A-Za-z0-9\\s\\u4E00-\\u9FA5]+$";
    /**
     * 因为数字下划线正则表达式
     */
    public static final String WORD_PATTERN = "^\\w+$";

    /**
     * 验证密码是否是简单密码
     * @param password
     * @return
     */
    public static boolean isLowPassword(String password){
        boolean isPassword = false;
        if(Pattern.matches(ascPattern, password) || Pattern.matches(descPattern, password) || Pattern.matches(samePattern, password)){
            isPassword = true;
        }
        return isPassword;
    }
    /**
     * 判断身份证号码格式是否正确：正确返回true，否则返回false
     *
     * @param identityNo
     * @return
     */
    public static boolean isIdentityNo(String identityNo){
        boolean isNo = false;
        if(Pattern.matches(IDENTITYCARD_PATTERN, identityNo)){
            isNo = true;
        }
        return isNo;
    }

    /**
     * 验证Email格式是否正确
     *
     * @param inputEmail
     * @return
     */
    public static boolean isEmail(String inputEmail){
        boolean isChecked = false;
        if(Pattern.matches(EMAIL_PATTERN, inputEmail)){
            isChecked = true;
        }
        return isChecked;
    }
    /**
     * 验证是否是数字
     * @param inputStr
     * @return
     */
    public static boolean isNumber(String inputStr){
        boolean isChecked = false;
        if(Pattern.matches(NUMBER_PATTERN, inputStr)){
            isChecked = true;
        }
        return isChecked;
    }
    /**
     * 验证是否是移动的手机号码
     * @param inputNo
     * @return
     */
    public static boolean isMobileNo(String inputNo){
        boolean isChecked = false;
        if(Pattern.matches(MOBILENUM_PATTERN, inputNo)){
            isChecked = true;
        }
        return isChecked;
    }

    /**
     * 验证是否是座机号码
     * @param inputNo
     * @return
     */
    public static boolean isTelNo(String inputNo){
        boolean isChecked = false;
        if(Pattern.matches(TEL_PATTERN, inputNo)){
            isChecked = true;
        }
        return isChecked;
    }

    /**
     * 判断身份证号码格式是否运营 牛+手机号：正确返回true，否则返回false
     *
     * @param
     * @return
     */
    public static boolean isYY(String str){
        if(str==null){
            return false;
        }
        Pattern p = Pattern.compile(YY);
        Matcher m = p.matcher(str);
        return m.find();
    }
    /**
     * 判断身份证号码格式是否运营 牛+手机号：正确返回true，否则返回false
     *
     * @param
     * @return
     */
    public static boolean isYYCN(String str){
        if(str==null){
            return false;
        }
        Pattern p = Pattern.compile(YYCN);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 验证是否是汉字跟字母组成且不超过10个字符的姓名
     * @param str
     * @return
     */
    public static boolean isMemberName(String str){
        if(str == null){
            return false;
        }
        Pattern p = Pattern.compile(MEMBER_NAME);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public static boolean isWord(String str){
        if(str == null){
            return false;
        }
        Pattern p = Pattern.compile(WORD_PATTERN);
        Matcher m = p.matcher(str);
        return m.find();
    }


}
