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
package com.jporm.query.crud.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.exception.OrmException;
import com.jporm.mapper.OrmClassTool;
import com.jporm.mapper.ServiceCatalog;
import com.jporm.mapper.clazz.ClassFieldImpl;
import com.jporm.mapper.relation.RelationOuterFK;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.reflection.SetManipulator;
import com.jporm.query.OrmRowMapper;
import com.jporm.query.find.FindQueryOrm;
import com.jporm.query.find.FindWhere;
import com.jporm.query.find.cache.CacheStrategyCallback;
import com.jporm.query.find.cache.CacheStrategyEntry;
import com.jporm.session.ResultSetReader;
import com.jporm.session.SqlExecutor;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorFindImpl implements OrmCRUDQueryExecutorFind {

    private final ServiceCatalog serviceCatalog;

    public OrmCRUDQueryExecutorFindImpl(final ServiceCatalog serviceCatalog) {
        this.serviceCatalog = serviceCatalog;
    }

    @Override
    public <BEAN> int getRowCount(final FindQueryOrm<BEAN> findQuery) {
        final List<Object> values = new ArrayList<Object>();
        findQuery.appendValues(values);
        final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
        sqlExec.setQueryTimeout(findQuery.getQueryTimeout());
        return sqlExec.queryForIntUnique(findQuery.renderRowCountSql(), values);
    }

    @Override
    public <BEAN> void get(final FindQueryOrm<BEAN> findQuery, final Class<BEAN> clazz, final OrmRowMapper<BEAN> srr, final int firstRow, final int maxRows, final int ignoreResultsMoreThan) throws OrmException {
        final List<Object> values = new ArrayList<Object>();
        findQuery.appendValues(values);
        final String sql = serviceCatalog.getDbProfile().getQueryTemplate().paginateSQL(findQuery.renderSql(), firstRow, maxRows);
        serviceCatalog.getCacheStrategy().find(findQuery.getCacheName(), sql, values, findQuery.getIgnoredFields(), srr, new CacheStrategyCallback<BEAN>() {

            @Override
            public void doWhenNotInCache(final CacheStrategyEntry<BEAN> cacheStrategyEntry) {
                final ResultSetReader<Object> resultSetReader = new ResultSetReader<Object>() {
                    @Override
                    public Object read(final ResultSet resultSet) throws SQLException {
                        int rowCount = 0;
                        final OrmClassTool<BEAN> ormClassTool = serviceCatalog.getOrmClassTool(clazz);
                        while ( resultSet.next() && (rowCount<ignoreResultsMoreThan)) {
                            BeanFromResultSet<BEAN> beanFromRS = ormClassTool.getOrmPersistor().beanFromResultSet(resultSet, findQuery.getIgnoredFields());
                            loadInnerRelations(findQuery.isLazy(), beanFromRS.getBean(), beanFromRS.getInnerFkValues(), ormClassTool );
                            loadOuterRelations(findQuery.isLazy(), beanFromRS.getBean(), ormClassTool);
                            srr.read( beanFromRS.getBean() , rowCount );
                            cacheStrategyEntry.add(beanFromRS.getBean());
                            rowCount++;
                        }
                        cacheStrategyEntry.end();
                        return null;
                    }

                };

                final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
                sqlExec.setQueryTimeout(findQuery.getQueryTimeout());
                sqlExec.query(sql, resultSetReader, values);
            }

        });

    }

    private <BEAN> void loadOuterRelations(final boolean lazy, final BEAN bean, final OrmClassTool<BEAN> ormClassTool) {
        if (!lazy) {
            List<RelationOuterFK<BEAN, ?, ?>> relations = ormClassTool.getClassMap().getOuterRelations();
            if (!relations.isEmpty()) {
                String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
                Object beanPrimaryKey = ormClassTool.getOrmPersistor().getPropertyValues(pks, bean)[0];
                for (RelationOuterFK<BEAN, ?, ?> relation : relations) {
                    Class<?> relationWith = relation.getRelationWithClass();
                    String relationFieldName = relation.getRelationClassField().getFieldName();

                    FindWhere<?> query = serviceCatalog.getSession().findQuery(relationWith).lazy(lazy).where().eq(relationFieldName, beanPrimaryKey);
                    Object relatedBeans = null;
                    if (relation.isOneToMany()) {
                        relatedBeans = query.getList();
                    } else {
                        relatedBeans = query.get();
                    }
                    (( SetManipulator<BEAN, Object>) relation.getSetManipulator()).setValue(bean, relatedBeans);
                }
            }
        }
    }

    private <BEAN, RELATION_VERSUS> void loadInnerRelations(final boolean lazy, final BEAN bean, final Map<String, Object> innerFkValues, final OrmClassTool<BEAN> ormClassTool) {
        if (!lazy) {
            for (Entry<String, Object> innerFkEntry : innerFkValues.entrySet()) {
                Object innerFkValue = innerFkEntry.getValue();
                if (innerFkValue!=null) {
                    String innerFkKey = innerFkEntry.getKey();
                    ClassFieldImpl<BEAN, RELATION_VERSUS> innerFkField = ormClassTool.getClassMap().getClassFieldByJavaName(innerFkKey);
                    Class<RELATION_VERSUS> relationWith = innerFkField.getRelationVersusClass();
                    innerFkField.getSetManipulator().setValue(bean, serviceCatalog.getSession().find(relationWith, innerFkValue).get());
                }
            }
        }
    }

}
