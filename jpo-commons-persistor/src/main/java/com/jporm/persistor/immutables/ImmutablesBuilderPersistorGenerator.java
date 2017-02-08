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
package com.jporm.persistor.immutables;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.persistor.PersistorGenerator;
import com.jporm.persistor.PropertyPersistor;
import com.jporm.persistor.PropertyPersistorImpl;
import com.jporm.persistor.accessor.BeanPropertyAccessorFactory;
import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.accessor.Setter;
import com.jporm.persistor.generator.GeneratorManipulator;
import com.jporm.persistor.generator.GeneratorManipulatorImpl;
import com.jporm.persistor.generator.NullGeneratorManipulator;
import com.jporm.persistor.version.NullVersionManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.persistor.version.VersionManipulatorImpl;
import com.jporm.persistor.version.VersionMath;
import com.jporm.persistor.version.VersionMathFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;

/**
 * A {@link PersistorGenerator} for Immutables objects (See <a href="https://immutables.github.io/">https://immutables.github.io/</a>).
 *
 * @author Francesco Cina
 *
 */
public class ImmutablesBuilderPersistorGenerator<BEAN, BEAN_BUILDER> implements PersistorGenerator<BEAN_BUILDER>{


	private final String LOTS_OF_DUPLICATED_CODE_TO_BE_REFACTORED = "";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final BeanPropertyAccessorFactory accessorFactory = new BeanPropertyAccessorFactory();
	private final ClassDescriptor<BEAN_BUILDER> classMap;
	private final TypeConverterFactory typeFactory;
	private final Class<BEAN> beanClass;

	public ImmutablesBuilderPersistorGenerator(final Class<BEAN> beanClass, final ClassDescriptor<BEAN_BUILDER> classMap, final TypeConverterFactory typeFactory) {
		this.beanClass = beanClass;
		this.classMap = classMap;
		this.typeFactory = typeFactory;
	}

	@SuppressWarnings("unchecked")
	private <P> GeneratorManipulator<BEAN_BUILDER> buildGeneratorManipulator(final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistors)
			throws SecurityException {
		if (this.classMap.getAllGeneratedColumnJavaNames().length > 0) {
			final String columnJavaName = this.classMap.getAllGeneratedColumnJavaNames()[0];
			final PropertyPersistor<BEAN_BUILDER, P, ?> fieldManipulator = (PropertyPersistor<BEAN_BUILDER, P, ?>) propertyPersistors.get(columnJavaName);
			return new GeneratorManipulatorImpl<>(fieldManipulator);
		}
		return new NullGeneratorManipulator<>();
	}

	private <P, DB> Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> buildPropertyPersistorMap() throws SecurityException, IllegalArgumentException {
		final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistors = new HashMap<>();
		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN_BUILDER, P> classField = this.classMap.getFieldDescriptorByJavaName(columnJavaName);
			propertyPersistors.put(columnJavaName, getPropertyPersistor(classField));
		}
		return propertyPersistors;
	}

	private VersionManipulator<BEAN_BUILDER> buildVersionManipulator(final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistorMap) {
		VersionManipulator<BEAN_BUILDER> versionManipulator = new NullVersionManipulator<>();

		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN_BUILDER, ?> classField = this.classMap.getFieldDescriptorByJavaName(columnJavaName);
			if (classField.getVersionInfo().isVersionable()) {
				versionManipulator = new VersionManipulatorImpl<>(propertyPersistorMap.get(classField.getFieldName()));
				break;
			}
		}

		return versionManipulator;
	}

	@Override
	public ImmutablesBuilderPersistor<BEAN, BEAN_BUILDER> generate() throws Exception {
		final Map<String, PropertyPersistor<BEAN_BUILDER, ?, ?>> propertyPersistorMap = this.buildPropertyPersistorMap();
		return new ImmutablesBuilderPersistor<>(beanClass, this.classMap, propertyPersistorMap, this.buildVersionManipulator(propertyPersistorMap),
				buildGeneratorManipulator(propertyPersistorMap));
	}

	private <P> Getter<BEAN_BUILDER, P> getGetManipulator(final FieldDescriptor<BEAN_BUILDER, P> fieldDescriptor) {
		if (fieldDescriptor.getGetter().isPresent()) {
			return accessorFactory.buildGetter(fieldDescriptor.getGetter().get());
		}
		return accessorFactory.buildGetter(fieldDescriptor.getField());
	}

	private <P, DB> PropertyPersistor<BEAN_BUILDER, P, DB> getPropertyPersistor(final FieldDescriptor<BEAN_BUILDER, P> classField) {
		logger.debug("Build PropertyPersistor for field [{}]", classField.getFieldName()); //$NON-NLS-1$
		final VersionMath<P> versionMath = new VersionMathFactory().getMath(classField.getRawType(), classField.getVersionInfo().isVersionable());
		logger.debug("VersionMath type is [{}]", versionMath.getClass());

		final TypeConverterJdbcReady<P, DB> typeWrapper = this.typeFactory.getTypeConverterFromClass(classField.getRawType(), classField.getGenericArgumentType());
		logger.debug("JdbcIO type is [{}]", typeWrapper.getJdbcIO().getClass());
		logger.debug("TypeConverter type is [{}]", typeWrapper.getTypeConverter().getClass());
		return new PropertyPersistorImpl<>(classField.getFieldName(), getGetManipulator(classField), getSetManipulator(classField), typeWrapper,
				versionMath);

	}

	private <P> Setter<BEAN_BUILDER, P> getSetManipulator(final FieldDescriptor<BEAN_BUILDER, P> fieldDescriptor) {
		if (fieldDescriptor.getSetter().isPresent()) {
			return accessorFactory.buildSetterOrWither(fieldDescriptor.getSetter().get());
		}
		return accessorFactory.buildSetter(fieldDescriptor.getField());
	}

}
