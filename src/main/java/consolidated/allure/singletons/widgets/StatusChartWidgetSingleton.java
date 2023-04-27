package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StatusChartWidgetSingleton {

    private static StatusChartWidgetSingleton instance = null;

    private static JSONArray statusChartJsonArray;



    /**
     * Initializing StatusChartWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static StatusChartWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new StatusChartWidgetSingleton();

            statusChartJsonArray = new JSONArray();

        }

        return instance;
    }

    public void addTestCaseStatusChart(JSONObject testCaseJsonObject){

        statusChartJsonArray.add(testCaseJsonObject);

    }

    public JSONArray getStatusChartJsonArray() {
        return statusChartJsonArray;
    }

}
