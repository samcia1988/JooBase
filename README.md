# JooBase

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)]()

# Overview

Java Object Oriented dataBase.

# Features

1. SQL parse engine : Use JoSql package and give interface-annotation formed encapsulation.
2. Backward storage : Now just JVM storage.(I think about full-distrubuted storage later) 

# Quick Start

1. Add dependencies.(//TODO)

2. Create an interface like this.

    ```java
    @JooShard("s1")
    public interface MyInterface {
    
    	@JooQuery(value = "select * from org.theta.joobase.josql.DataObject where pr1>:pr1", parameterPolicy = JooParameterPolicy.ByName)
    	public List<DataObject> selectTest(@JooParameter("pr1") int arg0);
    
    	@JooQuery(value = "select * from org.theta.joobase.josql.DataObject where pr5 = ?", parameterPolicy = JooParameterPolicy.ByOrder)
    	@JooShard("s2")
    	public List<DataObject> selectTest2(String para1);
    
    	@JooInsert
    	public int insertDataObjectS1(DataObject... dataObject);
    
    	@JooInsert
    	@JooShard("s2")
    	public int insertDataObjectS2(DataObject dataObject);
    
    }
    
    ```

3. Use JooDaoFactory to call them.

    ```java
		MyInterface ti = JooDaoFactory.getDao(MyInterface.class);

		DataObject obj1 = new DataObject(1, 2, 3, 4, "5");
		System.out.println(ti.insertDataObjectS1(obj1,obj1,obj1));
		System.out.println(ti.insertDataObjectS2(obj1));
		List<DataObject> result = ti.selectTest2("5");
		System.out.println(result);
     ```

# Documents

* [JoSQL homepage](http://josql.sourceforge.net)

# Contributors

* ranger([@ranger](https://github.com/samcia1988))

# License

JooBase is released under the [MIT License](https://mit-license.org/).