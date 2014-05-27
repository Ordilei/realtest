package cc.mycode.realtest.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class SeleniumFactory {
	
	public static WebDriver driver;
	
	public static WebDriver getDriver() {
		if(driver==null){
			return new SeleniumFactory().newInstance(System.getProperty("selenium.browser"));
		}		
		return driver;
	}

	public WebDriver newInstance(String driver) {
		try {
			SeleniumStrategy strategy = availableDrivers.get(driver.toLowerCase());
			return strategy.create();
		} catch(Exception e) {
			throw new RuntimeException("The WebDriver can't be instanciated", e);
		}
	}
	
	private static Map<String, SeleniumStrategy> availableDrivers;
	
	static {
		availableDrivers = new HashMap<String, SeleniumStrategy>();
		availableDrivers.put("firefox", SeleniumStrategy.FIREFOX);
		availableDrivers.put("chrome", SeleniumStrategy.CHROME);
		availableDrivers.put("ie", SeleniumStrategy.IE);
		availableDrivers.put("htmlunit", SeleniumStrategy.HTMLUNIT);
	}
	
	private enum SeleniumStrategy {
		FIREFOX {
			@Override
			protected WebDriver create() {
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("browser.safebrowsing.malware.enabled", false);
				return new FirefoxDriver(profile);
			}
		},
		HTMLUNIT {
			@Override
			protected WebDriver create() {
				return new HtmlUnitDriver();
			}
		},
		CHROME {
			@Override
			protected WebDriver create() {
				String exe = System.getProperty("selenium.driver.chrome");
				if(StringUtils.isBlank(exe)) {
					throw new RuntimeException("Chrome driver not found");
				}
				System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, exe);
				return new ChromeDriver();
			}
		}, 
		IE {
			@Override
			protected WebDriver create() {
				String exe = System.getProperty("selenium.driver.ie");
				if(StringUtils.isBlank(exe)) {
					throw new RuntimeException("IE driver not found or incompatible operating system");
				}
				System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, exe);
				return new InternetExplorerDriver();
			}
		};
		
		protected abstract WebDriver create();

}
	}
