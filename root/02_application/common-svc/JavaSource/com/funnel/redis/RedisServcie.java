package com.funnel.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisPubSub;

@SuppressWarnings("rawtypes")
public interface RedisServcie {
	public Set<String> hkeys(String key);

	public String hget(String key, String field);

	public <T> T hgetList(String key, String field, Class elememtClass);

	public <T> T hgetMap(String key, String field, Class elememtClass);

	public Map<String, String> hgetAll(String key);

	public long hset(String key, String field, String value);

	public long hset(String key, String field, Object value);

	public long hincrby(String key, String field);

	public long hincrby(String key, String field, Integer num);

	public Double hincrbyFloat(String key, String field, Float num);

	public long incrby(String key);

	public long incrby(String key, Integer num);

	public Double incrbyFloat(String key, Float num);

	public long hdel(String key, String field);

	public long del(String key);

	/**
	 * 设置key的过期时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int seconds);

	public String hmset(String key, Map hash);

	public String set(String key, String value);

	public String setex(String key, int seconds, String value);

	public String setex(String key, int seconds, Object value);

	public Set<String> keys(String pattern);

	public String get(String key);

	public <T> T get(String key, Class targetClass);

	public boolean exists(String key);

	public Set<String> smembers(String key);

	public Long sadd(String key, String... members);

	public Long srem(String key, String... members);

	public Long publish(String channel, Object msg);

	public void subscribe(final JedisPubSub jedisPubSub, final String channel);

	public void lpush(String key, Object value);

	public void rpush(String key, Object value);

	public String lpop(String key);

	public String rpop(String key);

	public List<String> blpop(String... key);

	public List<String> brpop(String... key);

	public Long llen(String key);

	public List<String> lrange(String key, int start, int end);

}
