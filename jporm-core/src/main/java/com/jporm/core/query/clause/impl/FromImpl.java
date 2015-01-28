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
package com.jporm.core.query.clause.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.clause.From;
import com.jporm.core.query.clause.impl.from.FromElement;
import com.jporm.core.query.clause.impl.from.FullOuterJoinElement;
import com.jporm.core.query.clause.impl.from.InnerJoinElement;
import com.jporm.core.query.clause.impl.from.JoinElement;
import com.jporm.core.query.clause.impl.from.LeftOuterJoinElement;
import com.jporm.core.query.clause.impl.from.NaturalJoinElement;
import com.jporm.core.query.clause.impl.from.RightOuterJoinElement;
import com.jporm.core.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public abstract class FromImpl<T extends From<?>> extends AQuerySubElement implements From<T> {

	private final List<FromElement> joinElements = new ArrayList<FromElement>();
	private final ServiceCatalog serviceCatalog;
	private final Class<?> clazz;
	private final Integer mainNameSolverClassId;
	private final NameSolver nameSolver;

	public FromImpl (final ServiceCatalog serviceCatalog, final Class<?> clazz, final Integer nameSolverClassId, final NameSolver nameSolver) {
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		this.mainNameSolverClassId = nameSolverClassId;
		this.nameSolver = nameSolver;
	}

	protected abstract T from();

	@Override
	public T join(final Class<?> joinClass) {
		return join(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T join(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new JoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T naturalJoin(final Class<?> joinClass) {
		return naturalJoin(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new NaturalJoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T innerJoin(final Class<?> joinClass) {
		return innerJoin(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new InnerJoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		return innerJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new InnerJoinElement(this.serviceCatalog, joinClass, nameSolverClassId, onLeftProperty, onRigthProperty) );
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass) {
		return leftOuterJoin(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new LeftOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		return leftOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new LeftOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId, onLeftProperty, onRigthProperty) );
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass) {
		return rightOuterJoin(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new RightOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		return rightOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new RightOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId, onLeftProperty, onRigthProperty) );
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass) {
		return fullOuterJoin(joinClass, joinClass.getSimpleName());
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new FullOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId) );
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		return fullOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		Integer nameSolverClassId = nameSolver.register(joinClass, joinClassAlias);
		return addJoinElement( new FullOuterJoinElement(this.serviceCatalog, joinClass, nameSolverClassId, onLeftProperty, onRigthProperty) );
	}

	@Override
	public final int getElementStatusVersion() {
		return this.joinElements.size();
	}

	@Override
	public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver localNameSolver) {
		final String alias = localNameSolver.normalizedAlias(this.mainNameSolverClassId);
		queryBuilder.append("FROM "); //$NON-NLS-1$
		queryBuilder.append(this.serviceCatalog.getClassToolMap().get(this.clazz).getDescriptor().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append( " " ); //$NON-NLS-1$
		queryBuilder.append(alias);
		queryBuilder.append(" "); //$NON-NLS-1$
		for (final FromElement joinElement : this.joinElements) {
			joinElement.renderSqlElement(queryBuilder, localNameSolver);
		}
	}

	@Override
	public final void appendElementValues(final List<Object> values) {
		// do nothing
	}

	private T addJoinElement(final FromElement joinElement) {
		this.joinElements.add(joinElement);
		return from();
	}

}
