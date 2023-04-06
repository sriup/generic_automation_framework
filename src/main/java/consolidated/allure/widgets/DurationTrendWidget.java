package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.singletons.widgets.DurationTrendWidgetSingleton;
import framework.commonfunctions.CommonFunctions;

import java.io.File;
import java.io.FileWriter;

public class DurationTrendWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public DurationTrendWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    public void writeToFile(){

        // update the duration-trend.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() + File.separatorChar + AllureCommonVariables.DURATION_TREND_JSON);
            file.write(DurationTrendWidgetSingleton.getInstance().getDurationTrendJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
