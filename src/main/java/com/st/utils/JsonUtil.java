package com.st.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

import com.st.constant.Constant;

public class JsonUtil {
    private static Logger logger =LoggerFactory.getLogger(JoYoUtil.class);
    public static String getJsonFile(String fileName){
//        if(Constant.URL.indexOf("shebaoonline")>0||Constant.ENVIROMENT.equals("formal")){
        if(Constant.ENVIROMENT.equals("formal")){
            String jsonContext = loadJson("http:"+Constant.staticUrl+"/md5-map.json");
            JSONObject jsonObject=JSONObject.fromObject(jsonContext);
            if(jsonObject.has(fileName)){
                return Constant.staticUrl+"/"+(String) jsonObject.get(fileName);
            }else{
                return  Constant.staticUrl+"/"+fileName;
            }
        }else{
            return Constant.staticUrl+"/"+fileName;
        }
    }
    public static String loadJson(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            logger.error("读取静态MD5出现异常！"+e.getMessage(),e);
        } catch (IOException e) {
            logger.error("读取静态MD5出现异常！"+e.getMessage(),e);
        }
        return json.toString();
    }
}


