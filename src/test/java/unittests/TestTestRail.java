package unittests;

import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.TestRailUtil;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

public class TestTestRail {

    @Test
    public void testTestRail() throws Exception {
        System.out.println("Test TestRail");

        LogAccess logAccess = new LogAccess("TestRailTest", LogVerboseEnums.INFO);
        TestRailUtil testRailUtil = new TestRailUtil(logAccess);

        String usersDataJsonFilePath = new File("").getAbsolutePath() + File.separatorChar + "Output" + File.separatorChar + "TestCaseDetails.json";
        String evidencesFolderPath = new File("").getAbsolutePath() + File.separatorChar + "Output";

        // update the test cases status
        Response response = testRailUtil.updateMultipleTestStatuses(usersDataJsonFilePath);

        // add attachments to the tests
        testRailUtil.addAttachmentsToTests(response, evidencesFolderPath);
    }
}