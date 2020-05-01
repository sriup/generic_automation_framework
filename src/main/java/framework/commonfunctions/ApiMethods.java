package framework.commonfunctions;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiMethods {

	public String getChromeDriverVersion() {
		String uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
		Response response = get(uri);
		// response code
		System.out.println("RESPONSE CODE: " + response.getStatusCode());
		return response.getBody().asString();
	}
	public String getGeckoDriverVersion() {
		String uri = "https://github.com/mozilla/geckodriver/releases/latest";
		Response response = get(uri);
		// response code
		System.out.println("RESPONSE CODE: " + response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		String versionNumber = jsonPathEvaluator.getString("tag_name");
		return versionNumber;
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
