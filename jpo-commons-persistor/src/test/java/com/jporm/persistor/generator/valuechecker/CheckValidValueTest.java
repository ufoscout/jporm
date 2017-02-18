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
package com.jporm.persistor.generator.valuechecker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.generator.valuechecker.BigDecimalValueChecker;
import com.jporm.persistor.generator.valuechecker.ByteValueChecker;
import com.jporm.persistor.generator.valuechecker.IntegerValueChecker;
import com.jporm.persistor.generator.valuechecker.LongValueChecker;
import com.jporm.persistor.generator.valuechecker.ShortValueChecker;

/**
 *
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public class CheckValidValueTest extends BaseTestApi {

    @Test
    public void testValues() {

        assertTrue(new BigDecimalValueChecker().useGenerator(null));
        assertTrue(new BigDecimalValueChecker().useGenerator(BigDecimal.valueOf(-1)));
        assertFalse(new BigDecimalValueChecker().useGenerator(BigDecimal.valueOf(0)));
        assertFalse(new BigDecimalValueChecker().useGenerator(BigDecimal.valueOf(12)));

        assertTrue(new ByteValueChecker().useGenerator(null));
        assertTrue(new ByteValueChecker().useGenerator((byte) -1));
        assertFalse(new ByteValueChecker().useGenerator((byte) 0));
        assertFalse(new ByteValueChecker().useGenerator((byte) 1));

        assertTrue(new IntegerValueChecker().useGenerator(null));
        assertTrue(new IntegerValueChecker().useGenerator(-1));
        assertFalse(new IntegerValueChecker().useGenerator(0));
        assertFalse(new IntegerValueChecker().useGenerator(1));

        assertTrue(new LongValueChecker().useGenerator(null));
        assertTrue(new LongValueChecker().useGenerator(-1l));
        assertFalse(new LongValueChecker().useGenerator(0l));
        assertFalse(new LongValueChecker().useGenerator(1l));

        assertTrue(new ShortValueChecker().useGenerator(null));
        assertTrue(new ShortValueChecker().useGenerator((short) -1));
        assertFalse(new ShortValueChecker().useGenerator((short) 0));
        assertFalse(new ShortValueChecker().useGenerator((short) 1));

    }

}
