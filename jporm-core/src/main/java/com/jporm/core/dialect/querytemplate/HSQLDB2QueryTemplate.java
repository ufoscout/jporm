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
package com.jporm.core.dialect.querytemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 16, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class HSQLDB2QueryTemplate implements QueryTemplate {

    private static final String SPACE = " ";
    private static final String ROWS = " ROWS ";
    private static final String OFFSET2 = "OFFSET ";
    private static final String OFFSET = " OFFSET ";
    private static final String LIMIT = "LIMIT ";
    private static final String NEXT_VALUE_FOR = "NEXT VALUE FOR ";

    @Override
    public String insertQuerySequence(final String name) {
        return NEXT_VALUE_FOR + name;
    }

    @Override
    public String paginateSQL(final String sql, final int firstRow, final int maxRows) {
        if ( (firstRow>=0) && (maxRows>0)) {
            StringBuilder query = new StringBuilder(sql);
            query.append(LIMIT);
            query.append(maxRows);
            query.append(OFFSET);
            query.append(firstRow);
            query.append(SPACE);
            return query.toString();
        }
        if (firstRow>=0) {
            StringBuilder query = new StringBuilder(sql);
            query.append(OFFSET2);
            query.append(firstRow);
            query.append(ROWS);
            return query.toString();
        }
        if (maxRows>0) {
            StringBuilder query = new StringBuilder(sql);
            query.append(LIMIT);
            query.append(maxRows);
            query.append(SPACE);
            return query.toString();
        }
        return sql;
    }

    @Override
    public PreparedStatement prepareStatement(final Connection conn, final String sql, final String[] generatedColumnNames) throws SQLException {
        return conn.prepareStatement(sql, generatedColumnNames);
    }

}
