package framework.logs;

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
import framework.utilities.FolderFileUtils;

public class LogAccess {

	private boolean initializationFlag;
	private String fileName;
	private Logger log;

	public LogAccess(String filename, boolean initializationFlag) {
		// TODO Auto-generated constructor stub
		this.setFileName(filename);

		this.setInitializationFlag(initializationFlag);

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

		log.info("appender filename in LogAccess class is: " + fileName);

		FileAppender appender = new FileAppender();

		appender.setAppend(true);

		String filePath = System.getProperty("user.dir") + "\\target\\logs\\";

		FolderFileUtils folderFileUtils = new FolderFileUtils(this);

		try {
			folderFileUtils.createFolder(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		appender.setFile(filePath + fileName + ".log");

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

		if (!initializationFlag) {
			System.out.println("HERE TO INITIALIZE LOGGER AGAIN");

			log = Logger.getLogger(fileName);

			Logger.getRootLogger().setAdditivity(false);
			log.setLevel(getLogLevel());

			intializeLogger();
			initializationFlag = true;
			return this.log;
		} else {
			return this.log;
		}
	}

	/**
	 * Fetching the Initialization flag
	 * @return
	 */
	public boolean isInitializationFlag() {
		return initializationFlag;
	}

	/**
	 * Setting the Initialization flag
	 * @param initializationFlag
	 */
	public void setInitializationFlag(boolean initializationFlag) {
		this.initializationFlag = initializationFlag;
	}

	/**
	 * Fetching the Filename
	 * @return The filename
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setting the Filename
	 * @param fileName The filename
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
		System.out.println("Filename in LogAccess setFilename() is: " + this.fileName);

	}
	
	/**
	 * Fetching the Log Level from the CommonVariables
	 * @return Level of the Log
	 */
	public Level getLogLevel() {

		switch (CommonVariables.logLevel) {
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
