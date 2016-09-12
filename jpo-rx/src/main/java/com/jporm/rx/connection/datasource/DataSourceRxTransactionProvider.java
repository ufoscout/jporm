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
package com.jporm.rx.connection.datasource;

import javax.sql.DataSource;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rx.connection.RxTransaction;
import com.jporm.rx.connection.RxTranscationProvider;
import com.jporm.sql.dialect.DBProfile;

public class DataSourceRxTransactionProvider implements RxTranscationProvider {

//    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceRxTransactionProvider.class);
    private final DataSource dataSource;
    private DBProfile dbType;

    public DataSourceRxTransactionProvider(final DataSource dataSource) {
        this(dataSource, null);
    }

    public DataSourceRxTransactionProvider(final DataSource dataSource, final DBProfile dbType) {
        this.dataSource = dataSource;
        this.dbType = dbType;
    }

    @Override
    public final DBProfile getDBProfile() {
        if (dbType == null) {
            dbType = DBTypeDescription.build(dataSource).getDBType().getDBProfile();
        }
        return dbType;
    }

    @Override
    public RxTransaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
        return new DataSourceRxTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory, dataSource);
    }

}
