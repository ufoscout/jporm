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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.dialect.DBType;
import com.jporm.core.dialect.DetermineDBType;
import com.jporm.exception.OrmException;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;

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

	public abstract SqlPerformerStrategy sqlPerformerStrategy() throws OrmException;

	public final DBType getDBType() {
		if (dbType==null) {
			Connection connection = null;
			dbType = DBType.UNKNOWN;
			try
			{
				DataSource dataSource = getDataSource();
				if (dataSource!=null) {
					connection = dataSource.getConnection();
					DatabaseMetaData metaData = connection.getMetaData();

					String driverName = metaData.getDriverName();
					String driverVersion = metaData.getDriverVersion();
					String url = metaData.getURL();
					String databaseProductName = metaData.getDatabaseProductName();

					getLogger().info("DB username: " + metaData.getUserName()); //$NON-NLS-1$
					getLogger().info("DB driver name: " + driverName); //$NON-NLS-1$
					getLogger().info("DB driver version: " + driverVersion); //$NON-NLS-1$
					getLogger().info("DB url: " + url); //$NON-NLS-1$
					getLogger().info("DB product name: " + databaseProductName); //$NON-NLS-1$
					getLogger().info("DB product version: " + metaData.getDatabaseProductVersion()); //$NON-NLS-1$

					dbType = new DetermineDBType().determineDBType(driverName, url, databaseProductName);
				}
			}
			catch (SQLException ex)
			{
				getLogger().warn("Error while determining the database type", ex); //$NON-NLS-1$
			}
			finally
			{
				if (connection != null)
				{
					try
					{
						connection.close();
					}
					catch (SQLException ex)
					{
						// we ignore this one
					}
				}
			}
			getLogger().info("DB type is " + dbType); //$NON-NLS-1$
		}
		return dbType;
	}

	public Logger getLogger() {
		return logger;
	}
}
