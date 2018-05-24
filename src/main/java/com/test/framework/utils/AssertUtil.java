package com.test.framework.utils;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class AssertUtil {
	public static boolean flag = true;

	public static List<Error> errors = new ArrayList<Error>();

	public static void verifyEquals(Object actual, Object expected) {
		try {
			Assert.assertEquals(actual, expected);
		} catch (Error e) {
			errors.add(e);
			flag = false;
		}
	}

	public static void verifyEquals(Object actual, Object expected,
			String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Error e) {
			errors.add(e);
			flag = false;
		}
	}
	public static void verifyFail(String message) {
		try {
			Assert.fail(message);
		} catch (Error e) {
			errors.add(e);
			Assert.fail(message,e);
			flag = false;
		}
	}
	public static void verifyPass() {
		try {
			Assert.assertTrue(true);
		} catch (Error e) {
			errors.add(e);
			Assert.fail(e.toString());
			flag = false;
		}
	}

}
