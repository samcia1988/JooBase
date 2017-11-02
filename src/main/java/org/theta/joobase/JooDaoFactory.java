package org.theta.joobase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.joobase.info.JooAnnParser;
import org.theta.joobase.info.JooDaoInfo;
import org.theta.joobase.proxy.JooDaoProxyCglib;
import org.theta.joobase.shard.JooShardManager;
import org.theta.joobase.shard.JooShardManagerMemory;
import org.theta.joobase.shard.JooShardManagerRedis;

import net.sf.cglib.proxy.Enhancer;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public abstract class JooDaoFactory {

	private static Logger logger = LoggerFactory.getLogger(JooDaoFactory.class);

	private static JooShardManager jooShardManager;

	@SuppressWarnings("unchecked")
	public static <T> T getDao(Class<?> clazz) {
		try {
			if (jooShardManager == null)
				throw new RuntimeException("JooDaoFactory's JooShardManager must be set.");
			if (clazz == null)
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a not null parameter of the dao interface's class.");
			if (!clazz.isInterface())
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a interface class parameter.");

			logger.debug("JooDaoFactory getDao() method get return type:{}", clazz.getName());

			JooDaoInfo daoInfo = JooAnnParser.parse(clazz);

			JooDaoProxyCglib proxy = JooDaoProxyCglib.getInstance(daoInfo, jooShardManager);

			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clazz);
			enhancer.setCallback(proxy);
			return (T) enhancer.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setJooShardManager(JooShardManager jooShardManager) {
		JooDaoFactory.jooShardManager = jooShardManager;
	}

	public static void createMemoryJooShardManager() {
		setJooShardManager(JooShardManagerMemory.getInstance());
	}

	public static void createRedisJooShardManager(String host, int port, int dbIndex, String password) {
		JooShardManagerRedis.init(host, port, dbIndex, password);
		setJooShardManager(JooShardManagerRedis.getInstance());
	}

}
