package org.theta.joobase.shard;

import java.util.List;

/**
 * 
 * @author ranger
 * @date Nov 2, 2017
 *
 */
public interface JooShardManager {

	/**
	 * 
	 * @param shardName
	 * @param clazz
	 * @return
	 */
	public List<Object> getShard(String shardName, Class<?> clazz);

	/**
	 * 
	 * @param shardName
	 * @param clazz
	 * @return
	 */
	public List<Object> addShard(String shardName, Class<?> clazz);

	/**
	 * 
	 * @param obj
	 * @param shardName
	 * @return
	 */
	public long addOne(Object obj, String shardName);

	/**
	 * 
	 * @param objs
	 * @param shardName
	 * @return
	 */
	public long addBatch(Object[] objs, String shardName);

	/**
	 * 
	 * @param objs
	 * @param shardName
	 * @return
	 */
	public long addBatch(List<?> objs, String shardName);

}
