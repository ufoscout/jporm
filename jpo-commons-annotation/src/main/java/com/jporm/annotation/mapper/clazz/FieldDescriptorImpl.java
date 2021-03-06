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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.annotation.introspector.column.ColumnInfo;
import com.jporm.annotation.introspector.generator.GeneratorInfo;
import com.jporm.annotation.introspector.type.JsonInfo;
import com.jporm.annotation.introspector.version.VersionInfo;

/**
 *
 * @author cinafr
 *
 * @param
 *            <P>
 */
public class FieldDescriptorImpl<BEAN, P> implements FieldDescriptor<BEAN, P> {

	private VersionInfo versionInfo;
	private GeneratorInfo generatorInfo;
	private ColumnInfo columnInfo;
	private JsonInfo jsonInfo;
	private final String fieldName;
	private final Class<P> processedClass;
	private boolean identifier = false;
	private boolean ignored = false;
	private final PropertyWrapper<Field, ?, P> field;
	private final PropertyWrapper<Method, ?, P> getter;
	private final PropertyWrapper<Method, ?, P> setter;

	public FieldDescriptorImpl(String fieldName, Class<P> processedClass,
			PropertyWrapper<Field, ?, P> field,
			PropertyWrapper<Method, ?, P> getter,
			PropertyWrapper<Method, ?, P> setter) {
		this.field = field;
		this.fieldName = fieldName;
		this.processedClass = processedClass;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public final ColumnInfo getColumnInfo() {
		return this.columnInfo;
	}

	@Override
	public final String getFieldName() {
		return this.fieldName;
	}

	@Override
	public GeneratorInfo getGeneratorInfo() {
		return this.generatorInfo;
	}

	@Override
	public VersionInfo getVersionInfo() {
		return this.versionInfo;
	}

	@Override
	public final boolean isIdentifier() {
		return this.identifier;
	}

	public final void setColumnInfo(final ColumnInfo columnInfo) {
		this.columnInfo = columnInfo;
	}

	public void setGeneratorInfo(final GeneratorInfo generatorInfo) {
		this.generatorInfo = generatorInfo;
	}

	public final void setIdentifier(final boolean identifier) {
		this.identifier = identifier;
	}

	public void setVersionInfo(final VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	/**
	 * @return the getter
	 */
	@Override
	public  PropertyWrapper<Method, ?, P> getGetter() {
		return getter;
	}

	/**
	 * @return the setter
	 */
	@Override
	public  PropertyWrapper<Method, ?, P>  getSetter() {
		return setter;
	}

	/**
	 * @return the field
	 */
	@Override
	public PropertyWrapper<Field, ?, P>  getField() {
		return field;
	}

	/**
	 * @return the ignored
	 */
	@Override
	public boolean isIgnored() {
		return ignored;
	}

	/**
	 * @param ignored the ignored to set
	 */
	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	/**
	 * @return the processedClass
	 */
	@Override
	public Class<P> getProcessedClass() {
		return processedClass;
	}

	/**
	 * @return the jsonInfo
	 */
	@Override
	public JsonInfo getJsonInfo() {
		return jsonInfo;
	}

	/**
	 * @param jsonInfo the jsonInfo to set
	 */
	public void setJsonInfo(JsonInfo jsonInfo) {
		this.jsonInfo = jsonInfo;
	}

}
