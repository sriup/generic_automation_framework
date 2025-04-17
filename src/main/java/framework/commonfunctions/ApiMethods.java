package framework.commonfunctions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import framework.enums.ContentTypesEnums;
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
		defaultHeadersMap = new HashMap<>();
		defaultHeadersMap.put("Content-Type", "application/json");
		defaultHeadersMap.put("Accept", "application/json");

	}

	/**
	 * makes Get request to the rest API
	 *
	 * @param requestType request type
	 * @param uri		 uri/endpoint
	 * @return the response object {@link io.restassured.response.Response Response}
	 *
	 */
	public Response sendRequest(String requestType, String uri) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// request header
		defaultHeadersMap.forEach((k, v) -> httpRequest.header(k, v));
		Response response;
		// send request
		if (requestType.equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.equalsIgnoreCase("delete")) {
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
	 * @param uri		 uri/endpoint
	 * @param headers	 headers
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendRequest(String requestType, String uri, Map<String, String> headers) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));

		for (Map.Entry<String, String> header : headers.entrySet()) {
			// print the header if it's not authorization related.
			if(!header.getKey().equalsIgnoreCase("authorization")){
				this.logAccess.getLogger().debug("headers \n " + headers);
			}
		}

		Response response;
		// send request
		if (requestType.equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.equalsIgnoreCase("delete")) {
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
	 * @param uri		 uri/endpoint
	 * @param headers	 headers
	 * @param data		data
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendRequest(String requestType, String uri, HashMap<String, String> headers,
								HashMap<String, Object> data) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();

		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));
		this.logAccess.getLogger().debug("headers \n " + headers);
		// add data to the request
		httpRequest.body(data);
		this.logAccess.getLogger().debug("data \n " + data.toString());
		Response response;
		// send request
		if (requestType.equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.equalsIgnoreCase("delete")) {
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
	 * @param requestType the request type
	 * @param uri		 the uri/endpoint
	 * @param headers	 the headers
	 * @param bodyPayload payload to be sent as part of request body
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendRequest(String requestType, String uri, HashMap<String, String> headers, ObjectNode bodyPayload) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();


		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));
		this.logAccess.getLogger().debug("headers \n " + headers);
		httpRequest.body(bodyPayload);

		this.logAccess.getLogger().debug("data \n " + bodyPayload.toString());
		Response response;
		// send request
		if (requestType.equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.equalsIgnoreCase("delete")) {
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
	 * @param requestType the request type
	 * @param uri		 the uri/endpoint
	 * @param headers	 the headers
	 * @param data		the body data <br>
	 *					<b>Note:</b> Use {@link #sendRequest(String, String, HashMap, ObjectNode)} in the case of
	 *					multipart body data
	 * @return response object {@link io.restassured.response.Response Response}
	 */
	public Response sendRequest(String requestType, String uri, Map<String, String> headers, ContentTypesEnums contentType,
								Object data) {
		this.logAccess.getLogger().debug("Sending " + requestType + " request to " + uri);

		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();


		// add all headers to the request
		headers.forEach((k, v) -> httpRequest.header(k, v));
		this.logAccess.getLogger().debug("headers \n " + headers);
		if(contentType.toString().equalsIgnoreCase("MULTIPART_FORMDATA")){
			httpRequest.header("content-type","multipart/form-data");
			Map<String, Object> multipartData = (Map<String, Object>) data;
			multipartData.forEach((k, v) -> httpRequest.multiPart(k, v));
		}else if(contentType.toString().equalsIgnoreCase("APPLICATION_JSON")){
			httpRequest.body(data);
		}

		this.logAccess.getLogger().debug("data \n " + data.toString());
		Response response;
		// send request
		if (requestType.equalsIgnoreCase("get")) {
			response = httpRequest.get(uri);
		} else if (requestType.equalsIgnoreCase("post")) {
			response = httpRequest.post(uri);
		} else if (requestType.equalsIgnoreCase("put")) {
			response = httpRequest.put(uri);
		} else if (requestType.equalsIgnoreCase("patch")) {
			response = httpRequest.patch(uri);
		} else if (requestType.equalsIgnoreCase("delete")) {
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
	 * 		'<a href="https://www.javadoc.io/doc/io.rest-assured/json-path/3.0.0/io/restassured/path/json/JsonPath.html">JSonPath Reference</a>'
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
	 * @param folderPath	  folder path to store file name
	 * @param fileName		file name to store the response content
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
	 *				   Response}
	 * @throws Exception the exception
	 */
	public void writeResponseToFile(String folderPath, String fileName, Response response) throws Exception {
		String responseCode = response.body().prettyPrint();
		this.logAccess.getLogger().debug("Writing response to the file : " + folderPath + File.separatorChar + fileName
				+ "\n Response Content: \n" + responseCode);
		folderFileUtil.writeToTextFile(folderPath, fileName, responseCode);
	}
}
