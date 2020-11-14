package framework.utilities;

import java.io.File;
import java.io.IOException;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import framework.logs.LogAccess;

public class JsonUtil {
	private final LogAccess logAccess;

	public JsonUtil(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * Get Value from from file based on the json path.
	 * 
	 * @param jsonFilePath Json file path
	 * @param jsonPath     Json path
	 * @return value
	 * @throws IOException I/O exception
	 */
	public String getValue(String jsonFilePath, String jsonPath) throws IOException {
		DocumentContext jPathDocCon;
		jPathDocCon = JsonPath.parse(new File(jsonFilePath));
		String result;
		result = jPathDocCon.read(jsonPath).toString();
		this.logAccess.getLogger().debug("JSonPath : " + jsonPath + "\n Result : " + result);
		return result;
	}

}
