package cc.mycode.realtest.core;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Screen {

	private final WebDriver driver;

	protected static Map<String,String> context;
	
	protected static ErrorContext errorContext;
	
	private Wait<WebDriver> defaultWait;
	
	private Wait<WebDriver> shortWait;
	
	private Wait<WebDriver> veryShortWait;
	
	public Logger log = Logger.getLogger(WebDriver.class.getName());
	
	protected String some;
	
	public Screen() {
		this.driver=TestCase.getNativeDriver();
		initializeWaits();
		//load properties
		some = ResourceLocator.getProperty("some");
	}
	
	public void initializeWaits(){
		defaultWait=new WebDriverWait(driver, 5);
		shortWait= new WebDriverWait(driver, 3);
		veryShortWait=new WebDriverWait(driver, 1);
	}

	public void scroll(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight," +
				"document.body.scrollHeight,document.documentElement.clientHeight));");
	}

	public static <T> T nextPage(Class<T> pageClassToProxy) {
		return ProxyWrapper.initElements(TestCase.getNativeDriver(), pageClassToProxy,context,errorContext);
	}

	public static <T> T use(WebDriver initialDriver,Class<T> pageClassToProxy,Map<String,String> context,ErrorContext errorContext) {
		return ProxyWrapper.initElements(initialDriver, pageClassToProxy,context,errorContext);
	}

	public WebDriver getNativeDriver() {
		return driver;
	}

	public void throwValidationError() {
		assertTrue(false,errorContext.putMessage("Validation Error"));
	}
	
	public void stop() {
		driver.quit();
	}

	public void open(String url) {
		driver.navigate().to(url);
	}

	public void navigateTo(String url) {
		driver.navigate().to(url);
	}

	public void check(WebElement element) {
		if (!element.isSelected()) {
			element.click();
			waitForChecked(element);
		}
	}

	public void uncheck(WebElement element) {
		if (element.isSelected()) {
			element.click();
			waitForNotChecked(element);
		}
	}

	public boolean isElementContainsText(WebElement element, String value) {
		return element.getText().contains(value);
	}



	public void type(WebElement element, String value) {
		waitForVisible(element);
		element.clear();
		element.sendKeys(value);
	}

	public void typeHtml(String idTextArea, String html){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("$('#" + idTextArea + "').wysiwyg('setContent', '" + html + "')");
	}

	public void selectPluginFirstTwoOption(String selectId){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("$('#"+selectId+" option:lt(2)').attr('selected',true).trigger('liszt:updated')");
	}

	public void clear(WebElement element) {
		waitForVisible(element);
		element.clear();
	}

	public void confirmBox() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	@Deprecated
	public void typeKeys(WebElement element, String value) {
		type(element, value);
	}

	public void click(WebElement element) {
		waitForVisible(element);
		element.click();
	}

	@Deprecated
	public void clickAndWait(WebElement element) {
		click(element);
	}

	public void selectByVisibleText(WebElement element, String visibleText) {
		new Select(element).selectByVisibleText(visibleText);
	}


	public void selectByValue(WebElement element, String value) {
		new Select(element).selectByValue(value);
	}

	public void chooseOkOnNextConfirmation() {
		driver.switchTo().alert().accept();
	}

	public void openWindow(String url, String windowName) {
		driver.switchTo().window(windowName).navigate().to(url);
	}

	public void selectWindow(String windowName) {
		driver.switchTo().window(windowName);

	}

	public boolean isElementPresent(WebElement element) {
		try {
			element.isDisplayed();
			return element!= null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isElementPresent(String locator) {
		try {
			WebElement element =  element(locator);
			element.isDisplayed();
			return element!= null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	public boolean isElementNotPresent(String locator) {
		return !isElementPresent(locator);
	}


	public boolean isVisible(String locator) {
		try {
			WebElement element = element(locator);
			return element != null ? element.isDisplayed() : false;
		} catch (ElementNotVisibleException e) {
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public String getText(WebElement element) {
		waitForVisible(element);
		return element.getText();
	}


	public String getValue(String locator) {
		return valueFrom(element(locator));
	}

	public String getAttribute(String locator, String attributeName) {
		return element(locator).getAttribute(attributeName);
	}

	public String getAttribute(String locator) {
		String[] locatorAndAttrName = locator.split("@");
		if (locatorAndAttrName.length != 2) {
			throw new IllegalArgumentException("Sintaxe esperada: cssLocator@nomeAtributo");
		}
		return getAttribute(locatorAndAttrName[0], locatorAndAttrName[1]);
	}

	public int getLocatorCount(String locator) {
		return elements(locator).size();
	}

	public String getSessionId() {
		Cookie jSessionId = driver.manage().getCookieNamed("JSESSIONID");
		return jSessionId != null ? jSessionId.getValue() : null;
	}

	
	public void assertTitle(String title) {
		assertEquals("O titulo da pagina nao era o esperado", title, driver.getTitle());
	}

	public void assertText(String locator, String value) {
		String text = element(locator).getText();
		assertTrue(text.contains(value), "O texto do elemento nao era o esperado");
	}

	public void assertElementPresent(String locator) {
		try {
			assertNotNull(element(locator), "Elemento esperado nao foi encontrado");
		} catch (NoSuchElementException e) {
			fail("Elemento esperado nao foi encontrado");
		}
	}

	public void assertElementNotPresent(String locator) {
		try {
			assertNull(element(locator), "Elemento nao deveria existir");
		} catch (NoSuchElementException e) {
		}
	}

	public void assertVisible(String locator) {
		try {
			assertTrue(element(locator).isDisplayed(), "Elemento que deveria estar visivel esta invisivel");
		} catch (NoSuchElementException e) {
			fail("Elemento que deveria estar visivel nao existe no codigo da tela");
		} catch (ElementNotVisibleException e) {
			fail("Elemento que deveria estar visivel esta invisivel");
		}
	}

	public void assertNotVisible(String locator) {
		try {
			assertFalse(element(locator).isDisplayed(), "Elemento que deveria estar invisivel esta visivel");
		} catch (NoSuchElementException e) {
			fail("Elemento que deveria estar invisivel nao existe no codigo da tela");
		} catch (ElementNotVisibleException e) {
			// Esta' ok se lancar esta excecao...
		}
	}

	public void assertTextPresent(String text) {
		assertTrue(driver.getPageSource().contains(text), "O texto do elemento deveria ter sido encontrado na pagina");
	}

	public void assertTextNotPresent(String text) {
		assertFalse(driver.getPageSource().contains(text), "O texto do elemento nao deveria ter sido encontrado na pagina");
	}

	public void assertValue(String locator, String value) {
		assertEquals("O valor do elemento nao e' o esperado", value, element(locator).getAttribute("value"));
	}

	public void assertSelectedLabel(String locator, String value) {
		Select select = new Select(element(locator));
		assertEquals("O elemento selecionado nao e' o esperado", value, select.getFirstSelectedOption().getText());
	}

	public void assertChecked(String locator) {
		assertTrue(element(locator).isSelected(), "O elemento nao esta selecionado");
	}

	public void assertNotChecked(String locator) {
		assertFalse(element(locator).isSelected(), "O elemento esta selecionado");
	}

	@Deprecated
	public void assertAttribute(String locator, String value) {
		assertEquals("Valor do atributo nao era o esperado", value, getAttribute(locator));
	}

	public void assertAttribute(String locator, String attributeName, String value) {
		assertEquals("Valor do atributo nao era o esperado", value, getAttribute(locator, attributeName));
	}

	public void assertLocation(String location) {
		assertEquals("O valor do elemento nao e' o esperado", location, driver.getCurrentUrl());
	}

	public void assertTableCellText(String tableId, int numRow, int numColumn, String text) {
		assertEquals("O valor da celula da tabela " + tableId + " nao corresponde ao esperado", text,
				element(locateTableCell(tableId, numRow, numColumn)).getText());
	}

	public void assertTableCellTextNotEquals(String tableId, int numRow, int numColumn, String text) {
		assertFalse(text.equals(element(locateTableCell(tableId, numRow, numColumn)).getText()), "O valor da celula da tabela " + tableId + " e' o mesmo que o informado");
	}

	public void assertLocatorCount(String locator, int expectedNumber) {
		assertEquals(expectedNumber, elements(locator).size(), "Numero inesperado de elementos");
	}

	public void assertTableRowExistence(String tableId, String... rowCellsValues) {
		List<WebElement> tableRows = driver.findElements(By.cssSelector(tableId + " tr"));
		int numMatchedValues = -1;

		for (WebElement tableRow : tableRows) {
			numMatchedValues = 0;
			String rowText = tableRow.getText();

			for (String rowCellValue : rowCellsValues) {
				if (rowText.contains(rowCellValue))
					numMatchedValues++;
			}

			if (numMatchedValues == rowCellsValues.length)
				break;
		}

		assertNotSame(-1, numMatchedValues, "A tabela informada nao possui linhas");

		assertSame(rowCellsValues.length, numMatchedValues, "Nenhuma linha da tabela possui todos os valores especificados");
	}

	public void waitForTitle(final String value) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.getTitle().contains(value);
			}
		});
	}

	@Deprecated
	public void waitForConfirmation(final String value) {
		waitForAlert(value);
	}

	public void waitForElementPresent(final WebElement element) {
		veryShortWait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return element;
			}
		});
	}

	public void waitForElementNotPresent(final String locator) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					return element(locator) == null;
				} catch (NotFoundException e) {
					return true;
				}
			}
		});
	}

	public void waitForElementPresent(final String css) {
		veryShortWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					return element(css) != null;
				} catch (NotFoundException e) {
					return true;
				}
			}
		});
	}


	public void waitForVisible(final WebElement element) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return element != null ? element.isDisplayed() : false;
			}
		});
	}

	public void waitForNotVisible(final String locator) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					WebElement element = element(locator);
					return element != null ? !element.isDisplayed() : true;
				} catch (ElementNotVisibleException e) {
					return true;
				} catch (NoSuchElementException e) {
					return true;
				}
			}
		});
	}

	public void waitForChecked(final WebElement element) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return element.isSelected();
			}
		});
	}

	public void waitForNotChecked(final WebElement locator) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return !locator.isSelected();
			}
		});
	}

	public void waitForTextPresent(final String text) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.getPageSource().contains(text);
			}
		});
	}

	public void waitForText(final String locator, final String text) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return text.equals(element(locator).getText());
			}
		});
	}

	public void waitForAlert(final String pattern) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.switchTo().alert().getText().contains(pattern); // TODO verificar exceушes, precisa?
			}
		});
	}

	public void waitForLocatorCount(final String locator, final int expectedNumber) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return elements(locator).size() == expectedNumber;
			}
		});
	}

	public void waitForValue(final String locator, final String pattern) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return valueFrom(element(locator)).contains(pattern);
			}
		});
	}



	public void waitForPopUp(final String windowName) {
		shortWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				driver.switchTo().window(windowName);				
				return true;
			}
		});
	}

	public void waitForAnyValue(final String locator) {
		defaultWait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return valueFrom(element(locator)).matches(".+");
			}
		});
	}

	public void waitSeconds(long time) {
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}

	public WebElement findByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}

	/**
	 * Use css selector for find element
	 * */
	public WebElement find(String css) {
		return driver.findElement(By.cssSelector(css));
	}

	public String showWindow() {
		return driver.getTitle().toString();

	}

	public void lookForIframe(String iframe) {
		driver.switchTo().frame(iframe);
	}

	private WebElement element(String cssLocator) {
		return driver.findElement(By.cssSelector(cssLocator));
	}

	private List<WebElement> elements(String cssLocator) {
		return driver.findElements(By.cssSelector(cssLocator));
	}

	private String valueFrom(WebElement element) {
		return element.getAttribute("value");
	}

	private String locateTableCell(String tableId, int numRow, int numColumn) {
		StringBuilder locator = new StringBuilder() //
		.append("table#" + tableId) //
		.append(" ") //
		.append("tr:nth(").append(numRow - 1).append(")") //
		.append(" > ") //
		.append("td:nth(").append(numColumn - 1).append(")");

		return locator.toString();
	}

	public void defaultWait() {
		try {
			Thread.sleep(3000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void longWait() {
		try {
			Thread.sleep(25000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void shortWait() {
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentUrl(){
		return driver.getCurrentUrl();
	}

	public void insereFotoEmIframe(String nameIframe, WebElement elemento, String path, WebElement botaoEnviarFoto){
		String _whandle = driver.getWindowHandle();
		driver.switchTo().frame(nameIframe);
		elemento.sendKeys(path);
		botaoEnviarFoto.click();
		driver.switchTo().window(_whandle);
	}

	public void refresh(){
		driver.navigate().refresh();
	}
}