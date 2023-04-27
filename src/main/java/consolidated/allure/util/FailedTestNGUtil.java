package consolidated.allure.util;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.data.FailedTestNG;
import consolidated.allure.initclass.AllureInitClass;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class FailedTestNGUtil {

    public void consolidateReport() throws Exception {

        String allureIterationFolderPath = "";

        AllureInitClass allureInitClass = new AllureInitClass(allureIterationFolderPath);

        FailedTestNG failedTestNG = new FailedTestNG(allureInitClass);

        failedTestNG.extractFailedTestCases();

        Map<String, List<String>> failedTestNGMap = failedTestNG.getFailedTestNGMap();

        System.out.println(failedTestNGMap);

        // update the behaviors.json content
        FileWriter file = null;
        try {
            file = new FileWriter(allureInitClass.getConsolidatedAllureReportDataFolder()
                    + File.separatorChar + "FailedTestNG.xml");

            for(Map.Entry<String, List<String>> currentList : failedTestNGMap.entrySet()){

                List<String> testCasesList = currentList.getValue();

                String classPackage = "<class name=\"" + currentList.getKey() + "\">\n";
                file.write(classPackage);

                file.write("<methods>\n");

                for(String currentTestCase: testCasesList){

                    file.write("<include name=\"" + currentTestCase + "\"/>\n");

                }

                file.write("</methods>\n");

                file.write("</class>\n");

            }

            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }






    }

}
