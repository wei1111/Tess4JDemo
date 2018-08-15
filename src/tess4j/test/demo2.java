package tess4j.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

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

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import util.Png2Jpg;

public class demo2 {
	public static void main(String args[]) throws InterruptedException {

		long millis1 = System.currentTimeMillis();
		final Map<Integer, String> resultMap1 = new HashMap<Integer, String>();
		final Map<Integer, String> resultMap2 = Collections
				.synchronizedMap(resultMap1);

		ExecutorService service = Executors.newCachedThreadPool();
		final CyclicBarrier cb = new CyclicBarrier(10, new Runnable() {
			@Override
			public void run() {
				Set<Map.Entry<Integer, String>> entry = resultMap2.entrySet();
				Iterator<Map.Entry<Integer, String>> iterator = entry
						.iterator();
				while (iterator.hasNext()) {
					Map.Entry<Integer, String> en = iterator.next();
					System.out.println(en.getKey());
					System.out.println(en.getValue());
				}
			}
		});
		readTT read = new readTT(millis1, resultMap2, cb);

		int n = 10;
		Thread[] threads = new Thread[n];
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(read, "线程" + i);
			service.execute(threads[i]);
		}
		service.shutdown();

		long millis2 = System.currentTimeMillis();
		System.out.println("(millis2 - millis1)----------->"
				+ (millis2 - millis1));
	}
}

class readTT implements Runnable {
	List<File> filePathsList = new ArrayList<File>();
	Map<Integer, String> resultMap = null;
	CyclicBarrier cb2;

	int index = 0;
	private long millis;

	public readTT(long millis, Map<Integer, String> resultMap, CyclicBarrier cb1) {
		this.resultMap = Collections.synchronizedMap(resultMap);
		this.millis = millis;
		this.cb2 = cb1;
		File f = new File("C:\\Users\\12044\\Desktop\\桶\\天猫工商信息执照");
		getFileList(f);
	}

	private void getFileList(File f) {
		File[] filePaths = f.listFiles();
		for (File s : filePaths) {
			// System.out.println(s.getName() + "---" + s.getPath());
			if (s.isDirectory()) {
				getFileList(s);
			} else {
				if (-1 != s.getName().lastIndexOf(".png")) {
					filePathsList.add(s);
				}
			}
		}
	}

	@Override
	public void run() {
		File file = null;
		InputStream is = null;
		BufferedImage image = null;
		int beginIndex;
		int endIndex;
		while (index < filePathsList.size()) {
			synchronized (this) {
				if (index >= filePathsList.size()) {
					return;
				}
				file = filePathsList.get(index);
				index++;
			}
			try {
				ITesseract instance = new Tesseract();
				instance.setDatapath("");
				instance.setLanguage("chi_sim+eng");

				is = new FileInputStream(file.getPath());
				image = ImageIO.read(is);

				image = Png2Jpg.png2Jpg(image, 0, 8, 533, 67);
				image = ImageHelper.convertImageToBinary(image);

				image = ImageHelper.getScaledInstance(image, 536 * 2, 67 * 2);

				String result = instance.doOCR(image);
				System.out.println(index + "------------>" + result);

				// System.out.println("企-----"+result.indexOf("企"));
				// System.out.println("二-----"+result.indexOf("二"));
				// System.out.println("9-----"+result.indexOf("9"));
				// System.out.println(result.indexOf("\\"));
				// System.out.println(result.lastIndexOf("二"));

				beginIndex = result.indexOf("二");
				endIndex = result.lastIndexOf("企");
				String busName = result.substring(result.lastIndexOf("二") + 1);
				String busId = result.substring(beginIndex + 1, endIndex - 1);
				System.out.println(busId);
				resultMap.put(index, result);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();

					// long millis2 = System.currentTimeMillis();
					// System.out.println("millis2-------->" + millis2);
					// System.out.println("(millis2 - millis)----------->"
					// + (millis2 - millis));

					cb2.await();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Workbook creatExcel(List<List<String>> lists,
			String[] titles, String excelExtName) throws Exception {
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
		Sheet sheet = wb.createSheet("biao1");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < titles.length; i++) {
			// sheet.setColumnWidth((short) i, (short) (35.7 * 150));
			sheet.setColumnWidth((short) i, (short) (35 * 150));
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
		for (short i = 1; i < lists.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((short) i);
			for (short j = 0; j < titles.length; j++) {
				// 在row行上创建一个方格
				Cell cell = row1.createCell(j);
				cell.setCellValue(lists.get(i).get(j));
				System.out.println(lists.get(i).get(j));
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}

}
