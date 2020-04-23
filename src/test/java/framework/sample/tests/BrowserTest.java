package framework.sample.tests;

import org.testng.annotations.Test;

import framework.sample.baseclass.BaseClass;

public class BrowserTest {

	@Test
	public void sampleTest_One() {
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		try {
			BaseClass baseClass = new BaseClass("BusinessOwner", "sampleTest_One", 0);
			baseClass.getLogAccess().getLogger().info("sampleTest_One '1'");

			for (int i = 0; i < 20; i++) {

				Thread.sleep(1000);
				baseClass.getLogAccess().getLogger().info("sampleTest_One in iteration '" + i + "'");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Test
	public void sampleTest_Two() {
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		try {
			BaseClass baseClass = new BaseClass("ProjectOfficer", "sampleTest_Two", 0);
			baseClass.getLogAccess().getLogger().info("sampleTest_Two '1'");

			for (int i = 0; i < 20; i++) {

				Thread.sleep(300);
				baseClass.getLogAccess().getLogger().info("sampleTest_Two in iteration '" + i + "'");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}

	@Test
	public void sampleTest_Three() {
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		try {

			BaseClass baseClass = new BaseClass("SNS", "sampleTest_Three", 0);
			baseClass.getLogAccess().getLogger().info("sampleTest_Three '1'");

			for (int i = 0; i < 20; i++) {

				Thread.sleep(500);
				baseClass.getLogAccess().getLogger().info("sampleTest_Three in iteration '" + i + "'");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Test
	public void sampleTest_Four() {
		// BaseClass baseClass = new BaseClass(browser, downloadPath);
		try {
			BaseClass baseClass = new BaseClass("NCS", "sampleTest_Four", 0);
			baseClass.getLogAccess().getLogger().info("sampleTest_Four '1'");

			for (int i = 0; i < 20; i++) {

				Thread.sleep(800);
				baseClass.getLogAccess().getLogger().info("sampleTest_Four in iteration '" + i + "'");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
