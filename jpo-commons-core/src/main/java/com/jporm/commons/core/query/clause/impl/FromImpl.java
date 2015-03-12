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
package com.jporm.commons.core.query.clause.impl;

import com.jporm.commons.core.query.AQuerySubElement;
import com.jporm.commons.core.query.clause.From;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public abstract class FromImpl<T extends From<?>> extends AQuerySubElement implements From<T> {

	private final com.jporm.sql.query.clause.From sqlFrom;

	public FromImpl (final com.jporm.sql.query.clause.From sqlFrom) {
		this.sqlFrom = sqlFrom;
	}

	protected abstract T from();

	@Override
	public T join(final Class<?> joinClass) {
		sqlFrom.join(joinClass);
		return from();
	}

	@Override
	public T join(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.join(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T naturalJoin(final Class<?> joinClass) {
		sqlFrom.naturalJoin(joinClass);
		return from();
	}

	@Override
	public T naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.naturalJoin(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T innerJoin(final Class<?> joinClass) {
		sqlFrom.innerJoin(joinClass);
		return from();
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.innerJoin(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.innerJoin(joinClass, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T innerJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.innerJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass) {
		sqlFrom.leftOuterJoin(joinClass);
		return from();
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.leftOuterJoin(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T leftOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass) {
		sqlFrom.rightOuterJoin(joinClass);
		return from();
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.rightOuterJoin(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T rightOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass) {
		sqlFrom.fullOuterJoin(joinClass);
		return from();
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
		sqlFrom.fullOuterJoin(joinClass, joinClassAlias);
		return from();
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return from();
	}

	@Override
	public T fullOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		sqlFrom.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return from();
	}
	
}
