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
package com.jporm.types.io;

import java.util.function.BiFunction;

public class GeneratedKeyReaderImpl<R> implements GeneratedKeyReader<R> {

    private final BiFunction<ResultSet, Integer, R> result;
    private final String[] generatedColumns;

    public GeneratedKeyReaderImpl(String[] generatedColumns, BiFunction<ResultSet, Integer, R> result) {
        this.generatedColumns = generatedColumns;
        this.result = result;

    }

    @Override
    public String[] generatedColumnNames() {
        return generatedColumns;
    }

    @Override
    public R read(ResultSet generatedKeyResultSet, int affectedRows) {
        return result.apply(generatedKeyResultSet, affectedRows);
    }

}
