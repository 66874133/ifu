package com.funnel.photo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.util.ICountStat;
import com.funnel.util.ImageColorConvertor;
import com.funnel.util.ImageUtil;
import com.funnel.util.KVCountStat;
import com.funnel.util.PropertiesUtil;
import com.funnel.util.S3ImageUtil;

public class PhotoResolveService extends SyncService {

	private ImageColorConvertor colorConvertor = new ImageColorConvertor(64);

	private static final String IMG_SERVER_URL_KEY = "imgAddrPerfix";

	private static final String PEFIX_PATH = "pefixPath";

	private static final String BUCKET_NAME = "bucketName";

	@Override
	public String getSvcCode() {
		return "photoResolveService";
	}

	@Override
	public String getSvcDesc() {
		return "图片分解";
	}

	@Override
	public void process(SvcContext context) {

		String action = context.getRequestData().getAsString("action");
		if ("upload".equalsIgnoreCase(action)) {
			uploadS3Image(context);
		} else if ("parseLocal".equalsIgnoreCase(action)) {
			parseLocalImage(context);
		} else {
			parseRemoteImage(context);
		}

	}

	private void uploadS3Image(SvcContext context) {

		String imgUrl = context.getRequestData().getAsString("imgUrl");
		logger.info("图片上传开始 imgUrl=" + imgUrl);
		byte[] input = context.getInput();
		String s3Path = S3ImageUtil.saveImg2S3("/s3/img/goods/",
				"img-shopguid-test", imgUrl, input);
		logger.info("s3Path=" + s3Path + " imgUrl=" + imgUrl);

	}

	private void parseLocalImage(SvcContext context) {
		String imgUrl = context.getRequestData().getAsString("imgUrl");

		byte[] input = context.getInput();
		List<String> list;
		try {
			list = getDCNumber(input, 33, 4);
			context.getResponseData().put("dcNumber", list);
		} catch (IOException e) {
			logger.error("", e);
		}

		logger.info("图片上传开始 imgUrl=" + imgUrl);
		String s3Path = S3ImageUtil.saveImg2S3(
				PropertiesUtil.getProperty(PEFIX_PATH),
				PropertiesUtil.getProperty(BUCKET_NAME), imgUrl, input);

		String url = PropertiesUtil.getProperty(IMG_SERVER_URL_KEY);
		context.getResponseData().put("imgUrl", url + s3Path);
		logger.info("图片上传完成 s3Path=" + s3Path + " imgUrl=" + imgUrl);

	}

	private void parseRemoteImage(SvcContext context) {
		String imgUrl = context.getRequestData().getAsString("imgUrl");
		logger.info("图片分解开始 imgUrl=" + imgUrl);

		try {
			List<String> list = getDCNumber(imgUrl, 33, 4);
			logger.info("list=" + list);
			context.getResponseData().put("dcNumber", list);
		} catch (IOException e) {
			logger.error("", e);
			context.getResponseData().put("dcNumber", "01,05,06,09,11,25,12");
		}
		logger.info("图片分解结束  imgUrl=" + imgUrl);

		context.getResponseData().put("imgUrl", imgUrl);
		return;

	}

	private List<String> getDCNumber(BufferedImage bufferedImage, int dcDiv,
			int t) throws IOException {
		colorConvertor.convertImage(bufferedImage);

		List<String> list = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 4; j++) {
				List<ICountStat> top = ImageUtil.getSudokuColorStatTop(
						bufferedImage, i, j, t);
				long n = 1;
				for (ICountStat kv : top) {
					int index = colorConvertor.getCloseColorIndex(Integer
							.parseInt(((KVCountStat) kv).getKey()));
					n = n + (index * ((KVCountStat) kv).getCountValue());
					logger.info("i=" + i + " j=" + j + " index=" + index);
				}
				long k = n % dcDiv;
				String s = String.valueOf((int) k);

				if (s.length() < 2) {
					s = "0" + s;
				}
				if (!list.contains(s)) {
					list.add(s);
				}

				logger.info("k=" + k);
			}
		}

		Collections.sort(list);

		return list;

	}

	private List<String> getDCNumber(String imgUrl, int dcDiv, int t)
			throws IOException {
		BufferedImage bufferedImage = ImageUtil.getBufferedImage(imgUrl);
		return getDCNumber(bufferedImage, dcDiv, t);

	}

	private List<String> getDCNumber(byte[] bytes, int dcDiv, int t)
			throws IOException {
		BufferedImage bufferedImage = ImageUtil.getBufferedImage(bytes);
		return getDCNumber(bufferedImage, dcDiv, t);

	}

}
