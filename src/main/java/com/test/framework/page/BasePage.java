package com.test.framework.page;

import java.util.HashMap;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.framework.domain.Locator;
import com.test.framework.utils.Log;
import com.test.framework.utils.XMLUtils;

public class BasePage {
	protected Log log = new Log(this.getClass());

	protected HashMap<String, Locator> locators;

	protected WebDriver driver;

	protected String xmlName;

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	
	protected String path = System.getProperty("user.dir")
			+ "/src/main/java/com/test/framework/page/";

	public BasePage(WebDriver driver, String xmlName) {
		super();
		this.driver = driver;
		this.xmlName = xmlName;
		path += xmlName;
		locators = XMLUtils.readXMLDocument(path, xmlName.substring(0, xmlName.indexOf(".")));
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	protected void input(Locator locator, String value) {
		WebElement e = findElement(driver, locator);
		log.info("type value is:  " + value);
		e.sendKeys(value);
	}

	protected void click(Locator locator) {
		WebElement e = findElement(driver, locator);
		log.info("click button" + e);
		e.click();
	}

	protected void select(Locator locator, String value) {
		WebElement e = findElement(driver, locator);
		Select select = new Select(e);

		try {
			log.info("select by Value " + value);
			select.selectByValue(value);
		} catch (Exception notByValue) {
			log.info("select by VisibleText " + value);
			select.selectByVisibleText(value);
		}
	}

	protected void alertConfirm() {
		Alert alert = driver.switchTo().alert();
		try {
			alert.accept();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	protected void alertDismiss() {
		Alert alert = driver.switchTo().alert();
		try {
			alert.dismiss();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	protected String getAlertText() {
		Alert alert = driver.switchTo().alert();
		try {
			return alert.getText();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}

	protected void clickAndHold(Locator locator) {
		WebElement e = findElement(driver, locator);
		Actions actions = new Actions(driver);
		actions.clickAndHold(e).perform();
	}

	protected void move(Locator locator) {
		WebElement e = findElement(driver, locator);
		Actions actions = new Actions(driver);
		actions.moveToElement(e).perform();
	}

	public WebElement findElement(WebDriver driver, final Locator locator) {
		int timeOut = 0;
		if (locator != null) {
			try {
				timeOut = locator.getWaitSec();
				if(timeOut == 0 || timeOut <0){
					timeOut = 8;
				}
			} catch (NullPointerException e) {
				log.error("can't get Default time out "
						+ locator.getElementExp());
			}

			WebElement element = (new WebDriverWait(driver, timeOut))
					.until(new ExpectedCondition<WebElement>() {

						@Override
						public WebElement apply(WebDriver driver) {
							try {
								return getElement(locator);
							} catch (Exception e) {
								log.error("can't find element "
										+ locator.getElementExp());
								return null;
							}

						}

					});
			return element;

		} else {
			return null;
		}

	}

	public WebElement getElement(Locator sourceLocator) {
		Locator locator = getLocator(sourceLocator.getElementExp());
		if (locator == null) {
			locator = new Locator(sourceLocator.getElementExp(),
					sourceLocator.getWaitSec(), sourceLocator.getByType());
		}
		WebElement e;
		switch (locator.getByType()) {
		case xpath:
			log.debug("find element By xpath");
			e = driver.findElement(By.xpath(locator.getElementExp()));
			break;
		case id:
			log.debug("find element By id");
			e = driver.findElement(By.id(locator.getElementExp()));
			break;
		case name:
			log.debug("find element By name");
			e = driver.findElement(By.name(locator.getElementExp()));
			break;
		case cssSelector:
			log.debug("find element By cssSelector");
			e = driver.findElement(By.cssSelector(locator.getElementExp()));
			break;
		case className:
			log.debug("find element By className");
			e = driver.findElement(By.className(locator.getElementExp()));
			break;
		case tagName:
			log.debug("find element By tagName");
			e = driver.findElement(By.tagName(locator.getElementExp()));
			break;
		case linkText:
			log.debug("find element By linkText");
			e = driver.findElement(By.linkText(locator.getElementExp()));
			break;
		case partialLinkText:
			log.debug("find element By partialLinkText");
			e = driver.findElement(By.partialLinkText(locator.getElementExp()));
			break;
		default:
			e = driver.findElement(By.id(locator.getElementExp()));
		}
		return e;
	}

	public boolean isElementPresent(Locator myLocator, int timeOut) {

		final Locator locator = getLocator(myLocator.getElementExp());
		boolean isPresent = false;
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebElement e = wait.until(new ExpectedCondition<WebElement>() {

			@Override
			public WebElement apply(WebDriver d) {
				try {
					return findElement(driver, locator);
				} catch (NoSuchElementException e) {
					return null;
				}
			}
		});
		if (e != null) {
			isPresent = e.isDisplayed();
		}

		return isPresent;
	}

	public Locator getLocator(String locatorName) {

		Locator locator = new Locator(locatorName);

		if (locators != null) {
			locator = locators.get(locatorName);
		} else {
			locator = new Locator(locatorName);
		}
		return locator;
	}

}
