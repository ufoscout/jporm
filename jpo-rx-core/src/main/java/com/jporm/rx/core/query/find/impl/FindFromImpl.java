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
package com.jporm.rx.core.query.find.impl;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.From;
import com.jporm.commons.core.query.clause.impl.FromImpl;
import com.jporm.rx.core.query.find.FindQuery;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class FindFromImpl<BEAN> extends FromImpl<FindQuery<BEAN>> implements From<FindQuery<BEAN>> {

    private final FindQuery<BEAN> findQuery;

    public FindFromImpl(final com.jporm.sql.query.clause.From sqlFrom, final FindQuery<BEAN> findQuery)  {
        super(sqlFrom);
        this.findQuery = findQuery;
    }

    @Override
    protected FindQuery<BEAN> from() throws JpoException {
        return this.findQuery;
    }

}
