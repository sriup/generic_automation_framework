package framework.commonfunctions;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.constants.CommonVariables;
import framework.enums.ExpectedConditionsEnums;

/**
 * 
 * All the methods related to the common operations will be handled in this
 * class.
 */
public class CommonFunctions {

	/** Folder path where the captured screenshots should be stored */
	private String screenShotsPath;

	/** Date time format to be prepended to the screenshot name */
	private String screenShotTimeStamp;

	/**
	 * Instantiates a new common functions.
	 *
	 * @param screenShotsPath the screen shots path
	 */
	public CommonFunctions(String screenShotsPath) {
		this.screenShotsPath = screenShotsPath;
	}

	/**
	 * Sets the date time format for the screenshot. <br>
	 * Note:<i> This will be prepended to the screenshot name.</i>
	 * 
	 * @param timeStampFormat the new screen shot time stamp
	 */
	public void setScreenShotTimeStampFormat(String timeStampFormat) {
		this.screenShotTimeStamp = timeStampFormat;
	}

	/**
	 * Find element.
	 *
	 * @return true, if successful
	 */

	public boolean findElement() {
		return false;
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
		return waitUntilElement(driver, element, expectedCondition, maxTimeout);
	}

	/**
	 * Wait for element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by                the {@link org.openqa.selenium.By By} locator
	 * @param expectedCondition the expected condition
	 * @return the web element
	 */
	public WebElement waitForElementByLocator(WebDriver driver, By by, ExpectedConditionsEnums expectedCondition) {
		return waitUntilElementByLocator(driver, by, expectedCondition, CommonVariables.MAX_TIMEOUT);
	}

	/**
	 * Wait for element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by                the {@link org.openqa.selenium.By By} locator
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 * @return the web element
	 */
	public WebElement waitForElementByLocator(WebDriver driver, By by, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		return waitUntilElementByLocator(driver, by, expectedCondition, maxTimeout);
	}

	/**
	 * Wait for invisibility of element.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element           the {@link org.openqa.selenium.WebElement element}
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 */
	public void waitForInvisibilityOfElement(WebDriver driver, WebElement element, ExpectedConditions expectedCondition,
			int maxTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

	/**
	 * Wait for invisibility of element by locator.
	 *
	 * @param driver            the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by                the {@link org.openqa.selenium.By By} locator
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void waitForInvisibilityOfElementByLocator(WebDriver driver, By by, ExpectedConditions expectedCondition,
			int maxTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		wait.equals(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	/**
	 * Checks if is element present
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element present
	 */
	// is element present
	public boolean isElementPresent(WebDriver driver, WebElement element) {
		// wait for the {@link org.openqa.selenium.WebElement element} to present
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, CommonVariables.MED_TIMEOUT);
			// return true if the {@link org.openqa.selenium.WebElement element} is visible
			return true;
		} catch (Exception e) {
			// return false if the {@link org.openqa.selenium.WebElement element} is not
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
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElement(driver, element, ExpectedConditionsEnums.VISIBLE, maxTimeout);
			// return true if the {@link org.openqa.selenium.WebElement element} is visible
			return true;
		} catch (Exception e) {
			// return false if the {@link org.openqa.selenium.WebElement element} is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element present by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by         the {@link org.openqa.selenium.By By} locator
	 * @param maxTimeout the max timeout
	 * @return true, if is element present by locator
	 */
	public boolean isElementPresentByLocator(WebDriver driver, By by, int maxTimeout) {
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElementByLocator(driver, by, ExpectedConditionsEnums.VISIBLE, maxTimeout);
			// return true if the {@link org.openqa.selenium.WebElement element} is visible
			return true;
		} catch (Exception e) {
			// return false if the {@link org.openqa.selenium.WebElement element} is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by         the {@link org.openqa.selenium.By By} locator
	 * @param maxTimeout the max timeout
	 * @return true, if is element displayed by locator
	 */
	public boolean isElementDisplayedByLocator(WebDriver driver, By by, int maxTimeout) {
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElementByLocator(driver, by, ExpectedConditionsEnums.VISIBLE, maxTimeout);
			// return true if the {@link org.openqa.selenium.WebElement element} is visible
			return true;
		} catch (Exception e) {
			// return false if the {@link org.openqa.selenium.WebElement element} is not
			// visible
			return false;
		}
	}

	/**
	 * Checks if is element displayed.
	 *
	 * @param driver the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by     the by
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayedByLocator(WebDriver driver, By by) {
		// wait for the {@link org.openqa.selenium.WebElement element} to present for
		// specified time
		try {
			waitForElementByLocator(driver, by, ExpectedConditionsEnums.VISIBLE, CommonVariables.MED_TIMEOUT);
			// return true if the {@link org.openqa.selenium.WebElement element} is visible
			return true;
		} catch (Exception e) {
			// return false if the {@link org.openqa.selenium.WebElement element} is not
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
		return false;
	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element}
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(WebDriver driver, WebElement element) {
		return false;
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
		return false;
	}

	/**
	 * Checks if is element enabled by locator.
	 *
	 * @param driver     the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param by         the {@link org.openqa.selenium.By By} locator
	 * @param maxTimeout the max timeout
	 * @return true, if is element enabled by locator
	 */
	public boolean isElementEnabledByLocator(WebDriver driver, By by, int maxTimeout) {
		return false;
	}

	/**
	 * Highlights the element.
	 *
	 * @param driver  the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param element the {@link org.openqa.selenium.WebElement element} <br>
	 *                This method will highlight the element and does not set back
	 *                the original style. <br>
	 *                Note: Use {@link #unHighlightElement unHighlightElement}
	 *                method to set back the original style of the element.
	 * @return the String with original style of the element
	 */
	public String highlightElement(WebDriver driver, WebElement element) {
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
		// set element original style
		try {
			setOriginalStyle(driver, element, originalStyle);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to set the original style.");
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
	 */
	// click element
	public void clickOnElement(WebDriver driver, WebElement element, boolean isCaptureScreenShot,
			boolean captureBefore) {
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// capture before (private capture screenshot)
		if (isCaptureScreenShot && captureBefore) {
			// take screenshot
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
	 *                            screenshot name by default. Make sure set the date
	 *                            time format using
	 *                            {@link #setScreenShotTimeStampFormat
	 *                            setScreenShotTimeStampFormat} setter. <br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 */
	public void inputValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
			String screenShotName) {
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
			captureScreenShot(driver, element, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
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
	 *                            screenshot name by default. Make sure set the date
	 *                            time format using
	 *                            {@link #setScreenShotTimeStampFormat
	 *                            setScreenShotTimeStampFormat} setter. <br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 */
	public void selectItemByIndex(WebDriver driver, WebElement element, int index, boolean isCaptureScreenshot,
			String screenShotName) {
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by index
		Select listElement = new Select(element);
		listElement.selectByIndex(index);
		// capture
		if (isCaptureScreenshot) {
			captureElementScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
	}

	/**
	 * Select drop down list item based on value
	 *
	 * @param driver              the {@link org.openqa.selenium.WebDriver
	 *                            WebDriver}
	 * @param element             the {@link org.openqa.selenium.WebElement element}
	 * @param value               the list item value to be selected
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default. Make sure set the date
	 *                            time format using
	 *                            {@link #setScreenShotTimeStampFormat
	 *                            setScreenShotTimeStampFormat} setter. <br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 */
	public void selectItemByValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
			String screenShotName) {
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by value
		Select dropDown = new Select(element);
		dropDown.selectByValue(value);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureElementScreenShot(driver, screenShotName);
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
	 * @param partialText         the partial text
	 * @param isCaptureScreenshot the is capture screenshot
	 * @param screenShotName      the screenshot name <br>
	 *                            Date time Stamp will be <i>prepended</i> to the
	 *                            screenshot name by default. Make sure set the date
	 *                            time format using
	 *                            {@link #setScreenShotTimeStampFormat
	 *                            setScreenShotTimeStampFormat} setter. <br>
	 *                            Note: Use {@link #screenShotsPath screenShotsPath}
	 *                            setter to set the path where you want to store the
	 *                            screenshots.
	 */
	public void selectItemByVisibleText(WebDriver driver, WebElement element, String visibleText,
			boolean isCaptureScreenshot, String screenShotName) {
		// highlight element
		String originalStyle = highlightElement(driver, element);
		// select item by partial text
		Select dropDown = new Select(element);
		dropDown.selectByVisibleText(visibleText);
		// capture (private capture screenshot)
		if (isCaptureScreenshot) {
			captureElementScreenShot(driver, screenShotName);
		}
		// un-highlihgt
		unHighlightElement(driver, element, originalStyle);
	}

	/**
	 * Fire event on element.
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
	public void fireEventOnElement(WebDriver driver, WebElement element, String eventType, String eventName) {
		String jsFunction = " var clickEvent = document.createEvent ('" + eventType + "');" 
								 	+ "clickEvent.initEvent ('" + eventName + "', true, true); "
									+ "arguments [0].dispatchEvent (clickEvent); ";
		executeJs(driver, element, jsFunction);
	}

	/**
	 * Trigger general events on the {@link org.openqa.selenium.WebElement element}.
	 * <br>
	 * Note: This helps in running the tests on IE where the associated event not
	 * triggered <br>
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
	 *                       screenshot name by default. Make sure set the date time
	 *                       format using {@link #setScreenShotTimeStampFormat
	 *                       setScreenShotTimeStampFormat} setter. <br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 */
	// screenshots
	public void captureScreenShot(WebDriver driver, WebElement element, String screenshotName) {
		// highlight
		String originalStyle = highlightElement(driver, element);
		// capture screenshot
		captureElementScreenShot(driver, screenshotName);
		// un-highlight
		setOriginalStyle(driver, element, originalStyle);
	}

	/**
	 * Capture screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default. Make sure set the date time
	 *                       format using {@link #setScreenShotTimeStampFormat
	 *                       setScreenShotTimeStampFormat} setter. <br>
	 *                       Note: Use {@link #screenShotsPath screenShotsPath}
	 *                       setter to set the path where you want to store the
	 *                       screenshots.
	 */
	// screenshots
	public void captureScreenShot(WebDriver driver, String screenshotName) {
		// capture screenshot
		captureElementScreenShot(driver, screenshotName);
	}

	/**
	 * Capture full page screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenshotName the screenshot name <br>
	 *                       Date time Stamp will be <i>prepended</i> to the
	 *                       screenshot name by default. Make sure set the date time
	 *                       format using {@link #setScreenShotTimeStampFormat
	 *                       setScreenShotTimeStampFormat} setter. <br>
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
		executeJs(driver, element, originalStyle);
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
	 * @param by                the by
	 * @param expectedCondition the expected condition
	 * @param maxTimeout        the max timeout
	 * @return the web element
	 */
	private WebElement waitUntilElementByLocator(WebDriver driver, By by, ExpectedConditionsEnums expectedCondition,
			int maxTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		switch (expectedCondition) {
		case CLICKABLE:
			return wait.until(ExpectedConditions.elementToBeClickable(by));
		case PRESENCE:
			return wait.until(ExpectedConditions.presenceOfElementLocated(by));
		case VISIBLE:
			return (WebElement) wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		default:
			throw new IllegalArgumentException("Unexpected value: " + expectedCondition
					+ ".\n Please refer to  ExpectedConditionsEnums for the available optoins.");
		}
	}

	/**
	 * Capture screen shot.
	 *
	 * @param driver         the {@link org.openqa.selenium.WebDriver WebDriver}
	 * @param screenshotName the screenshot name
	 */
	private void captureElementScreenShot(WebDriver driver, String screenshotName) {

		// Get TimeStamp
		DateFormat dateFormat = new SimpleDateFormat(screenShotTimeStamp);
		Date timeStamp = new Date();
		String screenShotTime = dateFormat.format(timeStamp);
		File scrrenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrrenShot,
					new File(this.screenShotsPath + "\\" + screenShotTime + "_" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}