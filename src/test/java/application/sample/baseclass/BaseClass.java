package application.sample.baseclass;

import java.awt.desktop.QuitEvent;
import java.io.File;

import com.mysql.cj.x.protobuf.MysqlxConnection.Close;

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
	public BaseClass(String role, String methodName, int retryCount)  {
		super();
		String logFilename = role + "_" + methodName + "_" + retryCount;
		String screenshotFolderName = role + "_" + methodName + "_" + retryCount;
		LogVerboseEnums logLevel = LogVerboseEnums.DEBUG;
		
		initializeLogger(logFilename, logLevel);
		
		fileUtils = new FolderFileUtils(getLogAccess());
		
		//Create Screenshot folder path
		createScreenshotPath(screenshotFolderName);
	
		try {
			init(CommonVariables.BROWSER_SELECT, this.getScreenshotPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
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
			File folderObj = fileUtils.createFolder(System.getProperty("user.dir") + "\\Output\\Sample_Project\\" ,
					 screenshotFolderName);
			SCREENSHOT_SUBFOLDER_PATH = folderObj.getAbsolutePath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setScreenshotPath(SCREENSHOT_SUBFOLDER_PATH);
		
	}


	@Override
	public void setPositiveStatus() {
		 getBrowserFunctions().close();
	}

	@Override
	public void setNegativeStatus() {
		 getBrowserFunctions().close();
	}

}
