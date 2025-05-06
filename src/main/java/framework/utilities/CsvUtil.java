package framework.utilities;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import framework.logs.LogAccess;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * All the methods related to the CSV operations will be handled in this
 * class.
 */
public class CsvUtil {

	/** The log access. */
	private final LogAccess logAccess;

	private List<String[]> allRows;

	/**
	 * Instantiates a new CSV functions.
	 *
	 * @param logAccess the log access
	 */
	public CsvUtil(LogAccess logAccess) {

		this.logAccess = logAccess;

	}

	/**
	 * Opens the CSV Reader based on the csv filepath and the custom separator (if any)
	 *
	 * @param csvFilePath	the csv file path where the file is located
	 * @param csvFileName	the csv file name
	 * @param separator	the separator with which the columns are separated.
	 * @throws Exception	the exception
	 */
	public void openCsvReader(String csvFilePath, String csvFileName, char separator) throws Exception {

		String filePath = csvFilePath + File.separatorChar + csvFileName;

		openCsvReader(filePath, separator);
	}

	/**
	 * Opens the CSV Reader based on the csv filepath and the custom separator (if any)
	 *
	 * @param filePath	the csv file path where the file is located along with the filename
	 * @param separator	the separator with which the columns are separated.
	 * @throws Exception	the exception
	 */
	public void openCsvReader(String filePath, char separator) throws Exception {

		FileReader filereader = new FileReader(filePath, StandardCharsets.UTF_8);

		CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();

		CSVReader csvReader = new CSVReaderBuilder(filereader)
				.withCSVParser(parser)
				.build();

		// Reads all the rows and store it. We will do only one time read
		setAllRows(csvReader.readAll());

		csvReader.close();

	}

	/**
	 * Gets all the rows
	 *
	 * @return	the rows list
	 */
	public List<String[]> getAllRows() {

		return this.allRows;
	}

	/**
	 * Sets all the rows
	 */
	// Making this as a private method. So that it cannot be manipulated.
	private void setAllRows(List<String[]> allRows) {

		this.allRows = allRows;

	}

	public String[] getRow(int rowNumber) throws Exception{
		return getAllRows().get(rowNumber);
	}

	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	public int getRowCount() throws Exception {

		// get number of rows
		return getAllRows().size();
	}

	/**
	 *
	 * Gets the column headers.
	 *
	 * @param headersRowIndex the index of the row where it contains all the headers
	 *
	 * @return the column headers
	 *
	 */
	public String[] getColumnHeaders(int headersRowIndex) throws Exception {

		// get the header row
		String[] tempHeaders = new String[getRow(headersRowIndex).length];

		int counter = 0;
		for (String currenHeader: getRow(headersRowIndex)) {

			currenHeader = currenHeader.replaceAll("\\uFEFF", "")
					.replaceAll("ZWNBSP", "").replaceAll("ï»¿", "");

			tempHeaders[counter] = currenHeader;

			counter++;
		}

		return tempHeaders;

	}

	/**
	 *
	 * Gets the column headers.
	 *
	 * @return the column headers
	 *
	 */
	public String[] getColumnHeaders() throws Exception {

		// get the header row
		return getRow(0);

	}

	/**
	 * Gets the column header index.
	 *
	 * @param headersRowIndex  the index of the row where it contains all the headers
	 * @param columnName the column name
	 * @return the column header index
	 */
	public int getColumnHeaderIndex(int headersRowIndex, String columnName) throws Exception {
		return ArrayUtils.indexOf(getColumnHeaders(headersRowIndex), columnName);
	}

	/**
	 * Gets the column header index.
	 *
	 * @param columnName the column name
	 * @return the column header index
	 */
	public int getColumnHeaderIndex(String columnName) throws Exception {
		return ArrayUtils.indexOf(getColumnHeaders(), columnName);
	}

	/**
	 * Gets the cell data.
	 *
	 * @param rowNumber    the row number
	 * @param columnNumber the column number
	 * @return the cell data
	 */
	public String getCellData(int rowNumber, int columnNumber) throws Exception {
		String cellValue = "";

		// check if the rowNumber is with in the limit of available rows
		if (getRowCount() >= rowNumber) {
			try {

				// get row based on the row index
				String[] row = getRow(rowNumber);
				cellValue = row[columnNumber];

			} catch (NullPointerException NPE) {
				cellValue = "";
			}
		} else {
			this.logAccess.getLogger().warn("Cell :  \"R" + rowNumber + "C"
					+ columnNumber + "\"  is out of range.");
			throw new ArrayIndexOutOfBoundsException("Cell :  \"R" + rowNumber + "C"
					+ columnNumber + "\"  is out of range.");
		}
		return cellValue;
	}

	/**
	 * Gets the cell data.
	 *
	 * @param rowNumber    the row number
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(int rowNumber, String columnName) throws Exception {

		int columnNumber = getColumnHeaderIndex(columnName);

		return getCellData(rowNumber, columnNumber);

	}

	/**
	 * Gets the column data.
	 *
	 * @param columnNumber the column number
	 * @return the column data
	 */
	public String[] getColumnData(int columnNumber) throws Exception {
		// get number of rows
		int numberOfRows = getRowCount();
		String[] columnData = new String[numberOfRows];
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			columnData[rowIndex] = getCellData(rowIndex, columnNumber);
		}
		return columnData;
	}

	/**
	 * Gets the column data.
	 *
	 * @param columnName the column name
	 * @return the column data
	 */
	public String[] getColumnData(String columnName) throws Exception {

		int columnNumber = getColumnHeaderIndex(columnName);

		return getColumnData(columnNumber);
	}

	/**
	 * Gets the row data.
	 *
	 * @param rowNumber the row number
	 * @return the row data
	 */
	public Map<String, String> getRowData(int rowNumber) throws Exception {
		Map<String, String> map = new HashMap<>();
		String[] headers = getColumnHeaders();
		String columnName, value;
		for (int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
			columnName = headers[columnIndex].replaceAll("\\uFEFF", "")
					.replaceAll("ZWNBSP", "").replaceAll("ï»¿", "");
			value = getCellData(rowNumber, columnIndex);
			map.put(columnName, value);
		}
		return map;
	}

	/**
	 * Gets the cell data
	 *
	 * @param rowHeaderIndex row header index
	 * @param row        row data
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(int rowHeaderIndex, String[] row, String columnName) throws Exception {
		String cellValue = "";

		try {
			int columnNumber = getColumnHeaderIndex(rowHeaderIndex, columnName);

			// get row based on the sheet name and row index
			cellValue = row[columnNumber];

		} catch (NullPointerException NPE) {
			cellValue = "";
		}

		return cellValue;
	}

	/**
	 * Gets the cell data
	 *
	 * @param row        row data
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(String[] row, String columnName) throws Exception {

		return getCellData(0, row, columnName);
	}

	/**
	 * Gets the filtered row if it matches all the column values
	 *
	 * @param filtersDataMap Column values in the Map to filter the row <br>
	 *                       <font color="blue"><b>Example:</b> Below is the example
	 *                       for the filtersDataMap to get all the rows which match
	 *                       the specified k,v in the Map<br>
	 *
	 *                       <pre>
	 *                       Map<String, String> filtersDataMap = new HashMap<>();
	 *                       filtersDataMap.put("TestMethod", "Test_001");
	 *                       filtersDataMap.put("ExecutionFlag", "Y");
	 *                       filtersDataMap.put("Role", "Admin");
	 *
	 *                       <pre>
	 *                       </font>
	 * @return The filtered row if it matches all the expected column values
	 */
	public String[] getFilteredRow(Map<String, String> filtersDataMap) throws Exception {

		String[] filteredRow = null;

		for (String[] currentRow : getAllRows()) {

			Set<String> filtersDataMapKeys = filtersDataMap.keySet();

			boolean isFoundAllFilters = true;

			for (String filterMapKey : filtersDataMapKeys) {

				String filterMapValue = filtersDataMap.get(filterMapKey);

				String currentCellValue = getCellData(currentRow, filterMapKey);

				if (!currentCellValue.trim().equals(filterMapValue.trim())) {
					isFoundAllFilters = false;
					break;
				}

			}

			if (isFoundAllFilters) {
				filteredRow = currentRow;
				break;
			}

		}

		return filteredRow;

	}

	/**
	 * Gets the list of filtered rows if it matches all the column values
	 *
	 * @param filtersDataMap Column values in the Map to filter the row <br>
	 *                       <font color="blue"><b>Example:</b> Below is the example
	 *                       for the filtersDataMap to get all the rows which match
	 *                       the specified k,v in the Map<br>
	 * 
	 *                       <pre>
	 *                       Map<String, String> filtersDataMap = new HashMap<>();
	 *                       filtersDataMap.put("TestMethod", "Test_001");
	 *                       filtersDataMap.put("ExecutionFlag", "Y");
	 *                       filtersDataMap.put("Role", "Admin");
	 * 
	 *                       <pre>
	 *                       </font>
	 * @return The list of filtered rows if it matches all the expected column
	 *         values
	 */
	public List<String[]> getFilteredRows(Map<String, String> filtersDataMap) throws Exception {

		List<String[]> filteredRows = new ArrayList<>();


		for (String[] currentRow : getAllRows()) {

			Set<String> filtersDataMapKeys = filtersDataMap.keySet();

			boolean isFoundAllFilters = true;

			for (String filterMapKey : filtersDataMapKeys) {

				String filterMapValue = filtersDataMap.get(filterMapKey);

				String currentCellValue = getCellData(currentRow, filterMapKey);

				if (!currentCellValue.trim().equals(filterMapValue.trim())) {
					isFoundAllFilters = false;
					break;
				}

			}

			if (isFoundAllFilters) {
				filteredRows.add(currentRow);
			}

		}

		return filteredRows;

	}

}
