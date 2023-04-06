package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;

public class DurationTrendWidgetSingleton {

    private static DurationTrendWidgetSingleton instance = null;

    private static JSONArray durationTrendJson;

    /**
     * Initializing DurationTrendWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static DurationTrendWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new DurationTrendWidgetSingleton();

            durationTrendJson = new JSONArray();

        }

        return instance;
    }

    public JSONArray getDurationTrendJson() {
        return durationTrendJson;
    }

}
