package com.st.controller;

import com.st.core.util.text.StringUtils;
import com.st.service.WeixinAPIService;
import com.st.utils.ContextHolderUtils;
import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

@Controller
@RequestMapping("/personrefund")
public class PersonrefundController {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	@Resource
	private WeixinAPIService weixinAPIService;
	   /**
     * 退款 初始化    
     * @param request
     * @return
     */
    @RequestMapping("/refundInit")
    public @ResponseBody Object refundInit(HttpServletRequest request ,HttpServletResponse response) throws IOException{
        HttpSession session=ContextHolderUtils.getSession();
        Object userIdObject=session.getAttribute("userId");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String insurerId=request.getParameter("insurerId");
        String cityId=request.getParameter("cityId");
        String insuranceMonth=request.getParameter("insuranceMonth");
        String housingFundMonth=request.getParameter("housingFundMonth");
        String primary=request.getParameter("primary");
        String type=request.getParameter("type");
        if(StringUtils.isEmpty(insurerId)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择参保人\"}");
        }
        StringBuffer buffer=new StringBuffer();
        buffer.append("insurerId=").append(insurerId);
        if(StringUtils.isNotEmpty(cityId)){
            buffer.append("&cityId=").append(cityId);
            buffer.append("&type=").append(type);
        }
        if(StringUtils.isNotEmpty(insuranceMonth)){
            buffer.append("&insuranceMonth=").append(insuranceMonth);
        }
        if(StringUtils.isNotEmpty(housingFundMonth)){
            buffer.append("&housingFundMonth=").append(housingFundMonth);
        }
        if(StringUtils.isNotEmpty(primary)){
            buffer.append("&primary=").append(primary);
        }
        try {
                //提交退款申请信息
            resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_REFUND_INIT, buffer.toString()));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    
    /**
   * 申请退款    
   * @param request
   * @return
   */
  @RequestMapping("/refundAdd")
  public @ResponseBody Object refundAdd(HttpServletRequest request ,HttpServletResponse response) throws IOException{
      HttpSession session=ContextHolderUtils.getSession();
      Object userIdObject=session.getAttribute("userId");
      if(userIdObject==null||"".equals(userIdObject)){
          return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
      }
      JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
      String insurerId=request.getParameter("insurerId");//参保人Id
      if(StringUtils.isEmpty(insurerId)){
          return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择参保人\"}");
      }
      String cityId=request.getParameter("cityId"); //"参保城市id" Integer
      if(StringUtils.isEmpty(cityId)){
          return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择退保城市\"}");
      }
      String cityName=request.getParameter("cityName");//参保城市名称
      String isBank=request.getParameter("isBank");//退款方式是否是银行卡
      String bankId=request.getParameter("bankId");//银行卡Id
      String reason=request.getParameter("reason");//退款原因
      String remark=request.getParameter("remark");//退款说明
      String insuranceMonth=request.getParameter("insuranceMonth");
      String housingFundMonth=request.getParameter("housingFundMonth");
      String mystr = "insurerId="+insurerId+"&policyHolderId="+userIdObject.toString()+"&cityId="+cityId+"&cityName="+cityName+"&isBank="+isBank+"&reason="+reason;
      if(StringUtils.isNotEmpty(bankId)){
          mystr+="&bankId="+bankId;
      }
      if(StringUtils.isNotEmpty(remark)){
          mystr+="&remark="+remark;
      }
      if(StringUtils.isNotEmpty(insuranceMonth)){
          mystr+="&insuranceMonth="+insuranceMonth;
      }
      if(StringUtils.isNotEmpty(housingFundMonth)){
          mystr+="&housingFundMonth="+housingFundMonth;
      }
      try {
              //提交退款申请信息
          resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_REFUND_ADD, mystr));
      } catch (Exception e) {
          logger.error("获取数据出错:" + e.getMessage(), e);
      }
      if(resultStr.containsKey("errcode")){
          JSONObject data=new JSONObject();
          data.put("status", 1);
          data.put("errcode", resultStr.getString("errcode"));
          data.put("msg", resultStr.getString("errmsg"));
          resultStr=data;
      }
      response.setContentType("text/html; charset=utf-8");  
      PrintWriter out=response.getWriter();
      out.println(resultStr);
      return null;
  }
  
      /**
     * 退款   修改保存
     * @param request
     * @return
     */
    @RequestMapping("/refundSave")
    public @ResponseBody Object refundSave(HttpServletRequest request ,HttpServletResponse response) throws IOException{
        HttpSession session=ContextHolderUtils.getSession();
        Object userIdObject=session.getAttribute("userId");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String refundId=request.getParameter("refundId");//退款单Id
        if(StringUtils.isEmpty(refundId)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择退款单\"}");
        }
        String cityId=request.getParameter("cityId"); //"参保城市id" Integer
        if(StringUtils.isEmpty(cityId)){
            return JSONObject.fromObject("{\"status\":1,\"msg\":\"请选择退保城市\"}");
        }
        String cityName=request.getParameter("cityName");//参保城市名称
        String isBank=request.getParameter("isBank");//退款方式是否是银行卡
        String bankId=request.getParameter("bankId");//银行卡Id
        String reason=request.getParameter("reason");//退款原因
        String remark=request.getParameter("remark");//退款说明
        int insuranceMonth=Integer.parseInt(request.getParameter("insuranceMonth")==null?"0":request.getParameter("insuranceMonth"));//社保的起退月份
        int housingFundMonth=Integer.parseInt(request.getParameter("housingFundMonth")==null?"0":request.getParameter("housingFundMonth"));//housingFundMonth
        String mystr = "refundId="+refundId+"&policyHolderId="+userIdObject.toString()+"&cityId="+cityId+"&cityName="+cityName+"&isBank="+isBank+"&reason="+reason;
        if(StringUtils.isNotEmpty(bankId)){
            mystr+="&bankId="+bankId;
        }
        if(StringUtils.isNotEmpty(remark)){
            mystr+="&remark="+remark;
        }
        if(insuranceMonth!=0){
            mystr+="&insuranceMonth="+insuranceMonth;
        }
        if(housingFundMonth!=0){
            mystr+="&housingFundMonth="+housingFundMonth;
        }
        try {
                //提交退款申请信息
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_REFUND_SAVE, mystr));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }
    	
      /**
     * 添加银行卡 
     * @param request
     * @return
     */
    @RequestMapping("/bankCardAdd")
    public @ResponseBody Object bankCardAdd(HttpServletRequest request ,HttpServletResponse response) throws IOException{
        HttpSession session=ContextHolderUtils.getSession();
        Object userIdObject=session.getAttribute("userId");
        if(userIdObject==null||"".equals(userIdObject)){
            return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
        }
        JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String bankName=request.getParameter("bankName");//银行名称
        String bankType=request.getParameter("bankType");//银行类型ID
        String bankAccount=request.getParameter("bankAccount");//卡号
        String bankHolder=request.getParameter("bankHolder");//收款人
        String bankDetail=request.getParameter("bankDetail");//开户行
        String mystr ="&policyHolderId="+userIdObject.toString()+"&bankName="+bankName+"&bankType="+bankType+"&bankAccount="+bankAccount+"&bankHolder="+bankHolder;
        if(StringUtils.isNotEmpty(bankDetail)){
            mystr+="&bankDetail="+bankDetail;
        }
        try {
                //提交退款申请信息
            resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_BANKCARD_ADD, mystr));
        } catch (Exception e) {
            logger.error("获取数据出错:" + e.getMessage(), e);
        }
        if(resultStr.containsKey("errcode")){
            JSONObject data=new JSONObject();
            data.put("status", 1);
            data.put("errcode", resultStr.getString("errcode"));
            data.put("msg", resultStr.getString("errmsg"));
            resultStr=data;
        }
        response.setContentType("text/html; charset=utf-8");  
        PrintWriter out=response.getWriter();
        out.println(resultStr);
        return null;
    }

        /**
       * 获取银行卡列表 
       * @param request
       * @return
       */
      @RequestMapping("/bankCardList")
      public @ResponseBody Object bankCardList(HttpServletRequest request ,HttpServletResponse response) throws IOException{
          HttpSession session=ContextHolderUtils.getSession();
          Object userIdObject=session.getAttribute("userId");
          if(userIdObject==null||"".equals(userIdObject)){
              return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
          }
          JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
          String mystr ="&policyHolderId="+userIdObject.toString();
          try {
              resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BANKCARD_LIST, mystr));
          } catch (Exception e) {
              logger.error("获取数据出错:" + e.getMessage(), e);
          }
          if(resultStr.containsKey("errcode")){
              JSONObject data=new JSONObject();
              data.put("status", 1);
              data.put("errcode", resultStr.getString("errcode"));
              data.put("msg", resultStr.getString("errmsg"));
              resultStr=data;
          }
          response.setContentType("text/html; charset=utf-8");  
          PrintWriter out=response.getWriter();
          out.println(resultStr);
          return null;
      }
	
      /**
         * 获取银行列表 
         * @param request
         * @return
         */
        @RequestMapping("/bankList")
        public @ResponseBody Object bankList(HttpServletRequest request ,HttpServletResponse response) throws IOException{
            HttpSession session=ContextHolderUtils.getSession();
            Object userIdObject=session.getAttribute("userId");
            if(userIdObject==null||"".equals(userIdObject)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            try {
                resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_BANK_LIST, ""));
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            if(resultStr.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr=data;
            }
            response.setContentType("text/html; charset=utf-8");  
            PrintWriter out=response.getWriter();
            out.println(resultStr);
            return null;
        }
      
        /**
         * 取消退款 
         * @param request
         * @return
         */
        @RequestMapping("/refundCancel")
        public @ResponseBody Object refundCancel(HttpServletRequest request ,HttpServletResponse response) throws IOException{
            HttpSession session=ContextHolderUtils.getSession();
            Object userIdObject=session.getAttribute("userId");
            if(userIdObject==null||"".equals(userIdObject)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            String refundId = request.getParameter("refundId");//退款单ID
            if(StringUtils.isEmpty(refundId)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择退款单\"}");
            }
            String cancelReason = request.getParameter("cancelReason");//退款原因
            String isExecute = request.getParameter("isExecute");//是否确认，0是开始调用1是确认
            try {
                resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_REFUND_CANCEL, 
                		"refundId=" + refundId + "&cancelReason=" + cancelReason 
                		+ "&policyHolderId=" + userIdObject.toString() + "&isExecute=" + isExecute ));
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            if(resultStr.containsKey("errcode")){
                JSONObject data = new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr = data;
            }
            response.setContentType("text/html; charset=utf-8");  
            PrintWriter out=response.getWriter();
            out.println(resultStr);
            return null;
        }
        
        
        /**
         * 退款详情
         * @param request
         * @return
         */
        @RequestMapping("/refundInfo")
        public @ResponseBody Object refundInfo(HttpServletRequest request ,HttpServletResponse response) throws IOException{
            HttpSession session=ContextHolderUtils.getSession();
            Object userIdObject=session.getAttribute("userId");
            if(userIdObject==null||"".equals(userIdObject)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            String refundId=request.getParameter("refundId");
            String refundSource=request.getParameter("refundSource");
            if(StringUtils.isEmpty(refundId)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择退款单\"}");
            }
            try {
                resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_REFUND_INFO, "refundId="+refundId+"&refundSource="+refundSource));
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            if(resultStr.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr=data;
            }
            response.setContentType("text/html; charset=utf-8");  
            PrintWriter out=response.getWriter();
            out.println(resultStr);
            return null;
        }
        /**
         * 修改退款加载初始化
         * @param request
         * @return
         */
        @RequestMapping("/refundUpdateInit")
        public @ResponseBody Object refundUpdateInit(HttpServletRequest request ,HttpServletResponse response) throws IOException{
            HttpSession session=ContextHolderUtils.getSession();
            Object userIdObject=session.getAttribute("userId");
            if(userIdObject==null||"".equals(userIdObject)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            String refundId=request.getParameter("refundId");
            if(StringUtils.isEmpty(refundId)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择退款单\"}");
            }
            try {
                resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_REFUND_UPDATE_INIT, "refundId="+refundId));
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            if(resultStr.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr=data;
            }
            response.setContentType("text/html; charset=utf-8");  
            PrintWriter out=response.getWriter();
            out.println(resultStr);
            return null;
        }
        /**
         * 获取退款列表
         * @param request
         * @return
         */
        @RequestMapping("/getRefundList")
        public @ResponseBody Object getRefundList(HttpServletRequest request ,HttpServletResponse response) throws IOException{
            HttpSession session=ContextHolderUtils.getSession();
            Object userIdObject=session.getAttribute("userId");
            if(userIdObject==null||"".equals(userIdObject)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
            }
            JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
            String insurerId=request.getParameter("insurerId");
            if(StringUtils.isEmpty(insurerId)){
                return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
            }
            String pageNow=request.getParameter("pageNow");//当前查询第几页
            String pageSize = request.getParameter("pageSize");//每页数据条数,此参数有默认值为10
            StringBuffer buffer=new StringBuffer();
            buffer.append("insurerId=").append(insurerId).append("&policyHolderId=").append(userIdObject.toString());
            if(StringUtils.isNotEmpty(pageNow)){
                buffer.append("&pageNow=").append(pageNow);
            }
            if(StringUtils.isNotEmpty(pageSize)){
                buffer.append("&pageSize=").append(pageSize);
            }
            try {
                resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_REFUND_LIST, buffer.toString()));
            } catch (Exception e) {
                logger.error("获取数据出错:" + e.getMessage(), e);
            }
            if(resultStr.containsKey("errcode")){
                JSONObject data=new JSONObject();
                data.put("status", 1);
                data.put("errcode", resultStr.getString("errcode"));
                data.put("msg", resultStr.getString("errmsg"));
                resultStr=data;
            }
            response.setContentType("text/html; charset=utf-8");  
            PrintWriter out=response.getWriter();
            out.println(resultStr);
            return null;
        }
        /**
         * 是否可退款
         * @param request
         * @return
          * @throws ParseException 
         */
         @RequestMapping("/isAbleRefund")
         public @ResponseBody Object isAbleRefund(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
             HttpSession session = ContextHolderUtils.getSession();
             Object userIdObject =  session.getAttribute("userId");//4125
             JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
             if(userIdObject==null||"".equals(userIdObject)){
                 return JSONObject.fromObject("{\"status\":-1,\"msg\":\"未登录\"}");
             }
             String insurerId=request.getParameter("insurerId");//参保人ID
             if(StringUtils.isEmpty(insurerId)){
                 return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保人\"}");
             }
             String serviceType=request.getParameter("serviceType");//公积金或者社保的区分字段 1社保2公积金
             if(StringUtils.isEmpty(serviceType)){
                 return JSONObject.fromObject("{\"status\":-1,\"msg\":\"请选择参保类别\"}");
             }
             String cityId=request.getParameter("cityId");//参保城市
             if(cityId==null||cityId.trim().equals("")){
                 return JSONObject.fromObject("{\"status\":3,\"msg\":\"请选择参保城市\"}");
             }
             try {
                 resultStr = JSONObject.fromObject(JoYoUtil.sendGet(JoYoUtil.JAVA_REFUND_ABLE, "cityId="+cityId+"&insurerId="+insurerId+"&serviceType="+serviceType));
             } catch (Exception e) {
                 logger.error("获取基础参保信息出错:" + e.getMessage(), e);
             }
             if(resultStr.containsKey("errcode")){
                 JSONObject data=new JSONObject();
                 data.put("status", resultStr.getString("errcode"));
                 data.put("msg", resultStr.getString("errmsg"));
                 resultStr=data;
             }
             response.setContentType("text/html; charset=utf-8");  
             PrintWriter out=response.getWriter();
             out.println(resultStr);
             return null;
         }
}
