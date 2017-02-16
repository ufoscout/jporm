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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.persistor.accessor.BeanAccessorFactory;
import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.accessor.Setter;
import com.jporm.persistor.generator.manipulator.GeneratorManipulator;
import com.jporm.persistor.generator.manipulator.GeneratorManipulatorImpl;
import com.jporm.persistor.generator.manipulator.NullGeneratorManipulator;
import com.jporm.persistor.version.NullVersionManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.persistor.version.VersionManipulatorImpl;
import com.jporm.persistor.version.VersionMath;
import com.jporm.persistor.version.VersionMathFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;

/**
 * @author Francesco Cina' Mar 24, 2012
 */
public abstract class PersistorGeneratorAbstract implements PersistorGenerator {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	private <BEAN, P> GeneratorManipulator<BEAN> buildGeneratorManipulator(final ClassDescriptor<BEAN> classMap, final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors)
			throws SecurityException {
		if (classMap.getAllGeneratedColumnJavaNames().length > 0) {
			final String columnJavaName = classMap.getAllGeneratedColumnJavaNames()[0];
			final PropertyPersistor<BEAN, P, ?> fieldManipulator = (PropertyPersistor<BEAN, P, ?>) propertyPersistors.get(columnJavaName);
			return new GeneratorManipulatorImpl<>(fieldManipulator);
		}
		return new NullGeneratorManipulator<>();
	}

	private <BEAN, R, P, DB> Map<String, PropertyPersistor<BEAN, ?, ?>> buildPropertyPersistorMap(final TypeConverterFactory typeFactory, final ClassDescriptor<BEAN> classMap) throws SecurityException, IllegalArgumentException {
		final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors = new HashMap<>();
		for (final String columnJavaName : classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN, R, P> classField = classMap.getFieldDescriptorByJavaName(columnJavaName);
			propertyPersistors.put(columnJavaName, getPropertyPersistor(typeFactory, classField));
		}
		return propertyPersistors;
	}

	private <BEAN> VersionManipulator<BEAN> buildVersionManipulator(final ClassDescriptor<BEAN> classMap, final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap) {
		VersionManipulator<BEAN> versionManipulator = new NullVersionManipulator<>();

		for (final String columnJavaName : classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN, ?, ?> classField = classMap.getFieldDescriptorByJavaName(columnJavaName);
			if (classField.getVersionInfo().isVersionable()) {
				versionManipulator = new VersionManipulatorImpl<>(propertyPersistorMap.get(classField.getFieldName()));
				break;
			}
		}

		return versionManipulator;
	}

	@Override
	public <BEAN> Persistor<BEAN> generate(final ClassDescriptor<BEAN> classDescriptor, final TypeConverterFactory typeFactory) throws Exception {
		logger.debug("Generate persistor for Class [{}]", classDescriptor.getMappedClass());
		final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap = this.buildPropertyPersistorMap(typeFactory, classDescriptor);
		return generate(classDescriptor, propertyPersistorMap, this.buildVersionManipulator(classDescriptor, propertyPersistorMap),
				buildGeneratorManipulator(classDescriptor, propertyPersistorMap), typeFactory);
	}

	protected abstract <BEAN> Persistor<BEAN> generate(ClassDescriptor<BEAN> classDescriptor,
			Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap,
			VersionManipulator<BEAN> versionManipulator,
			GeneratorManipulator<BEAN> generatorManipulator,
			final TypeConverterFactory typeFactory) throws Exception;

	private <BEAN, R, P> Getter<BEAN, P> getGetManipulator(final FieldDescriptor<BEAN, R, P> fieldDescriptor) {
		if (fieldDescriptor.getGetter().isPresent()) {
			return BeanAccessorFactory.buildGetter(fieldDescriptor.getGetter().get());
		}
		return BeanAccessorFactory.buildGetter(fieldDescriptor.getField());
	}

	private <BEAN, R, P, DB> PropertyPersistor<BEAN, P, DB> getPropertyPersistor(final TypeConverterFactory typeFactory, final FieldDescriptor<BEAN, R, P> classField) {
		logger.debug("Build PropertyPersistor for field [{}]", classField.getFieldName()); //$NON-NLS-1$
		final VersionMath<P> versionMath = new VersionMathFactory().getMath(classField.getProcessedClass(), classField.getVersionInfo().isVersionable());
		logger.debug("VersionMath type is [{}]", versionMath.getClass());

		final TypeConverterJdbcReady<P, DB> typeWrapper = typeFactory.getTypeConverter(classField.getProcessedClass());
		logger.debug("JdbcIO type is [{}]", typeWrapper.getJdbcIO().getClass());
		logger.debug("TypeConverter type is [{}]", typeWrapper.getTypeConverter().getClass());
		return new PropertyPersistorImpl<>(classField.getFieldName(), getGetManipulator(classField), getSetManipulator(classField), typeWrapper,
				versionMath);

	}

	private <BEAN, R, P> Setter<BEAN, P> getSetManipulator(final FieldDescriptor<BEAN, R, P> fieldDescriptor) {
		if (fieldDescriptor.getSetter().isPresent()) {
			return BeanAccessorFactory.buildSetterOrWither(fieldDescriptor.getSetter().get());
		}
		return BeanAccessorFactory.buildSetter(fieldDescriptor.getField());
	}

}
