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
package com.jporm.rm.query.find;

import com.jporm.sql.dsl.query.orderby.OrderByImpl;
import com.jporm.sql.dsl.query.select.Select;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CustomFindQueryOrderByImpl<BEAN> extends OrderByImpl<CustomFindQueryOrderBy<BEAN>> 
												implements CustomFindQueryOrderBy<BEAN>,
															CustomFindQueryAllProvidersDefault<BEAN> {

    private final CustomFindQueryImpl<BEAN> findQuery;

    public CustomFindQueryOrderByImpl(final CustomFindQueryImpl<BEAN> findQuery, final Select<?> select) {
        super(select);
        this.findQuery = findQuery;
    }

    @Override
    protected CustomFindQueryOrderBy<BEAN> getOrderBy() {
        return this;
    }

	@Override
	public CustomFindQueryImpl<BEAN> getFindQuery() {
		return findQuery;
	}


}