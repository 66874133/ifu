package com.funnel.redis.queue;

import org.apache.log4j.Logger;

public class DefaultRedisConsumer implements RedisConsumer {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private int corePoolSize = 1;
	private int maxPoolSize = 1;
	private int queueCapacity = 1;
	private long failWaitTime = 1000;
	private IMessageHandler messageHandler;

	public void onMessage(String msg) {
		try {
			if (null != messageHandler) {
				messageHandler.handle(msg);
			}
		} catch (Exception e) {
			logger.error("处理消息失败:" + msg, e);
		}
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public long getFailWaitTime() {
		return failWaitTime;
	}

	public void setFailWaitTime(long failWaitTime) {
		this.failWaitTime = failWaitTime;
	}

	public IMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(IMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

}
