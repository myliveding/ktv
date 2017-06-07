package com.st.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class PropertiesUtils {
    /**
     * 获取属性文件的数据 根据key获取值
     *
     * @param key
     * @return
     */
    public static String findPropertiesKey(String key,String filename) {

        try {
            Properties prop = getProperties(filename);
            return prop.getProperty(key);
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 返回　Properties
     *
     * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
     * @param
     * @return
     */
    public static Properties getProperties(String filename) {
        Properties prop = new Properties();
        String savePath = PropertiesUtils.class.getResource("/" + filename).getPath();
        //以下方法读取属性文件会缓存问题
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(savePath));
            prop.load(in);
        } catch (Exception e) {
            return null;
        }
        return prop;
    }

    /**
     * 写入properties信息
     *
     * @param key   名称
     * @param value 值
     */
    public static void modifyProperties(String key, String value, String filename) {
        try {
            // 从输入流中读取属性列表（键和元素对）
            Properties prop = getProperties(filename);
            prop.setProperty(key, value);
            String path = PropertiesUtils.class.getResource("/" + filename).getPath();
            FileOutputStream outputFile = new FileOutputStream(path);
            prop.store(outputFile, "modify");
            outputFile.close();
            outputFile.flush();
        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream in = PropertiesUtils.class
                .getResourceAsStream("/" + Constant.SEO_FILE_NAME);
        try {
            prop.load(in);
            Iterator<Entry<Object, Object>> itr = prop.entrySet().iterator();
            while (itr.hasNext()) {
                Entry<Object, Object> e = (Entry<Object, Object>) itr.next();
//                System.err.println((e.getKey().toString() + "" + e.getValue()
//                        .toString()));
            }
        } catch (Exception e) {

        }
    }
}
