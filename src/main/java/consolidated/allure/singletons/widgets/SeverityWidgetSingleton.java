package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SeverityWidgetSingleton {

    private static SeverityWidgetSingleton instance = null;

    private static JSONArray severityJsonArray;



    /**
     * Initializing SeverityWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static SeverityWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new SeverityWidgetSingleton();

            severityJsonArray = new JSONArray();

        }

        return instance;
    }

    public void addTestCaseSeverity(JSONObject testCaseJsonObject){

        severityJsonArray.add(testCaseJsonObject);

    }

    public JSONArray getSeverityJsonArray() {
        return severityJsonArray;
    }

}
