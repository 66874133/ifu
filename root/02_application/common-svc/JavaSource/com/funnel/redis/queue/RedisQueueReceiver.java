package com.funnel.redis.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.funnel.redis.RedisServcie;
import com.funnel.svc.comon.AsyncServiceUtil;
import com.funnel.svc.util.JsonUtil;

public class RedisQueueReceiver {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private RedisServcie redisServcie;
	private Map<String, RedisConsumer> consumers;
	private long failSleepTime = 0;
	private List<ReciverRunnable> runnables = new ArrayList<ReciverRunnable>();

	public void start() {
		for (Map.Entry<String, RedisConsumer> entry : consumers.entrySet()) {
			ReciverRunnable reciverRunnable = new ReciverRunnable();
			reciverRunnable.setQueueName(entry.getKey());
			reciverRunnable.setConsumer(entry.getValue());
			new Thread(reciverRunnable).start();
			runnables.add(reciverRunnable);
		}
	}

	public void end() {
		for (ReciverRunnable runnable : runnables) {
			runnable.end();
		}
	}

	private class ReciverRunnable implements Runnable {
		private String queueName;
		private RedisConsumer consumer;
		private boolean flag = true;

		@Override
		public void run() {
			while (flag) {
				try {
					List<String> receiveInfo = redisServcie.brpop(queueName);
					reciver(receiveInfo.get(0), receiveInfo.get(1));
				} catch (Exception e) {
					logger.error("接收redis消息队列:" + queueName + " 消息失败,sleep:"
							+ failSleepTime, e);
					if (failSleepTime > 0) {
						try {
							Thread.sleep(failSleepTime);
						} catch (InterruptedException e1) {
							logger.error("接收redis消息队列:" + queueName
									+ " 消息失败,sleep:" + failSleepTime
									+ " sleep失败", e);
						}
					}
				}
			}
		}

		public void reciver(final String queueName, final String msg) {
			try {
				ThreadPoolExecutor executor = AsyncServiceUtil.getExecutor(
						"reidsQueue:" + queueName + ":executorKey",
						consumer.getCorePoolSize(), consumer.getMaxPoolSize(),
						consumer.getQueueCapacity());

				executor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							consumer.onMessage(msg);
						} catch (Exception e) {
							logger.error("queueName:" + queueName
									+ " 消费着处理失败,msg:" + msg, e);
						}

					}
				});
			} catch (Exception e) {
				logger.error("queueName:" + queueName
						+ " 放入消息执行队列失败,放回原队列,msg:" + msg, e);
				redisServcie.lpush(queueName, JsonUtil.toJSONObject(msg));
				if (consumer.getFailWaitTime() > 0) {
					try {
						Thread.sleep(consumer.getFailWaitTime());
					} catch (InterruptedException e1) {
						logger.error("消息队列:" + queueName + " 处理消息消息失败,sleep:"
								+ failSleepTime + " sleep失败", e);
					}
				}
			}
		}

		public void setQueueName(String queueName) {
			this.queueName = queueName;
		}

		public void end() {
			flag = false;
		}

		public void setConsumer(RedisConsumer consumer) {
			this.consumer = consumer;
		}

	}

	public Map<String, RedisConsumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(Map<String, RedisConsumer> consumers) {
		this.consumers = consumers;
	}

	public RedisServcie getRedisServcie() {
		return redisServcie;
	}

	public void setRedisServcie(RedisServcie redisServcie) {
		this.redisServcie = redisServcie;
	}

	public long getFailSleepTime() {
		return failSleepTime;
	}

	public void setFailSleepTime(long failSleepTime) {
		this.failSleepTime = failSleepTime;
	}

}
