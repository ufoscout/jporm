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
package com.jporm.sql.dialect.sql;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 16, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public interface SqlStrategy {

    /**
     * Return the semantic of the DB to call a sequence inside an insert sql query.
     * E.G.
     * - For Oracle DB the returned String is:
     *   sequenceName + ".nextval"
     * - For HSQLDB the returned value is:
     *   "NEXT VALUE FOR " + sequenceName
     * @param sequenceName
     * @return
     */
    String insertQuerySequence(String sequenceName);

    /**
     * Decore the sql select query adding the proper pagination instructions if needed.
     *
     * @param sql
     * @param firstRow set the first row number to retrieve. It is ignored if negative.
     * @param maxRows set the max number of rows to retrieve. It is ignored if negative or equals to 0.
     * @return
     */
    String paginateSQL(String sql, int firstRow, int maxRows);

    /**
     * Decore the sql select query adding the proper pagination instructions if needed.
     *
     * @param sql
     * @param firstRow set the first row number to retrieve. It is ignored if negative.
     * @param maxRows set the max number of rows to retrieve. It is ignored if negative or equals to 0.
     * @return
     */
    String paginateSQL(StringBuffer sql, int firstRow, int maxRows);

}
