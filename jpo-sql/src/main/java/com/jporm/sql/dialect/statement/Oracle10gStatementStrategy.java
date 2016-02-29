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
package com.jporm.sql.dialect.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Francesco Cina
 *
 *         28/giu/2011
 */
public class Oracle10gStatementStrategy implements StatementStrategy {

    @Override
    public PreparedStatement prepareStatement(final Connection conn, final String sql, final String[] generatedColumnNames) throws SQLException {
        if (generatedColumnNames.length > 0) {
            return conn.prepareStatement(sql, generatedColumnNames);
        }
        return conn.prepareStatement(sql);
    }

}
