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
package com.jporm.annotation.mapper.clazz;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jporm.annotation.Version;
import com.jporm.annotation.exception.JpoWrongAnnotationException;
import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.annotation.introspector.table.TableInfo;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class ClassDescriptorImpl<BEAN> implements ClassDescriptor<BEAN> {

	private final TableInfo tableInfo;
	private final Class<BEAN> mappedClass;
	private final Map<String, FieldDescriptorImpl<BEAN, ?>> fieldClassMapByJavaName = new HashMap<>();
	private String[] allColumnJavaNames = new String[0];
	private String[] allNotGeneratedColumnJavaNames = new String[0];
	private String[] primaryKeyColumnJavaNames = new String[0];
	private String[] primaryKeyAndVersionColumnJavaNames = new String[0];
	private String[] notPrimaryKeyColumnJavaNames = new String[0];
	private String[] allGeneratedColumnJavaNames = new String[0];
	private String[] allGeneratedColumnDBNames = new String[0];
	private boolean versionGenerator = false;

	public ClassDescriptorImpl(final Class<BEAN> mappedClass, final TableInfo tableInfo) {
		this.mappedClass = mappedClass;
		this.tableInfo = tableInfo;
	}

	public <P> void addClassField(final FieldDescriptorImpl<BEAN, P> classField) {
		this.fieldClassMapByJavaName.put(classField.getFieldName(), classField);

		if (classField.getVersionInfo().isVersionable()) {
			if (this.versionGenerator) {
				throw new JpoWrongAnnotationException("A bean can have maximum one field annotated with @" + Version.class.getSimpleName() //$NON-NLS-1$
						+ ". Error in class:[" + this.getMappedClass() + "] field: [" + classField.getFieldName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			this.versionGenerator = true;
		}

	}

	@Override
	public String[] getAllColumnJavaNames() {
		return this.allColumnJavaNames;
	}

	@Override
	public String[] getAllGeneratedColumnDBNames() {
		return this.allGeneratedColumnDBNames;
	}

	@Override
	public String[] getAllGeneratedColumnJavaNames() {
		return this.allGeneratedColumnJavaNames;
	}

	@Override
	public String[] getAllNotGeneratedColumnJavaNames() {
		return this.allNotGeneratedColumnJavaNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P> FieldDescriptorImpl<BEAN, P> getFieldDescriptorByJavaName(final String javaName) {
		if (this.fieldClassMapByJavaName.containsKey(javaName)) {
			return (FieldDescriptorImpl<BEAN, P>) this.fieldClassMapByJavaName.get(javaName);
		}
		throw new JpoWrongPropertyNameException("Property with name [" + javaName + "] not found in class " + this.mappedClass); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Class<BEAN> getMappedClass() {
		return this.mappedClass;
	}

	@Override
	public String[] getNotPrimaryKeyColumnJavaNames() {
		return this.notPrimaryKeyColumnJavaNames;
	}

	@Override
	public String[] getPrimaryKeyAndVersionColumnJavaNames() {
		return this.primaryKeyAndVersionColumnJavaNames;
	}

	@Override
	public String[] getPrimaryKeyColumnJavaNames() {
		return this.primaryKeyColumnJavaNames;
	}

	@Override
	public TableInfo getTableInfo() {
		return this.tableInfo;
	}

	public Map<String, FieldDescriptorImpl<BEAN, ?>> getUnmodifiableFieldClassMap() {
		return Collections.unmodifiableMap(this.fieldClassMapByJavaName);
	}

	public void setAllColumnJavaNames(final String[] allColumnJavaNames) {
		this.allColumnJavaNames = allColumnJavaNames;
	}

	public void setAllGeneratedColumnDBNames(final String[] allGeneratedColumnDBNames) {
		this.allGeneratedColumnDBNames = allGeneratedColumnDBNames;
	}

	public void setAllGeneratedColumnJavaNames(final String[] allGeneratedColumnJavaNames) {
		this.allGeneratedColumnJavaNames = allGeneratedColumnJavaNames;
	}

	public void setAllNotGeneratedColumnJavaNames(final String[] allNotGeneratedColumnJavaNames) {
		this.allNotGeneratedColumnJavaNames = allNotGeneratedColumnJavaNames;
	}

	public void setNotPrimaryKeyColumnJavaNames(final String[] notPrimaryKeyColumnJavaNames) {
		this.notPrimaryKeyColumnJavaNames = notPrimaryKeyColumnJavaNames;
	}

	public void setPrimaryKeyAndVersionColumnJavaNames(final String[] primaryKeyAndVersionColumnJavaNames) {
		this.primaryKeyAndVersionColumnJavaNames = primaryKeyAndVersionColumnJavaNames;
	}

	public void setPrimaryKeyColumnJavaNames(final String[] primaryKeyColumnJavaNames) {
		this.primaryKeyColumnJavaNames = primaryKeyColumnJavaNames;
	}

}
