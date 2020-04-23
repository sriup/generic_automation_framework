package framework.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import framework.logs.LogAccess;

/**
 * The Class DateTimeUtils.
 */
public class DateTimeUtils {

	private LogAccess logAccess;

	public DateTimeUtils(LogAccess logAccess) {
		this.logAccess = logAccess;
	}

	/**
	 * Fetching the Current Date in the "MM/dd/yyyy" format
	 * @return The Current Date
	 */
	public String getCurrentDate() {

		String currentDate = getCurrentDate("MM/dd/yyyy");

		this.logAccess.getLogger().info("currentDate :- " + currentDate);

		return currentDate;
	}

	/**
	 * Fetching the Current Date in the custom date format
	 * @param dateFormat The Custom date format
	 * @return The Current Date 
	 */
	public String getCurrentDate(String dateFormat) {
		Date date = new Date();
		String currentDate = new SimpleDateFormat(dateFormat).format(date);

		this.logAccess.getLogger().info("currentDate :- " + currentDate);

		return currentDate;
	}

	/**
	 * Fetching the Yesterday's Date in the "MM/dd/yyyy" format
	 * @return The Yesterday's date 
	 */
	public String getYesterdayDate() {

		String yesterdayDate = getPreviousDate(1, "MM/dd/yyyy");

		this.logAccess.getLogger().info("yesterdayDate :- " + yesterdayDate);

		return yesterdayDate;
	}

	/**
	 * Fetching the Previous Date by passing number of past days in the "MM/dd/yyyy" format
	 * @param noOfDays Number of past days
	 * @return Previous Date
	 */
	public String getPreviousDate(int noOfDays) {

		String previousDate = getPreviousDate(noOfDays, "MM/dd/yyyy");

		this.logAccess.getLogger().info("previousDate :- " + previousDate);

		return previousDate;
	}

	/**
	 * Fetching the Previous Date by passing number of past days and custom date format
	 * @param noOfDays Number of past days
	 * @param dateFormat Custom date format
	 * @return Previous Date
	 */
	public String getPreviousDate(int noOfDays, String dateFormat) {
		
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -noOfDays);
		
		String previousDate = simpleDateFormat.format(cal.getTime());
		
		this.logAccess.getLogger().info("previousDate :- " + previousDate);

		return previousDate;
	}

	/**
	 * Fetching the Next Date in the "MM/dd/yyyy" format
	 * @return Next Date
	 */
	public String getNextDate() {

		String nextDate = getFutureDate(1, "MM/dd/yyyy");
		
		this.logAccess.getLogger().info("nextDate :- " + nextDate);

		return nextDate;
	}

	/**
	 * Fetching the Future Date by passing number of future days
	 * @param noOfDays Number of future days
	 * @return Future Date
	 */
	public String getFutureDate(int noOfDays) {

		String futureDate = getFutureDate(noOfDays, "MM/dd/yyyy");
		
		this.logAccess.getLogger().info("futureDate :- " + futureDate);

		return futureDate;
	}

	/**
	 * Fetching the Future Date by passing number of future days and custom date format
	 * @param noOfDays Number of future days
	 * @param dateFormat Custom date format
	 * @return Future Date
	 */
	public String getFutureDate(int noOfDays, String dateFormat) {
		
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +noOfDays);
		
		String futureDate = simpleDateFormat.format(cal.getTime());
				
		this.logAccess.getLogger().info("futureDate :- " + futureDate);
		
		return futureDate;
	}

	/**
	 * Fetching the current time in the "hh:mm" format 
	 * @return Current time
	 * @throws Exception The Exception
	 */
	public String getCurrentTime() throws Exception {
		
		String time = getCurrentTime("hh:mm");
		
		this.logAccess.getLogger().info("time :- " + time);
		
		return time;
	}

	/**
	 * Fetching the current time in the custom date format
	 * @param dateFormat Custom date format
	 * @return Current time
	 * @throws Exception The Exception
	 */
	public String getCurrentTime(String dateFormat) throws Exception {
		
		Calendar mycalendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String time = formatter.format(mycalendar.getTime());
		
		this.logAccess.getLogger().info("time :- " + time);
		
		return time;
	}

	/**
	 * Fetching the Current DateTime in the "MM/dd/yyyy hh:mm" format
	 * @return Current DateTime
	 * @throws Exception The Exception
	 */
	public String getCurrentDateTime() throws Exception {
		
		String dateTime = getCurrentDateTime("MM/dd/yyyy hh:mm");
		
		this.logAccess.getLogger().info("dateTime :- " + dateTime);
		
		return dateTime;
	}

	/**
	 * Fetching the Current DateTime in the custom date format
	 * @param dateFormat Custom date format
	 * @return Current DateTime
	 * @throws Exception The Exception
	 */
	public String getCurrentDateTime(String dateFormat) throws Exception {
		
		Calendar mycalendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateTime = formatter.format(mycalendar.getTime());
		
		this.logAccess.getLogger().info("dateTime :- " + dateTime);
		
		return dateTime;
	}

	/**
	 * Fetching the Current DateTime Meridian in the "MM/dd/yyyy h:mma" format
	 * @return Current DateTime
	 * @throws Exception The Exception
	 */
	public String getCurrentDateTimeMeridian() throws Exception {
		
		Calendar mycalendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:mma");
		String dateTime = formatter.format(mycalendar.getTime());
		
		this.logAccess.getLogger().info("dateTime :- " + dateTime);
		
		return dateTime;
	}

	/**
	 * Fetching the Current Timestamp 
	 * @return Timestamp
	 * @throws Exception The Exception
	 */
	public long getCurrentTimestamp() throws Exception {
		
		Date date = new Date();

		long timestamp = date.getTime();
		
		this.logAccess.getLogger().info("timestamp :- " + timestamp);

		return timestamp;
	}

	/**
	 * Parsing the DateTime string using "MM/dd/yyyy h:mma" format and fetching the Timestamp
	 * @param dateTimeStr DateTime in the String format
	 * @return Timestamp
	 * @throws ParseException The ParseException
	 */
	public long parseToTimestamp(String dateTimeStr) throws ParseException {

		long timestamp = parseToTimestamp(dateTimeStr, "MM/dd/yyyy h:mma");

		this.logAccess.getLogger().info("Timestamp :- " + timestamp);

		return timestamp;
	}

	/**
	 * Parsing the DateTime string using custom date format and fetching the Timestamp
	 * @param dateTimeStr DateTime in the String format
	 * @param dateFormat Custom Date format
	 * @return Timestamp
	 * @throws ParseException The ParseException
	 */
	public long parseToTimestamp(String dateTimeStr,  String dateFormat) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date parsedDate = simpleDateFormat.parse(dateTimeStr);

		long timestamp = parsedDate.getTime();

		this.logAccess.getLogger().info("Timestamp :- " + timestamp);

		return timestamp;
	}

	/**
	 * Validating the string dateTime is matching with the expected "MM/dd/yyyy mm:ss a" format
	 * @param inputDate String dateTime
	 * @return Is a valid date format or not
	 */
	public boolean isValidDate(String inputDate) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy mm:ss a");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inputDate.trim());
		} catch (ParseException pe) {
			
			this.logAccess.getLogger().info("ParseException :- " + pe);
			
			return false;
		}
		
		this.logAccess.getLogger().info("The parsed date is a valid date :- '" + inputDate + "'");
		
		return true;
	}

	/**
	 * Validating the string dateTime is matching with the expected custom date format
	 * @param expectedDateFormat Custom date format
	 * @param inputDate String dateTime
	 * @return Is a valid date format or not
	 */
	public boolean isValidDate(String inputDate, String expectedDateFormat) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(expectedDateFormat);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inputDate.trim());
		} catch (ParseException pe) {
			
			this.logAccess.getLogger().info("ParseException :- " + pe);
			
			return false;
		}
		
		this.logAccess.getLogger().info("The parsed date is a valid date :- '" + inputDate + "' with the expectedDateFormate '" + expectedDateFormat + "'");
		
		return true;
	}

	/**
	 * Fetching the Date, Year and Month between the provided range of years and return it in the "MM/dd/YYYY" format
	 * @param min Minimum Year
	 * @param max Maximum Year
	 * @return Date, Year and Month
	 */
	public String getDateYearMonth(int min, int max) {
		
		String date = getDateYearMonth(min, max, "MM/dd/YYYY");
		
		this.logAccess.getLogger().info("Date, Year and Month in MM/dd/YYYY format :- " + date);
		
		return date;
	}
	
	/**
	 * Fetching the Date, Year and Month between the provided range of years and return it in the custom date format
	 * @param min Minimum Year
	 * @param max Maximum Year
	 * @param dateFormat Custom Date format
	 * @return Date, Year and Month
	 */
	public String getDateYearMonth(int min, int max, String dateFormat) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int year = min + new Random().nextInt(max - min + 1);
		calendar.set(Calendar.YEAR, year);
		int day = 1 + new Random().nextInt(calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		calendar.set(Calendar.DAY_OF_YEAR, day);
		
		String date = new SimpleDateFormat(dateFormat).format(calendar.getTime());
		
		this.logAccess.getLogger().info("Date, Year and Month in custom date format :- " + date);
		
		return date;
	}

}
