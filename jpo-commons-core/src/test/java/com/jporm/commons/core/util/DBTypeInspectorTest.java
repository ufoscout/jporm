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
package com.jporm.commons.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.commons.core.BaseCommonsCoreTestApi;
import com.jporm.sql.dsl.dialect.DBType;

public class DBTypeInspectorTest extends BaseCommonsCoreTestApi {

    @Test
    public void testDbType() {
        assertEquals(DBType.UNKNOWN, DBTypeDescription.build(null, null, null).getDBType());
        assertEquals(DBType.UNKNOWN, DBTypeDescription.build("", "", "bho").getDBType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals(DBType.ORACLE, DBTypeDescription.build("", "", "Oracle").getDBType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
