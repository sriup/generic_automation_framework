package framework.utilities;

import java.io.File;
import java.io.IOException;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import framework.logs.LogAccess;
import io.restassured.response.Response;

public class JsonUtil {
	private LogAccess logAccess;

	public JsonUtil(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	public String getValue(String jsonFilePath, String jsonPath) throws IOException {
		DocumentContext jPathDocCon = null;
		jPathDocCon = JsonPath.parse(new File(jsonFilePath));
		String result;
		result = jPathDocCon.read(jsonPath).toString();
		this.logAccess.getLogger().info("JSonPath : " + jsonPath + "\n Result : " + result);
		return result;
	}

	public String getValue(Response response, String jsonPath) {
		String result;
		result = response.jsonPath().getString(jsonPath);
		this.logAccess.getLogger().info("JSonPath : " + jsonPath + "\n Result : " + result);
		return result;
	}

}
