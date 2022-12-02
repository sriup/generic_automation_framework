package framework.commonfunctions;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import framework.utilities.JsonUtil;
import io.qameta.allure.Step;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.*;

import java.io.File;
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
	 * @param downloadPath the path where the files should be download when download
	 *                     from browser
	 * @throws Exception the exception
	 */
	private void setDownloadFolderPath(String downloadPath) throws Exception {
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
	private ThreadLocal<RemoteWebDriver> threadDriver = null;

	/**
	 * Sets for WebDriver.
	 *
	 * @param remoteDriver the new remote WebDriver in the current thread
	 */
	public void setWebDriver(RemoteWebDriver remoteDriver) {
		this.threadDriver.set(remoteDriver);
	}

	/**
	 * Gets for the web driver.
	 *
	 * @return the {@link org.openqa.selenium.WebDriver WebDriver} in the current
	 * thread
	 */
	public WebDriver getWebDriver() {
		return this.threadDriver.get();
	}

	/**
	 * Launches the specified browser.
	 *
	 * @param browserName  provide the browser name as per the below list.<br>
	 *                     <font color="blue"><b>Note:</b> Below is the list of
	 *                     currently supported browsers
	 *                     <ul>
	 *                     <li>Chrome</li>
	 *                     <li>Firefox</li>
	 *                     <li>Edge</li>
	 *                     <li>IE</li>
	 *                     </ul>
	 *                     </font>
	 * @param downloadPath the download path
	 * @return the {@link org.openqa.selenium.WebDriver WebDriver} for the specified
	 * browser
	 * @throws Exception the exception
	 */
	@Step("Launching \"{browserName}\" browser")
	public WebDriver launch(String browserName, String downloadPath) throws Exception {
		setDownloadFolderPath(downloadPath);
		this.logAccess.getLogger().info("Launching browser :-  " + browserName);
		this.logAccess.getLogger().info("Downloads folder :- " + getDownloadFolderPath());
		this.browserName = browserName;
		WebDriver driver = null;

		switch (browserName.trim().toLowerCase()) {
			case "chrome":
				DownloadWebDrivers.downloadDriver(BrowserEnums.Chrome);
				driver = launchChrome();
				break;
			case "firefox":
				DownloadWebDrivers.downloadDriver(BrowserEnums.Firefox);
				driver = launchFirefox();
				break;
			case "edge":
				DownloadWebDrivers.downloadDriver(BrowserEnums.Edge);
				driver = launchEdge();
				break;
//			case "phantomjs":
//				driver = launchPhantomJS();
//				break;
			default:
				this.logAccess.getLogger().info(
						"Unexpected value : " + browserName + "\n only supported browsers are: chrome, firefox, edge, ie");
				throw new IllegalArgumentException(
						"Unexpected value : " + browserName + "\n only supported browsers are: chrome, firefox, edge, ie");
		}

		if (driver != null) {
			Capabilities caps = ((RemoteWebDriver) getWebDriver()).getCapabilities();
			CommonVariables.launchedBrowsers.put(caps.getBrowserName(), caps.getVersion());
		}
		return driver;

	}

	/**
	 * Navigates to the URL and maximizes the browser.
	 *
	 * @param URL to load
	 * @see org.openqa.selenium.remote.RemoteWebDriver#get(String) get
	 */
	@Step("Navigating to \"{URL}\"")
	public void navigate(String URL) {
		this.logAccess.getLogger().info("Navigating to URL :- " + URL);
		this.threadDriver.get().manage().window().maximize();
		this.threadDriver.get().get(URL);
		CommonVariables.navigatedURLs.put(URL, URL);
	}

	/**
	 * Get a string representing the current URL that the browser is looking at.
	 *
	 * @return The URL of the page currently loaded in the browser
	 * @see org.openqa.selenium.remote.RemoteWebDriver#getCurrentUrl() getCurrentURL
	 */
	@Step("Getting current URL")
	public String getCurrentURL() {
		String currentURL = this.threadDriver.get().getCurrentUrl();
		this.logAccess.getLogger().info("Current URL :- " + currentURL);
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
		this.threadDriver.get().close();
	}

	/**
	 * Quits this driver, closing every associated window.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#quit() quit
	 */
	@Step("Quiting the browser")
	public void quit() {
		this.logAccess.getLogger().info("Quiting the browser");
		this.threadDriver.get().quit();
	}

	/**
	 * Refresh the current page.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#navigate() refresh
	 */
	@Step("Refreshing the browser")
	public void refresh() {
		this.logAccess.getLogger().info("Refreshing the browser");
		this.threadDriver.get().navigate().refresh();
	}

	/**
	 * Navigate back.
	 */
	// back
	@Step("Navigating back in browser")
	public void navigateBack() {
		this.logAccess.getLogger().info("Navigating back in browser");
		this.threadDriver.get().navigate().back();
	}

	/**
	 * Navigate forward.
	 */
	@Step("Navigating forward in browser")
	public void navigateForward() {
		this.logAccess.getLogger().info("Navigating forward in browser");
		this.threadDriver.get().navigate().forward();
	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!! Private Methods !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */

	/**
	 * Launch Chrome.
	 *
	 * @return the web driver
	 * @throws Exception the exception
	 */

	private WebDriver launchChrome() throws Exception {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
						+ BrowserEnums.Chrome.toString() + File.separatorChar
						+ getWebDriverLocation(BrowserEnums.Chrome).replace(".", "_") + File.separatorChar
						+ "chromedriver.exe");
		// !! Chrome Options !!
		HashMap<String, Object> chromePrefs = new HashMap<>();
//		
//		Map<String, String> mobileEmulation = new HashMap<>();
//
//		mobileEmulation.put("deviceName", "iPhone X");

		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", this.getDownloadFolderPath());
		chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");

//		HashMap<String, Object> chromeLocalStatePrefs = new HashMap<>();
//		List<String> experimentalFlags = new ArrayList<>();
//		experimentalFlags.add("calculate-native-win-occlusion@2");
//		chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
//		options.setExperimentalOption("localState", chromeLocalStatePrefs);
		//options.setExperimentalOption("mobileEmulation", mobileEmulation);


		threadDriver = new ThreadLocal<>();
		setWebDriver(new ChromeDriver(options));
		return getWebDriver();
	}

	/**
	 * Launch firefox.<br>
	 * <br>
	 * <p>
	 * Refer to <a href =
	 * 'http://kb.mozillazine.org/Firefox_:_FAQs_:_About:config_Entries'>Firefox
	 * Configuration Details</a> for detailed information about each configuration
	 * setting.<br>
	 * ! If you want to see the preferences you can click the menu button menu ,
	 * click Help and select Troubleshooting Information. The Troubleshooting
	 * Information tab will open. And then click on `Profile Folder`
	 *
	 * @return the web driver
	 * @throws Exception the exception
	 */
	private WebDriver launchFirefox() throws Exception {
		System.setProperty("webdriver.gecko.driver",
				System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
						+ BrowserEnums.Firefox.toString() + File.separatorChar
						+ getWebDriverLocation(BrowserEnums.Firefox).replace(".", "_") + File.separatorChar
						+ "geckodriver.exe");
		threadDriver = new ThreadLocal<>();

		FirefoxProfile profile = new FirefoxProfile();

		// set the download folder directory
		profile.setPreference("browser.download.dir", this.getDownloadFolderPath());

		// the last folder specified for a download
		profile.setPreference("browser.download.folderList", 2);

		// hide Download Manager window when a download begins
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		/**
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
		// options.setLogLevel(FirefoxDriverLogLevel.TRACE);
		// options.addPreference("dom.ipc.processCount", 1);

		setWebDriver(new FirefoxDriver(options));
		return getWebDriver();

	}

	/**
	 * Launch edge.
	 *
	 * @return the web driver
	 * @throws Exception the exception
	 */
	private WebDriver launchEdge() throws Exception {
		System.setProperty("webdriver.edge.driver",
				System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
						+ BrowserEnums.Edge.toString() + File.separatorChar
						+ getWebDriverLocation(BrowserEnums.Edge).replace(".", "_") + File.separatorChar
						+ "msedgedriver.exe");
		//TODO: Need to work on the edge options to change the download path location

		EdgeDriverService edgeDriverService = EdgeDriverService.createDefaultService();

		threadDriver = new ThreadLocal<RemoteWebDriver>();

		EdgeOptions edgeOptions = new EdgeOptions();


		setWebDriver(new EdgeDriver(edgeDriverService, edgeOptions));

		// set the download path and the download behavior
		Map<String, Object> commandParameters = new HashMap<>();

		commandParameters.put("cmd", "Page.setDownloadBehavior");

		Map<String, String> parameters = new HashMap<>();

		parameters.put("behavior", "allow");

		parameters.put("downloadPath", getDownloadFolderPath());


		commandParameters.put("params", parameters);

		ObjectMapper objectMapper = new ObjectMapper();

		HttpClient httpClient = HttpClientBuilder.create().build();

		String command = objectMapper.writeValueAsString(commandParameters);

		// get the remote driver session id
		SessionId session = ((RemoteWebDriver) this.getWebDriver()).getSessionId();

		String postRequestUri = edgeDriverService.getUrl().toString() + "/session/" + session + "/chromium/send_command";

		HttpPost postRequest = new HttpPost(postRequestUri);

		postRequest.addHeader("content-type", "application/json");

		postRequest.setEntity(new StringEntity(command));

		httpClient.execute(postRequest);

		return getWebDriver();

	}

//
//	private WebDriver launchPhantomJS() {
//		WebDriverManager.phantomjs().setup();
//		DesiredCapabilities caps = new DesiredCapabilities();
//		caps.setJavascriptEnabled(true);
//		caps.setCapability("takesScreenshot", true);
//		String[] service_args = {"--web-security=no", "--ssl-protocol=any", "--ignore-ssl-errors=yes"};
//		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX, service_args);
//		threadDriver = new ThreadLocal<>();
//		threadDriver.set(new PhantomJSDriver(caps));
//		return threadDriver.get();
//	}

	private String getWebDriverLocation(BrowserEnums browserName) throws Exception {

		JsonUtil jsonUtil = new JsonUtil(logAccess);

		return jsonUtil.getValue(System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
				+ "DriversInfo.json", browserName.toString() + ".version");
	}

	/**
	 * launches browser either on local or on selenium box
	 * @param browserName	the browser name
	 * @param downloadPath	the download path
	 * @param testCaseName  the test case name
	 * @return	the remote WebDriver instance
	 * @throws Exception the Exception
	 */
	public WebDriver launch(String browserName, String downloadPath, String testCaseName) throws Exception {

		//TODO need to remove this below condition once the local grid implementation is done.
		if (!CommonVariables.IS_RUNNING_ON_SBOX) {
			launch(browserName, downloadPath);
		} else {

			this.setBrowserName(browserName);

			this.setDownloadFolderpath(downloadPath);

			this.setTestCaseName(testCaseName);

			RemoteWebDriver remoteDriver;

			MutableCapabilities caps = getCapabilities();
			try {
				if (CommonVariables.IS_RUNNING_ON_SBOX) { // adding this check as we will be removing the initial if condition later
					remoteDriver = new RemoteWebDriver(new URL(CommonVariables.HOST_ADDRESS + "/wd/hub"), caps);
					setSessionId(remoteDriver.getSessionId());
				} else {
					remoteDriver = new RemoteWebDriver(caps);
				}
			} catch (Exception e) {
				this.logAccess.getLogger().info(e.getMessage());
				throw e;
			}
			threadDriver = new ThreadLocal<>();

			setWebDriver(remoteDriver);

			// to handle the file upload when running on remote box
			remoteDriver.setFileDetector(new LocalFileDetector());

			remoteDriver.manage().window().setSize(new Dimension(1920, 1080));
		}

		return getWebDriver();

	}

	/**
	 * get's the browser capabilities based on the browser name
	 * @return
	 * @throws Exception
	 */
	private MutableCapabilities getCapabilities() throws Exception {

		MutableCapabilities capabilities;

		switch (getBrowserName().toLowerCase()) {

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
		return capabilities;
	}

	/**
	 * sets SeleniumBox capabilities
	 * @return the SeleniumBox capabilities
	 */
	private DesiredCapabilities setSboxOptions() {
		DesiredCapabilities sboxCaps = new DesiredCapabilities();
		sboxCaps.setCapability("e34:token", System.getenv("SBOX_TOKEN"));
		sboxCaps.setCapability("e34:video", true);
		sboxCaps.setCapability("e34:timezone", "US/Eastern");
		sboxCaps.setCapability("e34:per_test_timeout_ms", 300000);
		sboxCaps.setCapability("e34:l_testName", this.getTestCaseName());
		return sboxCaps;
	}

	/**
	 * set firefox options
	 * @return the firefox options
	 * @throws Exception the exception
	 */
	private FirefoxOptions setFirefoxOptions() throws Exception {

		FirefoxProfile profile = new FirefoxProfile();

		// set the download folder directory
		if(!CommonVariables.IS_RUNNING_ON_SBOX){
			profile.setPreference("browser.download.dir", this.getDownloadFolderPath());
		}

		// the last folder specified for a download
		profile.setPreference("browser.download.folderList", 2);

		// hide Download Manager window when a download begins
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		/**
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

		if (CommonVariables.IS_RUNNING_ON_SBOX) {
			options.merge(setSboxOptions());
		} else {

			DownloadWebDrivers.downloadDriver(BrowserEnums.Firefox);
			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
							+ BrowserEnums.Firefox.toString() + File.separatorChar
							+ getWebDriverLocation(BrowserEnums.Firefox).replace(".", "_") + File.separatorChar
							+ "geckodriver.exe");
		}

		return options;
	}


	/**
	 * sets Chrome browser capabilities
	 * @return	the ChromeOptions
	 * @throws Exception the exception
	 */
	private ChromeOptions setChromeOptions() throws Exception {
		// !! Chrome Options !!
		HashMap<String, Object> chromePrefs = new HashMap<>();
//
//		Map<String, String> mobileEmulation = new HashMap<>();
//
//		mobileEmulation.put("deviceName", "iPhone X");


		chromePrefs.put("profile.default_content_settings.popups", 0);

		if(!CommonVariables.IS_RUNNING_ON_SBOX){
			chromePrefs.put("download.default_directory", this.getDownloadFolderPath());
		}
		chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");

		HashMap<String, Object> chromeLocalStatePrefs = new HashMap<>();
		List<String> experimentalFlags = new ArrayList<>();
		experimentalFlags.add("calculate-native-win-occlusion@2");
		chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
		options.setExperimentalOption("localState", chromeLocalStatePrefs);
//		options.setCapability("platformName", "Windows 10");
//		options.setCapability("screenResolution", "1920X1080");


		if (CommonVariables.IS_RUNNING_ON_SBOX) {
			options.merge(setSboxOptions());
		} else {
			DownloadWebDrivers.downloadDriver(BrowserEnums.Chrome);
			String chromeDriverPath = System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
					+ BrowserEnums.Chrome.toString() + File.separatorChar
					+ getWebDriverLocation(BrowserEnums.Chrome).replace(".", "_") + File.separatorChar
					+ "chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		}

		return options;
	}

	/**
	 * set's Edge browser capabiities
	 * @return the Edge browser capabilities
	 * @throws Exception the exception
	 */
	private EdgeOptions setEdgeOptions() throws Exception {
		EdgeOptions options = new EdgeOptions();
		if(!CommonVariables.IS_RUNNING_ON_SBOX){
			options.setCapability("download.default_directory", this.getDownloadFolderPath());
		}

		if (CommonVariables.IS_RUNNING_ON_SBOX) {
			options.merge(setSboxOptions());
		} else {
			System.setProperty("webdriver.edge.driver",
					System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
							+ BrowserEnums.Edge.toString() + File.separatorChar
							+ getWebDriverLocation(BrowserEnums.Edge).replace(".", "_") + File.separatorChar
							+ "msedgedriver.exe");
		}

		return options;
	}
}