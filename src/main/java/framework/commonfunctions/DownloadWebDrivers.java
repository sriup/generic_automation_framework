package framework.commonfunctions;

import com.jayway.jsonpath.DocumentContext;
import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.FolderFileUtil;
import framework.utilities.ZipUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class DownloadWebDrivers {
	private static ApiMethods apiMethods;
	private static DateTimeUtil dateTimeUtil;
	private static ZipUtil zipUtil;
	private static FolderFileUtil folderFileUtil;
	private static final LogAccess logAccess = new LogAccess("DownloadWebDrivers", LogVerboseEnums.ALL);

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
	public static synchronized void downloadDriver(BrowserEnums browserName) {
		apiMethods = new ApiMethods(logAccess);
		dateTimeUtil = new DateTimeUtil(logAccess);
		zipUtil = new ZipUtil(logAccess);
		folderFileUtil = new FolderFileUtil(logAccess);

		try {
			String downloadUrl;
			switch (browserName.toString().toLowerCase()) {
			case "chrome":
				String latestChromeVersion = getChromeDriverVersion();
//				downloadUrl = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/" + latestChromeVersion + "/win64/chromedriver-win64.zip";
				downloadUrl = "https://storage.googleapis.com/chrome-for-testing-public/" +  latestChromeVersion + "/win64/chromedriver-win64.zip";
				downloadWebDriver(browserName.toString(), latestChromeVersion, downloadUrl, "drivers"
						+ File.separatorChar + "Chrome" + File.separatorChar + latestChromeVersion.replace(".", "_"));
				break;
			case "firefox":
				String latestGeckoVersion = getGeckoDriverVersion();
				downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + latestGeckoVersion
						+ "/geckodriver-" + latestGeckoVersion + "-win64.zip";
				downloadWebDriver(browserName.toString(), latestGeckoVersion, downloadUrl, "drivers"
						+ File.separatorChar + "FireFox" + File.separatorChar + latestGeckoVersion.replace(".", "_"));
				break;
			case "ie":
			case "internetexplorer":
			case "internet explorer":
				// TODO need to implement IEDriver download logic
				break;
			case "edge":
				String latestEdgeVersion = getEdgeVersion();
				downloadUrl = "https://msedgedriver.azureedge.net/" + latestEdgeVersion + "/edgedriver_win64.zip";
				downloadWebDriver(browserName.toString(),latestEdgeVersion, downloadUrl, "drivers" + File.separatorChar + "Edge" + File.separatorChar + latestEdgeVersion.replace(".", "_"));
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getEdgeVersion() {
		String uri = "https://msedgedriver.azureedge.net/LATEST_STABLE";
		Response response = apiMethods.sendRequest("get", uri);
		// response code
		DownloadWebDrivers.logAccess.getLogger().debug("RESPONSE CODE: " + response.getStatusCode());
		return response.asString().replaceAll("[^0-9.]","");
	}

	/**
	 * Gets the Chrome driver latest version
	 *
	 * @return the Chrome driver latest version
	 */
	private static String getChromeDriverVersion() {
//		String uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
		String uri = "https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_STABLE";
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
		return jsonPathEvaluator.getString("tag_name");
	}

	/**
	 * Download latest driver<br>
	 * 
	 *
	 * @param browserName	   the browser name
	 * @param laterVersion	  the later version
	 * @param url			   the URL from where the driver should be downloaded
	 * @param destinationFolder the destination folder where driver should be stored
	 * @throws Exception the exception
	 */
	private static void downloadWebDriver(String browserName, String laterVersion, String url, String destinationFolder)
			throws Exception {
		// get json data
		String currentVersion = "";
		String driversFolderPath = System.getProperty("user.dir") + File.separatorChar + "drivers";
		DocumentContext jPathDocCon;
		try {
			jPathDocCon = com.jayway.jsonpath.JsonPath
					.parse(new File(driversFolderPath + File.separatorChar + "DriversInfo.json"));
			currentVersion = jPathDocCon.read(browserName + ".version").toString();
			DownloadWebDrivers.logAccess.getLogger()
					.debug("Current available driver version for  " + browserName + " is " + currentVersion);

		} catch (Exception e) {
			DownloadWebDrivers.logAccess.getLogger()
					.debug("DriverInfo.json file doesn't, hence creating the json file with default template.");
			String templateJson = "{\""+ BrowserEnums.Chrome + "\":{\"version\":\"0.00\"},\"" + BrowserEnums.Firefox +  "\":{\"version\":\"0.00\"},\"" + BrowserEnums.Edge +  "\":{\"version\":\"0.00\"},\"" + BrowserEnums.IE +  "\":{\"version\":\"0.00\"}}";
			folderFileUtil.writeToTextFile(driversFolderPath, "DriversInfo.json", templateJson);

			jPathDocCon = com.jayway.jsonpath.JsonPath
					.parse(new File(driversFolderPath + File.separatorChar + "DriversInfo.json"));

		}
		// check if the latest version does already exist in the system
		if (!currentVersion.equals(laterVersion)) {
			String destinationFilePath = new File(destinationFolder).getAbsolutePath();
			DownloadWebDrivers.logAccess.getLogger().debug("Latest webdriver is not available for " + browserName
					+ " on the local machine. Downloading latest version # " + laterVersion);
			String originalZipPath = destinationFilePath + dateTimeUtil.getCurrentDateTime(CommonVariables.TIME_FORMATS[7]) + ".zip";
			DownloadWebDrivers.logAccess.getLogger().debug("Downing the driver zip and storing as " + originalZipPath);
			File tempZipFile = new File(originalZipPath);
			// download the zip file to temp directory
			FileUtils.copyURLToFile(new URL(url), tempZipFile);
			// unzip the driver to the destination
			DownloadWebDrivers.logAccess.getLogger().debug("Extracting the driver to " + destinationFilePath);
			zipUtil.unzip(originalZipPath, destinationFilePath);

			// delete the zip from temp directory
			folderFileUtil.deleteFile(tempZipFile);
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
