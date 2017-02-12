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

import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.PersistorGenerator;
import com.jporm.persistor.generator.PersistorGeneratorAbstract;
import com.jporm.persistor.generator.PropertyPersistor;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.TypeConverterFactory;

/**
 * A {@link PersistorGenerator} for Immutables Builders (See <a href="https://immutables.github.io/">https://immutables.github.io/</a>).
 * For internal use only.
 *
 * @author Francesco Cina
 *
 */
class PersistorGeneratorImmutablesBuilder<BEAN> extends PersistorGeneratorAbstract {

	private final Class<BEAN> beanClass;

	PersistorGeneratorImmutablesBuilder(final Class<BEAN> beanClass) {
		this.beanClass = beanClass;
	}

	public <BEAN_BUILDER> PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> generateBuilder(ClassDescriptor<BEAN_BUILDER> classDescriptor, TypeConverterFactory typeFactory) throws Exception {
		return (PersistorImmutablesBuilder<BEAN, BEAN_BUILDER>) super.generate(classDescriptor, typeFactory);
	}

	@Override
	protected <BEAN_BUILDER> PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> generate(ClassDescriptor<BEAN_BUILDER> classDescriptor, Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistorMap,
			VersionManipulator<BEAN_BUILDER> versionManipulator, GeneratorManipulator<BEAN_BUILDER> generatorManipulator,
			final TypeConverterFactory typeFactory) throws Exception {
		return new PersistorImmutablesBuilder<>(beanClass, classDescriptor, propertyPersistorMap, versionManipulator, generatorManipulator);
	}

	@Override
	public <BEAN_BUILDER> boolean applicableFor(Class<BEAN_BUILDER> beanClass) {
		return false;
	}

}
