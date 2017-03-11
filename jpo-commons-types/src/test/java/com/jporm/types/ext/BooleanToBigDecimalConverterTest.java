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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.converter.BooleanToBigDecimalConverter;

/**
 *
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public class BooleanToBigDecimalConverterTest extends BaseTestApi {

    @Test
    public void testBoolean() {
        final BooleanToBigDecimalConverter wrap = new BooleanToBigDecimalConverter();

        assertNull(wrap.fromJdbcType(null));
        assertTrue(wrap.fromJdbcType(BigDecimal.ONE));
        assertFalse(wrap.fromJdbcType(BigDecimal.ZERO));
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(0.123)));
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(10)));
        assertTrue(wrap.fromJdbcType(BigDecimal.valueOf(-10)));

        assertNull(wrap.toJdbcType(null));
        assertEquals(BigDecimal.ZERO, wrap.toJdbcType(false));
        assertEquals(BigDecimal.ONE, wrap.toJdbcType(true));
    }

}
