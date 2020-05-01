package framework.commonfunctions;

import framework.logs.LogAccess;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiMethods {

	private LogAccess logAccess;

	public ApiMethods(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	public Response get(String uri) {

		RequestSpecification httpRequest = RestAssured.given();
		// request header
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Accept", "application/json");
		// get request
		return httpRequest.get(uri);
	}

}
