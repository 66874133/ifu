package com.funnel.store;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.log4j.Logger;

import com.funnel.datasource.connection.MongoConnection;
import com.funnel.datasource.connection.MongoConnection.Editor;
import com.funnel.datasource.connection.MongoConnectionManager;
import com.funnel.redis.queue.IMessageHandler;

public class MongoStoreHandler implements IMessageHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	private Editor editor = null;

	public MongoStoreHandler() {
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		editor = mongoConnection.editor("crawler");
	}

	public void handle(String msg) throws Exception {
		logger.info("开始存储数据...");
		List<String> list = new ArrayList<String>();
		Object obj = JSONValue.parse(msg);
		JSONArray array = (JSONArray) obj;
		for(Object o:array)
		{
			JSONObject strs = (JSONObject) o;
			String json = strs.toJSONString();
			list.add(json);
		}
		editor.clearTable("goods");
		editor.insertBatchJson("goods", list);
		logger.info("存储数据完成,共计" + list.size() + "条");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
