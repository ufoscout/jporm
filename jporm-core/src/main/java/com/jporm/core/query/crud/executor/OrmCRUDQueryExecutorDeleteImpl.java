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
package com.jporm.core.query.crud.executor;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.delete.DeleteQueryOrm;
import com.jporm.session.SqlExecutor;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorDeleteImpl implements OrmCRUDQueryExecutorDelete {

	private final ServiceCatalog serviceCatalog;

	public OrmCRUDQueryExecutorDeleteImpl(final ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	@Override
	public <BEAN> int delete(final DeleteQueryOrm<BEAN> deleteQuery, final Class<BEAN> clazz) {
		final List<Object> values = new ArrayList<Object>();
		deleteQuery.appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		sqlExec.setQueryTimeout(deleteQuery.getTimeout());
		return sqlExec.update(deleteQuery.renderSql(), values);
	}

}
