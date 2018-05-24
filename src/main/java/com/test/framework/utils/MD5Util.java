package com.test.framework.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * 
 * @author blank
 *
 */
public class MD5Util {

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * MD5加密 生成32位md5码
	 * 
	 * @param intStr
	 *            待加密字符串
	 * @return 返回32位md5码
	 * @throws Exception
	 */
	public static String md5Encode(String intStr) throws Exception {
		MessageDigest md5 = null;
		try {
			// 创建md5对象
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		// 将传入的字符串转换为UTF-8字节数组
		byte[] bytes = intStr.getBytes("UTF-8");
		// 将字节数组转化为md5字节数组
		byte[] md5Bytes = md5.digest(bytes);
		StringBuilder hexValue = new StringBuilder();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	/**
	 * SHA加密 生成40位SHA码
	 * 
	 * @param str
	 *            待加密字符串
	 * @return 返回40位SHA码
	 * @throws IOException
	 */
	public static String shaEncode(String str) throws IOException {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		byte[] bytes = str.getBytes("UTF-8");
		byte[] shaBytes = sha.digest(bytes);
		StringBuilder hexValue = new StringBuilder();
		for (int i = 0; i < shaBytes.length; i++) {
			int val = ((int) shaBytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	public static String toMd5(String s) {
		char[] str;
		try {
			byte[] btInput = s.getBytes("utf-8");
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "md5炸了";

	}

	public static String getMD5(String inStr) {
		byte[] inStrBytes = inStr.getBytes();
		try {
			MessageDigest MD = MessageDigest.getInstance("MD5");
			MD.update(inStrBytes);
			byte[] mdByte = MD.digest();
			char[] str = new char[mdByte.length * 2];
			int k = 0;
			for (int i = 0; i < mdByte.length; i++) {
				byte temp = mdByte[i];
				str[k++] = hexDigits[temp >>> 4 & 0xf];
				str[k++] = hexDigits[temp & 0xf];
			}
			return new String(str).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
