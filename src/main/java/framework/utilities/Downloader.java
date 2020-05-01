package framework.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import framework.logs.LogAccess;

public class Downloader {
	
	/**
	 * Downloads the file from the URL
	 * @param logAceess 
	 * LogAccess instance
	 * @param url		
	 * URL from where the file should be download
	 * @param driverZipPath 
	 * 	
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void downloadFile(LogAccess logAceess, String url, String destination) throws MalformedURLException, IOException {
		File tempFile = new File(destination);
		// Download the file to the destination directory
		FileUtils.copyURLToFile(new URL(url), tempFile);
	}
}
