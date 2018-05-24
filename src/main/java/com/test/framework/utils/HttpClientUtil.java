package com.test.framework.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class HttpClientUtil {

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getExcelData(String SheetName)
			throws Exception {
		List<Map<String, String>> params = new ArrayList<Map<String, String>>();
		Gson gson = new Gson();
		Object[][] array = null;
		List<Map<String, String>> sonList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> exList = ReadExcelUtil.readXlsx(
				"data/dataProvider.xls", SheetName);
		for (int i = 0; i < exList.size(); i++) {
			Map<String, String> map = (Map<String, String>) exList.get(i);
			sonList.add(map);
		}
		if (sonList.size() > 0) {
			array = new Object[sonList.size()][];
			for (int i = 0; i < sonList.size(); i++) {
				array[i] = new Object[] { sonList.get(i) };
			}
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				Map<String, String> mapp = new HashMap<String, String>();
				System.out.println("第" + (i + 1) + ":" + array[i][j]);
				mapp = gson.fromJson(JSONObject.toJSONString(array[i][j]),
						mapp.getClass());
				params.add(mapp);
			}
		}
		System.out.println("总数：" + params.size());
		return params;

	}

	public static String sendGet(String path, Map<String, String> headers,
			Map<String, String> cookies) {
		HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
		CookieStore cookieStore = new BasicCookieStore();
		if (cookies != null && cookies.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(en.getKey(),
						en.getValue());
				cookieStore.addCookie(cookie);
			}
		}
		CloseableHttpClient httpCilent2 = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy)
				.setConnectTimeout(5000) // 设置连接超时时间
				.setConnectionRequestTimeout(5000) // 设置请求超时时间
				.setSocketTimeout(5000).setRedirectsEnabled(true)// 默认允许自动重定向
				.build();
		HttpGet httpGet2 = new HttpGet(path);
		httpGet2.setConfig(requestConfig);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				httpGet2.setHeader(en.getKey(), en.getValue());
			}
		}

		String srtResult = "";
		try {
			HttpResponse httpResponse = httpCilent2.execute(httpGet2);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				srtResult = EntityUtils.toString(httpResponse.getEntity());// 获得返回的结果
				return srtResult;
			} else {
				return "Error Response: "
						+ httpResponse.getStatusLine().toString()
						+ EntityUtils.toString(httpResponse.getEntity());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "post failure :caused by-->" + e.getMessage().toString();
		} finally {
			try {
				if (null != httpCilent2) {
					httpCilent2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String sendPost4Map(String url,
			Map<String, String> paramsMap, Map<String, String> headers,
			Map<String, String> cookies) {
		HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
		CookieStore cookieStore = new BasicCookieStore();
		if (cookies != null && cookies.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(en.getKey(),
						en.getValue());
				cookieStore.addCookie(cookie);
			}
		}
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(180 * 1000)
				.setConnectionRequestTimeout(180 * 1000).setProxy(proxy)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				httpPost.setHeader(en.getKey(), en.getValue());
			}
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : paramsMap.keySet()) {
			nvps.add(new BasicNameValuePair(key, String.valueOf(paramsMap
					.get(key))));
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String strResult = "";
			if (response.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(response.getEntity());
				return strResult;
			} else {
				return "Error Response: " + response.getStatusLine().toString()
						+ EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "post failure :caused by-->" + e.getMessage().toString();
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String sendPost4Json(String url, String jsonParams,
			Map<String, String> headers, Map<String, String> cookies) {
		HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
		CookieStore cookieStore = new BasicCookieStore();
		if (cookies != null && cookies.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(en.getKey(),
						en.getValue());
				cookieStore.addCookie(cookie);
			}
		}
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(180 * 1000).setProxy(proxy)
				.setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				httpPost.setHeader(en.getKey(), en.getValue());
			}
		}
		try {
			httpPost.setEntity(new StringEntity(jsonParams, ContentType.create(
					"application/json", "utf-8")));
			HttpResponse response = httpClient.execute(httpPost);
			String strResult = "";
			if (response.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(response.getEntity());
				return strResult;
			} else {
				return "Error Response: " + response.getStatusLine().toString()
						+ EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "post failure :caused by-->" + e.getMessage().toString();
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String sendPost4File(String serverUrl,
			Map<String, File> files, Map<String, String> params,
			Map<String, String> headers, Map<String, String> cookies) {
		HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
		CookieStore cookieStore = new BasicCookieStore();
		if (cookies != null && cookies.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(en.getKey(),
						en.getValue());
				cookieStore.addCookie(cookie);
			}
		}
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		HttpPost httpPost = new HttpPost(serverUrl);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(180 * 1000).setProxy(proxy)
				.setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				httpPost.setHeader(en.getKey(), en.getValue());
			}
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		// 上传的文件
		if (files != null && files.size() > 0) {
			try {
				for (Map.Entry<String, File> entry : files.entrySet()) {
					String fileName = entry.getValue().toPath().getFileName()
							.toString();
					builder.addBinaryBody(entry.getKey(), entry.getValue(),
							ContentType.MULTIPART_FORM_DATA
									.withCharset("UTF-8"),
							new String(fileName.getBytes("UTF-8")));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 设置其他参数
		for (Entry<String, String> entry : params.entrySet()) {
			builder.addTextBody(entry.getKey(), entry.getValue(),
					ContentType.TEXT_PLAIN.withCharset("UTF-8"));
		}
		HttpEntity httpEntity = builder.build();
		try {

			httpPost.setEntity(httpEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			} else {
				return "Error Response: " + response.getStatusLine().toString()
						+ EntityUtils.toString(response.getEntity());

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "post failure :caused by-->" + e.getMessage().toString();
		} finally {
			if (httpClient != null)
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static String sendPost4FileArray(String url,
			Map<String, File[]> files, Map<String, String> params,
			Map<String, String> headers, Map<String, String> cookies) {
		HttpPost httpPost = new HttpPost(url);
		HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
		CookieStore cookieStore = new BasicCookieStore();
		if (cookies != null && cookies.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(en.getKey(),
						en.getValue());
				cookieStore.addCookie(cookie);
			}
		}
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(180 * 1000).setProxy(proxy)
				.setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> en : headers.entrySet()) {
				httpPost.setHeader(en.getKey(), en.getValue());
			}
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		// 上传的文件
		if (files != null && files.size() > 0) {
			File[] fileArray;
			try {
				for (Map.Entry<String, File[]> entry : files.entrySet()) {
					fileArray = entry.getValue();
					for (int i = 0; i < fileArray.length; i++) {
						String fileName = fileArray[i].toPath().getFileName()
								.toString();
						builder.addBinaryBody(entry.getKey(),
								entry.getValue()[i],
								ContentType.MULTIPART_FORM_DATA
										.withCharset("UTF-8"), new String(
										fileName.getBytes("UTF-8")));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置其他参数
		for (Entry<String, String> entry : params.entrySet()) {
			builder.addTextBody(entry.getKey(), entry.getValue(),
					ContentType.TEXT_PLAIN.withCharset("UTF-8"));
		}
		HttpEntity httpEntity = builder.build();
		try {

			httpPost.setEntity(httpEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			} else {
				return "Error Response: " + response.getStatusLine().toString()
						+ EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "post failure :caused by-->" + e.getMessage().toString();
		} finally {
			if (httpClient != null)
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

}
