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
package com.jporm.persistor.accessor.lambda;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.accessor.Setter;
import com.jporm.persistor.accessor.TestBean;

public class LambdaAccessorFactoryTest extends BaseTestApi {

	private Field privateStringField;
	private Field publicLongPrimitiveField;
	private Field publicLongField;
	private Method stringSetterMethod;
	private Method stringGetterMethod;
	private Method intPrimitiveSetterMethod;
	private Method intPrimitiveGetterMethod;
	private Method integerSetterMethod;
	private Method integerGetterMethod;
	private Method integerPrivateGetterMethod;

	private final LambdaAccessorFactory factory = new LambdaAccessorFactory();

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		privateStringField = TestBean.class.getDeclaredField("privateString");

		publicLongPrimitiveField = TestBean.class.getField("publicLongPrimitive");
		publicLongField = TestBean.class.getField("publicLong");

		stringSetterMethod = TestBean.class.getMethod("setString", String.class); //$NON-NLS-1$
		stringGetterMethod = TestBean.class.getMethod("getString"); //$NON-NLS-1$

		intPrimitiveSetterMethod = TestBean.class.getMethod("setIntPrimitive", Integer.TYPE); //$NON-NLS-1$
		intPrimitiveGetterMethod = TestBean.class.getMethod("getIntPrimitive"); //$NON-NLS-1$

		integerSetterMethod = TestBean.class.getMethod("setInteger", Integer.class); //$NON-NLS-1$
		integerGetterMethod = TestBean.class.getMethod("getInteger"); //$NON-NLS-1$
		integerPrivateGetterMethod = TestBean.class.getDeclaredMethod("getIntegerPrivate");
	}

	@Test
	public void testGetter() {
		final TestBean testBeanOne = new TestBean();

		// Method string
		final Getter<TestBean, String> stringGetter = factory.buildGetter(stringGetterMethod);
		testBeanOne.setString("StringNewValue");
		assertEquals("StringNewValue", stringGetter.getValue(testBeanOne));

		// Method Integer
		final Getter<TestBean, Integer> integerGetter = factory.buildGetter(integerGetterMethod);
		testBeanOne.setInteger(124);
		assertEquals(124, integerGetter.getValue(testBeanOne).intValue());

		// Method int
		final Getter<TestBean, Integer> intPrimitiveGetterMethodGetter = factory.buildGetter(intPrimitiveGetterMethod);
		testBeanOne.setIntPrimitive(87654321);
		assertEquals(87654321, intPrimitiveGetterMethodGetter.getValue(testBeanOne).intValue());

	}

	@Test(expected = RuntimeException.class)
	public void testGetterField() {
		final TestBean testBeanOne = new TestBean();
		final Getter<TestBean, Long> longPrimitiveGetter = factory.buildGetter(publicLongPrimitiveField);
		testBeanOne.publicLongPrimitive = 123456;
		longPrimitiveGetter.getValue(testBeanOne);
	}

	@Test(expected = RuntimeException.class)
	public void testGetterPrivate() {
		final TestBean testBeanOne = new TestBean();

		final Getter<TestBean, Integer> integerPrivateGetter = factory.buildGetter(integerPrivateGetterMethod);
		testBeanOne.setInteger(124);
		assertEquals(124, integerPrivateGetter.getValue(testBeanOne).intValue());

	}

	@Test(expected = RuntimeException.class)
	public void testGetterPrivateField() {
		final TestBean testBeanOne = new TestBean();
		final Getter<TestBean, String> privateStringGetter = factory.buildGetter(privateStringField);
		privateStringGetter.getValue(testBeanOne);
	}

	@Test(expected = RuntimeException.class)
	public void testGetterPublicField() {
		final TestBean testBeanOne = new TestBean();
		final Getter<TestBean, Long> longGetter = factory.buildGetter(publicLongField);
		testBeanOne.publicLong = 123456l;
		assertEquals(123456l, longGetter.getValue(testBeanOne).longValue());
	}

	@Test
	public void testSetter() {
		final TestBean testBeanOne = new TestBean();

		// Method string
		final Setter<TestBean, String> stringSetter = factory.buildSetter(stringSetterMethod);
		stringSetter.setValue(testBeanOne, "StringNewValue");
		assertEquals("StringNewValue", testBeanOne.getString());

		// Method Integer
		final Setter<TestBean, Integer> integerSetter = factory.buildSetter(integerSetterMethod);
		integerSetter.setValue(testBeanOne, 124);
		assertEquals(124, testBeanOne.getInteger().intValue());

		// Method int
		final Setter<TestBean, Integer> intPrimitiveSetterMethodGetter = factory.buildSetter(intPrimitiveSetterMethod);
		intPrimitiveSetterMethodGetter.setValue(testBeanOne, 87654321);
		assertEquals(87654321, testBeanOne.getIntPrimitive());

	}
}
