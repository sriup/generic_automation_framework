package framework.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import framework.commonfunctions.ApiMethods;
import framework.enums.ContentTypesEnums;
import framework.logs.LogAccess;
import io.restassured.response.Response;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class BitBucketUtil {

    /* bitbucket api */
    private String bitBucketAPIBaseURL =  "https://api.bitbucket.org/2.0/repositories/";

    /* the log access. */
    private LogAccess logAccess;

    // the workspace
    protected String workspace;

    // the encoded credentials
    protected String encodedCredentials;

    // the slug (also known as repository name)
    protected String slug;

    // pr number
    private String prNumber;

    /**
     * Constructor to use bitbucket token
     * @param logAccess the {@link LogAccess} instance
     * @param workspace the workspace
     * @param encodedCredentials the encoded BitBucket Credential token
     * @param repoName the repository name
     */
    public BitBucketUtil(LogAccess logAccess, String workspace, String encodedCredentials, String repoName) {
        this.workspace = workspace;

        this.encodedCredentials = encodedCredentials;

        this.slug = repoName;

        this.logAccess = logAccess;

    }

    /**
     * Constructor to use bitbucket username and password
     * @param logAccess the {@link LogAccess} instance
     * @param workspace the workspace
     * @param userName the Bitbucket user name (use complete email address)
     * @param password the password
     * @param repoName the repository name
     */
    public BitBucketUtil(LogAccess logAccess, String workspace, String userName, String password, String repoName) {
        this.workspace = workspace;

        this.encodedCredentials = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes(StandardCharsets.UTF_8));

        this.slug = repoName;

        this.logAccess = logAccess;

    }

    /**
     * create branch from the source branch
     * <br><b>Note:</b>
     *  This method will create a fork from the source branch no changes will be committed as part of this method.
     * @param sourceBranch the source branch name
     * @param newBranchName the new branch name
     */
    public void createBranch(String sourceBranch, String newBranchName){

        // Bitbucket API URL
        String uri = bitBucketAPIBaseURL + this.workspace + "/" + this.slug + "/src/";

        // create api methods instance
        ApiMethods apiMethods = new ApiMethods(this.logAccess);

        // create headers map
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);

        // create request multipart form data
        HashMap<String, Object> data = new HashMap<>();
        data.put("parents",sourceBranch);
        data.put("branch", newBranchName);

        // send request and capture the response
        Response response = apiMethods.sendRequest("post", uri, headers,ContentTypesEnums.MULTIPART_FORMDATA, data);

        // check if the request is successful
        this.logAccess.getLogger().debug(newBranchName + "creation is " +
                (response.getStatusCode() == 201 ? "successful.\n" : "failed.\n") +
                "Response code:" + response.getStatusCode());
    }

    /**
     * commits the specified file into the branch
     * @param branchName the branch to which the changes should be committed.
     * @param branchFilePath the path of the file in the repo
     * @param absFilePath the absolute file path of the local file that should be committed.
     */
    public void commitChanges(String branchName, String branchFilePath, String absFilePath) {
        // Bitbucket API URL
        String uri = bitBucketAPIBaseURL + this.workspace + "/" + this.slug + "/src/";

        ApiMethods apiMethods = new ApiMethods(this.logAccess);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);


        HashMap<String, Object> data = new HashMap<>();
        data.put("branch", branchName);
        data.put(branchFilePath, new File(absFilePath));

        Response response = apiMethods.sendRequest("post", uri, headers,ContentTypesEnums.MULTIPART_FORMDATA, data);

        // check if the request is successful
        this.logAccess.getLogger().debug("Commit changes to " + branchName + " is " +
                (response.getStatusCode() == 201 ? "successful.\n" : "failed.\n") +
                "Response code:" + response.getStatusCode());

    }

    /**
     * creates pull request
     * @param sourceBranch the source branch name
     * @param targetBranch the target branch name
     * @return the pull request number
     */

    public String createPullRequest(String sourceBranch, String targetBranch){

        ApiMethods apiMethods = new ApiMethods(this.logAccess);
        String uri = bitBucketAPIBaseURL + this.workspace + "/" + this.slug + "/pullrequests/";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);
        headers.put("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode payload = mapper.createObjectNode();
        payload.put("title", "bot created request");


        ObjectNode sourceNode = mapper.createObjectNode();
        sourceNode.set("branch", mapper.createObjectNode().put("name", sourceBranch));

        ObjectNode targetNode = mapper.createObjectNode();
        targetNode.set("branch", mapper.createObjectNode().put("name", targetBranch));

        payload.set("source", sourceNode);
        payload.set("destination", targetNode);

        Response response = apiMethods.sendRequest("post", uri, headers, payload);

        // check if the request is successful
        this.logAccess.getLogger().debug(sourceBranch + " => " + targetBranch + " Pull Request (PR) " +
                (response.getStatusCode() == 201 ? "created successful.\n" : "creation is failed.\n") +
                "Response code:" + response.getStatusCode());
        // get the pull request id from the response
        this.prNumber =  apiMethods.getValue(response, "id");

        return this.prNumber;
    }

    /**
     * approve and merges the latest pr created using {@link #createPullRequest}
     */

    public void approveAndMergePR(){
        actionOnPR("approve");
        actionOnPR("merge");
    }

    /**
     * performs the defined action on the latest Pull request.
     * @param action the action to perform on the PR<br>
     *    <b>Note:</b>The available actions are<br>
     *      <ul>
     *              <li>approve</li>
     *               <li>decline</li>
     *               <li>merge</li>
     *      </ul>
     */
    private void actionOnPR(String action) {

        // Bitbucket API URL
        String uri = bitBucketAPIBaseURL + this.workspace + "/" + this.slug + "/pullrequests/" + this.prNumber + "/" + action;

        ApiMethods apiMethods = new ApiMethods(this.logAccess);
        HashMap <String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);
        headers.put("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode payload = mapper.createObjectNode();
        payload.put("message", action + "d by bot.");

        Response response = apiMethods.sendRequest("post", uri, headers, payload);

        // check if the request is successful
        this.logAccess.getLogger().debug("Pull Request#" + this.prNumber + " - " + action  + (response.getStatusCode() == 200 ? " successful.\n" : " failed.\n") +
                "Response code:" + response.getStatusCode());
    }

}
