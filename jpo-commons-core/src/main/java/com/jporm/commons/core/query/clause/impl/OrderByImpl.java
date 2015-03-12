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
import com.jporm.commons.core.query.clause.OrderBy;

/**
 *
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public abstract class OrderByImpl<T extends OrderBy<?>> extends AQuerySubElement implements OrderBy<T> {

	private final com.jporm.sql.query.clause.OrderBy sqlOrderBy;

	public OrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy) {
		this.sqlOrderBy = sqlOrderBy;
	}

    protected abstract T orderBy();


    @Override
    public T asc(final String property) {
    	sqlOrderBy.asc(property);
    	return orderBy();
    }

    @Override
    public T desc(final String property) {
    	sqlOrderBy.desc(property);
    	return orderBy();
    }

    @Override
    public T ascNullsFirst(final String property) {
    	sqlOrderBy.ascNullsFirst(property);
    	return orderBy();
    }

    @Override
    public T ascNullsLast(final String property) {
    	sqlOrderBy.ascNullsLast(property);
    	return orderBy();
    }

    @Override
    public T descNullsFirst(final String property) {
    	sqlOrderBy.descNullsFirst(property);
    	return orderBy();
    }

    @Override
    public T descNullsLast(final String property) {
    	sqlOrderBy.descNullsLast(property);
    	return orderBy();
    }

}
