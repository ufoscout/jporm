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
import java.util.Optional;

import com.jporm.annotation.introspector.column.ColumnInfo;
import com.jporm.annotation.introspector.generator.GeneratorInfo;
import com.jporm.annotation.introspector.version.VersionInfo;

/**
 *
 * @author cinafr
 *
 * @param
 *            <P>
 */
public class FieldDescriptorImpl<BEAN, R, P> implements FieldDescriptor<BEAN, R, P> {

	private VersionInfo versionInfo;
	private GeneratorInfo generatorInfo;
	private ColumnInfo columnInfo;
	private final String fieldName;
	private final Class<P> processedClass;
	private final Class<R> realClass;
	private boolean identifier = false;
	private boolean ignored = false;
	private Optional<Method> getter = Optional.empty();
	private Optional<Method> setter = Optional.empty();
	private final Field field;
	private final ValueProcessor<R, P> valueProcessor;

	public FieldDescriptorImpl(Field field, String fieldName, Class<R> realClass, Class<P> processedClass, ValueProcessor<R, P> valueProcessor) {
		this.field = field;
		this.fieldName = fieldName;
		this.realClass = realClass;
		this.processedClass = processedClass;
		this.valueProcessor = valueProcessor;
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
	public Optional<Method> getGetter() {
		return getter;
	}

	/**
	 * @param getter the getter to set
	 */
	public void setGetter(Optional<Method> getter) {
		this.getter = getter;
	}

	/**
	 * @return the setter
	 */
	@Override
	public Optional<Method> getSetter() {
		return setter;
	}

	/**
	 * @param setter the setter to set
	 */
	public void setSetter(Optional<Method> setter) {
		this.setter = setter;
	}

	/**
	 * @return the field
	 */
	@Override
	public Field getField() {
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
	 * @return the valueProcessor
	 */
	@Override
	public ValueProcessor<R, P> getValueProcessor() {
		return valueProcessor;
	}

	/**
	 * @return the processedClass
	 */
	@Override
	public Class<P> getProcessedClass() {
		return processedClass;
	}

	/**
	 * @return the realClass
	 */
	@Override
	public Class<R> getRealClass() {
		return realClass;
	}

}
