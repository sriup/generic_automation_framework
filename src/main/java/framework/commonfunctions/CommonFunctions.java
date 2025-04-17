package framework.commonfunctions;

import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.enums.ExpectedConditionsEnums;
import framework.logs.LogAccess;
import framework.utilities.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.*;

/**
 * All the methods related to the common operations will be handled in this
 * class.
 */
public class CommonFunctions {

	/**
	 * LogAccess object.
	 */
	private final LogAccess logAccess;
	/**
	 * DateTimeUtil object.
	 */
	private final DateTimeUtil dateTimeUtil;
	/**
	 * GenericUtil object
	 */
	private final GenericUtil genericUtil;
	/**
	 * FolderFileUtil object.
	 */
	private final FolderFileUtil folderFileUtil;
	/**
	 * The Excel Utility.
	 */
	private final ExcelUtil excelUtil;
	/**
	 * The Fake Data Utility.
	 */
	private final FakeDataUtil fakeDataUtil;
	/**
	 * The Security Utility.
	 */
	private final SecurityUtil securityUtil;
	/**
	 * The Zip Utility.
	 */
	private final ZipUtil zipUtil;

	private final ApiMethods apiMethods;

	public ApiMethods getApiMethods() {
		return apiMethods;
	}

	/**
	 * Folder path where the captured screenshots should be stored.
	 */
	private String screenShotsPath;
	private String highlightBgColor = "orange";

	private String downloadFolderPath = null;

	private CsvUtil csvUtil;

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
	public CommonFunctions(String screenShotsPath, LogAccess logAccess, String downloadFolderPath) {
		this.logAccess = logAccess;
		this.screenShotsPath = screenShotsPath;
		dateTimeUtil = new DateTimeUtil(logAccess);
		folderFileUtil = new FolderFileUtil(logAccess);
		excelUtil = new ExcelUtil(logAccess);
		fakeDataUtil = new FakeDataUtil(logAccess);
		securityUtil = new SecurityUtil();
		zipUtil = new ZipUtil(logAccess);
		genericUtil = new GenericUtil();
		apiMethods = new ApiMethods(logAccess);
		this.downloadFolderPath = downloadFolderPath;
		this.csvUtil = new CsvUtil(logAccess);

	}

	/**
	 * Gets the dateTimeUtil object.
	 *
	 * @return the instance of DateTimeUtils
	 */
	public DateTimeUtil getDateTimeUtil() {
		return dateTimeUtil;
	}

	/**
	 * Gets the folderFileUtil object.
	 *
	 * @return the folderFileUtil
	 */
	public FolderFileUtil getFolderFileUtil() {
		return folderFileUtil;
	}

	/**
	 * Gets the excelUtil.
	 *
	 * @return the excelUtil
	 */
	public ExcelUtil getExcelUtil() {
		return excelUtil;
	}

	/**
	 * Gets the csvUtil
	 *
	 * @return the csvUtil
	 */
	public CsvUtil getCsvUtil() { return csvUtil; }

	/**
	 * Gets the FakeDataUtil.
	 *
	 * @return the FakeDataUtil
	 */
	public FakeDataUtil getFakeDataUtil() {
		return fakeDataUtil;
	}

	/**
	 * Gets the SecurityUtil.
	 *
	 * @return the SecurityUtil
	 */
	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}

	/**
	 * Gets the ZipUtil.
	 *
	 * @return the ZipUtil
	 */
	public ZipUtil getZipUtil() {
		return this.zipUtil;
	}

	/**
	 * Gets the Generic Util
	 *
	 * @return the GenericUtil
	 */
	public GenericUtil genericUtil() {
		return this.genericUtil;
	}

	/**
	 * Sets the highlight color
	 *
	 * @param color the background color for highlight
	 */
	public void setHighlightColor(String color) {
		this.highlightBgColor = color;
	}

	/**
	 * Gets the screenshot path
	 *
	 * @return the screenshot path
	 */

	public String getScreenShotsPath() {
		return screenShotsPath;
	}

	/**
	 * Sets the screenshot path
	 *
	 * @param screenShotsPath the screenshot path
	 */
	public void setScreenShotsPath(String screenShotsPath) {
		this.screenShotsPath = screenShotsPath;
	}

	/**
	 * Sets the date time format for the screenshot. <br>
	 * <font color="blue">Note:<i> This will be prepended to the screenshot
	 * name.</i></font>
	 *
	 * @return the screen shot time
	 */
	public String getScreenShotTime() {
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
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition)
			throws Exception {
		return waitForElement(driver, element, expectedCondition, CommonVariables.MED_TIMEOUT, true);
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
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return the {@link org.openqa.selenium.WebElement element}
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition, boolean isScrollElementToCenter)
			throws Exception {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + element);
		return waitUntilElement(driver, element, expectedCondition, CommonVariables.MED_TIMEOUT,isScrollElementToCenter);
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
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
									 int maxTimeout) throws Exception {
		return waitForElement(driver, element, expectedCondition, maxTimeout, true);
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
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return the {@link org.openqa.selenium.WebElement element}
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
									 int maxTimeout, boolean isScrollElementToCenter) throws Exception {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + element);
		return waitUntilElement(driver, element, expectedCondition, maxTimeout, isScrollElementToCenter);
	}
	
	/**
	 * Web driver wait
	 *
	 * @param driver	the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param maxTimeout	the max timeout in seconds
	 * @return	the webDriverWait
	 */
	public WebDriverWait webDriverWait(WebDriver driver, int maxTimeout){
		
		Duration duration = Duration.ofSeconds(maxTimeout);
		
		return new WebDriverWait(driver, duration);
	}
	
	/**
	 * Wait for alert.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param maxTimeout the max timeout in seconds
	 */
	public Alert waitForAlert(WebDriver driver, int maxTimeout) {
		this.logAccess.getLogger().debug("waiting for alert");
		
		WebDriverWait wait = webDriverWait(driver, maxTimeout);
		
		return wait.ignoring(NoAlertPresentException.class).until(ExpectedConditions.alertIsPresent());
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
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @return the web element
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition)
			throws Exception {
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
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return the web element
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition, boolean isScrollElementToCenter)
			throws Exception {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, CommonVariables.MED_TIMEOUT, isScrollElementToCenter);
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
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
									 int maxTimeout) throws Exception {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitForElement(driver, byLocator, expectedCondition, maxTimeout, true);
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
	 *                          <li>VISIBLE</li>
	 *                          </ul>
	 *                          </font>
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 * @throws Exception the exception
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
									 int maxTimeout, boolean isScrollElementToCenter) throws Exception {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, maxTimeout, isScrollElementToCenter);
	}
	
	/**
	 * Wait for element(s) visible.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param elements   the list of {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeOut Maximum time to wait for the element(s) visible
	 * @return List of WebElements
	 */
	public List<WebElement> waitForElementsToVisible(WebDriver driver, List<WebElement> elements, int maxTimeOut) {
		this.logAccess.getLogger().debug("waiting for all specified elements in the list to be visible" + elements);
		return (webDriverWait(driver, maxTimeOut)).until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	/**
	 * Wait for element(s) visible.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the locator for the element(s)
	 * @param maxTimeOut Maximum time to wait for the element(s) visible
	 * @return List of WebElements
	 */
	public List<WebElement> waitForElementsToVisible(WebDriver driver, By byLocator, int maxTimeOut) {
		this.logAccess.getLogger().debug("waiting for all specified elements in the list to be visible " + byLocator);
		return (webDriverWait(driver, maxTimeOut))
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byLocator));
	}

	/**
	 * Wait for invisibility of element. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @throws Exception the exception
	 */
	public boolean waitForInvisibilityOfElement(WebDriver driver, WebElement element, int maxTimeout) throws Exception {
		return waitForInvisibilityOfElement(driver, element, CommonVariables.MIN_TIMEOUT, maxTimeout);
	}

	/**
	 * Wait for invisibility of element. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver          the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element         the {@link org.openqa.selenium.WebElement element}
	 * @param initialWaitTime the time to be waited for the element to be present in
	 *                        *						seconds
	 * @param maxTimeout      the max timeout in seconds
	 * @return the status of element invisibility<br>
	 * <b>Note:</b><br>
	 * <ul>
	 * 	 <li>true: if the element is invisible</li>
	 * 	 <li>false: if the element is visible</li>
	 * </ul>
	 * @throws Exception the exception
	 */
	public boolean waitForInvisibilityOfElement(WebDriver driver, WebElement element, int initialWaitTime, int maxTimeout) throws Exception {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + element);

		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, initialWaitTime);
		} catch (Exception ignoreException) {
			// ignore the exception and continue with the script
		}

		long currentTimestamp = (new Date()).getTime();
		int waitingSeconds = maxTimeout * 1000;
		long endTimestamp = currentTimestamp + waitingSeconds;

		boolean isElementVisible = true;

		this.logAccess.getLogger().info("End timestamp for Invisibility of an Element is " + endTimestamp);

		try {

			do {

				isElementVisible = isElementPresent(driver, element, CommonVariables.NO_TIMEOUT);

				if (isElementVisible) {
					// Checking if element is visible though it is in the DOM.
					isElementVisible = element.isDisplayed();

				}
				Thread.sleep(500);
			} while ((new Date()).getTime() < endTimestamp && isElementVisible);
		} catch (NoSuchElementException | StaleElementReferenceException ignoreException) {
			// intentionally left it blank (we can ignore the above exceptions when waiting
			// for element in-visibility)
		}
		return !isElementVisible;
	}


	/**
	 * Wait for invisibility of element by locator. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the time to be waited for the element to be present in
	 *                   *						seconds
	 * @throws Exception the exception
	 */
	public boolean waitForInvisibilityOfElement(WebDriver driver, By byLocator, int maxTimeout) throws Exception {
		return waitForInvisibilityOfElement(driver, byLocator, CommonVariables.MIN_TIMEOUT, maxTimeout);
	}

	/**
	 * Wait for invisibility of element by locator. Method will wait for
	 * {@link CommonVariables#MIN_TIMEOUT} before checking for element invisibility.
	 *
	 * @param driver          the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator       the by locator
	 * @param initialWaitTime the time to be waited for the element to be present in
	 *                        seconds
	 * @param maxTimeout      the max timeout in seconds
	 * @throws Exception the exception
	 */
	public boolean waitForInvisibilityOfElement(WebDriver driver, By byLocator, int initialWaitTime, int maxTimeout)
			throws Exception {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + byLocator);

		try {
			waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE, initialWaitTime);
		} catch (Exception ignoreException) {
			// ignore the exception and continue with the script
		}

		long currentTimestamp = (new Date()).getTime();
		int waitingSeconds = maxTimeout * 1000;
		long endTimestamp = currentTimestamp + waitingSeconds;

		boolean isElementVisible = true;

		this.logAccess.getLogger().info("End timestamp for Invisibility of an Element is " + endTimestamp);
		try {
			do {

				isElementVisible = isElementPresent(driver, byLocator, CommonVariables.NO_TIMEOUT);

				if (isElementVisible) {
					// Checking if element is visible though it is in the DOM.
					isElementVisible = isElementDisplayed(driver, byLocator, CommonVariables.NO_TIMEOUT);
				}
				Thread.sleep(500);
			} while ((new Date()).getTime() < endTimestamp && isElementVisible);
		} catch (NoSuchElementException | StaleElementReferenceException ignoreException) {
			// intentionally left it blank (we can ignore the above exceptions when waiting
			// for element in-visibility)
		}
		return !isElementVisible;
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
			WebElement targetEle = waitForElement(driver, element, ExpectedConditionsEnums.PRESENCE, CommonVariables.MED_TIMEOUT);
			// return true if the element is present
			return targetEle != null;

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
			WebElement targetEle = waitForElement(driver, element, ExpectedConditionsEnums.PRESENCE, maxTimeout);

			// return true if the element is present
			return targetEle != null;

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
			WebElement targetEle = waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, CommonVariables.MED_TIMEOUT);

			// return true if the /element is visible
			return targetEle != null;

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
			WebElement targetEle = waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, maxTimeout);
			// return true if the /element is visible
			return targetEle != null;
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
			WebElement targetEle = waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE, maxTimeout);

			// return true if the element is visible
			return targetEle != null;

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
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return true, if is element displayed by locator
	 */
	public boolean isElementDisplayed(WebDriver driver, By byLocator, int maxTimeout, boolean isScrollElementToCenter) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + byLocator);
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			WebElement targetEle = waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE, maxTimeout, isScrollElementToCenter);
			
			// return true if the element is visible
			return targetEle != null;
			
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
		return isElementDisplayed(driver, byLocator, CommonVariables.MED_TIMEOUT, true);
	}
	
	/**
	 * Checks if is element displayed.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, By byLocator, boolean isScrollElementToCenter) {
		return isElementDisplayed(driver, byLocator, CommonVariables.MED_TIMEOUT, isScrollElementToCenter);
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
		return isElementDisplayed(driver, element, maxTimeout, true);
	}
	
	/**
	 * Checks if is element displayed.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout in seconds
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, WebElement element, int maxTimeout, boolean isScrollElementToCenter) {
		this.logAccess.getLogger().info("checking if element is displayed  :- " + element);
		try {
			WebElement targetEle = waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, maxTimeout, isScrollElementToCenter);
			// return true if the element is visible
			return targetEle != null;
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
		return isElementDisplayed(driver, element, CommonVariables.MED_TIMEOUT, true);
	}
	
	/**
	 * Checks if is element displayed.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, WebElement element, boolean isScrollElementToCenter) {
		return isElementDisplayed(driver, element, CommonVariables.MED_TIMEOUT, isScrollElementToCenter);
	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(WebDriver driver, WebElement element) {
		return isElementEnabled(driver, element, CommonVariables.MED_TIMEOUT);

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
		long endTimestamp = currentTimestamp + maxTimeout * 1000L;
		boolean conditionalCheck = false;
		do {
			conditionalCheck = element.isEnabled();
		} while ((new Date()).getTime() < endTimestamp && !conditionalCheck);
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
	 * @throws Exception the exception
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
	 * @throws Exception the exception
	 */
	public boolean isElementEnabled(WebDriver driver, By byLocator, int maxTimeout) throws Exception {
		this.logAccess.getLogger().info("checking if element is enabled  :- " + byLocator);
		long currentTimestamp = (new Date()).getTime();
		long endTimestamp = currentTimestamp + maxTimeout * 1000L;
		boolean conditionalCheck = false;
		do {
			conditionalCheck = getElement(driver, byLocator).isEnabled();
			Thread.sleep(500);
		} while ((new Date()).getTime() < endTimestamp && !conditionalCheck);
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
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, WebElement element) throws Exception {
		return highlightElement(driver, element, false, true);
	}
	
	/**
	 * Highlights the element.
	 *
	 * @param driver      the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element     the {@link org.openqa.selenium.WebElement element} <br>
	 *                    This method will highlight the element and does not set back
	 *                    the original style. <br>
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @param bordersOnly the border highlight status<br>
	 *                    			 <ul>
	 *                    			 	<li>true - will highlight the elements borders only.</li>
	 *                      		   	<li>false - will highlight the element using normal highlighting
	 *                    				 	i.e. the entire element background.
	 *                    			 	</li>
	 *                    			 </ul>
	 *                    <font color="blue"><b>Note:</b> Use {@link #unHighlightElement
	 *                    			 unHighlightElement} method to set back the original style of
	 *                    			 the element.</font>
	 * @
	 * @return the String with original style of the element
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, WebElement element, boolean bordersOnly, boolean isScrollElementToCenter) throws Exception {
		String originalStyle = "";
		try {
			this.logAccess.getLogger().debug("Highlighting element" + (bordersOnly ? " border" : "") + ":- " + element);
			// get the original
			originalStyle = getOriginalStyle(element);
			// scroll element to the center
			if(isScrollElementToCenter) scrollElement(driver, element, "center");
			String highlightJavaScript;
			if (bordersOnly) {
				// highlight the web element
				highlightJavaScript = "arguments[0].style.border=\"3px solid \"" + this.highlightBgColor + "\";";
			} else {
				// highlight the web element
				highlightJavaScript = "arguments[0].style.background=\"" + this.highlightBgColor + "\";";
			}
			executeJs(driver, element, highlightJavaScript);
		} catch (Exception e) {
			//ignore exception as sometimes the element might either not exist
			// or might get refreshed
		}
		return originalStyle;
	}
	
	/**
	 * Highlights the element.
	 *
	 * @param driver      the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element     the {@link org.openqa.selenium.WebElement element} <br>
	 *                    This method will highlight the element and does not set back
	 *                    the original style. <br>
	 * @param bordersOnly the border highlight status<br>
	 *                    			 <ul>
	 *                    			 	<li>true - will highlight the elements borders only.</li>
	 *                      		   	<li>false - will highlight the element using normal highlighting
	 *                    				 	i.e. the entire element background.
	 *                    			 	</li>
	 *                    			 </ul>
	 *                    <font color="blue"><b>Note:</b> Use {@link #unHighlightElement
	 *                    			 unHighlightElement} method to set back the original style of
	 *                    			 the element.</font>
	 * @return the String with original style of the element
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, WebElement element, boolean bordersOnly) throws Exception {
		
		return highlightElement(driver, element, bordersOnly, true);
	}

	/**
	 * Highlights the element.
	 *
	 * @param driver      the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator   the by locator <br>
	 *                    This method will highlight the element and does not set back
	 *                    the original style. <br>
	 * @param bordersOnly the border highlight status<br>
	 *                    <ul>
	 *                    	<li>true - will highlight the elements borders only.</li>
	 *                    	<li>false - will highlight the element using normal highlighting
	 *                     	i.e. the entire element background.
	 *                    	</li>
	 *                    </ul>
	 *                     <font color="blue"><b>Note:</b> Use
	 *                     {@link #unHighlightElement unHighlightElement} method to set
	 *                     back the original style of the element.</font>
	 * @return the String with original style of the element
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, By byLocator, boolean bordersOnly) throws Exception {
		this.logAccess.getLogger().debug("Highlighting element :- " + byLocator);
		// highlight the element and return the original style
		return highlightElement(driver, getElement(driver, byLocator), bordersOnly);
	}
	
	
	/**
	 * Highlights the element.
	 *
	 * @param driver      the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator   the by locator <br>
	 *                    This method will highlight the element and does not set back
	 *                    the original style. <br>
	 * @param bordersOnly the border highlight status<br>
	 *                    <ul>
	 *                    	<li>true - will highlight the elements borders only.</li>
	 *                    	<li>false - will highlight the element using normal highlighting
	 *                     	i.e. the entire element background.
	 *                    	</li>
	 *                    </ul>
	 *                     <font color="blue"><b>Note:</b> Use
	 *                     {@link #unHighlightElement unHighlightElement} method to set
	 *                     back the original style of the element.</font>
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return the String with original style of the element
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, By byLocator, boolean bordersOnly, boolean isScrollElementToCenter) throws Exception {
		this.logAccess.getLogger().debug("Highlighting element :- " + byLocator);
		// highlight the element and return the original style
		return highlightElement(driver, getElement(driver, byLocator), bordersOnly, isScrollElementToCenter);
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
	 * @throws Exception the exception
	 */
	public String highlightElement(WebDriver driver, By byLocator) throws Exception {
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
	 * @throws Exception the exception
	 */
	public void unHighlightElement(WebDriver driver, WebElement element, String originalStyle) throws Exception {
		this.logAccess.getLogger().debug("Un-highlighting element  :- " + element);
		// set element original style
		try {
			setOriginalStyle(driver, element, originalStyle);
		} catch (Exception e) { // TODO need to track this not intractable
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
	 * @throws Exception the exception
	 */
	public void unHighlightElement(WebDriver driver, By byLocator, String originalStyle) throws Exception {
		this.logAccess.getLogger().debug("Un-highlighting element  :- " + byLocator);
		unHighlightElement(driver, getElement(driver, byLocator), originalStyle);
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
		WebElement tempElement = getElement(driver, element);
		// get element original style
		String originalStyle = getOriginalStyle(tempElement);
		for (int highlightIndex = 1; highlightIndex <= numberOfTimes; highlightIndex++) {
			highlightElement(driver, tempElement);
			Thread.sleep(300);
			unHighlightElement(driver, tempElement, originalStyle);
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
		clickOnElement(driver, element, isCaptureScreenShot, captureBefore, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	// click element
	public void clickOnElement(WebDriver driver, WebElement element, boolean isCaptureScreenShot, boolean captureBefore,
							   String screenShotName, int maxTimeOut) throws Exception {
		this.logAccess.getLogger().info("Clicking on element  :- " + element);

		WebElement tempElement = getElement(driver, element, maxTimeOut);
		// highlight element
		String originalStyle = highlightElement(driver, tempElement);
		// capture before (private capture screenshot)
		if (isCaptureScreenShot && captureBefore) {
			// take screenshot
			captureScreenShot(driver, screenShotName);
		}
		// un-highlight
		unHighlightElement(driver, tempElement, originalStyle);

		tempElement.click();

		// capture after (private capture screenshot)
		if (isCaptureScreenShot && !captureBefore) {

			// highlight element
			originalStyle = highlightElement(driver, tempElement);

			// take screenshot
			captureScreenShot(driver, screenShotName);
			// un-highlight
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
		clickOnElement(driver, byLocator, isCaptureScreenShot, captureBefore, screenShotName, CommonVariables.MED_TIMEOUT);
	}


	/**
	 * Click on element.
	 *
	 * @param driver              the driver
	 * @param byLocator           the by locator
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param captureBefore       the capture before
	 * @param screenShotName      the screen shot name
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void clickOnElement(WebDriver driver, By byLocator, boolean isCaptureScreenShot, boolean captureBefore,
							   String screenShotName, int maxTimeOut) throws Exception {
		WebElement element = getElement(driver, byLocator);
		clickOnElement(driver, element, isCaptureScreenShot, captureBefore, screenShotName, maxTimeOut);
	}

	/**
	 * Input file path in file upload/browse.
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param value               the absolute path of the file to be uploaded
	 * @param isCaptureScreenshot toggle to capture screenshot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @throws Exception the exception
	 */
	public void browseFile(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
						   String screenShotName) throws Exception {

		this.logAccess.getLogger().info("Element :- " + element);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// enter value in the field
		element.sendKeys(value);

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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void inputValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
						   String screenShotName, int maxTimeOut) throws Exception {

		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);

		if (isElementEnabled(driver, tempElement)) {

			// Check if the tag name is input, there are cases where we can enter the
			// data but the field is not input and it's not possible to check the type
			// attribute in those scenarios.
			if (tempElement.getAttribute("tagName").equalsIgnoreCase("input") && tempElement.getAttribute("type").equals("password")) {
				this.logAccess.getLogger().info("Masked value :- " + "x".repeat(value.length()));
			} else {
				this.logAccess.getLogger().info("value :- " + value);
			}

			// click in the field
			try {
				tempElement.click();
			} catch (ElementClickInterceptedException enie) {
				executeJs(driver, tempElement, "arguments[0].click()");
			}
			// clear any existing values
			tempElement.clear();
			// enter value in the field
			tempElement.sendKeys(value);

			// highlight element
			String originalStyle = highlightElement(driver, tempElement);
			// capture (private capture screenshot)
			if (isCaptureScreenshot) {
				captureScreenShot(driver, screenShotName);
			}
			// un-highlight
			unHighlightElement(driver, tempElement, originalStyle);
		} else {
			throw new ElementNotInteractableException(tempElement + " is disabled.");
		}
	}

	/**
	 * Input value.
	 *
	 * @param driver              the driver
	 * @param byLocator           the by locator
	 * @param value               the value
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screen shot name
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void inputValue(WebDriver driver, By byLocator, String value, boolean isCaptureScreenshot,
						   String screenShotName, int maxTimeOut) throws Exception {
		WebElement element = getElement(driver, byLocator);
		inputValue(driver, element, value, isCaptureScreenshot, screenShotName, maxTimeOut);
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
		inputValue(driver, byLocator, value, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
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

		// un-highlight
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
	 * @return WebElement List of selected list items
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

		// un-highlight
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
	 * @param element             the {@link org.openqa.selenium.WebElement element}
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
	 * @return WebElement List of selected list items
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByIndex(WebDriver driver, WebElement element, int index, boolean isCaptureScreenshot,
								  String screenShotName, int maxTimeOut) throws Exception {
		this.logAccess.getLogger().info("Index :-  " + index);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);

		// select item by index
		Select listElement = new Select(tempElement);
		listElement.selectByIndex(index);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlight
		unHighlightElement(driver, tempElement, originalStyle);
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
		selectItemByIndex(driver, element, index, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
								  String screenShotName, int maxTimeOut) throws Exception {
		this.logAccess.getLogger().info("Value :-  " + value);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);

		// select item by value
		Select dropDown = new Select(tempElement);
		dropDown.selectByValue(value);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlight
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
		selectItemByValue(driver, element, value, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum timeout to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByVisibleText(WebDriver driver, WebElement element, String visibleText,
										boolean isCaptureScreenshot, String screenShotName, int maxTimeOut) throws Exception {
		this.logAccess.getLogger().info("Visible Text :-  " + visibleText);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);

		// select item by visible text
		Select dropDown = new Select(tempElement);
		dropDown.selectByVisibleText(visibleText);

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlight
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
		selectItemByVisibleText(driver, element, visibleText, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByPartialVisibleText(WebDriver driver, WebElement element, String partialVisibleText,
											   boolean isCaptureScreenshot, String screenShotName, int maxTimeOut) throws Exception {
		this.logAccess.getLogger().info("Partial visible Text :-  " + partialVisibleText);
		this.logAccess.getLogger().info("Element :- " + element);
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);
		WebElement option = tempElement
				.findElement(By.xpath("./option[contains(text(),'" + partialVisibleText + "')]"));
		option.click();

		// highlight element
		String originalStyle = highlightElement(driver, tempElement);

		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}

		// un-highlight
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
		selectItemByPartialVisibleText(driver, element, partialVisibleText, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByIndex(WebDriver driver, By byLocator, int index, boolean isCaptureScreenshot,
								  String screenShotName, int maxTimeOut) throws Exception {
		selectItemByIndex(driver, getElement(driver, byLocator), index, isCaptureScreenshot, screenShotName, maxTimeOut);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByValue(WebDriver driver, By byLocator, String value, boolean isCaptureScreenshot,
								  String screenShotName, int maxTimeOut) throws Exception {
		selectItemByValue(driver, getElement(driver, byLocator), value, isCaptureScreenshot, screenShotName, maxTimeOut);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByVisibleText(WebDriver driver, By byLocator, String visibleText, boolean isCaptureScreenshot,
										String screenShotName, int maxTimeOut) throws Exception {
		selectItemByVisibleText(driver, getElement(driver, byLocator), visibleText, isCaptureScreenshot,
				screenShotName, maxTimeOut);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @throws Exception the exception
	 */
	public void selectItemByPartialVisibleText(WebDriver driver, By byLocator, String partialVisibleText,
											   boolean isCaptureScreenshot, String screenShotName, int maxTimeOut) throws Exception {
		selectItemByPartialVisibleText(driver, getElement(driver, byLocator), partialVisibleText, isCaptureScreenshot,
				screenShotName, maxTimeOut);
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
	 * @param maxTimeOut          the maximum time to wait for the element
	 * @return the visible text of this element.
	 * @throws Exception the exception
	 */
	public String getText(WebDriver driver, WebElement element, boolean isCaptureScreenShot, String screenShotName, int maxTimeOut)
			throws Exception {
		this.logAccess.getLogger().info("Getting text form element :- " + element);
		String elementText;
		// get the element
		WebElement tempElement = getElement(driver, element, maxTimeOut);
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
		return getText(driver, element, isCaptureScreenShot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * @param maxTimeOut          the maximum to wait for the element
	 * @return the visible text of this element.
	 * @throws Exception the exception
	 */
	public String getText(WebDriver driver, By byLocator, boolean isCaptureScreenShot, String screenShotName, int maxTimeOut)
			throws Exception {
		this.logAccess.getLogger().info("Getting text form element :- " + byLocator);
		String elementText;
		// get the element
		WebElement tempElement = getElement(driver, byLocator, maxTimeOut);
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
		return getText(driver, byLocator, isCaptureScreenShot, screenShotName, CommonVariables.MED_TIMEOUT);
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
	 * set.
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
	 * @param byLocator           the by locator
	 * @param attributeName       the attribute name
	 * @param isCaptureScreenShot the is capture screen shot
	 * @param screenShotName      the screen shot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default.<br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 * @return the attribute/property's current value or null if the value is not
	 * set.
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
	 * @see <a href=
	 * 'https://www.w3.org/TR/uievents'>https://www.w3.org/TR/uievents/</a> for
	 * more information on the UIEvents.
	 */
	public void jsTriggerEventOnElement(WebDriver driver, WebElement element, String eventType, String eventName) {
		this.logAccess.getLogger().debug("Dispatching " + eventName + " event on element :- " + element);
		String jsFunction = " var clickEvent = document.createEvent ('" + eventType + "');" + "clickEvent.initEvent ('"
				+ eventName + "', true, false); " + "arguments [0].dispatchEvent (clickEvent); ";
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
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element   the {@link org.openqa.selenium.WebElement element} the
	 *                  JavaScript has to dispatch the event.
	 * @param eventName the event name to trigger <b> e.g: </b>onchange, onblur,
	 *                  onclick
	 */
	public void jsTriggerEventOnElement(WebDriver driver, WebElement element, String eventName) {
		this.logAccess.getLogger().debug("Dispatching " + eventName + " event on element :- " + element);
		String jsFunction = " var triggerEvent = document.createEvent ('Event');  triggerEvent.initEvent ('" + eventName
				+ "', true, false); arguments [0].dispatchEvent (triggerEvent); ";
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
		String jsFunction = " var triggerEvent = document.createEvent ('MouseEvents');  triggerEvent.initEvent ('"
				+ mouseEvent + "', true, false); arguments [0].dispatchEvent (triggerEvent); ";
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
		String tempScreenshotName = "";
		//if (!CommonVariables.IS_RUNNING_ON_SBOX) {
			this.logAccess.getLogger().debug("Capturing screenshot for element :- " + element);
			// highlight
			String originalStyle = highlightElement(driver, element);

			// capture screenshot
			tempScreenshotName = captureScreenShot(driver, screenshotName);
			// un-highlight
			setOriginalStyle(driver, element, originalStyle);
		//}
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

		String tempScreenshotName = "";
		//if (!CommonVariables.IS_RUNNING_ON_SBOX) {
			this.logAccess.getLogger().debug("Capturing screenshot for element :- " + byLocator.toString());
			WebElement element = waitForElement(driver, byLocator, ExpectedConditionsEnums.VISIBLE);
			// highlight
			String originalStyle = highlightElement(driver, element);
			// capture screenshot
			tempScreenshotName = captureScreenShot(driver, screenshotName);
			// un-highlight
			setOriginalStyle(driver, element, originalStyle);
		//}
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

		String tempScreenshotName = "";
		//if (!CommonVariables.IS_RUNNING_ON_SBOX) {
			this.logAccess.getLogger().debug("Capturing screenshot");
			tempScreenshotName = this.screenShotsPath + File.separatorChar + getScreenShotTime() + "_"
					+ screenShotName.replaceAll("[^-A-Za-z0-9]", "_").replace("__", "_") + ".png";

			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(tempScreenshotName));
		//}
		return tempScreenshotName;

	}

	/**
	 * Capture full page screenshot.
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
	 * @return the screenshot path
	 * @throws Exception the exception
	 */
	public String captureFullPageScreenShot(WebDriver driver, String screenShotName) throws Exception {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		// capture the entire page using page chunks approach if the flag is true and
		// browser name is not PhantomJs
		if (browserName.equalsIgnoreCase(BrowserEnums.PhantomJs.toString())) {
			return captureScreenShot(driver, screenShotName);
		} else {

			String tempScreenShotsFolderName = screenShotsPath + File.separatorChar + "TempFolder" + File.separatorChar
					+ getScreenShotTime() + "_" + screenShotName;
			folderFileUtil.createFolder(tempScreenShotsFolderName);
			boolean capturedChunks = capturePageChunks(driver, tempScreenShotsFolderName);
			if (capturedChunks) {
				return mergeImagesToSingleImage(tempScreenShotsFolderName,
						screenShotName.replaceAll("[^-A-Za-z0-9]", "_").replace("__", "_") + ".png");
			} else {
				return captureScreenShot(driver, screenShotName);
			}
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
	 * @return the screenshot path
	 * @throws Exception the exception
	 */
	// TODO - Need to revisit this as the latest IDM changes are causing some issues
	public String captureFullPageScreenShot(WebDriver driver, WebElement headerElement, boolean notIncludeHeader,
											String screenShotName) throws Exception {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		// capture the entire page using page chunks approach if the flag is true and
		// browser name is not PhantomJs
		if (browserName.equalsIgnoreCase(BrowserEnums.PhantomJs.toString())) {
			return captureScreenShot(driver, screenShotName);

		} else {
			String tempScreenShotsFolderName = screenShotsPath + File.separatorChar + "TempFolder" + File.separatorChar
					+ getScreenShotTime() + "_" + screenShotName;
			folderFileUtil.createFolder(tempScreenShotsFolderName);

			boolean capturedPageChunks = capturePageChunks(driver, tempScreenShotsFolderName, headerElement, true);
			if (capturedPageChunks) {
				return mergeImagesToSingleImage(tempScreenShotsFolderName,
						screenShotName.replaceAll("[^-A-Za-z0-9]", "_").replace("__", "_") + ".png");
			} else {
				return captureScreenShot(driver, screenShotName);
			}
		}

	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver           the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator        the by locator for the element that shouldn't repeat in the screenshot
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
	 * @return the screenshot path
	 * @throws Exception the exception
	 */
	public String captureFullPageScreenShot(WebDriver driver, By byLocator, boolean notIncludeHeader,
											String screenShotName) throws Exception {
		WebElement tempElement = getElement(driver, byLocator);
		return captureFullPageScreenShot(driver, tempElement, notIncludeHeader, screenShotName);

	}

	/*
	 * This method will wait until the file download is completed and waits for
	 * specified max time
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param maxTimeoutInSeconds the maximum timeout in seconds
	 * @param sessionId           the session id
	 * @return the downloaded file path
	 * @throws Exception the exception
	 */
	public String waitUntilDownloadCompleted(WebDriver driver, int maxTimeoutInSeconds, SessionId sessionId) throws Exception {
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
				System.out.println(driver.getCurrentUrl());
				if(driver.getCurrentUrl().equals("about:blank")){
					break;
				}
			}
			boolean downloadStarted = false;
			JavascriptExecutor js = (JavascriptExecutor) driver;

			if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("chrome")) {
				// navigate to Chrome downloads
				driver.get("chrome://downloads");
				long startTime = (new Date()).getTime();

				while (!downloadStarted && (((new Date().getTime()) - startTime) / 1000) < maxTimeoutInSeconds) {
					try {
						Object downloadedItemsObject = js.executeScript(
								"return document.querySelector('downloads-manager').shadowRoot.querySelectorAll('#downloadsList downloads-item').length");
						downloadStarted = (Integer.parseInt(downloadedItemsObject.toString()) > 0);
					} catch (Exception ignoreException) {
						// do nothing ignore the exception
						// until the element is present
					}
				}
				if (downloadStarted) {
					
					webDriverWait(driver, maxTimeoutInSeconds).until(ExpectedConditions
							.elementToBeClickable((WebElement) ((JavascriptExecutor) driver).executeScript(
									"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content #file-link')")));
					// get the file name
					fileName = (String) ((JavascriptExecutor) driver).executeScript(
							"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content #file-link').text");
					captureScreenShot(driver, "download_file");
					Thread.sleep(1000);
					js.executeScript(
							"document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('button#remove').click()");
				}

				// TODO Need to implement the logic to clear the downloaded file entry from
				// download history in FireFox
			} else if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("firefox")) {

				// navigate to Firefox downloads
				driver.get("about:downloads");
				//Thread.sleep(CommonVariables.MIN_TIMEOUT);
				webDriverWait(driver, maxTimeoutInSeconds)
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".download.download-state")));
				if (driver.findElements(By.cssSelector(".download.download-state")).size() > 0) {
					webDriverWait(driver, maxTimeoutInSeconds).until(ExpectedConditions.visibilityOf(
							driver.findElement(By.cssSelector("button[tooltiptext='Show in Folder']"))));
					// get the file name
					fileName = getAttribute(driver, By.cssSelector(
									"#contentAreaDownloadsView .downloadMainArea .downloadContainer description:nth-of-type(1)"),
							"value", true, "");
				}
				
				// file downloaded location
				// downloadedAt = (String) ((JavascriptExecutor) driver).executeScript(
				// "return document.querySelector('#contentAreaDownloadsView .downloadMainArea
				// .downloadTypeIcon').src");
				
				//
				if(driver.findElements(By.cssSelector(".downloadRemoveFromHistoryMenuItem")).size()>0){
					Actions downloadActions = new Actions(driver);
					downloadActions.contextClick(driver.findElement(By.cssSelector("richlistbox#downloadsListBox"))).perform();
					clickOnElement(driver,  By.cssSelector("menuitem[label='Clear Downloads']"), false, false, fileName + " - Clear Download History");
				}
			}else if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("edge")) {

				driver.get("edge://downloads/all");
				
				webDriverWait(driver, maxTimeoutInSeconds)
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".downloads-list  [role='listitem']")));

				if (driver.findElements(By.cssSelector(".downloads-list  [role='listitem']")).size() > 0) {
					waitForInvisibilityOfElement(driver, By.cssSelector(".downloads-list  [role='listitem'] [role='progressbar']"),maxTimeoutInSeconds);
					// get the file name
					fileName = getAttribute(driver, By.cssSelector(
									".downloads-list  [role='listitem'] button[id^='open_file']"),
							"aria-label", true, "");
				}
			}
			
			
			// close the downloads tab2
			driver.close();
			
			// switch back to main window
			driver.switchTo().window(mainWindow);
			
			if (CommonVariables.EXEC_PLATFORM.equalsIgnoreCase("docker")) {
				((RemoteWebDriver) driver).downloadFile(fileName, Path.of(downloadFolderPath));
				((RemoteWebDriver) driver).deleteDownloadableFiles();
				
			}

		} catch (Exception e) {
			// switch back to main window
			driver.switchTo().window(mainWindow);
			throw e;

		}
		return fileName;

	}


	/**
	 * gets the element based on the by locator
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator) throws Exception {
		return waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE);
	}
	
	/**
	 * gets the element based on the by locator
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, boolean isScrollElementToCenter) throws Exception {
		return waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, isScrollElementToCenter);
	}
	
	
	/**
	 * gets the element based on the by locator and max timeout
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeOut Maximum time to wait for WebElement
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, int maxTimeOut) throws Exception {
		return waitForElement(driver, byLocator, ExpectedConditionsEnums.PRESENCE, maxTimeOut);
	}

	/**
	 * gets the element based on the element
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the WebElement
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element) throws Exception {
		return waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE);
	}
	
	/**
	 * gets the element based on the element
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the WebElement
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element, boolean isScrollElementToCenter) throws Exception {
		return waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, isScrollElementToCenter);
	}

	/**
	 * gets the element based on the locator
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition)
			throws Exception {
		return waitForElement(driver, byLocator, expectedCondition);
	}
	
	/**
	 * gets the element based on the locator
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition, boolean isScrollElementToCenter)
			throws Exception {
		return waitForElement(driver, byLocator, expectedCondition, isScrollElementToCenter);
	}

	/**
	 * gets the element based on the element
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the WebElement
	 * @param expectedCondition the expected condition
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition)
			throws Exception {
		return waitForElement(driver, element, expectedCondition);
	}
	
	/**
	 * gets the element based on the element
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the WebElement
	 * @param expectedCondition the expected condition
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition, boolean isScrollElementToCenter)
			throws Exception {
		return waitForElement(driver, element, expectedCondition, isScrollElementToCenter);
	}

	/**
	 * gets the element based on the locator
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
								 int maxTimeOut) throws Exception {
		return waitForElement(driver, byLocator, expectedCondition, maxTimeOut);
	}
	
	/**
	 * gets the element based on the locator
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
								 int maxTimeOut, boolean isScrollElementToCenter) throws Exception {
		return waitForElement(driver, byLocator, expectedCondition, maxTimeOut);
	}

	/**
	 * gets the element based on the element expected condition
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the WebElement
	 * @param expectedCondition the expected condition
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
								 int maxTimeOut) throws Exception {
		return waitForElement(driver, element, expectedCondition, maxTimeOut);
	}

	/**
	 * gets the element based on the element and max timeout
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the WebElement
	 * @param maxTimeOut Maximum time to wait for the element
	 * @return WebElement
	 * @throws Exception the exception
	 */
	public WebElement getElement(WebDriver driver, WebElement element, int maxTimeOut) throws Exception {
		return waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, maxTimeOut);
	}

	/**
	 * Gets the elements.
	 *
	 * @param driver    the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator the by locator
	 * @return the list of elements,empty list will be return if there are no
	 * matching elements
	 */
	public List<WebElement> getElements(WebDriver driver, By byLocator) {
		return getElements(driver, byLocator, CommonVariables.MED_TIMEOUT);

	}

	/**
	 * Gets the elements.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeOut Maximum time to wait for the element
	 * @return the elements,empty list will be return if there are no matching
	 * elements
	 */
	public List<WebElement> getElements(WebDriver driver, By byLocator, int maxTimeOut) {
		List<WebElement> listElements = new ArrayList<WebElement>();
		// check if the elements are present
		try {
			// get the elements if they are present
			listElements = waitForElementsToVisible(driver, byLocator, maxTimeOut);
		} catch (Exception e) {
			// Do nothing when
		}
		return listElements;
	}

	/**
	 * Executes the JavaScript on the specified element.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param javaScript the java script
	 */
	public void executeJs(WebDriver driver, WebElement element, String javaScript) {

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
	public void executeJs(WebDriver driver, String javaScript) {
		this.logAccess.getLogger().debug("Executing \"" + javaScript + "\" JavaScript");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript);
	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Private Methods
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */

	/**
	 * Gets the original style.
	 *
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return the original style
	 */
	private String getOriginalStyle(WebElement element) {
		this.logAccess.getLogger().debug("Getting original style of element :- " + element);
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
		try {
			this.logAccess.getLogger().debug("Setting original style \"" + originalStyle + "\" to element :- " + element);
			String js = "arguments[0].setAttribute('style', '" + originalStyle + "');";
			executeJs(driver, element, js);
		} catch (Exception e) {
			// ignore exceptions as there might be cases where either the element
			// is refreshed or no more exist or might the element reference might updated (stale element)
		}
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
	 * @throws Exception the exception
	 */
	private WebElement waitUntilElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
										int maxTimeout) throws Exception {
		return waitUntilElement(driver, element, expectedCondition, maxTimeout, true);
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
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 * @throws Exception the exception
	 */
	private WebElement waitUntilElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
										int maxTimeout, boolean isScrollElementToCenter) throws Exception {
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = webDriverWait(driver, maxTimeout);
		
		WebElement returnElement;
		switch (expectedCondition) {
			case CLICKABLE:
				returnElement = wait.until(ExpectedConditions.elementToBeClickable(element));
				break;
			case VISIBLE:
				returnElement = wait.until(ExpectedConditions.visibilityOf(element));
				break;
			// case PRESENCE:
//			wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
//			.executeScript("return arguments[0].tagName !==''",element).equals(true));

//			returnElement = (WebElement) wait.until((ExpectedCondition<Object>) wd -> ((JavascriptExecutor) wd)
//					.executeScript("return if(arguments[0].tagName !==''){arguments[0]}else{null}",element));
			// break;
			
			// TODO - For now we are giving back the element when expected condition is
			// presence so that we can use same method
			// at other places
			case PRESENCE:
				returnElement = element; // TODO need to keep observation on this change for next 2 releases
				break;
			default:
				throw new IllegalArgumentException("??? Unexpected value: " + expectedCondition
						+ ". This method supports clickable, Visible and Presence options.");
		}
		
		if(isScrollElementToCenter) scrollElement(driver, returnElement, "center");
		
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
	 * @throws Exception the exception
	 */
	private WebElement waitUntilElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
										int maxTimeout) throws Exception {
		return waitUntilElement(driver, byLocator,expectedCondition, maxTimeout, true);
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
	 * @param isScrollElementToCenter is element need to be scrolled to center or not
	 * @param maxTimeout        the max timeout in seconds
	 * @return the web element
	 * @throws Exception the exception
	 */
	private WebElement waitUntilElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
										int maxTimeout, boolean isScrollElementToCenter) throws Exception {
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = webDriverWait(driver, maxTimeout);
		
		WebElement returnElement;
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
		if(isScrollElementToCenter) scrollElement(driver, returnElement, "center");
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
	 *                             and opposite holds good too</font>
	 * @throws Exception the exception
	 */
	private boolean capturePageChunks(WebDriver driver, String tempImagesFolderPath, WebElement hideElement,
									  boolean notIncludeHeader) throws Exception {

		boolean capturedAllPageChunks = false;

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
		double fullFraction = pageHeight / (windowHeight - 5);
		int fullShots = (int) fullFraction; // this simply removes the decimals

		// decide the start index based on the window and page height
		int startIndex = (pageHeight == windowHeight) ? 1 : 0;

		// Calculate our scroll script
		String script = "window.scrollBy(0," + (windowHeight - 5) + ")";
		// get the header element style
		String originalStyle = null;
		if (hideElement != null) {
			originalStyle = hideElement.getAttribute("style");
		}
		int coveredHeight = 0;
		for (int screenshotIndex = startIndex; screenshotIndex < fullShots; screenshotIndex++) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			folderFileUtil.copyFile(tmpFile, new File(tempImagesFolderPath + File.separatorChar
					+ screenshotIndex * windowHeight + ".png"));
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

		//TODO need to handle "java.awt.image.RasterFormatException: y lies outside the raster"
		try {
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
				BufferedImage lastChunk = lastPageBufferImage.getSubimage(0, (int) (lastPageBufferImage.getHeight() - (lastChunkHeight * pxRatio)),
						lastPageBufferImage.getWidth(), (int) (lastChunkHeight * pxRatio));

				ImageIO.write(lastChunk, "png", tmpFile);
				folderFileUtil.copyFile(tmpFile,
						new File(tempImagesFolderPath + File.separatorChar + "lastScreenshot.png"));

				capturedAllPageChunks = true;
			}
		} catch (RasterFormatException rasterFormatException) {
			this.logAccess.getLogger().warn(rasterFormatException.getMessage());
		}

		if (notIncludeHeader) {
			String javaScript = "arguments[0].setAttribute('style', '" + originalStyle + "');";
			js.executeScript(javaScript, hideElement);
		}
		// set back to the original position
		js.executeScript(
				"window.scrollTo(" + currentXPosition + "," + currentYPosition + ");");

		return capturedAllPageChunks;
	}

	/**
	 * * Capture multiple images in chunks one per each window height and then
	 * capture last screenshot if any part is left over
	 *
	 * @param driver               the driver
	 * @param tempImagesFolderPath temporary folder where the images should be
	 *                             stored
	 * @throws Exception the exception
	 */

	private boolean capturePageChunks(WebDriver driver, String tempImagesFolderPath) throws Exception {
		boolean capturedAllChunks = false;
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
		double fullFraction = pageHeight / (windowHeight - 5);
		int fullShots = (int) fullFraction; // this simply removes the decimals

		// decide the start index based on the window and page height
		int startIndex = (pageHeight == windowHeight) ? 1 : 0;
		// Calculate our scroll script
		String script = "window.scrollBy(0," + (windowHeight - 5) + ")";
		int coveredHeight = 0;
		for (int screenshotIndex = startIndex; screenshotIndex < fullShots; screenshotIndex++) {
			File tmpFile = screenCapture.getScreenshotAs(OutputType.FILE);
			folderFileUtil.copyFile(tmpFile, new File(tempImagesFolderPath + File.separatorChar
					+ screenshotIndex * windowHeight + ".png"));
			coveredHeight = coveredHeight + (windowHeight - 5);
			// scroll to the next chunk
			js.executeScript(script);
			Thread.sleep(500);
		}

		//TODO need to handle "java.awt.image.RasterFormatException: y lies outside the raster"
		try {
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
						(int) (lastPageBufferImage.getHeight()
								- (lastChunkHeight * pxRatio)),
						lastPageBufferImage.getWidth(), (int) (lastChunkHeight * pxRatio));

				ImageIO.write(lastChunk, "png", tmpFile);
				folderFileUtil.copyFile(tmpFile,
						new File(tempImagesFolderPath + File.separatorChar + "lastScreenshot.png"));
				capturedAllChunks = true;
			}
		} catch (RasterFormatException rasterFormatException) {
			this.logAccess.getLogger().warn(rasterFormatException.getMessage());
		}

		// set back to the original position
		js.executeScript(
				"window.scrollTo(" + currentXPosition + "," + currentYPosition + ");");
		return capturedAllChunks;
	}

	/**
	 * Stitch all the screenshots located in the temporary images folder
	 *
	 * @param tempImagesFolderPath temporary images folder path
	 * @param outputPNGFileName    output .png image file name
	 * @return the screenshot path
	 * @throws Exception the exception
	 */
	public String mergeImagesToSingleImage(String tempImagesFolderPath, String outputPNGFileName) throws Exception {

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
			bufferedImageHeight = ((imagesList.length - 1) * (imageHeight - 5)) + lastImage.getHeight();
		} else {
			bufferedImageHeight = firstImage.getHeight();
		}

		// create Buffered Image

		BufferedImage finalBufferedImage = new BufferedImage(imageWidth - 30, bufferedImageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = finalBufferedImage.getGraphics();
		// X axis where the image should be incorporated
		int imageXOffset = 0;
		// Y axis where the image should be incorporated (this will get updated after
		// each image added to the Buffered Image.
		int imageYOffset = 0;
		// loop through all images and append them to buffered image
		for (File image : imagesList) {
			// read image into the temporary Buffered Image
			BufferedImage bufferedImage = ImageIO.read(image);

			// add the image to the final Buffered Image graphics
			graphics.drawImage(
					bufferedImage.getSubimage(0, 0, bufferedImage.getWidth() - 30, bufferedImage.getHeight()),
					imageXOffset, imageYOffset, null);
			// update Y axis value (so that the next image will be added at the end)
			// moving the next image 10 pixels up to make sure the images does not show
			// any gap
			imageYOffset += bufferedImage.getHeight() - 5;
		}
		String failedScreenShotPath = this.screenShotsPath + File.separatorChar + getScreenShotTime() + "_"
				+ outputPNGFileName;
		// Save the the final Buffered Image build with graphics to output file
		ImageIO.write(finalBufferedImage, "png", new File(failedScreenShotPath));
		folderFileUtil.deleteFolder(folder);
		return failedScreenShotPath;

	}

	/**
	 * Scroll element on the page to the specific position
	 *
	 * @param driver   the driver
	 * @param element  the element to be scrolled
	 * @param location the location Eg: top,bottom, center
	 */
	public void scrollElement(WebDriver driver, WebElement element, String location) {
		String elePosition = (location.equalsIgnoreCase("TOP")) ? "start"
				: (location.equalsIgnoreCase("BOTTOM")) ? "end" : "center";
		String jScript;

		//TODO - Need to remove the below commented code once the code is working as expected in all projects
//		if (elePosition.equalsIgnoreCase("center")) {
//			jScript = "function scrollToCentre(elem) {" + "var eleWindow = elem.ownerDocument.defaultView || window,"
//					+ "eleRect = elem.getBoundingClientRect(),"
//					+ "targetX = eleRect.left - (eleWindow.innerWidth-eleRect.width)/2;"
//					+ "targetY = eleRect.top - (eleWindow.innerHeight - eleRect.height) / 2;"
//					+ "eleWindow.scrollTo(eleWindow.pageXOffset+targetX, eleWindow.pageYOffset + targetY);"
//					+ "}; scrollToCentre(arguments[0]);";
//
//		} else {
		jScript = "arguments[0].scrollIntoView({behavior: 'auto', block: '" + elePosition + "', inline: 'center'})";
		// }

		executeJs(driver, element, jScript);
	}
	
	/**
	 * Clears the cache in Chrome browser
	 * @param driver the driver
	 * @throws Exception the exception
	 */
	public void clearChromeCache(WebDriver driver) throws Exception {
		if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("chrome")) {
			String mainWindow = driver.getWindowHandle();
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.open()");
			
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				System.out.println(driver.getCurrentUrl());
				if(driver.getCurrentUrl().equals("about:blank")){
					break;
				}
			}
			
			driver.get("chrome://settings/clearBrowserData");
			Thread.sleep(2000);
			
			WebElement clearDataBtn =  (WebElement) js.executeScript("return document.querySelector(\"body > settings-ui\").shadowRoot.querySelector(\"div#container > #main\").shadowRoot.querySelector(\".cr-centered-card-container\").shadowRoot.querySelector(\"[page-title='Privacy and security'] settings-privacy-page\").shadowRoot.querySelector(\"settings-clear-browsing-data-dialog\").shadowRoot.querySelector(\"#clearBrowsingDataDialog #clearBrowsingDataConfirm\")");
			
			js.executeScript("arguments[0].click();", clearDataBtn);
			
			driver.close();
			driver.switchTo().window(mainWindow);
		}else{
			System.out.println("As of now Cache clearing is supported for Chrome browser only.");
		}
	}
	
	/**
	 * Clears the site data in Chrome browser
	 *
	 * @param driver the driver
	 * @param domainName 	the domain name for which the data cache has to be deleted
	 * @param siteName 	the site name
	 * @throws Exception the exception
	 */
	public void clearChromeSiteData(WebDriver driver, String domainName, String siteName) throws Exception {
		if (CommonVariables.BROWSER_SELECT.equalsIgnoreCase("chrome")) {
			String mainWindow = driver.getWindowHandle();
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.open()");
			
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				System.out.println(driver.getCurrentUrl());
				if(driver.getCurrentUrl().equals("about:blank")){
					break;
				}
			}
			
			driver.get("chrome://settings/content/all");
			Thread.sleep(2000);
			
			ArrayList<WebElement> siteEntryElements = (ArrayList<WebElement>)js .executeScript("return document.querySelector('body > settings-ui').shadowRoot.querySelector(\"div#container > #main\").shadowRoot.querySelector(\".cr-centered-card-container\").shadowRoot.querySelector(\"[page-title='Privacy and security'] settings-privacy-page\").shadowRoot.querySelector(\"[section='privacy'] [page-title='All sites'] all-sites\").shadowRoot.querySelectorAll(\"#allSitesList > .no-outline\")");
			
			for(WebElement currentSiteEntryElement : siteEntryElements){
				
				WebElement currentExpandIconElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector(\"#toggleButton > #expandIcon\")", currentSiteEntryElement);
				
				String expandIconSiteName = (String) js.executeScript("return arguments[0].getAttribute('aria-label');", currentExpandIconElement);
				
				String expandIconSiteNameHiddenStatus = (String) js.executeScript("return arguments[0].getAttribute('hidden');", currentExpandIconElement);
				
				boolean isClickedOnSiteDataRemoveButton = false;
				
				boolean isSiteNameMatched = domainName.equalsIgnoreCase(expandIconSiteName);
				
				if(isSiteNameMatched && expandIconSiteNameHiddenStatus != null){
					
					WebElement siteDeleteButton = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector(\"#removeSiteButton\")", currentSiteEntryElement);
					
					js.executeScript("arguments[0].click();", siteDeleteButton);
					
					isClickedOnSiteDataRemoveButton = true;
					
				} else if (isSiteNameMatched) {
					
					
					WebElement expandIconElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector(\"#toggleButton > #expandIcon [aria-label='" + domainName + "']\")", currentSiteEntryElement);
					
					js.executeScript("arguments[0].click();", expandIconElement);
					
					WebElement subSiteDeleteButton = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector(\"#collapseChild > .list-frame > .row-aligned > [data-origin='" + siteName + "'\")", currentSiteEntryElement);
					
					js.executeScript("arguments[0].click();", subSiteDeleteButton);
					
					isClickedOnSiteDataRemoveButton = true;
					
				}
				
				Thread.sleep(2000);
				
				WebElement deleteConfirmationButton = (WebElement) js.executeScript("return document.querySelector('body > settings-ui').shadowRoot.querySelector(\"div#container > #main\").shadowRoot.querySelector(\".cr-centered-card-container\").shadowRoot.querySelector(\"[page-title='Privacy and security'] settings-privacy-page\").shadowRoot.querySelector(\"[section='privacy'] [page-title='All sites'] all-sites\").shadowRoot.querySelector(\"[close-text='Close'] .action-button\")");
				
				if(isClickedOnSiteDataRemoveButton) {
					js.executeScript("arguments[0].click();", deleteConfirmationButton);
					
					System.out.println(((siteName != null && !siteName.isEmpty()) ? "'" + siteName + "' Site under the " : "") + "'" + domainName + "' Domain data is deleted!!");
				}
				
				// If Site name matches then we have to come out of this loop.
				if(isSiteNameMatched)
					break;
			
			}
			
			driver.close();
			driver.switchTo().window(mainWindow);
		}else{
			System.out.println("As of now site data clearing is supported for Chrome browser only.");
		}
	}

}