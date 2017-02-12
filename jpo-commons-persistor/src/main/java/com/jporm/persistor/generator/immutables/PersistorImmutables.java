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
package com.jporm.persistor.generator.immutables;

import java.util.List;
import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.PersistorBean;
import com.jporm.persistor.generator.PropertyPersistor;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.io.ResultEntry;

/**
 * A persistor implementation based on reflection
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorImmutables<BEAN, BEAN_BUILDER> extends PersistorBean<BEAN> {

	private final PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> persistorImmutablesBuilder;

	public PersistorImmutables(final ClassDescriptor<BEAN> classMap, final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors,
			final VersionManipulator<BEAN> versionManipulator, final GeneratorManipulator<BEAN> generatorManipulator,
			PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> persistorImmutablesBuilder)
					throws SecurityException, IllegalArgumentException {
		super(classMap, propertyPersistors, versionManipulator, generatorManipulator);
		this.persistorImmutablesBuilder = persistorImmutablesBuilder;
	}

	@Override
	public BEAN beanFromResultSet(final ResultEntry rs, final List<String> fieldsToIgnore) {
		final BEAN_BUILDER builder = persistorImmutablesBuilder.beanFromResultSet(rs, fieldsToIgnore);
		return persistorImmutablesBuilder.build(builder);
	}

	@Override
	public BEAN clone(final BEAN entity) {
		return entity;
	}

}
