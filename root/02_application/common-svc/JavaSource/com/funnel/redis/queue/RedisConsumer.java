package com.funnel.redis.queue;

public interface RedisConsumer {

	public void onMessage(String msg);

	public int getMaxPoolSize();

	public int getQueueCapacity();

	public int getCorePoolSize();

	public long getFailWaitTime();
}
