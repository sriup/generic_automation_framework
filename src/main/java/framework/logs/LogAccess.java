package framework.logs;

import java.io.File;
/**
 * The Class LogAccess.
 */
import java.io.OutputStreamWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import framework.constants.CommonVariables;
import framework.enums.LogVerboseEnums;
import framework.utilities.FolderFileUtils;

public class LogAccess {

	private boolean isInitialized;
	private String loggerFileName;
	private Logger log;
	private LogVerboseEnums logLevel;

	public LogAccess(String filename, LogVerboseEnums logLevel) {
		// TODO Auto-generated constructor stub
		this.logLevel = logLevel;
		
		this.setLoggerFileName(filename);

		this.setIsInitialized(false);

	}
	
	/**
	 * Creating the Console Appender and File Appender
	 */
	private void intializeLogger() {

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

		String filePath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "logs" ;

		try {
			
			File folderFile = new File(filePath);
			if (!folderFile.exists()) {
				if (folderFile.mkdir()) {
					System.out.println("Folder is created");
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		appender.setFile(filePath + File.separator + loggerFileName + ".log");

		appender.setThreshold(getLogLevel());

		PatternLayout layOut = new PatternLayout();
		layOut.setConversionPattern("%d{YYYY-MM-dd HH:mm:ss} - [%M] %m%n");
		appender.setLayout(layOut);
		appender.activateOptions();
		log.addAppender(appender);

	}

	/**
	 * Initializing the logger if the initializationFlag is false.
	 * @return Logger object
	 */
	public Logger getLogger() {

		if (!isInitialized) {
			System.out.println("Initializating the logger");

			log = Logger.getLogger(loggerFileName);

			Logger.getRootLogger().setAdditivity(false);
			log.setLevel(getLogLevel());

			intializeLogger();
			isInitialized = true;
			return this.log;
		} else {
			return this.log;
		}
	}

	/**
	 * Fetching the Initialization flag
	 * @return
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Setting the Initialization flag
	 * @param isInitialized
	 */
	public void setIsInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	/**
	 * Fetching the Logger Filename
	 * @return The filename
	 */
	public String getLoggerFileName() {
		return loggerFileName;
	}

	/**
	 * Setting the Filename
	 * @param loggerFileName The filename
	 */
	public void setLoggerFileName(String loggerFileName) {
		this.loggerFileName = loggerFileName;
		System.out.println("Filename in LogAccess setLoggerFileName() is: " + this.loggerFileName);

	}
	
	/**
	 * Fetching the Log Level from the CommonVariables
	 * @return Level of the Log
	 */
	public Level getLogLevel() {

		switch (this.logLevel) {
		//We are getting ENUMS from LogVerboseEnums class
		case DEBUG:
			return Level.DEBUG;

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
