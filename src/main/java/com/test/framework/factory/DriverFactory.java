package com.test.framework.factory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.test.framework.utils.ConfigUtils;
import com.test.framework.utils.Context.BrowserType;
import com.test.framework.utils.Log;

public class DriverFactory {
	static Log log = new Log(DriverFactory.class);
	private static String chromedriver;
	private static String fireBug;
	private static String firefoxdriver;
	private static Properties p = null;
	private static String IEDriverServer;
	private static String EDGEDriver;
	private static String OSType = System.getProperty("os.name");
	private static String config = System.getProperty("user.dir")
			+ "/config.properties";
	private static String currentDir = System.getProperty("user.dir");
	static WebDriver driver = null;
	public static DriverFactory driverfactory;

	public static DriverFactory getInstance() {
		if (driverfactory == null) {
			synchronized (DriverFactory.class) {
				driverfactory = new DriverFactory();
			}
		}
		return driverfactory;
	}

	public BrowserType getBrowserType() {

		if (driver instanceof FirefoxDriver) {
			return BrowserType.firefox;
		} else if (driver instanceof ChromeDriver) {
			return BrowserType.chrome;
		} else if (driver instanceof InternetExplorerDriver) {
			return BrowserType.ie;
		} else if (driver instanceof EdgeDriver) {
			return BrowserType.edge;
		} else if (driver instanceof SafariDriver) {
			return BrowserType.safari;
		} else {
			return null;
		}

	}

	public WebDriver getChromeDriver() {
		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		if (p != null) {
			if (!OSType.contains("Mac")) {
				chromedriver = p.getProperty("chromedriver");
			} else {
				chromedriver = p.getProperty("MAC_chromedriver");
			}
			if(OSType.contains("Linux") || OSType.contains("CentOS")){
				chromedriver = p.getProperty("linux_chromedriver");
			}

		}
		String path = System.getProperty("user.dir") + "/" + chromedriver;
		System.setProperty("webdriver.chrome.driver", path);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("test-type");
		options.addArguments("start-maximized");
		options.addArguments("--js-flags=--expose-gc");
		options.addArguments("--enable-precise-memory-info");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-default-apps");
		options.addArguments("test-type=browser");
		options.addArguments("disable-infobars");
		// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		// capabilities.setCapability("chrome.switches",Arrays.asList("--start-maximized"));
		driver = new ChromeDriver(options);
		// 元素隐式等待时间设置
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// 等待页面完全加载的超时时间设置
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getFirefoxDriver() {
		try {
			p = ConfigUtils.getProperties(config);
			WindowsUtils.killByName("firefox");

		} catch (Exception e) {
			log.error("can not find firefox process");
		}

		if (p != null) {
			fireBug = p.getProperty("fireBug");
			if (!OSType.contains("Mac")) {
				firefoxdriver = p.getProperty("firefoxdriver");
			} else {
				firefoxdriver = p.getProperty("MAC_firefoxdriver");
			}

		}
		firefoxdriver = currentDir + "/" + firefoxdriver;
		log.info("firefox geckodriver path is " + firefoxdriver);
		System.setProperty("webdriver.gecko.driver", firefoxdriver);
		File file = new File(fireBug);
		FirefoxProfile profile = new FirefoxProfile();

		try {
			profile.addExtension(file);
			profile.setPreference("extensions.firebug.currentVersion", "2.0.17");
			profile.setPreference("extensions.firebug.allPagesActivation",
					"off");
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		profile.setPreference(
				"browser.helperApps.neverAsk.saveToDisk",
				"application/octet-stream, application/vnd.ms-excel, text/csv, application/zip,application/exe");
		driver = new FirefoxDriver();

		log.info("Create FirefoxDriver ");
		return driver;

	}

	public synchronized WebDriver getIEDriver() {
		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p != null) {
			IEDriverServer = p.getProperty("IEDriverServer");
		}
		System.setProperty("webdriver.ie.driver", IEDriverServer);
		String PROXY = "http://proxy:8083";
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);

		DesiredCapabilities ds = DesiredCapabilities.internetExplorer();
		ds.setCapability(
				InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				true);
		ds.setCapability("ignoreProtectedModeSettings", true);
		ds.setCapability(CapabilityType.PROXY, proxy);
		driver = new InternetExplorerDriver(ds);
		return driver;
	}

	public WebDriver getEDGEDriver() {
		try {
			p = ConfigUtils.getProperties(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p != null) {
			EDGEDriver = p.getProperty("EDGEDriver");
		}
		System.setProperty("webdriver.edge.driver", EDGEDriver);
		String PROXY = "https://raw.githubusercontent.com/seveniruby/gfwlist2pac/master/test/proxy.pac";
		Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
		DesiredCapabilities capabilities = DesiredCapabilities.edge();
		EdgeOptions options = new EdgeOptions();
		options.setPageLoadStrategy("normal");
		capabilities.setCapability(EdgeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		driver = new EdgeDriver(capabilities);
		return driver;
	}

}
