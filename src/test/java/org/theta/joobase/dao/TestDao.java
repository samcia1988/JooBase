package org.theta.joobase.dao;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.theta.joobase.JooDaoFactory;
import org.theta.joobase.josql.DataObject;
import org.theta.joobase.shard.JooShardManager;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class TestDao {

	@Test
	public void testMain() {
		MyInterface ti = JooDaoFactory.getDao(MyInterface.class);
		List<DataObject> result = ti.selectTest(0);
		System.out.println(result);
	}

	@Before
	public void before() {
		JooShardManager.addShard("s1", DataObject.class);
		DataObject obj1 = new DataObject(1, 2, 3, 4, "5");
		JooShardManager.getShard("s1", DataObject.class).add(obj1);
	}

}
