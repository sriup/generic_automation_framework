package framework.commonfunctions;

import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import framework.utilities.JsonUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All the methods related to the browser operations will be handled in this
 * class.
 *
 * @see org.openqa.selenium.remote.RemoteWebDriver
 */
public class BrowserFunctions {

	/**
	 * Instantiates a new {@link BrowserFunctions} object with log access.
	 *
	 * @param logAccess the instance of {@link LogAccess}
	 */
	public BrowserFunctions(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * The download folder path.
	 */
	private String downloadFolderpath = "";

	/**
	 * Log info is written in LogAccess.
	 */
	private final LogAccess logAccess;

	public void setDownloadFolderpath(String downloadFolderpath) {
		this.downloadFolderpath = downloadFolderpath;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	private String browserName;

	public SessionId getSessionId() {
		return sessionId;
	}

	public void setSessionId(SessionId sessionId) {
		this.sessionId = sessionId;
	}

	private SessionId sessionId;

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	private String testCaseName;

	private DesiredCapabilities remoteWdDesiredCaps;

	/**
	 * Gets the remote web driver desired capabilities.
	 *
	 * @return the remote web driver desired capabilities
	 */
	public DesiredCapabilities getRemoteWdDesiredCaps() {
		return remoteWdDesiredCaps;
	}

	/**
	 * Sets the remote web driver desired capabilities.
	 *
	 * @param remoteWdDesiredCaps the remote web driver desired capabilities
	 */
	public void setRemoteWdDesiredCaps(DesiredCapabilities remoteWdDesiredCaps) {
		this.remoteWdDesiredCaps = remoteWdDesiredCaps;
	}



	/**
	 * Gets the browser name
	 *
	 * @return the browser name
	 */
	public String getBrowserName() {
		return this.browserName;
	}

	/**
	 * Sets the download folder path.<br>
	 * <font color="blue"><b>Note : </b></font> This will set the download folder
	 * path as part of driver capabilities.
	 *
	 * @param downloadPath the path where the files should be downloaded when download
	 *					 from browser
	 */
	private void setDownloadFolderPath(String downloadPath){
		if (downloadPath.isEmpty()) {
			this.downloadFolderpath = System.getProperty("user.dir") + File.separatorChar + "Download_File";

		} else {
			this.downloadFolderpath = downloadPath;
		}
		new FolderFileUtil(this.logAccess).createFolder(this.downloadFolderpath);
	}

	/**
	 * Gets the download folder path.
	 *
	 * @return the download folder path
	 */
	public String getDownloadFolderPath() {
		return this.downloadFolderpath;
	}

	/**
	 * The thread driver.
	 */
	private WebDriver driver = null;

	/**
	 * Sets for WebDriver.
	 *
	 * @param driver the new WebDriver
	 */
	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Gets for the web driver.
	 *
	 * @return the {@link org.openqa.selenium.WebDriver WebDriver} in the current
	 * thread
	 */
	public WebDriver getWebDriver() {
		return this.driver;
	}



	/**
	 * Navigates to the url and maximizes the browser.
	 *
	 * @param url to load
	 * @see org.openqa.selenium.remote.RemoteWebDriver#get(String) get
	 */
	@Step("Navigating to \"{url}\"")
	public void navigate(String url) {
		this.logAccess.getLogger().info("Navigating to url :- " + url);
		this.driver.manage().window().maximize();
		this.driver.get(url);
		CommonVariables.navigatedURLs.put(url, url);
	}

	/**
	 * Get a string representing the current URL that the browser is looking at.
	 *
	 * @return The URL of the page currently loaded in the browser
	 * @see org.openqa.selenium.remote.RemoteWebDriver#getCurrentUrl() getCurrentURL
	 */
	@Step("Getting current URL")
	public String getCurrentURL() {
		String currentURL = this.driver.getCurrentUrl();
		this.logAccess.getLogger().info("Current URL :- "+ currentURL);
		return currentURL;
	}

	/**
	 * Close the current window, quitting the browser if it's the last window
	 * currently open.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#close() close
	 */
	@Step("Closing browser")
	public void close() {
		this.logAccess.getLogger().info("Closing browser");
		this.driver.close();
	}

	/**
	 * Quits this driver, closing every associated window.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#quit() quit
	 */
	@Step("Quiting the browser")
	public void quit() {
		this.logAccess.getLogger().info("Quiting the browser");
		this.driver.quit();
	}

	/**
	 * Refresh the current page.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#navigate() refresh
	 */
	@Step("Refreshing the browser")
	public void refresh() {
		this.logAccess.getLogger().info("Refreshing the browser");
		this.driver.navigate().refresh();
	}

	/**
	 * Navigate back.
	 */
	// back
	@Step("Navigating back in browser")
	public void navigateBack() {
		this.logAccess.getLogger().info("Navigating back in browser");
		this.driver.navigate().back();
	}

	/**
	 * Navigate forward.
	 */
	@Step("Navigating forward in browser")
	public void navigateForward() {
		this.logAccess.getLogger().info("Navigating forward in browser");
		this.driver.navigate().forward();
	}


	/**
	 * Launches the specified browser.
	 *
	 * @param browserName  provide the browser name as per the below list.<br>
	 *					 <font color="blue"><b>Note:</b> Below is the list of
	 *					 currently supported browsers
	 *					 <ul>
	 *					 <li>Chrome</li>
	 *					 <li>Firefox</li>
	 *					 <li>Edge</li>
	 *					 </ul>
	 *					 </font>
	 * @param downloadPath the download path
	 * @return the {@link org.openqa.selenium.WebDriver WebDriver} for the specified
	 * browser
	 * @throws IOException the IO exception
	 */
	@Step("Launching \"{browserName}\" browser")
	public WebDriver launch(String browserName, String downloadPath, String testCaseName) throws Exception {

		this.setBrowserName(browserName);

		this.setDownloadFolderpath(downloadPath);

		this.setTestCaseName(testCaseName);

		MutableCapabilities driverCaps = getCapabilities(browserName);

		switch (CommonVariables.EXEC_PLATFORM.toLowerCase()) {
			case "remote":
			case "docker":
				launchRemoteDriver(browserName.toLowerCase(), driverCaps);
				break;
			default:
				launchLocalDriver(browserName.toLowerCase(), driverCaps);
				break;
		}

		if (getWebDriver() != null) {
			Capabilities caps = ((RemoteWebDriver) getWebDriver()).getCapabilities();

			CommonVariables.launchedBrowsers.put(caps.getBrowserName(), caps.getBrowserVersion());
		}
		return getWebDriver();

	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!! Private Methods !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */


	private WebDriver launchLocalDriver(String browserName, MutableCapabilities driverCaps) {

		switch (browserName.toLowerCase()){
			case "firefox":
				setWebDriver(new FirefoxDriver((FirefoxOptions) driverCaps));
				break;
			case "edge":
				setWebDriver(new EdgeDriver((EdgeOptions) driverCaps));
				break;
			default:
				setWebDriver(new ChromeDriver((ChromeOptions) driverCaps));
				break;
		}
		return getWebDriver();
	}

	private WebDriver launchRemoteDriver(String browserName, MutableCapabilities driverCaps) throws Exception {
		RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL(CommonVariables.HOST_ADDRESS + "/wd/hub"), driverCaps);
		// set the file detector to handle the file upload to the remote driver
		remoteDriver.setFileDetector(new LocalFileDetector());
		// making sure the browser
		remoteDriver.manage().window().setSize(new Dimension(1920, 1080));
		setWebDriver(remoteDriver);
		setSessionId(remoteDriver.getSessionId());
		return remoteDriver;
	}


	/**
	 * get the browser capabilities based on the browser name
	 * @return the browser capabilities
	 * @throws IOException the IO Exception
	 */
	private MutableCapabilities getCapabilities(String browserName) throws IOException {

		MutableCapabilities capabilities;

		switch (browserName.toLowerCase()) {
			case "firefox":
				capabilities = setFirefoxOptions();
				break;
			case "edge":
				capabilities = setEdgeOptions();
				break;
			default:
				capabilities = setChromeOptions();
				break;
		}
		switch (CommonVariables.EXEC_PLATFORM.toLowerCase()) {
			case "remote":
				getRemoteWdDesiredCaps().asMap().forEach(capabilities::setCapability);
				break;
			case "docker":
				setDockerBrowserOptions().asMap().forEach(capabilities::setCapability);
				break;
		}
		return capabilities;
	}



	/**
	 * sets Docker browser capabilities
	 * @return the Docker Browser capabilities
	 */
	private DesiredCapabilities setDockerBrowserOptions() {
		DesiredCapabilities dockerBrowserCaps = new DesiredCapabilities();
		dockerBrowserCaps.setCapability("se:downloadsEnabled", true);
		dockerBrowserCaps.setCapability("se:recordVideo", true);
		dockerBrowserCaps.setCapability("se:name", getTestCaseName());
		return dockerBrowserCaps;
	}

	/**
	 * set firefox options
	 * @return the firefox options
	 * @throws IOException the IO exception
	 */
	private FirefoxOptions setFirefoxOptions() throws IOException {

		FirefoxProfile profile = new FirefoxProfile();

		// set the download folder directory
		if(CommonVariables.EXEC_PLATFORM.equalsIgnoreCase("local")){
			profile.setPreference("browser.download.dir", this.getDownloadFolderPath());
		}

		// the last folder specified for a download
		profile.setPreference("browser.download.folderList", 2);

		// hide Download Manager window when a download begins
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		/*
		 This is the most important setting that will make sure the pdf is downloaded
		 without any prompt
		 */
		profile.setPreference("pdfjs.disabled", true);

		profile.setPreference("pref.downloads.disable_button.edit_actions", false);
		profile.setPreference("media.navigator.permission.disabled", true);

		// A comma-separated list of MIME types to save to disk without asking what to
		// use to open the file.
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/pdf,application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/zip,text/csv,text/plain,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;octet/stream");

		// A comma-separated list of MIME types to open directly without asking for
		// confirmation.
		profile.setPreference("browser.helperApps.neverAsk.openFile",
				"application/pdf,application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/zip,text/csv,text/plain,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;octet/stream");

		// Do not ask what to do with an unknown MIME type
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);

		// Leave the window in the background when starting a download (Default Setting
		// is false)
		profile.setPreference("browser.download.manager.focusWhenStarting", false);

		// popup window at bottom right corner of the screen will not appear once all
		// downloads are finished.
		profile.setPreference("browser.download.manager.showAlertOnComplete", true);

		// Close the Download Manager when all downloads are complete
		profile.setPreference("browser.download.manager.closeWhenDone", true);


		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(profile);
		options.setEnableDownloads(true);


		return options;
	}

	/**
	 * sets Chrome browser capabilities
	 * @return	the ChromeOptions
	 * @throws IOException the IO exception
	 */
	private ChromeOptions setChromeOptions() throws IOException {
		// !! Chrome Options !!
		HashMap<String, Object> chromePrefs = new HashMap<>();

		chromePrefs.put("profile.default_content_settings.popups", 0);

		if(CommonVariables.EXEC_PLATFORM.equalsIgnoreCase("local")){
			chromePrefs.put("download.default_directory", this.getDownloadFolderPath());
		}
		chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);

		options.setAcceptInsecureCerts(true);
		/*options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);*/

		options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");

		HashMap<String, Object> chromeLocalStatePrefs = new HashMap<>();
		List<String> experimentalFlags = new ArrayList<>();
		experimentalFlags.add("calculate-native-win-occlusion@2");
		chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
		options.setExperimentalOption("localState", chromeLocalStatePrefs);
		options.setEnableDownloads(true);


		return options;
	}

	/**
	 * set's Edge browser capabilities
	 * @return the Edge browser capabilities
	 * @throws IOException the IO Exception
	 */
	private EdgeOptions setEdgeOptions() throws IOException {
		EdgeOptions options = new EdgeOptions();
		options.setEnableDownloads(true);

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("browser.show_hub_popup_on_download_start", false);
		prefs.put("download.default_directory", this.getDownloadFolderPath());
		options.setExperimentalOption("prefs", prefs);

		return options;
	}

	private String getWebDriverLocation(BrowserEnums browserName) throws IOException {

		JsonUtil jsonUtil = new JsonUtil(logAccess);

		return jsonUtil.getValue(System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
				+ "DriversInfo.json", browserName.toString() + ".version");
	}

}