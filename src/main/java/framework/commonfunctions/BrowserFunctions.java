package framework.commonfunctions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.logs.LogAccess;
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
	 */
	private void setDownloadFolderPath(String downloadPath) {
		if (downloadPath.isEmpty()) {
			this.downloadFolderpath = System.getProperty("user.dir") + File.separatorChar + "Download_File";
		}
	}

	/**
	 * Gets the download folder path.
	 *
	 * @return the download folder path
	 */
	private String getDownloadFilePath() {
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
		setDownloadFolderPath(this.downloadFolderpath);
		this.logAccess.getLogger().info("Launching browser :-  " + browserName);
		this.logAccess.getLogger().info("Downloads folder :- " + getDownloadFilePath());

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
		chromePrefs.put("download.default_directory", this.getDownloadFilePath());
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability("ACCEPT_SSL_CERTS", true);
		options.setCapability("pageLoadStrategy", "none");

		threadDriver = new ThreadLocal<RemoteWebDriver>();
		setWebDriver(new ChromeDriver(options));
		//getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT, TimeUnit.SECONDS);
		return getWebDriver();
	}

	/**
	 * Launch firefox.
	 *
	 * @return the web driver
	 * @throws Exception
	 */
	private WebDriver launchFirefox() throws Exception {
//		WebDriverManager.firefoxdriver().setup();
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
				+ BrowserEnums.Firefox.toString() + File.separatorChar
				+ getWebDriverLocation(BrowserEnums.Firefox).replace(".", "_") + File.separatorChar
				+ "geckodriver.exe");
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		setWebDriver(new FirefoxDriver());
		//getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT, TimeUnit.SECONDS);
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
		//getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT, TimeUnit.SECONDS);
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
		//getWebDriver().manage().timeouts().implicitlyWait(CommonVariables.IMPLICIT_WAIT, TimeUnit.SECONDS);
		return threadDriver.get();
	}

	private String getWebDriverLocation(BrowserEnums browserName) throws Exception {

		JsonUtil jsonUtil = new JsonUtil(logAccess);

		return jsonUtil.getValue(System.getProperty("user.dir") + File.separatorChar + "drivers" + File.separatorChar
				+ "DriversInfo.json", browserName.toString() + ".version");
	}
}