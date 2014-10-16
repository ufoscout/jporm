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
package com.jporm.core.query.find;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.clause.FromImpl;
import com.jporm.exception.OrmException;
import com.jporm.query.find.FindFrom;
import com.jporm.query.find.FindQuery;
import com.jporm.query.namesolver.NameSolver;

/**
 * 
 * @author ufo
 *
 * @param <BEAN>
 */
public class FindFromImpl<BEAN> extends FromImpl<FindQuery<BEAN>> implements FindFrom<BEAN> {

    private final FindQuery<BEAN> findQuery;

    public FindFromImpl(final FindQuery<BEAN> findQuery, final ServiceCatalog serviceCatalog, final Class<?> clazz, final Integer nameSolverClassId, final NameSolver nameSolver)  {
        super(serviceCatalog, clazz, nameSolverClassId, nameSolver);
        this.findQuery = findQuery;
    }

    @Override
    protected FindQuery<BEAN> from() throws OrmException {
        return this.findQuery;
    }

}
