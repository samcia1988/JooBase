package org.theta.joobase.proxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.josql.Query;
import org.josql.QueryResults;
import org.theta.joobase.info.JooMethodInfo;
import org.theta.joobase.shard.JooShardManager;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public class JooMethodProxy {

	private JooMethodInfo jooMethodInfo;

	private JooMethodProxy(JooMethodInfo jooMethodInfo) {
		this.jooMethodInfo = jooMethodInfo;
	}

	public static JooMethodProxy getInstance(JooMethodInfo jooMethodInfo) {
		return new JooMethodProxy(jooMethodInfo);
	}

	public Object invoke(Object[] args) {
		try {
			Query query = new Query();
			query.parse(jooMethodInfo.getSql());
			if (args != null)
				switch (jooMethodInfo.getPolicy()) {
				case ByOrder: {
					for (int i = 0; i < args.length; i++)
						query.setVariable(i + 1,
								args[i]); /*- JoSQL's anonymous args should start from 1 instead of Java's default value 0. */
				}
					break;
				case ByName: {
					for (int i = 0; i < args.length; i++)
						query.setVariable(jooMethodInfo.getParameterNames()[i], args[i]);
				}
					break;
				default:
					throw new RuntimeException("Undefined parameter policy:" + jooMethodInfo.getPolicy());
				}
			Class<?> originalReturnType = jooMethodInfo.getReturnType();
			Class<?> returnType = originalReturnType;
			if (originalReturnType.isAssignableFrom(List.class)) {
				Type actualType = ((ParameterizedType) jooMethodInfo.getMethod().getGenericReturnType())
						.getActualTypeArguments()[0];
				returnType = Class.forName(actualType.getTypeName());
			}
			QueryResults results = query.execute(JooShardManager.getShard(jooMethodInfo.getShard(), returnType));
			return results.getResults();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
