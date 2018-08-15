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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.util.ImageHelper;
import util.Png2Jpg;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class demo3 {
	public static void main(String args[]) throws InterruptedException,
			ExecutionException {

		boolean flag = true;
		long millis1 = System.currentTimeMillis();
		final Map<Integer, String> resultMap1 = new HashMap<Integer, String>();
		final Map<Integer, String> resultMap2 = Collections
				.synchronizedMap(resultMap1);

		// ExecutorService service = Executors.newCachedThreadPool();
		Callable<Map<Integer, String>> task = new Task(resultMap2);
		FutureTask<Map<Integer, String>> future = new FutureTask<Map<Integer, String>>(
				task);

		int n = 10;
		Thread[] threads = new Thread[n];
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(future);
			// service.execute(threads[i]);
			threads[i].start();
		}
		// service.shutdown();

		while (flag) {
			while (future.isDone()) {

				Set<Map.Entry<Integer, String>> entry = (future.get())
						.entrySet();
				Iterator<Map.Entry<Integer, String>> iterator = entry
						.iterator();
				while (iterator.hasNext()) {
					Map.Entry<Integer, String> en = iterator.next();
					System.out.println(en.getKey());
					System.out.println(en.getValue());

				}
				long millis2 = System.currentTimeMillis();
				System.out.println("(millis2 - millis1)----------->"
						+ (millis2 - millis1));
				flag = false;
				break;
			}
		}
	}
}

class Task implements Callable<Map<Integer, String>> {
	List<File> filePathsList = new ArrayList<File>();
	Map<Integer, String> resultMap = null;

	int index = 0;

	public Task(Map<Integer, String> resultMap) {
		this.resultMap = resultMap;
		File f = new File("C:\\Users\\12044\\Desktop\\桶\\天猫工商信息执照");
		// File f = new File("C:\\Users\\12044\\Desktop\\侧视图");
		getFileList(f);
	}

	private void getFileList(File f) {
		File[] filePaths = f.listFiles();
		for (File s : filePaths) {
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
	public Map<Integer, String> call() throws Exception {
		File file = null;
		InputStream is = null;
		BufferedImage image = null;
		while (index < filePathsList.size()) {
			synchronized (this) {
				if (index >= filePathsList.size()) {
					continue;
				}
				file = filePathsList.get(index);
				index++;
				System.out.println("index---------" + index);
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

				resultMap.put(index, result);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(resultMap.size());
		return resultMap;
	}
}