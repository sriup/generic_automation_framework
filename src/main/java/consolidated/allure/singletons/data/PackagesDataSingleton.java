package consolidated.allure.singletons.data;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PackagesDataSingleton {

    private static PackagesDataSingleton instance = null;

    private static JSONObject packagesDataJson;
    private static int testCasesCount;

    /**
     * Initializing PackagesDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static PackagesDataSingleton getInstance() {

        if (instance == null) {

            instance = new PackagesDataSingleton();

            packagesDataJson = new JSONObject();

            testCasesCount = 0;

        }

        return instance;
    }

    public void addPackagesData(String parentUid, String  parentName,
                                 String  subParentUid, String  subParentName,
                                 String testClassUid, String testClassName,
                                 String childUid, JSONObject childJsonObject){

        String currentParentUid = (String) packagesDataJson.get(AllureCommonVariables.UID);

        if(currentParentUid != null && currentParentUid.equalsIgnoreCase(parentUid)){

            JSONArray parentChildrenJsonArray = (JSONArray) packagesDataJson.get(AllureCommonVariables.CHILDREN);

            boolean isSubParentFound = false;

            for (int currentParentChildIndex = 0; currentParentChildIndex < parentChildrenJsonArray.size(); currentParentChildIndex++){

                JSONObject currentParentChildJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildIndex);

                String currentParentChildUid = currentParentChildJsonObject.get(AllureCommonVariables.UID).toString();

                if(currentParentChildUid != null && currentParentChildUid.equals(subParentUid)){

                    JSONArray subParentChildrenJsonArray = (JSONArray) currentParentChildJsonObject.get(AllureCommonVariables.CHILDREN);

                    boolean isTestClassFound = false;

                    for (int testClassIndex = 0; testClassIndex < subParentChildrenJsonArray.size(); testClassIndex++){


                        JSONObject currentTestClassJsonObject = (JSONObject) subParentChildrenJsonArray.get(testClassIndex);

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


                        subParentChildrenJsonArray.add(testClassJsonObject);

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

                JSONArray subParentChildJsonArray = new JSONArray();
                subParentChildJsonArray.add(testClassJsonObject);

                parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

                parentChildrenJsonArray.add(parentChildJsonObject);

            }



        } else {

            packagesDataJson.put(AllureCommonVariables.UID, parentUid);
            packagesDataJson.put(AllureCommonVariables.NAME, parentName);



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

            JSONArray subParentChildJsonArray = new JSONArray();
            subParentChildJsonArray.add(testClassJsonObject);

            parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

            JSONArray parentChildrenJsonArray = new JSONArray();
            parentChildrenJsonArray.add(parentChildJsonObject);

            packagesDataJson.put(AllureCommonVariables.CHILDREN, parentChildrenJsonArray);

        }

    }

    public JSONObject getPackagesDataJson() {
        return packagesDataJson;
    }

    public int getTestCasesCount(){
        return testCasesCount;
    }

    public void setTestCasesCount(){
        testCasesCount++;
    }

}
