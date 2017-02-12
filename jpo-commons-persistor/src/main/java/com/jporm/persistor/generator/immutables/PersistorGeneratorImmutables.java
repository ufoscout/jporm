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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.jporm.annotation.mapper.ReflectionUtils;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorGenerator;
import com.jporm.persistor.generator.PersistorGeneratorAbstract;
import com.jporm.persistor.generator.PropertyPersistor;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.TypeConverterFactory;

/**
 * A {@link PersistorGenerator} for Immutables objects (See <a href="https://immutables.github.io/">https://immutables.github.io/</a>).
 *
 * @author Francesco Cina
 *
 */
public class PersistorGeneratorImmutables extends PersistorGeneratorAbstract {

	static String STATIC_BUILDER_METHOD_NAME = "builder";
	static List<String> BUILDER_FIELDS_TO_BE_IGNORED = Arrays.asList("initBits");
	static String BUILDER_BUILD_METHOD_NAME = "build";

	@Override
	protected <BEAN> Persistor<BEAN> generate(ClassDescriptor<BEAN> classDescriptor, Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap, VersionManipulator<BEAN> versionManipulator,
			GeneratorManipulator<BEAN> generatorManipulator, final TypeConverterFactory typeFactory) throws Exception {
		final Optional<Method> builderMethod = getBuilderMethod(classDescriptor.getMappedClass());
		final PersistorImmutablesBuilder<BEAN, ?> builderPersistor = buildBeanBuilder(classDescriptor.getMappedClass(), builderMethod.get().getReturnType(), typeFactory);
		return new PersistorImmutables<>(classDescriptor, propertyPersistorMap, versionManipulator, generatorManipulator, builderPersistor);
	}

	@Override
	public <BEAN> boolean applicableFor(Class<BEAN> beanClass) {
		final boolean hasDefaultConstructor = ReflectionUtils.getDefaultConstructor(beanClass).isPresent();
		return !hasDefaultConstructor
				&& getBuilderMethod(beanClass).isPresent();
	}

	private <BEAN, BEAN_BUILDER> PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> buildBeanBuilder(Class<BEAN> beanClass, Class<BEAN_BUILDER> builderClass, final TypeConverterFactory typeFactory) throws Exception {
		final ClassDescriptorBuilderImpl<BEAN_BUILDER> builderDescriptor = new ClassDescriptorBuilderImpl<>(builderClass, BUILDER_FIELDS_TO_BE_IGNORED);
		final PersistorGeneratorImmutablesBuilder<BEAN> builderGenerator = new PersistorGeneratorImmutablesBuilder<>(beanClass);
		return builderGenerator.generateBuilder(builderDescriptor.build(), typeFactory);
	}

	private <BEAN> Optional<Method> getBuilderMethod(Class<BEAN> beanClass) {
		return ReflectionUtils.getMethod(beanClass, STATIC_BUILDER_METHOD_NAME)
				.filter(ReflectionUtils::isStatic)
				.filter(method -> ReflectionUtils.getMethod(method.getReturnType(), BUILDER_BUILD_METHOD_NAME).isPresent());
	}

}
