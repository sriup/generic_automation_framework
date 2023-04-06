package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.widgets.EnvironmentWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;

public class EnvironmentWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public EnvironmentWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Fetches the environment details
     *
     * @throws Exception the exception
     */
    public void fetchEnvironmentDetails()
            throws Exception  {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String environmentJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.ENVIRONMENT_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(environmentJsonPath));

                System.out.println("jPathDocCon - '" + jPathDocCon + "'");

                JSONParser parser = new JSONParser();

                JSONArray environmentDetailsJsonArray = (JSONArray) parser.parse(jPathDocCon.read("$").toString());

                for (int currentIndex = 0; currentIndex < environmentDetailsJsonArray.size(); currentIndex++){
                    JSONObject currentEnvironmentDetailsJsonObject = (JSONObject) environmentDetailsJsonArray.get(currentIndex);

                    String name = currentEnvironmentDetailsJsonObject.get(AllureCommonVariables.NAME).toString();

                    JSONArray valuesJsonArray = (JSONArray) currentEnvironmentDetailsJsonObject.get(AllureCommonVariables.VALUES);

                    for(int currentValueIndex = 0; currentValueIndex < valuesJsonArray.size(); currentValueIndex++){

                        String values = (String) valuesJsonArray.get(currentValueIndex);

                        EnvironmentWidgetSingleton.getInstance().addEnvironmentDetails(name, values);

                    }

                }

            }catch (Exception e){

                e.printStackTrace();
            }


        }




        System.out.println("Check all the items - " + EnvironmentWidgetSingleton.getInstance().getEnvironmentDetailsJsonArray());


    }

    public void writeToFile(){

        // update the environment.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() +
                    File.separatorChar + AllureCommonVariables.ENVIRONMENT_JSON);
            file.write(EnvironmentWidgetSingleton.getInstance().getEnvironmentDetailsJsonArray().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
