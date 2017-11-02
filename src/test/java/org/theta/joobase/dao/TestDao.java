package org.theta.joobase.dao;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.theta.joobase.JooDaoFactory;
import org.theta.joobase.josql.DataObject;
import org.theta.joobase.shard.JooShardManagerMemory;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class TestDao {

	@Test
	public void testMain() {
		JooDaoFactory.setJooShardManager(new JooShardManagerMemory());

		MyInterface ti = JooDaoFactory.getDao(MyInterface.class);

		DataObject obj1 = new DataObject(1, 2, 3, 4, "5");
		System.out.println(ti.insertDataObjectS1(obj1, obj1, obj1));
		System.out.println(ti.insertDataObjectS2(obj1));
		List<DataObject> result = ti.selectTest2("5");
		System.out.println(result);
	}

	@Before
	public void before() {
		/*-
		JooShardManager.addShard("s1", DataObject.class);
		DataObject obj1 = new DataObject(1, 2, 3, 4, "5");
		JooShardManager.getShard("s1", DataObject.class).add(obj1);
		JooShardManager.addShard("s2", DataObject.class);
		JooShardManager.getShard("s2", DataObject.class).add(obj1);
		 */
	}

}
