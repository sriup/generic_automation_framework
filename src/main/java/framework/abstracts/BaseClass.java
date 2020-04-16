package framework.abstracts;

import framework.commonfunctions.BrowserFunctions;

public class BaseClass {
	BrowserFunctions browserFunctions = new BrowserFunctions();
	public BrowserFunctions getBrowserFunctions() {
		return browserFunctions;
	}

	public void setBrowserFunctions(BrowserFunctions browserFunctions) {
		this.browserFunctions = browserFunctions;
	}
}
