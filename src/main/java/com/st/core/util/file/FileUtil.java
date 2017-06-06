/*
 * @(#)FileUtil.java 2013-12-10下午12:05:08
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.st.core.util.file;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件工具类
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 *gaozl<br/>
 * </p>
 * </li>
 * </ul>
 */

public class FileUtil {
	/**
	 * 上传
	 * @author sunju
	 * @creationDate. 2013-12-10 下午02:38:52 
	 * @param uploadDir 上传目录
	 * @param file 文件
	 * @param destFileName 目标文件名
	 * @return 上传的文件路径
	 * @throws IOException
	 */
	public static String upload(String uploadDir, MultipartFile file, String destFileName) throws IOException {
		// 获得目标目录
		String destDir = _getDestDir(uploadDir);
		
		// 上传文件全路径
		File destFile = new File(destDir + "/" + destFileName);
		
		// 写入硬盘
		FileCopyUtils.copy(file.getBytes(), destFile);
		
		// 返回文件相对路径
		return uploadDir + "/" + destFileName;
	}
	
	/**
	 * 删除文件
	 * @author sunju
	 * @creationDate. 2013-12-11 上午11:53:34 
	 * @param filePath 文件相对路径
	 * @return 布尔
	 * @throws IOException
	 */
	public static boolean deleteFile(String filePath) throws IOException {
		boolean bool = false;
		File file = new File(_getRootFolderDiskPath() + filePath);
		if (!file.exists()) return false;
		if (file.isFile())file.delete();
		return bool;
	}
	
	/**
	 * 获得年月日格式上传目录（上传目录 + 年/月/日）
	 * @author sunju
	 * @creationDate. 2013-12-19 上午09:39:42 
	 * @param uploadDir 上传目录
	 * @return 目标目录
	 */
	public static String getYmdUploadDir(String uploadDir) {
		return uploadDir + "/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	}
	
	/**
	 * 获得目标文件名称（年月日+系统纳秒数+源文件后缀）
	 * @author sunju
	 * @creationDate. 2013-12-10 下午03:08:54 
	 * @param srcFileName 源文件名称
	 * @return 文件名称
	 */
	public static String getDestFileName(String srcFileName) {
		String prefix = srcFileName.substring(srcFileName.lastIndexOf(".") + 1);
		return System.nanoTime() + "." + prefix;
	}
	
	/**
	 * 获得真实路径
	 * @author sunju
	 * @creationDate. 2013-12-17 下午11:26:10 
	 * @param uploadDir 上传目录
	 * @return 硬盘真实目录
	 */
	public static String getRealPath(String uploadDir) {
		return _getRootFolderDiskPath() + uploadDir;
	}
	
	/**
	 * 获得目标目录，上传目录不存在即创建
	 * @author sunju
	 * @creationDate. 2013-12-10 下午04:22:07 
	 * @param uploadDir 上传目录
	 * @return 目标目录
	 */
	private static String _getDestDir(String uploadDir) {
		String destDir = getRealPath(uploadDir);
		File dest = new File(destDir);
	    if (!dest.exists()) dest.mkdirs();
	    return destDir;
	}
	
	// 获得根目录的硬盘目录
	private static String _getRootFolderDiskPath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request.getSession().getServletContext().getRealPath("") + "/";
	}
	
}
