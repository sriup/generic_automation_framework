package framework.logs;

import framework.enums.LogVerboseEnums;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

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

		ThreadContext.put("threadName", filename);


		ThreadContext.put("logLevel", this.logLevel.toString().toUpperCase());

		this.setIsInitialized(false);

	}

	/**
	 * Initializing the logger if the initializationFlag is false.
	 * 
	 * @return Logger object
	 */
	public Logger getLogger() {

		if (!isInitialized) {
			System.out.println("Initializing the logger");

			log = LogManager.getLogger(this.loggerFileName);

			isInitialized = true;
		}
		return this.log;
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
}
