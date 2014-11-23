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
package com.jporm.core.persistor.reflection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.persistor.PropertyPersistorImpl;
import com.jporm.core.persistor.generator.GeneratorManipulator;
import com.jporm.core.persistor.generator.GeneratorManipulatorImpl;
import com.jporm.core.persistor.version.NullVersionMath;
import com.jporm.deprecated.core.mapper.clazz.ClassFieldImpl;
import com.jporm.types.TypeFactory;
import com.jporm.types.TypeWrapperJdbcReady;

/**
 *
 * @author Francesco Cina'
 *
 * Apr 1, 2012
 */
public class ReflectionGeneratorManipulatorTest<P, DB> extends BaseTestApi {

	private PropertyPersistorImpl<MockBeanInteger, Integer, DB> manipulator;
	private ClassFieldImpl<MockBeanInteger, Integer> classField;
	private MockBeanInteger entity;

	@Before
	public void setUp() throws SecurityException, NoSuchMethodException {
		this.entity = new MockBeanInteger();
		this.classField = new ClassFieldImpl<MockBeanInteger, Integer>(Integer.class, ""); //$NON-NLS-1$
		this.classField.setGetManipulator( new GetterGetManipulator<MockBeanInteger, Integer>(this.entity.get) );
		this.classField.setSetManipulator( new SetterSetManipulator<MockBeanInteger, Integer>(this.entity.set) );

		TypeFactory typeFactory = new TypeFactory();
		TypeWrapperJdbcReady<Integer, DB> typeWrapper = typeFactory.getTypeWrapper(this.classField.getType());
		this.manipulator = new PropertyPersistorImpl<MockBeanInteger, Integer, DB>(typeWrapper, this.classField, new NullVersionMath<Integer>());
	}

	@Test
	public void testManipulator1() throws Exception {
		final GeneratorManipulator<MockBeanInteger> genMap = new GeneratorManipulatorImpl<MockBeanInteger, Integer>(this.manipulator);
		assertTrue( genMap.hasGenerator() );
		assertTrue( genMap.useGenerator(this.entity) );
	}


	@Test
	public void testManipulator2() throws Exception {
		final GeneratorManipulator<MockBeanInteger> genMap = new GeneratorManipulatorImpl<MockBeanInteger, Integer>(this.manipulator);
		assertTrue( genMap.hasGenerator() );
		assertTrue( genMap.useGenerator(this.entity) );
	}

	@Test
	public void testManipulator3() throws Exception {
		final MockBeanInteger localEntity = new MockBeanInteger();
		localEntity.setValue(10);

		final GeneratorManipulator<MockBeanInteger> genMap = new GeneratorManipulatorImpl<MockBeanInteger, Integer>(this.manipulator);
		assertTrue( genMap.hasGenerator() );
		assertFalse( genMap.useGenerator(localEntity) );
	}

	public class MockBeanInteger {
		private Integer value;
		Field valueField;
		Method get;
		Method set;

		MockBeanInteger() throws SecurityException, NoSuchMethodException {
			for (final Field field : this.getClass().getDeclaredFields()) {
				if (field.getName().equals("value")) { //$NON-NLS-1$
					this.valueField = field;
				}
			}
			this.get = this.getClass().getMethod("getValue"); //$NON-NLS-1$
			this.set = this.getClass().getMethod("setValue", Integer.class); //$NON-NLS-1$
		}
		public Integer getValue() {
			return this.value;
		}

		public void setValue(final Integer value) {
			this.value = value;
		}
	}
}
