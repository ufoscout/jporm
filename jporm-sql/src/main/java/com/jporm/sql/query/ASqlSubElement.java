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
package com.jporm.sql.query;

import com.jporm.sql.query.namesolver.NameSolver;

/**
 * An {@link SqlSubElement} that keep track of the status of the object.
 * After a call to one of the render methods the result is stored and used for future calls
 * if the status of the object doen't change
 *
 * @author ufo
 *
 */
public abstract class ASqlSubElement implements SqlSubElement {

    /**
     * @return
     */
    @Override
    public final String renderSqlElement(final NameSolver nameSolver) {
        StringBuilder queryBuilder = new StringBuilder();
        renderSqlElement(queryBuilder, nameSolver);
        return queryBuilder.toString();
    }

}
