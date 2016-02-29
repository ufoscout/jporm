/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.commons.core.query.find;

import java.util.Collections;
import java.util.List;

import com.jporm.commons.core.query.SqlFactory;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.select.SelectCommon;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomResultFindQueryBase<BEAN> implements SelectCommon
{
	private final Select<Class<?>> select;

    public CustomResultFindQueryBase(final String[] selectFields, final Class<BEAN> clazz, final String alias, final SqlFactory sqlFactory) {
        select = sqlFactory.select(selectFields).from(clazz, alias);
    }

	@Override
	public final void sqlValues(List<Object> values) {
		getSelect().sqlValues(values);
	}

	@Override
	public final void sqlQuery(StringBuilder queryBuilder) {
		getSelect().sqlQuery(queryBuilder);
	}

	@Override
	public final String sqlRowCountQuery() {
		return getSelect().sqlRowCountQuery();
	}

    public final List<String> getIgnoredFields() {
        return Collections.EMPTY_LIST;
    }

    /**
     * @return the select
     */
    protected final Select<Class<?>> getSelect() {
        return select;
    }

}
