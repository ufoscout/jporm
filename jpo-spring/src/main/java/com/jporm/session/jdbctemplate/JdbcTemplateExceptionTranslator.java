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
import org.springframework.transaction.TransactionTimedOutException;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.exception.sql.JpoSqlBadGrammarException;
import com.jporm.commons.core.exception.sql.JpoSqlConcurrencyFailureException;
import com.jporm.commons.core.exception.sql.JpoSqlDataAccessResourceFailureException;
import com.jporm.commons.core.exception.sql.JpoSqlDataIntegrityViolationException;
import com.jporm.commons.core.exception.sql.JpoSqlException;
import com.jporm.commons.core.exception.sql.JpoSqlTransientDataAccessResourceException;

/**
 *
 * @author cinafr
 *
 */
public class JdbcTemplateExceptionTranslator {

	private JdbcTemplateExceptionTranslator() {
	}

	public static RuntimeException doTranslate(final Exception ex) {
		if (ex instanceof JpoException) {
			throw (JpoException) ex;
		}
		if (ex instanceof BadSqlGrammarException) {
			return new JpoSqlBadGrammarException(ex);
		}
		else if (ex instanceof DataIntegrityViolationException) {
			return new JpoSqlDataIntegrityViolationException(ex);
		}
		else if (ex instanceof DataAccessResourceFailureException) {
			return new JpoSqlDataAccessResourceFailureException(ex);
		}
		else if (ex instanceof TransientDataAccessResourceException) {
			return new JpoSqlTransientDataAccessResourceException(ex);
		}
		else if (ex instanceof ConcurrencyFailureException) {
			return new JpoSqlConcurrencyFailureException(ex);
		}
		else if (ex instanceof TransactionTimedOutException) {
			return new JpoTransactionTimedOutException(ex);
		}
		return new JpoSqlException(ex);
	}
}
