package consolidated.allure.util;

import consolidated.allure.initclass.AllureInitClass;

public class ConsolidateAllureReportUtil {

    public void generateConsolidateReport(AllureInitClass allureInitClass) throws Exception {

        allureInitClass.testCasesData.extractPassedTestCases();
        allureInitClass.testCasesData.extractFailedTestCases();


        // ************** Behaviors Data Extract **************//
        allureInitClass.behaviorsData.extractPassedBehaviorsData();
        allureInitClass.behaviorsData.extractFailedBehaviorsData();
        allureInitClass.behaviorsData.writeToFile();
        // ************** End Of Behaviors Data Extract **************//

        // ************** Categories Data Extract **************//
        allureInitClass.categoriesData.extractPassedCategoriesData();
        allureInitClass.categoriesData.extractFailedCategoriesData();
        allureInitClass.categoriesData.writeToFile();
        // ************** End Of Categories Data Extract **************//

        // ************** Packages Data Extract **************//
        allureInitClass.packagesData.extractPassedPackagesData();
        allureInitClass.packagesData.extractFailedPackagesData();
        allureInitClass.packagesData.writeToFile();
        // ************** End Of Packages Data Extract **************//

        // ************** Suites Data Extract **************//
        allureInitClass.suitesData.extractPassedSuitesData();
        allureInitClass.suitesData.extractFailedSuitesData();
        allureInitClass.suitesData.writeToFile();
        // ************** End Of Packages Data Extract **************//

        // ************** Timeline Data Extract **************//
        allureInitClass.timelineData.extractPassedTimelineData();
        allureInitClass.timelineData.extractFailedTimelineData();
        allureInitClass.timelineData.writeToFile();
        // ************** End Of Timeline Data Extract **************//


        // ************** Behaviors Widgets Extract **************//
        allureInitClass.behaviorsWidget.writeToFile();
        // ************** End Of Behaviors Widgets Extract **************//

        // ************** Categories Widgets Extract **************//
        /*baseClass.categoriesWidget.extractPassedCategories();*/
        allureInitClass.categoriesWidget.writeToFile();
        // ************** End Of Categories Widgets Extract **************//

        // ************** Duration Widgets Extract **************//
        allureInitClass.durationWidget.extractPassedTestCaseDuration();
        allureInitClass.durationWidget.extractFailedTestCaseDuration();
        allureInitClass.durationWidget.writeToFile();
        // ************** End Of Duration Widgets Extract **************//

        // ************** Environment Widgets Extract **************//
        allureInitClass.environmentWidget.fetchEnvironmentDetails();
        allureInitClass.environmentWidget.writeToFile();
        // ************** End Of Environment Widgets Extract **************//

        // ************** Severity Widgets Extract **************//
        allureInitClass.severityWidget.extractPassedTestCaseSeverity();
        allureInitClass.severityWidget.extractFailedTestCaseSeverity();
        allureInitClass.severityWidget.writeToFile();
        // ************** End Of Severity Widgets Extract **************//

        // ************** Status-Chart Widgets Extract **************//
        allureInitClass.statusChartWidget.extractPassedTestCaseStatusChart();
        allureInitClass.statusChartWidget.extractFailedTestCaseStatusChart();
        allureInitClass.statusChartWidget.writeToFile();
        // ************** End Of Status-Chart Widgets Extract **************//

        // ************** Categories-Trend Widgets Extract **************//
        allureInitClass.categoriesTrendWidget.writeToFile();
        // ************** End Of Categories-Trend Widgets Extract **************//

        // ************** Duration-Trend Widgets Extract **************//
        allureInitClass.durationTrendWidget.writeToFile();
        // ************** End Of Duration-Trend Widgets Extract **************//

        // ************** Retry-Trend Widgets Extract **************//
        allureInitClass.retryTrendWidget.writeToFile();
        // ************** End Of Retry-Trend Widgets Extract **************//

        // ************** Executors Widgets Extract **************//
        allureInitClass.executorsWidget.fetchExecutorDetails();
        allureInitClass.executorsWidget.writeToFile();
        // ************** End Of Executors Widgets Extract **************//

        // ************** Suites Widgets Extract **************//
        allureInitClass.suitesWidget.writeToFile();
        // ************** End Of Suites Widgets Extract **************//

        // ************** Summary Widgets Extract **************//
        allureInitClass.summaryWidget.extractTimeDetails();
        allureInitClass.summaryWidget.writeToFile();
        // ************** End Of Summary Widgets Extract **************//

    }

}
