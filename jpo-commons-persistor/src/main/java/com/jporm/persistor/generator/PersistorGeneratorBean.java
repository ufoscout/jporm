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

import java.util.Map;

import com.jporm.annotation.mapper.ReflectionUtils;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.TypeConverterFactory;

/**
 *
 * A {@link PersistorGenerator} that generates {@link Persistor}s for plain Java beans.
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorGeneratorBean extends PersistorGeneratorAbstract {

	@Override
	protected <BEAN> Persistor<BEAN> generate(ClassDescriptor<BEAN> classDescriptor,
			Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap,
			VersionManipulator<BEAN> versionManipulator,
			GeneratorManipulator<BEAN> generatorManipulator,
			final TypeConverterFactory typeFactory) throws Exception {
		return new PersistorBean<>(classDescriptor, propertyPersistorMap, versionManipulator, generatorManipulator);
	}

	@Override
	public <BEAN> boolean applicableFor(Class<BEAN> beanClass) {
		return ReflectionUtils.getDefaultConstructor(beanClass).isPresent();
	};

}
