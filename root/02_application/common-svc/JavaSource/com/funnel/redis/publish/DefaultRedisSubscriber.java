package com.funnel.redis.publish;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPubSub;

import com.funnel.redis.queue.IMessageHandler;

public class DefaultRedisSubscriber extends JedisPubSub {

	private IMessageHandler messageHandler;
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onMessage(String channel, String message) {
		try {
			messageHandler.handle(message);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {

	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {

	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {

	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

	}

	public IMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(IMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

}
