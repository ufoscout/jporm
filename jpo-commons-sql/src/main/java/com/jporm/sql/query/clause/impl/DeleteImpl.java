/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.sql.query.clause.impl;

import java.util.List;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlRoot;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.Where;
import com.jporm.sql.query.namesolver.NameSolver;
import com.jporm.sql.query.namesolver.impl.NameSolverImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.sql.query.tool.DescriptorToolMap;

public class DeleteImpl<BEAN> extends ASqlRoot implements Delete {

	private final WhereImpl where = new WhereImpl();
	private final NameSolver nameSolver;
	private final ClassDescriptor<BEAN> classDescriptor;

	public DeleteImpl(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, Class<BEAN> clazz) {
		super(classDescriptorMap);
		this.classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
		nameSolver = new NameSolverImpl(propertiesFactory, true);
		nameSolver.register(clazz, clazz.getSimpleName(), classDescriptor);
	}

	@Override
	public Where where() {
		return where;
	}


	@Override
	public final void appendValues(final List<Object> values) {
		where.appendElementValues(values);
	}

	@Override
	public final void renderSql(DBProfile dbProfile, final StringBuilder queryBuilder) {
		queryBuilder.append("DELETE FROM ");
		queryBuilder.append(classDescriptor.getTableInfo().getTableNameWithSchema());
		queryBuilder.append(" "); //$NON-NLS-1$
		where.renderSqlElement(dbProfile, queryBuilder, nameSolver);
	}

}
