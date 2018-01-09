package com.funnel.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageUtil {

	private static Logger logger = Logger.getLogger(ImageUtil.class);

	public static BufferedImage getBufferedImage(String imgUrl)
			throws IOException {
		byte[] imgDataArray = S3ImageUtil.downloadImg(imgUrl);
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(
				imgDataArray));
		return bufferedImage;
	}

	public static BufferedImage getBufferedImage(byte[] imgDataArray)
			throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(
				imgDataArray));
		return bufferedImage;
	}

	public static void draw(String code,Font font) throws FileNotFoundException, IOException {
		int len = code.length();
		
		int fontSize = font.getSize();
		
		int width = len *fontSize;
		BufferedImage image = new BufferedImage(width, 300,
				BufferedImage.TYPE_INT_RGB);
		// 得到图片
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		// 向图片上写写数据
		g.setColor(Color.BLACK);// 设置图片颜色
		g.setFont(font);
		// 把想要写的字符串画在图片上

		g.drawString(code, 0, fontSize);// 画图片
		
		Color[] colors = {Color.ORANGE, Color.LIGHT_GRAY};
		 //   平移原点到图形环境的中心
        g.translate(image.getWidth() / 2, image.getHeight() / 2);
        //   旋转文本
     
            g.rotate(30 * Math.PI / 180);

            g.drawString(code, 0, 0);
        

		ImageIO.write(image, "jpg", new FileOutputStream("d://abc.jpg"));// 输出图片
	}

	/**
	 * 获取颜色统计
	 * 
	 * @param bufferedImage
	 * @param width
	 * @param height
	 * @param x
	 *            起始x坐标
	 * @param y
	 *            起始y坐标
	 * @return
	 */
	public static Map<Integer, Integer> getColorStat(
			BufferedImage bufferedImage, int width, int height, int x, int y) {
		logger.info("width=" + width + " height=" + height);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int minx = bufferedImage.getMinX() + x;
		int miny = bufferedImage.getMinY() + y;
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bufferedImage.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字

				if (map.containsKey(pixel)) {
					map.put(pixel, map.get(pixel) + 1);
				} else {
					map.put(pixel, 1);
				}
			}
		}

		return map;
	}

	public static Map<Integer, Integer> getColorStat(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		return getColorStat(bufferedImage, width, height, 0, 0);

	}

	/**
	 * 按九宫格布局获取图片像素统计
	 * 
	 * @param bufferedImage
	 * @param sudokuX
	 *            九宫格种的行号
	 * @param sudokuY
	 *            九宫格种的列号
	 * @return
	 */
	public static Map<Integer, Integer> getSudokuColorStat(
			BufferedImage bufferedImage, int sudokuX, int sudokuY) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int w = width / 3;
		int h = height / 3;

		return getColorStat(bufferedImage, w * sudokuX, h * sudokuY, w
				* (sudokuX - 1), h * (sudokuY - 1));
	}

	public static List<ImageRGB> getImageRGB(byte[] imgDataArray) {

		List<ImageRGB> list = new ArrayList<ImageRGB>();

		try {

			BufferedImage bufferedImage = ImageIO
					.read(new ByteArrayInputStream(imgDataArray));
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			int minx = bufferedImage.getMinX();
			int miny = bufferedImage.getMinY();
			for (int i = minx; i < width; i++) {
				for (int j = miny; j < height; j++) {
					int pixel = bufferedImage.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
					int[] rgb = new int[3];
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);

					ImageRGB imageRGB = new ImageRGB();
					imageRGB.setI(i);
					imageRGB.setJ(j);
					imageRGB.setRgb(rgb);
					list.add(imageRGB);
					// System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0]
					// + "," + rgb[1] + "," + rgb[2] + ")");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static List<ImageRGB> getImageRGB(String imgUrl) {

		byte[] imgDataArray = S3ImageUtil.downloadImg(imgUrl);
		return getImageRGB(imgDataArray);

	}

	public static int getRGB(int alpha, int red, int green, int blue) {
		int cRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
		return cRGB;
	}

	/**
	 * 转换rgb值
	 * 
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int convertRGB(int alpha, int red, int green, int blue) {

		int testnum = 0xbb;
		if (red > testnum || green > testnum || blue > testnum)
			if (red + green < testnum * 1.5 && red + blue < testnum * 1.5
					&& green + blue < testnum * 1.5
					&& red + green + blue < testnum * 1.5)

				return 0xffffffff;
			else
				return 0xff000000;
		else
			return 0xff000000;

		// return 0xff000000; //黑色
		// else return 0xffffffff; //白色

	}

	public static List<ICountStat> getSudokuColorStatTop(
			BufferedImage bufferedImage, int sudokuX, int sudokuY, int top) {
		Map<Integer, Integer> map = getSudokuColorStat(bufferedImage, sudokuX,
				sudokuY);
		logger.info("map=" + map);

		Iterator<Integer> iterator = map.keySet().iterator();
		List<ICountStat> list2 = new ArrayList<ICountStat>();
		while (iterator.hasNext()) {
			int k = iterator.next();
			int v = map.get(k);

			KVCountStat kvCountStat = new KVCountStat("" + k, v);
			list2.add(kvCountStat);
		}

		List<ICountStat> topList = MathUtil.getCountStatTop(list2, top);

		logger.info("top=" + topList);

		return topList;

	}

	public static void main(String[] args) throws Exception {
		String code = "天下为公";
		Font font = new Font("隶书", Font.ITALIC, 50);
		draw(code,font);

	}
}
