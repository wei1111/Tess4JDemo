package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Png2Jpg {
	public static BufferedImage png2Jpg(BufferedImage bufferedImage, int x,
			int y, int width, int height) {

		BufferedImage newBufferedImage = null;

		// create a blank, RGB, same width and height, and a white background

		newBufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g2 = newBufferedImage.createGraphics();

		g2.drawImage(bufferedImage.getSubimage(x, y, width, height), 0, 0,
				Color.WHITE, null);
		g2.dispose();
		return newBufferedImage;
	}
}
