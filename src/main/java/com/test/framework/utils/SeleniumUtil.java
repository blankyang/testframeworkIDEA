package com.test.framework.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtil {
	private static Logger logger = LogManager.getLogger("SeleniumUtil");
	
	public static WebDriver driver;
	
	public static WebDriver initDriver(String browser,String path) {
		if ("chrome".equals(browser.toLowerCase())) {
			System.setProperty("webdriver.chrome.driver",path);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("start-maximized");
			options.addArguments("--js-flags=--expose-gc");  
			options.addArguments("--enable-precise-memory-info"); 
			options.addArguments("--disable-popup-blocking");
			options.addArguments("--disable-default-apps");
			options.addArguments("test-type=browser");
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);
		}
		return driver;
	}

	public static void Config(WebDriver driver,int waitTime, int loadTime) {
		// 元素隐式等待时间设置
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
		// 等待页面完全加载的超时时间设置
		driver.manage().timeouts().pageLoadTimeout(loadTime, TimeUnit.SECONDS);
		
	}


	// 判断是不是alert弹出框
	public static Boolean dealPotentialAlert(WebDriver driver, boolean option) {
		boolean flag = false;
		try {
			Alert alert = driver.switchTo().alert();
			if (null == alert)
				throw new NoAlertPresentException();
			try {
				if (option) {
					alert.accept();
					System.out.println("点击弹出框: " + alert.getText());
				} else {
					alert.dismiss();
					System.out.println("关闭弹出框: " + alert.getText());
				}
				flag = true;
			} catch (WebDriverException ex) {
				if (ex.getMessage().startsWith("Could not find")) {
					System.out.println("没发现弹出框!");
					logger.error("没发现弹出框!" + ex);
				} else {
					throw ex;
				}
			}
		} catch (NoAlertPresentException e) {
			System.out.println("没发现弹出框!");
		}
		return flag;
	}

	// 全屏截图
	public static void snapshotByAll(TakesScreenshot drivername, String filename) {
		String path = "E:\\selenium截图\\"
				+ new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		try {
			System.out.println("截图保存路径:" + path + "\\" + filename);
			FileUtils.copyFile(scrFile, new File(path + "\\" + filename));
		} catch (IOException e) {
			System.out.println("截图失败");
			logger.error("没发现弹出框!" + e);
			e.printStackTrace();
		}
	}

	// 根据元素位置局部截图
	public static void snapshotByPart(WebDriver driver, int[] locations,
			String filename) {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		BufferedImage originalImage;
		try {
			originalImage = ImageIO.read(new ByteArrayInputStream(
					((TakesScreenshot) augmentedDriver)
							.getScreenshotAs(OutputType.BYTES)));
			BufferedImage croppedImage = originalImage.getSubimage(
					locations[0], locations[1], locations[2], locations[3]);
			String path = "E:\\selenium截图\\"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ "\\" + filename;
			File imageFile = new File(path);
			if (!imageFile.exists())
				imageFile.mkdirs();
			ImageIO.write(croppedImage, "png", imageFile);
		} catch (WebDriverException e) {
			e.printStackTrace();
			System.out.println("截图失败");
			logger.error("没发现弹出框!" + e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("截图失败");
			logger.error("没发现弹出框!" + e);
		}
	}

	// 获取元素的坐标，宽度
	public static int[] getElementLocation(WebElement element) {
		int[] locations = new int[4];
		Point point = element.getLocation();
		int x = point.getX();
		int y = point.getY();
		Dimension size = element.getSize();
		int height = size.getHeight();
		int width = size.getWidth();
		locations[0] = x;
		locations[1] = y;
		locations[2] = width;
		locations[3] = height;
		return locations;

	}

	// 显式等待一个元素在指定时间里出现
	public static WebElement findWebElement(final By locator,
			int waitTime) {
		WebElement e = (new WebDriverWait(driver, waitTime))
				.until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						return d.findElement(locator);
					}
				});
		return e;
	}

	/**
	 * 判断元素是否在指定时间内存在。 只要元素出现在dom结构中（不管属性是显示还是隐藏） 马上返回true
	 * 在指定时间仍不存在与dom结构则返回false。 适用于ajax
	 * 
	 * @param by
	 *            元素
	 * @param seconds
	 *            指定秒数
	 * @return 出现返回true 否则返回false
	 */
//	public static boolean waitForElementPresence(final By by, int seconds) {
//		try {
//			new WebDriverWait(driver, seconds).until(ExpectedConditions
//					.presenceOfElementLocated(by));
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	/**
	 * 判断元素在指定时间是否显示 元素是否在指定时间内显示（存在dom结构且属性为显示）马上返回true 如果到指定时间仍未显示（不存在与dom结构
	 * 或者存在于dom结构但属性为‘隐藏’）则返回false 适用于ajax
	 * 
	 * @param by
	 *            元素
	 * @param seconds
	 *            指定秒数
	 * @return 出现返回true 否则返回false
	 */
//	public static boolean waitForElementVisible(final By by,
//			int seconds) {
//		try {
//			new WebDriverWait(driver, seconds).until(ExpectedConditions
//					.visibilityOfElementLocated(by));
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	/**
	 * 判断元素是否在指定时间内隐藏或者消失 如果元素消失(不存在于dom结构 或者属性为 ‘隐藏’)则立刻返回true
	 * 如果指定时间后元素仍然存在（存在于dom结构且属性为‘显示’）则返回false
	 * 
	 * @param by
	 *            元素
	 * @param seconds
	 *            秒数
	 * @return
	 */
//	public static boolean waitForElementInvisible(final By by, int seconds) {
//		try {
//			new WebDriverWait(driver, seconds).until(ExpectedConditions
//					.invisibilityOfElementLocated(by));
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	public static void scroll(String x, String y) {
		if (x.equals("left")) {
			x = "0";
		} else if (x.equals("right")) {
			x = "document.body.scrollWidth";
		} else if (x.equals("middle")) {
			x = "document.body.scrollWidth/2";
		}
		if (y.equals("top")) {
			y = "0";
		} else if (y.equals("buttom")) {
			y = "document.body.scrollHeight";
		} else if (y.equals("middle")) {
			y = "document.body.scrollHeight/2";
		}
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript(String.format("scroll(%s,%s);", x, y));
	}
	
	

}
