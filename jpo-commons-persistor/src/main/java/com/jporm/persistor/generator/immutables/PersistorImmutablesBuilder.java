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

import java.lang.reflect.Method;
import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.NoOpsValueProcessor;
import com.jporm.persistor.accessor.BeanAccessorFactory;
import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.generator.PersistorBean;
import com.jporm.persistor.generator.PropertyPersistor;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;

/**
 * A persistor implementation based on reflection
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorImmutablesBuilder<BEAN, BEAN_BUILDER> extends PersistorBean<BEAN_BUILDER> {

	private final Class<BEAN> beanClass;
	private final Getter<BEAN, BEAN_BUILDER, BEAN_BUILDER> staticBuilderMethod;
	private final Getter<BEAN_BUILDER, BEAN, BEAN> buildMethod;

	public PersistorImmutablesBuilder(final Class<BEAN> beanClass, final ClassDescriptor<BEAN_BUILDER> classMap, final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistors,
			final VersionManipulator<BEAN_BUILDER> versionManipulator, final GeneratorManipulator<BEAN_BUILDER> generatorManipulator)
					throws SecurityException, IllegalArgumentException {
		super(classMap, propertyPersistors, versionManipulator, generatorManipulator);
		this.beanClass = beanClass;
		this.staticBuilderMethod = getStaticBuilderMethod();
		this.buildMethod = getBuildMethod();
	}

	@Override
	protected BEAN_BUILDER newInstance() {
		return staticBuilderMethod.getValue(null);
	}

	public BEAN build(BEAN_BUILDER builder) {
		return buildMethod.getValue(builder);
	}

	private Getter<BEAN, BEAN_BUILDER, BEAN_BUILDER> getStaticBuilderMethod() {
		try {
			final Method method = beanClass.getMethod(PersistorGeneratorImmutables.STATIC_BUILDER_METHOD_NAME);
			return BeanAccessorFactory.buildGetter(method, new NoOpsValueProcessor<>());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private Getter<BEAN_BUILDER, BEAN, BEAN> getBuildMethod() {
		try {
			final Method method = classMap.getMappedClass().getMethod(PersistorGeneratorImmutables.BUILDER_BUILD_METHOD_NAME);
			return BeanAccessorFactory.buildGetter(method, new NoOpsValueProcessor<>());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
