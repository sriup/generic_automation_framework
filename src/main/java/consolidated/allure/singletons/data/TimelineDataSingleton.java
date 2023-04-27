package consolidated.allure.singletons.data;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TimelineDataSingleton {

    private static TimelineDataSingleton instance = null;

    private static JSONObject timelineDataJson;
    private static int testCasesCount;

    /**
     * Initializing TimelineDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static TimelineDataSingleton getInstance() {

        if (instance == null) {

            instance = new TimelineDataSingleton();

            timelineDataJson = new JSONObject();

            testCasesCount = 0;

        }

        return instance;
    }

    public void addTimelineData(String parentUid, String  parentName,
                                 String  subParentUid, String  subParentName,
                                 String testUid, String testName,
                                 String childUid, JSONObject childJsonObject){

        String currentParentUid = (String) timelineDataJson.get(AllureCommonVariables.UID);

        if(currentParentUid != null && currentParentUid.equalsIgnoreCase(parentUid)){

            JSONArray parentChildrenJsonArray = (JSONArray) timelineDataJson.get(AllureCommonVariables.CHILDREN);

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

                            testChildrenJsonArray.add(childJsonObject);

                            isTestFound = true;

                            break;

                        }

                    }

                    if(!isTestFound){

                        JSONArray testJsonArray = new JSONArray();
                        testJsonArray.add(childJsonObject);

                        JSONObject testJsonObject = new JSONObject();
                        testJsonObject.put(AllureCommonVariables.UID, testUid);

                        if(testName != null && !testName.isEmpty()){
                            testJsonObject.put(AllureCommonVariables.NAME, testName);
                        }

                        testJsonObject.put(AllureCommonVariables.CHILDREN, testJsonArray);


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

                JSONArray testJsonArray = new JSONArray();
                testJsonArray.add(childJsonObject);

                JSONObject testJsonObject = new JSONObject();
                testJsonObject.put(AllureCommonVariables.UID, testUid);

                if(testName != null && !testName.isEmpty()){
                    testJsonObject.put(AllureCommonVariables.NAME, testName);
                }

                testJsonObject.put(AllureCommonVariables.CHILDREN, testJsonArray);

                JSONArray subParentChildJsonArray = new JSONArray();
                subParentChildJsonArray.add(testJsonObject);

                parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

                parentChildrenJsonArray.add(parentChildJsonObject);

            }



        } else {

            timelineDataJson.put(AllureCommonVariables.UID, parentUid);
            timelineDataJson.put(AllureCommonVariables.NAME, parentName);



            JSONObject parentChildJsonObject = new JSONObject();
            parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
            parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

            JSONArray testJsonArray = new JSONArray();
            testJsonArray.add(childJsonObject);

            JSONObject testJsonObject = new JSONObject();
            testJsonObject.put(AllureCommonVariables.UID, testUid);

            if(testName != null && !testName.isEmpty()){
                testJsonObject.put(AllureCommonVariables.NAME, testName);
            }

            testJsonObject.put(AllureCommonVariables.CHILDREN, testJsonArray);

            JSONArray subParentChildJsonArray = new JSONArray();
            subParentChildJsonArray.add(testJsonObject);

            parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

            JSONArray parentChildrenJsonArray = new JSONArray();
            parentChildrenJsonArray.add(parentChildJsonObject);

            timelineDataJson.put(AllureCommonVariables.CHILDREN, parentChildrenJsonArray);

        }

    }

    public JSONObject getTimelineDataJson() {
        return timelineDataJson;
    }

    public int getTestCasesCount(){
        return testCasesCount;
    }

    public void setTestCasesCount(){
        testCasesCount++;
    }

}
