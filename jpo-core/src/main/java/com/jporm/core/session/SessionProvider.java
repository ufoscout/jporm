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
package com.jporm.core.session;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.core.transaction.TransactionCallback;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public abstract class SessionProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private DBType dbType;

	public abstract DataSource getDataSource();

	public abstract <T> T doInTransaction(Session session, TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	public abstract SqlPerformerStrategy sqlPerformerStrategy() throws JpoException;

	public final DBType getDBType() {
		if (dbType==null) {
			dbType = DBType.UNKNOWN;
			try
			{
				DBTypeDescription dbTypeDescription = DBTypeDescription.build(getDataSource());
				dbType = dbTypeDescription.getDBType();
				getLogger().info("DB username: {}", dbTypeDescription.getUsername()); //$NON-NLS-1$
				getLogger().info("DB driver name: {}", dbTypeDescription.getDriverName()); //$NON-NLS-1$
				getLogger().info("DB driver version: {}", dbTypeDescription.getDriverVersion()); //$NON-NLS-1$
				getLogger().info("DB url: {}", dbTypeDescription.getUrl()); //$NON-NLS-1$
				getLogger().info("DB product name: {}", dbTypeDescription.getDatabaseProductName()); //$NON-NLS-1$
				getLogger().info("DB product version: {}", dbTypeDescription.getDatabaseProductVersion()); //$NON-NLS-1$
			}
			catch (RuntimeException ex)	{
				getLogger().warn("Error while determining the database type", ex); //$NON-NLS-1$
			}
			getLogger().info("DB type is {}", dbType); //$NON-NLS-1$
		}
		return dbType;
	}

	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDBType(DBType dbType) {
		this.dbType = dbType;
	}
}
