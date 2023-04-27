package consolidated.allure.data;

import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.CategoriesDataSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import consolidated.allure.singletons.widgets.CategoriesWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class CategoriesData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public CategoriesData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;
    }

    /**
     * Extracts the passed categories data
     *
     * @throws Exception the exception
     */
    public void extractPassedCategoriesData()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String categoriesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.CATEGORIES_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(categoriesJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                String parentUid = jPathDocCon.read(AllureCommonVariables.UID).toString();
                String  parentName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                JSONParser parser = new JSONParser();

                JSONArray parentChildrenJsonArray = (JSONArray) parser.parse(jPathDocCon.read(AllureCommonVariables.CHILDREN).toString());

                for (int currentParentChildrenIndex = 0; currentParentChildrenIndex < parentChildrenJsonArray.size(); currentParentChildrenIndex++){

                    JSONObject currentSubParentJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildrenIndex);

                    String subParentUid = currentSubParentJsonObject.get(AllureCommonVariables.UID).toString();
                    String subParentName = currentSubParentJsonObject.get(AllureCommonVariables.NAME).toString();

                    if(subParentName.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                        JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                        for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                            JSONObject currentCategoryJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                            String currentCategoryUid = currentCategoryJsonObject.get(AllureCommonVariables.UID).toString();

                            String currentCategoryName = (currentCategoryJsonObject.get(AllureCommonVariables.NAME) != null) ? currentCategoryJsonObject.get(AllureCommonVariables.NAME).toString() : null;

                            JSONArray categoryChildrenJsonArray = (JSONArray) currentCategoryJsonObject.get(AllureCommonVariables.CHILDREN);

                            for (int currentCategoryIndex = 0; currentCategoryIndex < categoryChildrenJsonArray.size(); currentCategoryIndex++){

                                JSONObject currentChildJsonObject = (JSONObject) categoryChildrenJsonArray.get(currentCategoryIndex);

                                String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                                String childStatus = currentChildJsonObject.get(AllureCommonVariables.STATUS).toString();

                                if(childStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)) {

                                    JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                    if(childTestCaseDataJson != null){

                                        CategoriesDataSingleton.getInstance().addCategoriesData(parentUid, parentName,
                                                subParentUid, subParentName, currentCategoryUid, currentCategoryName, childUid, currentChildJsonObject);

                                        CategoriesDataSingleton.getInstance().setTestCasesCount();

                                        int failed = 0;
                                        int passed = 1;
                                        int broken = 0;
                                        int skipped = 0;
                                        int unknown = 0;

                                        // Updating the Categories Widget
                                        CategoriesWidgetSingleton.getInstance().updateCategoriesItem(
                                                subParentUid, subParentName, failed, broken, skipped, passed, unknown);
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

        System.out.println("Check all the items - " + CategoriesDataSingleton.getInstance().getCategoriesDataJson());

    }

    /**
     * Extracts the failed Categories data
     *
     * @throws Exception the exception
     */
    public void extractFailedCategoriesData()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String categoriesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + AllureCommonVariables.CATEGORIES_JSON;


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();



                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(categoriesJsonPath));

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

                        if(!subParentName.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                            JSONArray subParentChildrenJsonArray = (JSONArray) currentSubParentJsonObject.get(AllureCommonVariables.CHILDREN);

                            for (int currentSubParentChildrenIndex = 0; currentSubParentChildrenIndex < subParentChildrenJsonArray.size(); currentSubParentChildrenIndex++){

                                JSONObject currentCategoryJsonObject = (JSONObject) subParentChildrenJsonArray.get(currentSubParentChildrenIndex);

                                String currentCategoryUid = currentCategoryJsonObject.get(AllureCommonVariables.UID).toString();

                                String currentCategoryName = (currentCategoryJsonObject.get(AllureCommonVariables.NAME) != null) ? currentCategoryJsonObject.get(AllureCommonVariables.NAME).toString() : null;

                                JSONArray categoryChildrenJsonArray = (JSONArray) currentCategoryJsonObject.get(AllureCommonVariables.CHILDREN);

                                for (int currentCategoryIndex = 0; currentCategoryIndex < categoryChildrenJsonArray.size(); currentCategoryIndex++){

                                    JSONObject currentChildJsonObject = (JSONObject) categoryChildrenJsonArray.get(currentCategoryIndex);

                                    String childUid = currentChildJsonObject.get(AllureCommonVariables.UID).toString();
                                    String childName = currentChildJsonObject.get(AllureCommonVariables.NAME).toString();
                                    String childStatus = currentChildJsonObject.get(AllureCommonVariables.STATUS).toString();

                                    if(childName.equalsIgnoreCase(currentTestCaseId)){

                                        JSONObject childTestCaseDataJson = (JSONObject) TestCasesDataSingleton.getInstance().getTestCasesDataJson().get(childUid);

                                        if(childTestCaseDataJson != null){

                                            CategoriesDataSingleton.getInstance().addCategoriesData(parentUid, parentName,
                                                    subParentUid, subParentName, currentCategoryUid, currentCategoryName, childUid, currentChildJsonObject);

                                            CategoriesDataSingleton.getInstance().setTestCasesCount();

                                            int failed = (childStatus.equalsIgnoreCase(AllureCommonVariables.FAILED)) ? 1 : 0;
                                            int passed = 0;
                                            int broken = (childStatus.equalsIgnoreCase(AllureCommonVariables.BROKEN)) ? 1 : 0;
                                            int skipped = (childStatus.equalsIgnoreCase(AllureCommonVariables.SKIPPED)) ? 1 : 0;
                                            int unknown = (childStatus.equalsIgnoreCase(AllureCommonVariables.UNKNOWN)) ? 1 : 0;

                                            // Updating the Categories Widget
                                            CategoriesWidgetSingleton.getInstance().updateCategoriesItem(
                                                    subParentUid, subParentName, failed, broken, skipped, passed, unknown);
                                        }

                                        isTestCaseFound = true;

                                        break;

                                    }

                                }

                                if(isTestCaseFound){
                                    break;
                                }


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

        // update the categories.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder() + File.separatorChar + AllureCommonVariables.CATEGORIES_JSON);
            file.write(CategoriesDataSingleton.getInstance().getCategoriesDataJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
