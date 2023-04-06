package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.widgets.ExecutorsWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;

public class ExecutorsWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public ExecutorsWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Fetches the executor details
     *
     * @throws Exception the exception
     */
    public void fetchExecutorDetails() {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String executorsJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.EXECUTORS_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(executorsJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                JSONParser parser = new JSONParser();

                JSONArray executorDetailsJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                for (int currentIndex = 0; currentIndex < executorDetailsJsonArray.size(); currentIndex++){

                    JSONObject currentExecutorDetailsJsonObject = (JSONObject) executorDetailsJsonArray.get(currentIndex);

                    ExecutorsWidgetSingleton.getInstance().addExecutorDetails(currentExecutorDetailsJsonObject);

                }

            }catch (Exception e){

                e.printStackTrace();
            }

            break;

        }

        System.out.println("Check all the items - " + ExecutorsWidgetSingleton.getInstance().getExecutorDetailsJsonArray());


    }

    public void writeToFile(){

        // update the executors.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() +
                    File.separatorChar + AllureCommonVariables.EXECUTORS_JSON);
            file.write(ExecutorsWidgetSingleton.getInstance().getExecutorDetailsJsonArray().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
