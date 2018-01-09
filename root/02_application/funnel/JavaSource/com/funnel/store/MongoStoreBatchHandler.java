package com.funnel.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.funnel.datasource.connection.MongoConnection;
import com.funnel.datasource.connection.MongoConnection.Editor;
import com.funnel.datasource.connection.MongoConnectionManager;
import com.funnel.redis.queue.IMessageHandler;

public class MongoStoreBatchHandler implements IMessageHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	private Editor editor = null;

	private List<String> list = new ArrayList<String>();

	public MongoStoreBatchHandler() {
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		BatchInsertThread batchInsertThread = new BatchInsertThread();
		batchInsertThread.start();
		editor = mongoConnection.editor("crawler");
	}

	public void handle(String msg) throws Exception {
		logger.debug("开始存储数据,msg=" + msg);

		synchronized (list) {
			list.add(msg);
			if (list.size() > 50) {
				list.notifyAll();
			}
		}

		logger.debug("存储数据完成,msg=" + msg);
	}

	private class BatchInsertTimerThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					List<String> list2 = new ArrayList<String>();
					synchronized (list) {
						if (list.size() > 0) {
							list2.addAll(list);
							list.clear();
						}
					}

					if (list2.size() > 0) {
						logger.info("准备写入" + list2.size() + "条数据");
						editor.insertBatch("goods", list2);
						logger.info("成功写入" + list2.size() + "条数据");
						list2.clear();
					}

					sleep(8000);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
	}
	
	private class BatchInsertThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					List<String> list2 = new ArrayList<String>();
					synchronized (list) {
						if (list.size() > 0) {
							list2.addAll(list);
							list.clear();
						}
						else
						{
							list.wait(3000);
						}
					}

					if (list2.size() > 0) {
						logger.info("准备写入" + list2.size() + "条数据");
						editor.insertBatch("goods", list2);
						logger.info("成功写入" + list2.size() + "条数据");
						list2.clear();
					}

					sleep(8000);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
	}
}
