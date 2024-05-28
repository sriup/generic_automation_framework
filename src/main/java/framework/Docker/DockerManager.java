package framework.Docker;

import framework.constants.CommonVariables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;

public class DockerManager {
	private static final String DOCKER_LOG_FILE = "docker_logs.txt";
	private static final String DOCKER_START_FILE_NAME = "startDockerSeleniumHub.bat";
//	private static final String DOCKER_STOP_FILE_NAME = "stopDockerSeleniumHub.bat";
	private static final String DOCKER_STOP_FILE_NAME = "cleanup.bat";
	private static final Path DOCKER_LOG_PATH = Path.of(CommonVariables.DOCKER_FOLDER_PATH + File.separatorChar + DOCKER_LOG_FILE);

	private static final String NODE_STARTED_MESSAGE = "Node has been added";
	private static final String HUB_STOPPED_MESSAGE = "stopped: selenium-grid-hub";

	public DockerManager() throws IOException {
		clearOldLogFile();
	}

	/**
	 * Delete docker log file
	 * @throws IOException IO Exception
	 */
	private void clearOldLogFile() throws IOException {
		if (Files.exists(DOCKER_LOG_PATH)) {
			Files.delete(DOCKER_LOG_PATH);
			System.out.println("Docker Log file deleted.");
			Files.createFile(DOCKER_LOG_PATH);
		}
	}

	/**
	 *
	 * @param operation
	 */
	public void dockerOperation(String operation) throws IOException {
		ProcessBuilder pb;
		File dir;
		Process p;

		// TODO Need to implement logic to leverage the compose and config file from the framework folder rather project level
		// to have centralized control
		switch (operation) {
			case "START":
				pb = new ProcessBuilder("C:\\Windows\\System32\\cmd.exe", "/c", "start", "/b", DOCKER_START_FILE_NAME);
				dir = new File(CommonVariables.DOCKER_FOLDER_PATH);
				pb.directory(dir);
				p = pb.start();
				waitForDockerMessage(NODE_STARTED_MESSAGE);
				System.out.println("Selenium Grid is up");
				break;
			case "STOP":
				pb = new ProcessBuilder("C:\\Windows\\System32\\cmd.exe", "/c", "start", "/b", DOCKER_STOP_FILE_NAME);
				dir = new File(CommonVariables.DOCKER_FOLDER_PATH);
				pb.directory(dir);
				p = pb.start();
				waitForDockerMessage(HUB_STOPPED_MESSAGE);
				System.out.println("Selenium Grid is stopped.");
				break;
			default:
				throw new RuntimeException("Please provide either START or STOP for running docker");
		}
	}
	private void waitForDockerMessage(String message) throws IOException {
		LocalTime endTime = LocalTime.now().plus(Duration.ofSeconds(300));
		String logContent = "";
		boolean isMessageFound = false;
		boolean waitForFile = true;

		while(LocalTime.now().compareTo(endTime) < 0 && waitForFile){
			File f = new File(String.valueOf(DOCKER_LOG_PATH));
			if(f.exists() && !f.isDirectory()) { waitForFile=false; break;}
		}

		while (LocalTime.now().compareTo(endTime) < 0) {
			logContent = Files.readString(DOCKER_LOG_PATH);
			if (logContent.contains(message)) {
				isMessageFound = true;
				break;
			}
		}

		if (!isMessageFound)
			throw new RuntimeException(message + " is not started found, check corresponding action for errors.");
	}
}
