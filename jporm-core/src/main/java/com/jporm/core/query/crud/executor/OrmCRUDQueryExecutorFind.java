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
package com.jporm.core.query.crud.executor;

import java.sql.ResultSet;

import com.jporm.core.query.find.FindQueryOrm;
import com.jporm.exception.OrmException;
import com.jporm.query.OrmRowMapper;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public interface OrmCRUDQueryExecutorFind {

    /**
     * 
     * @param findQuery
     * @param clazz
     * @param srr
     * @param firstRow the firstRow used for pagination purposes
     * @param maxRows the maxRows used for pagination purposes
     * @param ignoreResultsMoreThan set the maximum number of entries that will be read from the {@link ResultSet}.
     * To note that this simply stop the loop on the {@link ResultSet} and does not modify the query.
     * @throws OrmException
     */
    <BEAN> void get(FindQueryOrm<BEAN> findQuery, Class<BEAN> clazz, OrmRowMapper<BEAN> srr, int firstRow, int maxRows, int ignoreResultsMoreThan) throws OrmException;

    /**
     * @param findQuery
     * @return
     */
    <BEAN> int getRowCount(FindQueryOrm<BEAN> findQuery);

}
