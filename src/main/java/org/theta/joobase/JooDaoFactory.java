package org.theta.joobase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.joobase.info.JooAnnParser;
import org.theta.joobase.info.JooDaoInfo;
import org.theta.joobase.proxy.JooDaoProxyCglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public abstract class JooDaoFactory {

	private static Logger logger = LoggerFactory.getLogger(JooDaoFactory.class);

	@SuppressWarnings("unchecked")
	public static <T> T getDao(Class<?> clazz) {
		try {
			if (clazz == null)
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a not null parameter of the dao interface's class.");
			if (!clazz.isInterface())
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a interface class parameter.");

			logger.debug("JooDaoFactory getDao() method get return type:{}", clazz.getName());

			JooDaoInfo daoInfo = JooAnnParser.parse(clazz);

			JooDaoProxyCglib proxy = JooDaoProxyCglib.getInstance(daoInfo);

			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clazz);
			enhancer.setCallback(proxy);
			return (T) enhancer.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
