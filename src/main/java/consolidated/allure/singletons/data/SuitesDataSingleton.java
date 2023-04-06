package consolidated.allure.singletons.data;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SuitesDataSingleton {

    private static SuitesDataSingleton instance = null;

    private static JSONObject suitesDataJson;
    private static int testCasesCount;

    /**
     * Initializing SuitesDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static SuitesDataSingleton getInstance() {

        if (instance == null) {

            instance = new SuitesDataSingleton();

            suitesDataJson = new JSONObject();

            testCasesCount = 0;

        }

        return instance;
    }

    public void addSuitesData(String parentUid, String  parentName,
                                 String  subParentUid, String  subParentName,
                                 String testUid, String testName,
                                 String testClassUid, String testClassName,
                                 String childUid, JSONObject childJsonObject){

        String currentParentUid = (String) suitesDataJson.get(AllureCommonVariables.UID);

        if(currentParentUid != null && currentParentUid.equalsIgnoreCase(parentUid)){

            JSONArray parentChildrenJsonArray = (JSONArray) suitesDataJson.get(AllureCommonVariables.CHILDREN);

            boolean isSubParentFound = false;

            for (int currentParentChildIndex = 0; currentParentChildIndex < parentChildrenJsonArray.size(); currentParentChildIndex++){

                JSONObject currentParentChildJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildIndex);

                String currentParentChildUid = currentParentChildJsonObject.get(AllureCommonVariables.UID).toString();

                if(currentParentChildUid != null && currentParentChildUid.equals(subParentUid)){

                    JSONArray subParentChildrenJsonArray = (JSONArray) currentParentChildJsonObject.get(AllureCommonVariables.CHILDREN);

                    boolean isTestFound = false;

                    for (int testIndex = 0; testIndex < subParentChildrenJsonArray.size(); testIndex++){

                        JSONObject currentTestJsonObject = (JSONObject) subParentChildrenJsonArray.get(testIndex);

                        String currentTestUid = currentTestJsonObject.get(AllureCommonVariables.UID).toString();

                        if(currentTestUid != null && currentTestUid.equals(testUid)){

                            JSONArray testChildrenJsonArray = (JSONArray) currentTestJsonObject.get(AllureCommonVariables.CHILDREN);

                            boolean isTestClassFound = false;

                            for (int testClassIndex = 0; testClassIndex < testChildrenJsonArray.size(); testClassIndex++){

                                JSONObject currentTestClassJsonObject = (JSONObject) testChildrenJsonArray.get(testClassIndex);

                                String currentTestClassUid = currentTestClassJsonObject.get(AllureCommonVariables.UID).toString();

                                if(currentTestClassUid != null && currentTestClassUid.equals(testClassUid)){

                                    JSONArray testClassChildrenJsonArray = (JSONArray) currentTestClassJsonObject.get(AllureCommonVariables.CHILDREN);

                                    testClassChildrenJsonArray.add(childJsonObject);

                                    isTestClassFound = true;

                                    break;

                                }

                            }

                            if(!isTestClassFound){

                                JSONArray testClassJsonArray = new JSONArray();
                                testClassJsonArray.add(childJsonObject);

                                JSONObject testClassJsonObject = new JSONObject();
                                testClassJsonObject.put(AllureCommonVariables.UID, testClassUid);

                                if(testClassName != null && !testClassName.isEmpty()){
                                    testClassJsonObject.put(AllureCommonVariables.NAME, testClassName);
                                }

                                testClassJsonObject.put(AllureCommonVariables.CHILDREN, testClassJsonArray);


                                testChildrenJsonArray.add(testClassJsonObject);

                            }

                            isTestFound = true;

                            break;

                        }

                    }

                    if(!isTestFound){

                        JSONArray testClassJsonArray = new JSONArray();
                        testClassJsonArray.add(childJsonObject);

                        JSONObject testClassJsonObject = new JSONObject();
                        testClassJsonObject.put(AllureCommonVariables.UID, testClassUid);

                        if(testClassName != null && !testClassName.isEmpty()){
                            testClassJsonObject.put(AllureCommonVariables.NAME, testClassName);
                        }

                        testClassJsonObject.put(AllureCommonVariables.CHILDREN, testClassJsonArray);

                        JSONArray testChildrenJsonArray = new JSONArray();
                        testChildrenJsonArray.add(testClassJsonObject);

                        JSONObject testJsonObject = new JSONObject();
                        testJsonObject.put(AllureCommonVariables.UID, testUid);
                        testJsonObject.put(AllureCommonVariables.NAME, testName);
                        testJsonObject.put(AllureCommonVariables.CHILDREN, testChildrenJsonArray);

                        subParentChildrenJsonArray.add(testJsonObject);

                    }

                    isSubParentFound = true;

                    break;

                }

            }

            if(!isSubParentFound){

                JSONObject parentChildJsonObject = new JSONObject();
                parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
                parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

                JSONArray testClassJsonArray = new JSONArray();
                testClassJsonArray.add(childJsonObject);

                JSONObject testClassJsonObject = new JSONObject();
                testClassJsonObject.put(AllureCommonVariables.UID, testClassUid);

                if(testClassName != null && !testClassName.isEmpty()){
                    testClassJsonObject.put(AllureCommonVariables.NAME, testClassName);
                }

                testClassJsonObject.put(AllureCommonVariables.CHILDREN, testClassJsonArray);

                JSONArray testChildrenJsonArray = new JSONArray();
                testChildrenJsonArray.add(testClassJsonObject);

                JSONObject testJsonObject = new JSONObject();
                testJsonObject.put(AllureCommonVariables.UID, testUid);
                testJsonObject.put(AllureCommonVariables.NAME, testName);
                testJsonObject.put(AllureCommonVariables.CHILDREN, testChildrenJsonArray);

                JSONArray subParentChildrenJsonArray = new JSONArray();
                subParentChildrenJsonArray.add(testJsonObject);

                parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildrenJsonArray);

                parentChildrenJsonArray.add(parentChildJsonObject);

            }



        } else {

            suitesDataJson.put(AllureCommonVariables.UID, parentUid);
            suitesDataJson.put(AllureCommonVariables.NAME, parentName);



            JSONObject parentChildJsonObject = new JSONObject();
            parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
            parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

            JSONArray testClassJsonArray = new JSONArray();
            testClassJsonArray.add(childJsonObject);

            JSONObject testClassJsonObject = new JSONObject();
            testClassJsonObject.put(AllureCommonVariables.UID, testClassUid);

            if(testClassName != null && !testClassName.isEmpty()){
                testClassJsonObject.put(AllureCommonVariables.NAME, testClassName);
            }

            testClassJsonObject.put(AllureCommonVariables.CHILDREN, testClassJsonArray);

            JSONArray testChildrenJsonArray = new JSONArray();
            testChildrenJsonArray.add(testClassJsonObject);

            JSONObject testJsonObject = new JSONObject();
            testJsonObject.put(AllureCommonVariables.UID, testUid);
            testJsonObject.put(AllureCommonVariables.NAME, testName);
            testJsonObject.put(AllureCommonVariables.CHILDREN, testChildrenJsonArray);

            JSONArray subParentChildrenJsonArray = new JSONArray();
            subParentChildrenJsonArray.add(testJsonObject);

            parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildrenJsonArray);

            JSONArray parentChildrenJsonArray = new JSONArray();
            parentChildrenJsonArray.add(parentChildJsonObject);

            suitesDataJson.put(AllureCommonVariables.CHILDREN, parentChildrenJsonArray);

        }

    }

    public JSONObject getSuitesDataJson() {
        return suitesDataJson;
    }

    public int getTestCasesCount(){
        return testCasesCount;
    }

    public void setTestCasesCount(){
        testCasesCount++;
    }

}
