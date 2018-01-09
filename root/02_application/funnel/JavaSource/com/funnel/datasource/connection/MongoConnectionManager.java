package com.funnel.datasource.connection;

import com.funnel.util.PropertiesUtil;

public class MongoConnectionManager {

	private static MongoConnection connection = null;

	public static MongoConnection getConnection() {

		if (null != connection) {
			return connection;
		} else {
			String ip = PropertiesUtil.getProperty("ali.mongo.ip");
			String port = PropertiesUtil.getProperty("ali.mongo.port");
			String user = PropertiesUtil.getProperty("ali.mongo.user");
			String pwd = PropertiesUtil.getProperty("ali.mongo.password");
			String db = PropertiesUtil.getProperty("ali.mongo.db");
			String auth = PropertiesUtil.getProperty("ali.mongo.auth");
			connection = new MongoConnection(ip, Integer.parseInt(port), user,
					pwd, db,auth);
		}
		return connection;

	}

}
