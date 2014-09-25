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
package com.jporm.core.persistor.wrapper;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.persistor.type.ext.BooleanToBigDecimalWrapper;

/**
 * 
 * @author Francesco Cina'
 *
 * Apr 1, 2012
 */
public class BooleanToBigDecimalWrapperTest extends BaseTestApi {

	@Test
	public void testBoolean() {
		final BooleanToBigDecimalWrapper wrap = new BooleanToBigDecimalWrapper();

		assertNull( wrap.wrap(null) );
		assertTrue( wrap.wrap(BigDecimal.ONE) );
		assertFalse( wrap.wrap(BigDecimal.ZERO) );
		assertTrue( wrap.wrap(BigDecimal.valueOf( 0.123 )) );
		assertTrue( wrap.wrap(BigDecimal.valueOf( 10 )) );
		assertTrue( wrap.wrap(BigDecimal.valueOf( -10 )) );

		assertNull( wrap.unWrap(null) );
		assertEquals( BigDecimal.ZERO , wrap.unWrap(false) );
		assertEquals( BigDecimal.ONE , wrap.unWrap(true) );
	}

}
