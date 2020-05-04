package framework.abstracts;

import java.io.File;

import org.apache.log4j.LogManager;

import framework.commonfunctions.BrowserFunctions;
import framework.constants.CommonVariables;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.DateTimeUtil;
import framework.utilities.FolderFileUtil;

/**
 * The Class FwBaseClass will be extended in application/project level BaseClass
 */
public abstract class FwBaseClass {

	/** The instance BrowserFunctions class */
	private BrowserFunctions browserFunctions;

	/** The screenshot path. */
	private String screenshotPath;

	/** Capturing all the log info in the LogAccess. */
	private LogAccess logAccess;

	private FolderFileUtil fileUtils;

	/**
	 * Initialize method. This method will launch the browser.<br>
	 * <b>Note : </b> {@link CommonVariables#BROWSER_SELECT} will be updated based
	 * on the maven command.<br>
	 * Browser name will be picked based on the value specified in the test case
	 * level, if "browserName" is not specified or empty in the maven commond. <br>
	 * This gives the flexibility to run the tests from pom.xml, test or
	 * TestNg.xml<br>
	 * <br>
	 * <u><i>browserType specified in the maven will supersedes the value from
	 * test.</i></u>
	 * 
	 *
	 * @param browserName  The browser Name
	 * @param downloadPath The download path
	 * @throws Exception The exception
	 */
	public void init(String browserName, String downloadPath) throws Exception {
		if (CommonVariables.BROWSER_SELECT == null || CommonVariables.BROWSER_SELECT.isEmpty()) {
			this.getLogAccess().getLogger().warn("POM.xml is having empty value for Browser selection");
			this.getLogAccess().getLogger()
					.info("So, we are using local browser '" + browserName + "' from test case ");
		} else {
			browserName = CommonVariables.BROWSER_SELECT;
			this.getLogAccess().getLogger()
					.info("Using Browser selection from POM.xml '" + CommonVariables.BROWSER_SELECT + "'");
		}

		this.browserFunctions = new BrowserFunctions(this.logAccess);
		browserFunctions.launch(browserName, downloadPath);

	}

	/**
	 * Gets the instance of {@link BrowserFunctions BrowserFunctions}
	 *
	 * @return the instance of {@link BrowserFunctions BrowserFunctions}
	 */
	public BrowserFunctions getBrowserFunctions() {
		return browserFunctions;
	}

	/**
	 * Gets the screenshot path.<br>
	 * <b>Note : </b>This method will create "SystemGenerated_" folder appended with
	 * date time stamp under "Outputs/Screenshots" <br>
	 * folder, if "createScreenshotPath" is not called in the "BaseClass"
	 * constructor.
	 * 
	 * @return the screenshot path
	 * @throws Exception exception
	 */
	public String getScreenshotPath() throws Exception {
		if (this.screenshotPath == null || this.screenshotPath.isEmpty()) {
			this.logAccess.getLogger().debug(
					"Generating the screenshots folder as user not defined the screenshots location using \"createScreenshotPath\" method in \"BaseClass\" constructor.");
			createScreenshotName("SystemGenerated_" + File.separatorChar
					+ new DateTimeUtil(this.logAccess).getCurrentDateTime(CommonVariables.TIME_FORMATS[7]));
		}
		return this.screenshotPath;
	}

	/**
	 * Sets the screenshot path.<br>
	 * <b>Note : </b> This method in turn calls "createScreenshotPath" method that
	 * creates the screenshots folder
	 * and also sets the screenshot path.<br>
	 * 
	 * @param folderName the screenshots folder name <br>
	 *                   Folder will be created under "Outputs/Screenshots" folder.
	 */
	public void setScreenshotPath(String folderName) {
		// though "screenshotPath" is set with in "createScreenshotPath" method,
		// we are just assigning the return value again in this setter to follow
		this.screenshotPath = createScreenshotName(folderName);
	}

	/**
	 * Gets the instance of {@link LogAccess LogAccess}
	 *
	 * @return the instance of {@link LogAccess LogAccess}
	 */
	public LogAccess getLogAccess() {
		return logAccess;
	}

	/**
	 * Initializes the logger and also creates the .log file based on the filename.
	 *
	 * @param filename the new log access filename
	 * @param logLevel the console log level <br>
	 *                 <b>Note:</b> .log file will always store <i>ALL</i> log level
	 *                 information.
	 */
	public void initializeLogger(String filename, LogVerboseEnums logLevel) {

		LogManager.resetConfiguration();

		logAccess = new LogAccess(filename, logLevel);

	}

	/**
	 * Creates the screenshot folder<br>
	 * <b>Note : </b> Folder will be created under "Outputs/Screenshots" folder also
	 * set's the {@link #screenshotPath} variable.
	 * 
	 * @param folderName the folder name
	 * @return the absolute path to the screenshots folder
	 */
	public String createScreenshotName(String folderName) {
		String SCREENSHOT_SUBFOLDER_PATH = "";
		fileUtils = new FolderFileUtil(this.logAccess);
		try {
			File folderObj = fileUtils.createFolder(
					System.getProperty("user.dir") + File.separatorChar + "Output" + File.separatorChar + "ScreenShots",
					folderName);
			SCREENSHOT_SUBFOLDER_PATH = folderObj.getAbsolutePath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.screenshotPath = SCREENSHOT_SUBFOLDER_PATH;
		return SCREENSHOT_SUBFOLDER_PATH;
	};

	/**
	 * Abstract method - This should be implemented in the project/application level "BaseClass"<br>
	 * The series of steps to be perfomed when the test is passed
	 */
	public abstract void setPositiveStatus();

	/**
	 * Abstract method - This should be implemented in the project/application level "BaseClass"<br>
	 * The series of steps to be perfomed when the test is failed.
	 */
	public abstract void setNegativeStatus();
}
