package consolidated.allure.singletons.widgets;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CategoriesWidgetSingleton {

    private static CategoriesWidgetSingleton instance = null;

    private static JSONObject categoriesJson;

    /**
     * Initializing CategoriesSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static CategoriesWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new CategoriesWidgetSingleton();

            categoriesJson = new JSONObject();

            categoriesJson.put("total", 0);
            categoriesJson.put("items", new JSONArray());

        }

        return instance;
    }

    public void updateCategoriesTotal(int total){
        categoriesJson.put("total", total);
    }

    public void updateCategoriesItem(String uid, String name, int failed, int broken, int skipped,
                                     int passed, int unknown){

        boolean isItemFound = false;

        boolean isPreCondition = (name.toLowerCase().contains("precondition"));

        JSONArray itemsJsonArray = (JSONArray) categoriesJson.get("items");

        for (int currentItemIndex = 0; currentItemIndex < itemsJsonArray.size(); currentItemIndex++){

            if(isPreCondition){
                break;
            }

            JSONObject currentItemJsonObject = (JSONObject) itemsJsonArray.get(currentItemIndex);

            String currentItemUid = currentItemJsonObject.get(AllureCommonVariables.UID).toString();

            if(currentItemUid.equals(uid)){

                JSONObject currentStatisticJsonObject = (JSONObject) currentItemJsonObject.get("statistic");

                int increasedFailed = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.FAILED).toString()) + failed;
                int increasedBroken = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.BROKEN).toString()) + broken;
                int increasedSkipped = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.SKIPPED).toString()) + skipped;
                int increasedPassed = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.PASSED).toString()) + passed;
                int increasedUnknown = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.UNKNOWN).toString()) + unknown;


                currentStatisticJsonObject.put(AllureCommonVariables.FAILED, increasedFailed);
                currentStatisticJsonObject.put(AllureCommonVariables.BROKEN, increasedBroken);
                currentStatisticJsonObject.put(AllureCommonVariables.SKIPPED, increasedSkipped);
                currentStatisticJsonObject.put(AllureCommonVariables.PASSED, increasedPassed);
                currentStatisticJsonObject.put(AllureCommonVariables.UNKNOWN, increasedUnknown);

                int total = increasedFailed + increasedBroken + increasedSkipped + increasedPassed + increasedUnknown;

                currentStatisticJsonObject.put("total", total);

                currentItemJsonObject.put("statistic", currentStatisticJsonObject);

                isItemFound = true;

                break;
            }


        }

        if(!isItemFound && !isPreCondition){

            JSONObject currentItemJsonObject = new JSONObject();

            currentItemJsonObject.put(AllureCommonVariables.UID, uid);
            currentItemJsonObject.put(AllureCommonVariables.NAME, name);

            JSONObject statisticJsonObject = new JSONObject();
            statisticJsonObject.put(AllureCommonVariables.FAILED, failed);
            statisticJsonObject.put(AllureCommonVariables.BROKEN, broken);
            statisticJsonObject.put(AllureCommonVariables.SKIPPED, skipped);
            statisticJsonObject.put(AllureCommonVariables.PASSED, passed);
            statisticJsonObject.put(AllureCommonVariables.UNKNOWN, unknown);

            int total = failed + broken + skipped + passed + unknown;

            statisticJsonObject.put("total", total);

            currentItemJsonObject.put("statistic", statisticJsonObject);

            itemsJsonArray.add(currentItemJsonObject);

        }

        // Updating the total items
        updateCategoriesTotal(itemsJsonArray.size());

    }

    public JSONObject getCategoriesJson() {
        return categoriesJson;
    }

}
