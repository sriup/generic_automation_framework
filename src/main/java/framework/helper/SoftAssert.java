package framework.helper;

import java.util.Map;

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
 * achieve custom actions when the soft assert is performed.<br><br>
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
	 * @param fwBaseClass {@link framework.commonfunctions.BrowserFunctions BrowserFunctions} instance
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
			// if the failure count >0 then quit the browser here
			if (driver != null) {
				driver.quit();
			}
			// throw Assertion Error to Fail the test
			throw new AssertionError(sb.toString());
		}
	}

	/**
	 * Adds screenshot to the allure report as a sub-step
	 * @param message 
	 * Assert Message <br>
	 * If soft assert failed it will show the exception details as message
	 * @param status
	 * soft assertion status
	 * @return
	 *  image bytes
	 * @throws Exception
	 */
	@Step("SoftAssert :\n{message}")
//	@Attachment(value = "SoftAssert Screenshot", type = "image/png")
	public byte[] addScreenshotInAllureReport(String message, Status status) throws Exception {
		byte[] screenshot = null;

		screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		// this will mark the sub-step status to fail(red arrow)/passed(green arrow)
		Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(status));
		// attaches the screenshot to the sub-step
		Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshot);
		Allure.getLifecycle().stopStep();
		// this will mark the main-step status to fail(red arrow)/passed(green arrow)
		Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(status));
		Allure.getLifecycle().stopStep();
		return screenshot;
	}
}