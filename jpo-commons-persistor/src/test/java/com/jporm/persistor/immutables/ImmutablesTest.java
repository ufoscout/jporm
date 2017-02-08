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
package com.jporm.persistor.immutables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.immutables.ImmutableFoobarValue.Builder;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultEntry;

/**
 *
 * @author ufo
 *
 */
public class ImmutablesTest extends BaseTestApi {

	@Test
	public void testIncreaser() {
		final ImmutableFoobarValue foobar = ImmutableFoobarValue.builder().bar("hello").foo(3).build();
		assertNotNull(foobar);
	}

	@Test
	public void builder() throws Exception {
		final ClassDescriptorBuilderImpl<ImmutableFoobarValue.Builder> builderDescriptor = new ClassDescriptorBuilderImpl<>(ImmutableFoobarValue.Builder.class, Arrays.asList("initBits"));
		final ImmutablesBuilderPersistorGenerator<ImmutableFoobarValue, ImmutableFoobarValue.Builder> builderGenerator = new ImmutablesBuilderPersistorGenerator<>(ImmutableFoobarValue.class, builderDescriptor.build(), new TypeConverterFactory());
		final ImmutablesBuilderPersistor<ImmutableFoobarValue, ImmutableFoobarValue.Builder> builder = builderGenerator.generate();

		final ResultEntry rs = Mockito.mock(ResultEntry.class);
		Mockito.when(rs.getString("bar")).thenReturn("barValue");
		Mockito.when(rs.getString("name")).thenReturn("nameValue");
		Mockito.when(rs.getInt("foo")).thenReturn(111);

		final Builder beanBuilder = builder.beanFromResultSet(rs, new ArrayList<>());
		assertNotNull(beanBuilder);

		final ImmutableFoobarValue value = beanBuilder.build();
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
}
