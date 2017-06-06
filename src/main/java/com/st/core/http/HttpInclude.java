/**
 * Copyright 2005-2012 insigmaedu.com
 * All rights reserved.
 * 
 * @project
 * @author Flouny.Caesar
 * @version 1.0
 * @date 2011-08-26
 */
package com.st.core.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <pre>
 * 完成于&lt;jsp:include page''/>相同的功能
 * 用于include其它页面以用于布局,可以用于在freemarker,velocity的servlet环境应用中直接include其它http请求
 * </pre>
 * 
 * <br />
 * <b>Freemarker及Velocity示例使用:</b>
 * <pre>
 * ${httpInclude.include("http://www.google.com")};
 * ${httpInclude.include("/servlet/head?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.jsp")};
 * ${httpInclude.include("/head.do?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.htm")};
 * </pre>
 * 
 * @author gaozhanglei
 *
 */
public class HttpInclude {
	
	private final static Logger LOG = LoggerFactory.getLogger(HttpInclude.class);
	
	private static String sessionIdKey = "JSESSIONID";
	
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public HttpInclude(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

	public String include(String includePath) {
		StringWriter sw = new StringWriter(8192);
		include(includePath,sw);
		
		return sw.toString();
    }
	
	public void include(String includePath, Writer writer) {
        try {
            if(isRemoteHttpRequest(includePath)) {
            	getRemoteContent(includePath,writer);
            } else {
            	getLocalContent(includePath,writer);
            }
        } catch (ServletException e) {
            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
        } catch (IOException e) {
            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
        }
    }
	
	private static boolean isRemoteHttpRequest(String includePath) {
		return includePath != null && (includePath.toLowerCase().startsWith("http://") || includePath.toLowerCase().startsWith("https://"));
    }

    private void getLocalContent(String includePath, Writer writer) throws ServletException, IOException {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
        
        CustomOutputHttpServletResponseWrapper customResponse = new CustomOutputHttpServletResponseWrapper(response,writer,outputStream);
        request.getRequestDispatcher(includePath).include(request, customResponse);
        
        customResponse.flushBuffer();
        if(customResponse.useOutputStream) {
        	writer.write(outputStream.toString(response.getCharacterEncoding()));
        }
        
        writer.flush();
    }
    
    private void getRemoteContent(String urlString,Writer writer) throws MalformedURLException, IOException {
        URL url = new URL(getWithSessionIdUrl(urlString));
		URLConnection conn = url.openConnection();
        setConnectionHeaders(urlString, conn);
        InputStream input = conn.getInputStream();
        try {
        	Reader reader = new InputStreamReader(input,Utils.getContentEncoding(conn,response));
        	Utils.copy(reader,writer);
        } finally {
        	if(input != null) input.close();
        }
        
        writer.flush();
    }

	private void setConnectionHeaders(String urlString, URLConnection conn) {
		conn.setReadTimeout(6000);
        conn.setConnectTimeout(6000);
        String cookie = getCookieString();
		conn.setRequestProperty("Cookie", cookie);
		if(LOG.isDebugEnabled()) {
			LOG.debug("request properties:"+conn.getRequestProperties() + " for url: " + urlString);
		}
	}
	
	private String getWithSessionIdUrl(String url) {
		
		return url;
	}

    private static final String SET_COOKIE_SEPARATOR=  ";";
    
	private String getCookieString() {
		StringBuffer sb = new StringBuffer(64);
		Cookie[] cookies = request.getCookies();
		if(cookies != null ) {
			for(Cookie c : cookies) {
				if(!sessionIdKey.equals(c.getName())) {
					sb.append(c.getName()).append("=").append(c.getValue()).append(SET_COOKIE_SEPARATOR);
				}
			}
		}
		
		String sessionId = Utils.getSessionId(request);
		if(sessionId != null) {
			sb.append(sessionIdKey).append("=").append(sessionId).append(SET_COOKIE_SEPARATOR);
		}
		
		return sb.toString();
	}
	
	public static class CustomOutputHttpServletResponseWrapper extends HttpServletResponseWrapper {
		private boolean useWriter = false;
		private boolean useOutputStream = false;
        private PrintWriter printWriter;
        private ServletOutputStream servletOutputStream;
        
        public CustomOutputHttpServletResponseWrapper(HttpServletResponse response, final Writer customWriter, final OutputStream customOutputStream) {
            super(response);
            this.printWriter = new PrintWriter(customWriter);
            this.servletOutputStream = new ServletOutputStream() {
            	public void write(int b) throws IOException {
                    customOutputStream.write(b);
                }
                
                public void write(byte[] b) throws IOException {
                    customOutputStream.write(b);
                }
                
                public void write(byte[] b, int off, int len)throws IOException {
                    customOutputStream.write(b, off, len);
                }
            };
        }
        
        public PrintWriter getWriter() throws IOException {
            if(useOutputStream) throw new IllegalStateException("getOutputStream() has already been called for this response");
            useWriter = true;
            
            return printWriter;
        }
        
        public ServletOutputStream getOutputStream() throws IOException {
            if(useWriter) throw new IllegalStateException("getWriter() has already been called for this response");
            useOutputStream = true;
            
            return servletOutputStream;
        }
        
        public void flushBuffer() throws IOException {
            if(useWriter) printWriter.flush();
            if(useOutputStream) servletOutputStream.flush();
        }
	}

    static class Utils { 
    	static String getContentEncoding(URLConnection conn, HttpServletResponse response) {
    		String contentEncoding = conn.getContentEncoding();
            if(conn.getContentEncoding() == null) {
            	contentEncoding = parseContentTypeForCharset(conn.getContentType());
            	if(contentEncoding == null) {
            		contentEncoding =  response.getCharacterEncoding();
            	}
            } else {
            	contentEncoding = conn.getContentEncoding();
            }
            
    		return contentEncoding;
    	}
    	
    	static Pattern p = Pattern.compile("(charset=)(.*)", Pattern.CASE_INSENSITIVE);
        private static String parseContentTypeForCharset(String contentType) {
        	if(contentType == null) return null;
        	Matcher m = p.matcher(contentType);
        	if(m.find()) {
        		return m.group(2).trim();
        	}
        	
			return null;
		}

		private static void copy(Reader in, Writer out) throws IOException {
            char[] buff = new char[8192];
            while(in.read(buff) >= 0) {
                out.write(buff);
            }
        }
        
    	private static String getSessionId(HttpServletRequest request) {
    		HttpSession session = request.getSession(false);
    		if(session == null) {
    			return null;
    		}
    		
    		return session.getId();
    	}
    }
}