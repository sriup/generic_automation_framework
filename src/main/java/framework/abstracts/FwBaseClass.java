/**
 *
 */

package framework.abstracts;

import framework.commonfunctions.BrowserFunctions;
import framework.commonfunctions.CommonFunctions;
import framework.constants.CommonVariables;
import framework.enums.BrowserEnums;
import framework.enums.LogVerboseEnums;
import framework.helper.SoftAssert;
import framework.logs.LogAccess;
import framework.utilities.AllureUtil;
import framework.utilities.DateTimeUtil;
import framework.utilities.FolderFileUtil;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;

/**
 * This class will be extended in application/project level BaseClass
 */
public abstract class FwBaseClass {

	/**
	 * The BrowserFunctions object
	 */
	private BrowserFunctions browserFunctions;

	/**
	 * The LogAccess object
	 */
	protected LogAccess logAccess;

	/**
	 * The FolderFileUtil object
	 */
	private FolderFileUtil fileUtils;

	/**
	 * The screenshot path.
	 */
	private String screenshotPath;

	/**
	 * SoftAssert Object
	 */
	private SoftAssert softAssert;

	/**
	 * AllureUtil Object
	 */
	protected AllureUtil allureUtil;

	/**
	 * CommonFunctions Object
	 */
	protected CommonFunctions commonFunctions;

	private HashMap<String, Object> setBrowserOptions = null;
	
	public DesiredCapabilities getRemoteDesiredCapabilities() {
		return remoteDesiredCapabilities;
	}
	
	public void setRemoteDesiredCapabilities(DesiredCapabilities remoteDesiredCapabilities) {
		this.remoteDesiredCapabilities = remoteDesiredCapabilities;
	}
	
	private DesiredCapabilities remoteDesiredCapabilities = null;
	
	

	/**
	 * This method will launch the browser.<br>
	 * <font color="blue"><b>Note : </b> browser will be decided based on the value
	 * provided in the maven command.<br>
	 * If browser is provided in the maven command it will be updated in the
	 * {@link CommonVariables#BROWSER_SELECT} and same will be used while launching
	 * the browser.<br>
	 * <br>
	 * browser will be picked from the test case, if "browser" is not specified or
	 * empty in the maven command. <br>
	 * This gives the flexibility to run the tests from maven command, test or
	 * TestNg.xml<br>
	 * </font> <br>
	 * <font color="red">browser specified in the maven command will
	 * <b>supersedes</b> the browser specified from test.</font>
	 *
	 * @param browserEnum  Select the browser name from the <b>BrowserEnums</b>
	 *                     class
	 * @param downloadPath The download path
	 * @throws Exception the exception
	 */
	public void init(BrowserEnums browserEnum, String downloadPath, String methodName) throws Exception {

		String browserName = "";

		boolean isRunningOnSbox;

		if (CommonVariables.BROWSER_SELECT == null || CommonVariables.BROWSER_SELECT.isEmpty()) {
			browserName = browserEnum.toString();
			this.getLogAccess().getLogger()
					.warn("maven command is having empty value for Browser selection.\nSo, we are using local browser '"
							+ browserName + "' from test case.\nIf you want to specify the browser in the maven command"
							+ "please use \"browser\" property to specify the browser name.");
		} else {
			browserName = CommonVariables.BROWSER_SELECT;
			this.getLogAccess().getLogger()
					.info("Using Browser selection from maven command '" + CommonVariables.BROWSER_SELECT + "'");
		}

		this.browserFunctions = new BrowserFunctions(this.logAccess);
		
		browserFunctions.setRemoteWdDesiredCaps(getRemoteDesiredCapabilities());
		
		browserFunctions.launch(browserName, downloadPath, methodName);

		setSoftAssert();

		// initializing the AllureUtil
		this.allureUtil = new AllureUtil();

	}
	
	

	public void setSoftAssert() {
		// initializing the SoftAssert
		this.softAssert = new SoftAssert(this);
	}

	/**
	 * Sets browser options<br>
	 * Example:
	 *
	 * <pre>
	 * HashMap<String, Object> ieOptions = new HashMap<String, Object>();
	 * ieOptions.put("nativeEvents", false);
	 * </pre>
	 * <p>
	 * Note: Currently this is been supported for IE only
	 *
	 * @param browserOptions the browser options
	 */
	public void setBrowserOptions(HashMap<String, Object> browserOptions) {
		this.setBrowserOptions = browserOptions;
	}

	/**
	 * Gets the instance of {@link BrowserFunctions BrowserFunctions}
	 *
	 * @return the instance of {@link BrowserFunctions BrowserFunctions}
	 */
	public BrowserFunctions getBrowserFunctions() {
		return this.browserFunctions;
	}

	public CommonFunctions getCommonFunctions() {
		return this.commonFunctions;
	}

	/**
	 * Gets the instance of {@link framework.helper.SoftAssert SoftAssert}
	 *
	 * @return the instance of {@link framework.helper.SoftAssert SoftAssert}
	 */
	public SoftAssert getSoftAssert() {
		return this.softAssert;
	}

	/**
	 * Gets the screenshot path.<br>
	 * <font color="blue"><b>Note : </b>This method will create "SystemGenerated_"
	 * folder appended with date time stamp under "Outputs/Screenshots" folder, if
	 * createScreenshotPath is not called in the "BaseClass"
	 * constructor.</font>
	 *
	 * @return the screenshot path
	 */
	public String getScreenshotPath() {
		if (this.screenshotPath == null || this.screenshotPath.isEmpty()) {
			this.logAccess.getLogger().debug(
					"Generating the screenshots folder as user not defined the screenshots location using \"createScreenshotPath\" method in \"BaseClass\" constructor.");
			createScreenShotsFolder("SystemGenerated_" + File.separatorChar
					+ new DateTimeUtil(this.logAccess).getCurrentDateTime(CommonVariables.TIME_FORMATS[7]));
		}
		return this.screenshotPath;
	}

	/**
	 * Sets the screenshot path.<br>
	 * <font color="blue"><b>Note :</b> This method in turn calls
	 * createScreenshotName method that creates the screenshots folder and
	 * also sets the {@link #screenshotPath}.</font>
	 *
	 * @param folderName the screenshots folder name, folder will be created under
	 *                   "Outputs/Screenshots" folder.
	 */
	public void setScreenshotPath(String folderName) {
		// though "screenshotPath" is set with in "createScreenshotPath" method,
		// we are just assigning the return value again in this setter to follow
		this.screenshotPath = createScreenShotsFolder(folderName);
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
	 *                 <font color="blue"><b>Note:</b> .log file will always store
	 *                 <i>ALL</i> log level information.</font>
	 */
	public void initializeLogger(String filename, LogVerboseEnums logLevel) {
		logAccess = new LogAccess(filename, logLevel);

	}

	/**
	 * Creates the screenshot folder<br>
	 * <font color="blue"><b>Note : </b> Folder will be created under
	 * "Outputs/Screenshots" folder also set's the {@link #screenshotPath}
	 * variable.</font>
	 *
	 * @param folderName the folder name
	 * @return the absolute path to the screenshots folder
	 */
	public String createScreenShotsFolder(String folderName) {
		String SCREENSHOT_SUBFOLDER_PATH = "";
		fileUtils = new FolderFileUtil(this.logAccess);
		try {
			File folderObj = fileUtils.createFolder(
					System.getProperty("user.dir") + File.separatorChar + "Output" + File.separatorChar + "ScreenShots",
					folderName);
			SCREENSHOT_SUBFOLDER_PATH = folderObj.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.screenshotPath = SCREENSHOT_SUBFOLDER_PATH;

		// update the commonFunctions instance
		if (commonFunctions != null) {
			commonFunctions.setScreenShotsPath(this.screenshotPath);
		} else {
			commonFunctions = new CommonFunctions(this.screenshotPath, getLogAccess(), this.screenshotPath);
		}

		return SCREENSHOT_SUBFOLDER_PATH;
	}

	/**
	 * This should be implemented in the project/application level "BaseClass"<br>
	 * with series of steps to be performed when the test is
	 * <font color="green"><b>passed</b></font>
	 */
	public abstract void setPositiveStatus();

	/**
	 * This should be implemented in the project/application level "BaseClass"<br>
	 * with series of steps to be performed when the test is
	 * <font color="red"><b>failed</b></font>
	 */
	public abstract void setNegativeStatus();
}
