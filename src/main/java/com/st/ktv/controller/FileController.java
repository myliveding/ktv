package com.st.ktv.controller;

import com.st.core.ContextHolderUtils;
import com.st.utils.DataUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        String path="";
        long l1=System.currentTimeMillis();
        String result= "";
        long l2=System.currentTimeMillis();
        logger.info("web上传路:"+result+" 图像大小:"+file.getSize()+"耗时（ms）:"+(l2-l1));
        String fileURL= "";
        logger.info("web上传后获取临时显示URL:"+fileURL);
        Map<String,String> filemap=new HashMap<String, String>();
        filemap.put("filename", result);
        filemap.put("fileURL", fileURL);
        if(DataUtil.isNotEmpty(fileURL)){
            jsonObj.put("status", 0);
            jsonObj.put("data",filemap);
            return jsonObj;
        }else{
            jsonObj.put("status", 1);
            jsonObj.put("msg", "图片上传出错，请重试！");
            return jsonObj;
        }
    }

    @RequestMapping("/upload.do")
    // 访问此方法的url路径/test/hello.do
    public String upload() {
        return "fileUpload";
    }
    /**
     * 文件上传方式一
     * @param request
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = "/fileUpload.do")
//    public String fileUpload(HttpServletRequest request) throws Exception {
//        // 第一步转化request
//        MultipartHttpServletRequest rm = (MultipartHttpServletRequest) request;
//        // 获得原始文件
//        CommonsMultipartFile cfile = (CommonsMultipartFile) rm.getFile("pic");
//        // 获得原始文件名
//        String origFileName = cfile.getOriginalFilename();
//        // 获得原始文件的后缀 XXX.jpg
//        String suffix = origFileName.contains(".") ? origFileName.substring(origFileName.lastIndexOf(".")) : "error";
//        if ("error".equalsIgnoreCase(suffix)) {
//            return "error";
//        }
//        // 获得原始文件的字节数组
//        byte[] bfile = cfile.getBytes();
//
//        // 新文件名=当前时间的毫秒数+3位随机数
//        String fileName = String.valueOf(System.currentTimeMillis());
//        // 获得三位随机数
//        Random random = new Random();
//        for (int i = 0; i < 3; i++) {
//            fileName = fileName + random.nextInt(9);
//        }
//        // 拿到项目的部署路径
//        String path = request.getSession().getServletContext().getRealPath("/");
//        // 将用户上传的文件保存到服务器上
//        OutputStream out = new FileOutputStream(new File(path + "/upload/" + fileName + suffix));
//        IOUtils.write(bfile, out);
//        out.close();
//        return "success";
//    }
    /**
     * 文件上传方式二
     * @param request
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = "/fileUpload2.do")
//    public String fileUpload2(@RequestParam(value="pc",required=false) MultipartFile file,HttpServletRequest request) throws Exception {
//        if(file==null){
//            MultipartHttpServletRequest rm = (MultipartHttpServletRequest) request;
//            file=rm.getFile("pic");
//        }
//        String realPath=request.getSession().getServletContext().getRealPath("upload");
//        File destFile=new File(realPath+"/"+ UUID.randomUUID().toString()+file.getName());
//        file.transferTo(destFile);//将上传上来的临时文件移到到我们的目标目录，File类型的也有类似的renameTo方法移动文件
//        return "success";
//    }

}
