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

	public static void addShard(String shardName, Class<?> clazz) {
		if (!shards.containsKey(shardName))
			shards.put(shardName, new HashMap<Class<?>, List<Object>>());
		Map<Class<?>, List<Object>> shardMap = shards.get(shardName);
		if (shardMap.containsKey(clazz))
			throw new RuntimeException(
					String.format("Shard(%s) - Class(%s) exists already", shardName, clazz.getName()));
		shardMap.put(clazz, new ArrayList<Object>());
	}

	public static List<Object> getShard(String shardName, Class<?> clazz) {
		if (shards.get(shardName) == null)
			return null;
		return shards.get(shardName).get(clazz);
	}

}
