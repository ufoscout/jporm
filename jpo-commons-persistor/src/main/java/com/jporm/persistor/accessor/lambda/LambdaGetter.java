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
import java.util.function.Function;

import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.accessor.Getter;

/**
 *
 * Set the value of a using the related setter method
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class LambdaGetter<BEAN, R, P> extends Getter<BEAN, R, P> {

	private Function<BEAN, R> function;

	public LambdaGetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		super(valueProcessor);
		try {
			field.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			buildFunction(caller, caller.unreflectGetter(field));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public LambdaGetter(final Method getterMethod, ValueProcessor<R, P> valueProcessor) {
		super(valueProcessor);
		try {
			getterMethod.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			buildFunction(caller, caller.unreflect(getterMethod));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildFunction(final MethodHandles.Lookup caller, final MethodHandle methodHandle) {
		try {
			final MethodType func = methodHandle.type();
			final CallSite site = LambdaMetafactory.metafactory(caller, "apply", MethodType.methodType(Function.class),
					MethodType.methodType(Object.class, Object.class), methodHandle, MethodType.methodType(func.returnType(), func.parameterArray()));

			final MethodHandle factory = site.getTarget();
			function = (Function<BEAN, R>) factory.invoke();

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected R getUnProcessedValue(BEAN bean) {
		try {
			return function.apply(bean);
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
