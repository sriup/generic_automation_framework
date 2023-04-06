package consolidated.allure.data;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.initclass.AllureInitClass;
import framework.commonfunctions.CommonFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

public class Attachments extends CommonFunctions {

    private AllureInitClass allureInitClass;

    public Attachments(AllureInitClass allureInitClass) {
        super(null, allureInitClass.logAccess, null );

        this.allureInitClass = allureInitClass;

    }

    public void parseAttachments(String currentTestCaseFolderPath, JSONObject testCaseJsonObject) throws Exception {

        String currentAttachmentFolderPath = currentTestCaseFolderPath.replace("test-cases",
                "attachments");

        JSONObject testStageJsonObject = (JSONObject) testCaseJsonObject.get("testStage");

        JSONArray testStageAttachmentsJsonArray = (JSONArray) testStageJsonObject.get("attachments");

        extractAttachment(testStageAttachmentsJsonArray, currentAttachmentFolderPath);

        extractStepAttachments(testStageJsonObject, currentAttachmentFolderPath);


    }

    private void extractStepAttachments(JSONObject currentJsonObject, String currentAttachmentFolderPath) throws Exception {

        if(currentJsonObject.containsKey("steps")){

            JSONArray stepsJsonArray = (JSONArray) currentJsonObject.get("steps");

            for(int currentStepIndex = 0; currentStepIndex < stepsJsonArray.size();
                currentStepIndex++){

                JSONObject currentStepJsonObject = (JSONObject) stepsJsonArray.get(currentStepIndex);

                JSONArray stepAttachmentsJsonArray = (JSONArray) currentStepJsonObject.get("attachments");

                extractAttachment(stepAttachmentsJsonArray, currentAttachmentFolderPath);

                extractStepAttachments(currentStepJsonObject, currentAttachmentFolderPath);
            }

        }

    }

    private void extractAttachment(JSONArray attachmentsJsonArray, String currentAttachmentFolderPath) throws Exception {

        for(int currentAttachmentIndex = 0; currentAttachmentIndex < attachmentsJsonArray.size();
            currentAttachmentIndex++){

            JSONObject currentAttachmentJsonObject = (JSONObject) attachmentsJsonArray.get(currentAttachmentIndex);

            String currentSourceFileName = (String) currentAttachmentJsonObject.get("source");

            String currentAttachmentFileName = currentAttachmentFolderPath + File.separatorChar + currentSourceFileName;

            String newAttachmentFileName = allureInitClass.getConsolidatedAllureReportDataAttachmentsFolder() +
                    File.separatorChar + currentSourceFileName;

            File oldPath = new File(currentAttachmentFileName);
            File newPath = new File(newAttachmentFileName);

            getFolderFileUtil().copyFile(oldPath, newPath);

        }

    }

}
