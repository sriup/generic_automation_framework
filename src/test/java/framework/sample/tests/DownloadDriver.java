package framework.sample.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import framework.commonfunctions.ApiMethods;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.Downloader;
import framework.utilities.FolderFileUtil;
import framework.utilities.ZipUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DownloadDriver {

	private LogAccess logAccess;
	
	 private ApiMethods apiMethods;
	 private DateTimeUtil dateTimeUtil;
	 private ZipUtil zipUtil;
	 private FolderFileUtil folderFileUtil;
	 
	 public DownloadDriver(LogAccess logAccess) {
		 this.logAccess = logAccess;
		 this.apiMethods = new ApiMethods(logAccess);
		 this.dateTimeUtil = new DateTimeUtil(logAccess);
		 this.zipUtil = new ZipUtil(logAccess);
		 this.folderFileUtil = new FolderFileUtil(logAccess);
	 }
	 
	//@Test
	public void downloadDriver() {
		try {
		String browserName = "Firefox";
		String driverDownloadPath = null;
		String downloadUrl = null;
		switch (browserName.toLowerCase()) {
		case "chrome":
			downloadUrl = "https://chromedriver.storage.googleapis.com/" + getChromeDriverVersion() +"/chromedriver_win32.zip";
			System.out.println(downloadUrl);
			downloadWebDriver(downloadUrl, "drivers/Chrome");
			break;
		case "firefox":
			String ffVersion = getGeckoDriverVersion() ;
			downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + ffVersion + "/geckodriver-" + ffVersion + "-win64.zip";
			System.out.println(downloadUrl);
			downloadWebDriver(downloadUrl, "drivers/Firefox");
			break;
		case "ie":
		case "internetexplorer":
		case "internet explorer":
			break;

		default:
			break;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private String getChromeDriverVersion() {
		String uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
		Response response = apiMethods.get(uri);
		// response code
		System.out.println("RESPONSE CODE: " + response.getStatusCode());
		return response.getBody().asString();
	}
	private String getGeckoDriverVersion() {
		String uri = "https://github.com/mozilla/geckodriver/releases/latest";
		Response response = apiMethods.get(uri);
		// response code
		System.out.println("RESPONSE CODE: " + response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		String versionNumber = jsonPathEvaluator.getString("tag_name");
		return versionNumber;
	}
	private synchronized void downloadWebDriver(String url, String destinationFolder) throws Exception {
		String driverZipPath = System.getenv("temp") +  dateTimeUtil.getCurrentDateTime() + ".zip";
		Downloader.downloadFile(this.logAccess,url, driverZipPath);
		// un-zip the driver to the destination
		zipUtil.unzip(driverZipPath, destinationFolder);
		// delete the zip from temp directory
		folderFileUtil.deleteFileOrFolder(driverZipPath);
		this.logAccess.getLogger().info("Successfully downloaded driver from " + url);
	}
	
	private String getCurrentWebDriverVersionFromJson(String filePath, String browserName) throws FileNotFoundException {
		// check if json file exist
		FileInputStream inputStream = new FileInputStream(filePath);
		System.out.println(JsonPath.from(inputStream).getString("$."+ browserName));
		return "";
	}
	
}
