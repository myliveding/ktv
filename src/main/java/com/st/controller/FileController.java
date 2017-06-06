package com.st.controller;

import com.st.constant.Constant;
import com.st.core.util.text.StringUtils;
import com.st.utils.ContextHolderUtils;
import com.st.utils.WeixinUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {
	private static Logger logger =LoggerFactory.getLogger(FileController.class);
    @RequestMapping("/upload")  
    public @ResponseBody Object upload( MultipartFile file, HttpServletRequest request) throws IOException{  
        String type = request.getParameter("uploaderType")==null?"":request.getParameter("uploaderType");//上传类型   1营业执照  ； 2身份证正面；3身份证反面；4 吐槽
        HttpSession session = ContextHolderUtils.getSession();
        JSONObject jsonObj = new JSONObject();
        Object userIdObject =  session.getAttribute("userId");//4125
//        if(userIdObject==null||"".equals(userIdObject)){
//            jsonObj = JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
//            return jsonObj;
//        }
        String path="";
        if("1".equals(type)){
            logger.info("用户ID："+userIdObject.toString()+" 通过web端上传营业执照");
            path=Constant.WYB_BUSINESS;
        }else if("2".equals(type)){
            logger.info("用户ID："+userIdObject.toString()+" 通过web端上传身份证正面");
            path=Constant.WYB_POSITIVE;
        }else if("3".equals(type)){
            logger.info("用户ID："+userIdObject.toString()+" 通过web端上传身份证反面");
            path=Constant.WYB_NEGATIVE;
        }else if("4".equals(type)){
            logger.info("用户ID："+userIdObject.toString()+" 通过web端上传吐槽图片");
            path=Constant.WYB_ADVICE;
        }else{
            logger.info("用户ID："+userIdObject.toString()+" 通过web端上传营业执照");
            path=Constant.WYB_BUSINESS;
        }
        long l1=System.currentTimeMillis();
        String result= "";
//        WeixinUtil.uploadMediaFromWeb(file,path);
        long l2=System.currentTimeMillis();
        logger.info("web上传路:"+result+" 图像大小:"+file.getSize()+"耗时（ms）:"+(l2-l1));
        String fileURL= "";
//        WeixinUtil.getTempURLFromOSS(result, path);
        logger.info("web上传后获取临时显示URL:"+fileURL);
        Map<String,String> filemap=new HashMap<String, String>();
        filemap.put("filename", result);
        filemap.put("fileURL", fileURL);
        if(StringUtils.isNotEmpty(fileURL)){
            jsonObj.put("status", 0);
            jsonObj.put("data",filemap);
            return jsonObj;
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", "图片上传出错，请重试！");
            return jsonObj;
        }
    }  
}
