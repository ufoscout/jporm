/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.types.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.ext.BooleanToBigDecimalConverter;

public class OptionalTypeConverterTest extends BaseTestApi {

    @Test
    public void testOptionalConverter() {
        final OptionalTypeConverter<Boolean, BigDecimal> wrap = new OptionalTypeConverter<Boolean, BigDecimal>(new BooleanToBigDecimalConverter());

        assertNotNull(wrap.fromJdbcType(null));
        assertFalse(wrap.fromJdbcType(null).isPresent());
        assertTrue(wrap.fromJdbcType(BigDecimal.ONE).get());
        assertFalse(wrap.fromJdbcType(BigDecimal.ZERO).get());
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(0.123)).get());
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(10)).get());
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(-10)).get());

        assertNull(wrap.toJdbcType(null));
        assertNull(wrap.toJdbcType(Optional.empty()));
        assertEquals(BigDecimal.ZERO, wrap.toJdbcType(Optional.<Boolean> of(Boolean.FALSE)));
        assertEquals(BigDecimal.ONE, wrap.toJdbcType(Optional.<Boolean> of(Boolean.TRUE)));
    }

}
