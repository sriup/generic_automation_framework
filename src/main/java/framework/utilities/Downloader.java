package framework.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import framework.logs.LogAccess;

public class Downloader {
	
	public static void downloadFile(LogAccess logAceess, String url, String driverZipPath) throws MalformedURLException, IOException {
		File tempZipFile = new File(driverZipPath);
		// donwload the zip file to temp directory
		FileUtils.copyURLToFile(new URL(url), tempZipFile);
	}
}
