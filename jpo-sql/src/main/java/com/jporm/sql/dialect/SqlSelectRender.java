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
package com.jporm.sql.dialect;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.SelectImpl;
import com.jporm.sql.util.StringUtil;

public interface SqlSelectRender {

    String SELECT = "SELECT ";
    String DISTINCT = "DISTINCT ";
    String WHITE_SPACE = " ";
    String COMMA = ", ";
    String SQL_SELECT_SPLIT_PATTERN = "[^,]*[\\(][^\\)]*[\\)][^,]*|[^,]+";
    Pattern patternSelectClause = Pattern.compile(SQL_SELECT_SPLIT_PATTERN);

    String SQL_EXCEPT = "\nEXCEPT \n";
    String SQL_INTERSECT = "\nINTERSECT \n";
    String SQL_UNION = "\nUNION \n";
    String SQL_UNION_ALL = "\nUNION ALL \n";

    SqlPaginationRender getPaginationRender();
    SqlFromRender getFromRender();
    SqlWhereRender getWhereRender();
    SqlGroupByRender getGroupByRender();
    SqlOrderByRender getOrderByRender();

    default void render(SelectImpl<?> select, StringBuilder queryBuilder) {
        int firstRow = select.getFirstRow();
        int maxRows = select.getMaxRows();
        if ((firstRow>-1) || (maxRows>0)) {
            getPaginationRender().paginateSQL(queryBuilder, firstRow, maxRows, builder -> renderWithoutPagination(select, builder));
        } else {
            renderWithoutPagination(select, queryBuilder);
        }
    }

    default void renderWithoutPagination(SelectImpl<?> select, final StringBuilder builder) {
        builder.append(SELECT);
        if (select.isDistinct()) {
            builder.append(DISTINCT);
        }

        String[] selectFieldsArray = preProcessFields(select);
        int size = selectFieldsArray.length;
        boolean first = true;
        PropertiesProcessor propertiesProcessor = select.getPropertiesProcessor();

        for (int i = 0; i < size; i++) {
            String field = selectFieldsArray[i];
            if (!first) {
                builder.append(COMMA);
            } else {
                first = false;
            }

            final Matcher m = patternSelectClause.matcher(field);
            boolean loop = m.find();
            while (loop) {
                solveField(m.group().trim(), builder, propertiesProcessor);
                loop = m.find();
                if (loop) {
                    builder.append(COMMA);
                }
            }
        }

        builder.append(WHITE_SPACE);

        getFromRender().render(select, builder, propertiesProcessor);
        getWhereRender().render(select.where(), builder, propertiesProcessor);
        getGroupByRender().render(select.groupByImpl(), builder, propertiesProcessor);
        getOrderByRender().render(select.orderBy(), builder, propertiesProcessor);
        render(SQL_UNION, select.getUnions(), builder);
        render(SQL_UNION_ALL, select.getUnionAlls(), builder);
        render(SQL_EXCEPT, select.getExcepts(), builder);
        render(SQL_INTERSECT,select.getIntersects(), builder);

        builder.append(select.getLockMode().getMode());
    }

    default String[] preProcessFields(SelectImpl<?> select) {
        return select.getSelectFields().get();
    }


    String OPEN_PARENTESIS = "(";
    String LOWERCASE_AS = " as ";
    String STAR = "*";
    String AS_OPEN_QUOTES = " AS \"";
    String QUOTES = "\"";

    /**
     * @param string
     * @return
     */
    default void solveField(final String field, final StringBuilder queryBuilder, final PropertiesProcessor propertiesProcessor) {
        if (field.contains(OPEN_PARENTESIS) || StringUtil.containsIgnoreCase(field, LOWERCASE_AS) || field.contains(STAR)) {
            propertiesProcessor.solveAllPropertyNames(field, queryBuilder);
        } else {
            queryBuilder.append(propertiesProcessor.solvePropertyName(field));
            queryBuilder.append(AS_OPEN_QUOTES);
            queryBuilder.append(field);
            queryBuilder.append(QUOTES);
        }
    }


    default void render(String clause, List<SelectCommon> selects, final StringBuilder queryBuilder) {
        for (SelectCommon selectCommon : selects) {
            queryBuilder.append(clause);
            selectCommon.sqlQuery(queryBuilder);
        }
    }

}
