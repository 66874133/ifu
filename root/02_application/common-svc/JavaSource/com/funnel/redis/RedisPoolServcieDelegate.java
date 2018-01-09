package com.funnel.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.funnel.redis.util.JacksonUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisPoolServcieDelegate implements RedisServcie {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hget(key, field);
		} catch (Exception e) {
			throw new RedisException(
					"hget fail,key=" + key + ",field=" + field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hget time:" + userTime);
			}
		}
	}

	public <T> T hget(String key, String field, Class targetCalss) {
		String value = hget(key, field);
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return (T) JacksonUtil.json2pojo(value, targetCalss);
	}

	@Override
	public <T> T hgetList(String key, String field, Class elememtClass) {

		String value = hget(key, field);
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return (T) JacksonUtil.json2list(value, elememtClass);

	}

	@Override
	public <T> T hgetMap(String key, String field, Class elememtClass) {

		String value = hget(key, field);
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return (T) JacksonUtil.json2map(value, elememtClass);

	}

	@Override
	public long hset(String key, String field, String value) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			throw new RedisException(
					"hset fail,key=" + key + ",field=" + field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hset time:" + userTime);
			}
		}
	}

	@Override
	public long hset(String key, String field, Object value) {
		return hset(key, field, JacksonUtil.obj2json(value));
	}

	@Override
	public long hincrby(String key, String field) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hincrBy(key, field, 1);
		} catch (Exception e) {
			throw new RedisException("hincrby fail,key=" + key + ",field="
					+ field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hincrby time:" + userTime);
			}
		}
	}

	@Override
	public long hincrby(String key, String field, Integer num) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hincrBy(key, field, num);
		} catch (Exception e) {
			throw new RedisException("hincrby fail,key=" + key + ",field="
					+ field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hincrby time:" + userTime);
			}
		}
	}

	@Override
	public Double hincrbyFloat(String key, String field, Float num) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hincrByFloat(key, field, num);
		} catch (Exception e) {
			throw new RedisException("hincrby fail,key=" + key + ",field="
					+ field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hincrby time:" + userTime);
			}
		}
	}

	@Override
	public long incrby(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.incrBy(key, 1);
		} catch (Exception e) {
			throw new RedisException("incrby fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("incrby time:" + userTime);
			}
		}
	}

	@Override
	public long incrby(String key, Integer num) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.incrBy(key, num);
		} catch (Exception e) {
			throw new RedisException("incrby fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("incrby time:" + userTime);
			}
		}
	}

	@Override
	public Double incrbyFloat(String key, Float num) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.incrByFloat(key, num);
		} catch (Exception e) {
			throw new RedisException("incrbyFloat fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("incrbyFloat time:" + userTime);
			}
		}
	}

	@Override
	public long hdel(String key, String field) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hdel(key, field);
		} catch (Exception e) {
			throw new RedisException(
					"hdel fail,key=" + key + ",field=" + field, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hdel time:" + userTime);
			}
		}
	}

	@Override
	public long del(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			throw new RedisException("del fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("del time:" + userTime);
			}
		}
	}

	@Override
	public long expire(String key, int seconds) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.expire(key, seconds);
		} catch (Exception e) {
			throw new RedisException("expire fail,key=" + key + ",seconds="
					+ seconds, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("expire time:" + userTime);
			}
		}
	}

	@Override
	public String hmset(String key, Map hash) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			Map<String, String> saveMap = new HashMap<String, String>();
			Iterator<Map.Entry> it = hash.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = it.next();
				saveMap.put(entry.getKey().toString(),
						JacksonUtil.obj2json(entry.getValue()));
			}
			return jedis.hmset(key, saveMap);
		} catch (Exception e) {
			throw new RedisException("hmset fail,key=" + key + ",hash=" + hash,
					e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hmset time:" + userTime);
			}
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			throw new RedisException("hgetAll fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hgetAll time:" + userTime);
			}
		}
	}

	@Override
	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.hkeys(key);
		} catch (Exception e) {
			throw new RedisException("hkeys fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("hkeys time:" + userTime);
			}
		}
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			throw new RedisException("set fail,key=" + key + " value=" + value,
					e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("set time:" + userTime);
			}
		}
	}

	@Override
	public String setex(String key, int seconds, String value) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			throw new RedisException("setex fail,key=" + key + "seconds="
					+ seconds + " value=" + value, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("setex time:" + userTime);
			}
		}
	}

	public String setex(String key, int seconds, Object value) {
		return setex(key, seconds, JacksonUtil.obj2json(value));
	}

	@Override
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(pattern);
		} catch (Exception e) {
			throw new RedisException("keys fail,pattern=" + pattern, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("keys time:" + userTime);
			}
		}
	}

	@Override
	public String get(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			throw new RedisException("get fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("get time:" + userTime);
			}
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			throw new RedisException("exists fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("exists time:" + userTime);
			}
		}
	}

	@Override
	public <T> T get(String key, Class targetClass) {
		String value = get(key);
		long t = System.currentTimeMillis();
		if (!StringUtils.hasText(value)) {
			return null;
		}
		try {
			return (T) JacksonUtil.json2pojo(value, targetClass);
		} catch (Exception e) {
			throw new RedisException("getObject fail,key=" + key
					+ " targetClass=" + targetClass, e);
		} finally {

			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("getObject time:" + userTime);
			}
		}
	}

	@Override
	public Set<String> smembers(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.smembers(key);
		} catch (Exception e) {
			throw new RedisException("smembers fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("smembers time:" + userTime);
			}
		}
	}

	@Override
	public Long sadd(String key, String... members) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.sadd(key, members);
		} catch (Exception e) {
			throw new RedisException("sadd fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("sadd time:" + userTime);
			}
		}
	}

	@Override
	public Long srem(String key, String... members) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.srem(key, members);
		} catch (Exception e) {
			throw new RedisException("srem fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("srem time:" + userTime);
			}
		}
	}

	@Override
	public Long publish(String channel, Object msg) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.publish(channel, JacksonUtil.obj2json(msg));
		} catch (Exception e) {
			throw new RedisException("publish fail,channel=" + channel
					+ " msg=" + msg, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("publish time:" + userTime);
			}
		}
	}

	@Override
	public void subscribe(final JedisPubSub jedisPubSub, final String channel) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			final Jedis subJedis = jedis;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("开始监听渠道:" + channel + " 消息");
						subJedis.subscribe(jedisPubSub, channel);
						logger.info("结束监听渠道:" + channel + " 消息");
					} catch (Exception e) {
						logger.error("监听渠道:" + channel + " 消息失败", e);
					} finally {
						if (null != subJedis) {
							jedisPool.returnResourceObject(subJedis);
						}
					}
				}
			}).start();
		} catch (Exception e) {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			throw new RedisException("subscribe fail,channel=" + channel, e);
		}
	}

	@Override
	public void lpush(String key, Object value) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, JacksonUtil.obj2json(value));
		} catch (Exception e) {
			throw new RedisException("lpush fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("lpush time:" + userTime);
			}
		}

	}

	@Override
	public void rpush(String key, Object value) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			jedis.rpush(key, JacksonUtil.obj2json(value));
		} catch (Exception e) {
			throw new RedisException("rpush fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("rpush time:" + userTime);
			}
		}
	}

	@Override
	public String lpop(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.lpop(key);
		} catch (Exception e) {
			throw new RedisException("lpop fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("lpop time:" + userTime);
			}
		}

	}

	@Override
	public String rpop(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.rpop(key);
		} catch (Exception e) {
			throw new RedisException("rpop fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("rpop time:" + userTime);
			}
		}
	}

	@Override
	public List<String> blpop(String... key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.blpop(0, key);
		} catch (Exception e) {
			throw new RedisException("blpop fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("blpop time:" + userTime);
			}
		}
	}

	@Override
	public List<String> brpop(String... key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.brpop(0, key);
		} catch (Exception e) {
			throw new RedisException("brpop fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("brpop time:" + userTime);
			}
		}
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.llen(key);
		} catch (Exception e) {
			throw new RedisException("llen to obj fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("llen to obj time:" + userTime);
			}
		}
	}

	@Override
	public List<String> lrange(String key, int start, int end) {
		Jedis jedis = null;
		long t = System.currentTimeMillis();
		try {
			jedis = jedisPool.getResource();
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			throw new RedisException("lrange to obj fail,key=" + key, e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResourceObject(jedis);
			}
			long userTime = System.currentTimeMillis() - t;
			if (userTime > 30) {
				logger.warn("lrange to obj time:" + userTime);
			}
		}
	}

}
