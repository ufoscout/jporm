/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dialect.sqlserver2008;

import java.util.List;

import com.jporm.sql.dialect.SqlFromRender;
import com.jporm.sql.dialect.SqlGroupByRender;
import com.jporm.sql.dialect.SqlOrderByRender;
import com.jporm.sql.dialect.SqlPaginationRender;
import com.jporm.sql.dialect.SqlSelectRender;
import com.jporm.sql.dialect.SqlWhereRender;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.SelectImpl;
import com.jporm.sql.query.select.orderby.OrderByImpl;
import com.jporm.sql.query.select.orderby.OrderElementImpl;

public class SQLServer2008_SqlSelectRender implements SqlSelectRender, SqlFromRender, SqlOrderByRender, SqlGroupByRender, SqlWhereRender {

    private static final String ROW_NUMBER_OVER_SELECT_NULL_AS_ROW_NUM = ", ROW_NUMBER() OVER (ORDER BY (SELECT null)) AS RowNum ";
    private static final String AS_ROW_NUM = ") AS RowNum ";
    private static final String ROW_NUMBER_OVER_ORDER_BY = ", ROW_NUMBER() OVER (ORDER BY ";

    private final SqlPaginationRender paginationRender = new SQLServer2008_SqlPaginationRender();

    @Override
    public void postSelectBuilder(SelectImpl<?> select, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        renderOrderByRowNumber(select, queryBuilder, propertiesProcessor);
    }

    @Override
    public void render(OrderByImpl<?> orderBy, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
    }

    private void renderOrderByRowNumber(SelectImpl<?> select, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        List<OrderElementImpl> elementList = select.orderBy().getOrderByElements();
        if (!elementList.isEmpty()) {
            queryBuilder.append(ROW_NUMBER_OVER_ORDER_BY);
            for (final OrderElementImpl orderElement : elementList) {
                renderOrderElement(orderElement, queryBuilder, propertiesProcessor);
            }
            queryBuilder.append(AS_ROW_NUM);
        } else if ((select.getFirstRow() >= 0) || (select.getMaxRows() > 0)) {
            queryBuilder.append(ROW_NUMBER_OVER_SELECT_NULL_AS_ROW_NUM);
        }
    }

    @Override
    public SqlPaginationRender getPaginationRender() {
        return paginationRender;
    }

    @Override
    public SqlFromRender getFromRender() {
        return this;
    }

    @Override
    public SqlWhereRender getWhereRender() {
        return this;
    }

    @Override
    public SqlGroupByRender getGroupByRender() {
        return this;
    }

    @Override
    public SqlOrderByRender getOrderByRender() {
        return this;
    }

}
