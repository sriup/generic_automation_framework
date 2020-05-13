package framework.commonfunctions;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.jayway.jsonpath.DocumentContext;

import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.FolderFileUtil;
import framework.utilities.ZipUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DownloadWebDrivers {
	private static ApiMethods apiMethods;
	private static DateTimeUtil dateTimeUtil;
	private static ZipUtil zipUtil;
	private static FolderFileUtil folderFileUtil;
	private static LogAccess logAccess = new LogAccess("DownloadWebDrivers", LogVerboseEnums.ALL);

	/**
	 * Download driver <br>
	 * <font color="blue"><b>Note : </b> Driver will be downloaded only if the
	 * latest version is not available on the local machine.<br>
	 * If the latest driver is available, simple it will consume the existing driver
	 * rather downloading it again. <i>Currently this method will support
	 * <u>WINDOWS</u> platform</i></font>
	 * 
	 * @param browserName the browser name
	 */
	public static synchronized void downloadDriver(String browserName) {
		apiMethods = new ApiMethods(logAccess);
		dateTimeUtil = new DateTimeUtil(logAccess);
		zipUtil = new ZipUtil(logAccess);
		folderFileUtil = new FolderFileUtil(logAccess);

		try {
			String downloadUrl = null;
			switch (browserName.toLowerCase()) {
			case "chrome":
				String latestChromeVersion = getChromeDriverVersion();
				downloadUrl = "https://chromedriver.storage.googleapis.com/" + latestChromeVersion
						+ "/chromedriver_win32.zip";
				downloadWebDriver(browserName.toLowerCase(), latestChromeVersion, downloadUrl,
						"drivers" + File.separatorChar + "Chrome");
				break;
			case "firefox":
				String latestGeckoVersion = getGeckoDriverVersion();
				downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + latestGeckoVersion
						+ "/geckodriver-" + latestGeckoVersion + "-win64.zip";
				downloadWebDriver(browserName.toLowerCase(), latestGeckoVersion, downloadUrl,
						"drivers" + File.separatorChar + "FireFox");
				break;
			case "ie":
			case "internetexplorer":
			case "internet explorer":
				// TODO need to implement IEDriver download logic
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the Chrome driver latest version
	 *
	 * @return the Chrome driver latest version
	 */
	private static String getChromeDriverVersion() {
		String uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
		Response response = apiMethods.sendRequest("get", uri);
		// response code
		DownloadWebDrivers.logAccess.getLogger().debug("RESPONSE CODE: " + response.getStatusCode());
		return response.getBody().asString();
	}

	/**
	 * Gets the Gecko driver latest version.
	 *
	 * @return the Gecko driver latest version
	 */
	private static String getGeckoDriverVersion() {
		String uri = "https://github.com/mozilla/geckodriver/releases/latest";
		Response response = apiMethods.sendRequest("get", uri);
		// response code
		DownloadWebDrivers.logAccess.getLogger().debug("RESPONSE CODE: " + response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		String versionNumber = jsonPathEvaluator.getString("tag_name");
		return versionNumber;
	}

	/**
	 * Download latest driver<br>
	 * 
	 *
	 * @param browserName       the browser name
	 * @param laterVersion      the later version
	 * @param url               the URL from where the drier should be download
	 * @param destinationFolder the destination folder where driver should be stored
	 * @throws Exception the exception
	 */
	private static void downloadWebDriver(String browserName, String laterVersion, String url, String destinationFolder)
			throws Exception {
		// get json data
		String currentVersion = "";
		String driversFolderPath = System.getProperty("user.dir") + File.separatorChar + "drivers";
		DocumentContext jPathDocCon = null;
		try {
			jPathDocCon = com.jayway.jsonpath.JsonPath
					.parse(new File(driversFolderPath + File.separatorChar + "DriversInfo.json"));
			currentVersion = jPathDocCon.read(browserName + ".version").toString();
			DownloadWebDrivers.logAccess.getLogger()
					.debug("Current available driver version for  " + browserName + " is " + currentVersion);

		} catch (Exception e) {
			DownloadWebDrivers.logAccess.getLogger()
					.debug("DriverInfo.json file doesn't, hence creating the json file with default template.");
			String templateJson = "{\"chrome\":{\"version\":\"0.00\"},\"firefox\":{\"version\":\"0.00\"},\"edge\":{\"version\":\"0.00\"},\"ie\":{\"version\":\"0.00\"}}";
			folderFileUtil.writeToTextFile(driversFolderPath, "DriversInfo.json", templateJson);

			jPathDocCon = com.jayway.jsonpath.JsonPath
					.parse(new File(driversFolderPath + File.separatorChar + "DriversInfo.json"));

		}
		// check if the latest version does already exist in the system
		if (!currentVersion.equals(laterVersion)) {
			String destinationFilePath = new File(destinationFolder).getAbsolutePath();
			DownloadWebDrivers.logAccess.getLogger().debug("Latest webdriver is not availble for " + browserName
					+ " on the local machine. Downloading latest version # " + laterVersion);
			String originalZipPath = System.getenv("temp") + browserName + "_" + dateTimeUtil.getCurrentDateTime()
					+ ".zip";
			DownloadWebDrivers.logAccess.getLogger().debug("Downing the driver zip and storing as " + originalZipPath);
			File tempZipFile = new File(originalZipPath);
			// donwload the zip file to temp directory
			FileUtils.copyURLToFile(new URL(url), tempZipFile);
			// unzip the driver to the destination
			DownloadWebDrivers.logAccess.getLogger().debug("Extracting the driver to " + destinationFilePath);
			zipUtil.unzip(originalZipPath, destinationFilePath);

			// delet the zip from temp directory
			folderFileUtil.deleteFileOrFolder(tempZipFile.getAbsolutePath());
			DownloadWebDrivers.logAccess.getLogger()
					.debug("Successfully downloaded driver for " + browserName.toUpperCase());

			jPathDocCon.set(browserName + ".version", laterVersion);
			// System.out.println("After " + jPathDocCon.jsonString());
			folderFileUtil.writeToTextFile(driversFolderPath, "DriversInfo.json", jPathDocCon.jsonString());

			// update the json
		} else {
			DownloadWebDrivers.logAccess.getLogger()
					.debug("Using latest webdriver for " + browserName + " browser.\nLatest Version : " + laterVersion);
		}

	}

}
