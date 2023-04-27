package consolidated.allure.singletons.data;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BehaviorsDataSingleton {

    private static BehaviorsDataSingleton instance = null;

    private static JSONObject behaviorsDataJson;

    private static Map<String, String> multipleChildrenTestCasesMap;
    private static int testCasesCount;

    /**
     * Initializing BehaviorsDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static BehaviorsDataSingleton getInstance() {

        if (instance == null) {

            instance = new BehaviorsDataSingleton();

            behaviorsDataJson = new JSONObject();

            multipleChildrenTestCasesMap = new HashMap<>();

            testCasesCount = 0;

        }

        return instance;
    }

    public void addBehaviorsData(String parentUid, String  parentName,
                                 String  subParentUid, String  subParentName,
                                 String childUid, JSONObject childJsonObject){

        String currentParentUid = (String) behaviorsDataJson.get(AllureCommonVariables.UID);

        if(currentParentUid != null && currentParentUid.equalsIgnoreCase(parentUid)){

            JSONArray parentChildrenJsonArray = (JSONArray) behaviorsDataJson.get(AllureCommonVariables.CHILDREN);

            boolean isSubParentFound = false;

            for (int currentParentChildIndex = 0; currentParentChildIndex < parentChildrenJsonArray.size(); currentParentChildIndex++){

                JSONObject currentParentChildJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildIndex);

                String currentParentChildUid = currentParentChildJsonObject.get(AllureCommonVariables.UID).toString();

                if(currentParentChildUid != null && currentParentChildUid.equals(subParentUid)){

                    JSONArray subParentChildrenJsonArray = (JSONArray) currentParentChildJsonObject.get(AllureCommonVariables.CHILDREN);

                    subParentChildrenJsonArray.add(childJsonObject);

                    isSubParentFound = true;

                    break;

                }

            }

            if(!isSubParentFound){

                JSONObject parentChildJsonObject = new JSONObject();
                parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
                parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

                JSONArray subParentChildJsonArray = new JSONArray();
                subParentChildJsonArray.add(childJsonObject);

                parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

                parentChildrenJsonArray.add(parentChildJsonObject);

            }



        } else {

            behaviorsDataJson.put(AllureCommonVariables.UID, parentUid);
            behaviorsDataJson.put(AllureCommonVariables.NAME, parentName);



            JSONObject parentChildJsonObject = new JSONObject();
            parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
            parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

            JSONArray subParentChildJsonArray = new JSONArray();
            subParentChildJsonArray.add(childJsonObject);

            parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

            JSONArray parentChildrenJsonArray = new JSONArray();
            parentChildrenJsonArray.add(parentChildJsonObject);

            behaviorsDataJson.put(AllureCommonVariables.CHILDREN, parentChildrenJsonArray);

        }

    }

    public JSONObject getBehaviorsDataJson() {
        return behaviorsDataJson;
    }

    public Map<String, String> getMultipleChildrenTestCasesMap(){
        return multipleChildrenTestCasesMap;
    }

    public void setMultipleChildrenTestCasesMap(String testCaseKey, String childrenCount){
        multipleChildrenTestCasesMap.put(testCaseKey, childrenCount);
    }

    public int getTestCasesCount(){
        return testCasesCount;
    }

    public void setTestCasesCount(){
        testCasesCount++;
    }

}
