package com.funnel.redis.publish;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import redis.clients.jedis.JedisPubSub;

import com.funnel.redis.RedisServcie;

public class RedisMessagePublisher {
	private RedisServcie redisServcie;

	private Map<String, JedisPubSub> subscribers = new HashMap<String, JedisPubSub>();

	public void start() {
		Iterator<String> iterator = subscribers.keySet().iterator();
		while (iterator.hasNext()) {
			String channel = iterator.next();
			redisServcie.subscribe(subscribers.get(channel), channel);
		}

	}

	public void end() {
		Iterator<String> iterator = subscribers.keySet().iterator();
		while (iterator.hasNext()) {
			String channel = iterator.next();
			subscribers.get(channel).unsubscribe(channel);
		}
	}

	public RedisServcie getRedisServcie() {
		return redisServcie;
	}

	public void setRedisServcie(RedisServcie redisServcie) {
		this.redisServcie = redisServcie;
	}

	public Map<String, JedisPubSub> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Map<String, JedisPubSub> subscribers) {
		this.subscribers = subscribers;
	}

}
