package framework.utilities;

import framework.commonfunctions.ApiMethods;
import framework.enums.ContentTypesEnums;
import framework.logs.LogAccess;
import io.restassured.response.Response;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class BitBucketUtil {

    /* the log access. */
    private final LogAccess logAccess;

    // the workspace
    protected String workspace;

    // the encoded credentials
    protected String encodedCredentials;

    // the slug (also known as repository name)
    protected String slug;

    public BitBucketUtil(LogAccess logAccess, String workspace, String encodedCredentials, String repoName) {
        this.workspace = workspace;

        this.encodedCredentials = encodedCredentials;

        this.slug = repoName;

        this.logAccess = logAccess;

    }

    public BitBucketUtil(LogAccess logAccess, String workspace, String userName, String password, String repoName) throws Exception {
        this.workspace = workspace;

        this.encodedCredentials = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes("UTF-8"));

        this.slug = repoName;

        this.logAccess = logAccess;

    }

    public void createBranch(String sourceBranch, String newBranchName) throws Exception {

        // Bitbucket API URL
        String uri = "https://api.bitbucket.org/2.0/repositories/" + this.workspace + "/" + this.slug + "/src/";

        ApiMethods apiMethods = new ApiMethods(this.logAccess);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);

        HashMap<String, String> data = new HashMap<>();
        data.put("parents",sourceBranch);
        data.put("branch", newBranchName);

        Response response = apiMethods.sendRequest("post", uri, headers, data);

        // check if the request is successful
        this.logAccess.getLogger().debug(newBranchName + "creation is " +
                (response.getStatusCode() == 201 ? "successful.\n" : "failed.\n") +
                "Response code:" + response.getStatusCode());
    }

    public void commitChanges(String branchName, String fileName) throws Exception {
        // Bitbucket API URL
        URL endPointURL = new URL("https://api.bitbucket.org/2.0/repositories/" + this.workspace + "/" + this.slug + "/src/");

        // create the connection object
        HttpURLConnection connection = (HttpURLConnection) endPointURL.openConnection();

        // add authentication
        connection.addRequestProperty("Authorization", "Basic " + encodedCredentials);

        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        // Setting the form data in the post request
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

        // adding new/modified file
        out.write("branch=" + branchName + "&" + "/" + fileName + "=@" + fileName);

        out.close();


        connection.connect();

        // check if the request is successful
        this.logAccess.getLogger().debug("\"" + fileName + "\"" +
                (connection.getResponseCode() == 201 ? "committed successful.\n" : "commit is failed.\n") +
                connection.getResponseCode() + " - " + connection.getResponseMessage());

    }

    public String createPullRequest(String sourceBranch, String targetBranch, String prComment) throws Exception {

        // create the payload string
        String payLoad = String.format("{\"title\":\"%s\",\"source\":{\"branch\":{\"name\":\"%s\"}},\"destination\":{\"branch\":{\"name\":\"%s\"}}}", prComment, sourceBranch, targetBranch);
        this.logAccess.getLogger().info(payLoad);

        // convert the payload to byte format
        byte[] payLoadBytes = payLoad.getBytes(StandardCharsets.UTF_8);

        // get the payload Content-Length
        int payLoadContentLength = payLoadBytes.length;

        ApiMethods apiMethods = new ApiMethods(this.logAccess);
        String uri = "https://api.bitbucket.org/2.0/repositories/" + this.workspace + "/" + this.slug + "/pullrequests/";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);
        headers.put("Content-Type", "application/json");
        // headers.put("Content-Length", Integer.toString(payLoadContentLength));

        HashMap<String, String> data = new HashMap<>();
        data.put("\"title\"",prComment);
        data.put("\"source\"", "{\"branch\":{\"name\":\"" + sourceBranch + "\"}}");
        data.put("\"destination\"", "{\"branch\":{\"name\":\"" + targetBranch + "\"}}");

        Response response = apiMethods.sendRequest("post", uri, headers,data);
        String prNumber = apiMethods.getValue(response, "values.id");

        // check if the request is successful
        this.logAccess.getLogger().debug(sourceBranch + " => " + targetBranch + " Pull Request (PR) " +
                (response.getStatusCode() == 201 ? "created successful.\n" : "creation is failed.\n") +
                "Response code:" + response.getStatusCode());

        return prNumber.replace("[","").replace("]","");


    }

    public void actionOnPR(int prNumber, String action, String message) throws Exception{

        // Bitbucket API URL
        String uri = "https://api.bitbucket.org/2.0/repositories/" + this.workspace + "/" + this.slug + "/pullrequests/" + prNumber + "/" + action;

        ApiMethods apiMethods = new ApiMethods(this.logAccess);
        HashMap <String, String> headers = new HashMap<>();
        HashMap <String, String> data = new HashMap<>();
        data.put("message",message);
        Response response = apiMethods.sendRequest("post", uri, headers, ContentTypesEnums.APPLICATION_JSON,data);

        // check if the request is successful
        this.logAccess.getLogger().debug("Pull Request#" + prNumber + " - " + action  + (response.getStatusCode() == 201 ? " successful.\n" : " failed.\n") +
                "Response code:" + response.getStatusCode());
    }

}
