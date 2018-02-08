/**
 * 
 */
package dlt.study.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import static dlt.study.excel.WriteExcel.outputFile;

/**
 * @author dlt	Aug 21, 2007
 *
 */
public class ReadExcel {
	static String fileName=  "c:\\\\副本规划预案导入模板－茂名.xls";
	static String fileName2= "c:\\\\副本规划预案导入模板－茂名2.xls";
	public static void main(String[] args){
		//readExcel();
		modifyExcel();
	}
	public static void readExcel(){
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheetAt(0);
		    for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i ++) {
		    	HSSFRow row = sheet.getRow(i);
		    	HSSFCell cell = row.getCell((short)0);
		    	System.out.print((i) + "行");
		    	//System.out.print(":" + cell.getRichStringCellValue());
		    	System.out.print(":" + cell.getStringCellValue());
		    	cell = row.getCell((short)1);
		    	//System.out.print(":" + cell.getRichStringCellValue());
		    	System.out.print(":" + cell.getStringCellValue());
		    	System.out.println();
		    }
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void modifyExcel(){
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs,true);

		    HSSFSheet sheet = wb.getSheetAt(0);
		    for (int i=5; i <= sheet.getLastRowNum();i++){
		    	HSSFRow row = sheet.getRow(i);
			    HSSFCell  cell = row.getCell((short)0);
			    System.out.println(cell.getRichStringCellValue());
			    if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
			    	System.out.println(cell.getCellFormula());
			    }
				HSSFCellStyle cellStyle = wb.createCellStyle();
				// cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
				cellStyle.setFillForegroundColor(HSSFColor.RED.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(cellStyle);
				HSSFRichTextString text = cell.getRichStringCellValue();
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(text.getString()+"-dlt"));
				
				if (i>8){
					break;
				}
		    }
			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel 工作簿存盘
			wb.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
