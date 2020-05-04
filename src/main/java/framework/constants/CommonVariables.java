package framework.constants;

/**
 * All the common variables related to the framework.
 */
public class CommonVariables {
	
	/**
	 * The browser Name
	 * This variable will be updated based on the value provided as part of<br> 
	 * maven command. Make sure you include "-DbrowserName" to set the browser name.<br>
	 * eg : <b>-DbrowserName="chrome"</b>
	 * */
	
    public static String BROWSER_SELECT = System.getProperty("browserName"); 
    
	/** The max timeout. */
	public static int MAX_TIMEOUT = 120;

	/** The medium timeout. */
	public static int MED_TIMEOUT = 30;

	/** The minimum timeout. */
	public static int MIN_TIMEOUT = 5;

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
	 * </table>
	 */
	public static String[] TIME_FORMATS = { "yyyy:MM:dd_hh_mm_ss", "mm:dd:yy", "MM/dd/yyyy", "MM/dd/yyyy hh:mm", "hh:mm",
			"MM/dd/yyyy h:mma", "MM/dd/yyyy mm:ss a", "MM_dd_yyyy_hh_mm_ss"};

}
