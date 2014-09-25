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
package com.jporm.core.dialect;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.dialect.DBType;
import com.jporm.core.dialect.DetermineDBType;

public class DetermineDBTypeTest extends BaseTestApi {

	@Test
	public void testDbType() {
		assertEquals( DBType.UNKNOWN,  new DetermineDBType().determineDBType(null, null, null) );
		assertEquals( DBType.UNKNOWN,  new DetermineDBType().determineDBType("", "", "bho") ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals( DBType.ORACLE,  new DetermineDBType().determineDBType("", "", "Oracle") ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
