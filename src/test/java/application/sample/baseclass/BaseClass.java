package application.sample.baseclass;

import java.awt.desktop.QuitEvent;
import java.io.File;

import com.mysql.cj.x.protobuf.MysqlxConnection.Close;

import framework.abstracts.FwBaseClass;
import framework.constants.CommonVariables;
import framework.enums.LogVerboseEnums;
import framework.utilities.FolderFileUtils;

// TODO: Auto-generated Javadoc
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
    public BaseClass(String browserName, String role, String methodName, int retryCount) throws Exception {
        super();
        String logFilename = role + methodName + "_" + retryCount;
       
        LogVerboseEnums logLevel = LogVerboseEnums.DEBUG;
       
        initializeLogger(logFilename, logLevel);
       
        fileUtils = new FolderFileUtils(getLogAccess());
        getLogAccess().getLogger().warn(
				"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //Create Screenshot folder path
        createScreenshotPath(methodName);
       
        if(CommonVariables.BROWSER_SELECT == null || CommonVariables.BROWSER_SELECT.isEmpty() ) {
            this.getLogAccess().getLogger().warn("POM.xml is having empty value for Browser selection");
            this.getLogAccess().getLogger().info("So, we are using local browser '" + browserName + "' from test case ");
        } else {
            browserName = CommonVariables.BROWSER_SELECT;
            this.getLogAccess().getLogger().info("Using Browser selection from POM.xml '" + CommonVariables.BROWSER_SELECT + "'");
        }
       
        init(browserName, this.getScreenshotPath());
       
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
			File folderObj = fileUtils.createFolder(System.getProperty("user.dir") + "\\Output\\Sample_Project\\",
					screenshotFolderName);
			SCREENSHOT_SUBFOLDER_PATH = folderObj.getAbsolutePath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setScreenshotPath(SCREENSHOT_SUBFOLDER_PATH);

	}

	/**
	 * Sets the positive status.
	 */
	@Override
	public void setPositiveStatus() {
		getBrowserFunctions().close();
	}

	/**
	 * Sets the negative status.
	 */
	@Override
	public void setNegativeStatus() {

		try {
			getBrowserFunctions().close();
		} catch (NullPointerException e) {
			this.getLogAccess().getLogger().warn("Driver instance not present");
			e.printStackTrace();
		}

	}
}
