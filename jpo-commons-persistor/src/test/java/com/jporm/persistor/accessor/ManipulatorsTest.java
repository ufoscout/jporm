/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class ManipulatorsTest extends BaseTestApi {

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
	private Method integerPrivateSetterMethod;

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
		integerPrivateSetterMethod = TestBean.class.getDeclaredMethod("setIntegerPrivate", Integer.class);
	}

	@Test
	public void testMethodHandler() {
		final AccessorFactory factory = new MethodHandlerAccessorFactory();

		testPrivateField(factory);
		testPrivateMethod(factory);
		testPublicField(factory);
		testPublicMethod(factory);
		testPublicMethodAutoboxing(factory);
		testPublicFieldPrimitive(factory);
		testPublicMethodPrimitive(factory);
	}

	private void testPrivateField(final AccessorFactory accessorFactory) {

		final Getter<TestBean, String> privateStringFieldGetter = accessorFactory.buildGetter(privateStringField);
		final Setter<TestBean, String> privateStringFieldSetter = accessorFactory.buildSetter(privateStringField);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals("privateValue", privateStringFieldGetter.getValue(testBean));
		assertEquals(testBean, privateStringFieldSetter.setValue(testBean, "privateValueNew"));;
		assertEquals("privateValueNew", privateStringFieldGetter.getValue(testBean));
		assertEquals(testBean, privateStringFieldSetter.setValue(testBean, null));
		assertNull(privateStringFieldGetter.getValue(testBean));
	}

	private void testPrivateMethod(final AccessorFactory accessorFactory) {
		final Getter<TestBean, Integer> fieldGetter = accessorFactory.buildGetter(integerPrivateGetterMethod);
		final Setter<TestBean, Integer> fieldSetter = accessorFactory.buildSetter(integerPrivateSetterMethod);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals(12, testBean.getInteger().intValue());
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getInteger().intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, null));
		assertNull(fieldGetter.getValue(testBean));
	}

	private void testPublicField(final AccessorFactory accessorFactory) {
		final Getter<TestBean, Long> fieldGetter = accessorFactory.buildGetter(publicLongField);
		final Setter<TestBean, Long> fieldSetter = accessorFactory.buildSetter(publicLongField);
		final TestBean testBean = new TestBean();
		testBean.publicLong = 12l;
		assertEquals(12l, fieldGetter.getValue(testBean).longValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14l));
		assertEquals(14l, testBean.publicLong.longValue());
	}

	private void testPublicFieldPrimitive(final AccessorFactory accessorFactory) {
		final Getter<TestBean, Long> fieldGetter = accessorFactory.buildGetter(publicLongPrimitiveField);
		final Setter<TestBean, Long> fieldSetter = accessorFactory.buildSetter(publicLongPrimitiveField);
		final TestBean testBean = new TestBean();
		testBean.publicLongPrimitive = 12l;
		assertEquals(12l, fieldGetter.getValue(testBean).longValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14l));
		assertEquals(14l, testBean.publicLongPrimitive);
	}

	private void testPublicMethod(final AccessorFactory accessorFactory) {
		final Getter<TestBean, String> fieldGetter = accessorFactory.buildGetter(stringGetterMethod);
		final Setter<TestBean, String> fieldSetter = accessorFactory.buildSetter(stringSetterMethod);
		final TestBean testBean = new TestBean();
		testBean.setString("stringValue");
		assertEquals("stringValue", fieldGetter.getValue(testBean));
		assertEquals(testBean, fieldSetter.setValue(testBean, "stringValueNew"));
		assertEquals("stringValueNew", testBean.getString());
	}

	private void testPublicMethodAutoboxing(final AccessorFactory accessorFactory) {
		final Getter<TestBean, Integer> fieldGetter = accessorFactory.buildGetter(integerGetterMethod);
		final Setter<TestBean, Integer> fieldSetter = accessorFactory.buildSetter(integerSetterMethod);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals(12, testBean.getInteger().intValue());
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getInteger().intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, null));
		assertNull(fieldGetter.getValue(testBean));
	}

	private void testPublicMethodPrimitive(final AccessorFactory accessorFactory) {
		final Getter<TestBean, Integer> fieldGetter = accessorFactory.buildGetter(intPrimitiveGetterMethod);
		final Setter<TestBean, Integer> fieldSetter = accessorFactory.buildSetter(intPrimitiveSetterMethod);
		final TestBean testBean = new TestBean();
		testBean.setIntPrimitive(12);
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getIntPrimitive());
	}

}
