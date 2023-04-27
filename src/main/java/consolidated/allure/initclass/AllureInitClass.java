package consolidated.allure.initclass;

import consolidated.allure.common.AllureCommonVariables;
import consolidated.allure.data.*;
import consolidated.allure.singletons.ExecutionRunDetailsSingleton;
import consolidated.allure.widgets.*;
import framework.enums.LogVerboseEnums;
import framework.logs.LogAccess;
import framework.utilities.FolderFileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class AllureInitClass {

    public BehaviorsWidget behaviorsWidget;
    public FolderFileUtil folderFileUtil;
    public CategoriesWidget categoriesWidget;
    public TestCasesData testCasesData;
    public BahaviorsData behaviorsData;
    public CategoriesData categoriesData;
    public PackagesData packagesData;
    public TimelineData timelineData;
    public SuitesData suitesData;
    public DurationWidget durationWidget;
    public EnvironmentWidget environmentWidget;
    public SeverityWidget severityWidget;
    public StatusChartWidget statusChartWidget;
    public CategoriesTrendWidget categoriesTrendWidget;
    public DurationTrendWidget durationTrendWidget;
    public RetryTrendWidget retryTrendWidget;
    public ExecutorsWidget executorsWidget;
    public SuitesWidget suitesWidget;
    public SummaryWidget summaryWidget;
    public Attachments attachments;

    public LogAccess logAccess;


    /** Provide the Iterations folder path to consolidate the allure report */
    private String allureIterationPath;
    private String consolidatedAllureReportFolder;
    private String consolidatedAllureReportDataFolder;
    private String consolidatedAllureReportDataAttachmentsFolder;
    private String consolidatedAllureReportDataTestcasesFolder;

    private String consolidatedAllureReportWidgetsFolder;
    private String consolidatedAllureReportPluginsFolder;
    private String consolidatedAllureReportPluginsBehaviorsFolder;
    private String consolidatedAllureReportPluginsJiraFolder;
    private String consolidatedAllureReportPluginsPackagesFolder;
    private String consolidatedAllureReportPluginsScreenDiffFolder;

    public String getAllureIterationPath() {
        return allureIterationPath;
    }

    public String getConsolidatedAllureReportFolder() {
        return consolidatedAllureReportFolder;
    }

    public String getConsolidatedAllureReportDataFolder() {
        return consolidatedAllureReportDataFolder;
    }

    public String getConsolidatedAllureReportDataAttachmentsFolder() {
        return consolidatedAllureReportDataAttachmentsFolder;
    }

    public String getConsolidatedAllureReportDataTestcasesFolder() {
        return consolidatedAllureReportDataTestcasesFolder;
    }

    public String getConsolidatedAllureReportWidgetsFolder() {
        return consolidatedAllureReportWidgetsFolder;
    }

    public String getConsolidatedAllureReportPluginsFolder() {
        return consolidatedAllureReportPluginsFolder;
    }

    public String getConsolidatedAllureReportPluginsBehaviorsFolder() {
        return consolidatedAllureReportPluginsBehaviorsFolder;
    }

    public String getConsolidatedAllureReportPluginsJiraFolder() {
        return consolidatedAllureReportPluginsJiraFolder;
    }

    public String getConsolidatedAllureReportPluginsPackagesFolder() {
        return consolidatedAllureReportPluginsPackagesFolder;
    }

    public String getConsolidatedAllureReportPluginsScreenDiffFolder() {
        return consolidatedAllureReportPluginsScreenDiffFolder;
    }

    public void setVariables(){

        consolidatedAllureReportFolder = getAllureIterationPath() +
                File.separatorChar + "ConsolidatedReport" + File.separatorChar + "allure-report";

        consolidatedAllureReportDataFolder = getConsolidatedAllureReportFolder() +
                File.separatorChar + "data";

        consolidatedAllureReportDataAttachmentsFolder = getConsolidatedAllureReportDataFolder() + File.separatorChar + "attachments";

        consolidatedAllureReportDataTestcasesFolder = getConsolidatedAllureReportDataFolder() + File.separatorChar + "test-cases";

        consolidatedAllureReportWidgetsFolder = getConsolidatedAllureReportFolder() + File.separatorChar + "widgets";

        consolidatedAllureReportPluginsFolder = getConsolidatedAllureReportFolder() + File.separatorChar + "plugins";

        consolidatedAllureReportPluginsBehaviorsFolder = getConsolidatedAllureReportPluginsFolder() + File.separatorChar + "behaviors";

        consolidatedAllureReportPluginsJiraFolder = getConsolidatedAllureReportPluginsFolder() + File.separatorChar + "jira";

        consolidatedAllureReportPluginsPackagesFolder = getConsolidatedAllureReportPluginsFolder() + File.separatorChar + "packages";

        consolidatedAllureReportPluginsScreenDiffFolder = getConsolidatedAllureReportPluginsFolder() + File.separatorChar + "screen-diff";

    }

    public AllureInitClass(String allureIterationPath) throws Exception {

        logAccess = new LogAccess("ConsolidateAllureReport", LogVerboseEnums.DEBUG);

        folderFileUtil = new FolderFileUtil(logAccess);

        this.allureIterationPath = allureIterationPath;
        setVariables();

        createFolders();

        copyDefaultFiles();

        initWidgets();

        initDataClasses();

        ExecutionRunDetailsSingleton.getInstance(this);

    }

    /**
     * Initializes the widgets classes
     */
    private void initWidgets(){

        behaviorsWidget = new BehaviorsWidget(this);
        categoriesWidget = new CategoriesWidget(this);
        durationWidget = new DurationWidget(this);
        environmentWidget = new EnvironmentWidget(this);
        severityWidget = new SeverityWidget(this);
        statusChartWidget = new StatusChartWidget(this);
        categoriesTrendWidget = new CategoriesTrendWidget(this);
        durationTrendWidget = new DurationTrendWidget(this);
        retryTrendWidget = new RetryTrendWidget(this);
        executorsWidget = new ExecutorsWidget(this);
        suitesWidget = new SuitesWidget(this);
        summaryWidget = new SummaryWidget(this);

    }

    /**
     * Initializes the data classes
     */
    private void initDataClasses(){
        testCasesData = new TestCasesData(this);
        behaviorsData = new BahaviorsData(this);
        categoriesData = new CategoriesData(this);
        packagesData = new PackagesData(this);
        timelineData = new TimelineData(this);
        suitesData = new SuitesData(this);
        attachments = new Attachments(this);
    }

    /**
     * Creates the folders if it is not present
     */
    private void createFolders() {
        folderFileUtil.createFolder(getConsolidatedAllureReportDataAttachmentsFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportDataTestcasesFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportWidgetsFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportPluginsBehaviorsFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportPluginsJiraFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportPluginsPackagesFolder());
        folderFileUtil.createFolder(getConsolidatedAllureReportPluginsScreenDiffFolder());
        
    }

    /**
     * Copies the resource files to the destination folder
     *
     * @param sourcePath    the source path
     * @param destinationPath   the destination path
     * @throws Exception    the exception
     */
    private void copyResourceFiles(String sourcePath, String destinationPath)
            throws Exception {

        URL sourceUrl = getClass().getResource(sourcePath);
        File destination = new File(destinationPath);

        FileUtils.copyURLToFile(sourceUrl, destination);

    }

    /**
     * Copies the default files to the Consolidated folder
     * @throws Exception    the exception
     */
    private void copyDefaultFiles() throws Exception {

        String oldBehaviorsIndexFilePath = "/plugins/behaviors/index.js";
        String newBehaviorsIndexFilePath = getConsolidatedAllureReportPluginsBehaviorsFolder() +
                File.separatorChar + "index.js";

        copyResourceFiles(oldBehaviorsIndexFilePath, newBehaviorsIndexFilePath);


        String oldPackagesIndexFilePath = "/plugins/packages/index.js";
        String newPackagesIndexFilePath = getConsolidatedAllureReportPluginsPackagesFolder() +
                File.separatorChar + "index.js";

        copyResourceFiles(oldPackagesIndexFilePath, newPackagesIndexFilePath);



        String oldScreenDiffIndexFilePath = "/plugins/screen-diff/index.js";
        String newScreenDiffIndexFilePath = getConsolidatedAllureReportPluginsScreenDiffFolder() +
                File.separatorChar + "index.js";

        copyResourceFiles(oldScreenDiffIndexFilePath, newScreenDiffIndexFilePath);



        String oldScreenDiffStylesFilePath = "/plugins/screen-diff/styles.css";
        String newScreenDiffStylesFilePath = getConsolidatedAllureReportPluginsScreenDiffFolder() +
                        File.separatorChar + "styles.css";

        copyResourceFiles(oldScreenDiffStylesFilePath, newScreenDiffStylesFilePath);



        String oldAppFilePath = "/app.js";
        String newAppFilePath = this.getConsolidatedAllureReportFolder() +
                        File.separatorChar + "app.js";

        copyResourceFiles(oldAppFilePath, newAppFilePath);



        String oldFaviconFilePath = "/favicon.ico";
        String newFaviconFilePath = this.getConsolidatedAllureReportFolder() +
                        File.separatorChar + "favicon.ico";

        copyResourceFiles(oldFaviconFilePath, newFaviconFilePath);



        String oldIndexHtmlFilePath = "/index.html";
        String newIndexHtmlFilePath = this.getConsolidatedAllureReportFolder() +
                        File.separatorChar + "index.html";

        copyResourceFiles(oldIndexHtmlFilePath, newIndexHtmlFilePath);



        String oldStyleCssFilePath = "/styles.css";
        String newStyleCssFilePath = this.getConsolidatedAllureReportFolder() +
                        File.separatorChar + "styles.css";

        copyResourceFiles(oldStyleCssFilePath, newStyleCssFilePath);
        
    }

    public int getIterationFoldersSize(){

        int numberOfIterations = 0;

        List<String> allFilesOrFoldersNames = folderFileUtil.getAllFileNames(getAllureIterationPath());

        for (String currentFilesOrFoldersName : allFilesOrFoldersNames) {

            if (currentFilesOrFoldersName.contains(AllureCommonVariables.RUN)) {
                numberOfIterations++;
            }
        }

        System.out.println("The number of Iterations are :- '" + numberOfIterations + "'");

        return numberOfIterations;
    }
    
}
