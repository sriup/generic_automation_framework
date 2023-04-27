package consolidated.allure.singletons.data;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CategoriesDataSingleton {

    private static CategoriesDataSingleton instance = null;

    private static JSONObject categoriesDataJson;
    private static int testCasesCount;

    /**
     * Initializing CategoriesDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static CategoriesDataSingleton getInstance() {

        if (instance == null) {

            instance = new CategoriesDataSingleton();

            categoriesDataJson = new JSONObject();

            testCasesCount = 0;

        }

        return instance;
    }

    public void addCategoriesData(String parentUid, String  parentName,
                                 String  subParentUid, String  subParentName,
                                 String categoryUid, String categoryName,
                                 String childUid, JSONObject childJsonObject){

        String currentParentUid = (String) categoriesDataJson.get(AllureCommonVariables.UID);

        if(currentParentUid != null && currentParentUid.equalsIgnoreCase(parentUid)){

            JSONArray parentChildrenJsonArray = (JSONArray) categoriesDataJson.get(AllureCommonVariables.CHILDREN);

            boolean isSubParentFound = false;

            for (int currentParentChildIndex = 0; currentParentChildIndex < parentChildrenJsonArray.size(); currentParentChildIndex++){

                JSONObject currentParentChildJsonObject = (JSONObject) parentChildrenJsonArray.get(currentParentChildIndex);

                String currentParentChildUid = currentParentChildJsonObject.get(AllureCommonVariables.UID).toString();

                if(currentParentChildUid != null && currentParentChildUid.equals(subParentUid)){

                    JSONArray subParentChildrenJsonArray = (JSONArray) currentParentChildJsonObject.get(AllureCommonVariables.CHILDREN);

                    boolean isCategoryFound = false;

                    for (int categoryIndex = 0; categoryIndex < subParentChildrenJsonArray.size(); categoryIndex++){


                        JSONObject currentCategoryJsonObject = (JSONObject) subParentChildrenJsonArray.get(categoryIndex);

                        String currentCategoryUid = currentCategoryJsonObject.get(AllureCommonVariables.UID).toString();

                        if(currentCategoryUid != null && currentCategoryUid.equals(categoryUid)){

                            JSONArray categoryChildrenJsonArray = (JSONArray) currentCategoryJsonObject.get(AllureCommonVariables.CHILDREN);

                            categoryChildrenJsonArray.add(childJsonObject);

                            isCategoryFound = true;

                            break;

                        }

                    }

                    if(!isCategoryFound){

                        JSONArray categoryJsonArray = new JSONArray();
                        categoryJsonArray.add(childJsonObject);

                        JSONObject categoryJsonObject = new JSONObject();
                        categoryJsonObject.put(AllureCommonVariables.UID, categoryUid);

                        if(categoryName != null && !categoryName.isEmpty()){
                            categoryJsonObject.put(AllureCommonVariables.NAME, categoryName);
                        }

                        categoryJsonObject.put(AllureCommonVariables.CHILDREN, categoryJsonArray);


                        subParentChildrenJsonArray.add(categoryJsonObject);

                    }


                    isSubParentFound = true;

                    break;

                }

            }

            if(!isSubParentFound){

                JSONObject parentChildJsonObject = new JSONObject();
                parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
                parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

                JSONArray categoryJsonArray = new JSONArray();
                categoryJsonArray.add(childJsonObject);

                JSONObject categoryJsonObject = new JSONObject();
                categoryJsonObject.put(AllureCommonVariables.UID, categoryUid);

                if(categoryName != null && !categoryName.isEmpty()){
                    categoryJsonObject.put(AllureCommonVariables.NAME, categoryName);
                }

                categoryJsonObject.put(AllureCommonVariables.CHILDREN, categoryJsonArray);

                JSONArray subParentChildJsonArray = new JSONArray();
                subParentChildJsonArray.add(categoryJsonObject);

                parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

                parentChildrenJsonArray.add(parentChildJsonObject);

            }



        } else {

            categoriesDataJson.put(AllureCommonVariables.UID, parentUid);
            categoriesDataJson.put(AllureCommonVariables.NAME, parentName);



            JSONObject parentChildJsonObject = new JSONObject();
            parentChildJsonObject.put(AllureCommonVariables.UID, subParentUid);
            parentChildJsonObject.put(AllureCommonVariables.NAME, subParentName);

            JSONArray categoryJsonArray = new JSONArray();
            categoryJsonArray.add(childJsonObject);

            JSONObject categoryJsonObject = new JSONObject();
            categoryJsonObject.put(AllureCommonVariables.UID, categoryUid);

            if(categoryName != null && !categoryName.isEmpty()){
                categoryJsonObject.put(AllureCommonVariables.NAME, categoryName);
            }

            categoryJsonObject.put(AllureCommonVariables.CHILDREN, categoryJsonArray);

            JSONArray subParentChildJsonArray = new JSONArray();
            subParentChildJsonArray.add(categoryJsonObject);

            parentChildJsonObject.put(AllureCommonVariables.CHILDREN, subParentChildJsonArray);

            JSONArray parentChildrenJsonArray = new JSONArray();
            parentChildrenJsonArray.add(parentChildJsonObject);

            categoriesDataJson.put(AllureCommonVariables.CHILDREN, parentChildrenJsonArray);

        }

    }

    public JSONObject getCategoriesDataJson() {
        return categoriesDataJson;
    }

    public int getTestCasesCount(){
        return testCasesCount;
    }

    public void setTestCasesCount(){
        testCasesCount++;
    }

}
