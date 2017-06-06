/*
 * @(#)Result.java 2013-12-8下午03:42:02
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.core.javabean;

import java.io.Serializable;

/**
 * 结果
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2013-12-8下午03:42:02]sunju<br/>
 * </p>
 * </li>
 * </ul>
 */

public class Result implements Serializable {

	/**
	 * serialVersionUID:（用一句话描述这个变量表示什么）
	 *
	 * @since v 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	/**
	 * 是否成功：默认为true
	 */
	private boolean success = true;
	/**
	 * 附带对象
	 */
	private Object object;
	/**
	 * 消息
	 */
	private String msg;
	
	private String msg1;
	private String msg2;
	
	
	
	
	public String getMsg1() {
		return msg1;
	}

	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}

	public String getMsg2() {
		return msg2;
	}

	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}

	/**
	 * 创建一个新的实例Result.
	 *
	 */
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 创建一个新的实例Result.
	 *
	 * @param object
	 */
	public Result(Object object) {
		super();
		this.object = object;
	}
	
	/**
	 * 创建一个新的实例Result.
	 *
	 * @param object
	 * @param msg
	 */
	public Result(Object object, String msg) {
		super();
		this.object = object;
		this.msg = msg;
	}

	/**
	 * 创建一个新的实例Result.
	 *
	 * @param success
	 * @param object
	 * @param msg
	 */
	public Result(boolean success, Object object, String msg) {
		super();
		this.success = success;
		this.object = object;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return this.success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return this.msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObject() {
		return this.object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
