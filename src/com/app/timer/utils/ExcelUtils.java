package com.app.timer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.timer.business.util.Empty;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月6日 下午4:44:44 类说明:Excel文件操作工具类，基于POI封装
 */
public class ExcelUtils {

	/**默认最大允许导入的Excel行数*/
	public static long allowmaxrows=1000;
	
	/**默认最大每一页显示个数*/
	public static long pageNumbers=15;
	
	/**
	 *将上传的Excel文件流解析为json字符串,针对07版本的Excel
	 * @param excelstream
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> readExcelByXlsx(InputStream excelstream) throws Exception {
		Map<String,Object> dataMap=new HashMap<String,Object>();
		XSSFWorkbook workbook;
		// 表格的头信息
		List<String> tableTitle = new ArrayList<String>();
		//List<List<String>> bodyContent = new ArrayList<List<String>>();
		List<Map<String,String>> bodyContent = new ArrayList<Map<String,String>>();
		try {
			workbook = new XSSFWorkbook(excelstream);
			XSSFFormulaEvaluator evaluator=workbook.getCreationHelper().createFormulaEvaluator();   
			// 根据名字获得工作标签页
			// Sheet sheet=workbook.getSheet("Sheet0");
			// 根据位置读取标签页 默认都是读取第1个sheet
			final Sheet sheet = workbook.getSheetAt(0);
			final int firstRowNumber = sheet.getFirstRowNum();
			final int lastRowNumber = sheet.getLastRowNum();// 获取最后一行
			
			//如果Excel的允许导入的最大行超过规定  则不导入
			if((lastRowNumber+1)>allowmaxrows){
				throw new Exception("ExcelUtils.readExcel方法读取文件行数发现比管理员设置的值大，抛出异常");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat  df= new DecimalFormat("#.######");  
			for (int i = firstRowNumber; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				// 获得当前行最后一个单元格号
				int firstCellNumber = row.getFirstCellNum();
				int lastCellNumber = row.getLastCellNum();
				
				if(i==0){
					dataMap.put("rows", lastRowNumber);
					dataMap.put("cells", lastCellNumber);
				}
				//保存每一行记录
				//List<String> rowData=new ArrayList<String>();
				Map<String,String> cellMap=new HashMap<String,String>();
				for (int j = firstCellNumber; j < lastCellNumber; j++) {
					Cell cell = row.getCell(j);
					if (!Empty.isEmpty(cell)) {
						int cellType = cell.getCellType();
						String value ="";
						if(cellType==HSSFCell.CELL_TYPE_NUMERIC){//CELL_TYPE_NUMERIC 数值型 0
						    if (HSSFDateUtil.isCellDateFormatted(cell)) {  
							   value= formatter.format(cell.getDateCellValue());
	                        } else {  
	                            value = df.format(cell.getNumericCellValue());
	                        }
						}else if(cellType==HSSFCell.CELL_TYPE_STRING){//CELL_TYPE_STRING 字符串型 1
							value = cell==null?"":cell.getStringCellValue();
						}else if(cellType==HSSFCell.CELL_TYPE_FORMULA){//CELL_TYPE_FORMULA 公式型 2
							CellValue tempCellValue = evaluator.evaluate(cell);  
					        value= getCellValue(tempCellValue); 
						}else if(cellType==HSSFCell.CELL_TYPE_BLANK){//CELL_TYPE_BLANK 空值 3
							 value="NULL";
						}else if(cellType==HSSFCell.CELL_TYPE_BOOLEAN){//CELL_TYPE_BOOLEAN 布尔型 4
							 value=String.valueOf(cell.getBooleanCellValue());
						}else if(cellType==HSSFCell.CELL_TYPE_ERROR){//CELL_TYPE_ERROR 
							 value="ERROR";
						}else{
							value = cell.toString().trim();  
						}
						if (i == 0) {// 表示在读取表格的头信息 第一行
							tableTitle.add(value);
						}else{
							cellMap.put(""+j+"", value);
							//rowData.add(cellMap);
						}
					} else {
						if (i != 0) {
							cellMap.put(""+j+"", "");
						}
					}
				}
				if(!cellMap.isEmpty()){
					bodyContent.add(cellMap);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				excelstream.close();
				//System.out.println(bodyContent.size());
				//System.out.println(bodyContent.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		dataMap.put("tableTitle", tableTitle);
		dataMap.put("tableBody", bodyContent);
		return dataMap;
	}
	
	/**
	 *将上传的Excel文件流解析为json字符串,针对97-03版本的Excel
	 * @param excelstream
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> readExcelByXls(InputStream excelstream) throws Exception {
		Map<String,Object> dataMap=new HashMap<String,Object>();
		HSSFWorkbook workbook = null;
		// 表格的头信息
		List<String> tableTitle = new ArrayList<String>();
		List<Map<String,String>> bodyContent = new ArrayList<Map<String,String>>();
		try {
			workbook = new HSSFWorkbook(excelstream);
			HSSFFormulaEvaluator evaluator=workbook.getCreationHelper().createFormulaEvaluator();
			
			// 根据名字获得工作标签页
			// Sheet sheet=workbook.getSheet("Sheet0");
			// 根据位置读取标签页 默认都是读取第1个sheet
			final Sheet sheet = workbook.getSheetAt(0);
			final int firstRowNumber = sheet.getFirstRowNum();
			final int lastRowNumber = sheet.getLastRowNum();// 获取最后一行
			
			//如果Excel的允许导入的最大行超过规定  则不导入
			if((lastRowNumber+1)>allowmaxrows){
				throw new Exception("ExcelUtils.readExcelByXls方法读取文件行数发现比管理员设置的值大，抛出异常");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat  df= new DecimalFormat("#.######");  
			for (int i = firstRowNumber; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				// 获得当前行最后一个单元格号
				int firstCellNumber = row.getFirstCellNum();
				int lastCellNumber = row.getLastCellNum();
				
				if(i==0){
					dataMap.put("rows", lastRowNumber);
					dataMap.put("cells", lastCellNumber);
				}
				//保存每一行记录
				//List<String> rowData=new ArrayList<String>();
				Map<String,String> cellMap=new HashMap<String,String>();
				for (int j = firstCellNumber; j < lastCellNumber; j++) {
					Cell cell = row.getCell(j);
					if (!Empty.isEmpty(cell)) {
						int cellType = cell.getCellType();
						String value ="";
						if(cellType==HSSFCell.CELL_TYPE_NUMERIC){//CELL_TYPE_NUMERIC 数值型 0
						    if (HSSFDateUtil.isCellDateFormatted(cell)) {  
							   value= formatter.format(cell.getDateCellValue());
	                        } else {  
	                            value = df.format(cell.getNumericCellValue());
	                        }
						}else if(cellType==HSSFCell.CELL_TYPE_STRING){//CELL_TYPE_STRING 字符串型 1
							value = cell==null?"":cell.getStringCellValue();
						}else if(cellType==HSSFCell.CELL_TYPE_FORMULA){//CELL_TYPE_FORMULA 公式型 2
							CellValue tempCellValue = evaluator.evaluate(cell);  
					        value= getCellValue(tempCellValue); 
						}else if(cellType==HSSFCell.CELL_TYPE_BLANK){//CELL_TYPE_BLANK 空值 3
							 value="NULL";
						}else if(cellType==HSSFCell.CELL_TYPE_BOOLEAN){//CELL_TYPE_BOOLEAN 布尔型 4
							 value=String.valueOf(cell.getBooleanCellValue());
						}else if(cellType==HSSFCell.CELL_TYPE_ERROR){//CELL_TYPE_ERROR 
							 value="ERROR";
						}else{
							value = cell.toString().trim();  
						}
						if (i == 0) {// 表示在读取表格的头信息 第一行
							tableTitle.add(value);
						}else{
							cellMap.put(""+j+"", value);
							//rowData.add(cellMap);
						}
					} else {
						if (i != 0) {
							cellMap.put(""+j+"", "");
						}
					}
				}
				if(!cellMap.isEmpty()){
					bodyContent.add(cellMap);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				excelstream.close();
				//System.out.println(bodyContent.size());
				//System.out.println(bodyContent.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		dataMap.put("tableTitle", tableTitle);
		dataMap.put("tableBody", bodyContent);
		return dataMap;
	}
	
	/**
	 * 获取值
	 * @param cell
	 * @return
	 */
	 private static String getCellValue(CellValue cell) {
	        String cellValue = null;
	        switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_STRING:
	            //System.out.print("String :");
	            cellValue=cell.getStringValue();
	            break;
	        case Cell.CELL_TYPE_NUMERIC:
	        	try {
					cellValue = new DecimalFormat("#.####").format(cell.getNumberValue());
				} catch (IllegalStateException e) {
					cellValue = String.valueOf(cell.getStringValue());
				}
			break;
	        case Cell.CELL_TYPE_FORMULA:
	            //System.out.print("FORMULA:");
	            break;
	        default:
	            break;
	        }
	        return cellValue;
	 }
	
	
}
