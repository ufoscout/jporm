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
package com.jporm.rm.session;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.sql.dialect.DBType;

public class DBTypeUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBTypeUtil.class);

    public final DBType getDBType(final DataSource dataSource) {
        DBType dbType = DBType.UNKNOWN;
        try {
            DBTypeDescription dbTypeDescription = DBTypeDescription.build(dataSource);
            dbType = dbTypeDescription.getDBType();
            getLogger().info("DB username: {}", dbTypeDescription.getUsername()); //$NON-NLS-1$
            getLogger().info("DB driver name: {}", dbTypeDescription.getDriverName()); //$NON-NLS-1$
            getLogger().info("DB driver version: {}", dbTypeDescription.getDriverVersion()); //$NON-NLS-1$
            getLogger().info("DB url: {}", dbTypeDescription.getUrl()); //$NON-NLS-1$
            getLogger().info("DB product name: {}", dbTypeDescription.getDatabaseProductName()); //$NON-NLS-1$
            getLogger().info("DB product version: {}", dbTypeDescription.getDatabaseProductVersion()); //$NON-NLS-1$
        } catch (RuntimeException ex) {
            getLogger().warn("Error while determining the database type", ex); //$NON-NLS-1$
        }
        getLogger().info("DB type is {}", dbType); //$NON-NLS-1$
        return dbType;
    }

    public Logger getLogger() {
        return logger;
    }
}
