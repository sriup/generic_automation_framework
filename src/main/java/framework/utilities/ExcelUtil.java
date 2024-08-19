package framework.utilities;

import framework.logs.LogAccess;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * All the methods related to the excel operations will be handled in this
 * class.
 */
public class ExcelUtil {

	/** The workbook. */
	private Workbook wb;

	/** The log access. */
	private final LogAccess logAccess;

	/**
	 * Instantiates a new excel functions.
	 *
	 * @param logAccess the log access
	 */
	public ExcelUtil(LogAccess logAccess) {

		this.logAccess = logAccess;

	}

	/**
	 * Gets the work book based on the excel file path and name.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @return the work book
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Workbook openWorkBook(String excelFilePath, String excelFileName) throws IOException {

		return openWorkBook(excelFilePath + File.separatorChar + excelFileName);
	}

	/**
	 * Gets the work book based on the excel file path and name.
	 *
	 * @param excelFilePath the excel file path
	 * @return the work book
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Workbook openWorkBook(String excelFilePath) throws IOException {
		// Create an object of File class to open xlsx file
		File file = new File(excelFilePath);

		// Gets the filename
		String excelFileName = file.getName();

		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook wb = null;

		// get the excel file extension
		String fileExtensionName = excelFileName.substring(excelFileName.indexOf("."));

		// return the work book based on the excel file extension
		if (fileExtensionName.equals(".xlsx")) {
			// return xlsx workbook (Microsoft Excel 2007 file or later)
			wb = new XSSFWorkbook(inputStream);
		} else if (fileExtensionName.equals(".xls")) {
			// return xls workbook (Microsoft Excel 2003 file)
			wb = new HSSFWorkbook(inputStream);
		}
		setWb(wb);
		return wb;
	}

	/**
	 * Creates the excel.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetName     Name of the work sheet
	 * @param excelData     the excel data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createExcel(String excelFilePath, String excelFileName, String sheetName, Object[][] excelData)
			throws Exception {
		this.logAccess.getLogger().debug("File Path :- " + excelFilePath);
		this.logAccess.getLogger().debug("File Name :- " + excelFileName);
		this.logAccess.getLogger().debug("Sheet Name" + sheetName);
		this.logAccess.getLogger()
				.debug("Creating Excel File :- " + excelFilePath + File.separatorChar + excelFileName);
		this.logAccess.getLogger().debug("Sheet Name :- " + sheetName);
		new FolderFileUtil(this.logAccess).createFile(excelFilePath, excelFileName);
		this.wb = openWorkBook(excelFilePath, excelFileName);
		Sheet ws = wb.createSheet(sheetName);
		// row index
		int rowIndex = 0;
		// check if excelData is null
		if (excelData != null) {
			// write the data in the sheet
			for (Object[] rowData : excelData) {
				// create a row
				Row row = ws.createRow(rowIndex++);
				// column index
				int colIndex = 0;
				for (Object cellData : rowData) {
					Cell cell = row.createCell(colIndex);
					// set value in the cell
					if (cellData instanceof Integer) {
						// set value as integer
						cell.setCellValue((Integer) cellData);
					} else {
						// set value as String for anything other than Integer
						cell.setCellValue((String) cellData);
					}
					// increase the column number
					colIndex++;
				}
			}
		}
		// save the excel file
		saveWorkBook(excelFilePath, excelFileName);
	}

	// add sheet

	/**
	 * Gets the sheet.
	 *
	 * @param sheetName Name of the work sheet
	 * @return the work sheet
	 */
	public Sheet getSheet(String sheetName) {
		return wb.getSheet(sheetName);
	}

	/**
	 * Gets the sheet at the specified index (index starts with 0).
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @return the work sheet
	 */
	public Sheet getSheet(int sheetIndex) {
		return wb.getSheetAt(sheetIndex);
	}

	/**
	 * Gets the sheet index.
	 *
	 * @param sheetName Name of the work sheet
	 * @return the sheet index
	 */
	public int getSheetIndex(String sheetName) {
		return wb.getSheetIndex(sheetName);
	}

	/**
	 * Gets the column header index.
	 *
	 * @param sheetName  Name of the work sheet
	 * @param columnName the column name
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the column header index
	 */
	public int getColumnHeaderIndex(String sheetName, String columnName, int headersRowIndex) {
		return ArrayUtils.indexOf(getColumnHeaders(sheetName, headersRowIndex), columnName);
	}
	
	/**
	 * Gets the column header index.
	 *
	 * @param sheetName  Name of the work sheet
	 * @param columnName the column name
	 * @return the column header index
	 */
	public int getColumnHeaderIndex(String sheetName, String columnName) {
		return getColumnHeaderIndex(sheetName, columnName, 0);
	}

	/**
	 * Gets the column header index.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical) <br>
	 *                   sheet index starts with 0.
	 * @param columnName Name of the column
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the index of the column (index starts with 0)
	 */
	public int getColumnHeaderIndex(int sheetIndex, String columnName, int headersRowIndex) {
		return ArrayUtils.indexOf(getColumnHeaders(sheetIndex, headersRowIndex), columnName);
	}
	
	/**
	 * Gets the column header index.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical) <br>
	 *                   sheet index starts with 0.
	 * @param columnName Name of the column
	 * @return the index of the column (index starts with 0)
	 */
	public int getColumnHeaderIndex(int sheetIndex, String columnName) {
		return getColumnHeaderIndex(sheetIndex, columnName, 0);
	}

	/**
	 * Gets the used row count from the sheet.
	 *
	 * @param sheetName Name of the work sheet
	 * @return the used rows count
	 */
	public int getRowCount(String sheetName) {
		return getRowCount(getSheetIndex(sheetName));
	}

	/**
	 * Gets the row count.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @return the row count
	 */
	// get row count by sheet index
	public int getRowCount(int sheetIndex) {
		// get work sheet based on the sheet name
		Sheet ws = getSheet(sheetIndex);
		// get number of rows
		return ws.getLastRowNum();
	}

	/**
	 * Gets the row.
	 *
	 * @param sheetName Name of the work sheet
	 * @param rowNumber the row number
	 * @return the row
	 */
	// get Row
	public Row getRow(String sheetName, int rowNumber) {
		// get work sheet based on the sheet name
		Sheet ws = getSheet(sheetName);
		// get row based on the index
		return ws.getRow(rowNumber);
	}

	/**
	 * Gets the row.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @param rowNumber  the row number
	 * @return the row
	 */
	// get Row
	public Row getRow(int sheetIndex, int rowNumber) {
		// get work sheet based on the sheet name
		Sheet ws = getSheet(sheetIndex);
		// get row based on the index
		return ws.getRow(rowNumber);
	}

	/**
	 * Gets the row
	 *
	 * @param sheetIndex  the index of the sheet number (0-based physical and
	 *                    logical)
	 * @param startRowNum Row index starts from
	 * @param endRowNum   Row index up to
	 * @param isFallBack  return the rows based on the maximum number of rows and <i>endRowNum</i>.
	 * <ul>
	 *     <li>true: get's maximum rows available from the sheet if maximum rows is less than <i>endRowNum</i>.</li>
	 *     <li>false: throws exception if the maximum rows count is less than <i>endRowNum</i>.</li>
	 *
	 * </ul>
	 * @return List of rows
	 */
	public List<Row> getRows(int sheetIndex, int startRowNum, int endRowNum, boolean isFallBack) {
		// get row based on the index
		int maxRows = getRowCount(sheetIndex);

		boolean isWithinRange = (startRowNum <= maxRows && endRowNum <= maxRows);

		if (!isWithinRange && !isFallBack) {
			throw new ArrayIndexOutOfBoundsException("Invalid row range : StartRowNum '" + startRowNum
					+ "' and endRowNum '" + endRowNum + "'. Max rows are '" + maxRows + "'");
		}

		List<Row> rowsList = new ArrayList<>();

		endRowNum = Math.min(endRowNum, maxRows);

		for (int currentRowIndex = startRowNum; currentRowIndex <= endRowNum; currentRowIndex++) {

			Row row = this.getRow(sheetIndex, currentRowIndex);

			rowsList.add(row);
		}

		return rowsList;
	}

	/**
	 * Gets the row
	 * 
	 * @param sheetIndex  the index of the sheet number (0-based physical and
	 *                    logical)
	 * @param startRowNum Row index starts from
	 * @param endRowNum   Row index up to
	 * @return List of rows
	 */
	public List<Row> getRows(int sheetIndex, int startRowNum, int endRowNum) {

		return getRows(sheetIndex, startRowNum, endRowNum, false);
	}

	/**
	 * Gets the row
	 * 
	 * @param sheetName   Name of the work sheet
	 * @param startRowNum Row index starts from
	 * @param endRowNum   Row index up to
	 * @return List of rows
	 */
	public List<Row> getRows(String sheetName, int startRowNum, int endRowNum) {

		int sheetIndex = getSheetIndex(sheetName);

		return getRows(sheetIndex, startRowNum, endRowNum, false);
	}

	/**
	 * Gets the column data.
	 *
	 * @param sheetName    Name of the work sheet
	 * @param columnNumber the column number
	 * @return the column data
	 */
	// get column data
	public String[] getColumnData(String sheetName, int columnNumber) {
		// get number of rows
		int numberOfRows = getRowCount(sheetName);
		String[] columnData = new String[numberOfRows];
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			columnData[rowIndex] = getCellData(sheetName, rowIndex, columnNumber);
		}
		return columnData;
		//TODO Need clean-up call the overloaded method with index rather replicating the code
	}

	/**
	 * Gets the column data.
	 *
	 * @param sheetIndex   the index of the sheet number (0-based physical and
	 *                     logical)
	 * @param columnNumber the column number
	 * @return the column data
	 */
	public String[] getColumnData(int sheetIndex, int columnNumber) {
		// get number of rows
		int numberOfRows = getRowCount(sheetIndex);
		String[] columnData = new String[numberOfRows];
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			columnData[rowIndex] = getCellData(sheetIndex, rowIndex, columnNumber);
		}
		return columnData;
	}

	/**
	 * Gets the column headers.
	 *
	 * @param sheetName Name of the work sheet
	 * @return the column headers
	 */
	// get headers
	public String[] getColumnHeaders(String sheetName) {
		int sheetIndex = getSheetIndex(sheetName);

		return getColumnHeaders(sheetIndex, 0);
	}

	/**
	 * Gets the column headers.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @return the column headers
	 */
	// get headers
	public String[] getColumnHeaders(int sheetIndex) {

		return getColumnHeaders(sheetIndex, 0);
	}

	/**
	 * 
	 * Gets the column headers.
	 *
	 * 
	 * 
	 * @param sheetIndex      the index of the sheet number (0-based physical and
	 * 
	 *                        logical)
	 * 
	 * @param headersRowIndex the index of the row where it contains all the headers
	 * 
	 * @return the column headers
	 * 
	 */

	public String[] getColumnHeaders(int sheetIndex, int headersRowIndex) {
		// get the header row

		Row row = getRow(sheetIndex, headersRowIndex);

		// get number of columns

		int columnCount = row.getLastCellNum();

		String[] headers = new String[columnCount];

		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {

			headers[columnIndex] = (row.getCell(columnIndex) != null) ? StringUtils.normalizeSpace(row.getCell(columnIndex).toString()) : "";

		}

		return headers;

	}

	/**
	 * 
	 * Gets the column headers.
	 *
	 * 
	 * 
	 * @param sheetName       Name of the work sheet
	 * 
	 * @param headersRowIndex the index of the row where it contains all the headers
	 * 
	 * @return the column headers
	 * 
	 */

	public String[] getColumnHeaders(String sheetName, int headersRowIndex) {

		int sheetIndex = getSheetIndex(sheetName);

		return getColumnHeaders(sheetIndex, headersRowIndex);

	}

	/**
	 * Gets the cell value based on the cell
	 * 
	 * @param cell Cell from which the value should be returned
	 * @return cell value
	 */
	public String getCellData(Cell cell) {
		String cellValue = "";

		CellType cellType = cell.getCellType();

		if (cellType == CellType.STRING) {
			cellValue = cell.toString().trim();
		} else if (cellType == CellType.NUMERIC) {
			if (cell.getCellStyle().getDataFormatString().contains("%")) {
				cellValue = cell.getNumericCellValue() * 100 + "%";
			} else {
				cellValue = Double.toString(cell.getNumericCellValue());
			}
		} else if (cellType == CellType.BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		}

		return cellValue;
	}

	/**
	 * Gets the cell data.
	 *
	 * @param sheetIndex   the index of the sheet number (0-based physical and
	 *                     logical)
	 * @param rowNumber    the row number
	 * @param columnNumber the column number
	 * @return the cell data
	 */
	// get cell value by row, column (indexes)
	public String getCellData(int sheetIndex, int rowNumber, int columnNumber) {
		String cellValue = "";

		// check if the rowNumber is with in the limit of available rows
		if (getRowCount(sheetIndex) >= rowNumber) {
			try {
				// get row based on the sheet name and row index
				Row row = getRow(sheetIndex, rowNumber);
				Cell cell = row.getCell(columnNumber);
				cellValue = getCellData(cell);
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
	 * @param sheetName    Name of the work sheet
	 * @param rowNumber    the row number
	 * @param columnNumber the column number
	 * @return the cell data
	 */
	// get cell value by row, column (indexes)
	public String getCellData(String sheetName, int rowNumber, int columnNumber) {
		String cellValue = null;
		// check if the rowNumber is with in the limit of available rows
		if (rowNumber <= getRowCount(sheetName)) {
			// get row based on the sheet name and row index
			Row row = getRow(sheetName, rowNumber);
			cellValue = row.getCell(columnNumber).toString();
		} else {
			// throw error that rowNumber is > rows count in the sheet
			//TODO need to add logic to throw the error
		}
		return cellValue;
	}

	// get cell data based on sheet name and column name
	/**
	 * Gets the cell data.
	 *
	 * @param sheetName  Name of the work sheet
	 * @param rowNumber  the row number
	 * @param columnName the column name
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the cell data
	 */
	// get cell value by header
	public String getCellData(String sheetName, int rowNumber, String columnName, int headersRowIndex) {
		String cellValue;
		// check if the rowNumber is with in the limit of available rows
		if (rowNumber <= getRowCount(sheetName)) {
			// get row based on the sheet name and row index
			Row row = getRow(sheetName, rowNumber);
			cellValue = row.getCell(getColumnHeaderIndex(sheetName, columnName, headersRowIndex)).toString();
		} else {
			// throw error that rowNumber is > rows count in the sheet
			throw new ArrayIndexOutOfBoundsException();
		}
		return cellValue;
	}
	
	// get cell data based on sheet name and column name
	/**
	 * Gets the cell data.
	 *
	 * @param sheetName  Name of the work sheet
	 * @param rowNumber  the row number
	 * @param columnName the column name
	 * @return the cell data
	 */
	// get cell value by header
	public String getCellData(String sheetName, int rowNumber, String columnName) {
		
		return getCellData(sheetName, rowNumber, columnName, 0);
	}

	/**
	 * Gets the cell data
	 * 
	 * @param sheetName  Name of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(String sheetName, Row row, String columnName) {

		return getCellData(sheetName, row, columnName, true);
	}
	
	/**
	 * Gets the cell data
	 *
	 * @param sheetName  Name of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the cell data
	 */
	public String getCellData(String sheetName, Row row, String columnName,
							  int headersRowIndex) {
		
		return getCellData(sheetName, row, columnName, true, headersRowIndex);
	}

	/**
	 * Gets the cell data
	 *
	 * @param sheetName  Name of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @param  addLogMsg  flag to decide whether the details should be logged to logger or not.
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the cell data
	 */
	public String getCellData(String sheetName, Row row, String columnName,
							  boolean addLogMsg, int headersRowIndex) {

		int sheetIndex = getSheetIndex(sheetName);

		String cellValue = getCellData(sheetIndex, row, columnName, headersRowIndex);

		if(addLogMsg) {
			this.logAccess.getLogger().debug("Current cell value is '" + cellValue + "'");
		}
		return cellValue;
	}
	
	/**
	 * Gets the cell data
	 *
	 * @param sheetName  Name of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @param  addLogMsg  flag to decide whether the details should be logged to logger or not.
	 * @return the cell data
	 */
	public String getCellData(String sheetName, Row row, String columnName, boolean addLogMsg) {
		
		return getCellData(sheetName, row, columnName, addLogMsg, 0);
	}

	/**
	 * Gets the cell data
	 * 
	 * @param sheetIndex Sheet Index of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return the cell data
	 */
	public String getCellData(int sheetIndex, Row row, String columnName,
							  int headersRowIndex) {
		String cellValue = "";

		try {
			int columnNumber = getColumnHeaderIndex(sheetIndex, columnName, headersRowIndex);

			// get row based on the sheet name and row index
			Cell cell = row.getCell(columnNumber);
			cellValue = getCellData(cell);
		} catch (NullPointerException NPE) {
			cellValue = "";
		}

		return cellValue;
	}
	
	/**
	 * Gets the cell data
	 *
	 * @param sheetIndex Sheet Index of the work sheet
	 * @param row        Excel row
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(int sheetIndex, Row row, String columnName) {
		
		return getCellData(sheetIndex, row, columnName, 0);
	}

	/**
	 * Gets the row data.
	 *
	 * @param sheetName Name of the work sheet
	 * @param rowNumber the row number
	 * @return the row data
	 */
	public Map<String, String> getRowData(String sheetName, int rowNumber) {
		Map<String, String> map = new HashMap<>();
		String[] headers = getColumnHeaders(sheetName);
		String columnName, value;
		for (int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
			// Print Excel data in console
			columnName = headers[columnIndex];
			value = getCellData(sheetName, rowNumber, columnIndex);
			map.put(columnName, value);
		}
		return map;
	}

	/**
	 * Gets the row data.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @param rowNumber  the row number
	 * @return the row data
	 */
	public Map<String, String> getRowData(int sheetIndex, int rowNumber) {
		Map<String, String> map = new HashMap<>();
		String[] headers = getColumnHeaders(sheetIndex);
		String columnName, value;
		for (int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
			// Print Excel data in console
			columnName = headers[columnIndex];
			value = getCellData(sheetIndex, rowNumber, columnIndex);
			map.put(columnName, value);
		}
		return map;
	}
	
	
	/**
	 * Gets the filtered row if it matches all the column values
	 *
	 * @param sheetName      Name of the work sheet
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
	 * @param headersRowIndex the index of the row where it contains all the headers
	 * @return The filtered row if it matches all the expected column values
	 */
	public Row getFilteredRow(String sheetName, Map<String, String> filtersDataMap,
							  int headersRowIndex) {
		
		Sheet sheet = this.wb.getSheet(sheetName);
		
		Row filteredRow = null;
		
		for (Row currentRow : sheet) {
			
			Set<String> filtersDataMapKeys = filtersDataMap.keySet();
			
			boolean isFoundAllFilters = true;
			
			for (String filterMapKey : filtersDataMapKeys) {
				
				String filterMapValue = filtersDataMap.get(filterMapKey);
				
				String currentCellValue = getCellData(sheetName, currentRow, filterMapKey,
						headersRowIndex);
				
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
	 * @param sheetName      Name of the work sheet
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
	 * @param headersRowIndex 	the index of the row where it contains all the headers
	 * @return The list of filtered rows if it matches all the expected column
	 *         values
	 */
	public List<Row> getFilteredRows(String sheetName, Map<String, String> filtersDataMap,
									 int headersRowIndex) {
		
		List<Row> filteredRows = new ArrayList<Row>();
		
		Sheet sheet = this.wb.getSheet(sheetName);
		
		for (Row currentRow : sheet) {
			
			Set<String> filtersDataMapKeys = filtersDataMap.keySet();
			
			boolean isFoundAllFilters = true;
			
			for (String filterMapKey : filtersDataMapKeys) {
				
				String filterMapValue = filtersDataMap.get(filterMapKey);
				
				String currentCellValue = getCellData(sheetName, currentRow, filterMapKey,
						headersRowIndex);
				
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
	
	/**
	 * Gets the filtered row if it matches all the column values
	 * 
	 * @param sheetName      Name of the work sheet
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
	public Row getFilteredRow(String sheetName, Map<String, String> filtersDataMap) {

		return getFilteredRow(sheetName, filtersDataMap, 0);

	}

	/**
	 * Gets the list of filtered rows if it matches all the column values
	 * 
	 * @param sheetName      Name of the work sheet
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
	public List<Row> getFilteredRows(String sheetName, Map<String, String> filtersDataMap) {
		
		return getFilteredRows(sheetName, filtersDataMap, 0);

	}

	/*
	 * Write
	 */
	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetName     Name of the work sheet
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnNumber  the column number (starts with 0)
	 * @param value         the value
	 * @param indexColor    Index of the color <br>
	 *                      <font color="blue"><b>Example:</b> IndexedColors.GREEN
	 *                      </font>
	 * @param patternType   Fill Pattern <br>
	 *                      <font color="blue"><b>Example:</b>
	 *                      FillPatternType.LESS_DOTS </font>
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// write value in cell
	public Cell writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			int columnNumber, Object value, IndexedColors indexColor, FillPatternType patternType) throws IOException {

		CellStyle cellStyle = createCellStyle(indexColor, patternType);

		Cell cell = writeCellData(excelFilePath, excelFileName, sheetName, rowNumber, columnNumber, value);

		cell.setCellStyle(cellStyle);

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetIndex    the index of the sheet number (0-based physical and
	 *                      logical)
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnNumber  the column number (starts with 0)
	 * @param value         the value
	 * @param indexColor    Index of the color <br>
	 *                      <font color="blue"><b>Example:</b> IndexedColors.GREEN
	 *                      </font>
	 * @param patternType   Fill Pattern <br>
	 *                      <font color="blue"><b>Example:</b>
	 *                      FillPatternType.LESS_DOTS </font>
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			int columnNumber, Object value, IndexedColors indexColor, FillPatternType patternType) throws IOException {

		CellStyle cellStyle = createCellStyle(indexColor, patternType);

		Cell cell = writeCellData(excelFilePath, excelFileName, sheetIndex, rowNumber, columnNumber, value);

		cell.setCellStyle(cellStyle);

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetName     Name of the work sheet
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnName    the column name
	 * @param value         the value
	 * @param indexColor    Index of the color <br>
	 *                      <font color="blue"><b>Example:</b> IndexedColors.GREEN
	 *                      </font>
	 * @param patternType   Fill Pattern <br>
	 *                      <font color="blue"><b>Example:</b>
	 *                      FillPatternType.LESS_DOTS </font>
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			String columnName, Object value, IndexedColors indexColor, FillPatternType patternType) throws IOException {

		CellStyle cellStyle = createCellStyle(indexColor, patternType);

		Cell cell = writeCellData(excelFilePath, excelFileName, sheetName, rowNumber, columnName, value);
		cell.setCellStyle(cellStyle);

		saveWorkBook(excelFilePath, excelFileName);

		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetIndex    the index of the sheet number (0-based physical and
	 *                      logical)
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnName    the column name
	 * @param value         the value
	 * @param indexColor    Index of the color <br>
	 *                      <font color="blue"><b>Example:</b> IndexedColors.GREEN
	 *                      </font>
	 * @param patternType   Fill Pattern <br>
	 *                      <font color="blue"><b>Example:</b>
	 *                      FillPatternType.LESS_DOTS </font>
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			String columnName, Object value, IndexedColors indexColor, FillPatternType patternType) throws IOException {

		CellStyle cellStyle = createCellStyle(indexColor, patternType);

		Cell cell = writeCellData(excelFilePath, excelFileName, sheetIndex, rowNumber, columnName, value);

		cell.setCellStyle(cellStyle);

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetName     the sheet name
	 * @param rowNumber     the row number
	 * @param columnNumber  the column number
	 * @param value         the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// write value in cell
	public Cell writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			int columnNumber, Object value) throws IOException {
		Cell cell;
		if (rowNumber > getRowCount(sheetName)) {
			// create a new row and append the data
			cell = getSheet(sheetName).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row
			if (getRow(sheetName, rowNumber).getCell(columnNumber) == null) {
				getRow(sheetName, rowNumber).createCell(columnNumber);
			}
			cell = getRow(sheetName, rowNumber).getCell(columnNumber);
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetIndex    the index of the sheet number (0-based physical and
	 *                      logical)
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnNumber  the column number (starts with 0)
	 * @param value         the value
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			int columnNumber, Object value) throws IOException {
		Cell cell;

		if (rowNumber > getRowCount(sheetIndex)) {
			// create a new row and append the data
			cell = getSheet(sheetIndex).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row
			if (getRow(sheetIndex, rowNumber).getCell(columnNumber) == null) {
				getRow(sheetIndex, rowNumber).createCell(columnNumber);
			}
			cell = getRow(sheetIndex, rowNumber).getCell(columnNumber);
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetName     Name of the work sheet
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnName    the column name
	 * @param value         the value
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			String columnName, Object value) throws IOException {
		Cell cell;

		int columnNumber = getColumnHeaderIndex(sheetName, columnName);

		if (rowNumber > getRowCount(sheetName)) {
			// create a new row and append the data
			cell = getSheet(sheetName).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row

			if (getRow(sheetName, rowNumber).getCell(columnNumber) == null) {
				getRow(sheetName, rowNumber).createCell(columnNumber);
			}

			cell = getRow(sheetName, rowNumber).getCell(columnNumber);
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}

		saveWorkBook(excelFilePath, excelFileName);
		return cell;
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @param sheetIndex    the index of the sheet number (0-based physical and
	 *                      logical)
	 * @param rowNumber     the row number (starts with 0)
	 * @param columnName    the column name
	 * @param value         the value
	 * @return the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cell writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			String columnName, Object value) throws IOException {
		Cell cell;

		int columnNumber = getColumnHeaderIndex(sheetIndex, columnName);

		if (rowNumber > getRowCount(sheetIndex)) {
			// create a new row and append the data
			cell = getSheet(sheetIndex).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row
			if (getRow(sheetIndex, rowNumber).getCell(columnNumber) == null) {
				getRow(sheetIndex, rowNumber).createCell(columnNumber);
			}
			cell = getRow(sheetIndex, rowNumber).getCell(columnNumber);
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}

		saveWorkBook(excelFilePath, excelFileName);

		return cell;
	}

	/**
	 * Format cell style.
	 *
	 * @param cell            the cell
	 * @param indexColorEnum  the {@link org.apache.poi.ss.usermodel.IndexedColors
	 *                        IndexColors}
	 * @param patternTypeEnum the {@link org.apache.poi.ss.usermodel.FillPatternType
	 *                        FillPatternType}
	 */
	public void formatCellStyle(Cell cell, IndexedColors indexColorEnum, FillPatternType patternTypeEnum) {
		CellStyle cellStyle = createCellStyle(indexColorEnum, patternTypeEnum);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * Creating the style for the cells
	 * 
	 * @param indexColor  Index of the color <br>
	 *                    <font color="blue"><b>Example:</b> IndexedColors.GREEN
	 *                    </font>
	 * @param patternType Fill Pattern <br>
	 *                    <font color="blue"><b>Example:</b>
	 *                    FillPatternType.LESS_DOTS </font>
	 * @return			  the cell style
	 */
	private CellStyle createCellStyle(IndexedColors indexColor, FillPatternType patternType) {

		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		cellStyle.setFillBackgroundColor(indexColor.getIndex());
		cellStyle.setFillPattern(patternType);

		return cellStyle;

	}

	/*
	 * Delete
	 */
	/**
	 * Delete row.
	 *
	 * @param sheetName Name of the work sheet
	 * @param rowNumber the row number
	 */
	// delete row
	public void deleteRow(String sheetName, int rowNumber) {
		getSheet(sheetName).removeRow(getRow(sheetName, rowNumber));
	}

	/**
	 * Delete row.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @param rowNumber  the row number
	 */
	public void deleteRow(int sheetIndex, int rowNumber) {
		getSheet(sheetIndex).removeRow(getRow(sheetIndex, rowNumber));
	}

	/**
	 * Delete column.
	 *
	 * @param sheetIndex   the index of the sheet number (0-based physical and
	 *                     logical)
	 * @param columnNumber the column number
	 */
	// delete cell
	public void deleteColumn(int sheetIndex, int columnNumber) {
		deleteColumnAndRetainColumnWidth(sheetIndex, columnNumber);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 * @param columnName the column name
	 */
	public void deleteColumn(int sheetIndex, String columnName) {
		int columnIndex = getColumnHeaderIndex(sheetIndex, columnName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnIndex);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetName    Name of the work sheet
	 * @param columnNumber the column number
	 */
	// delete column
	public void deleteColumn(String sheetName, int columnNumber) {
		int sheetIndex = getSheetIndex(sheetName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnNumber);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetName  Name of the work sheet
	 * @param columnName the column name
	 */
	public void deleteColumn(String sheetName, String columnName) {
		int sheetIndex = getSheetIndex(sheetName);
		int columnIndex = getColumnHeaderIndex(sheetIndex, columnName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnIndex);
	}

	/**
	 * Delete sheet based on the sheet name.
	 *
	 * @param sheetName Name of the work sheet
	 */
	// delete sheet
	public void deleteSheet(String sheetName) {
		int sheetIndex = getSheetIndex(sheetName);
		deleteSheet(sheetIndex);
	}

	/**
	 * Delete sheet based on the sheet index.
	 *
	 * @param sheetIndex the index of the sheet number (0-based physical and
	 *                   logical)
	 */
	public void deleteSheet(int sheetIndex) {
		wb.removeSheetAt(sheetIndex);
	}

	/**
	 * Save work book.
	 *
	 * @param excelFilePath the excel file path
	 * @param excelFileName the excel file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// save workbook
	public void saveWorkBook(String excelFilePath, String excelFileName) throws IOException {
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(excelFilePath + File.separatorChar + excelFileName);
		wb.write(outputStream);
		outputStream.close();
	}

	/**
	 * Close work book.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// close sheet
	public void closeWorkBook() throws IOException {
		wb.close();

	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Private methods
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	/**
	 * Delete column and retain column width.
	 *
	 * @param sheetIndex   the index of the sheet number (0-based physical and
	 *                     logical)
	 * @param columnNumber the column number
	 */
	private void deleteColumnAndRetainColumnWidth(int sheetIndex, int columnNumber) {
		// get number of rows
		int rowCount = getRowCount(sheetIndex);
		// iterate through all the rows and delete the cell in the specified column
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			Row row = getRow(sheetIndex, rowIndex);
			Cell cellToDelete = row.getCell(columnNumber);
			row.removeCell(cellToDelete);
		}
	}

	public Workbook getWb() {
		return wb;
	}

	public void setWb(Workbook wb) {
		if (this.wb != null) {
			try {
				this.wb.close();
			} catch (IOException e) {
				this.logAccess.getLogger().debug("received IOException, please check the trace for more information.");
				e.printStackTrace();
			}
			this.wb = null;
		}
		this.wb = wb;
	}


	/**
	 * Gets the list of sheetNames from the file
	 * @param wb   the workbook
	 * @return the list of sheetnames
	 */
	public List<String> getSheetNames(Workbook wb){

		int numberOfSheets = wb.getNumberOfSheets();

		List<String> sheetNamesList = new ArrayList<>();

		for (int currentSheetIndex = 0; currentSheetIndex < numberOfSheets; currentSheetIndex++){

			String currentSheetName = wb.getSheetName(currentSheetIndex);

			sheetNamesList.add(currentSheetName);

		}

		return sheetNamesList;
	}

}
