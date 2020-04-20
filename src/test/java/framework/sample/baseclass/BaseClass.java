package framework.sample.baseclass;

import java.io.File;

import framework.abstracts.FwBaseClass;
import framework.constants.CommonVariables;
import framework.utilities.FolderFileUtils;

public class BaseClass extends FwBaseClass {


	private FolderFileUtils fileUtils;
	
	public BaseClass(String role, String methodName, int retryCount) throws Exception {
		super();
		// TODO Auto-generated constructor stub
		
		fileUtils = new FolderFileUtils();
		
		String logFilename = role + methodName + "_" + retryCount;
		
		//Create Screenshot folder path
		createScreenshotPath(methodName);
		
		init(logFilename, this.getScreenshotPath(), CommonVariables.BROWSER_SELECT);
		
	}

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
