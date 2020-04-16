package framework.logs;

import org.apache.log4j.Logger;

import framework.enums.LogVerbose;

public class LogAccess {
	private String logFileName;
	private static final Logger log = Logger.getLogger(LogAccess.class);
	public LogAccess(String logFileName, LogVerbose logLevel) {
		this.logFileName = logFileName;
		
	}
	
	
}
