package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.singletons.widgets.RetryTrendWidgetSingleton;
import framework.commonfunctions.CommonFunctions;

import java.io.File;
import java.io.FileWriter;

public class RetryTrendWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public RetryTrendWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    public void writeToFile(){

        // update the retry-trend.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() + File.separatorChar + AllureCommonVariables.RETRY_TREND_JSON);
            file.write(RetryTrendWidgetSingleton.getInstance().getRetryTrendJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
