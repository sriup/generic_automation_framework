package consolidated.allure.widgets;

import consolidated.allure.initclass.AllureInitClass;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.singletons.widgets.CategoriesTrendWidgetSingleton;
import framework.commonfunctions.CommonFunctions;

import java.io.File;
import java.io.FileWriter;

public class CategoriesTrendWidget extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public CategoriesTrendWidget(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    public void writeToFile(){

        // update the categories-trend.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportWidgetsFolder() + File.separatorChar + AllureCommonVariables.CATEGORIES_TREND_JSON);
            file.write(CategoriesTrendWidgetSingleton.getInstance().getCategoriesTrendJson().toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
