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
package com.jporm.sql.query.clause.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.core.domain.Employee;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.H2DBProfile;
import com.jporm.sql.query.clause.impl.SelectImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;


/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public class SelectTest extends BaseSqlTestApi {

	@Test
	public void testSelectRender1() {
//		SelectImpl<Employee> select = new SelectImpl<Employee>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
//
//		final String[] selectClause = {"Employee.id as hello, sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum, Beppe.Signori.Goal"};
//		//        final String[] selectClause = {"Employee.id as hello", "  sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum", "  Beppe.Signori.Goal "}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		select.selectFields(selectClause);
//		String expected = "SELECT Employee.id as hello, sum(Employee.id,Blobclob_ByteArray.index,nada.nada) as sum, Beppe.Signori.Goal AS \"Beppe.Signori.Goal\" "; //$NON-NLS-1$
//		System.out.println("select.render(): " + select.renderSql()); //$NON-NLS-1$
//		System.out.println("expected       : " + expected); //$NON-NLS-1$
//
//		assertEquals(expected, select.renderSql());
	}

	@Test
	public void testSelectRenderWrongPrefix() {
		SelectImpl<Employee> select = new SelectImpl<Employee>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "EmployeeAlias");
		final String[] selectClause = {"EmployeeAlias.id as hello", "sum(EmployeeAlias.age, nada1.nada2) as sum"}; //$NON-NLS-1$ //$NON-NLS-2$
		select.selectFields(selectClause);
		try {
			select.renderSql();
			fail("The operation should thrown an Exception due to the fact that the prefix 'nada1' cannot be solved"); //$NON-NLS-1$
		} catch (JpoWrongPropertyNameException e) {
			assertTrue(e.getMessage().contains("nada1")); //$NON-NLS-1$
		}
	}

	@Test
	public void testSelectRenderWrongFieldName() {
		SelectImpl<Employee> select = new SelectImpl<Employee>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "Beppe.Signori");
		final String[] selectClause = {"Beppe.Signori.goal"};
		select.selectFields(selectClause);
		try {
			select.renderSql();
			fail("The operation should thrown an Exception due to the fact that the field 'goal' cannot be solved"); //$NON-NLS-1$
		} catch (JpoWrongPropertyNameException e) {
			assertTrue(e.getMessage().contains("goal")); //$NON-NLS-1$
		}
	}
}
