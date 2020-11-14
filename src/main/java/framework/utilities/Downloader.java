package framework.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import framework.logs.LogAccess;

public class Downloader {

	/**
	 * Downloads the file from the URL <font color="blue"><b>Note : </b> File will
	 * be override with the new download, if already exist in the
	 * destination.</font>
	 * 
	 * @param logAccess   LogAccess instance
	 * @param url         URL from where the file should be download
	 * @param destination destination folder path to save the file
	 * 
	 * @throws MalformedURLException exception
	 * @throws IOException           exception
	 */
	public void downloadFile(LogAccess logAccess, String url, String destination)
			throws MalformedURLException, IOException {
		File tempFile = new File(destination);
		// Download the file to the destination directory
		FileUtils.copyURLToFile(new URL(url), tempFile);
	}
}
