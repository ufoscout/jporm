/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.commons.core.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import com.jporm.annotation.introspector.column.ColumnInfo;
import com.jporm.annotation.introspector.generator.GeneratorInfo;
import com.jporm.annotation.introspector.version.VersionInfo;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.commons.core.inject.valuegenerator.ValueGenerator;
import com.jporm.sql.dialect.DBProfile;

public class ExtendedFieldDescriptorImpl<BEAN, P> implements ExtendedFieldDescriptor<BEAN, P> {

	private final FieldDescriptor<BEAN, P> fieldDescriptor;
	private ValueGenerator valueGenerator;
	private final ClassDescriptor<BEAN> descriptor;

	public ExtendedFieldDescriptorImpl(ClassDescriptor<BEAN> descriptor, FieldDescriptor<BEAN, P> fieldDescriptor) {
		this.descriptor = descriptor;
		this.fieldDescriptor = fieldDescriptor;

	}

	@Override
	public ColumnInfo getColumnInfo() {
		return fieldDescriptor.getColumnInfo();
	}

	@Override
	public Optional<Field> getField() {
		return fieldDescriptor.getField();
	}

	@Override
	public String getFieldName() {
		return fieldDescriptor.getFieldName();
	}

	@Override
	public GeneratorInfo getGeneratorInfo() {
		return fieldDescriptor.getGeneratorInfo();
	}

	@Override
	public Optional<Method> getGetter() {
		return fieldDescriptor.getGetter();
	}

	@Override
	public Optional<Method> getSetter() {
		return fieldDescriptor.getSetter();
	}

	@Override
	public Class<P> getRawType() {
		return fieldDescriptor.getRawType();
	}

	@Override
	public VersionInfo getVersionInfo() {
		return fieldDescriptor.getVersionInfo();
	}

	@Override
	public boolean isIdentifier() {
		return fieldDescriptor.isIdentifier();
	}

	@Override
	public ValueGenerator getGenerator(DBProfile dbProfile) {
		if (valueGenerator==null) {
			valueGenerator = ValueGenerator.get(descriptor.getMappedClass(), getGeneratorInfo(), dbProfile);
		}
		return valueGenerator;
	}

	@Override
	public <G> Optional<Class<G>> getGenericArgumentType() {
		return fieldDescriptor.getGenericArgumentType();
	}

}
