package framework.utilities;

import io.qameta.allure.Step;

/**
 * This class will hold all methods related to allure
 */
public class AllureUtil {
	public AllureUtil() {}
	
	/**
	 * Pushes the step information into Allure report
	 *
	 * @param message the message need to be entered into Allure report
	 */
	@Step("{message}")
	public void logStep(String message) {
		// intentionally left blank
	}

}
