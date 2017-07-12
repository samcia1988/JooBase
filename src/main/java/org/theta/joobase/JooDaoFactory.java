package org.theta.joobase;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.joobase.annotations.JooParameter;
import org.theta.joobase.annotations.JooParameterPolicy;
import org.theta.joobase.annotations.JooQuery;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class JooDaoFactory {

	private static Logger logger = LoggerFactory.getLogger(JooDaoFactory.class);

	public static <T> T getDao(Class<?> clazz) {
		try {
			if (clazz == null)
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a not null parameter of the dao interface's class.");
			if (!clazz.isInterface())
				throw new RuntimeException(
						"JooDaoFactory's getDao() method must be given a interface class parameter.");

			logger.debug("JooDaoFactory getDao() method get return type:{}", clazz.getName());

			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				JooQuery ann = method.getDeclaredAnnotation(JooQuery.class);
				if (ann != null) {
					String sql = ann.value();
					JooParameterPolicy policy = ann.parameterPolicy();
					String[] parameterNames = null;
					switch (policy) {
					case ByOrder:
						break;
					case ByName:
						parameterNames = new String[method.getParameters().length];
						for (int i = 0; i < method.getParameters().length; i++) {
							JooParameter jooParameter = method.getParameters()[i]
									.getDeclaredAnnotation(JooParameter.class);
							if (jooParameter == null)
								throw new RuntimeException(String.format(
										"In ByName policy,current parameter has no JooParameter annotation.Class:%s,Method:%s,Parameter:%s",
										clazz.getName(), method.getName(), method.getParameters()[i].getName()));
							parameterNames[i] = jooParameter.value();
						}
						break;
					default:
						throw new RuntimeException("Undefined parameter policy:" + policy);
					}
					logger.debug(
							"Method {} has JooQuery annotation. Sql value is ({}). Parameter policy is {}.Parameters are ({})",
							method.getName(), sql, policy, parameterNames);
				}
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
