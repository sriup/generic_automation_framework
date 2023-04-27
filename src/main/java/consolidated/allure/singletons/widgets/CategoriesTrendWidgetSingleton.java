package consolidated.allure.singletons.widgets;

import org.json.simple.JSONArray;

public class CategoriesTrendWidgetSingleton {

    private static CategoriesTrendWidgetSingleton instance = null;

    private static JSONArray categoriesTrendJson;

    /**
     * Initializing CategoriesTrendSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static CategoriesTrendWidgetSingleton getInstance() {

        if (instance == null) {

            instance = new CategoriesTrendWidgetSingleton();

            categoriesTrendJson = new JSONArray();

        }

        return instance;
    }

    public JSONArray getCategoriesTrendJson() {
        return categoriesTrendJson;
    }

}
