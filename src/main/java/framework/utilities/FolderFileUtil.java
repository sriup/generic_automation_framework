package framework.utilities;

import framework.logs.LogAccess;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All methods related to the file and folder.
 */
public class FolderFileUtil {

	/** The log access. */
	private final LogAccess logAccess;

	public void normalizeFilePath(String filePath) {
		// TODO need to implement this
	}

	/**
	 * Instantiates a new folder file utils.
	 *
	 * @param logAccess the log access
	 */
	public FolderFileUtil(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * It creates the folder if folder is not present or else it will return the
	 * existing Absolute path.
	 *
	 * @param folderPath Provide the Absolute Folder path
	 * @param folderName the folder name
	 * @return The File object
	 */
	public File createFolder(String folderPath, String folderName) {

		this.logAccess.getLogger().debug("FolderPath :- " + folderPath);
		this.logAccess.getLogger().debug("folderName :- " + folderName);

		File folderFile = new File(folderPath + File.separatorChar + folderName);
		if (!folderFile.exists()) {
			if (folderFile.mkdirs()) {
				this.logAccess.getLogger().debug("Folder is created!");
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
	 */
	public File createFolder(String folderPath) {

		this.logAccess.getLogger().debug("FolderPath :- " + folderPath);

		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			if (folderFile.mkdirs()) {
				this.logAccess.getLogger().debug("Folder is created!");
			}
		}
		return folderFile;
	}

	/**
	 * It deletes the file if it exists.
	 *
	 * @param fileObject Provide the file object
	 */
	public boolean deleteFile(File fileObject) {
		String filePath = fileObject.getAbsolutePath();
		fileObject.delete();
		File deletedFile = new File(filePath);
		if (!deletedFile.exists()) {
			this.logAccess.getLogger().debug("File is deleted!");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * It deletes the file if it exists.
	 *
	 * @param filePath Provide the Absolute File path
	 */
	public boolean deleteFile(String filePath) {
		this.logAccess.getLogger().debug("file Path :- " + filePath);
		return deleteFile(new File(filePath));
	}

	/**
	 * It deletes the file if it exists.
	 *
	 * @param folderPath Provide the Absolute folder path
	 * @param fileName   Provide the file name
	 * @return the delete file status
	 */
	public boolean deleteFile(String folderPath, String fileName) {

		this.logAccess.getLogger().debug("folder Path :- " + folderPath);
		this.logAccess.getLogger().debug("file Name :- " + fileName);
		return deleteFile(new File(folderPath + File.separatorChar + fileName));
	}

	/**
	 * It deletes the folder if it exists.
	 *
	 * @param folderPath Provide the Absolute Folder path
	 * @param folderName Provide the Folder name
	 * @throws Exception the exception
	 */
	public boolean deleteFolder(String folderPath, String folderName) throws Exception {

		this.logAccess.getLogger().debug("folder Path :- " + folderPath);
		this.logAccess.getLogger().debug("folder Name :- " + folderName);

		File folderFile = new File(folderPath + File.separatorChar + folderName);
		return deleteFolder(folderFile);
	}

	/**
	 * It deletes the folder if it exists.
	 *
	 * @param folderObject Provide the Folder object
	 * @throws Exception the exception
	 */
	public boolean deleteFolder(File folderObject) throws Exception {
		String folderPath = folderObject.getAbsolutePath();
		FileUtils.deleteDirectory(folderObject);
		File deletedFolder = new File(folderPath);
		if (!deletedFolder.exists()) {
			this.logAccess.getLogger().debug("Folder is deleted!");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * It deletes the folder if it exists.
	 *
	 * @param folderPath Provide the Absolute Folder path
	 * @throws Exception the exception
	 */
	public boolean deleteFolder(String folderPath) throws Exception {

		this.logAccess.getLogger().debug("folder Path :- " + folderPath);

		File folderFile = new File(folderPath);
		return deleteFolder(folderFile);
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

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);

		createFolder(filePath);

		File file = new File(filePath + File.separatorChar + filename);

		String logMessage = (file.createNewFile()) ? "File is created!" : "File already exists.";

		// TODO
		this.logAccess.getLogger().debug(logMessage);

		return file;

	}

	// Write file
	/**
	 * Writing the single inputted line to the specified File.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @param input    the input line
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File writeToTextFile(String filePath, String filename, String input) throws Exception {

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);

		this.logAccess.getLogger().debug("input :- " + input);

		File file = createFile(filePath, filename);

		// Write Content
		FileWriter writer = new FileWriter(file);
		writer.write(input);
		writer.close();

		this.logAccess.getLogger().debug("Writing to text file is completed");

		return file;
	}

	/**
	 * Writing Multiple lines to the file.
	 *
	 * @param filePath       Provide the Absolute File path
	 * @param filename       the filename
	 * @param inputLinesList the input lines list
	 * @return The File object
	 * @throws Exception the exception
	 */
	public File writeMultiLinesToTextFile(String filePath, String filename, List<String> inputLinesList)
			throws Exception {

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);

		File file = createFile(filePath, filename);

		// Write Content.
		FileWriter writer = new FileWriter(file);
		
		int counter = 1;
		
		for (String currentInputLine : inputLinesList) {

			this.logAccess.getLogger().debug("Writing Current input line :- " + inputLinesList);

			writer.write(currentInputLine);
			
			if(counter < inputLinesList.size()){
				writer.write("\r\n");
			}
			
			counter++;
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

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);

		return FileUtils.readLines(new File(filePath + File.separatorChar + filename), "utf-8");
	}

	/**
	 * Fetching all the lines from the file.
	 *
	 * @param filePath Provide the Absolute File path
	 * @param filename the filename
	 * @return List of lines in String format
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readTextFileToString(String filePath, String filename) throws IOException {

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);

		return FileUtils.readFileToString(new File(filePath + File.separatorChar + filename), "utf-8");
	}

	/**
	 * Fetching the single line by specified line number from the file .
	 *
	 * @param filePath   Provide the Absolute File path
	 * @param filename   the filename
	 * @param lineNumber The line number which needs to be returned
	 * @return The line by the line number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readTextFile(String filePath, String filename, int lineNumber) throws IOException {

		this.logAccess.getLogger().debug("filePath :- " + filePath);
		this.logAccess.getLogger().debug("filename :- " + filename);
		this.logAccess.getLogger().debug("lineNumber :- " + lineNumber);

		List<String> lines = FileUtils.readLines(new File(filePath + File.separatorChar + filename), "utf-8");

		return lines.get(lineNumber);
	}

	/**
	 * Moving the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param newFilePath Provide the New Absolute target File path
	 * @param filename    the filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File moveFile(String oldFilePath, String newFilePath, String filename) throws IOException {

		File oldFile = new File(oldFilePath + File.separatorChar + filename);

		File newFile = new File(newFilePath + File.separatorChar + filename);

		Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		this.logAccess.getLogger().debug("The file '" + filename + "' got moved from '" + oldFilePath
				+ "' to target location '" + newFilePath + "'");

		return newFile;

	}


	/**
	 * Moving the File from one location to the other location.
	 *
	 * @param sourceFilePath the Old Absolute source File path
	 * @param sourceFileName the source file name
	 * @param newFilePath the New Absolute target File path
	 * @param newFileName    the newFileName
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File moveFile(String sourceFilePath, String sourceFileName, String newFilePath, String newFileName) throws IOException {

		File oldFile = new File(sourceFilePath + File.separatorChar + sourceFileName);

		File newFile = new File(newFilePath + File.separatorChar + newFileName);

		Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		this.logAccess.getLogger().debug("The file '" + newFileName + "' got moved from '" + sourceFilePath
				+ "' to target location '" + newFilePath + "'");

		return newFile;

	}

	/**
	 * Copying the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param newFilePath Provide the New Absolute target File path
	 * @param filename    the filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(String oldFilePath, String newFilePath, String filename, StandardCopyOption standardCopyOption)
			throws IOException {

		File oldFile = new File(oldFilePath + File.separatorChar + filename);

		File newFile = new File(newFilePath + File.separatorChar + filename);

		return copyFile(oldFile, newFile, standardCopyOption);
	}

	/**
	 * Copying the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param oldFileName the old filename
	 * @param newFilePath Provide the New Absolute target File path
	 * @param newFileName    the new filename
	 * @param standardCopyOption  the {@link StandardCopyOption}
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(String oldFilePath, String oldFileName, String newFilePath, String newFileName, StandardCopyOption standardCopyOption)
			throws IOException {

		File oldFile = new File(oldFilePath + File.separatorChar + oldFileName);

		File newFile = new File(newFilePath + File.separatorChar + newFileName);

		return copyFile(oldFile, newFile, standardCopyOption);
	}

	/**
	 * Copying the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param oldFileName the old filename
	 * @param newFilePath Provide the New Absolute target File path
	 * @param newFileName    the new filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(String oldFilePath, String oldFileName, String newFilePath, String newFileName)
			throws IOException {

		File oldFile = new File(oldFilePath + File.separatorChar + oldFileName);

		File newFile = new File(newFilePath + File.separatorChar + newFileName);

		return copyFile(oldFile, newFile);
	}

	/**
	 * Copying the File from one location to the other location.
	 * 
	 * @param oldFile The object of the old file
	 * @param newFile The object of the new file
	 * @param standardOption  the {@link StandardCopyOption}
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(File oldFile, File newFile, StandardCopyOption standardOption) throws IOException {
		FileUtils.forceMkdirParent(newFile);
		Files.copy(oldFile.toPath(), newFile.toPath(), standardOption);

		this.logAccess.getLogger().debug("The file '" + oldFile.getName() + "' got copied from '"
				+ oldFile.getAbsolutePath() + "' to target location '" + newFile.getAbsolutePath() + "'");

		return newFile;

	}

	/**
	 * Copying the File from one location to the other location.
	 *
	 * @param oldFilePath Provide the Old Absolute source File path
	 * @param newFilePath Provide the New Absolute target File path
	 * @param filename    the filename
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(String oldFilePath, String newFilePath, String filename) throws IOException {
		return copyFile(oldFilePath, newFilePath, filename, StandardCopyOption.REPLACE_EXISTING);

	}

	/**
	 * Copying the File from one location to the other location.
	 * 
	 * @param oldFile The object of the old file
	 * @param newFile The object of the new file
	 * @return It will return the New File object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyFile(File oldFile, File newFile) throws IOException {
		return copyFile(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);

	}

	/**
	 * Renaming the current filename to the new filename.
	 *
	 * @param filePath    Provide the Absolute File path
	 * @param oldFilename Provide the old filename
	 * @param newFilename Provide the new filename to be changed
	 * @return Whether it is renamed successfully of not in boolean.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean renameFilename(String filePath, String oldFilename, String newFilename) throws IOException {

		File oldFile = new File(filePath + File.separatorChar + oldFilename);

		File newFile = new File(filePath + File.separatorChar + newFilename);

		if (newFile.exists()) {

			this.logAccess.getLogger()
					.info("newFilename '" + newFilename + "' is already exists in the filepath '" + filePath + "'");

			throw new java.io.IOException("file exists");
		}

		// Rename file (or directory)
		boolean success = oldFile.renameTo(newFile);

		this.logAccess.getLogger()
				.info("newFilename '" + newFilename + "' is renamed in the filepath '" + filePath + "'");

		return success;

	}

	/**
	 * Fetch list of all the files from the folder.
	 * 
	 * Note:- This will only fetch files from the folder. It will not fetch files
	 * from the sub-folder.
	 * 
	 * @param folderPath Provide the path of the folder where it has all the files
	 * @return The list of all the files from the expected folder
	 */
	public List<File> getAllFiles(String folderPath) {

		File dir = new File(folderPath);

		List<File> fileList = new ArrayList<File>();
		File[] files = dir.listFiles();
		fileList.addAll(Arrays.asList(files));

		return fileList;

	}

	/**
	 * Fetch list of all the files from the folder.
	 * 
	 * Note:- This will only fetch files from the folder. It will not fetch files
	 * from the sub-folder.
	 * 
	 * @param folderPath Provide the path of the folder where it has all the files
	 * @return The list of all the files from the expected folder
	 */
	public List<String> getAllFileNames(String folderPath) {

		File dir = new File(folderPath);

		List<String> filenamesList = new ArrayList<String>();
		File[] files = dir.listFiles();
		for (File file : files) {
			filenamesList.add(file.getName());
		}

		return filenamesList;

	}

}
