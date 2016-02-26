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
 *          ON : Feb 12, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.sql.dsl.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 12, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class StringUtilTest {

    @SuppressWarnings("nls")
    @Test
    public void test() {

        assertTrue(StringUtil.containsIgnoreCase("FranCescO( AS ", "francesco"));
        assertTrue(StringUtil.containsIgnoreCase("FranCescO( as ", " as "));
        assertTrue(StringUtil.containsIgnoreCase("FranCescO( AS ", " as "));
        assertTrue(StringUtil.containsIgnoreCase("FranCescO( as ", " AS "));
        assertTrue(StringUtil.containsIgnoreCase("FranCescO( AS ", "("));
        assertFalse(StringUtil.containsIgnoreCase("FranCescO( As ", "ff"));

    }

}
