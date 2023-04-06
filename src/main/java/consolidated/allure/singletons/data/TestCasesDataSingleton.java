package consolidated.allure.singletons.data;

import framework.utilities.FolderFileUtil;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

public class TestCasesDataSingleton {

    private static TestCasesDataSingleton instance = null;

    private static JSONObject testCasesDataJson;

    /**
     * Initializing TestCasesDataSingleton instance if the instance is null
     *
     * @return instance of this singleton class
     */
    public static TestCasesDataSingleton getInstance() {

        if (instance == null) {

            instance = new TestCasesDataSingleton();

            testCasesDataJson = new JSONObject();

        }

        return instance;
    }

    public void addTestCases(String uid, JSONObject testCaseJsonObject, String testCaseStatus,
                             FolderFileUtil folderFileUtil, String oldFilePath, String newFilePath) throws IOException {

        testCasesDataJson.put(uid, testCaseJsonObject);

        File oldPath = new File(oldFilePath);
        File newPath = new File(newFilePath);
        folderFileUtil.copyFile(oldPath, newPath);


    }

    public JSONObject getTestCasesDataJson() {
        return testCasesDataJson;
    }

}
