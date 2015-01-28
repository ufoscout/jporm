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
package com.jporm.core.query.clause.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.core.BaseTestApi;
import com.jporm.core.JPOrm;
import com.jporm.core.domain.Blobclob_ByteArray;
import com.jporm.core.domain.Employee;
import com.jporm.core.exception.JpoException;
import com.jporm.core.query.clause.impl.OrmCustomSelect;
import com.jporm.core.query.find.CustomFindSelect;
import com.jporm.core.query.find.impl.CustomFindSelectImpl;
import com.jporm.core.query.namesolver.NameSolver;
import com.jporm.core.query.namesolver.impl.NameSolverImpl;
import com.jporm.core.query.namesolver.impl.NullNameSolver;
import com.jporm.core.session.impl.NullSessionProvider;

/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public class SelectTest extends BaseTestApi {

	@Test
	public void testSelectRender1() {
		final String[] selectClause = {"Employee.id as hello, sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum, Beppe.Signori.Goal"};
		//        final String[] selectClause = {"Employee.id as hello", "  sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum", "  Beppe.Signori.Goal "}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final OrmCustomSelect<CustomFindSelect> select = new CustomFindSelectImpl(selectClause);

		String expected = "SELECT Employee.id as hello, sum(Employee.id,Blobclob_ByteArray.index,nada.nada) as sum, Beppe.Signori.Goal AS \"Beppe.Signori.Goal\" "; //$NON-NLS-1$
		System.out.println("select.render(): " + select.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
		System.out.println("expected       : " + expected); //$NON-NLS-1$

		assertEquals(expected, select.renderSqlElement(new NullNameSolver()));
	}

	@Test
	public void testSelectRenderWrongPrefix() {
		final JPOrm jpOrm = new JPOrm(new NullSessionProvider());
		jpOrm.register(Employee.class );
		jpOrm.register(Blobclob_ByteArray.class);

		final NameSolver nameSolver = new NameSolverImpl( jpOrm.getServiceCatalog(), false );
		nameSolver.register(Employee.class, "EmployeeAlias"); //$NON-NLS-1$
		nameSolver.register(Employee.class, "Beppe.Signori"); //$NON-NLS-1$
		nameSolver.register(Blobclob_ByteArray.class, "BCAlias"); //$NON-NLS-1$

		final String[] selectClause = {"EmployeeAlias.id as hello", "sum(EmployeeAlias.age, BCAlias.index, nada.nada) as sum", "Beppe.Signori.id"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final OrmCustomSelect<CustomFindSelect> select = new CustomFindSelectImpl(selectClause);

		try {
			select.renderSqlElement(nameSolver);
			fail("The operation should thrown an Exception due to the fact that the prefix 'nada' cannot be solved"); //$NON-NLS-1$
		} catch (JpoException e) {
			assertTrue(e.getMessage().contains("nada")); //$NON-NLS-1$
		}
	}

	@Test
	public void testSelectRenderWrongFieldName() {
		final JPOrm jpOrm = new JPOrm(new NullSessionProvider());
		jpOrm.register(Employee.class );
		jpOrm.register(Blobclob_ByteArray.class);

		final NameSolver nameSolver = new NameSolverImpl( jpOrm.getServiceCatalog(), false );
		nameSolver.register(Employee.class, "EmployeeAlias"); //$NON-NLS-1$
		nameSolver.register(Employee.class, "Beppe.Signori"); //$NON-NLS-1$
		nameSolver.register(Blobclob_ByteArray.class, "BCAlias"); //$NON-NLS-1$

		final String[] selectClause = {"EmployeeAlias.id as hello", "sum(EmployeeAlias.age, BCAlias.index) as sum", "Beppe.Signori.goal"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final OrmCustomSelect<CustomFindSelect> select = new CustomFindSelectImpl(selectClause);

		try {
			select.renderSqlElement(nameSolver);
			fail("The operation should thrown an Exception due to the fact that the field 'goal' cannot be solved"); //$NON-NLS-1$
		} catch (JpoWrongPropertyNameException e) {
			assertTrue(e.getMessage().contains("goal")); //$NON-NLS-1$
		}
	}
}
