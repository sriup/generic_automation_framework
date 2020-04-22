package framework.abstracts;

import org.apache.log4j.LogManager;

import framework.commonfunctions.BrowserFunctions;
import framework.logs.LogAccess;

/**
 * The Class FwBaseClass.
 */
public abstract class FwBaseClass {
	
	/** The browser functions. */
	private BrowserFunctions browserFunctions;
	
	/** The screenshot path. */
	private String screenshotPath;
	
	/** Capturing all the log info in the LogAccess */
	private LogAccess logAccess;
	
	/**
	 * Initialization method
	 *
	 * @param logFilename the log filename
	 * @param downloadPath the download path
	 * @param browserType the browser type
	 * @throws Exception the exception
	 */
	public void init(String logFilename, String downloadPath, String browserType) throws Exception {
		// TODO Auto-generated constructor stub
		
		initializaLogger(logFilename);
		
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

	
	public LogAccess getLogAccess() {
		return logAccess;
	}

	public void setLogAccess(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * Sets the log access filename.
	 *
	 * @param filename the new log access filename
	 */
	public void initializaLogger(String filename) {

		LogManager.resetConfiguration();
		
		logAccess = new LogAccess(filename, false);

	}
	
	/**
	 * Creates the screenshot path.
	 *
	 * @param screenshotFolderName the screenshot folder name
	 */
	public abstract void createScreenshotPath(String screenshotFolderName);
	
}
