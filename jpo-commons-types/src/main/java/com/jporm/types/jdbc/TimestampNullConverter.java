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

import java.sql.Timestamp;

import com.jporm.types.TypeConverter;

public class TimestampNullConverter implements TypeConverter<Timestamp, Timestamp> {

    @Override
    public Timestamp clone(final Timestamp source) {
        return source;
    }

    @Override
    public Timestamp fromJdbcType(final Timestamp value) {
        return value;
    }

    @Override
    public Class<Timestamp> jdbcType() {
        return Timestamp.class;
    }

    @Override
    public Class<Timestamp> propertyType() {
        return Timestamp.class;
    }

    @Override
    public Timestamp toJdbcType(final Timestamp value) {
        return value;
    }

}
