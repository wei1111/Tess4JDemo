package ImageDemo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import util.CreatExcel;
import util.ImageProcess;
import util.ListListSortComparator;
import util.ResultProcess;

public class ImageDemo {
	public static String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	// public ImageDemo(){
	//
	// }
	public List<String> runImageProcess(String imagePath, String resultExcelName, int n, int x, int y, int width,
			int height, String[] titles) throws Exception {
		// 线程数
		List<String> list = new ArrayList<String>();

		// int n = 9;

		// 截取图片的x轴y轴开始坐标
		// int x = 0;
		// int y = 8;

		// 截取图片的x轴y轴长度
		// int width = 533;
		// int height = 67;

		// 图片的位置
		// String imagePath = "C:\\天猫工商信息执照";

		// 开始时间
		long millis1 = System.currentTimeMillis();

		// Excel的列名
		// String[] titles = { "图片名称", "公司名称", "公司注册号" };
		// String[] titles = { "公司名称", "公司注册号" };

		// 生成的Excel名称
		// String resultExcelName = "C:\\天猫工商信息执照\\天猫工商信息执照识别结果.xlsx";

		// 使用CountDownLatch线程工具类进行线程控制
		final CountDownLatch latch = new CountDownLatch(n);

		List<List<? extends Object>> lists = new ArrayList<List<? extends Object>>();

		lists = Collections.synchronizedList(lists);
		processImage read = new processImage(lists, x, y, width, height, latch, imagePath);

		Thread[] threads = new Thread[n];
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(read, "线程" + i);
			threads[i].start();
		}
		latch.await();
		// 挂起主线程，等待其他线程处理完毕

		// 排序,ListListSortComparator为抽象类
		Collections.sort(lists, new ListListSortComparator(0, "asc") {
		});

		int successSize = read.resultProcess.successSize;
		// System.out.println("扫描图片共：" + read.imageSize + "张");
		// System.out.println("成功识别：" + successSize + "张");
		// System.out.println("识别率：" + (successSize * 100.0) / read.imageSize +
		// "%");
		String scanSize_Str = "扫描图片共：" + read.imageSize + "张" + LINE_SEPARATOR;
		String successSize_Str = "成功识别：" + successSize + "张" + LINE_SEPARATOR;
		// String successPercent_Str = "识别率：" + (successSize * 100.0) /
		// read.imageSize + "%" + LINE_SEPARATOR;

		list.add(scanSize_Str);
		list.add(successSize_Str);
		// list.add(successPercent_Str);

		OutputStream os = new FileOutputStream(new File(resultExcelName));

		// 对结果使用图片名升序进行排列
		CreatExcel.creatExcel(lists, titles, "xlsx", "企业信息表").write(os);
		os.close();

		// 结束时间
		long millis2 = System.currentTimeMillis();

		// 计算总耗时
		// System.out.println("耗时-------->" + (millis2 - millis1) / 1000.0 +
		// "秒");
		String timeExpend = "耗时-------->" + (millis2 - millis1) / 1000.0 + "秒" + LINE_SEPARATOR;
		list.add(timeExpend);
		return list;
	}

}

class processImage implements Runnable {
	// 图片名称集合
	List<File> filePathsList = new ArrayList<File>();
	List<List<? extends Object>> lists = null;
	int index = 0;
	int x, y, width, height;
	Object obj = new Object();
	CountDownLatch latch;
	int imageSize;
	ResultProcess resultProcess;

	public processImage(List<List<? extends Object>> lists, int x, int y, int width, int height, CountDownLatch latch,
			String imagePath) {
		this.lists = lists;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		// 图片所在地址
		File f = new File(imagePath);
		getFileList(f);
		this.latch = latch;
	}

	private int getFileList(File f) {
		File[] filePaths = f.listFiles();
		for (File s : filePaths) {
			if (s.isDirectory()) {
				getFileList(s);
			} else {
				if ((-1 != s.getName().lastIndexOf(".png")) || (-1 != s.getName().lastIndexOf(".jpg"))
						|| (-1 != s.getName().lastIndexOf(".jpeg"))) {
					filePathsList.add(s);
				}
			}
		}
		imageSize = filePathsList.size();
		resultProcess = new ResultProcess(imageSize);
		return filePathsList.size();
	}

	@Override
	public void run() {
		File file = null;
		BufferedImage image = null;
		// 存储单个图片处理结果
		List<String> list = null;
		String fileName = null;
		while (index < filePathsList.size()) {
			synchronized (this) {
				if (index >= filePathsList.size()) {
					return;
				}
				file = filePathsList.get(index);

				index++;
			}
			try {

				fileName = file.getName();
				ITesseract instance = new Tesseract();

				// 设置使用中文+英文字符集
				instance.setDatapath("");
				instance.setLanguage("chi_sim+eng");
				// 图片处理
				image = ImageProcess.imageProcess(file, x, y, width, height);

				String result = instance.doOCR(image);
				// 对result进行处理
				list = resultProcess.resultProcess(result, fileName);

				lists.add(list);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		// 线程计数
		latch.countDown();
	}

}
