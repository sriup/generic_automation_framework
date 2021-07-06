package framework.logs;

import framework.commonfunctions.CommonFunctions;
import framework.constants.CommonVariables;
import framework.enums.LogVerboseEnums;
import org.apache.log4j.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.OutputStreamWriter;

/**
 * The Class LogAccess.
 */
public class LogAccess {

	/** The is initialized. */
	private boolean isInitialized;

	/** The logger file name. */
	private String loggerFileName;

	/** The log. */
	private Logger log;

	/** The log level. */
	private final LogVerboseEnums logLevel;

	/**
	 * Instantiates a new log access.
	 *
	 * @param filename the filename
	 * @param logLevel the log level
	 */
	public LogAccess(String filename, LogVerboseEnums logLevel) {
		this.logLevel = logLevel;

		this.setLoggerFileName(filename);

		this.setIsInitialized(false);

	}

	/**
	 * Creating the Console Appender and File Appender.
	 */
	private void initializeLogger() {

		Logger.getLogger("org.apache.http").setLevel(org.apache.log4j.Level.OFF);
		ConsoleAppender ca = new ConsoleAppender();
		ca.setThreshold(getLogLevel());
		ca.setWriter(new OutputStreamWriter(System.out));
		ca.setLayout(new PatternLayout("%d{YYYY-MM-dd HH:mm:ss} - [%M] %m%n"));
		ca.activateOptions();
		log.addAppender(ca);
		log.info("appender filename in LogAccess class is: " + loggerFileName);

		FileAppender appender = new FileAppender();
		

		appender.setAppend(true);

		String filePath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "logs";

		try {

			File folderFile = new File(filePath);
			if (!folderFile.exists()) {
				if (folderFile.mkdirs()) {
					System.out.println("Folder is created");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		appender.setFile(filePath + File.separator + loggerFileName + ".log");

		appender.setThreshold(Level.ALL);
		
		PatternLayout layOut = new PatternLayout();
		layOut.setConversionPattern("%d{YYYY-MM-dd HH:mm:ss} - [%M] %m%n");
		appender.setLayout(layOut);
		appender.activateOptions();
		log.addAppender(appender);

	}

	/**
	 * Initializing the logger if the initializationFlag is false.
	 * 
	 * @return Logger object
	 */
	public Logger getLogger() {

		if (!isInitialized) {
			System.out.println("Initializing the logger");

			log = Logger.getLogger(loggerFileName);

			Logger.getRootLogger().setAdditivity(false);
			log.setLevel(Level.ALL);

			initializeLogger();
			isInitialized = true;
		}
		return this.log;
	}

	/**
	 * Fetching the Initialization flag.
	 *
	 * @return true, if is initialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Setting the Initialization flag.
	 *
	 * @param isInitialized the new checks if is initialized
	 */
	public void setIsInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	/**
	 * Fetching the Logger Filename.
	 *
	 * @return The filename
	 */
	public String getLoggerFileName() {
		return loggerFileName;
	}

	/**
	 * Setting the Filename.
	 *
	 * @param loggerFileName The filename
	 */
	public void setLoggerFileName(String loggerFileName) {
		this.loggerFileName = loggerFileName;
		System.out.println("Filename in LogAccess setLoggerFileName() is: " + this.loggerFileName);

	}

	/**
	 * Fetching the Log Level from the CommonVariables.
	 *
	 * @return Level of the Log
	 */
	public Level getLogLevel() {

		switch (this.logLevel) {
		// We are getting ENUMS from LogVerboseEnums class
		case INFO:
			return Level.INFO;

		case ALL:
			return Level.ALL;

		case ERROR:
			return Level.ERROR;

		case FATAL:
			return Level.FATAL;

		case OFF:
			return Level.OFF;

		case TRACE:
			return Level.TRACE;

		case WARN:
			return Level.WARN;

		default:
			return Level.DEBUG;
		}

	}

    /**
     * Input value.
     *
     * @param driver              the {@link WebDriver
     *                            WebDriver}
     * @param element             the {@link WebElement element}
     * @param value               the value to be set in the element
     * @param isCaptureScreenshot toggle to capture screenshot
     * @param screenShotName      the screen shot name <br>
     *                            Date time Stamp will be <i>prepended</i> to the
     *                            screenshot name by default.<br>
     *                            Note: Use {@link framework.abstracts.FwBaseClass#setScreenshotPath(String)}
     *                            setter to set the path where you want to store the
     *                            screenshots.
     * @param commonFunctions	the {@link CommonFunctions} instance
     * @throws Exception the exception
     */
    public void inputValue(WebDriver driver, WebElement element, String value, boolean isCaptureScreenshot,
						   String screenShotName, CommonFunctions commonFunctions) throws Exception {
        commonFunctions.inputValue(driver, element, value, isCaptureScreenshot, screenShotName, CommonVariables.MED_TIMEOUT);
    }
}
