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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author ufo
 *
 */
public class ResultSetRowReaderToResultSetReader<T> implements Function<ResultSet, List<T>> {

    private final BiFunction<ResultEntry, Integer, T> rsrr;

    public ResultSetRowReaderToResultSetReader(final BiFunction<ResultEntry, Integer, T> rsrr) {
        this.rsrr = rsrr;

    }

    @Override
    public List<T> apply(final ResultSet resultSet) {
        final List<T> results = new ArrayList<T>();
        int rowNum = 0;
        while (resultSet.hasNext()) {
            ResultEntry entry = resultSet.next();
            results.add(this.rsrr.apply(entry, rowNum++));
        }
        return results;
    }

}
