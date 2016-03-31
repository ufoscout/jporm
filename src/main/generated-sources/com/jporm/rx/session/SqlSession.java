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
package com.jporm.rx.session;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.find.SelectQueryBuilder;
import com.jporm.rx.query.save.CustomSaveQuery;
import com.jporm.rx.query.update.CustomUpdateQuery;

public interface SqlSession {

    SqlExecutor executor();

    /**
     * Build and execute a SQL Select query
     * @param table
     * @return
     * @throws JpoException
     */
    SelectQueryBuilder selectAll();

    /**
     * Build and execute a SQL Select query
     * @param table
     * @return
     * @throws JpoException
     */
    SelectQueryBuilder select(String... fields);

    /**
     * Build and execute a SQL Delete query
     * @param table
     * @return
     * @throws JpoException
     */
    CustomDeleteQuery deleteFrom(String table) throws JpoException;

    /**
     * Build and execute a SQL Insert query
     * @param table
     * @param fields
     * @return
     */
    CustomSaveQuery insertInto(String table, String... fields);

    /**
     * Build and execute a SQL Update query
     * @param table
     * @return
     */
    CustomUpdateQuery update(String table);

}
