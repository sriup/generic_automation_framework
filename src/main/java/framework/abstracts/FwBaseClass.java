package framework.abstracts;

import org.apache.log4j.LogManager;

import framework.commonfunctions.BrowserFunctions;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;

/**
 * The Class FwBaseClass.
 */
public abstract class FwBaseClass {
	
	/** The browser functions. */
	private BrowserFunctions browserFunctions;
	
	/** The screenshot path. */
	private String screenshotPath;
	
	/**  Capturing all the log info in the LogAccess. */
	private LogAccess logAccess;
	
	/**
	 * Initialize method.
	 *
	 * @param browserType The browser type
	 * @param downloadPath The download path
	 * @throws Exception The exception
	 */
	public void init(String browserType, String downloadPath) throws Exception {

		this.browserFunctions = new BrowserFunctions(this.logAccess);
		browserFunctions.launch(browserType, downloadPath);
		
	}

	/**
	 * Gets the browser functions.
	 *
	 * @return the browser functions
	 */
	public BrowserFunctions getBrowserFunctions() {
		return browserFunctions;
	}

	/**
	 * Sets the browser functions.
	 *
	 * @param browserFunctions the new browser functions
	 */
	public void setBrowserFunctions(BrowserFunctions browserFunctions) {
		this.browserFunctions = browserFunctions;
	}
	
	
	
	/**
	 * Gets the screenshot path.
	 *
	 * @return the screenshot path
	 */
	public String getScreenshotPath() {
		return screenshotPath;
	}

	/**
	 * Sets the screenshot path.
	 *
	 * @param screenshotPath the new screenshot path
	 */
	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
	}

	
	/**
	 * Gets the log access.
	 *
	 * @return the log access
	 */
	public LogAccess getLogAccess() {
		return logAccess;
	}

	/**
	 * Sets the log access.
	 *
	 * @param logAccess the new log access
	 */
	public void setLogAccess(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * Sets the log access filename.
	 *
	 * @param filename the new log access filename
	 * @param logLevel the log level
	 */
	public void initializeLogger(String filename, LogVerboseEnums logLevel) {

		LogManager.resetConfiguration();
		
		logAccess = new LogAccess(filename, logLevel);

	}
	
	/**
	 * Creates the screenshot path.
	 *
	 * @param screenshotFolderName the screenshot folder name
	 */
	public abstract void createScreenshotPath(String screenshotFolderName);
	
	
	/**
	 * Sets the positive status.
	 */
	public abstract void setPositiveStatus();
	
	/**
	 * Sets the negative status.
	 */
	public abstract void setNegativeStatus();
}
