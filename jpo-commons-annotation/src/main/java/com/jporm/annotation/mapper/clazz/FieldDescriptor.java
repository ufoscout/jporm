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
 * @author Francesco Cina
 *
 *         04/giu/2011
 */
public interface FieldDescriptor<BEAN, P> {

	ColumnInfo getColumnInfo();

	Optional<Field> getField();

	String getFieldName();

	GeneratorInfo getGeneratorInfo();

	Optional<Method> getGetter();

	Optional<Method> getSetter();

	/**
	 * This is the type of the field.
	 * E.g.
	 * String name -> RawType is {@link String}
	 * Optional<String> name -> RawType is {@link Optional}
	 * @return
	 */
	Class<P> getRawType();

	/**
	 * This is the optional generic type of the field.
	 * E.g.
	 * String name -> GenericArgumentType is null
	 * Optional<String> name -> GenericArgumentType is {@link String}
	 * @return
	 */
	<G> Optional<Class<G>> getGenericArgumentType();

	VersionInfo getVersionInfo();

	boolean isIdentifier();

}
