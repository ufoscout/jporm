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
package com.jporm.persistor.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.commons.json.jackson2.Jackson2JsonService;
import com.jporm.persistor.BaseTestApi;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

/**
 *
 * @author ufo
 *
 */
public class SampleDomainObjectWithJsonTest extends BaseTestApi {

	@Test
	public void testCloneBeanWithJsonField() throws Exception {
		final ClassDescriptor<SampleDomainObjectWithJson> classDBMap = new ClassDescriptorBuilderImpl<>(SampleDomainObjectWithJson.class).build();
		final Persistor<SampleDomainObjectWithJson> persistor = new PersistorGeneratorBean().generate(classDBMap, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final SampleDomainObjectWithJson entity = new SampleDomainObjectWithJson();
		entity.setJsonEmployee(new Employee());
		entity.setOptionalJsonEmployee(Optional.of(new Employee()));

		final SampleDomainObjectWithJson clonedEntity = persistor.clone(entity);

		assertSame(entity.getJsonEmployee(), clonedEntity.getJsonEmployee());
		assertNotSame(entity.getOptionalJsonEmployee().get(), clonedEntity.getOptionalJsonEmployee().get());
	}

	@Test
	public void testCreateBeanFromResultSet() throws Exception {
		final ClassDescriptor<SampleDomainObjectWithJson> classDBMap = new ClassDescriptorBuilderImpl<>(SampleDomainObjectWithJson.class).build();
		final Persistor<SampleDomainObjectWithJson> persistor = new PersistorGeneratorBean().generate(classDBMap, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final ResultEntry rs = Mockito.mock(ResultEntry.class);
		Mockito.when(rs.getString(Mockito.anyString())).thenReturn("{\"id\":12345}");

		final SampleDomainObjectWithJson entity = persistor.beanFromResultSet(rs, Collections.emptyList());
		assertEquals(12345, entity.getJsonEmployee().getId().intValue());
		assertEquals(12345, entity.getOptionalJsonEmployee().get().getId().intValue());
	}

	@Test
	public void testAccessJDBC() throws Exception {
		final ClassDescriptor<SampleDomainObjectWithJson> classDBMap = new ClassDescriptorBuilderImpl<>(SampleDomainObjectWithJson.class).build();
		final Persistor<SampleDomainObjectWithJson> persistor = new PersistorGeneratorBean().generate(classDBMap, new TypeConverterFactory(() -> new Jackson2JsonService()));

		final SampleDomainObjectWithJson entity = new SampleDomainObjectWithJson();
		entity.setJsonEmployee(new Employee());
		entity.getJsonEmployee().setId(12345);

		final Statement statement = Mockito.mock(Statement.class);
		final ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);

		persistor.setBeanValuesToStatement(new String[]{"jsonEmployee"}, entity, statement);

		Mockito.verify(statement, Mockito.times(1)).setString(Mockito.anyInt(), jsonCaptor.capture());

		final String jsonString = new Jackson2JsonService().toJson(entity.getJsonEmployee());

		assertEquals(jsonString, jsonCaptor.getValue());

	}
}
