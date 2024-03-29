package cc.mycode.realtest.core;

import java.lang.reflect.Field;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;


public class ProxyWrapper {

	private static Map<String,String> contextTemp;
	private static ErrorContext errorContextTemp;

	/**
	 * Instantiate an instance of the given class, and set a lazy proxy for each of the WebElement
	 * and List<WebElement> fields that have been declared, assuming that the field name is also
	 * the HTML element's "id" or "name". This means that for the class:
	 *
	 * <code>
	 * public class Page {
	 *     private WebElement submit;
	 * }
	 * </code>
	 *
	 * there will be an element that can be located using the xpath expression "//*[@id='submit']" or
	 * "//*[@name='submit']"
	 *
	 * By default, the element or the list is looked up each and every time a method is called upon it.
	 * To change this behaviour, simply annotate the field with the {@link CacheLookup}.
	 * To change how the element is located, use the {@link FindBy} annotation.
	 *
	 * This method will attempt to instantiate the class given to it, preferably using a constructor
	 * which takes a WebDriver instance as its only argument or falling back on a no-arg constructor.
	 * An exception will be thrown if the class cannot be instantiated.
	 *
	 * @see FindBy
	 * @see CacheLookup
	 * @param driver The driver that will be used to look up the elements
	 * @param pageClassToProxy A class which will be initialised.
	 * @return An instantiated instance of the class with WebElement and List<WebElement> fields proxied
	 */
	public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy,Map<String,String> context,ErrorContext errorContext) {
		contextTemp=context;
		errorContextTemp=errorContext;
		T page = instantiatePage(driver, pageClassToProxy);
		initElements(driver, page);
		return page;
	}

	/**
	 * As
	 * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver, Class)}
	 * but will only replace the fields of an already instantiated Page Object.
	 *
	 * @param driver The driver that will be used to look up the elements
	 * @param page The object with WebElement and List<WebElement> fields that should be proxied.
	 */
	public static void initElements(WebDriver driver, Object page) {
		final WebDriver driverRef = driver;
		initElements(new DefaultElementLocatorFactory(driverRef), page);
	}

	/**
	 * Similar to the other "initElements" methods, but takes an {@link ElementLocatorFactory} which
	 * is used for providing the mechanism for fniding elements. If the ElementLocatorFactory returns
	 * null then the field won't be decorated.
	 *
	 * @param factory The factory to use
	 * @param page The object to decorate the fields of
	 */
	public static void initElements(ElementLocatorFactory factory, Object page) {
		final ElementLocatorFactory factoryRef = factory;
		initElements(new DefaultFieldDecorator(factoryRef), page);
	}

	/**
	 * Similar to the other "initElements" methods, but takes an {@link FieldDecorator} which is used
	 * for decorating each of the fields.
	 *
	 * @param decorator the decorator to use
	 * @param page The object to decorate the fields of
	 */
	public static void initElements(FieldDecorator decorator, Object page) {
		Class<?> proxyIn = page.getClass();
		while (proxyIn != Object.class) {
			proxyFields(decorator, page, proxyIn);
			proxyIn = proxyIn.getSuperclass();
		}
	}

	private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {

		Field[] fields = proxyIn.getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().equals("context")){
				try {
					field.set(page, contextTemp);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if(field.getName().equals("errorContext")){
				try {
					field.set(page, errorContextTemp);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			Object value = decorator.decorate(page.getClass().getClassLoader(), field);
			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(page, value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy) {
		try {
			return pageClassToProxy.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}