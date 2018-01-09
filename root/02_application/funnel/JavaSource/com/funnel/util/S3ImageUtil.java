package com.funnel.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.funnel.s3.S3ProduceExecutor;
import com.funnel.svc.SvcException;
import com.funnel.svc.util.HttpUtil;
import com.funnel.svc.util.StringUtils;

public class S3ImageUtil {
	private static final Logger logger = Logger.getLogger(S3ImageUtil.class);

	private static int connectionTimeout = 120000;

	private static int readTimeout = 120000;
	private static String bucketName;
	private static String pefixPath;

	@SuppressWarnings("unchecked")
	public static void saveGoodsImg2S3(List<Map<String, Object>> goodsInfos) {
		long t = System.currentTimeMillis();
		for (Map<String, Object> goodsInfo : goodsInfos) {
			byte[] imgDataArray = downloadImg(goodsInfo.get("imgUrl")
					.toString());
			Map<String, String> imgResolutionMap = calculateImageResolution(imgDataArray);
			String s3Path = saveImg2S3(goodsInfo.get("imgUrl").toString(),
					imgDataArray);
			goodsInfo.put("imgS3Url", s3Path);
			goodsInfo.put("imgHeight", imgResolutionMap.get("height"));
			goodsInfo.put("imgWidth", imgResolutionMap.get("width"));

			saveGoodsImgTextInfoImg2S3((List<Map<String, Object>>) goodsInfo
					.get("goodsImgTextInfos"));

		}
		logger.info("下载并上传商品图片到s3,商品总数:" + goodsInfos.size() + ",耗时:"
				+ (System.currentTimeMillis() - t));
	}

	private static void saveGoodsImgTextInfoImg2S3(
			List<Map<String, Object>> goodsImgTextInfos) {
		if (CollectionUtils.isEmpty(goodsImgTextInfos)) {
			return;
		}
		for (Map<String, Object> goodsImgTextInfo : goodsImgTextInfos) {
			if ("1".equals(goodsImgTextInfo.get("type"))) {
				byte[] imgDataArray = downloadImg(goodsImgTextInfo
						.get("imgUrl").toString());
				Map<String, String> imgResolutionMap = calculateImageResolution(imgDataArray);
				String s3Path = saveImg2S3(goodsImgTextInfo.get("imgUrl")
						.toString(), imgDataArray);
				goodsImgTextInfo.put("imgS3Url", s3Path);
				goodsImgTextInfo.put("imgHeight",
						imgResolutionMap.get("height"));
				goodsImgTextInfo.put("imgWidth", imgResolutionMap.get("width"));
			}
		}
	}

	private static Map<String, String> calculateImageResolution(
			byte[] imgDataArray) {
		try {
			BufferedImage bufferedImage = ImageIO
					.read(new ByteArrayInputStream(imgDataArray));
			Map<String, String> imgResolutionMap = new HashMap<String, String>();
			imgResolutionMap.put("width",
					new Integer(bufferedImage.getWidth()).toString());
			imgResolutionMap.put("height",
					new Integer(bufferedImage.getHeight()).toString());
			return imgResolutionMap;
		} catch (Exception e) {
			throw new SvcException("获取图片分辨率失败", e);
		}

	}

	public static String saveImg2S3(String imgUrl, byte[] imgDataArray) {
		return saveImg2S3(pefixPath, bucketName, imgUrl, imgDataArray);
	}

	public static String saveImg2S3(String pefixPath, String bucketName,
			String imgUrl, byte[] imgDataArray) {
		String imgSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
		if (imgSuffix.contains("?")) {
			imgSuffix = imgSuffix.substring(0, imgSuffix.indexOf("?"));
		}
		if (imgSuffix.contains("/")) {
			imgSuffix = imgSuffix.substring(0, imgSuffix.indexOf("/"));
		}
		String s3Path = S3ProduceExecutor.getInstance().upload(imgDataArray,
				bucketName, imgSuffix);
		return pefixPath + bucketName + "/" + s3Path;
	}

	public static byte[] downloadImg(String imgUrl) {
		Map<Object, Object> reqParamsMap = new HashMap<Object, Object>();
		reqParamsMap.put(HttpUtil.REQ_URL_PARAM, imgUrl);

		reqParamsMap.put(HttpUtil.ClIENT_KEY, "imgDownload");
		reqParamsMap.put(HttpUtil.CONNECTION_TIME_OUT, connectionTimeout);
		reqParamsMap.put(HttpUtil.READ_TIME_OUT, readTimeout);

		ImgDownloadStreamHandler imgDownloadStreamHandler = new ImgDownloadStreamHandler();
		imgDownloadStreamHandler.setImgUrl(imgUrl);
		reqParamsMap
				.put(HttpUtil.RESP_STREAM_HANDLER, imgDownloadStreamHandler);

		try {
			HttpUtil.getRequest(reqParamsMap);
		} catch (Exception e) {
			throw new SvcException("下载图片失败," + imgUrl);
		}

		byte[] imgDataArray = ((ByteArrayOutputStream) imgDownloadStreamHandler
				.getOutputStream()).toByteArray();
		imgDownloadStreamHandler.setOutputStream(null);
		return imgDataArray;
	}

	public static void deleteS3File(List<String> s3FilePaths) {
		for (String s3FilePath : s3FilePaths) {
			deleteS3File(s3FilePath);
		}
	}

	public static void deleteS3File(String s3FilePath) {

		String s3FileKey = s3FilePath
				.substring(s3FilePath.lastIndexOf("/") + 1);
		String s3bBucket = s3FilePath.substring(0, s3FilePath.lastIndexOf("/"));
		s3bBucket = s3bBucket.substring(s3bBucket.lastIndexOf("/") + 1);
		try {
			S3ProduceExecutor.getInstance().deleteObject(s3bBucket, s3FileKey);
			logger.info("删除s3文件,bucketName:" + s3bBucket + ",s3FileKey:"
					+ s3FileKey + " 原始路径:" + s3FilePath);
		} catch (Exception e) {
			logger.error("删除s3文件失败,bucketName:" + s3bBucket + ",s3FileKey:"
					+ s3FileKey + " 原始路径:" + s3FilePath, e);
		}

	}

	public static void removeGoodsImgFormS3(List<Map<String, Object>> goodsInfos) {
		List<String> s3FilePaths = new ArrayList<String>();
		for (Map<String, Object> goodsInfo : goodsInfos) {
			if (StringUtils.hasText((String) goodsInfo.get("imgS3Url"))) {
				s3FilePaths.add((String) goodsInfo.get("imgS3Url"));
			}
			List<Map<String, Object>> goodsImgTextInfos = (List<Map<String, Object>>) goodsInfo
					.get("goodsImgTextInfos");
			if (!CollectionUtils.isEmpty(goodsImgTextInfos)) {
				for (Map<String, Object> goodsImgTextInfo : goodsImgTextInfos) {
					if ("1".equals(goodsImgTextInfo.get("type"))
							&& StringUtils.hasText((String) goodsImgTextInfo
									.get("imgS3Url"))) {
						s3FilePaths.add((String) goodsImgTextInfo
								.get("imgS3Url"));
					}
				}
			}
		}
		deleteS3File(s3FilePaths);
	}

	

	public static void main(String[] args) {
		String s3FilePath = "/s3/img/goods/img-shopguid-test/4511-2016-03-16050700-1458119220010.jpg";
		String s3FileKey = s3FilePath
				.substring(s3FilePath.lastIndexOf("/") + 1);
		String s3bBucket = s3FilePath.substring(0, s3FilePath.lastIndexOf("/"));
		s3bBucket = s3bBucket.substring(s3bBucket.lastIndexOf("/") + 1);
		try {
			S3ProduceExecutor.getInstance().deleteObject(s3bBucket, s3FileKey);
			logger.info("删除s3文件,bucketName:" + s3bBucket + ",s3FileKey:"
					+ s3FileKey + " 原始路径:" + s3FilePath);
		} catch (Exception e) {
			logger.error("删除s3文件失败,bucketName:" + s3bBucket + ",s3FileKey:"
					+ s3FileKey + " 原始路径:" + s3FilePath, e);
		}
	}

	public static class ImgDownloadStreamHandler implements
			HttpUtil.ResponseStreamHandler {
		private OutputStream outputStream;

		private long total;

		private int readNum;

		private String imgUrl;

		public void init(long downFileSize) {
			outputStream = new ByteArrayOutputStream((int) downFileSize);
		}

		public void process(byte[] b, int off, int len, long totalSize) {
			try {
				total = total + len;
				readNum++;
				outputStream.write(b, off, len);
			} catch (IOException e) {
				throw new SvcException("下载图片失败:" + imgUrl, e);
			}

			if (readNum % 200 == 0 || total >= totalSize) {
				logger.info(MessageFormat.format(
						"Download img progress: {0}% ({1}/{2}), url: {3}",
						(total * 100) / totalSize, total, totalSize, imgUrl));
			}
		}

		public void destory() {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
		}

		public OutputStream getOutputStream() {
			return outputStream;
		}

		public void setOutputStream(OutputStream outputStream) {
			this.outputStream = outputStream;
		}

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

	}

	public static String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		S3ImageUtil.bucketName = bucketName;
	}

	public static String getPefixPath() {
		return pefixPath;
	}

	public void setPefixPath(String pefixPath) {
		S3ImageUtil.pefixPath = pefixPath;
	}

}
