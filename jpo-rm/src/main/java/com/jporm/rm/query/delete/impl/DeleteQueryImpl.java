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
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.rm.query.delete.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.delete.ADeleteQuery;
import com.jporm.commons.core.query.strategy.DeleteExecutionStrategy;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy;
import com.jporm.rm.query.delete.DeleteQuery;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBType;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class DeleteQueryImpl<BEAN> extends ADeleteQuery<BEAN> implements DeleteQuery, DeleteExecutionStrategy {

    // private final BEAN bean;
    private final Collection<BEAN> beans;
    private final SqlExecutor sqlExecutor;
    private final DBType dbType;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public DeleteQueryImpl(final Collection<BEAN> beans, final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory, final DBType dbType) {
        super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
        this.beans = beans;
        this.sqlExecutor = sqlExecutor;
        this.dbType = dbType;
    }

    @Override
    public int execute() {
        return QueryExecutionStrategy.build(dbType.getDBProfile()).executeDelete(this);
    }

    @Override
    public int executeWithBatchUpdate() {
        String query = getCacheableQuery(dbType.getDBProfile());
        String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();

        // WITH BATCH UPDATE VERSION:
        Collection<Object[]> values = new ArrayList<>();
        beans.forEach(bean -> values.add(getOrmClassTool().getPersistor().getPropertyValues(pks, bean)));
        int[] result = sqlExecutor.batchUpdate(query, values);
        return IntStream.of(result).sum();
    }

    @Override
    public int executeWithSimpleUpdate() {
        String query = getCacheableQuery(dbType.getDBProfile());
        String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();

        // WITHOUT BATCH UPDATE VERSION:
        int result = 0;
        for (BEAN bean : beans) {
            Object[] values = getOrmClassTool().getPersistor().getPropertyValues(pks, bean);
            result += sqlExecutor.update(query, values);

        }
        return result;
    }

}
