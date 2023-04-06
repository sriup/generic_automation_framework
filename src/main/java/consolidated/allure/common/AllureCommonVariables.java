package consolidated.allure.common;

import java.io.File;

public class AllureCommonVariables {

    ///////////////// ------------------ DEFAULT FILE NAMES ---------------- ///////////////////

    public static final String sd = "";

    ///////////////// ------------------ END OF DEFAULT FILE NAMES ---------------- ///////////////////

    public static int PRECONDITIONS_PASS_COUNTS_IN_BEHAVIORS;

    public static int getPreconditionsPassCountsInBehaviors(){
        return PRECONDITIONS_PASS_COUNTS_IN_BEHAVIORS;
    }

    public static void setPreconditionsPassCountsInBehaviors(int preconditionsPassCountsInBehaviors){
        PRECONDITIONS_PASS_COUNTS_IN_BEHAVIORS += preconditionsPassCountsInBehaviors;
    }

    public static final String BEHAVIORS_JSON = "behaviors.json";

    public static final String CATEGORIES_JSON = "categories.json";

    public static final String PACKAGES_JSON = "packages.json";

    public static final String TIMELINE_JSON = "timeline.json";

    public static final String SUITES_JSON = "suites.json";

    public static final String DURATION_JSON = "duration.json";

    public static final String ENVIRONMENT_JSON = "environment.json";

    public static final String SEVERITY_JSON = "severity.json";

    public static final String STATUS_CHART_JSON = "status-chart.json";

    public static final String CATEGORIES_TREND_JSON = "categories-trend.json";

    public static final String DURATION_TREND_JSON = "duration-trend.json";

    public static final String RETRY_TREND_JSON = "retry-trend.json";

    public static final String EXECUTORS_JSON = "executors.json";

    public static final String SUMMARY_JSON = "summary.json";

    public static final String FAILURES_COLLECTION_EXCEL_FILENAME = "Failures_Collection.xlsx";

    public static final String ALLURE_MAVEN_PLUGIN = "allure-maven-plugin";

    public static final String SITE = "site";

    public static final String DATA = "data";

    public static final String RUN = "Run";

    public static final String UID = "uid";

    public static final String NAME = "name";

    public static final String CHILDREN = "children";

    public static final String STATUS = "status";

    public static final String PASSED = "passed";

    public static final String FAILED = "failed";

    public static final String VALUES = "values";

    public static final String BROKEN = "broken";

    public static final String SKIPPED = "skipped";

    public static final String UNKNOWN = "unknown";

}
