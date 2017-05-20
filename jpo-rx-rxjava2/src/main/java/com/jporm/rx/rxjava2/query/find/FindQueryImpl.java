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
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.rx.rxjava2.query.find;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.find.FindQueryBase;
import com.jporm.rx.rxjava2.query.find.ExecutionEnvProvider;
import com.jporm.rx.rxjava2.query.find.FindQuery;
import com.jporm.rx.rxjava2.session.SqlExecutor;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class FindQueryImpl<BEAN> extends FindQueryBase<BEAN> implements FindQuery<BEAN>, ExecutionEnvProvider<BEAN> {

    private final ClassTool<BEAN> ormClassTool;
	private final SqlExecutor sqlExecutor;

    public FindQueryImpl(Class<BEAN> clazz, Object[] pkFieldValues, final ClassTool<BEAN> ormClassTool, SqlExecutor sqlExecutor, SqlFactory sqlFactory,
            SqlCache sqlCache) {
        super(clazz, pkFieldValues, sqlCache);
        this.ormClassTool = ormClassTool;
		this.sqlExecutor = sqlExecutor;
    }

	@Override
	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	@Override
	public ClassTool<BEAN> getOrmClassTool() {
		return ormClassTool;
	}

	@Override
	public ExecutionEnvProvider<BEAN> getExecutionEnvProvider() {
		return this;
	}

}
