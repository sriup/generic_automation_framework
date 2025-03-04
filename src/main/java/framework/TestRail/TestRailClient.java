package framework.TestRail;

import framework.commonfunctions.ApiMethods;
import framework.logs.LogAccess;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.*;

public class TestRailClient {
    private final String testrailBaseEndPoint;
    private HashMap<String, Integer> testCaseStatusCodes; // Map to store status names and their IDs
    private List<LinkedHashMap<String, Object>> projectIds; // Map to store project names and their IDs>
    private List<LinkedHashMap<String, Object>> testPlanIds;
    private List<LinkedHashMap<String, Object>> testRunIds;
    private final ApiMethods apiMethods;
    private final LogAccess logAccess;
    private final HashMap<String, String> defaultHeaders;


    public TestRailClient(TestRailConfig testRailConfig, LogAccess logAccess) {

        this.testrailBaseEndPoint = testRailConfig.getTestRailUrl() + "/index.php?/api/v2";
        String testRailUsername = testRailConfig.getTestRailUsername();
        String testRailPassword = testRailConfig.getTestRailPassword();

        this.logAccess = logAccess;

        this.apiMethods = new ApiMethods(this.logAccess);

        this.defaultHeaders = new HashMap<>();
        this.defaultHeaders.put("Content-Type", "application/json");

        String authHeader = testRailUsername + ":" + testRailPassword;
        String base64EncodedAuthHeader = Base64.getEncoder().encodeToString(authHeader.getBytes());
        this.defaultHeaders.put("Authorization", "Basic " + base64EncodedAuthHeader);

//
//        // Set up the RestAssured request specification so that the client can make requests to the TestRail API
//        // using the provided credentials and content type header by reusing the same request specification
//        this.requestSpecification = RestAssured.given()
//                .auth().preemptive().basic(testRailUsername, testRailPassword)
//                .header("Content-Type", "application/json");
        // get the project IDs from TestRail
        fetchProjectIds();

        // get the test case status codes
        fetchStatusCodes();
    }


    // <<<<<<<< Test Case Run Status >>>>>>>>


    /**
     * Fetches the status codes from TestRail and stores them in a map.
     * The map is keyed by the status name (in lowercase) with the status ID as the value.
     */
    private void fetchStatusCodes() {

        // Make a request to get all status codes from TestRail
        Response response = apiMethods.sendRequest("get",
                testrailBaseEndPoint + "/get_statuses", this.defaultHeaders);

        // Parse the response body to get the list of status codes
        List<LinkedHashMap<String, Object>> statusCodes = response.getBody().jsonPath().getList("");

        // Initialize the map to store status codes if it's not already initialized
        if (this.testCaseStatusCodes == null) {
            this.testCaseStatusCodes = new HashMap<>();
        }

        // Iterate over the status codes and store them in the map
        for (LinkedHashMap<String, Object> code : statusCodes) {
            // Extract the status code ID and name from the response
            int statusCode = (int) code.get("id");
            String statusName = (String) code.get("label");

            // Store the status code in the map with the status name as the key
            this.testCaseStatusCodes.put(statusName.toLowerCase(), statusCode);
        }
    }


    public HashMap<String, Integer> getTestCaseStatusCodes() {
        return testCaseStatusCodes;
    }

    // <<<<<<<< Project >>>>>>>>

    /**
     * Fetches the project IDs from TestRail.
     * This method makes a GET request to the TestRail API to retrieve the list of project IDs
     * and stores them in the `projectIds` list.
     */
    private void fetchProjectIds() {

        // Construct the URL for the TestRail API endpoint to get projects
        String targetUrl = testrailBaseEndPoint + "/get_projects";
        System.out.println(targetUrl); // Log the target URL for debugging purposes

        // Make a GET request to the TestRail API to fetch projects
        Response response = apiMethods.sendRequest("get", targetUrl, this.defaultHeaders);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to fetch project IDs. Response code: " + response.getStatusCode());
        }
        // Parse the response body to get the list of project IDs
        projectIds = response.getBody().jsonPath().getList("");
    }


    /**
     * Retrieves the list of project IDs.
     *
     * @return a list of project IDs as LinkedHashMap objects.
     */
    public List<LinkedHashMap<String, Object>> getProjectIds() {
        // Return the list of project IDs
        return projectIds;
    }


    // <<<<<<<< Test Plan >>>>>>>>

    /**
     * Fetches the test plan IDs from TestRail.
     * This method makes a GET request to the TestRail API to retrieve the list of test plan IDs
     * and stores them in the `testPlanIds` map with the test plan name as the key.
     *
     * @param projectId the ID of the project to fetch test plan IDs for.
     */
    public void fetchTestPlanIds(int projectId) {

        Response response = apiMethods.sendRequest("get", testrailBaseEndPoint +
                "/get_plans/" + projectId + "&is_completed=0", this.defaultHeaders);

        testPlanIds = response.getBody().jsonPath().getList("");

    }


    /**
     * Retrieves the list of test plan IDs.
     *
     * @return a list of test plan IDs as LinkedHashMap objects.
     */
    public List<LinkedHashMap<String, Object>> getTestPlanIds() {
        // Return the list of test plan IDs
        return testPlanIds;
    }

    /**
     * Gets the ID of a test plan by its name.
     * This method takes a test plan name as input and returns its ID.
     * The test plan ID is retrieved from the `testPlanIds` map.
     *
     * @param planName the name of the test plan to retrieve the ID for.
     * @return the ID of the test plan as an integer.
     */
    public int getTestPlanId(String planName) {
        // Retrieve the test plan ID from the testPlanIds map
        return this.testPlanIds.stream()
                .filter(plan -> plan.get("name").equals(planName))
                .findFirst()
                .map(plan -> (int) plan.get("id"))
                .orElseThrow(() -> new RuntimeException("Test plan not found"));
    }

    // <<<<<<<< Test Runs >>>>>>>>
    /**
     * Fetches the test run IDs from TestRail for a given test plan ID.
     * This method makes a GET request to the TestRail API to retrieve the list of test run IDs
     * and stores them in the `testRunIds` map with the test run name as the key.
     *
     * @param testPlanId the ID of the test plan to fetch test run IDs for.
     */
    public void fetchTestRunIds(int testPlanId) {

        // Fetch the test run IDs for the given test plan ID
        Response response = apiMethods.sendRequest("get",
                testrailBaseEndPoint + "/get_runs/" + testPlanId, this.defaultHeaders);

        // Store the test run IDs in the testRunIds map
        testRunIds = response.getBody().jsonPath().getList("");
    }

    /**
     * Retrieves the list of test run IDs.
     *
     * @return a list of test run IDs as LinkedHashMap objects.
     */
    public List<LinkedHashMap<String, Object>> getTestRunIds() {
        // Return the list of test run IDs
        return testRunIds;
    }

    public int getTestRunId(String runName) {
        return (int) this.testRunIds.stream()
                .filter(run -> run.get("name").equals(runName))
                .findFirst()
                .map(run -> (int) run.get("id"))
                .orElseThrow(() -> new RuntimeException("Test run not found"));
    }

    // <<<<<<<<< Test Cases >>>>>>>>
    public void fetchTestCaseIds(int testRunId) {

       Response response = apiMethods.sendRequest("get",
               testrailBaseEndPoint + "/get_tests/" + testRunId, this.defaultHeaders);

       for(Object item : response.getBody().jsonPath().getList("")){

            // test case details
            JSONObject testCaseDetailsObj = (JSONObject) item;

            // test case id
            int testCaseId = (int) testCaseDetailsObj.get("id");
            String testCaseName = testCaseDetailsObj.get("name").toString();

            
        }

    }

    @SuppressWarnings("unchecked")
    // <<<<<<<<< Update Test Status >>>>>>>>
    public Response updateTestStatuses(JSONObject requestBody, int testRunId) {

        String requestUrl = testrailBaseEndPoint + "/add_results_for_cases/" + testRunId;

        this.logAccess.getLogger().info("Request body: {}",requestBody);

        Response response = apiMethods.sendRequest("post", requestUrl, this.defaultHeaders, requestBody);
        if(response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to update test statuses. Response code: " + response.getStatusCode());
        }else {
            System.out.println("Test statuses updated successfully.");
        }
        return response;

    }
}
