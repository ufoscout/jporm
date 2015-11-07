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
package com.jporm.persistor.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.version.VersionMathFactory;

/**
 *
 * @author ufo
 *
 */
public class VersionMathTest extends BaseTestApi {

    @Test
    public void testIncreaser() {

        VersionMathFactory mathFactory = new VersionMathFactory();

        assertEquals(Integer.valueOf(0), mathFactory.getMath(Integer.class, true).increase(true, 9));
        assertEquals(Integer.valueOf(0), mathFactory.getMath(Integer.class, true).increase(false, null));
        assertEquals(Integer.valueOf(10), mathFactory.getMath(Integer.class, true).increase(false, 9));

        assertEquals(Integer.valueOf(0), mathFactory.getMath(Integer.TYPE, true).increase(true, 9));
        assertEquals(Integer.valueOf(0), mathFactory.getMath(Integer.TYPE, true).increase(false, null));
        assertEquals(Integer.valueOf(10), mathFactory.getMath(Integer.TYPE, true).increase(false, 9));

        assertEquals(Long.valueOf(0), mathFactory.getMath(Long.class, true).increase(true, 9l));
        assertEquals(Long.valueOf(0), mathFactory.getMath(Long.class, true).increase(false, null));
        assertEquals(Long.valueOf(10), mathFactory.getMath(Long.class, true).increase(false, 9l));

        assertEquals(Long.valueOf(0), mathFactory.getMath(Long.TYPE, true).increase(true, 9l));
        assertEquals(Long.valueOf(0), mathFactory.getMath(Long.TYPE, true).increase(false, null));
        assertEquals(Long.valueOf(10), mathFactory.getMath(Long.TYPE, true).increase(false, 9l));

        assertEquals(BigDecimal.valueOf(0), mathFactory.getMath(BigDecimal.class, true).increase(true, BigDecimal.valueOf(9)));
        assertEquals(BigDecimal.valueOf(0), mathFactory.getMath(BigDecimal.class, true).increase(false, null));
        assertEquals(BigDecimal.valueOf(10), mathFactory.getMath(BigDecimal.class, true).increase(false, BigDecimal.valueOf(9)));
    }

    @Test
    public void testNullIncreaser() {

        VersionMathFactory mathFactory = new VersionMathFactory();

        assertEquals(Integer.valueOf(0), mathFactory.getMath(Integer.class, true).increase(true, 9));
        assertEquals(Integer.valueOf(9), mathFactory.getMath(Integer.class, false).increase(true, 9));

    }

    @Test
    public void testWrongType() {

        VersionMathFactory mathFactory = new VersionMathFactory();

        try {
            mathFactory.getMath(String.class, true);
            fail("An OrmConfigurationException should have been thrown"); //$NON-NLS-1$
        } catch (RuntimeException e) {
            // do nothing
        }

    }
}
