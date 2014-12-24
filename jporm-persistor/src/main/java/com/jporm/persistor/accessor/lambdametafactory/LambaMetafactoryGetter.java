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

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

import com.jporm.exception.OrmException;
import com.jporm.persistor.accessor.Getter;

/**
 *
 * Set the value of a using the related setter method
 *
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public class LambaMetafactoryGetter<BEAN, P> extends Getter<BEAN, P> {

	private Function<BEAN, P> function;

	public LambaMetafactoryGetter(final Field field) {
		try {
			field.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			buildFunction(caller, caller.unreflectGetter(field));
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	public LambaMetafactoryGetter(final Method getterMethod) {
		try {
			getterMethod.setAccessible(true);
			MethodHandles.Lookup caller = MethodHandles.lookup();
			buildFunction(caller, caller.unreflect(getterMethod));
		} catch (IllegalAccessException e) {
			throw new OrmException(e);
		}
	}

	private void buildFunction(final MethodHandles.Lookup caller, final MethodHandle methodHandle) {
		try {
			MethodType func=methodHandle.type();
			CallSite site = LambdaMetafactory.metafactory(caller,
					"apply",
					MethodType.methodType(Function.class),
					MethodType.methodType(Object.class, Object.class),
					methodHandle,
					MethodType.methodType(func.returnType(), func.parameterArray())
					);

			MethodHandle factory = site.getTarget();
			function = (Function<BEAN, P>) factory.invoke();

		} catch (Throwable e) {
			throw new OrmException(e);
		}
	}

	@Override
	public P getValue(final BEAN bean) {
		try {
			return function.apply(bean);
		} catch (Throwable e) {
			throw new OrmException(e);
		}
	}

}
