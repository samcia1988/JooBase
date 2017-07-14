package org.theta.joobase.info;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.theta.joobase.annotations.JooParameterPolicy;

/**
 * 
 * @author ranger
 * @date Jul 14, 2017
 *
 */
public class JooQueryMethodInfo extends JooMethodInfo {

	private String sql;

	private JooParameterPolicy policy;

	private String[] parameterNames;

	public JooQueryMethodInfo(Method method, String shard, JooMethodType methodType, Class<?> returnType) {
		super(method, shard, methodType, returnType);
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

	@Override
	public String toString() {
		return super.toString() + String.format(",Sql:%s,Policy:%s,parameterNames:%s)", sql, policy.name(),
				Arrays.toString(parameterNames));
	}

}
