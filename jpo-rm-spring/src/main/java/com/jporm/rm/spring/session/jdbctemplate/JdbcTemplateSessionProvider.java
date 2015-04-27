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

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rm.session.SessionProvider;
import com.jporm.rm.session.SqlPerformerStrategy;

/**
 *
 * @author Francesco Cina
 *
 * 15/giu/2011
 */
public class JdbcTemplateSessionProvider extends SessionProvider {

	private final DataSource dataSource;
	private final SqlPerformerStrategy performerStrategy;

	public JdbcTemplateSessionProvider(final DataSource dataSource, final PlatformTransactionManager platformTransactionManager) {
		this.dataSource = dataSource;
		performerStrategy = new JdbcTemplateSqlPerformerStrategy( new JdbcTemplate(dataSource), platformTransactionManager, getDBType().getDBProfile().getStatementStrategy() );
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public SqlPerformerStrategy sqlPerformerStrategy() throws JpoException {
		return performerStrategy;
	}

}
