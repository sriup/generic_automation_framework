package framework.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

/**
 * This class will hold all methods related to allure
 */
public class AllureUtil {
	public AllureUtil() {
	}

	/**
	 * Pushes the step information into Allure report
	 *
	 * @param message the message need to be entered into Allure report
	 */
	@Step("{message}")
	public void logStep(String message) {
		// intentionally left blank
	}

	/**
	 * This method will attach the screenshot to the allure report
	 * 
	 * @param allureScreenshotName the screenshot name to be displayed
	 * @param screenShotPath       the screenshot path
	 * @throws IOException 		   the exception
	 */
	public void attachScreenShotPNG(String allureScreenshotName, String screenShotPath) throws IOException {
		Path content = Paths.get(screenShotPath);

		InputStream is = Files.newInputStream(content);

		Allure.addAttachment(allureScreenshotName, is);

		is.close();
	}

}
