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
package com.jporm.persistor.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.annotation.mapper.clazz.NoOpsValueProcessor;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerGetter;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerSetter;
import com.jporm.persistor.generator.PropertyPersistorImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;

public class PropertyPersistorCloneTest extends BaseTestApi {

	public class MockBean {
		private int value = 10;
		public boolean getCalled = false;
		public boolean setCalled = false;

		public int getValue() {
			getCalled = true;
			return value;
		}

		public void setValue(final int value) {
			setCalled = true;
			this.value = value;
		}

	}

	private final String fieldName = "value";
	private Method setterMethod;

	private Method getterMethod;

	@Before
	public void setUp() throws Exception {
		setterMethod = MockBean.class.getMethod("setValue", Integer.TYPE); //$NON-NLS-1$
		assertNotNull(setterMethod);

		getterMethod = MockBean.class.getMethod("getValue"); //$NON-NLS-1$
		assertNotNull(getterMethod);

	}

	@Test
	public void testCloneProperty() throws Exception {
		final MockBean source = new MockBean();

		final Getter<MockBean, Integer, Integer> getter = new MethodHandlerGetter<>(getterMethod, new NoOpsValueProcessor<>());
		final Setter<MockBean, Integer, Integer> setter = new MethodHandlerSetter<>(setterMethod, new NoOpsValueProcessor<>());
		final TypeConverterJdbcReady<Integer, Integer> typeWrapper = new TypeConverterFactory().getTypeConverter(Integer.class);
		final PropertyPersistorImpl<MockBean, Integer, Integer> pp = new PropertyPersistorImpl<>(fieldName, getter, setter, typeWrapper,
				null);

		MockBean destination = new MockBean();
		source.setValue(new Random().nextInt());

		destination = pp.clonePropertyValue(source, destination);
		assertEquals(source.getValue(), destination.getValue());

	}

}
