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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.lambda.LambdaGetter;
import com.jporm.persistor.accessor.lambda.LambdaSetter;
import com.jporm.persistor.accessor.lambda.LambdaWither;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerGetter;

public class BeanPropertyAccessorFactoryTest extends BaseTestApi {

	private final BeanPropertyAccessorFactory factory = new BeanPropertyAccessorFactory();

	@Test
	public void testMethodSetter() throws NoSuchMethodException, SecurityException {
		final Method setAgeMethod = TestBean.class.getMethod("setString", String.class);
		assertNotNull(setAgeMethod);

		final Setter<TestBean, String> setter = factory.buildSetterOrWither(setAgeMethod);
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

		final Setter<TestBean, String> wither = factory.buildSetterOrWither(withMethod);
		assertTrue(wither instanceof LambdaWither);

		final TestBean bean = new TestBean();
		final String address = UUID.randomUUID().toString();

		final TestBean beanFromWither = wither.setValue(bean, address);
		assertNotNull(beanFromWither);

		assertEquals(address, beanFromWither.getAddress());
		assertNull(bean.getAddress());
		assertNotSame(bean, beanFromWither);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOptionalField() throws NoSuchFieldException, SecurityException {
		final Field optionalField = TestBean.class.getField("hobby");
		assertNotNull(optionalField);

		final Getter<TestBean, String> getter = factory.buildGetter(optionalField);
		assertTrue(getter instanceof GetterOptional);
		assertTrue(((GetterOptional)getter).getInnerGetter() instanceof MethodHandlerGetter);

		final TestBean bean = new TestBean();

		assertNull(getter.getValue(bean));

		bean.hobby = Optional.empty();
		assertNull(getter.getValue(bean));

		bean.hobby = Optional.of(UUID.randomUUID().toString());
		assertEquals( bean.hobby.get(), getter.getValue(bean));

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOptionalGetter() throws SecurityException, NoSuchMethodException {
		final Method getMethod = TestBean.class.getMethod("getNickname");
		assertNotNull(getMethod);

		final Getter<TestBean, String> getter = factory.buildGetter(getMethod);
		assertTrue(getter instanceof GetterOptional);
		assertTrue(((GetterOptional)getter).getInnerGetter() instanceof LambdaGetter);

		final TestBean bean = new TestBean();

		assertNull(getter.getValue(bean));

		bean.setNickname( Optional.empty() );
		assertNull(getter.getValue(bean));

		bean.setNickname( Optional.of(UUID.randomUUID().toString()) );
		assertEquals( bean.getNickname().get(), getter.getValue(bean));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOptionalSetter() throws NoSuchMethodException, SecurityException {
		final Method setMethod = TestBean.class.getMethod("setNickname", Optional.class);
		assertNotNull(setMethod);

		final Setter<TestBean, String> setter = factory.buildSetterOrWither(setMethod);
		assertTrue(setter instanceof SetterOptional);
		assertTrue(((SetterOptional)setter).getInnerSetter() instanceof LambdaSetter);

		final TestBean bean = new TestBean();

		TestBean beanFromSetter = setter.setValue(bean, null);
		assertNotNull(beanFromSetter);
		assertFalse(bean.getNickname().isPresent());
		assertEquals(bean, beanFromSetter);

		final String name = UUID.randomUUID().toString();
		beanFromSetter = setter.setValue(bean, name);
		assertNotNull(beanFromSetter);
		assertEquals(name, bean.getNickname().get());
		assertEquals(bean, beanFromSetter);

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOptionalWither() throws NoSuchMethodException, SecurityException {
		final Method withMethod = TestBean.class.getMethod("withCar", Optional.class);
		assertNotNull(withMethod);

		final Setter<TestBean, String> wither = factory.buildSetterOrWither(withMethod);
		assertTrue(wither instanceof SetterOptional);
		assertTrue(((SetterOptional)wither).getInnerSetter() instanceof LambdaWither);

		final TestBean bean = new TestBean();

		TestBean beanFromSetter = wither.setValue(bean, null);
		assertNotNull(beanFromSetter);
		assertNull(bean.getCar());
		assertNotNull(beanFromSetter.getCar());
		assertFalse(beanFromSetter.getCar().isPresent());
		assertNotSame(bean, beanFromSetter);

		final String name = UUID.randomUUID().toString();
		beanFromSetter = wither.setValue(bean, name);
		assertNotNull(beanFromSetter);
		assertNull(bean.getCar());
		assertNotNull(beanFromSetter.getCar());
		assertTrue(beanFromSetter.getCar().isPresent());
		assertEquals(name, beanFromSetter.getCar().get());
		assertNotSame(bean, beanFromSetter);
	}

}
