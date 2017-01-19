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
package com.jporm.types.wrapper;

import java.util.Optional;

import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterWrapper;
import com.jporm.types.TypeConverterWrapperBuilder;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Nov 21, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class OptionalTypeConverterWrapperBuilder<DB, P> implements TypeConverterWrapperBuilder<Optional<P>, DB, P> {

	private final TypeConverterFactory typeConverterFactory;

	public OptionalTypeConverterWrapperBuilder(final TypeConverterFactory typeConverterFactory) {
		this.typeConverterFactory = typeConverterFactory;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Optional<P>> wrapperType() {
		return (Class) Optional.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TypeConverterWrapper<Optional<P>, DB, P> build(Class<Optional<P>> wrapperClass, Optional<Class<P>> wrappedClass) {
		if(wrappedClass.isPresent()) {
			return build(wrappedClass.get());
		}
		return build((Class) Object.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TypeConverterWrapper<Optional<P>, DB, P> build(Optional<P> instance) {
		if(instance.isPresent()) {
			return build((Class<P>) instance.get().getClass());
		}
		return build((Class) Object.class);
	}

	@SuppressWarnings("rawtypes")
	private TypeConverterWrapper<Optional<P>, DB, P> build(Class<P> wrappedClass) {
		return new OptionalTypeConverterWrapper(typeConverterFactory.getTypeConverterFromClass(wrappedClass));
	}

}
