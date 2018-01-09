package com.funnel.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.funnel.datasource.connection.MongoConnection;
import com.funnel.datasource.connection.MongoConnection.Editor;
import com.funnel.datasource.connection.MongoConnectionManager;
import com.funnel.rank.RankConfInfo;

public class MongoDatabaseUtil {

	public static void insert(List<Object> list) {
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		Editor editor = mongoConnection.editor();

		if (null != list) {
			editor.createTable(list.get(0).getClass().getSimpleName());
		}

		try {
			editor.insertBatch(list.get(0).getClass().getSimpleName(), list);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void update()
	{
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		Editor editor = mongoConnection.editor();
		Map<String, String> where2 = new HashMap<String, String>();
		where2.put("paramKey", "222");

		editor.update("testABC", where2, "paramType", "xxxxx");
	}
	
	public static void delete()
	{
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		Editor editor = mongoConnection.editor();
		Map<String, String> where = new HashMap<String, String>();
		where.put("paramKey", "222");
		where.put("paramType", "333");
		try {
			editor.delete("testABC", where);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * @param args
	 * @throws Exception
	 * @throws
	 */
	public static void main(String[] args) throws Exception {

		List<Object> list = new ArrayList<Object>();
		RankConfInfo confInfo = new RankConfInfo();
		confInfo.setRankExpression("12121");
		list.add(confInfo);
		insert(list);

	}

	// private static void testMongoServer() {
	// String ip = "120.25.228.198";
	// int port = 27017;
	// String user = "root";
	// String pwd = "root_win2016";
	//
	// // MongoClient client = new MongoClient(Arrays.asList(new ServerAddress(
	// // "120.25.228.198", 27017)));
	//
	// MongoClient client = MongoConnectionManager.newConnection(ip, port,
	// user, pwd);
	// // 查询所有的Database
	// for (String name : client.listDatabaseNames()) {
	// System.out.println("dbName: " + name);
	// }
	//
	// MongoDatabase db = client.getDatabase("crawler");
	//
	// // 查询所有的聚集集合
	// for (String name : db.listCollectionNames()) {
	// System.out.println("collectionName: " + name);
	// }
	//
	// db.createCollection("crawler");
	// MongoCollection<Document> users = db.getCollection("system.users");
	//
	//
	// // 查询所有的数据
	// MongoCursor<Document> cur = users.find().iterator();
	// while (cur.hasNext()) {
	// Document document = cur.next();
	// System.out.println(document);
	// System.out.println("serialize:" + JSON.serialize(document));
	// }
	//
	// client.close();
	// }

}
