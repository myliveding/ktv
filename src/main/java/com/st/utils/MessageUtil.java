package com.st.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.st.javabean.pojo.resp.*;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st.controller.wechat.common.report.protocol.ReportReqData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * 
 * @author wuh
 * 
 */
public class MessageUtil {
	private static Logger logger =LoggerFactory.getLogger(MessageUtil.class);
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	
	/**
	 * 消息转发到多客服
	 */
	public static final String REQ_MESSAGE_TYPE_Transfer_Customer_Service = "transfer_customer_service";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	/**
	 * 事件类型：SCAN(扫码)
	 */
	public static final String EVENT_TYPE_SCAN = "SCAN";


	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	
	/**
	 * 事件类型：LOCATION(上报地理位置)
	 */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";
	
    public static Map<String, String> parseXml(String xml,String parentPath) {
    	  Map<String, String> map = null; 
    	try {
    		 	SAXReader reader = new SAXReader();  
    	        InputStream in = IOUtils.toInputStream(xml, "UTF-8");  
    	        Document doc = reader.read(in);  
    	        map = getParseMapFromXMLStream(doc,parentPath);  
		} catch (Exception e) {
			logger.error("XML解析出现异常！"+e.getMessage(),e);
			
		}
        
       
        return map;  
    } 
	
    @SuppressWarnings({ "rawtypes", "unused" })
	public static Map<String,String> getParseMapFromXMLStream(Document doc,String parentPath) throws DocumentException {  
        
        List rowList = doc.selectNodes(parentPath);  
        Map<String, String> map = null;  
        List list = null;  
        if(rowList != null && rowList.size() >0) {  
            list = new ArrayList();  
            for(Iterator iter = rowList.iterator();iter.hasNext();){    
                map = new HashMap<String, String>();  
                //获得具体的节点的父元素     
                Element element = (Element)iter.next();    
                //获得具体的节点的父元素的属性    
//              List elementList = element.attributes();    
//              for(Iterator iter1 = elementList.iterator();iter1.hasNext();){    
//                  //将每个属性转化为一个抽象属性，然后获取其名字和值    
//                  AbstractAttribute aa = (AbstractAttribute)iter1.next();    
//                  System.out.println("Name:"+aa.getName()+";Value:"+aa.getValue());    
//              }    
//              如果element下有子元素，(类似width="**")，要想获得该子元素的值，可以用如下方法    
//              System.out.println(element.elementText("test"));  
                  
                //获得父节点内的各种借点或者属性  
                Iterator it1 = element.elementIterator();  
                while(it1.hasNext()) {  
                    Element element1 = (Element)it1.next();    
                    //获得子节点的所有列表    
                    List elementList1 = element1.attributes();    
//                  System.out.println("name is " + element1.getName() + "123 is " + element1.getText());  
                    map.put(element1.getName(), element1.getText());  
//                  list.add(map);  
//                  for(Iterator it2 = elementList1.iterator();it2.hasNext();){    
//                      //将每个属性转化为一个抽象属性，然后获取其名字和值    
//                      AbstractAttribute aa = (AbstractAttribute)it2.next();    
////                        System.out.println("Name11:"+aa.getName()+";Value11:"+aa.getValue());    
//                      //这边需要添加借点的名字为KEY的值(重要)  
//                      map.put(element1.getName(), aa.getValue());  
//                  }  
                }  
            }  
        }     
        return map;  
    } 

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws IOException,DocumentException {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList){
			map.put(e.getName(), e.getText());
		}


		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}
	
	public static String getReportReqDataToXml(ReportReqData reportReqData) {
		xstream.alias("xml", reportReqData.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(reportReqData);
	}


	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}