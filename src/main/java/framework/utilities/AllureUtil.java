package framework.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.qameta.allure.Attachment;
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

	@Attachment(value = "{screenShotName}", type = "image/png")
	public byte[] attachScreenShotPNG(String screenShotName, String path) throws IOException {
		File file = new File(path);
		BufferedImage bufferedImage = ImageIO.read(file);

		byte[] image = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(bufferedImage, "png", bos);
			image = bos.toByteArray();
		} catch (Exception e) {
		}

		return image;
	}

}
