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
import com.jporm.annotation.introspector.version.VersionInfo;

/**
 *
 * @author Francesco Cina
 *
 *         04/giu/2011
 */
public interface FieldDescriptor<BEAN, P> {

	boolean isIgnored();

	ColumnInfo getColumnInfo();

	String getFieldName();

	GeneratorInfo getGeneratorInfo();

	PropertyWrapper<Field, ?, P> getField();

	PropertyWrapper<Method, ?, P> getGetter();

	PropertyWrapper<Method, ?, P> getSetter();

	/**
	 * This is the resulting {@link Class} of the object when the {@link ValueProcessor} is applied.
	 * E.g.
	 * String name -> GenericArgumentType is null
	 * Optional<String> name -> GenericArgumentType is {@link String}
	 * @return
	 */
	Class<P> getProcessedClass();

	VersionInfo getVersionInfo();

	boolean isIdentifier();

}
