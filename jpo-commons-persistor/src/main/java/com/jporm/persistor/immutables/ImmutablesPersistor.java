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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.PersistorImpl;
import com.jporm.persistor.PropertyPersistor;
import com.jporm.persistor.generator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.io.ResultEntry;

/**
 * A persistor implementation based on reflection
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class ImmutablesPersistor<BEAN> extends PersistorImpl<BEAN> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ImmutablesPersistor(final ClassDescriptor<BEAN> classMap, final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors,
			final VersionManipulator<BEAN> versionManipulator, final GeneratorManipulator<BEAN> generatorManipulator)
					throws SecurityException, IllegalArgumentException {
		super(classMap, propertyPersistors, versionManipulator, generatorManipulator);
	}

	@Override
	public BEAN beanFromResultSet(final ResultEntry rs, final List<String> fieldsToIgnore) {
		//		final String[] allColumnNames = this.classMap.getAllColumnJavaNames();
		//		try {
		//			logger.trace("Build bean [{}] from ResultSet. Ignoring fields: [{}]", classMap.getMappedClass(), fieldsToIgnore); //$NON-NLS-1$
		//			BEAN entity = newInstance();
		//			for (final String columnJavaName : allColumnNames) {
		//				if (!fieldsToIgnore.contains(columnJavaName)) {
		//					logger.trace("Load from ResultSet value for field [{}]", columnJavaName); //$NON-NLS-1$
		//					final PropertyPersistor<BEAN, ?, ?> persistor = this.propertyPersistors.get(columnJavaName);
		//					entity = persistor.getFromResultSet(entity, rs);
		//				}
		//			}
		//			return entity;
		//		} catch (final Exception e) {
		//			throw new RuntimeException(e);
		//		}
		return null;
	}

	@Override
	public BEAN clone(final BEAN entity) {
		return entity;
	}

}
