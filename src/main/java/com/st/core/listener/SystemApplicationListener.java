package com.st.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
/**
 * 
 * @author dzr
 *  启动的时候加载系统配置到application里面去
 */
public class SystemApplicationListener  extends ContextLoaderListener{
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		 	super.contextInitialized(event);
//		 	logger.info("项目启动的时候加载伪静态缓存...");
//            PseudoStaticUrl.getInstance();
	}
	


}
