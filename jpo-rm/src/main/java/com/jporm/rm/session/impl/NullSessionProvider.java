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
package com.jporm.rm.session.impl;

import javax.sql.DataSource;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rm.session.SessionProvider;
import com.jporm.rm.session.SqlPerformerStrategy;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public class NullSessionProvider extends SessionProvider {

	public NullSessionProvider() {
	}

	public NullSessionProvider(DBType dbType) {
		setDBType(dbType);
	}

	@Override
	public DataSource getDataSource() {
		return null;
	}

	@Override
	public SqlPerformerStrategy sqlPerformerStrategy() throws JpoException {
		return new NullSqlPerformerStrategy();
	}

}
