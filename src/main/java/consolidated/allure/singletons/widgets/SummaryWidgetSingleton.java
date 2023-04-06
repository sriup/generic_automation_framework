package consolidated.allure.singletons.widgets;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SummaryWidgetSingleton {

    private static SummaryWidgetSingleton instance = null;

    private static JSONObject summaryJsonObject;

    /**
     * Initializing SummaryWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static SummaryWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new SummaryWidgetSingleton();

            summaryJsonObject = new JSONObject();

            // These are the default values which we are setting it.
            summaryJsonObject.put("reportName", "Consolidated Allure Report");
            summaryJsonObject.put("testRuns", new JSONArray());
            // **** End of default values set *********//

            JSONObject statisticJsonObject = new JSONObject();
            statisticJsonObject.put(AllureCommonVariables.FAILED, 0);
            statisticJsonObject.put(AllureCommonVariables.BROKEN, 0);
            statisticJsonObject.put(AllureCommonVariables.SKIPPED, 0);
            statisticJsonObject.put(AllureCommonVariables.PASSED, 0);
            statisticJsonObject.put(AllureCommonVariables.UNKNOWN, 0);
            statisticJsonObject.put("total", 0);

            summaryJsonObject.put("statistic", statisticJsonObject);

            JSONObject timeJsonObject = new JSONObject();
            timeJsonObject.put("start", 0l);
            timeJsonObject.put("stop", 0l);
            timeJsonObject.put("duration", 0l);
            timeJsonObject.put("minDuration", 0);
            timeJsonObject.put("maxDuration", 0);
            timeJsonObject.put("sumDuration", 0);

            summaryJsonObject.put("time", timeJsonObject);

        }

        return instance;
    }

    public void updateSummaryStatistic(int failed, int broken, int skipped,
                                 int passed, int unknown){

        JSONObject currentStatisticJsonObject = (JSONObject) summaryJsonObject.get("statistic");

        int increasedFailed = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.FAILED).toString()) + failed;
        int increasedBroken = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.BROKEN).toString()) + broken;
        int increasedSkipped = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.SKIPPED).toString()) + skipped;
        int increasedPassed = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.PASSED).toString()) + passed;
        int increasedUnknown = Integer.parseInt(currentStatisticJsonObject.get(AllureCommonVariables.UNKNOWN).toString()) + unknown;


        currentStatisticJsonObject.put(AllureCommonVariables.FAILED, increasedFailed);
        currentStatisticJsonObject.put(AllureCommonVariables.BROKEN, increasedBroken);
        currentStatisticJsonObject.put(AllureCommonVariables.SKIPPED, increasedSkipped);
        currentStatisticJsonObject.put(AllureCommonVariables.PASSED, increasedPassed);
        currentStatisticJsonObject.put(AllureCommonVariables.UNKNOWN, increasedUnknown);

        int total = increasedFailed + increasedBroken + increasedSkipped + increasedPassed + increasedUnknown;

        currentStatisticJsonObject.put("total", total);

        summaryJsonObject.put("statistic", currentStatisticJsonObject);

    }

    public void updateSummaryTime(long start, long stop){

        JSONObject currentTimeJsonObject = (JSONObject) summaryJsonObject.get("time");

        if(start != 0){
            currentTimeJsonObject.put("start", start);
        }

        currentTimeJsonObject.put("stop", stop);

        long currentStart = (Long) currentTimeJsonObject.get("start");

        long duration = stop - currentStart;
        currentTimeJsonObject.put("duration", duration);

        summaryJsonObject.put("time", currentTimeJsonObject);

    }

    public JSONObject getSummaryJsonObject() {
        return summaryJsonObject;
    }

}
