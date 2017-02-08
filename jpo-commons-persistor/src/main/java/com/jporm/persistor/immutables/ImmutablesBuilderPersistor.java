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

import java.lang.reflect.Method;
import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.PersistorImpl;
import com.jporm.persistor.PropertyPersistor;
import com.jporm.persistor.generator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;

/**
 * A persistor implementation based on reflection
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class ImmutablesBuilderPersistor<BEAN, BEAN_BUILDER> extends PersistorImpl<BEAN_BUILDER> {

	private final Class<BEAN> beanClass;

	public ImmutablesBuilderPersistor(final Class<BEAN> beanClass, final ClassDescriptor<BEAN_BUILDER> classMap, final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistors,
			final VersionManipulator<BEAN_BUILDER> versionManipulator, final GeneratorManipulator<BEAN_BUILDER> generatorManipulator)
					throws SecurityException, IllegalArgumentException {
		super(classMap, propertyPersistors, versionManipulator, generatorManipulator);
		this.beanClass = beanClass;
	}

	@Override
	protected BEAN_BUILDER newInstance() {
		try {
			final Method method = beanClass.getMethod("builder");
			return (BEAN_BUILDER) method.invoke(null);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
