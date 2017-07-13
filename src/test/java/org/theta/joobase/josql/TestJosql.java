package org.theta.joobase.josql;

import java.util.ArrayList;
import java.util.List;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class TestJosql {

	private List<DataObject> datas;

	@Before
	public void prepareData() {
		datas = new ArrayList<>();
		DataObject do1 = new DataObject(1, 2, 3, 4, "1");
		DataObject do2 = new DataObject(5, 6, 7, 8, "11");
		datas.add(do1);
		datas.add(do2);
	}

	@Test
	public void testBaseQuery() throws QueryParseException, QueryExecutionException {
		Query q = new Query();
		q.parse("select pr1,pr2 from org.theta.joobase.josql.DataObject where pr2.toString = ? ");
		q.setVariable(1, 2);
		QueryResults results = q.execute(datas);

		System.out.println(results.getResults().size());
	}

}
