package framework.TestRail;

import framework.commonfunctions.ApiMethods;
import framework.enums.ContentTypesEnums;
import framework.logs.LogAccess;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

public class TestRailClient {
    private final String testrailBaseEndPoint;
    private final HashMap<String, Integer> testCaseStatusCodes; // Map to store status names and their IDs
    private List<LinkedHashMap<String, Object>> projectIds; // Map to store project names and their IDs>

    private final ApiMethods apiMethods;
    private final LogAccess logAccess;
    private final HashMap<String, String> defaultHeaders;


    /**
     * Constructs a new TestRailClient instance with the provided TestRailConfig and LogAccess.
     *
     * @param testRailConfig The TestRail configuration to use.
     * @param logAccess The LogAccess instance to use for logging.
     */
    public TestRailClient(TestRailConfig testRailConfig, LogAccess logAccess) {

        // Set the log access instance
        this.logAccess = logAccess;

        // Create a new ApiMethods instance with the log access instance
        this.apiMethods = new ApiMethods(this.logAccess);

        // Update the TestRail base endpoint URL
        // Append "/index.php?/api/v2" to the TestRail URL to form the base endpoint
        this.testrailBaseEndPoint = testRailConfig.getTestRailUrl() + "/index.php?/api/v2";

        // Initialize the default headers map
        this.defaultHeaders = new HashMap<>();

        // Generate the authentication header
        // Combine the username and password with a colon in between
        String testRailUsername = testRailConfig.getTestRailUsername();
        String testRailPassword = testRailConfig.getTestRailPassword();
        String authHeader = testRailUsername + ":" + testRailPassword;

        // Base64 encode the authentication header
        String base64EncodedAuthHeader = Base64.getEncoder().encodeToString(authHeader.getBytes());

        // Add the authentication header to the default headers map
        // Use the "Basic" authentication scheme
        this.defaultHeaders.put("Authorization", "Basic " + base64EncodedAuthHeader);

        // Add the content type header to the default headers map
        // Set the content type to "application/json"
        this.defaultHeaders.put("Content-Type", "application/json");

        // Fetch the project IDs from TestRail
        fetchProjects();

        // Check if the project ID is valid
        // If the project ID is not 0, check if it exists in the project IDs list
        boolean isProjectIdValid = false;
        if (testRailConfig.getTestRailProjectId() != 0) {
            // Iterate over the project IDs list
            for (LinkedHashMap<String, Object> project : this.projectIds) {
                // Check if the project ID matches the configured project ID
                if (project.get("id").equals(testRailConfig.getTestRailProjectId())) {
                    // Log a message indicating that the project ID is valid
                    this.logAccess.getLogger().info("Project ID {} is valid", testRailConfig.getTestRailProjectId());
                    isProjectIdValid = true;
                    break;
                }
            }
        }

        // Throw an exception if the project ID is not valid
        if (!isProjectIdValid) {
            // Log a message indicating that the project ID is not valid
            this.logAccess.getLogger().info("Project ID {} is not valid", testRailConfig.getTestRailProjectId());
            throw new RuntimeException("Project ID is not valid");
        }


        // initialize the test status codes map
        this.testCaseStatusCodes = new HashMap<>();

        // Fetch the test case status codes from TestRail
        fetchStatusCodes();

    }



    /**
     * Fetches the status codes from TestRail and stores them in a map.
     * The map is keyed by the status name (in lowercase) with the status ID as the value.
     * This method is invoked during the construction of the TestRail client.
     * If an exception occurs during the API request or response parsing, it is caught and logged,
     * and a RuntimeException is thrown.
     */
    private void fetchStatusCodes() {
        try {
            // Construct the URL for the TestRail API endpoint to get all status codes
            String getStatusCodesUrl = testrailBaseEndPoint + TestRailApiEndPoints.GET_STATUSES;

            // Make a GET request to the TestRail API to retrieve the list of status codes
            Response response = apiMethods.sendRequest("get", getStatusCodesUrl, this.defaultHeaders);

            // Check if the response was successful
            if (response.getStatusCode() != 200) {

                throw new RuntimeException("Failed to fetch status codes. Response code: " + response.getStatusCode());
            }

            // Parse the response body to get the list of status codes
            List<LinkedHashMap<String, Object>> statusCodes = response.getBody().jsonPath().getList("");

            // Store the status codes in the map
            storeStatusCodes(statusCodes);

        } catch (Exception e) {
            // Log an error if an exception occurs during the API request or response parsing
            this.logAccess.getLogger().error("Error fetching status codes", e);
            throw new RuntimeException("Error fetching status codes", e);
        }
    }

    /**
     * Stores the status codes in the map.
     * The map is keyed by the status name (in lowercase) with the status ID as the value.
     * This method is invoked during the construction of the TestRail client.
     * @param statusCodes the list of status codes from TestRail
     */
    private void storeStatusCodes(List<LinkedHashMap<String, Object>> statusCodes) {
        // Iterate over the status codes and store them in the map
        for (LinkedHashMap<String, Object> code : statusCodes) {
            // Extract the status code ID and name from the response
            int statusCode = (int) code.get("id");
            String statusName = (String) code.get("label");

            // Check if the status code name is not null or empty
            if (statusName != null && !statusName.isEmpty()) {
                // Store the status code in the map with the status name as the key
                this.testCaseStatusCodes.put(statusName.toLowerCase(), statusCode);
            } else {
                // Log a warning if the status code name is null or empty
                this.logAccess.getLogger().warn("Status code name is null or empty for ID: {}", statusCode);
            }
        }
    }


    /**
     * Returns the map of test case status codes.
     * The map is keyed by the status name (in lowercase) with the status ID as the value.
     * For example, the status code ID for "Passed" is 1, so the map would contain the entry
     * "passed" -> 1.
     * @return the map of test case status codes
     */
    public HashMap<String, Integer> getTestCaseStatusCodes() {
        return testCaseStatusCodes;
    }

    // <<<<<<<< Project >>>>>>>>

    /**
     * Fetches the project IDs from TestRail.
     * This method makes a GET request to the TestRail API to retrieve the list of project IDs
     * and stores them in the `projectIds` list.
     * <p>
     * The TestRail API endpoint for fetching projects is GET /get_projects.
     * The response is a JSON object containing an array of project details.
     * The project details include the project ID, name, and suite ID.
     */
    private void fetchProjects() {
        try {
            // Construct the URL for the TestRail API endpoint to get projects
            String requestUrl = testrailBaseEndPoint + TestRailApiEndPoints.GET_PROJECTS;

            // Make a GET request to the TestRail API to fetch projects
            Response response = apiMethods.sendRequest("get", requestUrl, this.defaultHeaders);

            // Check if the response was successful
            if (response.getStatusCode() != 200) {
                throw new RuntimeException("Failed to fetch project IDs. Response code: " + response.getStatusCode());
            }

            // Parse the response body to get the list of project IDs
            // The response body is a JSON object containing an array of project details
            // The project IDs are stored in the `projectIds` list
            this.projectIds = response.getBody().jsonPath().getList("");
        } catch (Exception e) {
            // Log an error if an exception occurs during the API request or response parsing
            this.logAccess.getLogger().error("Error fetching project IDs", e);
            throw new RuntimeException("Error fetching project IDs", e);
        }

    }

    // <<<<<<<< Test Plan >>>>>>>>

    /**
     * Fetches the active test plan IDs from TestRail for a given project.
     * This method makes a GET request to the TestRail API to retrieve the list of test plan IDs
     * for the specified project and stores them in the `testPlanIds` map with the test plan name
     * as the key.
     * <p>
     * The TestRail API endpoint for fetching test plans is GET /get_plans/{project_id}&is_completed=0.
     * The response is a JSON object containing an array of test plan details.
     *
     * @param projectId the ID of the project for which to fetch test plan IDs.
     * @return a list of maps where each map contains the active test plan details
     */
    public List<LinkedHashMap<String, Object>> fetchTestPlans(int projectId) {
        try {
            // Construct the URL for the TestRail API endpoint to get test plans
            String targetUrl = testrailBaseEndPoint +
                    TestRailApiEndPoints.GET_OPEN_PLANS.replace("{project_id}", String.valueOf(projectId));

            // Make a GET request to the TestRail API to fetch test plans
            Response response = apiMethods.sendRequest("get", targetUrl, this.defaultHeaders);

            // Check if the response was successful
            if (response.getStatusCode() != 200) {
                throw new RuntimeException("Failed to fetch test plans. Response code: " + response.getStatusCode());
            }

            // Parse the response body to get the list of test plans
            return response.getBody().jsonPath().getList("");
        } catch (Exception e) {
            // Log an error if an exception occurs during the API request or response parsing
            this.logAccess.getLogger().error("Error fetching test plans", e);
            throw new RuntimeException("Error fetching test plans", e);
        }
    }


    // <<<<<<<<< Test Cases >>>>>>>>
    /**
     * Fetches the test cases for a given test run ID and stores them in a map.
     * The map contains the test case ID as the key and a map of the test case details as the value.
     * The test case details include the test case ID, name, and ID.
     *
     * @param testRunId the ID of the test run for which to fetch the test cases
     * @return the map of test case IDs to test case details
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, HashMap<String, Object>> fetchTestCases(int testRunId) {

        // Construct the URL for the TestRail API endpoint to get test cases
        String requestUrl = testrailBaseEndPoint +
                TestRailApiEndPoints.GET_TESTS_BY_TEST_RUN_ID.replace("{test_run_id}", String.valueOf(testRunId));

        // Make a GET request to the TestRail API to fetch test cases
        Response response = apiMethods.sendRequest("get",
                requestUrl, this.defaultHeaders);

        // Create a map to store the test case IDs and details
        HashMap<Integer, HashMap<String, Object>> testCaseIds = new HashMap<>();

        // Get the list of test cases from the response body
        List<Object> testCasesDetails = response.getBody().jsonPath().getList("");

        // Iterate over the list of test cases
        for (Object item : testCasesDetails) {

            // Get the test case details
            LinkedHashMap<String, Object> testCaseDetailsObj = (LinkedHashMap<String, Object>) item;

            // Get the test case ID, name, and ID
            int testCaseId = (int) testCaseDetailsObj.get("case_id");
            String testCaseName = testCaseDetailsObj.get("title").toString();
            int testId = (int) testCaseDetailsObj.get("id");

            // Store the test case details in a map
            HashMap<String, Object> testCaseDetails = new HashMap<>();
            testCaseDetails.put("test_id", testId);
            testCaseDetails.put("title", testCaseName);
            testCaseDetails.put("case_id", testCaseId);

            // Add the test case ID and details to the map
            testCaseIds.put(testCaseId, testCaseDetails);
        }

        // Return the map of test case IDs to test case details
        return testCaseIds;
    }

    @SuppressWarnings("unchecked")
    // <<<<<<<<< Update Test Status >>>>>>>>
    public Response updateTestStatuses(JSONObject requestBody, int testRunId) {

        String requestUrl = testrailBaseEndPoint +
                TestRailApiEndPoints.POST_ADD_RESULTS_FOR_CASES_BY_TEST_RUN_ID.replace("{test_run_id}",
                        String.valueOf(testRunId));

        this.logAccess.getLogger().info("Request body: {}",requestBody);

        Response response = apiMethods.sendRequest("post", requestUrl, this.defaultHeaders, requestBody);
        if(response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to update test statuses. Response code: " + response.getStatusCode());
        }else {
            System.out.println("Test statuses updated successfully.");
        }
        return response;

    }

    public Response fetchTestPlan(Integer testPlanId) {

        String requestUrl = testrailBaseEndPoint +
                TestRailApiEndPoints.GET_PLAN.replace("{plan_id}", String.valueOf(testPlanId));

        Response response = apiMethods.sendRequest("get", requestUrl, this.defaultHeaders);

        if(response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to fetch test plan. Response code: " + response.asString());
        }else{
            System.out.println("Test plan fetched successfully for test plan id: " + testPlanId + ".");
        }

        return response;

    }

    /**
     * Attaches evidence to a test result using the TestRail API.
     * This method takes a test result ID and a map of file names to file paths,
     * and attaches each file as evidence to the specified test result in TestRail.
     *
     * @param resultId the ID of the test result to which to attach the evidence
     * @param evidenceFilesMap a map of file names to file paths
     * @throws IllegalArgumentException if the evidence files map is empty or null
     * @throws RuntimeException if the attachment fails
     */
    public void attachEvidenceToTestResult(int resultId, Map<String, String> evidenceFilesMap, HashMap<String, Object> testCaseDetails) {
        // Validate the evidence files map
        if (evidenceFilesMap == null || evidenceFilesMap.isEmpty()) {
            throw new IllegalArgumentException("Evidence files map is empty or null");
        }

        // Construct the URL for attaching evidence to the test result
        String requestUrl = testrailBaseEndPoint +
                TestRailApiEndPoints.POST_ATTACHMENTS_TO_RESULT_BY_RESULT_ID.replace("{result_id}", String.valueOf(resultId));

        // Iterate over each entry in the evidence files map
        for (Map.Entry<String, String> entry : evidenceFilesMap.entrySet()) {
            // Prepare the multipart data for the file attachment
            Map<String, Object> multipartData = new HashMap<>();
            multipartData.put("attachment", new File(entry.getValue()));

            // Send the POST request to attach the file
            Response response = apiMethods.sendRequest(
                    "post", requestUrl, this.defaultHeaders, ContentTypesEnums.MULTIPART_FORMDATA, multipartData);

            // Check if the response was unsuccessful
            if (response.getStatusCode() != 200) {
                // Log the error and throw a runtime exception
                this.logAccess.getLogger().error("Failed to attach evidence to test result to test case: {}. Response: {}", testCaseDetails.get("case_id"), response.asString());
                throw new RuntimeException("Failed to attach evidence to test result for test case: " + testCaseDetails.get("case_id") + ". Response code: " + response.asString());
            }
        }
    }
}
