/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jporm.test.crud.AutoIdTest;
import com.jporm.test.crud.EmployeeTest;
import com.jporm.test.crud.EmployeeWithEnumTest;
import com.jporm.test.crud.PeopleMultipleTest;
import com.jporm.test.crud.PeopleTest;
import com.jporm.test.crud.WrapperTypeTableTest;
import com.jporm.test.dialect.DetermineDBTypeTest;
import com.jporm.test.exception.ConstraintViolationExceptionTest;
import com.jporm.test.lob.BlobClob_InputStream_Reader_Test;
import com.jporm.test.lob.BlobClob_String_Test;
import com.jporm.test.query.CustomQueryExecutionTest;
import com.jporm.test.query.QueryExecutionMultipleSchemaTest;
import com.jporm.test.query.QueryExecutionTest;
import com.jporm.test.query.forupdate.QuerySelectForUpdateExecutionTest;
import com.jporm.test.session.BeanOneToManyRelationTest;
import com.jporm.test.session.BeanOneToOneRelationTest;
import com.jporm.test.session.BeanRelationsCRUDTest;
import com.jporm.test.session.BeanRelationsCascadeTypeTest;
import com.jporm.test.session.CustomQueryPaginationTest;
import com.jporm.test.session.CustomQueryResultSetReaderTest;
import com.jporm.test.session.DataSourceConnectionTest;
import com.jporm.test.session.MaxRowsSideEffectTest;
import com.jporm.test.session.QueryExcludeFieldTest;
import com.jporm.test.session.QueryGroupByHavingTest;
import com.jporm.test.session.QueryPaginationTest;
import com.jporm.test.session.QueryWithCustomExpressionTest;
import com.jporm.test.session.SessionConditionalGeneratorTest;
import com.jporm.test.session.SessionSaveOrUpdateTest;
import com.jporm.test.session.find.cache.BeanCacheAnnotationTest;
import com.jporm.test.transaction.EmployeeTransactionTest;
import com.jporm.test.transaction.JdbcTemplatePeopleTest;
import com.jporm.test.transaction.TransactionCallbackTest;
import com.jporm.test.version.VersionTest;

/**
 * 
 * @author Francesco Cina
 * 
 *         20/mag/2011
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

	AutoIdTest.class,
	BeanCacheAnnotationTest.class,
	BeanRelationsCascadeTypeTest.class,
	BeanRelationsCRUDTest.class,
	BeanOneToOneRelationTest.class,
	BeanOneToManyRelationTest.class,
	BlobClob_InputStream_Reader_Test.class,
	BlobClob_String_Test.class,
	ConstraintViolationExceptionTest.class,
	CustomQueryExecutionTest.class,
	CustomQueryPaginationTest.class,
	CustomQueryResultSetReaderTest.class,
	DataSourceConnectionTest.class,
	DetermineDBTypeTest.class,
	EmployeeTest.class,
	EmployeeWithEnumTest.class,
	EmployeeTransactionTest.class,
	JdbcTemplatePeopleTest.class,
	MaxRowsSideEffectTest.class,
	PeopleMultipleTest.class,
	PeopleTest.class,
	QueryExcludeFieldTest.class,
	QueryExecutionMultipleSchemaTest.class,
	QueryExecutionTest.class,
	QueryGroupByHavingTest.class,
	QueryPaginationTest.class,
	QuerySelectForUpdateExecutionTest.class,
	QueryWithCustomExpressionTest.class,
	SessionConditionalGeneratorTest.class,
	SessionSaveOrUpdateTest.class,
	TransactionCallbackTest.class,
	VersionTest.class,
	WrapperTypeTableTest.class

})
public class AllTests {
	//Nothing to do
}
