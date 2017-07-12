package org.theta.joobase.dao;

import org.theta.joobase.annotations.JooParameter;
import org.theta.joobase.annotations.JooParameterPolicy;
import org.theta.joobase.annotations.JooQuery;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public interface TestInterface {

	@JooQuery(value = "select * from XXX", parameterPolicy = JooParameterPolicy.ByName)
	public void selectTest(@JooParameter("para1") String para1);

}
