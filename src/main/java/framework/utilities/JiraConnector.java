package framework.utilities;

import framework.commonfunctions.ApiMethods;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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
    private static String jiraAppBasicAuthToken;

    /**
     * Initializing JiraConnector instance <br>
     * <p>
     * Note: This method will get the instance of singleton JiraConnector class.
     * The instance will be created dynamically if it doesn't exist, otherwise uses
     * the existing instance
     *
     * @param xlUtil             the {@link ExcelUtil}
     * @param filePath           the full file path where the defects information is present
     * @param sheetName          the sheet name where the defects information is present
     * @param columnName         the column name where the defects information is present
     * @param jiraUrl            the Jira URL
     * @param jiraBasicAuthToken the Jira Basic Authentication Token
     * @return instance of this JiraConnector class
     * @throws Exception the exception
     */
    public static JiraConnector getInstance(ExcelUtil xlUtil, String filePath, String sheetName, String columnName, String jiraUrl, String jiraBasicAuthToken) throws Exception {
        if (jiraConnectorInstance == null) {
            // create JiraConnector class instance
            jiraConnectorInstance = new JiraConnector();

            // set jira url
            jiraAppUrl = jiraUrl;

            // set jira application basic authentication token
            jiraAppBasicAuthToken = jiraBasicAuthToken.replace("Basic ", "");


            // Fetching all the values after initializing the singleton class
            fetchDefectsInfo(xlUtil, filePath, sheetName, columnName);
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
     * @param jiraBasicAuthToken the Jira Basic Authentication Token
     * @return instance of this JiraConnector class
     * @throws Exception the exception
     */
    public static JiraConnector getInstance(String jiraUrl, String jiraBasicAuthToken) {
        if (jiraConnectorInstance == null) {
            // create JiraConnector class instance
            jiraConnectorInstance = new JiraConnector();

            jiraAppUrl = jiraUrl;
            jiraAppBasicAuthToken = jiraBasicAuthToken.replace("Basic ", "");
        }
        return jiraConnectorInstance;
    }

    /**
     * Empty constructor
     * @return the instance of {@link JiraConnector}
     * @throws InstantiationException
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
     * @param xlUtil the {@link ExcelUtil} instance
     * @param filePath  the file path
     * @param sheetName the sheet name
     * @param columnName the column name
     * @throws IOException the IO Exception
     */
    private static void fetchDefectsInfo(ExcelUtil xlUtil, String filePath, String sheetName, String columnName) throws IOException {

        Set<String> defectIdsSet = new LinkedHashSet<>();

        xlUtil.openWorkBook(filePath);

        Sheet sheet = xlUtil.getSheet(sheetName);


        for (Row currentRow : sheet) {
            String currentCellValue = xlUtil.getCellData(sheetName, currentRow, columnName).trim();
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

        // close the work book
        xlUtil.closeWorkBook();

    }

    /**
     * gets the status of the jira issue
     * @param defectIds the defect id(s)
     * @return the k,v of the defect and status
     */

    private static synchronized Map<String, String> getIssuesStatus(String defectIds) {


        if (!defectIds.isEmpty()) {
            // log access object
            LogAccess defectsLogAccess = new LogAccess("DefectsLog", LogVerboseEnums.ALL);

            // get the status of all the defects
            ApiMethods apiMethods = new ApiMethods(defectsLogAccess);

            // build query string
            String endPointURI = jiraAppUrl + "rest/api/2/search?jql=id IN (" + defectIds + ")&fields=status";

            // headers information
            HashMap<String, String> headers = new HashMap<>();

            headers.put("Authorization", "Basic " + jiraAppBasicAuthToken);

            try {
                // capture response
                Response response = apiMethods.sendRequest("get", endPointURI, headers);

                if (response.getStatusCode() != 200) {
                    throw new Exception(response.getBody().print());
                }

                // store each defect information
                for (String issue : defectIds.split(",")) {
                    // get the status of the defect
                    String status = apiMethods.getValue(response, "issues.findAll{issues -> issues.key=='" + issue.trim() + "'}.fields.status.name[0]");

                    // add to the defects map
                    defectsInfoMap.put(issue.trim(), status);
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
            // get the status with fresh call if it's not present in the map
            defectStatus = getIssuesStatus(defectId).get(defectId.trim());
        }

        return defectStatus;
    }
}