package consolidated.allure.singletons;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.initclass.AllureInitClass;
import framework.utilities.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionRunDetailsSingleton {

    private static ExecutionRunDetailsSingleton instance = null;

    private static Map<String, Map<String, String>> executionRunDetailsMap;



    /**
     * Initializing ExecutionRunDetailsSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static ExecutionRunDetailsSingleton getInstance(AllureInitClass allureInitClass) throws Exception {

        if (instance == null) {

            instance = new ExecutionRunDetailsSingleton();

            executionRunDetailsMap = new HashMap<>();

            fetchExecutionRunDetails(allureInitClass);

        }

        return instance;
    }

    public static void fetchExecutionRunDetails(AllureInitClass allureInitClass) throws Exception {

        ExcelUtil excelUtil = new ExcelUtil(allureInitClass.logAccess);

        excelUtil.openWorkBook(allureInitClass.getAllureIterationPath(),
                AllureCommonVariables.FAILURES_COLLECTION_EXCEL_FILENAME);

        List<String> sheetNames = excelUtil.getSheetNames(excelUtil.getWb());

        for (String currentSheetName: sheetNames){

            int sheetIndex = excelUtil.getSheetIndex(currentSheetName);

            List<Row> rowsList = excelUtil.getRows(sheetIndex, 1, 600, true);

            for (Row currentRow: rowsList){

                String testCaseId = excelUtil.getCellData(currentSheetName, currentRow, "Test Case ID");
                String status = excelUtil.getCellData(currentSheetName, currentRow, AllureCommonVariables.STATUS);

                if(status.equalsIgnoreCase(AllureCommonVariables.FAILED) || status.equalsIgnoreCase(AllureCommonVariables.SKIPPED)){
                    Map<String, String> currentExecutionRunMap = (executionRunDetailsMap.get(currentSheetName) != null) ? executionRunDetailsMap.get(currentSheetName) : new HashMap<>();

                    currentExecutionRunMap.put(testCaseId, status);

                    executionRunDetailsMap.put(currentSheetName, currentExecutionRunMap);
                }

            }

        }



    }

    public Map<String, Map<String, String>> getExecutionRunDetailsMap(){
        return executionRunDetailsMap;
    }

}
