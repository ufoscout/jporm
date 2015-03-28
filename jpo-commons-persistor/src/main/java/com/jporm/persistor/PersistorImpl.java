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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.LockMode;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.GeneratorManipulator;
import com.jporm.persistor.version.VersionManipulator;
import com.jporm.types.io.ResultEntry;

/**
 * A persistor implementation based on reflection
 *
 * @author Francesco Cina' Mar 24, 2012
 */
public class PersistorImpl<BEAN> implements Persistor<BEAN> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors;
	private final GeneratorManipulator<BEAN> generatorManipulator;
	private final ClassDescriptor<BEAN> classMap;
	private final VersionManipulator<BEAN> versionManipulator;

	public PersistorImpl(final ClassDescriptor<BEAN> classMap,
			final Map<String, PropertyPersistor<BEAN, ?, ?>> propertyPersistors,
			final VersionManipulator<BEAN> versionManipulator, final GeneratorManipulator<BEAN> generatorManipulator)
					throws SecurityException, IllegalArgumentException {
		this.classMap = classMap;
		this.propertyPersistors = propertyPersistors;
		this.versionManipulator = versionManipulator;
		this.generatorManipulator = generatorManipulator;
	}

	@Override
	public BeanFromResultSet<BEAN> beanFromResultSet(final ResultEntry rs, final List<String> fieldsToIgnore) {
		final String[] allColumnNames = this.classMap.getAllColumnJavaNames();
		try {
			logger.debug(
					"Build bean [{}] from ResultSet. Ignoring fields: [{}]", classMap.getMappedClass(), fieldsToIgnore); //$NON-NLS-1$
			final BEAN entity = this.classMap.getMappedClass().newInstance();
			BeanFromResultSet<BEAN> beanFromResultSet = new BeanFromResultSet<BEAN>(entity);
			for (final String columnJavaName : allColumnNames) {
				if (!fieldsToIgnore.contains(columnJavaName)) {
					logger.trace("Load from ResultSet value for field [{}]", columnJavaName); //$NON-NLS-1$
					PropertyPersistor<BEAN, ?, ?> persistor = this.propertyPersistors.get(columnJavaName);
					persistor.getFromResultSet(entity, rs);
				}
			}
			return beanFromResultSet;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BEAN newInstance() {
		try {
			return this.classMap.getMappedClass().newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BEAN clone(final BEAN entity) {
		try {
			final BEAN entityCopy = this.classMap.getMappedClass().newInstance();
			for (final Entry<String, PropertyPersistor<BEAN, ?, ?>> persistorEntry : this.propertyPersistors.entrySet()) {
				persistorEntry.getValue().clonePropertyValue(entity, entityCopy);
			}
			return entityCopy;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void increaseVersion(final BEAN entity, final boolean firstVersionNumber) {
		try {
			this.versionManipulator.updateVersion(entity, firstVersionNumber);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isVersionableWithLock() {
		return this.versionManipulator.isVersionable() && !LockMode.NO_LOCK.equals(this.versionManipulator.getLockMode());
	}

	@Override
	public LockMode getVersionableLockMode() {
		return this.versionManipulator.getLockMode();
	}

	@Override
	public void updateGeneratedValues(final ResultEntry rs, final BEAN entity) {
		final String[] allColumnNames = this.classMap.getAllGeneratedColumnJavaNames();
		try {
			int i = 1;
			for (final String columnJavaName : allColumnNames) {
				this.propertyPersistors.get(columnJavaName).getFromResultSet(entity, rs, i);
				i++;
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] getPropertyValues(final String[] javaColumnNames, final BEAN entity) {
		final Object[] result = new Object[javaColumnNames.length];
		try {
			for (int i = 0; i < javaColumnNames.length; i++) {
				final String javaColumnName = javaColumnNames[i];
				logger.trace("Extract value for property [{}]", javaColumnName); //$NON-NLS-1$
				result[i] = this.propertyPersistors.get(javaColumnName).getPropertyValueFromBean(entity);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public boolean useGenerators(final BEAN entity) {
		try {
			return this.generatorManipulator.useGenerator(entity);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasGenerator() {
		try {
			return this.generatorManipulator.hasGenerator();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
