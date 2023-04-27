package consolidated.allure.singletons.widgets;

import consolidated.allure.common.AllureCommonVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EnvironmentWidgetSingleton {

    private static EnvironmentWidgetSingleton instance = null;

    private static JSONArray environmentDetailsJsonArray;



    /**
     * Initializing EnvironmentWidgetSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static EnvironmentWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new EnvironmentWidgetSingleton();

            environmentDetailsJsonArray = new JSONArray();

        }

        return instance;
    }

    public void addEnvironmentDetails(String name, String values){

        boolean isNameFound = false;

        for(int currentIndex = 0; currentIndex < environmentDetailsJsonArray.size(); currentIndex++){

            JSONObject currentEnvironmentDetailsJsonObject = (JSONObject) environmentDetailsJsonArray.get(currentIndex);

            String currentName = (String) currentEnvironmentDetailsJsonObject.get(AllureCommonVariables.NAME);

            if(currentName.equalsIgnoreCase(name)){

                JSONArray currentValuesJsonArray = (JSONArray) currentEnvironmentDetailsJsonObject.get(AllureCommonVariables.VALUES);

                boolean isValueFound = false;

                for (int currentValuesIndex = 0; currentValuesIndex < currentValuesJsonArray.size(); currentValuesIndex++){

                    String currentValue = (String) currentValuesJsonArray.get(currentValuesIndex);

                    isValueFound = (currentValue.equalsIgnoreCase(values));

                }

                if(!isValueFound){

                    currentValuesJsonArray.add(values);

                }

                isNameFound = true;

                break;

            }


        }

        if(!isNameFound){

            JSONArray valuesJsonArray = new JSONArray();
            valuesJsonArray.add(values);

            JSONObject environmentDetailsJsonObject = new JSONObject();
            environmentDetailsJsonObject.put(AllureCommonVariables.NAME, name);
            environmentDetailsJsonObject.put(AllureCommonVariables.VALUES, valuesJsonArray);

            environmentDetailsJsonArray.add(environmentDetailsJsonObject);

        }

    }

    public JSONArray getEnvironmentDetailsJsonArray() {
        return environmentDetailsJsonArray;
    }

}
