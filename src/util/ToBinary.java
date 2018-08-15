package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ToBinary {
	public static BufferedImage toBinary(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		int[] rgb = new int[3];
		int[][] zuobiao = new int[w][h];
		BufferedImage bi = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_BINARY);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				int avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
				zuobiao[x][y] = avg;
			}
		}

		int SW = 135;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (zuobiao[x][y] <= SW) {
					int max = new Color(0, 0, 0).getRGB();
					bi.setRGB(x, y, max);
				} else {
					int min = new Color(255, 255, 255).getRGB();
					bi.setRGB(x, y, min);
				}
			}
		}
		return bi;
	}
}