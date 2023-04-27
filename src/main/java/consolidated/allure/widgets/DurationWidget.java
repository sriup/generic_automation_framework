package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import consolidated.allure.singletons.widgets.DurationWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class DurationWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public DurationWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;
    }

    /**
     * Extracts the passed test case duration
     *
     * @throws Exception the exception
     */
    public void extractPassedTestCaseDuration()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String durationJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.DURATION_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(durationJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                JSONParser parser = new JSONParser();

                JSONArray testCasesDurationJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                for (int currentTestCaseIndex = 0; currentTestCaseIndex < testCasesDurationJsonArray.size(); currentTestCaseIndex++){
                    JSONObject currentTestCaseJsonObject = (JSONObject) testCasesDurationJsonArray.get(currentTestCaseIndex);

                    String testCaseUid = currentTestCaseJsonObject.get(AllureCommonVariables.UID).toString();
                    String testCaseStatus = currentTestCaseJsonObject.get(AllureCommonVariables.STATUS).toString();

                    if(testCaseStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                        JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(testCaseUid);

                        if(childTestCaseDataJson != null){

                            DurationWidgetSingleton.getInstance().addTestCaseDuration(currentTestCaseJsonObject);
                        }

                    }

                }

            }catch (Exception e){

                e.printStackTrace();
            }


        }




        System.out.println("Check all the items - " + DurationWidgetSingleton.getInstance().getDurationJsonArray());


    }

    /**
     * Extracts the failed duration widget
     *
     * @throws Exception the exception
     */
    public void extractFailedTestCaseDuration()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String durationJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.DURATION_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();


                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(durationJsonPath));

                    System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                    JSONParser parser = new JSONParser();

                    JSONArray testCasesDurationJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                    for (int currentTestCaseIndex = 0; currentTestCaseIndex < testCasesDurationJsonArray.size(); currentTestCaseIndex++){
                        JSONObject currentTestCaseJsonObject = (JSONObject) testCasesDurationJsonArray.get(currentTestCaseIndex);

                        String testCaseUid = currentTestCaseJsonObject.get(AllureCommonVariables.UID).toString();
                        String testCaseName = currentTestCaseJsonObject.get(AllureCommonVariables.NAME).toString();
                        String testCaseStatus = currentTestCaseJsonObject.get(AllureCommonVariables.STATUS).toString();

                        if(!testCaseStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)
                                && testCaseName.equalsIgnoreCase(currentTestCaseId)){

                            JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(testCaseUid);

                            if(childTestCaseDataJson != null){

                                DurationWidgetSingleton.getInstance().addTestCaseDuration(currentTestCaseJsonObject);
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

        // update the duration.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() + File.separatorChar + AllureCommonVariables.DURATION_JSON);
            file.write(DurationWidgetSingleton.getInstance().getDurationJsonArray().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
