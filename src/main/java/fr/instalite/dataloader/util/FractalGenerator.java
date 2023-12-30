
package fr.instalite.dataloader.util;

import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Source :
 * https://bradforj287.blogspot.com/2010/11/creating-random-fractals-in-java-with.html
 */

public class FractalGenerator {

	public BufferedImage generateFractalBufferedImage() {
		Random rand = new Random();

		return generateFractalBufferedImage(() -> {
			BufferedImage newFrameImg = new BufferedImage(1920, 1200, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D) newFrameImg.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			g2d.setColor(randomColor);
			g2d.fillRect(0, 0, 1920, 1200);
			generateTreeFractal(g2d, 1920 / 2, 1200 / 2, 10, 200);
			return newFrameImg;
		});
	}

	private BufferedImage generateFractalBufferedImage(Generator generator) {
		return generator.generate();
	}

	private interface Generator {
		BufferedImage generate();
	}

	private Color brightness(Color c, double scale) {
		int r = Math.min(255, (int) (c.getRed() * scale));
		int g = Math.min(255, (int) (c.getGreen() * scale));
		int b = Math.min(255, (int) (c.getBlue() * scale));
		return new Color(r, g, b);
	}

	private void generateTreeFractal(Graphics2D g2d, double x, double y,
			double lineWidth, double lineLength) {
		final double LINE_WIDTH_MULTILIER = .80;
		final double LINE_LENGTH_MULTIPLIER = .85;

		if (lineWidth <= 1 || lineLength <= 2) {
			return;
		}

		Random rand = new Random();
		int numOfLinesToDraw = 1 + rand.nextInt(4);

		BasicStroke bs = new BasicStroke((float) lineWidth,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
		Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		g2d.setColor(randomColor);

		Color c = g2d.getColor();
		c = brightness(
				c,
				Math.abs(Math.cos(x * x + y * y + lineWidth * lineWidth + lineLength * lineLength)));
		g2d.setColor(c);

		for (int i = 0; i < numOfLinesToDraw; i++) {
			double x1 = rand.nextDouble() - .5;
			double y1 = rand.nextDouble() - .5;

			double mag = Math.sqrt(x1 * x1 + y1 * y1);
			x1 = x1 / mag;
			y1 = y1 / mag;

			x1 = x1 * lineLength;
			y1 = y1 * lineLength;

			x1 = x1 + x;
			y1 = y1 + y;

			g2d.drawLine((int) x, (int) y, (int) x1, (int) y1);

			generateTreeFractal(g2d, x1, y1, lineWidth * LINE_WIDTH_MULTILIER,
					lineLength * LINE_LENGTH_MULTIPLIER);
		}
	}
}
