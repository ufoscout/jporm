/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.test.config;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.json.jackson2.Jackson2JsonService;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmBuilder;
import com.jporm.rm.quasar.JpoRmQuasarBuilder;
import com.jporm.rm.spring.JpoRmJdbcTemplateBuilder;
import com.jporm.sql.dialect.DBType;

public class DBData {

	private PlatformTransactionManager springTransactionmanager;
	private DBType dbType;
	private boolean dbAvailable;
	private boolean multipleSchemaSupport;
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public DBType getDBType() {
		return dbType;
	}

	public JpoRm getJpoDataSource() {
		return JpoRmBuilder.get().setJsonService(new Jackson2JsonService()).build(getDataSource());
	}

	public JpoRm getJpoJdbcTemplate() {
		return JpoRmJdbcTemplateBuilder.get().setJsonService(new Jackson2JsonService()).build(new JdbcTemplate(getDataSource()), getSpringTransactionmanager());
	}

	public JpoRm getJpoQuasr() {
		return JpoRmQuasarBuilder.get().setJsonService(new Jackson2JsonService()).build(getDataSource());
	}

	/**
	 * @return the springTransactionmanager
	 */
	public PlatformTransactionManager getSpringTransactionmanager() {
		return springTransactionmanager;
	}

	public boolean isDbAvailable() {
		return dbAvailable;
	}

	public boolean isMultipleSchemaSupport() {
		return multipleSchemaSupport;
	}

	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDbAvailable(final boolean dbAvailable) {
		this.dbAvailable = dbAvailable;
	}

	public void setDBType(final DBType dbType) {
		this.dbType = dbType;
	}

	public void setMultipleSchemaSupport(final boolean multipleSchemaSupport) {
		this.multipleSchemaSupport = multipleSchemaSupport;
	}

	/**
	 * @param springTransactionmanager
	 *            the springTransactionmanager to set
	 */
	public void setSpringTransactionmanager(final PlatformTransactionManager springTransactionmanager) {
		this.springTransactionmanager = springTransactionmanager;
	}

}
