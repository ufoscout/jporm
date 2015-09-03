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
package com.jporm.rm.spring.session.jdbctemplate;

import java.util.function.BiFunction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rm.session.Connection;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.transaction.Transaction;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 * 15/giu/2011
 */
public class JdbcTemplateConnectionProvider implements ConnectionProvider {

	private final BiFunction<ConnectionProvider, ServiceCatalog, Transaction> transactionFactory;
	private DBType dbType;
	private final JdbcTemplate jdbcTemplate;

	public JdbcTemplateConnectionProvider(final JdbcTemplate jdbcTemplate, final PlatformTransactionManager platformTransactionManager) {
		this.jdbcTemplate = jdbcTemplate;
		transactionFactory =
				(connectionProvider, _serviceCatalog) -> {
					return new JdbcTemplateTransaction(connectionProvider, _serviceCatalog, platformTransactionManager);
				};
	}

	@Override
	public final DBType getDBType() {
		if (dbType==null) {
				dbType = DBTypeDescription.build(jdbcTemplate.getDataSource()).getDBType();
		}
		return dbType;
	}

	@Override
	public Connection getConnection(boolean autoCommit) throws JpoException {
		return new JdbcTemplateConnection( jdbcTemplate, getDBType().getDBProfile().getStatementStrategy() );
	}

	@Override
	public BiFunction<ConnectionProvider, ServiceCatalog, Transaction> getTransactionFactory() {
		return transactionFactory;
	}

}
