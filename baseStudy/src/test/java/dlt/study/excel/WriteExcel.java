/**
 * 
 */
package dlt.study.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author dlt Aug 21, 2007
 * 
 */
public class WriteExcel {
	static String outputFile = "c:\\test.xls";

	public static void main(String[] args) {
		//creatExcel();
		modifyExcel();
	}

	public static void creatExcel() {
		try {
			// 创建新的Excel 工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 在Excel工作簿中建一工作表，其名为缺省值
			// 如要新建一名为"效益指标"的工作表，其语句为：
			HSSFSheet sheet = workbook.createSheet("效益指标");
			//workbook.setSheetName(n, "中文",HSSFCell.ENCODING_UTF_16);
			// 在索引0的位置创建行（最顶端的行）
			HSSFRow row = sheet.createRow((short) 0);
			// 在索引0的位置创建单元格（左上端）
			HSSFCell cell = row.createCell((short) 0);
			// 定义单元格为字符串类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			// 在单元格中输入一些内容
			HSSFRichTextString text = new HSSFRichTextString("增加值");
			cell.setCellValue(text);
			// 新建一输出文件流
			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
			System.out.println("文件生成...");
		} catch (Exception e) {
			System.out.println("已运行 xlCreate() : " + e);
		}
	}

	public static void modifyExcel() {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(outputFile));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				HSSFCell cell = row.getCell((short) 0);
				System.out.println(cell.getRichStringCellValue());
				if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
					System.out.println(cell.getCellFormula());
				}
				HSSFCellStyle cellStyle = wb.createCellStyle();

/*				cellStyle.setFillForegroundColor(HSSFColor.RED.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);*/
				cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
				cellStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);

				cell.setCellStyle(cellStyle);
				HSSFRichTextString text = cell.getRichStringCellValue();
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(text.getString() + "-dlt"));
			}
			HSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
			HSSFCell newCell = newRow.createCell((short) 0);
			newCell.setCellValue(new Date());
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			newCell = newRow.createCell((short) 1);
			newCell.setCellStyle(cellStyle);
			newCell.setCellValue(new Date());
			newRow.createCell((short) 3).setCellValue(true);
			newRow.createCell((short) 4).setCellType(HSSFCell.CELL_TYPE_ERROR);

			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel 工作簿存盘
			wb.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
			System.out.println("文件生成...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
