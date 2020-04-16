package framework.commonfunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.enums.ExpectedConditions;

/**
 * 
 * All the methods related to the common operations will be handled in this
 * class.
 */
public class CommonFunctions {

	private WebDriver driver;

	public CommonFunctions(WebDriver driver) {
		this.driver = driver;
	}

	// find element
	public boolean findElement() {
		return false;
	}

	// wait for element
	public void waitForElement(WebElement element, ExpectedConditions expectedCondition) {

	}

	public void waitForElement(WebElement element, ExpectedConditions expectedCondition, int maxTimeout) {

	}

	public void waitForElementByLocator(By locator, ExpectedConditions expectedCondition) {

	}

	public void waitForElementByLocator(By locator, ExpectedConditions expectedCondition, int maxTimeout) {

	}

	public void waitForInvisibilityOfElement(WebElement element, ExpectedConditions expectedCondition, int maxTimeout) {

	}

	public void waitForInvisibilityOfElementByLocator(By locator, ExpectedConditions expectedCondition, int maxTimeout) {

	}

	// is element present
	public boolean isElementPresent(WebElement element) {
		return false;
	}

	public boolean isElementPresent(WebElement element, int maxTimeout) {
		return false;
	}

	public boolean isElementPresentByLocator(By locator, int maxTimeout) {
		return false;
	}

	// is element displayed
	public boolean isElementDisplayed(WebElement element) {
		return false;
	}

	public boolean isElementDisplayed(WebElement element, int maxTimeout) {
		return false;
	}

	public boolean isElementDisplayedByLocator(By locator, int maxTimeout) {
		return false;
	}

	// is element enabled
	public boolean isElementEnabled(WebElement element) {
		return false;
	}

	public boolean isElementEnabled(WebElement element, int maxTimeout) {
		return false;
	}

	public boolean isElementEnabledByLocator(By locator, int maxTimeout) {
		return false;
	}

	// highlight
	public void highlightElement(WebElement element) {
		// get the original
	}

	// un-highlight
	public void unHightElement(WebElement element) {

	}

	// flash
	public void flash(WebElement element, int numberOfTimes) {

	}

	// click element
	public void clickOnElement(WebElement element, boolean isCaptureScreenShot, boolean captureBefore) {
		// highlight element

		// capture before (private capture screenshot)
		if (isCaptureScreenShot && captureBefore) {
			// take screenshot
		}
		// click

		// capture after (private capture screenshot)
		if (isCaptureScreenShot && !captureBefore) {
			// take screenshot
		}
		// un-highlihgt
	}

	// input value
	public void inputValue(WebElement element, String value, boolean isCaptureScreenshot) {
		// highlight element

		// sendkeys

		// capture (private capture screenshot)

		// un-highlihgt

	}

	// select
	public void selectItemByIndex(WebElement element, int index, boolean isCaptureScreenshot) {
		// highlight element

		// select item by index

		// capture (private capture screenshot)

		// un-highlihgt
	}

	public void selectItemByText(WebElement element, String text, boolean isCaptureScreenshot) {
		// highlight element

		// select item by value

		// capture (private capture screenshot)

		// un-highlihgt
	}

	public void selectItemByPartialText(WebElement element, String partialText, boolean isCaptureScreenshot) {
		// highlight element

		// select item by partial text

		// capture (private capture screenshot)

		// un-highlihgt
	}

	// execute java script
	public void executeJS(WebElement element, String javaScript) {

	}

	public void fireEventOnElement(WebElement element, String eventType, String eventName) {

	}

	public void fireEventOnElement(WebElement element, String eventName) {

	}

	// screenshots
	public void catpureScreenShot(WebElement element) {

	}

	private void captureScreenShot(WebElement element) {

	}

	public void captureFullPageScreenShot() {

	}

}
