package org.theta.joobase.shard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public class JooShardManager {

	private static Map<String, Map<Class<?>, List<Object>>> shards;

	static {
		shards = new HashMap<>();
	}

	public static List<Object> addShard(String shardName, Class<?> clazz) {
		if (!shards.containsKey(shardName))
			shards.put(shardName, new HashMap<Class<?>, List<Object>>());
		Map<Class<?>, List<Object>> shardMap = shards.get(shardName);
		if (shardMap.containsKey(clazz))
			throw new RuntimeException(
					String.format("Shard(%s) - Class(%s) exists already", shardName, clazz.getName()));
		List<Object> shard = new ArrayList<Object>();
		shardMap.put(clazz, shard);
		return shard;
	}

	public static List<Object> getShard(String shardName, Class<?> clazz) {
		if (shards.get(shardName) == null)
			return null;
		return shards.get(shardName).get(clazz);
	}

	public static long addOne(Object obj, String shardName) {
		try {
			List<Object> shard = JooShardManager.getShard(shardName, obj.getClass());
			if (shard == null)
				shard = JooShardManager.addShard(shardName, obj.getClass());
			shard.add(obj);
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public static long addBatch(Object[] objs, String shardName) {
		long count = 0;
		for (Object obj : objs)
			count += addOne(obj, shardName);
		return count;
	}

	public static long addBatch(List<?> objs, String shardName) {
		long count = 0;
		for (Object obj : objs)
			count += addOne(obj, shardName);
		return count;
	}

}
