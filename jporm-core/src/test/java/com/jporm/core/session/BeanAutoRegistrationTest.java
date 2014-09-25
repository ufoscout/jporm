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
package com.jporm.core.session;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.core.BaseTestApi;
import com.jporm.core.JPOrm;
import com.jporm.core.domain.section05.AutoId;
import com.jporm.core.session.NullSessionProvider;

/**
 * 
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class BeanAutoRegistrationTest extends BaseTestApi {

	@Test
	public void testRegisterAutoId() {
		// Register a class in the orm and use it
		final JPO jpOrm = new JPOrm(new NullSessionProvider());
		jpOrm.register(AutoId.class);

		//SHOULD NOT THROWN EXCEPTIONS
		jpOrm.session().save(new AutoId());
	}

	@Test
	public void testAutoRegisterAutoId() {
		// Use a class without register it, it should be auto registered
		final JPO jpOrm = new JPOrm(new NullSessionProvider());

		//SHOULD NOT THROWN EXCEPTIONS
		jpOrm.session().save(new AutoId());
	}

}
