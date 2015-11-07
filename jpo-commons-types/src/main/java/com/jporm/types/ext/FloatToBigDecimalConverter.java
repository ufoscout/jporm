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
package com.jporm.types.ext;

import java.math.BigDecimal;

import com.jporm.types.TypeConverter;

/**
 * 
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public class FloatToBigDecimalConverter implements TypeConverter<Float, BigDecimal> {

    @Override
    public Float clone(final Float source) {
        return source;
    }

    @Override
    public Float fromJdbcType(final BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    @Override
    public Class<BigDecimal> jdbcType() {
        return BigDecimal.class;
    }

    @Override
    public Class<Float> propertyType() {
        return Float.class;
    }

    @Override
    public BigDecimal toJdbcType(final Float value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value);
    }

}
