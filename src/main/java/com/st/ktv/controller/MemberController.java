package com.st.ktv.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.st.core.ContextHolderUtils;
import com.st.ktv.controller.alipay.config.AlipayConfig;
import com.st.ktv.entity.WechatMember;
import com.st.ktv.service.MemberService;
import com.st.utils.Constant;
import com.st.utils.DataUtil;
import com.st.utils.ImagesUtil;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @Description
 * @FileName MemberController
 * @Author dingzr
 * @CreateTime 2017/6/10 14:23 六月
 */

@Controller
@RequestMapping("/member")
public class MemberController {

    @Resource
    MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 直接访问域名跳转首页推送相关数据
     * @param request
     * @return object
     */
    @RequestMapping("/gotoIndexDomain")
    public String getWeixintoIndex(HttpServletRequest request) {
        try {
            String[] arr = new String[]{};
            String mystr = "";
            JSONObject storeList = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.STORES, mystr, arr));
            if(storeList.getInt("error_code") == 0){
                request.setAttribute("storeList",storeList.get("result"));
            }

            //获取网站参数
            JSONObject web = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.WEB_PARAMS, mystr, arr));
            if(web.getInt("error_code") == 0){
                request.setAttribute("web",web.get("result"));
            }

            //获取轮播图
            String[] arr1 = new String[]{"type" + 1};
            String mystr1 = "type=" + 1;
            JSONObject result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.INDEX_BANNERS, mystr1, arr1));
            if(result.getInt("error_code") == 0){
                request.setAttribute("recruits",result.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取登录出错:" + e.getMessage(), e);
        }
        return "index";
    }

    /**
     * 跳转到首页
     * @param request
     * @return
     */
    @RequestMapping("/gotoIndex")
    public String gotoindex(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "/member/gotoIndexDomain.do";
    }

    /**
     * 跳转到积分兑换页面
     * @param request
     * @return
     */
    @RequestMapping("/gotoIntegralMall")
    public String gotoIntegralMall(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        //去获取积分的数据库集合并返回给页面  礼品、包厢、酒水 分区
        return "redirect:"+ Constant.URL + "my/integralmall";
    }

    /**
     * 跳转到在线超市
     * @param request
     * @return
     */
    @RequestMapping("/gotoShop")
    public String gotoShop(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "/shop/getShopping.do";
    }

    /**
     * 跳转到优惠活动
     * @param request
     * @return
     */
    @RequestMapping("/gotoDiscount")
    public String gotoDiscount(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "/discount/getDiscountList.do";
    }

    /**
     * 跳转到佣金提现
     * @param request
     * @return
     */
    @RequestMapping("/gotoCommission")
    public String gotoCommission(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+ Constant.URL + "commission/commission";
    }

    /**
     * 跳转到我的预定订单
     * @param request
     * @return
     */
    @RequestMapping("/gotoMyOrder")
    public String gotoMyOrder(HttpServletRequest request){

        //获取参数，可能为空
        HttpServletRequest httpServletRequest = request;
        String str = httpServletRequest.getQueryString();
        if (DataUtil.isNotEmpty(str)){
        }
        return "redirect:"+Constant.URL+"/personorder/getOrderList.do";

    }

    /**
     * 进入用户中心
     * @param request
     * @return object
     */
    @RequestMapping("/gotoUserCenter")
    public String gotoUserCenter(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
        Object appidObj =  session.getAttribute("appid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
//        appidObj = "wxbb336e8a40b636d6";
        if ( !"".equals(openidObj) && openidObj != null && !"".equals(appidObj) && appidObj != null) {
            memberService.checkLogin(openidObj.toString(), appidObj.toString());

            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            request.setAttribute("member",member);

            String[] arr = new String[]{"member_id" + member.getId()};
            String mystr = "member_id=" + member.getId();
            JSONObject result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.USER_MESSAGES, mystr, arr));
            if(0 == result.getInt("error_code")){
                JSONArray array = JSONArray.fromObject(result.getString("result"));
                request.setAttribute("num", array.size());
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "my/usercenter";
    }

    /**
     * 进入错误提示页面
     * @param request
     * @return
     */
    @RequestMapping("/gotoErrorMsg")
    public String gotoErrorMsg(HttpServletRequest request) {
        request.setAttribute("msg", "请重新登录");
        return "other/code";
    }

    /**
     * 校验保存和更新手机号码
     * @param request
     * @return
     */
    @RequestMapping("/checkAndUpdateMobile")
    public @ResponseBody Object checkAndUpdateMobile(HttpServletRequest request,HttpServletResponse response) throws IOException{

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");

        String phone = request.getParameter("mobile");//手机号码
        String openid = openidObj.toString();//openid
        String type = request.getParameter("type");//校验保存和更新手机号码
        JSONObject resultObject = memberService.checkAndUpdateMobile(openid, phone, type);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultObject);
        return null;
    }

    /**
     * 进入用户设置
     * @param request
     * @return object
     */
    @RequestMapping("/gotoUserSet")
    public String gotoUserSet(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
        if ( !"".equals(openidObj) && openidObj != null) {
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            request.setAttribute("member",member);

            String[] arr = new String[]{"member_id" + member.getId()};
            String mystr = "member_id=" + member.getId();
            JSONObject result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.USER_MESSAGES, mystr, arr));
            if(0 == result.getInt("error_code")){
                JSONArray array = JSONArray.fromObject(result.getString("result"));
                request.setAttribute("num", array.size());
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "my/edit";
    }

    /**
     * 文件上传方式二
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUpload")
    public String fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 上传配置
        int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
        int MAX_FILE_SIZE      = 1024 * 1024 * 5; // 40MB
        int MAX_REQUEST_SIZE   = 1024 * 1024 * 10; // 50MB

        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            logger.info("Error: 表单必须包含 enctype=multipart/form-data");
        }else{
            HttpSession session = ContextHolderUtils.getSession();
            Object openidObj =  session.getAttribute("openid");
//            openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
            if ( !"".equals(openidObj) && openidObj != null) {

                // 配置上传参数
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
                factory.setSizeThreshold(MEMORY_THRESHOLD);
                // 设置临时存储目录
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                ServletFileUpload upload = new ServletFileUpload(factory);

                // 设置最大文件上传值
                upload.setFileSizeMax(MAX_FILE_SIZE);
                // 设置最大请求值 (包含文件和表单数据)
                upload.setSizeMax(MAX_REQUEST_SIZE);
                // 中文处理
                upload.setHeaderEncoding("UTF-8");
                // 构造临时路径来存储上传的文件
                // 这个路径相对当前应用的目录
                // 上传文件存储目录
                String UPLOAD_DIRECTORY = "/jsp/upload";
                String uploadPath = request.getSession().getServletContext().getRealPath("") + UPLOAD_DIRECTORY;
                String savePath = Constant.URL + UPLOAD_DIRECTORY;
                String fileName = "";
                // 如果目录不存在则创建
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                try {
                    // 解析请求的内容提取文件数据
                    List<FileItem> formItems = upload.parseRequest(request);
                    if (!formItems.isEmpty()) {
                        // 迭代表单数据
                        for (FileItem item : formItems) {
                            // 处理不在表单中的字段
                            if (!item.isFormField()) {
                                fileName = new File(item.getName()).getName();
                                if(null != fileName && !"".equals(fileName)){
                                    String[] name = fileName.split("\\.");
                                    if(name.length > 0){
                                        String form = name[name.length - 1];
                                        logger.info("获取的图片格式为：" + form);
                                        fileName = UUID.randomUUID().toString() + "." + form;
                                        uploadPath = uploadPath + "/" + fileName;
                                        savePath = savePath + "/" + fileName;
                                    }
                                    File storeFile = new File(uploadPath);
                                    // 在控制台输出文件的上传路径
                                    logger.info("文件上传路径为：" + uploadPath + "--保存路径：" + savePath);
                                    // 保存文件到硬盘
                                    item.write(storeFile);
                                    request.setAttribute("message", "文件上传成功!");
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    request.setAttribute("message", "错误信息: " + ex.getMessage());
                }
                //存储到相应的用户属性中
                memberService.updateHeadPortrait(openidObj.toString(), savePath);

                //调用去进行图片的压缩
                if(!fileName.equals("")){
                    //获得文件源
                    File file = new File(uploadPath);
                    if(!file.exists()){
                    }else{
                        logger.info("即将压缩的图片文件名：" + file.getName() + ",文件大小：" + file.length()/1024 + "KB");
                    }
                    //只有大于50KB的才会去压缩
                    if(file.length()/1024 > 50){
                        ImagesUtil.scaleImageWithParams(uploadPath, uploadPath, 70, 70, true, "png");
                    }
                }
            }
        }
        return "redirect:"+Constant.URL+"/member/gotoUserSet.do";
    }

    /**
     * 从本地磁盘中读取图片
     * @param request
     * @param response
     */
    @RequestMapping("/showHeadPortrait")
    public void showHeadPortrait(HttpServletRequest request,HttpServletResponse response){

        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
        if ( !"".equals(openidObj) && openidObj != null) {
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            if(null != member && null != member.getHeadPortrait()){
                String filePath = Constant.FILE_PATH + member.getHeadPortrait();
//                filePath = "D:\\upload\\1.jpg";
                FileInputStream in;
                response.setContentType("application/octet-stream;charset=UTF-8");
                try {
                    //图片读取路径
                    in = new FileInputStream(filePath);
                    int i = in.available();
                    byte[]data = new byte[i];
                    in.read(data);
                    in.close();
                    //写图片
                    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                    outputStream.write(data);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                logger.info("用户不存在，请从微信端重新登录");
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
    }

    /**
     * 进入我的站内信
     * @param request
     */
    @RequestMapping("/gotoMyMessages")
    public String gotoMyMessages(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ContextHolderUtils.getSession();
        Object openidObj =  session.getAttribute("openid");
        Object appidObj =  session.getAttribute("appid");
//        openidObj = "oyAM9vwa6FN6trSrUweXCdK0Jh8s";
//        appidObj = "wxbb336e8a40b636d6";
        if ( !"".equals(openidObj) && openidObj != null && !"".equals(appidObj) && appidObj != null) {
            WechatMember member = memberService.getObjectByOpenid(openidObj.toString());
            String[] arr = new String[]{"member_id" + member.getId()};
            String mystr = "member_id=" + member.getId();
            JSONObject result = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.USER_MESSAGES, mystr, arr));
            if (0 == result.getInt("error_code")) {
                request.setAttribute("msgs",result.get("result"));
            }
        }else{
            request.setAttribute("error", "请在微信中访问");
        }
        return "my/message";
    }

    /**
     * 保存用户token
     * @param request
     * @author shenwf
     * @return
     */
    @RequestMapping("/alipayReturn")
    public String alipayReturn(HttpServletRequest request){
        logger.info("----------------支付宝同步回调开始----------------");
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//         try {
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//        } catch (UnsupportedEncodingException e) {
//             logger.error("转换格式出错:" + e.getMessage(), e);
//        }
            logger.info("name="+name+"   valueStr="+valueStr);
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String outTradeNo = "";
        String tradeNo = "";
        String tradeStatus = "";
        String totalAmount = "";
        try {
            outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("转换格式出错:" + e1.getMessage(), e1);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        logger.info("支付宝同步通知 订单号=" + outTradeNo + " 流水=" + tradeNo + " 金额=" + totalAmount);
//     boolean verify_result = AlipayNotify.verify(params);
        boolean verify_result = false;
        try {
            verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.info("verify_result  " + verify_result);
        if (verify_result) { //验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            logger.info("trade_status  " + tradeStatus);
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
            String mystr = "orderNo=" + outTradeNo + "&payType=alipay&serialNo=" + tradeNo + "&thirdPayAmt=" + totalAmount;
            logger.info("mystr=" + mystr);
            try {
                JSONObject resultStr = JSONObject.fromObject(JoYoUtil.sendPost("", mystr));
//                 if(resultStr.containsKey("status")||(resultStr.containsKey("errcode")&&resultStr.getString("errcode").equals("20095"))){
                if(resultStr.containsKey("status")){
                    logger.info("支付宝支付确认订单完成同步通知成功："+outTradeNo);
//                 }else if(resultStr.containsKey("errcode")&&!resultStr.getString("errcode").equals("20095")){
                }else{
                    logger.info("订单号"+outTradeNo+"支付宝支付成功，确认订单发生异常:"+resultStr.getString("errcode"));
                    request.setAttribute("msg", "订单号："+outTradeNo+" 支付发生异常，请联系无忧保客服400-111-8900");
                    request.setAttribute("orderNo", outTradeNo);
                    request.setAttribute("type", 0);
                    return "order/orderfail";
                }
            }catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            return "redirect:" + Constant.URL + "/personorder/gotoPaysuc.do?orderNo=" + outTradeNo;
            //该页面可做页面美工编辑
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
        } else {
            return "redirect:" + Constant.URL + "/jsp/my/allorderlist.jsp";
            //该页面可做页面美工编辑
        }
    }

}
