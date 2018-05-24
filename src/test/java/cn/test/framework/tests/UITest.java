package cn.test.framework.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.test.framework.action.LoginPageAction;
import com.test.framework.factory.DriverFactory;

public class UITest {
	public WebDriver driver;

	@BeforeClass
	public void init() {
		driver = DriverFactory.getInstance().getChromeDriver();
	}

	@Test(enabled = true)
	public void testLoginPage() {
		LoginPageAction.setDriver(driver);
		LoginPageAction.setXmlName("LoginPage.xml");
		LoginPageAction.login("admin", "111111");
	}

	@AfterTest
	public void stop() {
		if (driver != null) {
			driver.quit();
		}
	}

}
