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
package com.jporm.session.jdbctemplate;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;

import com.jporm.exception.OrmException;
import com.jporm.exception.sql.OrmSqlBadGrammarException;
import com.jporm.exception.sql.OrmSqlConcurrencyFailureException;
import com.jporm.exception.sql.OrmSqlDataAccessResourceFailureException;
import com.jporm.exception.sql.OrmSqlDataIntegrityViolationException;
import com.jporm.exception.sql.OrmSqlException;
import com.jporm.exception.sql.OrmSqlTransientDataAccessResourceException;

/**
 * 
 * @author cinafr
 *
 */
public class JdbcTemplateExceptionTranslator {

	private JdbcTemplateExceptionTranslator() {
	}

	public static OrmSqlException doTranslate(final Exception ex) {
		if (ex instanceof OrmException) {
			throw (OrmException) ex;
		}
		if (ex instanceof BadSqlGrammarException) {
			return new OrmSqlBadGrammarException(ex);
		}
		else if (ex instanceof DataIntegrityViolationException) {
			return new OrmSqlDataIntegrityViolationException(ex);
		}
		else if (ex instanceof DataAccessResourceFailureException) {
			return new OrmSqlDataAccessResourceFailureException(ex);
		}
		else if (ex instanceof TransientDataAccessResourceException) {
			return new OrmSqlTransientDataAccessResourceException(ex);
		}
		else if (ex instanceof ConcurrencyFailureException) {
			return new OrmSqlConcurrencyFailureException(ex);
		}
		return new OrmSqlException(ex);
	}
}
