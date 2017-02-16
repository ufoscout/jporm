/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.persistor.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.UUID;

import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.lambda.LambdaSetter;
import com.jporm.persistor.accessor.lambda.LambdaWither;

public class BeanPropertyAccessorFactoryTest extends BaseTestApi {

	@Test
	public void testMethodSetter() throws NoSuchMethodException, SecurityException {
		final Method setAgeMethod = TestBean.class.getMethod("setString", String.class);
		assertNotNull(setAgeMethod);

		final Setter<TestBean, String> setter = BeanAccessorFactory.buildSetterOrWither(setAgeMethod);
		assertTrue(setter instanceof LambdaSetter);

		final TestBean bean = new TestBean();
		final String name = UUID.randomUUID().toString();

		final TestBean beanFromSetter = setter.setValue(bean, name);
		assertNotNull(beanFromSetter);
		assertEquals(name, bean.getString());
		assertEquals(bean, beanFromSetter);
	}

	@Test
	public void testMethodWither() throws NoSuchMethodException, SecurityException {
		final Method withMethod = TestBean.class.getMethod("withAddress", String.class);
		assertNotNull(withMethod);

		final Setter<TestBean, String> wither = BeanAccessorFactory.buildSetterOrWither(withMethod);
		assertTrue(wither instanceof LambdaWither);

		final TestBean bean = new TestBean();
		final String address = UUID.randomUUID().toString();

		final TestBean beanFromWither = wither.setValue(bean, address);
		assertNotNull(beanFromWither);

		assertEquals(address, beanFromWither.getAddress());
		assertNull(bean.getAddress());
		assertNotSame(bean, beanFromWither);
	}

}
