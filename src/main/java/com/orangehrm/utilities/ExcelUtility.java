package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	public static List<String[]> getSheetData(String filePath, String sheetName) {

		// data variable is defined as a list of arrays of string
		List<String[]> data = new ArrayList<>();

		//
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exists");
			}

			// iterate through rows
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				// Read all cells in the row
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValue(cell));
				}
				// convert rowData to String[]
				data.add(rowData.toArray(new String[0]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((int) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}

	}

	public static String getCellData(String filePath, String sheetName, int rowNum, int colNum) {

		try {
			FileInputStream fis = new FileInputStream(filePath);
			Workbook workbook = new XSSFWorkbook(fis);

			Sheet sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(rowNum);
			Cell cell = row.getCell(colNum);

			DataFormatter formatter = new DataFormatter();
			String value = formatter.formatCellValue(cell);

			workbook.close();
			fis.close();

			return value;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void writeData(String filePath, String sheetName, int rowNum, int colNum, String data) {

		try {
			FileInputStream fis = new FileInputStream(filePath);
			Workbook workbook = new XSSFWorkbook(fis);

			Sheet sheet = workbook.getSheet(sheetName);

			Row row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);

			Cell cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);

			FileOutputStream fos = new FileOutputStream(filePath);
			workbook.write(fos);

			workbook.close();
			fis.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
