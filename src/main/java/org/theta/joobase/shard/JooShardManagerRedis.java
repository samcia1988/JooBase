package org.theta.joobase.shard;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author ranger
 * @date Nov 2, 2017
 *
 */
public class JooShardManagerRedis implements JooShardManager {

	private JedisPool pool;

	private static JooShardManagerRedis instance;

	public static JooShardManagerRedis getInstance() {
		if (instance == null)
			throw new RuntimeException("JooShardManagerRedis instance hasn't been initialized.");
		return instance;
	}

	public static synchronized void init(String host, int port, int dbIndex, String password) {
		instance = new JooShardManagerRedis(host, port, dbIndex, password);
	}

	private JooShardManagerRedis(String host, int port, int dbIndex, String password, int connectionTimeout,
			int maxTotal, int maxIdle, int minIdle, int maxWaitMillis) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setTestOnBorrow(true);
		config.setTestOnCreate(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);

		pool = new JedisPool(config, host, port, connectionTimeout, password, dbIndex, null);
	}

	private Jedis getJedis() {
		return pool.getResource();
	}

	private JooShardManagerRedis(String host, int port, int dbIndex, String password) {
		this(host, port, dbIndex, password, 5000, 8, 8, 0, -1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getShard(String shardName, Class<?> clazz) {
		Jedis jedis = getJedis();
		String objSer = jedis.hget(shardName, clazz.getName());
		if (objSer == null)
			return null;
		List<Object> objs = (List<Object>) JSON.parseArray(objSer, clazz);
		return objs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long addOne(Object obj, String shardName) {
		Jedis jedis = getJedis();
		jedis.watch(shardName);
		String className = obj.getClass().getName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		String objSer = jedis.hget(shardName, className);
		List<Object> writeObjs = null;
		if (objSer == null)
			writeObjs = new ArrayList<>();
		else
			writeObjs = (List<Object>) JSON.parseArray(objSer, clazz);
		writeObjs.add(obj);
		objSer = JSON.toJSONString(writeObjs);
		long ret = jedis.hset(shardName, className, objSer);
		return (ret == 1 || ret == 0) ? 1 : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long addBatch(Object[] objs, String shardName) {
		if (objs == null || objs.length == 0)
			return 0;
		long count = objs.length;
		Jedis jedis = getJedis();
		String className = objs[0].getClass().getName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		String objSer = jedis.hget(shardName, className);
		List<Object> writeObjs = null;
		if (objSer == null)
			writeObjs = new ArrayList<>();
		else
			writeObjs = (List<Object>) JSON.parseArray(objSer, clazz);
		for (int i = 0; i < objs.length; i++)
			writeObjs.add(objs[i]);
		objSer = JSON.toJSONString(writeObjs);
		long ret = jedis.hset(shardName, className, objSer);

		return (ret == 1 || ret == 0) ? count : 0;
	}

	@Override
	public long addBatch(List<?> objs, String shardName) {
		if (objs == null)
			return 0;
		return addBatch(objs.toArray(), shardName);
	}

}
