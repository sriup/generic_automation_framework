package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.BehaviorsDataSingleton;
import consolidated.allure.singletons.widgets.BehaviorsWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class BahaviorsData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public BahaviorsData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the passed behaviors data
     *
     * @throws Exception the exception
     */
    public void extractPassedBehaviorsData()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String behaviorsJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.BEHAVIORS_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(behaviorsJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                String parentUid = jPathDocCon.read(AllureCommonVariables.UID).toString();
                String  parentName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                JSONParser parser = new JSONParser();

                JSONArray parentChildrenJsonArray = (JSONArray) parser.parse(jPathDocCon.read(AllureCommonVariables.CHILDREN).toString());

                for (int currentParentChildrenIndex = 0; currentParentChildrenIndex < parentChildrenJsonArray.size(); currentParentChildrenIndex++){

                    JSONObject currentSubParentJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildrenIndex);

                    String subParentUid = currentSubParentJsonObject.get(AllureCommonVariables.UID).toString();
                    String subParentName = currentSubParentJsonObject.get(AllureCommonVariables.NAME).toString();

                    if(!subParentName.equalsIgnoreCase("Precondition")){

                        JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                        for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                            JSONObject currentChildJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                            String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                            String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                            int childrenCount = ((JSONArray) currentChildJsonObject.get(AllureCommonVariables.CHILDREN)).size();

                            if(childrenCount == 1){

                                String childStatus = ((JSONObject) ((JSONArray) currentChildJsonObject.get(AllureCommonVariables.CHILDREN)).get(0)).get(AllureCommonVariables.STATUS).toString();

                                if(childStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                                    BehaviorsDataSingleton.getInstance().addBehaviorsData(parentUid, parentName,
                                            subParentUid, subParentName, childUid, currentChildJsonObject);

                                    BehaviorsDataSingleton.getInstance().setTestCasesCount();

                                    int failed = 0;
                                    int passed = 1;
                                    int broken = 0;
                                    int skipped = 0;
                                    int unknown = 0;

                                    // Updating the Behaviors Widget
                                    BehaviorsWidgetSingleton.getInstance().updateBehaviorsItem(
                                            subParentUid, subParentName, failed, broken, skipped, passed, unknown);

                                }

                            } else {
                                BehaviorsDataSingleton.getInstance().setMultipleChildrenTestCasesMap(childName, String.valueOf(childrenCount));
                            }


                        }

                    }

                }


            }catch (Exception e){

                e.printStackTrace();
            }


        }

        System.out.println("Check all the items - " + BehaviorsDataSingleton.getInstance().getBehaviorsDataJson());

    }

    /**
     * Extracts the failed behaviors data
     *
     * @throws Exception the exception
     */
    public void extractFailedBehaviorsData()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String behaviorsJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.BEHAVIORS_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();


                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(behaviorsJsonPath));

                    System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                    String parentUid = jPathDocCon.read(AllureCommonVariables.UID).toString();
                    String  parentName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                    JSONParser parser = new JSONParser();

                    JSONArray parentChildrenJsonArray = (JSONArray) parser.parse(jPathDocCon.read(AllureCommonVariables.CHILDREN).toString());

                    boolean isTestCaseId = false;

                    for (int currentParentChildrenIndex = 0; currentParentChildrenIndex < parentChildrenJsonArray.size(); currentParentChildrenIndex++){

                        JSONObject currentSubParentJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildrenIndex);

                        String subParentUid = currentSubParentJsonObject.get(AllureCommonVariables.UID).toString();
                        String subParentName = currentSubParentJsonObject.get(AllureCommonVariables.NAME).toString();

                        if(!subParentName.equalsIgnoreCase("Precondition")){

                            JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                            for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                                JSONObject currentChildJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                                String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                                int childrenCount = ((JSONArray) currentChildJsonObject.get(AllureCommonVariables.CHILDREN)).size();

                                if(childrenCount == 1){

                                    String childStatus = ((JSONObject) ((JSONArray) currentChildJsonObject.get(AllureCommonVariables.CHILDREN)).get(0)).get(AllureCommonVariables.STATUS).toString();

                                    String childTestCaseName = ((JSONObject) ((JSONArray) currentChildJsonObject.get(AllureCommonVariables.CHILDREN)).get(0)).get(AllureCommonVariables.NAME).toString();

                                    if(!childStatus.equalsIgnoreCase(AllureCommonVariables.PASSED) &&
                                            currentTestCaseId.equalsIgnoreCase(childTestCaseName)){

                                        BehaviorsDataSingleton.getInstance().addBehaviorsData(parentUid, parentName,
                                                subParentUid, subParentName, childUid, currentChildJsonObject);

                                        BehaviorsDataSingleton.getInstance().setTestCasesCount();

                                        int failed = (childStatus.equalsIgnoreCase(AllureCommonVariables.FAILED)) ? 1 : 0;
                                        int passed = 0;
                                        int broken = (childStatus.equalsIgnoreCase(AllureCommonVariables.BROKEN)) ? 1 : 0;
                                        int skipped = (childStatus.equalsIgnoreCase(AllureCommonVariables.SKIPPED)) ? 1 : 0;
                                        int unknown = (childStatus.equalsIgnoreCase(AllureCommonVariables.UNKNOWN)) ? 1 : 0;

                                        // Updating the Behaviors Widget
                                        BehaviorsWidgetSingleton.getInstance().updateBehaviorsItem(
                                                subParentUid, subParentName, failed, broken, skipped, passed, unknown);

                                        isTestCaseId = true;

                                        break;

                                    }

                                } else {
                                    BehaviorsDataSingleton.getInstance().setMultipleChildrenTestCasesMap(childName, String.valueOf(childrenCount));
                                }

                            }

                        }

                        if(isTestCaseId){
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

        // update the behaviors.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder() + File.separatorChar + AllureCommonVariables.BEHAVIORS_JSON);
            file.write(BehaviorsDataSingleton.getInstance().getBehaviorsDataJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
