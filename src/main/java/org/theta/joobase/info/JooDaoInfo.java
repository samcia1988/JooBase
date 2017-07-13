package org.theta.joobase.info;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public class JooDaoInfo {

	private String shard;

	private Class<?> clazz;

	private Map<Method, JooMethodInfo> methodInfo;

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Map<Method, JooMethodInfo> getMethodInfo() {
		return methodInfo;
	}

	public void setMethodInfo(Map<Method, JooMethodInfo> methodInfo) {
		this.methodInfo = methodInfo;
	}

	@Override
	public String toString() {
		return String.format("(Class:%s,Shard:%s,Method:%s)", clazz.getName(), shard,
				methodInfo == null ? "null" : methodInfo.values().toString());
	}

}
