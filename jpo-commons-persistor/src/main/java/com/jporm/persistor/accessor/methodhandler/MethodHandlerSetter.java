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
package com.jporm.persistor.accessor.methodhandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.persistor.accessor.Setter;

/**
 *
 * Get the value of a field using the related getter method
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class MethodHandlerSetter<BEAN, P> implements Setter<BEAN, P> {

	private final MethodHandle methodHandle;

	public MethodHandlerSetter(final Field field) {
		try {
			field.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflectSetter(field);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public MethodHandlerSetter(final Method setterMethod) {
		try {
			setterMethod.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflect(setterMethod);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BEAN setValue(final BEAN bean, final P value) {
		try {
			methodHandle.invoke(bean, value);
			return bean;
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
