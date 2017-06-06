package com.st.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class InterfaceUtil {
	
	public static String getInterface(String urlPath, String mystr,String signature) throws IOException {
		mystr=mystr+"&signature="+signature;
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.getOutputStream().write(mystr.toString().getBytes());
		connection.getOutputStream().flush();
		connection.getOutputStream().close();
//		System.out.println(urlPath+mystr);
		InputStream inputStream = connection.getInputStream();

		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str);
		}
		reader.close();
		connection.disconnect();
//		System.out.println(sb.toString());
		return sb.toString();
	}
}