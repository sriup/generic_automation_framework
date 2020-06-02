package framework.commonfunctions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.enums.ExpectedConditionsEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.ExcelUtil;
import framework.utilities.FakeDataUtil;
import framework.utilities.FolderFileUtil;
import framework.utilities.SecurityUtil;
import framework.utilities.ZipUtil;

/**
 * 
 * All the methods related to the common operations will be handled in this
 * class.
 */
public class CommonFunctions {

	/** Folder path where the captured screenshots should be stored. */
	private String screenShotsPath;

	/** LogAccess object. */
	private LogAccess logAccess;

	/** DateTimeUtil object. */
	private DateTimeUtil dateTimeUtil;

	/**
	 * Gets the dateTimeUtil object.
	 *
	 * @return the instance of DateTimeUtils
	 */
	public DateTimeUtil getDateTimeUtil() {
		return dateTimeUtil;
	}

	/** FolderFileUtil object. */
	private FolderFileUtil folderFileUtil;

	/**
	 * Gets the folderFileUtil object.
	 *
	 * @return the folderFileUtil
	 */
	public FolderFileUtil getFolderFileUtil() {
		return folderFileUtil;
	}

	/** The Excel Utility. */
	private ExcelUtil excelUtil;

	/**
	 * Gets the excelUtil.
	 *
	 * @return the excelUtil
	 */
	public ExcelUtil getExcelUtil() {
		return excelUtil;
	}

	/** The Fake Data Utility. */
	private FakeDataUtil fakeDataUtil;

	/**
	 * Gets the FakeDataUtil.
	 *
	 * @return the FakeDataUtil
	 */
	public FakeDataUtil getFakeDataUtil() {
		return fakeDataUtil;
	}

	/** The Security Utility. */
	private SecurityUtil securityUtil;

	/**
	 * Gets the SecurityUtil.
	 *
	 * @return the SecurityUtil
	 */
	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}

	/** The Zip Utility. */
	private ZipUtil zipUtil;

	/**
	 * Gets the ZipUtil.
	 *
	 * @return the ZipUtil
	 */
	public ZipUtil getZipUtil() {
		return this.zipUtil;
	}

	/**
	 * Instantiates a new common functions.<br>
	 * All the utilities available in the framework will be instantiate as part of
	 * this. <br>
	 * Below is the list of utilities available in framework
	 * <ul>
	 * <li>{@link DateTimeUtil DateTimeUtil} can be accessed using
	 * {@link #getDateTimeUtil}</li>
	 * <li>{@link FolderFileUtil FolderFileUtil} can be accessed using
	 * {@link #getFolderFileUtil}</li>
	 * <li>{@link ExcelUtil ExcelUtil} can be accessed using
	 * {@link #getExcelUtil}</li>
	 * <li>{@link FakeDataUtil FakeDataUtil} can be accessed using
	 * {@link #getFakeDataUtil}</li>
	 * <li>{@link SecurityUtil SecurityUtil} can be accessed using
	 * {@link #getSecurityUtil}</li>
	 * <li>{@link ZipUtil ZipUtil} can be accessed using {@link #getZipUtil}</li>
	 * </ul>
	 * 
	 * @param screenShotsPath the screen shots path
	 * @param logAccess       Log Access object
	 */
	public CommonFunctions(String screenShotsPath, LogAccess logAccess) {
		this.logAccess = logAccess;
		this.screenShotsPath = screenShotsPath;
		dateTimeUtil = new DateTimeUtil(logAccess);
		folderFileUtil = new FolderFileUtil(logAccess);
		excelUtil = new ExcelUtil(logAccess);
		fakeDataUtil = new FakeDataUtil(logAccess);
		securityUtil = new SecurityUtil();
		zipUtil = new ZipUtil(logAccess);
	}

	/**
	 * Sets the date time format for the screenshot. <br>
	 * <font color="blue">Note:<i> This will be prepended to the screenshot
	 * name.</i></font>
	 *
	 * @return the screen shot time
	 * @throws Exception the exception
	 */
	public String getScreenShotTime() throws Exception {
		return dateTimeUtil.getCurrentDateTime(CommonVariables.TIME_FORMATS[8]);
	}

	/**
	 * Wait for element.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the {@link org.openqa.selenium.WebElement element}
	 * @param expectedCondition the expected condition <br>
	 *                          <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @return the {@link org.openqa.selenium.WebElement element}
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + element);
		return waitUntilElement(driver, element, expectedCondition, CommonVariables.MED_TIMEOUT);
	}

	/**
	 * Wait for element.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the {@link org.openqa.selenium.WebElement element}
	 * @param expectedCondition the expected condition<br>
	 *                          * * <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param maxTimeout        the max timeout in seconds
	 * @return the {@link org.openqa.selenium.WebElement element}
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + element);
		return waitUntilElement(driver, element, expectedCondition, maxTimeout);
	}

	/**
	 * Wait for element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition<br>
	 *                          <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>PRESENCE</li>
	 *                          </ul>
	 *                          </font>
	 * @return the web element
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, CommonVariables.MED_TIMEOUT);
	}

	/**
	 * Wait for element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition<br>
	 *                          <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>PRESENCE</li>
	 *                          </ul>
	 *                          </font>
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, maxTimeout);
	}

	/**
	 * Wait for element(s) visible.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param elements   the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeOut Maximum time to wait for the element(s) visible
	 * @return List of WebElements
	 */
	public List<WebElement> waitForElementsToVisible(WebDriver driver, List<WebElement> elements, int maxTimeOut) {
		this.logAccess.getLogger().debug("waiting for all specified elements in the list to be visible" + elements);
		return (new WebDriverWait(driver, maxTimeOut)).until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	/**
	 * Wait for invisibility of element. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @throws Exception
	 */
	public void waitForInvisibilityOfElement(WebDriver driver, WebElement element, int maxTimeout) throws Exception {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + element);

		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, CommonVariables.MIN_TIMEOUT);
		} catch (Exception ignoreException) {
			// ignore the exception and continue with the script
		}

		long currentTimestamp = (new Date()).getTime();
		int waitingSeconds = maxTimeout * 1000;
		long endTimestamp = currentTimestamp + waitingSeconds;

		boolean isElementInvisible = true;

		this.logAccess.getLogger().info("End timestamp for Invisibility of an Element is " + endTimestamp);

		try {

			while ((new Date()).getTime() < endTimestamp && isElementInvisible) {

				isElementInvisible = isElementPresent(driver, element, CommonVariables.NO_TIMEOUT);

				if (isElementInvisible) {
					// Checking if element is visible though it is in the DOM.
					isElementInvisible = element.isDisplayed();

				}
				Thread.sleep(500);
			}
		} catch (NoSuchElementException | StaleElementReferenceException ignoreException) {
			// intentionally left it blank (we can ignore the above exceptions when waiting
			// for element in-visibility)
		}

	}

	/**
	 * Wait for invisibility of element by locator. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout in seconds
	 * @throws Exception
	 */
	public void waitForInvisibilityOfElement(WebDriver driver, By byLocator, int maxTimeout) throws Exception {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + byLocator);

		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, CommonVariables.MIN_TIMEOUT);
		} catch (Exception ignoreException) {
			// ignore the exception and continue with the script
		}

		long currentTimestamp = (new Date()).getTime();
		int waitingSeconds = maxTimeout * 1000;
		long endTimestamp = currentTimestamp + waitingSeconds;

		boolean isElementInvisible = true;

		this.logAccess.getLogger().info("End timestamp for Invisibility of an Element is " + endTimestamp);
		try {
			while ((new Date()).getTime() < endTimestamp && isElementInvisible) {

				isElementInvisible = isElementPresent(driver, byLocator, CommonVariables.MIN_TIMEOUT);

				if (isElementInvisible) {
					// Checking if element is visible though it is in the DOM.
					isElementInvisible = isElementDisplayed(driver, byLocator, CommonVariables.MIN_TIMEOUT);
				}
			}
		} catch (NoSuchElementException | StaleElementReferenceException ignoreException) {
			// intentionally left it blank (we can ignore the above exceptions when waiting
			// for element in-visibility)
		}

	}

	/**
	 * Checks if is element present.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element present
	 */
	// is element present
	public boolean isElementPresent(WebDriver driver, WebElement element) {
		this.logAccess.getLogger().info("checking if element is present  :- " + element);
		// wait for the {@link org.openqa.selenium.WebElement element} to present
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.PRESENCE, CommonVariables.MED_TIMEOUT);
			// return true if the element is present
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// present
			return false;
		}
	}

	/**
	 * Checks if is element present.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element present
	 */
	public boolean isElementPresent(WebDriver driver, WebElement element, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is present  :- " + element);
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.PRESENCE, maxTimeout);
			// return true if the element is present
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// present
			return false;
		}
	}

	/**
	 * Checks if is element present by locator.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return true, if is element present by locator
	 */
	public boolean isElementPresent(WebDriver driver, By byLocator) {
		this.logAccess.getLogger().info("checking if element is present  :- " + byLocator);
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, CommonVariables.MED_TIMEOUT);
			// return true if the /element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element present by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element present by locator
	 */
	public boolean isElementPresent(WebDriver driver, By byLocator, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is present  :- " + byLocator);
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, maxTimeout);
			// return true if the /element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element displayed by locator
	 */
	public boolean isElementDisplayed(WebDriver driver, By byLocator, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + byLocator);
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE, maxTimeout);
			// return true if the element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, By byLocator) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + byLocator);
		// wait for the element to present for specified time
		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE, CommonVariables.MED_TIMEOUT);
			// return true if the element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, WebElement element, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + element);
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, maxTimeout);
			// return true if the element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, WebElement element) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + element);
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, CommonVariables.MED_TIMEOUT);
			// return true if the element is visible
			return true;
		} catch (Exception e) {
			// return false if the element is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(WebDriver driver, WebElement element) {
		return isElementDisplayed(driver, element, CommonVariables.MED_TIMEOUT);

	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(WebDriver driver, WebElement element, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is enabled  :- " + element);
		long currentTimestamp = (new Date()).getTime();
		long endTimestamp = currentTimestamp + maxTimeout * 1000;
		Boolean conditionalCheck = false;
		while ((new Date()).getTime() < endTimestamp && !conditionalCheck) {
			conditionalCheck = element.isEnabled();
		}
		return conditionalCheck;
	}

	/**
	 * Checks if is element enabled by locator.<br>
	 * <font color='blue'>Note :<br>
	 * This method will keep checking for max of {@link CommonVariables#MED_TIMEOUT}
	 * seconds<
	 * 
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return true, if is element enabled by locator
	 * @throws Exception
	 */
	public boolean isElementEnabled(WebDriver driver, By byLocator) throws Exception {

		return isElementEnabled(driver, byLocator, CommonVariables.MED_TIMEOUT);
	}

	/**
	 * Checks if is element enabled by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout in seconds
	 * @return true, if is element enabled by locator
	 * @throws Exception
	 */
	public boolean isElementEnabled(WebDriver driver, By byLocator, int maxTimeout) throws Exception {
		this.logAccess.getLogger().info("checking if element is enabled  :- " + byLocator);
		long currentTimestamp = (new Date()).getTime();
		long endTimestamp = currentTimestamp + maxTimeout * 1000;
		Boolean conditionalCheck = false;
		while ((new Date()).getTime() < endTimestamp && !conditionalCheck) {
			conditionalCheck = driver.findElement(byLocator).isEnabled();
			Thread.sleep(500);
		}
		return conditionalCheck;
	}

	/**
	 * Highlights the element.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element} <br>
	 *                This method will highlight the element and does not set back
	 *                the original style. <br>
	 *                <font color="blue"><b>Note:</b> Use {@link #unHighlightElement
	 *                unHighlightElement} method to set back the original style of
	 *                the element.</font>
	 * @return the String with original style of the element
	 */
	public String highlightElement(WebDriver driver, WebElement element) {
		this.logAccess.getLogger().debug("Highlighting element :- " + element);
		// get the original
		String originalStyle = getOriginalStyle(element);
		// scroll element to the center of the page
		String scrollToCentreJs = "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});";
		executeJs(driver, element, scrollToCentreJs);
		// highlight the web element
		String highlightJavaScript = "arguments[0].setAttribute('style', 'background: orange; border: 2px solid red');";
		executeJs(driver, element, highlightJavaScript);
		return originalStyle;
	}

	/**
	 * Highlights the element.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator <br>
	 *                  This method will highlight the element and does not set back
	 *                  the original style. <br>
	 *                  <font color="blue"><b>Note:</b> Use
	 *                  {@link #unHighlightElement unHighlightElement} method to set
	 *                  back the original style of the element.</font>
	 * @return the String with original style of the element
	 */
	public String highlightElement(WebDriver driver, By byLocator) {
		this.logAccess.getLogger().debug("Highlighting element :- " + byLocator);
		// highlight the element and return the original style
		return highlightElement(driver, getElement(driver, byLocator));
	}

	/**
	 * Un-highlight element.
	 *
	 * @param driver        the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element       the {@link org.openqa.selenium.WebElement element}
	 * @param originalStyle the original style
	 * 
	 */
	public void unHighlightElement(WebDriver driver, WebElement element, String originalStyle) {
		this.logAccess.getLogger().debug("Unhighlighting element  :- " + element);
		// set element original style
		try {
			setOriginalStyle(driver, element, originalStyle);
		} catch (NoSuchElementException | StaleElementReferenceException
				| ElementNotInteractableException ignoreException) {
			// we don't have to either print the trace or throw the exception
			// here as there are situations where the element might not present
			// after performing some actions like click
		}
	}

	/**
	 * Un-highlight element.
	 *
	 * @param driver        the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator     the by locator
	 * @param originalStyle the original style
	 * 
	 */
	public void unHighlightElement(WebDriver driver, By byLocator, String originalStyle) {
		this.logAccess.getLogger().debug("Unhighlighting element  :- " + byLocator);
		unHighlightElement(driver, driver.findElement(byLocator), originalStyle);
	}

	/**
	 * Flash the {@link org.openqa.selenium.WebElement element} <i>n</i> number of
	 * times.
	 *
	 * @param driver        the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element       the {@link org.openqa.selenium.WebElement element}
	 * @param numberOfTimes the number of times
	 * @throws Exception the exception
	 */
	public void flash(WebDriver driver, WebElement element, int numberOfTimes) throws Exception {
		this.logAccess.getLogger().debug("Flashing element  :- " + element);
		try {
			WebElement tempElement = getElement(driver, element);
			// get element original style
			String originalStyle = getOriginalStyle(tempElement);
			for (int highlightIndex = 1; highlightIndex <= numberOfTimes; highlightIndex++) {
				highlightElement(driver, tempElement);
				Thread.sleep(300);
				unHighlightElement(driver, tempElement, originalStyle);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Flash the {@link org.openqa.selenium.WebElement element} <i>5</i> number of
	 * times.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @throws Exception the exception
	 */
	public void flash(WebDriver driver, WebElement element) throws Exception {
		this.logAccess.getLogger().debug("Flashing element  :- " + element);
		flash(driver, element, 5);
	}

	/**
	 * Flash the {@link org.openqa.selenium.WebElement element} <i>n</i> number of
	 * times.
	 *
	 * @param driver        the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator     the by locator
	 * @param numberOfTimes the number of times
	 * @throws Exception the exception
	 */
	public void flash(WebDriver driver, By byLocator, int numberOfTimes) throws Exception {
		this.logAccess.getLogger().debug("Flashing element  :- " + byLocator);
		flash(driver, getElement(driver, byLocator), numberOfTimes);
	}

	/**
	 * Flash the {@link org.openqa.selenium.WebElement element} <i>5</i> number of
	 * times.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @throws Exception the exception
	 */
	public void flash(WebDriver driver, By byLocator) throws Exception {
		this.logAccess.getLogger().debug("Flashing element  :- " + byLocator);
		flash(driver, getElement(driver, byLocator), 5);
	}

	/**
	 * Click on element.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param captureBefore       the capture before
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	// click element
	public void clickOnElement(WebDriver driver, WebElement element, boolean isCaptureScreenShot, boolean captureBefore,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Clicking on element  :- " + element);
		WebElement tempElement = getElement(driver, element);
		// highlight element
		String originalStyle = highlightElement(driver, tempElement);
		// capture before (private capture screenshot)
		if (isCaptureScreenShot && captureBefore) {
			// take screenshot
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
		// click
		tempElement.click();
		// capture after (private capture screenshot)
		if (isCaptureScreenShot && !captureBefore) {

			// highlight element
			originalStyle = highlightElement(driver, tempElement);

			// take screenshot
			captureScreenShot(driver, screenShotName);
			// un-highlihgt
			unHighlightElement(driver, tempElement, originalStyle);
		}

	}

	/**
	 * Click on element.
	 *
	 * @param driver              the driver
	 * @param byLocator           the by locator
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param captureBefore       the capture before
	 * @param screenShotName      the screen shot name
	 * @throws Exception the exception
	 */
	public void clickOnElement(WebDriver driver, By byLocator, boolean isCaptureScreenShot, boolean captureBefore,
			String screenShotName) throws Exception {
		WebElement element = getElement(driver, byLocator);
		clickOnElement(driver, element, isCaptureScreenShot, captureBefore, screenShotName);
	}

	/**
	 * Input value.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param value               the value to be set in the element
	 * @param isCaptureScreenshot toggle to capture screenshot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void inputValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("value :- " + value);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempEle = getElement(driver, element);
		// click in the field
		tempEle.click();
		// clear any existing values
		tempEle.clear();
		// enter value in the field
		tempEle.sendKeys(value);
		// trigger the onchange event (to make sure the events dispatches correctly in
		// IE)
		jsTriggerEventOnElement(driver, tempEle, "onchange");

		// highlight element
		String originalStyle = highlightElement(driver, tempEle);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, tempEle, originalStyle);
	}

	/**
	 * Input value.
	 *
	 * @param driver              the driver
	 * @param byLocator           the by locator
	 * @param value               the value
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screen shot name
	 * @throws Exception the exception
	 */
	public void inputValue(WebDriver driver, By byLocator, String value, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		WebElement element = getElement(driver, byLocator);
		inputValue(driver, element, value, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Gets number of options(list items) in the list
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return number of options(list items) in the list
	 * @throws Exception the exception
	 */
	public int getNumberOfListItems(WebDriver driver, WebElement element, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by index
		Select listElement = new Select(tempElement);

		// get the number of options
		return listElement.getOptions().size();
	}

	/**
	 * Gets number of options(list items) in the list
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return number of options(list items) in the list
	 * @throws Exception the exception
	 */
	public int getNumberOfListItems(WebDriver driver, By byLocator, boolean isCaptureScreenshot, String screenShotName)
			throws Exception {
		// get the element
		WebElement tempElement = getElement(driver, byLocator);

		return getNumberOfListItems(driver, tempElement, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Gets the Selected list item.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return selected element
	 * @throws Exception the exception
	 */
	public WebElement getSelectedListItem(WebDriver driver, WebElement element, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by index
		Select listElement = new Select(tempElement);
		WebElement selectedItem = listElement.getFirstSelectedOption();

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
		return selectedItem;
	}

	/**
	 * Gets all the Selected list items.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return WebElent List of selected list items
	 * @throws Exception the exception
	 */
	public List<WebElement> getSelectedListItems(WebDriver driver, WebElement element, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by index
		Select listElement = new Select(tempElement);
		List<WebElement> selectedItems = listElement.getAllSelectedOptions();

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
		return selectedItems;
	}

	/**
	 * Gets the Selected list item.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by Locator
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return selected element
	 * @throws Exception the exception
	 */
	public WebElement getSelectedListItem(WebDriver driver, By byLocator, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("By Locator  :- " + byLocator);
		// get the element
		WebElement tempElement = getElement(driver, byLocator);
		return getSelectedListItem(driver, tempElement, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Gets the Selected list item text
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by Locator
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return selected element text
	 * @throws Exception the exception
	 */
	public String getSelectedListItemText(WebDriver driver, By byLocator, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("By Locator  :- " + byLocator);
		// get the element
		WebElement tempElement = getElement(driver, byLocator);
		return getSelectedListItemText(driver, tempElement, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Gets the Selected list item text
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by Locator
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return selected element text
	 * @throws Exception the exception
	 */
	public String getSelectedListItemText(WebDriver driver, WebElement element, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Element  :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);
		return getSelectedListItem(driver, tempElement, isCaptureScreenshot, screenShotName).getText();
	}

	/**
	 * Gets all the Selected list items.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return WebElent List of selected list items
	 * @throws Exception the exception
	 */
	public List<WebElement> getSelectedListItems(WebDriver driver, By byLocator, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("By Locator :- " + byLocator);
		// get the element
		WebElement tempElement = getElement(driver, byLocator);

		return getSelectedListItems(driver, tempElement, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Select item by index.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param index               the index
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByIndex(WebDriver driver, WebElement element, int index, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Index :-  " + String.valueOf(index));
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by index
		Select listElement = new Select(tempElement);
		listElement.selectByIndex(index);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
	}

	/**
	 * Select drop down list item based on value.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param value               the list item value to be selected
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Value :-  " + value);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by value
		Select dropDown = new Select(tempElement);
		dropDown.selectByValue(value);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
	}

	/**
	 * Select list item based on the visible text.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param visibleText         the visible text of the list item
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByVisibleText(WebDriver driver, WebElement element, String visibleText,
			boolean isCaptureScreenshot, String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Visible Text :-  " + visibleText);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);

		// select item by visible text
		Select dropDown = new Select(tempElement);
		dropDown.selectByVisibleText(visibleText);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
	}

	/**
	 * Select list item based on the partial visible text.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param partialVisibleText  the partial visible text of the list item
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByPartialVisibleText(WebDriver driver, WebElement element, String partialVisibleText,
			boolean isCaptureScreenshot, String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Partial visible Text :-  " + partialVisibleText);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element);
		WebElement option = tempElement
				.findElement(By.xpath("./option[contains(text(),'" + partialVisibleText + "')]"));
		option.click();

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlihgt
		unHighlightElement(driver, tempElement, originalStyle);
	}

	/**
	 * Select item by index.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param index               the index
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByIndex(WebDriver driver, By byLocator, int index, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		selectItemByIndex(driver, getElement(driver, byLocator), index, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Select drop down list item based on value.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param value               the list item value to be selected
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByValue(WebDriver driver, By byLocator, String value, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		selectItemByValue(driver, getElement(driver, byLocator), value, isCaptureScreenshot, screenShotName);
	}

	/**
	 * Select list item based on the visible text.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param visibleText         the visible text of the list item
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByVisibleText(WebDriver driver, By byLocator, String visibleText, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		selectItemByVisibleText(driver, getElement(driver, byLocator), visibleText, isCaptureScreenshot,
				screenShotName);
	}

	/**
	 * Select list item based on the partial visible text.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param partialVisibleText  the partial visible text of the list item
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void selectItemByPartialVisibleText(WebDriver driver, By byLocator, String partialVisibleText,
			boolean isCaptureScreenshot, String screenShotName) throws Exception {
		selectItemByPartialVisibleText(driver, getElement(driver, byLocator), partialVisibleText, isCaptureScreenshot,
				screenShotName);
	}

	/**
	 * Get the visible (i.e. not hidden by CSS) text of this element, including
	 * sub-elements.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return the visible text of this element.
	 * @throws Exception the exception
	 */
	public String getText(WebDriver driver, WebElement element, boolean isCaptureScreenShot, String screenShotName)
			throws Exception {
		this.logAccess.getLogger().info("Getting text form element :- " + element);
		String elementText;
		// get the element
		WebElement tempElement = getElement(driver, element);
		// highlight the element
		String originalStyle = highlightElement(driver, tempElement);
		// get the element text
		elementText = tempElement.getText();
		// capture screenshot
		if (isCaptureScreenShot) {
			captureScreenShot(driver, screenShotName);
		}
		// unhighlight the element
		unHighlightElement(driver, tempElement, originalStyle);
		return elementText;
	}

	/**
	 * Get the visible (i.e. not hidden by CSS) text of this element, including
	 * sub-elements.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param byLocator           the by locator
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return the visible text of this element.
	 * @throws Exception the exception
	 */
	public String getText(WebDriver driver, By byLocator, boolean isCaptureScreenShot, String screenShotName)
			throws Exception {
		this.logAccess.getLogger().info("Getting text form element :- " + byLocator);
		String elementText;
		// get the element
		WebElement tempElement = getElement(driver, byLocator);
		// highlight the element
		String originalStyle = highlightElement(driver, tempElement);
		// get the element text
		elementText = tempElement.getText();
		// capture screenshot
		if (isCaptureScreenShot) {
			captureScreenShot(driver, screenShotName);
		}
		// unhighlight the element
		unHighlightElement(driver, tempElement, originalStyle);
		return elementText;
	}

	/**
	 * Gets the attribute.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param attributeName       the attribute name
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return the attribute/property's current value or null if the value is not
	 *         set.
	 * @throws Exception the exception
	 * @see org.openqa.selenium.WebElement#getAttribute getAttribute
	 */
	public String getAttribute(WebDriver driver, WebElement element, String attributeName, boolean isCaptureScreenShot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger().info("Getting " + attributeName + " attribute for element :- " + element);
		String attributeValue;
		// get the element
		WebElement tempElement = getElement(driver, element);
		// highlight the element
		String originalStyle = highlightElement(driver, tempElement);
		// get the element text
		attributeValue = tempElement.getAttribute(attributeName);
		// capture screenshot
		if (isCaptureScreenShot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlight the element
		unHighlightElement(driver, tempElement, originalStyle);
		return attributeValue;
	}

	/**
	 * Gets the attribute.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param attributeName       the attribute name
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return the attribute/property's current value or null if the value is not
	 *         set.
	 * @throws Exception the exception
	 * @see org.openqa.selenium.WebElement#getAttribute getAttribute
	 */
	public String getAttribute(WebDriver driver, By byLocator, String attributeName, boolean isCaptureScreenShot,
			String screenShotName) throws Exception {
		this.logAccess.getLogger()
				.info("Getting " + attributeName + " attribute for element by locator :- " + byLocator);
		// get the element
		WebElement tempElement = getElement(driver, byLocator);
		return getAttribute(driver, tempElement, attributeName, isCaptureScreenShot, screenShotName);
	}

	/**
	 * Trigger general events on the {@link org.openqa.selenium.WebElement element}
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element   the {@link org.openqa.selenium.WebElement element} the
	 *                  JavaScript has to dispatch the event.
	 * @param eventType the event type <b> eg: </b>MouseEvents, KeyBoardEvents
	 * @param eventName the event name
	 * 
	 * @see <a href=
	 *      'https://www.w3.org/TR/uievents'>https://www.w3.org/TR/uievents/</a> for
	 *      more information on the UIEvents.
	 */
	public void jsTriggerEventOnElement(WebDriver driver, WebElement element, String eventType, String eventName) {
		this.logAccess.getLogger().debug("Dispatching " + eventName + " event on element :- " + element);
		String jsFunction = " var clickEvent = document.createEvent ('" + eventType + "');" + "clickEvent.initEvent ('"
				+ eventName + "', true, true); " + "arguments [0].dispatchEvent (clickEvent); ";
		executeJs(driver, element, jsFunction);
	}

	/**
	 * Trigger general events on the {@link org.openqa.selenium.WebElement element}.
	 * <br>
	 * <font color="blue"><b>Note:</b> This helps in running the tests on IE where
	 * the associated event not triggered <br>
	 * </font> Please use {@link #jsTriggerMouseEvent} method for mouse related
	 * events.
	 * 
	 * 
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element   the {@link org.openqa.selenium.WebElement element} the
	 *                  JavaScript has to dispatch the event.
	 * @param eventName the event name to trigger <b> e.g: </b>onchange, onblur,
	 *                  onclick
	 */
	public void jsTriggerEventOnElement(WebDriver driver, WebElement element, String eventName) {
		this.logAccess.getLogger().debug("Dispatching " + eventName + " event on element :- " + element);
		String jsFunction = " var clickEvent = document.createEvent ('Event');  clickEvent.initEvent ('" + eventName
				+ "', true, true); arguments [0].dispatchEvent (clickEvent); ";
		executeJs(driver, element, jsFunction);
	}

	/**
	 * Trigger mouse events on the {@link org.openqa.selenium.WebElement element}.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element} the
	 *                   JavaScript has to dispatch the mouse event.
	 * @param mouseEvent the mouse event name to trigger <b> e.g: </b>onmousedown,
	 *                   onmouseup, onmouseover
	 */
	public void jsTriggerMouseEvent(WebDriver driver, WebElement element, String mouseEvent) {
		this.logAccess.getLogger().debug("Dispatching " + mouseEvent + " mouse event on element :- " + element);
		String jsFunction = " var clickEvent = document.createEvent ('MouseEvents');  clickEvent.initEvent ('"
				+ mouseEvent + "', true, true); arguments [0].dispatchEvent (clickEvent); ";
		executeJs(driver, element, jsFunction);
	}

	/**
	 * Captures screenshot after highlighting the element. Element original style
	 * will be set back after screenshot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element        the {@link org.openqa.selenium.WebElement element}
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 * @throws Exception the exception
	 */
	public String captureScreenShotWithHighlight(WebDriver driver, WebElement element, String screenshotName)
			throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot for element :- " + element);
		// highlight
		String originalStyle = highlightElement(driver, element);

		// capture screenshot
		String tempScreenshotName = captureScreenShot(driver, screenshotName);
		// un-highlight
		setOriginalStyle(driver, element, originalStyle);
		return tempScreenshotName;
	}

	/**
	 * Captures screenshot after highlighting the element. Element original style
	 * will be set back after screenshot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator      the by Locator
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 * @throws Exception the exception
	 */
	public String captureScreenShotWithHighlight(WebDriver driver, By byLocator, String screenshotName)
			throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot for element :- " + byLocator.toString());
		WebElement element = waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE);
		// highlight
		String originalStyle = highlightElement(driver, element);
		// capture screenshot
		String tempScreenshotName = captureScreenShot(driver, screenshotName);
		// un-highlight
		setOriginalStyle(driver, element, originalStyle);
		return tempScreenshotName;
	}

	/**
	 * Capture screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenShotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 * @throws Exception the exception
	 */
	// screenshots
	public String captureScreenShot(WebDriver driver, String screenShotName) throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot");
		File scrrenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String tempScreenshotName = this.screenShotsPath + "\\" + getScreenShotTime() + "_" + screenShotName + ".png";
		FileUtils.copyFile(scrrenShot, new File(tempScreenshotName));
		return tempScreenshotName;

	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenShotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.<br>
	 *                       <font color='blue'>Note :<br>
	 *                       Please use
	 *                       {@link #captureFullPageScreenShot(WebDriver, WebElement, boolean, String)}
	 *                       method to show the header only once in the full page
	 *                       screenshot</font>
	 * @return
	 * @throws Exception
	 */
	public String captureFullPageScreenShot(WebDriver driver, String screenShotName) throws Exception {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		// capture the entire page using page chunks approach if the flag is true and
		// browser name is not PhantomJs
		if (browserName.equalsIgnoreCase(BrowserEnums.PhantomJs.toString())) {
			return captureScreenShot(driver, screenShotName);
		} else {

			String tempScreenShotsFolderName = System.getProperty("user.dir") + File.separatorChar + "TempFolder"
					+ File.separatorChar + getScreenShotTime() + "_" + screenShotName;
			folderFileUtil.createFolder(tempScreenShotsFolderName);
			capturePageChunks(driver, tempScreenShotsFolderName);
			return mergeImagesToSingleImage(tempScreenShotsFolderName, screenShotName + ".png");
		}
	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver           the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param headerElement    the header element
	 * @param notIncludeHeader true/false<br>
	 *                         <font color='blue'>Note: <br>
	 *                         If you want to show any specific header only one full
	 *                         screenshot page mark it as true<br>
	 *                         If this is false, you will see the static header
	 *                         multiple times in the full screenshot</font>
	 * @param screenShotName   the screenshot name <br>
	 *                         Date time Stamp will be <i>prepended</i> to the
	 *                         screenshot name by default.<br>
	 *                         Note: Use {@link #screenShotsPath screenShotsPath}
	 *                         setter to set the path where you want to store the
	 *                         screenshots.
	 * @return
	 * @throws Exception
	 */
	public String captureFullPageScreenShot(WebDriver driver, WebElement headerElement, boolean notIncludeHeader,
			String screenShotName) throws Exception {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		// capture the entire page using page chunks approach if the flag is true and
		// browser name is not PhantomJs
		if (browserName.equalsIgnoreCase(BrowserEnums.PhantomJs.toString())) {
			return captureScreenShot(driver, screenShotName);

		} else {
			String tempScreenShotsFolderName = System.getProperty("user.dir") + File.separatorChar + "TempFolder"
					+ File.separatorChar + getScreenShotTime() + "_" + screenShotName;
			folderFileUtil.createFolder(tempScreenShotsFolderName);

			capturePageChunks(driver, tempScreenShotsFolderName, headerElement, true);
			return mergeImagesToSingleImage(tempScreenShotsFolderName, screenShotName + ".png");
		}

	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver           the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param headerElement    the header element
	 * @param notIncludeHeader true/false<br>
	 *                         <font color='blue'>Note: <br>
	 *                         If you want to show any specific header only one full
	 *                         screenshot page mark it as true<br>
	 *                         If this is false, you will see the static header
	 *                         multiple times in the full screenshot</font>
	 * @param screenShotName   the screenshot name <br>
	 *                         Date time Stamp will be <i>prepended</i> to the
	 *                         screenshot name by default.<br>
	 *                         Note: Use {@link #screenShotsPath screenShotsPath}
	 *                         setter to set the path where you want to store the
	 *                         screenshots.
	 * @return
	 * @throws Exception
	 */
	public String captureFullPageScreenShot(WebDriver driver, By byLocator, boolean notIncludeHeader,
			String screenShotName) throws Exception {
		WebElement tempElement = getElement(driver, byLocator);
		return captureFullPageScreenShot(driver, tempElement, notIncludeHeader, screenShotName);

	}

	/**
	 * This method will wait until the file download is completed and waits for
	 * specified max time
	 * 
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param downloadFolderPath  the download folder path
	 *                            {@link BrowserFunctions#getDownloadFolderPath()
	 *                            getDownloadFolderPath}
	 * @param expectedFileName    the expected file name
	 * @param maxTimeOutInSeconds the maximum time script should wait for the
	 *                            download to complete
	 * @throws IOException the exception
	 */
	public void waitUntilDonwloadCompleted(WebDriver driver, String downloadFolderPath, String expectedFileName,
			int maxTimeOutInSeconds) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeOutInSeconds);
		File fileToCheck = new File(downloadFolderPath).toPath().resolve(expectedFileName).toFile();

		wait.until((WebDriver wd) -> fileToCheck.exists());

	}

	/**
	 * This method will wait until the file download is completed and waits for
	 * specified max time
	 * 
	 * @param driver the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @return the down loaded file path
	 * @throws Exception the exception
	 */
	public String waitUntilDonwloadCompleted(WebDriver driver, int maxTimeoutInSeconds) throws Exception {
		// Store the current window handle
		String mainWindow = driver.getWindowHandle();
		String fileName = null;
		try {
			// open a new tab
			((JavascriptExecutor) driver).executeScript("window.open()");
			// switch to new tab
			// Switch to new window opened
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
			if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("chrome")) {
				// navigate to chrome downloads
				driver.get("chrome://downloads");
				Thread.sleep(2000);
				new WebDriverWait(driver, maxTimeoutInSeconds)
						.until(ExpectedConditions.visibilityOf((WebElement) ((JavascriptExecutor) driver).executeScript(
								"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item')")));
				new WebDriverWait(driver, maxTimeoutInSeconds).until(ExpectedConditions
						.elementToBeClickable((WebElement) ((JavascriptExecutor) driver).executeScript(
								"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('a#show')")));
				// get the file name
				fileName = (String) ((JavascriptExecutor) driver).executeScript(
						"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content #file-link').text");

				// file downloaded location
				// donwloadedAt = (String) ((JavascriptExecutor) driver).executeScript(
				// "return
				// document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList
				// downloads-item').shadowRoot.querySelector('div.is-active.focus-row-active
				// #file-icon-wrapper img').src");
			} else if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("firefox")) {

				// navigate to chrome downloads
				driver.get("about:downloads");
				Thread.sleep(2000);
				new WebDriverWait(driver, maxTimeoutInSeconds).until(ExpectedConditions.attributeContains(
						(WebElement) ((JavascriptExecutor) driver).executeScript(
								"return document.querySelector('#contentAreaDownloadsView .downloadMainArea .downloadContainer progress')"),
						"value", "100"));
				// get the file name
				fileName = (String) ((JavascriptExecutor) driver).executeScript(
						"return document.querySelector('#contentAreaDownloadsView .downloadMainArea .downloadContainer description:nth-of-type(1)').value");

				// file downloaded location
				// donwloadedAt = (String) ((JavascriptExecutor) driver).executeScript(
				// "return document.querySelector('#contentAreaDownloadsView .downloadMainArea
				// .downloadTypeIcon').src");
			}

			// close the downloads tab2
			driver.close();
			// switch back to main window
			driver.switchTo().window(mainWindow);

		} catch (Exception e) {
			// switch back to main window
			driver.switchTo().window(mainWindow);
			throw e;

		}
		return fileName;

	}

	/**
	 * gets the element based on the by locater
	 * 
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return WebElement
	 */
	public WebElement getElement(WebDriver driver, By byLocator) {
		return waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE);
	}

	/**
	 * gets the element based on the by locater and max timeout
	 * 
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeOut maximum time to wait for WebElement
	 * @return WebElement
	 */
	public WebElement getElement(WebDriver driver, By byLocator, int maxTimeOut) {
		return waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, maxTimeOut);
	}

	/**
	 * gets the element based on the element
	 * 
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the WebElement
	 * @return WebElement
	 */
	public WebElement getElement(WebDriver driver, WebElement element) {
		return waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE);
	}

	/**
	 * gets the element based on the element and max timeout
	 * 
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the WebElement
	 * @param maxTimeOut maximum time to wait for the element
	 * @return WebElement
	 */
	public WebElement getElement(WebDriver driver, WebElement element, int maxTimeOut) {
		return waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, maxTimeOut);
	}

	/**
	 * gets the bytes for the give images
	 * 
	 * @param imagePath the image path
	 * @return image bytes
	 * @throws Exception the exception
	 */
	public byte[] getImageBytes(String imagePath) throws Exception {
		File file = new File(imagePath);
		BufferedImage bufferedImage = ImageIO.read(file);

		byte[] image = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(bufferedImage, "png", bos);
			image = bos.toByteArray();
		} catch (Exception e) {
		}

		return image;
	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Private Methods
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */

	/**
	 * Executes the JavaScript on the specified element.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param javaScript the java script
	 */
	private void executeJs(WebDriver driver, WebElement element, String javaScript) {

		this.logAccess.getLogger().debug("Executing \"" + javaScript + "\" JavaScript on element :- " + element);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript, element);
	}

	/**
	 * Executes the JavaScript.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param javaScript the java script
	 */
	private void executeJs(WebDriver driver, String javaScript) {
		this.logAccess.getLogger().debug("Executing \"" + javaScript + "\" JavaScript");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript);
	}

	/**
	 * Gets the original style.
	 *
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return the original style
	 */
	private String getOriginalStyle(WebElement element) {
		this.logAccess.getLogger().debug("Gettting original style of element :- " + element);
		return element.getAttribute("style");
	}

	/**
	 * Sets the original style of the element.
	 *
	 * @param driver        the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element       the {@link org.openqa.selenium.WebElement element}
	 * @param originalStyle the original style
	 */
	private void setOriginalStyle(WebDriver driver, WebElement element, String originalStyle) {
		this.logAccess.getLogger().debug("Setting orignial style \"" + originalStyle + "\" to element :- " + element);
		String js = "arguments[0].setAttribute('style', '" + originalStyle + "');";
		executeJs(driver, element, js);
	}

	/**
	 * Wait until element.
	 *
	 * @param driver            the driver
	 * @param element           the element
	 * @param expectedCondition the expected condition<br>
	 *                          * <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 */
	private WebElement waitUntilElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);

		WebElement returnElement = null;
		switch (expectedCondition) {
		case CLICKABLE:
			returnElement = wait.until(ExpectedConditions.elementToBeClickable(element));
			break;
		case VISIBLE:
			returnElement = wait.until(ExpectedConditions.visibilityOf(element));
			break;
		default:
			throw new IllegalArgumentException("??? Unexpected value: " + expectedCondition
					+ ". This method supports clickable and Vislble options. Please use waitUntilElement by locator method for PRESENCE. ???");
		}
		return returnElement;
	}

	/**
	 * Wait until element by locator.
	 *
	 * @param driver            the driver
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition<br>
	 *                          <font color='blue'>Note : Below is the list of
	 *                          options supported for this method
	 *                          <ul>
	 *                          <li>CLICKABLE</li>
	 *                          <li>PRESENCE</li>
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 */
	private WebElement waitUntilElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);

		WebElement returnElement = null;
		switch (expectedCondition) {
		case CLICKABLE:
			returnElement = wait.until(ExpectedConditions.elementToBeClickable(byLocator));
			break;
		case PRESENCE:
			returnElement = wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
			break;
		case VISIBLE:
			returnElement = wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
			break;
		default:
			throw new IllegalArgumentException("????Unexpected value: " + expectedCondition
					+ ". This method supports clickable and Presence options. Please use waitUntilElement by locator method for VISIBLE.");
		}
		return returnElement;
	}

	/**
	 * Capture multiple images in chunks one per each window height and then capture
	 * last screenshot if any part is left over. This will also handle the element
	 * to be hidden.
	 * 
	 * @param driver               the driver
	 * @param tempImagesFolderPath temporary folder where the images should be
	 *                             stored
	 * @param hideElement          header element that need to hidden based on the
	 *                             notIncludeHeader flag
	 * @param notIncludeHeader     not included the header based on this<br>
	 *                             <font color='blue'>Note : First screenshot will
	 *                             show the header and will be hidden in the
	 *                             subsequent screenshots if you choose <b>true</b>
	 *                             and oppositive holds good too</font>
	 * @throws Exception exception
	 */
	private void capturePageChunks(WebDriver driver, String tempImagesFolderPath, WebElement hideElement,
			boolean notIncludeHeader) throws Exception {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		// get the current scroll position
		int currentXPosition = ((Number) js.executeScript("return window.pageXOffset")).intValue();
		int currentYPosition = ((Number) js.executeScript("return window.pageYOffset")).intValue();

		// Scroll right to the top
		js.executeScript("window.scrollTo(0,0)");
		TakesScreenshot screenCapture = ((TakesScreenshot) driver);

		// Get the height of browser window
		int windowHeight = ((Number) js.executeScript("return window.innerHeight")).intValue();

		// Get the total height of the page
		int pageHeight = ((Number) js.executeScript("return document.body.scrollHeight")).intValue();

		// Calculate the number of full screen shots
		double fullFraction = pageHeight / windowHeight;
		int fullShots = (int) fullFraction; // this simply removes the decimals

		// decide the start index based on the window and page height
		int startIndex = (pageHeight == windowHeight) ? 1 : 0;

		// Calculate our scroll script
		String script = "window.scrollBy(0," + String.valueOf(windowHeight - 5) + ")";
		// get the header element style
		String originalStyle = null;
		if (hideElement != null) {
			originalStyle = hideElement.getAttribute("style");
		}
		int coveredHeight = 0;
		for (int screenshotIndex = startIndex; screenshotIndex < fullShots; screenshotIndex++) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			folderFileUtil.copyFile(tmpFile, new File(tempImagesFolderPath + File.separatorChar
					+ String.valueOf(screenshotIndex * windowHeight) + ".png"));
			// scroll to the next chunk
			js.executeScript(script);
			coveredHeight = coveredHeight + (windowHeight - 5);
			Thread.sleep(500);
			if (hideElement != null && screenshotIndex == 0 && notIncludeHeader) {
				// hide the header
				String javaScript = "arguments[0].setAttribute('style', 'display:none;');";
				js.executeScript(javaScript, hideElement);
			}
		}
		// get last page chunk
		int lastChunkHeight = pageHeight - coveredHeight;
		// get the last part of the page if there is any chunk left over
		if (lastChunkHeight > 0) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			BufferedImage lastPageBufferImage = ImageIO.read(tmpFile);
			// get the image vs window px ratio
			double pxRatio = Math.round(((double) lastPageBufferImage.getHeight() / (double) windowHeight) * 100.0)
					/ 100.0;
			// capture the small chunk
			BufferedImage lastChunk = lastPageBufferImage.getSubimage(0, (int) (lastPageBufferImage.getHeight()
					- (lastPageBufferImage.getHeight() * (lastChunkHeight * pxRatio)) / (windowHeight * pxRatio)),
					lastPageBufferImage.getWidth(), (int) (lastChunkHeight * pxRatio));

			ImageIO.write(lastChunk, "png", tmpFile);
			folderFileUtil.copyFile(tmpFile,
					new File(tempImagesFolderPath + File.separatorChar + "lastScreenshot.png"));
		}
		if (notIncludeHeader) {
			String javaScript = "arguments[0].setAttribute('style', '" + originalStyle + "');";
			js.executeScript(javaScript, hideElement);
		}
		// set back to the original position
		js.executeScript(
				"window.scrollTo(" + String.valueOf(currentXPosition) + "," + String.valueOf(currentYPosition) + ");");

	}

	/**
	 * * Capture multiple images in chunks one per each window height and then
	 * capture last screenshot if any part is left over
	 * 
	 * @param driver               the driver
	 * @param tempImagesFolderPath temporary folder where the images should be
	 *                             stored
	 * @throws Exception exception
	 */

	private void capturePageChunks(WebDriver driver, String tempImagesFolderPath) throws Exception {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		// get the current scroll position
		int currentXPosition = ((Number) js.executeScript("return window.pageXOffset")).intValue();
		int currentYPosition = ((Number) js.executeScript("return window.pageYOffset")).intValue();

		// Scroll right to the top
		js.executeScript("window.scrollTo(0,0)");
		TakesScreenshot screenCapture = ((TakesScreenshot) driver);

		// Get the height of browser window
		int windowHeight = ((Number) js.executeScript("return window.innerHeight")).intValue();

		// Get the total height of the page
		int pageHeight = ((Number) js.executeScript("return document.body.scrollHeight")).intValue();

		// Calculate the number of full screen shots
		double fullFraction = pageHeight / windowHeight;
		int fullShots = (int) fullFraction; // this simply removes the decimals

		// decide the start index based on the window and page height
		int startIndex = (pageHeight == windowHeight) ? 1 : 0;
		// Calculate our scroll script
		String script = "window.scrollBy(0," + String.valueOf(windowHeight - 5) + ")";
		int coveredHeight = 0;
		for (int screenshotIndex = startIndex; screenshotIndex <= fullShots; screenshotIndex++) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			folderFileUtil.copyFile(tmpFile, new File(tempImagesFolderPath + File.separatorChar
					+ String.valueOf(screenshotIndex * windowHeight) + ".png"));
			coveredHeight = coveredHeight + (windowHeight - 5);
			// scroll to the next chunk
			js.executeScript(script);
			Thread.sleep(500);
		}
		// get last page chunk
		int lastChunkHeight = pageHeight - coveredHeight;
		// get the last part of the page if there is any chunk left over
		if (lastChunkHeight > 0) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			BufferedImage lastPageBufferImage = ImageIO.read(tmpFile);
			// get the image vs window px ratio
			double pxRatio = Math.round(((double) lastPageBufferImage.getHeight() / (double) windowHeight) * 100.0)
					/ 100.0;
			// capture the small chunk
			BufferedImage lastChunk = lastPageBufferImage.getSubimage(0,
					lastPageBufferImage.getHeight()
							- (lastPageBufferImage.getHeight() * lastChunkHeight) / windowHeight,
					lastPageBufferImage.getWidth(), (int) (lastChunkHeight * pxRatio));

			ImageIO.write(lastChunk, "png", tmpFile);
			folderFileUtil.copyFile(tmpFile,
					new File(tempImagesFolderPath + File.separatorChar + "lastScreenshot.png"));
		}

		// set back to the original position
		js.executeScript(
				"window.scrollTo(" + String.valueOf(currentXPosition) + "," + String.valueOf(currentYPosition) + ");");
	}

	/**
	 * Stitch all the screenshots located in the temporary images folder
	 * 
	 * @param tempImagesFolderPath temporary images folder path
	 * @param outputPNGFileName    output .png image file name
	 * @return
	 * @throws Exception exception
	 */
	private String mergeImagesToSingleImage(String tempImagesFolderPath, String outputPNGFileName) throws Exception {

		// access the images folder
		File folder = new File(tempImagesFolderPath);
		// get all images from folder
		File[] imagesList = folder.listFiles();
		// sort the images based on created time stamp
		Arrays.sort(imagesList, Comparator.comparingLong(File::lastModified));
		// get the first image (need this to get the width and height of each image)
		BufferedImage firstImage = ImageIO.read(imagesList[0]);
		// get image width
		int imageWidth = firstImage.getWidth();
		// get image height (need this to build the BufferedImage)
		int imageHeight = firstImage.getHeight();
		// get the final Buffer Image height
		// bufferedImageHeight = ((Number Of Images-1) * Image Height)+ LastImageHeight

		// get last image to get it's height
		int bufferedImageHeight;
		if (imagesList.length > 0) {
			BufferedImage lastImage = ImageIO.read(imagesList[imagesList.length - 1]);
			bufferedImageHeight = ((imagesList.length - 1) * imageHeight) + lastImage.getHeight();
		} else {
			bufferedImageHeight = firstImage.getHeight();
		}

		// create Buffered Image

		BufferedImage finalBufferedImage = new BufferedImage(imageWidth - 30, bufferedImageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = finalBufferedImage.getGraphics();
		// X axis where the image should be incorporated
		int imageXOfffset = 0;
		// Y axis where the image should be incorporated (this will get updated after
		// each image added to the Buffered Image.
		int imageYOfffset = 0;
		// loop through all images and append them to buffered image
		for (File image : imagesList) {
			// read image into the temporary Buffered Image
			BufferedImage bufferedImage = ImageIO.read(image);

			// add the image to the final Buffered Image graphics
			graphics.drawImage(
					bufferedImage.getSubimage(0, 0, bufferedImage.getWidth() - 30, bufferedImage.getHeight()),
					imageXOfffset, imageYOfffset, null);
			// update Y axis value (so that the next image will be added at the end)
			// moving the next image 10 pixels up to make sure the images does not show
			// any gap
			imageYOfffset += bufferedImage.getHeight() - 5;
		}
		String failedScreenShotPath = this.screenShotsPath + File.separatorChar + getScreenShotTime() + "_"
				+ outputPNGFileName;
		// Save the the final Buffered Image build with graphics to output file
		ImageIO.write(finalBufferedImage, "png", new File(failedScreenShotPath));
		folderFileUtil.deleteFolder(folder);
		return failedScreenShotPath;

	}

}