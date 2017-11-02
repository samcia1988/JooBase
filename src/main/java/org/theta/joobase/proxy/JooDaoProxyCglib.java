package org.theta.joobase.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.theta.joobase.info.JooDaoInfo;
import org.theta.joobase.info.JooMethodInfo;
import org.theta.joobase.shard.JooShardManager;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * @author ranger
 * @date Jul 13, 2017
 *
 */
public class JooDaoProxyCglib implements MethodInterceptor {

	private JooDaoInfo jooDaoInfo;

	private Map<Method, JooMethodProxy> proxies;

	private JooDaoProxyCglib(JooDaoInfo jooDaoInfo, JooShardManager shardManager) {
		this.jooDaoInfo = jooDaoInfo;
		proxies = new HashMap<>();
		if (this.jooDaoInfo.getMethodInfo() != null)
			for (Method method : this.jooDaoInfo.getMethodInfo().keySet()) {
				JooMethodInfo methodInfo = this.jooDaoInfo.getMethodInfo().get(method);
				JooMethodProxy proxy = JooMethodProxy.getInstance(methodInfo, shardManager);
				proxies.put(method, proxy);
			}
	}

	public static JooDaoProxyCglib getInstance(JooDaoInfo jooDaoInfo, JooShardManager shardManager) {
		return new JooDaoProxyCglib(jooDaoInfo, shardManager);
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (proxies.containsKey(method))
			return proxies.get(method).invoke(args);
		else
			throw new RuntimeException(String.format("JooDaoProxy doesn't contain method:%s", method.getName()));
	}

}
