package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import consolidated.allure.singletons.widgets.SeverityWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class SeverityWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public SeverityWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the passed test case severity
     *
     * @throws Exception the exception
     */
    public void extractPassedTestCaseSeverity()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String severityJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.SEVERITY_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(severityJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                JSONParser parser = new JSONParser();

                JSONArray testCasesSeverityJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                for (int currentTestCaseIndex = 0; currentTestCaseIndex < testCasesSeverityJsonArray.size(); currentTestCaseIndex++){
                    JSONObject currentTestCaseJsonObject = (JSONObject) testCasesSeverityJsonArray.get(currentTestCaseIndex);

                    String testCaseUid = currentTestCaseJsonObject.get(AllureCommonVariables.UID).toString();
                    String testCaseStatus = currentTestCaseJsonObject.get(AllureCommonVariables.STATUS).toString();

                    if(testCaseStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                        JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(testCaseUid);

                        if(childTestCaseDataJson != null){

                            SeverityWidgetSingleton.getInstance().addTestCaseSeverity(currentTestCaseJsonObject);
                        }

                    }

                }

            }catch (Exception e){

                e.printStackTrace();
            }


        }




        System.out.println("Check all the items - " + SeverityWidgetSingleton.getInstance().getSeverityJsonArray());


    }

    /**
     * Extracts the failed severity widget
     *
     * @throws Exception the exception
     */
    public void extractFailedTestCaseSeverity()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String severityJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.SEVERITY_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();


                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(severityJsonPath));

                    System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                    JSONParser parser = new JSONParser();

                    JSONArray testCasesSeverityJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                    for (int currentTestCaseIndex = 0; currentTestCaseIndex < testCasesSeverityJsonArray.size(); currentTestCaseIndex++){
                        JSONObject currentTestCaseJsonObject = (JSONObject) testCasesSeverityJsonArray.get(currentTestCaseIndex);

                        String testCaseUid = currentTestCaseJsonObject.get(AllureCommonVariables.UID).toString();
                        String testCaseName = currentTestCaseJsonObject.get(AllureCommonVariables.NAME).toString();
                        String testCaseStatus = currentTestCaseJsonObject.get(AllureCommonVariables.STATUS).toString();

                        if(!testCaseStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)
                                && testCaseName.equalsIgnoreCase(currentTestCaseId)){

                            JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(testCaseUid);

                            if(childTestCaseDataJson != null){

                                SeverityWidgetSingleton.getInstance().addTestCaseSeverity(currentTestCaseJsonObject);
                            }

                            break;

                        }

                    }

                }catch (Exception e){

                    e.printStackTrace();
                }

            }


        }


    }

    public void writeToFile(){

        // update the severity.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() +
                    File.separatorChar + AllureCommonVariables.SEVERITY_JSON);
            file.write(SeverityWidgetSingleton.getInstance().getSeverityJsonArray().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
