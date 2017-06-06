package com.st.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 对密码进行md5加密
 * @author Administrator
 *
 */
public class Md5Tool {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static Logger logger = LoggerFactory.getLogger(Md5Tool.class);
    public static String getMd5(String password) {
        String str = "";
        if (password != null && !password.equals("")) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                //加密后的字符串
                final byte[] digest = md.digest(password.getBytes("utf-8"));
                return getFormattedText(digest);
            } catch (Exception e) {
                logger.error("MD5加密出现异常！"+e.getMessage(),e);
            }
        }
        return str;
    }

    private static String getFormattedText(final byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
//        System.err.println(getMd5("HWgpt9IDQgTqGyoTmHuse"));
//        System.err.println(System.currentTimeMillis());
    }
}
