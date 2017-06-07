package com.st.core;


import com.st.core.handle.SpringContextHolder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;



/**
 * 
 * @author gaozl
 *
 */
@Component
public class TheadHelper {
	private ThreadPoolTaskExecutor taskExecutor;
	TheadHelper() {
		taskExecutor = SpringContextHolder.getBean("taskExecutor");
	}
	/**
	 * 获得线程池执行实例
	 * @author sunju
	 * @creationDate. 2012-8-23 下午06:54:55 
	 * @return 执行实例
	 */
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
}
