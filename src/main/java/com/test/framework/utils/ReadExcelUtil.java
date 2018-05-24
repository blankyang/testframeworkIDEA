package com.test.framework.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public class ReadExcelUtil {

	private static Workbook wb;

	@SuppressWarnings("resource")
	public static List<Map<String, String>> readXlsx(String fileName,
			String SheetName) throws FileNotFoundException {
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook hssfWorkbook = null;
		try {
			hssfWorkbook = new HSSFWorkbook(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取工作表
		//HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(SheetCount);
		HSSFSheet hssfSheet = hssfWorkbook.getSheet(SheetName);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 循环行Row
		HSSFRow rowTitleRow = hssfSheet.getRow(0);
		for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			if (hssfRow == null) {
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
			// 循环列Cell
			for (int cellNum = 0; cellNum < rowTitleRow.getLastCellNum(); cellNum++) {
				HSSFCell hssfCell = hssfRow.getCell(cellNum);
				HSSFCell hssfCellTitleCell = rowTitleRow.getCell(cellNum);
				map.put(getValue(hssfCellTitleCell), getValue(hssfCell));
			}
			list.add(map);

		}
		return list;
	}

	@SuppressWarnings("deprecation")
	private static String getValue(HSSFCell hssfCellTitleCell) {
		if (hssfCellTitleCell == null) {
			return "";
		} else {
			hssfCellTitleCell.setCellType(Cell.CELL_TYPE_STRING);
			return String.valueOf(hssfCellTitleCell.getStringCellValue());
		}
	}

	public static Workbook ReadExcelUtils(String filepath) {
		String ext = filepath.substring(filepath.lastIndexOf("."));
		try {
			InputStream is = new FileInputStream(filepath);
			if (".xls".equals(ext)) {
				wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(ext)) {
				// wb = new XSSFWorkbook(is);
			} else {
				wb = null;
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return wb;
	}

}