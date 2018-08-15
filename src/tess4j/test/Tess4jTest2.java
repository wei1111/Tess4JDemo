package tess4j.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.util.ImageHelper;
import util.Png2Jpg;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class Tess4jTest2 {
	public static void main(String args[]) {

		long millis1 = System.currentTimeMillis();
		read2 read = new read2(millis1);
		ExecutorService service = Executors.newFixedThreadPool(15);
		for (int i = 1; i <= 15; i++) {
			service.execute(new Thread(read, "线程" + i));
		}
	}
}

class read2 implements Runnable {
	List<File> filePathsList = new ArrayList<File>();
	int index = 0;
	private long millis;

	public read2(long millis) {
		this.millis = millis;
		File f = new File("C:\\Users\\12044\\Desktop\\桶\\天猫工商信息执照");
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
	public void run() {
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
			}
			try {
				ITesseract instance = new Tesseract();
				instance.setDatapath("");
				instance.setLanguage("chi_sim+eng");

				is = new FileInputStream(file.getPath());
				image = ImageIO.read(is);

				// image = ImageHelper.getSubImage(image, 0, 8, 562, 67);
				image = Png2Jpg.png2Jpg(image, 0, 8, 562, 67);
				image = ImageHelper.convertImageToGrayscale(image);
				image = ImageHelper.convertImageToBinary(image);

				image = ImageHelper.getScaledInstance(image, 551 * 4, 67 * 4);

				String result = instance.doOCR(image);
				// String fileName = file.toString().substring(
				// file.toString().lastIndexOf("\\") + 1);
				// str3[i++] = result;
				System.out
						.println("图片名：" + file.toString() + " 识别结果：" + result);

			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();

					long millis2 = System.currentTimeMillis();

					System.out.println("millis--------->" + millis);
					System.out.println("millis2-------->" + millis2);
					System.out.println("(millis2 - millis)----------->"
							+ (millis2 - millis));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
