package cn.test.framework.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.test.framework.utils.SeleniumUtil;

public class TestSelenium {
	
	WebDriver driver;

	@Test(enabled = false)
	public void test1() throws Exception {
		driver = SeleniumUtil.initDriver("chrome",
				"D:\\Program Files (x86)\\Chrome\\chromedriver.exe");

		SeleniumUtil.Config(driver, 8, 8);
		driver.get("http://192.168.6.226:8080/rcp/portal/login/init");
		login();
		// 获取左边功能栏
		List<WebElement> aList = driver.findElements(By.tagName("a"));
		for (int i = 0; i < aList.size(); i++) {
			String txt = aList.get(i).getText();
			if ("案件信息录入".equals(txt)) {
				aList.get(i).click();
				// 案件信息录入
				caseInfoInput();
			}
		}
	}
	
	private void login() {
		WebElement window = driver.findElement(By.id("login_window"));
		WebElement form = window.findElement(By.id("login_form"));
		form.findElement(By.name("username")).sendKeys("test");
		form.findElement(By.name("password")).sendKeys("111111");
		WebElement submit = form.findElement(By.name("submit"));
		submit.click();
	}

	private void caseInfoInput() {
		WebElement apply_name = driver.findElement(By.name("apply_name"));
		WebElement apply_idCard = driver.findElement(By.name("apply_idCard"));
		WebElement apply_phone = driver.findElement(By.name("apply_phone"));
		WebElement contacter1_name = driver.findElement(By
				.name("contacter1_name"));
		WebElement contacter2_name = driver.findElement(By
				.name("contacter2_name"));
		WebElement contacter1_phone = driver.findElement(By
				.name("contacter1_phone"));
		WebElement contacter2_phone = driver.findElement(By
				.name("contacter2_phone"));
		WebElement serve_psw = driver.findElement(By.name("serve_psw"));
		Select sel1 = new Select(driver.findElement(By
				.id("contacter1_relation_id")));
		Select sel2 = new Select(driver.findElement(By
				.id("contacter2_relation_id")));

		// 设置select选项可见
		((JavascriptExecutor) driver)
				.executeScript("document.querySelectorAll('select')[0].style.display='block';");
		((JavascriptExecutor) driver)
				.executeScript("document.querySelectorAll('select')[1].style.display='block';");
		apply_name.sendKeys("123");
		apply_idCard.sendKeys("123");
		apply_phone.sendKeys("123");
		contacter1_name.sendKeys("123");
		contacter2_name.sendKeys("123");
		sel1.selectByValue("同学");
		sel2.selectByValue("朋友");
		contacter1_phone.sendKeys("123");
		contacter2_phone.sendKeys("123");
		serve_psw.sendKeys("123");
	}

}
