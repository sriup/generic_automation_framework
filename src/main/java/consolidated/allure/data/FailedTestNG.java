package consolidated.allure.data;

import consolidated.allure.initclass.AllureInitClass;
import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FailedTestNG extends CommonFunctions {

    private Map<String, List<String>> failedTestNGMap;

    private AllureInitClass allureInitClass;

    public FailedTestNG(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

        failedTestNGMap = new HashMap<>();

    }


    /**
     * Extracts the failed Test Cases
     *
     * @throws Exception the exception
     */
    public void extractFailedTestCases() {

            String testCasesFolderPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + AllureCommonVariables.DATA + File.separatorChar + "test-cases";

            List<String> allTestCasesFileNames = getFolderFileUtil().getAllFileNames(testCasesFolderPath);

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
                        if(!isPrecondition && (testCasesStatus.equalsIgnoreCase(AllureCommonVariables.FAILED)
                        || testCasesStatus.equalsIgnoreCase(AllureCommonVariables.SKIPPED)
                        || testCasesStatus.equalsIgnoreCase(AllureCommonVariables.BROKEN))){


                            String packageName = "";

                            for (int currentLabelIndex = 0; currentLabelIndex < labelsJsonArray.size(); currentLabelIndex++) {

                                JSONObject currentLabelJsonObject = (JSONObject) labelsJsonArray.get(currentLabelIndex);

                                String name = currentLabelJsonObject.get(AllureCommonVariables.NAME).toString();
                                String value = currentLabelJsonObject.get("value").toString();

                                if(name.equalsIgnoreCase("testClass")){

                                    packageName = value;
                                    break;
                                }


                            }

                            List<String> failedTestCasesList = (failedTestNGMap.containsKey(packageName))
                                    ? failedTestNGMap.get(packageName) : new ArrayList();

                            failedTestCasesList.add(testCasesName);

                            failedTestNGMap.put(packageName, failedTestCasesList);


                        }


                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }


    }

    public Map<String, List<String>> getFailedTestNGMap(){
        return failedTestNGMap;
    }

}
