package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.singletons.data.TestCasesDataSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestCasesData extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public TestCasesData(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the passed Test Cases
     *
     * @throws Exception the exception
     */
    public void extractPassedTestCases()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String currentIterationFolderName = AllureCommonVariables.RUN + currentIterationFolder;

            String testCasesFolderPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + currentIterationFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + "test-cases";

            List<String> allTestCasesFileNames = getFolderFileUtil().getAllFileNames(testCasesFolderPath);

            for (String currentTestCaseFileName: allTestCasesFileNames) {

                System.out.println("Current Test Case filename - '" + currentTestCaseFileName + "'");

                String testCasesDataJsonPath = testCasesFolderPath + File.separatorChar + currentTestCaseFileName;

                DocumentContext jPathDocCon;

                try{

                    jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(testCasesDataJsonPath));

                    System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                    String uid = jPathDocCon.read(AllureCommonVariables.UID).toString();

                    String testCasesStatus = jPathDocCon.read(AllureCommonVariables.STATUS).toString();

                    JSONParser parser = new JSONParser();

                    JSONObject testCaseJsonObject = (JSONObject) parser.parse(jPathDocCon.jsonString());

                    JSONArray labelsJsonArray = (JSONArray) parser.parse(jPathDocCon.read("labels").toString());

                    boolean isPrecondition = false;

                    for (int currentLabelIndex = 0; currentLabelIndex < labelsJsonArray.size(); currentLabelIndex++) {

                        JSONObject currentLabelJsonObject = (JSONObject) labelsJsonArray.get(currentLabelIndex);

                        String name = currentLabelJsonObject.get(AllureCommonVariables.NAME).toString();
                        String value = currentLabelJsonObject.get("value").toString();

                        if(name.equalsIgnoreCase("feature") &&
                                value.equalsIgnoreCase("Precondition")){

                            isPrecondition = true;
                            break;
                        }


                    }

                    // If it is not a pre-condition then add it to the TestCasesDataSingleton
                    if(!isPrecondition && testCasesStatus.equalsIgnoreCase(AllureCommonVariables.PASSED)){

                        String newFilePath = allureInitClass.getConsolidatedAllureReportDataTestcasesFolder() + File.separatorChar + currentTestCaseFileName;

                        TestCasesDataSingleton.getInstance().addTestCases(uid, testCaseJsonObject, testCasesStatus,
                                getFolderFileUtil(), testCasesDataJsonPath, newFilePath);
                    }


                }catch (Exception e){

                    e.printStackTrace();
                }

            }


        }

    }


    /**
     * Extracts the failed Test Cases
     *
     * @throws Exception the exception
     */
    public void extractFailedTestCases()
            throws Exception {

        Map<String, Map<String, String>> executionRunDetailsMap =
                ExecutionRunDetailsSingleton.getInstance(allureInitClass).getExecutionRunDetailsMap();

        for(Map.Entry<String, Map<String, String>> currentExecutionRunDetailsMap: executionRunDetailsMap.entrySet()){

            String runFolderName = currentExecutionRunDetailsMap.getKey();
            Map<String, String> currentExecutionRunMap = currentExecutionRunDetailsMap.getValue();

            String testCasesFolderPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + runFolderName + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + "test-cases";

            List<String> allTestCasesFileNames = getFolderFileUtil().getAllFileNames(testCasesFolderPath);


            for (Map.Entry<String, String> executionRun: currentExecutionRunMap.entrySet()){

                String currentTestCaseId = executionRun.getKey();
                String currentStatus = executionRun.getValue();


                for (String currentTestCaseFileName: allTestCasesFileNames) {

                    String testCasesDataJsonPath = testCasesFolderPath + File.separatorChar + currentTestCaseFileName;

                    DocumentContext jPathDocCon;

                    try{

                        jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(testCasesDataJsonPath));

                        System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                        String uid = jPathDocCon.read(AllureCommonVariables.UID).toString();

                        String testCasesStatus = jPathDocCon.read(AllureCommonVariables.STATUS).toString();

                        String testCasesName = jPathDocCon.read(AllureCommonVariables.NAME).toString();

                        JSONParser parser = new JSONParser();

                        JSONObject testCaseJsonObject = (JSONObject) parser.parse(jPathDocCon.jsonString());

                        JSONArray labelsJsonArray = (JSONArray) parser.parse(jPathDocCon.read("labels").toString());

                        boolean isPrecondition = false;

                        for (int currentLabelIndex = 0; currentLabelIndex < labelsJsonArray.size(); currentLabelIndex++) {

                            JSONObject currentLabelJsonObject = (JSONObject) labelsJsonArray.get(currentLabelIndex);

                            String name = currentLabelJsonObject.get(AllureCommonVariables.NAME).toString();
                            String value = currentLabelJsonObject.get("value").toString();

                            if(name.equalsIgnoreCase("feature") &&
                                    value.equalsIgnoreCase("Precondition")){

                                isPrecondition = true;
                                break;
                            }


                        }

                        // If it is not a pre-condition then add it to the TestCasesDataSingleton
                        if(!isPrecondition && testCasesName.equalsIgnoreCase(currentTestCaseId)){

                            String newFilePath = allureInitClass.getConsolidatedAllureReportDataTestcasesFolder() +
                                    File.separatorChar + currentTestCaseFileName;

                            TestCasesDataSingleton.getInstance().addTestCases(uid, testCaseJsonObject, testCasesStatus,
                                    getFolderFileUtil(), testCasesDataJsonPath, newFilePath);

                            allureInitClass.attachments.parseAttachments(testCasesFolderPath, testCaseJsonObject);

                            break;

                        }


                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }



            }

        }


    }

}
