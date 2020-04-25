package application.sample.pagefunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import application.sample.functions.AppCommonFunctions;
import framework.enums.ExpectedConditionsEnums;
import framework.logs.LogAccess;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginPage.
 */
public class LoginPage extends AppCommonFunctions {
	
	
	/**
	 * Instantiates a new login page.
	 *
	 * @param screenShotsPath the screen shots path
	 * @param logAccess the log access
	 */
	public LoginPage(String screenShotsPath, LogAccess logAccess) {
		super(screenShotsPath, logAccess);

	}

	/**
	 * Search for text.
	 *
	 * @param driver the driver
	 * @param value the value
	 * @throws Exception the exception
	 */
	public void searchForText(WebDriver driver, String value) throws Exception {
		WebElement element = waitForElement(driver, By.xpath("//input[@name='q']"), ExpectedConditionsEnums.CLICKABLE);
		inputValue(driver, element, value, true, "sent value");		
	}

}
