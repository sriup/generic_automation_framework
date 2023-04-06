package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.PackagesDataSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class PackagesData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public PackagesData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;
    }

    /**
     * Extracts the passed packages data
     *
     * @throws Exception the exception
     */
    public void extractPassedPackagesData() {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String packagesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.PACKAGES_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(packagesJsonPath));

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

                        JSONObject currentTestClassJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

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

                                        PackagesDataSingleton.getInstance().addPackagesData(parentUid, parentName,
                                                subParentUid, subParentName, currentTestClassUid, currentTestClassName, childUid, currentChildJsonObject);

                                        PackagesDataSingleton.getInstance().setTestCasesCount();
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

        System.out.println("Check all the items - " + PackagesDataSingleton.getInstance().getPackagesDataJson());

    }


    /**
     * Extracts the failed packages data
     *
     * @throws Exception the exception
     */
    public void extractFailedPackagesData()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String packagesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.PACKAGES_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();



                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(packagesJsonPath));

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

                            JSONObject currentTestClassJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                            String currentTestClassUid = currentTestClassJsonObject.get(AllureCommonVariables.UID).toString();

                            String currentTestClassName = currentTestClassJsonObject.get(AllureCommonVariables.NAME).toString();

                            if(!currentTestClassName.equalsIgnoreCase("PreConditions")){

                                JSONArray testClassChildrenJsonArray = (JSONArray) currentTestClassJsonObject.get(AllureCommonVariables.CHILDREN);

                                for (int currentTestClassIndex = 0; currentTestClassIndex < testClassChildrenJsonArray.size(); currentTestClassIndex++){

                                    JSONObject currentChildJsonObject = (JSONObject) testClassChildrenJsonArray.get(currentTestClassIndex);

                                    String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                    String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();

                                    if(currentTestCaseId.equalsIgnoreCase(childName)){

                                        JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                        if(childTestCaseDataJson != null){

                                            PackagesDataSingleton.getInstance().addPackagesData(parentUid, parentName,
                                                    subParentUid, subParentName, currentTestClassUid, currentTestClassName, childUid, currentChildJsonObject);

                                            PackagesDataSingleton.getInstance().setTestCasesCount();
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


                }catch (Exception e){

                    e.printStackTrace();
                }

            }


        }


    }


    public void writeToFile(){

        // update the packages.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder() + File.separatorChar + AllureCommonVariables.PACKAGES_JSON);
            file.write(PackagesDataSingleton.getInstance().getPackagesDataJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
