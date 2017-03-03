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
package com.jporm.persistor.generator.immutables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.commons.json.jackson2.Jackson2JsonService;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorGenerator;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultEntry;

public class PersistorGeneratorImmutablesTest extends BaseTestApi {

	private final PersistorGenerator immutablesGenerator = new PersistorGeneratorImmutables();

	@Test
	public void should_be_applicable_for_immutables() {
		assertTrue(immutablesGenerator.applicableFor(ImmutableFoobarValue.class));
	}

	@Test
	public void should_not_be_applicable_for_beans() {
		assertFalse(immutablesGenerator.applicableFor(Employee.class));
	}

	@Test
	public void should_use_builder_when_reading_the_resultset() throws Exception {
		final ClassDescriptor<ImmutableFoobarValue> classDescriptor = new ClassDescriptorBuilderImpl<>(ImmutableFoobarValue.class).build();
		final Persistor<ImmutableFoobarValue> persistor = immutablesGenerator.generate(classDescriptor, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final ResultEntry rs = Mockito.mock(ResultEntry.class);
		Mockito.when(rs.getString("bar")).thenReturn("barValue");
		Mockito.when(rs.getString("name")).thenReturn("nameValue");
		Mockito.when(rs.getInt("foo")).thenReturn(111);

		final ImmutableFoobarValue value = persistor.beanFromResultSet(rs, new ArrayList<>());
		assertNotNull(value);

		assertEquals("barValue", value.bar());
		assertTrue(value.name().isPresent());
		assertEquals("nameValue", value.name().get());
		assertEquals(111, value.foo());

		Mockito.verify(rs, Mockito.times(1)).getString("bar");
		Mockito.verify(rs, Mockito.times(1)).getString("name");
		Mockito.verify(rs, Mockito.times(1)).getInt("foo");
		Mockito.verify(rs, Mockito.times(0)).getLong("initBits");

	}


	@Test
	public void should_increase_the_bean_version() throws Exception {
		final ClassDescriptor<ImmutableFoobarValue> classDescriptor = new ClassDescriptorBuilderImpl<>(ImmutableFoobarValue.class).build();
		final Persistor<ImmutableFoobarValue> persistor = immutablesGenerator.generate(classDescriptor, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final int version = 1;

		final ImmutableFoobarValue bean = ImmutableFoobarValue.builder()
				.bar("bar")
				.foo(0)
				.version(1)
				.build();

		final ImmutableFoobarValue updatedBean = persistor.increaseVersion(bean, false);
		assertEquals(version + 1, updatedBean.version().get().intValue());

	}

	@Test
	public void should_read_properties() throws Exception {

		final ClassDescriptor<ImmutableFoobarValue> classDescriptor = new ClassDescriptorBuilderImpl<>(ImmutableFoobarValue.class).build();
		final Persistor<ImmutableFoobarValue> persistor = immutablesGenerator.generate(classDescriptor, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final ImmutableFoobarValue foobar = ImmutableFoobarValue.builder()
				.name(UUID.randomUUID().toString())
				.bar(UUID.randomUUID().toString())
				.foo(new Random().nextInt())
				.build();

		assertEquals(foobar.bar(), persistor.getPropertyValues(new String[]{"bar"}, foobar)[0]);
		assertEquals(foobar.name().get(), persistor.getPropertyValues(new String[]{"name"}, foobar)[0]);
		assertEquals(foobar.foo(), persistor.getPropertyValues(new String[]{"foo"}, foobar)[0]);

	}

}
