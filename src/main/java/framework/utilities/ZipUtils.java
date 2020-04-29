package framework.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import framework.logs.LogAccess;


/**
 * All the methods related to the zip operations will be  handled in this class.
 */
public class ZipUtils {
	
	private LogAccess logAccess;
	
	public ZipUtils(LogAccess logAccess) {
		this.logAccess = logAccess;
	}
	
	// zip folder
	/**
	 * Zipping all the files in the mentioned folder.
	 * Note:- The zipping will happen only on folder level files not on the sub-folder files 
	 * @param folderPath
	 */
	public void zipFolder(String folderPath) {
		
		FolderFileUtils fFUtils = new FolderFileUtils(logAccess);
		
		this.logAccess.getLogger().debug("Fetching all the files from the folder path '" + folderPath + "'");
		
		List<File> filesList = fFUtils.getAllFiles(folderPath);
		
		this.logAccess.getLogger().debug("Number of files are '" + filesList.size() + "'");
		
		File directoryToZip = new File(folderPath);
		
		String destinationFolder = directoryToZip.getParentFile().getAbsolutePath();
		
		this.logAccess.getLogger().debug("Going to zip the folder in the destination folder '" + destinationFolder + "'");
		
		zipFiles(directoryToZip, filesList, destinationFolder);
		
	}
	
	

	/**
	 * Zipping the files in the mentioned Folder
	 * @param directoryToZip The folder name in which all the files to be zipped
	 * @param fileList List of all the files to be zipped
	 * @param destinationFolder Destination folder where the zip file to be saved
	 */
	public void zipFiles(File directoryToZip, List<File> fileList, String destinationFolder) {

		try {
			this.logAccess.getLogger().debug("directoryToZip :- '" + directoryToZip + "' and the destinationFolder :- '" + destinationFolder + "'");
			
			FileOutputStream fos = new FileOutputStream(destinationFolder + File.separator + directoryToZip.getName() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) {
					this.logAccess.getLogger().debug("Zipping only files. Not zipping on directories");
					addToZip(directoryToZip, file, zos);
				}
			}
			
			this.logAccess.getLogger().debug("Completed zipping all the files");

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			
			this.logAccess.getLogger().error("File not found!");
			
			e.printStackTrace();
		} catch (IOException e) {
			
			this.logAccess.getLogger().error("Signals that an I/O exception has occurred.");
			
			e.printStackTrace();
		}
	}

	/**
	 * Adding individual file to the ZipOutputStream
	 * @param directoryToZip The folder name in which the files are present
	 * @param file File to be zipped
	 * @param zos ZipOutputStream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		this.logAccess.getLogger().debug("directoryToZip :- '" + directoryToZip + "' and the filename :- '" + file.getName() + "'");
		
		FileInputStream fis = new FileInputStream(file);

		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		
		this.logAccess.getLogger().debug("Writing '" + zipFilePath + "' to zip file");
		
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	/**
	 * Unzip the zipped files to the destination folder
	 * @param zipFilePath Zipped Files path
	 * @param destDir Destination folder
	 */
	public void unzip(String zipFilePath, String destDir) {
		
		this.logAccess.getLogger().debug("zipFilePath :- '" + zipFilePath + "' and the destDir :- '" + destDir + "'");
		
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                
                this.logAccess.getLogger().debug("Unzipping to "+newFile.getAbsolutePath());
                
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	
	/**
	 * Fetching all the filenames from the Zip
	 * @param filePath Filepath for the zip
	 * @return List of all the filenames
	 */
	public ArrayList<String> getFilenamesInZip(String filePath){
        
		this.logAccess.getLogger().debug("filePath :- '" + filePath + "'");
		
        FileInputStream fileInptStrm = null;
        ZipInputStream zipInptStrm = null;
        ZipEntry file = null;
        ArrayList<String> listOfFiles = new ArrayList<String>();
        
        try {
        	fileInptStrm = new FileInputStream(filePath);
            zipInptStrm = new ZipInputStream(new BufferedInputStream(fileInptStrm));
            while((file = zipInptStrm.getNextEntry()) != null){
            	String currentFilename = file.getName();
            	
            	this.logAccess.getLogger().debug("currentFilename :- '" + currentFilename + "'");
            	
            	listOfFiles.add(currentFilename);
            }
            zipInptStrm.close();
            fileInptStrm.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return listOfFiles;
    }
	
	
	
}
