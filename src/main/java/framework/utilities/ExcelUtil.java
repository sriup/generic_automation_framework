package framework.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import framework.logs.LogAccess;

/**
 * All the methods related to the excel operations will be handled in this
 * class.
 */
public class ExcelUtil {

	/** The workbook. */
	private Workbook wb;

	/** The log access. */
	private LogAccess logAccess;

	/**
	 * Instantiates a new excel functions.
	 *
	 * @param logAccess
	 *            the log access
	 */
	public ExcelUtil(LogAccess logAccess) {

		this.logAccess = logAccess;

	}

	/**
	 * Creates the excel.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @param sheetName
	 *            Name of the work sheet
	 * @param excelData
	 *            the excel data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void createExcel(String excelFilePath, String excelFileName, String sheetName, Object[][] excelData)
			throws IOException {
		this.logAccess.getLogger().info("File Path :- " + excelFilePath);
		this.logAccess.getLogger().info("File Name :- " + excelFileName);
		this.logAccess.getLogger().info("Sheet Name" + sheetName);
		this.logAccess.getLogger()
				.debug("Creating Excel File :- " + excelFilePath + File.pathSeparatorChar + excelFileName);
		this.logAccess.getLogger().debug("Sheet Name :- " + sheetName);
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
	 * @param sheetName
	 *            Name of the work sheet
	 * @return the work sheet
	 */
	public Sheet getSheet(String sheetName) {
		return wb.getSheet(sheetName);
	}

	/**
	 * Gets the sheet at the specified index (index starts with 0).
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @return the work sheet
	 */
	public Sheet getSheet(int sheetIndex) {
		return wb.getSheetAt(sheetIndex);
	}

	/**
	 * Gets the sheet index.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @return the sheet index
	 */
	public int getSheetIndex(String sheetName) {
		return wb.getSheetIndex(sheetName);
	}

	/**
	 * Gets the column header index.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param columnName
	 *            the column name
	 * @return the column header index
	 */
	public int getColumnHeaderIndex(String sheetName, String columnName) {
		return ArrayUtils.indexOf(getColumnHeaders(sheetName), columnName);
	}

	/**
	 * Gets the column header index.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical) <br>
	 *            sheet index starts with 0.
	 * @param columnName
	 *            Name of the column
	 * @return the index of the column (index starts with 0)
	 */
	public int getColumnHeaderIndex(int sheetIndex, String columnName) {
		return ArrayUtils.indexOf(getColumnHeaders(sheetIndex), columnName);
	}

	/**
	 * Gets the used row count from the sheet.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @return the used rows count
	 */
	public int getRowCount(String sheetName) {
		return getRowCount(getSheetIndex(sheetName));
	}

	/**
	 * Gets the row count.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
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
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number
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
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number
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
	 * @param sheetIndex the index of the sheet number (0-based physical & logical)
	 * @param startRowNum Row index starts from
	 * @param endRowNum Row index upto 
	 * @return List of rows
	 */
	public List<Row> getRows(int sheetIndex, int startRowNum, int endRowNum) {
		// get row based on the index
		int maxRows = getRowCount(sheetIndex);

		boolean isWithinRange = (startRowNum <= maxRows && endRowNum >= maxRows);

		if (!isWithinRange) {
			throw new ArrayIndexOutOfBoundsException("Invalid row range : StartRowNum '" + startRowNum
					+ "' and endRowNum '" + endRowNum + "'. Max rows are '" + maxRows + "'");
		}
		
		List<Row> rowsList = new ArrayList<Row>();

		for (int currentRowIndex = startRowNum; currentRowIndex <= endRowNum; currentRowIndex++) {

			Row row = this.getRow(sheetIndex, currentRowIndex);
			
			rowsList.add(row);
		}

		return rowsList;
	}
	

	/**
	 * Gets the row 
	 * @param sheetName Name of the work sheet
	 * @param startRowNum Row index starts from
	 * @param endRowNum Row index upto 
	 * @return List of rows
	 */
	public List<Row> getRows(String sheetName, int startRowNum, int endRowNum) {
		
		int sheetIndex = getSheetIndex(sheetName);
		
		List<Row> rowsList = getRows(sheetIndex, startRowNum, endRowNum);
		
		return rowsList;
	}

	/**
	 * Gets the column data.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param columnNumber
	 *            the column number
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
	}

	/**
	 * Gets the column data.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param columnNumber
	 *            the column number
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
	 * @param sheetName
	 *            Name of the work sheet
	 * @return the column headers
	 */
	// get headers
	public String[] getColumnHeaders(String sheetName) {
		// get the header row
		Row row = getRow(sheetName, 0);
		// get number of columns
		int columnCount = row.getLastCellNum();
		String[] headers = new String[columnCount];
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			headers[columnIndex] = row.getCell(columnIndex).toString();
		}
		return headers;
	}

	/**
	 * Gets the column headers.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @return the column headers
	 */
	// get headers
	public String[] getColumnHeaders(int sheetIndex) {
		// get the header row
		Row row = getRow(sheetIndex, 0);
		// get number of columns
		int columnCount = row.getLastCellNum();
		String[] headers = new String[columnCount];
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			headers[columnIndex] = row.getCell(columnIndex).toString();
		}
		return headers;
	}

	/**
	 * Gets the cell data.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number
	 * @param columnNumber
	 *            the column number
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
				CellType cellType = cell.getCellTypeEnum();

				if (cellType == CellType.STRING) {
					cellValue = cell.toString().trim();
				} else if (cellType == CellType.NUMERIC) {
					if (cell.getCellStyle().getDataFormatString().contains("%")) {
						cellValue = Double.toString(cell.getNumericCellValue() * 100) + "%";
					} else {
						cellValue = Double.toString(cell.getNumericCellValue());
					}
				} else if (cellType == CellType.BOOLEAN) {
					cellValue = String.valueOf(cell.getBooleanCellValue());
				}
			} catch (NullPointerException NPE) {
				cellValue = "";
			}
		} else {
			this.logAccess.getLogger().warn("Cell :  \"R" + String.valueOf(rowNumber) + "C"
					+ String.valueOf(columnNumber) + "\"  is out of range.");
			throw new ArrayIndexOutOfBoundsException("Cell :  \"R" + String.valueOf(rowNumber) + "C"
					+ String.valueOf(columnNumber) + "\"  is out of range.");
		}
		return cellValue;
	}

	/**
	 * Gets the cell data.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number
	 * @param columnNumber
	 *            the column number
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
		}
		return cellValue;
	}

	// get cell data based on sheet name and column name
	/**
	 * Gets the cell data.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number
	 * @param columnName
	 *            the column name
	 * @return the cell data
	 */
	// get cell value by header
	public String getCellData(String sheetName, int rowNumber, String columnName) {
		String cellValue = null;
		// check if the rowNumber is with in the limit of available rows
		if (rowNumber <= getRowCount(sheetName)) {
			// get row based on the sheet name and row index
			Row row = getRow(sheetName, rowNumber);
			cellValue = row.getCell(getColumnHeaderIndex(sheetName, columnName)).toString();
		} else {
			// throw error that rowNumber is > rows count in the sheet
			throw new ArrayIndexOutOfBoundsException();
		}
		return cellValue;
	}
	
	/**
	 * Gets the cell data 
	 * @param sheetName Name of the work sheet
	 * @param row Excel row
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(String sheetName, Row row, String columnName) {
		
		int sheetIndex = getSheetIndex(sheetName);
		
		String cellValue = getCellData(sheetIndex, row, columnName);
		
		this.logAccess.getLogger().debug("Current cell value is '" + cellValue + "'");
		
		return cellValue;
	}
	
	/**
	 * Gets the cell data 
	 * @param sheetIndex Sheet Index of the work sheet
	 * @param row Excel row
	 * @param columnName the column name
	 * @return the cell data
	 */
	public String getCellData(int sheetIndex, Row row, String columnName) {
		String cellValue = "";
		
		try {
			int columnNumber = getColumnHeaderIndex(sheetIndex, columnName);
			
			// get row based on the sheet name and row index
			Cell cell = row.getCell(columnNumber);
			CellType cellType = cell.getCellTypeEnum();

			if (cellType == CellType.STRING) {
				cellValue = cell.toString().trim();
			} else if (cellType == CellType.NUMERIC) {
				if (cell.getCellStyle().getDataFormatString().contains("%")) {
					cellValue = Double.toString(cell.getNumericCellValue() * 100) + "%";
				} else {
					cellValue = Double.toString(cell.getNumericCellValue());
				}
			} else if (cellType == CellType.BOOLEAN) {
				cellValue = String.valueOf(cell.getBooleanCellValue());
			}
		} catch (NullPointerException NPE) {
			cellValue = "";
		}
	
		return cellValue;
	}
	
	
	// get row data return map

	// get sheet data

	/**
	 * Gets the row data.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number
	 * @return the row data
	 */
	public Map<String, String> getRowData(String sheetName, int rowNumber) {
		Map<String, String> map = new HashMap<String, String>();
		// Row row = getRow(sheetName, rowNumber); //TODO - Remove this line if not
		// using the row
		// get column headers information
		String[] headers = getColumnHeaders(sheetName);
		String columnName, value;
		for (int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
			// Print Excel data in console
			columnName = headers[columnIndex];
			value = getCellData(sheetName, rowNumber, columnIndex);
			// value = row.getCell(columnIndex).toString(); //TODO - need to check the
			// resources usage using row approach
			map.put(columnName, value);
		}
		return map;
	}

	/**
	 * Gets the row data.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number
	 * @return the row data
	 */
	public Map<String, String> getRowData(int sheetIndex, int rowNumber) {
		Map<String, String> map = new HashMap<String, String>();
		// Row row = getRow(sheetName, rowNumber); //TODO - Remove this line if not
		// using the row
		// get column headers information
		String[] headers = getColumnHeaders(sheetIndex);
		String columnName, value;
		for (int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
			// Print Excel data in console
			columnName = headers[columnIndex];
			value = getCellData(sheetIndex, rowNumber, columnIndex);
			// value = row.getCell(columnIndex).toString(); //TODO - need to check the
			// resources usage using row approach
			map.put(columnName, value);
		}
		return map;
	}

	/*
	 * Write
	 */
	/**
	 * Write cell data.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number (starts with 0)
	 * @param columnNumber
	 *            the column number (starts with 0)
	 * @param value
	 *            the value
	 * @param style
	 *            the style
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	// write value in cell
	public void writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			int columnNumber, Object value, String style) throws IOException {
		Cell cell;
		if (rowNumber > getRowCount(sheetName)) {
			// create a new row and append the data
			cell = getSheet(sheetName).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row
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
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number (starts with 0)
	 * @param columnNumber
	 *            the column number (starts with 0)
	 * @param value
	 *            the value
	 * @param color
	 *            the color
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			int columnNumber, Object value, String color) throws IOException {
		Cell cell;
		if (rowNumber > getRowCount(sheetIndex)) {
			// create a new row and append the data
			cell = getSheet(sheetIndex).createRow(rowNumber).createCell(columnNumber);
		} else { // update the data to the existing row
			cell = getRow(sheetIndex, rowNumber).getCell(columnNumber);
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}

		// XSSFCellStyle iSatzCellStyle = wb.createCellStyle();
		// iSatzCellStyle.setBorderTop(BorderStyle.THIN);
		// iSatzCellStyle.setBorderBottom(BorderStyle.THIN);
		// iSatzCellStyle.setBorderLeft(BorderStyle.THIN);
		// iSatzCellStyle.setBorderRight(BorderStyle.THIN);
		// if (color.equalsIgnoreCase("GREEN")) {
		// iSatzCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
		// iSatzCellStyle.setFillPattern(FillPatternType.LESS_DOTS);
		// } else if (color.equalsIgnoreCase("RED")) {
		// iSatzCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
		// iSatzCellStyle.setFillPattern(FillPatternType.LESS_DOTS);
		// } else if (color.equalsIgnoreCase("GOLD")) {
		// iSatzCellStyle.setFillBackgroundColor(IndexedColors.GOLD.getIndex());
		// iSatzCellStyle.setFillPattern(FillPatternType.LESS_DOTS);
		// } else if (color.equalsIgnoreCase("NONE")) {
		// }
		saveWorkBook(excelFilePath, excelFileName);
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number (starts with 0)
	 * @param columnName
	 *            the column name
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeCellData(String excelFilePath, String excelFileName, String sheetName, int rowNumber,
			String columnName, Object value) throws IOException {
		Cell cell;
		if (rowNumber > getRowCount(sheetName)) {
			// create a new row and append the data
			cell = getSheet(sheetName).createRow(rowNumber).createCell(getColumnHeaderIndex(sheetName, columnName));
		} else { // update the data to the existing row
			cell = getRow(sheetName, rowNumber).getCell(getColumnHeaderIndex(sheetName, columnName));
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}
		saveWorkBook(excelFilePath, excelFileName);
	}

	/**
	 * Write cell data.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number (starts with 0)
	 * @param columnName
	 *            the column name
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeCellData(String excelFilePath, String excelFileName, int sheetIndex, int rowNumber,
			String columnName, Object value) throws IOException {
		Cell cell;
		if (rowNumber > getRowCount(sheetIndex)) {
			// create a new row and append the data
			cell = getSheet(sheetIndex).createRow(rowNumber).createCell(getColumnHeaderIndex(sheetIndex, columnName));
		} else { // update the data to the existing row
			cell = getRow(sheetIndex, rowNumber).getCell(getColumnHeaderIndex(sheetIndex, columnName));
		}
		if (value instanceof Integer) {
			// set value as integer
			cell.setCellValue((Integer) value);
		} else {
			// set value as String for anything other than Integer
			cell.setCellValue((String) value);
		}
		saveWorkBook(excelFilePath, excelFileName);
	}

	/*
	 * Delete
	 */
	/**
	 * Delete row.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param rowNumber
	 *            the row number
	 */
	// delete row
	public void deleteRow(String sheetName, int rowNumber) {
		getSheet(sheetName).removeRow(getRow(sheetName, rowNumber));
	}

	/**
	 * Delete row.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param rowNumber
	 *            the row number
	 */
	public void deleteRow(int sheetIndex, int rowNumber) {
		getSheet(sheetIndex).removeRow(getRow(sheetIndex, rowNumber));
	}

	/**
	 * Delete column.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param columnNumber
	 *            the column number
	 */
	// delete cell
	public void deleteColumn(int sheetIndex, int columnNumber) {
		deleteColumnAndRetainColumnWidth(sheetIndex, columnNumber);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param columnName
	 *            the column name
	 */
	public void deleteColumn(int sheetIndex, String columnName) {
		int columnIndex = getColumnHeaderIndex(sheetIndex, columnName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnIndex);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param columnNumber
	 *            the column number
	 */
	// delete column
	public void deleteColumn(String sheetName, int columnNumber) {
		int sheetIndex = getSheetIndex(sheetName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnNumber);
	}

	/**
	 * Delete column.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 * @param columnName
	 *            the column name
	 */
	public void deleteColumn(String sheetName, String columnName) {
		int sheetIndex = getSheetIndex(sheetName);
		int columnIndex = getColumnHeaderIndex(sheetIndex, columnName);
		deleteColumnAndRetainColumnWidth(sheetIndex, columnIndex);
	}

	/**
	 * Delete file.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @throws Exception
	 *             the exception
	 */
	// delete file
	public void deleteFile(String excelFilePath, String excelFileName) throws Exception {
		FolderFileUtil ffUtils = new FolderFileUtil(this.logAccess);
		ffUtils.deleteFileOrFolder(excelFilePath, excelFileName);
	}

	/**
	 * Delete sheet based on the sheet name.
	 *
	 * @param sheetName
	 *            Name of the work sheet
	 */
	// delete sheet
	public void deleteSheet(String sheetName) {
		int sheetIndex = getSheetIndex(sheetName);
		deleteSheet(sheetIndex);
	}

	/**
	 * Delete sheet based on the sheet index.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 */
	public void deleteSheet(int sheetIndex) {
		wb.removeSheetAt(sheetIndex);
	}

	/**
	 * Save work book.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	// save workbook
	public void saveWorkBook(String excelFilePath, String excelFileName) throws IOException {
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(excelFilePath + File.pathSeparator + excelFileName);
		wb.write(outputStream);
		// close the workbook
		closeWorkBook();
	}

	/**
	 * Close work book.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
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
	 * Gets the work book based on the excel file path and name.
	 *
	 * @param excelFilePath
	 *            the excel file path
	 * @param excelFileName
	 *            the excel file name
	 * @return the work book
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Workbook openWorkBook(String excelFilePath, String excelFileName) throws IOException {
		// Create an object of File class to open xlsx file
		File file = new File(excelFilePath + File.separatorChar + excelFileName);
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
	 * Delete column and retain column width.
	 *
	 * @param sheetIndex
	 *            the index of the sheet number (0-based physical & logical)
	 * @param columnNumber
	 *            the column number
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
				this.logAccess.getLogger().debug("recieved IOException, please check the trace for more informtaion.");
				e.printStackTrace();
			}
			this.wb = null;
		}
		this.wb = wb;
	}

}
