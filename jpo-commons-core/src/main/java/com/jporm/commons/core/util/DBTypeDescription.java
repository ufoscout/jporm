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
package com.jporm.commons.core.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.sql.dialect.DBType;

/**
 * Return the type of underlying DB
 *
 * @author ufo
 *
 */
public class DBTypeDescription {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBTypeDescription.class);
    private final static Map<String, DBType> dbProductNameMap = new ConcurrentHashMap<String, DBType>();
    private final static Map<String, DBType> dbURLMap = new ConcurrentHashMap<String, DBType>();

    static {
        dbProductNameMap.put("Derby", DBType.DERBY); //$NON-NLS-1$
        dbProductNameMap.put("H2", DBType.H2); //$NON-NLS-1$
        dbProductNameMap.put("HSQL", DBType.HSQLDB); //$NON-NLS-1$
        dbProductNameMap.put("Mysql", DBType.MYSQL); //$NON-NLS-1$
        dbProductNameMap.put("Oracle", DBType.ORACLE); //$NON-NLS-1$
        dbProductNameMap.put("Postgresql", DBType.POSTGRESQL); //$NON-NLS-1$

        dbURLMap.put("sqlserver", DBType.SQLSERVER12); //$NON-NLS-1$
    }

    public static DBTypeDescription build(final DatabaseMetaData metaData) {
        try {
            LOGGER.debug("Database data: driver [{}], url [{}], product name [{}]", metaData.getDriverName(), metaData.getURL(), metaData.getDatabaseProductName());
            DBTypeDescription dbTypeDescription = build(metaData.getDriverName(), metaData.getURL(), metaData.getDatabaseProductName());
            dbTypeDescription.username = metaData.getUserName();
            dbTypeDescription.driverVersion = metaData.getDriverVersion();
            dbTypeDescription.databaseProductVersion = metaData.getDatabaseProductVersion();
            LOGGER.debug("Inferred DB type: [{}]", dbTypeDescription.getDBType());
            return dbTypeDescription;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBTypeDescription build(final DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return build(connection.getMetaData());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    // ignore this one
                }
            }
        }
    }

    public static DBTypeDescription build(final String driverName, final String URL, final String databaseProductName) {
        DBTypeDescription dbTypeDescription = new DBTypeDescription();
        for (Entry<String, DBType> entry : dbProductNameMap.entrySet()) {
            if ((databaseProductName != null) && StringUtil.containsIgnoreCase(databaseProductName, entry.getKey())) {
                dbTypeDescription.dbType = entry.getValue();
            }
        }
        if (DBType.UNKNOWN.equals(dbTypeDescription.dbType)) {
        for (Entry<String, DBType> entry : dbURLMap.entrySet()) {
            if ((URL != null) && StringUtil.containsIgnoreCase(URL, entry.getKey())) {
                dbTypeDescription.dbType = entry.getValue();
            }
        }
        }
        dbTypeDescription.driverName = driverName;
        dbTypeDescription.url = URL;
        dbTypeDescription.databaseProductName = databaseProductName;
        return dbTypeDescription;
    }

    DBType dbType = DBType.UNKNOWN;
    String username = "";
    String driverName = "";
    String driverVersion = "";
    String url = "";
    String databaseProductName = "";
    String databaseProductVersion = "";

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    /**
     * @return the dbType
     */
    public DBType getDBType() {
        return dbType;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

}
