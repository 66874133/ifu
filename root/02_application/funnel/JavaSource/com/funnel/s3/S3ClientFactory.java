package com.funnel.s3;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.internal.Profile;
import com.amazonaws.auth.profile.internal.ProfilesConfigFileLoader;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3ClientFactory {

	private static AWSCredentials credentials;
	private static AWSCredentials credentialsTest;

	private static String env = "idc";
	static {
		try {
			// 閫氳繃ProfilesConfigFile鍔犺浇鎸囧畾璺緞涓嬬殑credentials鏂囦欢,鐒跺悗閫氳繃鏂规硶getCredentials浼犲叆鐢ㄦ埛鍚嶅緱鍒板叿浣撶殑credentials淇℃伅
			InputStream fileIs = S3ClientFactory.class
					.getResourceAsStream("/credentials");

			Method m = ProfilesConfigFileLoader.class.getDeclaredMethod(
					"loadProfiles", InputStream.class);
			if (!m.isAccessible()) {
				m.setAccessible(true);
			}

			Map<String, Profile> o = (Map<String, Profile>) m.invoke(null,
					fileIs);
			credentials = o.get("default").getCredentials();

			// credentials = new
			// ProfilesConfigFile("credentials").getCredentials("default");
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.",
					e);
		}

		try {
			// 閫氳繃ProfilesConfigFile鍔犺浇鎸囧畾璺緞涓嬬殑credentials鏂囦欢,鐒跺悗閫氳繃鏂规硶getCredentials浼犲叆鐢ㄦ埛鍚嶅緱鍒板叿浣撶殑credentials淇℃伅
			InputStream fileIs = S3ClientFactory.class
					.getResourceAsStream("/credentials-test");

			Method m = ProfilesConfigFileLoader.class.getDeclaredMethod(
					"loadProfiles", InputStream.class);
			if (!m.isAccessible()) {
				m.setAccessible(true);
			}

			Map<String, Profile> o = (Map<String, Profile>) m.invoke(null,
					fileIs);
			credentialsTest = o.get("default").getCredentials();

			// credentials = new
			// ProfilesConfigFile("credentials").getCredentials("default");
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credentials-test profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.",
					e);
		}

	}

	public static AmazonS3 createClient() {
		AmazonS3 s3 = null;
		if ("idc".equals(env)) {
			if (credentials == null) {
				throw new RuntimeException("credentials must not be null!");
			}

			s3 = new AmazonS3Client(credentials);
		} else {
			if (credentialsTest == null) {
				throw new RuntimeException("credentialsTest must not be null!");
			}

			s3 = new AmazonS3Client(credentialsTest);
		}

		Region region = Region.getRegion(Regions.CN_NORTH_1);
		s3.setRegion(region);

		return s3;

	}

	public static String getEnv() {
		return env;
	}

	public static void setEnv(String env) {
		S3ClientFactory.env = env;
	}

}
