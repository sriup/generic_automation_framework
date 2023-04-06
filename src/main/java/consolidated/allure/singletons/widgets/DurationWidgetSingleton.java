package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DurationWidgetSingleton {

    private static DurationWidgetSingleton instance = null;

    private static JSONArray durationJsonArray;



    /**
     * Initializing DurationWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static DurationWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new DurationWidgetSingleton();

            durationJsonArray = new JSONArray();

        }

        return instance;
    }

    public void addTestCaseDuration(JSONObject testCaseJsonObject){

        durationJsonArray.add(testCaseJsonObject);

    }

    public JSONArray getDurationJsonArray() {
        return durationJsonArray;
    }

}
