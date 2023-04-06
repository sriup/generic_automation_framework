package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.widgets.CategoriesWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;

public class CategoriesWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public CategoriesWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;
    }

    /**
     * Extracts the passed categories
     *
     * @throws Exception the exception
     */
    public void extractPassedCategories()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String categoriesJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.CATEGORIES_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(categoriesJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                if(currentIterationFolder == 1){
                    CategoriesWidgetSingleton.getInstance().updateCategoriesTotal((Integer) jPathDocCon.read("total"));
                }

                JSONParser parser = new JSONParser();

                JSONArray itemsJsonArray = (JSONArray) parser.parse(jPathDocCon.read("items").toString());

                for (int currentItemIndex = 0; currentItemIndex < itemsJsonArray.size(); currentItemIndex++){
                    JSONObject currentItemJsonObject = (JSONObject) itemsJsonArray.get(currentItemIndex);

                    String uid = currentItemJsonObject.get(AllureCommonVariables.UID).toString();
                    String name = currentItemJsonObject.get(AllureCommonVariables.NAME).toString();
                    JSONObject statisticJsonObject = (JSONObject) currentItemJsonObject.get("statistic");
                    int failed = 0;
                    int passed = Integer.parseInt(statisticJsonObject.get(AllureCommonVariables.PASSED).toString());
                    int broken = 0;
                    int skipped = 0;
                    int unknown = 0;

                    CategoriesWidgetSingleton.getInstance().updateCategoriesItem(
                            uid, name, failed, broken, skipped, passed, unknown);


                }

            }catch (Exception e){

                e.printStackTrace();
            }


        }

        JSONArray itemsJsonArray = (JSONArray) CategoriesWidgetSingleton.getInstance().getCategoriesJson().get("items");
        for (int currentItemIndex = 0; currentItemIndex < itemsJsonArray.size(); currentItemIndex++){
            JSONObject currentItemJsonObject = (JSONObject) itemsJsonArray.get(currentItemIndex);

            String uid = currentItemJsonObject.get(AllureCommonVariables.UID).toString();
            String name = currentItemJsonObject.get(AllureCommonVariables.NAME).toString();

            if(name.toLowerCase().contains(AllureCommonVariables.PASSED)){

                CategoriesWidgetSingleton.getInstance().updateCategoriesItem(
                        uid, name, 0, 0, 0, -AllureCommonVariables.getPreconditionsPassCountsInBehaviors(), 0);

                break;

            }


        }


        System.out.println("Check all the items - " + CategoriesWidgetSingleton.getInstance().getCategoriesJson());


    }

    public void writeToFile(){

        // update the categories.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() + File.separatorChar + AllureCommonVariables.CATEGORIES_JSON);
            file.write(CategoriesWidgetSingleton.getInstance().getCategoriesJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
