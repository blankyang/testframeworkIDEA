package com.test.framework.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONObject;

public class HttpUtil {

	public static String byGet(String path, Map<String, String> headers) {
		try {
			URL url = new URL(path.trim());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			if (headers != null) {
				// 设置请求头
				for (Map.Entry<String, String> en : headers.entrySet()) {
					conn.setRequestProperty(en.getKey(), en.getValue());
				}
			}
			conn.connect();
			if (200 == conn.getResponseCode()) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				baos.close();
				is.close();
				conn.disconnect();
				return baos.toString("utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "请求炸了";
	}

	public static String byPost(String path, Map<String, String> headers,String params) {
		try {
			URL url = new URL(path.trim());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			if (headers != null) {
				// 设置请求头
				for (Map.Entry<String, String> en : headers.entrySet()) {
					conn.setRequestProperty(en.getKey(), en.getValue());
				}
			}
			conn.connect();
			PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
			printWriter.write(params);// post的参数 xx=xx&yy=yy
			printWriter.flush();
			printWriter.close();
			if (200 == conn.getResponseCode()) {
				BufferedInputStream bis = new BufferedInputStream(
						conn.getInputStream());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = bis.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				baos.close();
				bis.close();
				conn.disconnect();
				return baos.toString("utf-8");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "请求炸了";
	}

	public static String byJson(String path, Map<String, String> headers,
			JSONObject json) {
		try {
			URL url = new URL(path.trim());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			if (headers != null) {
				// 设置请求头
				for (Map.Entry<String, String> en : headers.entrySet()) {
					conn.setRequestProperty(en.getKey(), en.getValue());
				}
			}
			conn.connect();
			PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
			printWriter.write(json.toString());
			printWriter.flush();
			printWriter.close();
			if (200 == conn.getResponseCode()) {
				BufferedInputStream bis = new BufferedInputStream(
						conn.getInputStream());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = bis.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				baos.close();
				bis.close();
				conn.disconnect();
				return baos.toString("utf-8");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "请求炸了";
	}

	public static String byFilePost(String path, Map<String, String> params,
			Map<String, File> files, Map<String, String> headers) {
		String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"; // 定义数据分隔线
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			// if(headers != null && !headers.isEmpty()){
			// for(Map.Entry<String, String> entry : headers.entrySet()){
			// connection.setRequestProperty(entry.getKey(), entry.getValue());
			// }
			//
			// }
			OutputStream out = new DataOutputStream(
					connection.getOutputStream());
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			// 文件
			if (files != null && !files.isEmpty()) {
				for (Map.Entry<String, File> entry : files.entrySet()) {
					File file = entry.getValue();
					String fileName = entry.getKey();

					StringBuilder sb = new StringBuilder();
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data;name=\""
							+ fileName + "\";filename=\"" + file.getName()
							+ "\"\r\n");
					sb.append("Content-Type: image/jpg\r\n\r\n");
					byte[] data = sb.toString().getBytes();
					out.write(data);
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
					in.close();
				}
			}
			// 数据参数
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"");
					sb.append("\r\n");
					sb.append("\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
					byte[] data = sb.toString().getBytes();
					out.write(data);
				}
			}
			out.write(end_data);
			out.flush();
			out.close();
			if (connection.getResponseCode() == 200) {
				InputStream inStream = connection.getInputStream();
				byte[] number = read(inStream);
				String json = new String(number);
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "请求炸了";

	}

	private static byte[] read(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outStream.toByteArray();
	}

}
