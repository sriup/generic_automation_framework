package framework.sample.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import application.sample.baseclass.BaseClass;
import application.sample.pagefunctions.LoginPage;

public class BrowserTest {
	
	@BeforeSuite
	public void testBed() {
		System.setProperty("browserType", "chrome");
	}

	@Test
	public void sampleTest_One() {
		String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
		
		BaseClass baseClass = new BaseClass("BusinessOwner", methodName, 0);
		try {
		
			baseClass.getLogAccess().getLogger().info("sampleTest_One '1'");
			WebDriver driver = baseClass.getBrowserFunctions().getWebDriver();
			baseClass.getBrowserFunctions().navigate("https://google.com");
			LoginPage loginPage = new LoginPage(baseClass.getScreenshotPath(), baseClass.getLogAccess());
			loginPage.searchForText(driver, "Selenium.dev");
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}
		
	}

	@Test
	public void sampleTest_Two() {
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
		BaseClass baseClass = new BaseClass("ProjectOfficer", methodName, 0);
		try {
			
			baseClass.getLogAccess().getLogger().info("sampleTest_Two '1'");

			for (int i = 0; i < 2; i++) {

				Thread.sleep(300);
				baseClass.getLogAccess().getLogger().info("sampleTest_Two in iteration '" + i + "'");
				
			}
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}
		

	}

	@Test
	public void sampleTest_Three() {
		String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		BaseClass baseClass = new BaseClass("SNS", methodName, 0);
		try {

			
			baseClass.getLogAccess().getLogger().info("sampleTest_Three '1'");

			for (int i = 0; i < 2; i++) {

				Thread.sleep(500);
				baseClass.getLogAccess().getLogger().info("sampleTest_Three in iteration '" + i + "'");
			}
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}
	}

	@Test
	public void sampleTest_Four() {
		String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		BaseClass baseClass = new BaseClass("NCS", methodName, 0);
		try {
			
			baseClass.getLogAccess().getLogger().info("sampleTest_Four '1'");

			for (int i = 0; i < 2; i++) {

				Thread.sleep(800);
				baseClass.getLogAccess().getLogger().info("sampleTest_Four in iteration '" + i + "'");
			}
			baseClass.setPositiveStatus();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseClass.setNegativeStatus();
		}

	}

}
