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
package com.jporm.types.jdbc;

import java.io.Reader;

import com.jporm.types.TypeConverter;

public class ReaderNullConverter implements TypeConverter<Reader, Reader> {

    @Override
    public Reader clone(final Reader source) {
        return source;
    }

    @Override
    public Reader fromJdbcType(final Reader value) {
        return value;
    }

    @Override
    public Class<Reader> jdbcType() {
        return Reader.class;
    }

    @Override
    public Class<Reader> propertyType() {
        return Reader.class;
    }

    @Override
    public Reader toJdbcType(final Reader value) {
        return value;
    }

}
