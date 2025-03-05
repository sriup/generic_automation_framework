package framework.TestRail;

public interface TestRailApiEndPoints {

    String GET_STATUSES = "/get_statuses";
    String GET_PROJECTS = "/get_projects";
    String GET_OPEN_PLANS = "/get_plans/{project_id}&is_completed=0";
    String GET_PLAN = "/get_plan/{plan_id}";
    String GET_TESTS_BY_TEST_RUN_ID = "/get_tests/{test_run_id}";
    String POST_ADD_RESULTS_FOR_CASES_BY_TEST_RUN_ID = "/add_results_for_cases/{test_run_id}";
    String POST_ATTACHMENTS_TO_RESULT_BY_RESULT_ID = "/add_attachment_to_result/{result_id}"; // POST_ADD_
}
