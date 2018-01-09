package com.funnel.redis.queue;

public interface IMessageHandler {
	void handle(String msg) throws Exception;
}
