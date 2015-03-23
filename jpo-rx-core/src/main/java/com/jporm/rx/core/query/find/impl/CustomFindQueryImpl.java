/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.core.query.find.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.rx.core.query.find.CustomFindQuery;
import com.jporm.rx.core.query.find.CustomFindQueryGroupBy;
import com.jporm.rx.core.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.core.query.find.CustomFindQueryWhere;
import com.jporm.rx.core.session.SessionProvider;
import com.jporm.sql.query.clause.Select;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomFindQueryImpl extends CommonFindQueryImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQuery {

	private final CustomFindQueryGroupByImpl groupBy;
	private SessionProvider sessionProvider;

	public CustomFindQueryImpl(final String[] selectFields, final ServiceCatalog<?> serviceCatalog, final Class<?> clazz,
			final String alias, SessionProvider sessionProvider) {
		super(serviceCatalog, clazz, alias);
		this.sessionProvider = sessionProvider;
		Select select = getSelect();
		select.selectFields(selectFields);
		groupBy = new CustomFindQueryGroupByImpl(select.groupBy(), this);
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new CustomFindQueryWhereImpl(select.where(), this));
		setOrderBy(new CustomFindQueryOrderByImpl(select.orderBy(), this));
	}

	private List<Object> getValues() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		return values;
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		groupBy.fields(fields);
		return groupBy;
	}

//	@Override
//	public void getList(Handler<AsyncResult<List<JsonObject>>> result) {
//		sessionProvider.getConnection(connectionHandler -> {
//			int deleteMe;
//			System.out.println("QUERY IS:");
//			System.out.println(renderSql());
//			connectionHandler.result().queryWithParams(renderSql(), new JsonArray(getValues()), queryHandler -> {
//				System.out.println("RESULT IS:");
//				System.out.println(renderSql());
//
//				ResultSet queryResult = queryHandler.result();
//				System.out.println(queryResult.getColumnNames());
//				System.out.println(queryResult.getResults());
//				System.out.println(queryResult.getRows());
//
//				result.handle(Future.succeededFuture(queryHandler.result().getRows()));
//			});
//		});
//	}

}
