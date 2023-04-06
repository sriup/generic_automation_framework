package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ExecutorsWidgetSingleton {

    private static ExecutorsWidgetSingleton instance = null;

    private static JSONArray executorDetailsJsonArray;



    /**
     * Initializing ExecutorsWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static ExecutorsWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new ExecutorsWidgetSingleton();

            executorDetailsJsonArray = new JSONArray();

        }

        return instance;
    }

    public void addExecutorDetails(JSONObject executorDetailsJsonObject){

        executorDetailsJsonArray.add(executorDetailsJsonObject);

    }

    public JSONArray getExecutorDetailsJsonArray() {
        return executorDetailsJsonArray;
    }

}
