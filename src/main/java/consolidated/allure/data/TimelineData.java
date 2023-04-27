package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import consolidated.allure.singletons.data.TimelineDataSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class TimelineData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public TimelineData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the passed Timeline data
     *
     * @throws Exception the exception
     */
    public void extractPassedTimelineData() {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String timelineJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.TIMELINE_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(timelineJsonPath));

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

                            JSONObject currentChildJsonObject = (JSONObject) testChildrenJsonArray.get(currentTestIndex);

                            String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                            String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                            String childStatus = currentChildJsonObject.get(AllureCommonVariables.STATUS).toString();

                            if(childStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                                JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                if(childTestCaseDataJson != null){

                                    TimelineDataSingleton.getInstance().addTimelineData(parentUid, parentName,
                                            subParentUid, subParentName, currentTestUid, currentTestName, childUid, currentChildJsonObject);

                                    TimelineDataSingleton.getInstance().setTestCasesCount();
                                }

                            }

                        }

                    }

                }


            }catch (Exception e){

                e.printStackTrace();
            }


        }

        System.out.println("Check all the items - " + TimelineDataSingleton.getInstance().getTimelineDataJson());

    }

    /**
     * Extracts the failed timeline data
     *
     * @throws Exception the exception
     */
    public void extractFailedTimelineData()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String timelineJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.TIMELINE_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();


                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(timelineJsonPath));

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

                                JSONObject currentChildJsonObject = (JSONObject) testChildrenJsonArray.get(currentTestIndex);

                                String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();

                                if(childName.equalsIgnoreCase(currentTestCaseId)){

                                    JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                    if(childTestCaseDataJson != null){

                                        TimelineDataSingleton.getInstance().addTimelineData(parentUid, parentName,
                                                subParentUid, subParentName, currentTestUid, currentTestName, childUid, currentChildJsonObject);

                                        TimelineDataSingleton.getInstance().setTestCasesCount();
                                    }

                                    isTestCaseFound = true;

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

        // update the timeline.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder() + File.separatorChar + AllureCommonVariables.TIMELINE_JSON);
            file.write(TimelineDataSingleton.getInstance().getTimelineDataJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
