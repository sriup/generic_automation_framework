package framework.abstracts;

import framework.commonfunctions.BrowserFunctions;

public abstract class FwBaseClass {
	
	BrowserFunctions browserFunctions = new BrowserFunctions();
	
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
	
	//Initialize Logger
	public void setLogAccessFilename(String filename) {
		
	}
	
	public abstract void createScreenshotPath(String screenshotFolderName);
	
}
