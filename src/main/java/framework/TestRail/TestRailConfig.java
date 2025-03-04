package framework.TestRail;

public class TestRailConfig {
    private String testRailUrl;
    private String testRailUsername;
    private String testRailPassword;
    private int testRailProjectId;
    private int testPlanId;
    private int testRunId;


    public TestRailConfig(String testRailUrl, String testRailUsername,
                          String testRailPassword, int testRailProjectId, int testPlanId, int testRunId) {

        this.testRailUrl = testRailUrl;
         if (testRailUrl.endsWith("/")) {
             this.testRailUrl = testRailUrl.substring(0, testRailUrl.length() - 1);
         }

        this.testRailUsername = testRailUsername;
        this.testRailPassword = testRailPassword;
        this.testRailProjectId = testRailProjectId;
        this.testPlanId = testPlanId;
        this.testRunId = testRunId;
    }


    /**
     * Retrieves the TestRail URL.
     *
     * This method returns the base URL of the TestRail instance that is configured in this TestRailConfig.
     *
     * @return A string representing the TestRail URL.
     */
    public String getTestRailUrl() {
        return testRailUrl;
    }

    /**
     * Sets the TestRail URL.
     *
     * @param testRailUrl the TestRail URL
     */
    public void setTestRailUrl(String testRailUrl) {
        this.testRailUrl = testRailUrl;
    }

    /**
     * Returns the TestRail username.
     *
     * @return the TestRail username
     */
    public String getTestRailUsername() {
        return testRailUsername;
    }

    /**
     * Sets the TestRail username.
     *
     * @param testRailUsername the TestRail username
     */
    public void setTestRailUsername(String testRailUsername) {
        this.testRailUsername = testRailUsername;
    }

    /**
     * Returns the TestRail password.
     *
     * @return the TestRail password
     */
    public String getTestRailPassword() {
        return testRailPassword;
    }

    /**
     * Sets the TestRail password.
     *
     * @param testRailPassword the TestRail password
     */
    public void setTestRailPassword(String testRailPassword) {
        this.testRailPassword = testRailPassword;
    }

    /**
     * Returns the TestRail project ID.
     *
     * @return the TestRail project ID
     */
    public int getTestRailProjectId() {
        return testRailProjectId;
    }

    /**
     * Sets the TestRail project ID.
     *
     * @param testRailProjectId the TestRail project ID
     */
    public void setTestRailProjectId(int testRailProjectId) {
        this.testRailProjectId = testRailProjectId;
    }


}
