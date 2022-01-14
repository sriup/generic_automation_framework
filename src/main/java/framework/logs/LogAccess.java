package framework.logs;

import framework.enums.LogVerboseEnums;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.File;

/**
 * The Class LogAccess.
 */
public class LogAccess {

	/** The is initialized. */
	private boolean isInitialized;

	/** The logger file name. */
	private String loggerFileName;

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
	 * Creating the Console Appender and File Appender.
	 */
	private void initializeLogger() {

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

		String logFilePath = filePath + File.separator + loggerFileName + ".log";
		String pattern = "%d{YYYY-MM-dd HH:mm:ss} - [%M] %m%n";

		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

		builder.setStatusLevel(Level.DEBUG);
		builder.setConfigurationName("FrameworkLogger");

		LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
				.addAttribute("pattern", pattern);

		AppenderComponentBuilder fileAppenderBuilder = builder.newAppender("LogToFile", "File")
				.addAttribute("fileName", logFilePath)
				.add(layoutBuilder);

		builder.add(fileAppenderBuilder);

		RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
		rootLogger.add(builder.newAppenderRef("LogToFile"));
		builder.add(rootLogger);

		Configurator.reconfigure(builder.build());
	}

	/**
	 * Initializing the logger if the initializationFlag is false.
	 * 
	 * @return Logger object
	 */
	public Logger getLogger() {

		return LogManager.getLogger(this.loggerFileName);
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
}
