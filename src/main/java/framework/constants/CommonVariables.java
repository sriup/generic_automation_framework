package framework.constants;

import framework.enums.LogVerboseEnums;

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

	/** The Log Level **/
	public static LogVerboseEnums logLevel;

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
	 * </table>
	 * 
	 * <tr>
	 * <td>2</td>
	 * <td>MM/dd/yyyy</td>
	 * </tr>
	 * </table>
	 * 
	 * <tr>
	 * <td>3</td>
	 * <td>MM/dd/yyyy hh:mm</td>
	 * </tr>
	 * </table>
	 * 
	 * <tr>
	 * <td>4</td>
	 * <td>hh:mm</td>
	 * </tr>
	 * </table>
	 * 
	 * <tr>
	 * <td>5</td>
	 * <td>MM/dd/yyyy h:mma</td>
	 * </tr>
	 * </table>
	 * 
	 * <tr>
	 * <td>6</td>
	 * <td>MM/dd/yyyy mm:ss a</td>
	 * </tr>
	 * </table>
	 */
	public String[] TIME_FORMATS = { "yyyy:mm:dd_hh_MM_ss", "mm:dd:yy", "MM/dd/yyyy", "MM/dd/yyyy hh:mm", "hh:mm",
			"MM/dd/yyyy h:mma", "MM/dd/yyyy mm:ss a" };

}
