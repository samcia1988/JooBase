package org.theta.joobase.info;

import java.lang.reflect.Method;

/**
 * 
 * @author ranger
 * @date Jul 14, 2017
 *
 */
public class JooInsertMethodInfo extends JooMethodInfo {

	public JooInsertMethodInfo(Method method, String shard, JooMethodType methodType, Class<?> returnType) {
		super(method, shard, methodType, returnType);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
