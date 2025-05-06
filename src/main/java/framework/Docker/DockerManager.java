package framework.Docker;

import framework.constants.CommonVariables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;

public class DockerManager {
	private static final String DOCKER_LOG_FILE = "docker_logs.txt";
	private static final String DOCKER_START_FILE_NAME = "startDockerSeleniumHub" + (CommonVariables.osName.equalsIgnoreCase("linux")?".sh":".bat");
//	private static final String DOCKER_STOP_FILE_NAME = "stopDockerSeleniumHub.bat";
	private static final String DOCKER_STOP_FILE_NAME = "cleanup" + (CommonVariables.osName.equalsIgnoreCase("linux")?".sh":".bat");
	private static final Path DOCKER_LOG_PATH = Path.of(CommonVariables.DOCKER_FOLDER_PATH + File.separatorChar + DOCKER_LOG_FILE);

	private static final String NODE_STARTED_MESSAGE = "Node has been added";
	private static final String HUB_STOPPED_MESSAGE = "stopped: selenium-grid-hub";

	/**
	 * perform the docker operations
	 * Notes:
	 * 	START - will run the bat/sh file with docker compose up command
	 * 	STOP - will run the bat/sh file with docker compose down command
	 * @param operation the operation to perform START/STOP
	 * @throws Exception the exception
	 */
	public void dockerOperation(String operation) throws Exception {
		ProcessBuilder pb;
		File dir;
		Process p;


		switch (operation.toUpperCase()) {
			//TODO Need to clear the cmd windows process once the pb is done.
			case "START":

				pb = new ProcessBuilder("C:\\Windows\\System32\\cmd.exe", "/c", "start", "/b", DOCKER_START_FILE_NAME);
				dir = new File(CommonVariables.DOCKER_FOLDER_PATH);
				pb.directory(dir);
				p = pb.start();
				CommonVariables.docker_composer_triggered = Files.readString(Paths.get(CommonVariables.DOCKER_FOLDER_PATH + File.separatorChar + "status.txt")).trim().contains("true");
				waitForDockerMessage(NODE_STARTED_MESSAGE);
				Thread.sleep(15000);
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

	/**
	 * wait for specific docker message displayed in the docker_logs.txt file
	 * @param message the message to be displayed
	 * @throws Exception the exception
	 */
	private void waitForDockerMessage(String message) throws Exception {

		// the end time to complete the waiting for the message.
		LocalTime endTime = LocalTime.now().plus(Duration.ofSeconds(300));
		// initialize the variables
		String logContent = "";
		boolean isMessageFound = false;

		while(LocalTime.now().isBefore(endTime)){
			File f = new File(String.valueOf(DOCKER_LOG_PATH));
			if(f.exists() && !f.isDirectory()) { break;}
		}

		// wait until the time is passed
		while (LocalTime.now().isBefore(endTime)) {
			// read the content from docker_logs.txt file
			logContent = Files.readString(DOCKER_LOG_PATH);
			// check if the log contains the specified message
			if (logContent.contains(message)) {
				// set the flag to true and exit the loop
				isMessageFound = true;
				break;
			}
		}


		if (!isMessageFound) {
			throw new RuntimeException(message + " is not started found, check corresponding action for errors.");
		} else{
			System.out.println(message + " displayed in the log file.");
		}
	}
}
