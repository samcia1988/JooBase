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
public class JooShardManagerMemory implements JooShardManager {

	private Map<String, Map<Class<?>, List<Object>>> shards;

	public JooShardManagerMemory() {
		shards = new HashMap<>();
	}

	@Override
	public List<Object> addShard(String shardName, Class<?> clazz) {
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

	@Override
	public List<Object> getShard(String shardName, Class<?> clazz) {
		if (shards.get(shardName) == null)
			return null;
		return shards.get(shardName).get(clazz);
	}

	@Override
	public long addOne(Object obj, String shardName) {
		try {
			List<Object> shard = this.getShard(shardName, obj.getClass());
			if (shard == null)
				shard = this.addShard(shardName, obj.getClass());
			shard.add(obj);
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	@Override
	public long addBatch(Object[] objs, String shardName) {
		long count = 0;
		for (Object obj : objs)
			count += addOne(obj, shardName);
		return count;
	}

	@Override
	public long addBatch(List<?> objs, String shardName) {
		long count = 0;
		for (Object obj : objs)
			count += addOne(obj, shardName);
		return count;
	}

}
