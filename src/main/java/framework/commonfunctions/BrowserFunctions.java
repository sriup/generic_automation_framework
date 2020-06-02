package framework.commonfunctions;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import framework.enums.BrowserEnums;
import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import framework.utilities.JsonUtil;
import io.github.bonigarcia.wdm.Architecture;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;

/**
 * 
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

	/** The download folder path. */
	private String downloadFolderpath = "";

	/** Log info is written in LogAccess. */
	private LogAccess logAccess;

	/**
	 * Sets the download folder path.<br>
	 * <font color="blue"><b>Note : </b></font> This will set the download folder
	 * path as part of driver capabilities.
	 * 
	 * @param downloadPath the path where the files should be download when download
	 *                     from browser
	 * @throws Exception
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

	/** The thread driver. */
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
	 *         thread
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
	 *         browser
	 * @throws Exception
	 */
	@Step("Lauching \"{browserName}\" browser")
	public WebDriver launch(String browserName, String downloadPath) throws Exception {
		setDownloadFolderPath(downloadPath);
		this.logAccess.getLogger().info("Launching browser :-  " + browserName);
		this.logAccess.getLogger().info("Downloads folder :- " + getDownloadFolderPath());

		switch (browserName.trim().toLowerCase()) {
		case "chrome":
			DownloadWebDrivers.downloadDriver(BrowserEnums.Chrome);
			return launchChrome();
		case "firefox":
			DownloadWebDrivers.downloadDriver(BrowserEnums.Firefox);
			return launchFirefox();
		case "edge":
			return launchEdge();
		case "ie":
		case "internetexplorer":
			return launchInternetExplorer();
		case "phantomjs":
			return launchPhantomJS();
		default:
			this.logAccess.getLogger().info(
					"Unexpected value : " + browserName + "\n only supported browsers are: chrome, firefox, edge, ie");
			throw new IllegalArgumentException(
					"Unexpected value : " + browserName + "\n only supported browsers are: chrome, firefox, edge, ie");
		}

	}

	/**
	 * Navigates to the URL and maximizes the browser.
	 *
	 * @param URL to load
	 * @see org.openqa.selenium.remote.RemoteWebDriver#get(String) get
	 */
	@Step("Navigating to \"{URL}\"")
	public void navigate(String URL) {
		logAccess.getLogger().info("Navigating to URL :- " + URL);
		this.threadDriver.get().manage().window().maximize();
		this.threadDriver.get().get(URL);
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
		logAccess.getLogger().info("Current URL :- " + currentURL);
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
		logAccess.getLogger().info("Closing browser");
		this.threadDriver.get().close();
	}

	/**
	 * Quits this driver, closing every associated window.
	 * 
	 * @see org.openqa.selenium.remote.RemoteWebDriver#quit() quit
	 */
	@Step("Quiting the browser")
	public void quit() {
		logAccess.getLogger().info("Quiting the browser");
		this.threadDriver.get().quit();
	}

	/**
	 * Refresh the current page.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#navigate() refresh
	 */
	@Step("Refreshing the browser")
	public void refresh() {
		logAccess.getLogger().info("Refreshing the browser");
		this.threadDriver.get().navigate().refresh();
	}

	/**
	 * Navigate back.
	 */
	// back
	@Step("Navigating back in browser")
	public void navigateBack() {
		logAccess.getLogger().info("Navigating back in browser");
		this.threadDriver.get().navigate().back();
	}

	/**
	 * Navigate forward.
	 */
	@Step("Navigating forward in browser")
	public void navigateForward() {
		logAccess.getLogger().info("Navigating forward in browser");
		this.threadDriver.get().navigate().forward();
	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!! Private Methods !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */

	/**
	 * Launch Chrome.
	 *
	 * @return the web driver
	 * @throws Exception
	 */

	private WebDriver launchChrome() throws Exception {
//		WebDriverManager.chromedriver().setup();
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
						+ BrowserEnums.Chrome.toString() + File.separatorChar
						+ getWebDriverLocation(BrowserEnums.Chrome).replace(".", "_") + File.separatorChar
						+ "chromedriver.exe");
		// !! Chrome Options !!
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", this.getDownloadFolderPath());
		
		/** commented the below line of code which saves the print "Save As PDF" file
		 * as we have to take print window screenshots, it should be handled with in 
		 * the test
		 */
		// save file to the custom downloads folder when try to print
		//chromePrefs.put("printing.default_destination_selection_rules",
		//		"{ \"kind\": \"local\", \"idPattern\": \".*\", \"namePattern\": \"Save as PDF\" }");
		//chromePrefs.put("printing.print_header_footer", true);
		//chromePrefs.put("savefile.default_directory",this.getDownloadFolderPath());
		
		
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability("ACCEPT_SSL_CERTS", true);
		options.setCapability("pageLoadStrategy", "none");
		// enable kiosk printing mode 
		//options.addArguments("--kiosk-printing");

		threadDriver = new ThreadLocal<RemoteWebDriver>();
		setWebDriver(new ChromeDriver(options));
		// getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT,
		// TimeUnit.SECONDS);
		return getWebDriver();
	}

	/**
	 * Launch firefox.<br>
	 * <br>
	 *
	 * Refer to <a href =
	 * 'http://kb.mozillazine.org/Firefox_:_FAQs_:_About:config_Entries'>Firefox
	 * Configuration Details</a> for detailed information about each configuration
	 * setting.
	 * 
	 * @return the web driver
	 * @throws Exception
	 */
	private WebDriver launchFirefox() throws Exception {
//		WebDriverManager.firefoxdriver().setup();
		System.setProperty("webdriver.gecko.driver",
				System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
						+ BrowserEnums.Firefox.toString() + File.separatorChar
						+ getWebDriverLocation(BrowserEnums.Firefox).replace(".", "_") + File.separatorChar
						+ "geckodriver.exe");
		threadDriver = new ThreadLocal<RemoteWebDriver>();

		FirefoxProfile profile = new FirefoxProfile();

		// set the download folder directory
		profile.setPreference("browser.download.dir", this.getDownloadFolderPath());

		// the last folder specified for a download
		profile.setPreference("browser.download.folderList", 2);

		// hide Download Manager window when a download begins
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		// A comma-separated list of MIME types to save to disk without asking what to
		// use to open the file.
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/pdf,application/zip,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;octet/stream");

		// A comma-separated list of MIME types to open directly without asking for
		// confirmation.
		profile.setPreference("browser.helperApps.neverAsk.openFile",
				"application/pdf,application/zip,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;octet/stream");

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

		setWebDriver(new FirefoxDriver(options));
		// getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT,
		// TimeUnit.SECONDS);
		return getWebDriver();

	}

	/**
	 * Launch edge.
	 *
	 * @return the web driver
	 */
	private WebDriver launchEdge() {
		WebDriverManager.edgedriver().setup();
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		setWebDriver(new EdgeDriver());
		// getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT,
		// TimeUnit.SECONDS);
		return getWebDriver();

	}

	/**
	 * Launch Internet explorer.
	 *
	 * @return the web driver
	 */
	private WebDriver launchInternetExplorer() {
		WebDriverManager.globalConfig().setArchitecture(Architecture.X32);
		WebDriverManager.iedriver().setup();
		String binaryPath = WebDriverManager.iedriver().getBinaryPath();
		System.setProperty("webdriver.ie.driver", binaryPath);
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		InternetExplorerOptions options = new InternetExplorerOptions();
		options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		// options.destructivelyEnsureCleanSession();
		options.disableNativeEvents();
		options.enablePersistentHovering();
		options.requireWindowFocus();
		options.introduceFlakinessByIgnoringSecurityDomains();
		threadDriver.set(new InternetExplorerDriver(options));
		// getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT,
		// TimeUnit.SECONDS);
		return threadDriver.get();
	}

	private WebDriver launchPhantomJS() {
		WebDriverManager.phantomjs().setup();
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		String[] service_args = { "--web-security=no", "--ssl-protocol=any", "--ignore-ssl-errors=yes" };
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX, service_args);
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		threadDriver.set(new PhantomJSDriver(caps));
		return threadDriver.get();
	}

	private String getWebDriverLocation(BrowserEnums browserName) throws Exception {

		JsonUtil jsonUtil = new JsonUtil(logAccess);

		return jsonUtil.getValue(System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
				+ "DriversInfo.json", browserName.toString() + ".version");
	}
}