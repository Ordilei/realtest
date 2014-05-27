package cc.mycode.realtest.core;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class TestCase {

	private static WebDriver driver;
	public static  Map<String,String> context;
	public static ErrorContext errorContext;
	
	public static <T> T startFrom(Class<T> pageClassToProxy) {
		return Screen.use(driver,pageClassToProxy,context,errorContext);
	}

	public static WebDriver getNativeDriver() {
		return driver;
	}

	@BeforeSuite(alwaysRun = true)
	public void startDriver() {
		driver = SeleniumFactory.getDriver();
	}

	@AfterSuite(alwaysRun = true)
	public static void stopDriver() {
		driver.quit();
	}

	@BeforeMethod(alwaysRun = true)
	public void clearContexts() {
		context=new HashMap<String,String>();
		errorContext=new ErrorContext();
	}


}