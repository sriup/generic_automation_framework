package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;

public class RetryTrendWidgetSingleton {

    private static RetryTrendWidgetSingleton instance = null;

    private static JSONArray retryTrendJson;

    /**
     * Initializing RetryTrendWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static RetryTrendWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new RetryTrendWidgetSingleton();

            retryTrendJson = new JSONArray();

        }

        return instance;
    }

    public JSONArray getRetryTrendJson() {
        return retryTrendJson;
    }

}
