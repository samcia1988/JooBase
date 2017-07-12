package org.theta.joobase.dao;

import org.junit.Test;
import org.theta.joobase.JooDaoFactory;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class TestDao {

	@Test
	public void testMain() {
		TestInterface ti = JooDaoFactory.getDao(TestInterface.class);
	}

}
