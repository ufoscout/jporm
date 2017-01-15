/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.persistor.accessor.lambda;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

import com.jporm.persistor.accessor.Setter;

public class LambdaWither<BEAN, P> implements Setter<BEAN, P> {

	private BiFunction<BEAN, P, BEAN> function;

	public LambdaWither(final Field field) {
		try {
			field.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			build(caller, caller.unreflectSetter(field));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public LambdaWither(final Method setterMethod) {
		try {
			setterMethod.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			build(caller, caller.unreflect(setterMethod));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void build(final MethodHandles.Lookup caller, final MethodHandle methodHandle) {
		try {
			final MethodType func = methodHandle.type();
			final CallSite site = LambdaMetafactory.metafactory(caller, "apply", MethodType.methodType(BiFunction.class),
					MethodType.methodType(Object.class, Object.class, Object.class), methodHandle, MethodType.methodType(func.returnType(), func.parameterArray()));

			final MethodHandle factory = site.getTarget();
			function = (BiFunction<BEAN, P, BEAN>) factory.invoke();

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BEAN setValue(final BEAN bean, final P value) {
		try {
			return function.apply(bean, value);
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
