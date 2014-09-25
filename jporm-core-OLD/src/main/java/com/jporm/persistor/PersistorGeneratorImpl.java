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
import com.jporm.mapper.ServiceCatalog;
import com.jporm.mapper.clazz.ClassField;
import com.jporm.mapper.clazz.ClassFieldImpl;
import com.jporm.mapper.clazz.ClassMap;
import com.jporm.persistor.generator.GeneratorManipulator;
import com.jporm.persistor.generator.GeneratorManipulatorImpl;
import com.jporm.persistor.generator.NullGeneratorManipulator;
import com.jporm.persistor.type.TypeFactory;
import com.jporm.persistor.type.TypeWrapperJdbcReady;
import com.jporm.persistor.version.NullVersionManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.persistor.version.VersionManipulatorImpl;
import com.jporm.persistor.version.VersionMath;
import com.jporm.persistor.version.VersionMathFactory;

/**
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorGeneratorImpl<BEAN> implements PersistorGenerator<BEAN> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ClassMap<BEAN> classMap;
	private final TypeFactory typeFactory;
	private final ServiceCatalog serviceCatalog;

	public PersistorGeneratorImpl(final ServiceCatalog serviceCatalog, final ClassMap<BEAN> classMap,
			final TypeFactory typeFactory) {
		this.serviceCatalog = serviceCatalog;
		this.classMap = classMap;
		this.typeFactory = typeFactory;
	}

	@Override
	public OrmPersistor<BEAN> generate() throws Exception {
		Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap = this.buildPropertyPersistorMap();
		return new OrmPersistorImpl<BEAN>(this.classMap, propertyPersistorMap,
				this.buildVersionManipulator(propertyPersistorMap), buildGeneratorManipulator(propertyPersistorMap));
	}

	private <P, DB> Map<String, PropertyPersistor<BEAN, ?, ?>> buildPropertyPersistorMap() throws SecurityException,
			IllegalArgumentException {
		Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors = new HashMap<String, PropertyPersistor<BEAN, ?, ?>>();
		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final ClassField<BEAN, P> classField = this.classMap.getClassFieldByJavaName(columnJavaName);
			if (classField.getRelationVersusClass() != null) {
				logger.debug(
						"Build PropertyPersistor for field [{}] that is a relation versus [{}]", classField.getFieldName(), classField.getRelationVersusClass()); //$NON-NLS-1$
				Class<P> versusClass = classField.getRelationVersusClass();
				ClassMap<P> versusClassMap = serviceCatalog.getOrmClassTool(versusClass).getClassMap();
				if (versusClassMap.getPrimaryKeyColumnJavaNames().length > 1) {
				    throw new OrmConfigurationException("Cannot map relation versus class " + versusClass + ". No unique id defined or more than an id available.");
				}
				ClassFieldImpl<P, Object> versusPkField = versusClassMap.getClassFieldByJavaName(versusClassMap
						.getPrimaryKeyColumnJavaNames()[0]);
				logger.debug("Versus versusPkField type [{}]", versusPkField.getType());
				PropertyPersistor<P, Object, DB> decoratedPropertyPersistor = getPropertyPersistor(versusPkField);
				propertyPersistors.put(columnJavaName, new PropertyPeristorDecorator<P, BEAN, Object, DB>(
						decoratedPropertyPersistor, classField));
			} else {
				propertyPersistors.put(columnJavaName, getPropertyPersistor(classField));
			}
		}
		return propertyPersistors;
	}

	private VersionManipulator<BEAN> buildVersionManipulator(
			final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistorMap) {
		VersionManipulator<BEAN> versionManipulator = new NullVersionManipulator<BEAN>();

		for (final String columnJavaName : this.classMap.getAllColumnJavaNames()) {
			final ClassField<BEAN, ?> classField = this.classMap.getClassFieldByJavaName(columnJavaName);
			if (classField.getVersionInfo().isVersionable()) {
				versionManipulator = new VersionManipulatorImpl<BEAN>(propertyPersistorMap.get(classField.getFieldName()),
						classField.getVersionInfo().getLockMode());
				break;
			}
		}

		return versionManipulator;
	}

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

	private <THIS_BEAN, P, DB> PropertyPersistor<THIS_BEAN, P, DB> getPropertyPersistor(
			final ClassField<THIS_BEAN, P> classField) {
		logger.debug("Build PropertyPersistor for field [{}]", classField.getFieldName()); //$NON-NLS-1$
		VersionMath<P> versionMath = new VersionMathFactory().getMath(classField.getType(), classField.getVersionInfo()
				.isVersionable());
		TypeWrapperJdbcReady<P, DB> typeWrapper = this.typeFactory.getTypeWrapper(classField.getType());
		return new PropertyPersistorImpl<THIS_BEAN, P, DB>(typeWrapper, classField, versionMath);

	}
}
