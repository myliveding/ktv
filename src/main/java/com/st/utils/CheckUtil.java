package com.st.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 验证类 
 * @author shenwf
 * @date 2015-09-08 
 */
public class CheckUtil {
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

    public static void main(String[] args) {
        String a="12-34aW";
        String b="12_34aWsadsadsadadsadads";
        System.out.println(CheckUtil.isWord(a));
        System.out.println(CheckUtil.isWord(b));
    }
}
