package framework.abstracts;

import framework.commonfunctions.BrowserFunctions;

public abstract class FwBaseClass {
	
	private BrowserFunctions browserFunctions = new BrowserFunctions();
	private String screenshotPath;
	
	public void init(String logFilename, String downloadPath, String browserType) throws Exception {
		// TODO Auto-generated constructor stub
		
		setLogAccessFilename(logFilename);
		
		this.browserFunctions = new BrowserFunctions();
		browserFunctions.launch(browserType, downloadPath);
		
	}

	public BrowserFunctions getBrowserFunctions() {
		return browserFunctions;
	}

	public void setBrowserFunctions(BrowserFunctions browserFunctions) {
		this.browserFunctions = browserFunctions;
	}
	
	
	
	public String getScreenshotPath() {
		return screenshotPath;
	}

	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
	}

	//Initialize Logger
	public void setLogAccessFilename(String filename) {
		
	}
	
	public abstract void createScreenshotPath(String screenshotFolderName);
	
}
