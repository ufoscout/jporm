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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 23, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.types.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.ext.EnumConverter;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeFactoryEnumTest extends BaseTestApi {

    enum Color {
        WHITE, BLUE
    }

    enum Number {
        ZERO(BigDecimal.valueOf(0)), ONE(BigDecimal.valueOf(1)), TWO(BigDecimal.valueOf(2));

        static Number fromValue(final BigDecimal fromValue) {
            if (BigDecimal.valueOf(0).equals(fromValue)) {
                return ZERO;
            }
            if (BigDecimal.valueOf(1).equals(fromValue)) {
                return ONE;
            }
            if (BigDecimal.valueOf(2).equals(fromValue)) {
                return TWO;
            }
            return null;
        }

        private final BigDecimal value;

        Number(final BigDecimal value) {
            this.value = value;
        }

        public BigDecimal getValue() {
            return value;
        }

    }

    class NumberTypeConverter implements TypeConverter<Number, BigDecimal> {

        @Override
        public Number clone(final Number source) {
            return source;
        }

        @Override
        public Number fromJdbcType(final BigDecimal value) {
            return Number.fromValue(value);
        }

        @Override
        public Class<BigDecimal> jdbcType() {
            return BigDecimal.class;
        }

        @Override
        public Class<Number> propertyType() {
            return Number.class;
        }

        @Override
        public BigDecimal toJdbcType(final Number value) {
            return value.getValue();
        }

    }

    private final TypeConverterFactory typeFactory = new TypeConverterFactory();

    @Test
    public void testEnumConverterNotNull() {
        assertNotNull(typeFactory.getTypeConverter(Number.class));
        assertNotNull(typeFactory.getTypeConverter(Color.class));
    }

    @Test
    public void testEnumConverterOverriding() {
        assertEquals(EnumConverter.class, typeFactory.getTypeConverter(Number.class).getTypeConverter().getClass());
        assertEquals(EnumConverter.class, typeFactory.getTypeConverter(Color.class).getTypeConverter().getClass());

        typeFactory.addTypeConverter(new NumberTypeConverter());

        assertEquals(NumberTypeConverter.class, typeFactory.getTypeConverter(Number.class).getTypeConverter().getClass());
        assertEquals(EnumConverter.class, typeFactory.getTypeConverter(Color.class).getTypeConverter().getClass());
    }

    @Test
    public void testEnumConverterOverridingAndUse() {

        TypeConverter<Color, Object> colorConverter = typeFactory.getTypeConverter(Color.class).getTypeConverter();
        assertEquals("WHITE", colorConverter.toJdbcType(Color.WHITE));
        assertEquals(Color.BLUE, colorConverter.fromJdbcType("BLUE"));

        TypeConverter<Number, Object> numberConverter = typeFactory.getTypeConverter(Number.class).getTypeConverter();
        assertEquals("ONE", numberConverter.toJdbcType(Number.ONE));
        assertEquals(Number.TWO, numberConverter.fromJdbcType("TWO"));

        typeFactory.addTypeConverter(new NumberTypeConverter());

        TypeConverter<Number, Object> overriddenNumberConverter = typeFactory.getTypeConverter(Number.class).getTypeConverter();
        assertEquals(BigDecimal.valueOf(1), overriddenNumberConverter.toJdbcType(Number.ONE));
        assertEquals(Number.TWO, overriddenNumberConverter.fromJdbcType(BigDecimal.valueOf(2)));

    }

}
