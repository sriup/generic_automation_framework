package framework.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.FileUtils;

import framework.logs.LogAccess;

// TODO: Auto-generated Javadoc
/**
 * All methods related to the file and folder.
 */
public class FolderFileUtils {

	/** The log access. */
	private LogAccess logAccess;

	/**
	 * Instantiates a new folder file utils.
	 *
	 * @param logAccess the log access
	 */
	public FolderFileUtils(LogAccess logAccess) {
		// TODO Auto-generated constructor stub
		this.logAccess = logAccess;
	}

	/**
	 * It creates the folder if folder is not present or else it will return the
	 * existing Absolute path.
	 *
	 * @param folderPath            Provide the Absolute Folder path
	 * @param folderName the folder name
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File createFolder(String folderPath, String folderName) throws Exception {

		this.logAccess.getLogger().info("FolderPath :- " + folderPath);
		this.logAccess.getLogger().info("folderName :- " + folderName);
		
		File folderFile = new File(folderPath + folderName);
		if (!folderFile.exists()) {
			if (folderFile.mkdir()) {
				 this.logAccess.getLogger().info("Folder is created!");
			}
		}
		return folderFile;
	}

	/**
	 * It creates the folder if folder is not present or else it will return the
	 * existing Absolute path.
	 *
	 * @param folderPath Provide the Absolute Folder path
	 * @return the file
	 * @throws Exception the exception
	 */
	public File createFolder(String folderPath) throws Exception {

		this.logAccess.getLogger().info("FolderPath :- " + folderPath);
		
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			if (folderFile.mkdir()) {
				this.logAccess.getLogger().info("Folder is created!");
			}
		}
		return folderFile;
	}
	
	/**
	 * It deletes the file or folder if it exists.
	 *
	 * @param folderPath            Provide the Absolute Folder path
	 * @param fileOrFolderName Provide the Filename or the Foldername
	 * @throws Exception the exception
	 */
	public void deleteFileOrFolder(String folderPath, String fileOrFolderName) throws Exception {

		this.logAccess.getLogger().info("folderPath :- " + folderPath);
		this.logAccess.getLogger().info("fileOrFolderName :- " + fileOrFolderName);
		
		File folderFile = new File(folderPath + fileOrFolderName);

		if (!folderFile.exists()) {
			if (folderFile.delete()) {
				 this.logAccess.getLogger().info("Folder is deleted!");
			}
		}
	}
	
	/**
	 * It deletes the file or folder if it exists.
	 *
	 * @param folderPath            Provide the Absolute Folder path
	 * @throws Exception the exception
	 */
	public void deleteFileOrFolder(String folderPath) throws Exception {

		this.logAccess.getLogger().info("folderPath :- " + folderPath);
		
		File folderFile = new File(folderPath);
		 
		if (!folderFile.exists()) {
			if (folderFile.delete()) {
				 this.logAccess.getLogger().info("Folder is deleted!");
			}
		}
	}

	// create file
	/**
	 * It will create the file if it is not present.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File createFile(String filePath, String filename) throws Exception {

		this.logAccess.getLogger().info("filePath :- " + filePath);
		this.logAccess.getLogger().info("filename :- " + filename);
		
		createFolder(filePath);
		
		File file = new File(filePath + filename);

		String logMessage = (file.createNewFile()) ? "File is created!" : "File already exists.";

		//TODO
		 this.logAccess.getLogger().info(logMessage);

		return file;

	}

	// Write file
	/**
	 * Writing the single inputed line to the specified File.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @param inputLine the input line
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File writeToTextFile(String filePath, String filename, String inputLine) throws Exception {

		this.logAccess.getLogger().info("filePath :- " + filePath);
		this.logAccess.getLogger().info("filename :- " + filename);
		
		this.logAccess.getLogger().info("inputLine :- " + inputLine);

		File file = createFile(filePath, filename);

		// Write Content
		FileWriter writer = new FileWriter(file);
		writer.write(inputLine);
		writer.close();

		this.logAccess.getLogger().info("Writing to text file is completed");

		return file;
	}

	/**
	 * Writing Multiple lines to the file.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @param inputLinesList the input lines list
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File writeMultiLinesToTextFile(String filePath, String filename, List<String> inputLinesList) throws Exception {
		
		this.logAccess.getLogger().info("filePath :- " + filePath);
		this.logAccess.getLogger().info("filename :- " + filename);

		File file = createFile(filePath, filename);

		// Write Content
		FileWriter writer = new FileWriter(file);

		for (String currentInputLine : inputLinesList) {
			
			this.logAccess.getLogger().info("Writing Current input line :- " + inputLinesList);

			writer.write(currentInputLine);
		}

		writer.close();

		return file;
	}

	// read file
	/**
	 * Fetching all the lines from the file.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @return List of lines in String format
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<String> readTextFile(String filePath, String filename) throws IOException {
		
		this.logAccess.getLogger().info("filePath :- " + filePath);
		this.logAccess.getLogger().info("filename :- " + filename);
		
		List<String> lines = FileUtils.readLines(new File(filePath), "utf-8");
		
		return lines;
	}
	
	/**
	 * Fetching the single line by specified line number from the file .
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @param lineNumber The line number which needs to be returned
	 * @return The line by the line number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readTextFile(String filePath, String filename, int lineNumber) throws IOException {
		
		this.logAccess.getLogger().info("filePath :- " + filePath);
		this.logAccess.getLogger().info("filename :- " + filename);
		this.logAccess.getLogger().info("lineNumber :- " + lineNumber);
		
		List<String> lines = FileUtils.readLines(new File(filePath), "utf-8");
		
		return lines.get(lineNumber);
	}
	
	/**
	 * Moving the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param newFilePath Provide the New Absolute target File path
	 * @param filename the filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File moveFile(String oldFilePath, String newFilePath, String filename) throws IOException {
		
		File oldFile = new File(oldFilePath + filename);
		
		File newFile = new File(newFilePath + filename);
		
		Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		this.logAccess.getLogger().info("The file '" + filename + "' got moved from '" + oldFilePath + "' to target location '" + newFilePath + "'");
		
		return newFile;
		
	}
	
	/**
	 * Copying the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param newFilePath Provide the New Absolute target File path
	 * @param filename the filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(String oldFilePath, String newFilePath, String filename) throws IOException {
		
		File oldFile = new File(oldFilePath + filename);
		
		File newFile = new File(newFilePath + filename);
		
		Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		this.logAccess.getLogger().info("The file '" + filename + "' got copied from '" + oldFilePath + "' to target location '" + newFilePath + "'");
		
		return newFile;
		
	}
	
	/**
	 * Renaming the current filename to the new filename.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param oldFilename Provide the old filename
	 * @param newFilename Provide the new filename to be changed
	 * @return Whether it is renamed successfully of not in boolean.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean renameFilename(String filePath, String oldFilename, String newFilename) throws IOException {

		File oldFile = new File(filePath + oldFilename);

		File newFile = new File(filePath + newFilename);

		if (newFile.exists()) {
			
			this.logAccess.getLogger().info("newFilename '" + newFilename + "' is already exists in the filepath '" + filePath + "'");

			throw new java.io.IOException("file exists");
		}
		
		// Rename file (or directory)
		boolean success = oldFile.renameTo(newFile);
		
		this.logAccess.getLogger().info("newFilename '" + newFilename + "' is renamed in the filepath '" + filePath + "'");
		
		return success;

	}
	
}
