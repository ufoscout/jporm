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
package com.jporm.commons.core.io;

import java.util.function.Function;

import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author ufo
 *
 */
public class ResultSetRowReaderToResultSetReaderUnique<T> implements Function<ResultSet, T> {

    private final IntBiFunction<ResultEntry, T> rsrr;

    public ResultSetRowReaderToResultSetReaderUnique(final IntBiFunction<ResultEntry, T> rsrr) {
        this.rsrr = rsrr;

    }

    @Override
    public T apply(final ResultSet resultSet) {
        if (resultSet.hasNext()) {
            ResultEntry entry = resultSet.next();
            T result = this.rsrr.apply(entry, 0);
            if (resultSet.hasNext()) {
                throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows higher than 1"); //$NON-NLS-1$
            }
            return result;
        }
        throw new JpoNotUniqueResultNoResultException("The query execution has returned zero rows. One row was expected"); //$NON-NLS-1$
    }

}
