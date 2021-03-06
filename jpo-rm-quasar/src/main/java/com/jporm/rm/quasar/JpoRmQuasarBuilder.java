/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rm.quasar;

import javax.sql.DataSource;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmImpl;
import com.jporm.rm.quasar.connection.datasource.QuasarDataSourceTransactionProvider;
import com.jporm.sql.dialect.DBProfile;

public class JpoRmQuasarBuilder extends AbstractJpoBuilder<JpoRmQuasarBuilder> {

	public static JpoRmQuasarBuilder get() {
		return new JpoRmQuasarBuilder();
	}

	private JpoRmQuasarBuilder() {
	}

	/**
	 * Create a {@link JPO} instance
	 *
	 * @param dataSource
	 * @return
	 */
	public JpoRm build(final DataSource dataSource) {
		return new JpoRmImpl(new QuasarDataSourceTransactionProvider(dataSource, getServiceCatalog().getAsyncTaskExecutor()), getServiceCatalog());
	}

	/**
	 * Create a {@link JPO} instance
	 *
	 * @param dataSource
	 * @param dbType
	 * @return
	 */
	public JpoRm build(final DataSource dataSource, final DBProfile dbType) {
		return new JpoRmImpl(new QuasarDataSourceTransactionProvider(dataSource, getServiceCatalog().getAsyncTaskExecutor(), dbType), getServiceCatalog());
	}

}
