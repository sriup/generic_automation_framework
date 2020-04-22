package framework.constants;

/**
 * All the common variables related to the framework.
 */
public class CommonVariables {

	/** The browser select. */
	public static String BROWSER_SELECT = System.getProperty("browserType");
	
	/** The max timeout. */
	public static int MAX_TIMEOUT = 120;
	
	/** The medium timeout. */
	public static int MED_TIMEOUT = 30;
	
	/** The minimum timeout. */
	public static int MIN_TIMEOUT = 5;

	/**
	 * List of available time formats.
	 *<table border=1>
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
	 * </table>
	 */
	public String[] TIME_FORMATS = { "yyyy:mm:dd_hh_MM_ss", "mm:dd:yy" };

}

