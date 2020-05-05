package framework.commonfunctions;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.constants.CommonVariables;
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

	/** LogAccess object */
	private LogAccess logAccess;

	/** DateTimeUtil object */
	private DateTimeUtil dateTimeUtil;

	/**
	 * Gets the dateTimeUtil object
	 *
	 * @return the instance of DateTimeUtils
	 */
	public DateTimeUtil getDateTimeUtil() {
		return dateTimeUtil;
	}

	/** FolderFileUtil object */
	private FolderFileUtil folderFileUtil;

	/**
	 * Gets the folderFileUtil object
	 *
	 * @return the folderFileUtil
	 */
	public FolderFileUtil getFolderFileUtil() {
		return folderFileUtil;
	}

	/** The Excel Utility */
	private ExcelUtil excelUtil;

	/**
	 * Gets the excelUtil.
	 *
	 * @return the excelUtil
	 */
	public ExcelUtil getExcelUtil() {
		return excelUtil;
	}

	/** The Fake Data Utility */
	private FakeDataUtil fakeDataUtil;

	/**
	 * Gets the FakeDataUtil.
	 *
	 * @return the FakeDataUtil
	 */
	public FakeDataUtil getFakeDataUtil() {
		return fakeDataUtil;
	}

	/** The Security Utility */
	private SecurityUtil securityUtil;

	/**
	 * Gets the SecurityUtil.
	 *
	 * @return the SecurityUtil
	 */
	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}

	/** The Zip Utility */
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
	 * <font color="blue">Note:<i> This will be prepended to the screenshot name.</i></font>
	 *
	 * @return the screen shot time
	 * @throws Exception the exception
	 */
	public String getScreenShotTime() throws Exception {
		return dateTimeUtil.getCurrentDateTime(CommonVariables.TIME_FORMATS[7]);
	}

	/**
	 * Wait for element.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the {@link org.openqa.selenium.WebElement element}
	 * @param expectedCondition the expected condition from
	 *                          {@link ExpectedConditionsEnums}
	 * @return the {@link org.openqa.selenium.WebElement element}
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + element);
		return waitUntilElement(driver, element, expectedCondition, CommonVariables.MAX_TIMEOUT);
	}

	/**
	 * Wait for element.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the {@link org.openqa.selenium.WebElement element}
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
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
	 * @param expectedCondition the expected condition
	 * @return the web element
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, CommonVariables.MAX_TIMEOUT);
	}

	/**
	 * Wait for element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 * @return the web element
	 */
	public WebElement waitForElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		this.logAccess.getLogger()
				.debug("waiting for element to be " + expectedCondition.toString() + " :- " + byLocator);
		return waitUntilElement(driver, byLocator, expectedCondition, maxTimeout);
	}

	/**
	 * Wait for element(s) visible
	 * 
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param elements   the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeOut Maximium time to wait for the element(s) visible
	 * @return List of WebElements
	 */
	public List<WebElement> waitForElementsToVisible(WebDriver driver, List<WebElement> elements, int maxTimeOut) {
		this.logAccess.getLogger().debug("waiting for all specified elements in the list to be visible" + elements);
		return (new WebDriverWait(driver, maxTimeOut)).until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	/**
	 * Wait for invisibility of element.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout
	 */
	public void waitForInvisibilityOfElement(WebDriver driver, WebElement element, int maxTimeout) {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + element);
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

	/**
	 * Wait for invisibility of element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void waitForInvisibilityOfElement(WebDriver driver, By byLocator, ExpectedConditions expectedCondition,
			int maxTimeout) {
		this.logAccess.getLogger().info("waiting for element to be invisible  :- " + byLocator);
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		wait.equals(ExpectedConditions.invisibilityOfElementLocated(byLocator));
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
	 * Checks if is element present by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout
	 * @return true, if is element present by locator
	 */
	public boolean isElementPresent(WebDriver driver, By byLocator, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is present  :- " + byLocator);
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
	 * Checks if is element displayed by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout
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
	 * @param maxTimeout the max timeout
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebDriver driver, WebElement element, int maxTimeout) {
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
		this.logAccess.getLogger().info("checking if element is enabled  :- " + element);
		if (element.isEnabled()) {
			// return true if the element is enabled
			return true;
		} else {
			// return false if the element is disabled
			return false;
		}

	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element    the {@link org.openqa.selenium.WebElement element}
	 * @param maxTimeout the max timeout
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(WebDriver driver, WebElement element, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is enabled  :- " + element);
		// TODO Need to implement the logic to keep checking until the specified time is
		// elapsed
		if (element.isEnabled()) {
			// return true if the element is enabled
			return true;
		} else {
			// return false if the element is disabled
			return false;
		}
	}

	/**
	 * Checks if is element enabled by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param byLocator  the by locator
	 * @param maxTimeout the max timeout
	 * @return true, if is element enabled by locator
	 */
	public boolean isElementEnabled(WebDriver driver, By byLocator, int maxTimeout) {
		this.logAccess.getLogger().info("checking if element is enabled  :- " + byLocator);
		// TODO Need to implement the logic to keep checking until the specified time is
		// elapsed
		if (driver.findElement(byLocator).isEnabled()) {
			// return true if the element is enabled
			return true;
		} else {
			// return false if the element is disabled
			return false;
		}
	}

	/**
	 * Highlights the element.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element} <br>
	 *                This method will highlight the element and does not set back
	 *                the original style. <br>
	 *                <font color="blue"><b>Note:</b> Use {@link #unHighlightElement unHighlightElement}
	 *                method to set back the original style of the element.</font>
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
		} catch (NoSuchElementException NSE) {
			// we don't have to either print the trace or throw the exception
			// here as there are situations where the element might not present
			// after performing some actions like click
		}
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
			// get element original style
			String originalStyle = getOriginalStyle(element);
			for (int highlightIndex = 1; highlightIndex <= numberOfTimes; highlightIndex++) {
				highlightElement(driver, element);
				Thread.sleep(300);

				unHighlightElement(driver, element, originalStyle);
			}
		} catch (Exception e) {
			throw e;
		}
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
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// capture before (private capture screenshot)
		if (isCaptureScreenShot && captureBefore) {
			// take screenshot
			captureScreenShot(driver, screenShotName);
		}
		// click
		element.click();
		// capture after (private capture screenshot)
		if (isCaptureScreenShot && !captureBefore) {
			// take screenshot
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
	}

	public void clickOnElement(WebDriver driver, By byLocator, boolean isCaptureScreenShot, boolean captureBefore,
			String screenShotName) throws Exception {
		WebElement element = driver.findElement(byLocator);
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
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// click in the field
		element.click();
		// clear any existing values
		element.clear();
		// enter value in the field
		element.sendKeys(value);
		// trigger the onchange event (to make sure the events dispatches correctly in
		// IE)
		jsTriggerEventOnElement(driver, element, "onchange");
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
	}

	public void inputValue(WebDriver driver, By byLocator, String value, boolean isCaptureScreenshot,
			String screenShotName) throws Exception {
		WebElement element = driver.findElement(byLocator);
		inputValue(driver, element, value, isCaptureScreenshot, screenShotName);
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
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by index
		Select listElement = new Select(element);
		listElement.selectByIndex(index);
		// capture
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
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
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by value
		Select dropDown = new Select(element);
		dropDown.selectByValue(value);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
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
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by partial text
		Select dropDown = new Select(element);
		dropDown.selectByVisibleText(visibleText);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
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
		// highlight the element
		String originalStyle = highlightElement(driver, element);
		// get the element text
		elementText = element.getText();
		// capture screenshot
		if (isCaptureScreenShot) {
			captureScreenShot(driver, screenShotName);
		}
		// unhighlight the element
		unHighlightElement(driver, element, originalStyle);
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
		// highlight the element
		String originalStyle = highlightElement(driver, element);
		// get the element text
		attributeValue = element.getAttribute(attributeName);
		// capture screenshot
		if (isCaptureScreenShot) {
			captureScreenShot(driver, screenShotName);
		}
		// un-highlight the element
		unHighlightElement(driver, element, originalStyle);
		return attributeValue;
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
	 * <font color="blue"><b>Note:</b> This helps in running the tests on IE where the associated event not
	 * triggered <br></font>
	 * Please use {@link #jsTriggerMouseEvent} method for mouse related events.
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
	public void captureScreenShotWithHighlight(WebDriver driver, WebElement element, String screenshotName)
			throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot for element :- " + element);
		// highlight
		String originalStyle = highlightElement(driver, element);
		// capture screenshot
		captureScreenShot(driver, screenshotName);
		// un-highlight
		setOriginalStyle(driver, element, originalStyle);
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
	public void captureScreenShotWithHighlight(WebDriver driver, By byLocator, String screenshotName) throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot for element :- " + byLocator.toString());
		WebElement element = waitForElement(driver, byLocator, ExpectedConditionsEnums.CLICKABLE);
		// highlight
		String originalStyle = highlightElement(driver, element);
		// capture screenshot
		captureScreenShot(driver, screenshotName);
		// un-highlight
		setOriginalStyle(driver, element, originalStyle);
		Thread.sleep(2000);
	}

	/**
	 * Capture screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 * @throws Exception the exception
	 */
	// screenshots
	public void captureScreenShot(WebDriver driver, String screenshotName) throws Exception {
		this.logAccess.getLogger().debug("Capturing screenshot");
		File scrrenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrrenShot,
				new File(this.screenShotsPath + "\\" + getScreenShotTime() + "_" + screenshotName + ".png"));

	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default.<br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 */
	public void captureFullPageScreenShot(WebDriver driver, String screenshotName) {
		// TODO: Need to implement the logic to capture the entire page screenshot
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
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 * @return the web element
	 */
	private WebElement waitUntilElement(WebDriver driver, WebElement element, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		switch (expectedCondition) {
		case CLICKABLE:
			return wait.until(ExpectedConditions.elementToBeClickable(element));
		case VISIBLE:
			return (new WebDriverWait(driver, CommonVariables.MAX_TIMEOUT))
					.until(ExpectedConditions.visibilityOf(element));
		case PRESENCE:
			return (new WebDriverWait(driver, CommonVariables.MAX_TIMEOUT)).until((WebDriver lDriver) -> element);
		default:
			throw new IllegalArgumentException("Unexpected value: " + expectedCondition + ".\n This method supports "
					+ ExpectedConditionsEnums.CLICKABLE.toString() + " and " + ExpectedConditionsEnums.VISIBLE
					+ ".  Please use waitUntilElementByLocator method for available options from ExpectedConditionsEnums.");
		}
	}

	/**
	 * Wait until element by locator.
	 *
	 * @param driver            the driver
	 * @param byLocator         the by locator
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 * @return the web element
	 */
	private WebElement waitUntilElement(WebDriver driver, By byLocator, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		switch (expectedCondition) {
		case CLICKABLE:
			return wait.until(ExpectedConditions.elementToBeClickable(byLocator));
		case VISIBLE:
//			return (WebElement) wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byLocator));
			return (new WebDriverWait(driver, CommonVariables.MAX_TIMEOUT))
					.until(ExpectedConditions.visibilityOf(driver.findElement(byLocator)));
		case PRESENCE:
			return wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
		default:
			throw new IllegalArgumentException("Unexpected value: " + expectedCondition
					+ ".\n Please refer to  ExpectedConditionsEnums for the available optoins.");
		}
	}

}