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
package com.jporm.commons.core.query.find.impl;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.From;
import com.jporm.commons.core.query.clause.impl.FromImpl;
import com.jporm.sql.dsl.query.select.from.FromProvider;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CommonFindFromImpl<T extends From<T>> extends FromImpl<T> implements From<T> {

    private final T findQuery;

    public CommonFindFromImpl(final FromProvider<Class<?>> sqlFrom, final T findQuery) {
        super(sqlFrom);
        this.findQuery = findQuery;
    }

    @Override
    protected T from() throws JpoException {
        return this.findQuery;
    }

}
