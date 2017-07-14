package org.theta.joobase.proxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
import org.theta.joobase.info.JooInsertMethodInfo;
import org.theta.joobase.info.JooMethodInfo;
import org.theta.joobase.info.JooQueryMethodInfo;
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
			if (jooMethodInfo.getMethodType() == null)
				throw new RuntimeException("Wrong joo method info.Its method type shouldn't be null-value.");
			switch (jooMethodInfo.getMethodType()) {
			case Query:
				return handleQuery(args, (JooQueryMethodInfo) jooMethodInfo);
			case Insert:
				return handleInsert(args, (JooInsertMethodInfo) jooMethodInfo);
			default:
				throw new RuntimeException("Undefined JooMethodType:" + jooMethodInfo.getMethodType());
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private Object handleQuery(Object[] args, JooQueryMethodInfo queryMethodInfo)
			throws QueryParseException, ClassNotFoundException, QueryExecutionException {
		Query query = new Query();
		query.parse(queryMethodInfo.getSql());
		if (args != null)
			switch (queryMethodInfo.getPolicy()) {
			case ByOrder: {
				for (int i = 0; i < args.length; i++)
					query.setVariable(i + 1,
							args[i]); /*- JoSQL's anonymous args should start from 1 instead of Java's default value 0. */
			}
				break;
			case ByName: {
				for (int i = 0; i < args.length; i++)
					query.setVariable(queryMethodInfo.getParameterNames()[i], args[i]);
			}
				break;
			default:
				throw new RuntimeException("Undefined parameter policy:" + queryMethodInfo.getPolicy());
			}
		Class<?> originalReturnType = queryMethodInfo.getReturnType();
		Class<?> returnType = originalReturnType;
		if (originalReturnType.isAssignableFrom(List.class)) {
			Type actualType = ((ParameterizedType) queryMethodInfo.getMethod().getGenericReturnType())
					.getActualTypeArguments()[0];
			returnType = Class.forName(actualType.getTypeName());
		}
		QueryResults results = query.execute(JooShardManager.getShard(queryMethodInfo.getShard(), returnType));
		return results.getResults();
	}

	private Object handleInsert(Object[] args, JooInsertMethodInfo insertMethodInfo) {
		String shardName = insertMethodInfo.getShard();
		long count = 0;
		if (args != null)
			for (Object arg : args)
				if (arg != null) {
					if (arg.getClass().isArray()) {
						Object[] argArr = (Object[]) arg;
						count += JooShardManager.addBatch(argArr, shardName);
					} else if (arg instanceof List) {
						List<?> argList = (List<?>) arg;
						count += JooShardManager.addBatch(argList, shardName);
					} else {
						count += JooShardManager.addOne(arg, shardName);
					}
				}
		Class<?> returnType = insertMethodInfo.getReturnType();
		if (returnType == int.class || returnType == Integer.class || returnType == long.class
				|| returnType == Long.class)
			return count;
		return null;
	}

}
