package com.funnel.s3;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;

public class S3ProduceExecutor {

	protected Log logger = LogFactory.getLog(this.getClass());

	private S3ProduceExecutor() {
	}

	private static S3ProduceExecutor executor;

	public static S3ProduceExecutor getInstance() {

		if (executor == null) {
			executor = new S3ProduceExecutor();
		}

		return executor;
	}

	public InputStream download(String bucketName, String fileName) {

		S3Object object = S3ClientFactory.createClient().getObject(
				new GetObjectRequest(bucketName, fileName));

		return object.getObjectContent();

	}

	public String uploadSingle(byte[] bytes, String bucketName, String fileName) {
		InputStream is = new ByteArrayInputStream(bytes);
		return uploadSingle(is, bucketName, fileName);
	}

	public String uploadSingle(String localFilePath, String bucketName,
			String fileName) {

		InputStream is = null;
		try {
			is = new FileInputStream(localFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return uploadSingle(is, bucketName, fileName);
	}

	public String uploadSingle(InputStream is, String bucketName,
			String fileName) {

		String keyName = getKey(fileName);

		try {
			ObjectMetadata metaData = new ObjectMetadata();
			PutObjectRequest s3Request = new PutObjectRequest(bucketName,
					keyName, is, metaData);
			s3Request.getRequestClientOptions()
					.setReadLimit(is.available() + 1);
			S3ClientFactory.createClient().putObject(s3Request);

			// try get Meta ,if throw exception when file is empty
			S3ClientFactory.createClient().getObjectMetadata(bucketName,
					keyName);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return keyName;

	}

	public String upload(InputStream is, String bucketName, String fileName) {
		ObjectMetadata metaData = new ObjectMetadata();

		TransferManager tm = new TransferManager(S3ClientFactory.createClient());
		TransferManagerConfiguration conf = tm.getConfiguration();

		int threshold = 4 * 1024 * 1024;
		conf.setMultipartUploadThreshold(threshold);
		tm.setConfiguration(conf);

		String keyName = getKey(fileName);

		try {

			PutObjectRequest s3Request = new PutObjectRequest(bucketName,
					keyName, is, metaData);
			s3Request.getRequestClientOptions()
					.setReadLimit(is.available() + 1);
			Upload upload = tm.upload(s3Request);
			TransferProgress p = upload.getProgress();
			p.setTotalBytesToTransfer(is.available());
			while (upload.isDone() == false) {
				int percent = (int) (p.getPercentTransferred());
				logger.info("bucketName:" + bucketName + " fileName:"
						+ fileName + " s3 uploading " + " - " + "[ " + percent
						+ "% ] " + p.getBytesTransferred() + " / "
						+ p.getTotalBytesToTransfer());
				// Do work while we wait for our upload to complete...
				Thread.sleep(1000);
			}
			logger.info("s3 uploaded over ,bucketName:" + bucketName
					+ " fileName:" + fileName + " keyName = " + keyName);
			tm.shutdownNow();

			AmazonClientException ex = upload.waitForException();
			if (ex != null) {
				throw new RuntimeException("s3涓婁紶澶辫触,bucketName:" + bucketName
						+ " fileName:" + fileName, ex);
			}

			// try get Meta ,if throw exception when file is empty
			S3ClientFactory.createClient().getObjectMetadata(bucketName,
					keyName);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return keyName;

	}

	public String upload(byte[] bytes, String bucketName, String fileName) {

		InputStream is = new ByteArrayInputStream(bytes);
		return upload(is, bucketName, fileName);
	}

	public String upload(String localFilePath, String bucketName,
			String fileName) {

		InputStream is = null;
		try {
			is = new FileInputStream(localFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return upload(is, bucketName, fileName);
	}

	private String getKey(String fileName) {
		String fileSuff = fileName.substring(fileName.lastIndexOf('.'));

		if (fileSuff == null || fileSuff.length() == 0) {

			throw new RuntimeException("鏂囦欢鐨勬墿灞曞悕涓虹┖,鎷掔粷鎵ц!");

		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhhmmss-");
		Calendar calendar = Calendar.getInstance();
		String randomNum = String
				.valueOf((int) ((Math.random() * 9 + 1) * 1000));
		String result = randomNum + "-" + dateFormat.format(calendar.getTime())
				+ System.currentTimeMillis() + fileSuff;
		return result;
	}

	public void deleteObject(String bucketName, String filePath) {
		S3ClientFactory.createClient().deleteObject(bucketName, filePath);
	}

	public static void main(String[] args) throws IOException {

		/**
		 * 绗竴涓弬鏁�:鏂囦欢path,绗簩涓弬鏁�:妗跺悕,绗笁涓弬鏁�:鏂囦欢鍚�(鍙槸涓轰簡鑾峰彇鏂囦欢鎵╁睍鍚峚pk鎴栬�
		 * 卝pg,鍓嶉潰鐨勪竴涓插瓧绗︿覆娌℃湁瀹為檯鐢ㄥ)
		 * 姝ゆ柟娉曡繕鏈変袱涓噸杞芥柟娉�,鏀寔绗竴涓弬鏁颁紶鍏yte[]鏁扮粍,鎴栬�呯洿鎺ヤ紶鍏ヨ緭鍏ユ祦InputStream
		 * return
		 * 杩斿洖鍊兼槸鍐呴儴閫氳繃涓�绉嶇畻娉曠敓鎴愮殑瀛樺偍浜嶴3涓婄殑鏂囦欢鍚�,涔熷氨鏄,鏂规硶鍙繑鍥炲瓨鍌ㄤ簬s3涓婄殑鏈
		 * �缁堢殑鏂囦欢鐨勫悕瀛�,涓嶈繑鍥炲畬鏁磒ath 闇�瑕佺敤鎴峰湪浣跨敤鏃�,閫氳繃杩斿洖鍊兼潵鑷繁鎷兼帴瀹屾暣鐨刾ath
		 */
		/*
		 * InputStream is =
		 * S3ProduceExecutor.getInstance().download("app-img-lestore-test",
		 * "5445-2015-07-03052654-1435915614581.sss2"); int n = 100; byte
		 * buffer[] = new byte[n]; ByteArrayOutputStream os = new
		 * ByteArrayOutputStream(); System.out.println(os.size()); int i = 0;
		 * while ((i = is.read(buffer, 0, 100)) > 0) { os.write(buffer, 0, i); }
		 */

		String fileName = S3ProduceExecutor.getInstance().uploadSingle(
				"C:\\apktest\\1111111.txt", "app-img-lestore-test", "xxxx.sss");

		System.out.println(fileName);

		S3ProduceExecutor.getInstance().upload("C:\\apktest\\1111111.txt",
				"app-img-lestore-test", "1111111.txt");

	}

}
