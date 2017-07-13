package org.theta.joobase.dao;

import java.util.List;

import org.theta.joobase.annotations.JooParameter;
import org.theta.joobase.annotations.JooParameterPolicy;
import org.theta.joobase.annotations.JooQuery;
import org.theta.joobase.annotations.JooShard;
import org.theta.joobase.josql.DataObject;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
@JooShard("s1")
public interface MyInterface {

	@JooQuery(value = "select * from org.theta.joobase.josql.DataObject where pr1>:pr1", parameterPolicy = JooParameterPolicy.ByName)
	public List<DataObject> selectTest(@JooParameter("pr1") int arg0);

	@JooQuery(value = "select * from XXX", parameterPolicy = JooParameterPolicy.ByName)
	@JooShard("s2")
	public List<DataObject> selectTest2(@JooParameter("para1") String para1);

}
