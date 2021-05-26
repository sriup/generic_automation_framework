package framework.commonfunctions;

import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will holds the common methods related to the rest API automation.
 *
 */
public class ApiMethods {

	private final LogAccess logAccess;
	private FolderFileUtil folderFileUtil;

	public final Map<String, Object> defaultHeadersMap;

	/**
	 * Instantiates a new APIMethods object to set the log access.
	 * 
	 * @param logAccess instance of{@link LogAccess}
	 */
	public ApiMethods(LogAccess logAccess) {
		this.logAccess = logAccess;
		defaultHeadersMap = new HashMap<String, Object>();
		defaultHeadersMap.put("Content-Type", "application/json");
		defaultHeadersMap.put("Accept", "application/json");

	}

	/**
	 * makes Get request to the rest API
	 * 
	 * @param requestType request type
	 * @param uri         uri/endpoint
	 * @return the response object {@link io.restassured.response.Response Response}
	 * 
	 */
	public Response sendRequest(String requestType, String uri) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given();
		// request header
		defaultHeadersMap.forEach((k, v) -> httpRequest.header(k, v));
		Response response;
		// send request
		if (requestType.toLowerCase().equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("delete")) {
			response = httpRequest.delete(uri);
		} else {
			this.logAccess.getLogger().debug("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");

			throw new IllegalArgumentException("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");
		}
		this.logAccess.getLogger().debug(response.body().prettyPrint());
		return response;
	}

	/**
	 * @param requestType request type
	 * @param uri         uri/endpoint
	 * @param headers     headers
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendRequest(String requestType, String uri, HashMap<String, String> headers) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given();
		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));
		this.logAccess.getLogger().debug("headers \n " + headers.toString());
		Response response;
		// send request
		if (requestType.toLowerCase().equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("delete")) {
			response = httpRequest.delete(uri);
		} else {
			this.logAccess.getLogger().debug("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");

			throw new IllegalArgumentException("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");
		}
		this.logAccess.getLogger().debug(response.body().prettyPrint());
		return response;
	}

	/**
	 * @param requestType request type
	 * @param uri         uri/endpoint
	 * @param headers     headers
	 * @param data        data
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendReqResponse(String requestType, String uri, HashMap<String, String> headers,
			HashMap<String, String> data) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given();

		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));
		this.logAccess.getLogger().debug("headers \n " + headers.toString());
		// add data to the request
		httpRequest.body(data);
		this.logAccess.getLogger().debug("data \n " + data.toString());
		Response response;
		// send request
		if (requestType.toLowerCase().equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.toLowerCase().equalsIgnoreCase("delete")) {
			response = httpRequest.delete(uri);
		} else {
			this.logAccess.getLogger().debug("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");

			throw new IllegalArgumentException("Unexpected value : " + requestType
					+ "\n only supported request types are: GET, POST, PUT, PATCH, DELETE");
		}
		this.logAccess.getLogger().debug(response.body().prettyPrint());
		return response;
	}

	/**
	 * get the value based on the jsonpath<br>
	 * <b>Note:</b><br> Please refer to
	 * 		<a href='https://www.javadoc.io/doc/io.rest-assured/json-path/3.0.0/io/restassured/path/json/JsonPath.html'>JSonPath Reference</a>
	 * 	   for more details on JsonPath
	 * @param response the rest-assured response
	 * @param jsonPath the json-path
	 * @return the value
	 */
	public String getValue(Response response, String jsonPath) {
		String result;
		result = response.jsonPath().getString(jsonPath);
		this.logAccess.getLogger().debug("JSonPath : " + jsonPath + "\n Result : " + result);
		return result;
	}
	
	/**
	 * @param folderPath      folder path to store file name
	 * @param fileName        file name to store the response content
	 * @param responseContent response content to be written in file
	 * @throws Exception the exception
	 */
	public void writeResponseToFile(String folderPath, String fileName, String responseContent) throws Exception {
		this.logAccess.getLogger().debug("Writing response to the file : " + folderPath + File.separatorChar + fileName
				+ "\n Response Content: \n" + responseContent);
		folderFileUtil.writeToTextFile(folderPath, fileName, responseContent);
	}

	/**
	 * @param folderPath folder path to store file name
	 * @param fileName   file name to store the response content
	 * @param response   response object {@link io.restassured.response.Response
	 *                   Response}
	 * @throws Exception the exception
	 */
	public void writeResponseToFile(String folderPath, String fileName, Response response) throws Exception {
		String responseCode = response.body().prettyPrint();
		this.logAccess.getLogger().debug("Writing response to the file : " + folderPath + File.separatorChar + fileName
				+ "\n Response Content: \n" + responseCode);
		folderFileUtil.writeToTextFile(folderPath, fileName, responseCode);
	}
}
