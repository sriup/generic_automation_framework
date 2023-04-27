package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.SuitesDataSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import consolidated.allure.singletons.widgets.SuitesWidgetSingleton;
import consolidated.allure.singletons.widgets.SummaryWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class SuitesData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public SuitesData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the passed suites data
     *
     * @throws Exception the exception
     */
    public void extractPassedSuitesData()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String suitesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.SUITES_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(suitesJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                String parentUid = jPathDocCon.read(AllureCommonVariables.UID).toString();
                String  parentName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                JSONParser parser = new JSONParser();

                JSONArray parentChildrenJsonArray = (JSONArray) parser.parse(jPathDocCon.read(AllureCommonVariables.CHILDREN).toString());

                for (int currentParentChildrenIndex = 0; currentParentChildrenIndex < parentChildrenJsonArray.size(); currentParentChildrenIndex++){

                    JSONObject currentSubParentJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildrenIndex);

                    String subParentUid = currentSubParentJsonObject.get(AllureCommonVariables.UID).toString();
                    String subParentName = currentSubParentJsonObject.get(AllureCommonVariables.NAME).toString();

                    JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                    for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                        JSONObject currentTestJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                        String currentTestUid = currentTestJsonObject.get(AllureCommonVariables.UID).toString();

                        String currentTestName = currentTestJsonObject.get(AllureCommonVariables.NAME).toString();

                        JSONArray testChildrenJsonArray = (JSONArray) currentTestJsonObject.get(AllureCommonVariables.CHILDREN);

                        for (int currentTestIndex = 0; currentTestIndex < testChildrenJsonArray.size(); currentTestIndex++){

                            JSONObject currentTestClassJsonObject = (JSONObject) testChildrenJsonArray.get(currentTestIndex);

                            String currentTestClassUid = currentTestClassJsonObject.get(AllureCommonVariables.UID).toString();

                            String currentTestClassName = currentTestClassJsonObject.get(AllureCommonVariables.NAME).toString();

                            if(!currentTestClassName.equalsIgnoreCase("PreConditions")){

                                JSONArray testClassChildrenJsonArray = (JSONArray) currentTestClassJsonObject.get(AllureCommonVariables.CHILDREN);

                                for (int currentTestClassIndex = 0; currentTestClassIndex < testClassChildrenJsonArray.size(); currentTestClassIndex++){

                                    JSONObject currentChildJsonObject = (JSONObject) testClassChildrenJsonArray.get(currentTestClassIndex);

                                    String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                    String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                                    String childStatus = currentChildJsonObject.get(AllureCommonVariables.STATUS).toString();

                                    if(childStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){
                                        JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                        if(childTestCaseDataJson != null){

                                            SuitesDataSingleton.getInstance().addSuitesData(parentUid, parentName,
                                                    subParentUid, subParentName, currentTestUid, currentTestName, currentTestClassUid, currentTestClassName, childUid, currentChildJsonObject);

                                            SuitesDataSingleton.getInstance().setTestCasesCount();

                                            SuitesWidgetSingleton.getInstance().updateSuitesItem(
                                                    subParentUid, subParentName, 0, 0, 0, 1, 0);

                                            SummaryWidgetSingleton.getInstance().updateSummaryStatistic(
                                                    0, 0, 0, 1, 0);
                                        }
                                    }

                                }

                            }


                        }

                    }

                }


            }catch (Exception e){

                e.printStackTrace();
            }


        }

        System.out.println("Check all the items - " + SuitesDataSingleton.getInstance().getSuitesDataJson());

    }

    /**
     * Extracts the failed suites data
     *
     * @throws Exception the exception
     */
    public void extractFailedSuitesData()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String suitesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.SUITES_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();

                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(suitesJsonPath));

                    System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                    String parentUid = jPathDocCon.read(AllureCommonVariables.UID).toString();
                    String  parentName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                    JSONParser parser = new JSONParser();

                    JSONArray parentChildrenJsonArray = (JSONArray) parser.parse(jPathDocCon.read(AllureCommonVariables.CHILDREN).toString());

                    boolean isTestCaseFound = false;
                    for (int currentParentChildrenIndex = 0; currentParentChildrenIndex < parentChildrenJsonArray.size(); currentParentChildrenIndex++){

                        JSONObject currentSubParentJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildrenIndex);

                        String subParentUid = currentSubParentJsonObject.get(AllureCommonVariables.UID).toString();
                        String subParentName = currentSubParentJsonObject.get(AllureCommonVariables.NAME).toString();

                        JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                        for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                            JSONObject currentTestJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                            String currentTestUid = currentTestJsonObject.get(AllureCommonVariables.UID).toString();

                            String currentTestName = currentTestJsonObject.get(AllureCommonVariables.NAME).toString();

                            JSONArray testChildrenJsonArray = (JSONArray) currentTestJsonObject.get(AllureCommonVariables.CHILDREN);

                            for (int currentTestIndex = 0; currentTestIndex < testChildrenJsonArray.size(); currentTestIndex++){

                                JSONObject currentTestClassJsonObject = (JSONObject) testChildrenJsonArray.get(currentTestIndex);

                                String currentTestClassUid = currentTestClassJsonObject.get(AllureCommonVariables.UID).toString();

                                String currentTestClassName = currentTestClassJsonObject.get(AllureCommonVariables.NAME).toString();

                                if(!currentTestClassName.equalsIgnoreCase("PreConditions")){

                                    JSONArray testClassChildrenJsonArray = (JSONArray) currentTestClassJsonObject.get(AllureCommonVariables.CHILDREN);

                                    for (int currentTestClassIndex = 0; currentTestClassIndex < testClassChildrenJsonArray.size(); currentTestClassIndex++){

                                        JSONObject currentChildJsonObject = (JSONObject) testClassChildrenJsonArray.get(currentTestClassIndex);

                                        String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                        String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                                        String childStatus = currentChildJsonObject.get(AllureCommonVariables.STATUS).toString();

                                        if(childName.equalsIgnoreCase(currentTestCaseId)){

                                            JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                            if(childTestCaseDataJson != null){

                                                SuitesDataSingleton.getInstance().addSuitesData(parentUid, parentName,
                                                        subParentUid, subParentName, currentTestUid, currentTestName, currentTestClassUid, currentTestClassName, childUid, currentChildJsonObject);

                                                SuitesDataSingleton.getInstance().setTestCasesCount();

                                                int failed = (childStatus.equalsIgnoreCase(AllureCommonVariables.FAILED)) ? 1 : 0;
                                                int passed = 0;
                                                int broken = (childStatus.equalsIgnoreCase(AllureCommonVariables.BROKEN)) ? 1 : 0;
                                                int skipped = (childStatus.equalsIgnoreCase(AllureCommonVariables.SKIPPED)) ? 1 : 0;
                                                int unknown = (childStatus.equalsIgnoreCase(AllureCommonVariables.UNKNOWN)) ? 1 : 0;

                                                SuitesWidgetSingleton.getInstance().updateSuitesItem(
                                                        subParentUid, subParentName, failed,
                                                        broken, skipped, passed, unknown);

                                                SummaryWidgetSingleton.getInstance().updateSummaryStatistic(
                                                        failed, broken, skipped, passed, unknown);

                                            }

                                            isTestCaseFound = true;

                                            break;

                                        }

                                    }

                                }

                                if(isTestCaseFound){
                                    break;
                                }


                            }

                            if(isTestCaseFound){
                                break;
                            }

                        }

                        if(isTestCaseFound){
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

        // update the suites.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder() + File.separatorChar + AllureCommonVariables.SUITES_JSON);
            file.write(SuitesDataSingleton.getInstance().getSuitesDataJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
