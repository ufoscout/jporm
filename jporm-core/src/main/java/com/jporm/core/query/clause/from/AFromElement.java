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
package com.jporm.core.query.clause.from;

import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQuerySubElement;
import com.jporm.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public abstract class AFromElement extends AQuerySubElement implements FromElement {

	protected final Class<?> joinClass;
	protected final ServiceCatalog serviceCatalog;
	private final Integer nameSolverClassId;

	public AFromElement(final ServiceCatalog serviceCatalog, final Class<?> joinClass, final Integer nameSolverClassId) {
		this.serviceCatalog = serviceCatalog;
		this.joinClass = joinClass;
		this.nameSolverClassId = nameSolverClassId;
	}

	@Override
	public final int getElementStatusVersion() {
		return 0;
	}

	@Override
	public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
		String alias = nameSolver.normalizedAlias(getNameSolverClassId());
		queryBuilder.append( getJoinName() );
		queryBuilder.append(serviceCatalog.getClassToolMap().get(joinClass).getDescriptor().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append( " " ); //$NON-NLS-1$
		queryBuilder.append(alias);

		if (hasOnClause()) {
			queryBuilder.append( " ON " ); //$NON-NLS-1$
			queryBuilder.append( nameSolver.solvePropertyName(onLeftProperty()) );
			queryBuilder.append( " = " ); //$NON-NLS-1$
			queryBuilder.append( nameSolver.solvePropertyName(onRightProperty()) );
		}

		queryBuilder.append( " " ); //$NON-NLS-1$
	}

	protected abstract String getJoinName();

	protected abstract boolean hasOnClause();

	protected abstract String onLeftProperty();

	protected abstract String onRightProperty();

	public Integer getNameSolverClassId() {
		return nameSolverClassId;
	}

	@Override
	public final void appendElementValues(final List<Object> values) {
		// do nothing
	}

}
