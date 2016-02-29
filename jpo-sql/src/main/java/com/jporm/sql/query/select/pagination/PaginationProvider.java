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
package com.jporm.sql.query.select.pagination;

import com.jporm.sql.query.Sql;
import com.jporm.sql.query.select.LockMode;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface PaginationProvider<PAGINATION_PROVIDER extends PaginationProvider<PAGINATION_PROVIDER>> extends Sql {

    PAGINATION_PROVIDER limit(int limit);

    PAGINATION_PROVIDER lockMode(LockMode lockMode);

    /**
     * Set the "FOR UPDATE" {@link LockMode} for the query
     *
     * @return
     */
    PAGINATION_PROVIDER forUpdate();

    /**
     * Set the "FOR UPDATE NOWAIT" {@link LockMode} for the query
     *
     * @return
     */
    PAGINATION_PROVIDER forUpdateNoWait();

    PAGINATION_PROVIDER offset(int offset);

}
