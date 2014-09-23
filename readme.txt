----------------------------------------------------------------------------
 - TODO
----------------------------------------------------------------------------
  - Priority HIGH: 
      - Add the UNION clause
  - Priority NORMAL:
      - Create Parent NameSolver (Decorator pattern here is a good choice)
      - The save/update/delete(List) should use the batchUpdate that is way faster than calling simple update (Performance)
      - Add timeout per transaction (should we maintain the timeout per query?)
      - Auto identify properties named 'id' as bean id
                   
For version 8:
  - change Spring transaction manager to use TransactionTemplate
                     
----------------------------------------------------------------------------
 - KNOWN BUGS
----------------------------------------------------------------------------
  - FK needs that the related bean has exactly one primary key, this not verified

--------------------------------
 - Release 8.0.0 - 2014.xx.xx -
--------------------------------
  - Changed: 
  	- Removed now() method on save, update and delete orm queries
  	- Removed lazy() and cascade() methods
  	- EhCache cache implementation moved to separate module
  	- JSR303 validator implementation moved to separate module
  	- Oval validator removed
  	- Removed JodaType support
  	- Added support for new Java 8 time classes: LocalDateTime, LocalTime, ZonedDateTime, Instant


--------------------------------
 - Release 7.2.1 - 2013.11.12 -
--------------------------------
  - Fixed: It is now possible to build JPOrm with a JDK8 


--------------------------------
 - Release 7.2.0 - 2013.11.22 -
--------------------------------
  - Added: TypeWrapper for Enum that permits to use Enums as valid fields for a JPO Bean


--------------------------------
 - Release 7.1.0 - 2013.06.04 -
--------------------------------
  - Added: getInt, getLong, getString, getBigDecimal, getDouble, getFloat, getArray methods to the
           CustomQuery interface


--------------------------------
 - Release 7.0.4 - 
--------------------------------
  - Fixed: the get() method of find queries should return the first entry of the ResultSet 
  - Changed: the default cascade value for delete queries is set to true
  
  
--------------------------------
 - Release 7.0.0 - 
--------------------------------
  - Changed package base name from com.jpattern.orm to com.jporm
  - Added pagination through maxResult and firstRow
  - Added custom clauses in the Where expression of a Find query
  - Added the GROUP BY and HAVING clauses
  - Removed old SqlExecutor. Renamed SqlPerformer to SqlExecutor
  - Fixed the wrong version check performed by SmartRenderableSqlSubElement and SmartRenderableSqlQuery classes
  - Added a cache service to save rendered queries
  - Changed: all getInt() getLong() etc. methods return Wrappers instead of primitives; consequently, they can return a null value
  - Added Apache License, Version 2.0 - The Apache Software Foundation
  - Added Postgresql support
  - Added new where() methods to the Find Query: where(ExpressionElement... exp) and where(String whereClause, Object... args)
  - Added methods and(String whereClause, Object... args) and or(String whereClause, Object... args) to the Where interface
  
  
--------------------------------
 - Release 6.3.0 - 2013.04.18 -
--------------------------------
  - JodaTime objects no longer use UTC timezone 
  
  
----------------------------------------------------
 - Release 6.2.0 - WRONG RELEASE - NOT TO BE USED -
----------------------------------------------------


--------------------------------
 - Release 6.1.0 - 2013.03.10 -
--------------------------------
  - Added @Cache annotation
  - Added @Cascade annotation to define conditional update/save to bean relations
  - "lazy" on Fins queries is now false by default
  - The Session object is now stateless and Thread safe


--------------------------------
 - Release 6.0.0 - 2013.03.08 -
--------------------------------
  - Save/update/load/delete aggregated beans!
  - "Bean" is now used in the documentation instead of "object" or "entity"
  - Complete rewrite of the ValidatorService
  - Use of JUnit @RunWith(Parameterized.class) to launch the same test on multiple DBs
  - Added mysql 5 support
  - Removed conditional generators. A generator is now used only if the value is null or negative.
  - Added Generators fallback mode
  - added method findQuery(String[], Class, ... ) to the Session 
  - alias are now used in the CustomQuery to get from the ResultSet using the Bean property names
  - Added a cacheManager to JPO and the cache(String cacheName) method to the find queries
  - Class alias used in SQL query are now independent from Class alias used in JPO Queries
  - Added ignore() method to the Find queries to avoid fetching not needed field values from the DB


-------------------
 - Release 5.3.0 -
-------------------
  - Fixed: if a query parameter is null the generated sql is not valid
  - Fixed: if an exception is thrown during the "update" the bean is silently not updated nor saved! 
  - Added: Added a find() method in the findQuery that returns the first BEAN found or null
  - Added: Added an exist() method in the Session to know whether a Bean exists  


-------------------
 - Release 5.2.0 -
-------------------
  - Is now possible to add more ExpressionElements in OR and AND in a query
  - Added the Expression factory to help building ExpressionElements  


-------------------
 - Release 5.1.1 -
-------------------
  - Fixed wrong query's parameters log


-------------------
 - Release 5.1.0 -
-------------------
  - Added ResultSetRowReader<T> class
  - Added doInTransaction(TransactionRollback) method to the session 


-------------------
 - Release 5.0.0 -
-------------------
  - Heavy code refactoring
  - Complete rewriting of all the core Persistor related classes
  - Improved use of generics
  - Is now possible to write inline queries without using the query() method
  - Removed "from()" method from Queries. Is now possible to join directly from the root query.
  - Performance: no more array to List and viceversa conversions
  - Performance: Changed many reflection related methods with equivalent ones based on the strategy pattern (without the use of reflection)
  - Performance: Added ifDebugEnabled check before every logger.debug() call 
  - Performance: SQL statement are rendered to String only if needed instead of on a each call


-------------------
 - Release 4.5.0 -
-------------------
  - Moved to slf4j
  - Synchronized core methods of the JPOrm class 
  - It's now possible to build JPO with a JDK 7
  - Added new Validator interface and specific implementations for Oval and JSR303


-------------------
 - Release 4.4.1 -
-------------------
  - Fixed bug: an OrmException is no longer wrapped in a OrmSqlException when thrown by a ResultSetReader 
  - Fixed bug: it's not possible to join the same table


-------------------
 - Release 4.4.0 -
-------------------
  - Big step forward: InputStream and Reader are now correctly handled even with Oracle DB (JDBC 3.0 driver required)


-------------------
 - Release 4.3.0 -
-------------------
  - Added automatic db Dialect discovering


-------------------
 - Release 4.2.0 -
-------------------
  - Added OrmSqlException hierarchy to map specific SQLException states


-------------------
 - Release 4.1.0 -
-------------------
  - Added "order by ... nulls last" and "order by ... nulls first"
  - Fixed bug: Not possible to use "createOrUpdate" method on beans with @Version and without conditional key generators


-------------------
 - Release 4.0.2 -
-------------------
  - Fixed a possible NullPointerException in WrapperTypeArray class


-------------------
 - Release 4.0.0 -
-------------------
  - ORM default behavior changed on save (update): the properties of a saved (updated) bean 
    are no longer updated (id, generated fields, version) instead a new bean with all the fields
    updated is created and returned  


-------------------
 - Release 3.5.0 -
-------------------
  - Now is possible to define a hierarchy of Bean
  - Added saveOrUpdate (if the @Generator is used and the "ifValueIn" property is defined, this will be used to 
    determine whether save or update the bean, otherwise a double call to the db is performed)


-------------------
 - Release 3.4.0 -
-------------------
  - OrmQueryFormatException is thrown if an unknown field is used in a query
  - Is now possible to use objects of Classes with an associated WrapperType in every orm queries


-------------------
 - Release 3.3.0 -
-------------------
  - Added wrapper for joda-time DateMidnight to java.sql.Date
  - Added register(TypeWrapper) to JPO to register new wrappers or override existing ones
  - ResultSet fields manipulator helpers are no longer static


-------------------
 - Release 3.2.0 -
-------------------
  - Project renamed to JPO
  - Added online query writing: queries can be written in one single line thanks to the query() method in the clauses
  - Added property "ifValueIn" in the @Generated annotation. This permits to use the generator only if the key has a desired value to check the value
  - Removed "I" prefix from main interfaces
  - Dropped javassist code generator support
  - All Exceptions now inherit from OrmException
  - *Unique methods throws an OrmNotUniqueResultException if the returned rows number is not exactly one
  - findUnique method added to the Session
  - the first Class registered in a query can use property names in the where clause without the prefix 
  - Query IJoin Interface renamed to From
  - Query IExpression Interface renamed to Where
  - Added @Ignore annotation to ignore bean fields
  - Added wrappers for Byte, Double, Float, Integer, Long, Short to BigDecimal
  - Added wrapper for Character to String
  - Added wrapper for Boolean to BigDecimal (false if ZERO, otherwise true )
  - Version increase feature now works with every wrapped type if their related type on the ResultSet is a Date or a number
  - New fields access strategy: if getter and/or setter are present they will be used by the orm, otherwise the field is directly accessed even if private 
  - Only one generator per bean is accepted
  - Code cleaning and very heavy refactoring


-------------------
 - Release 3.1.2 -
-------------------
  - Is no longer mandatory to register beans at startup, they are now automatically registered on first use
  - Added wrapper for java.util.Date
  - Added wrapper for joda-time DateTime
  - Added wrapper for joda-time LocalDate
  - Added wrapper for joda-time LocalDateTime
  - DB scanning for primary key search has been removed. @Id annotation is now the only way to identify a primary key


-------------------
 - Release 3.0.0 -
-------------------
  - New ReflectionPersistorGenerator based on reflection mechanism
  - New JavassistPersistorGenerator to create bytecode at runtime
  - new @Version annotation
  - javassist is optional, if not found reflection is used
  - Cojen code generator removed from dependencies