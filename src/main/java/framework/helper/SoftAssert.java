package framework.helper;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

import framework.abstracts.FwBaseClass;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

//TODO move this to framework and consider taking full page screenshot
/**
 * This custom SoftAssert will extend TestNg SoftAssert and override methods to
 * achieve custom actions when the soft assert is performed.<br>
 * <br>
 * 
 * When an assertion fails, don't throw an exception but record the failure.
 * Calling {@code assertAll()} will cause an exception to be thrown if at least
 * one assertion failed.<br>
 * Also capture the screenshot when the soft assert is failed.
 * 
 */

public class SoftAssert extends org.testng.asserts.SoftAssert {

	private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
	private FwBaseClass fwBaseClass;
	private WebDriver driver;

	/**
	 * SoftAssert constructor
	 * 
	 * @param fwBaseClass {@link framework.abstracts.FwBaseClass FwBaseClass}
	 *                    instance
	 */
	public SoftAssert(FwBaseClass fwBaseClass) {
		this.fwBaseClass = fwBaseClass;
		this.driver = fwBaseClass.getBrowserFunctions().getWebDriver();
	}

	/**
	 * Perform the assert
	 */
	@Override
	protected void doAssert(IAssert<?> a) {
		onBeforeAssert(a);
		try {
			a.doAssert();
			onAssertSuccess(a);

			// uncomment the below try/catch block if you want screenshot for +ve SA

//			try {
//				saveScreenshot(a.getActual().toString(), Status.PASSED);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		} catch (AssertionError ex) {
			onAssertFailure(a, ex);
			m_errors.put(ex, a);
			// capture screenshot and save to allure report
			try {
				addScreenshotInAllureReport(ex.toString(), Status.FAILED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			onAfterAssert(a);
		}
	}

	/**
	 * Checks if any soft asserts failed
	 */
	@Override
	public void assertAll() {
		if (!m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("\n\t");
				sb.append(ae.getKey().getMessage());
			}
			// throw Assertion Error to Fail the test
			throw new AssertionError(sb.toString());
		}
	}

	/**
	 * Adds screenshot to the allure report as a sub-step
	 * 
	 * @param message Assert Message <br>
	 *                If soft assert failed it will show the exception details as
	 *                message
	 * @param status  soft assertion status
	 * @return image bytes
	 * @throws Exception
	 */
	@Step("SoftAssert :\n{message}")
	public void addScreenshotInAllureReport(String message, Status status) throws Exception {
		File screenshot = null;
		String screenshotPath = fwBaseClass.getScreenshotPath() + File.separatorChar
				+ new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss_SS").format((new Date()));
		screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(screenshotPath + "_sa_failed.png"));

		// this will mark the sub-step status to fail(red arrow)/passed(green arrow)
		Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(status));
		// attaches the screenshot to the sub-step
		Path content = Paths.get(screenshotPath);
		InputStream is = Files.newInputStream(content);
		Allure.addAttachment("Screenshot", is);
		Allure.getLifecycle().stopStep();

		// this will mark the main-step status to fail(red arrow)/passed(green arrow)
		Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(status));
		Allure.getLifecycle().stopStep();
	}
}