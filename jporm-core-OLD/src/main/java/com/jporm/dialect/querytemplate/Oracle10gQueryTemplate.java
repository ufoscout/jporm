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
package com.jporm.dialect.querytemplate;

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
public class Oracle10gQueryTemplate implements QueryTemplate {

    private static final String A_B_WHERE_B_A_ROWNUM = ") A ) B WHERE B.a_rownum > ";
    private static final String B_WHERE_B_A_ROWNUM = ") B WHERE B.a_rownum > ";
    private static final String A_WHERE_ROWNUM = ") A WHERE rownum <= ";
    private static final String SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM = "SELECT * FROM (SELECT A.*, rownum a_rownum FROM ( ";
    private static final String SPACE = " ";
    private static final String WHERE_ROWNUM_MIN = ") A WHERE rownum <= ";
    private static final String SELECT_A_FROM = "SELECT A.* FROM ( ";

    @Override
    public String insertQuerySequence(final String name) {
        return name + ".nextval"; //$NON-NLS-1$
    }

    /**
     * The first rownum in Oracle is 1
     */
    @Override
    public String paginateSQL(final String sql, final int firstRow, final int maxRows) {
        if ( (firstRow>=0) && (maxRows>0)) {
            StringBuilder query = new StringBuilder(SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM);
            query.append( sql );
            query.append( A_WHERE_ROWNUM );
            query.append( (firstRow + maxRows) );
            query.append(B_WHERE_B_A_ROWNUM);
            query.append( firstRow );
            query.append(SPACE);
            return query.toString();
        }
        if (firstRow>=0) {
            StringBuilder query = new StringBuilder(SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM);
            query.append( sql );
            query.append( A_B_WHERE_B_A_ROWNUM);
            query.append( firstRow );
            query.append(SPACE);
            return query.toString();
        }
        if (maxRows>0) {
            StringBuilder query = new StringBuilder(SELECT_A_FROM);
            query.append(sql);
            query.append(WHERE_ROWNUM_MIN);
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
