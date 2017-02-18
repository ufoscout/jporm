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
package com.jporm.persistor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorBean;
import com.jporm.persistor.generator.immutables.ImmutableFoobarValue;
import com.jporm.persistor.generator.immutables.PersistorImmutables;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.TypeConverterFactory;

public class PersistorFactoryTest extends BaseTestApi {

	private final PersistorFactory factory = new PersistorFactory(new TypeConverterFactory());

	@Test
	public void should_use_immutables_generator() throws Exception {
		final Persistor<ImmutableFoobarValue> persistor = factory.generate(new ClassDescriptorBuilderImpl<>(ImmutableFoobarValue.class).build());
		assertTrue(persistor instanceof PersistorImmutables);
	}

	@Test
	public void should_use_bean_generator() throws Exception {
		final Persistor<Employee> persistor = factory.generate(new ClassDescriptorBuilderImpl<>(Employee.class).build());
		assertTrue(persistor instanceof PersistorBean);
	}

}
