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
package com.jporm.introspector.mapper.clazz;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jporm.annotation.Version;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.exception.OrmException;
import com.jporm.introspector.annotation.cache.CacheInfo;
import com.jporm.introspector.annotation.table.TableInfo;

/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ClassDescriptorImpl<BEAN> implements ClassDescriptor<BEAN> {

	private final CacheInfo cacheInfo;
	private final TableInfo tableInfo;
	private final Class<BEAN> mappedClass;
	private final Map<String, FieldDescriptorImpl<BEAN, ?>> fieldClassMapByJavaName = new HashMap<String, FieldDescriptorImpl<BEAN,?>>();
	private String[] allColumnJavaNames = new String[0];
	private String[] allNotGeneratedColumnJavaNames = new String[0];
	private String[] primaryKeyColumnJavaNames = new String[0];
	private String[] primaryKeyAndVersionColumnJavaNames = new String[0];
	private String[] notPrimaryKeyColumnJavaNames = new String[0];
	private String[] allGeneratedColumnJavaNames = new String[0];
	private String[] allGeneratedColumnDBNames = new String[0];
	private boolean versionGenerator = false;

	public ClassDescriptorImpl(final Class<BEAN> mappedClass, final TableInfo tableInfo, final CacheInfo cacheInfo) {
		this.mappedClass = mappedClass;
		this.tableInfo = tableInfo;
		this.cacheInfo = cacheInfo;
	}

	@Override
	public Class<BEAN> getMappedClass() {
		return this.mappedClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P> FieldDescriptorImpl<BEAN, P> getClassFieldByJavaName(final String javaName) {
		if (this.fieldClassMapByJavaName.containsKey(javaName)) {
			return (FieldDescriptorImpl<BEAN, P>) this.fieldClassMapByJavaName.get(javaName);
		}
		throw new OrmException("Property with name [" + javaName + "] not found in class " + this.mappedClass); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public <P> void addClassField(final FieldDescriptorImpl<BEAN, P> classField) {
		this.fieldClassMapByJavaName.put(classField.getFieldName(), classField);

		if (classField.getVersionInfo().isVersionable()) {
			if (this.versionGenerator) {
				throw new OrmConfigurationException("A bean can have maximum one field annotated with @" + Version.class.getSimpleName() + ". Error in class:[" + this.getMappedClass() + "] field: [" + classField.getFieldName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			this.versionGenerator = true;
		}

	}

	public Map<String, FieldDescriptorImpl<BEAN, ?>> getUnmodifiableFieldClassMap() {
		return Collections.unmodifiableMap( this.fieldClassMapByJavaName );
	}

	@Override
	public String[] getAllColumnJavaNames() {
		return this.allColumnJavaNames;
	}

	@Override
	public String[] getPrimaryKeyColumnJavaNames() {
		return this.primaryKeyColumnJavaNames;
	}

	@Override
	public String[] getNotPrimaryKeyColumnJavaNames() {
		return this.notPrimaryKeyColumnJavaNames;
	}

	public void setAllColumnJavaNames(final String[] allColumnJavaNames) {
		this.allColumnJavaNames = allColumnJavaNames;
	}

	public void setPrimaryKeyColumnJavaNames(final String[] primaryKeyColumnJavaNames) {
		this.primaryKeyColumnJavaNames = primaryKeyColumnJavaNames;
	}

	public void setNotPrimaryKeyColumnJavaNames(final String[] notPrimaryKeyColumnJavaNames) {
		this.notPrimaryKeyColumnJavaNames = notPrimaryKeyColumnJavaNames;
	}

	public void setAllNotGeneratedColumnJavaNames(final String[] allNotGeneratedColumnJavaNames) {
		this.allNotGeneratedColumnJavaNames = allNotGeneratedColumnJavaNames;
	}

	@Override
	public String[] getAllNotGeneratedColumnJavaNames() {
		return this.allNotGeneratedColumnJavaNames;
	}

	public void setAllGeneratedColumnJavaNames(final String[] allGeneratedColumnJavaNames) {
		this.allGeneratedColumnJavaNames = allGeneratedColumnJavaNames;
	}

	@Override
	public String[] getAllGeneratedColumnJavaNames() {
		return this.allGeneratedColumnJavaNames;
	}

	public void setPrimaryKeyAndVersionColumnJavaNames(
			final String[] primaryKeyAndVersionColumnJavaNames) {
		this.primaryKeyAndVersionColumnJavaNames = primaryKeyAndVersionColumnJavaNames;
	}

	@Override
	public String[] getPrimaryKeyAndVersionColumnJavaNames() {
		return this.primaryKeyAndVersionColumnJavaNames;
	}

	@Override
	public TableInfo getTableInfo() {
		return this.tableInfo;
	}

	@Override
	public String[] getAllGeneratedColumnDBNames() {
		return this.allGeneratedColumnDBNames;
	}

	public void setAllGeneratedColumnDBNames(
			final String[] allGeneratedColumnDBNames) {
		this.allGeneratedColumnDBNames = allGeneratedColumnDBNames;
	}

	/**
	 * @return the cacheInfo
	 */
	 @Override
	 public CacheInfo getCacheInfo() {
		return cacheInfo;
	}

}
