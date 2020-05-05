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
 * This class will be extended in application/project level BaseClass
 */
public abstract class FwBaseClass {

	/** The BrowserFunctions object */
	private BrowserFunctions browserFunctions;

	/** The LogAccess object */
	private LogAccess logAccess;

	/** The FolderFileUtil object */
	private FolderFileUtil fileUtils;

	/** The screenshot path. */
	private String screenshotPath;

	/**
	 * This method will launch the browser.<br>
	 * <font color="orange"><b>Note : </b> browserName will be decided based on the
	 * value provided in the maven command.<br>
	 * If browserName is provided in the maven command it will be updated in the
	 * {@link CommonVariables#BROWSER_SELECT} and same will be used while launching
	 * the browser.<br>
	 * <br>
	 * browserName will be picked from the test case, if "browserName" is not
	 * specified or empty in the maven commond. <br>
	 * This gives the flexibility to run the tests from pom.xml, test or
	 * TestNg.xml<br>
	 * </font> <br>
	 * <font color="blue">browserName specified in the maven command will
	 * <b>supersedes</b> the browserName from test.</font>
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
	 * <font color="orange"><b>Note : </b>This method will create "SystemGenerated_"
	 * folder appended with date time stamp under "Outputs/Screenshots" folder, if
	 * {@link #createScreenshotPath} is not called in the "BaseClass"
	 * constructor.</font>
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
	 * <font color="orange"><b>Note :</b> This method in turn calls
	 * {@link #createScreenshotName} method that creates the screenshots folder and
	 * also sets the {@link #screenshotPath}.</font>
	 * 
	 * @param folderName the screenshots folder name, folder will be created under
	 *                   "Outputs/Screenshots" folder.
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
	 *                 <font color="orange"><b>Note:</b> .log file will always store
	 *                 <i>ALL</i> log level information.</font>
	 */
	public void initializeLogger(String filename, LogVerboseEnums logLevel) {

		LogManager.resetConfiguration();

		logAccess = new LogAccess(filename, logLevel);

	}

	/**
	 * Creates the screenshot folder<br>
	 * <font color="orange"><b>Note : </b> Folder will be created under
	 * "Outputs/Screenshots" folder also set's the {@link #screenshotPath}
	 * variable.</font>
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
	 * This should be implemented in the project/application level "BaseClass"<br>
	 * with series of steps to be performed when the test is <font color="green"><b>passed</b></font>
	 */
	public abstract void setPositiveStatus();

	/**
	 * This should be implemented in the project/application level "BaseClass"<br>
	 * with series of steps to be performed when the test is <font color="red"><b>failed</b></font>
	 */
	public abstract void setNegativeStatus();
}
