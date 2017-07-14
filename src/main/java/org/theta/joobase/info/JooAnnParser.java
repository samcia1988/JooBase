package org.theta.joobase.info;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.joobase.annotations.JooInsert;
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
			JooMethodInfo methodInfo = null;
			JooShard jooShardAnnMethod = method.getDeclaredAnnotation(JooShard.class);
			String shard = null;
			if (jooShardAnnMethod != null)
				shard = jooShardAnnMethod.value();
			else
				shard = daoInfo.getShard();

			JooQuery jooQueryAnn = method.getDeclaredAnnotation(JooQuery.class);
			JooInsert jooInsertAnn = method.getDeclaredAnnotation(JooInsert.class);
			if (jooQueryAnn != null) {
				JooQueryMethodInfo queryMethodInfo = new JooQueryMethodInfo(method, shard, JooMethodType.Query,
						method.getReturnType());

				String sql = jooQueryAnn.value();
				JooParameterPolicy policy = jooQueryAnn.parameterPolicy();
				queryMethodInfo.setSql(sql);
				queryMethodInfo.setPolicy(policy);

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
					queryMethodInfo.setParameterNames(parameterNames);
					break;
				default:
					throw new RuntimeException("Undefined parameter policy:" + policy);
				}
				logger.debug("Method {} has JooQuery annotation. JooInfo is {}", method.getName(),
						queryMethodInfo.toString());

				methodInfo = queryMethodInfo;
			} else if (jooInsertAnn != null) {
				JooInsertMethodInfo insertMethodInfo = new JooInsertMethodInfo(method, shard, JooMethodType.Insert,
						method.getReturnType());

				methodInfo = insertMethodInfo;
			}

			if (methodInfo != null) {
				if (daoInfo.getMethodInfo() == null)
					daoInfo.setMethodInfo(new HashMap<Method, JooMethodInfo>());
				daoInfo.getMethodInfo().put(method, methodInfo);
			}
		}

		logger.debug("Class {} JooInfo:{}", daoInfo.toString());

		return daoInfo;
	}

}
