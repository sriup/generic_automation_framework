package framework.constants;

import java.io.File;
import java.util.HashMap;

/**
 * All the common variables related to the framework.
 */
public class CommonVariables {


    /**
     * The browser Name This variable will be updated based on the value provided as
     * part of<br>
     * maven command. Make sure you include
     * <font color="blue">"-Dbrowser"</font> to set the browser name.<br>
     * eg : <b>-Dbrowser="chrome"</b>
     */

    public static final String BROWSER_SELECT = System.getProperty("browser");
    public static final String EMAIL_RECIPIENTS="recipients";
    public static final String EMAIL_SUBJECT = "subject";
    public static final String EMAIL_BODY = "body";
    public static final String EMAIL_ATTACHMENT_FILE_PATH = "attachment";
    public static final String PROJECT_FOLDER_PATH = System.getProperty("user.dir");
    public static final String DOCKER_FOLDER_PATH = new File(PROJECT_FOLDER_PATH).getParentFile().getAbsolutePath() + File.separatorChar + "icits-docker";

    // Max SeleniumBox browser instance timeout
    public static int MAX_SBOX_BROWSER_TIMEOUT = 40000;
    public static final String SBOX_HUB_URL = (System.getenv("SBOX_URL") != null && !System.getenv("SBOX_URL").isEmpty()) ? System.getenv("SBOX_URL") : System.getProperty("SBOX_URL");

    public static final String  SBOX_TOKEN = (System.getenv("SBOX_TOKEN") != null) ? System.getenv("SBOX_TOKEN") : System.getProperty("SBOX_TOKEN");
    public static final String EXEC_PLATFORM = (System.getProperty("exec_platform") != null && !System.getProperty("exec_platform").isEmpty())? System.getProperty("exec_platform"):"Local";
    public static final String HOST_ADDRESS = CommonVariables.EXEC_PLATFORM.equals("docker")? "http://localhost:4444": SBOX_HUB_URL;

    public static final boolean IS_RUNNING_ON_SBOX = CommonVariables.EXEC_PLATFORM.equals("sbox");

	/* The implicit wait time */
    //public static int IMPLICIT_WAIT = 15;

    /**
     * The max timeout.
     */
    public static int MAX_TIMEOUT = 120;

    /**
     * The medium timeout.
     */
    public static final int MED_TIMEOUT = 30;

    /**
     * The minimum timeout.
     */
    public static final int MIN_TIMEOUT = 5;

    /**
     * The no timeout
     */
    public static final int NO_TIMEOUT = 0;

    /**
     * List of available time formats.
     * <table border=1>
     * <tr>
     * <th>Index</th>
     * <th>Format</th>
     * </tr>
     *
     * <tr>
     * <td>0</td>
     * <td>yyyy:mm:dd_hh_MM_ss</td>
     * </tr>
     *
     * <tr>
     * <td>1</td>
     * <td>mm:dd:yy</td>
     * </tr>
     *
     * <tr>
     * <td>2</td>
     * <td>MM/dd/yyyy</td>
     * </tr>
     *
     * <tr>
     * <td>3</td>
     * <td>MM/dd/yyyy hh:mm</td>
     * </tr>
     *
     * <tr>
     * <td>4</td>
     * <td>hh:mm</td>
     * </tr>
     *
     * <tr>
     * <td>5</td>
     * <td>MM/dd/yyyy h:mma</td>
     * </tr>
     *
     * <tr>
     * <td>6</td>
     * <td>MM/dd/yyyy mm:ss a</td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>MM_dd_yyyy_hh_mm_ss</td>
     * </tr>
     * <tr>
     * <td>8</td>
     * <td>MM_dd_yyyy_hh_mm_ss_SS</td>
     * </tr>
     * <tr>
     *     <td>9</td>
     *     <td>MM/dd/yyyy HH:mm:ss</td>
     * </tr>
     * </table>
     */
    public static final String[] TIME_FORMATS = {"yyyy:MM:dd_hh_mm_ss", "mm:dd:yy", "MM/dd/yyyy", "MM/dd/yyyy hh:mm",
            "hh:mm", "MM/dd/yyyy h:mma", "MM/dd/yyyy mm:ss a", "MM_dd_yyyy_hh_mm_ss", "MM_dd_yyyy_hh_mm_ss_SS",
            "MM/dd/yyyy HH:mm:ss"};

    /**
     * store navigated urls
     */
    public static HashMap<String, String> navigatedURLs = new HashMap<>();

    /**
     * store launched browsers information
     */
    public static HashMap<String, String> launchedBrowsers = new HashMap<>();

    public static boolean captureFullPageOnSoftAssert = true;
}
