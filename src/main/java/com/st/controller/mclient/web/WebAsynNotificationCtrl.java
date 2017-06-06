package com.st.controller.mclient.web;

import com.st.constant.Constant;
import com.st.controller.mclient.constant.MerchantConstant;
import com.st.controller.mclient.domain.request.AsynNotificationReqDto;
import com.st.core.util.xml.ParseXmlUtil;
import com.st.utils.JoYoUtil;
import com.st.utils.mclient.BASE64;
import com.st.utils.mclient.DESUtil;
import com.st.utils.mclient.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RequestMapping("/mclient/pay")
@Controller
public class WebAsynNotificationCtrl {
	@SuppressWarnings("unused")
	@Resource
    private MerchantConstant merchantConstant;



    private static final Logger logger = Logger.getLogger(WebAsynNotificationCtrl.class);

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void execute(HttpServletRequest request,HttpServletResponse response) {
        logger.info("**********接收异步通知开始。**********");
        //获取通知原始信息
        String resp = request.getParameter("resp");
        logger.info("异步通知原始数据:"+resp);
        if(null == resp){
            return ;
        }
        //获取配置密钥
//        String desKey = merchantConstant.getMerchantDESKey();
        String desKey = Constant.MERCHANT_DESKEY;
        String md5Key =Constant.MERCHANT_MD5KKEY;
        logger.info("desKey:"+ desKey);
        logger.info("md5Key:"+ md5Key);
        try {
            PrintWriter out = response.getWriter();
            boolean flag=false;
            //首先对Base64编码的数据进行解密
            byte[] decryptBASE64Arr =  BASE64.decode(resp);
            //解析XML
            AsynNotificationReqDto dto = parseXML(decryptBASE64Arr);
            logger.info("解析XML得到对象:"+ JsonUtil.write2JsonStr(dto));
            //验证签名
            String ownSign = generateSign(dto.getVersion(), dto.getMerchant(), dto.getTerminal(), dto.getData(), md5Key);
            logger.info("根据传输数据生成的签名:"+ownSign);
            if (!dto.getSign().equals(ownSign)) {
                //验签不对
                logger.info("签名验证错误!");
                throw new RuntimeException();
            }else{
                logger.info("签名验证正确!");
                //
            }
            //验签成功，业务处理
            //对Data数据进行解密
            byte[] rsaKey = decryptBASE64(desKey);
            String decryptArr = DESUtil.decrypt(dto.getData(), rsaKey, "utf-8");
            logger.info("decryptArr："+decryptArr);
            //解密出来的数据也为XML文档，可以用dom4j解析
            /**
             * 解析出来 获取id  然后进行业务处理
             * <?xml version="1.0" encoding="UTF-8"?>
             * <DATA>	
             * <TRADE>		
             * <TYPE>S</TYPE>		
             * <ID>223127811409154509690</ID>		
             * <AMOUNT>1</AMOUNT>		
             * <CURRENCY>CNY</CURRENCY>		
             * <DATE>20140827</DATE>		
             * <TIME>160011</TIME>		
             * <NOTE>交易描述</NOTE>		
             * <STATUS>0</STATUS>	
             * </TRADE>	<RETURN>		
             * <CODE>0000</CODE>		
             * <DESC>成功</DESC>
             * 	</RETURN>
             * </DATA>
             * 
             */
            Map<String, String> map=ParseXmlUtil.parseXmlText(decryptArr);
            /**
             * 业务
             */
			String serialNo = ""; // 交易流水号
			String payType = "unionpay"; // 交易方式
			String orderNo = ""; // 订单号
			String thirdPayAmt = "0";//订单金额
			String status ="7";//支付状态
			String timetemp="";
			String datetemp="";
			String codetemp="";
//			14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- TIME and value=---- 142844
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- CURRENCY and value=---- CNY
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- DESC and value=---- 成功
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- DATE and value=---- 20150717
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- ID and value=---- 20150717142747353323
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- NOTE and value=---- 无忧保订单-20150717142747353323
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- STATUS and value=---- 0
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- AMOUNT and value=---- 1
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- TYPE and value=---- S
//					14:32:57.541 [http-bio-80-exec-49] INFO  c.s.c.m.w.WebAsynNotificationCtrl - key=------- CODE and value=---- 0000

			for (String key : map.keySet()) {
				logger.info("key=------- "+ key + " and value=---- " + map.get(key));
				if("TIME".equals(key)){
					timetemp = map.get(key);//时间
				} else if("DATE".equals(key)){
					datetemp = map.get(key);//日期
				} else if("ID".equals(key)){
					orderNo = map.get(key);//订单号
				} else if("CODE".equals(key)){
					codetemp = map.get(key);//交易返回码
				} else if("STATUS".equals(key)){
					status = map.get(key);//交易返回状态
				} else if("AMOUNT".equals(key)){
						thirdPayAmt = map.get(key);//订单金额
						try {
					        StringBuffer result = new StringBuffer();    
					        if(thirdPayAmt.length()==1){    
					            result.append("0.0").append(thirdPayAmt);    
					        }else if(thirdPayAmt.length() == 2){    
					            result.append("0.").append(thirdPayAmt);    
					        }else{    
					            String intString = thirdPayAmt.substring(0,thirdPayAmt.length()-2);    
					            for(int i=1; i<=intString.length();i++){    
					                if( (i-1)%3 == 0 && i !=1){    
					                    result.append(",");    
					                }    
					                result.append(intString.substring(intString.length()-i,intString.length()-i+1));    
					            }    
					            result.reverse().append(".").append(thirdPayAmt.substring(thirdPayAmt.length()-2));    
					        }
					        thirdPayAmt = result.toString();

						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
				}
			}
			serialNo=datetemp+""+timetemp;//以成交时间作为流水号
			logger.info("交易流水号serialNo:"+serialNo);
			logger.info("订单号orderNo:"+orderNo);
			logger.info("订单金额thirdPayAmt:"+thirdPayAmt);
			//判断交易是否成功
			if("0000".equals(codetemp)&&"0".equals(status)){
				logger.info("通知状态正常，进行订单状态修改操作");
				JSONObject resultStr = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
				thirdPayAmt=thirdPayAmt.replaceAll(",", "");
	            String mystr="orderNo="+orderNo+"&payType="+payType+"&serialNo="+serialNo+"&thirdPayAmt="+thirdPayAmt;
                resultStr = JSONObject.fromObject(JoYoUtil.sendPost(JoYoUtil.JAVA_ORDER_CONFIRM, mystr));
                if(resultStr.containsKey("errcode")&&!resultStr.getString("errcode").equals("20095")){
                    logger.info("京东支付成功，确认订单发生异常:"+resultStr.getString("errcode"));

                }
				if(resultStr.containsKey("status")&&0 == resultStr.getInt("status")){
				    logger.info("message:"+resultStr.getString("data"));
                    flag=true;
				}
			}
            
            logger.info("对<DATA>进行解密得到的数据:"+decryptArr);
            dto.setData(decryptArr);
           
            logger.info("最终数据:"+ JsonUtil.write2JsonStr(dto));
            logger.info("**********接收异步通知结束。**********");
            if (flag){
                out.println("success");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return ;
    }

    //XML解析为Java对象
    private static AsynNotificationReqDto parseXML(byte[] xmlString) {
        SAXReader reader = new SAXReader();
        Document document = null;
        Element rootElement =null;
        try {
            InputStream is = new ByteArrayInputStream(xmlString);
            SAXReader sax = new SAXReader(false);
            document = sax.read(is);
            rootElement = document.getRootElement();
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
        }
        AsynNotificationReqDto dto = new AsynNotificationReqDto();
        if (null == rootElement) {
            return dto;
        }
        Element versionEliment = rootElement.element("VERSION");
        if (null != versionEliment) {
            dto.setVersion(versionEliment.getText());
        }
        Element merchantEliment = rootElement.element("MERCHANT");
        if (null != merchantEliment) {
            dto.setMerchant(merchantEliment.getText());
        }
        Element terminalEliment = rootElement.element("TERMINAL");
        if (null != terminalEliment) {
            dto.setTerminal(terminalEliment.getText());
        }
        Element datalEliment = rootElement.element("DATA");
        if (null != datalEliment) {
            dto.setData(datalEliment.getText());
        }
        Element signEliment = rootElement.element("SIGN");
        if (null != signEliment) {
            dto.setSign(signEliment.getText());
        }
        return dto;
    }


    //对Base64进行解密
    public static byte[] decryptBASE64(String key) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * 签名
     */
    public static String generateSign(String version, String merchant, String terminal, String data, String md5Key) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append(merchant);
        sb.append(terminal);
        sb.append(data);
        String sign = "";
        sign = md5(sb.toString(), md5Key);
        return sign;
    }

    public static String md5(String text, String salt) throws NoSuchAlgorithmException {
        byte[] bytes = (text + salt).getBytes();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return sb.toString().toLowerCase();
    }


    /*****以下为测试代码*****/


    //通知数据样例
    private static String testString = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxDSElOQUJBTks+DQogICAg\n" +
            "PFZFUlNJT04+MS4yLjA8L1ZFUlNJT04+DQogICAgPE1FUkNIQU5UPjIyMzEyNzgxPC9NRVJDSEFO\n" +
            "VD4NCiAgICA8VEVSTUlOQUw+MDAwMDAwMDE8L1RFUk1JTkFMPg0KICAgIDxEQVRBPkJiQ2NzeVUx\n" +
            "L3kyMjlIeXZ4RFQ1Rm1SVnVjSFRXUFJqaGhyWmViRW1wTVAvL2xjdW04cjBRdVhGcjZPeDY5NXdN\n" +
            "dEYwblMwWHhjYVYKOVNoMWdrYU16dVJHQXorcytjOWhYREVnMUFXNVNUY3lRM0c3UTZKdzlBajJM\n" +
            "V0ZpelQ5cUNRYXVqT1FQT3RPWjIyRWF2RzZzNVJYLwo4c3AzYlJKa3hKNnpDYlQ3ckw0anNJZm9G\n" +
            "T05BdDBIV1VYUUpiazRBa3NlK1d3emNybU5QUDVzMVcyckRPUnA5Z3cwcVVhVW9DZmdNCk5LSGNY\n" +
            "Ymx2ZVZlVmNZeHlBMHJrRk9xNnFIV0tybEhqRGdqVWl0dFJZQU9CYlA0TDJDSTVvK3dMQzNpV1Z5\n" +
            "NmVpTHd2QnNJeWM3amwKU2NhanNaTkgxeDlPbUhUVitXWFA1ejBlejdYb0U1SGJUaDBmckdaeERZ\n" +
            "U2wwZlQvMnkvUDFpbnlrVUpwQktiVnA3c2w4NVVyZjVTcgpZZGM0VzA0QXdJajI0NnBpOW1KUHU2\n" +
            "d0w2bG5VV24zdXpjT2xDRUxpWkJ6OTJueXI3anlYeXIzR05Ha0VwdFdudXlYenhXV3AzMW8zCmNm\n" +
            "MFkwWTMrMGJjbm5BPT08L0RBVEE+DQogICAgPFNJR04+YzhhYTc1NGVmZjQ5MzIyNmYzNzU4NTJk\n" +
            "MGFmNTlmMmU8L1NJR04+DQo8L0NISU5BQkFOSz4NCg0K";

    //DES 密钥
    private static final String DES_KEY = "Z8KMT8cT4z5ruu89znxFhRP4DdDBqLUH";

    //MD5 密钥
    private static final String MD5_KEY = "test";

    //供测试用
//    public static void main(String[] args) throws Exception {
//        byte[] decryptBASE64Arr = BASE64.decode(testString);
//        String decryptBASE64ArrStr = new String(decryptBASE64Arr,"utf-8");
//        System.out.println(decryptBASE64ArrStr);
//        AsynNotificationReqDto dto = parseXML(decryptBASE64Arr);
//        System.out.println("AsynNotificationReqDto:"+ JsonUtil.write2JsonStr(dto));
//        byte[] rsaKey = decryptBASE64(DES_KEY);
//        String decryptArr = DESUtil.decrypt(dto.getData(), rsaKey, "utf-8");
//        System.out.println("decryptArr:"+decryptArr);
//        //验证签名
//        String ownSign = generateSign(dto.getVersion(), dto.getMerchant(), dto.getTerminal(), dto.getData(), MD5_KEY);
//        System.out.println(ownSign);
//    }

//接收到的数据
    /*
     "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxDSElOQUJBTks+DQogICAg\n" +
            "PFZFUlNJT04+MS4yLjA8L1ZFUlNJT04+DQogICAgPE1FUkNIQU5UPjIyMzEyNzgxPC9NRVJDSEFO\n" +
            "VD4NCiAgICA8VEVSTUlOQUw+MDAwMDAwMDE8L1RFUk1JTkFMPg0KICAgIDxEQVRBPkJiQ2NzeVUx\n" +
            "L3kyMjlIeXZ4RFQ1Rm1SVnVjSFRXUFJqaGhyWmViRW1wTVAvL2xjdW04cjBRdVhGcjZPeDY5NXdN\n" +
            "dEYwblMwWHhjYVYKOVNoMWdrYU16dVJHQXorcytjOWhYREVnMUFXNVNUY3lRM0c3UTZKdzlHVE9G\n" +
            "cERHVis5a1g0TVI3aGVUQUZPWjIyRWF2RzZzNVJYLwo4c3AzYlJKa3hKNnpDYlQ3ckw0anNJZm9G\n" +
            "T05BdDBIV1VYUUpiazRBa3NlK1d3emNybU5QUDVzMVcyckRPUnA5Z3cwcVVhVW9GUTFsCjNmV09S\n" +
            "WHR2ZVZlVmNZeHlBMHJrRk9xNnFIV0t1Mjd2LzN0UDVQbFJZQU9CYlA0TDJDSTVvK3dMQzNpV1Z5\n" +
            "NmVpTHd2QnNJeWM3amwKU2NhanNaTkgxeDlPbUhUVldoUDVZM3l5UkxEb0U1SGJUaDBmckdaeERZ\n" +
            "U2wwZlQvMnkvUDFpbnlrVUpwQktiVnA3c2w4NVVyZjVTcgpZZGM0VGVRS2lWSVFmSWdDb0l4ZVJD\n" +
            "dmFzZmEycWVUSUd0Z1M5akVSdWpGQ3p1Myt4T20wa1VyVHdrUXQxNW1SVUxZcVM2MC9mcDJKCjhj\n" +
            "VmF3WXFVMzNjL014ZVIySWZiZkgxais1OHVZbHNOajFlQjlyVThKQ29ncnFmbEFyMFY3R0NaPC9E\n" +
            "QVRBPg0KICAgIDxTSUdOPjQ1OWZjNjE2YjUwYmQyOWM2N2YxZjg0YmU3YzEwMzAzPC9TSUdOPg0K\n" +
            "PC9DSElOQUJBTks+DQoNCg=="
     */

//接收数据进行Base64转码获取的数据
/*
<?xml version="1.0" encoding="UTF-8"?>
<CHINABANK>
    <VERSION>1.2.0</VERSION>
    <MERCHANT>22312781</MERCHANT>
    <TERMINAL>00000001</TERMINAL>
    <DATA>BbCcsyU1/y229HyvxDT5FmRVucHTWPRjhhrZebEmpMP//lcum8r0QuXFr6Ox695wMtF0nS0XxcaV
9Sh1gkaMzuRGAz+s+c9hXDEg1AW5STcyQ3G7Q6Jw9GTOFpDGV+9kX4MR7heTAFOZ22EavG6s5RX/
8sp3bRJkxJ6zCbT7rL4jsIfoFONAt0HWUXQJbk4Akse+WwzcrmNPP5s1W2rDORp9gw0qUaUoFQ1l
3fWORXtveVeVcYxyA0rkFOq6qHWKu27v/3tP5PlRYAOBbP4L2CI5o+wLC3iWVy6eiLwvBsIyc7jl
ScajsZNH1x9OmHTVWhP5Y3yyRLDoE5HbTh0frGZxDYSl0fT/2y/P1inykUJpBKbVp7sl85Urf5Sr
Ydc4TeQKiVIQfIgCoIxeRCvasfa2qeTIGtgS9jERujFCzu3+xOm0kUrTwkQt15mRULYqS60/fp2J
8cVawYqU33c/MxeR2IfbfH1j+58uYlsNj1eB9rU8JCogrqflAr0V7GCZ</DATA>
    <SIGN>459fc616b50bd29c67f1f84be7c10303</SIGN>
</CHINABANK>
*/

//DATA解密出来的数据
/*
<?xml version="1.0" encoding="UTF-8"?>
<DATA>
	<TRADE>
		<TYPE>S</TYPE>
		<ID>223127811409087990546</ID>
		<AMOUNT>1</AMOUNT>
		<CURRENCY>CNY</CURRENCY>
		<DATE>20140826</DATE>
		<TIME>213101</TIME>
		<NOTE>交易描述</NOTE>
		<STATUS>7</STATUS>
	</TRADE>
	<RETURN>
		<CODE>EEB0004</CODE>
		<DESC>银行交易失败 需自动签到重试</DESC>
	</RETURN>
</DATA>
*/
}
