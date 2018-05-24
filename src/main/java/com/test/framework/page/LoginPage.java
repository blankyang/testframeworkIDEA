package com.test.framework.page;

import org.openqa.selenium.WebDriver;

import com.test.framework.domain.Locator;

public class LoginPage extends BasePage {

	WebDriver driver;

	Locator username = locators.get("username");
	Locator password = locators.get("password");
	Locator submit = locators.get("submit");
	Locator admin = locators.get("admin");

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public LoginPage(WebDriver driver, String xmlName) {
		super(driver, xmlName);
		driver.get("http://192.168.6.226:8080/rcp/portal/login/init");
	}

	public void inputUsername(String value) {
		input(username, value);
	}

	public void inputPassword(String value) {
		input(password, value);
	}

	public void submit() {
		click(submit);
	}
	
	public boolean isLogin(){
		return findElement(super.driver, admin).isDisplayed();
	}
}
