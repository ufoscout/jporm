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
import java.util.function.BiConsumer;

import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.accessor.Setter;

/**
 *
 * Get the value of a field using the related getter method
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class LambdaSetter<BEAN, R, P> extends Setter<BEAN, R, P> {

	private BiConsumer<BEAN, R> consumer;

	public LambdaSetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		super(valueProcessor);
		try {
			field.setAccessible(true);
			final MethodHandles.Lookup caller = MethodHandles.lookup();
			build(caller, caller.unreflectSetter(field));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public LambdaSetter(final Method setterMethod, ValueProcessor<R, P> valueProcessor) {
		super(valueProcessor);
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
			final CallSite site = LambdaMetafactory.metafactory(caller, "accept", MethodType.methodType(BiConsumer.class),
					MethodType.methodType(Void.TYPE, Object.class, Object.class), methodHandle, MethodType.methodType(Void.TYPE, func.parameterArray()));

			final MethodHandle factory = site.getTarget();
			consumer = (BiConsumer<BEAN, R>) factory.invoke();

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected BEAN setUnProcessedValue(BEAN bean, R value) {
		try {
			consumer.accept(bean, value);
			return bean;
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
