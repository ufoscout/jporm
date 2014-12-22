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
package com.jporm.persistor.accessor.lambdametafactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.exception.OrmException;
import com.jporm.persistor.accessor.Setter;


/**
 *
 * Get the value of a field using the related getter method
 *
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public class LambaMetafactorySetter<BEAN, P> extends Setter<BEAN, P> {

	private final MethodHandle methodHandle;

	public LambaMetafactorySetter(final Field field) {
		try {
			field.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflectSetter(field);
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	public LambaMetafactorySetter(final Method setterMethod) {
		try {
			setterMethod.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			methodHandle = caller.unreflect(setterMethod);
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	@Override
	public void setValue(final BEAN bean, final P value) {
		try {
			methodHandle.invoke(bean, value);
		} catch (Throwable e) {
			throw new OrmException(e);
		}
	}

}
