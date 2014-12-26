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
package com.jporm.persistor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.exception.OrmConfigurationException;
import com.jporm.introspector.mapper.clazz.ClassDescriptor;
import com.jporm.introspector.mapper.clazz.FieldDescriptor;
import com.jporm.persistor.accessor.AccessorFactory;
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
import com.jporm.types.TypeFactory;
import com.jporm.types.TypeWrapperJdbcReady;

/**
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorGeneratorImpl<BEAN> implements PersistorGenerator<BEAN> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final AccessorFactory accessorFactory = new BeanPropertyAccessorFactory();
	private final ClassDescriptor<BEAN> classMap;
	private final TypeFactory typeFactory;

	public PersistorGeneratorImpl(final ClassDescriptor<BEAN> classMap, final TypeFactory typeFactory) {
		this.classMap = classMap;
		this.typeFactory = typeFactory;
	}

	@Override
	public Persistor<BEAN> generate() throws Exception {
		Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap = this.buildPropertyPersistorMap();
		return new PersistorImpl<BEAN>(this.classMap, propertyPersistorMap,
				this.buildVersionManipulator(propertyPersistorMap), buildGeneratorManipulator(propertyPersistorMap));
	}

	private <P, DB> Map<String, PropertyPersistor<BEAN, ?, ?>> buildPropertyPersistorMap() throws SecurityException,
	IllegalArgumentException {
		Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors = new HashMap<String, PropertyPersistor<BEAN, ?, ?>>();
		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN, P> classField = this.classMap.getFieldDescriptorByJavaName(columnJavaName);
			propertyPersistors.put(columnJavaName, getPropertyPersistor(classField));
		}
		return propertyPersistors;
	}

	private VersionManipulator<BEAN> buildVersionManipulator(
			final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap) {
		VersionManipulator<BEAN> versionManipulator = new NullVersionManipulator<BEAN>();

		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final FieldDescriptor<BEAN, ?> classField = this.classMap.getFieldDescriptorByJavaName(columnJavaName);
			if (classField.getVersionInfo().isVersionable()) {
				versionManipulator = new VersionManipulatorImpl<BEAN>(propertyPersistorMap.get(classField.getFieldName()),
						classField.getVersionInfo().getLockMode());
				break;
			}
		}

		return versionManipulator;
	}

	@SuppressWarnings("unchecked")
	private <P> GeneratorManipulator<BEAN> buildGeneratorManipulator(
			final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors) throws OrmConfigurationException,
			SecurityException {
		if (this.classMap.getAllGeneratedColumnJavaNames().length > 0) {
			final String columnJavaName = this.classMap.getAllGeneratedColumnJavaNames()[0];
			final PropertyPersistor<BEAN, P, ?> fieldManipulator = (PropertyPersistor<BEAN, P, ?>) propertyPersistors
					.get(columnJavaName);
			return new GeneratorManipulatorImpl<BEAN, P>(fieldManipulator);
		}
		return new NullGeneratorManipulator<BEAN>();
	}

	private <P, DB> PropertyPersistor<BEAN, P, DB> getPropertyPersistor(
			final FieldDescriptor<BEAN, P> classField) {
		logger.debug("Build PropertyPersistor for field [{}]", classField.getFieldName()); //$NON-NLS-1$
		VersionMath<P> versionMath = new VersionMathFactory().getMath(classField.getType(), classField.getVersionInfo()
				.isVersionable());
		TypeWrapperJdbcReady<P, DB> typeWrapper = this.typeFactory.getTypeWrapper(classField.getType());
		return new PropertyPersistorImpl<BEAN, P, DB>(classField.getFieldName(), getGetManipulator(classField),
				getSetManipulator(classField), typeWrapper, versionMath);

	}


	private <P> Getter<BEAN, P> getGetManipulator(final FieldDescriptor<BEAN, P> fieldDescriptor) {
		if ( fieldDescriptor.getGetter() != null) {
			return accessorFactory.buildGetter(fieldDescriptor.getGetter());
		}
		return accessorFactory.buildGetter(fieldDescriptor.getField());
	}

	private <P> Setter<BEAN, P> getSetManipulator(final FieldDescriptor<BEAN, P> fieldDescriptor) {
		if ( fieldDescriptor.getSetter() != null) {
			return accessorFactory.buildSetter(fieldDescriptor.getSetter());
		}
		return accessorFactory.buildSetter(fieldDescriptor.getField());
	}

}
