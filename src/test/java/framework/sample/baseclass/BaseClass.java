package framework.sample.baseclass;

import java.io.File;

import framework.abstracts.FwBaseClass;
import framework.constants.CommonVariables;
import framework.enums.LogVerboseEnums;
import framework.utilities.FolderFileUtils;


/**
 * The Class BaseClass.
 */
public class BaseClass extends FwBaseClass {


	/** The file utils. */
	private FolderFileUtils fileUtils;
	
	/**
	 * Instantiates a new base class.
	 *
	 * @param role the role
	 * @param methodName the method name
	 * @param retryCount the retry count
	 * @throws Exception the exception
	 */
	public BaseClass(String role, String methodName, int retryCount) throws Exception {
		super();
		String logFilename = role + methodName + "_" + retryCount;
		
		LogVerboseEnums logLevel = LogVerboseEnums.DEBUG;
		
		initializeLogger(logFilename, logLevel);
		
		fileUtils = new FolderFileUtils(getLogAccess());
		
		//Create Screenshot folder path
		createScreenshotPath(methodName);
		
		init(CommonVariables.BROWSER_SELECT, this.getScreenshotPath());
		
	}
	
	

	/**
	 * Creates the screenshot path.
	 *
	 * @param screenshotFolderName the screenshot folder name
	 */
	@Override
	public void createScreenshotPath(String screenshotFolderName) {
		
		String SCREENSHOT_SUBFOLDER_PATH = "";
		try {
			File folderObj = fileUtils.createFolder(System.getProperty("user.dir") + "\\Output\\CMMI_Execution\\" ,
					"_" + screenshotFolderName);
			SCREENSHOT_SUBFOLDER_PATH = folderObj.getAbsolutePath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setScreenshotPath(SCREENSHOT_SUBFOLDER_PATH);
		
	}

}
