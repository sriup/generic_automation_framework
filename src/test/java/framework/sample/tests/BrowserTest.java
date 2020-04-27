package framework.sample.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import application.sample.baseclass.BaseClass;
import application.sample.pagefunctions.LoginPage;

// TODO: Auto-generated Javadoc
/**
 * The Class BrowserTest.
 */
public class BrowserTest {


	/**
	 * Sample test one.
	 */
	@Test
	public void sampleTest_One() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		BaseClass baseClass = null;
		try {
			baseClass = new BaseClass("chrome", "BusinessOwner", methodName, 0);
			WebDriver driver = baseClass.getBrowserFunctions().getWebDriver();
			baseClass.getBrowserFunctions().navigate("https://www.sparksoftcorp.com/");
			LoginPage loginPage = new LoginPage(baseClass.getScreenshotPath(), baseClass.getLogAccess());
			loginPage.captureScreenShotWithHighlight(driver, By.xpath("//a[@href='sparklabs.html']"), "SparkLab");
			loginPage.clickOnElement(driver, By.cssSelector("ol.carousel-indicators li:first-of-type"), true, true,
					"Ignite_Radio");
			loginPage.captureScreenShotWithHighlight(driver, By.cssSelector("div.item.active .carousel-caption"),
					"Ignite");
//			loginPage.searchForText(driver, "Selenium.dev");
			// Thread.sleep(2000);
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}

	}

	/**
	 * Sample test two.
	 */
	@Test
	public void sampleTest_Two() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		BaseClass baseClass = null;
		try {
			baseClass = new BaseClass("", "BusinessOwner", methodName, 0);
			WebDriver driver = baseClass.getBrowserFunctions().getWebDriver();
			baseClass.getBrowserFunctions().navigate("https://www.sparksoftcorp.com/");
			LoginPage loginPage = new LoginPage(baseClass.getScreenshotPath(), baseClass.getLogAccess());

			loginPage.captureScreenShotWithHighlight(driver, By.xpath("//a[@href='sparklabs.html']"), "SparkLab");
			loginPage.clickOnElement(driver, By.cssSelector("ol.carousel-indicators li:nth-of-type(2)"), true, true,
					"Innovative_Radio");
			loginPage.captureScreenShotWithHighlight(driver, By.cssSelector("div.item.active .carousel-caption"),
					"Innovative");
//			loginPage.searchForText(driver, "Selenium.dev");
			// Thread.sleep(2000);
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}

	}

	/**
	 * Sample test three.
	 */
	@Test
	public void sampleTest_Three() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		BaseClass baseClass = null;
		try {
			baseClass = new BaseClass("Firefox", "BusinessOwner", methodName, 0);
			WebDriver driver = baseClass.getBrowserFunctions().getWebDriver();
			baseClass.getBrowserFunctions().navigate("https://www.sparksoftcorp.com/");
			LoginPage loginPage = new LoginPage(baseClass.getScreenshotPath(), baseClass.getLogAccess());
			loginPage.captureScreenShotWithHighlight(driver, By.xpath("//a[@href='sparklabs.html']"), "SparkLab");
			loginPage.clickOnElement(driver, By.cssSelector("ol.carousel-indicators li:nth-of-type(3)"), true, true,
					"Implement_Radio");
			loginPage.captureScreenShotWithHighlight(driver, By.cssSelector("div.item.active .carousel-caption"),
					"Implement");
//			loginPage.searchForText(driver, "Selenium.dev");
			// Thread.sleep(2000);
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}

	}
	/**
	 * Sample test four.
	 */
	@Test
	public void sampleTest_Four() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		BaseClass baseClass = null;
		try {
			baseClass = new BaseClass("IE", "BusinessOwner", methodName, 0);
			WebDriver driver = baseClass.getBrowserFunctions().getWebDriver();
			baseClass.getBrowserFunctions().navigate("https://www.sparksoftcorp.com/");
			LoginPage loginPage = new LoginPage(baseClass.getScreenshotPath(), baseClass.getLogAccess());
			loginPage.captureScreenShotWithHighlight(driver, By.xpath("//a[@href='sparklabs.html']"), "SparkLab");
			loginPage.clickOnElement(driver, By.cssSelector("ol.carousel-indicators li:nth-of-type(4)"), true, true,
					"Inspire_Radio");
			loginPage.captureScreenShotWithHighlight(driver, By.cssSelector("div.item.active .carousel-caption"),
					"Inspire");
			loginPage.captureScreenShot(driver, "after unhighlight");
//			loginPage.searchForText(driver, "Selenium.dev");
			// Thread.sleep(2000);
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}
	}
}
