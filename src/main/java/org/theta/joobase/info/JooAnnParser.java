package org.theta.joobase.info;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.joobase.annotations.JooParameter;
import org.theta.joobase.annotations.JooParameterPolicy;
import org.theta.joobase.annotations.JooQuery;
import org.theta.joobase.annotations.JooShard;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public abstract class JooAnnParser {
	private static Logger logger = LoggerFactory.getLogger(JooAnnParser.class);

	public static JooDaoInfo parse(Class<?> clazz) {
		JooDaoInfo daoInfo = new JooDaoInfo();
		daoInfo.setClazz(clazz);

		JooShard jooShardAnn = clazz.getDeclaredAnnotation(JooShard.class);
		if (jooShardAnn != null)
			daoInfo.setShard(jooShardAnn.value());

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			JooMethodInfo methodInfo = new JooMethodInfo();
			methodInfo.setMethod(method);
			JooShard jooShardAnnMethod = method.getDeclaredAnnotation(JooShard.class);
			if (jooShardAnnMethod != null)
				methodInfo.setShard(jooShardAnnMethod.value());
			else
				methodInfo.setShard(daoInfo.getShard());

			JooQuery jooQueryAnn = method.getDeclaredAnnotation(JooQuery.class);
			if (jooQueryAnn != null) {
				String sql = jooQueryAnn.value();
				JooParameterPolicy policy = jooQueryAnn.parameterPolicy();
				methodInfo.setSql(sql);
				methodInfo.setPolicy(policy);
				methodInfo.setReturnType(method.getReturnType());

				String[] parameterNames = null;
				switch (policy) {
				case ByOrder:
					break;
				case ByName:
					parameterNames = new String[method.getParameters().length];
					for (int i = 0; i < method.getParameters().length; i++) {
						JooParameter jooParameter = method.getParameters()[i].getDeclaredAnnotation(JooParameter.class);
						if (jooParameter == null)
							throw new RuntimeException(String.format(
									"In ByName policy,current parameter has no JooParameter annotation.Class:%s,Method:%s,Parameter:%s",
									clazz.getName(), method.getName(), method.getParameters()[i].getName()));
						parameterNames[i] = jooParameter.value();
					}
					methodInfo.setParameterNames(parameterNames);
					break;
				default:
					throw new RuntimeException("Undefined parameter policy:" + policy);
				}
				logger.debug("Method {} has JooQuery annotation. JooInfo is {}", method.getName(),
						methodInfo.toString());
				if (daoInfo.getMethodInfo() == null)
					daoInfo.setMethodInfo(new HashMap<Method, JooMethodInfo>());
				daoInfo.getMethodInfo().put(method, methodInfo);
			}
		}

		logger.debug("Class {} JooInfo:{}", daoInfo.toString());

		return daoInfo;
	}

}
