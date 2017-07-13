package org.theta.joobase.info;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.theta.joobase.annotations.JooParameterPolicy;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public class JooMethodInfo {

	private Method method;

	private String shard;

	private String sql;

	private JooParameterPolicy policy;

	private String[] parameterNames;

	private Class<?> returnType;

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public JooParameterPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(JooParameterPolicy policy) {
		this.policy = policy;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return String.format("(Method:%s,Shard:%s,Sql:%s,Policy:%s,parameterNames:%s)", method.getName(), shard, sql,
				policy.name(), Arrays.toString(parameterNames));
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

}
