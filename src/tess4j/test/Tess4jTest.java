package tess4j.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import util.Png2Jpg;

public class Tess4jTest {
	public static void main(String[] args) throws IOException {
		// 图片所在文件夹
		long time1 = System.currentTimeMillis();
		String path = "C:\\Users\\12044\\Desktop\\桶\\天猫工商信息执照";
		testTess4j(path);
		long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1);
	}

	/**
	 * @param filePath
	 * @throws IOException
	 */
	public static void testTess4j(String filePath) throws IOException {
		File root = new File(filePath);
		ITesseract instance = new Tesseract();
		// URL url = ClassLoader.getSystemResource("tessdata");
		// String path = url.getPath().substring(1);
		instance.setDatapath("");
		instance.setLanguage("chi_sim+eng");

		try {
			File[] files = root.listFiles();
			// String[] str3 = new String[files.length];
			int i = 0;

			for (File file : files) {
				BufferedImage image = ImageIO.read(file);
				
				long timePng2Jpg1 = System.currentTimeMillis();

				image = Png2Jpg.png2Jpg(image, 0, 8, 530, 67);
				
				long timePng2Jpg2 = System.currentTimeMillis();
				
				System.out.println("timePng2Jpg2-timePng2Jpg1----------->"+(timePng2Jpg2-timePng2Jpg1));

				long timeGrayscale1 = System.currentTimeMillis();

				image = ImageHelper.convertImageToGrayscale(image);
				
				long timeGrayscale2 = System.currentTimeMillis();
				
				System.out.println("timeGrayscale2-timeGrayscale1----------->"+(timeGrayscale2-timeGrayscale1));


				long timeToBinary1 = System.currentTimeMillis();

				image = ImageHelper.convertImageToBinary(image);
				
				long timeToBinary2 = System.currentTimeMillis();

				System.out.println("timeToBinary2-timeToBinary1------------>"+(timeToBinary2-timeToBinary1));

				long timeScaled1 = System.currentTimeMillis();

				image = ImageHelper.getScaledInstance(image, 530 * 4, 67 * 4);

				long timeScaled2 = System.currentTimeMillis();
				
				System.out.println("timeScaled2-timeScaled1----------->"+(timeScaled2-timeScaled1));


				long timedoOCR1 = System.currentTimeMillis();

				String result = instance.doOCR(image);
				
				long timedoOCR2 = System.currentTimeMillis();

				System.out.println("timedoOCR2-timedoOCR1----------->"+(timedoOCR2-timedoOCR1));

				String fileName = file.toString().substring(
						file.toString().lastIndexOf("\\") + 1);
				// str3[i++] = result;
				System.out
						.println("图片名：" + file.toString() + " 识别结果：" + result);
			}
			// System.out.println(Arrays.toString(str3));
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}
}
