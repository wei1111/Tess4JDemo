package util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreatExcel {
	public static Workbook creatExcel(List<List<? extends Object>> lists, String[] titles, String excelExtName,
			String sheetName) throws Exception {
		// System.out.println(lists);
		// 创建新的工作薄
		Workbook wb = null;
		if ("xls".equals(excelExtName)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(excelExtName)) {
			wb = new XSSFWorkbook();
		} else {
			throw new Exception("当前文件不是excel文件");
		}
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(sheetName);
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < titles.length; i++) {
			if (i == 0) {
				sheet.setColumnWidth((short) i, (short) (35 * 100));
				// sheet.setColumnWidth((short) i, (short) (35 * 300));
			} else {
				sheet.setColumnWidth((short) i, (short) (35 * 300));
			}
		}

		// 创建第一行
		Row row = sheet.createRow((short) 0);

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBold(true);
		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(BorderStyle.THIN);
		cs.setBorderRight(BorderStyle.THIN);
		cs.setBorderTop(BorderStyle.THIN);
		cs.setBorderBottom(BorderStyle.THIN);
		cs.setAlignment(HorizontalAlignment.CENTER);

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(BorderStyle.THIN);
		cs2.setBorderRight(BorderStyle.THIN);
		cs2.setBorderTop(BorderStyle.THIN);
		cs2.setBorderBottom(BorderStyle.THIN);
		cs2.setAlignment(HorizontalAlignment.CENTER);
		// 设置列名
		for (int i = 0; i < titles.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(cs);
		}
		if (lists == null || lists.size() == 0) {
			return wb;
		}
		// 设置每行每列的值
		
		// System.out.println("共识别图片" + lists.size() + "张");
		
		for (short i = 1; i <= lists.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((short) i);
			for (short j = 0; j < titles.length; j++) {
				// 在row行上创建一个方格
				Cell cell = row1.createCell(j);
				cell.setCellValue((String) lists.get(i - 1).get(j));
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}
}
