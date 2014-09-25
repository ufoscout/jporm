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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.factory.ObjectBuilder;
import com.jporm.mapper.OrmClassTool;
import com.jporm.mapper.ServiceCatalog;
import com.jporm.mapper.clazz.ClassFieldImpl;
import com.jporm.mapper.clazz.ClassMap;
import com.jporm.mapper.relation.RelationInnerFK;
import com.jporm.mapper.relation.RelationOuterFK;
import com.jporm.query.clause.where.ExpressionElement;
import com.jporm.query.delete.DeleteQueryOrm;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.session.ResultSetRowReader;
import com.jporm.session.SqlExecutor;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorDeleteImpl implements OrmCRUDQueryExecutorDelete {

    private final ServiceCatalog serviceCatalog;

    public OrmCRUDQueryExecutorDeleteImpl(final ServiceCatalog serviceCatalog) {
        this.serviceCatalog = serviceCatalog;
    }

    @Override
    public <BEAN> int delete(final DeleteQueryOrm<BEAN> deleteQuery, final Class<BEAN> clazz) {
        OrmClassTool<BEAN> ormClassTool = serviceCatalog.getOrmClassTool(clazz);
        int deleted = deleteOuterRelations(deleteQuery, ormClassTool, clazz);
        Map<String, Object> innerRelationValues = fetchInnerRelationValue(deleteQuery, ormClassTool, clazz);

        final List<Object> values = new ArrayList<Object>();
        deleteQuery.appendValues(values);
        final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
        sqlExec.setQueryTimeout(deleteQuery.getQueryTimeout());
        deleted+= sqlExec.update(deleteQuery.renderSql(), values);

        deleted+= deleteInnerRelations(deleteQuery, ormClassTool, clazz, innerRelationValues);
        return deleted;
    }

    private <BEAN> int deleteOuterRelations(final DeleteQueryOrm<BEAN> deleteQuery, final OrmClassTool<BEAN> ormClassTool, final Class<BEAN> clazz) {
        int deleted = 0;
        List<RelationOuterFK<BEAN, ?, ?>> relations = ormClassTool.getClassMap().getOuterRelations();

        for (RelationOuterFK<BEAN, ?, ?> relation : relations) {
            if (relation.getCascadeInfo().onDelete()) {
                Class<?> relationWith = relation.getRelationWithClass();
                String relationFieldName = serviceCatalog.getOrmClassTool(relationWith).getClassMap().getFKs().versus(clazz).getBeanFieldName();

                String alias = clazz.getSimpleName();
                final String[] pkName = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
                CustomFindQuery idQuery = serviceCatalog.getSession().findQuery(pkName, clazz, alias);
                for (ExpressionElement expressionElement : deleteQuery.where().getElementList()) {
                    idQuery.where().and(expressionElement);
                }
                //                List<Object> ids = idQuery.find(new ResultSetRowReader<Object>() {
                //                    @Override
                //                    public Object readRow(final ResultSet rs, final int rowNum) throws SQLException {
                //                        return rs.getObject(pkName);
                //                    }
                //                });
                deleted+=serviceCatalog.getSession().deleteQuery(relationWith).where().in(relationFieldName, idQuery).now();
            }
        }
        return deleted;
    }


    private <BEAN> Map<String, Object> fetchInnerRelationValue(final DeleteQueryOrm<BEAN> deleteQuery, final OrmClassTool<BEAN> ormClassTool, final Class<BEAN> clazz) {
        final Map<String, Object> values = new HashMap<String, Object>();
        final List<RelationInnerFK<BEAN, ?>> allRelations = ormClassTool.getClassMap().getInnerRelations();
        final List<String> relationFieldNames = new ArrayList<String>();

        for (RelationInnerFK<BEAN, ?> relationInnerFK : allRelations) {
            if (relationInnerFK.getCascadeInfo().onDelete()) {
                relationFieldNames.add(relationInnerFK.getFieldName());
            }
        }
        if (!relationFieldNames.isEmpty()) {
            String alias = clazz.getSimpleName();
            ResultSetRowReader<Void> rsrr = new ResultSetRowReader<Void>() {
                @Override
                public Void readRow(final ResultSet rs, final int rowNum) throws SQLException {
                    for (String relationFieldName : relationFieldNames) {
                        values.put(relationFieldName, rs.getObject(relationFieldName) );
                    }
                    return null;
                }
            };
            CustomFindQuery find = serviceCatalog.getSession().findQuery(relationFieldNames.toArray(ObjectBuilder.EMPTY_STRING_ARRAY), clazz, alias);
            for (ExpressionElement expressionElement : deleteQuery.where().getElementList()) {
                find.where().and(expressionElement);
            }
            find.get(rsrr);
        }
        return values;
    }

    /**
     * @return
     */
    private <BEAN, RELATION_WITH> int deleteInnerRelations(final DeleteQueryOrm<BEAN> deleteQuery, final OrmClassTool<BEAN> ormClassTool, final Class<BEAN> clazz, final Map<String, Object> innerRelationValues) {
        int deleted = 0;
        for (Entry<String, Object> innerRelationEntry : innerRelationValues.entrySet()) {
            Object fieldValue = innerRelationEntry.getValue();
            if (fieldValue!=null) {
                String fieldName = innerRelationEntry.getKey();
                ClassFieldImpl<BEAN, Object> field = ormClassTool.getClassMap().getClassFieldByJavaName(fieldName);
                Class<RELATION_WITH> relationWith = field.getRelationVersusClass();
                ClassMap<RELATION_WITH> relationWithClassMap = serviceCatalog.getOrmClassTool(relationWith).getClassMap();
                String relationWithPkFieldName = relationWithClassMap.getPrimaryKeyColumnJavaNames()[0];
                deleted+=serviceCatalog.getSession().deleteQuery(relationWith).where().eq(relationWithPkFieldName, fieldValue).now();
            }
        }
        return deleted;
    }


}
