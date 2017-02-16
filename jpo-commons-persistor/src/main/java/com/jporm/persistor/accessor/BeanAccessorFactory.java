/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.persistor.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.accessor.lambda.LambdaAccessorFactory;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class BeanAccessorFactory {

	private final static Logger logger = LoggerFactory.getLogger(BeanAccessorFactory.class);

	private final static AccessorFactory lambaAccessorFactory = new LambdaAccessorFactory();
	private final static AccessorFactory mhAccessorFactory = new MethodHandlerAccessorFactory();

	private BeanAccessorFactory() {

	}

	public static <BEAN, R, P> Getter<BEAN, R, P> buildGetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		Getter<BEAN, R, P> getter = null;
		try {
			getter = lambaAccessorFactory.buildGetter(field, valueProcessor);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(),
					field.getDeclaringClass().getName());
			getter = mhAccessorFactory.buildGetter(field, valueProcessor);
		}
		return getter;
	}

	public static <BEAN, R, P> Getter<BEAN, R, P> buildGetter(final Method method, ValueProcessor<R, P> valueProcessor) {
		Getter<BEAN, R, P> getter = null;
		try {
			getter = lambaAccessorFactory.buildGetter(method, valueProcessor);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			getter = mhAccessorFactory.buildGetter(method, valueProcessor);
		}
		return getter;
	}

	public static <BEAN, R, P> Setter<BEAN, R, P> buildSetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		Setter<BEAN, R, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(field, valueProcessor);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(),
					field.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(field, valueProcessor);
		}
		return setter;
	}

	public static <BEAN, R, P> Setter<BEAN, R, P> buildSetterOrWither(final Method method, ValueProcessor<R, P> valueProcessor) {
		Setter<BEAN, R, P> setter;
		if ( method.getDeclaringClass().isAssignableFrom( method.getReturnType() ) ) {
			setter = buildWither(method, valueProcessor);
		} else {
			setter = buildSetter(method, valueProcessor);
		}
		return setter;
	}

	private static <BEAN, R, P> Setter<BEAN, R, P> buildSetter(final Method method, ValueProcessor<R, P> valueProcessor) {
		Setter<BEAN, R, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(method, valueProcessor);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for method [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(method, valueProcessor);
		}
		return setter;
	}

	private static <BEAN, R, P> Setter<BEAN, R, P> buildWither(final Method method, ValueProcessor<R, P> valueProcessor) {
		Setter<BEAN, R, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildWither(method, valueProcessor);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for method [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildWither(method, valueProcessor);
		}
		return setter;
	}

}
