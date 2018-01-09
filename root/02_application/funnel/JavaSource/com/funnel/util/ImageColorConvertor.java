package com.funnel.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageColorConvertor {

	protected Logger logger = Logger.getLogger(this.getClass());
	private Map<Integer, Integer> colorToIndex = null;

	private int colorDistance;

	/**
	 * 颜色距离划分
	 * 
	 * @param colorDistance
	 *            颜色距离划分区间值
	 */
	public ImageColorConvertor(int colorDistance) {
		colorToIndex = getMap(colorDistance);
		this.colorDistance = colorDistance;
	}

	public void convertImage(BufferedImage image, OutputStream outputStream)
			throws IOException {
		convertImage(image);
		ImageIO.write(image, "JPEG", outputStream);
	}

	/**
	 * 在当前转换器下对图片进行转换
	 * 
	 * @param bufferedImage
	 * @throws IOException
	 */
	public void convertImage(BufferedImage bufferedImage) throws IOException {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int minx = bufferedImage.getMinX();
		int miny = bufferedImage.getMinY();
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bufferedImage.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				int alpha = (pixel >> 24) & 0xff; // 透明度通道
				int red = (pixel & 0xff0000) >> 16;
				int green = (pixel & 0xff00) >> 8;
				int blue = (pixel & 0xff);

				alpha = 255;

				int pixel2 = getCloseColor(alpha, red, green, blue);
				bufferedImage.setRGB(i, j, pixel2);
			}
		}
	}

	public int getCloseColorIndex(int red, int green, int blue) {
		int c = getCloseColor(255, red, green, blue);
		return colorToIndex.get(c);
	}

	public int getCloseColorIndex(int pixel2) {
		return colorToIndex.get(pixel2);
	}

	public int getCloseColor(int alpha, int red, int green, int blue) {
		red = MathUtil.getCloseMultipleNumber(red, colorDistance);
		green = MathUtil.getCloseMultipleNumber(green, colorDistance);
		blue = MathUtil.getCloseMultipleNumber(blue, colorDistance);
		int pixel2 = ImageUtil.getRGB(alpha, red, green, blue);
		return pixel2;
	}

	private Map<Integer, Integer> getMap(int m) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> list = new ArrayList<Integer>();
		int l = 256 / m;
		for (int i = 0; i < l + 1; i++) {
			if (i * m < 256) {
				list.add(i * m);

			} else {
				list.add(255);
				break;
			}

		}

		int a = 0;
		for (int red : list) {
			for (int green : list) {
				for (int blue : list) {
					logger.info(a + ":"
							+ ImageUtil.getRGB(255, red, green, blue));
					map.put(ImageUtil.getRGB(255, red, green, blue), a);
					a++;
				}

			}

		}

		return map;
	}
}
