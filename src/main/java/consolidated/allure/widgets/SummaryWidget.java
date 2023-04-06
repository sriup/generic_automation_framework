package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import com.jayway.jsonpath.DocumentContext;
import consolidated.allure.singletons.widgets.SummaryWidgetSingleton;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;

public class SummaryWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public SummaryWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    /**
     * Extracts the time details in summary report
     *
     * @throws Exception the exception
     */
    public void extractTimeDetails()
            throws Exception {

        int numberOfIterationsSize = allureInitClass.getIterationFoldersSize();

        for (int currentIterationFolder = 1; currentIterationFolder <= numberOfIterationsSize; currentIterationFolder++) {

            String summaryJsonPath = allureInitClass.getAllureIterationPath() + File.separatorChar
                    + (AllureCommonVariables.RUN + currentIterationFolder) +
                    File.separatorChar + AllureCommonVariables.SITE + File.separatorChar + AllureCommonVariables.ALLURE_MAVEN_PLUGIN +
                    File.separatorChar + "widgets" + File.separatorChar + AllureCommonVariables.SUMMARY_JSON;

            DocumentContext jPathDocCon;

            try{

                jPathDocCon = com.jayway.jsonpath.JsonPath.parse(new File(summaryJsonPath));

                JSONParser parser = new JSONParser();

                JSONObject currentSummaryJsonObject = (JSONObject) parser.parse(jPathDocCon.jsonString());

                JSONObject currentTimeJsonObject = (JSONObject) currentSummaryJsonObject.get("time");

                long currentStart = (currentIterationFolder == 1) ? (Long) currentTimeJsonObject.get("start") : 0;
                long currentStop = (Long) currentTimeJsonObject.get("stop");

                SummaryWidgetSingleton.getInstance().updateSummaryTime(currentStart, currentStop);

            }catch (Exception e){

                e.printStackTrace();
            }


        }

    }

    public void writeToFile(){

        // update the summary.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() +
                    File.separatorChar + AllureCommonVariables.SUMMARY_JSON);
            file.write(SummaryWidgetSingleton.getInstance().getSummaryJsonObject().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
