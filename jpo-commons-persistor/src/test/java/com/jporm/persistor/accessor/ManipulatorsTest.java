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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.annotation.mapper.clazz.NoOpsValueProcessor;
import com.jporm.annotation.mapper.clazz.OptionalValueProcessor;
import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class ManipulatorsTest extends BaseTestApi {

	@SuppressWarnings("rawtypes")
	private final ValueProcessor valueProcessor = NoOpsValueProcessor.build();

	private final AccessorFactory accessorFactory = new MethodHandlerAccessorFactory();

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

	private Method optionalNicknameGetterMethod;
	private Method optionalNicknameSetterMethod;

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

		optionalNicknameGetterMethod = TestBean.class.getDeclaredMethod("getNickname");
		optionalNicknameSetterMethod = TestBean.class.getDeclaredMethod("setNickname", Optional.class);
	}

	@Test
	public void testPrivateField() {

		final Getter<TestBean, String, String> privateStringFieldGetter = accessorFactory.buildGetter(privateStringField, valueProcessor);
		final Setter<TestBean, String, String> privateStringFieldSetter = accessorFactory.buildSetter(privateStringField, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals("privateValue", privateStringFieldGetter.getValue(testBean));
		assertEquals(testBean, privateStringFieldSetter.setValue(testBean, "privateValueNew"));;
		assertEquals("privateValueNew", privateStringFieldGetter.getValue(testBean));
		assertEquals(testBean, privateStringFieldSetter.setValue(testBean, null));
		assertNull(privateStringFieldGetter.getValue(testBean));
	}

	@Test
	public void testPrivateMethod() {
		final Getter<TestBean, Integer, Integer> fieldGetter = accessorFactory.buildGetter(integerPrivateGetterMethod, valueProcessor);
		final Setter<TestBean, Integer, Integer> fieldSetter = accessorFactory.buildSetter(integerPrivateSetterMethod, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals(12, testBean.getInteger().intValue());
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getInteger().intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, null));
		assertNull(fieldGetter.getValue(testBean));
	}

	@Test
	public void testPublicField() {
		final Getter<TestBean, Long, Long> fieldGetter = accessorFactory.buildGetter(publicLongField, valueProcessor);
		final Setter<TestBean, Long, Long> fieldSetter = accessorFactory.buildSetter(publicLongField, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.publicLong = 12l;
		assertEquals(12l, fieldGetter.getValue(testBean).longValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14l));
		assertEquals(14l, testBean.publicLong.longValue());
	}

	@Test
	public void testPublicFieldPrimitive() {
		final Getter<TestBean, Long, Long> fieldGetter = accessorFactory.buildGetter(publicLongPrimitiveField, valueProcessor);
		final Setter<TestBean, Long, Long> fieldSetter = accessorFactory.buildSetter(publicLongPrimitiveField, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.publicLongPrimitive = 12l;
		assertEquals(12l, fieldGetter.getValue(testBean).longValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14l));
		assertEquals(14l, testBean.publicLongPrimitive);
	}

	@Test
	public void testPublicMethod() {
		final Getter<TestBean, String, String> fieldGetter = accessorFactory.buildGetter(stringGetterMethod, valueProcessor);
		final Setter<TestBean, String, String> fieldSetter = accessorFactory.buildSetter(stringSetterMethod, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.setString("stringValue");
		assertEquals("stringValue", fieldGetter.getValue(testBean));
		assertEquals(testBean, fieldSetter.setValue(testBean, "stringValueNew"));
		assertEquals("stringValueNew", testBean.getString());
	}

	@Test
	public void testPublicMethodAutoboxing() {
		final Getter<TestBean, Integer, Integer> fieldGetter = accessorFactory.buildGetter(integerGetterMethod, valueProcessor);
		final Setter<TestBean, Integer, Integer> fieldSetter = accessorFactory.buildSetter(integerSetterMethod, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.setInteger(12);
		assertEquals(12, testBean.getInteger().intValue());
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getInteger().intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, null));
		assertNull(fieldGetter.getValue(testBean));
	}

	@Test
	public void testPublicMethodPrimitive() {
		final Getter<TestBean, Integer, Integer> fieldGetter = accessorFactory.buildGetter(intPrimitiveGetterMethod, valueProcessor);
		final Setter<TestBean, Integer, Integer> fieldSetter = accessorFactory.buildSetter(intPrimitiveSetterMethod, valueProcessor);
		final TestBean testBean = new TestBean();
		testBean.setIntPrimitive(12);
		assertEquals(12, fieldGetter.getValue(testBean).intValue());
		assertEquals(testBean, fieldSetter.setValue(testBean, 14));
		assertEquals(14, testBean.getIntPrimitive());
	}

	@Test
	public void testOptionalMethod() {
		final Getter<TestBean, Optional<String>, String> fieldGetter = accessorFactory.buildGetter(optionalNicknameGetterMethod, OptionalValueProcessor.build());
		final Setter<TestBean, Optional<String>, String> fieldSetter = accessorFactory.buildSetter(optionalNicknameSetterMethod, OptionalValueProcessor.build());
		final TestBean testBean = new TestBean();

		testBean.setNickname(null);
		assertNull(fieldGetter.getValue(testBean));

		testBean.setNickname(Optional.empty());
		assertNull(fieldGetter.getValue(testBean));

		final String nickName = UUID.randomUUID().toString();
		testBean.setNickname(Optional.of(nickName));
		assertEquals( nickName, fieldGetter.getValue(testBean));

		final String newNickName = UUID.randomUUID().toString();
		assertEquals(testBean, fieldSetter.setValue(testBean, newNickName));
		assertEquals(newNickName, testBean.getNickname().get());

		assertEquals(testBean, fieldSetter.setValue(testBean, null));
		assertFalse(testBean.getNickname().isPresent());
	}

}
