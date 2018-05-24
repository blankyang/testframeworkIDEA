package com.test.framework.action;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.test.framework.page.LoginPage;

public class LoginPageAction {

	public static WebDriver driver;

	public static LoginPage lp;

	public static String xmlName;

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		LoginPageAction.driver = driver;
	}

	public static String getXmlName() {
		return xmlName;
	}

	public static void setXmlName(String xmlName) {
		LoginPageAction.xmlName = xmlName;
	}
	
	public static void login(String username,String password){
		lp = new LoginPage(getDriver(),getXmlName());
		lp.inputUsername(username);
		lp.inputPassword(password);
		lp.submit();
		Assert.assertTrue(lp.isLogin(),"登录失败");

		
	}
	
	
}
