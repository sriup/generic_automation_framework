package framework.Docker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;

public class DockerManager {
	private static final String PROJECT_DIR = System.getProperty("user.dir");
	private static final String DOCKER_LOG_FILE = "docker_logs.txt";
	private static final Path DOCKER_LOG_PATH = Path.of(PROJECT_DIR + File.separatorChar + DOCKER_LOG_FILE);

	private static final String NODE_STARTED_MESSAGE = " Node has been added";
	private static final String HUB_STOPPED_MESSAGE = "selenium-hub exited with code 0";

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
		Runtime runtime = Runtime.getRuntime();

		// TODO Need to implement logic to leverage the compose and config file from the framework folder rather project level
		// to have centralized control
		switch (operation) {
			case "START":
				runtime.exec("cmd /c docker compose -f docker-compose-v3-dynamic-grid.yml up >> ./docker_logs.txt");
				waitForDockerMessage(NODE_STARTED_MESSAGE);
				System.out.println("Selenium Grid is up");
				break;
			case "STOP":
				runtime.exec("cmd /c docker compose -f docker-compose-v3-dynamic-grid.yml down");
				waitForDockerMessage(HUB_STOPPED_MESSAGE);
				System.out.println("Selenium Grid is stopped.");
				break;
			default:
				throw new RuntimeException("Please provide either START or STOP for running docker");
		}
	}
	private void waitForDockerMessage(String message) throws IOException {
		LocalTime endTime = LocalTime.now().plus(Duration.ofSeconds(120));
		String logContent = "";
		boolean isMessageFound = false;

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
