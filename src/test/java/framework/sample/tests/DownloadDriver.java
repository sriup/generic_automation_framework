package framework.sample.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.ast.tools.GenericsUtils;
import org.testng.annotations.Test;

import framework.commonfunctions.ApiMethods;
import framework.commonfunctions.CommonFunctions;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.FolderFileUtil;
import framework.utilities.ZipUtil;

public class DownloadDriver {

	LogAccess logAccess = new LogAccess("downloadrivers", LogVerboseEnums.DEBUG);
	ZipUtil zipUtil = new ZipUtil(logAccess);
	FolderFileUtil fileUtil = new FolderFileUtil(logAccess);
	ApiMethods apiMethods = new ApiMethods();
	DateTimeUtil dtUtil = new DateTimeUtil(logAccess);
	//@Test
	public void downloadDriver() {
		ApiMethods apiMethods = new ApiMethods();
		try {
		String browserName = "Firefox";
		String driverDownloadPath = null;
		String downloadUrl = null;
		switch (browserName.toLowerCase()) {
		case "chrome":
			downloadUrl = "https://chromedriver.storage.googleapis.com/" + apiMethods.getChromeDriverVersion() +"/chromedriver_win32.zip";
			System.out.println(downloadUrl);
			downloadWebDriver(downloadUrl, "drivers/Chrome");
			break;
		case "firefox":
			String ffVersion = apiMethods.getGeckoDriverVersion() ;
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
	
	@Test
	public void downloadDrivers() {
		
		try {
			String downloadUrl = null;
			// download Chrome driver
			String chromeDriverVersion = apiMethods.getChromeDriverVersion();
			downloadUrl = "https://chromedriver.storage.googleapis.com/" + chromeDriverVersion +"/chromedriver_win32.zip";
			downloadWebDriver(downloadUrl, "drivers/Chrome");
			
			//download Gecko Driver
			String geckoDriverVersion = apiMethods.getGeckoDriverVersion();
			downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + geckoDriverVersion + "/geckodriver-" + geckoDriverVersion + "-win64.zip";
			downloadWebDriver(downloadUrl, "drivers/Firefox");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void downloadWebDriver(String url, String destinationFolder) throws Exception {
		String originalZipPath = System.getenv("temp") + dtUtil.getCurrentDateTime() + ".zip";
		File tempZipFile = new File(originalZipPath);
		// donwload the zip file to temp directory
		FileUtils.copyURLToFile(new URL(url), tempZipFile);
		// unzip the driver to the destination
		zipUtil.unzip(originalZipPath, destinationFolder);
		// delet the zip from temp directory
		fileUtil.deleteFileOrFolder(tempZipFile.getAbsolutePath());
		this.logAccess.getLogger().info("Successfully downloaded driver from " + url);
	}
	
}
