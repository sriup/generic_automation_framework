package framework.commonfunctions;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * All the methods related to the browser operations will be handled in this
 * class.
 * 
 * @see org.openqa.selenium.remote.RemoteWebDriver
 */
public class BrowserFunctions {

	/** The download folder path. */
	private String downloadFolderpath = "";

	/**
	 * Sets the download folder path.
	 *
	 * @param downloadPath the path where the files should be download
	 */
	private void setDownloadFolderPath(String downloadPath) {
		if (downloadPath.isEmpty()) {
			this.downloadFolderpath = System.getProperty("user.dir") + "/Download_File";
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
	 *                     Note: Below is the list of currently supported
	 *                     browsers
	 *                     <ul>
	 *                     <li>Chrome</li>
	 *                     <li>Firefox</li>
	 *                     <li>Edge</li>
	 *                     <li>IE</li>
	 *                     </ul>
	 * @param downloadPath the download path
	 * @return the {@link org.openqa.selenium.WebDriver WebDriver} for the specified
	 *         browser
	 */
	public WebDriver launch(String browserName, String downloadPath) {
		setDownloadFolderPath(downloadPath);

		switch (browserName.trim().toLowerCase()) {
		case "chrome":
			return launchChrome();
		case "firefox":
			return launchFirefox();
		case "edge":
			return launchEdge();
		case "ie":
		case "internetexplorer":
			return launchInternetExplorer();
		default:
			throw new IllegalArgumentException("Unexpected value: " + browserName
					+ ".\n only supported browsers are: chrome, firefox, edge, ie, phantomjs");
		}

	}

	/**
	 * Navigates to the URL and maximizes the browser.
	 *
	 * @param URL to load
	 * @see org.openqa.selenium.remote.RemoteWebDriver#get(String) get
	 */
	public void navigate(String URL) {
		threadDriver.get().manage().window().maximize();
		threadDriver.get().get(URL);
	}

	/**
	 * Get a string representing the current URL that the browser is looking at.
	 *
	 * @return The URL of the page currently loaded in the browser
	 * @see org.openqa.selenium.remote.RemoteWebDriver#getCurrentUrl() getCurrentURL
	 */
	public String getCurrentURL() {
		return threadDriver.get().getCurrentUrl();
	}

	/**
	 * Close the current window, quitting the browser if it's the last window
	 * currently open.
	 * 
	 * @see org.openqa.selenium.remote.RemoteWebDriver#close() close
	 */
	public void close() {
		threadDriver.get().close();
	}

	/**
	 * Quits this driver, closing every associated window.
	 * 
	 * @see org.openqa.selenium.remote.RemoteWebDriver#quit() quit
	 */
	public void quit() {
		threadDriver.get().quit();
	}

	/**
	 * Refresh the current page.
	 *
	 * @see org.openqa.selenium.remote.RemoteWebDriver#navigate() refresh
	 */
	public void refresh() {
		threadDriver.get().navigate().refresh();
	}

	/**
	 * Navigate back.
	 */
	// back
	public void navigateBack() {
		threadDriver.get().navigate().back();
	}

	/**
	 * Navigate forward.
	 */
	// forward
	public void navigateForward() {
		threadDriver.get().navigate().forward();
	}

	/**
	 * Launch Chrome.
	 *
	 * @return the web driver
	 */
	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!! !! Private Methods !!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	private WebDriver launchChrome() {
		WebDriverManager.chromedriver().setup();

		// !! Chrome Options !!
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", this.getDownloadFilePath());
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability("pageLoadStrategy", "none");
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		setWebDriver(new ChromeDriver(cap));
		return getWebDriver();
	}

	/**
	 * Launch firefox.
	 *
	 * @return the web driver
	 */
	private WebDriver launchFirefox() {
		WebDriverManager.firefoxdriver().setup();
		return getWebDriver();

	}

	/**
	 * Launch edge.
	 *
	 * @return the web driver
	 */
	private WebDriver launchEdge() {
		return getWebDriver();

	}

	/**
	 * Launch internet explorer.
	 *
	 * @return the web driver
	 */
	private WebDriver launchInternetExplorer() {
		return getWebDriver();
	}
}