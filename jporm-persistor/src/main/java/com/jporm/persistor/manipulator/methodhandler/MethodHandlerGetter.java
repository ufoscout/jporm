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
package com.jporm.persistor.manipulator.methodhandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.exception.OrmException;
import com.jporm.persistor.manipulator.Getter;

/**
 *
 * Set the value of a using the related setter method
 *
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public class MethodHandlerGetter<BEAN, P> extends Getter<BEAN, P> {

	private final MethodHandle methodHandle;

	public MethodHandlerGetter(final Field field) {
		try {
			field.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflectGetter(field);
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	public MethodHandlerGetter(final Method getterMethod) {
		try {
			getterMethod.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflect(getterMethod);
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	@Override
	public P getValue(final BEAN bean) {
		try {
			return (P) methodHandle.invoke(bean);
		} catch (Throwable e) {
			throw new OrmException(e);
		}
	}

}
