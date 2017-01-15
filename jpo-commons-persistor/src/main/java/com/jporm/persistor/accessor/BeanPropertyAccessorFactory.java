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

import com.jporm.persistor.accessor.lambda.LambdaAccessorFactory;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class BeanPropertyAccessorFactory {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final AccessorFactory lambaAccessorFactory = new LambdaAccessorFactory();
	private final AccessorFactory mhAccessorFactory = new MethodHandlerAccessorFactory();

	public <BEAN, P> Getter<BEAN, P> buildGetter(final Field field) {
		Getter<BEAN, P> getter = null;
		try {
			getter = lambaAccessorFactory.buildGetter(field);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(),
					field.getDeclaringClass().getName());
			getter = mhAccessorFactory.buildGetter(field);
		}
		return getter;
	}

	public <BEAN, P> Getter<BEAN, P> buildGetter(final Method method) {
		Getter<BEAN, P> getter = null;
		try {
			getter = lambaAccessorFactory.buildGetter(method);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			getter = mhAccessorFactory.buildGetter(method);
		}
		return getter;
	}

	public <BEAN, P> Setter<BEAN, P> buildSetter(final Field field) {
		Setter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(field);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(),
					field.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(field);
		}
		return setter;
	}

	public <BEAN, P> Setter<BEAN, P> buildSetterOrWither(final Method method) {
		Setter<BEAN, P> setter;
		if ( method.getDeclaringClass().isAssignableFrom( method.getReturnType() ) ) {
			setter = buildWither(method);
		} else {
			setter = buildSetter(method);
		}
		return setter;
	}

	private <BEAN, P> Setter<BEAN, P> buildSetter(final Method method) {
		Setter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(method);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for method [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(method);
		}
		return setter;
	}

	private <BEAN, P> Setter<BEAN, P> buildWither(final Method method) {
		Setter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildWither(method);
		} catch (final RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for method [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(),
					method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildWither(method);
		}
		return setter;
	}

}
