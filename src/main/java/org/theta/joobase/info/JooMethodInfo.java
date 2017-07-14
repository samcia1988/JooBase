package org.theta.joobase.info;

import java.lang.reflect.Method;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public abstract class JooMethodInfo {

	private Method method;

	private String shard;

	private JooMethodType methodType;

	private Class<?> returnType;

	public JooMethodInfo() {

	}

	public JooMethodInfo(Method method, String shard, JooMethodType methodType, Class<?> returnType) {
		this.method = method;
		this.shard = shard;
		this.methodType = methodType;
		this.returnType = returnType;
	}

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public JooMethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(JooMethodType methodType) {
		this.methodType = methodType;
	}

	@Override
	public String toString() {
		return String.format("Method:%s,Shard:%s,JooMethodType:%s,ReturnType:%s", getMethod().getName(), getShard(),
				methodType, returnType);
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

}
