package framework.sample.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import framework.utilities.ZipUtil;

public class DownloadDriver {

	//@Test
	public void downloadDriver() {
		try {
		String browserName = "Firefox";
		String driverDownloadPath = null;
		String downloadUrl = null;
		switch (browserName.toLowerCase()) {
		case "chrome":
			driverDownloadPath = "drivers/Chrome/chromedriver.exe";
			File chromeDriverFile = new File(driverDownloadPath);
			downloadUrl = "https://chromedriver.storage.googleapis.com/" + get_chrome_latest_version() +"/chromedriver_win32.zip";
			System.out.println(downloadUrl);
			FileUtils.copyURLToFile(new URL(downloadUrl), chromeDriverFile);
			break;
		case "firefox":
			String ffVersion = get_ff_latest_version();
			downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + ffVersion + "/geckodriver-" + ffVersion + "-win64.zip";
			driverDownloadPath = "drivers/FireFox/gecodriver.exe";
			File ffDriverFile = new File(driverDownloadPath);
			System.out.println(downloadUrl);
			FileUtils.copyURLToFile(new URL(downloadUrl), ffDriverFile);
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
		LogAccess logAccess = new LogAccess("downloadrivers", LogVerboseEnums.DEBUG);
		ZipUtil zipUtil = new ZipUtil(logAccess);
		FolderFileUtil fileUtil = new FolderFileUtil(logAccess);
		try {
			String downloadUrl = null;
			// download Chrome driver
			String chromeZip = "drivers/Chrome/chromedriver.zip";
			File chromeDriverZipFile = new File(chromeZip);
			downloadUrl = "https://chromedriver.storage.googleapis.com/" + get_chrome_latest_version() +"/chromedriver_win32.zip";
			System.out.println(downloadUrl);
			FileUtils.copyURLToFile(new URL(downloadUrl), chromeDriverZipFile);
			zipUtil.unzip(chromeZip, chromeDriverZipFile.getParent());
			fileUtil.deleteFileOrFolder(chromeDriverZipFile.getAbsolutePath());
			
			//download Gecko Driver
			String ffVersion = get_ff_latest_version();
			downloadUrl = "https://github.com/mozilla/geckodriver/releases/download/" + ffVersion + "/geckodriver-" + ffVersion + "-win64.zip";
			String fireFoxZip = "drivers/FireFox/gecodriver.zip";
			File ffDriverZipFile = new File(fireFoxZip);
			System.out.println(downloadUrl);
			FileUtils.copyURLToFile(new URL(downloadUrl), ffDriverZipFile);
			zipUtil.unzip("drivers/FireFox/gecodriver.zip", "drivers/FireFox");
			zipUtil.unzip(fireFoxZip, ffDriverZipFile.getParent());
			fileUtil.deleteFileOrFolder(ffDriverZipFile.getAbsolutePath());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String get_chrome_latest_version() throws IOException {

		URL url = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader bufferReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String latestDriver = bufferReader.readLine();
		conn.disconnect();
		return latestDriver;
	}
	
	private String get_ff_latest_version() throws IOException{
		URL url = new URL("https://github.com/mozilla/geckodriver/releases/latest");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String latestDriver = bufferReader.readLine().split(",")[1].replace("\"tag_name\":\"", "").replace("\"","");
		
		conn.disconnect();
		return latestDriver;
		
	}

}
