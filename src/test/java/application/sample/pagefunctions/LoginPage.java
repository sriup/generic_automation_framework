package application.sample.pagefunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import application.sample.functions.AppCommonFunctions;
import framework.enums.ExpectedConditionsEnums;
import framework.logs.LogAccess;

public class LoginPage extends AppCommonFunctions {
	
	
	public LoginPage(String screenShotsPath, LogAccess logAccess) {
		super(screenShotsPath, logAccess);

	}

	public void searchForText(WebDriver driver, String value) throws Exception {
		WebElement element = waitForElement(driver, By.xpath("//input[@name='q']"), ExpectedConditionsEnums.CLICKABLE);
		inputValue(driver, element, value, true, "sent value");		
	}

}
