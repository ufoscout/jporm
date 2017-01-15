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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * Get the value of a field using the related getter method and return the BEAN instance returned by the getter itself
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class MethodHandlerWither<BEAN, P> extends MethodHandlerSetter<BEAN, P> {

	public MethodHandlerWither(final Field field) {
		super(field);
	}

	public MethodHandlerWither(final Method setterMethod) {
		super(setterMethod);
	}

	@Override
	public BEAN setValue(final BEAN bean, final P value) {
		try {
			return (BEAN) methodHandle.invoke(bean, value);
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
