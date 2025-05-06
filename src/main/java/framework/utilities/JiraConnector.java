package framework.utilities;

import framework.commonfunctions.ApiMethods;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class JiraConnector {

    /**
     * Get all defects from the execution sheet
     **/
    private static final HashMap<String, String> defectsInfoMap = new HashMap<>();
    /**
     * JiraConnector instance
     */
    private static JiraConnector jiraConnectorInstance = null;
    private static String jiraAppUrl;
    private static String jiraAppBearerAuthToken;

    private static boolean isSuccessfullyLogged;

    private static boolean isIssuesStatusTriggered;

    /**
     * Initializing JiraConnector instance <br>
     * <p>
     * Note: This method will get the instance of singleton JiraConnector class.
     * The instance will be created dynamically if it doesn't exist, otherwise uses
     * the existing instance
     *
     * @param csvUtil             the {@link CsvUtil}
     * @param filePath           the full file path where the defects' information is present
     * @param columnName         the column name where the defects' information is present
     * @param jiraUrl            the Jira URL
     * @param jiraBearerAuthToken the Jira Bearer Authentication Token
     * @return instance of this JiraConnector class
     * @throws Exception the exception
     */
    public static JiraConnector getInstance(CsvUtil csvUtil, String filePath, String columnName, String jiraUrl, String jiraBearerAuthToken) throws Exception {
        if (jiraConnectorInstance == null) {
            // create JiraConnector class instance
            jiraConnectorInstance = new JiraConnector();

            // set jira url
            jiraAppUrl = jiraUrl;

            try{
                SecurityUtil securityUtil = new SecurityUtil();
                String decryptedJiraAuthToken = securityUtil.decrypt(jiraBearerAuthToken);
                jiraBearerAuthToken = decryptedJiraAuthToken != null ? decryptedJiraAuthToken: jiraBearerAuthToken;
            }catch (Exception exception){
                System.out.println("Suggestion: The JIRA_TOKEN is not encrypted, it's recommended to use Encrypted tokens.");
            }
            // set jira application basic authentication token
            jiraAppBearerAuthToken = jiraBearerAuthToken.replace("Bearer ", "");


            // Fetching all the values after initializing the singleton class
            fetchDefectsInfo(csvUtil, filePath, columnName);
        }
        return jiraConnectorInstance;
    }



    /**
     * Initializing JiraConnector instance <br>
     * <p>
     * Note: This method will get the instance of singleton JiraConnector class.
     * The instance will be created dynamically if it doesn't exist, otherwise uses
     * the existing instance
     *
     * @param jiraUrl            the Jira URL
     * @param jiraBearerAuthToken the Jira Bearer Authentication Token
     * @return instance of this JiraConnector class
     */
    public static JiraConnector getInstance(String jiraUrl, String jiraBearerAuthToken) {
        if (jiraConnectorInstance == null) {
            // create JiraConnector class instance
            jiraConnectorInstance = new JiraConnector();

            jiraAppUrl = jiraUrl;
            jiraAppBearerAuthToken = jiraBearerAuthToken.replace("Basic ", "");
        }
        return jiraConnectorInstance;
    }

    /**
     * Empty constructor
     * @return the instance of {@link JiraConnector}
     */
    public static JiraConnector getInstance() throws InstantiationException {
        if (jiraConnectorInstance != null) {
            return jiraConnectorInstance;
        }else {
            throw new InstantiationException("Instantiate the class with getInstance() method by passing the required parameters.");
        }
    }

    /**
     * fetches the defects information based on the defects in the specified column
     * @param csvUtil the {@link CsvUtil} instance
     * @param filePath  the file path
     * @param columnName the column name
     * @throws IOException the IO Exception
     */
    private static void fetchDefectsInfo(CsvUtil csvUtil, String filePath, String columnName) throws Exception {

        Set<String> defectIdsSet = new LinkedHashSet<>();

        csvUtil.openCsvReader(filePath, ',');

        for (String[] currentRow : csvUtil.getAllRows()) {
            String currentCellValue = csvUtil.getCellData(currentRow, columnName).trim();
            if (currentCellValue.equalsIgnoreCase(columnName) || currentCellValue.isEmpty()) {
                continue;
            }
            // get the defect value from defect column and iterate
            for(String defect : currentCellValue.split(",")){
                // add defect to defectIdsSet
                if(!defect.contains("ALM") && !defect.trim().equals("")) {
                    defectIdsSet.add(defect.trim());
                }
            }

        }

        // build the jira defects string
        String defectIds = (defectIdsSet.isEmpty()) ? "" : String.join(",", defectIdsSet).replace(",,", "").replace("Defects,", "");

        // get defects status
        getIssuesStatus(defectIds);

    }

    /**
     * gets the status of the jira issue
     * @param defectIds the defect id(s)
     * @return the k,v of the defect and status
     */

    private static synchronized Map<String, String> getIssuesStatus(String defectIds) {


        if (!defectIds.isEmpty()) {

            isIssuesStatusTriggered = true;

            // log access object
            LogAccess defectsLogAccess = new LogAccess("DefectsLog", LogVerboseEnums.ALL);

            // get the status of all the defects
            ApiMethods apiMethods = new ApiMethods(defectsLogAccess);

            // build query string
            String endPointURI = jiraAppUrl + "rest/api/2/search?jql=id IN (" + defectIds + ")&fields=status";

            // headers information
            HashMap<String, String> headers = new HashMap<>();

            headers.put("Authorization", "Bearer " + jiraAppBearerAuthToken);

            try {
                // capture response
                Response response = apiMethods.sendRequest("get", endPointURI, headers);

                if(response.getStatusCode() == 200) {
                    isSuccessfullyLogged = true;


                    // store each defect information
                    for (String issue : defectIds.split(",")) {
                        // get the status of the defect
                        String status = apiMethods.getValue(response, "issues.findAll{issues -> issues.key=='" + issue.trim() + "'}.fields.status.name[0]");

                        // add to the defects map
                        defectsInfoMap.put(issue.trim(), status);
                    }
                }else{
                    defectsLogAccess.getLogger().warn(response.getBody().prettyPrint());
                }
            } catch (Exception e) {
                // TODO need to handle this exception case, for now printing the exception in the console log
                //  possible scenarios for exception are
                //  1) the defect number is invalid
                //  2) token updated
                //  3) response code might not 200
                defectsLogAccess.getLogger().error(e.getMessage());
                e.printStackTrace();
            }
        }
        return defectsInfoMap;
    }

    /**
     * gets the defect status from jira based on the defect id
     *
     * @param defectId the defect(issue) id
     * @return the defect status
     */
    public String getDefectStatus(String defectId) {
        String defectStatus;

        // check if the defect is already part of the defectsInfoMap
        if (defectsInfoMap.containsKey(defectId.trim())) {
            defectStatus = defectsInfoMap.get(defectId);
        } else {

            if(!isIssuesStatusTriggered){
                // get the status with fresh call if it's not present in the map
                getIssuesStatus(defectId);
            }

            if(!isSuccessfullyLogged){
                defectStatus = "JIRA Access Denied";
            } else if(isSuccessfullyLogged && !defectsInfoMap.containsKey(defectId.trim())){
                defectStatus = "Defect Info not fetched";
            } else {
                defectStatus = defectsInfoMap.get(defectId.trim());
            }

        }

        return defectStatus;
    }

}