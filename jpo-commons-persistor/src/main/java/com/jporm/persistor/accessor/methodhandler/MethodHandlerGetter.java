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

import com.jporm.persistor.accessor.Getter;

/**
 *
 * Set the value of a using the related setter method
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class MethodHandlerGetter<BEAN, P> implements Getter<BEAN, P> {

	private MethodHandle methodHandle;

	public MethodHandlerGetter(final Field field) {
		try {
			field.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflectGetter(field);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public MethodHandlerGetter(final Method getterMethod) {
		try {
			getterMethod.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflect(getterMethod);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public P getValue(final BEAN bean) {
		try {
			if (bean!=null) {
				return (P) methodHandle.invoke(bean);
			}
			else {
				return (P) methodHandle.invoke();
			}
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
