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

import java.math.BigDecimal;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author ufo
 *
 */
public class BigDecimalResultSetReaderUnique implements Function<ResultSet, BigDecimal> {

    @Override
    public BigDecimal apply(final ResultSet resultSet) {
        if (resultSet.next()) {
            BigDecimal result = resultSet.getBigDecimal(0);
            if (resultSet.next()) {
                throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows higher than 1"); //$NON-NLS-1$
            }
            return result;
        }
        throw new JpoNotUniqueResultNoResultException("The query execution has returned zero rows. One row was expected"); //$NON-NLS-1$
    }

}
