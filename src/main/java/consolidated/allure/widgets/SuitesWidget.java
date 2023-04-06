package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.singletons.widgets.SuitesWidgetSingleton;
import framework.commonfunctions.CommonFunctions;

import java.io.File;
import java.io.FileWriter;

public class SuitesWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public SuitesWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    public void writeToFile(){

        // update the suites.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() +
                    File.separatorChar + AllureCommonVariables.SUITES_JSON);
            file.write(SuitesWidgetSingleton.getInstance().getSuitesJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
